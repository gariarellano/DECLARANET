/**
 * @(#)Server.java 11/06/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.verticle;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.circuitbreaker.OpenCircuitException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import mx.gob.sfp.dgti.decnet.as.ValidacionAS;
import mx.gob.sfp.dgti.decnet.graphql.AsyncExecException;
import mx.gob.sfp.dgti.decnet.graphql.AsyncGraphQLExec;
import mx.gob.sfp.dgti.decnet.util.TipoSchema;

import java.util.*;

import static io.vertx.core.http.HttpMethod.GET;
import static mx.gob.sfp.dgti.decnet.util.Constantes.*;

/**
 * Verticle para catálogos declaranet
 * 
 * @author Miriam Sánchez Sánchez programador07
 * @since 11/06/2019
 */
public class Server extends AbstractVerticle {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

	/**
	 * Path principal
	 */
	private static final String PATH = "/";

	/**
	 * Path principal schema publico
	 */
	private static final String PATH_PUB = "/publico/";

	/**
	 * Path principal para schema privado
	 */
	private static final String PATH_PRIV = "/privado/";

	/**
	 * graphiql
	 */
	private static final String GRAPHIQL = "graphiql";

	/**
	 * Path para GraphQL
	 */
	private static final String GRAPHQL_ = "/*";

	/**
	 * graphiql route
	 */
	private static final String GRAPHIQL_ROUTE_PUB = "/publico/graphiql/*";

	/**
	 * graphiql template priv
	 */
	private static final String GRAPHIQL_ROUTE_PRIV =  "/privado/graphiql/*";

	/**
	 * graphiql template
	 */
	private static final String GRAPHIQL_TEMPLATE =  "graphiql.html";

	/**
	 * Nombre para obtener el query de GraphQL.
	 */
	private static final String QUERY = "query";

	/**
	 * Nombre para obtener el operation de GraphQL.
	 */
	private static final String OPERATION_NAME = "operationName";

	/**
	 * Nombre para obtener las variables.
	 */
	private static final String VARIABLES = "variables";

	/**
	 * Nombre de Json que contiene toda la respuesta devuelta por GraphQL
	 */
	private static final String DATA = "data";

	/**
	 * Nombre que contiene el error devulto en respuesta en Graphql
	 */
	private static final String ERROR = "error";

	/**
	 * Circuit breaker
	 */
	private static final String CIRCUIT_BREAKER = "circuit-breaker";

	/**
	 * Nombre para el circuit breaker
	 */
	private static final String NAME = "name";

	/**
	 * Maximo de fallas
	 */
	private static final String MAX_FAILURES = "max-failures";

	/**
	 * Tiempo limite
	 */
	private static final String TIMEOUT = "timeout";

	/**
	 * Tiempo para volver a iniciar
	 */
	private static final String RESET_TIMEOUT = "reset-timeout";

	/**
	 * Nombre de la variable de entorno del server port
	 */
	private static final String CAT_DNET_SERVER_PORT = "SERVER_PORT";

	/**
	 * Puerto por defecto para atender el servicio
	 */
	private static final Integer DEFAULT_SERVER_PORT = 5000;

	/**
	 * Implementacion asincrona de GraphQL publico
	 */
	private AsyncGraphQLExec asyncGraphQLPub;

	/**
	 * Implementacion asincrona de GraphQL privado
	 */
	private AsyncGraphQLExec asyncGraphQLPriv;

	/**
	 * Circuit breaker
	 */
	private CircuitBreaker circuitBreaker;

	/**
	 * Servicio para realizar validaciones
	 */
	ValidacionAS validacionAS;

	/**
	 * Puerto final para escuchar la aplicacion
	 */
	private static final Integer SERVER_PORT_VALOR = System.getenv(CAT_DNET_SERVER_PORT) != null ?
			Integer.parseInt(System.getenv(CAT_DNET_SERVER_PORT)) : DEFAULT_SERVER_PORT;

	/**
	 * Iniciar el verticle de Server
	 *
	 * @param vertx
	 * @param context
	 */
	@Override
	public void init(Vertx vertx, Context context) {
		super.init(vertx, context);
		asyncGraphQLPub = AsyncGraphQLExec.create(vertx, TipoSchema.PUBLICO);
		asyncGraphQLPriv = AsyncGraphQLExec.create(vertx, TipoSchema.PRIVADO);
		validacionAS = ValidacionAS.create(vertx);
	}

