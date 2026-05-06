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
import mx.gob.sfp.dgti.dao.impl.DAOFirmantes;

/**
 * Interface que contiene la firma de los métodos con el que accede a la tabla de firmantes.
 * 
 * @author cgarias
 * @since 19/12/2019
 */
public interface DAOFirmantesInterface {
    
    static DAOFirmantesInterface init(Vertx vertx){
        return new DAOFirmantes(vertx);
    }
    
    public DAOFirmantesInterface consultaFirmanteActivo(Integer collName, Handler<AsyncResult<JsonObject>> resultHandler);
}
