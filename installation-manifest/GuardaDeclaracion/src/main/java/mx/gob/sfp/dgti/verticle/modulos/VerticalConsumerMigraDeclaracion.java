/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.verticle.modulos;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import java.util.Date;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.dao.DAOFirmantesInterface;
import mx.gob.sfp.dgti.dao.DAOGuardaDeclaracionInterface;
import mx.gob.sfp.dgti.dao.DAORecepcionWebInterface;
import mx.gob.sfp.dgti.util.enums.EnumTipoFirma;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ACUSE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_COLL_NAME;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_DIGESTION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_FECHA_REGISTRO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_FIRMADO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ID_FIRMANTE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_NOMBRE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_NOMBRE_FIRMANTE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_PRIMER_APELLIDO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_PUESTO_FIRMANTE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_SEGUNDO_APELLIDO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_TIPO_FIRMA;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_TITULO_FIRMANTE;
import static mx.gob.sfp.dgti.util.Constantes.GUARDADO;
import static mx.gob.sfp.dgti.util.Constantes.MESSAGE;
import static mx.gob.sfp.dgti.util.Constantes.OK;
import static mx.gob.sfp.dgti.util.Constantes._ID;
import mx.gob.sfp.dgti.util.UtileriaFunciones;
import mx.gob.sfp.dgti.utils.FechaUtil;
import static mx.gob.sfp.dgti.verticle.modulos.VerticalRegistrosExternos.VERTICAL_ENVIA_CUMPLIMIENTO;
import static mx.gob.sfp.dgti.verticle.modulos.VerticalRegistrosExternos.VERTICAL_ENVIA_DATOS_PUBLICOS;
import static mx.gob.sfp.dgti.verticle.modulos.VerticalRegistrosExternos.VERTICAL_ENVIA_PDN;

/**
 *
 * @author cgarias
 */
public class VerticalConsumerMigraDeclaracion extends AbstractVerticle{
    private static final Logger logger = Logger.getLogger(VerticalConsumerMigraDeclaracion.class.getName());
    public static final String RECEPCION_MIGRADECLARACION = EnumTipoFirma.FIEL.name()+EnumTipoFirma.FUP.name();    
    private DAOFirmantesInterface daoFirmantesInterface;
    private DAOGuardaDeclaracionInterface daoGuardaDeclaracionInterface;
    private DAORecepcionWebInterface daoRecepcionWebInterface;
    
    /**
     * Método que crea consumidores por módulo para hacer paralelo el llamado
     * de tal forma que se reduce el tiempo de ejecución
     * 
     * @param startFuture
     * @throws Exception 
     */
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        daoGuardaDeclaracionInterface = DAOGuardaDeclaracionInterface.init(vertx);
        daoRecepcionWebInterface = DAORecepcionWebInterface.init(vertx);        
        daoFirmantesInterface = DAOFirmantesInterface.init(vertx);
        
