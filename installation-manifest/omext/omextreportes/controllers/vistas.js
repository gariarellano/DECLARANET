const SchemaVista = require("../models/vista");
const moongose = require("../db");
const config = require("../config/config");
const fetch = require('node-fetch');
const entesReceptores = require("../entesReceptores");
let collNamesDisponibles = entesReceptores.entesReceptores;
let mapaNolocalizadosr = entesReceptores.mapaNolocalizadosr;
let mapaCumplimiento = entesReceptores.mapaCumplimiento;
let SchemaServidorVista = require("..//models/servidoresvista");

exports.generarServidoresVistas = async (req, res) => {
    console.log("=== Se hace una llamada, se obtiene del body: " + JSON.stringify(req.body));
    console.log("Estamos en las vistas, el coleccion es: " + req.body.collName);
    console.log("Estamos en las vistas, el IDvISTA es: " + req.body.folios);

    try {
        const collName = req.body.collName;
        const folios = req.body.folios;
        await folios.forEach(function(folio) {
            generarServidoresVistasPorFolio(collName, folio);
        });
        console.log("=== Parte final.")
        res.status(200).send({
                    "status": "Exito"
                }
            );
    } catch(err) {
        console.log("=== Error en: " + err)
        res.status(400).send("Error");
    }
}

generarServidoresVistasPorFolio = async (collName, folio) => {
    console.log("=== Se genera vista para folio: " + folio);
    try {
        const nombreColeccion = "vistas" + collName;

        let idPeriodo;
        let Vista = obtenerModeloVista(nombreColeccion);
        //let ServidorVista = obtenerModeloServidorVista(nombreColeccion);

        let ServidorIncumplimiento = mapaNolocalizadosr[collName];
        let ServidorVista = moongose.model("servidoresvista" + collName, SchemaServidorVista);

        // Se consulta informacion de la vista con el id que fue proporcionado
        console.log("=== Se va mandar a guardar.")
        const v = await Vista.findOne({
                _id : folio,
                vistaGenerada : "EN_PROCESO" 
            });

        console.log("=== La vista quedaría: " + v);
        if(v == null) {
            console.log("No existe la vista");
            throw Error("No existe la vista");
        }
        
        idPeriodo = v.idPeriodo;

        // Se establecen las condiciones para la creacion de la vista
        let condicionesNuevaVista = {
            idEnte : v.idEnte,
            anio : v.anioDeclaracion,
            tipoObligacion : obtenerTipoDeclaracionRusp(v.tipoDeclaracion),
            folioVista : {
                $exists :false
            },
            activo :1
            
        }
        if(v.tipoDeclaracion != "MODIFICACION") {
            condicionesNuevaVista.mes = v.mesDeclaracion;
        }
        console.log("=== Las caracteristicas de los servidores en la vista son : " + v.idEnte + " - " + JSON.stringify(condicionesNuevaVista));

        let cuentaServidores = 0;
        // Se hace consulta de los servidores omisos, iterando con el cursor cada uno, se copia a servidoresVista y se setea el de omisos para que no se
        // llame a otra vista
        await ServidorIncumplimiento.find(condicionesNuevaVista)
            .cursor()
            .eachAsync(async function (servincumpl, i) {ß
                    // Se copian los valores
                    const servista = new ServidorVista({
                        //_id : servincumpl._id + moongose.Types.ObjectId(),
                        _id : moongose.Types.ObjectId(),
                        nombres: servincumpl.nombres,
                        primerApellido: servincumpl.primerApellido,
                        segundoApellido: servincumpl.segundoApellido,
                        idEnte: servincumpl.idEnte,
                        curp: servincumpl.curp,
                        idPuesto: servincumpl.idPuesto,
                        anio: servincumpl.anio,
                        idPeriodo: v.idPeriodo,
                        fechaLimite : v.fechaLimite,
                        folio: v.folio,
                        nombreCompleto : servincumpl.nombreCompleto,
                        claveUa : servincumpl.claveUa,
                        tipoDeclaracion: v.tipoDeclaracion,
                        tipoIncumplimiento: "OMISO",
                        fechaTomaPosesionPuesto: servincumpl.fechaTomaPosesionPuesto,
                        unidadAdministrativa: servincumpl.unidadAdministrativa,
                        empleoCargoComision: servincumpl.empleoCargoComision,
                        fechaRegistro : new Date()

                    });
                    await servista.save()
                    await ServidorIncumplimiento.updateOne(
                        {_id : servincumpl.id},
                        {
                        $set : {
                            folioVista : v.folio,
                            idPeriodo : v.idPeriodo,
                            enVista : true}
                        },
                        {upsert : true}
                    ).then(x => cuentaServidores ++);
                ;
            });
        // Cuando se pasaron todos los elementos del cursor
        console.log("=== La cuenta de servidores quedo: " + cuentaServidores);
        console.log("=== Termina todo ? " );
        // Se actualiza la vista indicando que se han generados los elementos
        await Vista.updateOne(
            {_id : folio},
            {$set : {
                vistaGenerada : "GENERADA_SIN_FIRMAS",
                totalServidores : cuentaServidores
            }},
            {upsert : true}
        );
        console.log(" === Se llega al final ")
        
        await fetch(config.omextBackFirmaService, {
            method: 'post',
            body: JSON.stringify( {
                operationName : "terminarProcesoVista",
                query: "mutation terminarProcesoVista($params :ParametrosProcesoVistaInput) {terminarProcesoVista(params :$params)}",
                variables : {
                    params : {
                      collName: collName,
                      folio: folio
                    }
                  }
            }),
            headers: { 'Content-Type': 'application/json' },
            })
            .then(res => res.json())
            .then(json => console.log("Se regresa a firmar: " + json));
        console.log("=== Ahora si se llega al final final.");
        return;
    } catch (err) {
        console.log("=== Hubo un problema " + err)
        return;
    }
} 



obtenerTipoDeclaracionDeclaranet = (tipoDeclaracion) => {
    switch(tipoDeclaracion) {
        case "ACTIVO_MAYO":
            return "MODIFICACION";
        case "ALTA":
            return "INICIO";
        case "BAJA":
            return "CONCLUSION";
        default:
             return null;
    }
}

obtenerTipoDeclaracionRusp = (tipoDeclaracion) => {
    switch(tipoDeclaracion) {
        case "MODIFICACION":
            return "ACTIVO_MAYO";
        case "INICIO":
            return "ALTA";
        case "CONCLUSION":
            return "BAJA";
        default:
             return null;
    }
}

obtenerModeloVista = (coleccion) => { 
    return moongose.model(coleccion, SchemaVista);
}

obtenerModeloServidorVista = (coleccion) => {
    return moongose.model(coleccion, SchemaServidorVista);
}