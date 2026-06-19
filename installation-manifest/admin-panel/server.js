const express  = require('express');
const { MongoClient } = require('mongodb');
const { Pool }   = require('pg');
const path       = require('path');
const axios      = require('axios');
const { sha3_512 } = require('js-sha3');
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
              { projection: { 'declarante.curp': 1, 'declaracion.tipoDeclaracion': 1, 'declaracion.numeroDeclaracion': 1, 'declaracion.idUsuario': 1, _id: 0 } }
            )
            .toArray();

          const suffix = colName.replace('recepcionWeb', '');
          const collNameVal = suffix ? parseInt(suffix) : 0;

          docs.forEach(d => {
            if (d.declarante && d.declarante.curp) {
              const curp = d.declarante.curp.toUpperCase().trim();
              const tipo = (d.declaracion && d.declaracion.tipoDeclaracion) || 'DESCONOCIDO';
              const numeroDeclaracion = (d.declaracion && d.declaracion.numeroDeclaracion) || '';
              const idUsuario = (d.declaracion && d.declaracion.idUsuario) || 0;
              // Si ya existe, no sobreescribir (quedarse con la primera encontrada)
              if (!declaMap.has(curp)) {
                declaMap.set(curp, {
                  tipoDeclaracion: tipo,
                  numeroDeclaracion,
                  idUsuario,
                  collName: collNameVal
                });
              }
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
      .map(u => {
        const declaData = declaMap.get((u.curp || '').toUpperCase().trim());
        return {
          ...u,
          tipoDeclaracion: declaData ? declaData.tipoDeclaracion : 'DESCONOCIDO',
          numeroDeclaracion: declaData ? declaData.numeroDeclaracion : '',
          idUsuarioDeclarante: u.idUsuario,
          collName: declaData ? declaData.collName : 0
        };
      });
    res.json({ anio, totalUsuarios: todos.length, conDeclaracion: declararon.length, lista: declararon });
  } catch (err) {
    console.error('Error en /api/declararon:', err);
    res.status(500).json({ error: err.message });
  }
});

// ─── API: Dar de baja (Desactivar) usuario ───────────────────────────────────
app.post('/api/desactivar-usuario', async (req, res) => {
  const { idUsuario } = req.body;
  if (!idUsuario) {
    return res.status(400).json({ error: 'idUsuario es requerido' });
  }
  try {
    const query = 'UPDATE ip_usuario SET activo = 0 WHERE id_usuario = $1';
    await pgPool.query(query, [idUsuario]);
    res.json({ ok: true, message: 'Usuario dado de baja (desactivado) correctamente.' });
  } catch (err) {
    console.error('Error al dar de baja al usuario:', err);
    res.status(500).json({ error: err.message });
  }
});

// ─── Consulta usuarios inactivos de PostgreSQL ──────────────────────────────────
async function getUsuariosInactivos() {
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
    WHERE activo = 0
    ORDER BY primer_apellido, segundo_apellido, nombre
  `);
  return result.rows;
}

// ─── API: Listar usuarios inactivos ──────────────────────────────────────────
app.get('/api/inactivos', async (req, res) => {
  try {
    const lista = await getUsuariosInactivos();
    res.json({ totalInactivos: lista.length, lista });
  } catch (err) {
    console.error('Error en /api/inactivos:', err);
    res.status(500).json({ error: err.message });
  }
});

// ─── API: Reactivar (Activar) usuario ────────────────────────────────────────
app.post('/api/activar-usuario', async (req, res) => {
  const { idUsuario } = req.body;
  if (!idUsuario) {
    return res.status(400).json({ error: 'idUsuario es requerido' });
  }
  try {
    const query = 'UPDATE ip_usuario SET activo = 1 WHERE id_usuario = $1';
    await pgPool.query(query, [idUsuario]);
    res.json({ ok: true, message: 'Usuario reactivado correctamente.' });
  } catch (err) {
    console.error('Error al activar al usuario:', err);
    res.status(500).json({ error: err.message });
  }
});

// ─── API: Restablecer Contraseña ─────────────────────────────────────────────
app.post('/api/restablecer-password', async (req, res) => {
  const { idUsuario, login, curp, newPassword } = req.body;
  if (!idUsuario || !login || !curp || !newPassword) {
    return res.status(400).json({ error: 'Parámetros incompletos.' });
  }
  try {
    const sSalt = login.split('').reverse().join('');
    const sToDigest = sSalt + "&&" + newPassword.toUpperCase();
    
    // Hash en hexadecimal (en Java d1 y d2 usa Integer.toHexString que es en minúsculas)
    const hash = crypto.createHash('sha1').update(sToDigest).digest('hex').toLowerCase();

    await pgPool.query('BEGIN');
    const updateQuery = 'UPDATE ip_usuario SET pwd_enc = $1 WHERE id_usuario = $2';
    await pgPool.query(updateQuery, [hash, idUsuario]);

    // Insertar log de la acción (7 = REESTABLECER PASSWORD)
    const fecha = new Date().toISOString().replace('T', ' ').substring(0, 19);
    const insertLog = 'INSERT INTO ip_bitacora (id_aplicacion, fecha, curp, accion) VALUES ($1, $2, $3, $4)';
    await pgPool.query(insertLog, [0, fecha, curp, 7]);

    await pgPool.query('COMMIT');
    res.json({ ok: true, message: 'Contraseña restablecida correctamente.' });
  } catch (err) {
    await pgPool.query('ROLLBACK');
    console.error('Error restableciendo contraseña:', err);
    res.status(500).json({ error: err.message });
  }
});




// ─── API: Descargar Declaración y Acuse ───────────────────────────────────────
const SERVICIO_IMPRESION_URL = process.env.SERVICIO_IMPRESION_URL || 'http://servicioimpresion:5000/api';

app.post('/api/descargar-declaracion', async (req, res) => {
  try {
    const { declaracion } = req.body;
    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    
    const payload = {
      declaracion,
      digitoVerificador
    };

    const response = await axios.post(`${SERVICIO_IMPRESION_URL}/consulta-declaracion`, payload, { timeout: 15000 });
    res.json(response.data);
  } catch (error) {
    console.error('Error descargando declaración:', error.message);
    res.status(500).json({ error: 'Error al generar el PDF de la declaración.' });
  }
});

app.post('/api/descargar-acuse', async (req, res) => {
  try {
    const { declaracion } = req.body;
    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    
    const payload = {
      declaracion,
      digitoVerificador
    };

    const response = await axios.post(`${SERVICIO_IMPRESION_URL}/consulta-acuse/`, payload, { timeout: 15000 });
    res.json(response.data);
  } catch (error) {
    console.error('Error descargando acuse:', error.message);
    res.status(500).json({ error: 'Error al generar el PDF del acuse.' });
  }
});

// ─── Health check ─────────────────────────────────────────────────────────────
app.get('/health', (_, res) => res.json({ status: 'ok' }));

// ─── Start ────────────────────────────────────────────────────────────────────
const PORT = process.env.SERVER_PORT || 5010;
app.listen(PORT, () => console.log(`Panel Admin Declaranet escuchando en puerto ${PORT}`));
