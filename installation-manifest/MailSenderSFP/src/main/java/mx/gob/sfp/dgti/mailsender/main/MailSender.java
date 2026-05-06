/* 
 * Sistema Propiedad de la Secretaría de la Función Pública DGTI
 * MailSenderSFP"oservicio que expone métodos de envío de 
 * correo electronicos de un emisor especificado a otra 
 * cuenta de receptor especificada.
 * 
 * desarrolado por: cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.mailsender.main;

import static io.vertx.core.http.HttpMethod.GET;

import java.util.Map;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import java.util.Arrays;
import java.util.HashMap;
import mx.gob.sfp.dgti.graphql.resolvers.AsyncExecException;
import mx.gob.sfp.dgti.graphql.resolvers.AsyncGraphQLExec;


/**
 * Clase principal que crea unvertice en la herramienta de
 * Vertx y permite la exposicion de path para recibir información
 * enviada de tipo GraphQl y poder realizarla petición para 
 * TODO describir el objetivo del proyecto.
 * 
 * @author cgarias
 * @since 01/05/2019 
 */
public class MailSender extends AbstractVerticle{
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    private static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "*";
    private static final String HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "true";
    private static final String HEADER_ACCESS_CONTROL_ALLOW_METHODS = "POST, GET, OPTIONS";
    private static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "authorization,content-type";
    private static final String CACHE_CONTROL = "Cache-Control";
    private static final String PRAGMA = "Pragma";
    private static final String HEADER_PRAGMA = "no-cache";
    private static final String HEADER_CACHE_CONTROL = "no-store";
    private static final String ERROR = "error";
    
    private static final Logger LOGG = LoggerFactory.getLogger(MailSender.class);
    
    /**
     * Propiedad que permite realizar la interacionde Java con GraphQl
     */
    
    private Router router;
    private AsyncGraphQLExec asyncGraphQL;

    /**
     * Constructor de la clase que inicializa el objeto de GraphQl y 
     * permite realizar la interaccion con la capa del la herramienta.
     * @param vertx
     * @param context
     */
  
    @Override
    public void init(Vertx vertx, Context context){
        super.init(vertx, context);
        asyncGraphQL = AsyncGraphQLExec.create(vertx);
        router = Router.router(vertx);
        router.route("/graphiql").method(GET).handler(rc -> {
          if ("/graphiql".equals(rc.request().path())) {
            rc.response().setStatusCode(302);
            rc.response().headers().set("Location", rc.request().path() + "/");
            rc.response().end("You are in path!!");
          } else {
            rc.next();
          }
        });

        StaticHandler staticHandler = StaticHandler.create("graphiql");
        staticHandler.setDirectoryListing(false);
        staticHandler.setCachingEnabled(false);
        staticHandler.setIndexPage("index.html");
        router.route("/graphiql/*").method(GET).handler(staticHandler);
        vertx.createHttpServer().requestHandler(router).listen(5000);
    }
            
    /**
     * Método principal del vertice que permite realizar la exposioción del 
     * path principal del proyecto para resolver las péticiones recibidas 
     * por cualquier cliente con sentencias GraphQl, exte método 
     * tambien expone un path /browser el cual representa una
     * interface de GraphQl para conocerel schema.
     */
    @Override
    public void start() {        
        router.route("/").handler(rc -> {
            rc.request().bodyHandler(rh -> {
              String query = rh.toString();
              handleQuery(rc, query);
            });
          }).handler(CorsHandler.create("/")
                    .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                    .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
                    .allowCredentials(true)
                    .allowedHeader(ACCESS_CONTROL_ALLOW_METHODS)
                    .allowedHeader(ACCESS_CONTROL_ALLOW_ORIGIN)
                    .allowedHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS)
                    .allowedHeader(ACCESS_CONTROL_ALLOW_HEADERS)
                    .allowedHeader(HEADER_CONTENT_TYPE));

        
    }
    
    /**
    * Se agregan los headers correspondientes para el Intercambio de recursos de
    * origen cruzado (CORS).
    * @param routingContext
    * @return
    */
    public static HttpServerResponse obtenerHeaders(RoutingContext routingContext) {
        return routingContext.response().putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                        .putHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS)
                        .putHeader(ACCESS_CONTROL_ALLOW_METHODS, HEADER_ACCESS_CONTROL_ALLOW_METHODS)
                        .putHeader(ACCESS_CONTROL_ALLOW_HEADERS, HEADER_ACCESS_CONTROL_ALLOW_HEADERS)
                        .putHeader(CACHE_CONTROL, HEADER_CACHE_CONTROL).putHeader(PRAGMA, HEADER_PRAGMA);
    }


    /**
     * Este método esun handle programado en los paths de exposición de VertX, paraque cualquier
     * llamada a alguno de los métodos expuestos contengan un contenido de query tipo
     * json y que tenga la estructurade graphQl.
     *
     * @param rc   routing context para realizar el enrutamiento de respuesta.
     * @param json Cadena de tipo json para el procesamiento enjava y enrutamiento envertx y respuesta GraphQl.
     */
    private void handleQuery(RoutingContext rc, String json) {
        obtenerHeaders(rc);
        if ("".equals(json)){
            rc.response().putHeader("content-type", "text/html");
            rc.response().setStatusCode(200);
            rc.response().end("hola MUNDO");
            return ;
        }        
        //para prueba y poder visualizar el query que se esta recibiendo.
//        LOGG.info("Handling query : = " + json + " ");
        // The graphql query is transmitted within a JSON string
        JsonObject queryJson = new JsonObject(json);
        String query = queryJson.getString("query");
        
        try {
            LOGG.info("Sigue con el proceso");
            String operationName = queryJson.getString("operationName");
            JsonObject bodyVariables = queryJson.getJsonObject("variables");
            
            Map<String, Object> variables = new HashMap<>();
            if (bodyVariables != null && !bodyVariables.isEmpty()) {
                variables = bodyVariables.getMap();
            }
            // execute the graphql query
            asyncGraphQL.executeQuery(query, operationName, rc, variables)
                    .setHandler(queryResult -> {
                if (queryResult.succeeded()) {
                    rc.response().end(new JsonObject().put("data", queryResult.result()).encode());
                } else {
                    Map<String, Object> res = new HashMap<>();
                    res.put("data", null);
                    if (queryResult.cause() instanceof AsyncExecException) {
                            AsyncExecException ex = (AsyncExecException) queryResult.cause();
                            res.put(ERROR, ex.getErrors());
                    } else {
                            res.put(ERROR, queryResult.cause() != null ? Arrays.asList(queryResult.cause())
                                            : Arrays.asList(new Exception("Internal error")));
                    }
                    rc.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
							.end(new JsonObject(res).encode());
                    LOGG.error("ERRORES para mandar mail:\n" + queryResult.cause() +" \n\r AsuntoCorreo: "+ (query.contains("asunto")? (query.substring(query.indexOf("asunto"), query.indexOf("asunto")+20)):"no trae asunto"));   
                }
            });
        } catch (Exception e) {
                Map<String, Object> res = new HashMap<>();
                res.put("errors", Arrays.asList(e));
                JsonObject errorResult = new JsonObject(res);
                rc.response().setStatusCode(500).end(errorResult.encode());
                LOGG.error("ERROR no reconocido:\n" + e +" \n\r AsuntoCorreo: "+ (query.contains("asunto")? (query.substring(query.indexOf("asunto"), query.indexOf("asunto")+100)):"no trae asunto"));   
        }
    }
}