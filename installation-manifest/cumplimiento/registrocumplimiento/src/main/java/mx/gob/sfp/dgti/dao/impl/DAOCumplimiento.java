/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.dao.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.BulkOperation;
import io.vertx.ext.mongo.FindOptions;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.dao.DAOGeneric;
import mx.gob.sfp.dgti.dao.IDAOCumplimiento;
import mx.gob.sfp.dgti.dto.http.response.JsonObjectHttpResponse;
import mx.gob.sfp.dgti.dto.http.response.JsonObjectRespone;
import mx.gob.sfp.dgti.util.UtileriaFunciones;
import mx.gob.sfp.dgti.util.enums.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.utils.FechaUtil;

/**
 *
 * @author cgarias
 */
public class DAOCumplimiento extends DAOGeneric implements IDAOCumplimiento{
    private static final Logger logger = Logger.getLogger(DAOCumplimiento.class.getName());
    private static final String COLLECTION_CUMPLIMIENTO = "cumplimiento%d";
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String FIELD_FECHA_RECEPCION = "fechaRecepcion";
    public static final String FIELD_ANIO = "anio";
    
    public DAOCumplimiento(Vertx vertx){
        super(vertx);
    }

    @Override
    public IDAOCumplimiento consultaCumplimientoById(Integer collName, String id, Handler<AsyncResult<Boolean>> sePuedeAgregar) {
        FindOptions options = new FindOptions();
        JsonObject fields = new JsonObject().put("_id", Boolean.TRUE);
        options.setFields(fields);
        
        this.mongoClient.findWithOptions(
                String.format(COLLECTION_CUMPLIMIENTO, collName), 
                new JsonObject().put("_id", id), 
                options, response->{
                    if (response.succeeded()){
                        sePuedeAgregar.handle(Future.succeededFuture(response.result().isEmpty())); 
                    }else{
                        sePuedeAgregar.handle(Future.failedFuture(response.cause()));
                    }
                });
        return this;
    }
    
    @Override
    public IDAOCumplimiento consultaCumplimientoByIdRusp(Integer collName, String idRusp, Handler<AsyncResult<Boolean>> sePuedeAgregar) {
        FindOptions options = new FindOptions();
        JsonObject fields = new JsonObject().put("_id", Boolean.TRUE);
        options.setFields(fields);
        
        this.mongoClient.findWithOptions(
                String.format(COLLECTION_CUMPLIMIENTO, collName), 
                new JsonObject().put("datosRusp.id", idRusp), 
                options, response->{
                    if (response.succeeded()){
                        sePuedeAgregar.handle(Future.succeededFuture(response.result().isEmpty())); 
                    }else{
                        sePuedeAgregar.handle(Future.failedFuture(response.cause()));
                    }
                });
        return this;
    }

    @Override
    public IDAOCumplimiento registraCumplimiento(JsonObject cumplimientoDecla, JsonObject movimientoRusp, Handler<AsyncResult<String>> registrado) {
        if ((cumplimientoDecla.getInteger("idMovimiento") != null
                && (
                    movimientoRusp.getInteger("idSp").equals(cumplimientoDecla.getInteger("idSp")) ||
                    movimientoRusp.getString("curp").equals(cumplimientoDecla.getString("curp")))
                    )
                || cumplimientoDecla.getInteger("idMovimiento") == null) {
            
            JsonObject data = new JsonObject()
                    .put("_id", cumplimientoDecla.getString("idDNetNoLocalizado"))
                    .put("datosRusp", movimientoRusp)
                    .put("datosDecla", cumplimientoDecla)
                    .put("fechaRegistro", FechaUtil.getFechaFormatoISO8601(new Date()))
                    .put("activo", 1);
            
            this.isExtemporaneo(data);
            mongoClient.save(
                    String.format(COLLECTION_CUMPLIMIENTO, cumplimientoDecla.getJsonObject("institucionReceptora").getInteger("collName")), 
                    data, 
                    guardado->{
                        if (guardado.succeeded()){
//                            logger.info("Se registro cumplimiento.");
                            registrado.handle(Future.succeededFuture("OK"));
                        }else{
                            registrado.handle(Future.failedFuture(guardado.cause()));
                        }
                    });
        }else{
            registrado.handle(Future.succeededFuture("NO_CORRESPONDE_MOVIMIENTO"));
        }
        return this;
    }
    
    @Override
    public IDAOCumplimiento registraCumplimiento(JsonObject cumplimientoDecla, List<BulkOperation> insertar, Handler<AsyncResult<String>> registrado){
        
        mongoClient.bulkWrite(
                String.format(COLLECTION_CUMPLIMIENTO, cumplimientoDecla.getJsonObject("institucionReceptora").getInteger("collName")), 
                insertar, 
                guardado->{
                    if (guardado.succeeded()){
//                        logger.info("Se registro cumplimiento.");
                        registrado.handle(Future.succeededFuture("OK"));
                    }else{
                        registrado.handle(Future.failedFuture(guardado.cause()));
                    }
                });
        return this;
    }

    @Override
    public IDAOCumplimiento guardaCumplimientoManual(Integer collName, JsonObject data, Handler<AsyncResult<String>> guardado) {
        this.isExtemporaneo(data);
        mongoClient.save(
                String.format(COLLECTION_CUMPLIMIENTO, collName), 
                data, guardar->{
                    guardado.handle(guardar);
                });
        return this;
    }

