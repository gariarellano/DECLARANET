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
import static mx.gob.sfp.dgti.util.Constantes.TEXT_HTML;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_ESTRUCTURA;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_PROCESO_SOLICITUD;
import static mx.gob.sfp.dgti.util.Constantes.PATH_HISTORIAL;
import static mx.gob.sfp.dgti.util.Constantes.PATH_CONSULTA_GABINETE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_TIPO_GABINETE;
import static mx.gob.sfp.dgti.util.Constantes.PATH_BUSQUEDA_SP;

import java.util.HashSet;
import java.util.Set;
import mx.gob.sfp.dgti.util.Helper;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
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
import mx.gob.sfp.dgti.daopxy.DAOExpuestosInterface;
import mx.gob.sfp.dgti.service.ServiceConsultaPublicaInterface;
import mx.gob.sfp.dgti.util.Constantes;
import static mx.gob.sfp.dgti.util.Constantes.CONSULTA_DECLARACION_VALUE;
import static mx.gob.sfp.dgti.util.Constantes.PATH_DECLA_PUBLICA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_BUSQUEDA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_COLL_NAME;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ID_USR_DECNET;

public class Server extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private static final String MESSAGE_TEST = "Servicio para la búsqueda de declaraciones presentadas";
    private static Router router;
    private WebClient client;
    private ServiceConsultaPublicaInterface serviceConsultaPublicaInterface;
    private DAOExpuestosInterface daoExpuestosInterface;
    public static final String CURPS_ADMIN = System.getenv("CURPS_ADMIN");

    
    /**
     * Método principal sobrescrito de un vertical para que inicialice la instancia.
     * 
     * @param vertx objeto principal con el que trabaja todo el aplicativo
     * @param context 
     */
    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        router = Router.router(vertx);
        router.route()
                .handler(CorsHandler.create(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).allowedHeaders(getAllowedHeaders()));
        router.route(PATH).handler(routingContext -> {
            HttpServerResponse httpResponse = routingContext.response();
            httpResponse.putHeader(HEADER_CONTENT_TYPE, TEXT_HTML)
            .end(MESSAGE_TEST);
        });
        client = WebClient.create(vertx);
        this.getVertx().createHttpServer().requestHandler(router::accept).listen(config().getInteger(CONFIG_PORT, 5000),
                ar -> {
                    if (ar.succeeded()) {
                        LOGGER.info(MESSAGE_TEST);
                    } else {
                        LOGGER.info(ar.cause());
                    }
                });
        serviceConsultaPublicaInterface = ServiceConsultaPublicaInterface.init(vertx, client);
        daoExpuestosInterface = DAOExpuestosInterface.create(vertx);
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
        router.route().handler(BodyHandler.create());
        router.post(PATH_BUSQUEDA_SP).handler(this::busquedaServidorSp);
        router.post(PATH_HISTORIAL).handler(this::busquedaHistorial);
        router.post(PATH_DECLA_PUBLICA).handler(this::consultaDeclaracionPublica);
        router.get(PATH_DECLA_PUBLICA).handler(this::consultaDeclaracionPublicaInai);
        
        router.post(PATH_CONSULTA_GABINETE).handler(this::consultaDeclaracionGabinete);
        
        router.get(Constantes.PATH_CONSULTA_DATOS_EXPUESTOS).handler(this::consultaDatosExpuestos);
	}
    
    /**
     * Método para buscar servidores publicos con declaraciones presentadas
     *
     * @param routingContext
     */
    private void busquedaServidorSp(RoutingContext routingContext) {
        obtenerHeaders(routingContext);
        	 if ((routingContext.request().params().get(PROP_BUSQUEDA) != null && !routingContext.request().params().get(PROP_BUSQUEDA).isEmpty())
                     && routingContext.request().params().get(PROP_COLL_NAME) != null) {
                 serviceConsultaPublicaInterface.buscarServidorPublicoSp(routingContext.request().params().get(PROP_BUSQUEDA).trim(), Integer.parseInt(routingContext.request().params().get(PROP_COLL_NAME))).setHandler(busqueda -> {
                     if (busqueda.succeeded()) {
                         routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                                 .end(Json.encode(busqueda.result()));
                     } else {
                         routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                                 .end(Json.encode(Helper.crearRespuestaErrorEstructura(ERROR_PROCESO_SOLICITUD)));
                         LOGGER.error(busqueda.cause().getMessage());
                     }
                 });
             } else {
                 routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                         .end(Json.encode(Helper.crearRespuestaErrorEstructura(ERROR_ESTRUCTURA)));
             }
    }

    /**
     * Método para buscar historial de declaraciones de un SP
     *
     * @param routingContext
     */
    private void busquedaHistorial(RoutingContext routingContext) {
        obtenerHeaders(routingContext);
        if (routingContext.request().params().get(PROP_ID_USR_DECNET) != null && routingContext.request().params().get(PROP_COLL_NAME) != null) {
            serviceConsultaPublicaInterface.buscarHistorialServidorPublico(Integer.parseInt(routingContext.request().params().get(PROP_ID_USR_DECNET)), Integer.parseInt(routingContext.request().params().get(PROP_COLL_NAME))).setHandler(busqueda -> {
                if (busqueda.succeeded()) {
                    routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                            .end(Json.encode(busqueda.result()));
                } else {
                    routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                            .end(Json.encode(Helper.crearRespuestaErrorEstructura(ERROR_PROCESO_SOLICITUD)));
                    LOGGER.error(busqueda.cause().getMessage());
                }
            });
        } else {
            routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                    .end(Json.encode(Helper.crearRespuestaErrorEstructura(ERROR_ESTRUCTURA)));
        }
    }
    
    /**
     * Método para realizar las consultas de las declaraciones de gabinete.
     * 
     * @param routingContext 
     */
    private void consultaDeclaracionGabinete(RoutingContext routingContext) {
        obtenerHeaders(routingContext);
        if (routingContext.request().params().get(PROP_TIPO_GABINETE) != null) {
            serviceConsultaPublicaInterface.consultaGabinete(Integer.parseInt(routingContext.request().params().get(PROP_TIPO_GABINETE))).setHandler(busqueda -> {
                if (busqueda.succeeded()) {
                    routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                            .end(Json.encode(busqueda.result()));
                } else {
                    routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                            .end(Json.encode(Helper.crearRespuestaErrorEstructura(ERROR_PROCESO_SOLICITUD)));
                    LOGGER.error(busqueda.cause().getMessage());
                }
            });
        } else {
            routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                    .end(Json.encode(Helper.crearRespuestaErrorEstructura(ERROR_ESTRUCTURA)));
        }
    }

    /**
     * Método que realiza la consutla de la declaracion publica.
     * @param routingContext 
     */
    private void consultaDeclaracionPublica(RoutingContext routingContext) {
        obtenerHeaders(routingContext);
        client.postAbs(CONSULTA_DECLARACION_VALUE)
                .timeout(11000)
                .sendJsonObject(routingContext.getBodyAsJson(), response -> {
                    if (response.succeeded() && response.result().statusCode() == HttpResponseStatus.OK.code()) {
                        routingContext
                        .response()
                        .setStatusCode(HttpResponseStatus.OK.code())
                        .end(response.result().bodyAsBuffer());
                    } else {
                        if (!response.succeeded()) {
                            LOGGER.error(String.format("Error al obtener la declaracion %s", response.cause()));
                        }
                        if (response.succeeded()) {
                            LOGGER.error(String.format("Error al obtener la declaracion estatus: %s", response.result().statusCode()));
                        }
                    }
                });
    }
    
    /**
     * Método que recibe la instrucción de consultar la declaración
     * de Inai, para devolver la declaracion pública para inai.
     * 
     * @param routingContext 
     * @author cgarias
     */
    private void consultaDeclaracionPublicaInai(RoutingContext routingContext) {
        obtenerHeaders(routingContext);
        client.getAbs(CONSULTA_DECLARACION_VALUE)
                .addQueryParam("dI", routingContext.request().getParam("dI"))
                .timeout(11000)
                .send(response -> {
                    if (response.succeeded() && response.result().statusCode() == HttpResponseStatus.OK.code()) {
                        routingContext
                        .response()
                        .setStatusCode(HttpResponseStatus.OK.code())
                        .end(response.result().bodyAsBuffer());
                    } else {
                        if (!response.succeeded()) {
                            LOGGER.error(String.format("Error al obtener la declaracion publica inai %s", response.cause()));
                        }
                        if (response.succeeded()) {
                            LOGGER.error(String.format("Error al obtener la declaracion publca inai estatus: %s", response.result().statusCode()));
                        }
                    }
                });
    }
    
    /**
     * Método que realiza la consulta de la incidencia de los datos 
     * expuestos.
     * 
     * @param routingContext 
     * @author cgarias
     */
    public void consultaDatosExpuestos(RoutingContext routingContext){
        if (routingContext.request().getParam("collName") != null &&
                routingContext.request().getParam("CURP") != null && !routingContext.request().getParam("CURP").isEmpty() &&
                routingContext.request().getParam("RFC") != null && !routingContext.request().getParam("RFC").isEmpty()){
            daoExpuestosInterface.consultaDatosExpuestos(routingContext.request().getParam("RFC"), routingContext.request().getParam("CURP"), resultado->{
                if (resultado.succeeded()){
                    obtenerHeaders(routingContext);
                    routingContext.response().setStatusCode(HttpResponseStatus.OK.code()).end((resultado.result() != null ? resultado.result() : new JsonObject()).encode());
                }else{
                    routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                }
            });
        }else{
            routingContext.response()
                    .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                    .end("Parametros incorrectos.");    
        }
    }
    
    public boolean ifTimeValido(String timeResponse) {
    	boolean valido;
    	LOGGER.info("dataTimeResponse:"+timeResponse);
    	if(timeResponse == null ) {
    		valido = false;
    	}else {
    		 long resta = System.currentTimeMillis()-Long.parseLong(timeResponse);
    		 LOGGER.info("Resta ms:"+resta);
    		 if(resta < 60000) {
    			 valido = true;
    		 }else {
    			 valido = false;
    		 }
    	}
    	return valido;
    }
    /**
     * Headers permitidos para CORS
     *
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
                .putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON)
                ;
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
