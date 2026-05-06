/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.dao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.BulkOperation;
import java.util.List;
import mx.gob.sfp.dgti.dao.impl.DAOCumplimiento;

/**
 *
 * @author cgarias
 */
public interface IDAOCumplimiento {
    
    public static IDAOCumplimiento init(Vertx vertx){
        return new DAOCumplimiento(vertx);
    }
    
    public IDAOCumplimiento consultaCumplimientoById(Integer collName, String id, Handler<AsyncResult<Boolean>> sePuedeAgregar);
    public IDAOCumplimiento consultaCumplimientoByIdRusp(Integer collName, String idRusp, Handler<AsyncResult<Boolean>> sePuedeAgregar);
    
    public IDAOCumplimiento registraCumplimiento(JsonObject cumplimientoDecla, JsonObject movimientoRusp, Handler<AsyncResult<String>> registrado);
    public IDAOCumplimiento registraCumplimiento(JsonObject cumplimientoDecla, List<BulkOperation> insertar, Handler<AsyncResult<String>> registrado);
    public IDAOCumplimiento guardaCumplimientoManual(Integer collName, JsonObject data, Handler<AsyncResult<String>> guardado);
    
    public IDAOCumplimiento localizaMovimiento (Integer collName, JsonObject cumplimientoDecla, Handler<AsyncResult<List<JsonObject>>> localizados);
    
    public IDAOCumplimiento localizaMovimientoPorNoComprobante(Integer collName, String noComprobante, Handler<AsyncResult<Boolean>> localizado);
    public IDAOCumplimiento eliminaCumplimiento(Integer collName, JsonObject cumplimiento, Handler<AsyncResult<JsonObject>> eliminado);
    public void isExtemporaneo(JsonObject cumplimiento);
}