    @Override
    public IDAOCumplimiento localizaMovimiento(Integer collName, JsonObject cumplimientoDecla, Handler<AsyncResult<List<JsonObject>>> localizados){
        JsonObject query = this.getParamettersToQueryRusp(cumplimientoDecla);
        mongoClient.find(
                String.format(COLLECTION_CUMPLIMIENTO, collName),
                query, 
                localizada->{
                    localizados.handle(localizada);
                });
        return this;
    }
    
    @Override
    public IDAOCumplimiento localizaMovimientoPorNoComprobante(Integer collName, String noComprobante, Handler<AsyncResult<Boolean>> localizado) {
        FindOptions options = new FindOptions();
        JsonObject fields = new JsonObject().put("_id", Boolean.TRUE);
        options.setFields(fields);
        
        this.mongoClient.findWithOptions(
                String.format(COLLECTION_CUMPLIMIENTO, collName), 
                new JsonObject().put("datosDecla.noComprobante", noComprobante).put("activo",1), 
                options, response->{
                    if (response.succeeded()){
                        localizado.handle(Future.succeededFuture(!response.result().isEmpty())); 
                    }else{
                        localizado.handle(Future.failedFuture(response.cause()));
                    }
                });
        return this;
    }
    
    public void isExtemporaneo(JsonObject cumplimiento){
        try {
            if (!EnumTipoDeclaracion.AVISO.name().equals(cumplimiento.getJsonObject("datosDecla").getString("tipoDeclaracion"))){
                if (UtileriaFunciones.esDeclaracionExtemporanea(
                        EnumTipoDeclaracion.valueOf(cumplimiento.getJsonObject("datosDecla").getString("tipoDeclaracion")),
                        new SimpleDateFormat(DATE_FORMAT).format(FechaUtil.getFechaFormatoISO8601(cumplimiento.getJsonObject("datosDecla").getString(FIELD_FECHA_RECEPCION))),
                        cumplimiento.getJsonObject("datosDecla").getString("fechaEncargo") != null ? new SimpleDateFormat(DATE_FORMAT).format(FechaUtil.getFechaFormatoISO8601(cumplimiento.getJsonObject("datosDecla").getString("fechaEncargo")+"T00:00:00.000")) : null,
                        cumplimiento.getJsonObject("datosDecla").getInteger(FIELD_ANIO).toString(),
                        cumplimiento.getJsonObject("datosRusp").getInteger("idNivelJerarquico"),
                        cumplimiento.getJsonObject("datosRusp").getBoolean("sindicalizado"))){
                    cumplimiento.put("extemporaneo", Boolean.TRUE);
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al realizar el parceado de la fecha de recepcion de la declaracion. en isExtemporaneo {0} {1}", new Object[]{ex, cumplimiento});   
        }
    }
    
    @Override
    public IDAOCumplimiento eliminaCumplimiento(Integer collName, JsonObject cumplimiento, Handler<AsyncResult<JsonObject>> response){
        mongoClient.removeDocument(
                String.format(COLLECTION_CUMPLIMIENTO, collName),
                cumplimiento, eliminado->{
                    if (eliminado.succeeded()){
                        logger.info("Cumplimiento eliminado por localizacion correcta"+cumplimiento.getString("_id"));
                        response.handle(
                             Future.succeededFuture(
                                     new JsonObjectHttpResponse(
                                             HttpResponseStatus.OK.code(),
                                             new JsonObjectRespone("OK", "Registro guarado-cump"))));
                    }
                });
        return this;
    }
    
    /**
     * Método que realiza el armado del query para consultar 
     * movimientos Rusp en cumplimiento y asi darle el 
     * seguimiento por puesto.
     *
     * @param cumplimientoDecla objeto cumplimiento de declaración.
     * @return String cadena de parametros y valores para consultar en elastic
     * serch y localizar el movimiento RUSP.
     */
    private JsonObject getParamettersToQueryRusp(JsonObject cumplimientoDecla) {
        JsonObject query = new JsonObject();
        JsonArray or = new JsonArray();
        or.add(new JsonObject().put("datosRusp.idMovimiento", cumplimientoDecla.getInteger("idMovimiento")));
        or.add(new JsonObject().put("datosRusp.idSp", cumplimientoDecla.getInteger("idSp")));
        or.add(new JsonObject().put("datosRusp.curp", cumplimientoDecla.getString("curp")));
        query.put("$or", or);
        
        switch (cumplimientoDecla.getString("tipoDeclaracion")) {
            case "INICIO":
                query.put("datosRusp.tipoObligacion", "ALTA");                
                query.put("datosRusp.fechaTomaPosesionPuesto", cumplimientoDecla.getString("fechaEncargo"));
                break;
            case "CONCLUSION":
                query.put("datosRusp.tipoObligacion", "BAJA");                
                query.put("datosRusp.fechaBaja", cumplimientoDecla.getString("fechaEncargo"));
                break;
            case "MODIFICACION":
                query.put("datosRusp.tipoObligacion", "ACTIVO_MAYO");
                query.put("datosRusp.anio", cumplimientoDecla.getInteger("anio"));
                break;
            case "AVISO":
                query.put("datosRusp.tipoObligacion", "AVISO");
                query.put("datosRusp.fechaTomaPosesionPuesto", cumplimientoDecla.getString("fechaEncargo"));
                break;
            default:
                logger.info(String.format("el tipo de declaracion %s no requiere pearameto de fecha encargo para buscar en RUSP", cumplimientoDecla.getString("tipoDeclaracion")));
                break;
        }
        query.put("datosRusp.idNivelJerarquico", cumplimientoDecla.getInteger("idNivelJerarquico"));
        query.put("activo",1);
        return query;
    }
}
