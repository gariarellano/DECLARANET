const moongose = require("../db")

const schema = new moongose.Schema(
    {
            _id: String,
            folio: String,
            tipoDeclaracion: String,
            tipoIncumplimiento: String,
            anio: Number,
            mes: String,
            numOficio: Number,
            anioDeclaracion : Number,
            mesDeclaracion : String,
            remitente :String,
            idEnte : String,
            idPeriodo: String,
            idTextoOficio : String,
            totalOmisos : Number,
            fechaLimite : Date,
            fechaRegistro : Date,
            vistaGenerada : String,
            totalServidores : Number,
            ente : {
                idEnte: String,
                nombreCorto: String,
                nombreEnte: String,
                ramo: Number,
                ur: String
            },
            firmante : {
                cliente_id: String,
                secret_key: String,
                servicio: String
            },
            firmaOficio:{
                datos:{
                    datosParaSha:String,
                    sha:String
                },
                firma:{
                    codigoSha: String,
                    estatus : Number,
                    folio : String,
                    nombreFirm : String,
                    numeroCertificado : String,
                    rfc :String,
                    respuestaValidacion : String,
                    transaccion : Number
                }
        
            },
            firmaListado: {
                datos:{
                    datosParaSha:String,
                    sha:String
                },
                firma:{
                    codigoSha: String,
                    estatus: Number,
                    folio: String,
                    nombreFirm: String,
                    numeroCertificado: String,
                    rfc: String,
                    respuestaValidacion: String,
                    transaccion: Number
                }
            }   
    }
);

module.exports = schema;