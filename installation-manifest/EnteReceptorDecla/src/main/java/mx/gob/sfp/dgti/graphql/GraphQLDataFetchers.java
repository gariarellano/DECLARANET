/**
 * @(#)GraphQLDataFetchers.java 11/05/2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.graphql;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.dao.interfaces.DAOEnteReceptorInterface;
import mx.gob.sfp.dgti.dto.EnteReceptorDeclaDTO;
import mx.gob.sfp.dgti.dto.RespuestaGraphEntesDTO;
import mx.gob.sfp.dgti.graphql.queryExecutor.AsyncDataFetcher;
import mx.gob.sfp.dgti.helper.Helper;
import mx.gob.sfp.dgti.validacion.ValidacionEnte;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Clase que contiene los DataFetcher para resolver los queries y mutations de graphql
 *
 * @author programador04
 * @since 11/05/2020
 */
public class GraphQLDataFetchers {

    /**
     * Logger
     */
	private static final Logger logger = Logger.getLogger(GraphQLDataFetchers.class.getName());
	private static final String QUERY_DEFAULT = "query default";
	private static final String ENTE_NUEVO = "enteNuevo";
    private ValidacionEnte validacionEnte;
    private DAOEnteReceptorInterface daoEnteReceptorInterface;
    /**
     * DataFetcher para obtener el historico de algun ente
     *
     * @author programador04
     * @since 11/05/2020
     */
    private final AsyncDataFetcher<String> queryDefault = (env, handler) -> {
    	System.out.println(env.getVariables());
        handler.handle(Future.succeededFuture(QUERY_DEFAULT));
    };
    
    private final AsyncDataFetcher<RespuestaGraphEntesDTO> agregarEnte = (env, handler) -> {
    	Map<String, Object> argumentos = env.getArguments();
    	try {
        			validacionEnte.realizaValidaciones(JsonObject.mapFrom(argumentos.get(ENTE_NUEVO)), resultHandler ->{
           			 if (resultHandler.succeeded()) {
           				 if (resultHandler.result().getErrores().isEmpty()) {
           					 daoEnteReceptorInterface.agregaEnteReceptorDeclaracion(JsonObject.mapFrom(argumentos.get(ENTE_NUEVO)), resultGuarado -> {
                                    if (resultGuarado.succeeded()) {
                                    	if(!resultGuarado.result().isEmpty()) {
                                    		logger.log(Level.SEVERE, "Ente guardado:{0}", resultGuarado.result().getString("mensaje"));
                                    		RespuestaGraphEntesDTO respuesta = Helper.crearMensajeExito();
                                    		respuesta.setEnte(Json.decodeValue(JsonObject.mapFrom(resultGuarado.result().getJsonObject("enteReceptorDeclaracion")).toBuffer(), EnteReceptorDeclaDTO.class));
                                			handler.handle(Future.succeededFuture(respuesta));
                                    	}else {
                                    		logger.log(Level.INFO, "Error al guardar el ente:{0}", resultGuarado);
                                    		RespuestaGraphEntesDTO respuesta = Helper.crearMensajeError("Error al guardar el ente, inténtelo más tarde");
                                    		respuesta.setEnte(Json.decodeValue(JsonObject.mapFrom(argumentos.get(ENTE_NUEVO)).toBuffer(), EnteReceptorDeclaDTO.class));
                                        	handler.handle(Future.succeededFuture(respuesta));
                                    	}
                                    	
                                    } else {
                                    	logger.log(Level.SEVERE, "Error al guardar el ente en la bd {0}", resultGuarado.cause());
                                    	RespuestaGraphEntesDTO respuesta = Helper.crearMensajeError("Error al guardar el ente, favor de inténtarlo más tarde");
                                		respuesta.setEnte(Json.decodeValue(JsonObject.mapFrom(argumentos.get(ENTE_NUEVO)).toBuffer(), EnteReceptorDeclaDTO.class));
                            			handler.handle(Future.succeededFuture(respuesta));
                            		}
                                });
           				 }else {
           					 logger.info("Ente a guardar con errores en su validación");
           					 RespuestaGraphEntesDTO respuestaGraphEntesDTO = new RespuestaGraphEntesDTO(resultHandler.result() , Json.decodeValue(JsonObject.mapFrom(argumentos.get(ENTE_NUEVO)).toBuffer(), EnteReceptorDeclaDTO.class) );
           					 handler.handle(Future.succeededFuture(respuestaGraphEntesDTO));
           				 }
           			 }else {
           				 handler.handle(Future.failedFuture(Json.encode(resultHandler.cause())));
           			 }
           		});
    	}catch(Exception e) {    		
    		logger.severe(String.format("%s %s", "error al guardar ente  ", e));
    		RespuestaGraphEntesDTO respuesta = Helper.crearMensajeError("Error al procesar la solicitud, inténtelo más tarde");
    		respuesta.setEnte(Json.decodeValue(JsonObject.mapFrom(argumentos.get(ENTE_NUEVO)).toBuffer(), EnteReceptorDeclaDTO.class));
			handler.handle(Future.succeededFuture(respuesta));
    	}
    };

    public AsyncDataFetcher<String> getQueryDefault() {
        return queryDefault;
    }
    
    public AsyncDataFetcher<RespuestaGraphEntesDTO> getAgregarEnte() {
        return agregarEnte;
    }


    /**
     * Init
     *
     * @param vertx
     */
    public void init(Vertx vertx){
    	daoEnteReceptorInterface = DAOEnteReceptorInterface.createProxy(vertx, DAOEnteReceptorInterface.SERVICE_ADDRESS);
    	validacionEnte = new ValidacionEnte(vertx);
    }
    
}