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
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.dao.DAOGeneric;
import mx.gob.sfp.dgti.dao.IDAOMovimientosRusp;

/**
 *
 * @author cgarias
 */
public class DAOMovimientosRusp extends DAOGeneric implements IDAOMovimientosRusp{
    
    private static final Logger logger = Logger.getLogger(DAOMovimientosRusp.class.getName());
    private static final String COLECTION_NO_LOCALIZADOS_R = "nolocalizadosr%d";
    
    public DAOMovimientosRusp(Vertx vertx){
        super(vertx);
    }

    @Override
    public IDAOMovimientosRusp localizaMovimiento(JsonObject cumplimientoDecla, Integer collName, Handler<AsyncResult<List<JsonObject>>> localizado) {
        JsonObject query = this.getParamettersToQueryRuspEnCumplimiento(cumplimientoDecla);
        mongoClient.find(
                String.format(COLECTION_NO_LOCALIZADOS_R, collName),
                query, 
                localizada->{
                    localizado.handle(localizada);
                });
        return this;
    }

    @Override
    public IDAOMovimientosRusp eliminaDatosRusp(Integer collName, List<String> localizado, Handler<AsyncResult<String>> eliminado) {
        
        JsonObject query  = new JsonObject().put("_id", new JsonObject().put("$in", localizado));
        mongoClient.removeDocuments(
                String.format(COLECTION_NO_LOCALIZADOS_R, collName), 
                query, 
                docEliminado->{
                    if (docEliminado.succeeded()){
                        eliminado.handle(Future.succeededFuture("OK"));
                    }else{
                        eliminado.handle(Future.failedFuture(docEliminado.cause()));
                    }
                });
        return this;
    }
    
    /**
     * Método que realiza el armado del query para consultar movimientos Rusp,
     * deribados de un cumplimiento.
     *
     * @param cumplimientoDecla objeto cumplimiento de declaración.
     * @return String cadena de parametros y valores para consultar en elastic
     * serch y localizar el movimiento RUSP.
     */
    private JsonObject getParamettersToQueryRuspEnCumplimiento(JsonObject cumplimientoDecla) {
        JsonObject query = new JsonObject();
        JsonArray or = new JsonArray();
        or.add(new JsonObject().put("idMovimiento", cumplimientoDecla.getInteger("idMovimiento")));
        or.add(new JsonObject().put("idSp", cumplimientoDecla.getInteger("idSp")));
        or.add(new JsonObject().put("curp", cumplimientoDecla.getString("curp")));
        query.put("$or", or);
        
        switch (cumplimientoDecla.getString("tipoDeclaracion")) {
            case "INICIO":
                query.put("tipoObligacion", "ALTA");                
                query.put("fechaTomaPosesionPuesto", cumplimientoDecla.getString("fechaEncargo"));
                break;
            case "CONCLUSION":
                query.put("tipoObligacion", "BAJA");                
                query.put("fechaBaja", cumplimientoDecla.getString("fechaEncargo"));
                break;
            case "MODIFICACION":
                query.put("tipoObligacion", "ACTIVO_MAYO");
                query.put("anio", cumplimientoDecla.getInteger("anio"));
                break;
            case "AVISO":
                query.put("tipoObligacion", "AVISO");
                query.put("fechaTomaPosesionPuesto", cumplimientoDecla.getString("fechaEncargo"));
                break;
            default:
                logger.info(String.format("el tipo de declaracion %s no requiere pearameto de fecha encargo para buscar en RUSP", cumplimientoDecla.getString("tipoDeclaracion")));
                break;
        }
        query.put("activo",1);
        return query;
    }

    @Override
    public IDAOMovimientosRusp guardaNoLocalizado(Integer collName, JsonObject movimientoRusp, Handler<AsyncResult<String>> guardado) {
        mongoClient.save(
                String.format(COLECTION_NO_LOCALIZADOS_R,collName), 
                movimientoRusp, 
                guardadoR->{
                    guardado.handle(guardadoR);
                });
        return this;
    }

    @Override
    public IDAOMovimientosRusp localizaMovimientoById(Integer collName, String idRusp, Handler<AsyncResult<JsonObject>> localizadoR) {
        mongoClient.findOne(
                String.format(COLECTION_NO_LOCALIZADOS_R, collName),
                new JsonObject().put("_id", idRusp), 
                new JsonObject(),
                localizada->{
                    localizadoR.handle(localizada);
                });
        return this;
    }
}
