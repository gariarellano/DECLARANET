/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.dao.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.dao.DAOGeneric;
import mx.gob.sfp.dgti.dao.DAORecepcionWebInterface;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import static mx.gob.sfp.dgti.util.Constantes.COLLECTION_NAME_DECLARACIONES;
import static mx.gob.sfp.dgti.util.Constantes.COLLECTION_NAME_RECEPCION_WEB;
import static mx.gob.sfp.dgti.util.Constantes.COLLECTION_NAME_TRANSACCION_FUP;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ACUSE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ANIO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_CARGO_COMISION_INICIA;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_COLL_NAME;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_CORREO_ELECTRONICO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_CURP;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_DATOS_EMPLEO_CARGO_COMISION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_DATOS_GENERALES;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_DATOS_PERSONALES;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_DECLARANTE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_DEPENDECIA;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_DETALLE_AVISO_CAMBIO_DEP;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_DIGESTION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_DIGESTION_DCN;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_EMPLEO_CARGO_COMISION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ENCABEZADO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ENTE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ENTE_CARGO_COMISION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_EXTEMPORANEA;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_FECHA_ENCARGO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_FECHA_ID;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_FECHA_RECEPCION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_FIRMADA;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_FIRMADO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_FIRMA_ACUSE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_FIRMA_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ID;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ID_USR_DECNET;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ID_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_INSTITUCIONAL;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_INSTITUCION_RECEPTORA;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_NOMBRE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_NO_COMPROBANTE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_NUMERO_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_PERSONAL_ALTERNO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_PRIMER_APELLIDO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_RFC;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_SEGUNDO_APELLIDO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_SITUACION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_TIPO_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_TIPO_FORMATO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_VERSION_RECEPCION_WEB;
import static mx.gob.sfp.dgti.util.Constantes._ID;
import mx.gob.sfp.dgti.util.UtileriaFunciones;
import mx.gob.sfp.dgti.utils.FechaUtil;

/**
 * Calse que realiza el guardado de Recepcion Web
 * de recepcion de la decalración.
 * 
 * @author cgarias
 * @since 19/12/2019
 */
public class DAORecepcionWeb extends DAOGeneric implements DAORecepcionWebInterface {
    private static final Logger logger = Logger.getLogger(DAORecepcionWeb.class.getName());
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    public DAORecepcionWeb(Vertx vertx){
        super(vertx);
    }
    
