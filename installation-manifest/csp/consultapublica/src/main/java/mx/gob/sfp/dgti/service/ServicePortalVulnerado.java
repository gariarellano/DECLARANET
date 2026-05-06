/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.service;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.dto.http.response.JsonObjectHttpResponse;
import mx.gob.sfp.dgti.dto.http.response.JsonObjectRespone;

/**
 *
 * @author cgarias
 */
public class ServicePortalVulnerado implements ServicePortalVulneradosInterface{
    
    private final WebClient client;
    private static final Logger logger = Logger.getLogger(ServicePortalVulnerado.class.getName());
    private static final String HOST_ELASTIC = System.getenv("ELASTIC_HOST");//ejemplo http://172.29.50.69:9201/%s/%s
    private static final String HST_ELS_DECLARACION_SEARCH = String.format(HOST_ELASTIC, "declaracion%d","_search");
    private static final String HST_ELS_NOLOCALIZADOSD_SEARCH = String.format(HOST_ELASTIC, "nolocalizadosd%d","_search");
    private static final String HST_ELS_NOLOCALIZADOSR_SEARCH = String.format(HOST_ELASTIC, "nolocalizadosr%d","_search");
    public static final String HST_ELS_CUMPLIMIENTO_SEARCH = String.format(HOST_ELASTIC, "cumplimiento%d","_search");
    private static final String queryDatosDela = "{\"query\":{\"bool\":{\"should\":["
            + "{\"term\":{\"rfc\":\"%s\"}},"
            + "{\"term\":{\"curp\":\"%s\"}}"
            + "],"
            + "\"minimum_should_match\" : 1"
            + "}}}";
    private static final String queryDatosRusp = "{\"query\":{\"bool\":{\"should\":["
            + "{\"term\":{\"rfc\":\"%s\"}},"
            + "{\"term\":{\"curp\":\"%s\"}}"
            + "],"
            + "\"minimum_should_match\" : 1"
            + "}}}";
    private static final String queryCumplimiento = "{\"query\":{\"bool\":{\"should\":["
            + "{\"term\":{\"datosRusp.rfc\":\"%s\"}},"
            + "{\"term\":{\"datosRusp.curp\":\"%s\"}}"
            + "],"
            + "\"minimum_should_match\" : 1"
            + "}}}";
    private static final String queryDecla = "{\"query\":{\"bool\":{\"should\":"
            + "["
            + "{\"bool\":{\"should\":["
            + "{\"term\":{\"declaracion.declaracion.situacionPatrimonial.datosGenerales.rfc.rfc\": \"%s\"}},"
            + "{\"term\":{\"declaracion.declaracion.situacionPatrimonial.datosGenerales.rfc.homoClave\": \"%s\"}},"
            + "{\"term\": {\"declaracion.declaracion.situacionPatrimonial.datosGenerales.curp\": \"%s\"}}],"
            + "\"minimum_should_match\": 2}},"
            + "{\"bool\":{\"should\":["
            + "{\"term\":{\"declaracion.declaracion.datosGenerales.datosPersonales.rfc\": \"%s\"}},"
            + "{\"term\":{\"declaracion.declaracion.datosGenerales.datosPersonales.curp\":\"%s\"}}],"
            + "\"minimum_should_match\": 1}}"
            + "]}}}";
    private static final String queryByName = "{\"query\":{\"query_string\":"
            + "{\"query\":\"%s\",\"default_operator\":\"AND\"}}}";
    private static final String HITS = "hits";
    private static final String SOURCE = "_source";
    private static final String DECLARACION = "declaracion";
    private static final String DATOS_GENERALES = "datosGenerales";
    private static final String DATOS_PERSONALES = "datosPersonales";
    private static final String CURP = "curp";
    private static final String RFC = "rfc";
    private static final String NOMBRES = "nombres";
    private static final String NOMBRE = "nombre";
    private static final String PRIMER_APELLIDO = "primerApellido";
    private static final String SEGUNDO_APELLIDO = "segundoApellido";
    private static final String DATOS_RUSP = "datosRusp";
    private static final String DATOS_DECLA = "datosDecla";
    private static final String CUMPLIMIENTO = "cumplimiento";
    
    
    public ServicePortalVulnerado(Vertx vertx ){
        client = WebClient.create(vertx);
    }
    
