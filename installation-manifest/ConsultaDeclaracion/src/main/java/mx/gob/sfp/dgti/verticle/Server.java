/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "ConsultaDeclaracion" Sistema que permite realizar el guardado de declaraciones
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
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import mx.gob.sfp.dgti.service.interfaces.ServiceConsultaDeclaInerface;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_PORT;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_CURP;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_NUMERO_DECLARACION_PRECARGA;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_APPLICATION_JSON;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_TYPE;
import static mx.gob.sfp.dgti.util.Constantes.PATH;
import static mx.gob.sfp.dgti.util.Constantes.PATH_CONSULTAR_HISTORIAL;
import static mx.gob.sfp.dgti.util.Constantes.TEXT_HTML;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_CONSULTA_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_CONSULTA_DECLARACION_DIRECCIONES;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_CONSULTA_DECLARACION_SERVICIO_IMPRESION;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_CONSULTA_DECLA_HIST;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_CONSULTA_FIRMADO_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_CONSULTA_PRECARGA;


/**
 * Clase que coneitne la funcionalidad principal para 
 * inicializar la aplicacion.
 * 
 * @author cgarias
 * @since 06/11/2019
 * @edited by programador04 23/12/2019
 */
public class Server extends AbstractVerticle {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private ServiceConsultaDeclaInerface serviceConsultaDecla;
    private static final String MESSAGE_TEST_SANITY= "Hola consulta decla...";
    private static final String URL_CAT_ORIGEN = "URL_CAT_ORIGEN";
    private static final String URL_CATALOGOS_ORIGEN = System.getenv(URL_CAT_ORIGEN) != null ? System.getenv(URL_CAT_ORIGEN) : "https://dnet-catalogosorigen-staging.dkla8s.funcionpublica.gob.mx/catalogos/";
    private static final String CATALOGOS = "catalogos";
    private static final String CAT_NIVEL_JERARQUICO = "CAT_NIVEL_JERARQUICO";
    private static HashMap<Integer, JsonObject> catNivelPresupuestal = null;
    private Router router;
    private WebClient webClient;
    
    
    
    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        router = Router.router(vertx);
        router.route().handler(CorsHandler.create(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                            .allowedHeaders(getAllowedHeaders()));
        router.route(PATH).handler(routingContext -> {
                    HttpServerResponse response = routingContext.response();
                    response.putHeader(HEADER_CONTENT_TYPE, TEXT_HTML).end(MESSAGE_TEST_SANITY);
            });
        webClient = WebClient.create(vertx);
        this.getVertx().createHttpServer().requestHandler(router).listen(
                    config().getInteger(CONFIG_PORT, 5000), ar -> {
                        if(ar.succeeded()) {
                            LOGGER.info("Running");
                        } else {
                            LOGGER.info(ar.cause());
                        }
                    });
        this.asignaCatNivelJerarquico();
    }
    
    /**
     * Start inicializar router y la definición de paths
     * @param future
     * @throws Exception
     */
    @Override
    public void start(Future<Void> future) throws Exception {
            super.start(future);
            serviceConsultaDecla = ServiceConsultaDeclaInerface.init(vertx, webClient);
            router.route().handler(BodyHandler.create());
            router.post(URL_APPI_CONSULTA_DECLARACION).handler(this::consultarDeclaracion);
            router.post(URL_APPI_CONSULTA_FIRMADO_DECLARACION).handler(this::consultarFirmadoDeclaracion);
            router.post(URL_APPI_CONSULTA_DECLARACION_SERVICIO_IMPRESION).handler(this::consultarDeclaracionSi);
            
            router.post(URL_APPI_CONSULTA_DECLA_HIST).handler(this::consultarDeclaracionHistorica);
            router.post(URL_APPI_CONSULTA_PRECARGA).handler(this::consultarDeclaracionPrecarga);
            //path de consulta historial declaraciones ordenado por fecha encargo.
            router.post(PATH_CONSULTAR_HISTORIAL).handler(this::historialDeclaracionesUsuario);
            //path de la consulta de la declaracion, solo regresa módulos de las direcciones.
            router.post(URL_APPI_CONSULTA_DECLARACION_DIRECCIONES).handler(this::consultarDeclaracioDirecciones);
            
            router.route().handler(StaticHandler.create());
    }

