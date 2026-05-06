///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package mx.gob.sfp.dgti.service.impl;
//
//import io.netty.handler.codec.http.HttpResponseStatus;
//import io.vertx.core.AsyncResult;
//import io.vertx.core.CompositeFuture;
//import io.vertx.core.Future;
//import io.vertx.core.Handler;
//import io.vertx.core.Vertx;
//import io.vertx.core.json.JsonArray;
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.web.client.WebClient;
//import java.util.HashMap;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import mx.gob.sfp.dgti.dao.DAOReservadosInterface;
//import mx.gob.sfp.dgti.service.ServiceProcesaVulneradosInterface;
//import mx.gob.sfp.dgti.verticle.modulos.VerticalIdentificaVulnerados;
//import static mx.gob.sfp.dgti.verticle.modulos.VerticalIdentificaVulnerados.HST_ELS_CUMPLIMIENTO_SEARCH;
//import static mx.gob.sfp.dgti.verticle.modulos.VerticalIdentificaVulnerados.HST_ELS_DECLARACION_SEARCH;
//import static mx.gob.sfp.dgti.verticle.modulos.VerticalIdentificaVulnerados.HST_ELS_NOLOCALIZADOSD_SEARCH;
//
///**
// *
// * @author gio_j
// */
//public class ServicePocesaVulnerados implements ServiceProcesaVulneradosInterface{
// 
//    private static final Logger logger = Logger.getLogger(ServicePocesaVulnerados.class.getName());
//    private final DAOReservadosInterface daoRecervados;
//    private final WebClient client;
//    
//    private static final String queryDatosDela = "{\"query\":{\"bool\":{\"should\":["
//            + "{\"term\":{\"rfc\":\"%s\"}},"
//            + "{\"term\":{\"curp\":\"%s\"}}"
//            + "],"
//            + "\"minimum_should_match\" : 1"
//            + "}}}";
//    private static final String queryCumplimiento = "{\"query\":{\"bool\":{\"should\":["
//            + "{\"term\":{\"datosRusp.rfc\":\"%s\"}},"
//            + "{\"term\":{\"datosRusp.curp\":\"%s\"}}"
//            + "],"
//            + "\"minimum_should_match\" : 1"
//            + "}}}";
//    private static final String queryDecla = "{\"query\":{\"bool\":{\"should\":["
//            + "{\"term\":{\"declaracion.declaracion.situacionPatrimonial.datosGenerales.rfc.rfc\":\"%s\"}},"
//            + "{\"term\":{\"declaracion.declaracion.situacionPatrimonial.datosGenerales.rfc.homoClave\":\"%s\"}},"
//            + "{\"term\":{\"declaracion.declaracion.situacionPatrimonial.datosGenerales.curp\":\"%s\"}}"
//            + "],"
//            + "\"minimum_should_match\" : 2"
//            + "}}}";
//    private final HashMap<Integer, HashMap> reservados;
//    
//    public ServicePocesaVulnerados(Vertx vertx){
//        client = WebClient.create(vertx);
//        daoRecervados = DAOReservadosInterface.init(vertx);
//        this.reservados = VerticalIdentificaVulnerados.getReservados();
//    }
//    
//    
//    @Override
//    public ServiceProcesaVulneradosInterface procesarUnaPorUna(Integer collName, JsonObject objetosNoLocR, Handler<AsyncResult<String>> resultHandler) {
//        Future<JsonObject> datosDeclaF = Future.future();
//        Future<JsonObject> cumplimientoF = Future.future();
//        Future<JsonObject> declaracionF = Future.future();
//        
//        clientPost(String.format(HST_ELS_NOLOCALIZADOSD_SEARCH, collName), 
//                new JsonObject(String.format(queryDatosDela, objetosNoLocR.getJsonObject("_source").getString("rfc").toLowerCase(), objetosNoLocR.getJsonObject("_source").getString("curp").toLowerCase())), 
//                //3, 
//                respNoLo->{
//                    datosDeclaF.handle(respNoLo);
//                });
//        clientPost(String.format(HST_ELS_CUMPLIMIENTO_SEARCH, collName), 
//                new JsonObject(String.format(queryCumplimiento, objetosNoLocR.getJsonObject("_source").getString("rfc").toLowerCase(), objetosNoLocR.getJsonObject("_source").getString("curp").toLowerCase())), 
//                //3, 
//                respCump->{
//                    cumplimientoF.handle(respCump);
//                });
//        clientPost(String.format(HST_ELS_DECLARACION_SEARCH, collName), 
//                new JsonObject(String.format(queryDecla, objetosNoLocR.getJsonObject("_source").getString("rfc").substring(0,10).toLowerCase(), objetosNoLocR.getJsonObject("_source").getString("rfc").substring(10).toLowerCase(), objetosNoLocR.getJsonObject("_source").getString("curp").toLowerCase())), 
//                //3, 
//                respDeclac->{
//                    declaracionF.handle(respDeclac);                  
//                });
//        
//        CompositeFuture.all(datosDeclaF, cumplimientoF, declaracionF).setHandler(comprobante ->{
//            
//            if (comprobante.succeeded()){
//                JsonArray datosDeclaracion = new JsonArray();
//                if (declaracionF.result().getJsonObject("hits").getJsonArray("hits")!= null){
//                    for (Object index : declaracionF.result().getJsonObject("hits").getJsonArray("hits")) {
//                        if (((JsonObject)index).getJsonObject("_source").getJsonObject("declaracion").containsKey("encabezado")){
//                            datosDeclaracion.add(new JsonObject().put("id", ((JsonObject)index).getJsonObject("_source").getJsonObject("declaracion").getString("id")).put("formato", "AVISO"));
//                        }else {
//                            datosDeclaracion.add(new JsonObject().put("id", ((JsonObject)index).getJsonObject("_source").getJsonObject("declaracion").getString("id")).put("formato", ((JsonObject)index).getJsonObject("_source").getJsonObject("declaracion").getJsonObject("declaracion").containsKey("bienesMuebles") ? "COMPLETO":"SIMPLIFICADO"));
//                        }
//                    }
//                }
//
//                JsonObject localizado = new JsonObject();
//                localizado
//                        .put("_id", objetosNoLocR.getJsonObject("_source").getString("id"))
//                        .put("curp", objetosNoLocR.getJsonObject("_source").getString("curp"))
//                        .put("rfc", objetosNoLocR.getJsonObject("_source").getString("rfc"))
//                        .put("datosRusp", Boolean.TRUE)
//                        .put("datosDecla", !datosDeclaF.result().getJsonObject("hits").getJsonArray("hits").isEmpty())
//                        .put("cumplimiento", !cumplimientoF.result().getJsonObject("hits").getJsonArray("hits").isEmpty())
//                        .put("declaracion", datosDeclaracion);
//
//                if (reservados.get(collName).containsKey(objetosNoLocR.getJsonObject("_source").getString("rfc")) || 
//                        reservados.get(collName).containsKey(objetosNoLocR.getJsonObject("_source").getString("curp"))){
//                    daoRecervados.guardar("vulneradosReservados"+collName,localizado, 3, esGuardado->{
//                        if (esGuardado.succeeded()){
//                            resultHandler.handle(Future.succeededFuture("OK"));
//                        }else{
//                            logger.severe("Error al intentar insertar en vulneradosReservados");
//                            resultHandler.handle(Future.succeededFuture("ERROR_VULNERADOS_RESERVADOS"));
//                        }
//                        resultHandler.handle(Future.succeededFuture("EXITO"));
//                    });
//                }else{
//                    daoRecervados.guardar("vulnerados"+collName,localizado, 3, esGuardado->{
//                        if (esGuardado.succeeded()){
//                            resultHandler.handle(Future.succeededFuture("OK"));
//                        }else{
//                            logger.severe("Error al intentar insertar en vulnerado");
//                            resultHandler.handle(Future.succeededFuture("ERROR_VULNERADOS"));
//                        }
//                        resultHandler.handle(Future.succeededFuture("EXITO"));
//                    });
//                }
//            }else{
//                logger.log(Level.SEVERE, "fallo alguna consulta de las triples {0}", comprobante.cause());
//                resultHandler.handle(Future.succeededFuture("ERROR_3_CONSULTAS"));
//            }
//        });
//        return this;
//    }
//    
//    @Override
//    public ServiceProcesaVulneradosInterface clientPost(String url, JsonObject body, //Integer retry, 
//            Handler<AsyncResult<JsonObject>> resultHandler){
//        client.postAbs(url)
//                .timeout(240000).putHeader("Content-Type", "application/json")
//                .sendJsonObject(body, response->{
//                    if (response.succeeded() && response.result().statusCode()== HttpResponseStatus.OK.code()){
//                        resultHandler.handle(Future.succeededFuture(response.result().bodyAsJsonObject()));
//                    }else{                        
//                        if (response.succeeded() && response.result().statusCode()!= HttpResponseStatus.OK.code()){
//                            logger.severe(String.format("Error no catch status %s", response.result().statusCode()));
//                        }
//                        logger.severe(String.format("Error Cause: %s %n Error al realizar peticion curl -X POST -d '%s' %s ", response.cause(), body, url));
//                        resultHandler.handle(Future.failedFuture("Fallo cinsultaElastic"));
//                    }
//                });
//        return this;
//    }
//}
