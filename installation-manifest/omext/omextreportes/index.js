const express = require("express");
const config = require("./config/config")

const reportesRoutes = require("./routes/reportes");
const vistasRoutes = require("./routes/vistas");
const cron = require("./controllers/cron");
var db = require("./db");

const app = express();
const port = (!config.serverPort)?config.serverPort:5000;

app.use(express.json());
app.use(express.urlencoded({extended : true}));

app.use(reportesRoutes);
app.use(vistasRoutes);
app.use(cron);

app.get("/", (req, res) => {
    res.json({"Hola": "Servicio Reportes Omext"})
})

app.get((req, res, next) => {
  res.status(400).send("<h1>Página no encontrada</h1>");
});

app.listen(port, () => {
  console.log(`=== Se levanta servicio sobre puerto:${port}`)
})