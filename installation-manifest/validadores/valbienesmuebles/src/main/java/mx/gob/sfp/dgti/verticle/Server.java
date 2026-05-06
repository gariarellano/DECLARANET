/**
 * @(#)Server.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.verticle;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.reactivex.core.AbstractVerticle;
import mx.gob.sfp.dgti.declaracion.dto.general.BienesMueblesDTO;
import mx.gob.sfp.dgti.validacion.Validadores;

import java.util.HashSet;
import java.util.Set;

import static mx.gob.sfp.dgti.util.Constantes.*;

/**
 * Verticle para el server
 *
 * @author Miriam Sanchez Sanchez programador07
 * @since 19/09/2019
 */
public class Server extends AbstractVerticle {

	/**
	 * El logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

	/**
	 * Ruta que realiza las validaciones
	 */
	private static final String PATH_VALIDACION = "/validador";

	/**
	 * Path
	 */
	private static final String PATH = "/";

	/**
	 *
	 */
	private Validadores validacion;
	
	/**
	 * Start inicializar router y la definición de paths
	 * @throws Exception
	 */
	@Override
	public void start(Future<Void> future) throws Exception {
		super.start();

		validacion = new Validadores(vertx);

		Router router = Router.router(vertx.getDelegate());
		router.route().handler(CorsHandler.create(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
				.allowedHeaders(getAllowedHeaders()));
		router.route(PATH).handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader(HEADER_CONTENT_TYPE, TEXT_HTML).end("Hola validador Bienes Muebles");
		});
		
		router.route().handler(BodyHandler.create());
		router.post(PATH_VALIDACION).handler(this::validarDomDecl);
		router.route().handler(StaticHandler.create());

		this.getVertx().createHttpServer().requestHandler(router::accept).listen(
			config().getInteger(CONFIG_PORT, Integer.parseInt(System.getenv(SERVER_PORT) != null ?
					System.getenv(SERVER_PORT) : "5000")), ar -> {
	            if(ar.succeeded()) {
	                LOGGER.info("En ejecución...");
	                future.complete();
	            } else {
	                LOGGER.info(ar.cause());
	                future.fail(ar.cause());
	            }
			});
	}
	
	/**
	 * Validador del módulo de datos generales
	 * @param context: contexto de la petición que contiene los stringParams, header y body de la petición
	 */
	private void validarDomDecl(RoutingContext context) {
		this.obtenerHeaders(context);
		if(!context.getBodyAsString().equals("")) {
			try {

				BienesMueblesDTO bienesMueblesDto = Json.decodeValue(context.getBodyAsString(),
						BienesMueblesDTO.class);

				validacion.validar(bienesMueblesDto).setHandler(modulo -> {
					if (modulo.succeeded()) {
						if (modulo.result().getErrores().isEmpty() && !modulo.result().isIncompleto()) {
							context.response().end(new JsonObject().toString());
						} else {
							context.response().end(Json.encode(modulo.result()));
						}
					} else {
						context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
					}
				});
			} catch (Exception e) {
				LOGGER.info("Algo ocurrió:\n" + e.getMessage() + e.getCause().getMessage());
				context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
			}
		} else {
			context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
		}
	}
	
	/**
	* Se agregan los headers correspondientes para el Intercambio de recursos de
	* origen cruzado (CORS).
	* 
	* @param routingContext: contexto
	* @return HttpServerResponse
	*/
	public HttpServerResponse obtenerHeaders(RoutingContext routingContext) {
		return routingContext.response().putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
				.putHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS)
				.putHeader(ACCESS_CONTROL_ALLOW_METHODS, HEADER_ACCESS_CONTROL_ALLOW_METHODS)
				.putHeader(ACCESS_CONTROL_ALLOW_HEADERS, HEADER_ACCESS_CONTROL_ALLOW_HEADERS)
				.putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON);
	}

	/**
	 * Headers permitidos para CORS
	 * @return Set<String>: Headers permitidos en las peticiones
	 */
	public static Set<String> getAllowedHeaders() {
		Set<String> allowedHeaders = new HashSet<>();
	    allowedHeaders.add(HEADER_CONTENT_TYPE);
	    return allowedHeaders;
	}

	/**
	 * Main para pruebas locales
	 * @param args
	 */
	public static void main(String[] args) {
		Vertx vertex = Vertx.vertx();
		vertex.deployVerticle(new ServerLauncher());
	}

}