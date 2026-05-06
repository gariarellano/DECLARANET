/**
 * @(#)AppVerticle.java 11/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.serviceproxy.ServiceException;
import mx.gob.sfp.dgti.applicationservice.ApplicationService;
import mx.gob.sfp.dgti.constantes.Respuestas;
import mx.gob.sfp.dgti.dto.EnteDTO;
import mx.gob.sfp.dgti.dto.MigracionDTO;
import mx.gob.sfp.dgti.dto.ParamConsultaEnteDTO;
import mx.gob.sfp.dgti.graphql.queryExecutor.AsyncExecException;
import mx.gob.sfp.dgti.graphql.queryExecutor.AsyncGraphQLExec;
import mx.gob.sfp.dgti.utils.LoggerUtils;
import mx.gob.sfp.dgti.utils.ValidacionUtils;
import org.apache.log4j.Logger;

import java.util.*;

import static io.vertx.core.http.HttpMethod.GET;
import static mx.gob.sfp.dgti.constantes.Http.*;

/**
 * Clase con el verticle que recibe peticiones de http
 * 
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 11/02/2019
 */
public class AppVerticle extends AbstractVerticle {

	/**
	 * Logger
	 */

	final Logger logger = Logger.getLogger(AppVerticle.class);


	/**
	 * Implementacion capa de servicio
	 */
	private ApplicationService applicationService;

	/**
	 * Implementacion graphql
	 */
	private AsyncGraphQLExec asyncGraphQL;

	/**
	 * Objeto para validacion
	 */
	private ValidacionUtils validacionUtils;
	
	/**
	 * Clase de excepcion que puede recibir
	 */
	private ServiceException exc;

	  @Override
	  public void init(Vertx vertx, Context context) {
	      super.init(vertx, context);

		  asyncGraphQL = AsyncGraphQLExec.create(vertx);

	      applicationService = ApplicationService
					.createProxy(vertx, ApplicationService.SERVICE_ADDRESS);

	  }
	