    /**
     * Validador del módulo de datos generales
     * @param context contexto de la petición que contiene los stringParams, header y body de la petición
     */
    private void consultarDeclaracion(RoutingContext context) {
        try{
            this.obtenerHeaders(context);
            serviceConsultaDecla.consultaDeclaracion(//declaracionConsultar
                context.getBodyAsJson()).setHandler(result ->{
                if(result.succeeded()){
                    context.response().end(Json.encode(result.result()));
                }else {
                    context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                }
            });
        }catch(Exception e ){
            LOGGER.error(e);      
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }
    
    private void consultarDeclaracionSi(RoutingContext context) {
        try{
            this.obtenerHeaders(context);            
            serviceConsultaDecla.consultaDeclaracionSi(//declaracionConsultar
                context.getBodyAsJson()).setHandler(result ->{
                if(result.succeeded()){
                    context.response().end(Json.encode(result.result()));
                }else {
                    context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                }
            });
        }catch(Exception e ){
            LOGGER.error(e);      
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }
    
    /**
     * Método para consultar el firmado de la declaración 
     **/
    private void consultarFirmadoDeclaracion(RoutingContext context) {
        try{
            this.obtenerHeaders(context);            
            serviceConsultaDecla.consultarFirmados(
                context.getBodyAsJson()).setHandler(result ->{
                if(result.succeeded()){
                    context.response().end(Json.encode(result.result()));
                }else {
                    context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                }
            });
        }catch(Exception e ){
            LOGGER.error(e);      
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }
    /**
     * Método que realiza la consulta de la declaración ya firmada para obener los headers y toda
     * la información par nota aclaratoria.
     * 
     * @param context contexto de vertx para obtener el request.
     */
    private void consultarDeclaracionHistorica(RoutingContext context) {
        try{
            this.obtenerHeaders(context);            
            serviceConsultaDecla.consultaDeclaracionFirmada(context.getBodyAsJson()).setHandler(result ->{
                if(result.succeeded()){
                    context.response().end(Json.encode(result.result()));
                }else {
                    context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                }
            });
        }catch(Exception e ){
            LOGGER.error(e);
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }
    
    private void consultarDeclaracionPrecarga(RoutingContext context) {
        try{
            this.obtenerHeaders(context);
            if (context.getBodyAsJson().getJsonObject("usuario").getString(FIELD_CURP) != null &&
                    context.getBodyAsJson().getString(FIELD_NUMERO_DECLARACION_PRECARGA) != null &&
                    !context.getBodyAsJson().getString(FIELD_NUMERO_DECLARACION_PRECARGA).isEmpty()){
                this.asignaCatNivelJerarquico();
                serviceConsultaDecla.consultaDeclaracionFirmadaParaPrecarga(context.getBodyAsJson(), catNivelPresupuestal).setHandler(resultado ->{
                    if (resultado.succeeded()){
                        context.response().setStatusCode(HttpResponseStatus.OK.code())
                                .end(resultado.result().toBuffer());
                    }else{
                        context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
                    }
                });
            }else{
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
            }
        }catch(Exception e ){
            LOGGER.error(e);      
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }
    
    /**
     * Método que reagresa el arreglo de historial de declaraciones de un usuario en particular.
     * 
     * @param context 
     */
    private void historialDeclaracionesUsuario(RoutingContext context){        
            try{
                this.obtenerHeaders(context);
                if (context.getBodyAsJson().getInteger("idUsuario") != null &&
                        context.getBodyAsJson().getInteger("collName") != null){
                    serviceConsultaDecla.consultaHistorialUsurio(context.getBodyAsJson()).setHandler(r ->{
                        if (r.succeeded()){
                                context.response().setStatusCode(HttpResponseStatus.OK.code())
                            .end(r.result().toBuffer());
                        }else{
                            LOGGER.info("Error al obtener el historial "+r.cause().getMessage());
                            context.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                                .end("Ocurrio un error al obtener histoal para precarga");
                        }
                    });
                }else{
                    context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();    
                }
            }catch(Exception e){
                LOGGER.error(e);
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
            }
    }
    
    private void consultarDeclaracioDirecciones(RoutingContext context){        
            try{
                this.obtenerHeaders(context);
                if (context.getBodyAsJson().getInteger("idUsuario") != null &&
                        context.getBodyAsJson().getString("numeroDeclaracion") != null &&
                        context.getBodyAsJson().getInteger("collName") != null){
                    serviceConsultaDecla.consultaDeclaracionDirecciones(context.getBodyAsJson()).setHandler(r ->{
                        if (r.succeeded()){
                                context.response().setStatusCode(HttpResponseStatus.OK.code())
                            .end(r.result().toBuffer());
                        }else{
                            LOGGER.info("Error al obtener direcciones declaracion "+r.cause().getMessage());
                            context.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                                .end("Ocurrio un error al obtener las direcciones de la declaracion");
                        }
                    });
                }else{
                    context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();    
                }
            }catch(Exception e){
                LOGGER.error(e);
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
            }
    }
    
    /**
    * Se agregan los headers correspondientes para el Intercambio de recursos de
    * origen cruzado (CORS).
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
     * @return Set<String> Headers permitidos en las peticiones
     */
    private static Set<String> getAllowedHeaders() {
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
    public void asignaCatNivelJerarquico(){
        if (catNivelPresupuestal==null){
            webClient.getAbs(URL_CATALOGOS_ORIGEN)
                .send(response ->{
                    if(response.succeeded() && response.result().statusCode()==HttpResponseStatus.OK.code()){
                        JsonArray catNivelPresupuestalInternal = response.result().bodyAsJsonObject().getJsonObject(CATALOGOS).getJsonArray(CAT_NIVEL_JERARQUICO);
                        catNivelPresupuestal = new HashMap<>();
                        int idRups =1;
                        for (int x= catNivelPresupuestalInternal.size()-1; x>=0; x--){
                            catNivelPresupuestal.put(idRups++, catNivelPresupuestalInternal.getJsonObject(x));
                        }
                    }else{
                        LOGGER.error("No se pudo Realizar la carga del Arreglo de CAT_NIVEL_JERARQUICO");
                    }
                });
        }
    }
}