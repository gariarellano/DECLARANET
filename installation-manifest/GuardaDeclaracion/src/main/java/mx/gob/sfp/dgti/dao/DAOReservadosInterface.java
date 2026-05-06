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
import mx.gob.sfp.dgti.dao.impl.DAOReservados;

/**
 *
 * @author cgarias
 * @since 05/07/2020
 */
public interface DAOReservadosInterface {
    
    static DAOReservadosInterface init(Vertx vertx){
        return new DAOReservados(vertx);
    }
    
    public DAOReservadosInterface consultaReservados(Integer collName, Handler<AsyncResult<List<JsonObject>>> resultHandler);
    public DAOReservadosInterface guardar(String collection, JsonObject aGuardar, Integer intentoGuardado, Handler<AsyncResult<String>> resultHandler);
}
