/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.dao.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.dao.DAOGeneric;
import mx.gob.sfp.dgti.dao.IDAOExclusion;

/**
 *
 * @author gio_j
 */
public class DAOExclusion extends DAOGeneric implements IDAOExclusion{
    private static final String EXCLUSION = "exclusion";
    
    public DAOExclusion(Vertx vertx){
        super(vertx);
    }
    
    @Override
    public IDAOExclusion registraExclusion(String collName, JsonObject data, Handler<AsyncResult<String>> guardado) {
        mongoClient.save(EXCLUSION.concat(collName), data, guardar->{
            guardado.handle(guardar);
        });
        return this;
    }

    @Override
    public IDAOExclusion existeMovimientoExcluido(Integer collName, String id, Handler<AsyncResult<JsonObject>> encontrado) {
        mongoClient.findOne(EXCLUSION.concat(String.valueOf(collName)), new JsonObject().put("_id", id), new JsonObject(), esta->{
            encontrado.handle(esta);
        });
       return this;
    }
}
