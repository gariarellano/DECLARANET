package mx.gob.sfp.dgti.verticle;

import static mx.gob.sfp.dgti.util.Constantes.ADDRESS_EMAIL;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.WebClient;
import mx.gob.sfp.dgti.util.AutenticacionHelper;

/**
 * Verticle para consumir el servicio de valida curp con Renapo
 * @author Miriam Sánchez Sánchez programador07
 * @since 11/06/2019
 */
public class EnvioEmailVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvioEmailVerticle.class);
    private static final String URL_ENVIO_EMAIL = System.getenv("IP_DOMINIO_CORREO") != null ? System.getenv("IP_DOMINIO_CORREO") : AutenticacionHelper.obtenerPropiedad("ip.dominio.correo");
    private WebClient client;
    
    private static final String EMPTY = "";
    private static final String NOMBRE_INPUT = "input : ";
    private static final String NOMBRE_PARAMETRO_PARA = "to :";
    private static final String NOMBRE_PARAMETRO_DE = "from :";
    private static final String NOMBRE_PARAMETRO_ASUNTO = "asunto :";
    private static final String NOMBRE_PARAMETRO_MESSAGE = "message : ";
    private static final String NOMBRE_PARAMETRO_HTML = "html : ";
    private static final String HTML = "html";
    private static final String DE_SFP = System.getenv("CUENTA_CORREO_FROM");
    private static final String ASUNTO_REESTABLECER_PWD = "Reestablecer contraseña";
    private static final String PLANTILLA = "<html><header><center style='font-weight: bold; font-size: 19px;'><div style='color:#b21f2d'><h2>IDENTIDAD FUNCI&Oacute;N P&Uacute;BLICA"
                    + "</h2></div></center></header><body><center style='font-size: 19px;'> Est&aacute;"
                    + " recibiendo este correo porque hemos recibido su solicitud para reestablecer la contrase&ntilde;a de su cuenta."
                    + "<h3><label style='font-weight: bold !important; font-size: 19px;'>"
                    + "<p>Nombre: <span style='color:#27ae60'>%s</span></p>"
                    + "<a href='%s' class='btn btn-success'>Si desea cambiar su contrase&ntilde;a d&eacute; click aqu&iacute;</a>"
                    + "</label></h3>Si no desea reestablecer la contrase&ntilde;a haga caso omiso de este correo.</center></body></html>";

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context); //To change body of generated methods, choose Tools | Templates.
        client = WebClient.create(vertx);
    }
	


    /**
     * Start del verticle
     * @param future
     * @throws java.lang.Exception
     */
    @Override
    public void start(Future<Void> future) throws Exception {
        super.start();
        vertx.eventBus().consumer(ADDRESS_EMAIL, receivedCurp -> {

            JsonObject jsonObject = new JsonObject(receivedCurp.body().toString());
            if(jsonObject.getString("email") != null && !EMPTY.equals(jsonObject.getString("email")) 
                                    && URL_ENVIO_EMAIL != null && !EMPTY.equals(URL_ENVIO_EMAIL) 
                                    && jsonObject.getString("link") != null && !EMPTY.equals(jsonObject.getString("link"))) {

                StringBuilder builder= new StringBuilder();
                builder.append("sendMailObjResp ( ")
                        .append(NOMBRE_INPUT).append(" { ")
                        .append(NOMBRE_PARAMETRO_PARA).append("\"").append(jsonObject.getString("email")).append("\" ")
                        .append(NOMBRE_PARAMETRO_DE).append("\"").append(DE_SFP).append("\" ")
                        .append(NOMBRE_PARAMETRO_HTML).append("\"").append(HTML).append("\" ")
                        .append(NOMBRE_PARAMETRO_ASUNTO).append("\"").append("Reestablecer contrase\u00f1a - ").append(jsonObject.getString("nombre", "")).append("\" ")
                        .append(NOMBRE_PARAMETRO_MESSAGE).append("\"").append(String.format(PLANTILLA, jsonObject.getString("nombre", ""), jsonObject.getString("link"))).append("\"")
                        .append(" } ) { mensaje }");

                JsonObject messaje = new JsonObject();
                messaje.put("query", " mutation {"+builder+"}");
                client.postAbs(URL_ENVIO_EMAIL)
                    .putHeader("Content-Type", "application/json")
                    .sendJson(messaje, response->{
                        LOGGER.info(String.format("=============  envio email restablecer contraseña ============= %s ", jsonObject.getString("email")));
                        if (response.succeeded()){
                            LOGGER.info(String.format("status resoponse porara %s:  %d", jsonObject.getString("email"),response.result().statusCode()));
                        }else{
                            LOGGER.info(String.format("Error de respuesta %s", response.cause()));
                        }
                        
                });
            }
        });
    }
}
