/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.verticle.modulos;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.dao.impl.DAOGuardaDeclaracion;
import mx.gob.sfp.dgti.dao.impl.DAORecepcionWeb;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_COLL_NAME;
import mx.gob.sfp.dgti.utils.FechaUtil;
import static mx.gob.sfp.dgti.verticle.modulos.VerticalRegistrosExternos.VERTICAL_ENVIA_CUMPLIMIENTO;
import static mx.gob.sfp.dgti.verticle.modulos.VerticalRegistrosExternos.VERTICAL_ENVIA_DATOS_PUBLICOS;
import static mx.gob.sfp.dgti.verticle.modulos.VerticalRegistrosExternos.VERTICAL_ENVIA_PDN;

/**
 * Vertical que se encargara de hacer el reporte de las declaraciones falantes
 * a cumplimineto, esto por periodo el cual se le hara por dia.
 * @author cgarias
 * @since 
 */
public class VerticalCumplimiento extends AbstractVerticle {
    private static final Logger logger = Logger.getLogger(VerticalCumplimiento.class.getName());
    public static final String VERTICLE_REPORTA_DATOS_POR_PERIODO = "VERTICLE_REPORTA_DATOS_POR_PERIODO";
    public static final String VERTICLE_REPORTA_POR_NUM_CUMPLIMIENTO = "VERTICLE_REPORTA_POR_NUM_CUMPLIMIENTO";
    private DAOGuardaDeclaracion daoGuardaDeclaracion;
    private DAORecepcionWeb daoRecepcionWeb;
    private SimpleDateFormat format;
    
    @Override
    public void init(Vertx vertx, Context context){
        super.init(vertx, context);
        daoGuardaDeclaracion = new DAOGuardaDeclaracion(vertx);
        daoRecepcionWeb = new DAORecepcionWeb(vertx);
        format = new SimpleDateFormat("yyyy-MM-dd");
    }
    
    
    @Override
    public void start(Future<Void> future) throws Exception {
        
        vertx.eventBus().consumer(VERTICLE_REPORTA_DATOS_POR_PERIODO, message->{  
            Calendar calendar = Calendar.getInstance();
            JsonObject periodoReportar = (JsonObject) message.body();            
            
            try {
                String fechaEvaluacion = periodoReportar.getString("fechaInicio");
                if (FechaUtil.fechaMenorQue(fechaEvaluacion, periodoReportar.getString("fechaFin"))){
                    calendar.setTime(format.parse(fechaEvaluacion));
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    String fechaMasUno = format.format(calendar.getTime());

                    if (FechaUtil.fechaMenorQue(fechaEvaluacion, periodoReportar.getString("fechaFin"))){
                        JsonObject otroPreodoEnviar = periodoReportar.copy();
                        otroPreodoEnviar.put("fechaInicio", fechaMasUno);
                        vertx.eventBus().send(VERTICLE_REPORTA_DATOS_POR_PERIODO, otroPreodoEnviar);
                    }

                    periodoReportar.put("fechaFin", fechaMasUno);

                    daoRecepcionWeb.consultaDeclaracionesPeriodo(periodoReportar, resultado->{
                        if (resultado.succeeded()){
                            Integer indiceTotal = resultado.result().size();
                            logger.info(String.format("Periodo a ejecutar %s a %s  total a enviar.. = %d", fechaEvaluacion, fechaMasUno, indiceTotal));                        
                            JsonArray listaRecDecla =  resultado.result();
                            if (!listaRecDecla.isEmpty()){
                                JsonObject indexJsO = listaRecDecla.getJsonObject(indiceTotal-1);
                                indexJsO.put(FIELD_COLL_NAME,periodoReportar.getInteger(FIELD_COLL_NAME));
                                realizaEnvio(listaRecDecla, indiceTotal-1, indexJsO);
                            }
                        }else{
                            logger.severe(String.format("Error al obtener listado de envios VERTICLE_REPORTA_DATOS_POR_PERIODO %s", resultado.cause()));
                        }
                    });
                }
            }catch (ParseException ex) {
                logger.severe("error en parceos");
                logger.log(Level.SEVERE, "Error", ex);
            }
        });
        
        vertx.eventBus().consumer(VERTICLE_REPORTA_POR_NUM_CUMPLIMIENTO, message->{  
            JsonObject cumplimiento = (JsonObject) message.body();            
            daoRecepcionWeb.consultaDeclaracionesPeriodo(cumplimiento, resultado->{
                        if (resultado.succeeded()){
                            JsonArray listaRecDecla =  resultado.result();
                            if (!listaRecDecla.isEmpty()){
                                JsonObject indexJsO = listaRecDecla.getJsonObject(0);
                                logger.info(String.format(">>>> A PROCESAR EN LISTA %d",listaRecDecla.size()));
                                indexJsO.put(FIELD_COLL_NAME,cumplimiento.getInteger(FIELD_COLL_NAME));
                                realizaEnvio(listaRecDecla, -1, indexJsO);
                            }
                        }else{
                            logger.severe(String.format("Error al obtener listado de envios VERTICLE_REPORTA_DATOS_POR_PERIODO %s", resultado.cause()));
                        }
                    });
        });
    }
    
   
    