    @Override
    public DAORecepcionWebInterface insertaRecepcion(JsonObject jsonFirmado, Handler<AsyncResult<JsonObject>> resultHandler) {
        try {
            JsonObject recepcionWeb = this.generaJsonRececpionWeb(jsonFirmado);
            String collectionRecWeb = COLLECTION_NAME_RECEPCION_WEB+jsonFirmado.getJsonObject(FIELD_DIGESTION).getInteger(FIELD_COLL_NAME);
            mongoClient.save(collectionRecWeb, recepcionWeb, saveReWeb -> {
                if(saveReWeb.succeeded()) {
                    recepcionWeb.put(_ID, saveReWeb.result());
                    resultHandler.handle(Future.succeededFuture(recepcionWeb));
                }else{
                    resultHandler.handle(Future.failedFuture("Error en guardar recepcionWeb"));
                }
            });            
        } catch (ParseException | NullPointerException ex) {
            logger.severe(String.format("sin propiedades necesarias para Recepcion WEB = %s", ex));
            resultHandler.handle(Future.failedFuture("Error en guardar recepcionWeb"));
        }
        return this;
    }
    
    
    private JsonObject generaJsonRececpionWeb(JsonObject jsonFirmado) throws ParseException{
        JsonObject recepcionWeb = new JsonObject();
        recepcionWeb.put(FIELD_VERSION_RECEPCION_WEB, System.getenv("VERSION_RECEPCION_WEB"));

        recepcionWeb.put(FIELD_INSTITUCION_RECEPTORA, jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_INSTITUCION_RECEPTORA));
            JsonObject declaracion = new JsonObject();
            declaracion.put(FIELD_NUMERO_DECLARACION, jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_NUMERO_DECLARACION));
            declaracion.put(FIELD_TIPO_DECLARACION, jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_TIPO_DECLARACION));
            
            
            
            declaracion.put(FIELD_TIPO_FORMATO, jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_TIPO_FORMATO));
            declaracion.put(FIELD_FECHA_RECEPCION,jsonFirmado.getString(FIELD_FECHA_RECEPCION));
            declaracion.put(FIELD_SITUACION, FIELD_FIRMADA);
            declaracion.put(FIELD_NO_COMPROBANTE, jsonFirmado.getString(FIELD_NO_COMPROBANTE));
            declaracion.put(FIELD_FECHA_ID,jsonFirmado.getString(FIELD_FECHA_ID));
            
            JsonObject declarante = new JsonObject();
            JsonObject datosPersonales;
            String correo;
            if ("NOTA".equals(jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_TIPO_DECLARACION))){
                datosPersonales = jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getJsonObject(FIELD_DATOS_PERSONALES);
                correo = datosPersonales.getString("personalAlterno");    
                declaracion.put(FIELD_FECHA_ENCARGO, jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getJsonObject("declaracionOrigen").getJsonObject(FIELD_ENCABEZADO).getString(FIELD_FECHA_ENCARGO));
                declaracion.put(FIELD_EXTEMPORANEA,false);
                declaracion.put("declaracionOrigen", jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getJsonObject("declaracionOrigen"));
            }else if(EnumTipoDeclaracion.AVISO.name().equals(jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_TIPO_DECLARACION))){
                datosPersonales = jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_GENERALES).getJsonObject(FIELD_DATOS_PERSONALES);
                correo = jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_GENERALES).getJsonObject(FIELD_CORREO_ELECTRONICO).getString(FIELD_INSTITUCIONAL) != null ? jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_GENERALES).getJsonObject(FIELD_CORREO_ELECTRONICO).getString(FIELD_INSTITUCIONAL): jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_GENERALES).getJsonObject(FIELD_CORREO_ELECTRONICO).getString(FIELD_PERSONAL_ALTERNO);    
                declaracion.put(FIELD_ANIO, jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getInteger(FIELD_ANIO));
                declaracion.put(FIELD_FECHA_ENCARGO, jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DETALLE_AVISO_CAMBIO_DEP).getJsonObject(FIELD_CARGO_COMISION_INICIA).getString("fechaInicioEncargo"));
                declaracion.put(FIELD_EXTEMPORANEA, false);
                JsonArray enteCargoComision = new JsonArray();
                enteCargoComision.add(new JsonObject()
                                .put(FIELD_ID, jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DETALLE_AVISO_CAMBIO_DEP).getJsonObject(FIELD_CARGO_COMISION_INICIA).getJsonObject(FIELD_ENTE).getString(FIELD_ID))
                                .put(FIELD_DEPENDECIA, jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DETALLE_AVISO_CAMBIO_DEP).getJsonObject(FIELD_CARGO_COMISION_INICIA).getJsonObject(FIELD_ENTE).getString(FIELD_NOMBRE)));
                declarante.put(FIELD_ENTE_CARGO_COMISION, enteCargoComision);
            }else{
                datosPersonales = jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_GENERALES).getJsonObject(FIELD_DATOS_PERSONALES);
                correo = jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_GENERALES).getJsonObject(FIELD_CORREO_ELECTRONICO).getString(FIELD_INSTITUCIONAL) != null ? jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_GENERALES).getJsonObject(FIELD_CORREO_ELECTRONICO).getString(FIELD_INSTITUCIONAL): jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_GENERALES).getJsonObject(FIELD_CORREO_ELECTRONICO).getString(FIELD_PERSONAL_ALTERNO);    
                declaracion.put(FIELD_ANIO, jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getInteger(FIELD_ANIO));
                declaracion.put(FIELD_FECHA_ENCARGO, jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_FECHA_ENCARGO));
                declaracion.put(FIELD_EXTEMPORANEA,
                        UtileriaFunciones
                                .esDeclaracionExtemporanea(
                                        EnumTipoDeclaracion.valueOf(jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_TIPO_DECLARACION)),
                                        new SimpleDateFormat(DATE_FORMAT).format(FechaUtil.getFechaFormatoISO8601(jsonFirmado.getString(FIELD_FECHA_RECEPCION))),
                                        jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getInteger(FIELD_ANIO).toString(),
                                        jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getJsonObject("nivelJerarquico").getInteger("id")
                                )
                );
                JsonArray entesCargoComision = new JsonArray();
                for (Object index : jsonFirmado.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_EMPLEO_CARGO_COMISION).getJsonArray(FIELD_EMPLEO_CARGO_COMISION)){
                    JsonObject empleoCargoComision  = (JsonObject) index;
                    entesCargoComision
                            .add(new JsonObject()
                                    .put(FIELD_ID, empleoCargoComision.getJsonObject(FIELD_ENTE).getString(FIELD_ID))
                                    .put(FIELD_DEPENDECIA, empleoCargoComision.getJsonObject(FIELD_ENTE).getString(FIELD_NOMBRE))
                            );
                }
                declarante.put(FIELD_ENTE_CARGO_COMISION, entesCargoComision);
            }
            declarante.put(FIELD_NOMBRE, UtileriaFunciones.getNombreCompleto(datosPersonales.getString(FIELD_NOMBRE), datosPersonales.getString(FIELD_PRIMER_APELLIDO), datosPersonales.getString(FIELD_SEGUNDO_APELLIDO)));
            declarante.put(FIELD_RFC, datosPersonales.getString(FIELD_RFC));
            declarante.put(FIELD_CURP, datosPersonales.getString(FIELD_CURP));
            declarante.put(FIELD_ID_USR_DECNET, jsonFirmado.getJsonObject(FIELD_DIGESTION).getInteger(FIELD_ID_USUARIO));
            declarante.put(FIELD_CORREO_ELECTRONICO, correo);
            
        recepcionWeb.put(FIELD_DECLARACION,declaracion);     
        recepcionWeb.put(FIELD_DECLARANTE,declarante);
            JsonObject firmaDeclaracion = jsonFirmado.getJsonObject(FIELD_FIRMADO);
            firmaDeclaracion.put(FIELD_DIGESTION_DCN, jsonFirmado.getJsonObject(FIELD_DIGESTION).getString(FIELD_DIGESTION_DCN));
        recepcionWeb.put(FIELD_FIRMA_DECLARACION,firmaDeclaracion);
        recepcionWeb.put(FIELD_FIRMA_ACUSE,jsonFirmado.getJsonObject(FIELD_ACUSE));
        return recepcionWeb;
    }

    @Override
    public DAORecepcionWebInterface rollBack(JsonObject declaracion, Handler<AsyncResult<Void>> resultHandler) {
        String collDecla = COLLECTION_NAME_DECLARACIONES + declaracion.getJsonObject(FIELD_DIGESTION).getInteger(FIELD_COLL_NAME);
        String collRecWeb = COLLECTION_NAME_RECEPCION_WEB + declaracion.getJsonObject(FIELD_DIGESTION).getInteger(FIELD_COLL_NAME);
        String collTransFup = COLLECTION_NAME_TRANSACCION_FUP + declaracion.getJsonObject(FIELD_DIGESTION).getInteger(FIELD_COLL_NAME);
        
        Future deleteDeclaF = Future.future();
        Future deleteRWF = Future.future();
        Future deleteFupF = Future.future();
        
        JsonObject rmDoc = new JsonObject();
        rmDoc.put(_ID, declaracion.getJsonObject(FIELD_DIGESTION).getString(FIELD_NUMERO_DECLARACION));
        mongoClient.removeDocument(collDecla, rmDoc, deleteDecla -> {
            if (deleteDecla.succeeded()){                
                logger.log(Level.INFO, "RollBack on : {0} Se realizo el borrado de la declaracion _id : {1}", new Object[]{collDecla, declaracion.getJsonObject(FIELD_DIGESTION).getString(FIELD_NUMERO_DECLARACION)});
                deleteDeclaF.complete();
            }
        });  
        rmDoc.clear();
        rmDoc.put("declaracion.numeroDeclaracion", declaracion.getJsonObject(FIELD_DIGESTION).getString(FIELD_NUMERO_DECLARACION));
        mongoClient.removeDocument(collRecWeb, rmDoc, deleteRecWeb -> {
            if (deleteRecWeb.succeeded()){                
                logger.log(Level.INFO, "RollBack on : {0} Se realizo el borrado de la recepcionWeb declaracion.numeroDeclaracion : {1}", new Object[]{collRecWeb, declaracion.getJsonObject(FIELD_DIGESTION).getString(FIELD_NUMERO_DECLARACION)});
                deleteRWF.complete();
            }
        });
        rmDoc.clear();
        rmDoc.put("digestion.numeroDeclaracion", declaracion.getJsonObject(FIELD_DIGESTION).getString(FIELD_NUMERO_DECLARACION));
        mongoClient.removeDocument(collTransFup, rmDoc, deleteTransFup -> {
            if (deleteTransFup.succeeded()){
                logger.log(Level.INFO, "RollBack on : {0}  Se realizo el borrado de TR Fup digestion.numeroDeclaracion : {1}", new Object[]{collTransFup, declaracion.getJsonObject(FIELD_DIGESTION).getString(FIELD_NUMERO_DECLARACION)});
                deleteFupF.complete();
            }
        });
        CompositeFuture.all(deleteDeclaF, deleteRWF, deleteFupF).setHandler(deletes ->{
            if (deletes.succeeded())
                resultHandler.handle(Future.succeededFuture());
        });
        return this;
    }

    @Override
    public DAORecepcionWebInterface consultaDeclaracionesPeriodo(JsonObject periodoReportar, Handler<AsyncResult<JsonArray>> resultHandler) {
    
        String collection = COLLECTION_NAME_RECEPCION_WEB+periodoReportar.getInteger(FIELD_COLL_NAME);
        FindOptions findOption = new FindOptions();
        JsonObject fields = new JsonObject().put("declaracion.numeroDeclaracion", Boolean.TRUE);
        findOption.setFields(fields);
        JsonObject query = new JsonObject();
        if (periodoReportar.containsKey("noComprobante")){
            query.put("declaracion.noComprobante", periodoReportar.getString("noComprobante"));
        }else{
            query.put("declaracion.fechaRecepcion", new JsonObject().put("$gt", periodoReportar.getString("fechaInicio")).put("$lte", periodoReportar.getString("fechaFin")))
                    .put("declaracion.tipoDeclaracion", periodoReportar.getString("tipoDeclaracion"));

            if (periodoReportar.containsKey("usuariosId")){
                JsonArray usuariosArreglo = new JsonArray();
                for (Object index: periodoReportar.getJsonArray("usuariosId")){
                    Integer idUsuario =(Integer) index;
                    usuariosArreglo.add(new JsonObject().put("declarante.idUsrDecnet", idUsuario));
                }
                query.put("$or", usuariosArreglo);
            }        

            if (periodoReportar.containsKey("idEnte")){
                query.put("declarante.enteCargoComision.id", periodoReportar.getString("idEnte"));
            }
        }
        
        mongoClient.findWithOptions(collection, query, findOption, respusta->{
            if (respusta.succeeded()){
                resultHandler.handle(Future.succeededFuture(new JsonArray(respusta.result())));
            }else{
                resultHandler.handle(Future.failedFuture("Ocurrio un error al consultar recepcion web del periodo"));
            }       
        });
        return this;
    }

    @Override
    public DAORecepcionWebInterface consultaRecepcionWeb(JsonObject declaracion, Handler<AsyncResult<JsonObject>> resultHandler) {
        String collection = COLLECTION_NAME_RECEPCION_WEB+declaracion.getInteger(FIELD_COLL_NAME);
        JsonObject query = new JsonObject().put(_ID, declaracion.getString(_ID));
        mongoClient.findOne(collection, query, new JsonObject(), recepcionWe->{
            if (recepcionWe.succeeded()){
                resultHandler.handle(recepcionWe);
            }else{
                resultHandler.handle(Future.failedFuture("Error al consultad la RW "+declaracion.getString(_ID)));
            }
        });
        return this;
    }
    
    @Override
    public DAORecepcionWebInterface localizaPrimerMongoId(Integer collName, Handler<AsyncResult<String>> resultHandler) {
        FindOptions options = new FindOptions();
        options.setLimit(1);
        options.setFields(new JsonObject().put("_id",Boolean.TRUE));
        options.setSort(new JsonObject().put("_id",1));
        
        List<Object> areglo = Arrays.asList(new JsonObject().put("$strLenCP", "$_id"), 10);        
        mongoClient.findWithOptions(
                COLLECTION_NAME_RECEPCION_WEB.concat(collName.toString()), 
                new JsonObject().put("_id", new JsonObject().put("$exists", Boolean.TRUE)).put("$expr", new JsonObject().put("$gt", areglo)), 
                options, 
                localizado->{
                    if (localizado.succeeded()){
                        if (localizado.result() != null && !localizado.result().isEmpty()){
                            logger.info(String.format("recupera _id %s", localizado.result().get(0).getString(_ID)));
                            resultHandler.handle(Future.succeededFuture(localizado.result().get(0).getString(_ID)));
                        }else{
                            logger.info("asigna id null por sin resultado id mongo");
                            resultHandler.handle(Future.succeededFuture(null));
                        }
                    }else{
                        logger.severe(String.format("error en la consulta de identificar primer mongoid %s ", localizado.cause()));
                        resultHandler.handle(Future.succeededFuture(null));
                    }
                });
        return this;
    }

    @Override
    public DAORecepcionWebInterface recorreRecepcionWeb(String posicion, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler) {
        FindOptions options = new FindOptions();
        options.setFields(new JsonObject().put("declarante", 1));
//        options.setSkip(posicion);
        options.setSort(new JsonObject().put(_ID, 1));
        options.setLimit(1);
        
        mongoClient.findWithOptions(COLLECTION_NAME_RECEPCION_WEB.concat(collName.toString()), 
                new JsonObject().put(_ID, new JsonObject().put("$gt", posicion)), 
                options, 
                localizado->{
                    if (localizado.succeeded()){
                        if (localizado.result() != null && !localizado.result().isEmpty()){
                            resultHandler.handle(Future.succeededFuture(localizado.result().get(0)));
                        }else{
                            logger.info("==Termina por no localizar datos");
                            resultHandler.handle(Future.succeededFuture(null));
                        }
                    }else {
                        logger.severe(String.format("Error en la consulta de datos %s ", localizado.cause()));
                        resultHandler.handle(Future.succeededFuture(null));
                    }
                });
        return this;
    }
}
