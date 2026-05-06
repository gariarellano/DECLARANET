/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "GuardaDeclaracion" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.dao.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.Date;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.dao.DAOGeneric;
import mx.gob.sfp.dgti.dao.DAOGuardaDeclaracionInterface;
import static mx.gob.sfp.dgti.util.Constantes.COLLECTION_NAME_DECLARACIONES;
import static mx.gob.sfp.dgti.util.Constantes.COLLECTION_NAME_DECLARACIONES_TEMP;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_MESSAGE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_COLL_NAME;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_DIGESTION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ENCABEZADO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ERROR_MESSAGE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_FECHA_ACTUALIZACION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_FECHA_REGISTRO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ID_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_INSTITUCION_RECEPTORA;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_NUMERO_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.GUARDADO;
import static mx.gob.sfp.dgti.util.Constantes.PROPERTY_QUERY_ID_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes._ID;
import mx.gob.sfp.dgti.util.IExecutJsonUtils;
import mx.gob.sfp.dgti.utils.FechaUtil;

/**
 * Clase que contiene la implementacion de los 
 * métodos para la interaccion con la base
 * y consulta de informacion de entes receptores 
 * de información.
 * 
 * @author cgarias
 * @since 06/11/2019
 */
public class DAOGuardaDeclaracion extends DAOGeneric implements DAOGuardaDeclaracionInterface{
    private static final Logger logger = Logger.getLogger(DAOGuardaDeclaracion.class.getName());
        
    public DAOGuardaDeclaracion(Vertx vertx){
        super(vertx);
    }
    
    /**
     * @see DAOGuardaDeclaracionInterface
     */
    @Override
    public DAOGuardaDeclaracionInterface validaEsNuevaOigual(JsonObject declaracion, Handler<AsyncResult<JsonObject>> resultHandler) {
        String nameColection = COLLECTION_NAME_DECLARACIONES_TEMP+declaracion.getJsonObject(FIELD_INSTITUCION_RECEPTORA).getInteger(FIELD_COLL_NAME);        
        mongoClient.findOne(nameColection, 
                new JsonObject().put(PROPERTY_QUERY_ID_USUARIO, declaracion.getJsonObject(FIELD_ENCABEZADO).getJsonObject(FIELD_USUARIO).getInteger(FIELD_ID_USUARIO)),
                new JsonObject().put(PROPERTY_QUERY_ID_USUARIO, Boolean.TRUE), 
                findaHandler->{
            if ( findaHandler.succeeded()){
                if (findaHandler.result()== null){
                    resultHandler.handle(Future.succeededFuture(null));
                }else{
                    if (findaHandler.result().getString(_ID).equals(declaracion.getJsonObject(FIELD_ENCABEZADO).getString(FIELD_NUMERO_DECLARACION)))
                        resultHandler.handle(Future.succeededFuture(null));
                    else
                        resultHandler.handle(Future.succeededFuture(new JsonObject().put(FIELD_ERROR_MESSAGE, ERROR_MESSAGE)));
                }
            }else{
                resultHandler.handle(Future.failedFuture(findaHandler.cause()));
            }
        });
        return this;
    }
    
    /**
     * @see DAOGuardaDeclaracionInterface
     */
    @Override
    public DAOGuardaDeclaracionInterface guardaDeclaracion(JsonObject declaracion, Handler<AsyncResult<JsonObject>> resultHandler) {
        String nameColection = COLLECTION_NAME_DECLARACIONES_TEMP+declaracion.getJsonObject(FIELD_INSTITUCION_RECEPTORA).getInteger(FIELD_COLL_NAME);        
        if (declaracion.getJsonObject(FIELD_ENCABEZADO).getString(FIELD_NUMERO_DECLARACION) != null){
            declaracion.put(_ID, declaracion.getJsonObject(FIELD_ENCABEZADO).getString(FIELD_NUMERO_DECLARACION));
        }
        declaracion.getJsonObject(FIELD_ENCABEZADO).remove(FIELD_NUMERO_DECLARACION);
        if (declaracion.getJsonObject(FIELD_ENCABEZADO).getString(FIELD_FECHA_REGISTRO)== null){
            declaracion.getJsonObject(FIELD_ENCABEZADO).put(FIELD_FECHA_REGISTRO, FechaUtil.getFechaFormatoISO8601(new Date()));
        }
        declaracion.getJsonObject(FIELD_ENCABEZADO).put(FIELD_FECHA_ACTUALIZACION, FechaUtil.getFechaFormatoISO8601(new Date()));
        IExecutJsonUtils.excutTrimJson(declaracion, declaracion.fieldNames().iterator());
        mongoClient.save(
                nameColection, 
                declaracion, 
                insertExecuted -> {
                    if(insertExecuted.succeeded()){
                        String id = insertExecuted.result();//string mongoID
                        if (id != null){
                            declaracion.getJsonObject(FIELD_ENCABEZADO).put(FIELD_NUMERO_DECLARACION, id);
                            declaracion.remove(_ID);
                        }else{
                            declaracion.getJsonObject(FIELD_ENCABEZADO).put(FIELD_NUMERO_DECLARACION, declaracion.getString(_ID));
                            declaracion.remove(_ID);
                        }
                        resultHandler.handle(Future.succeededFuture(declaracion));
                    }else{
                        logger.warning("NOT succeeded BY ID duplicate");
                        logger.severe(String.format("%s", insertExecuted.cause()));
                        resultHandler.handle(Future.failedFuture(insertExecuted.cause()));
                    }
                });
        return this;
    }
    
