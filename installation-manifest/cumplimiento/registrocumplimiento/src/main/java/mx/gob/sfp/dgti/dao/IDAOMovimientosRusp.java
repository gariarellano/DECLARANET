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
import mx.gob.sfp.dgti.dao.impl.DAOMovimientosRusp;

/**
 *
 * @author cgarias
 */
public interface IDAOMovimientosRusp {
    static IDAOMovimientosRusp init(Vertx vertx){
        return new DAOMovimientosRusp(vertx);
    }

    public IDAOMovimientosRusp localizaMovimiento(JsonObject cumplimientoDecla, Integer collName, Handler<AsyncResult<List<JsonObject>>> localizado);
    public IDAOMovimientosRusp eliminaDatosRusp(Integer collName, List<String> localizado, Handler<AsyncResult<String>> eliminado);
    public IDAOMovimientosRusp guardaNoLocalizado(Integer collName, JsonObject movimientoRusp, Handler<AsyncResult<String>> guardado);
    public IDAOMovimientosRusp localizaMovimientoById(Integer collName, String idRusp, Handler<AsyncResult<JsonObject>>localizadoR);
}
