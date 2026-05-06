package mx.gob.sfp.dgti.service;

import mx.gob.sfp.dgti.appi.ip.AppiExtensionSFP;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.WebClient;
import io.vertx.reactivex.core.Vertx;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import mx.gob.sfp.dgti.appi.ip.enu.EnumPathsData;
import mx.gob.sfp.dgti.appi.ip.enu.EnumPathsEnables;
import mx.gob.sfp.dgti.daopxy.DAODeclaracionAPresentarInterface;
import mx.gob.sfp.dgti.decla.presentar.dto.CatTiposDeclaConAniosDTO;
import mx.gob.sfp.dgti.declaracion.dto.base.CabeceraDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.DatosGeneralesDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.DatosPersonalesDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.InstitucionReceptoraDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CorreoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.TelefonoDTO;
import mx.gob.sfp.dgti.declaracion.encrypt.ValidaMensajeFrontInterface;
import mx.gob.sfp.dgti.declaracion.encrypt.im.GeneraMensajeFront;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoFormatoDeclaracion;
import static mx.gob.sfp.dgti.util.Constantes.ACCES_TOKEN;
import static mx.gob.sfp.dgti.util.Constantes.AMBIENTE_AMBIENTE;
import static mx.gob.sfp.dgti.util.Constantes.ANIO_MIN_CAMBIO_DEP;
import static mx.gob.sfp.dgti.util.Constantes.ANIO_MIN_MODIFICACION_INICO_CONCLUSION;
import static mx.gob.sfp.dgti.util.Constantes.CURP;
import static mx.gob.sfp.dgti.util.Constantes.DECLARACION_FIRMADA;
import static mx.gob.sfp.dgti.util.Constantes.DECLARACION_PRECARGA_VACIA;
import static mx.gob.sfp.dgti.util.Constantes.EMPTY;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_CREAR_DECLARACION_TEMP;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_CREAR_NOTA;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_ELIMINAR_DECLARACION_TEMPORAL;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_FIDELIDAD;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_OBTENER_HISTORIAL;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_OBTENER_HISTORIAL_NOTA;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_OBTENER_LISTA_DECLARACIONES_FIRMADAS;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_SOLICITUD_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_VALIDAR_PROPIEDADES;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_VERIFICAR_DECLARACION_A_PRESENTAR;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_VERIFICA_DECLA_EN_PROCESO;
import static mx.gob.sfp.dgti.util.Constantes.ID_CLIENTE;
import static mx.gob.sfp.dgti.util.Constantes.ID_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.INTITUCION_RECEPTORA;
import static mx.gob.sfp.dgti.util.Constantes.NOTA_CREADA_CORRECTAMENTE;
import static mx.gob.sfp.dgti.util.Constantes.NOTA_NO_CREADA_DECLA_PEMDIENTE;
import static mx.gob.sfp.dgti.util.Constantes.PETICION_DATOS_INCOMPLETOS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ACUSE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ANIO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ANIOS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_AUTHORIZATION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_COLL_NAME;
import static mx.gob.sfp.dgti.util.Constantes.PROP_CURP;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DATOS_GENERALES;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DATOS_PERSONALES;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DECLARACION_A_PRESENTAR;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DECLARACION_ORIGEN;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DECLARACION_PENDIENTE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_EMAIL;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ENCABEZADO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ERROR;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ESTATUS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_FECHA_ACTUALIZACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_FECHA_ENCARGO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_FECHA_RECEPCION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_FECHA_REGISTRO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_FIRMADA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_FORMATO_FECHA_MONGO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_HISTORIAL;
import static mx.gob.sfp.dgti.util.Constantes.PROP_HOMOCLAVE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ID;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ID_MOVIMIENTO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ID_NIVEL_JERARQUICO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ID_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_INSTITUCION_RECEPTORA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MENSAJE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MENSAJE_DECLARACION_NO_PENDIENTE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MENSAJE_VERIFICAR;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MENSJAE_CONTINUAR_DECLA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_NIVEL_JERARQUICO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_NOMBRE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_NUM_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_PAIS_CELULAR_PERSONAL;
import static mx.gob.sfp.dgti.util.Constantes.PROP_PERSONAL_ALTERNO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_PRIMER_APELLIDO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_REGISTRO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_RESPUESTA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_RFC;
import static mx.gob.sfp.dgti.util.Constantes.PROP_SEGUNDO_APELLIDO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_SELLECIONAR_ANIO_EXTEMPORANEA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_TELEFONO_CASA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_TIPO_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_TIPO_DECLARACION_NOTA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_TIPO_FORMATO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_VALOR_NIVEL_JERARQUICO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_VALOR_UNO_NIVEL_JERARQUICO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_VERSION_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_VERSION_NOTA;
import static mx.gob.sfp.dgti.util.Constantes.SECRET_KEY;
import static mx.gob.sfp.dgti.util.Constantes.URL_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.VALIDACION_CON_ERRORES;
import static mx.gob.sfp.dgti.util.Constantes.VERSION_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.VERSION_DECLARACION_ORACLE;
import static mx.gob.sfp.dgti.util.Constantes.VERSION_NOTA;
import static mx.gob.sfp.dgti.util.Constantes.VERSION_NOTA_STRING;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_CONSULTAR_HISTORIAL;
import static mx.gob.sfp.dgti.util.Constantes.FORMATO_SIMPLIFICADO_INICIAL;
import static mx.gob.sfp.dgti.util.Constantes.ID_SP;
import static mx.gob.sfp.dgti.util.Constantes.PATHERN_DIGITOS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_NUM_DECLA_PRECARGA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ID_CATALOGO;
import static mx.gob.sfp.dgti.util.Constantes.URL_DECLARACION_HISTORIAL;
import static mx.gob.sfp.dgti.util.Constantes.URL_DECLARACION_PRECARGA;
import static mx.gob.sfp.dgti.util.Constantes.DECLARACION_CEROS;
import mx.gob.sfp.dgti.validacion.ValidacionDatosInicio;

