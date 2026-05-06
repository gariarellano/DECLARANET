/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "GuardaDeclaracion" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.dao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.dao.impl.DAOGuardaDeclaracion;

/**
 * Interface que contiene la firma de los métodos 
 * para el acceso a la base de datos.
 * 
 * @author cgarias
 * @since 06/11/2019
 */
public interface DAOGuardaDeclaracionInterface {
    
    static DAOGuardaDeclaracionInterface init(Vertx vertx){
        return new DAOGuardaDeclaracion(vertx);
    }

    /**
     * Método que realiza la validacion de una existencia de la declaración, para no permitir realizar una inserción.
     * 
     * @param declaracion   Recibe el objeto declaracion todo completo.
     * @param resultHandler Objeto futuro para regresar la respuesta.
     * 
     * @return DAOGuardaDeclaracionInterface interface que regresa para retornar la respueta asincrona.
     */
    public DAOGuardaDeclaracionInterface validaEsNuevaOigual(JsonObject declaracion, Handler<AsyncResult<JsonObject>> resultHandler);
    
    /**
     * Método que realiza el guardado de la declaracion.
     * 
     * @param declaracion   Recibe el objeto declaracion todo completo.
     * @param resultHandler Objeto futuro para regresar la respuesta.
     * 
     * @return DAOGuardaDeclaracionInterface interface que regresa para retornar la respueta asincrona.
     */
    public DAOGuardaDeclaracionInterface guardaDeclaracion(JsonObject declaracion, Handler<AsyncResult<JsonObject>> resultHandler);
    
    
    public DAOGuardaDeclaracionInterface recibeDeclaracionFirmada(JsonObject declaracionMigrar, Handler<AsyncResult<String>> resultHandler);
    
    public DAOGuardaDeclaracionInterface obtenDeclaracin(JsonObject idDeclaracion, Handler<AsyncResult<JsonObject>> resultHandler);
}