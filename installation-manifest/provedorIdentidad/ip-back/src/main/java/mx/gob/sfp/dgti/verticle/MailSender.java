/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Verical que contiene los llamados 
 * a servicios de correo.
 * 
 * @since 16/05/2020
 * @author cgarias
 */
public class MailSender extends AbstractVerticle {
    private static final Logger logger = Logger.getLogger(MailSender.class.getName());
    public static final String MAIL_SENDER_VERTICAL = "MAIL-SENDER-VERTICAL";
	
    private static final String URL_ENVIO_EMAIL = System.getenv("IP_DOMINIO_CORREO") != null ? System.getenv("IP_DOMINIO_CORREO"): "";
    private static final String NOMBRE_OPERACION_ENVIO_EMAIL = " sendMailObjResp ( ";
    private static final String NOMBRE_INPUT = "input : ";
    private static final String NOMBRE_PARAMETRO_PARA = "to : ";
    private static final String NOMBRE_PARAMETRO_DE = "from : ";
    private static final String NOMBRE_PARAMETRO_ASUNTO = "asunto : ";
    private static final String NOMBRE_PARAMETRO_MESSAGE = "message : ";
    private static final String NOMBRE_PARAMETRO_HTML = "html : ";
    private static final String HTML = "html";
    private static final String EMPTY = "";
    public static final String DE_SFP = System.getenv("CUENTA_CORREO_FROM");
    public static final String ASUNTO = "Código de confirmación";
//    public static final String INPUT = "{\"query\":\"mutation sendMailObj($input:Mail!){sendMailObj(input:$input)}\",\"variables\":{\"input\":"
//                    + "{\"from\": \"%s\", \"to\":\"%s\",\"asunto\":\"%s\",\"message\":\"%s\",\"html\":\"html\"}},\"operationName\":\"sendMailObj\"}";
//    public static final String PLANTILLA = "<html><header><center style='font-weight: bold; font-size: 19px;'><div style='color:#b21f2d'>"
//                    + "<h2>IDENTIDAD FUNCI&Oacute;N P&Uacute;BLICA</h2></div></center></header><body><center style='font-size: 19px;'>"
//                    + "C&Oacute;DIGO DE CONFIRMACI&Oacute;N<h3><label style='font-weight: bold !important; font-size: 19px;'> %s </label></h3></center></body></html>";
    private static final String PLANTILLA_ACTIVACION_ENLACE = "<html><header><center style='font-weight: bold; font-size: 19px;'><div style='color:#b21f2d'><h2>SECRETARIA DE CONTRALORIA DE MICHOACAN"
                    + "</h2></div></center></header><body><center style='font-size: 19px;'>"
                    + "<p>Est&aacute; recibiendo este correo porque se ha solicitado restablecer la contrase&ntilde;a de la siguiente cuenta.</p>"
                    + "<h3><label style='font-size: 19px;'>"
                    + "<h2>Nombre: <span style='color:#27ae60'> %s </span></h2>"
                    + "<h2>CURP: <span style='color:#2980b9'> %s </span></h2>"
                    + "<a href='%s' class='btn btn-success'>D&eacute; click aqu&iacute; para restablecer su contrase&ntilde;a</a>"
                    + "</label></h3></center></body></html>";
    private WebClient webClient;
    
    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        webClient = WebClient.create(vertx);
    }
    
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.eventBus().consumer(MAIL_SENDER_VERTICAL, message -> {
            JsonObject mail= (JsonObject) message.body();
            try {
                if(mail.getString("correos") != null && !EMPTY.equals(mail.getString("correos")) 
                                && mail.getString("curp") != null && !EMPTY.equals(mail.getString("curp")) 
                                && mail.getString("url") != null && !EMPTY.equals(mail.getString("url"))) {
                    StringBuilder builder= new StringBuilder();
                    builder.append(NOMBRE_OPERACION_ENVIO_EMAIL)
                            .append(NOMBRE_INPUT).append(" { ")
                            .append(NOMBRE_PARAMETRO_PARA).append("\"").append(mail.getString("correos")).append("\" ")
                            .append(NOMBRE_PARAMETRO_DE).append("\"").append(DE_SFP).append("\" ")
                            .append(NOMBRE_PARAMETRO_HTML).append("\"").append(HTML).append("\" ")
                            .append(NOMBRE_PARAMETRO_ASUNTO).append("\"").append("Restablecimiento de contrase\u00f1a - ").append(mail.getString("nombre", mail.getString("curp"))).append("\" ")
                            .append(NOMBRE_PARAMETRO_MESSAGE).append("\"").append(String.format(PLANTILLA_ACTIVACION_ENLACE, mail.getString("nombre", ""), mail.getString("curp"), mail.getString("url"))).append("\"")
                            .append(" } ) { mensaje }");

                    JsonObject messaje = new JsonObject();
                    messaje.put("query", " mutation {"+builder+"}");
                    webClient.postAbs(URL_ENVIO_EMAIL)
                        .putHeader("Content-Type", "application/json")
                        .sendJson(messaje, response->{
                            logger.info("=============  envio email codigo confirmacion =============  " );
                    });
                } 
            } catch(Exception e) {
                    logger.log(Level.SEVERE,"Error al enviar correo {0} ",e);
            }
        });
    }
}
