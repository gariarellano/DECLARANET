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
import java.util.List;
import mx.gob.sfp.dgti.dao.impl.DAONoLocDecla;

/**
 *
 * @author gio_j
 */
public interface IDAONoLocDecla {
    
    static IDAONoLocDecla init(Vertx vertx){
        return new DAONoLocDecla(vertx);
    }
    
    public IDAONoLocDecla guardaNoLocalizadoDecla(JsonObject cumplimientoDecla, Handler<AsyncResult<String>> guardado);
    public IDAONoLocDecla localizaMovimiento(Integer collName, JsonObject cumplimientoDecla, Handler<AsyncResult<List<JsonObject>>> localizados);
    public IDAONoLocDecla eliminaDatosDecla(Integer collName, JsonObject localizado, Handler<AsyncResult<String>> eliminado);
    public IDAONoLocDecla localizaMovimientoPorNoComprobante(Integer collName, String noComprobante, Handler<AsyncResult<List<JsonObject>>> localizados);
    public IDAONoLocDecla localizaMovimientoById(Integer collName, String idNoLocDecla, Handler<AsyncResult<JsonObject>> localizado);
}
