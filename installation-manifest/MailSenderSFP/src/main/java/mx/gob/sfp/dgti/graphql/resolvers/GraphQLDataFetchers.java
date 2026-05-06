/* 
 * Sistema propiedad de la Secretaría de la Función Pública DGTI
 * "RegAdqPub" sistema que permite realizar el resitro 
 * de adquisiciones Publicas
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.graphql.resolvers;

import io.vertx.core.Future;
import java.util.Map;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.List;
import mx.gob.sfp.dgti.mailsender.resolvers.Document;
import mx.gob.sfp.dgti.mailsender.resolvers.Mail;
import mx.gob.sfp.dgti.mailsender.resolvers.Mutation;
import mx.gob.sfp.dgti.mailsender.response.Respuesta;

/**
 * Clase donde se comienza la programación 
 * despues de Graphql, punbto donde se entrega el query 
 * del framework graph a java.
 * 
 * @author cgarias
 * @since 01/05/2019
 */
public class GraphQLDataFetchers {
    
    //Inyeccion de clase para la programacion de logica de negocion (service).
    private static Mutation mutation;
    
    /**
     * Método inicial que permite iniciar la clase como objeto.
     *
     * @param vertx herramienta verxt.
     * @return GraphQLDataFetchers retorna la definición de métodos y aspectos para uso se graphql.
     */
    public static GraphQLDataFetchers init(Vertx vertx){
        mutation = new Mutation();  
        return new GraphQLDataFetchers();
    }
    
    /**
     * Método que define la entrada del uso de los métodos definidos en el Schema.
     */
    public final AsyncDataFetcher<String> sendMail = (env, handler) -> {
        Map<String, Object> argumentos = env.getArguments();
        mutation.sendMail(
                argumentos.get("from").toString(), 
                argumentos.get("to").toString(), 
                (List<String>) argumentos.get("replyTo"), 
                (List<String>) argumentos.get("copias"), 
                (List<String>) argumentos.get("copiasOcultas"), 
                argumentos.get("asunto").toString(), 
                argumentos.get("message").toString(), 
                (String) argumentos.get("html"), 
                (Document) argumentos.get("document"),
                (List<Document>) argumentos.get("documents")
                , handler);
    };
    
    /**
     * Método que define la entrada del uso de los métodos definidos en el Schema.
     */
    public final AsyncDataFetcher<String> sendMailObj = (env, handler) -> {
        JsonObject b = new JsonObject(env.getArguments());
        mutation.sendMailObj(b.getJsonObject("input").mapTo(Mail.class), handler);
    };
    
    /**
     * Método que define la entrada del uso de los métodos definidos en el Schema.
     */
    public final AsyncDataFetcher<Respuesta> sendMailObjResp = (env, handler) -> {
        JsonObject b = new JsonObject(env.getArguments());
        mutation.sendMailObjResp(b.getJsonObject("input").mapTo(Mail.class), handler);
    };
    
    public final AsyncDataFetcher<String> mailSends = (env, handler) -> {
        //JsonObject b = new JsonObject(env.getArguments());
        handler.handle(Future.succeededFuture("Hola MailSenderSFP"));
    };
    
}
