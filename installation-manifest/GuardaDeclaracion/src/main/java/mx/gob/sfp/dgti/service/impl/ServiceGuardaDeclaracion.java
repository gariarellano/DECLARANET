/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "GuardaDeclaracion" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.service.impl;


import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.dao.DAOGuardaDeclaracionInterface;
import mx.gob.sfp.dgti.service.ServiceGuardaDeclaracionInterface;
import mx.gob.sfp.dgti.util.IExecutJsonUtils;

/**
 * Clase que contien la implementaion de los metodos de logica de negocio de 
 * entesd receptor de informacion.
 * 
 * @author cgarias
 * @since 06/11/2019
 */
public class ServiceGuardaDeclaracion implements ServiceGuardaDeclaracionInterface{
    private static final Logger logger = Logger.getLogger(ServiceGuardaDeclaracion.class.getName());
    private final DAOGuardaDeclaracionInterface daoGuardaDeclaracionInterface;
    public ServiceGuardaDeclaracion(Vertx vertx){        
        daoGuardaDeclaracionInterface = DAOGuardaDeclaracionInterface.init(vertx);
    }
    
    @Override
    public Future<JsonObject> guardaDeclaracion(JsonObject origDecla) {  
        Future<JsonObject> respuestaConIds = Future.future();
        daoGuardaDeclaracionInterface.validaEsNuevaOigual(origDecla, resultHandler->{
            if(resultHandler.result() == null){
                IExecutJsonUtils.excutGeneraIds(origDecla, origDecla.fieldNames().iterator());
                daoGuardaDeclaracionInterface.guardaDeclaracion(origDecla, daoResponseGuardado->{
                    if(daoResponseGuardado.succeeded()){                                
                        respuestaConIds.handle(Future.succeededFuture(daoResponseGuardado.result()));
                    }else{
                        respuestaConIds.fail(daoResponseGuardado.cause());
                    }
                });
            }else{
                respuestaConIds.handle(Future.succeededFuture(resultHandler.result()));
            }
        });
        
        return respuestaConIds;
    }
}