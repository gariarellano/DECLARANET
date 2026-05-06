const moongose = require("../db")

const schema = new moongose.Schema(
    {
        _id : String,
        idPuesto: String,
        idEnte: String,
        activo: Number,
        tipoObligacion: String,
        folio : String,
        anio: Number,
        fechaTomaPosesionPuesto: String,
        curp: String,
        nombreCompleto : String,
        nombres: String,
        segundoApellido: String,
        claveUa : String,
        primerApellido: String,
        unidadAdministrativa: String,
        empleoCargoComision: String,
        idPeriodo : String,
        folioVista : String,
        enVista : Boolean
        }
);

module.exports = schema;

//module.exports = moongose.model('rusp', schema);