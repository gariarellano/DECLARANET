const moongose = require("../db");

var CronJob = require('cron').CronJob;

const entesReceptores = require("../entesReceptores");
let collNamesDisponibles = entesReceptores.entesReceptores;
let mapaNolocalizadosr = entesReceptores.mapaNolocalizadosr;
let mapaCumplimiento = entesReceptores.mapaCumplimiento;
let mapaPreconteosInicio = entesReceptores.mapaPreconteosInicio;
let mapaGrupos = entesReceptores.mapaGrupos;

var job = new CronJob('0 */15 * * * *', function() {
  ejecutarMergeInicio();
}, null, true, 'America/Los_Angeles');

job.start();

const COLECCION_PRECONTEOS = "preconteosinicio";

ejecutarMergeInicio = async () => { 
  console.log("============================")
  collNamesDisponibles.forEach(collName => {
      mergePreconteos(collName, "ACTIVO_MAYO");
  });
}

module.exports = CronJob;


mergePreconteos = async (collName, tipoObligacion) => {
  try {
    
    await mapaPreconteosInicio[collName].deleteMany({}); 
    await mapaCumplimiento[collName].aggregate(obtenerPipeline(COLECCION_PRECONTEOS, collName, tipoObligacion, "CUMPLIMIENTO")).read('primary').exec();
    await mapaNolocalizadosr[collName].aggregate(obtenerPipeline(COLECCION_PRECONTEOS, collName, tipoObligacion, "PENDIENTE")).read('primary').exec();
    console.log("=== Se ejecuta merge vistas de inicio cada 15min con collName " + collName); 
    if(mapaGrupos[collName] != undefined) {
      mapaGrupos[collName].forEach(async (ente) => {
        await mapaPreconteosInicio[collName].updateMany(
          { idEnte : ente.id},
          {
            $set : {
              grupo: ente.grupo,
              nombreGrupo: ente.nombre
            }
          },
          { upsert : false});
      });
    }
  } catch(err) {
    console.log("=== Error al hacer el merge en collName " + collName + ": " + err.stack);
  }
}

obtenerPipeline = (coleccionDestino, collName, tipo, cumplimiento) => {

    if(cumplimiento == "CUMPLIMIENTO") {
        return [
            {
              "$match": {
                "datosRusp.activo": 1,
                "datosRusp.tipoObligacion": tipo
              }
            },
            {
              "$project": {
                  "datosRusp.idEnte": 1,
                  "datosRusp.tipoObligacion": 1,
                  "datosRusp.anio": 1,
                  "extemp": {
                      "$cond" : [{"$eq": ["$extemporaneo",true] }, 1, 0] 
                  },
                  "tiempo": { 
                      "$cond" : [{ "$eq" : ["$extemporaneo", true] }, 0, 1] 
                  }
                  
              }
          },
          {
              "$group": {
                  "_id": {
                      "idEnte": "$datosRusp.idEnte",
                      "tipoObligacion": "$datosRusp.tipoObligacion",
                      "anio": "$datosRusp.anio"
                  },
                  "EXTEMPORANEO": { "$sum": "$extemp" },
                  "CUMPLIO": { "$sum": "$tiempo" },
              }
          },
          {
              "$addFields": {
                "tipoObligacion":  "$_id.tipoObligacion",
                "anio": "$_id.anio",
                "idEnte": "$_id.idEnte"
              } 
            },
            {
              "$merge": {
                "into": coleccionDestino + collName,
                "on": "_id",
                "whenMatched": "merge",
                "whenNotMatched": "insert"
              }
            }
          ];
    } else if(cumplimiento == "PENDIENTE") {
        return [
            {
              "$match": {
                "activo": 1,
                "tipoObligacion": tipo
              }
            },
            {
              "$group": {
                "_id": {
                  "idEnte": "$idEnte",
                  "tipoObligacion": "$tipoObligacion",
                  "anio": "$anio"
                },
                "PENDIENTE": {
                  "$sum": 1
                }
              }
            },
            {
              "$addFields": {
                "tipoObligacion":  "$_id.tipoObligacion",
                "anio": "$_id.anio",
                "idEnte": "$_id.idEnte"
              }
            },
            {
              "$merge": {
                "into": coleccionDestino + collName,
                "on": "_id",
                "whenMatched": "merge",
                "whenNotMatched": "insert"
              }  
            }
          ];
    }
}

inicioEntesMerge = async () => {
  
  try {
    console.log("=== (2) Se manda crean merge por primera vez")
    ejecutarMergeInicio();
  } catch(err) {
  console.log("=== Ocurrio un error al consultar entes " + err);
    }
}

setTimeout(() => {
  inicioEntesMerge();
}, 10000)


