/**
 * @(#)ServerVerticle.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.recepdeclaracion.server;


import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Promise;
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
import mx.gob.sfp.dgti.recepdeclaracion.servicioext.LlamadorCatalogos;
import mx.gob.sfp.dgti.recepdeclaracion.util.Constantes;
import mx.gob.sfp.dgti.recepdeclaracion.util.EnumCampos;
import mx.gob.sfp.dgti.recepdeclaracion.util.EnumEtiquetas;
import mx.gob.sfp.dgti.recepdeclaracion.util.EnumProxies;

import java.util.HashSet;
import java.util.Set;

import static io.vertx.core.http.HttpMethod.GET;
import static mx.gob.sfp.dgti.recepdeclaracion.util.Constantes.HEADER_CAHCE_CONTOL_INMUTABLE;
import static mx.gob.sfp.dgti.recepdeclaracion.util.Constantes.PRAGMA;
import mx.gob.sfp.dgti.recepdeclaracion.util.EnumRespuestas;

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
     * Path principal
     */
    private static final String PATH_CATALOGO = "/catalogo";

    /**
     * Path principal
     */
    private static final String PATH_CATALOGOS = "/catalogos";

    /**
     * Path principal
     */
    private static final String PATH_VALIDAR = "/validar";
    
    private static JsonObject catalogos;

    private mx.gob.sfp.dgti.recepdeclaracion.servicioext.reactivex.LlamadorCatalogos llamadorCatalogos;

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerVerticle.class);

    @Override
    public void start(Promise<Void> promise) throws Exception {

        llamadorCatalogos = LlamadorCatalogos
                .createProxy(vertx, EnumProxies.SERVICE_ADDRESS.getValor());

        Router router = Router.router(this.getVertx());

        router.route().handler(CorsHandler.create(Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                .allowedHeaders(getAllowedHeaders()).allowedMethods(getAllowedMethods()));

        router.route(PATH).method(GET).handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader(Constantes.HEADER_CONTENT_TYPE, Constantes.TEXT_HTML).end("Hola CATS");
        });

        router.route().handler(BodyHandler.create());
        router.get(PATH_CATALOGO).handler(this::consultarCatalogo);
        router.get(PATH_CATALOGOS).handler(this::consultarCatalogos);
        router.post(PATH_VALIDAR).handler(this::validarCatalogo);
        router.route().handler(StaticHandler.create());

        this.getVertx().createHttpServer().requestHandler(router)
                .listen(config().getInteger(Constantes.CONFIG_PORT,
                        Integer.parseInt(System.getenv(Constantes.SERVER_PORT) != null ?
                        System.getenv(Constantes.SERVER_PORT) : "5000")), ar -> {
                    if(ar.succeeded()) {
                        LOGGER.info("Escuchando en puerto..." + System.getenv(Constantes.SERVER_PORT));
                        promise.complete();
                    } else {
                        LOGGER.error(ar.cause());
                        promise.fail(ar.cause());
                    }
                });
    }



    /**
     * Validador del módulo de datos generales
     * @param context: contexto de la petición que contiene los stringParams, header y body de la petición
     */
    private void consultarCatalogo(RoutingContext context) {

        obtenerHeaders(context);
        String catalogo =  context.request().getParam(EnumCampos.CAT.getValor()) ;
        if(catalogo != null && !catalogo.isEmpty()) {
            try {
                llamadorCatalogos.rxLlamarCatalogo(catalogo).subscribe(

                        respuesta -> {
                            context.response().end(respuesta.toString());
                        },
                        error -> {
                            LOGGER.info(EnumEtiquetas.OCURRIO_ALGO.getValor() + error.getMessage() + error.getCause());
                            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
                        }
                );

            } catch (Exception e) {
                LOGGER.info(EnumEtiquetas.OCURRIO_ALGO.getValor() + e.getMessage() + e.getCause());
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
            }
        } else {
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }

    /**
     * Validador del módulo de datos generales
     * @param context: contexto de la petición que contiene los stringParams, header y body de la petición
     */
    private void consultarCatalogos(RoutingContext context) {

        obtenerHeaders(context);
        try {
            llamadorCatalogos.rxLlamarTodosCatalogos().subscribe(
                    respuesta -> {
                        if ( respuesta!= null && !respuesta.isEmpty() && !EnumRespuestas.CAT_NO_DISPONIBLE.name().equals(respuesta.getValue(EnumCampos.ESTADO.getValor()))){
                            context.response().putHeader(Constantes.CACHE_CONTROL,HEADER_CAHCE_CONTOL_INMUTABLE);
                            context.response().headers().remove(PRAGMA);                            
                            context.response().end(respuesta.encode());
                            catalogos = respuesta;
                        }else{
                            if (catalogos != null){
                                context.response().putHeader(Constantes.CACHE_CONTROL,HEADER_CAHCE_CONTOL_INMUTABLE);
                                context.response().headers().remove(PRAGMA);
                                context.response().end(catalogos.encode());
                            }else{
                                context.response().end(respuesta.encode());
                            }
                        }
                    },
                    error -> {
                        LOGGER.info(EnumEtiquetas.OCURRIO_ALGO.getValor() + error.getMessage() +
                                error.getCause());
                        context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
                    }
            );

        } catch (Exception e) {
            LOGGER.info(EnumEtiquetas.OCURRIO_ALGO.getValor() + e.getMessage() + e.getCause());
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }

    }

    /**
     * Validador del módulo de datos generales
     * @param context: contexto de la petición que contiene los stringParams, header y body de la petición
     */
    private void validarCatalogo(RoutingContext context) {
        obtenerHeaders(context);
        if(!context.getBodyAsString().equals("")) {
            try {
                JsonObject parametros = new JsonObject(context.getBodyAsString());

                llamadorCatalogos.rxValidarCatalogo(
                        parametros.getString(EnumCampos.CATALOGO.getValor()),
                        parametros.getJsonObject(EnumCampos.INFO.getValor())).subscribe(

                        respuesta -> {
                            context.response().end(respuesta.toString());
                        },
                        error -> {
                            LOGGER.info(EnumEtiquetas.OCURRIO_ALGO.getValor() + error.getMessage() + error.getCause());
                            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
                        }
                );

            } catch (Exception e) {
                LOGGER.info(EnumEtiquetas.OCURRIO_ALGO.getValor() + e.getMessage() + e.getCause());
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
            }
        } else {
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }
    
    /**
     * Headers permitidos para CORS
     * @return
     */
    private Set<String> getAllowedHeaders() {
        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add(Constantes.X_REQUESTED_WITH);
        allowedHeaders.add(Constantes.ACCESS_CONTROL_ALLOW_ORIGIN);
        allowedHeaders.add(Constantes.HEADER_CONTENT_TYPE);
        allowedHeaders.add(Constantes.ACCEPT);
        allowedHeaders.add(Constantes.AUTHORIZATION);
        allowedHeaders.add(Constantes.X_PINGARUNER);
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