        vertx.eventBus().consumer(RECEPCION_MIGRADECLARACION, message  -> {
            JsonObject declaracionMigrar = (JsonObject) message.body();
            try{
                daoFirmantesInterface.consultaFirmanteActivo(declaracionMigrar.getJsonObject(FIELD_DIGESTION).getInteger(FIELD_COLL_NAME), firmante->{
                    if (firmante.succeeded() && firmante.result() != null){
                        JsonObject firmanteObject  = firmante.result();
                        declaracionMigrar.getJsonObject(FIELD_ACUSE)
                                .put(FIELD_ID_FIRMANTE, firmanteObject.getString(_ID))
                                .put(FIELD_TITULO_FIRMANTE,firmanteObject.getString(FIELD_TITULO_FIRMANTE))
                                .put(FIELD_NOMBRE_FIRMANTE,UtileriaFunciones.getNombreCompleto(firmanteObject.getString(FIELD_NOMBRE),firmanteObject.getString(FIELD_PRIMER_APELLIDO),firmanteObject.getString(FIELD_SEGUNDO_APELLIDO)))
                                .put(FIELD_PUESTO_FIRMANTE,firmanteObject.getString(FIELD_PUESTO_FIRMANTE));

                        if (declaracionMigrar.getString(FIELD_TIPO_FIRMA).equals(EnumTipoFirma.FUP.name())){                        
                            JsonObject jsonFup = declaracionMigrar.copy();
//                            logger.info(jsonFup.toString()); Es apra prubas y validar Objetos.
                            jsonFup.remove(FIELD_TIPO_FIRMA);
                            jsonFup.getJsonObject(FIELD_FIRMADO).put(FIELD_FECHA_REGISTRO, FechaUtil.getFechaFormatoISO8601(new Date()));                            
                            jsonFup.getJsonObject(FIELD_DIGESTION).remove(FIELD_DECLARACION);

                            vertx.eventBus().send(EnumTipoFirma.FUP.name(), jsonFup, registroFup -> {
                                if (registroFup.succeeded()){
                                    insertaRecepcionWeb(declaracionMigrar, respuestaInsRW -> {
                                        if (respuestaInsRW.succeeded() && GUARDADO.equals(respuestaInsRW.result())){
                                            message.reply(new JsonObject().put(MESSAGE, OK));
                                        }else{
                                            message.fail(1,respuestaInsRW.result());
                                        }
                                    });
                                }
                            });
                        }else{
                            insertaRecepcionWeb(declaracionMigrar, respuestaInsRW -> {
                                if (respuestaInsRW.succeeded() && GUARDADO.equals(respuestaInsRW.result())){
                                    message.reply(new JsonObject().put(MESSAGE, OK));
                                }else{
                                    message.fail(1,respuestaInsRW.result());
                                }
                            });
                        }
                    }else{
                        logger.severe(String.format("Recuperacion de firmante errornea.%n cause = %s  %n resultado = %s ", firmante.cause(), firmante.result()));
                        message.fail(1, "Recuperacion de firmante errornea.");
                    }
                });
            }catch(NullPointerException ex){
                vertx.eventBus().send("VertRollBackRececpion", declaracionMigrar);
                logger.severe(String.format("Error en %s",ex));
            }
        });
        
        ///EventBus registro de VertRollBackRecepcion.
        vertx.eventBus().consumer("VertRollBackRececpion", message  -> {
            JsonObject numDeclaracion = (JsonObject) message.body();
            daoRecepcionWebInterface.rollBack(numDeclaracion, rollBackEnd -> {
                if (rollBackEnd.succeeded())
                    logger.info("Termino el RollBack");
                else 
                    logger.info("No rtermino por errores RollBack");
            });    
        });
    }    
    
    /**
     * Método creado para reutilizacion de código.
     * 
     * @param declaracionMigrar Objeto principal que recibe la peticion principal.
     * @param resultHandler Resultado del proceso asincrono.
     * @return VerticalConsumerMigraDeclaracion funcion lamda.
     */
    private VerticalConsumerMigraDeclaracion insertaRecepcionWeb(JsonObject declaracionMigrar, Handler<AsyncResult<String>> resultHandler){
        daoRecepcionWebInterface.insertaRecepcion(declaracionMigrar, insertRecepcion -> {
            if(insertRecepcion.succeeded() && !insertRecepcion.result().isEmpty()){
                daoGuardaDeclaracionInterface.recibeDeclaracionFirmada(declaracionMigrar, migrado->{
                    if(migrado.succeeded() && GUARDADO.equals(migrado.result())){
                        //se envía el Json de Recepcion web y la declaracion
                        resultHandler.handle(migrado);
                        JsonObject recepcionYdecla = new JsonObject().put("recepcionWeb",insertRecepcion.result()).put("declaracion", declaracionMigrar.getJsonObject(FIELD_DIGESTION).getJsonObject(FIELD_DECLARACION));
                        vertx.eventBus().send(VERTICAL_ENVIA_PDN, recepcionYdecla);
                        vertx.eventBus().send(VERTICAL_ENVIA_CUMPLIMIENTO, recepcionYdecla);
                        vertx.eventBus().send(VERTICAL_ENVIA_DATOS_PUBLICOS, recepcionYdecla);
                    }else{
                        vertx.eventBus().send("VertRollBackRececpion", declaracionMigrar);
                        logger.severe(String.format("eroror al migrar: %s", migrado.cause()));
                        resultHandler.handle(Future.failedFuture(migrado.cause()));
                    }
                });
            }else{
                vertx.eventBus().send("VertRollBackRececpion", declaracionMigrar);
                logger.severe(String.format("eroror al migrar: %s", insertRecepcion.cause()));
                resultHandler.handle(Future.failedFuture(insertRecepcion.cause()));
            }
        }); 
        return this;
    }
}
