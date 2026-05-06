/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "EnteReceptorDecla" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.verticle;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
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
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.serviceproxy.ServiceBinder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mx.gob.sfp.dgti.dao.interfaces.DAOEnteReceptorInterface;
import mx.gob.sfp.dgti.graphql.queryExecutor.AsyncExecException;
import mx.gob.sfp.dgti.graphql.queryExecutor.AsyncGraphQLExec;
import mx.gob.sfp.dgti.service.interfaces.ServiceEnteReceptorInerface;
import static mx.gob.sfp.dgti.service.interfaces.ServiceEnteReceptorInerface.DEFAULT_PATH_ENTE_RECEPTOR;
import static mx.gob.sfp.dgti.service.interfaces.ServiceEnteReceptorInerface.PROPERTY_COLL_NAME;
import static mx.gob.sfp.dgti.service.interfaces.ServiceEnteReceptorInerface.PROPERTY_ID;
import static mx.gob.sfp.dgti.service.interfaces.ServiceEnteReceptorInerface.PROPERTY_PATH;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_PORT;
import static mx.gob.sfp.dgti.util.Constantes.DATA;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_APPLICATION_JSON;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_TYPE;
import static mx.gob.sfp.dgti.util.Constantes.PATH;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_AGREGA_ENTE_RECEPTOR;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_CONSULTAR_ENTE;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_CONSULTA_ENTE_RECEPTOR;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_CONSULTA_ENTE_RECEPTOR_AS_PARAM;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_CONSULTA_ENTE_RECEPTOR_COLLNAME;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_CONSULTA_ENTE_RECEPTOR_POR_ID;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_CONSULTA_TODOS_ENTES;
import static io.vertx.core.http.HttpMethod.GET;
import static mx.gob.sfp.dgti.util.Constantes.CACHE_CONTROL;
import static mx.gob.sfp.dgti.util.Constantes.PATH_ROUTE;
import static mx.gob.sfp.dgti.util.Constantes.PATH_API_GRAPHQL;
import static mx.gob.sfp.dgti.util.Constantes.GRAPHIQL;
import static mx.gob.sfp.dgti.util.Constantes.PATH_GRAPHQL_ROUTE;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_INTERNO_GRAPHQL;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_GRAPHQL;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_QUERYS_GRAPHQL;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_PARAMETROS_GRAPHQL;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CACHE_CONTOL_INMUTABLE;

/**
 * Clase que coneitne la funcionalidad principal para inicializar la aplicacion.
 *
 * @author cgarias
 * @since 06/11/2019
 */
