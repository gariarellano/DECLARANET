/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.dao.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.dao.DAOGeneric;
import mx.gob.sfp.dgti.dao.DAOReservadosInterface;

/**
 * @author cgarias
 * @since 05/07/2020
 */
public class DAOReservados extends DAOGeneric implements DAOReservadosInterface{
    private static final Logger logger = Logger.getLogger(DAOReservados.class.getName());
    
    public DAOReservados(Vertx  vertx){
        super(vertx);
    }

    @Override
    public DAOReservadosInterface consultaReservados(Integer collName, Handler<AsyncResult<List<JsonObject>>> resultHandler) {
        FindOptions options = new FindOptions().setFields(new JsonObject()
                .put("rfc", 1)
                .put("curp", 1));
        mongoClient.findWithOptions("reservaSN" + collName, new JsonObject(), options, res -> {
            if (res.succeeded()) {
                if (res.result() != null) {
                    resultHandler.handle(Future.succeededFuture(res.result()));
                } else {
                    resultHandler.handle(Future.succeededFuture(new ArrayList<>()));
                }
            } else {
                logger.severe(res.cause().getMessage());
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
        return this;
    }

    @Override
    public DAOReservadosInterface guardar(String collection, JsonObject aGuardar, Integer intentoGuardado, Handler<AsyncResult<String>> resultHandler) {
        mongoClient.save(collection, aGuardar, insert->{
            if (insert.succeeded()){
                resultHandler.handle(Future.succeededFuture("OK"));
            }else{
                if (intentoGuardado > 0){
                    guardar(collection, aGuardar, intentoGuardado-1, resultHandler);
                }else{
                    logger.severe("error al realizar el insertado en mongo, por tercer vez.");
                    resultHandler.handle(Future.failedFuture("error al insertar"));
                }
            }
        });
        return this;
    }
}
