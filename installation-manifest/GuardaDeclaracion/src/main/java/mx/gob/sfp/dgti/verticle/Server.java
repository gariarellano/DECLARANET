/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "GuardaDeclaracion" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.verticle;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
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
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import java.util.HashSet;
import java.util.Set;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.service.ServiceGuardaDeclaracionInterface;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_PORT;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_APPLICATION_JSON;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_TYPE;
import static mx.gob.sfp.dgti.util.Constantes.PATH;
import static mx.gob.sfp.dgti.util.Constantes.TEXT_HTML;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_GUARDA_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_PROCESO_UNICO_DATOS_PUB;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_REALIZA_REPORTE_PERIODO;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_REALIZA_REPORTE_POR_NUM_CUMPL;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_RECEPCION_DECLARACION;
import static mx.gob.sfp.dgti.verticle.modulos.VerticalConsumerMigraDeclaracion.RECEPCION_MIGRADECLARACION;
import static mx.gob.sfp.dgti.verticle.modulos.VerticalCumplimiento.VERTICLE_REPORTA_DATOS_POR_PERIODO;
import static mx.gob.sfp.dgti.verticle.modulos.VerticalCumplimiento.VERTICLE_REPORTA_POR_NUM_CUMPLIMIENTO;
import static mx.gob.sfp.dgti.verticle.modulos.VerticalRegHistialNobreInstitucion.PORCESO_UNICO;

/**
 * Clase que coneitne la funcionalidad principal para 
 * inicializar la aplicacion.
 * 
 * @author cgarias
 * @since 06/11/2019
 */
