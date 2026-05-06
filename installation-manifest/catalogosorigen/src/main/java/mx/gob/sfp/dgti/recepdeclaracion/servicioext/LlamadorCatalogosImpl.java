/**
 * @(#)LlamadorCatalogosImpl.java 10/12/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.recepdeclaracion.servicioext;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.*;
import mx.gob.sfp.dgti.recepdeclaracion.dto.InfoDTO;
import mx.gob.sfp.dgti.recepdeclaracion.dto.LlamadoDTO;
import mx.gob.sfp.dgti.recepdeclaracion.util.EnumCampos;
import mx.gob.sfp.dgti.recepdeclaracion.util.EnumEtiquetas;
import mx.gob.sfp.dgti.recepdeclaracion.util.EnumQueries;
import mx.gob.sfp.dgti.recepdeclaracion.util.EnumRespuestas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Clase para el servicio LlamadorCatalogosImpl
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 10/12/2019
 */
public class LlamadorCatalogosImpl implements LlamadorCatalogos{

    /**
     * Mapa con los catalogos
     */
    private HashMap<String, InfoDTO> serviciosCatalogos;

    /**
     * Lista con los catalogos disponibles en to  dos
     */
    private List<String> listaDisponiblesTodos;

    /**
     * WebCLient
     */
    private WebClient client;

    /**
     * JsonObject con la lista de enums
     */
    private final JsonObject catalogoEnums = obtenerEnums();

    /**
     * El logger
     */
    static final Logger LOGGER = LoggerFactory.getLogger(LlamadorCatalogosImpl.class);

    /**
     * Consulta individual
     */
    static final JsonObject consultaIndividual = new JsonObject()
            .put(EnumQueries.QUERY.getValor(), EnumQueries.CONSULTA_INDIVIDUAL.getValor());

    /**
     * Consulta individual uno
     */
    static final JsonObject consultaIndividualUno = new JsonObject()
            .put(EnumQueries.QUERY.getValor(), EnumQueries.CONSULTA_INDIDUAL_UNO.getValor());

    /**
     * Consulta individual uno
     */
    static final JsonObject consultaIndividualFk = new JsonObject()
            .put(EnumQueries.QUERY.getValor(), EnumQueries.CONSULTA_INDIVIDUAL_FK.getValor());

    /**
     * Consulta individual uno fk
     */
    static final JsonObject consultaIndividualUnoFk = new JsonObject()
            .put(EnumQueries.QUERY.getValor(), EnumQueries.CONSULTA_INDIVIDUAL_UNO_FK.getValor());

    /**
     * Consulta para validar
     */
    static final JsonObject consultaValidar = new JsonObject()
            .put(EnumQueries.QUERY.getValor(), EnumQueries.CONSULTA_VALIDAR.getValor());

    /**
     * Consulta ente
     */
    static final JsonObject consultaEntes = new JsonObject()
            .put(EnumQueries.QUERY.getValor(), EnumQueries.CONSULTA_ENTES.getValor());