    @Override
    public ServicePortalVulneradosInterface consultaDatosExpuestos(Integer collName, String curp, String rfc, Handler<AsyncResult<JsonObject>> respuesta) {
        
//        localizaDatos(collName, rfc, curp, consultado->{
//            if (consultado.succeeded()){
//                if (Server.getAdmins().containsKey(rfc) || Server.getAdmins().containsKey(curp))
//                    consultado.result().put("buscaDatos", Boolean.TRUE);
//                            
//                respuesta.handle(
//                        Future.succeededFuture(
//                                new JsonObjectHttpResponse(
//                                        HttpResponseStatus.OK.code(), 
//                                        new JsonObjectRespone("OK", consultado.result()))
//                        )
//                );
//            }else{
//                respuesta.handle(
//                        Future.succeededFuture(
//                                new JsonObjectHttpResponse(
//                                        HttpResponseStatus.CONFLICT.code(), 
//                                        new JsonObjectRespone("ERROR", "Ocurrio un error al realizar la consulta de datos."))
//                        )
//                );
//            }
//        });
        
        return this;
    }

    @Override
    public ServicePortalVulneradosInterface consultaDatosExpuestosAdmin(Integer collName, String parametro, Handler<AsyncResult<JsonObject>> respuesta) {
//        HashMap<Integer, HashMap> reservados = ServiceConsultaPublicaImpl.getReservados();
        if (parametro.contains(" ")){
            localizaDatosPorNombre(collName, parametro, consultado->{
                respuesta.handle(
                        Future.succeededFuture(
                                new JsonObjectHttpResponse(
                                        HttpResponseStatus.OK.code(), new JsonObjectRespone("OK", consultado.result())
                                )
                        )
                );
            });    
        }else{
            localizaDatos(collName, parametro, parametro, consultado->{
//                if (consultado.succeeded()){
//                    if (reservados.get(collName).containsKey(parametro) || 
//                                reservados.get(collName).containsKey(parametro)){
//                        respuesta.handle(
//                                Future.succeededFuture(
//                                        new JsonObjectHttpResponse(
//                                                HttpResponseStatus.OK.code(), 
//                                                new JsonObjectRespone("OK", consultado.result().put("reservado", Boolean.TRUE)))
//                                )
//                        );
//                    }else{
//                        respuesta.handle(
//                                Future.succeededFuture(
//                                        new JsonObjectHttpResponse(
//                                                HttpResponseStatus.OK.code(), 
//                                                new JsonObjectRespone("OK", consultado.result().put("reservado", Boolean.FALSE)))
//                                )
//                        );
//                    }
//                }else{
//                    respuesta.handle(
//                            Future.succeededFuture(
//                                    new JsonObjectHttpResponse(
//                                            HttpResponseStatus.CONFLICT.code(), 
//                                            new JsonObjectRespone("ERROR", "Ocurrio un error al realizar la consulta de datos."))
//                            )
//                    );
//                }
            });
        }
        return this;
    }
    
