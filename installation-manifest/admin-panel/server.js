const express = require('express');
const { MongoClient } = require('mongodb');
const { Pool } = require('pg');
const path = require('path');

const app = express();
app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));

// ─── Configuración desde variables de entorno ────────────────────────────────
const ADMIN_PASSWORD   = process.env.ADMIN_PANEL_PASSWORD || 'admin2026';
const MONGO_HOST       = process.env.MONGO_HOST            || '172.17.0.1';
const MONGO_PORT       = process.env.MONGO_PORT            || '27017';
const MONGO_DB         = process.env.MONGO_DB              || 'declaranet';
const MONGO_USER       = process.env.MONGO_USER            || 'declausr';
const MONGO_PASS       = process.env.MONGO_PASS            || 'usrpass123';
const PG_HOST          = process.env.PG_HOST               || '172.17.0.1';
const PG_PORT          = parseInt(process.env.PG_PORT)     || 5434;
const PG_DB            = process.env.PG_DB                 || 'identidad';
const PG_USER          = process.env.PG_USER               || 'identidadsfpusr';
const PG_PASS          = process.env.PG_PASS               || 'userpasss123';
// Número de colecciones MongoDB (el sistema usa recepcionWeb1, recepcionWeb2, etc.)
const TOTAL_COLECCIONES = parseInt(process.env.TOTAL_COLECCIONES) || 10;

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

// ─── Obtener usuarios con declaración en el año dado desde MongoDB ──────────
async function getCurpsConDeclaracion(anio) {
  const mongoUri = `mongodb://${MONGO_USER}:${encodeURIComponent(MONGO_PASS)}@${MONGO_HOST}:${MONGO_PORT}/${MONGO_DB}?authSource=${MONGO_DB}`;
  const client = new MongoClient(mongoUri, { serverSelectionTimeoutMS: 8000 });
  const curpsConDecla = new Set();

  try {
    await client.connect();
    const db = client.db(MONGO_DB);

    // Obtener dinámicamente todas las colecciones recepcionWeb*
    const allCollections = await db.listCollections().toArray();
    const recepcionCols = allCollections
      .map(c => c.name)
      .filter(name => name.startsWith('recepcionWeb'));

    console.log(`Colecciones recepcionWeb encontradas: ${recepcionCols.join(', ')}`);

    for (const colName of recepcionCols) {
      try {
        const docs = await db.collection(colName)
          .find(
            { 'declaracion.anio': anio },
            { projection: { 'declarante.curp': 1, _id: 0 } }
          )
          .toArray();

        // También intentar con el año como string por si acaso
        const docsStr = await db.collection(colName)
          .find(
            { 'declaracion.anio': String(anio) },
            { projection: { 'declarante.curp': 1, _id: 0 } }
          )
          .toArray();

        [...docs, ...docsStr].forEach(d => {
          if (d.declarante && d.declarante.curp) {
            curpsConDecla.add(d.declarante.curp.toUpperCase().trim());
          }
        });

        console.log(`${colName}: ${docs.length + docsStr.length} declaraciones en ${anio}`);
      } catch (err) {
        console.warn(`Error en colección ${colName}:`, err.message);
      }
    }
  } catch (err) {
    console.error('Error conectando a MongoDB:', err.message);
  } finally {
    await client.close();
  }
  return curpsConDecla;
}

// ─── API: usuarios pendientes ─────────────────────────────────────────────────
app.get('/api/pendientes', async (req, res) => {
  const anio = parseInt(req.query.anio) || new Date().getFullYear();

  try {
    // 1. Obtener todos los usuarios activos de PostgreSQL
    const pgResult = await pgPool.query(`
      SELECT 
        nombre,
        primer_apellido   AS "primerApellido",
        segundo_apellido  AS "segundoApellido",
        curp,
        email,
        email_alt         AS "emailAlt"
      FROM ip_usuario
      WHERE activo = 1
      ORDER BY primer_apellido, segundo_apellido, nombre
    `);

    const todosUsuarios = pgResult.rows;

    // 2. Obtener CURPs que SÍ declararon en el año dado
    const curpsConDecla = await getCurpsConDeclaracion(anio);

    // 3. Filtrar los que NO declararon
    const pendientes = todosUsuarios.filter(u => {
      const curp = (u.curp || '').toUpperCase().trim();
      return !curpsConDecla.has(curp);
    });

    res.json({
      anio,
      totalUsuarios: todosUsuarios.length,
      conDeclaracion: curpsConDecla.size,
      pendientes: pendientes.length,
      lista: pendientes
    });
  } catch (err) {
    console.error('Error en /api/pendientes:', err);
    res.status(500).json({ error: err.message });
  }
});

// ─── API: usuarios que SÍ declararon ─────────────────────────────────────────
app.get('/api/declararon', async (req, res) => {
  const anio = parseInt(req.query.anio) || new Date().getFullYear();

  try {
    const pgResult = await pgPool.query(`
      SELECT 
        nombre,
        primer_apellido   AS "primerApellido",
        segundo_apellido  AS "segundoApellido",
        curp,
        email,
        email_alt         AS "emailAlt"
      FROM ip_usuario
      WHERE activo = 1
      ORDER BY primer_apellido, segundo_apellido, nombre
    `);

    const todosUsuarios = pgResult.rows;
    const curpsConDecla = await getCurpsConDeclaracion(anio);

    // Filtrar los que SÍ declararon
    const declararon = todosUsuarios.filter(u => {
      const curp = (u.curp || '').toUpperCase().trim();
      return curpsConDecla.has(curp);
    });

    res.json({
      anio,
      totalUsuarios: todosUsuarios.length,
      conDeclaracion: declararon.length,
      lista: declararon
    });
  } catch (err) {
    console.error('Error en /api/declararon:', err);
    res.status(500).json({ error: err.message });
  }
});

// ─── Health check ─────────────────────────────────────────────────────────────
app.get('/health', (_, res) => res.json({ status: 'ok' }));

// ─── Start ────────────────────────────────────────────────────────────────────
const PORT = process.env.SERVER_PORT || 5010;
app.listen(PORT, () => {
  console.log(`Panel Admin Declaranet escuchando en puerto ${PORT}`);
});
