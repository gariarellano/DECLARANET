/**
 * @(#)ServerVerticle.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.server;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
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
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.appi.ip.AppiExtensionPermisosSFP;
import mx.gob.sfp.dgti.appi.ip.enu.EnumPathsEnables;
import mx.gob.sfp.dgti.omextback.exception.SFPException;
import mx.gob.sfp.dgti.omextback.graphql.AsyncGraphQLExec;
import mx.gob.sfp.dgti.omextback.util.constantes.Constantes;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumError;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumRespuestaError;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.vertx.core.http.HttpMethod.GET;
import static mx.gob.sfp.dgti.omextback.util.constantes.Constantes.*;

/**
 * Verticle para el Server
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
public class ServerVerticle extends AbstractVerticle {

    /**
     * Path principal
     */
    private static final String PATH = "/";

    /**
     * Path principal que empieza con api
     */
    private static final String PATH_API = "/api/";

    /**
     * Path para GraphQL
     */
    private static final String GRAPHQL = "/*";

    /**
     * graphiql
     */
    private static final String GRAPHIQL = "graphiql";

    /**
     * graphiql path
     */
    private static final String GRAPHIQL_PATH = "/graphiql";

    /**
     * graphiql route
     */
    private static final String GRAPHIQL_ROUTE = "/graphiql/*";

    /**
     * path inicio
     */
    public static final String PATH_INICIAR = "/inicio/iniciar";

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
     * User-Agent para mostrar en caso de error
     */
    private static final String USER_AGENT = "User-Agent";

    /**
     * Implementacion asincrona de GraphQL
     */
    private AsyncGraphQLExec asyncGraphQL;

    /**
     * WebCLient
     */
    private WebClient client;

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerVerticle.class);

    /**
     * Objetos para iniciar
     */
    private static final String AMBIENTE = "AMBIENTE";
    private static final String AMBIENTE_VALUE = System.getenv(AMBIENTE) != null ? System.getenv(AMBIENTE) : EnumPathsEnables.STAGING.name();
    private static final String ID_CLIENTE = System.getenv("ID_CLIENTE");
    private static final String SECRET = System.getenv("SECRET");
    private final AppiExtensionPermisosSFP appi = new AppiExtensionPermisosSFP();

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        client = WebClient.create(vertx);

        asyncGraphQL = AsyncGraphQLExec.create(vertx);

        Router router = Router.router(this.getVertx());
        router.route()
                .handler(CorsHandler.create(Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                .allowedHeaders(getAllowedHeaders()).allowedMethods(getAllowedMethods()));

        router.route(PATH).method(GET).handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader(Constantes.HEADER_CONTENT_TYPE, Constantes.TEXT_HTML).end("Hola OmextBack.");
        });

        router.route(GRAPHQL).handler(BodyHandler.create());

        router.post(PATH_INICIAR).handler(this::iniciar);
        router.post(PATH).handler(this::createGraphQL);
        router.route(PATH_API).handler(this::createGraphQL);

        StaticHandler staticHandler = StaticHandler.create(GRAPHIQL);
        staticHandler.setDirectoryListing(false);
        staticHandler.setCachingEnabled(false);
        staticHandler.setIndexPage(GRAPHIQL_TEMPLATE);
        router.route(GRAPHIQL_ROUTE).method(GET).handler(staticHandler);

        this.getVertx().createHttpServer().requestHandler(router::accept)
                .listen(config().getInteger(
                        Constantes.CONFIG_PORT,
                        Integer.parseInt(System.getenv(Constantes.SERVER_PORT) != null ?
                        System.getenv(Constantes.SERVER_PORT) : "5000")
                ), ar -> {
                    if(ar.succeeded()) {
                        LOGGER.info("Escuchando en puerto..." + 5000);
                        startFuture.complete();
                    } else {
                        LOGGER.error(ar.cause());
                        startFuture.fail(ar.cause());
                    }
                });
    }

    /**
     * Funcion de iniciar, utiliza librerías externas
     *
     * @param routingContext
     */
    private void iniciar(RoutingContext routingContext) {
        obtenerHeaders(routingContext);
        JsonObject datosInicio = routingContext.getBodyAsJson();
        if (datosInicio.getString(ACCES_TOKEN) != null && datosInicio.getString(TRANSACTION) != null
                && datosInicio.getInteger(COLL_NAME) != null) {
            appi.sfpPetitionToIpPermisos(
                    client.getDelegate(),
                    AMBIENTE_VALUE,
                    datosInicio.getString(ACCES_TOKEN),
                    datosInicio.getString(TRANSACTION),
                    ID_CLIENTE,
                    SECRET,
                    datosInicio.getInteger(COLL_NAME),  response->{
                        if (response.succeeded()){
                            JsonObject respuesta= response.result();
                            String authorization = respuesta.getString("authorization");
                            respuesta.remove("authorization");
                            routingContext.response().putHeader(HEADER_AUTHORIZATION_KEY, authorization).end(respuesta.encode());
                        }else{
                            routingContext.response().setStatusCode(HttpResponseStatus.UNAUTHORIZED.code()).end("Validacion token transaccion incorrecta.");
                        }
                    });
        }else{
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Error de parametros iniciales \""+ACCES_TOKEN+"\" \""+TRANSACTION+"\" \""+COLL_NAME+"\"");
        }
    }


    /**
     * Funcion que manda los datos a resolver con GraphQL
     *
     * @param context: informacion enviada
     */
    private void createGraphQL(RoutingContext context) {
        obtenerHeaders(context);
        JsonObject bodyJson = context.getBodyAsJson();
        String query = bodyJson.getString(QUERY);
        String operationName = bodyJson.getString(OPERATION_NAME);
        JsonObject bodyVariables = bodyJson.getJsonObject(VARIABLES);
        Map<String, Object> variables = new HashMap<>();
        if (bodyVariables!= null && !bodyVariables.isEmpty()) {
            variables = bodyVariables.getMap();
        }

        asyncGraphQL.executeQuery(query, operationName, context, variables).setHandler(resultado -> {

            if (resultado.succeeded()) {
                JsonObject json = resultado.result();
                context.response().end(new JsonObject().put(DATA, json).encode());

            } else {
                Map<String, Object> res = new HashMap<>();
                res.put(DATA, null);
                if (resultado.cause() instanceof SFPException) {
                    SFPException ex = (SFPException) resultado.cause();
                    res.put(ERROR, crearRespuestaError(ex, context, bodyVariables, query, operationName));
                    context.response().setStatusCode(ex.getDebugInfo().getInteger("statusHttp"))
                            .end(new JsonObject(res).encode());
                } else {
                    context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end(new JsonObject(res).encode());
                }
            }
        });
    }

    /**
     * Headers permitidos para CORS
     * @return
     */
    private Set<String> getAllowedHeaders() {
        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add(Constantes.ACCESS_CONTROL_ALLOW_ORIGIN);
        allowedHeaders.add(Constantes.HEADER_CONTENT_TYPE);
        allowedHeaders.add(Constantes.ACCEPT);
        allowedHeaders.add(Constantes.HEADER_AUTHORIZATION);
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
     *
     * @param routingContext
     *
     * @return
     */
    public static HttpServerResponse obtenerHeaders(RoutingContext routingContext) {
        return routingContext.response().putHeader(Constantes.ACCESS_CONTROL_ALLOW_ORIGIN, Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                .putHeader(Constantes.ACCESS_CONTROL_ALLOW_CREDENTIALS, Constantes.HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS)
                .putHeader(Constantes.ACCESS_CONTROL_ALLOW_METHODS, Constantes.HEADER_ACCESS_CONTROL_ALLOW_METHODS)
                .putHeader(Constantes.ACCESS_CONTROL_ALLOW_HEADERS, Constantes.HEADER_ACCESS_CONTROL_ALLOW_HEADERS)
                .putHeader(Constantes.ACCESS_CONTROL_EXPOSE_HEADERS, Constantes.HEADER_AUTHORIZATION_KEY)
                .putHeader(Constantes.CACHE_CONTROL, Constantes.HEADER_CACHE_CONTROL).putHeader(Constantes.PRAGMA, Constantes.HEADER_PRAGMA)
                .putHeader(Constantes.HEADER_CONTENT_TYPE, Constantes.HEADER_CONTENT_APPLICATION_JSON);
    }

    /**
     * Funcion que en caso de error crea las respuestas de error y de paso lo agrega en el log
     *
     * @param e excepcion
     * @param context context
     * @param bodyVariables los parametros mandados
     *
     * @return objeto json para responder
     *
     * @author pavel.martinez
     * @since 05/06/2020
     */
    private JsonObject crearRespuestaError(SFPException e, RoutingContext context, JsonObject bodyVariables, String query,
                                           String operationName) {

        LOGGER.info(EnumRespuestaError.LINEA.getValor());
        LOGGER.info(EnumRespuestaError.USER_AGENT.getValor() + context.request().getHeader(USER_AGENT));
        LOGGER.info(EnumRespuestaError.OPERACION.getValor() + operationName);
        LOGGER.info(EnumRespuestaError.QUERY.getValor() + query);
        LOGGER.info(EnumRespuestaError.PARAMETROS_ENVIADOS.getValor() + bodyVariables);
        e.logError(LOGGER);

        if (e.failureCode() == EnumError.ERROR_GRAPHQL.getId().intValue()) {
            return new JsonObject()
                    .put(EnumRespuestaError.ID.getValor(), e.failureCode())
                    .put(EnumRespuestaError.DESCRIPCION.getValor(), e.getDebugInfo().getString("descripcion"))
                    .put(EnumRespuestaError.MENSAJE_GRAPHQL.getValor(), e.getDebugInfo().getString("mensaje"));
        } else {
            return new JsonObject()
                    .put(EnumRespuestaError.ID.getValor(), e.failureCode())
                    .put(EnumRespuestaError.DESCRIPCION.getValor(), e.getDebugInfo().getString("descripcion"));
        }
    }

}