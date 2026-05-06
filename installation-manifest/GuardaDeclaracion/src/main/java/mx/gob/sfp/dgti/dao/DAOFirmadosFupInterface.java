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
import mx.gob.sfp.dgti.dao.impl.DAOFirmdosFup;

/**
 * interface que contiene los métodos de accesoa la collection de FUP.
 * @author cgarias
 */
public interface DAOFirmadosFupInterface {
    
    static DAOFirmadosFupInterface init(Vertx vertx){
        return new DAOFirmdosFup(vertx);
    }
    
    /**
     * Método que realiza el guardado de una transaccion Fup de declaranet.
     * @param firmadosDeclaracion Objeto que contiene los valores de firmado y la declaración.
     * @param resultHandler
     * 
     * @return DAOGuardaDeclaracionInterface interface que regresa para retornar la respueta asincrona.
     */
    public DAOFirmadosFupInterface realizaGuardadoFup(JsonObject firmadosDeclaracion, Handler<AsyncResult<String>> resultHandler);
}