    private ServicePortalVulneradosInterface localizaDatosPorNombre(Integer collName, String nombre, Handler<AsyncResult<List<JsonObject>>> respuesta){
        StringBuilder nombreToElastic = new StringBuilder("*");
        for (String index :nombre.split(" ")){
            nombreToElastic.append(index).append("* *");
        }
        nombreToElastic.delete(nombreToElastic.length()-2, nombreToElastic.length());
        
        Future<List<JsonObject>> noloalizadosrF = Future.future();
        Future<List<JsonObject>> cumplimientoF = Future.future();
        Future<List<JsonObject>> declaracionF = Future.future();
        
        clientPost(String.format(HST_ELS_NOLOCALIZADOSR_SEARCH, collName), 
                new JsonObject(String.format(queryByName, nombreToElastic)), 
                //1, 
                respNoLoR->{
                    List<JsonObject> datos = new ArrayList<>();
                    if (respNoLoR.result().getJsonObject(HITS).getJsonArray(HITS)!= null){
                        for (Object index : respNoLoR.result().getJsonObject(HITS).getJsonArray(HITS)) {
                            StringBuilder nombreCompleto= new StringBuilder();
                            nombreCompleto.append(((JsonObject)index).getJsonObject(SOURCE).getString(NOMBRES)).append(" ")
                                .append(((JsonObject)index).getJsonObject(SOURCE).getString(PRIMER_APELLIDO));
                            if (((JsonObject)index).getJsonObject(SOURCE).getString(SEGUNDO_APELLIDO) != null){
                                nombreCompleto.append(" ").append(((JsonObject)index).getJsonObject(SOURCE).getString(SEGUNDO_APELLIDO));
                            }
                            datos.add(new JsonObject().put("localizadoEn", DATOS_RUSP).put("nombre", nombreCompleto.toString()).put("curp", ((JsonObject)index).getJsonObject(SOURCE).getString("curp")));
                        }
                    }
                    noloalizadosrF.handle(Future.succeededFuture(datos));
                });
        clientPost(String.format(HST_ELS_CUMPLIMIENTO_SEARCH, collName), 
                new JsonObject(String.format(queryByName, nombreToElastic)), 
                //2, 
                respCump->{
                    List<JsonObject> datos = new ArrayList<>();
                    if (respCump.result().getJsonObject(HITS).getJsonArray(HITS)!= null){
                        for (Object index : respCump.result().getJsonObject(HITS).getJsonArray(HITS)) {
                            StringBuilder nombreCompleto= new StringBuilder();
                            nombreCompleto.append(((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DATOS_RUSP).getString(NOMBRES)).append(" ")
                                .append(((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DATOS_RUSP).getString(PRIMER_APELLIDO));
                            if (((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DATOS_RUSP).getString(SEGUNDO_APELLIDO) != null){
                                nombreCompleto.append(" ").append(((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DATOS_RUSP).getString(SEGUNDO_APELLIDO));
                            }
                            datos.add(new JsonObject().put("localizadoEn", CUMPLIMIENTO).put("nombre", nombreCompleto.toString()).put("curp", ((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DATOS_RUSP).getString("curp")));
                        }
                    }
                    cumplimientoF.handle(Future.succeededFuture(datos));                    
                });
        clientPost(String.format(HST_ELS_DECLARACION_SEARCH, collName), 
                new JsonObject(String.format(queryByName, nombreToElastic)), 
                //3, 
                respDeclac->{
                    List<JsonObject> datos = new ArrayList<>();
                    if (respDeclac.result().getJsonObject(HITS).getJsonArray(HITS)!= null){
                        for (Object index : respDeclac.result().getJsonObject(HITS).getJsonArray(HITS)) {
                            StringBuilder nombreCompleto= new StringBuilder();
                            if (((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).containsKey("encabezado")){
                                nombreCompleto.append(((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject(DATOS_GENERALES).getJsonObject(DATOS_PERSONALES).getString(NOMBRE)).append(" ")
                                        .append(((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject(DATOS_GENERALES).getJsonObject(DATOS_PERSONALES).getString(PRIMER_APELLIDO));
                                if (((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject(DATOS_GENERALES).getJsonObject(DATOS_PERSONALES).getString(SEGUNDO_APELLIDO) != null){
                                    nombreCompleto.append(" ").append(((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject(DATOS_GENERALES).getJsonObject(DATOS_PERSONALES).getString(SEGUNDO_APELLIDO));
                                }
                                datos.add(new JsonObject().put("localizadoEn", DATOS_DECLA).put("nombre",nombreCompleto.toString()).put("curp", ((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject(DATOS_GENERALES).getJsonObject(DATOS_PERSONALES).getString("curp")));
                            }else {                                
                                if (nombreCompleto.toString().isEmpty()){
                                    nombreCompleto.append(((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject("situacionPatrimonial").getJsonObject(DATOS_GENERALES).getString(NOMBRE)).append(" ")
                                            .append(((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject("situacionPatrimonial").getJsonObject(DATOS_GENERALES).getString(PRIMER_APELLIDO));
                                    if (((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject("situacionPatrimonial").getJsonObject(DATOS_GENERALES).getString(SEGUNDO_APELLIDO) != null){
                                        nombreCompleto.append(" ").append(((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject("situacionPatrimonial").getJsonObject(DATOS_GENERALES).getString(SEGUNDO_APELLIDO));
                                    }
                                }
                                datos.add(new JsonObject().put("localizadoEn", DATOS_DECLA).put("nombre",nombreCompleto.toString()).put("curp", ((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject("situacionPatrimonial").getJsonObject(DATOS_GENERALES).getString("curp")));
                            }
                        }
                    }
                    declaracionF.handle(Future.succeededFuture(datos));
                });
        
         
        CompositeFuture.all(noloalizadosrF, cumplimientoF, declaracionF).setHandler(comprobante ->{
            if (comprobante.succeeded()){
                List<JsonObject> resultado = noloalizadosrF.result();
                resultado.addAll(cumplimientoF.result());
                resultado.addAll(declaracionF.result());
                respuesta.handle(Future.succeededFuture(resultado));
            }else{
                logger.log(Level.SEVERE, "fallo alguna consulta de las 3 fallo {0}", comprobante.cause());
                respuesta.handle(Future.failedFuture("ERROR_3_CONSULTAS"));
            }
        });

        return this;
    }

    private ServicePortalVulneradosInterface localizaDatos(Integer collName, String rfc, String curp, Handler<AsyncResult<JsonObject>> respuesta) {
        Future<JsonObject> noloalizadosdF = Future.future();
        Future<JsonObject> noloalizadosrF = Future.future();
        Future<JsonObject> cumplimientoF = Future.future();
        Future<JsonObject> declaracionF = Future.future();
        
        clientPost(String.format(HST_ELS_NOLOCALIZADOSD_SEARCH, collName), 
                new JsonObject(String.format(queryDatosDela, rfc.toLowerCase(), curp.toLowerCase())), 
                //1, 
                respNoLoD->{
                    noloalizadosdF.handle(respNoLoD);
                });
        clientPost(String.format(HST_ELS_NOLOCALIZADOSR_SEARCH, collName), 
                new JsonObject(String.format(queryDatosRusp, rfc.toLowerCase(), curp.toLowerCase())), 
                //2, 
                respNoLoR->{
                    noloalizadosrF.handle(respNoLoR);
                });
        clientPost(String.format(HST_ELS_CUMPLIMIENTO_SEARCH, collName), 
                new JsonObject(String.format(queryCumplimiento, rfc.toLowerCase(), curp.toLowerCase())), 
                //3, 
                respCump->{
                    cumplimientoF.handle(respCump);
                });
        clientPost(String.format(HST_ELS_DECLARACION_SEARCH, collName), 
                new JsonObject(String.format(queryDecla, rfc.substring(0,10).toLowerCase(), rfc.substring(10).toLowerCase(), curp.toLowerCase(), rfc.toLowerCase(), curp.toLowerCase())), 
                //4, 
                respDeclac->{
                    declaracionF.handle(respDeclac);                  
                });
        
        CompositeFuture.all(noloalizadosdF, noloalizadosrF, cumplimientoF, declaracionF).setHandler(comprobante ->{
            if (comprobante.succeeded()){
                try{
                    StringBuilder nombreCompleto= new StringBuilder();
                    JsonArray datosDeclaracion = new JsonArray();
                    if (declaracionF.result().getJsonObject(HITS).getJsonArray(HITS)!= null){
                        for (Object index : declaracionF.result().getJsonObject(HITS).getJsonArray(HITS)) {
                            if (((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).containsKey("encabezado")){
                                datosDeclaracion.add(new JsonObject().put("id", ((JsonObject)index).getJsonObject(SOURCE).getString("id")).put("formato", "AVISO"));
                                if (nombreCompleto.toString().isEmpty()){
                                    nombreCompleto.append(((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject(DATOS_GENERALES).getJsonObject(DATOS_PERSONALES).getString(NOMBRE)).append(" ")
                                            .append(((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject(DATOS_GENERALES).getJsonObject(DATOS_PERSONALES).getString(PRIMER_APELLIDO));
                                    if (((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject(DATOS_GENERALES).getJsonObject(DATOS_PERSONALES).getString(SEGUNDO_APELLIDO) != null){
                                        nombreCompleto.append(" ").append(((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject(DATOS_GENERALES).getJsonObject(DATOS_PERSONALES).getString(SEGUNDO_APELLIDO));
                                    }
                                }
                            }else {
                                datosDeclaracion.add(new JsonObject().put("id", ((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getString("id")).put("formato", ((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).containsKey("bienesMuebles") ? "COMPLETO":"SIMPLIFICADO"));
                                if (nombreCompleto.toString().isEmpty()){
                                    nombreCompleto.append(((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject("situacionPatrimonial").getJsonObject(DATOS_GENERALES).getString(NOMBRE)).append(" ")
                                            .append(((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject("situacionPatrimonial").getJsonObject(DATOS_GENERALES).getString(PRIMER_APELLIDO));
                                    if (((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject("situacionPatrimonial").getJsonObject(DATOS_GENERALES).getString(SEGUNDO_APELLIDO) != null){
                                        nombreCompleto.append(" ").append(((JsonObject)index).getJsonObject(SOURCE).getJsonObject(DECLARACION).getJsonObject(DECLARACION).getJsonObject("situacionPatrimonial").getJsonObject(DATOS_GENERALES).getString(SEGUNDO_APELLIDO));
                                    }
                                }
                            }
                        }
                    }

                    JsonObject localizado = new JsonObject();
                    localizado
                            .put("datoConsultado", curp)
                            .put(DATOS_DECLA, !noloalizadosdF.result().getJsonObject(HITS).getJsonArray(HITS).isEmpty())
                            .put(DATOS_RUSP, !noloalizadosrF.result().getJsonObject(HITS).getJsonArray(HITS).isEmpty())
                            .put(CUMPLIMIENTO, !cumplimientoF.result().getJsonObject(HITS).getJsonArray(HITS).isEmpty())
                            .put(DECLARACION, datosDeclaracion);

                    if (localizado.getBoolean(DATOS_RUSP) && nombreCompleto.toString().isEmpty()){ 
                        nombreCompleto.append(noloalizadosrF.result().getJsonObject(HITS).getJsonArray(HITS).getJsonObject(0).getJsonObject(SOURCE).getString(NOMBRES)).append(" ")
                                .append(noloalizadosrF.result().getJsonObject(HITS).getJsonArray(HITS).getJsonObject(0).getJsonObject(SOURCE).getString(PRIMER_APELLIDO));
                        if (noloalizadosrF.result().getJsonObject(HITS).getJsonArray(HITS).getJsonObject(0).getJsonObject(SOURCE).getString(SEGUNDO_APELLIDO) != null){
                            nombreCompleto.append(" ").append(noloalizadosrF.result().getJsonObject(HITS).getJsonArray(HITS).getJsonObject(0).getJsonObject(SOURCE).getString(SEGUNDO_APELLIDO));
                        }
                    }else if (localizado.getBoolean(CUMPLIMIENTO) && nombreCompleto.toString().isEmpty()){ 
                        nombreCompleto.append(cumplimientoF.result().getJsonObject(HITS).getJsonArray(HITS).getJsonObject(0).getJsonObject(SOURCE).getJsonObject(DATOS_RUSP).getString(NOMBRES)).append(" ")
                                .append(cumplimientoF.result().getJsonObject(HITS).getJsonArray(HITS).getJsonObject(0).getJsonObject(SOURCE).getJsonObject(DATOS_RUSP).getString(PRIMER_APELLIDO));
                        if (cumplimientoF.result().getJsonObject(HITS).getJsonArray(HITS).getJsonObject(0).getJsonObject(SOURCE).getJsonObject(DATOS_RUSP).getString(SEGUNDO_APELLIDO) != null){
                            nombreCompleto.append(" ").append(cumplimientoF.result().getJsonObject(HITS).getJsonArray(HITS).getJsonObject(0).getJsonObject(SOURCE).getJsonObject(DATOS_RUSP).getString(SEGUNDO_APELLIDO));
                        }
                    }
                    
                    localizado.put(NOMBRE,  nombreCompleto.toString().trim());
                    respuesta.handle(Future.succeededFuture(localizado));
                }catch(Exception e){
                    logger.log(Level.SEVERE, "Error {0}", e);
                    respuesta.handle(Future.failedFuture("ERROR_EN_NOMBRE"));
                }
            }else{
                logger.log(Level.SEVERE, "fallo alguna consulta de las 4 fallo {0}", comprobante.cause());
                respuesta.handle(Future.failedFuture("ERROR_4_CONSULTAS"));
            }
        });
        return this;
    }
    
    private ServicePortalVulneradosInterface clientPost(String url, JsonObject body, Handler<AsyncResult<JsonObject>> resultHandler){
        client.postAbs(url)
                .timeout(240000).putHeader("content-type", "application/json")
                .sendJsonObject(body, response->{
                    if (response.succeeded() && response.result().statusCode()== HttpResponseStatus.OK.code()){
                        resultHandler.handle(Future.succeededFuture(response.result().bodyAsJsonObject()));
                    }else{                        
                        if (response.succeeded() && response.result().statusCode()!= HttpResponseStatus.OK.code()){
                            logger.severe(String.format("Error no catch status %s", response.result().statusCode()));
                        }
                        logger.severe(String.format("Error Cause: %s %n Error al realizar peticion curl -X POST -d '%s' %s ", response.cause(), body, url));
                        resultHandler.handle(Future.failedFuture("Fallo cinsultaElastic"));
                    }
                });
        return this;
    }
}