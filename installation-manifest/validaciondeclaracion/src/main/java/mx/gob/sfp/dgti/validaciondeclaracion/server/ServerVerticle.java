/**
 * @(#)ServerVerticle.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.server;


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
import mx.gob.sfp.dgti.declaracion.encrypt.im.ValidaMensajeFront;
import mx.gob.sfp.dgti.validaciondeclaracion.graphql.AsyncExecException;
import mx.gob.sfp.dgti.validaciondeclaracion.graphql.AsyncGraphQLExec;
import mx.gob.sfp.dgti.validaciondeclaracion.util.Constantes;
import mx.gob.sfp.dgti.validaciondeclaracion.util.EnumCampos;
import mx.gob.sfp.dgti.validaciondeclaracion.util.EnumTipoResp;
import mx.gob.sfp.dgti.validaciondeclaracion.util.ValidacionException;

import java.util.*;

import static io.vertx.core.http.HttpMethod.GET;

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

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        client = WebClient.create(vertx);

        asyncGraphQL = AsyncGraphQLExec.create(vertx);

        Router router = Router.router(this.getVertx());
        router.route().handler(CorsHandler.create(Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                .allowedHeaders(getAllowedHeaders()).allowedMethods(getAllowedMethods()));

        router.route(PATH).method(GET).handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader(Constantes.HEADER_CONTENT_TYPE, Constantes.TEXT_HTML).end("Hola Recepcion Declaranet.");
        });

        router.route(GRAPHQL).handler(BodyHandler.create());

        router.post(PATH).handler(this::createGraphQL);
        router.route(PATH_API).handler(this::createGraphQL);

        router.route(GRAPHIQL_PATH).method(GET).handler(rc -> {
            if (GRAPHIQL_PATH.equals(rc.request().path())) {
                rc.response().setStatusCode(302);
                rc.response().headers().set("Location", rc.request().path() + "/");
                rc.response().end("You are in path!!");
            } else {
                rc.next();
            }
        });

        StaticHandler staticHandler = StaticHandler.create(GRAPHIQL);
        staticHandler.setDirectoryListing(false);
        staticHandler.setCachingEnabled(false);
        staticHandler.setIndexPage(GRAPHIQL_TEMPLATE);
        router.route(GRAPHIQL_ROUTE).method(GET).handler(staticHandler);

        this.getVertx().createHttpServer().requestHandler(router::accept)
                .listen(config().getInteger(
                        Constantes.CONFIG_PORT,
                        //5001
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

        try {
            if(ValidaMensajeFront.fidelidadMensaje(
                    bodyVariables.getJsonObject(EnumCampos.PARAMETROS.getNombre())) == null) {
                throw new ValidacionException();
            }
        } catch (Exception e) {
            context.response().setStatusCode(HttpResponseStatus.ACCEPTED.code())
                    .end(EnumTipoResp.ERROR_DIGITO.getMensaje());
            LOGGER.info("=== Digito verificador no coincide.");
            LOGGER.info(e);
            return;
        }

        Map<String, Object> variables = new HashMap<>();
        if (bodyVariables!= null && !bodyVariables.isEmpty()) {
            variables = bodyVariables.getMap();
        }

        asyncGraphQL.executeQuery(query, operationName, context, variables).setHandler(resultado -> {

            if (resultado.succeeded()) {

                JsonObject json = resultado.result();
                formatearRespuesta(json);
                context.response().end(new JsonObject().put(DATA, json).encode());

            } else {
                Map<String, Object> res = new HashMap<>();
                res.put(DATA, null);
                if (resultado.cause() instanceof AsyncExecException) {
                    AsyncExecException ex = (AsyncExecException) resultado.cause();
                    LOGGER.info("=== Problema ejecutando query de GraphQL: " + ex);
                    LOGGER.info("=== User-Agent: " + context.request().getHeader(USER_AGENT));
                    LOGGER.info("=== Parametros con problema: " + bodyVariables);
                    res.put(ERROR, ex.getErrors().toString());
                } else {
                    LOGGER.info("=== Problema ejecutando query de GraphQL: " + resultado.cause().toString());
                    res.put(ERROR, resultado.cause() != null ? Arrays.asList(resultado.cause())
                            : Arrays.asList(new Exception("Error interno")));
                }
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                        .end(new JsonObject(res).encode());

            }
        });
    }

    /**
     * Funcion para dar formato necesario a la declaracion que devolvemos. Se determina si una respuesta coincide con el
     * guardado de una declaracion, aviso por cambio por dependencia o una nota aclaratoria. En esos casos la respuesta
     * recibida desde GraphQL sera un String con el JSON, por lo que es necesario parsear el String a JsonObject. Cuando
     * no es ninguno de estos casos, se deja pasar la respuesta tal cual se recibe de GraphQL
     *
     * @param respuesta: declaracion como JsonObject
     *
     * @author pavel.martinez
     * @since 24/02/2020
     */
    private void formatearRespuesta(JsonObject respuesta) {

        try {
            String nombreQuery = "";

            if (respuesta.getJsonObject(EnumCampos.SERVICIO_GUARDAR_DECLARACION.getNombre()) != null
                || respuesta.getJsonObject(EnumCampos.SERVICIO_GUARDAR_AVISO.getNombre()) != null
                || respuesta.getJsonObject(EnumCampos.SERVICIO_GUARDAR_NOTAS.getNombre()) != null
            ) {
                if (respuesta.getJsonObject(EnumCampos.SERVICIO_GUARDAR_DECLARACION.getNombre()) != null) {
                    nombreQuery = EnumCampos.SERVICIO_GUARDAR_DECLARACION.getNombre();
                } else if (respuesta.getJsonObject(EnumCampos.SERVICIO_GUARDAR_AVISO.getNombre()) != null) {
                    nombreQuery = EnumCampos.SERVICIO_GUARDAR_AVISO.getNombre();
                } else if (respuesta.getJsonObject(EnumCampos.SERVICIO_GUARDAR_NOTAS.getNombre()) != null) {
                    nombreQuery = EnumCampos.SERVICIO_GUARDAR_NOTAS.getNombre();
                }
                else {
                    return;
                }
            } else {
                return;
            }
            if(respuesta.getJsonObject(nombreQuery)
                    .getString(EnumCampos.ESTADO.getNombre()).equals(EnumTipoResp.CORRECTO.name())) {
                JsonObject declaracionJson = new JsonObject(
                        respuesta.getJsonObject(nombreQuery)
                                .getString(EnumCampos.DECLARACION.getNombre()));

                respuesta.getJsonObject(nombreQuery)
                        .put(EnumCampos.DECLARACION.getNombre(), declaracionJson);
            }

        } catch (Exception e) {
            LOGGER.info("=== Error al formatear respuesta, y la respuesta fue: " + respuesta.toString());
        }
        return;
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
     * @param routingContext
     * @return
     */
    public static HttpServerResponse obtenerHeaders(RoutingContext routingContext) {
        return routingContext.response().putHeader(Constantes.ACCESS_CONTROL_ALLOW_ORIGIN, Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                .putHeader(Constantes.ACCESS_CONTROL_ALLOW_CREDENTIALS, Constantes.HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS)
                .putHeader(Constantes.ACCESS_CONTROL_ALLOW_METHODS, Constantes.HEADER_ACCESS_CONTROL_ALLOW_METHODS)
                .putHeader(Constantes.ACCESS_CONTROL_ALLOW_HEADERS, Constantes.HEADER_ACCESS_CONTROL_ALLOW_HEADERS)
                .putHeader(Constantes.CACHE_CONTROL, Constantes.HEADER_CACHE_CONTROL).putHeader(Constantes.PRAGMA, Constantes.HEADER_PRAGMA)
                .putHeader(Constantes.HEADER_CONTENT_TYPE, Constantes.HEADER_CONTENT_APPLICATION_JSON);
    }

}