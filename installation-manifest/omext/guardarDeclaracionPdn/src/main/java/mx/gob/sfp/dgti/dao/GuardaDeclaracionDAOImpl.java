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
public class GuardaDeclaracionDAOImpl extends DaoGenerico implements GuardaDeclaracionDAO{

    private static final String COLLECTION_DECLARACION = "declaracion";
    
    public GuardaDeclaracionDAOImpl(Vertx vertx) {
        super(vertx);
    }

    @Override
    public void guardaDeclaracion(Integer collName, String numeroDeclaracion, JsonObject declaracionPdn, Handler<AsyncResult<String>> guardado) {
        mongoClient.save(COLLECTION_DECLARACION.concat(collName.toString()), declaracionPdn, esGuardado->{
            guardado.handle(esGuardado);
        });
    }   
}
