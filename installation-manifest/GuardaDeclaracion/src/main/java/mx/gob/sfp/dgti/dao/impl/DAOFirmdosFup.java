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
import mx.gob.sfp.dgti.dao.DAOFirmadosFupInterface;
import mx.gob.sfp.dgti.dao.DAOGeneric;
import static mx.gob.sfp.dgti.util.Constantes.COLLECTION_NAME_TRANSACCION_FUP;
import static mx.gob.sfp.dgti.util.Constantes.ERRO_AL_GUARADAR_EN_FUP;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_COLL_NAME;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_DIGESTION;
import static mx.gob.sfp.dgti.util.Constantes.OK;

/**
 *
 * @author cgarias
 */
public class DAOFirmdosFup extends DAOGeneric implements DAOFirmadosFupInterface{

    public DAOFirmdosFup(Vertx vertx){
        super(vertx);
    }
    
    @Override
    public DAOFirmadosFupInterface realizaGuardadoFup(JsonObject jsonFup, Handler<AsyncResult<String>> respuesta) {
        String collection = COLLECTION_NAME_TRANSACCION_FUP + jsonFup.getJsonObject(FIELD_DIGESTION).getInteger(FIELD_COLL_NAME);
        jsonFup.getJsonObject(FIELD_DIGESTION).remove(FIELD_COLL_NAME);
        
        mongoClient.save(collection, jsonFup, salvado->{
            if(salvado.succeeded()){
                respuesta.handle(Future.succeededFuture(OK));
            }else{
                respuesta.handle(Future.succeededFuture(ERRO_AL_GUARADAR_EN_FUP));
            }
        });
        return this;
    }
}