    /**
     * Constructor
     *
     * @param vertx: el vertx
     */
    public LlamadorCatalogosImpl(Vertx vertx) {

        serviciosCatalogos = new HashMap<>();
        listaDisponiblesTodos = new ArrayList<>();
        LOGGER.info(EnumEtiquetas.LLAMADOR_CATALOGOS.getValor());

        for (EnumCatalogos cat: EnumCatalogos.values()) {
            if(System.getenv(cat.name()) != null) {
                LOGGER.info(EnumEtiquetas.CATALOGO_AGREGADO.getValor() + cat.name() + " - " + System.getenv(cat.name()));
                serviciosCatalogos.put(cat.name(),
                        new InfoDTO(System.getenv(cat.name()), obtenerQuery(cat.name())));
                listaDisponiblesTodos.add(cat.name());
            }
        }
        for (EnumCatalogosUno cat: EnumCatalogosUno.values()) {
            if(System.getenv(cat.name()) != null) {
                LOGGER.info(EnumEtiquetas.CATALOGO_AGREGADO.getValor() + cat.name() + " - " + System.getenv(cat.name()));
                serviciosCatalogos.put(cat.name(),
                        new InfoDTO(System.getenv(cat.name()), obtenerQuery(cat.name())));
                listaDisponiblesTodos.add(cat.name());
            }
        }
        for (EnumCatalogosFk cat: EnumCatalogosFk.values()) {
            if(System.getenv(cat.name()) != null) {
                LOGGER.info(EnumEtiquetas.CATALOGO_AGREGADO.getValor() + cat.name() + " - " + System.getenv(cat.name()));
                serviciosCatalogos.put(cat.name(),
                        new InfoDTO(System.getenv(cat.name()), obtenerQuery(cat.name())));
            }
        }
        for (EnumCatalogosUnoFk cat: EnumCatalogosUnoFk.values()) {
            if(System.getenv(cat.name()) != null) {
                LOGGER.info(EnumEtiquetas.CATALOGO_AGREGADO.getValor() + cat.name() + " - " + System.getenv(cat.name()));
                serviciosCatalogos.put(cat.name(),
                        new InfoDTO(System.getenv(cat.name()), obtenerQuery(cat.name())));
                listaDisponiblesTodos.add(cat.name());
            }
        }
        for (EnumCatalogosOtros cat: EnumCatalogosOtros.values()) {
            if(System.getenv(cat.name()) != null) {
                LOGGER.info(EnumEtiquetas.CATALOGO_AGREGADO.getValor() + cat.name() + " - " + System.getenv(cat.name()));
                serviciosCatalogos.put(cat.name(),
                        new InfoDTO(System.getenv(cat.name()), obtenerQuery(cat.name())));
            }
        }

        client = WebClient.create(vertx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LlamadorCatalogos llamarCatalogo(String catalogo, Handler<AsyncResult<JsonObject>> resultHandler) {

        consultaIndividual(catalogo)
                .subscribe( respuesta -> resultHandler.handle(Future.succeededFuture(
                        new JsonObject().put(EnumCampos.ESTADO.getValor(), EnumRespuestas.CORRECTO).put(catalogo, respuesta)
                )),
        error -> {
            LOGGER.info(EnumEtiquetas.OCURRIO_ALGO.getValor() + error.getMessage());
                resultHandler.handle(Future.succeededFuture(
                        new JsonObject().put(EnumCampos.ESTADO.getValor(), EnumRespuestas.CAT_NO_DISPONIBLE.name())));
                });
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LlamadorCatalogos validarCatalogo(String catalogo, JsonObject info, Handler<AsyncResult<JsonObject>> resultHandler) {

        validarCatalogo(catalogo, new JsonObject().put(EnumCampos.REGISTRO.getValor(),info))
                .subscribe( respuesta -> resultHandler.handle(Future.succeededFuture(respuesta)),
                        error -> {
                            LOGGER.info(EnumEtiquetas.OCURRIO_ALGO.getValor() + error.getMessage());
                            resultHandler.handle(Future.succeededFuture(
                                    new JsonObject().put(EnumCampos.ESTADO.getValor(), EnumRespuestas.CAT_NO_DISPONIBLE.name())));
                        });
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LlamadorCatalogos llamarTodosCatalogos(Handler<AsyncResult<JsonObject>> resultHandler) {

        JsonObject todosCatalogos = new JsonObject();

        Single<List<JsonArray>> elFlow = Single.just(todosCatalogos)
                .flatMap(catalogosDisponibles -> Flowable.fromIterable(listaDisponiblesTodos)
                        .flatMapSingle(
                                catalogosDisponible -> consultaIndividual(catalogosDisponible)
                                        .doOnSuccess(
                                                respuesta -> {
                                                    if(respuesta != null) {
                                                        todosCatalogos.put(catalogosDisponible,
                                                                respuesta);

                                                     }
                                                })
                                        .doOnError(error -> {
                                                    LOGGER.info("=== En cat : " + catalogosDisponible + " : "
                                                    + EnumEtiquetas.ERROR_SERVICIO.getValor()
                                                    + error.getMessage()
                                                );
                                        })
                        ).toList()
                ).onErrorResumeNext(e -> Single.error(new Exception("Error al iterar lista de catalogos.")));

        elFlow.subscribe( respuesta -> resultHandler.handle(Future.succeededFuture(
                new JsonObject().put(EnumCampos.ESTADO.name(), EnumRespuestas.CORRECTO.name())
                        .put(EnumCampos.CATALOGOS.getValor(), todosCatalogos)
                        .put(EnumCampos.ENUMS.getValor(), catalogoEnums)
        )),
                error -> {
                    resultHandler.handle(Future.succeededFuture(
                            new JsonObject().put(EnumCampos.ESTADO.getValor(), EnumRespuestas.CAT_NO_DISPONIBLE.name())));
                });

        return this;
    }

    /**
     * Metodo que permite llamar a otros servicios
     *
     * @param llamado: Objeto LlamadoDTO con la informacion del servicio
     * @return: JsonObject con la respuesta mandada por el servicio con la respuesta de cada modulo
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 10/12/2019
     */
    private Single<JsonObject> llamarServicio(LlamadoDTO llamado) {

        return client
                .postAbs(llamado.getUrlAbs())
                .rxSendJsonObject(llamado.getInfo())
                .timeout(10, TimeUnit.SECONDS )
                .retry(1)
                .onErrorResumeNext(e -> Single.error(new Exception("Error en llamarServicio(): " + e)))
                .map(HttpResponse::bodyAsJsonObject)
                ;
    }

    /**
     * Metodo para hacer una consulta individual a un catalogo
     *
     * @param catalogo: catalogo que se va a consultar
     *
     * @return Single son la lista de elementos del catalogo
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 10/12/2019
     */
    private Single<JsonArray> consultaIndividual(String catalogo) {
        if (serviciosCatalogos.get(catalogo) == null) return Single.just(new JsonArray());
        JsonObject query = obtenerQuery(catalogo);
        if (query == null ) {
            return Single.error(new Exception());
        }

        LlamadoDTO llamadoCatalogo = new LlamadoDTO(
                catalogo,
                serviciosCatalogos.get(catalogo).getUrl(),
                serviciosCatalogos.get(catalogo).getQuery());

        return llamarServicio(llamadoCatalogo)
                .map( respuesta -> {
                    if (!respuesta.isEmpty() && respuesta.getJsonObject(EnumCampos.DATA.getValor()) != null
                            && respuesta.getJsonObject(
                                    EnumCampos.DATA.getValor()).getJsonArray(EnumCampos.CATALOGO.getValor()) != null) {
                        return respuesta.getJsonObject(
                                EnumCampos.DATA.getValor()).getJsonArray(EnumCampos.CATALOGO.getValor());
                    }
                    return null;
                }
        );

    }

    /**
     * Metodo para validar un catalogo
     * @param catalogo: catalogo que se va a validar
     * @param info: info del catalogo que se va a validar
     *
     * @return Single con el resultado de la validacion
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 10/12/2019
     */
    private Single<JsonObject> validarCatalogo(String catalogo, JsonObject info) {
        if(serviciosCatalogos.get(catalogo)==null) {
            return Single.just(new JsonObject()
                    .put(EnumCampos.ESTADO.getValor(), EnumRespuestas.CAT_NO_DISPONIBLE.name()));
        }

        JsonObject query = new JsonObject(consultaValidar.toString())
            .put(EnumCampos.VARIABLES.getValor(), info);
        if (query == null ) {
            return Single.error(new Exception());
        }
        LlamadoDTO llamadoCatalogo = new LlamadoDTO(
                catalogo,
                serviciosCatalogos.get(catalogo).getUrl(),
                query);

        return llamarServicio(llamadoCatalogo)
                .map(respuesta -> {
                    try {
                        return new JsonObject()
                                .put(EnumCampos.ESTADO.getValor(), EnumRespuestas.CORRECTO.name())
                                .put(EnumCampos.RESPUESTA.getValor(), respuesta.getJsonObject(EnumCampos.DATA.getValor())
                                        .getBoolean(EnumCampos.VALIDAR_INFO_CATALOGO.getValor()));

                    } catch (Exception e) {
                        LOGGER.error(EnumEtiquetas.ERROR_SERVICIO.getValor() + llamadoCatalogo.getNombreServicio() + e.getMessage());
                        return new JsonObject()
                                .put(EnumCampos.ESTADO.getValor(), EnumRespuestas.CAT_NO_DISPONIBLE.name());
                    }
                });
    }

    /**
     * Metodo para obtener el query de GraphQL
     *
     * @param catalogo: nombre del catalogo que se va a consultar ya que regresan diferentes campos dependiendo del
     *                catalogo
     * @return: el query necesario para la consulta
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 10/12/2019
     */
    private JsonObject obtenerQuery(String catalogo) {
        if(EnumCatalogos.existeValor(catalogo)) {
            return consultaIndividual;
        } else if(EnumCatalogosUno.existeValor(catalogo)) {
            return consultaIndividualUno;
        } else if(EnumCatalogosFk.existeValor(catalogo)) {
            return consultaIndividualFk;
        } else if(EnumCatalogosUnoFk.existeValor(catalogo)) {
            return consultaIndividualUnoFk;
        } else if(EnumCatalogosOtros.existeValor(catalogo)) {
            return consultaEntes;
        }
        return null;
    }

    /**
     * Metodo para obtener los enums, utilizando las funciones definidas en Utils.
     *
     * @return JsonObject con los enums existentes.
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 10/12/2019
     */
    private JsonObject obtenerEnums() {
        try {
            JsonObject enums = new JsonObject();

            enums.put(EnumEnums.ENUM_TIPO_DOCUMENTO_OBTENIDO.name(), new JsonArray(EnumToJson.enumTipoDocumento()));
            enums.put(EnumEnums.ENUM_RELACION_CON_DECLARANTE.name(), new JsonArray(EnumToJson.enumRelacionDeclarante()));
            enums.put(EnumEnums.ENUM_TIPO_BIEN.name(), new JsonArray(EnumToJson.enumTipoBien()));
            enums.put(EnumEnums.ENUM_TIPO_BIEN_PRESTADO.name(), new JsonArray(EnumToJson.enumTipoBienPrestamo()));

            enums.put(EnumEnums.ENUM_FORMA_RECEPCION.name(), new JsonArray(EnumToJson.enumFormaRecepcion()));
            enums.put(EnumEnums.ENUM_PARTICIPANTE.name(), new JsonArray(EnumToJson.enumParticipante()));
            enums.put(EnumEnums.ENUM_TIPO_REPRESENTACION.name(), new JsonArray(EnumToJson.enumTipoRepresentacion()));
            enums.put(EnumEnums.ENUM_TIPO_FIDEICOMISO.name(), new JsonArray(EnumToJson.enumTipoFideicomiso()));
            enums.put(EnumEnums.ENUM_TIPO_PARTICIPACION_F.name(), new JsonArray(EnumToJson.enumTipoParticipacionF()));
            enums.put(EnumEnums.ENUM_NIVEL_ORDEN_GOBIERNO.name(), new JsonArray(EnumToJson.enumNivelGobierno()));
            enums.put(EnumEnums.ENUM_AMBITO_PODER.name(), new JsonArray(EnumToJson.enumAmbitoPoder()));
            enums.put(EnumEnums.ENUM_TIPO_FORMATO.name(), new JsonArray(EnumToJson.enumTipoFormatoDeclaracion()));
            enums.put(EnumEnums.ENUM_TIPO_DECLARACION.name(), new JsonArray(EnumToJson.enumTipoDeclaracion()));
            enums.put(EnumEnums.ENUM_TIPO_OPERACION.name(), new JsonArray(EnumToJson.enumTipoOperacion()));
            enums.put(EnumEnums.ENUM_AMBITO_SECTOR.name(), new JsonArray(EnumToJson.enumAmbitoSector()));
            enums.put(EnumEnums.ENUM_ESTATUS_ESCOLARIDAD.name(), new JsonArray(EnumToJson.enumEstatusEscolaridad()));
            enums.put(EnumEnums.ENUM_FORMA_PAGO.name(), new JsonArray(EnumToJson.enumFormaPago()));
            enums.put(EnumEnums.ENUM_FORMA_PAGO_OTRO.name(), new JsonArray(EnumToJson.enumFormaPagoOtro()));
            enums.put(EnumEnums.ENUM_TIPO_PERSONA.name(), new JsonArray(EnumToJson.enumTipoPersona()));
            enums.put(EnumEnums.ENUM_TIPO_REMUNERACION.name(), new JsonArray(EnumToJson.enumTipoRemuneracion()));
            enums.put(EnumEnums.ENUM_UBICACION.name(), new JsonArray(EnumToJson.enumUbicacion()));
            enums.put(EnumEnums.ENUM_LUGAR_RESIDE.name(), new JsonArray(EnumToJson.enumLugarReside()));
            enums.put(EnumEnums.ENUM_VALOR_CONFORME_A.name(), new JsonArray(EnumToJson.enumValorConformeA()));

            return enums;
        } catch(Exception e) {
            LOGGER.error(EnumEtiquetas.ERROR_SERVICIO.getValor() + e);
            return null;
        }
    }

}
