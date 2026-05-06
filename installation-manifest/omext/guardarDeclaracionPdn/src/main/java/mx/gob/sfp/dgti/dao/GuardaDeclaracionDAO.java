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

/**
 *
 * @author programador09
 */
public interface GuardaDeclaracionDAO {
    
    static GuardaDeclaracionDAO init(Vertx vertx){
        return new GuardaDeclaracionDAOImpl(vertx);
    }
    
    public void guardaDeclaracion(Integer collName, String numeroDeclaracion, JsonObject declaracionPdn, Handler<AsyncResult<String>> guardado);
}
