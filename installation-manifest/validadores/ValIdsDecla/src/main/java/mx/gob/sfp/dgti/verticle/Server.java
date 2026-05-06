/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "GuardaDeclaracion" Sistema que permite realizar el guardado de declaraciones
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
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.service.ServiceConsultaDeclaracionInterface;
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
import static mx.gob.sfp.dgti.util.Constantes.MESSAGE_TEST_SANITY;
import static mx.gob.sfp.dgti.util.Constantes.PATH;
import static mx.gob.sfp.dgti.util.Constantes.TEXT_HTML;
import static mx.gob.sfp.dgti.util.Constantes.URL_APPI_VALIDADOR;

/**
 * Clase que coneitne la funcionalidad principal para 
 * inicializar la aplicacion.
 * 
 * @author cgarias
 * @since 06/11/2019
 */
public class Server extends AbstractVerticle {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    
    private ServiceConsultaDeclaracionInterface serviceConsultaDeclaracion;
    private Router router;
    private WebClient webclient;
    
    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context); 
        webclient = WebClient.create(vertx);
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
     * Start inicializar router y la definición de paths
     * @param future
     * @throws Exception
     */
    @Override
    public void start(Future<Void> future) throws Exception {
            super.start();
            serviceConsultaDeclaracion = ServiceConsultaDeclaracionInterface.init(vertx, webclient);
            router.route().handler(BodyHandler.create());
            router.post(URL_APPI_VALIDADOR).handler(this::validaIdentificadores);
            router.route().handler(StaticHandler.create());
            
    }

    /**
     * Método principal del servicio 
     * para recibir las declaraciones 
     * de los servidores publicos que 
     * pertenecen a un ente receptor.
     * 
     * @param context Objeto de contexto de vertx.
     */
    private void validaIdentificadores(RoutingContext context) {
        try{
            this.obtenerHeaders(context);
            serviceConsultaDeclaracion.validaDeclaracion(context.getBodyAsJson()).setHandler(result ->{
                if(result.succeeded()){
                    context.response().setStatusCode(HttpResponseStatus.OK.code()).end(Json.encode(result.result()));                        
                }else {
                    context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                }
            });
        }catch(Exception e){
            logger.severe(String.format("%s", e));
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