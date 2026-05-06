const moongose = require("../db")

const schema = new moongose.Schema(
    {
        _id: String,

        idEnte: String,
        anio: Number,
        idPeriodo: String,
        fechaLimite : Date,
        folio: String,
        tipoDeclaracion: String,
        tipoIncumplimiento: String,
        fechaTomaPosesionPuesto: String,
        curp: String,
        nombreCompleto: String,
        nombres: String,
        segundoApellido: String,
        primerApellido: String,
        unidadAdministrativa: String,
        claveUa : String,
        idPuesto: String,
        empleoCargoComision: String,
        fechaRegistro : Date
            
    }
);

module.exports = schema;

//module.exports = moongose.model('rusp', schema);