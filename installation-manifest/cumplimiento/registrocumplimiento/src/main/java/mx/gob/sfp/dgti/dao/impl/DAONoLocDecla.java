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
import mx.gob.sfp.dgti.dao.IDAONoLocDecla;

/**
 *
 * @author gio_j
 */
public class DAONoLocDecla extends DAOGeneric implements IDAONoLocDecla{
    private static final Logger logger = Logger.getLogger(DAONoLocDecla.class.getName());
    private static final String COLLECTION_NO_LOCALISADOSD = "nolocalizadosd%d";
    
    public DAONoLocDecla(Vertx vertx){
        super(vertx);
    }

    @Override
    public IDAONoLocDecla guardaNoLocalizadoDecla(JsonObject cumplimientoDecla, Handler<AsyncResult<String>> guardado) {
        
        mongoClient.save(
                String.format(COLLECTION_NO_LOCALISADOSD, cumplimientoDecla.getJsonObject("institucionReceptora").getInteger("collName")), 
                cumplimientoDecla, 
                guardar->{                    
                        guardado.handle(guardar);
                });
        return this;
    }

    @Override
    public IDAONoLocDecla localizaMovimiento(Integer collName, JsonObject movimientoRusp, Handler<AsyncResult<List<JsonObject>>> localizados) {
        try{
            JsonObject parametersQuery = this.getParamettersToQueryDecla(movimientoRusp);
            mongoClient.find(
                    String.format(COLLECTION_NO_LOCALISADOSD, collName),
                    parametersQuery, localizadosD->{
                        localizados.handle(localizadosD);
                    });
        }catch(Exception ex){
            logger.severe(String.format("error en armar los parametros para consultar  nolocalizadosD %s", ex));
            localizados.handle(Future.failedFuture("Erroren getParamettersToQueryDecla a la hora de armar el filtrado para consulta de datos en decla."));
        }
        return this;
    }
    
    @Override
    public IDAONoLocDecla eliminaDatosDecla(Integer collName, JsonObject localizado, Handler<AsyncResult<String>> eliminado) {
        mongoClient.removeDocument(
                String.format(COLLECTION_NO_LOCALISADOSD, collName), 
                localizado, 
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
     * Método que arma los parametros de consulta para encontrar cumplimientos
     * de declaranet.
     *
     * @param movimientoRusp Objeto movimineto Rusp.
     * @return String query para localizar movimientos.
     */
    private JsonObject getParamettersToQueryDecla(JsonObject movimientoRusp) {
        JsonObject query = new JsonObject();
        JsonArray or = new JsonArray();
        or.add(new JsonObject().put("idMovimiento", movimientoRusp.getInteger("idMovimiento")));
        or.add(new JsonObject().put("idSp", movimientoRusp.getInteger("idSp")));
        or.add(new JsonObject().put("curp", movimientoRusp.getString("curp")));
        query.put("$or", or);
        
        query.put("anio", movimientoRusp.getInteger("anio"));
        switch (movimientoRusp.getString("tipoObligacion")) {
            case "ALTA":
                query.put("tipoDeclaracion", "INICIO");
                query.put("fechaEncargo", movimientoRusp.getString("fechaTomaPosesionPuesto"));
            case "BAJA":
                query.put("tipoDeclaracion", "CONCLUCION");
                query.put("fechaEncargo", movimientoRusp.getString("fechaBaja"));
                break;
            case "ACTIVO_MAYO":
                query.put("tipoDeclaracion", "MODIFICACION");
                break;
            default:
                logger.info(String.format("El tipo de declaracion %s no tiene id de movimiento defeinido PENDIENTE", movimientoRusp.getString("tipoDeclaracion")));
                break;
        }
        query.put("activo", 1);
        return query;
    }

    @Override
    public IDAONoLocDecla localizaMovimientoPorNoComprobante(Integer collName, String noComprobante, Handler<AsyncResult<List<JsonObject>>> localizado) {
        mongoClient.find(
                String.format(COLLECTION_NO_LOCALISADOSD, collName), 
                new JsonObject().put("noComprobante", noComprobante).put("activo",1), 
                localizadoD->{
                    localizado.handle(localizadoD);
                });
        return this;
    }

    @Override
    public IDAONoLocDecla localizaMovimientoById(Integer collName, String idNoLocDecla, Handler<AsyncResult<JsonObject>> localizado) {
        mongoClient.findOne(
                String.format(COLLECTION_NO_LOCALISADOSD, collName), 
                new JsonObject().put("_id", idNoLocDecla), 
                new JsonObject(),
                localizadoD->{
                    localizado.handle(localizadoD);
                });
        return this;
    }     
}
