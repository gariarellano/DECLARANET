/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.dao.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.dao.DAODatosPublicosInterface;
import mx.gob.sfp.dgti.dao.DAOGeneric;

/**
 * Clase que realizara los guardados de datos publicos 
 * de los servidores públicos para poder tener listados limpios.
 * 
 * @author cgarias
 * @since 05/08/2020
 */
public class DAODatosPublicos extends DAOGeneric implements DAODatosPublicosInterface{
    private static final Logger logger = Logger.getLogger(DAODatosPublicos.class.getName());
    private static final String COLLECTION_DATOS_PUBLICOS = "datosPublicos";
    private final String FIELD_NOMBRE = "nombre";
    private final String FIEL_DEPENDENCIA = "dependencia";
    private final String FIELD_ID_DEPENDENCIA = "idDependencia";
    private final String FIELD_ID_USR_DECNET = "idUsrDecnet";
    private final String FIELD_CURP = "curp";
    private final String FIELD_RFC = "rfc";
    private final String _ID = "_id";
    
    public DAODatosPublicos(Vertx vertx){
        super(vertx);
    }

    @Override
    public DAODatosPublicosInterface registraDatosPublicos(JsonObject datosPublicos, Integer collName, Handler<AsyncResult<String>> procTerminado){
        JsonObject queryPrincipal = new JsonObject();
        queryPrincipal.put(FIELD_NOMBRE, datosPublicos.getString(FIELD_NOMBRE));
        queryPrincipal.put(FIEL_DEPENDENCIA, datosPublicos.getString(FIEL_DEPENDENCIA));
        queryPrincipal.put(FIELD_ID_DEPENDENCIA, datosPublicos.getString(FIELD_ID_DEPENDENCIA));
        queryPrincipal.put(FIELD_ID_USR_DECNET, datosPublicos.getInteger(FIELD_ID_USR_DECNET));
        
        mongoClient.find(COLLECTION_DATOS_PUBLICOS.concat(collName.toString()), queryPrincipal, encontrado->{
            if (encontrado.succeeded()){
                if (datosPublicos.containsKey(FIELD_CURP) && datosPublicos.getString(FIELD_CURP)!= null){
                    queryPrincipal.put(FIELD_CURP, datosPublicos.getString(FIELD_CURP));
                }
                if (datosPublicos.containsKey(FIELD_RFC) && datosPublicos.getString(FIELD_RFC)!= null){
                    queryPrincipal.put(FIELD_RFC, datosPublicos.getString(FIELD_RFC));
                }
                
                if (encontrado.result().isEmpty()){    
                    mongoClient.save(COLLECTION_DATOS_PUBLICOS.concat(collName.toString()), queryPrincipal, insertado->{
                        procTerminado.handle(insertado);
                    });
                }else{
                    if (queryPrincipal.size() > encontrado.result().get(0).getMap().size()-1 ||
                            (queryPrincipal.getString(FIELD_CURP) != null && !queryPrincipal.getString(FIELD_CURP).equals(encontrado.result().get(0).getString(FIELD_CURP))) ||
                            (queryPrincipal.getString(FIELD_RFC) != null && !queryPrincipal.getString(FIELD_RFC).equals(encontrado.result().get(0).getString(FIELD_RFC)))
                            ){                        
                        queryPrincipal.put(_ID, ((JsonObject)encontrado.result().get(0)).getString(_ID));
                        mongoClient.save(COLLECTION_DATOS_PUBLICOS.concat(collName.toString()), queryPrincipal, actualizado->{
                            procTerminado.handle(actualizado);
                        });
                    }else{
                        procTerminado.handle(Future.succeededFuture());
                    }
                }
            }else{
                logger.severe(String.format("Ocurrio un error %s", encontrado.cause()));
                procTerminado.handle(Future.failedFuture("Ocurrio un error al validar"));
            }
        });
        return this;
    }
}
