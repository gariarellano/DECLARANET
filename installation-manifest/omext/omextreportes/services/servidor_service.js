const servidorRuspSchema = require("../models/servidor_rusp_schema");
const servidorCumplimientoSchema = require("../models/servidor_cumplimiento_schema")
const entesReceptores = require("../entesReceptores");

let mapaNolocalizadosr = entesReceptores.mapaNolocalizadosr;
let mapaCumplimiento = entesReceptores.mapaCumplimiento;


const stream = require("stream");
const stringify = require("csv-stringify");
const fs = require("fs");
const fsp = require('fs').promises;
const util = require("util");

const moongose = require("../db");
const { log } = require("console");
const x = require("uniqid");

const pipeline = util.promisify(stream.pipeline)

const bom = "\ufeff";

const encabezado = ['Tipo obligación', 'Año','Id Puesto' , 'CURP', 'Nombres',
'Primer apellido','Segundo apellido','Unidad Administrativa', 'Puesto', '"Fecha toma posesión"', 'Estatus'].join(",") + "\n";


fs.mkdir("./archivos", {recursive:true}, (err) => {
    if (err) throw err;
    console.log("=== Se crea la carpeta <<archivos>>.");
});

exports.crearCsvRusp = async (reportes, id, nombre) => {
    
    fs.mkdirSync("./archivos/" + id, {recursive:true});    
    fs.writeFileSync("./archivos/" + id + "/" + nombre + ".csv", bom +  encabezado, "utf8" );
    
    let dst = "archivos/" + id + "/" + nombre + ".csv";
    for (let i = 0; i < reportes.length; i++) {
        
        const tipoColeccion = obtenerTipoColeccion(reportes[i].cumplimiento);
        
        let Servidor = obtenerModelo(reportes[i].cumplimiento, reportes[i].collName);

        //let Servidor = obtenerModelo(tipoColeccion, reportes[i].coleccion); 

        let servidoresCursor = Servidor
            .aggregate(reportes[i].pipeline)
            .cursor();

        let csvStream = obtenerCsvStream(tipoColeccion);
        let archivoStream = fs.createWriteStream(dst, {flags: 'a', encoding:'utf8'});
        
        await pipeline(servidoresCursor,csvStream, archivoStream);
    }
    console.log("*** Termina iteraciones de cursor. " + id);

}

/*
obtenerModelo = (tipoColeccion, coleccion) => {
    
    if(tipoColeccion == "rusp") {
        return moongose.model(coleccion, servidorCumplimientoSchema);
    } else if(tipoColeccion == "cumplimiento") {
        return moongose.model(coleccion, servidorRuspSchema);
    }
    return null;
 
}
*/

obtenerTipoColeccion = (cumplimiento) => {
    switch(cumplimiento) {
        case "CUMPLIO":
        case "EXTEMPORANEO":
            return "cumplimiento"
        case "PENDIENTE":
            return "rusp"
        default:
            return null;
    }   
}

obtenerModelo = (cumplimiento, collName) => {
    switch(cumplimiento) {
        case "CUMPLIO":
        case "EXTEMPORANEO":
            return mapaCumplimiento[collName];
        case "PENDIENTE":
            return mapaNolocalizadosr[collName];
        default:
            return null;
    }   
}

obtenerCsvStream =(tipoColeccion) => {
    if(tipoColeccion == "rusp") {
        return stringify({
            columns: {
                "tipoDeclaracionDesc": 'Tipo obligación',
                "anio": 'Año',
                "idPuesto": 'Id Puesto',
                "curp": 'CURP',
                "nombres": 'Nombres',
                "primerApellido": 'Primer apellido',
                "segundoApellido": 'Segundo apellido',
                "unidadAdministrativa": 'Unidad Administrativa',
                "empleoCargoComision": 'Puesto',
                "fechaTomaPosesionPuesto": 'Fecha toma posesión',
                "cumplimientoDesc": "Estatus"
            }
        })
    } else if(tipoColeccion == "cumplimiento") {
        return stringify({
            columns: {

                "datosRusp.tipoDeclaracionDesc": 'Tipo obligación',
                "datosRusp.anio": 'Año',
                "datosRusp.idPuesto": 'Id Puesto',
                "datosRusp.curp": 'CURP',
                "datosRusp.nombres": 'Nombres',
                "datosRusp.primerApellido": 'Primer apellido',
                "datosRusp.segundoApellido": 'Segundo apellido',
                "datosRusp.unidadAdministrativa": 'Unidad Administrativa',
                "datosRusp.empleoCargoComision": 'Puesto',
                "datosRusp.fechaTomaPosesionPuesto": 'Fecha toma posesión',
                "datosRusp.cumplimientoDesc": "Estatus"
            }
        })
    } 
}