package mx.gob.sfp.dgti.verticle;

import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_PORT;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_AUTHORIZATION_KEY;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_APPLICATION_JSON;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_TYPE;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_DOMINIO;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_TRANSACCION;
import static mx.gob.sfp.dgti.util.Constantes.PATH;
import static mx.gob.sfp.dgti.util.Constantes.PATH_BUSQUEDA;
import static mx.gob.sfp.dgti.util.Constantes.PATH_OBTENER_ACUSE;
import static mx.gob.sfp.dgti.util.Constantes.PATH_OBTENER_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.PATH_OBTENER_DOMICILIOS;
import static mx.gob.sfp.dgti.util.Constantes.TEXT_HTML;
import static mx.gob.sfp.dgti.util.Constantes.PROP_NOMBRE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ID_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_PRIMER_APELLIDO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_SEGUNDO_APELLIDO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_CURP;
import static mx.gob.sfp.dgti.util.Constantes.PROP_RFC;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ID_USR_DECNET;
import static mx.gob.sfp.dgti.util.Constantes.PROP_COLL_NAME;
import static mx.gob.sfp.dgti.util.Constantes.PROP_HOMOCLAVE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_EMAIL;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_VERIFICAR_DATOS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_NUM_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_OBTENER_DOMICILIOS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_JUSTIFICACION;

import java.util.HashSet;
import java.util.Set;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.reactivex.core.AbstractVerticle;
import mx.gob.sfp.dgti.appi.ip.AppiExtensionPermisosSFP;
import mx.gob.sfp.dgti.appi.ip.enu.EnumPathsEnables;
import mx.gob.sfp.dgti.services.ServiceConsultaHistorialInterface;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_EXPOSE_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.ACCES_TOKEN;
import static mx.gob.sfp.dgti.util.Constantes.COLL_NAME;
import static mx.gob.sfp.dgti.util.Constantes.PATH_INICIAR;
import static mx.gob.sfp.dgti.util.Constantes.TRANSACTION;
import mx.gob.sfp.dgti.util.Helper;

/**
 * Clase que permite realizar los llamados a los path's del microservicio y
 * poder obtener los resultados.
 *
 * @author programador04@sfp.gob.mx
 * @since 09/07/2020
 */
