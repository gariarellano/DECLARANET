package mx.gob.sfp.dgti.verticle;

import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_EXPOSE_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.ADDRESS;
import static mx.gob.sfp.dgti.util.Constantes.AMBIENTE;
import static mx.gob.sfp.dgti.util.Constantes.AMPERSAND;
import static mx.gob.sfp.dgti.util.Constantes.ANIO;
import static mx.gob.sfp.dgti.util.Constantes.CERO;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_PORT;
import static mx.gob.sfp.dgti.util.Constantes.CURP;
import static mx.gob.sfp.dgti.util.Constantes.DATOS_GENERALES;
import static mx.gob.sfp.dgti.util.Constantes.DATOS_PERSONALES;
import static mx.gob.sfp.dgti.util.Constantes.DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.DIGEST;
import static mx.gob.sfp.dgti.util.Constantes.DIGITO_VERIFICADOR;
import static mx.gob.sfp.dgti.util.Constantes.DOM_BITACORA_FIEL;
import static mx.gob.sfp.dgti.util.Constantes.DOM_CONSULTA_ACUSE;
import static mx.gob.sfp.dgti.util.Constantes.DOM_CONSULTA_DECLA;
import static mx.gob.sfp.dgti.util.Constantes.DOM_CONSULTA_DECLA_REPORTE;
import static mx.gob.sfp.dgti.util.Constantes.DOM_GUARDAR_DECLA;
import static mx.gob.sfp.dgti.util.Constantes.EMPTY;
import static mx.gob.sfp.dgti.util.Constantes.ENCABEZADO;
import static mx.gob.sfp.dgti.util.Constantes.ENTE;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_AL_FIRMAR;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_COMUNICACION;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_DATOS_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_DECLARACION_MODIFICACION_PERIODO;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_VALIDACION_CURP;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_VALIDACION_CURP_HEADER_DATOS_GENERALES;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_VALIDACION_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_VALIDACION_RFC;
import static mx.gob.sfp.dgti.util.Constantes.ESPACIO;
import static mx.gob.sfp.dgti.util.Constantes.EXITO;
import static mx.gob.sfp.dgti.util.Constantes.EXT_FUP;
import static mx.gob.sfp.dgti.util.Constantes.FECHA_ANIO_A_MILLIS;
import static mx.gob.sfp.dgti.util.Constantes.FECHA_FORMATO_CORTA;
import static mx.gob.sfp.dgti.util.Constantes.FORMATO;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_AUTHORIZATION_KEY;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_APPLICATION_JSON;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_TYPE;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_DOMINIO;
import static mx.gob.sfp.dgti.util.Constantes.IGUAL;
import static mx.gob.sfp.dgti.util.Constantes.INSTITUCION_RECEPTORA;
import static mx.gob.sfp.dgti.util.Constantes.JSON_VACIO;
import static mx.gob.sfp.dgti.util.Constantes.MAS_PARAMETRO;
import static mx.gob.sfp.dgti.util.Constantes.MENSAJE;
import static mx.gob.sfp.dgti.util.Constantes.MENSAJE_ERROR_DECLARACION_DIFERENTE;
import static mx.gob.sfp.dgti.util.Constantes.NOMBRE;
import static mx.gob.sfp.dgti.util.Constantes.NOMBRE_FIRM;
import static mx.gob.sfp.dgti.util.Constantes.NO_CERTIFICADO;
import static mx.gob.sfp.dgti.util.Constantes.NUMERO_CERTIFICADO;
import static mx.gob.sfp.dgti.util.Constantes.PARAMETRO_RFC;
import static mx.gob.sfp.dgti.util.Constantes.PARAMETRO_TRANSACCION;
import static mx.gob.sfp.dgti.util.Constantes.PARAM_FIRMADA;
import static mx.gob.sfp.dgti.util.Constantes.PATH_RAIZ;
import static mx.gob.sfp.dgti.util.Constantes.PROPERTIES;
import static mx.gob.sfp.dgti.util.Constantes.PUERTO;
import static mx.gob.sfp.dgti.util.Constantes.PUNTO;
import static mx.gob.sfp.dgti.util.Constantes.RFC;
import static mx.gob.sfp.dgti.util.Constantes.SHADECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.TEXT_HTML;
import static mx.gob.sfp.dgti.util.Constantes.TIPO_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.URL_FIRMA;
import static mx.gob.sfp.dgti.util.Constantes.URL_FIRMADO;
import static mx.gob.sfp.dgti.util.Constantes.USUARIO;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import mx.gob.sfp.dgti.EncripDecla;
import mx.gob.sfp.dgti.appi.ip.AppiExtensionSFP;
import mx.gob.sfp.dgti.appi.ip.enu.EnumPathsEnables;
import mx.gob.sfp.dgti.declaracion.encrypt.im.GeneraMensajeFront;
import mx.gob.sfp.dgti.declaracion.encrypt.im.ValidaMensajeFront;
import mx.gob.sfp.dgti.dto.AcuseDTO;
import mx.gob.sfp.dgti.dto.BitacoraDTO;
import mx.gob.sfp.dgti.dto.DigestDTO;
import mx.gob.sfp.dgti.dto.FirmaDTO;
import mx.gob.sfp.dgti.dto.ParametrosDTO;
import mx.gob.sfp.dgti.util.Constantes;
import mx.gob.sfp.dgti.util.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.util.EnumTipoFirma;
import mx.gob.sfp.dgti.util.FechaUtil;

