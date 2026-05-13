const express  = require('express');
const { MongoClient } = require('mongodb');
const { Pool }   = require('pg');
const path       = require('path');
const crypto     = require('crypto');

const app = express();
app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));

// ─── Configuración desde variables de entorno ────────────────────────────────
const ADMIN_PASSWORD = process.env.ADMIN_PANEL_PASSWORD || 'admin2026';
const MONGO_HOST     = process.env.MONGO_HOST  || '172.17.0.1';
const MONGO_PORT     = process.env.MONGO_PORT  || '27017';
const MONGO_DB       = process.env.MONGO_DB    || 'declaranet';
const MONGO_USER     = process.env.MONGO_USER  || 'declausr';
const MONGO_PASS     = process.env.MONGO_PASS  || 'usrpass123';
const PG_HOST        = process.env.PG_HOST     || '172.17.0.1';
const PG_PORT        = parseInt(process.env.PG_PORT) || 5434;
const PG_DB          = process.env.PG_DB       || 'identidad';
const PG_USER        = process.env.PG_USER     || 'identidadsfpusr';
const PG_PASS        = process.env.PG_PASS     || 'userpasss123';

// ─── PostgreSQL pool ──────────────────────────────────────────────────────────
const pgPool = new Pool({
  host: PG_HOST, port: PG_PORT,
  database: PG_DB, user: PG_USER, password: PG_PASS,
  max: 3, idleTimeoutMillis: 30000
});

// ─── Hash de contraseña (mismo algoritmo que el sistema: SHA1 con salt=login invertido) ──
function hashPassword(login, password) {
  const salt     = login.split('').reverse().join('');
  const toDigest = salt + '&&' + password;
  return crypto.createHash('sha1').update(toDigest).digest('hex');
}

// ─── Generar contraseña temporal aleatoria ────────────────────────────────────
function generarPasswordTemporal() {
  const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789@#$';
  let pwd = '';
  for (let i = 0; i < 10; i++) {
    pwd += chars[Math.floor(Math.random() * chars.length)];
  }
  return pwd;
}

// ─── Auth simple ──────────────────────────────────────────────────────────────
app.post('/api/login', (req, res) => {
  const { password } = req.body;
  if (password === ADMIN_PASSWORD) {
    res.json({ ok: true });
  } else {
    res.status(401).json({ ok: false, error: 'Contraseña incorrecta' });
  }
});

// ─── Obtener declaraciones del año (CURP → tipoDeclaracion) desde MongoDB ────
async function getDeclaracionesPorAnio(anio) {
  const mongoUri = `mongodb://${MONGO_USER}:${encodeURIComponent(MONGO_PASS)}@${MONGO_HOST}:${MONGO_PORT}/${MONGO_DB}?authSource=${MONGO_DB}`;
  const client   = new MongoClient(mongoUri, { serverSelectionTimeoutMS: 8000 });
  // Map: CURP → tipoDeclaracion
  const declaMap = new Map();

  try {
    await client.connect();
    const db = client.db(MONGO_DB);

    const allCollections = await db.listCollections().toArray();
    const recepcionCols  = allCollections.map(c => c.name).filter(n => n.startsWith('recepcionWeb'));
    console.log(`Colecciones recepcionWeb: ${recepcionCols.join(', ')}`);

    for (const colName of recepcionCols) {
      try {
        for (const filtroAnio of [anio, String(anio)]) {
          const docs = await db.collection(colName)
            .find(
              { 'declaracion.anio': filtroAnio },
              { projection: { 'declarante.curp': 1, 'declaracion.tipoDeclaracion': 1, _id: 0 } }
            )
            .toArray();

          docs.forEach(d => {
            if (d.declarante && d.declarante.curp) {
              const curp = d.declarante.curp.toUpperCase().trim();
              const tipo = (d.declaracion && d.declaracion.tipoDeclaracion) || 'DESCONOCIDO';
              // Si ya existe, no sobreescribir (quedarse con la primera encontrada)
              if (!declaMap.has(curp)) declaMap.set(curp, tipo);
            }
          });
        }
        console.log(`${colName}: ${declaMap.size} declaraciones acumuladas en ${anio}`);
      } catch (err) {
        console.warn(`Error en colección ${colName}:`, err.message);
      }
    }
  } catch (err) {
    console.error('Error conectando a MongoDB:', err.message);
  } finally {
    await client.close();
  }
  return declaMap;
}

