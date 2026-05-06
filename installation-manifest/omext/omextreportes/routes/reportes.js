const path = require('path');
const express = require('express');

const reporteController = require('../controllers/reportes');
const router = express.Router();

router.post("/crearReporte", reporteController.generarReporte);

router.get("/descargarReporte", reporteController.descargarReporte);

module.exports = router;