public class Server extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private static final String AMBIENTE = "AMBIENTE";
    private static final String AMBIENTE_VALUE = System.getenv(AMBIENTE) != null ? System.getenv(AMBIENTE) : EnumPathsEnables.STAGING.name();
    private static final String ID_CLIENTE = System.getenv("ID_CLIENTE");
    private static final String SECRET = System.getenv("SECRET");
    private static final String MESSAGE_TEST = "Servicio para la búsqueda de declaraciones presentadas";
    public static final String EMPTY = "";
    private final AppiExtensionPermisosSFP appi = new AppiExtensionPermisosSFP();
    private static Router router;
    private static ServiceConsultaHistorialInterface serviceConsultaHistorialInterface;
    private WebClient client;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        router = Router.router(vertx);
        client = WebClient.create(vertx);
        router.route(PATH).handler(routingContext -> {
            HttpServerResponse httpResponse = routingContext.response();
            httpResponse.putHeader(HEADER_CONTENT_TYPE, TEXT_HTML).end(MESSAGE_TEST);
        });

        this.getVertx().createHttpServer().requestHandler(router::accept).listen(config().getInteger(CONFIG_PORT, 5000),
                ar -> {
                    if (ar.succeeded()) {
                        LOGGER.info(MESSAGE_TEST);
                    } else {
                        LOGGER.info(ar.cause());
                    }
                });
        serviceConsultaHistorialInterface = ServiceConsultaHistorialInterface.init(vertx, client);
    }

    /**
     * Start inicializar router y la definición de paths
     *
     * @param future
     * @throws Exception
     */
    @Override
    public void start(Future<Void> future) throws Exception {
        super.start(future);
        router.route().handler(CorsHandler.create(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).allowedHeaders(getAllowedHeaders()));
        router.route().handler(BodyHandler.create());
        router.post(PATH_INICIAR).handler(this::iniciar);
        router.post(PATH_BUSQUEDA).handler(this::busqueda);
        router.post(PATH_OBTENER_DECLARACION).handler(this::obtenerDeclaracion);
        router.post(PATH_OBTENER_ACUSE).handler(this::obtenerAcuse);
        router.post(PATH_OBTENER_DOMICILIOS).handler(this::obtenerDomicilios);
        router.route().handler(StaticHandler.create());
    }

    private void iniciar(RoutingContext routingContext) {
        obtenerHeaders(routingContext);
        JsonObject datosInicio = routingContext.getBodyAsJson();
        if (datosInicio.getString(ACCES_TOKEN) != null && datosInicio.getString(TRANSACTION) != null
                && datosInicio.getInteger(COLL_NAME) != null) {
            appi.sfpPetitionToIpPermisos(
                    client, 
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
     * Método para buscar el historial
     *
     * @param routingContext
     */
    private void busqueda(RoutingContext routingContext) {
        obtenerHeaders(routingContext);
        if (!EMPTY.equals(routingContext.getBodyAsString())) {
            try {
                if (routingContext.request().params().size() == 2 && (routingContext.request().params().get(PROP_CURP) != null
                        || routingContext.request().params().get(PROP_RFC) != null || routingContext.request().params().get(PROP_NOMBRE) != null
                        || routingContext.request().params().get(PROP_ID_USR_DECNET) != null) && routingContext.request().params().get(PROP_COLL_NAME) != null
                        && verficarObjetoUsuario(routingContext.getBodyAsJson())) {
                    serviceConsultaHistorialInterface.consultaHistorial(routingContext.request().params().get(PROP_CURP),
                            routingContext.request().params().get(PROP_RFC),
                            routingContext.request().params().get(PROP_NOMBRE),
                            routingContext.request().params().get(PROP_ID_USR_DECNET),
                            Integer.parseInt(routingContext.request().params().get(PROP_COLL_NAME)),
                            routingContext.getBodyAsJson().getJsonObject(PROP_USUARIO)).setHandler(service -> {

                                if (service.succeeded()) {
                                    routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                                    .end(Json.encode(service.result()));
                                } else {
                                    routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                                    .end(Json.encode(Helper.crearRespuestaErrorEstructura("Error al obtener el historial, verifique los datos enviados")));
                                }
                            });
                } else {
                    routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                            .end(Json.encode(Helper.crearRespuestaErrorEstructura(ERROR_VERIFICAR_DATOS)));
                }
            } catch (Exception e) {
                routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                        .end(Json.encode(Helper.crearRespuestaErrorEstructura(ERROR_VERIFICAR_DATOS)));
            }
        } else {
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                    .end(Json.encode(Helper.crearRespuestaErrorEstructura(ERROR_VERIFICAR_DATOS)));
        }

    }

    private void obtenerDeclaracion(RoutingContext routingContext) {
        obtenerHeaders(routingContext);
        if (!EMPTY.equals(routingContext.getBodyAsString())) {
            if (verficarObjetoUsuario(routingContext.getBodyAsJson())) {
                serviceConsultaHistorialInterface.obtenerDeclaracion(routingContext.getBodyAsJson()).setHandler(decla -> {

                    if (decla.succeeded()) {
                        routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                                .end(Json.encode(decla.result()));
                    } else {
                        routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                                .end(Json.encode(Helper.crearRespuestaErrorEstructura("Verifique el envío de los datos ó inténtelo más tarde")));
                    }

                });
            } else {
                routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                        .end(Json.encode(Helper.crearRespuestaErrorEstructura(ERROR_VERIFICAR_DATOS)));
            }

        } else {
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                    .end(Json.encode(Helper.crearRespuestaErrorEstructura(ERROR_VERIFICAR_DATOS)));
        }
    }

    private void obtenerAcuse(RoutingContext routingContext) {
        obtenerHeaders(routingContext);
        if (!EMPTY.equals(routingContext.getBodyAsString())) {
            if (verficarObjetoUsuario(routingContext.getBodyAsJson())) {
                serviceConsultaHistorialInterface.obtenerAcuse(routingContext.getBodyAsJson()).setHandler(acuse -> {

                    if (acuse.succeeded()) {
                        routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                                .end(Json.encode(acuse.result()));
                    } else {
                        routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                                .end(Json.encode(Helper.crearRespuestaErrorEstructura("Verifique el envío de los datos ó inténtelo más tarde")));
                    }

                });
            } else {
                routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                        .end(Json.encode(Helper.crearRespuestaErrorEstructura(ERROR_VERIFICAR_DATOS)));
            }
        } else {
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                    .end(Json.encode(Helper.crearRespuestaErrorEstructura(ERROR_VERIFICAR_DATOS)));
        }
    }

    /**
     * Método para obtener los domicilios de la declaración
     *
     * @param routingContext
     */
    private void obtenerDomicilios(RoutingContext routingContext) {
        if (routingContext.request().params().size() == 3
                && (routingContext.request().params().get(PROP_NUM_DECLARACION) != null && routingContext.request().params().get(PROP_COLL_NAME) != null && routingContext.request().params().get(PROP_ID_USUARIO) != null)
                && verficarObjetoUsuario(routingContext.getBodyAsJson())
                && routingContext.getBodyAsJson().getString(PROP_JUSTIFICACION) != null) {
            serviceConsultaHistorialInterface.obtenerDomicilios(routingContext.request().params().get(PROP_NUM_DECLARACION), Integer.parseInt(routingContext.request().params().get(PROP_COLL_NAME)), routingContext.getBodyAsJson(), Integer.parseInt(routingContext.request().params().get(PROP_ID_USUARIO))).setHandler(domis -> {
                if (domis.succeeded()) {
                    routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                            .end(Json.encode(domis.result()));
                } else {
                    LOGGER.info("ERROR EN OBTEER DOMICILIOS:");
                    LOGGER.info(domis.cause());
                    routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                            .end(Json.encode(Helper.crearRespuestaErrorEstructura(ERROR_OBTENER_DOMICILIOS)));
                }
            });
        } else {
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                    .end(Json.encode(Helper.crearRespuestaErrorEstructura(ERROR_VERIFICAR_DATOS)));
        }
    }

    /**
     * Headers permitidos para CORS
     * @return Set<String> Headers permitidos en las peticiones
     */
    public static Set<String> getAllowedHeaders() {
            Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add(HEADER_DOMINIO);
        allowedHeaders.add(HEADER_AUTHORIZATION_KEY);
        allowedHeaders.add(HEADER_CONTENT_TYPE);
        allowedHeaders.add(HEADER_TRANSACCION);
        return allowedHeaders;
    }

    /**
     * Se agregan los headers correspondientes para el Intercambio de recursos
     * de origen cruzado (CORS).
     *
     * @param routingContext
     * @return
     */
    public static HttpServerResponse obtenerHeaders(RoutingContext routingContext) {
        return routingContext.response().putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                .putHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS)
                .putHeader(ACCESS_CONTROL_ALLOW_METHODS, HEADER_ACCESS_CONTROL_ALLOW_METHODS)
                .putHeader(ACCESS_CONTROL_ALLOW_HEADERS, HEADER_ACCESS_CONTROL_ALLOW_HEADERS)
                .putHeader(ACCESS_CONTROL_EXPOSE_HEADERS, HEADER_AUTHORIZATION_KEY)                
                .putHeader(HEADER_DOMINIO, HEADER_DOMINIO)
                .putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON);
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

    public boolean verficarObjetoUsuario(JsonObject peticion) {
        boolean verificacion = true;
        if (peticion.getJsonObject("usuario") == null || peticion.getJsonObject(PROP_USUARIO).getInteger(PROP_ID_USUARIO) == null
                || peticion.getJsonObject(PROP_USUARIO).getString(PROP_NOMBRE) == null
                || peticion.getJsonObject(PROP_USUARIO).getString(PROP_PRIMER_APELLIDO) == null
                || peticion.getJsonObject(PROP_USUARIO).getString(PROP_SEGUNDO_APELLIDO) == null
                || peticion.getJsonObject(PROP_USUARIO).getString(PROP_CURP) == null
                || peticion.getJsonObject(PROP_USUARIO).getString(PROP_RFC) == null
                || peticion.getJsonObject(PROP_USUARIO).getString(PROP_HOMOCLAVE) == null
                || peticion.getJsonObject(PROP_USUARIO).getString(PROP_EMAIL) == null) {
            verificacion = false;
        }

        return verificacion;
    }
}