	/**
	 * Es llamado cuando el verticle se despliega, crea el servidor HTTP
	 */
	@Override
	public void start(Future<Void> startFuture)  {

		logger.info("Iniciando AppVerticle");		
		
		Router router = Router.router(this.getVertx());

		router.route("/*").handler(BodyHandler.create());

		router.get("/").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "text/html")
					.end("Hola dependencias http!");
		});
		router.route().handler(
				CorsHandler.create(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
						.allowedHeaders(obtenerEncabezadosPermitidos())
						.allowedMethods(obtenerMetodosPermitidos()));

		router.route("/graphiql").method(GET).handler(rc -> {
			if ("/graphiql".equals(rc.request().path())) {
				rc.response().setStatusCode(302);
				rc.response().headers().set("Location", rc.request().path() + "/");
				rc.response().end("Correcto");
			} else {
				rc.next();
			}
		});

		StaticHandler staticHandler = StaticHandler.create("graphiql");
		staticHandler.setDirectoryListing(false);
		staticHandler.setCachingEnabled(false);
		staticHandler.setIndexPage("index.html");
		router.route("/graphiql/*").method(GET).handler(staticHandler);

		router.post("/api/graphql/").handler(this::resolverGraphql);
		router.post("/").handler(this::resolverGraphql);

		logger.info("Escucha puerto: " + System.getenv("SERVER_PORT"));

		this.getVertx().createHttpServer().requestHandler(router::accept).listen(
			config().getInteger("http.port", Integer.parseInt(System.getenv("SERVER_PORT"))), ar -> {
				if(ar.succeeded()) {
					logger.info("Escuchando...");
					startFuture.complete();
				} else {
					logger.error(ar.cause());
					startFuture.fail(ar.cause());
				}
		});

		validacionUtils = new ValidacionUtils();
	}

	/**
	 * Funcion para resolver los queries de graphql
	 *
	 * @param routingContext: RoutingContext
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 02/05/2019
	 */
	private void resolverGraphql(RoutingContext routingContext) {
		try {
			obtenerHeaders(routingContext);

			JsonObject bodyJson = routingContext.getBodyAsJson();

			String query = bodyJson.getString("query");
			JsonObject bodyVariables = bodyJson.getJsonObject("variables");

			String operationName = bodyJson.getString("operationName");
			Map<String, Object> variables;

			if (bodyVariables == null || bodyVariables.isEmpty()) {
				variables = new HashMap<>();
			} else {
				variables = bodyVariables.getMap();
			}

			// execute the graphql query
			asyncGraphQL.executeQuery(query, operationName, routingContext, variables).setHandler(queryResult -> {
				if (queryResult.succeeded()) {
					JsonObject json = queryResult.result();
					inicializarResponse(routingContext);
					routingContext.response().end(new JsonObject().put("data", json).encode());
				} else {

					Map<String, Object> res = new HashMap<>();
					res.put("data", null);
					if (queryResult.cause() instanceof AsyncExecException) {
						AsyncExecException ex = (AsyncExecException) queryResult.cause();
						logger.info("=== Problema ejecutando query de GraphQL: " + ex);
						logger.info("=== Parametros con problema: " + bodyVariables);
						res.put("error", ex.getErrors().toString());
					} else {
						res.put("errors", queryResult.cause() != null ?
								Arrays.asList(queryResult.cause()) : Arrays.asList(new Exception("Error interno")));
					}
					JsonObject errorResult = new JsonObject(res);
					routingContext.response().setStatusCode(400).end(errorResult.encode());
				}
			});
		} catch (Exception e) {
			Map<String, Object> res = new HashMap<>();
			res.put("errors", Arrays.asList(e));
			JsonObject errorResult = new JsonObject(res);
			routingContext.response().setStatusCode(400).end(errorResult.encode());
		}
	}

	/**
	 * Funcion que agregar un nuevo ente, el primero de su historia, es su propio padre
	 *
	 * @param routingContext: body que debe ser de tipo json con los datos del ente que sera agregado
	 * por primera vez, no puede contener id, devolvera un mensaje
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 19/02/2019
	 */
	@Deprecated
	private void agregarEnteNuevo(RoutingContext routingContext) {
		logger.info("-- agregarEnteNuevo - POST");

		try {
			final EnteDTO ente = Json.decodeValue(routingContext.getBodyAsString(), EnteDTO.class);

			applicationService.agregarEnteNuevo(ente, resp -> {
				if(resp.succeeded()) {
					agregarResponse(routingContext, Respuestas.EXITO.getId(), resp.result().toJson());
				} else {
					if (resp.cause() instanceof ServiceException) {
						exc = (ServiceException) resp.cause();
						agregarResponse(routingContext, exc.failureCode());
					}
				}});
		} catch (DecodeException de) {
			LoggerUtils.logError(de);
			agregarResponse(routingContext, Respuestas.JSON_INCORRECTO.getId());
		}
	}
	
	/**
	 * Funcion que actualiza unicamente la informacion del ente, sin crear un documento nuevo
	 * 
	 * @param routingContext: body que debe ser de tipo json con los datos del ente que sera
	 * actualizado
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 19/02/2019
	 */
	@Deprecated
	private void actualizarEnte(RoutingContext routingContext) {
		logger.info("-- actualizarInfoEnte - POST");

		try {
			final EnteDTO ente = Json.decodeValue(routingContext.getBodyAsString(), EnteDTO.class);

			applicationService.actualizarEnte(ente, resp -> {
				if (resp.succeeded()) {

					agregarResponse(routingContext, Respuestas.EXITO.getId(), resp.result().toJson());

				} else {
					if (resp.cause() instanceof ServiceException) {
						exc = (ServiceException) resp.cause();
						agregarResponse(routingContext, exc.failureCode());
					}
				}
			});
		} catch (DecodeException de) {
			agregarResponse(routingContext, Respuestas.JSON_INCORRECTO.getId());
			LoggerUtils.logError(de);
		}
	}

	/**
	 * Funcion que consulta los entes de forma dinamica de acuerdo a los parametros mandados
	 * 
	 * @param routingContext: body que debe ser de tipo json con los parametros para realizar
	 * el filtrado en la consulta.
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 19/02/2019
	 */
	private void consultarEntes(RoutingContext routingContext) {
		logger.info("-- consultarEntes - POST");
		obtenerHeaders(routingContext);
		
		try {
			final ParamConsultaEnteDTO parametros = Json.decodeValue(
					routingContext.getBodyAsString(), ParamConsultaEnteDTO.class);
	
			if (!validacionUtils.validarParametrosConsultaEntes(parametros)) {
				agregarResponse(routingContext, Respuestas.PARAMETROS_INCOMPLETOS.getId());

			} else {

				applicationService.consultarEntes(parametros, resp -> {
					if (resp.succeeded()) {
						logger.info("-- respuesta obtenida ---> " + resp.result());
						if(resp.result() != null && resp.result().size() > 0) {
							agregarResponse(routingContext, Respuestas.EXITO.getId(),
									EnteDTO.toJsonObjectList(resp.result()));

						} else {
							agregarResponse(routingContext, Respuestas.SIN_RESULTADOS.getId(),
									EnteDTO.toJsonObjectList(resp.result()));
						}

					} else {
						if (resp.cause() instanceof ServiceException) {
							exc = (ServiceException) resp.cause();
							agregarResponse(routingContext, exc.failureCode());
						}
					}
				});
			}
		} catch (DecodeException de) {

			agregarResponse(routingContext, Respuestas.JSON_INCORRECTO.getId());
			LoggerUtils.logError(de);

		}  catch (Exception e) {
			agregarResponse(routingContext, Respuestas.ERROR.getId());
			LoggerUtils.logError(e);
		}
	}

	
	/**
	 * Funcion que consulta los el historial completo de un ente cuando se le envia cualquiera de los
	 * entes dentro de la historia
	 * 
	 * @param routingContext: body que debe ser de tipo json con un ente dentro de la historia
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 19/02/2019
	 */
	@Deprecated
	private void consultarHistorialEntePorEnte(RoutingContext routingContext) {
		logger.info("-- consultarHistorialEntePorEnte - POST");
		
		try {
			final EnteDTO ente = Json.decodeValue(routingContext.getBodyAsString(), EnteDTO.class);

			applicationService.consultarHistorialEntePorEnte(ente.getIdEnteOrigen(), busq -> {
				if(busq.succeeded()) {

					if(busq.result() != null && busq.result().size()>0) {
						agregarResponse(routingContext, Respuestas.EXITO.getId(),
								EnteDTO.toJsonObjectList(busq.result()));

					} else {
						agregarResponse(routingContext, Respuestas.SIN_RESULTADOS.getId(), busq.result());
					}
				} else {
					if (busq.cause() instanceof ServiceException) {
				        exc = (ServiceException) busq.cause();

						agregarResponse(routingContext, exc.failureCode());

					}
				}
			});
		} catch (DecodeException de) {
			agregarResponse(routingContext, Respuestas.JSON_INCORRECTO.getId());
			LoggerUtils.logError(de);
		}
	}

	/**
	 * Funcion para insertar varios entes nuevos
	 * 
	 * @param routingContext: body que debe ser de tipo json con los entes
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 12/03/2019
	 */
	@Deprecated
	private void agregarEntesNuevos(RoutingContext routingContext) {
		logger.info("-- agregarEntesNuevos - POST");
		
		try {
			final MigracionDTO migracion = Json.decodeValue(routingContext.getBodyAsString(), MigracionDTO.class);

			applicationService.agregarEntesNuevos(migracion, resp -> {
				if (resp.succeeded()) {
					agregarResponse(routingContext, Respuestas.EXITO.getId(), resp.result());

				} else {
					if (resp.cause() instanceof ServiceException) {
						exc = (ServiceException) resp.cause();
						agregarResponse(routingContext, exc.failureCode());
					}
				}
			});
		
		} catch (DecodeException de) {
			LoggerUtils.logError(de);
			agregarResponse(routingContext, Respuestas.JSON_INCORRECTO.getId());

		}
	}
	
	/**
	 * Funcion para desactivar unicamente un ente o el ente y su historial
	 * 
	 * @param routingContext: parametros de GET:
	 * 		-idEnteOrigen: id del ente origen, si se recibe se actualizara el ente junto con
	 * 			su historico
	 * 		-idEnte: id del ente, si solo se encuentra este parametro, solo se actualizara
	 * 			ese ente 
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 03/04/2019
	 */
	@Deprecated
	private void desactivarEnte(RoutingContext routingContext) {

		logger.info("-- desactivarEnte - GET");

		try {
			final String id;
			final boolean desactivarHistorico;
			final String idEnte = routingContext.request().getParam("idEnte");
			final String idEnteOrigen = routingContext.request().getParam("idEnteOrigen");
			
			if(idEnteOrigen != null) {
				id = idEnteOrigen;
				desactivarHistorico = true;
			}  else if(idEnte != null) {
				id = idEnte;
				desactivarHistorico = false;
			} else {
				id = null;
				desactivarHistorico = false;
			}

			if (id != null ) {
				applicationService.desactivarEnte(desactivarHistorico, id,  resp -> {
					if(resp.succeeded()) {
						agregarResponse(routingContext, Respuestas.EXITO.getId());
					} else {
						agregarResponse(routingContext, Respuestas.ERROR.getId());
					}
				});
			} else {
				agregarResponse(routingContext, Respuestas.ERROR.getId());
			}
		} catch (Exception de) {
			agregarResponse(routingContext, Respuestas.JSON_INCORRECTO.getId());
			LoggerUtils.logError(de);
		}
	}

	/**
	 * Funcion para informacion para la respuesta
	 *
	 * @param routingContext: RoutingContext
	 * @param idRespuesta: id de la respuesta que corresponde al enum Respuestas
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 08/04/2019
	 */
	private void agregarResponse(RoutingContext routingContext, int idRespuesta) {
		inicializarResponse(routingContext);

		JsonObject respuestaBody = new JsonObject();
		respuestaBody.put("info", Respuestas.crearRespuestaJson(idRespuesta));
		routingContext.response().end(Json.encode(respuestaBody));
	}

	/**
	 * Funcion para informacion para la respuesta
	 *
	 * @param routingContext: RoutingContext
	 * @param idRespuesta: id de la respuesta que corresponde al enum Respuestas
	 * @param resultado: objeto con alguna respuesta adicional como String o listas de objetos
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 08/04/2019
	 */
	private void agregarResponse(RoutingContext routingContext, int idRespuesta, Object resultado) {
		inicializarResponse(routingContext);

		JsonObject respuestaBody = new JsonObject();
		respuestaBody.put("resultado:", resultado);
		respuestaBody.put("info", Respuestas.crearRespuestaJson(idRespuesta));
		routingContext.response().end(Json.encode(respuestaBody));
        logger.info("-- respuesta body ---> " + respuestaBody);
	}

	/**
	 * Funcion para inicializar la respuesta
	 *
	 * @param routingContext: RoutingContext
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 08/04/2019
	 */
	private void inicializarResponse(RoutingContext routingContext) {
		routingContext.response().setStatusCode(200)
				.putHeader("content-type", "application/json; charset=utf-8");
	}

	/**
	 *
	 * Metodo para obtener los metodos permitidos
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 08/04/2019
	 */
	private Set<HttpMethod> obtenerMetodosPermitidos() {
		Set<HttpMethod> metodosPermitidos = new HashSet<>();
		metodosPermitidos.add(HttpMethod.GET);
		metodosPermitidos.add(HttpMethod.POST);
		metodosPermitidos.add(HttpMethod.OPTIONS);
		metodosPermitidos.add(HttpMethod.DELETE);
		metodosPermitidos.add(HttpMethod.PATCH);
		metodosPermitidos.add(HttpMethod.PUT);
		return metodosPermitidos;
	}

	/**
	 * Metodo para obtener los encabezados permitidos
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 08/04/2019
	 */
	private Set<String> obtenerEncabezadosPermitidos() {

		Set<String> encabezadosPermitidos = new HashSet<>();
		encabezadosPermitidos.add(X_REQUESTED_WITH);
		encabezadosPermitidos.add(ACCESS_CONTROL_ALLOW_ORIGIN);
		encabezadosPermitidos.add(HEADER_ORIGIN);
		encabezadosPermitidos.add(HEADER_CONTENT_TYPE);
		encabezadosPermitidos.add(ACCEPT);
		encabezadosPermitidos.add(X_PINGARUNER);
		return encabezadosPermitidos;
	}

	/**
	 * Metodo para agregar encabezados
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 08/04/2019
	 */
	public static HttpServerResponse obtenerHeaders(RoutingContext routingContext) {
		return routingContext.response().putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
				.putHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS)
				.putHeader(ACCESS_CONTROL_ALLOW_METHODS, HEADER_ACCESS_CONTROL_ALLOW_METHODS)
				.putHeader(ACCESS_CONTROL_ALLOW_HEADERS, HEADER_ACCESS_CONTROL_ALLOW_HEADERS)
				.putHeader(CACHE_CONTROL, HEADER_CACHE_CONTROL).putHeader(PRAGMA, HEADER_PRAGMA);
	}

}