/**
 * Clase con la definición de los paths para la recepción de la declaración
 * patrimonial
 *
 * @author Miriam Sanchez Sanchez programador07
 * @since 13/12/2019
 */
public class Server extends AbstractVerticle {

    protected static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private static final String URL_CONSULTA_DECLA = System.getenv(DOM_CONSULTA_DECLA) != null
            ? System.getenv(DOM_CONSULTA_DECLA)
            : obtenerPropiedad("dominio.consulta.declaracion");
    protected static final String URL_CONSULTA_ACUSE = System.getenv(DOM_CONSULTA_ACUSE) != null
            ? System.getenv(DOM_CONSULTA_ACUSE)
            : obtenerPropiedad("dominio.consulta.acuse");
    protected static final String URL_CONSULTA_DECLA_REPORTE = System.getenv(DOM_CONSULTA_DECLA_REPORTE) != null
            ? System.getenv(DOM_CONSULTA_DECLA_REPORTE)
            : obtenerPropiedad("dominio.consulta.declaracion.reporte");
    private static final String URL_BITACORA_FIEL = System.getenv(DOM_BITACORA_FIEL) != null
            ? System.getenv(DOM_BITACORA_FIEL)
            : obtenerPropiedad("dominio.bitacora.fiel");
    ;
	private static final String URL_GUARDAR_DECLA = System.getenv(DOM_GUARDAR_DECLA) != null
            ? System.getenv(DOM_GUARDAR_DECLA)
            : obtenerPropiedad("dominio.guardar.declaracion");
    private static final String ENV_AMBIENTE = System.getenv(AMBIENTE) != null ? System.getenv(AMBIENTE)
            : obtenerPropiedad("ambiente.local");
    private static final String ESTATUS = "estatus";
    private static final String TRANSACCION = "transaccion";
    private static final String FOLIO = "folio";
    private static final String RESPUESTA_VALIDACION = "respuestaValidacion";
    private static final String CODIGO_SHA = "codigoSha";
    private WebClient client;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        client = WebClient.create(vertx);
    }

    /**
     * Start inicializar router y la definición de paths
     *
     * @throws Exception
     */
    @Override
    public void start(Future<Void> future) throws Exception {
        super.start();

        Router router = Router.router(vertx);
        router.route()
                .handler(CorsHandler.create(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).allowedHeaders(getAllowedHeaders()));
        router.route(PATH_RAIZ).handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader(HEADER_CONTENT_TYPE, TEXT_HTML)
                    .end("Hola recepción de firma de la declaraci&oacute;n ...");
        });
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());
        router.post(URL_FIRMA).handler(this::obtenerFirma);
        router.post(URL_FIRMADO).handler(this::obtenerFirmado);
        this.getVertx().createHttpServer().requestHandler(router::accept)
                .listen(config().getInteger(CONFIG_PORT, PUERTO), ar -> {
                    if (ar.succeeded()) {
                        LOGGER.info("Running");
                        future.complete();
                    } else {
                        LOGGER.info(ar.cause());
                        future.fail(ar.cause());
                    }
                });
    }

    /**
     * Obtener la consulta de la declaracion y regresa el digest(parametros +
     * declaracion consultada)
     *
     * @param context
     */
    private void obtenerFirma(RoutingContext context) {
        obtenerHeaders(context);

        if (!EMPTY.equals(context.getBodyAsString())) {

            JsonObject json = ValidaMensajeFront.fidelidadMensaje(context.getBodyAsJson());

            if (json != null) {

                String shaDeclaracion = json.getString(SHADECLARACION);
                consultarDeclaracion(json).setHandler(declaracionCons -> {

                    if (declaracionCons.succeeded() && declaracionCons.result() != null) {

                        JsonObject aValidar = new JsonObject().put(DECLARACION, declaracionCons.result())
                                .put(DIGITO_VERIFICADOR, shaDeclaracion);

                        if (ValidaMensajeFront.fidelidadMensaje(aValidar) != null) {

                            String rfc = obtenerDatosPersonales(declaracionCons.result()).getString(RFC);

                            if (rfc != null && !EMPTY.equals(rfc)) {

                                if (validaPeriodoTipoModificacion(declaracionCons.result())) {

                                    String respuesta = GeneraMensajeFront
                                            .fidelidadMensaje(new JsonObject()
                                                    .put(DIGEST, obtenerDigest(declaracionCons.result())).put(RFC, rfc))
                                            .toString();

                                    context.response().end(respuesta);

                                } else {
                                    LOGGER.info("[OBTENER FIRMA::" + ERROR_DECLARACION_MODIFICACION_PERIODO + "]" + rfc);
                                    context.response().setStatusCode(HttpResponseStatus.CONFLICT.code())
                                            .end(new JsonObject().put(MENSAJE, ERROR_DECLARACION_MODIFICACION_PERIODO)
                                                    .toString());
                                }

                            } else {
                                LOGGER.error("[OBTENER FIRMA::" + ERROR_VALIDACION_RFC + "]");
                                context.response().setStatusCode(HttpResponseStatus.ACCEPTED.code()).end(
                                        new JsonObject().put(MENSAJE, ERROR_VALIDACION_RFC).toString());
                            }

                        } else {
                            LOGGER.error("[OBTENER FIRMA::" + MENSAJE_ERROR_DECLARACION_DIFERENTE + "]" + json.toString());
                            context.response().setStatusCode(HttpResponseStatus.ACCEPTED.code())
                                    .end(new JsonObject().put(MENSAJE, MENSAJE_ERROR_DECLARACION_DIFERENTE).toString());
                        }
                    } else {
                        LOGGER.error("[OBTENER FIRMA::" + ERROR_VALIDACION_DECLARACION + "]" + json.toString());
                        context.response().setStatusCode(HttpResponseStatus.CONFLICT.code())
                                .end(Json.encode(new JsonObject().put(Constantes.MENSAJE_ERROR, ERROR_VALIDACION_DECLARACION)));
                    }
                });

            } else {
                LOGGER.error("[OBTENER FIRMA:: FIDELIDAD MENSAJE NO VALIDÒ EL JSON ]");
                context.response().setStatusCode(HttpResponseStatus.CONFLICT.code())
                        .end(Json.encode(new JsonObject().put(Constantes.MENSAJE_ERROR, ERROR_AL_FIRMAR)));
            }

        } else {
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }

    /**
     * Obtener el objeto de datos personales de acuerdo al tipo de declaración
     *
     * @param declaracion
     * @return
     */
    private JsonObject obtenerDatosPersonales(JsonObject declaracion) {

        if (EnumTipoDeclaracion.NOTA.name().equals(declaracion.getJsonObject(ENCABEZADO).getString(TIPO_DECLARACION))) {
            return declaracion.getJsonObject(ENCABEZADO).getJsonObject(DATOS_PERSONALES);

        } else {
            return declaracion.getJsonObject(DECLARACION).getJsonObject(DATOS_GENERALES).getJsonObject(DATOS_PERSONALES);
        }
    }

    /**
     * Validación para las declaraciones de MODIFICACIÓN No se podrán firmar si
     * son del anio actual y el mes se encuentra entre enero y marzo
     *
     * @param declaracion
     * @return boolean
     */
    private boolean validaPeriodoTipoModificacion(JsonObject declaracion) {
        String tipoDeclaracion = declaracion.getJsonObject(ENCABEZADO).getString(TIPO_DECLARACION);

        if (EnumTipoDeclaracion.MODIFICACION.name().equals(tipoDeclaracion)) {
            Integer anio = declaracion.getJsonObject(ENCABEZADO).getInteger(ANIO);
            Calendar fecha = Calendar.getInstance();
            int mes = fecha.get(Calendar.MONTH);

            if (fecha.get(Calendar.YEAR) == anio
                    && (Calendar.JANUARY == mes || Calendar.FEBRUARY == mes
                    || Calendar.MARCH == mes || Calendar.APRIL == mes)) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * Procesa dos tipos de firma FIEL y FUP FIEL: valida el digest recibido,
     * consulta la bitacora fiel y guarda la información FUP: consulta mediante
     * transaccion y rfc a IP y guarda la información
     *
     * @param routingContext
     */
    private void obtenerFirmado(RoutingContext context) {

        obtenerHeaders(context);

        if (!EMPTY.equals(context.getBodyAsString())) {

            JsonObject json = ValidaMensajeFront.fidelidadMensaje(context.getBodyAsJson());

            if (json != null) {
                consultarDeclaracion(json).setHandler(consultaD -> {
                    ParametrosDTO parametros = Json.decodeValue(json.toString(), ParametrosDTO.class);

                    if (consultaD.succeeded() && consultaD.result() != null) {

                        if (parametros.getDigest() != null && parametros.getDigest().equals(obtenerDigest(consultaD.result()))) {
                            String ente = consultaD.result()
                                    .getJsonObject(INSTITUCION_RECEPTORA)
                                    .getJsonObject(ENTE).getString(NOMBRE);
                            if (EnumTipoFirma.FIEL.equals(parametros.getTipoFirma())) {
                                procesarFIEL(context, parametros, consultaD.result(), ente);
                            } else if (EnumTipoFirma.FUP.equals(parametros.getTipoFirma())) {
                                procesarFUP(context, parametros, consultaD.result(), ente);
                            } else {
                                LOGGER.warn("[GUARDANDO FIRMADO:: EL TIPO DE FIRMA NO EXISTE ] " + parametros.toString());
                                context.response().setStatusCode(HttpResponseStatus.ACCEPTED.code())
                                        .end(Json.encode(new JsonObject().put(Constantes.MENSAJE_ERROR, MENSAJE_ERROR_DECLARACION_DIFERENTE)));
                            }
                        } else {
                            LOGGER.warn("[GUARDANDO FIRMADO:: NO COINCIDEN LOS DIGEST]");
                            context.response().setStatusCode(HttpResponseStatus.ACCEPTED.code())
                                    .end(Json.encode(new JsonObject().put(Constantes.MENSAJE_ERROR, ERROR_DATOS_USUARIO)));
                        }
                    } else {
                        LOGGER.error("[GUARDANDO FIRMADO::LA DECLARACION VIENE VACIA O HUBO UN CONFLICTO AL CONSULTAR LA DECLARACION] " + consultaD.toString());
                        context.response().setStatusCode(HttpResponseStatus.CONFLICT.code())
                                .end(Json.encode(new JsonObject().put(Constantes.MENSAJE_ERROR, ERROR_VALIDACION_DECLARACION)));
                    }
                });
            } else {
                LOGGER.error("[GUARDANDO FIRMADO:: FIDELIDAD MENSAJE NO VALIDÒ EL JSON  ]");
                context.response().setStatusCode(HttpResponseStatus.ACCEPTED.code())
                        .end(new JsonObject().put(Constantes.MENSAJE_ERROR, MENSAJE_ERROR_DECLARACION_DIFERENTE).toString());
            }
        } else {
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }

    /**
     * Procesa el tipo de firma FIEL
     *
     * @param context
     * @param parametros
     * @param declaracionConsultada
     */
    private void procesarFIEL(RoutingContext context, ParametrosDTO parametros, JsonObject declaracionConsultada, String ente) {

        consultarBitacora(parametros).setHandler(bitacora -> {

            if (bitacora.succeeded() && bitacora.result() != null) {
                String rfc = bitacora.result().getString(RFC);

                if (rfc != null && !EMPTY.equals(rfc)) {
                    guardarFirmaDeclaracion(context,
                            crearFirma(EnumTipoFirma.FIEL, parametros, declaracionConsultada, null, bitacora.result()), ente);

                } else {
                    LOGGER.warn("[EL RFC RECIBIDO DE consultarBitacora VIENE VACIO O NULO] " + parametros.getRfc());
                    context.response().setStatusCode(HttpResponseStatus.CONFLICT.code())
                            .end(Json.encode(new JsonObject().put(Constantes.MENSAJE_ERROR, ERROR_VALIDACION_RFC)));
                }

            } else {
                LOGGER.error("[ERROR AL CONSULTAR BITACORA] " + parametros.getRfc() + bitacora.cause().getMessage());
                context.response().setStatusCode(HttpResponseStatus.CONFLICT.code())
                        .end(Json.encode(new JsonObject().put(Constantes.MENSAJE_ERROR, ERROR_COMUNICACION)));
            }
        });
    }

    /**
     * Obtener el comprobante para la firma
     *
     * @param idUsuario
     * @param fechaRecepcion
     * @return
     */
    private String obtenerNoComprobante(Integer idUsuario, Date fechaRecepcion) {
        String usuarioConFormato = String.format(FORMATO, idUsuario);
        String fecha = new SimpleDateFormat(FECHA_ANIO_A_MILLIS).format(fechaRecepcion);
        String[] fechaEnPartes = fecha.split(PUNTO);
        String comprobante = fechaEnPartes[0] + usuarioConFormato + fechaEnPartes[1];
        return comprobante;
    }

    /**
     * Procesa el tipo de firma FUP
     *
     * @param context
     * @param parametros
     */
    private void procesarFUP(RoutingContext context, ParametrosDTO parametros, JsonObject declaracionConsultada, String ente) {

        EnumPathsEnables ambienteActual
                = EnumPathsEnables.PRODUCTION.name().equals(ENV_AMBIENTE) ? EnumPathsEnables.PRODUCTION : EnumPathsEnables.STAGING;

        new AppiExtensionSFP().sfpPetitionToIpToken(client, ambienteActual, parametros.getToken(), parametros.getTransaccion(), (AsyncResult<JsonObject> respuesta) -> {

            if (respuesta.succeeded()) {
                if (respuesta.result() != null && !respuesta.result().isEmpty()
                        && respuesta.result().getString(CURP) != null && !EMPTY.equals(respuesta.result().getString(CURP))) {
                    String curp = respuesta.result().getString(CURP);

                    if (curp.equals(declaracionConsultada.getJsonObject(ENCABEZADO).getJsonObject(USUARIO).getString(CURP))) {

                        if (curp.equals(obtenerDatosPersonales(declaracionConsultada).getString(CURP))) {
                            guardarFirmaDeclaracion(context, crearFirma(EnumTipoFirma.FUP, parametros, declaracionConsultada, curp, null), ente);
                        } else {
                            LOGGER.warn(String.format("[NO COINCIDE CON LA CURP DE IP A LA DE LA DECLARACION (DATOS_GENERALES)] ip = %s , curp-datos-gen= %s", curp, obtenerDatosPersonales(declaracionConsultada).getString(CURP)));
                            context.response().setStatusCode(HttpResponseStatus.CONFLICT.code())
                                    .end(Json.encode(new JsonObject().put(Constantes.MENSAJE_ERROR, ERROR_VALIDACION_CURP_HEADER_DATOS_GENERALES)));
                        }
                    } else {
                        LOGGER.warn(String.format("[NO COINCIDE CON LA CURP DE IP A LA DE LA DECLARACION (HEADER) ip = %s, header =%s", curp, declaracionConsultada.getJsonObject(ENCABEZADO).getJsonObject(USUARIO).getString(CURP)));
                        context.response().setStatusCode(HttpResponseStatus.CONFLICT.code())
                                .end(Json.encode(new JsonObject().put(Constantes.MENSAJE_ERROR, ERROR_VALIDACION_CURP)));
                    }
                } else {
                    LOGGER.error(String.format("[NO SE ENCONTRO LA CURP EN IP] %s",declaracionConsultada.getJsonObject(ENCABEZADO).getJsonObject(USUARIO).getString(CURP)));
                    context.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                            .end(Json.encode(new JsonObject().put(Constantes.MENSAJE_ERROR, ERROR_VALIDACION_CURP)));
                }
            } else {
                LOGGER.error(String.format("===AppiExtensionSFP ERROR %s, con curp de: %s", respuesta.cause(), declaracionConsultada.getJsonObject(ENCABEZADO).getJsonObject(USUARIO).getString(CURP)));
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
            }
        });
    }

    /**
     * Obtener el digest de la declaracion
     *
     * @param declaracion
     * @return
     */
    private String obtenerDigest(JsonObject declaracion) {
        String digest = null;
        if (declaracion != null) {
            digest = EncripDecla.conEspaciosImPares(EncripDecla.digestCadena(declaracion.toString()), ESPACIO);
        }
        return digest;
    }

    /**
     * Función que consulta la declaración al dominio obtenido de una variable
     * de entorno
     *
     * @param json para la consulta de la declaracion
     * @return Future<ParametrosDTO> se le agregan el digest y/o el codigo de
     * respuesta de la petición
     */
    private Future<JsonObject> consultarDeclaracion(JsonObject json) {
        Future<JsonObject> future = Future.future();
        client.postAbs(URL_CONSULTA_DECLA).sendJsonObject(json, ar -> {
            if (ar.succeeded()) {
                if (ar.result().statusCode() == HttpResponseStatus.OK.code()) {

                    if (ar.result().bodyAsString() != null && !ar.result().bodyAsString().equals(JSON_VACIO)) {

                        if (ar.result().bodyAsJsonObject().getBoolean(PARAM_FIRMADA)) {
                            future.complete(ar.result().bodyAsJsonObject());
                        } else {
                            LOGGER.warn("[BANDERA NO FIRMADA]");
                            future.complete();
                        }
                    } else {
                        LOGGER.warn("[CONSULTA DECLA:: NO HAY DECLARACION] " + json.toString());
                        future.complete();
                    }
                } else {
                    LOGGER.error("[CONSULTA DECLA:: FAILED]" + json.toString());
                    future.fail(ar.cause());
                }
            } else {
                LOGGER.error("[CONSULTA DECLA:: FAILED]" + ar.cause().getMessage());
                future.fail(ar.cause());
            }
        });
        return future;
    }

    /**
     * Consulta la bitacora fiel para obtener la informaciónd de la firma de la
     * declaracion
     *
     * @param parametros
     * @return
     */
    private Future<JsonObject> consultarBitacora(ParametrosDTO parametros) {
        Future<JsonObject> future = Future.future();

        String url = URL_BITACORA_FIEL + MAS_PARAMETRO + PARAMETRO_RFC + IGUAL + parametros.getRfc() + AMPERSAND
                + PARAMETRO_TRANSACCION + IGUAL + parametros.getTransaccion();
        client.getAbs(url)
                .timeout(30000)
                .send(ar -> {
                    if (ar.succeeded() && HttpResponseStatus.OK.code() == ar.result().statusCode()) {
                        future.complete(ar.result().bodyAsJsonObject());

                    } else if (ar.succeeded() && HttpResponseStatus.OK.code() != ar.result().statusCode()) {
                        LOGGER.error("[BITACORA FAILED VALIDATE] " + url);
                        future.fail("La consulta a la bitácora respondió con un status != 200: " + ar.result().statusCode());
                    } else {
                        LOGGER.error("[BITACORA FAILED] " + parametros.getRfc() + ar.cause().getMessage());
                        future.fail(ar.cause());
                    }
                });
        return future;
    }

    /**
     * Función para guardar la información de la firma de la declaración
     *
     * @param context
     */
    private void guardarFirmaDeclaracion(RoutingContext context, FirmaDTO firma, String ente) {

        client.postAbs(URL_GUARDAR_DECLA).sendJsonObject(firma.toJson(), ar -> {
            if (ar.succeeded()) {

                if (HttpResponseStatus.OK.code() == ar.result().statusCode()) {
                    context.response().end();
                    vertx.eventBus().send(ADDRESS, JsonObject.mapFrom(firma).put(ENTE, ente));

                } else {
                    LOGGER.error("[GUARDADO FIRMA CODE != 200] " + firma.getDigestion().getNumeroDeclaracion() + " - " + firma.getDigestion().getIdUsuario() + ar.result().statusCode());
                    context.response().setStatusCode(HttpResponseStatus.CONFLICT.code())
                            .end(Json.encode(ERROR_AL_FIRMAR));
                }
            } else {
                LOGGER.error("[GUARDADO FIRMA FAILED] " + firma.getDigestion().getNumeroDeclaracion() + " - " + firma.getDigestion().getIdUsuario() + ar.result().statusCode());
                context.response().setStatusCode(HttpResponseStatus.CONFLICT.code())
                        .end(Json.encode(new JsonObject().put(Constantes.MENSAJE_ERROR, ERROR_COMUNICACION)));
            }
        });
    }

    protected Future<String> obtenerAcuseYDeclaracion(JsonObject json, String url) {
        Future<String> future = Future.future();
        json = GeneraMensajeFront.fidelidadMensaje(json);

        client.postAbs(url).sendJsonObject(json, ar -> {

            if (ar.succeeded()) {

                if (ar.result().statusCode() == HttpResponseStatus.OK.code()) {

                    if (ar.result().bodyAsString() != null && !ar.result().bodyAsString().equals(JSON_VACIO)) {
                        future.complete(ar.result().bodyAsString());
                    } else {
                        LOGGER.warn("NO HAY REPORTE");
                        future.complete();
                    }
                } else {
                    LOGGER.error("[CONSULTA REPORTE != 200] " + ar.cause().getMessage());
                    future.fail(ar.cause());
                }
            } else {
                LOGGER.error("[CONSULTA REPORTE FAILED]" + ar.cause().getMessage());
                future.fail(ar.cause());
            }
        });

        return future;
    }

    /**
     * Crea la firma a guardar
     *
     * @param tipoFirma
     * @param parametros
     * @param declaracionConsultada
     * @param curp
     * @param bitacora
     * @return
     */
    private FirmaDTO crearFirma(EnumTipoFirma tipoFirma, ParametrosDTO parametros, JsonObject declaracionConsultada,
            String curp, JsonObject bitacora) {
        FirmaDTO firma = new FirmaDTO();
        Date fecha = new Date();

        String digestDeDeclaracion = obtenerDigest(declaracionConsultada);
        String noComprobante = obtenerNoComprobante(parametros.getIdUsuario(), fecha);
        String fechaRecepcion = FechaUtil.getFechaFormatoISO8601(fecha);

        firma.setFechaRecepcion(fechaRecepcion);
        firma.setTipoFirma(tipoFirma);
        firma.setFechaId(new SimpleDateFormat(FECHA_FORMATO_CORTA).format(fecha));
        firma.setNoComprobante(noComprobante);
        firma.setDigestion(new DigestDTO(parametros.getNumeroDeclaracion(), parametros.getIdUsuario(),
                parametros.getCollName(), declaracionConsultada, digestDeDeclaracion));

        if (EnumTipoFirma.FIEL.equals(tipoFirma)) {
            String acuse = this.obtenerDigestionAcuse(digestDeDeclaracion, parametros.getRfc(),
                    bitacora.getString(NUMERO_CERTIFICADO), bitacora.getString(NOMBRE_FIRM), fechaRecepcion,
                    noComprobante);

            firma.setFirmado(new BitacoraDTO(bitacora.getInteger(ESTATUS), bitacora.getLong(TRANSACCION),
                    bitacora.getString(RFC), bitacora.getString(NUMERO_CERTIFICADO), bitacora.getString(NOMBRE_FIRM),
                    bitacora.getString(FOLIO), bitacora.getString(RESPUESTA_VALIDACION),
                    bitacora.getString(CODIGO_SHA)));
            firma.setAcuse(new AcuseDTO(acuse, EncripDecla.digestCadena(acuse), null));

        } else if (EnumTipoFirma.FUP.equals(tipoFirma)) {
            String nombreFup = parametros.getTransaccion() + EXT_FUP;
            String acuse = this.obtenerDigestionAcuse(digestDeDeclaracion, curp, NO_CERTIFICADO, nombreFup,
                    fechaRecepcion, noComprobante);
            String transaccion = String.valueOf(parametros.getIdUsuario()) + String.valueOf(fecha.getTime());

            firma.setAcuse(new AcuseDTO(acuse, EncripDecla.digestCadena(acuse), null));
            firma.setFirmado(new BitacoraDTO(curp, transaccion, NO_CERTIFICADO, nombreFup, parametros.getTransaccion(),
                    EXITO, EncripDecla.digestCadena(firma.toString()), CERO));
        }
        return firma;
    }

    /**
     * Headers permitidos para CORS
     *
     * @return Set<String>: Headers permitidos en las peticiones
     */
    public static Set<String> getAllowedHeaders() {
        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add(HEADER_DOMINIO);
        allowedHeaders.add(HEADER_AUTHORIZATION_KEY);
        allowedHeaders.add(HEADER_CONTENT_TYPE);
        return allowedHeaders;
    }

    /**
     * Se agregan los headers correspondientes para la respuesta de las
     * solicitudes
     *
     * @param routingContext: contexto
     * @return HttpServerResponse
     */
    public static HttpServerResponse obtenerHeaders(RoutingContext routingContext) {
        return routingContext.response().putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                .putHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS)
                .putHeader(ACCESS_CONTROL_ALLOW_METHODS, HEADER_ACCESS_CONTROL_ALLOW_METHODS)
                .putHeader(ACCESS_CONTROL_ALLOW_HEADERS, HEADER_ACCESS_CONTROL_ALLOW_HEADERS)
                .putHeader(ACCESS_CONTROL_EXPOSE_HEADERS, HEADER_ACCESS_CONTROL_ALLOW_HEADERS)
                .putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON);
    }

    /**
     * Función para obtener propiedades para el ambiente local
     *
     * @param nombre de la propiedad a obtener
     * @return String: valor de la propiedad
     */
    public static String obtenerPropiedad(String nombre) {
        InputStream input = null;
        Properties prop = new Properties();
        try {
            input = Server.class.getClassLoader().getResourceAsStream(PROPERTIES);
            prop.load(input);
        } catch (IOException e) {
            LOGGER.error(e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.error(e);
                }
            }
        }
        return prop.getProperty(nombre);
    }

    /**
     * Obtener el digest para el acuse
     *
     * @param digestionDcn
     * @param rfcOCurp
     * @param numeroCertificado
     * @param nombreFirm
     * @param fechaRecepcion
     * @param noComprobante
     * @return
     */
    public String obtenerDigestionAcuse(String digestionDcn, String rfcOCurp, String numeroCertificado,
            String nombreFirm, String fechaRecepcion, String noComprobante) {

        StringBuilder digestAcuse = new StringBuilder();
        digestAcuse.append(digestionDcn.trim());
        digestAcuse.append(rfcOCurp.trim());
        digestAcuse.append(numeroCertificado.trim());
        digestAcuse.append(nombreFirm.trim());
        digestAcuse.append(fechaRecepcion.trim());
        digestAcuse.append(noComprobante.trim());
        return EncripDecla.conEspaciosImPares(EncripDecla.digestCadena(digestAcuse.toString()), ESPACIO);
    }
}
