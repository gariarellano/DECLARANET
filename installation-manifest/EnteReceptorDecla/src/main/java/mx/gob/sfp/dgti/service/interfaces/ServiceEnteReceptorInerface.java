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
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.service.ServiceEnteReceptor;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;

/**
 * Interface que contiene os métodos para 
 * realizar logica de negocio.
 * 
 * @author cgarias
 * @since 06/11/2019
 */
public interface ServiceEnteReceptorInerface {
    public static final String DEFAULT_PATH_ENTE_RECEPTOR = "VACIO";
    public static final String PROPERTY_PATH = "path";
    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_COLL_NAME = "collName";
    
    static ServiceEnteReceptorInerface init(Vertx vertx, WebClient webClient){   
        return new ServiceEnteReceptor(vertx, webClient);
    }
    
    public Future<JsonObject> consultaEnteReceptorDeclaracion(String path);
    public Future<JsonObject> consultaEnteReceptorporEnteId(String idEnte);
    public Future<JsonObject> agregaEnteReceptorDeclaracion(JsonObject enteReceptor);
    public Future<JsonObject> consultaEnteReceptorDeclaracionPorCollName(Integer collName);
    public Future<ModuloDTO> validarBody(JsonObject json);
    public Future<JsonObject> consultarServicioEnte(JsonObject json);
    public Future<JsonArray> consultarTodos(String  parametro);
    
    
    
}
