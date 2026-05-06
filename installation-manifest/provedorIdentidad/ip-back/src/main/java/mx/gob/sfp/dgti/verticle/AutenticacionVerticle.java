package mx.gob.sfp.dgti.verticle;

import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_EXPOSE_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.ACTIVO;
import static mx.gob.sfp.dgti.util.Constantes.ADDRESS;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_PORT;
import static mx.gob.sfp.dgti.util.Constantes.CREDENCIALES_INVALIDAS;
import static mx.gob.sfp.dgti.util.Constantes.CURP;
import static mx.gob.sfp.dgti.util.Constantes.ESTATUS_RENAPO;
import static mx.gob.sfp.dgti.util.Constantes.EXP_MINUTOS;
import static mx.gob.sfp.dgti.util.Constantes.FECHA;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_AUTHORIZATION_KEY;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_APPLICATION_JSON;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_TYPE;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_DOMINIO;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ORIGEN;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_TRANSACCION;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_VALUE_API;
import static mx.gob.sfp.dgti.util.Constantes.INACTIVO_FRONT;
import static mx.gob.sfp.dgti.util.Constantes.IP_SERVER_PORT;
import static mx.gob.sfp.dgti.util.Constantes.ISS;
import static mx.gob.sfp.dgti.util.Constantes.KEY_API;
import static mx.gob.sfp.dgti.util.Constantes.MENSAJE_KEY_ERROR;
import static mx.gob.sfp.dgti.util.Constantes.MENSAJE_TRANSACCION_ERROR;
import static mx.gob.sfp.dgti.util.Constantes.NO_EXISTE;
import static mx.gob.sfp.dgti.util.Constantes.NO_OBLIGATORIO_CURP_VALIDA;
import static mx.gob.sfp.dgti.util.Constantes.PARAMETRO_CLIENTE_ID;
import static mx.gob.sfp.dgti.util.Constantes.PARAMETRO_CLIENTE_OBLIGATORIO;
import static mx.gob.sfp.dgti.util.Constantes.PARAMETRO_SECRET_KEY;
import static mx.gob.sfp.dgti.util.Constantes.REGEX_PARA_CORREO;
import static mx.gob.sfp.dgti.util.Constantes.RENAPO_ESTATUS_INCONSISTENTE;
import static mx.gob.sfp.dgti.util.Constantes.RENAPO_ESTATUS_NO_VALIDADO;
import static mx.gob.sfp.dgti.util.Constantes.RENAPO_ESTATUS_VALIDADO;
import static mx.gob.sfp.dgti.util.Constantes.SIN_VINCULACION;
import static mx.gob.sfp.dgti.util.Constantes.TEXT_HTML;
import static mx.gob.sfp.dgti.util.Constantes.TOKEN_BEARER_KEY;
import static mx.gob.sfp.dgti.util.Constantes.USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.VINCULACION_Y_CREDENCIALES_VALIDAS;

import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.handler.StaticHandler;
import mx.gob.sfp.dgti.dto.RespuestaDTO;
import mx.gob.sfp.dgti.entity.AplicacionEO;
import mx.gob.sfp.dgti.entity.UsuarioEO;
import mx.gob.sfp.dgti.helper.AutenticacionHelper;
import static mx.gob.sfp.dgti.helper.HealthChecks.HEALTH_CHECK;
import mx.gob.sfp.dgti.helper.ValidacionHelper;
import mx.gob.sfp.dgti.persistence.DatabaseService;
import mx.gob.sfp.dgti.util.Constantes;
import static mx.gob.sfp.dgti.util.Constantes.DATA;
import static mx.gob.sfp.dgti.util.Constantes.LOCALIZADO;
import static mx.gob.sfp.dgti.util.Constantes.PARAMETRO_CLIENT_ID;
import static mx.gob.sfp.dgti.util.Constantes.PARAMETRO_PASSWORD;
import static mx.gob.sfp.dgti.util.Constantes.PARAMETRO_SCOPE;
import static mx.gob.sfp.dgti.util.Constantes.PARAMETRO_SECRET;
import static mx.gob.sfp.dgti.util.Constantes.PARAMETRO_USER_NAME;
import static mx.gob.sfp.dgti.util.Constantes.PARAM_TOKEN_ONE_TIME;
import static mx.gob.sfp.dgti.util.Constantes.TELEGRAM_ESTATUS_PRE_VINCULACION;
import static mx.gob.sfp.dgti.verticle.MailSender.MAIL_SENDER_VERTICAL;

/**
 * Clase con la definición de los paths para autenticar a usuarios y
 * aplicaciones clientes mediante la generacion de tokens
 *
 * @author Miriam Sanchez Sanchez programador07
 * @since 07/02/2019
 */
