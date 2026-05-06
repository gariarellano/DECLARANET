package mx.gob.sfp.dgti.verticle;

import io.netty.handler.codec.http.HttpResponseStatus;
import static mx.gob.sfp.dgti.util.Constantes.ADDRESS;
import static mx.gob.sfp.dgti.util.Constantes.RENAPO_ESTATUS_INCONSISTENTE;
import static mx.gob.sfp.dgti.util.Constantes.RENAPO_ESTATUS_NO_EXISTE;
import static mx.gob.sfp.dgti.util.Constantes.RENAPO_ESTATUS_VALIDADO;
import static mx.gob.sfp.dgti.util.Constantes.VALIDA_CURP_ESTATUS_EXITOSO;
import static mx.gob.sfp.dgti.util.Constantes.VALIDA_CURP_ESTATUS_NO_EXITOSO;
import static mx.gob.sfp.dgti.util.Constantes.VALIDA_CURP_MENSAJE_NO_EXITOSO;

import java.text.Collator;
import java.util.Locale;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.WebClient;
import mx.gob.sfp.dgti.dto.UsuarioRenapoDTO;
import mx.gob.sfp.dgti.entity.UsuarioEO;
import mx.gob.sfp.dgti.helper.AutenticacionHelper;
import mx.gob.sfp.dgti.persistence.DatabaseService;

/**
 * Verticle para consumir el servicio de valida curp con Renapo
 *
 * @author Miriam Sánchez Sánchez programador07
 * @since 11/06/2019
 */
public class ValidadoRenapoVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidadoRenapoVerticle.class);
    private static final String URL_VALIDAR_CURP = System.getenv("IP_DOMINIO_VALIDAR_CURP") != null ? System.getenv("IP_DOMINIO_VALIDAR_CURP") : AutenticacionHelper.obtenerPropiedad("ip.dominio.validar.curp");
    private static final String QUERY_VALIDACURP_CURP = "query{renapoCurp(curp:\"%s\") {statusOper message curp nombres primerApellido segundoApellido }}";
    private DatabaseService databaseService;
    private WebClient client;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        this.client = WebClient.create(vertx);
        this.databaseService = DatabaseService.create(vertx, AutenticacionHelper.getConfig());
    }

    /**
     * Start del verticle
     */
    @Override
    public void start(Future<Void> future) throws Exception {
        super.start();
        vertx.eventBus().consumer(ADDRESS, usuarioEO -> {
            JsonObject jsonObject = new JsonObject(usuarioEO.body().toString());
            UsuarioEO usuario = new UsuarioEO(jsonObject.getInteger("idUsuario"), jsonObject.getString("login"), jsonObject.getString("pwdEnc"), jsonObject.getString("nombre"), jsonObject.getString("primerApellido"),
                    jsonObject.getString("segundoApellido"), jsonObject.getString("curp"), jsonObject.getString("rfc"), jsonObject.getString("homoclave"),
                    jsonObject.getString("numCelular"), jsonObject.getString("email"), jsonObject.getString("emailAlt"), jsonObject.getInteger("validadoRenapo"), null, jsonObject.getInteger("activo"));

            LOGGER.info("=============  validarCurpRenapo =============  " + usuario.getCurp());
            String queryValidaCurp = String.format(QUERY_VALIDACURP_CURP, jsonObject.getString("curp"));
            client.postAbs(URL_VALIDAR_CURP)
                    .timeout(7000)
                    .sendJsonObject(new JsonObject().put("query", queryValidaCurp), response ->{
                        if (response.succeeded() && HttpResponseStatus.OK.code() == response.result().statusCode()){
                            
                            UsuarioRenapoDTO usuarioRenapo = response.result().bodyAsJsonObject().getJsonObject("data").getJsonObject("renapoCurp").mapTo(UsuarioRenapoDTO.class);
                            if (usuarioRenapo != null) {

                                 if (VALIDA_CURP_ESTATUS_EXITOSO.equals(usuarioRenapo.getStatusOper())) {
                                     Collator c = Collator.getInstance(new Locale("es"));
                                     c.setStrength(Collator.PRIMARY);

                                     if (c.equals(usuarioRenapo.getNombres(), usuario.getNombre())
                                             && c.equals(usuarioRenapo.getPrimerApellido(), usuario.getPrimerApellido())
                                             && c.equals(usuarioRenapo.getSegundoApellido(), usuario.getSegundoApellido())) {
                                         usuario.setValidadoRenapo(RENAPO_ESTATUS_VALIDADO);
                                     } else {
                                         usuario.setValidadoRenapo(RENAPO_ESTATUS_INCONSISTENTE);
                                     }
                                     this.databaseService.modificarUsuario(usuario);
                                 } else if (VALIDA_CURP_ESTATUS_NO_EXITOSO.equals(usuarioRenapo.getStatusOper()) && VALIDA_CURP_MENSAJE_NO_EXITOSO.equals(usuarioRenapo.getMessage())) {
                                     usuario.setValidadoRenapo(RENAPO_ESTATUS_NO_EXISTE);
                                     this.databaseService.modificarUsuario(usuario);
                                 }
                             }
                        }else{
                            if (response.succeeded() && HttpResponseStatus.OK.code() != response.result().statusCode()){
                                LOGGER.warn(String.format("no se logo validar renapo. por estatus: %s", response.result().statusCode()));
                            }else{
                                LOGGER.warn(String.format("no se logo validar renapo. por causa: %s", response.cause()));
                            }
                        }
                    });
        });
    }
}
