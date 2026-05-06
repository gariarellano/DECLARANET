/*
 * Sistemna Propiedad de la SecretarÃ­a de la FunciÃ³n PÃºblica DGTI
 * "RegistroCumplimiento" Sistema que permite realizar el guardado de declaraciones
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
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import mx.gob.sfp.dgti.service.IServiceRegistraCumplimiento;
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
import static mx.gob.sfp.dgti.util.Constantes.MESSAGE_TEST_SANITY;
import static mx.gob.sfp.dgti.util.Constantes.PATH;
import static mx.gob.sfp.dgti.util.Constantes.TEXT_HTML;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_CONSULTA_OBLIGACION;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_REG_CUMP_DECLA;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_REG_CUMP_MANUAL;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_REG_CUMP_RUSP;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_REG_EXCL_MANUAL;

/**
 * Clase que coneitne la funcionalidad principal para 
 * inicializar la aplicacion.
 * 
 * @author cgarias
 * @since 27/04/2020
 */
public class Server extends AbstractVerticle {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private IServiceRegistraCumplimiento serviceRegistroCumplimiento;
    private Router router;
    private WebClient webclient;
    private CircuitBreaker breaker;
    private static final Pattern PATHERN_DIGITOS = Pattern.compile("\\d+");
    
    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context); 
        webclient = WebClient.create(vertx);
        breaker = CircuitBreaker.create("circuit-breaker-registro", vertx,
                        new CircuitBreakerOptions()
                            .setMaxFailures(50) // number of failure before opening the circuit
                            .setTimeout(7000) // consider a failure if the operation does not succeed in time
                            .setFallbackOnFailure(true) // do we call the fallback on failure
                            .setResetTimeout(3000) // time spent in open state before attempting to re-try
                    );
        router = Router.router(vertx);
        router.route().handler(CorsHandler.create(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                        .allowedHeaders(getAllowedHeaders()));
        router.route(PATH).handler(routingContext -> {
                HttpServerResponse response = routingContext.response();
                response.putHeader(HEADER_CONTENT_TYPE, TEXT_HTML).end(MESSAGE_TEST_SANITY);
        });        
        this.getVertx().createHttpServer().requestHandler(router::accept).listen(
                config().getInteger(CONFIG_PORT, 5000), ar -> {
                    if(ar.succeeded()) {
                        logger.info(MESSAGE_TEST_SANITY);
                    } else {
                        logger.info(String.format("%s", ar.cause()));
                    }
                });
        
    }

    /**
     * Start inicializar router y la definiciÃ³n de paths
     * @param future
     * @throws Exception
     */
    @Override
    public void start(Future<Void> future) throws Exception {
            super.start();
            router.route().handler(BodyHandler.create());
            serviceRegistroCumplimiento = IServiceRegistraCumplimiento.init(vertx, webclient);
            //----REGISTRA CUMPLIMIENTO ATUMATICO-----
            router.post(URL_APPI_REG_CUMP_DECLA).handler(this::registraCumplimientoDecla);
            router.post(URL_APPI_REG_CUMP_RUSP).handler(this::registraCumplimientoRusp);
            
            //----REGISTRA CUMPLIMIENTO MANUAL-----
            router.get(URL_APPI_CONSULTA_OBLIGACION).handler(this::consultaObligacion);
            router.post(URL_APPI_REG_CUMP_MANUAL).handler(this::registraCumpliminetoManual);
            router.post(URL_APPI_REG_EXCL_MANUAL).handler(this::registraExclusionManual);
//            router.post(URL_APPI_REV_CUMP_MANUAL).handler(this::revierteCumpliminetoManual);
            router.route().handler(StaticHandler.create());
            
    }

    /**
     * MÃ©todo que realiza el registro de cumplimineto 
     * de una declaraciÃ³n, derivado de una 
     * declaracion presentada y encontrada 
     * la obligacion en rusp.
     * 
     * @param context Objeto de contexto de vertx.
     */
    private void registraCumplimientoDecla(RoutingContext context) {
        breaker.execute(futureCircuit -> {
            try{
                this.obtenerHeaders(context);
                    serviceRegistroCumplimiento.validaYregistraCumplimientoDecla(context.getBodyAsJson(), resp->{
                        if (resp.result().getInteger("statusCode")==200){
                            futureCircuit.handle(Future.succeededFuture());
                        }else{
                            futureCircuit.fail(resp.result().getInteger("statusCode").toString());
                        }
                        context.response().setStatusCode(resp.result().getInteger("statusCode")).end(resp.result().getJsonObject("response").encode()); 
                    });
            }catch(Exception e){
                logger.severe(String.format("%s", e));
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                futureCircuit.fail(e);
            }
        });
    }
    /**
     * MÃ©todo que realiza el registro de cumplimineto 
     * de una declaraciÃ³n, derivado de una 
     * declaracion presentada.
     * 
     * @param context Objeto de contexto de vertx.
     */
    private void registraCumplimientoRusp(RoutingContext context) {
        breaker.execute(futureCircuit -> {
            try{
                this.obtenerHeaders(context);
                serviceRegistroCumplimiento.validaYregistraCumplimientoRusp(context.getBodyAsJson(), resp ->{
                   if (resp.result().getInteger("statusCode")==200){
                       futureCircuit.handle(Future.succeededFuture());
                    }else{
                       futureCircuit.fail(resp.result().getInteger("statusCode").toString());
                    }
                    context.response().setStatusCode(resp.result().getInteger("statusCode")).end(resp.result().getJsonObject("response").encode()); 
                });
            }catch(Exception e){
                logger.severe(String.format("%s", e));
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                futureCircuit.fail(e);
            }
        });
    }
    
    private void consultaObligacion(RoutingContext context){
        String noComprobante = context.request().getParam("noComprobante");
        String collName = context.request().getParam("collName");
        if (noComprobante != null && !noComprobante.isEmpty() && 
                collName != null && !collName.isEmpty() &&
                isNumber(collName)){
            try{
                this.obtenerHeaders(context);
                serviceRegistroCumplimiento.consultaObligacion(noComprobante, collName, 2, result ->{
                    if (result.succeeded()){
                        context.response().setStatusCode(result.result().getInteger("statusCode")).end(result.result().getJsonObject("response").encode()); 
                    }else{
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end(); 
                    }
                });
            }catch(Exception e){
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end(); 
                logger.log(Level.SEVERE, "Error", e);
            }
        }else{
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Parametros incorrectos.");
            logger.log(Level.INFO, "Error en parametros: {0}", context.request().params().toString());
        }
    }
    
    private void registraCumpliminetoManual(RoutingContext context){
        String idNoLocDecla = context.request().getParam("idDNetNoLocalizado");
        String idRusp = context.request().getParam("idRusp");
        String collName = context.request().getParam("collName");
        if (idNoLocDecla != null && !idNoLocDecla.isEmpty() && 
                idRusp != null && !idRusp.isEmpty() && 
                collName != null && !collName.isEmpty() &&
                !context.getBodyAsJson().isEmpty()){
            try{
                this.obtenerHeaders(context);
                serviceRegistroCumplimiento.registrarCumplimientoManual(idNoLocDecla, idRusp, collName, context.getBodyAsJson(), result ->{
                    if (result.succeeded()){
                        context.response().setStatusCode(result.result().getInteger("statusCode")).end(result.result().getJsonObject("response").encode()); 
                    }else{
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end(); 
                    }
                });
            }catch(Exception e){
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end(); 
                logger.log(Level.SEVERE, "Error", e);
            }
        }else{
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Parametros incorrectos.");
        }
    }
    
    private void registraExclusionManual(RoutingContext context){
        String idRusp = context.request().getParam("idRusp");
        String collName = context.request().getParam("collName");
        if (idRusp != null && !idRusp.isEmpty() && 
                collName != null && !collName.isEmpty() &&
                !context.getBodyAsJson().isEmpty()){
            try{
                this.obtenerHeaders(context);
                serviceRegistroCumplimiento.registrarExclusionManual(idRusp, collName, context.getBodyAsJson(), result ->{
                    if (result.succeeded()){
                        context.response().setStatusCode(result.result().getInteger("statusCode")).end(result.result().getJsonObject("response").encode()); 
                    }else{
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end(); 
                    }
                });
            }catch(Exception e){
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end(); 
                logger.log(Level.SEVERE, "Error", e);
            }
        }else{
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Parametros incorrectos.");
        }
    }
    