public class Server extends AbstractVerticle {
//    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("io.vertx.core.impl.BlockedThreadChecker");//SOLO PARA DEBUG
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    
    private ServiceGuardaDeclaracionInterface serviceGuardaDeclaracionInerface;
    private static final String MESSAGE_TEST_SANITY= "Hola Receptor Declaracion";
    private CircuitBreaker breaker;
    

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context); //To change body of generated methods, choose Tools | Templates.
        breaker =   CircuitBreaker.create("circuit-breaker-guardado", vertx,
                        new CircuitBreakerOptions()
                            .setMaxFailures(2) // number of failure before opening the circuit
                            .setTimeout(2000) // consider a failure if the operation does not succeed in time
                            .setFallbackOnFailure(true) // do we call the fallback on failure
                            .setResetTimeout(3000) // time spent in open state before attempting to re-try
                    );
    }

    /**
     * Start inicializar router y la definición de paths
     * @param future
     * @throws Exception
     */
    @Override
    public void start(Future<Void> future) throws Exception {
            super.start();
//            logger.setLevel(Level.OFF);//SOLO PARA DEBUG
            serviceGuardaDeclaracionInerface = ServiceGuardaDeclaracionInterface.init(vertx);
            Router router = Router.router(vertx);
            router.route().handler(CorsHandler.create(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                            .allowedHeaders(getAllowedHeaders()));
            router.route(PATH).handler(routingContext -> {
                    HttpServerResponse response = routingContext.response();
                    response.putHeader(HEADER_CONTENT_TYPE, TEXT_HTML).end(MESSAGE_TEST_SANITY);
            });

            router.route().handler(BodyHandler.create());
            router.post(URL_APPI_GUARDA_DECLARACION).handler(this::guardaDeclaracion);
            router.post(URL_APPI_RECEPCION_DECLARACION).handler(this::recepcionDeclaracion);
            router.post(URL_APPI_REALIZA_REPORTE_POR_NUM_CUMPL).handler(this::realizaReportePorNumeroComprobante);
            
            
            router.post(URL_APPI_REALIZA_REPORTE_PERIODO).handler(this::realizaReportePeriodo);

            router.post(URL_APPI_PROCESO_UNICO_DATOS_PUB).handler(this::procUnicoDatPub);
            router.route().handler(StaticHandler.create());
            this.getVertx().createHttpServer().requestHandler(router::accept).listen(
                    config().getInteger(CONFIG_PORT, 5000), ar -> {
                if(ar.succeeded()) {
                    LOGGER.info(MESSAGE_TEST_SANITY);
                    future.complete();
                } else {
                    LOGGER.info(ar.cause());
                    future.fail(ar.cause());
                }
                    });
    }

    /**
     * Método principal del servicio 
     * para recibir las declaraciones 
     * de los servidores publicos que 
     * pertenecen a un ente receptor.
     * 
     * @param context Objeto de contexto de vertx.
     */
    private void guardaDeclaracion(RoutingContext context) {
        breaker.execute(beakerFuture ->{
            try{
                this.obtenerHeaders(context);
                JsonObject declaracion = context.getBodyAsJson();
                serviceGuardaDeclaracionInerface.guardaDeclaracion(declaracion).setHandler(result ->{
                    if(result.succeeded()){
                        beakerFuture.complete(result);
                        context.response().end(Json.encode(result.result()));                        
                    }else {
                        beakerFuture.fail("error desconocido.");
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                    }
                });
            }catch(Exception e){
                LOGGER.error(e);
                beakerFuture.fail(e);
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                
            }
        });
    }
    
    /**
     * Método principal del servicio 
     * para relizar la recepcion de la declaracion
     * de los servidores publicos que 
     * pertenecen a un ente receptor.
     * 
     * @param context Objeto de contexto de vertx.
     */
    private void recepcionDeclaracion(RoutingContext context) {
        breaker.execute(beakerFuture ->{
            try{
                this.obtenerHeaders(context);
                vertx.eventBus().send(RECEPCION_MIGRADECLARACION,context.getBodyAsJson(), result ->{
                    if(result.succeeded() && result.result().isSend()){
                        beakerFuture.complete(result.result());
                        context.response().end(Json.encode(result.result().body()));
                    }else {
                        beakerFuture.fail(result.cause());
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                    }
                });
            }catch(Exception e){
                vertx.eventBus().send("VertRollBackRececpion", context.getBodyAsJson());
                beakerFuture.fail(e);
                LOGGER.error(e);
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                
            }
        });
    }
    
    private void realizaReportePeriodo(RoutingContext context) {
        try{
            this.obtenerHeaders(context);
            JsonObject reportar = context.getBodyAsJson();
            if (reportar.getInteger("collName")!= null &&
                    reportar.getString("fechaInicio") != null && reportar.getString("fechaInicio").length()== 10 &&
                    reportar.getString("fechaFin") != null && reportar.getString("fechaFin").length()== 10 &&
                    reportar.getString("tipoDeclaracion") != null  &&
                    (EnumTipoDeclaracion.INICIO.name().equals(reportar.getString("tipoDeclaracion")) || 
                    EnumTipoDeclaracion.CONCLUSION.name().equals(reportar.getString("tipoDeclaracion")) ||
                    EnumTipoDeclaracion.MODIFICACION.name().equals(reportar.getString("tipoDeclaracion")))){         
                LOGGER.info("Se mandará a llamar el reporteo");
            vertx.eventBus().send(VERTICLE_REPORTA_DATOS_POR_PERIODO,reportar);
                LOGGER.info("llamo al reporteo.");
                context.response().setStatusCode(HttpResponseStatus.OK.code()).end("Procesando...");
            }else{
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Erro de parametros");    
            }
        }catch(Exception e){
            LOGGER.error(e);
            context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();

        }
    }
    
    private void procUnicoDatPub(RoutingContext context){
        vertx.eventBus().send(PORCESO_UNICO, context.getBodyAsJson(), enviado -> {
            context.response().setStatusCode(HttpResponseStatus.OK.code()).end(enviado.result().body().toString());
        });
        LOGGER.info("Procesando datos publicos...");
    }
    
    private void realizaReportePorNumeroComprobante(RoutingContext context) {
        try{
            this.obtenerHeaders(context);
            JsonObject reportar = context.getBodyAsJson();
            if (reportar.getInteger("collName")!= null &&
                    reportar.getString("noComprobante") != null && !reportar.getString("noComprobante").isEmpty()
                    ){         
                vertx.eventBus().send(VERTICLE_REPORTA_POR_NUM_CUMPLIMIENTO,reportar);
                context.response().setStatusCode(HttpResponseStatus.OK.code()).end("Procesando...");
            }else{
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Erro de parametros");    
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
}