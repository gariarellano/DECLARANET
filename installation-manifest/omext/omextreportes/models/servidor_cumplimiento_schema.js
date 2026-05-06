const moongose = require("../db");
const ruspSchema = require("./servidor_rusp_schema");

const schema = new moongose.Schema(
    {
        datosRusp : new moongose.Schema(
            {
                idPuesto: String,
                idEnte: String,
                activo: Number,
                tipoObligacion: String,
                anio: Number,
                fechaTomaPosesionPuesto: String,
                curp: String,
                nombres: String,
                segundoApellido: String,
                primerApellido: String,
                unidadAdministrativa: String,
                empleoCargoComision: String
                }
        )
    }
);

module.exports = schema;

//module.exports = moongose.model('rusp', schema);