public class AutenticacionVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutenticacionVerticle.class);
    private static final String URL_FRONT_ACTIVACION = "/activate";
    private static final String LOGIN_REDIRECT = "/login";
    private static final String ERROR_REDIRECT = "/error?r=1";
    private static final String URL_AUTHORIZE = "/api/public/authorize";
    private static final String URL_AUTHORIZE_PDN = "/api/public/authorize/pdn/";
    private static final String URL_AUTHORIZE_PDN_TOKEN = "/api/public/authorize/pdn/token/";
    private static final String URL_PRIVATE_VALIDATE_RESPONSE = "/api/public/validate-response";
    private static final String URL_PRIVATE_LOGIN = "/api/private/login";
    private static final String URL_PRIVATE_GENERATE_OTP_TELE = "/api/private/generate/otp/tele";
    private static final String URL_PRIVATE_VALIDATE_OTP_TELE = "/api/private/validate/otp/tele/:idUsuario/:otp";
    private static final String URL_PRIVATE_ELIMINA_PRE_REG_TELE = "/api/private/delete/otp/tele";
    private static final String URL_PRIVATE_SYNC = "/api/private/sync";
    private static final String URL_PRIVATE_USER = "/api/private/user";
    private static final String URL_PRIVATE_ACTIVATE = "/api/private/activate";
    private static final String URL_PRIVATE_REQUEST_ACTIVATE = "/api/private/get-activate";
    private static final String URL_HELTH_CHECKER = "/healt-checker/";
    private static final String REGEX_CORREO = "(?<=.{3}).(?=.*@)";
    private static final String ASTERISCO = "*";
    private static final int TOKEN_10S = Integer.parseInt(System.getenv("TIEMPO_25S") == null ? "600" : System.getenv("TIEMPO_25S"));
    private static final int TOKEN_60S = Integer.parseInt(System.getenv("TIEMPO_60S") == null ? "600" : System.getenv("TIEMPO_60S"));
    private static final int TOKEN_24HRS = Integer.parseInt(System.getenv("TIEMPO_24HR") == null ? "84600" : System.getenv("TIEMPO_24HR"));
    private static final String URL = System.getenv("URL_GENERADOR_TOKEN") != null ? System.getenv("URL_GENERADOR_TOKEN") : AutenticacionHelper.obtenerPropiedad("dominio.generador.token");
    private static final String URL_BASE_TELEGRAM = System.getenv("URL_BASE_TELEGRAM") != null ? System.getenv("URL_BASE_TELEGRAM") : "";
    private DatabaseService databaseService;
    private WebClient client;
    private Router router;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        this.databaseService = DatabaseService.create(vertx, AutenticacionHelper.getConfig());
        client = WebClient.create(vertx);
        router = Router.router(vertx);
        router.route().handler(CorsHandler.create(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                .allowedHeaders(AutenticacionHelper.getAllowedHeaders()));
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader(HEADER_CONTENT_TYPE, TEXT_HTML).end("Hola IP...");
        });
        router.route("/api/public/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader(HEADER_CONTENT_TYPE, TEXT_HTML).end("Hola IP...");
        });
        vertx.setPeriodic(86400000, timer->{
            databaseService.eliminaTokenUnSoloUSo();
        });
        this.getVertx().createHttpServer().requestHandler(router::accept).listen(
                config().getInteger(CONFIG_PORT, Integer.parseInt(System.getenv(IP_SERVER_PORT) != null ? System.getenv(IP_SERVER_PORT) : "8088")), ar -> {
                    if (ar.succeeded()) {
                        LOGGER.info("Running");
                    } else {
                        LOGGER.error(ar.cause());
                    }
                });
    }

    /**
     * Start inicializar router y la definición de paths
     *
     * @param future
     * @throws Exception
     */
    @Override
    public void start(Future<Void> future) throws Exception {
        super.start();
        this.databaseService = DatabaseService.create(vertx, AutenticacionHelper.getConfig());
        JWTAuth auth = JWTAuth.create(vertx, AutenticacionHelper.obtenerJwt());
        router.route().handler(BodyHandler.create());
        router.route(URL_AUTHORIZE).handler(this::autenticarAplicacion);
        router.route(URL_PRIVATE_LOGIN).handler(JWTAuthHandler.create(auth));
        router.post(URL_PRIVATE_LOGIN).handler(this::autenticarUsuario);
        router.route(URL_PRIVATE_GENERATE_OTP_TELE).handler(JWTAuthHandler.create(auth));
        router.get(URL_PRIVATE_GENERATE_OTP_TELE).handler(this::generaOtpToTele);
        router.route(URL_PRIVATE_VALIDATE_OTP_TELE).handler(JWTAuthHandler.create(auth));
        router.get(URL_PRIVATE_VALIDATE_OTP_TELE).handler(this::validateOtpToTele);
        router.route(URL_PRIVATE_ELIMINA_PRE_REG_TELE).handler(JWTAuthHandler.create(auth));
        router.post(URL_PRIVATE_ELIMINA_PRE_REG_TELE).handler(this::eliminaPreregistro);
        router.route(URL_PRIVATE_ACTIVATE).handler(JWTAuthHandler.create(auth));
        router.get(URL_PRIVATE_ACTIVATE).handler(this::activarUsuario);
        router.route(URL_PRIVATE_SYNC).handler(JWTAuthHandler.create(auth));
        router.get(URL_PRIVATE_SYNC).handler(this::vincularUsuario);
        router.route(URL_PRIVATE_VALIDATE_RESPONSE).handler(JWTAuthHandler.create(auth));
        router.get(URL_PRIVATE_VALIDATE_RESPONSE).handler(this::validarRespuesta);
        
        
        router.get(URL_PRIVATE_USER).handler(this::obtenerUsuario);
        router.get(URL_PRIVATE_REQUEST_ACTIVATE).handler(this::solicitarActivarUsuario);
        
        //Apartado de métodos para funcionalidad de autenticación y tokens para PDN
        router.get(URL_AUTHORIZE_PDN).handler(this::autenticarAplicacionPdn);
        router.put(URL_AUTHORIZE_PDN_TOKEN).handler(this::guardaToken);
        router.get(URL_AUTHORIZE_PDN_TOKEN).handler(this::validaToken);
        router.get(URL_HELTH_CHECKER).handler(this::registerHandler);
        router.route().handler(StaticHandler.create());
    }

    /**
     * Autenticacion de la aplicación cliente Recibe como parametros clientId,
     * secretKey, y se obtiene el dominio del request. Se agregan los headers
     * correspondientes para el response asi como la informacion requerida de
     * acuerdo a la validacion del cliente
     *
     * @param routingContext contexto de la petición que contiene los
     * stringParams, header y body de la petición
     */
    private void autenticarAplicacion(RoutingContext routingContext) {
        AutenticacionHelper.obtenerHeaders(routingContext);
        LOGGER.info("======================  OBTENIENDO autenticacin cliente ====================== ");
        if (routingContext.request().getParam(PARAMETRO_CLIENTE_ID) != null && routingContext.request().getParam(PARAMETRO_SECRET_KEY) != null && routingContext.request().getHeader(HEADER_DOMINIO) != null) {
            Integer idAplicacion = Integer.parseInt(routingContext.request().getParam(PARAMETRO_CLIENTE_ID));
            String secretKey = routingContext.request().getParam(PARAMETRO_SECRET_KEY);
            String dominioFront = routingContext.request().getHeader(HEADER_DOMINIO);
            String dominioCliente = AutenticacionHelper.validarDominio(routingContext.request().getHeader(HEADER_ORIGEN));
            RespuestaDTO respuesta = new RespuestaDTO();
            this.databaseService.obtenerAplicacion(dominioCliente, idAplicacion, secretKey, false).setHandler(aplicacion -> {
                if (aplicacion.failed()) {
                    routingContext.response().setStatusCode(200).end(Json.encodePrettily(dominioFront + ERROR_REDIRECT));
                } else {
                    String token = generarToken(new JsonObject().put(PARAMETRO_CLIENTE_ID, idAplicacion).put(PARAMETRO_CLIENTE_OBLIGATORIO, aplicacion.result().getObligatorioCurpValida()).put(FECHA, ValidacionHelper.getFechaFormatoISO8601(new Date())), TOKEN_60S);
                    respuesta.setUrl(dominioFront + LOGIN_REDIRECT);
                    routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                            .putHeader(Constantes.ACCESS_CONTROL_EXPOSE_HEADERS, Constantes.HEADER_AUTHORIZATION_KEY)
                            .putHeader(Constantes.HEADER_AUTHORIZATION_KEY, Constantes.TOKEN_BEARER_KEY + token)
                            .putHeader(Constantes.HEADER_CONTENT_TYPE, Constantes.HEADER_CONTENT_APPLICATION_JSON)
                            .end(AutenticacionHelper.jsonConvert(respuesta).encodePrettily());
                }
            });
        } else {
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }
    
    /**
     * Autenticacion de la aplicación cliente solo para los sistemas cliente
     * de la Plataforma Digital NAcional (PDN) recibe como parametros.
     *
     * @param routingContext contexto de la petición que contiene los
     * stringParams, header y body de la petición
     */
    private void autenticarAplicacionPdn(RoutingContext routingContext) {
        AutenticacionHelper.obtenerHeaders(routingContext);
        LOGGER.info("======================  OBTENIENDO autenticación cliente PDN====================== ");
        if (routingContext.request().getParam(PARAMETRO_USER_NAME) != null && 
                routingContext.request().getParam(PARAMETRO_PASSWORD) != null && 
                routingContext.request().getParam(PARAMETRO_CLIENT_ID) != null &&
                routingContext.request().getParam(PARAMETRO_SECRET) != null && 
                routingContext.request().getParam(PARAMETRO_SCOPE) != null) {
            String userPdn = routingContext.request().getParam(PARAMETRO_USER_NAME);
            String passwordPdn = routingContext.request().getParam(PARAMETRO_PASSWORD);
            String dominioCliente = userPdn+"-"+passwordPdn;
            Integer idAplicacion = Integer.parseInt(routingContext.request().getParam(PARAMETRO_CLIENT_ID));
            String secret = routingContext.request().getParam(PARAMETRO_SECRET);
            String scope = routingContext.request().getParam(PARAMETRO_SCOPE);
            String secretKey = secret+"-"+scope;
            
            this.databaseService.obtenerAplicacion(dominioCliente, idAplicacion, secretKey, false).setHandler(aplicacion -> {
                if (aplicacion.failed()) {
                    routingContext.response().setStatusCode(401).end("Unauthorized");
                } else {
                    routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                            .putHeader(Constantes.HEADER_CONTENT_TYPE, Constantes.HEADER_CONTENT_APPLICATION_JSON)
                            .end(new JsonObject().put("message", "Autorized").toBuffer());
                }
            });
        } else {
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }

    /**
     * Autenticacion del usuario, se validan las peticiones previas OPTIONS. Se
     * recibe un objeto Json de tipo Usuario con el usuario y su password Se
     * valida si el usuario existe y si ha sincronizado su cuenta con el cliente
     * dependiendo de la validacion se regresa un codigo {1 = USUARIO ACTIVO,
     * CON CREDENCIALES CORRECTAS, CON VINCULACIÓN (ACCESO PERMITIDO) 2 =
     * USUARIO SIN VINCULACIÓN 3 = USUARIO Y/O CONTRASENA INVÁLIDOS 4 = INACTIVO
     * 5 = ESTATUS DE LA CURP VALIDADA CON RENAPO}
     *
     * @param routingContext contexto de la petición que contiene los
     * stringParams, header y body de la petición
     */
    private void autenticarUsuario(RoutingContext routingContext) {
        AutenticacionHelper.obtenerHeaders(routingContext);
        if (routingContext.request().method() == HttpMethod.OPTIONS) {
            routingContext.response().setStatusCode(HttpResponseStatus.OK.code()).end();

        } else {
            LOGGER.info("============= AUTENTICACION USUARIO POST=============== ");
            if (routingContext.user().principal().getValue(PARAMETRO_CLIENTE_ID) != null && !"".equals(routingContext.getBodyAsString())) {
                Integer clienteId = Integer.parseInt(routingContext.user().principal().getValue(PARAMETRO_CLIENTE_ID).toString());
                Integer obligatorio = Integer.parseInt(routingContext.user().principal().getValue(PARAMETRO_CLIENTE_OBLIGATORIO).toString());
                UsuarioEO usuario = Json.decodeValue(routingContext.getBodyAsString(), UsuarioEO.class);
                usuario.setIdAplicacion(clienteId);
                String origin = routingContext.request().getHeader(HEADER_DOMINIO);
                RespuestaDTO respuesta = new RespuestaDTO();
                this.databaseService.obtenerUsuario(usuario.getCurp()).setHandler(ar -> {

                    if (ar.result() != null) {
                        if (ar.result().getPwdEnc().equals(EncripDecla.digestPassword(ar.result().getLogin(), usuario.getPwdEnc().toUpperCase()))) {
                            if (ACTIVO.equals(ar.result().getActivo())) {
                                respuesta.setCodigo(ESTATUS_RENAPO);
                                respuesta.setEstatus(ar.result().getValidadoRenapo());
                                if (RENAPO_ESTATUS_NO_VALIDADO.equals(respuesta.getEstatus())) {
                                    LOGGER.info("RENAPO_ESTATUS_NO_VALIDADO " + ar.result().toString());
                                    vertx.eventBus().send(ADDRESS, JsonObject.mapFrom(ar.result()));
                                }

                                if (RENAPO_ESTATUS_VALIDADO.equals(respuesta.getEstatus())
                                        || (RENAPO_ESTATUS_NO_VALIDADO.equals(respuesta.getEstatus()) && NO_OBLIGATORIO_CURP_VALIDA.equals(obligatorio))
                                        || RENAPO_ESTATUS_INCONSISTENTE.equals(respuesta.getEstatus()) || TELEGRAM_ESTATUS_PRE_VINCULACION.equals(respuesta.getEstatus())) {
                                    LOGGER.info("!!!RENAPO_ESTATUS_NO_VALIDADO " + ar.result().toString());
                                    databaseService.obtenerVinculacionUAEO(ar.result().getIdUsuario(), clienteId, usuario.getCurp()).setHandler(vinculacion -> {
                                        LOGGER.info("cgarias vamos a checar aplicacion");
                                        databaseService.obtenerAplicacion(null, clienteId, null, false).setHandler(aplicacion -> {
                                            if (aplicacion.succeeded()) {
                                                String fecha = ValidacionHelper.getFechaFormatoISO8601(new Date());
                                                if (vinculacion.failed()) {
                                                    LOGGER.info("No ha sincronizado su cuenta");
                                                    respuesta.setCodigo(SIN_VINCULACION);
                                                    respuesta.setToken(this.generarToken(new JsonObject().put(PARAMETRO_CLIENTE_ID, clienteId).put(CURP, usuario.getCurp()).put(FECHA, fecha), TOKEN_10S));
                                                    respuesta.setAplicacion(aplicacion.result().getNombre());
                                                    respuesta.setTransaccion(DigestUtils.sha256Hex(respuesta.getToken()));
                                                } else {
                                                    LOGGER.info("Credenciales y sincronizacion validas  ");
                                                    respuesta.setCodigo(VINCULACION_Y_CREDENCIALES_VALIDAS);
                                                    respuesta.setToken(this.generarToken(new JsonObject().put(CURP, usuario.getCurp()).put("idUsuario",ar.result().getIdUsuario()).put(FECHA, fecha), TOKEN_10S));
                                                    respuesta.setUrl(aplicacion.result().getUrl());
                                                    respuesta.setTransaccion(DigestUtils.sha256Hex(respuesta.getToken()));
                                                }
                                                databaseService.guardarTransaccion(respuesta.getTransaccion(),
                                                        new AutenticacionHelper().codificarBase64(respuesta.getToken(), clienteId, usuario.getCurp(), fecha),
                                                        respuesta.getToken(), clienteId, fecha, usuario.getCurp(), ar.result().getIdUsuario()).setHandler(guardaT -> {
                                                            if (guardaT.succeeded()) {
                                                                routingContext.response().putHeader(ACCESS_CONTROL_EXPOSE_HEADERS, HEADER_AUTHORIZATION_KEY).putHeader(HEADER_AUTHORIZATION_KEY, TOKEN_BEARER_KEY + respuesta.getToken());
                                                                respuesta.setToken(null);
                                                                routingContext.response().end(Json.encodePrettily(respuesta));
                                                            } else {
                                                                routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                                                            }
                                                        });
                                            } else {
                                                LOGGER.error(ar);
                                                routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                                            }
                                        });
                                    });
                                } else {
                                    LOGGER.info("Se detiene el flujo porque no está validado y tiene que ser obligatorio / o no existe");
                                    routingContext.response().setStatusCode(HttpResponseStatus.OK.code()).end(Json.encode(respuesta));
                                }
                            } else {
                                LOGGER.info("Inactivo");
                                String token = this.generarToken(new JsonObject().put(CURP, usuario.getCurp()).put(FECHA, ValidacionHelper.getFechaFormatoISO8601(new Date())), TOKEN_24HRS);
                                String url = origin + URL_FRONT_ACTIVACION + "?token=" + Constantes.TOKEN_BEARER_KEY + token;
                                this.databaseService.guardarConfirmacion(ar.result().getIdUsuario(), token).setHandler(confirmacion -> {
                                    if (confirmacion.succeeded()) {
                                        String institucional = ar.result().getEmail();
                                        String personal = ar.result().getEmailAlt();
                                        JsonObject mail = new JsonObject();
                                        mail.put("correos", obtenerEmails(institucional, personal));
                                        mail.put("curp", ar.result().getCurp());
                                        mail.put("url", url);
                                        // Agrega nombre completo para identificar al usuario en el correo admin
                                        String nombreCompleto = (ar.result().getNombre() != null ? ar.result().getNombre() : "")
                                            + " " + (ar.result().getPrimerApellido() != null ? ar.result().getPrimerApellido() : "")
                                            + " " + (ar.result().getSegundoApellido() != null ? ar.result().getSegundoApellido() : "");
                                        mail.put("nombre", nombreCompleto.trim());
                                        vertx.eventBus().send(MAIL_SENDER_VERTICAL, mail);
                                        respuesta.setCodigo(INACTIVO_FRONT);
                                        String emailIFront = validarEmail(institucional) != null ? institucional.replaceAll(REGEX_CORREO, ASTERISCO) : "";
                                        String emailPFront = validarEmail(personal) != null ? personal.replaceAll(REGEX_CORREO, ASTERISCO) : "";
                                        respuesta.setCorreo(emailIFront + " " + emailPFront);
                                        routingContext.response().end(Json.encodePrettily(respuesta));
                                    } else {
                                        LOGGER.error("Error al guardar la confirmacion para solicitud de la activación");
                                        routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                                    }
                                });
                            }
                        } else {
                            LOGGER.error("En validacion usuario y password no coinciden");
                            respuesta.setCodigo(CREDENCIALES_INVALIDAS);
                            routingContext.response().setStatusCode(HttpResponseStatus.OK.code()).end(Json.encodePrettily(respuesta));
                        }
                    } else {
                        LOGGER.error("En validacion usuario es null " + ar.result());
                        respuesta.setCodigo(CREDENCIALES_INVALIDAS);
                        routingContext.response().setStatusCode(HttpResponseStatus.OK.code()).end(Json.encodePrettily(respuesta));
                    }
                });
            } else {
                LOGGER.error("Falló la validacion de los parametros ");
                routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
            }
        }
    }

    public String obtenerEmails(String institucional, String personal) {

        institucional = validarEmail(institucional);
        personal = validarEmail(personal);
        StringBuilder emails = new StringBuilder();

        if (institucional != null) {
            emails.append(institucional);
        }
        if (institucional != null && personal != null) {
            emails.append(",");
        }
        if (personal != null) {
            emails.append(personal);
        }
        return emails.toString();
    }

    public String validarEmail(String correo) {
        if (correo != null && Pattern.compile(REGEX_PARA_CORREO).matcher(correo).matches()) {
            return correo;
        }
        return null;
    }

    /**
     * Método de envío de mensaje a usuario para comprobar su identidad
     * 
     * @param context Objeto de contexto de las peticiones.
     */
    private void generaOtpToTele(RoutingContext context ){       
        if (context.user().principal().getValue("curp") != null){
            try{
                databaseService.consultaDatosTelByIdUsr(context.user().principal().getString("curp"), result->{
                    if (result.succeeded()){
                        if (!result.result().isEmpty()){
                            String otp = ValidacionHelper.createOtpToTelegram();
                            client.postAbs(URL_BASE_TELEGRAM.concat("/send-message"))
                                    .putHeader("Content-Type", "application/json")
                                    .sendJsonObject(new JsonObject()
                                            .put("idTelegram", String.valueOf(result.result().getLong("idTelegram")))
                                            .put("message", "Ingresa el Siguiente código en la pantalla para completar tu sincronizacion \r\n "+otp)
                                            , res->{
                                                if (res.succeeded() && res.result().statusCode() == HttpResponseStatus.OK.code()){
                                                    databaseService.creaOactualizaOtp(result.result().getInteger("idUsuario"), otp, inOup->{
                                                        if (inOup.succeeded()){                                                            
                                                            String token = generarToken(new JsonObject().put(CURP, context.user().principal().getString("curp")).put(FECHA,ValidacionHelper.getFechaFormatoISO8601(new Date())), 300);
                                                            context.response()
                                                                    .putHeader(Constantes.ACCESS_CONTROL_EXPOSE_HEADERS, Constantes.HEADER_AUTHORIZATION_KEY)
                                                                    .putHeader(Constantes.HEADER_AUTHORIZATION_KEY, Constantes.TOKEN_BEARER_KEY + token)
                                                                    .putHeader(Constantes.HEADER_CONTENT_TYPE, Constantes.HEADER_CONTENT_APPLICATION_JSON)
                                                                    .setStatusCode(HttpResponseStatus.OK.code())
                                                                    .end(new JsonObject().put("idUsuario", result.result().getInteger("idUsuario")).put("estatus", "Enviado").encode());                                                            
                                                        }else{
                                                            LOGGER.error(String.format("Error de operacion con bd de OTP %s", inOup.cause()));
                                                            context.response().setStatusCode(HttpResponseStatus.CONFLICT.code()).end("Conflictos en operacion del OTP");
                                                        }
                                                    });                                        
                                                }else{
                                                    if (res.succeeded() && HttpResponseStatus.OK.code() != res.result().statusCode()){
                                                        context.response().setStatusCode(res.result().statusCode()).end(res.result().statusMessage());
                                                    }else{
                                                        LOGGER.error(String.format("Error de liberación de OTP %s", res.cause() ));
                                                        context.response().setStatusCode(HttpResponseStatus.CONFLICT.code()).end("Conflictos al envio del OTP");
                                                    }
                                                }
                                    });
                        }else{
                            context.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end(String.format("Datos no localizados tele para %d", context.getBodyAsJson().getInteger("idUsuario")));
                        }
                    }else{
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                    }
                });
            }catch(Exception e){
                LOGGER.error(e);
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end("Ocirrio un error.");
            }
        }else {
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }
    
    /**
     * Método que realiza la eliminacion del OTP al realizar la vinculación.
     * 
     * @param context Objeto contexto de las peticiones.
     */
    public void validateOtpToTele(RoutingContext context){
        AutenticacionHelper.obtenerHeaders(context);
        if (context.pathParam("otp")!= null && !context.pathParam("otp").isEmpty() &&
                context.pathParam("idUsuario") != null && !context.pathParam("idUsuario").isEmpty()){
            databaseService.validaOtp(context.pathParam("otp"), Integer.parseInt(context.pathParam("idUsuario")), validado ->{
                if (validado.succeeded()){
                    if (validado.result()){
                        context.response().setStatusCode(HttpResponseStatus.OK.code()).end(new JsonObject().put("localizado", Boolean.TRUE).encode());
                    }else{
                        context.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end(new JsonObject().put("localizado", Boolean.FALSE).encode());
                    }
                }else{
                    context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                }
            });
            
        }else {
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }
    
    /**
     * Mpetodo que realia la eliminación de preregistro telegram.
     * 
     * @param context Objeto contexto de las peticiones.
     */
    public void eliminaPreregistro(RoutingContext context){
        AutenticacionHelper.obtenerHeaders(context);
        if (context.user().principal().getValue("curp") != null && context.user().principal().getValue("idUsuario") != null){
            databaseService.eliminaPreregistroTele(context.user().principal().getInteger("idUsuario"), validado ->{
                if (validado.succeeded()){
                    if (validado.result()){
                        context.response().setStatusCode(HttpResponseStatus.OK.code()).end(new JsonObject().put("Eliminado", Boolean.TRUE).encode());
                    }else{
                        context.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end(new JsonObject().put("Eliminado", Boolean.FALSE).encode());
                    }
                }else{
                    context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                }
            });
        }else {
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }    
    
    /**
     * Se valida que exista la transacción en la bd con los datos recibidos.
     * Obtener el idDeclaranet del usuario autenticado. Se recibe como header
     * KeyAPI del cliente y TRANSACTION Se agregan los headers correspondientes
     * para el response as como la informacion requerida de acuerdo a la
     * validacion del keyAPI y la transaction
     *
     * @param routingContext contexto de la petición que contiene los
     * stringParams, header y body de la petición
     */
    private void validarRespuesta(RoutingContext rc) {
        LOGGER.info("=================== VALIDAR RESPUESTA ================= ");
        AutenticacionHelper.obtenerHeaders(rc);
        if (rc.request().getHeader(HEADER_TRANSACCION) != null
                && (rc.request().getHeader(HEADER_VALUE_API) != null
                || (rc.request().getParam(PARAMETRO_CLIENTE_ID) != null && rc.request().getParam(PARAMETRO_SECRET_KEY) != null))) {
            String pTransaccion = rc.request().getHeader(HEADER_TRANSACCION);
            String token = rc.request().getHeader(HEADER_AUTHORIZATION_KEY);
            String valueApi = rc.request().getHeader(HEADER_VALUE_API);
            String idAplicacion = rc.request().getParam(PARAMETRO_CLIENTE_ID);
            String secretKey = rc.request().getParam(PARAMETRO_SECRET_KEY);
            RespuestaDTO respuesta = new RespuestaDTO();

            validarCredenciales(valueApi, pTransaccion, idAplicacion, secretKey, token).setHandler(credenciales -> {

                if (credenciales.succeeded()) {
                    databaseService.obtenerTransaccion(pTransaccion, token).setHandler(transaccion -> {
                        if (transaccion.failed()) {
                            respuesta.setError(NO_EXISTE);
                            rc.response().setStatusCode(HttpResponseStatus.OK.code()).end(Json.encodePrettily(respuesta));
                        }
                        if (transaccion.result().getCodigoSha().equals(pTransaccion)) {
                            respuesta.setCurp(transaccion.result().getCurp());
                            respuesta.setTransaccion(pTransaccion);

                            if (credenciales.result().getIss() != null && !"".equals(credenciales.result().getIss())
                                    && credenciales.result().getMinutos() != null && credenciales.result().getMinutos() != 0) {
                                obtenerTokenSesion(credenciales.result(), respuesta.getCurp(), transaccion.result().getIdUsuario()).setHandler(ar -> {
                                            if (ar.succeeded()) {
                                                rc.response()
                                                .putHeader(ACCESS_CONTROL_EXPOSE_HEADERS, HEADER_AUTHORIZATION_KEY)
                                                .putHeader(HEADER_AUTHORIZATION_KEY, TOKEN_BEARER_KEY + ar.result())
                                                .end(Json.encodePrettily(respuesta));
                                            } else {
                                                rc.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                                                .end(Json.encodePrettily(respuesta));
                                            }
                                        });
                            } else {
                                rc.response()
                                        .putHeader(ACCESS_CONTROL_EXPOSE_HEADERS, HEADER_AUTHORIZATION_KEY)
                                        .end(Json.encodePrettily(respuesta));
                            }

                        } else {
                            respuesta.setError(MENSAJE_TRANSACCION_ERROR);
                            rc.response().putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON).end(Json.encodePrettily(respuesta));
                        }
                    });
                } else {
                    respuesta.setError(MENSAJE_KEY_ERROR);
                    rc.response().putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON).end(Json.encodePrettily(respuesta));
                }
            });
        } else {
            rc.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }

    /**
     * Validar las credenciales de la aplicación
     *
     * @param valueApi
     * @param transaccion
     * @param idAplicacion
     * @param secretKey
     * @param token
     * @return
     */
    private Future<AplicacionEO> validarCredenciales(
            String valueApi, String transaccion, String idAplicacion, String secretKey, String token) {
        Future<AplicacionEO> future = Future.future();

        if (validarHeaderKeyApi(valueApi)) {
            databaseService.obtenerAppPorTrans(transaccion, token).setHandler(aplicacion -> {
                future.handle(Future.succeededFuture(aplicacion.result()));
            });

        } else {
            databaseService.obtenerAplicacionPorId(Integer.parseInt(idAplicacion), secretKey).setHandler(aplicacion -> {
                future.handle(Future.succeededFuture(aplicacion.result()));
            });
        }
        return future;
    }

    private boolean validarHeaderKeyApi(String valueApi) {
        boolean headerValueApi = false;
        if (valueApi != null) {
            headerValueApi = KEY_API.equals(valueApi);
        }
        return headerValueApi;
    }

    /**
     * Metodo de vinculacion de cuentas de usuario (IP con alguna aplicacion
     * cliente) Este proceso se ejecuta 1 vez por cuenta de aplicacion cliente
     *
     * @param routingContext contexto de la petición que contiene los
     * stringParams, header y body de la petición
     */
    private void vincularUsuario(RoutingContext routingContext) {
        LOGGER.info("============ VINCULAR USUARIO  ============= ");
        AutenticacionHelper.obtenerHeaders(routingContext);
        if (routingContext.user().principal().getValue(PARAMETRO_CLIENTE_ID) != null && routingContext.user().principal().getValue(CURP) != null && routingContext.user().principal().getValue(FECHA) != null) {
            AutenticacionHelper helper = new AutenticacionHelper();
            Integer clienteId = Integer.parseInt(helper.obtenerPropiedadToken(routingContext, PARAMETRO_CLIENTE_ID));
            String curp = helper.obtenerPropiedadToken(routingContext, CURP);
            String fecha = helper.obtenerPropiedadToken(routingContext, FECHA);
            RespuestaDTO respuesta = new RespuestaDTO();
            databaseService.guardarVinculacionUAEO(clienteId, fecha, ACTIVO, curp).setHandler(vincular -> {
                databaseService.obtenerAplicacion(null, clienteId, null, false).setHandler(aplicacion -> {
                    respuesta.setUrl(aplicacion.result().getUrl());
                    routingContext.response().end(Json.encodePrettily(respuesta));
                });
            });
        } else {
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }

    /**
     * Método para consultar un usuario mediante su curp
     *
     * @param routingContext contexto de la petición que contiene los
     * stringParams, header y body de la petición
     */
    private void obtenerUsuario(RoutingContext routingContext) {
        LOGGER.info("====================== OBTENIENDO CURP ====================== ");
        AutenticacionHelper.obtenerHeaders(routingContext);
        if (routingContext.request().getParam(CURP) != null) {
            String curp = routingContext.request().getParam(CURP);
            this.databaseService.obtenerUsuario(curp).setHandler(AutenticacionHelper.writeJsonResponse(routingContext));
        } else {
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }

    }

    /**
     * Activar usuario mediante el token asignado enviado previamente en un
     * correo
     *
     * @param context contexto de la petición que contiene los stringParams,
     * header y body de la petición
     */
    private void activarUsuario(RoutingContext context) {
        LOGGER.info("====================== ACTIVAR USUARIO ====================== ");
        AutenticacionHelper.obtenerHeaders(context);
        if (context.user().principal().getValue(CURP) != null) {
            String curp = context.user().principal().getValue(CURP).toString();
            this.databaseService.obtenerUsuario(curp).setHandler(usuario -> {
                if (usuario.failed()) {
                    context.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code());
                } else {
                    String token = context.request().getHeader(HEADER_AUTHORIZATION_KEY);
                    this.databaseService.obtenerConfirmacion(curp, token).setHandler(confirmacion -> {
                        if (confirmacion.succeeded()) {
                            this.databaseService.activarUsuarioPorEnlace(usuario.result().getIdUsuario(), confirmacion.result().getIdConfirmacion()).setHandler(activacion -> {
                                if (activacion.failed()) {
                                    LOGGER.error("Error al activarusuario " + activacion.cause().getMessage());
                                    context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                                } else {
                                    UsuarioEO usuarioB = new UsuarioEO();
                                    usuarioB.setCurp(usuario.result().getCurp());
                                    context.response().end(Json.encode(usuarioB));
                                }
                            });
                        } else {
                            LOGGER.error("Error al obtener la confirmacion");
                            context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                        }
                    });
                }
            });
        } else {
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }

    /**
     * Servicio para solicitar activar usuario, se enviará un correo con el
     * enlace
     *
     * @param routingContext contexto de la petición que contiene los
     * stringParams, header y body de la petición
     */
    private void solicitarActivarUsuario(RoutingContext routingContext) {
        LOGGER.info("====================== SOLICITUD ACTIVAR USUARIO ====================== ");
        AutenticacionHelper.obtenerHeaders(routingContext);
        if (routingContext.request().getParam(CURP) != null && !"".equals(routingContext.request().getParam(CURP))) {
            String curp = routingContext.request().getParam(CURP);
            String origin = routingContext.request().getHeader(HEADER_DOMINIO);
            this.databaseService.obtenerUsuario(curp).setHandler(ar -> {
                if (ar.failed()) {
                    routingContext.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end();
                } else {
                    String token = this.generarToken(new JsonObject().put(CURP, curp).put(FECHA, ValidacionHelper.getFechaFormatoISO8601(new Date())), TOKEN_24HRS);
                    String url = origin + URL_FRONT_ACTIVACION + "?token=" + Constantes.TOKEN_BEARER_KEY + token;
                    this.databaseService.guardarConfirmacion(ar.result().getIdUsuario(), token).setHandler(confirmacion -> {
                        if (confirmacion.succeeded()) {
                            JsonObject mail = new JsonObject();
                            mail.put("correos", obtenerEmails(ar.result().getEmail(), ar.result().getEmailAlt()));
                            mail.put("curp", ar.result().getCurp());
                            mail.put("url", url);
                            // Agrega nombre completo para identificar al usuario en el correo admin
                            String nombreCompleto = (ar.result().getNombre() != null ? ar.result().getNombre() : "")
                                + " " + (ar.result().getPrimerApellido() != null ? ar.result().getPrimerApellido() : "")
                                + " " + (ar.result().getSegundoApellido() != null ? ar.result().getSegundoApellido() : "");
                            mail.put("nombre", nombreCompleto.trim());
                            vertx.eventBus().send(MAIL_SENDER_VERTICAL, mail);
                            routingContext.response().setStatusCode(HttpResponseStatus.OK.code()).end();
                        } else {
                            LOGGER.error("Error al guardar la confirmacion para solicitud de la activación");
                            routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                        }
                    });
                }
            });
        } else {
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }
    
    /**
     * Método que realiza el guardado de un token para un solo uso.
     * 
     * @param routingContext contexto de vertx par resolver las peticiones.
     */
    private void guardaToken(RoutingContext routingContext) {
        if (routingContext.request().getParam(PARAM_TOKEN_ONE_TIME) != null){
            databaseService.guardaTokenUnSoloUSo(routingContext.request().getParam(PARAM_TOKEN_ONE_TIME), respuesta->{
                if (respuesta.succeeded()){
                    routingContext.response().setStatusCode(HttpResponseStatus.OK.code()).end(new JsonObject().put("message", "guardado").toBuffer());
                }else{
                    routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end("eror de guardado");
                }
            });
        }else{
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }
    
    /**
     * Método que realiza la validacion de la existencia de un token para resolver datos.
     * 
     * @param routingContext  contesto de vertx para resolver las peticiones.
     */
    private void validaToken(RoutingContext routingContext) {
        if (routingContext.request().getParam(PARAM_TOKEN_ONE_TIME) != null){
            databaseService.validaTokenUnSoloUSo(routingContext.request().getParam(PARAM_TOKEN_ONE_TIME), respuesta->{
                if (respuesta.succeeded()){
                    if (respuesta.result().equals(LOCALIZADO)){
                        routingContext.response().setStatusCode(HttpResponseStatus.OK.code()).end(new JsonObject().put("message", "localizado").toBuffer());
                    }else{
                        routingContext.response().setStatusCode(401).end("Unauthorized");
                    }
                }else{
                    routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end("Error en validaToken");
                }
            });
        }else{
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }

    /**
     * Metodo que recibe los objetos para construir un token
     *
     * @param json {@link JsonObject} la informacion con la que queremos
     * construir el token
     * @param segundos el tiempo que le asignamos de vida al token
     * @return String cadena con el token generado
     */
    private String generarToken(JsonObject json, int segundos) {
        JWTAuth auth = JWTAuth.create(vertx, AutenticacionHelper.obtenerJwt());
        return auth.generateToken(json, new JWTOptions().setExpiresInSeconds(segundos));
    }

    /**
     * Obtiene un token de sesion para la aplicacion para autenticarse con kong
     *
     * @param idAplicacion
     * @param iss
     * @param minutos
     * @param context
     * @return
     */
    private Future<String> obtenerTokenSesion(AplicacionEO credenciales, String curp, Integer idUsuario) {
        Future<String> future = Future.future();

        JsonObject json = new JsonObject()
                .put(USUARIO, credenciales.getIdAplicacion())
                .put(ISS, credenciales.getIss())
                .put(EXP_MINUTOS, credenciales.getMinutos())
                .put(DATA, new JsonObject().put(CURP,curp).put("idUsuario", idUsuario));

        client.postAbs(URL).sendJson(json, ar -> {

            if (ar.succeeded() && ar.result().statusCode() == HttpResponseStatus.OK.code()) {
                future.complete(ar.result().bodyAsString().replaceAll("\"", ""));

            } else {
                LOGGER.error(ar.cause().getMessage());
                future.fail(ar.cause());
            }
        });
        return future;
    }
    
    /**
     * Método que realiza las pruebas de salud del contenedor ya en despligue en cubernates.
     * 
     * @param routingContext 
     */
    private void registerHandler(RoutingContext routingContext){
        try{
            vertx.eventBus().send(HEALTH_CHECK, new JsonObject(), testing->{
                if (testing.succeeded()){
                    JsonObject respuestas = (JsonObject)testing.result().body();
                    routingContext.response().setStatusCode(HttpResponseStatus.OK.code()).end(respuestas.encode());
                }else{
                    routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end(testing.cause().toString());
                }
            });
        }catch(Exception e){            
            routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end("Error de ejecución");
            LOGGER.error(e);
        }
    }
}