    @Override
    public DAOGuardaDeclaracionInterface recibeDeclaracionFirmada(JsonObject declaracionMigrar, Handler<AsyncResult<String>> resultHandler){
        String collection = COLLECTION_NAME_DECLARACIONES + declaracionMigrar.getJsonObject(FIELD_DIGESTION).getInteger(FIELD_COLL_NAME);
        String collectiontemp = COLLECTION_NAME_DECLARACIONES_TEMP + declaracionMigrar.getJsonObject(FIELD_DIGESTION).getInteger(FIELD_COLL_NAME);
        JsonObject declaracion = declaracionMigrar.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).copy();
        declaracion.put(_ID, declaracion.getJsonObject(FIELD_ENCABEZADO).getString(FIELD_NUMERO_DECLARACION));
        declaracion.getJsonObject(FIELD_ENCABEZADO).remove(FIELD_NUMERO_DECLARACION);
        mongoClient.save(collection, declaracion, guardado->{
            if (guardado.succeeded()){
                JsonObject toRemove = new JsonObject().put(_ID, declaracionMigrar.getJsonObject(FIELD_DIGESTION).getString(FIELD_NUMERO_DECLARACION));
                mongoClient.removeDocument(collectiontemp, toRemove, eliminado->{
                    if (eliminado.succeeded()){
                        resultHandler.handle(Future.succeededFuture(GUARDADO));
                    }else{
                        logger.severe(String.format("ERROR EN ELIMINAR LA DECLARACION TEMP  %s", guardado.cause()));
                        resultHandler.handle(Future.failedFuture(eliminado.cause()));
                    }
                });
            }else{
                logger.severe(String.format("ERROR EN GUARDAR EN FINAL ECLARACION : %s", guardado.cause()));
                resultHandler.handle(Future.failedFuture(guardado.cause()));
            }
        });
        return this;
    }

    @Override
    public DAOGuardaDeclaracionInterface obtenDeclaracin(JsonObject declaracionLoc, Handler<AsyncResult<JsonObject>> resultHandler) {
        String collection = COLLECTION_NAME_DECLARACIONES+declaracionLoc.getInteger(FIELD_COLL_NAME);
        JsonObject query = new JsonObject().put(_ID, declaracionLoc.getJsonObject("declaracion").getString(FIELD_NUMERO_DECLARACION));
        mongoClient.findOne(collection, query, new JsonObject(), dec->{
            if (dec.succeeded()){
                JsonObject declaracion = dec.result();
                if (declaracion != null){
                    declaracion.getJsonObject(FIELD_ENCABEZADO).put(FIELD_NUMERO_DECLARACION, declaracion.getString(_ID));
                    declaracion.remove(_ID);
                    resultHandler.handle(dec);
                }else{
                    resultHandler.handle(Future.failedFuture("Decalracion no localizada"));
                    logger.info(String.format("Declaracion no encontarda que esta en recepcion web %s",declaracionLoc.getJsonObject("declaracion").getString(FIELD_NUMERO_DECLARACION)));
                }
            }else{
                resultHandler.handle(Future.failedFuture("Error al consultad la declaracion"+declaracionLoc.getJsonObject("declaracion").getString(FIELD_NUMERO_DECLARACION)));
            }
        });
        return this;
    }
}