// ─── Consulta usuarios activos de PostgreSQL ──────────────────────────────────
async function getUsuariosActivos() {
  const result = await pgPool.query(`
    SELECT
      id_usuario  AS "idUsuario",
      login,
      nombre,
      primer_apellido  AS "primerApellido",
      segundo_apellido AS "segundoApellido",
      curp,
      email,
      email_alt        AS "emailAlt"
    FROM ip_usuario
    WHERE activo = 1
    ORDER BY primer_apellido, segundo_apellido, nombre
  `);
  return result.rows;
}

// ─── API: usuarios pendientes ─────────────────────────────────────────────────
app.get('/api/pendientes', async (req, res) => {
  const anio = parseInt(req.query.anio) || new Date().getFullYear();
  try {
    const [todos, declaMap] = await Promise.all([getUsuariosActivos(), getDeclaracionesPorAnio(anio)]);
    const pendientes = todos.filter(u => !declaMap.has((u.curp || '').toUpperCase().trim()));
    res.json({ anio, totalUsuarios: todos.length, conDeclaracion: declaMap.size, pendientes: pendientes.length, lista: pendientes });
  } catch (err) {
    console.error('Error en /api/pendientes:', err);
    res.status(500).json({ error: err.message });
  }
});

// ─── API: usuarios que SÍ declararon (con tipo de declaración) ───────────────
app.get('/api/declararon', async (req, res) => {
  const anio = parseInt(req.query.anio) || new Date().getFullYear();
  try {
    const [todos, declaMap] = await Promise.all([getUsuariosActivos(), getDeclaracionesPorAnio(anio)]);
    const declararon = todos
      .filter(u => declaMap.has((u.curp || '').toUpperCase().trim()))
      .map(u => ({
        ...u,
        tipoDeclaracion: declaMap.get((u.curp || '').toUpperCase().trim()) || 'DESCONOCIDO'
      }));
    res.json({ anio, totalUsuarios: todos.length, conDeclaracion: declararon.length, lista: declararon });
  } catch (err) {
    console.error('Error en /api/declararon:', err);
    res.status(500).json({ error: err.message });
  }
});

// ─── API: restablecer contraseña ──────────────────────────────────────────────
app.post('/api/reset-password', async (req, res) => {
  const { curp } = req.body;
  if (!curp) return res.status(400).json({ error: 'CURP requerida' });

  try {
    // Obtener login del usuario
    const result = await pgPool.query(
      `SELECT id_usuario AS "idUsuario", login FROM ip_usuario WHERE curp = $1 AND activo = 1`,
      [curp.toUpperCase().trim()]
    );

    if (result.rows.length === 0) {
      return res.status(404).json({ error: 'Usuario no encontrado' });
    }

    const { idUsuario, login } = result.rows[0];
    const nuevaPassword = generarPasswordTemporal();
    const pwdEnc        = hashPassword(login, nuevaPassword);

    // Actualizar contraseña en PostgreSQL
    await pgPool.query(
      `UPDATE ip_usuario SET pwd_enc = $1 WHERE id_usuario = $2`,
      [pwdEnc, idUsuario]
    );

    console.log(`Contraseña restablecida para CURP: ${curp}, Login: ${login}`);
    res.json({ ok: true, nuevaPassword, login });
  } catch (err) {
    console.error('Error en /api/reset-password:', err);
    res.status(500).json({ error: err.message });
  }
});

// ─── Health check ─────────────────────────────────────────────────────────────
app.get('/health', (_, res) => res.json({ status: 'ok' }));

// ─── Start ────────────────────────────────────────────────────────────────────
const PORT = process.env.SERVER_PORT || 5010;
app.listen(PORT, () => console.log(`Panel Admin Declaranet escuchando en puerto ${PORT}`));
