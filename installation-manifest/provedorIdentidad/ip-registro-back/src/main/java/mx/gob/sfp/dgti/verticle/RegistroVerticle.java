package mx.gob.sfp.dgti.verticle;

import static mx.gob.sfp.dgti.util.Constantes.ADDRESS;
import static mx.gob.sfp.dgti.util.Constantes.ADDRESS_EMAIL;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_PORT;
import static mx.gob.sfp.dgti.util.Constantes.CURP;
import static mx.gob.sfp.dgti.util.Constantes.EMPTY;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_AUTHORIZATION_KEY;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_TYPE;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ORIGINS;
import static mx.gob.sfp.dgti.util.Constantes.ID;
import static mx.gob.sfp.dgti.util.Constantes.IP_SERVER_PORT;
import static mx.gob.sfp.dgti.util.Constantes.LONGITUD_CURP;
import static mx.gob.sfp.dgti.util.Constantes.LONGITUD_RFC;
import static mx.gob.sfp.dgti.util.Constantes.PARAMETRO_ESTATUS_VALIDADO_RENAPO;
import static mx.gob.sfp.dgti.util.Constantes.REGEX_PARA_CORREO;
import static mx.gob.sfp.dgti.util.Constantes.REGEX_PARA_CURP;
import static mx.gob.sfp.dgti.util.Constantes.RENAPO_ESTATUS_INCONSISTENTE;
import static mx.gob.sfp.dgti.util.Constantes.RFC;
import static mx.gob.sfp.dgti.util.Constantes.SECRETKEY;
import static mx.gob.sfp.dgti.util.Constantes.TEXT_HTML;
import static mx.gob.sfp.dgti.util.Constantes.TOKEN_24HR;

import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.handler.StaticHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import mx.gob.sfp.dgti.dto.RespuestaDTO;
import mx.gob.sfp.dgti.entity.AplicacionEO;
import mx.gob.sfp.dgti.entity.UsuarioEO;
import static mx.gob.sfp.dgti.helper.HealthChecks.healthChecks;
import mx.gob.sfp.dgti.persistence.DatabaseService;
import mx.gob.sfp.dgti.util.AutenticacionHelper;
import mx.gob.sfp.dgti.util.Constantes;
import static mx.gob.sfp.dgti.util.Constantes.ID_TELEGRAM;
import mx.gob.sfp.dgti.util.ValidacionHelper;

/**
 * Servicio para registrar a usuarios y aplicaciones clientes
 *
 * @author Miriam Sanchez Sanchez programador07
 * @since 01/04/2019
 */
public class RegistroVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistroVerticle.class);
    private static final String URL_REESTABLECER_PWD_FRONT = "/recovery";
    private static final String URL_USUARIO = "/api/private/user";
    private static final String URL_OPER_USUARIO = "/api/oper/user";
    private static final String URL_REGISTRO_APLICACION = "/api/private/app";
    private static final String URL_CONSULTA_USUARIO = "/api/private/user/:id";
    private static final String URL_SOLICITUD_REESTABLECER_CONTRASENA = "/api/private/get-recovery";
    private static final String URL_REESTABLECER_CONTRASENA = "/api/private/recovery";
    private static final String URL_REESTABLECER_CONTRASENA_DAY_OPER = "/api/public/recovery-day-oper";
