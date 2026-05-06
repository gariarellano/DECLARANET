/**
 * @Server.java Mar 23, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.verticle;

import static io.vertx.core.http.HttpMethod.GET;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_PORT;
import static mx.gob.sfp.dgti.util.Constantes.EMPTY;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_APPLICATION_JSON;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_TYPE;
import static mx.gob.sfp.dgti.util.Constantes.PATH;
import static mx.gob.sfp.dgti.util.Constantes.PATH_GUARDADO;
import static mx.gob.sfp.dgti.util.Constantes.TEXT_HTML;

import java.util.HashSet;
import java.util.Set;

import io.netty.handler.codec.http.HttpResponseStatus;
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
import mx.gob.sfp.dgti.dao.CatalogoDAO;
import mx.gob.sfp.dgti.dao.GuardaDeclaracionDAO;
import mx.gob.sfp.dgti.enums.CamposPDN;
import mx.gob.sfp.dgti.enums.Encabezados;
import mx.gob.sfp.dgti.service.EstructuraPdnService;
import mx.gob.sfp.dgti.service.EstructuraPdnServiceImpl;
import mx.gob.sfp.dgti.service.NulosAmantener;

/**
 * Clase con la definición de los paths para el guardado de la declaración con
 * formato para PDN
 *
 * @author Miriam Sanchez Sanchez programador07
 * @modifedBy programador09
 * @since 23/03/2020
 */
public class Server extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private EstructuraPdnService estructuraPdnService;
    private CatalogoDAO catalogoDAO;
    private GuardaDeclaracionDAO guardaDeclaracionDao;
    private Router router;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        router = Router.router(vertx);
        catalogoDAO = CatalogoDAO.create(vertx);
        catalogoDAO.obtenerCatalogos();
        router.route().handler(CorsHandler.create(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                .allowedHeaders(getAllowedHeaders()).allowedMethods(getAllowedMethods()));

        router.route(PATH).method(GET).handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader(HEADER_CONTENT_TYPE, TEXT_HTML).end("Hola, guardado con formato PDN.");
        });
        this.getVertx().createHttpServer().requestHandler(router::accept)
                .listen(config().getInteger(CONFIG_PORT, 5000), ar -> {
                    if (ar.succeeded()) {
                        LOGGER.info("Escuchando en puerto..." + 5000);
                    } else {
                        LOGGER.error(ar.cause());
                    }
                });
        estructuraPdnService = new EstructuraPdnServiceImpl();
        guardaDeclaracionDao = GuardaDeclaracionDAO.init(vertx);
    }

    /**
     * Start inicializar router y la definición de paths
     *
     * @param future
     * @throws Exception
     */
    @Override
    public void start(Future<Void> future) throws Exception {
        router.route().handler(BodyHandler.create());
        router.post(PATH_GUARDADO).handler(this::guardarDeclaracionFormatoPdn);
    }

    /**
     * Funcion para el guardado de la declaración
     *
     */
    private void guardarDeclaracionFormatoPdn(RoutingContext context) {
        obtenerHeaders(context);

        if (!EMPTY.equals(context.getBodyAsString())) {

            try {
                Integer collname = context.getBodyAsJson().getJsonObject(CamposPDN.DECLARACION.getCampo())
                        .getJsonObject(Encabezados.INSTITUCION_RECEPTORA.getCampo())
                        .getInteger(Encabezados.COLL_NAME.getCampo());

                String numeroDeclaracion = context.getBodyAsJson().getJsonObject(CamposPDN.DECLARACION.getCampo())
                        .getJsonObject(Encabezados.ENCABEZADO.getCampo())
                        .getString(Encabezados.NUM_DEC.getCampo());

                estructuraPdnService.crearEstructuraDeclaracionPDN(context.getBodyAsJson()).setHandler(estructura -> {

                    if (estructura.succeeded()) {
                        JsonObject estructuraMantener= estructura.result();
                        new NulosAmantener(estructuraMantener);                        
                        guardaDeclaracionDao.guardaDeclaracion(collname, numeroDeclaracion, estructuraMantener,  guardado->{
                            if (guardado.succeeded()){
                                context.response().setStatusCode(HttpResponseStatus.OK.code()).end();
                            }else{
                                LOGGER.error("====ERROR de guarado con la base de datos"+guardado.cause());
                                context.response().setStatusCode(HttpResponseStatus.CONFLICT.code()).end();
                            }
                        });
                    } else {
                        LOGGER.error("===Error con:: " + estructura.cause().getMessage() + " --- " + context.getBodyAsJson());
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                    }
                });
            } catch (Exception e) {
                LOGGER.error("Ocurrio un error " + e.getCause().getMessage() + " --- " + context.getBodyAsJson());
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
            }
        } else {
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
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
     * Métodos permitidos para CORS
     *
     * @return
     */
    private Set<HttpMethod> getAllowedMethods() {
        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.OPTIONS);
        return allowedMethods;
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