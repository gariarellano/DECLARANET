/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "GuardaDeclaracion" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.dao.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.dao.DAOConsultaDeclaracionInterface;


/**
 * Clase que contiene la implementacion de los 
 * métodos para la interaccion con la base
 * y consulta de informacion de entes receptores 
 * de información.
 * 
 * @author cgarias
 * @since 06/11/2019
 */
public class DAOConsultaDeclaracion implements DAOConsultaDeclaracionInterface{
    private static final Logger logger = Logger.getLogger(DAOConsultaDeclaracion.class.getName());
    private final WebClient client;
    public static final String NUM_DECLARACION = "numDeclaracion";
    private static final String DOM_CONSULTA_DECLA = "DOM_CONSULTA_DECLA";
    private static final String DOM_CONSULTA_DECLA_VAUE = System.getenv(DOM_CONSULTA_DECLA) != null ? System.getenv(DOM_CONSULTA_DECLA) : "https://dnet-consultadeclaracion-staging.dkla8s.funcionpublica.gob.mx/api/";
    private static final String METHOD_CONSULTA_DECLARACION = DOM_CONSULTA_DECLA_VAUE + "consulta-declaracion/";
    private static final String METHOD_CONSULTA_DECLARACION_PRECARGA = DOM_CONSULTA_DECLA_VAUE  + "consulta-declaracion-pecarga/";
    
    public DAOConsultaDeclaracion(Vertx vertx, WebClient webClient){
        client = webClient;
    }
    
    @Override
    public DAOConsultaDeclaracionInterface consultaDeclaracion(JsonObject query, Handler<AsyncResult<JsonObject>> resultHandler) {
        client.postAbs(METHOD_CONSULTA_DECLARACION)
                .timeout(5000)
                .sendJson(
                        query, 
                        respuesta ->{
                    if (respuesta.succeeded() && HttpResponseStatus.OK.code() == respuesta.result().statusCode()){
                        resultHandler.handle(Future.succeededFuture(respuesta.result().bodyAsJsonObject()));
                    }else{
                        resultHandler.handle(Future.succeededFuture(null));
                        logger.severe(String.format("Eror al cunsultar el declaracion query: %n curl -X POST -d ' %s ' %s", query, METHOD_CONSULTA_DECLARACION));
                    }
                });
        return this;
    }

    @Override
    public DAOConsultaDeclaracionInterface consultaDeclaracionDePrecarga(JsonObject query, Handler<AsyncResult<JsonObject>> resultHandler) {
        client.postAbs(METHOD_CONSULTA_DECLARACION_PRECARGA)
                .timeout(7000)
                .sendJson(
                        query, 
                        respuesta ->{
                    if (respuesta.succeeded()){
                        resultHandler.handle(Future.succeededFuture(respuesta.result().bodyAsJsonObject()));
                    }else{
                        resultHandler.handle(Future.succeededFuture(null));
                        logger.severe(String.format("Eror al cunsultar el declaracion query: %n curl -X POST -d ' %s ' %s", query, METHOD_CONSULTA_DECLARACION_PRECARGA));
                    }
                });
        return this;
    }
}