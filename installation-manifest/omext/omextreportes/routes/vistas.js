const path = require('path');
const express = require('express');

const vistasController = require("../controllers/vistas");
const router= express.Router();

router.post("/generarVistas", vistasController.generarServidoresVistas);

module.exports=router;