public class Server extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
	
	
    private static final String CONTENT_TYPE = "content-type";
    private static final String TEXT_HTML = "text/html";
    private static final String QUERY = "query";
    private static final String VARIABLES = "variables";
    private static final String OPERATION_NAME = "operationName";
    private static final String INDEX_GRAPHQL = "index.html";
    private static final String MESSAGE_TEST_SANITY = "Hola entes recepctores Declaranet";
    private ServiceEnteReceptorInerface serviceEnteReceptorInerfaceVertWebClient;
    private static Router router;
    private WebClient webClient;
    private AsyncGraphQLExec asyncGraphQL;
    private MessageConsumer<JsonObject> binder;
    private DAOEnteReceptorInterface daoEnteReceptorInterface;
    @Override
    public void  init(Vertx vertx, Context context){
        super.init(vertx, context);
        router = Router.router(vertx);
        asyncGraphQL = AsyncGraphQLExec.create(vertx);
        webClient  = new WebClient(io.vertx.ext.web.client.WebClient.create(vertx));
        serviceEnteReceptorInerfaceVertWebClient = ServiceEnteReceptorInerface.init(vertx, webClient);
        
       router.route().handler(CorsHandler.create(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).allowedHeaders(getAllowedHeaders()));
       
       router.route(PATH_ROUTE).handler(BodyHandler.create());
        router.get(PATH).handler(rc -> {
            rc.response().putHeader(CONTENT_TYPE, TEXT_HTML);
            rc.response().setStatusCode(200);
            rc.response().end(MESSAGE_TEST_SANITY);        
        });
        
        StaticHandler staticHandler = StaticHandler.create(GRAPHIQL);
        staticHandler.setDirectoryListing(false);
        staticHandler.setCachingEnabled(false);
        staticHandler.setIndexPage(INDEX_GRAPHQL);
        router.route(PATH_GRAPHQL_ROUTE).method(GET).handler(staticHandler);
        
        this.getVertx().createHttpServer().requestHandler(router::accept).listen(
                config().getInteger(CONFIG_PORT, 5000), ar -> {
                    if (ar.succeeded()) {
                        LOGGER.info("Running");
                    } else {
                        LOGGER.info(ar.cause());
                    }
                });
    }
    
    /**
     * Start inicializar router y la definición de paths
     *
     * @param future
     * @throws Exception
     */
    @Override
    public void start(Future<Void> future) throws Exception {
        super.start();
        router.route().handler(BodyHandler.create());
        router.get(URL_APPI_CONSULTA_ENTE_RECEPTOR).handler(this::consultarEnteRececptor);
        router.get(URL_APPI_CONSULTA_ENTE_RECEPTOR_AS_PARAM).handler(this::consultarEnteRececptor);
        router.get(URL_APPI_CONSULTA_ENTE_RECEPTOR_POR_ID).handler(this::consultarEnteRececptorById);
        router.get(URL_APPI_CONSULTA_ENTE_RECEPTOR_COLLNAME).handler(this::consultarEnteRececeptorPorCollName);
        router.post(URL_APPI_AGREGA_ENTE_RECEPTOR).handler(this::agregaEnteReceptor);
        router.post(URL_APPI_CONSULTAR_ENTE).handler(this::consultarEnte);
        router.get(URL_APPI_CONSULTA_TODOS_ENTES).handler(this::consultarTodosEntes);
        
    	 router.post(PATH_API_GRAPHQL).handler(this::resolverGraphql);
         router.post(PATH).handler(this::resolverGraphql);

         daoEnteReceptorInterface = DAOEnteReceptorInterface.create(this.getVertx());
         binder = new ServiceBinder(vertx)
     			.setAddress(DAOEnteReceptorInterface.SERVICE_ADDRESS)
     			.register(DAOEnteReceptorInterface.class, daoEnteReceptorInterface);
     		binder.completionHandler(future);


    }

    /**
	 * Funcion para resolver los queries de graphql
	 *
	 * @param routingContext: RoutingContext
	 *
	 * @author programador04
	 * @since 11/05/2020
	 */
	private void resolverGraphql(RoutingContext routingContext) {
            try {
		obtenerHeaders(routingContext);
		JsonObject bodyJson = routingContext.getBodyAsJson();
		String query = bodyJson.getString(QUERY);
		JsonObject bodyVariables = bodyJson.getJsonObject(VARIABLES);

		String operationName = bodyJson.getString(OPERATION_NAME);
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
				routingContext.response().end(new JsonObject().put(DATA, json).encode());
			} else {
				Map<String, Object> res = new HashMap<>();
				res.put(DATA, null);
				if (queryResult.cause() instanceof AsyncExecException) {
					AsyncExecException ex = (AsyncExecException) queryResult.cause();
					LOGGER.info(ERROR_QUERYS_GRAPHQL + ex);
					LOGGER.info(ERROR_PARAMETROS_GRAPHQL + bodyVariables);
					res.put(ERROR_GRAPHQL, ex.getErrors().toString());
				} else {
					res.put(ERROR_GRAPHQL, queryResult.cause() != null ?
							Arrays.asList(queryResult.cause()) : Arrays.asList(new Exception(ERROR_INTERNO_GRAPHQL)));
				}
				JsonObject errorResult = new JsonObject(res);
				routingContext.response().setStatusCode(400).end(errorResult.encode());
			}
		});
	} catch (Exception e) {
		Map<String, Object> res = new HashMap<>();
		res.put(ERROR_GRAPHQL, Arrays.asList(e));
		JsonObject errorResult = new JsonObject(res);
		routingContext.response().setStatusCode(400).end(errorResult.encode());
	}
	}
    /**
     * Validador del módulo de datos generales
     *
     * @param context contexto de la petición que contiene los stringParams,
     * header y body de la petición
     */
    private void consultarEnteRececptor(RoutingContext context) {
        try {
            this.obtenerHeaders(context);
            String path = context.request().getParam(PROPERTY_PATH);
            if (!DEFAULT_PATH_ENTE_RECEPTOR.equals(path)) {
                serviceEnteReceptorInerfaceVertWebClient.consultaEnteReceptorDeclaracion(path).setHandler(result -> {
                    if (result.succeeded()) {
                        context.response().putHeader(CACHE_CONTROL, HEADER_CACHE_CONTOL_INMUTABLE);
                        context.response().end(result.result().toString());
                    } else {
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                    }
                });
            } else {
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }
    
    /**
     * Método que realiza la consulta de enteReceptor por id
     *
     * @param context contexto de la petición que contiene el id del ente a buscar.
     */
    private void consultarEnteRececptorById(RoutingContext context) {
        try {
            this.obtenerHeaders(context);
            String idEnte = context.request().getParam(PROPERTY_ID);
                serviceEnteReceptorInerfaceVertWebClient.consultaEnteReceptorporEnteId(idEnte).setHandler(result -> {
                    if (result.succeeded()) {
                        context.response().end(result.result().toString());
                    } else {
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                    }
                });
        } catch (Exception e) {
            LOGGER.error(e);
            context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
        }
    }

    private void consultarEnteRececeptorPorCollName(RoutingContext context) {
        try {
            this.obtenerHeaders(context);
            Integer collName = context.request().getParam(PROPERTY_COLL_NAME) != null ? Integer.parseInt(context.request().getParam(PROPERTY_COLL_NAME)) : null;
            if (collName != null) {
                serviceEnteReceptorInerfaceVertWebClient.consultaEnteReceptorDeclaracionPorCollName(collName).setHandler(result -> {
                    if (result.succeeded()) {
                        context.response().end(result.result().toString());
                    } else {
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                    }
                });
            } else {
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    private void agregaEnteReceptor(RoutingContext context) {
        this.obtenerHeaders(context);
        try {
            serviceEnteReceptorInerfaceVertWebClient.agregaEnteReceptorDeclaracion(context.getBodyAsJson()).setHandler(result -> {
                if (result.succeeded()) {
                    context.response().end(Json.encode(result.result()));
                } else {
                    context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                }
            });
        } catch (Exception e) {
            LOGGER.error(e);
        }

    }

    private void consultarEnte(RoutingContext context) {
        this.obtenerHeaders(context);
        try {
            serviceEnteReceptorInerfaceVertWebClient.validarBody(context.getBodyAsJson()).setHandler(modulo -> {
                if (modulo.succeeded()) {
                    if (modulo.result().getErrores().isEmpty()) {
                        serviceEnteReceptorInerfaceVertWebClient.consultarServicioEnte(context.getBodyAsJson()).setHandler(result -> {
                            if (result.succeeded()) {
                                context.response().end(result.result().getJsonObject(DATA).toString());
                            } else {
                                context.response().setStatusCode(HttpResponseStatus.CONFLICT.code()).end();
                            }
                        });
                    } else {
                        context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(Json.encode(modulo.result()));
                    }
                } else {
                    context.response().setStatusCode(HttpResponseStatus.CONFLICT.code()).end();
                }
            });

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
        }
    }

    private void consultarTodosEntes(RoutingContext context){
        serviceEnteReceptorInerfaceVertWebClient.consultarTodos("ENTES").setHandler(result -> {
                if (result.succeeded()) {                    
                    context.response().end(result.result().toBuffer());
                } else {
                    LOGGER.error(result.cause());
                    context.response().setStatusCode(HttpResponseStatus.CONFLICT.code()).end();
                }
            });
    }
    

    /**
     * Se agregan los headers correspondientes para el Intercambio de recursos
     * de origen cruzado (CORS).
     *
     * @param routingContext contexto
     * @return HttpServerResponse
     */
    private HttpServerResponse obtenerHeaders(RoutingContext routingContext) {
        return routingContext.response().putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                .putHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS)
                .putHeader(ACCESS_CONTROL_ALLOW_METHODS, HEADER_ACCESS_CONTROL_ALLOW_METHODS)
                .putHeader(ACCESS_CONTROL_ALLOW_HEADERS, HEADER_ACCESS_CONTROL_ALLOW_HEADERS)
                .putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON);
    }

    
    /**
	 * Funcion para inicializar la respuesta
	 *
	 * @param routingContext: RoutingContext
	 *
	 * @author programador04
	 * @since 11/05/2020
	 */
	private void inicializarResponse(RoutingContext routingContext) {
		routingContext.response().setStatusCode(200)
				.putHeader("content-type", "application/json; charset=utf-8");
	}
	
    /**
     * Headers permitidos para CORS
     *
     * @return Set<String> Headers permitidos en las peticiones
     */
    private static Set<String> getAllowedHeaders() {
        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add(HEADER_CONTENT_TYPE);
        return allowedHeaders;
    }

    /**
     * Main para pruebas locales
     *
     * @param args
     */
    public static void main(String[] args) {
        Vertx vertex = Vertx.vertx();
        vertex.deployVerticle(new ServerLauncher());
    }
}