//    /**
//     * MÃ©todo que realiza el revertir cumplimiento, siempre y cuando el cumplimiento
//     * @param contex 
//     */
//    private void revierteCumpliminetoManual(RoutingContext contex){
//        String idNoLocDecla = contex.request().getParam("idDNetNoLocalizado");
//        String idRusp = contex.request().getParam("idRusp");
//        String collName = contex.request().getParam("collName");
//        if (idNoLocDecla != null && !idNoLocDecla.isEmpty() && 
//                idRusp != null && !idRusp.isEmpty() && 
//                collName != null && !collName.isEmpty() &&
//                !contex.getBodyAsJson().isEmpty()){
//            try{
//                serviceRegistroCumplimiento.revierteCumplimientoManual(idNoLocDecla, idRusp, collName, contex.getBodyAsJson(), result ->{
//                    if (result.succeeded()){
//                        contex.response().setStatusCode(result.result().getInteger("statusCode")).end(result.result().getJsonObject("response").encode()); 
//                    }else{
//                        contex.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end(); 
//                    }
//                });
//            }catch(Exception e){
//                contex.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end(); 
//                logger.log(Level.SEVERE, "Error", e);
//            }
//        }else{
//            contex.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Parametros incorrectos.");
//        }
//    }

    public boolean isNumber(String cadena) {
        return PATHERN_DIGITOS.matcher(cadena).matches();
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
        allowedHeaders.add(HEADER_AUTHORIZATION_KEY);
        allowedHeaders.add(HEADER_CONTENT_TYPE);
	    
        return allowedHeaders;
    }
}