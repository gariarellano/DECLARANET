/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "GuardaDeclaracion" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.service;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.service.impl.ServiceGuardaDeclaracion;

/**
 * Interface que contiene os métodos para 
 * realizar logica de negocio.
 * 
 * @author cgarias
 * @since 06/11/2019
 */
public interface ServiceGuardaDeclaracionInterface {
    public static final String DEFAULT_PATH_ENTE_RECEPTOR = "VACIO";
    public static final String PROPERTY_PATH = "path";
    
    static ServiceGuardaDeclaracionInterface init(Vertx vertx){   
        return new ServiceGuardaDeclaracion(vertx);
    }
    
    public Future<JsonObject> guardaDeclaracion(JsonObject declaracion);
}
