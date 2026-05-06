/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "GuardaDeclaracion" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import mx.gob.sfp.dgti.service.impl.ServiceRegistraCumplimiento;

/**
 * Interface que contiene os métodos para 
 * realizar logica de negocio.
 * 
 * @author cgarias
 * @since 06/11/2019
 */
public interface IServiceRegistraCumplimiento {
    public static final String DEFAULT_PATH_ENTE_RECEPTOR = "VACIO";
    public static final String PROPERTY_PATH = "path";
    
    static IServiceRegistraCumplimiento init(Vertx vertx, WebClient webclient){   
        return new ServiceRegistraCumplimiento(vertx, webclient);
    }
    
    public IServiceRegistraCumplimiento validaYregistraCumplimientoDecla(JsonObject declaracion, Handler<AsyncResult<JsonObject>> andleResultHttp);
    public IServiceRegistraCumplimiento validaYregistraCumplimientoRusp(JsonObject movimientoRusp, Handler<AsyncResult<JsonObject>> response);
    
    public IServiceRegistraCumplimiento consultaObligacion(String noComprobante, String collName, Integer ejecuciones, Handler<AsyncResult<JsonObject>> andleResultHttp);
    
    public IServiceRegistraCumplimiento registrarCumplimientoManual(String idNoLocDecla, String idRusp, String collName, JsonObject body, Handler<AsyncResult<JsonObject>> handleResultHttp);
    public IServiceRegistraCumplimiento registrarExclusionManual(String idRusp, String collName, JsonObject body, Handler<AsyncResult<JsonObject>> handleResultHttp);
//    public IServiceRegistraCumplimiento revierteCumplimientoManual(String idNoLocDecla, String idRusp, String collName, JsonObject usuario, Handler<AsyncResult<JsonObject>> httpHandleResultHttp);
}
