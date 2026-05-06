const moongose = require("../db")

const schema = new moongose.Schema(
    {
        _id: {
            idEnte: String,
            tipoObligacion : String,
            anio : Number
        },
        EXTEMPORANEO: String,
        CUMPLIO: String,
        PENDIENTE: String,
        anio: Number,
        grupo : Number,
        nombreGrupo : String,
        mes: String,
        idEnte : String,
        tipoObligacion : String
    }
);

module.exports = schema;