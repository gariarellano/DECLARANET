const fetch = require('node-fetch');
const config = require("./config/config")
const moongose = require("./db");
const SchemaServidorIncumplimiento = require("./models/servidor_rusp_schema");
const SchemaServidorCumplimiento = require("./models/servidor_cumplimiento_schema");
const SchemaPreconteosInicio = require("./models/preconteosinicio");
const RelacionConteos = require("./models/relacionconteos");

let entesReceptores = [];
let mapaNolocalizadosr = new Map();
let mapaCumplimiento = new Map();
let mapaPreconteosInicio = new Map();
let mapaGrupos = new Map();

class EnteGrupo {
  constructor(id, nombre, grupo) {
    this.id = id;
    this.nombre = nombre;
    this.grupo = grupo;
  }
}

inicioEntesMerge = async () => {
    console.log("=== (1) Se obtienen los entes receptores" + config.entesReceptoresTodos)
    console.log("=== Obtener relaciones");
    const relacionConteos = await RelacionConteos.find({});
    
    relacionConteos.forEach( r => {
      mapaGrupos[r._id] = [];
      r.grupos.forEach(g=> {
        g.entes.forEach(e=> {
          mapaGrupos[r._id].push(new EnteGrupo(e, g.nombre, g.id));
        });
      });
    });

    try {
      await fetch(config.entesReceptoresTodos)
        .then(res => res.json())
        .then(entes => {
          entes.forEach(ente => {
            entesReceptores.push(ente.collName);
            mapaNolocalizadosr[ente.collName] = moongose.model("nolocalizadosr" + ente.collName, SchemaServidorIncumplimiento);
            mapaCumplimiento[ente.collName] = moongose.model("cumplimiento" + ente.collName, SchemaServidorCumplimiento);
            mapaPreconteosInicio[ente.collName] = moongose.model("preconteosinicio" + ente.collName, SchemaPreconteosInicio);
          });
          console.log("=== collNames disponibles desde entesReceptores: " + entesReceptores);

        })    
    } catch(err) {
      console.log("=== Ocurrio un error al consultar entes " + err);
    }
  }
  
  inicioEntesMerge();

module.exports = {
    entesReceptores : entesReceptores,
    mapaCumplimiento : mapaCumplimiento,
    mapaNolocalizadosr : mapaNolocalizadosr,
    mapaPreconteosInicio : mapaPreconteosInicio,
    mapaGrupos : mapaGrupos
};