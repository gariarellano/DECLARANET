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
import io.vertx.ext.web.client.WebClient;
import mx.gob.sfp.dgti.dao.impl.DAOConsultaDeclaracion;

/**
 * Interface que contiene la firma de los métodos 
 * para el acceso a la base de datos.
 * 
 * @author cgarias
 * @since 06/11/2019
 */
public interface DAOConsultaDeclaracionInterface {
    
    static DAOConsultaDeclaracionInterface init(Vertx vertx, WebClient webclient){
        return new DAOConsultaDeclaracion(vertx, webclient);
    }

//    public static DAOConsultaDeclaracionInterface init(Vertx vertx);

    /**
     * Método que realiza la consulta de la declaracion al serivicio de consultar declaracion.
     * 
     * @param query   Recibe el objeto declaracion todo completo.
     * @param resultHandler Objeto futuro para regresar la respuesta.
     * 
     * @return DAOConsultaDeclaracionInterface interface que regresa para retornar la respueta asincrona.
     */
    public DAOConsultaDeclaracionInterface consultaDeclaracion(JsonObject query, Handler<AsyncResult<JsonObject>> resultHandler);
    
    
    /**
     * Método que realiza la consulta de la declaracion al serivicio de consultar declaracion.
     * 
     * @param query   Recibe el objeto declaracion todo completo.
     * @param resultHandler Objeto futuro para regresar la respuesta.
     * 
     * @return DAOConsultaDeclaracionInterface interface que regresa para retornar la respueta asincrona.
     */
    public DAOConsultaDeclaracionInterface consultaDeclaracionDePrecarga(JsonObject query, Handler<AsyncResult<JsonObject>> resultHandler);
}