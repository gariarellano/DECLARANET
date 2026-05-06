/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.verticle.modulos;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.dao.DAOFirmadosFupInterface;
import mx.gob.sfp.dgti.util.enums.EnumTipoFirma;
import mx.gob.sfp.dgti.util.Constantes;

/**
 *
 * @author cgarias
 */
public class VerticalConsumerPocesoFup extends AbstractVerticle{
    private static final Logger logger = Logger.getLogger(VerticalConsumerPocesoFup.class.getName());
    private DAOFirmadosFupInterface daoFirmados;
    
    /**
     * Método que crea consumidores por módulo para hacer paralelo el llamado
     * de tal forma que se reduce el tiempo de ejecución
     * 
     * @param startFuture
     * @throws Exception 
     */
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        daoFirmados = DAOFirmadosFupInterface.init(vertx);
        
        vertx.eventBus().consumer(EnumTipoFirma.FUP.name(), mensaje -> {
            JsonObject jsonFup = (JsonObject) mensaje.body();            
            daoFirmados.realizaGuardadoFup(jsonFup, resultado ->{
                if (resultado.succeeded() && resultado.result().equals(Constantes.OK)){
                    logger.info("realizo guardado fup correctamente.");
                    mensaje.reply(resultado.result());                    
                }else{
                    logger.info("Error al guardar fup.");
                    mensaje.fail(1,resultado.result());
                }
            });
        });
    }
}