    private void  realizaEnvio(JsonArray listaRecDecla, Integer indice, JsonObject indexJsO){
        JsonObject jsonParaEnviar = new JsonObject();        
        daoGuardaDeclaracion.obtenDeclaracin(indexJsO, declaracion ->{
            if (declaracion.succeeded()){
                jsonParaEnviar.put("declaracion", declaracion.result());
                daoRecepcionWeb.consultaRecepcionWeb(indexJsO, recepcionWeb->{
                    if (recepcionWeb.succeeded()){
                        jsonParaEnviar.put("recepcionWeb",recepcionWeb.result());
                        
                        Future respuestaPdn = Future.future(envio->{
                            vertx.eventBus().send(VERTICAL_ENVIA_PDN, jsonParaEnviar, respuesta->{
                                if (respuesta.succeeded() || respuesta.failed()){
                                    
                                    //recursividad para enviar una por una.
                                    envio.handle(Future.succeededFuture());
                                }
                            });
                        });
                        Future respuestaCump = Future.future(envio->{
                            vertx.eventBus().send(VERTICAL_ENVIA_CUMPLIMIENTO, jsonParaEnviar, respuesta->{
                                if (respuesta.succeeded() || respuesta.failed()){
                                    //recursividad para enviar una por una.
                                    envio.handle(Future.succeededFuture());
                                }
                            });
                        });
                        Future respuestaDatosPub = Future.future(envio->{
                            vertx.eventBus().send(VERTICAL_ENVIA_DATOS_PUBLICOS, jsonParaEnviar, respuesta->{
                                if (respuesta.succeeded() || respuesta.failed()){
                                    envio.handle(Future.succeededFuture());
                                }
                            });
                        });
                        
                        logger.info(String.format("Enviada a PDN ---> RW = %s  de dec = %s", indexJsO.getString("_id"), indexJsO.getJsonObject("declaracion").getString("numeroDeclaracion")));
                        CompositeFuture.all(respuestaPdn, respuestaCump, respuestaDatosPub).setHandler(termino->{
                            if (indice >= 0 ){
                                JsonObject indexJsO1 = listaRecDecla.getJsonObject(indice);
                                indexJsO1.put(FIELD_COLL_NAME, indexJsO.getInteger(FIELD_COLL_NAME));
                                vertx.setTimer(1000, idAsignado->{
                                    realizaEnvio(listaRecDecla, indice-1, indexJsO1);
                                });
                            }  
                        });
                    }else{
                        logger.severe(String.format("Error al recuperar la info de recepcionWeb %s %s", recepcionWeb.cause(), indexJsO.toString()));
                        if (indice >= 0 ){
                            JsonObject indexJsO1 = listaRecDecla.getJsonObject(indice);
                            indexJsO1.put(FIELD_COLL_NAME, indexJsO.getInteger(FIELD_COLL_NAME));
                            vertx.setTimer(1000, idAsignado->{
                                realizaEnvio(listaRecDecla, indice-1, indexJsO1);
                            });
                        }  
                    }                    
                });
            }else{
                logger.severe(String.format("Error al recuperar la info de declaracion %s %s", declaracion.cause(), indexJsO.toString()));    
                if (indice >= 0 ){
                    JsonObject indexJsO1 = listaRecDecla.getJsonObject(indice);
                    indexJsO1.put(FIELD_COLL_NAME, indexJsO.getInteger(FIELD_COLL_NAME));
                    vertx.setTimer(1000, idAsignado->{
                        realizaEnvio(listaRecDecla, indice-1, indexJsO1);
                    });
                }        
            }
        });
    }
}
