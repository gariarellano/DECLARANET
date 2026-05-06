/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.daopxy;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author cgarias
 */
public class DAOExpuestos implements DAOExpuestosInterface {
    private static final String EXPUESTOS = "expuestos";
    private static final String BITACORA_EXPUESTOS = "bitacoraExpuestos";
    private static final String FORMATO_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.S";
    private final MongoClient mongoClientExp;
    
    public DAOExpuestos (Vertx vertx, JsonObject config){
        mongoClientExp = MongoClient.createShared(vertx, config, "mongo-expuestos");
    }
    
    @Override
    public DAOExpuestosInterface consultaDatosExpuestos(String rfc, String curp, Handler<AsyncResult<JsonObject>> existe){
        mongoClientExp.findOne(
                EXPUESTOS, 
                new JsonObject().put("rfc", rfc).put("curp", curp),
                new JsonObject(), 
                localizado->{
                    if (localizado.succeeded()){
                        existe.handle(localizado);
                        JsonObject bitacora = new JsonObject();
                        bitacora.put("rfc",rfc); 
                        bitacora.put("curp",curp); 
                        bitacora.put("localizado", (localizado.result() != null && !localizado.result().isEmpty()));
                        bitacora.put("fechaConsulta", new SimpleDateFormat(FORMATO_ISO_8601).format(new Date()));
                        mongoClientExp.save(BITACORA_EXPUESTOS, bitacora, actualiado->{});
                    }else{
                        existe.handle(Future.failedFuture("Error en la consulta."));
                    }
                });
        return this;
    }
}