	/**
	 * Inicia el verticle y se queda a la espera de llamado en el puerto asignado.
	 *
	 * @param startFuture
	 * @throws Exception
	 */
	@Override
	public void start(Future<Void> startFuture) throws Exception {

		LOGGER.info("=== Inicia Verticle Server");
		super.start();

		/*Definir circuitBreaker*/
		JsonObject cbOptions = config().getJsonObject(CIRCUIT_BREAKER) != null ? config().getJsonObject(CIRCUIT_BREAKER)
				: new JsonObject();
		circuitBreaker = CircuitBreaker.create(cbOptions.getString(NAME, CIRCUIT_BREAKER), vertx,
				new CircuitBreakerOptions().setMaxFailures(cbOptions.getInteger(MAX_FAILURES, 5)).setMaxRetries(3)
						.setTimeout(cbOptions.getLong(TIMEOUT, 5000L)).setFallbackOnFailure(true)
						.setResetTimeout(cbOptions.getLong(RESET_TIMEOUT, 10000L)));

		Router router = Router.router(this.getVertx());
		router.route().handler(CorsHandler.create(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
				.allowedHeaders(getAllowedHeaders()).allowedMethods(getAllowedMethods()));

		/*Ruta*/
		router.route(GRAPHQL_).handler(BodyHandler.create());

		/*Mostrar tabla*/
		router.get(PATH).handler(rc -> {
			rc.response()
				.putHeader(HEADER_CONTENT_TYPE, TEXT_HTML)
				.setStatusCode(HttpResponseStatus.OK.code())
					.end("=== Catalogo Declaranet: " + System.getenv(CAT_DNET_TABLE) + "===");
		});

		router.post(PATH_PUB).handler(this::createGraphQLPub);
		router.post(PATH_PRIV).handler(this::createGraphQLPriv);

		/*Definir template de schema publico*/
		StaticHandler staticHandler = StaticHandler.create(GRAPHIQL);
		staticHandler.setDirectoryListing(false);
		staticHandler.setCachingEnabled(false);
		staticHandler.setIndexPage(GRAPHIQL_TEMPLATE);
		router.route(GRAPHIQL_ROUTE_PUB).method(GET).handler(staticHandler);

		/*Definir template de schema privado*/
		StaticHandler staticHandlerPriv = StaticHandler.create(GRAPHIQL);
		staticHandlerPriv.setDirectoryListing(false);
		staticHandlerPriv.setCachingEnabled(false);
		staticHandlerPriv.setIndexPage(GRAPHIQL_TEMPLATE);
		router.route(GRAPHIQL_ROUTE_PRIV).method(GET).handler(staticHandlerPriv);


		this.getVertx().createHttpServer().requestHandler(router::accept)
				.listen(config().getInteger(SERVER_PORT, SERVER_PORT_VALOR), ar -> {
					if(ar.succeeded()) {
						LOGGER.info("Escuchando en puerto..." + SERVER_PORT_VALOR);
						startFuture.complete();
					} else {
						LOGGER.error(ar.cause());
						startFuture.fail(ar.cause());
					}
				});
	}

	private void createGraphQLPub(RoutingContext context) {
		LOGGER.info("GraphQL - público");
		createGraphQL(context, asyncGraphQLPub);
	}

	private void createGraphQLPriv(RoutingContext context) {
		LOGGER.info("GraphQL - privado");
		createGraphQL(context, asyncGraphQLPriv);
	}


	/**
	 * Resuelve los request de graphQL
	 * @param context
	 */
	private void createGraphQL(RoutingContext context, AsyncGraphQLExec graph) {

		obtenerHeaders(context);
		circuitBreaker.execute(futuro -> {
			JsonObject bodyJson = context.getBodyAsJson();
			String query = bodyJson.getString(QUERY);
			LOGGER.info("=== query graphql: ", query);
			String operationName = bodyJson.getString(OPERATION_NAME);
			JsonObject bodyVariables = bodyJson.getJsonObject(VARIABLES);

			Map<String, Object> variables = new HashMap<>();
			if (bodyVariables != null && !bodyVariables.isEmpty()) {
				variables = bodyVariables.getMap();
			}

			graph.executeQuery(query, operationName, context, variables).setHandler(resultado -> {
				if (resultado.succeeded()) {
					JsonObject json = resultado.result();
					context.response().end(new JsonObject().put(DATA, json).encode());
					futuro.complete();
				} else {
					Map<String, Object> res = new HashMap<>();
					res.put(DATA, null);
					if (resultado.cause() instanceof AsyncExecException) {
						AsyncExecException ex = (AsyncExecException) resultado.cause();
						res.put(ERROR, ex.getErrors());
					} else {
						res.put(ERROR, resultado.cause() != null ? Arrays.asList(resultado.cause())
								: Arrays.asList(new Exception("Internal error")));
					}
					context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
							.end(new JsonObject(res).encode());
					futuro.fail(ERROR);
				}
			});
		}).setHandler(res -> {
			if (res.cause() instanceof OpenCircuitException) {
				context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
						.end("=== Circuit breaker en acción");
			}
		});

	}

	/**
	 * Headers permitidos para CORS
	 * @return 
	 */
	private Set<String> getAllowedHeaders() {
		Set<String> allowedHeaders = new HashSet<>();
	    allowedHeaders.add(X_REQUESTED_WITH);
	    allowedHeaders.add(ACCESS_CONTROL_ALLOW_ORIGIN);
	    allowedHeaders.add(HEADER_CONTENT_TYPE);
	    allowedHeaders.add(ACCEPT);
	    allowedHeaders.add(X_PINGARUNER);   
	    return allowedHeaders;
	}
	
	/**
	 * Métodos permitidos para CORS
	 * @return
	 */
	private Set<HttpMethod> getAllowedMethods() {
		Set<HttpMethod> allowedMethods = new HashSet<>();
		allowedMethods.add(GET);
		allowedMethods.add(HttpMethod.POST);
		allowedMethods.add(HttpMethod.OPTIONS);
		allowedMethods.add(HttpMethod.DELETE);
		allowedMethods.add(HttpMethod.PATCH);
		allowedMethods.add(HttpMethod.PUT);  
		return allowedMethods;
	}
	
	/**
	 * Se agregan los headers correspondientes para el Intercambio de recursos de
	 * origen cruzado (CORS) para pruebas.
	 * @param routingContext
	 * @return
	 */
	public static HttpServerResponse obtenerHeaders(RoutingContext routingContext) {
		return routingContext.response().putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
				.putHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS)
				.putHeader(ACCESS_CONTROL_ALLOW_METHODS, HEADER_ACCESS_CONTROL_ALLOW_METHODS)
				.putHeader(ACCESS_CONTROL_ALLOW_HEADERS, HEADER_ACCESS_CONTROL_ALLOW_HEADERS)
				.putHeader(CACHE_CONTROL, HEADER_CACHE_CONTROL).putHeader(PRAGMA, HEADER_PRAGMA)
				.putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON);
	}
}
