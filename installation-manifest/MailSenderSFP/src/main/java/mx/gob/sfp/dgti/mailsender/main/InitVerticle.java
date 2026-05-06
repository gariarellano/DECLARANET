/* 
 * Sistema Propiedad de la Secretaría de la Función Pública DGTI
 * MailSenderSFP"oservicio que expone métodos de envío de 
 * correo electronicos de un emisor especificado a otra 
 * cuenta de receptor especificada.
 * 
 * desarrolado por: cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.mailsender.main;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import mx.gob.sfp.dgti.VerticalConfig;

/**
 * Método creado paraejecutar el Verticle 
 * en un ambiente local y para pruebas.
 * 
 * @author cgarias
 * @since 01/05/2019
 */
public class InitVerticle extends VerticalConfig {
    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new InitVerticle());
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        this.startDeploy("mx.gob.sfp.dgti.mailsender.main.MailSender");
    }
}