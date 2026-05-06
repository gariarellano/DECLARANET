/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "EnteReceptorDecla" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.service.interfaces;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import java.util.HashMap;
import java.util.List;
import mx.gob.sfp.dgti.service.ServiceConsultaDecla;

/**
 * Interface que contiene os métodos para 
 * realizar logica de negocio.
 * 
 * @author cgarias
 * @since 06/11/2019
 * 
 * @edited by programador04 23/12/2019
 */
public interface ServiceConsultaDeclaInerface {
    static ServiceConsultaDeclaInerface init(Vertx vertx, WebClient webClient){   
        return new ServiceConsultaDecla(vertx, webClient);
    }
    
    public Future<JsonObject> consultaDeclaracion(JsonObject parametrosDeclaracion);
    public Future<JsonObject> consultaDeclaracionDirecciones(JsonObject parametrosDeclaracion);
    public Future<JsonObject> consultarFirmados(JsonObject parametrosDeclaracion);
    public Future<JsonObject> consultaDeclaracionSi(JsonObject parametrosDeclaracion);
    public Future<JsonObject> consultaDeclaracionFirmada(JsonObject parametrosDeclaracion);
    public Future<JsonObject> consultaDeclaracionFirmadaParaPrecarga(JsonObject parametrosDeclaracion, HashMap<Integer, JsonObject> catNivelPresupuestal);
    public Future<JsonArray> consultaHistorialUsurio(JsonObject parametros);
}
