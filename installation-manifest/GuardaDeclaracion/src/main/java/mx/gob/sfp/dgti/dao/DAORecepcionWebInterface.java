/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.dao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.dao.impl.DAORecepcionWeb;

/**
 * Interface que contien los métodos 
 * para acceder a reepcionWeb.
 * 
 * @author cgarias/
 * @since 18/12/2019
 */
public interface DAORecepcionWebInterface {
    
    static DAORecepcionWebInterface init(Vertx vertx){
        return new DAORecepcionWeb(vertx);
    }
 
    public DAORecepcionWebInterface insertaRecepcion(JsonObject jsonFirmado, Handler<AsyncResult<JsonObject>> resultHandler);
    
    public DAORecepcionWebInterface rollBack(JsonObject declaracion, Handler<AsyncResult<Void>> resultHandler);
    
    public DAORecepcionWebInterface consultaDeclaracionesPeriodo(JsonObject periodo, Handler<AsyncResult<JsonArray>> resultHandler);
    
    public DAORecepcionWebInterface consultaRecepcionWeb(JsonObject declaracion, Handler<AsyncResult<JsonObject>> resultHandler);
    
    public DAORecepcionWebInterface localizaPrimerMongoId(Integer collName, Handler<AsyncResult<String>> resultHandler);
    
    public DAORecepcionWebInterface recorreRecepcionWeb(String posicion, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler);
    
}