//    private static final String URL_CONSULTA_CURP_RFC = "/api/public/search";
    private static final String URL_CONSULTA_CURP_RFC = "/api/private/search";
    private static final String URL_CONSULTA_TELEGRAM_CURP = "/api/consulta-user-telegram/:curp/:idTelegram";
    private static final String URL_ACTUALIZA_TELEGRAM_CURP = "/api/actualiza-user-telegram/";
    private static final String URL_CONSULTA_CURP_RFC_NAME_LIKE = "/api/public/search/like-curp-rfc-name/:idSistema/:secret/:valCons";
    private static final String URL_HELTH_CHECKER = "/healt-checker/";
    private static final String REGEX_CORREO = "(?<=.{3}).(?=.*@)";
    private static final String ASTERISCO = "*";
    private DatabaseService databaseService;
    private JWTAuth auth;
    private Router router;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        this.databaseService = DatabaseService.create(vertx, AutenticacionHelper.getConfig());
        this.auth = JWTAuth.create(vertx, AutenticacionHelper.obtenerJwt());
        this.router = Router.router(vertx);
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader(HEADER_CONTENT_TYPE, TEXT_HTML).end("Hola IP registro...");
        });
        router.route("/api/public/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader(HEADER_CONTENT_TYPE, TEXT_HTML).end("Hola IP registro...");
        });
        router.route().handler(CorsHandler.create(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                .allowedHeaders(AutenticacionHelper.getAllowedHeaders())
                .allowedMethods(AutenticacionHelper.getAllowedMethods()));
    }

    /**
     * Start del verticle
     */
    @Override
    public void start(Future<Void> future) throws Exception {
        super.start();
        router.route().handler(BodyHandler.create());
        router.post(URL_USUARIO).handler(this::agregarUsuario);
        router.put(URL_USUARIO).handler(this::editarUsuario);
        router.get(URL_CONSULTA_USUARIO).handler(this::consultarUsuario);
        router.post(URL_REGISTRO_APLICACION).handler(this::agregarAplicacion);
        router.get(URL_CONSULTA_CURP_RFC).handler(this::consultarCurpYRFC);
        router.get(URL_CONSULTA_CURP_RFC_NAME_LIKE).handler(this::consultarCurpORfcONameLike);
        router.get(URL_SOLICITUD_REESTABLECER_CONTRASENA).handler(this::solicitudReestablecerConstrasena);
        router.post(URL_REESTABLECER_CONTRASENA).handler(JWTAuthHandler.create(auth)).handler(this::reestablecerContrasena);
        router.post(URL_REESTABLECER_CONTRASENA_DAY_OPER).handler(this::reestablecerContrasenaDayOper);
        router.get(URL_OPER_USUARIO).handler(this::obtenerUsuario);
        router.put(URL_OPER_USUARIO).handler(this::editarCurpYEmailsDeUsuario);
        router.get(URL_HELTH_CHECKER).handler(this::registerHandler);
        router.get(URL_CONSULTA_TELEGRAM_CURP).handler(this::consultaUsaurioTelegram);
        router.post(URL_ACTUALIZA_TELEGRAM_CURP).handler(this::guardaUsuarioTelegram);
        
        router.route().handler(StaticHandler.create());
        this.getVertx().createHttpServer().requestHandler(router::accept).listen(
                config().getInteger(CONFIG_PORT, Integer.parseInt(System.getenv(IP_SERVER_PORT) != null ? System.getenv(IP_SERVER_PORT) : "8081")), ar -> {
                    if (ar.succeeded()) {
                        LOGGER.info("Running");
                        future.complete();
                    } else {
                        LOGGER.error(ar.cause());
                        future.fail(ar.cause());
                    }
                }
        );
    }

    /**
     * Registro del usuario, se validan las peticiones previas OPTIONS.
     *
     * @param routingContext
     */
    private void agregarUsuario(RoutingContext routingContext) {
        AutenticacionHelper.obtenerHeaders(routingContext);

        if (routingContext.request().method() == HttpMethod.OPTIONS) {
            routingContext.response().setStatusCode(HttpResponseStatus.OK.code()).end();

        } else {
            LOGGER.info("============= REGISTRO USUARIO POST =============== ");

            if (!"".equals(routingContext.getBodyAsString())) {

                UsuarioEO usuario = Json.decodeValue(routingContext.getBodyAsString(), UsuarioEO.class);

                if (usuario.getCurp() != null && !EMPTY.equals(usuario.getCurp())) {

                    this.databaseService.guardarUsuario(usuario).setHandler(ar -> {
                        if (ar.failed()) {
                            if (ar.cause() instanceof NoSuchElementException) {
                                RespuestaDTO respuesta = new RespuestaDTO();
                                respuesta.setError(ar.cause().getMessage());
                                routingContext.response().setStatusCode(200).end(Json.encodePrettily(respuesta));
                            } else {
                                routingContext.fail(ar.cause());
                            }
                        } else {
                            vertx.eventBus().send(ADDRESS, usuario.getCurp());
                            routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                                    .putHeader(Constantes.HEADER_CONTENT_TYPE, Constantes.HEADER_CONTENT_APPLICATION_JSON).end();
                        }
                    });

                } else {
                    LOGGER.error("==== REGISTRO USUARIO: La curp viene vacía o nula ");
                    routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
                }
            } else {
                LOGGER.error("==== REGISTRO USUARIO: La validacion de los parametros fallo ");
                routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
            }
        }
    }

    /**
     * Registro de la aplicacion, se validan las peticiones previas OPTIONS.
     *
     * @param routingContext
     */
    private void agregarAplicacion(RoutingContext routingContext) {
        AutenticacionHelper.obtenerHeaders(routingContext);
        if (routingContext.request().method() == HttpMethod.OPTIONS) {
            routingContext.response().setStatusCode(HttpResponseStatus.OK.code()).end();

        } else {
            LOGGER.info("============= REGISTRO APLICACION POST =============== ");

            if (!"".equals(routingContext.getBodyAsString())) {
                AplicacionEO aplicacion = Json.decodeValue(routingContext.getBodyAsString(), AplicacionEO.class);
                this.databaseService.guardarAplicacion(aplicacion).setHandler(AutenticacionHelper.writeJsonResponse(routingContext));
            } else {
                LOGGER.error("La validacion de los parametros fallo ");
                routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
            }
        }
    }

    /**
     * Consulta usuario por curp
     *
     * @param context
     */
    private void consultarUsuario(RoutingContext context) {
        AutenticacionHelper.obtenerHeaders(context);

        if (context.request().getParam(CURP) != null && context.request().getParam(SECRETKEY) != null) {

            String origen = context.request().getHeader(HEADER_ORIGINS) != null ? context.request().getHeader(HEADER_ORIGINS)
                    : AutenticacionHelper.validarDominio(context.request().getHeader(HEADER_ORIGIN));

            this.databaseService.consultarUsuarioVinculado(origen, context.request().getParam(CURP), Integer.parseInt(context.request().getParam(ID)),
                    context.request().getParam(SECRETKEY)).setHandler(consulta -> {
                        LOGGER.info("===== CONSULTAR USUARIO: resultado consultarUsuario " + context.request().getParam(CURP) + ": " + consulta.result());

                        if (consulta.succeeded()) {
                            if (consulta.result() != null) {
                                context.response().end(Json.encodePrettily(consulta.result()));
                            } else {
                                context.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end();
                            }
                        } else {
                            if (consulta.cause() instanceof NoSuchElementException) {
                                context.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end();
                            }
                            LOGGER.error("==== CONSULTAR USUARIO: Error al consultarUsuario: " + consulta.cause().getMessage());
                            context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                        }
                    });
        } else {
            LOGGER.info("==== CONSULTAR USUARIO: consultarUsuario  bad request ");
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }

    /**
     * Consulta si existe la curp o rfc en algún registro de usuario existente
     *
     * @param context
     */
    private void consultarCurpYRFC(RoutingContext context) {

        AutenticacionHelper.obtenerHeaders(context);
        if (context.request().getParam(CURP) != null || context.request().getParam(RFC) != null) {
            LOGGER.info("=====CONSULTARCURPYRFC:::: " + context.request().getParam(CURP) + " -- " + context.request().getParam(RFC));

            this.databaseService.consultarCurpYRFC(context.request().getParam(CURP), context.request().getParam(RFC)).setHandler(consulta -> {
                RespuestaDTO respuesta = new RespuestaDTO();
                if (consulta.succeeded()) {
                    LOGGER.info("===== CONSULTARCURPYRFC: resultado de la consulta " + context.request().getParam(CURP) + ": " + consulta.result());
                    respuesta.setCodigo(consulta.result());
                    context.response().end(Json.encode(respuesta));
                } else {
                    LOGGER.error("==== CONSULTARCURPYRFC: falló al consultarcurpyRFC " + consulta.cause().getMessage());
                    context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                }
            });
        } else {
            LOGGER.error("====== CONSULTARCURPYRFC: BAD REQUEST ");
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }
    
    /**
     * Consulta si existe la curp o rfc en algún registro de usuario existente
     *
     * @param context
     */
    private void consultarCurpORfcONameLike(RoutingContext context) {
        StringBuilder data = new StringBuilder();
        AutenticacionHelper.obtenerHeaders(context);
        if (context.pathParam("idSistema") != null && !context.pathParam("idSistema").isEmpty() 
                && context.pathParam("secret") != null && !context.pathParam("secret").isEmpty()
                && context.pathParam("valCons") != null && !context.pathParam("valCons").isEmpty() 
                && (context.request().params().isEmpty() || context.request().params().size() > 1)){
            JsonArray parametros = new JsonArray();
            parametros.add(context.pathParam("idSistema"));
            parametros.add(context.pathParam("secret"));
            if (validaYcreaParametros(data, parametros, context.pathParam("valCons"), context.request().params())) {                
                this.databaseService.consultarLike(data, parametros).setHandler(consulta -> {
                    if (consulta.succeeded()) {
                        context.response().end(new JsonObject().put("datosLocalizados", consulta.result()).encode());
                    } else {
                        LOGGER.error("==== consultarCurpORfcONameLike: falló al consultarcurpyRFC " + consulta.cause().getMessage());
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                    }
                });
            }else{
                LOGGER.error("====== consultarCurpORfcONameLike: BAD REQUEST validacion param ");
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(data.toString());
            }
        } else {
            LOGGER.error("====== consultarCurpORfcONameLike: BAD REQUEST ");
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }

    /**
     * Se genera la solicitud para reestablecer el password de un usuario
     *
     * @param context
     */
    public void solicitudReestablecerConstrasena(RoutingContext context) {
        LOGGER.info("=============  solicitudReestablecerConstrasena =============  ");
        AutenticacionHelper.obtenerHeaders(context);

        if (context.request().getParam(CURP) != null) {
            String curp = context.request().getParam(CURP);
            String origin = context.request().getHeader(HEADER_ORIGIN);

            this.databaseService.obtenerUsuarioPorCurp(curp).setHandler(resultado -> {

                if (resultado.failed()) {
                    if (resultado.cause() instanceof NoSuchElementException) {
                        LOGGER.info("==== solicitudReestablecerConstrasena: Curp no existe");
                        context.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end();
                    } else {
                        LOGGER.info("==== solicitudReestablecerConstrasena: Hubo un error obtenerUsuarioPorCurp " + curp + resultado.cause().getMessage());
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                    }
                } else {
                    String token = generarToken(new JsonObject().put(CURP, resultado.result().getCurp()).put("FECHA", ValidacionHelper.getFechaFormatoISO8601(new Date())), TOKEN_24HR);
                    this.databaseService.guardarConfirmacion(resultado.result().getIdUsuario(), token).setHandler(confirmacion -> {
                        if (confirmacion.succeeded()) {
                            String link = origin + URL_REESTABLECER_PWD_FRONT + "?curp=" + curp + "&token=" + Constantes.TOKEN_BEARER_KEY + token;
                            String institucional = resultado.result().getEmail();
                            String personal = resultado.result().getEmailAlt();

                            String nombreCompleto = (resultado.result().getNombre() != null ? resultado.result().getNombre() : "")
                                + " " + (resultado.result().getPrimerApellido() != null ? resultado.result().getPrimerApellido() : "")
                                + " " + (resultado.result().getSegundoApellido() != null ? resultado.result().getSegundoApellido() : "");
                            vertx.eventBus().send(ADDRESS_EMAIL, new JsonObject().put("link", link).put("email", obtenerEmails(institucional, personal)).put("nombre", nombreCompleto.trim()));
                            resultado.result().setCurp(null);
                            resultado.result().setEmailAlt(null);
                            String emailIFront = validarEmail(institucional) != null ? institucional.replaceAll(REGEX_CORREO, ASTERISCO) : "";
                            String emailPFront = validarEmail(personal) != null ? personal.replaceAll(REGEX_CORREO, ASTERISCO) : "";
                            resultado.result().setEmail(emailIFront + " " + emailPFront);
                            context.response().setStatusCode(HttpResponseStatus.OK.code())
                                    .putHeader(Constantes.HEADER_AUTHORIZATION_KEY, Constantes.TOKEN_BEARER_KEY + token)
                                    .putHeader(Constantes.HEADER_CONTENT_TYPE, Constantes.HEADER_CONTENT_APPLICATION_JSON)
                                    .end(Json.encode(resultado.result()));
                        } else {
                            LOGGER.info("===== solicitudReestablecerConstrasena: Hubo un problema al guardar la confirmación " + confirmacion.cause().getMessage());
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
     * Se recibe la solicitud para reestablecer el password del usuario con la
     * nueva contraseña
     *
     * @param context
     */
    public void reestablecerContrasena(RoutingContext context) {
        LOGGER.info("=============  reestablecerContrasena =============  ");
        AutenticacionHelper.obtenerHeaders(context);
        if (!"".equals(context.getBodyAsString())) {
            UsuarioEO usuario = Json.decodeValue(context.getBodyAsString(), UsuarioEO.class);

            if (usuario.getCurp().equals(context.user().principal().getValue(CURP))) {
                String token = context.request().getHeader(HEADER_AUTHORIZATION_KEY);
                this.databaseService.obtenerConfirmacion(usuario.getCurp(), token).setHandler(confirmacion -> {
                    if (confirmacion.succeeded()) {
                        this.databaseService.actualizarPwd(usuario, confirmacion.result().getIdConfirmacion()).setHandler(resultado -> {
                            if (resultado.succeeded()) {
                                context.clearUser();
                                usuario.setPwdEnc(null);
                                context.response().end(Json.encode(usuario));
                            } else {
                                LOGGER.error("Error al reestablecer contraseña " + usuario.getCurp());
                                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                            }
                        });
                    } else {
                        LOGGER.info("====== reestablecerContrasena: no content");
                        context.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end();
                    }
                });
            } else {
                LOGGER.info("===== reestablecerContrasena: no content");
                context.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end();
            }
        } else {
            LOGGER.info("===== reestablecerContrasena: bad request");
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }
    
    
    /**
     * Se recibe la solicitud para reestablecer el password del usuario con la
     * nueva contraseña
     *
     * @param context
     */
    public void reestablecerContrasenaDayOper(RoutingContext context) {
        LOGGER.info("=============  reestablecerContrasena automatica from oper=============  ");
        AutenticacionHelper.obtenerHeaders(context);
        if (!"".equals(context.getBodyAsString())) {
            UsuarioEO usuario = Json.decodeValue(context.getBodyAsString(), UsuarioEO.class);
            if (!EMPTY.equals(usuario.getIdUsuario())) {                
                    usuario.setPwdEnc("temp" + new SimpleDateFormat("yyyyMMdd").format(new Date()));
                    this.databaseService.actualizarPwd(usuario, null).setHandler(resultado -> {
                        if (resultado.succeeded()) {
                            usuario.setPwdEnc(null);
                            context.response().setStatusCode(HttpResponseStatus.OK.code()).end(new JsonObject().put("OK", "Restablecida Automaticamente").toString());
                        } else {
                            LOGGER.error("Error al reestablecer contraseña automatica " + usuario.getCurp());
                            context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                        }
                    });
            } else {
                LOGGER.info("===== reestablecerContrasena: no content");
                context.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end();
            }
        } else {
            LOGGER.info("===== reestablecerContrasena: bad request");
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }

    /**
     * Metodo que recibe los objetos para construir un token
     *
     * @param json {@link JsonObject} la informacion con la que queremos
     * construir el token
     * @param segundos el tiempo que le asignamos de vida al token
     * @return String
     */
    private String generarToken(JsonObject json, int segundos) {
        JWTAuth auth = JWTAuth.create(vertx, AutenticacionHelper.obtenerJwt());
        return auth.generateToken(json, new JWTOptions().setExpiresInSeconds(segundos));
    }

    /**
     * Editar usuario de acuerdo al estatus de Validado Renapo
     * ESTATUS_VALIDADO_RENAPO = 2 sólo se podrá modificar la curp
     * ESTATUS_VALIDADO_RENAPO = 3 sólo se podrán modificar nombre y apellidos
     *
     * @param context
     */
    public void editarUsuario(RoutingContext context) {
        LOGGER.info("=============  editarUsuario =============  ");
        AutenticacionHelper.obtenerHeaders(context);

        if (!"".equals(context.getBodyAsString())
                && context.request().getParam(PARAMETRO_ESTATUS_VALIDADO_RENAPO) != null && context.request().getParam(CURP) != null) {
            UsuarioEO usuario = Json.decodeValue(context.getBodyAsString(), UsuarioEO.class);
            String curp = context.request().getParam(CURP);
            Integer estatusValidadoRenapo = Integer.parseInt(context.request().getParam(PARAMETRO_ESTATUS_VALIDADO_RENAPO));

            this.databaseService.editarUsuario(usuario, estatusValidadoRenapo, curp).setHandler(editar -> {

                if (editar.succeeded()) {
                    context.response().setStatusCode(HttpResponseStatus.OK.code()).end();
                    String curpActual = estatusValidadoRenapo.equals(RENAPO_ESTATUS_INCONSISTENTE) ? curp : usuario.getCurp();
                    vertx.eventBus().send(ADDRESS, curpActual);

                } else {
                    context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                }
            });

        } else {
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }

    /**
     * Método para validar los emails
     *
     * @param institucional
     * @param personal
     * @return
     */
    public String obtenerEmails(String institucional, String personal) {
        LOGGER.info(institucional + " - " + personal);

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
        if (correo != null && Pattern.compile(REGEX_PARA_CORREO, Pattern.CASE_INSENSITIVE).matcher(correo).matches()) {
            return correo;
        }
        return null;
    }

    /**
     * Editar la curp o emails de un usuario
     *
     * @param context
     */
    public void editarCurpYEmailsDeUsuario(RoutingContext context) {
        LOGGER.info("=============  editarCurpYEmailsDeUsuario =============  ");
        AutenticacionHelper.obtenerHeaders(context);

        if (!"".equals(context.getBodyAsString()) && context.request().getParam(CURP) != null) {
            String curp = context.request().getParam(CURP);
            UsuarioEO usuario = Json.decodeValue(context.getBodyAsString(), UsuarioEO.class);
            usuario.setEmail(validarEmail(usuario.getEmail()));
            usuario.setEmailAlt(EMPTY.equals(usuario.getEmailAlt()) ? usuario.getEmailAlt() : validarEmail(usuario.getEmailAlt()));
            usuario.setCurp(usuario.getCurp() != null
                    && Pattern.compile(REGEX_PARA_CURP).matcher(usuario.getCurp()).matches() == true
                            ? usuario.getCurp() : null);

            if ((usuario.getCurp() != null && Pattern.compile(REGEX_PARA_CURP).matcher(usuario.getCurp()).matches())
                    || validarEmail(usuario.getEmail()) != null
                    || validarEmail(usuario.getEmailAlt()) != null) {

                if (usuario.getCurp() != null) {
                    LOGGER.info("Se va a consultar la curp nueva para ver que no exista");
                    this.databaseService.consultarUsuarioPorCurp(usuario.getCurp()).setHandler(usuarioConsulta -> {

                        if (usuarioConsulta.succeeded() && usuarioConsulta.result() != null) {
                            LOGGER.info("La consulta arroja un usuario con esa curp");
                            context.response().setStatusCode(HttpResponseStatus.CONFLICT.code()).end();
                        } else {
                            LOGGER.info("La curp nueva no existe, se sigue el proceso");
                            editarCurpYEmailsDeUsuario(context, usuario, curp, true);
                        }
                    });
                } else {
                    LOGGER.info("La curp del body de usuario es nula");
                    editarCurpYEmailsDeUsuario(context, usuario, curp, false);
                }
            } else {
                LOGGER.info("La validación de los datos recibidos no pasó");
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
            }
        } else {
            LOGGER.info("Los parametros son erróneos");
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }

    /**
     * Metodo para editar curp y/o emails de un usuario
     *
     * @param context
     * @param usuario
     * @param curp
     */
    private void editarCurpYEmailsDeUsuario(RoutingContext context, UsuarioEO usuario, String curp, boolean envioCorreo) {
        LOGGER.info("Entrando a editarCurpYEmails " + usuario.toString());

        this.databaseService.editarCurpYEmailsDeUsuario(usuario, curp).setHandler(usuarioEditado -> {
            if (usuarioEditado.succeeded()) {
                if (usuarioEditado.result() != null) {
                    LOGGER.info("La edición fue correcta ");
                    context.response().setStatusCode(HttpResponseStatus.OK.code()).end();
                    //Se comenta porque se està reiniciando el servicio
//					if(envioCorreo) {
//						this.graphService.consumirServicioEnvioEmailCurp(URL_ENVIO_EMAIL, usuario, 
//							obtenerEmails(usuarioEditado.result().getEmail(), usuarioEditado.result().getEmailAlt()));
//					}
                } else {
                    LOGGER.info("El usuario con esa curp no existe");
                    context.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end();
                }
            } else {
                LOGGER.info("Hubo un error al editar en la bd " + usuarioEditado.cause().getMessage());
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
            }
        });
    }

    /**
     * Obtener curp, emails de un usuario mediante su curp
     *
     * @param context
     * @param usuario
     * @param curp
     */
    private void obtenerUsuario(RoutingContext context) {
        LOGGER.info("=============  obtenerUsuario =============  ");
        AutenticacionHelper.obtenerHeaders(context);

        if (context.request().getParam(CURP) != null) {
            String curp = LONGITUD_CURP == context.request().getParam(CURP).length() ? context.request().getParam(CURP) : null;
            String rfc = LONGITUD_RFC == context.request().getParam(CURP).length() ? context.request().getParam(CURP) : null;

            if (curp != null || rfc != null) {
                this.databaseService.obtenerUsuarioPorCurp(curp, rfc).setHandler(consulta -> {
                    LOGGER.info("Resultado de la consulta curp " + curp + ", rfc " + rfc + " ::: " + consulta.result());

                    if (consulta.succeeded()) {
                        context.response().putHeader(Constantes.HEADER_AUTHORIZATION_KEY,
                                generarToken(new JsonObject().put(CURP, curp), 60));
                        context.response().end(Json.encode(consulta.result()));

                    } else {
                        if (consulta.cause() instanceof NoSuchElementException) {
                            LOGGER.info("No existe el usuario con la curp " + curp + "o  rfc: " + rfc);
                            context.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end();
                        } else {
                            LOGGER.error("Error: " + consulta.cause().getMessage());
                            context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                        }
                    }
                });
            } else {
                context.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end();
            }
        } else {
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }
    
    /**
     * Método que realiza la consulta de los datos del usuario Telegram.
     * @param context 
     */
    private void consultaUsaurioTelegram(RoutingContext context){
        if (context.pathParam(CURP)!= null && !context.pathParam(CURP).isEmpty() &&
                context.pathParam(ID_TELEGRAM)!= null && !context.pathParam(ID_TELEGRAM).isEmpty()){
            this.databaseService.consultarUsuarioTelegram(context.pathParam(CURP), context.pathParam(ID_TELEGRAM),resultado->{
               if(resultado.succeeded()){
                   context.response().setStatusCode(resultado.result().isEmpty()? HttpResponseStatus.NO_CONTENT.code():HttpResponseStatus.OK.code()).end(resultado.result().encode());
               }else{
                   context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
               }
            });
        }else{
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }
    
    /**
     * Método que realiza la consulta de los datos del usuario Telegram.
     * @param context 
     */
    private void guardaUsuarioTelegram(RoutingContext context){
        if (!context.getBodyAsJson().isEmpty()&&                
                context.getBodyAsJson().getInteger("idUsuario")!= null &&
                context.getBodyAsJson().getString("idTelegram")!= null && !context.getBodyAsJson().getString("idTelegram").isEmpty()){
            this.databaseService.guardaUsuarioTelegram(context.getBodyAsJson(),resultado->{
               if(resultado.succeeded()){
                   context.response().setStatusCode(HttpResponseStatus.OK.code()).end(new JsonObject().encode());
               }else{
                   context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
               }
            });
        }else{
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }    
    
    private boolean validaYcreaParametros(StringBuilder data, JsonArray parametros,String valCons, MultiMap parametro){
        boolean validado = false;
        if (parametro.contains("curp") && valCons.length() > 5){
            data.append("curp like ? ");
            parametros.add(valCons.toUpperCase().concat("%"));
            validado= true;
        }else if (parametro.contains("rfc") && valCons.length() > 5){
            data.append("rfc || homocve like ? ");
            parametros.add(valCons.toUpperCase().concat("%"));                    
            validado= true;
        }else if (parametro.contains("nombre") && valCons.contains(" ") && valCons.split(" ")[0].length() > 2 && valCons.split(" ")[1].length() > 2){
            data.append("nombre || iu.primer_apellido || iu.segundo_apellido like ? ");
            parametros.add(valCons.replace(" ", "%").toUpperCase().concat("%"));
            validado= true;
        }
        return validado;
    }
    
    /**
     * Método que realiza las pruebas de salud del contenedor ya en despligue en cubernates.
     * 
     * @param routingContext 
     */
    private void registerHandler(RoutingContext routingContext){
        try{
            vertx.eventBus().send(healthChecks, new JsonObject(), testing->{
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
