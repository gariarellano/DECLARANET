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
import mx.gob.sfp.dgti.dao.impl.DAOExclusion;

/**
 *
 * @author cgarias
 */
public interface IDAOExclusion {
    static IDAOExclusion init(Vertx vertx){
        return new DAOExclusion(vertx);
    }
    
    public IDAOExclusion registraExclusion(String collName, JsonObject data, Handler<AsyncResult<String>> guardado);
    public IDAOExclusion existeMovimientoExcluido(Integer collName, String id, Handler<AsyncResult<JsonObject>> encontrado);
}
