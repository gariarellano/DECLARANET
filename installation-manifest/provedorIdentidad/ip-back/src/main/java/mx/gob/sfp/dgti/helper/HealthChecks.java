/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.helper;


import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.client.WebClient;
import java.util.logging.Level;
import java.util.logging.Logger;
import static mx.gob.sfp.dgti.util.Constantes.IP_SERVER_PORT;

/**
 * Clase que contiene las pruebas de salud del servicio,.
 * en la cual se ejecutan las siguiente spruebas,
 * 1.- Comunicación EventBus
 * 2.- Manejo de memoria
 * 3.- Conexion a base de datos
 * 4.- Entradas y salidad http
 * 
 * @author cgarias
 * @since 27/04/2021
 */
public class HealthChecks extends AbstractVerticle{
    private static final Logger logger = Logger.getLogger(HealthChecks.class.getName());
    public static final String HEALTH_CHECK = "healthChecks";
    private static final Integer port = Integer.parseInt(System.getenv(IP_SERVER_PORT) != null ? System.getenv(IP_SERVER_PORT) : "8088");
    private static final String URL_LOCAL_SERVICE = "http://localhost:"+port+"/";
    private SQLClient postgreSQLt;    
    private WebClient client;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context); 
        this.postgreSQLt = PostgreSQLClient.createShared(vertx, AutenticacionHelper.getConfig().put("maxPoolSize", 1).put("testTimeout",5000), "shared-healt");
        client = WebClient.create(vertx);
    }

    @Override
    public void start() throws Exception {
        super.start(); 
        vertx.eventBus().consumer(HEALTH_CHECK, message ->{
            Future<JsonObject> first = Future.future(resulta->{
                postgreSQLt.getConnection(connect->{
                    if (connect.succeeded()){
                        connect.result().close();
                        resulta.handle(Future.succeededFuture(new JsonObject().put("DB-connection", "OK").put("Event-Bus", "OK")));
                    }else{
                        logger.log(Level.SEVERE, "Error con la base de datos {0}", connect.cause());
                        resulta.fail(connect.cause());
                    }

                });
            });
           
            Future<JsonObject> second = Future.future(resulta->{
                client.getAbs(URL_LOCAL_SERVICE)
                        .send(response->{
                            if (response.succeeded() && HttpResponseStatus.OK.code() == response.result().statusCode()){
                                resulta.handle(Future.succeededFuture(new JsonObject().put("Healt-check-get-Path", response.result().bodyAsString())));
                            }else{
                               if (response.failed()){
                                    resulta.fail(response.cause());
                                    logger.log(Level.SEVERE, "Error al realiar solicitud al path principal {0}", response.cause());
                               }else{
                                    resulta.fail("Error responseresponse code: " +response.result().statusCode());
                                    logger.log(Level.SEVERE, "Error de status respuesta al path {0}", response.result().statusCode());
                               }
                            }
                        });
            });
            
            CompositeFuture.all(first, second).setHandler(termino->{
                if (termino.succeeded()){
                    message.reply(new JsonObject().mergeIn(first.result()).mergeIn(second.result()).put("memory-status", "OK").put("Event-Bus", "OK")); 
                }else{
                    message.fail(HttpResponseStatus.GATEWAY_TIMEOUT.code(), termino.cause().getMessage());
                }
            });
        });
    }    
}