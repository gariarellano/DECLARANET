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
import mx.gob.sfp.dgti.dao.impl.DAODatosPublicos;

/**
 *
 * @author cgarias
 */
public interface DAODatosPublicosInterface {
    
    public static DAODatosPublicosInterface init(Vertx vertx){
        return new DAODatosPublicos(vertx);    
    }
 
    public DAODatosPublicosInterface registraDatosPublicos(JsonObject datosPublicos, Integer collName, Handler<AsyncResult<String>> procTerminado);
}
