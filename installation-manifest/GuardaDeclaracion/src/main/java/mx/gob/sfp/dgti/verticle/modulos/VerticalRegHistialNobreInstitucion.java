/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.verticle.modulos;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.dao.DAODatosPublicosInterface;
import mx.gob.sfp.dgti.dao.DAORecepcionWebInterface;

/**
 *
 * @author cgarias
 */
public class VerticalRegHistialNobreInstitucion extends AbstractVerticle{
    private static final Logger logger = Logger.getLogger(VerticalRegHistialNobreInstitucion.class.getName());
    public static final String REG_HIST_NOMBRE_INSTITUCION = "REG_HIST_NOMBRE_INSTITUCION";
    public  static final String PORCESO_UNICO = "PORCESO_UNICO";
    private DAORecepcionWebInterface daoRecepcionWebInterfae;
    private DAODatosPublicosInterface daoDatosPublicosInterface;
    
    @Override
    public void init(Vertx vertx, Context context){
        super.init(vertx, context);
        daoRecepcionWebInterfae = DAORecepcionWebInterface.init(vertx);
        daoDatosPublicosInterface = DAODatosPublicosInterface.init(vertx);
    }
    
    @Override
    public void start(Future<Void> future) throws Exception {
        vertx.eventBus().consumer(REG_HIST_NOMBRE_INSTITUCION, message ->{
            //Ejemplo recibe {"collName":###, "declarante":{}}
            Integer collName = ((JsonObject)message.body()).getInteger("collName");
            JsonObject declarante = ((JsonObject)message.body()).getJsonObject("declarante");
            daoDatosPublicosInterface.registraDatosPublicos(declarante, collName, termino->{
                message.reply("OK");
            });
        });
        
        vertx.eventBus().consumer(PORCESO_UNICO , message ->{//se crea para poder realizar la collecion por primera vez
//            daoRecepcionWebInterfae.localizaPrimerMongoId(((JsonObject)message.body()).getInteger("collName"), resultado->{
//                if (resultado.result() != null){
            realizaRecorrido("0", ((JsonObject)message.body()).getInteger("collName"));
//                }else{
//                    logger.severe("termino proceso por error.");
//                }
//            });
            message.reply("ejecutando");
        });
    }
    
    
    private void realizaRecorrido(String posicion, Integer collName){        
        daoRecepcionWebInterfae.recorreRecepcionWeb(posicion, collName, respuesta->{
            if (respuesta.succeeded()){
                if (respuesta.result() != null && !respuesta.result().isEmpty()){
                    JsonObject objetoABuscarEInsertar = respuesta.result().getJsonObject("declarante");
                    if (objetoABuscarEInsertar != null && objetoABuscarEInsertar.getInteger("idUsrDecnet")!= null){
                        if (objetoABuscarEInsertar.containsKey("dependencia")){
                            vertx.eventBus().send(REG_HIST_NOMBRE_INSTITUCION, 
                                    new JsonObject().put("collName", collName).put("declarante",objetoABuscarEInsertar), 
                                    termino->{
                                this.realizaRecorrido(respuesta.result().getString("_id"), collName);
                            });
                        }else if (objetoABuscarEInsertar.containsKey("enteCargoComision")){
                            if (objetoABuscarEInsertar.getValue("enteCargoComision") instanceof JsonArray){
                                this.recorreDependencias(
                                        objetoABuscarEInsertar,
                                        objetoABuscarEInsertar.getJsonArray("enteCargoComision"), 
                                        objetoABuscarEInsertar.getJsonArray("enteCargoComision").size()-1, 
                                        collName, 
                                        termino->{
                                            this.realizaRecorrido(respuesta.result().getString("_id"), collName);
                                        });
                            }else if (objetoABuscarEInsertar.getValue("enteCargoComision") instanceof JsonObject){
                                objetoABuscarEInsertar.put("dependencia", objetoABuscarEInsertar.getJsonObject("enteCargoComision").getString("dependencia"));
                                objetoABuscarEInsertar.put("idDependencia", objetoABuscarEInsertar.getJsonObject("enteCargoComision").getValue("id"));
                                objetoABuscarEInsertar.remove("enteCargoComision");
                                vertx.eventBus().send(REG_HIST_NOMBRE_INSTITUCION, 
                                    new JsonObject().put("collName", collName).put("declarante",objetoABuscarEInsertar), 
                                    termino->{
                                    this.realizaRecorrido(respuesta.result().getString("_id"), collName);
                                });
                            }else{
                                logger.info(String.format("no se localizo enteCargoComision ni como arreglo ni como Objeto: %s", objetoABuscarEInsertar));
                                this.realizaRecorrido(respuesta.result().getString("_id"), collName);
                            }
                        }else{
                            logger.info(String.format("no se localizo, ni dependencia, ni enteCargoComision en: %s", objetoABuscarEInsertar));
                            this.realizaRecorrido(respuesta.result().getString("_id"), collName);
                        }
                    }else{
                        this.realizaRecorrido(respuesta.result().getString("_id"), collName);
                    }
                }else{
                    logger.info("Termino el proceso de guardado.");
                }
            }else{
                logger.severe(String.format("Error en la lectura de la posicion %s", posicion));
            }
        });
    }
    
    private VerticalRegHistialNobreInstitucion recorreDependencias(JsonObject declarante, JsonArray dependnecias, Integer posicion, Integer collName, Handler<AsyncResult<String>> respuesta){
        if (posicion >= 0){
            declarante.remove("enteCargoComision");
            declarante.put("dependencia", dependnecias.getJsonObject(posicion).getString("dependencia"));
            declarante.put("idDependencia", dependnecias.getJsonObject(posicion).getValue("id"));
            vertx.eventBus().send(REG_HIST_NOMBRE_INSTITUCION, 
                    new JsonObject().put("collName", collName).put("declarante", declarante), 
                    termino->{
                recorreDependencias(declarante, dependnecias, posicion-1, collName, respuesta);
            });
        }else{
            respuesta.handle(Future.succeededFuture("OK"));
        }
        return this;
    }
}