public class ServiceInicioDeclaracionImpl implements ServiceInicioDeclaracionInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInicioDeclaracionImpl.class);
    private static DAODeclaracionAPresentarInterface daoDecla;
    private static ValidacionDatosInicio validacion;

    private static final String ID_CLIENTE_DEFAULT = "SIN_VALOR";
    private static final String SECRET_KEY_DEFAULT = "SIN_VALOR";
    private static final String VERSION_DECLARACION_DEFAULT = "20200414";
    private static final Integer VERSION_DECLARACION_VALUE = Integer.parseInt(System.getenv(VERSION_DECLARACION) != null ? System.getenv(VERSION_DECLARACION) : VERSION_DECLARACION_DEFAULT);
    private static final String ID_CLIENTE_VALUE = System.getenv(ID_CLIENTE) != null ? System.getenv(ID_CLIENTE) : ID_CLIENTE_DEFAULT;
    private static final String SECRET_KEY_VALUE = System.getenv(SECRET_KEY) != null ? System.getenv(SECRET_KEY) : SECRET_KEY_DEFAULT;
    private static final String URL_CONSULTA_DECLARACION = System.getenv(URL_DECLARACION) != null ? System.getenv(URL_DECLARACION) : "";
    private static final String THREE_MESSAGE = "%s %s %s";
    private static final String URL_DECLARACION_HISTORIAL_VALUE = System.getenv(URL_DECLARACION_HISTORIAL) != null ? System.getenv(URL_DECLARACION_HISTORIAL) : "";
    private static final String VERSION_NOTA_STRING_VALUE = System.getenv(VERSION_NOTA) != null ? System.getenv(VERSION_NOTA) : VERSION_NOTA_STRING;
    private static final String AMBIENTE_AMBIENTE_VALUE = System.getenv(AMBIENTE_AMBIENTE);
    private static final String URL_DECLARACION_PRECARGA_VALUE = System.getenv(URL_DECLARACION_PRECARGA);
    private static final String TRANSACTION_MIN = "transaction";

    private final WebClient client;
    private final AppiExtensionSFP appi = new AppiExtensionSFP();

    /**
     * Constructor de la clase, recibe ub objecto Vertx
     *
     * @param vertx
     * @param client
     */
    public ServiceInicioDeclaracionImpl(Vertx vertx, WebClient client) {
        daoDecla = DAODeclaracionAPresentarInterface.create(vertx.getDelegate());
        validacion = new ValidacionDatosInicio(vertx);
        this.client = client;
    }

    /**
     * {@inheritDoc}
     *
     * @param dataComplete
     */
    @SuppressWarnings("deprecation")
    @Override
    public Future<JsonObject> inicioDeclaracion(JsonObject dataComplete) {
        Future<JsonObject> jsonResul = Future.future();
        try {
            JsonObject data = ValidaMensajeFrontInterface.fidelidadMensaje(dataComplete);
            if (data != null) {
                if (data.getString(ACCES_TOKEN) != null && data.getString(TRANSACTION_MIN) != null
                        && data.getJsonObject(INTITUCION_RECEPTORA) != null) {
                    InstitucionReceptoraDTO institucion = Json.decodeValue(
                            data.getJsonObject(INTITUCION_RECEPTORA).toString(), InstitucionReceptoraDTO.class);
                    validacion.obtenerValidacionesInstitucion(institucion).setHandler(valIns -> {
                        if (valIns.succeeded()) {
                            if (valIns.result().getErrores().isEmpty()) {
                                // validar datos token y transaccion
                                try {
                                    appi.sfpPetitionToIpToken(client, EnumPathsEnables.valueOf(AMBIENTE_AMBIENTE_VALUE), data.getString(ACCES_TOKEN), data.getString(TRANSACTION_MIN), response -> {
                                        if (response.succeeded()) {
                                            JsonObject dataUser = response.result();
                                            
                                            appi.getDataUser(client, EnumPathsData.valueOf(AMBIENTE_AMBIENTE_VALUE), ID_CLIENTE_VALUE, SECRET_KEY_VALUE, dataUser.getString(CURP), datos -> {
                                                if (datos.succeeded()) {
                                                    JsonObject user = datos.result();
                                                    daoDecla.verificarDeclaracionEnProceso(data.getJsonObject(INTITUCION_RECEPTORA),
                                                            user.getInteger(ID_USUARIO)).setHandler(verificar -> {
                                                                if (verificar.succeeded()) {
                                                                    if (verificar.result().isEmpty()) {
                                                                        jsonResul.handle(Future.succeededFuture(
                                                                                        crearMensajeConFidelidad(generarJsonNoDeclaPendientes(
                                                                                                        data.getJsonObject(INTITUCION_RECEPTORA), user,
                                                                                                        dataUser.getString(PROP_AUTHORIZATION),
                                                                                                        this.crearJsonDatosPersonales(user)))));
                                                                    } else {
                                                                        if (PROP_TIPO_DECLARACION_NOTA.equals(verificar.result().get(0).getJsonObject(PROP_ENCABEZADO).getString(PROP_TIPO_DECLARACION))) {
                                                                            daoDecla.eliminarDeclaracionTemp(verificar.result().get(0).getJsonObject(PROP_ENCABEZADO).put(PROP_NUM_DECLARACION, verificar.result().get(0).getString(PROP_ID)), verificar.result().get(0).getJsonObject(PROP_INSTITUCION_RECEPTORA)).setHandler(eliminar -> {
                                                                                if (eliminar.succeeded() && eliminar.result()) {
                                                                                    jsonResul.handle(Future.succeededFuture(
                                                                                                    crearMensajeConFidelidad(generarJsonNoDeclaPendientes(
                                                                                                                    data.getJsonObject(INTITUCION_RECEPTORA), user,
                                                                                                                    dataUser.getString(PROP_AUTHORIZATION),
                                                                                                                    this.crearJsonDatosPersonales(user)))));
                                                                                } else {
                                                                                    LOGGER.info(eliminar.cause());
                                                                                    jsonResul.fail(generarVerificarDatos(ERROR_VERIFICA_DECLA_EN_PROCESO).toString());
                                                                                }
                                                                            });
                                                                        } else {
                                                                            jsonResul.handle(Future.succeededFuture(
                                                                                            crearMensajeConFidelidad(generarJsonDeclaPendientes(
                                                                                                            verificar.result().get(0),
                                                                                                            dataUser.getString(PROP_AUTHORIZATION),
                                                                                                            this.crearJsonDatosPersonales(user)))));
                                                                        }
                                                                    }
                                                                } else {
                                                                    LOGGER.fatal(verificar.cause());
                                                                    jsonResul
                                                                    .fail(generarVerificarDatos(ERROR_VERIFICA_DECLA_EN_PROCESO)
                                                                            .toString());
                                                                }
                                                            });
                                                } else {
                                                    LOGGER.error(response.cause());
                                                    jsonResul.fail(generarVerificarDatos("Error al consultar datos").toString());
                                                }
                                            });

                                        } else {
                                            LOGGER.error(response.cause());
                                            jsonResul.fail(generarVerificarDatos(response.cause().toString()).toString());
                                        }
                                    });

                                } catch (Exception e) {
                                    LOGGER.fatal(e);
                                    LOGGER.fatal(String.format("Error al consumir métodos de Utils %s", e));
                                    jsonResul.fail(generarVerificarDatos(e.getMessage()).toString());
                                }
                            } else {
                                LOGGER.fatal(VALIDACION_CON_ERRORES);
                                LOGGER.error(String.format("Recibido con error institucion01 : %s", Json.encode(institucion)));
                                jsonResul.fail(
                                        generarVerificarDatos(Json.encode(valIns.result().getErrores())).toString());
                            }
                        } else {
                            LOGGER.fatal(ERROR_VALIDAR_PROPIEDADES);
                            jsonResul.fail(generarVerificarDatos(ERROR_VALIDAR_PROPIEDADES).toString());
                        }
                    });
                } else {
                    LOGGER.info("no contiene acces tocke y transaction");
                    LOGGER.fatal(String.format(THREE_MESSAGE, PETICION_DATOS_INCOMPLETOS, ": ", dataComplete));
                    jsonResul.fail(generarVerificarDatos(PETICION_DATOS_INCOMPLETOS).toString());
                }
            } else {
                LOGGER.fatal(String.format(THREE_MESSAGE, ERROR_FIDELIDAD, ":", dataComplete));
                jsonResul.fail(generarVerificarDatos(ERROR_FIDELIDAD).toString());
            }
        } catch (Exception e) {
            LOGGER.fatal(String.format("Error inicioDeclaracion %s", e));
            jsonResul.fail(generarVerificarDatos(e.getMessage()).toString());
        }
        return jsonResul;

    }

    /**
     * {@inheritDoc}
     *
     * @param dataComplete
     */
    @SuppressWarnings("deprecation")
    @Override
    public Future<JsonObject> verificarDeclaracion(JsonObject dataComplete) {
        Future<JsonObject> jsonResul = Future.future();
        try {
            JsonObject result = ValidaMensajeFrontInterface.fidelidadMensaje(dataComplete);
            if (result != null) {
                if (result.getJsonObject(PROP_ENCABEZADO) != null
                        && result.getJsonObject(PROP_INSTITUCION_RECEPTORA) != null
                        && result.getJsonObject(PROP_DATOS_PERSONALES) != null) {
                    daoDecla.verificarDeclaracionEnProceso(result.getJsonObject(PROP_INSTITUCION_RECEPTORA), result
                            .getJsonObject(PROP_ENCABEZADO).getJsonObject(PROP_USUARIO).getInteger(PROP_ID_USUARIO))
                            .setHandler(verificar -> {
                                if (verificar.succeeded()) {
                                    if (verificar.result().isEmpty()) {
                                        jsonResul.handle(Future
                                                .succeededFuture(crearMensajeConFidelidad(generarJsonNoDeclaPendientes(
                                                                        result.getJsonObject(PROP_INSTITUCION_RECEPTORA),
                                                                        new JsonObject()
                                                                        .put(PROP_CURP,
                                                                                result.getJsonObject(PROP_ENCABEZADO)
                                                                                .getJsonObject(PROP_USUARIO)
                                                                                .getString(PROP_CURP))
                                                                        .put(PROP_ID_USUARIO,
                                                                                result.getJsonObject(PROP_ENCABEZADO)
                                                                                .getJsonObject(PROP_USUARIO)
                                                                                .getInteger(PROP_ID_USUARIO)),
                                                                        EMPTY, result.getJsonObject(PROP_DATOS_PERSONALES)))));
                                    } else {
                                        if (PROP_TIPO_DECLARACION_NOTA.equals(verificar.result().get(0).getJsonObject(PROP_ENCABEZADO).getString(PROP_TIPO_DECLARACION))) {
                                            daoDecla.eliminarDeclaracionTemp(verificar.result().get(0).getJsonObject(PROP_ENCABEZADO).put(PROP_NUM_DECLARACION, verificar.result().get(0).getString(PROP_ID)), verificar.result().get(0).getJsonObject(PROP_INSTITUCION_RECEPTORA)).setHandler(eliminar -> {
                                                if (eliminar.succeeded() && eliminar.result()) {
                                                    jsonResul.handle(Future
                                                            .succeededFuture(crearMensajeConFidelidad(generarJsonNoDeclaPendientes(
                                                                                    result.getJsonObject(PROP_INSTITUCION_RECEPTORA),
                                                                                    new JsonObject()
                                                                                    .put(PROP_CURP,
                                                                                            result.getJsonObject(PROP_ENCABEZADO)
                                                                                            .getJsonObject(PROP_USUARIO)
                                                                                            .getString(PROP_CURP))
                                                                                    .put(PROP_ID_USUARIO,
                                                                                            result.getJsonObject(PROP_ENCABEZADO)
                                                                                            .getJsonObject(PROP_USUARIO)
                                                                                            .getInteger(PROP_ID_USUARIO)),
                                                                                    EMPTY, result.getJsonObject(PROP_DATOS_PERSONALES)))));
                                                } else {
                                                    LOGGER.fatal(eliminar.cause());
                                                    jsonResul.fail(generarVerificarDatos(ERROR_VERIFICA_DECLA_EN_PROCESO).toString());
                                                }
                                            });
                                        } else {
                                            jsonResul.handle(Future.succeededFuture(crearMensajeConFidelidad(
                                                                    generarJsonDeclaPendientes(verificar.result().get(0), EMPTY,
                                                                            result.getJsonObject(PROP_DATOS_PERSONALES)))));
                                        }
                                    }
                                } else {
                                    LOGGER.fatal(verificar.cause());
                                    jsonResul.fail(generarVerificarDatos(ERROR_VERIFICA_DECLA_EN_PROCESO).toString());
                                }

                            });
                } else {
                    LOGGER.fatal(String.format(THREE_MESSAGE, PETICION_DATOS_INCOMPLETOS, ": ", dataComplete));
                    jsonResul.fail(generarVerificarDatos(PETICION_DATOS_INCOMPLETOS).toString());
                }

            } else {
                LOGGER.fatal(String.format(THREE_MESSAGE, ERROR_FIDELIDAD, ":", dataComplete));
                jsonResul.fail(generarVerificarDatos(ERROR_FIDELIDAD).toString());
            }
        } catch (Exception e) {
            LOGGER.fatal(e);
            jsonResul.fail(generarVerificarDatos(e.getMessage()).toString());
        }
        return jsonResul;
    }

    /**
     * {@inheritDoc}
     *
     * @param dataComplete
     */
    @SuppressWarnings("deprecation")
    @Override
    public Future<JsonObject> crearDeclaracionTemporal(JsonObject dataComplete) {
        Future<JsonObject> jsonResul = Future.future();
        try {
            JsonObject result = ValidaMensajeFrontInterface.fidelidadMensaje(dataComplete);
            if (result != null) {
                CabeceraDTO cabeceraDTO = Json.decodeValue(result.toString(), CabeceraDTO.class);
                validacion.obtenerValidaciones(cabeceraDTO).setHandler(val -> {
                    if (val.succeeded()) {
                        if (val.result().getErrores().isEmpty()) {
                            // flujo inicio
                            daoDecla.verificarDeclaracionFirmada(result.getJsonObject(PROP_ENCABEZADO), result.getJsonObject(PROP_INSTITUCION_RECEPTORA))
                                    .setHandler(firma -> {
                                        if (firma.succeeded()) {
                                            if (firma.result()) {
                                                jsonResul.handle(Future.succeededFuture(
                                                                crearMensajeConFidelidad(new JsonObject().put(PROP_RESPUESTA,
                                                                                new JsonObject().put(PROP_ESTATUS, false)
                                                                                .put(PROP_MENSAJE, DECLARACION_FIRMADA)))));
                                            } else if (EnumTipoDeclaracion.AVISO.name().equals(result.getJsonObject(PROP_ENCABEZADO).getString(PROP_TIPO_DECLARACION))) {
                                                daoDecla.crearNuevaDeclaracion(
                                                        result.getJsonObject(PROP_INSTITUCION_RECEPTORA),
                                                        result.getJsonObject(PROP_ENCABEZADO),
                                                        result.getJsonObject(PROP_DATOS_PERSONALES))
                                                .setHandler(insertar -> {
                                                    if (insertar.succeeded()) {
                                                        jsonResul.handle(Future.succeededFuture(crearMensajeConFidelidad(new JsonObject().put(
                                                                                        PROP_RESPUESTA, new JsonObject().put(PROP_ESTATUS, true)
                                                                                        .put(PROP_DECLARACION_PENDIENTE, true)
                                                                                        .put(PROP_MENSAJE, PROP_MENSJAE_CONTINUAR_DECLA)
                                                                                        .put(PROP_DECLARACION, insertar.result())))));
                                                    } else {
                                                        LOGGER.fatal(ERROR_CREAR_DECLARACION_TEMP);
                                                        jsonResul.fail(generarVerificarDatos(
                                                                        ERROR_CREAR_DECLARACION_TEMP).toString());
                                                    }
                                                });
                                            } else {
                                                JsonObject pet = new JsonObject().put(PROP_COLL_NAME, result.getJsonObject(PROP_INSTITUCION_RECEPTORA).getInteger(PROP_COLL_NAME)).put(PROP_ID_USUARIO, result.getJsonObject(PROP_ENCABEZADO).getJsonObject(PROP_USUARIO).getInteger(PROP_ID_USUARIO));

                                                client.postAbs(URL_DECLARACION_HISTORIAL_VALUE).timeout(14000).sendJsonObject(pet, ar -> {
                                                    if (ar.succeeded() && ar.result().statusCode() == HttpResponseStatus.OK.code()) {
                                                        JsonArray his = ar.result().bodyAsJsonArray();
                                                        if (his.isEmpty()) {
                                                            solicitarDeclaracionPrecarga(result, null, true).setHandler(solicitud -> {
                                                                if (solicitud.succeeded()) {
                                                                    jsonResul.handle(Future.succeededFuture(solicitud.result()));
                                                                } else {
                                                                    jsonResul.fail(generarVerificarDatos(
                                                                                    ERROR_CREAR_DECLARACION_TEMP).toString());
                                                                }
                                                            });
                                                        } else {
                                                            JsonObject declaAPrecargar = obtenerDeclaracionAPrecargar(his, result.getJsonObject(PROP_ENCABEZADO).getString(PROP_TIPO_DECLARACION), result.getJsonObject(PROP_ENCABEZADO).getInteger(PROP_ANIO));
                                                            if (declaAPrecargar == null) {
                                                                daoDecla.crearNuevaDeclaracion(
                                                                        result.getJsonObject(PROP_INSTITUCION_RECEPTORA),
                                                                        result.getJsonObject(PROP_ENCABEZADO),
                                                                        result.getJsonObject(PROP_DATOS_PERSONALES))
                                                                .setHandler(insertar -> {
                                                                    if (insertar.succeeded()) {
                                                                        jsonResul.handle(Future.succeededFuture(
                                                                                        crearMensajeConFidelidad(new JsonObject().put(
                                                                                                        PROP_RESPUESTA,
                                                                                                        new JsonObject().put(PROP_ESTATUS, true)
                                                                                                        .put(PROP_DECLARACION_PENDIENTE,
                                                                                                                true)
                                                                                                        .put(PROP_MENSAJE,
                                                                                                                PROP_MENSJAE_CONTINUAR_DECLA)
                                                                                                        .put(PROP_DECLARACION,
                                                                                                                insertar.result())))));
                                                                    } else {
                                                                        LOGGER.fatal(ERROR_CREAR_DECLARACION_TEMP);
                                                                        jsonResul.fail(generarVerificarDatos(
                                                                                        ERROR_CREAR_DECLARACION_TEMP).toString());
                                                                    }
                                                                });
                                                            } else {
                                                                solicitarDeclaracionPrecarga(result, declaAPrecargar, false).setHandler(solicitud -> {
                                                                    if (solicitud.succeeded()) {
                                                                        jsonResul.handle(Future.succeededFuture(solicitud.result()));
                                                                    } else {
                                                                        jsonResul.fail(generarVerificarDatos(
                                                                                        ERROR_CREAR_DECLARACION_TEMP).toString());
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    } else {
                                                        LOGGER.fatal(ar.cause().getMessage());
                                                        jsonResul.fail(generarVerificarDatos(ERROR_CONSULTAR_HISTORIAL)
                                                                .toString());
                                                    }
                                                });

                                            }
                                        } else {
                                            LOGGER.fatal(ERROR_VERIFICAR_DECLARACION_A_PRESENTAR);
                                            jsonResul
                                            .fail(generarVerificarDatos(ERROR_VERIFICAR_DECLARACION_A_PRESENTAR)
                                                    .toString());
                                        }
                                    });
                            // flujo fin
                        } else {
                            LOGGER.fatal(VALIDACION_CON_ERRORES);
                            LOGGER.error(String.format("Recibido con error cabeceraDTO : %s", Json.encode(cabeceraDTO)));
                            jsonResul.fail(generarVerificarDatos(Json.encode(val.result().getErrores())).toString());
                        }
                    } else {
                        LOGGER.fatal(ERROR_VALIDAR_PROPIEDADES, val.cause());
                        jsonResul.fail(generarVerificarDatos(ERROR_VALIDAR_PROPIEDADES).toString());
                    }
                });
            } else {
                LOGGER.fatal(String.format(THREE_MESSAGE, ERROR_FIDELIDAD, ":", dataComplete));
                jsonResul.fail(generarVerificarDatos(ERROR_FIDELIDAD).toString());
            }
        } catch (Exception e) {
            LOGGER.fatal(e);
            jsonResul.fail(generarVerificarDatos(e.getMessage()).toString());
        }
        return jsonResul;
    }

    /**
     * {@inheritDoc}
     *
     * @param dataComplete
     */
    @SuppressWarnings("deprecation")
    @Override
    public Future<JsonObject> consultaAniosDeclaExtemporanea(JsonObject dataComplete) {
        Future<JsonObject> jsonResul = Future.future();
        try {
            JsonObject result = ValidaMensajeFrontInterface.fidelidadMensaje(dataComplete);
            if (result != null) {
                if (result.getJsonObject(PROP_ENCABEZADO) != null
                        && result.getJsonObject(PROP_ENCABEZADO).getJsonObject(PROP_USUARIO) != null
                        && result.getJsonObject(PROP_ENCABEZADO).getJsonObject(PROP_USUARIO)
                        .getInteger(PROP_ID_USUARIO) != null
                        && result.getJsonObject(PROP_INSTITUCION_RECEPTORA) != null) {
                    InstitucionReceptoraDTO institucion = Json.decodeValue(
                            result.getJsonObject(PROP_INSTITUCION_RECEPTORA).toString(),
                            InstitucionReceptoraDTO.class);
                    validacion.obtenerValidacionesInstitucion(institucion).setHandler(valIns -> {
                        if (valIns.succeeded()) {
                            if (valIns.result().getErrores().isEmpty()) {
                                daoDecla.obtenerListaDeclaModificacionFirmadas(result.getJsonObject(PROP_ENCABEZADO),
                                        result.getJsonObject(PROP_INSTITUCION_RECEPTORA)).setHandler(firmadas -> {
                                            if (firmadas.succeeded()) {
                                                jsonResul.handle(Future.succeededFuture(crearMensajeConFidelidad(
                                                                        generarTodosAniosDeclaracionExtem(firmadas.result()))));
                                            } else {
                                                LOGGER.fatal(
                                                        ERROR_OBTENER_LISTA_DECLARACIONES_FIRMADAS, firmadas.cause());
                                                jsonResul.fail(generarVerificarDatos(
                                                                ERROR_OBTENER_LISTA_DECLARACIONES_FIRMADAS).toString());
                                            }
                                        });
                            } else {
                                LOGGER.fatal(VALIDACION_CON_ERRORES);
                                LOGGER.error(String.format("Recibido con error institucion : %s", Json.encode(institucion)));
                                jsonResul.fail(
                                        generarVerificarDatos(Json.encode(valIns.result().getErrores())).toString());
                            }
                        } else {
                            LOGGER.fatal(ERROR_VALIDAR_PROPIEDADES, valIns.cause());
                            jsonResul.fail(generarVerificarDatos(ERROR_VALIDAR_PROPIEDADES).toString());
                        }
                    });
                } else {
                    LOGGER.fatal(String.format(THREE_MESSAGE, PETICION_DATOS_INCOMPLETOS, ":", dataComplete));
                    jsonResul.fail(generarVerificarDatos(PETICION_DATOS_INCOMPLETOS).toString());
                }
            } else {
                LOGGER.fatal(String.format(THREE_MESSAGE, ERROR_FIDELIDAD, ":", dataComplete));
                jsonResul.fail(generarVerificarDatos(ERROR_FIDELIDAD).toString());
            }
        } catch (Exception e) {
            LOGGER.fatal(e);
            jsonResul.fail(generarVerificarDatos(e.getMessage()).toString());
        }
        return jsonResul;
    }

    /**
     * {@inheritDoc}
     *
     * @param dataComplete
     */
    @SuppressWarnings("deprecation")
    @Override
    public Future<JsonObject> eliminarDeclaracionTemporal(JsonObject dataComplete) {
        Future<JsonObject> jsonResul = Future.future();
        try {
            JsonObject result = ValidaMensajeFrontInterface.fidelidadMensaje(dataComplete);
            if (result != null) {
                daoDecla.eliminarDeclaracionTemp(result.getJsonObject(PROP_ENCABEZADO),
                        result.getJsonObject(PROP_INSTITUCION_RECEPTORA)).setHandler(elimina -> {
                            if (elimina.succeeded()) {
                                jsonResul.handle(Future.succeededFuture(crearMensajeConFidelidad(
                                                        new JsonObject().put(PROP_RESPUESTA, new JsonObject()
                                                                .put(PROP_ESTATUS, true)
                                                                .put(PROP_DECLARACION_PENDIENTE, false)
                                                                .put(PROP_MENSAJE, PROP_MENSAJE_DECLARACION_NO_PENDIENTE)
                                                                .put(PROP_DECLARACION_A_PRESENTAR, new JsonArray(Json.encode(crearListaCatalogoDeclaraciones())))
                                                                .put(PROP_INSTITUCION_RECEPTORA, result.getJsonObject(PROP_INSTITUCION_RECEPTORA))
                                                                .put(PROP_DATOS_PERSONALES, result.getJsonObject(PROP_DATOS_PERSONALES))
                                                                .put(PROP_ENCABEZADO, result.getJsonObject(PROP_ENCABEZADO))))));

                            } else {
                                LOGGER.fatal(ERROR_ELIMINAR_DECLARACION_TEMPORAL, elimina.cause());
                                jsonResul.fail(generarVerificarDatos(ERROR_ELIMINAR_DECLARACION_TEMPORAL).toString());
                            }
                        });
            } else {
                LOGGER.fatal(String.format(THREE_MESSAGE, ERROR_FIDELIDAD, ":", dataComplete));
                jsonResul.fail(generarVerificarDatos(ERROR_FIDELIDAD).toString());
            }
        } catch (Exception e) {
            LOGGER.fatal(e);
            jsonResul.fail(generarVerificarDatos(e.getMessage()).toString());
        }

        return jsonResul;
    }

    /**
     * {@inheritDoc}
     *
     * @param dataComplete
     */
    @SuppressWarnings("deprecation")
    @Override
    public Future<JsonObject> obtenerHistorialDeclaraciones(JsonObject dataComplete) {
        Future<JsonObject> jsonResul = Future.future();
        JsonObject data = ValidaMensajeFrontInterface.fidelidadMensaje(dataComplete);
        if (data != null) {
            try {
                daoDecla.obtenerHistorialDeclaracionesUsuario(data).setHandler(his -> {
                    if (his.succeeded()) {
                        jsonResul.handle(Future
                                .succeededFuture(crearMensajeConFidelidad(crearRespuestaHistorial(his.result()))));
                    } else {
                        LOGGER.fatal(String.format(THREE_MESSAGE, ERROR_OBTENER_HISTORIAL, ":", dataComplete));
                        jsonResul.fail(generarVerificarDatos(ERROR_OBTENER_HISTORIAL).toString());
                    }
                });
            } catch (Exception e) {
                LOGGER.fatal(e);
                jsonResul.fail(generarVerificarDatos(e.getMessage()).toString());
            }
        } else {
            LOGGER.fatal(String.format(THREE_MESSAGE, ERROR_FIDELIDAD, ":", dataComplete));
            jsonResul.fail(generarVerificarDatos(ERROR_FIDELIDAD).toString());
        }

        return jsonResul;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Future<JsonObject> crearNotaDeDeclaracion(JsonObject dataComplete) {
        Future<JsonObject> future = Future.future();
        JsonObject data = ValidaMensajeFrontInterface.fidelidadMensaje(dataComplete);
        if (data != null && data.getJsonObject(PROP_DATOS_PERSONALES) != null
                && data.getJsonObject(PROP_REGISTRO) != null) {
            if (isNumber(data.getJsonObject(PROP_REGISTRO).getString(PROP_NUM_DECLARACION))) {
                crearNota(false, null, data).setHandler(crearNota -> {
                    if (crearNota.succeeded()) {
                        future.handle(Future
                                .succeededFuture(crearNota.result()));
                    } else {
                        LOGGER.fatal(crearNota.cause().getMessage());
                        future.fail(generarVerificarDatos(crearNota.cause().getMessage()).toString());
                    }
                });
            } else {
                JsonObject res = new JsonObject();
                res.put(PROP_NUM_DECLARACION, data.getJsonObject(PROP_REGISTRO).getString(PROP_NUM_DECLARACION));
                res.put(PROP_ID_USUARIO, data.getJsonObject(PROP_REGISTRO).getInteger(PROP_ID_USUARIO));
                res.put(PROP_COLL_NAME, data.getJsonObject(PROP_REGISTRO).getInteger(PROP_COLL_NAME));
                client.postAbs(URL_CONSULTA_DECLARACION)
                        .sendJsonObject(res, ar -> {
                            if (ar.succeeded()) {
                                if (ar.result().bodyAsJsonObject().isEmpty()) {
                                    future.fail(generarVerificarDatos(DECLARACION_PRECARGA_VACIA).toString());
                                } else {
                                    crearNota(true, ar.result().bodyAsJsonObject(), data).setHandler(crearNota -> {
                                        if (crearNota.succeeded()) {
                                            future.handle(Future.succeededFuture(crearNota.result()));
                                        } else {
                                            LOGGER.fatal(ERROR_CREAR_NOTA + crearNota.cause());
                                            future.fail(generarVerificarDatos(crearNota.cause().getMessage()).toString());
                                        }
                                    });
                                }
                            } else {
                                LOGGER.fatal(ar.cause().getMessage());
                                future.fail(generarVerificarDatos(ERROR_SOLICITUD_DECLARACION).toString());
                            }
                        });
            }
        } else {
            LOGGER.fatal(ERROR_FIDELIDAD);
            future.fail(generarVerificarDatos(ERROR_FIDELIDAD).toString());
        }
        return future;
    }

    @SuppressWarnings({"deprecation"})
    @Override
    public Future<JsonObject> obtenerHistorialNotasDeclaracion(JsonObject dataComplete) {
        Future<JsonObject> jsonResul = Future.future();
        JsonObject data = ValidaMensajeFrontInterface.fidelidadMensaje(dataComplete);
        if (data != null) {
            try {
                daoDecla.obtenerHistorialNotasPorDeclaracion(data).setHandler(his -> {
                    if (his.succeeded()) {
                        jsonResul.handle(Future
                                .succeededFuture(crearMensajeConFidelidad(crearRespuestaHistorial(his.result()))));
                    } else {
                        jsonResul.fail(generarVerificarDatos(ERROR_OBTENER_HISTORIAL_NOTA).toString());
                    }
                });
            } catch (Exception e) {
                LOGGER.fatal(String.format("%s %s", ERROR_OBTENER_HISTORIAL_NOTA, e));
                jsonResul.fail(generarVerificarDatos(e.getMessage()).toString());
            }
        } else {
            LOGGER.fatal(ERROR_FIDELIDAD);
            jsonResul.fail(generarVerificarDatos(ERROR_FIDELIDAD).toString());
        }
        return jsonResul;
    }

    /**
     * Método para crear la nota
     *
     * @param datosUser
     * @return JsonObject
     */
    @SuppressWarnings("deprecation")
    private Future<JsonObject> crearNota(boolean isMongo, JsonObject declaracion, JsonObject data) {
        Future<JsonObject> future = Future.future();
        JsonObject nota = new JsonObject();
        daoDecla.verificarDeclaracionEnProceso(data.getJsonObject(PROP_INSTITUCION_RECEPTORA), data
                .getJsonObject(PROP_REGISTRO).getInteger(PROP_ID_USUARIO))
                .setHandler(verificar -> {
                    if (verificar.succeeded()) {
                        if (verificar.result().isEmpty()) {
                            nota.put(PROP_RESPUESTA, new JsonObject().put(PROP_ESTATUS, true).put(PROP_MENSAJE, NOTA_CREADA_CORRECTAMENTE).put(PROP_DECLARACION, new JsonObject()));
                            if (isMongo) {
                                nota.getJsonObject(PROP_RESPUESTA).getJsonObject(PROP_DECLARACION).put(PROP_ENCABEZADO, new JsonObject()
                                        .put(PROP_DATOS_PERSONALES, data.getJsonObject(PROP_DATOS_PERSONALES))
                                        .putNull(PROP_NUM_DECLARACION)
                                        .put(PROP_FECHA_ACTUALIZACION, new SimpleDateFormat(PROP_FORMATO_FECHA_MONGO).format(new Date()))
                                        .put(PROP_FECHA_REGISTRO, new SimpleDateFormat(PROP_FORMATO_FECHA_MONGO).format(new Date()))
                                        .put(PROP_TIPO_DECLARACION, PROP_TIPO_DECLARACION_NOTA)
                                        .put(PROP_TIPO_FORMATO, declaracion.getJsonObject(PROP_ENCABEZADO).getString(PROP_TIPO_FORMATO))
                                        .put(PROP_USUARIO, new JsonObject().put(PROP_ID_USUARIO, data.getJsonObject(PROP_REGISTRO).getInteger(PROP_ID_USUARIO))
                                                .put(PROP_CURP, data.getJsonObject(PROP_DATOS_PERSONALES).getString(PROP_CURP))
                                                .putNull(PROP_ID_MOVIMIENTO)
                                                .putNull(ID_SP))
                                        .put(PROP_VERSION_NOTA, VERSION_NOTA_STRING_VALUE)
                                        .put(PROP_DECLARACION_ORIGEN, new JsonObject().put(PROP_ENCABEZADO, declaracion.getJsonObject(PROP_ENCABEZADO))
                                                .put(PROP_ACUSE, new JsonObject().put(PROP_FECHA_RECEPCION, data.getJsonObject(PROP_REGISTRO).getString(PROP_FECHA_RECEPCION)))))
                                .put(PROP_FIRMADA, declaracion.getBoolean(PROP_FIRMADA))
                                .put(PROP_INSTITUCION_RECEPTORA, data.getJsonObject(PROP_INSTITUCION_RECEPTORA));
                                future.handle(Future.succeededFuture(crearMensajeConFidelidad(nota)));
                            } else {
                                nota.getJsonObject(PROP_RESPUESTA).getJsonObject(PROP_DECLARACION).put(PROP_ENCABEZADO, new JsonObject()
                                        .put(PROP_DATOS_PERSONALES, data.getJsonObject(PROP_DATOS_PERSONALES))
                                        .putNull(PROP_NUM_DECLARACION)
                                        .put(PROP_FECHA_ACTUALIZACION, new SimpleDateFormat(PROP_FORMATO_FECHA_MONGO).format(new Date()))
                                        .put(PROP_FECHA_REGISTRO, new SimpleDateFormat(PROP_FORMATO_FECHA_MONGO).format(new Date()))
                                        .put(PROP_TIPO_DECLARACION, PROP_TIPO_DECLARACION_NOTA)
                                        .put(PROP_TIPO_FORMATO, EnumTipoFormatoDeclaracion.COMPLETO.name())
                                        .put(PROP_USUARIO, new JsonObject().put(PROP_ID_USUARIO, data.getJsonObject(PROP_REGISTRO).getInteger(PROP_ID_USUARIO))
                                                .put(PROP_CURP, data.getJsonObject(PROP_DATOS_PERSONALES).getString(PROP_CURP))
                                                .putNull(PROP_ID_MOVIMIENTO)
                                                .putNull(ID_SP))
                                        .put(PROP_VERSION_NOTA, VERSION_NOTA_STRING_VALUE)
                                        .put(PROP_DECLARACION_ORIGEN, crearEncabezadoOracle(data)))
                                .put(PROP_FIRMADA, false)
                                .put(PROP_INSTITUCION_RECEPTORA, data.getJsonObject(PROP_INSTITUCION_RECEPTORA));
                                future.handle(Future.succeededFuture(crearMensajeConFidelidad(nota)));
                            }
                        } else {
                            nota.put(PROP_RESPUESTA, new JsonObject().put(PROP_ESTATUS, true).put(PROP_MENSAJE, NOTA_NO_CREADA_DECLA_PEMDIENTE).putNull(PROP_DECLARACION));
                            future.handle(Future.succeededFuture(crearMensajeConFidelidad(nota)));
                        }
                    } else {
                        future.fail(generarVerificarDatos(ERROR_VERIFICA_DECLA_EN_PROCESO).toString());
                    }
                });
        return future;
    }

    /**
     * Método para crear un json de datos generales
     *
     * @param datosUser
     * @return JsonObject
     */
    private JsonObject crearJsonDatosPersonales(JsonObject datosUser) {
        return new JsonObject().put(PROP_NOMBRE, datosUser.getString(PROP_NOMBRE))
                .put(PROP_PRIMER_APELLIDO, datosUser.getString(PROP_PRIMER_APELLIDO))
                .put(PROP_SEGUNDO_APELLIDO, datosUser.getString(PROP_SEGUNDO_APELLIDO))
                .put(PROP_RFC, datosUser.getString(PROP_RFC) + datosUser.getString(PROP_HOMOCLAVE))
                .put(PROP_CURP, datosUser.getString(PROP_CURP))
                .put(PROP_PERSONAL_ALTERNO, datosUser.getString(PROP_EMAIL));

    }

    /**
     * Método para generar el json de verificación de datos
     *
     * @return JsonObject
     */
    private JsonObject generarVerificarDatos(String mensaje) {
        return crearMensajeConFidelidad(new JsonObject().put(PROP_RESPUESTA, new JsonObject().put(PROP_ESTATUS, false)
                .put(PROP_MENSAJE, PROP_MENSAJE_VERIFICAR).put(PROP_ERROR, mensaje)));
    }

    /**
     * Método que genera el json cuando el usuario tiene una declaración sin
     * terminar
     *
     * @param data JsonObject que se recibe en la peetición
     * @param institucionReceptora propiedad JsonObject que se recibe en la
     * petición (dentro de data)
     * @param declaContinuar propiedad JsonObject que tiene la declaracion a
     * continuar
     * @return retrna un JsonObject que incluye la declaración a continuar junto
     * con la data de la petición
     *
     * @author programador04@sfp.gob.mx
     * @since 06/11/2019
     */
    private JsonObject generarJsonDeclaPendientes(JsonObject declaContinuar, String authorization,
            JsonObject datosPersonales) {
        declaContinuar.getJsonObject(PROP_ENCABEZADO).put(PROP_NUM_DECLARACION, declaContinuar.getString(PROP_ID));
        declaContinuar.getJsonObject(PROP_ENCABEZADO).getJsonObject(PROP_USUARIO).put(CURP, datosPersonales.getString(CURP));
        declaContinuar.remove(PROP_ID);
        return new JsonObject().put(PROP_RESPUESTA,
                new JsonObject().put(PROP_ESTATUS, true).put(PROP_AUTHORIZATION, authorization)
                .put(PROP_DECLARACION_PENDIENTE, true).put(PROP_MENSAJE, PROP_MENSJAE_CONTINUAR_DECLA)
                .put(PROP_DECLARACION, declaContinuar).put(PROP_DATOS_PERSONALES, datosPersonales));
    }

    /**
     *
     * Método para generar el json cuando no tiene declaración a continuar
     *
     * @param institucionReceptora propiedad JsonObject que se recibe en la
     * petición (dentro de data)
     * @param user JsonObject con los datos de usuario (id, curp)
     * @author programador04@sfp.gob.mx
     * @since 06/11/2019
     * @return JsonObject con la respuesta de la lista de años a seleccionar
     */
    private JsonObject generarJsonNoDeclaPendientes(JsonObject institucionReceptora, JsonObject user,
            String authorization, JsonObject modDatosPersonales) {

        return new JsonObject().put(PROP_RESPUESTA, new JsonObject()
                .put(PROP_ESTATUS, true).put(PROP_AUTHORIZATION, authorization).put(PROP_DECLARACION_PENDIENTE,
                        false)
                .put(PROP_MENSAJE,
                        PROP_MENSAJE_DECLARACION_NO_PENDIENTE)
                .put(PROP_DECLARACION_A_PRESENTAR, new JsonArray(Json.encode(crearListaCatalogoDeclaraciones())))
                .put(PROP_ENCABEZADO,
                        new JsonObject().putNull(PROP_NUM_DECLARACION).putNull(PROP_TIPO_DECLARACION).putNull(PROP_ANIO)
                        .putNull(PROP_FECHA_ENCARGO).putNull(PROP_FECHA_REGISTRO)
                        .putNull(PROP_FECHA_ACTUALIZACION)
                        .put(PROP_USUARIO,
                                new JsonObject().put(PROP_ID_USUARIO, user.getInteger(PROP_ID_USUARIO))
                                .put(PROP_CURP, user.getString(PROP_CURP)).putNull(PROP_ID_MOVIMIENTO)
                                .putNull(ID_SP))
                        .putNull(PROP_TIPO_FORMATO).put(PROP_VERSION_DECLARACION, VERSION_DECLARACION_VALUE)
                        .put(PROP_NIVEL_JERARQUICO, new JsonObject().putNull(PROP_ID_NIVEL_JERARQUICO).putNull(PROP_VALOR_NIVEL_JERARQUICO).putNull(PROP_VALOR_UNO_NIVEL_JERARQUICO)))
                .put(PROP_INSTITUCION_RECEPTORA, institucionReceptora).put(PROP_DATOS_PERSONALES, modDatosPersonales));
    }

    /**
     * Método para crear la lista de años a seleccionar en el frontend
     *
     * @return Lista de tipo CatTiposDeclaConAniosDTO
     * @author programador04@sfp.gob.mx
     * @since 06/11/2019
     */
    private List<CatTiposDeclaConAniosDTO> crearListaCatalogoDeclaraciones() {
        List<CatTiposDeclaConAniosDTO> catTiposDeclaConAnios = new ArrayList<>();

        for (EnumTipoDeclaracion tipoDecla : java.util.Arrays.asList(EnumTipoDeclaracion.values())) {
            CatTiposDeclaConAniosDTO catTD = new CatTiposDeclaConAniosDTO();
            switch (tipoDecla.getId()) {
                case 0:
                case 1:
                    catTD.setIdTipoDecla(tipoDecla.name());
                    catTD.setDescDecla(tipoDecla.getDescripcion());
                    catTiposDeclaConAnios.add(catTD);
                    break;
                case 2:
                    catTD.setIdTipoDecla(tipoDecla.name());
                    catTD.setDescDecla(tipoDecla.getDescripcion());
                    catTD.setAnios(new ArrayList<>());
                    catTD.getAnios().add(Calendar.getInstance().get(Calendar.YEAR));
                    catTiposDeclaConAnios.add(catTD);
                    break;
                case 6:
                    catTD.setIdTipoDecla(tipoDecla.name());
                    catTD.setDescDecla(tipoDecla.getDescripcion());
                    catTD.setAnios(new ArrayList<>());
                    for (int k = ANIO_MIN_CAMBIO_DEP; k <= Calendar.getInstance().get(Calendar.YEAR); k++) {
                        catTD.getAnios().add(k);
                    }
                    catTiposDeclaConAnios.add(catTD);
                    break;
                default:
                    LOGGER.info("No se encontró el tipo de declaración");
            }
        }
        return catTiposDeclaConAnios;
    }

//    /**
//     * Método para obtener el ambiente donde se encuentra el aplicativo
//     *
//     * @return EnumPathsData
//     * @author programador04@sfp.gob.mx
//     * @since 17/12/2019
//     */
//    private static EnumPathsData obtenerAmbienteUser() {
//        
//        if (EnumPathsEnables.PRODUCTION.name().equals(AMBIENTE_AMBIENTE_VALUE)) {
//            return EnumPathsData.PRODUCTION;
//        } else {
//            return EnumPathsData.STAGING;
//        }
//        
//    }
    /**
     * Método para obtener el ambiente donde se encuentra el aplicativo
     *
     * @return EnumPathsEnables
     * @author programador04@sfp.gob.mx
     * @since 17/12/2019
     */
    private static EnumPathsEnables obtenerAmbiente() {
        if (EnumPathsEnables.REVIEW.name().equals(AMBIENTE_AMBIENTE_VALUE)) {
            return EnumPathsEnables.REVIEW;
        } else if (EnumPathsEnables.PRODUCTION.name().equals(AMBIENTE_AMBIENTE_VALUE)) {
            return EnumPathsEnables.PRODUCTION;
        } else {
            return EnumPathsEnables.STAGING;
        }
    }

    private JsonObject crearMensajeConFidelidad(JsonObject respuesta) {
        return GeneraMensajeFront.fidelidadMensaje(respuesta);
    }

    /**
     * Método que agrega al JsonArray los años válidos para presentar una
     * declaración de modificación
     *
     * @param aniosDeclaMod propiedad para almacenar los años válidos
     * @param lista lista de JsonObject que el el resultado que contiene las
     * declaraciones tipo modificaación firmadas
     *
     * @author programador04@sfp.gob.mx
     * @since 22/11/2019
     */
    private JsonObject generarTodosAniosDeclaracionExtem(List<JsonObject> lista) {
        JsonArray aniosDeclaMod = new JsonArray();
        if (lista.isEmpty()) {
            for (int k = Calendar.getInstance().get(Calendar.YEAR); k >= ANIO_MIN_MODIFICACION_INICO_CONCLUSION; k--) {
                aniosDeclaMod.add(k);
            }
        } else {
            for (int k = Calendar.getInstance().get(Calendar.YEAR); k >= ANIO_MIN_MODIFICACION_INICO_CONCLUSION; k--) {
                boolean aux = false;
                for (JsonObject registro : lista) {
                    if (k == registro.getJsonObject(PROP_DECLARACION).getInteger(PROP_ANIO)) {
                        aux = true;
                        break;
                    }
                }
                if (!aux) {
                    aniosDeclaMod.add(k);
                }
            }
        }
        return new JsonObject().put(PROP_RESPUESTA, new JsonObject().put(PROP_ESTATUS, true)
                .put(PROP_MENSAJE, PROP_SELLECIONAR_ANIO_EXTEMPORANEA).put(PROP_ANIOS, aniosDeclaMod));
    }

    /**
     * Método para crear el jsoon de respues para el historial de declaraciones
     *
     * @param data
     * @return JsonObject
     */
    private JsonObject crearRespuestaHistorial(List<JsonObject> data) {
        JsonObject respuesta = new JsonObject().put(PROP_RESPUESTA, new JsonObject().put(PROP_ESTATUS, true));
        if (data.isEmpty()) {
            respuesta.getJsonObject(PROP_RESPUESTA).putNull(PROP_HISTORIAL);
        } else {
            respuesta.getJsonObject(PROP_RESPUESTA).put(PROP_HISTORIAL, data);
        }

        return respuesta;
    }

    /**
     * Método que crea el encabezado de la declaración origen para la nota
     * cuando la declaraciń origen es de oracle
     *
     * @param data
     * @return
     */
    public JsonObject crearEncabezadoOracle(JsonObject data) {
        return new JsonObject().put(PROP_ENCABEZADO, new JsonObject()
                .put(PROP_NUM_DECLARACION, data.getJsonObject(PROP_REGISTRO).getString(PROP_NUM_DECLARACION))
                .putNull(PROP_FECHA_ACTUALIZACION)
                .put(PROP_FECHA_REGISTRO, data.getJsonObject(PROP_REGISTRO).getString(PROP_FECHA_RECEPCION))
                .put(PROP_FECHA_ENCARGO, data.getJsonObject(PROP_REGISTRO).getString(PROP_FECHA_ENCARGO))
                .put(PROP_TIPO_DECLARACION, data.getJsonObject(PROP_REGISTRO).getString(PROP_TIPO_DECLARACION))
                .put(PROP_ANIO, data.getJsonObject(PROP_REGISTRO).getInteger(PROP_ANIO))
                .put(PROP_TIPO_FORMATO, EnumTipoFormatoDeclaracion.COMPLETO.name())
                .putNull(PROP_NIVEL_JERARQUICO)
                .put(PROP_USUARIO, new JsonObject().put(PROP_ID_USUARIO, data.getJsonObject(PROP_REGISTRO).getInteger(PROP_ID_USUARIO))
                        .put(PROP_CURP, data.getJsonObject(PROP_DATOS_PERSONALES).getString(PROP_CURP))
                        .putNull(PROP_ID_MOVIMIENTO))
                .put(PROP_VERSION_DECLARACION, VERSION_DECLARACION_ORACLE))
                .put(PROP_ACUSE, new JsonObject().put(PROP_FECHA_RECEPCION, data.getJsonObject(PROP_REGISTRO).getString(PROP_FECHA_RECEPCION)));
    }

    /**
     * metodo para saber si un string contiene solo numeros
     *
     * @param cadena
     * @return
     */
    public boolean isNumber(String cadena) {
        return PATHERN_DIGITOS.matcher(cadena).matches();
    }

    public JsonObject obtenerDeclaracionAPrecargar(JsonArray historial, String tipoDecla, int anio) {
        JsonObject declaRetornar = null;
        if (EnumTipoDeclaracion.INICIO.name().equals(tipoDecla) || EnumTipoDeclaracion.CONCLUSION.name().equals(tipoDecla)) {
            declaRetornar = historial.getJsonObject(0);
        } else if (EnumTipoDeclaracion.MODIFICACION.name().equals(tipoDecla)) {
            if (Calendar.getInstance().get(Calendar.YEAR) == anio) {
                declaRetornar = historial.getJsonObject(0);
            } else {
                for (Object registro : historial) {
                    JsonObject reg = (JsonObject) registro;
                    if (anio > reg.getInteger(PROP_ANIO)) {
                        declaRetornar = reg;
                        break;
                    }
                }
            }
        }
        return declaRetornar;
    }

    public JsonObject crearEncabezadoPrecarga(JsonObject encabezadoRequest) {
        encabezadoRequest.put(PROP_TIPO_FORMATO, FORMATO_SIMPLIFICADO_INICIAL.equals(encabezadoRequest.getJsonObject(PROP_NIVEL_JERARQUICO).getString(PROP_VALOR_UNO_NIVEL_JERARQUICO)) ? EnumTipoFormatoDeclaracion.SIMPLIFICADO.name() : EnumTipoFormatoDeclaracion.COMPLETO.name());
        encabezadoRequest.put(PROP_FECHA_REGISTRO, new SimpleDateFormat(PROP_FORMATO_FECHA_MONGO).format(new Date()));
        encabezadoRequest.put(PROP_FECHA_ACTUALIZACION, new SimpleDateFormat(PROP_FORMATO_FECHA_MONGO).format(new Date()));
        return encabezadoRequest;
    }

    public JsonObject crearModuloDatosGenerales(JsonObject datosUser) {
        DatosGeneralesDTO datosGeneralesDTO = new DatosGeneralesDTO();
        datosGeneralesDTO.setDatosPersonales(new DatosPersonalesDTO(datosUser));
        datosGeneralesDTO.setCorreoElectronico(new CorreoDTO());

        datosGeneralesDTO.setTelefonoCasa(new TelefonoDTO());
        datosGeneralesDTO.setTelefonoCelular(new TelefonoDTO());
        datosGeneralesDTO.getTelefonoCelular().setPaisCelularPersonal(new CatalogoDTO());
        datosGeneralesDTO.setRegimenMatrimonial(new CatalogoDTO());
//Sea ctualzia para que realice precarga de datos solicitud Javier y DG Rolando.        
//        datosGeneralesDTO.setSituacionPersonalEstadoCivil(new CatalogoDTO());
//        datosGeneralesDTO.setPaisNacimiento(new CatalogoDTO());
//        datosGeneralesDTO.setNacionalidad(new CatalogoDTO());

        JsonObject datosGrals = new JsonObject(Json.encode(datosGeneralesDTO));
        datosGrals.remove(PROP_ENCABEZADO);
        datosGrals.getJsonObject(PROP_TELEFONO_CASA).remove(PROP_PAIS_CELULAR_PERSONAL);
        return datosGrals;
    }

    public Future<JsonObject> solicitarDeclaracionPrecarga(JsonObject result, JsonObject declaAPrecargar, boolean isCeros) {
        Future<JsonObject> declaracion = Future.future();

        JsonObject peticionDeclaracion = result.getJsonObject(PROP_ENCABEZADO).copy();
        if (isCeros) {
            peticionDeclaracion.put(PROP_NUM_DECLA_PRECARGA, DECLARACION_CEROS);
            peticionDeclaracion.putNull(PROP_COLL_NAME);
        } else {
            peticionDeclaracion.put(PROP_NUM_DECLA_PRECARGA, declaAPrecargar.getString(PROP_NUM_DECLARACION));
            peticionDeclaracion.put(PROP_COLL_NAME, declaAPrecargar.getInteger(PROP_COLL_NAME));
        }

        LOGGER.info(String.format("URL_DECLARACION_PRECARGA: %s", URL_DECLARACION_PRECARGA_VALUE));
        client.postAbs(URL_DECLARACION_PRECARGA_VALUE != null ? URL_DECLARACION_PRECARGA_VALUE : "").timeout(14000).sendJsonObject(peticionDeclaracion, precarga -> {
            if (precarga.succeeded() && precarga.result().statusCode() == HttpResponseStatus.OK.code()) {
                JsonObject declaracionPrecargada = precarga.result().bodyAsJsonObject();
                if (declaracionPrecargada.isEmpty()) {
                    daoDecla.crearNuevaDeclaracion(
                            result.getJsonObject(PROP_INSTITUCION_RECEPTORA),
                            result.getJsonObject(PROP_ENCABEZADO),
                            result.getJsonObject(PROP_DATOS_PERSONALES))
                            .setHandler(insertar -> {
                                if (insertar.succeeded()) {
                                    declaracion.handle(Future.succeededFuture(
                                                    crearMensajeConFidelidad(new JsonObject().put(
                                                                    PROP_RESPUESTA,
                                                                    new JsonObject().put(PROP_ESTATUS, true)
                                                                    .put(PROP_DECLARACION_PENDIENTE,
                                                                            true)
                                                                    .put(PROP_MENSAJE,
                                                                            PROP_MENSJAE_CONTINUAR_DECLA)
                                                                    .put(PROP_DECLARACION,
                                                                            insertar.result())))));
                                } else {
                                    LOGGER.fatal(ERROR_CREAR_DECLARACION_TEMP);
                                    declaracion.fail(generarVerificarDatos(ERROR_CREAR_DECLARACION_TEMP).toString());
                                }
                            });
                } else {
                    declaracionPrecargada.put(PROP_ENCABEZADO, crearEncabezadoPrecarga(result.getJsonObject(PROP_ENCABEZADO)));
                    if (declaracionPrecargada.getJsonObject(PROP_ENCABEZADO).getJsonObject(PROP_USUARIO).getInteger(ID_SP) == null && declaracionPrecargada.getInteger(ID_SP) != null) {
                        declaracionPrecargada.getJsonObject(PROP_ENCABEZADO).getJsonObject(PROP_USUARIO).put(ID_SP, declaracionPrecargada.getInteger(ID_SP));
                    }
                    if (declaracionPrecargada.getJsonObject(PROP_NIVEL_JERARQUICO) != null
                            && declaracionPrecargada.getJsonObject(PROP_NIVEL_JERARQUICO).getInteger(PROP_ID_CATALOGO) > result.getJsonObject(PROP_ENCABEZADO).getJsonObject(PROP_NIVEL_JERARQUICO).getInteger(PROP_ID_CATALOGO)) {
                        declaracionPrecargada.getJsonObject(PROP_ENCABEZADO).put(PROP_NIVEL_JERARQUICO, declaracionPrecargada.getJsonObject(PROP_NIVEL_JERARQUICO));
                        declaracionPrecargada.put(PROP_ENCABEZADO, crearEncabezadoPrecarga(declaracionPrecargada.getJsonObject(PROP_ENCABEZADO)));
                    }
                    if ((EnumTipoDeclaracion.INICIO.name().equals(declaracionPrecargada.getJsonObject(PROP_ENCABEZADO).getString(PROP_TIPO_DECLARACION))
                            || EnumTipoDeclaracion.CONCLUSION.name().equals(declaracionPrecargada.getJsonObject(PROP_ENCABEZADO).getString(PROP_TIPO_DECLARACION))) && declaracionPrecargada.getString(PROP_FECHA_ENCARGO) != null && !declaracionPrecargada.getString(PROP_FECHA_ENCARGO).equals(declaracionPrecargada.getJsonObject(PROP_ENCABEZADO).getString(PROP_FECHA_ENCARGO))) {
                        declaracionPrecargada.getJsonObject(PROP_ENCABEZADO).put(PROP_FECHA_ENCARGO, declaracionPrecargada.getString(PROP_FECHA_ENCARGO));
                        declaracionPrecargada.getJsonObject(PROP_ENCABEZADO).put(PROP_ANIO, declaracionPrecargada.getString(PROP_FECHA_ENCARGO).substring(declaracionPrecargada.getString(PROP_FECHA_ENCARGO).length() - 4));
                    }
                    declaracionPrecargada.remove(PROP_FECHA_ENCARGO);
                    declaracionPrecargada.remove(PROP_NIVEL_JERARQUICO);
                    declaracionPrecargada.remove(ID_SP);
                    declaracionPrecargada.put(PROP_DATOS_PERSONALES, result.getJsonObject(PROP_DATOS_PERSONALES));
                    declaracionPrecargada.put(PROP_INSTITUCION_RECEPTORA, result.getJsonObject(PROP_INSTITUCION_RECEPTORA));
//Se realiza el comentar la linea para agregar la siguiente, y precargar los datos de sitPerEstCiv, RegMat, PaiNac y nacionalidad                                            
//                  declaracionPrecargada.getJsonObject(PROP_DECLARACION).put(PROP_DATOS_GENERALES, crearModuloDatosGenerales(result.getJsonObject(PROP_DATOS_PERSONALES)));
                    JsonObject datosGen = declaracionPrecargada.getJsonObject(PROP_DECLARACION).getJsonObject(PROP_DATOS_GENERALES);
                    declaracionPrecargada.getJsonObject(PROP_DECLARACION).put(PROP_DATOS_GENERALES, crearModuloDatosGenerales(result.getJsonObject(PROP_DATOS_PERSONALES)));
                    if (datosGen != null ){
                        declaracionPrecargada.getJsonObject(PROP_DECLARACION).getJsonObject(PROP_DATOS_GENERALES).put("nacionalidad", datosGen.getValue("nacionalidad"));
                        declaracionPrecargada.getJsonObject(PROP_DECLARACION).getJsonObject(PROP_DATOS_GENERALES).put("paisNacimiento", datosGen.getValue("paisNacimiento"));
                        declaracionPrecargada.getJsonObject(PROP_DECLARACION).getJsonObject(PROP_DATOS_GENERALES).put("situacionPersonalEstadoCivil", datosGen.getValue("situacionPersonalEstadoCivil"));
                    }

                    declaracion.handle(Future.succeededFuture(
                            crearMensajeConFidelidad(new JsonObject().put(
                                            PROP_RESPUESTA,
                                            new JsonObject().put(PROP_ESTATUS, true)
                                            .put(PROP_DECLARACION_PENDIENTE,
                                                    true)
                                            .put(PROP_MENSAJE,
                                                    PROP_MENSJAE_CONTINUAR_DECLA)
                                            .put(PROP_DECLARACION, declaracionPrecargada)))));
                }
            } else {
                LOGGER.fatal(String.format("Errpr solicitud de precarga: %s", precarga.cause().getMessage()));
                daoDecla.crearNuevaDeclaracion(
                        result.getJsonObject(PROP_INSTITUCION_RECEPTORA),
                        result.getJsonObject(PROP_ENCABEZADO),
                        result.getJsonObject(PROP_DATOS_PERSONALES))
                        .setHandler(insertar -> {
                            if (insertar.succeeded()) {
                                declaracion.handle(Future.succeededFuture(
                                                crearMensajeConFidelidad(new JsonObject().put(
                                                                PROP_RESPUESTA,
                                                                new JsonObject().put(PROP_ESTATUS, true)
                                                                .put(PROP_DECLARACION_PENDIENTE,
                                                                        true)
                                                                .put(PROP_MENSAJE,
                                                                        PROP_MENSJAE_CONTINUAR_DECLA)
                                                                .put(PROP_DECLARACION,
                                                                        insertar.result())))));
                            } else {
                                LOGGER.fatal(ERROR_CREAR_DECLARACION_TEMP);
                                declaracion.fail(generarVerificarDatos(ERROR_CREAR_DECLARACION_TEMP).toString());
                            }
                        });
            }
        });
        return declaracion;
    }
}
