/**
 * @(#)ConsultaApiImpl.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.as.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.Single;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.omextback.as.ConsultaHttpAS;
import mx.gob.sfp.dgti.omextback.dto.enteReceptor.ParamsEnteReceptorDTO;
import mx.gob.sfp.dgti.omextback.dto.hash.OeOficioxvista;
import mx.gob.sfp.dgti.omextback.dto.respuestas.FirmaDTO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.FirmaDatosDTO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.FirmaFirmaDTO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.VistaDTO;
import mx.gob.sfp.dgti.omextback.exception.SFPException;
import mx.gob.sfp.dgti.omextback.util.constantes.*;
import mx.gob.sfp.dgti.omextback.util.hash.Hash;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Clase para el servicio ConsultaApiImpl
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public class ConsultaHttpASImpl implements ConsultaHttpAS {

    /**
     * WebCLient
     */
    private WebClient client;

    /**
     *
     */
    private Vertx vertx;

    /**
     * El logger
     */
    static final Logger LOGGER = LoggerFactory.getLogger(ConsultaHttpASImpl.class);

    /**
     * Nombre de la variable de entorno de la url de catalogo de entes
     */
    static final String URL_GRAPHQL_LABEL = "URL_GRAPHQL_ENTES";

    static final String URL_REPORTES_GENERAR_LABEL = "URL_REPORTES_GENERAR";

    static final String URL_REPORTES_GENERAR_VISTA_LABEL = "URL_REPORTES_GENERAR_VISTA";

    static final String URL_ENTES_RECEPTORES_TODOS_LABEL = "URL_ENTES_RECEPTORES_TODOS";

    /**
     * Nombre de la variable de entorno de la url al servicio de usuariosClienteFiel
     */
    static final String URL_SERVICIO_FIRMA_AUTOMATIZADA_LABEL = "URL_SERVICIO_FIRMA_AUTOMATIZADA";


    static final String URL_SERVICIO_IMPRESION_VISTA_LABEL = "URL_SERVICIO_IMPRESION_VISTA";

    AtomicReference<JsonArray> entesMemoria = new AtomicReference<>(new JsonArray());

    public AtomicReference<HashMap<Integer, JsonArray>> mapaEntesReceptoresPorCollname = new AtomicReference<>(new HashMap());

    public HashMap<String, JsonObject> mapaEntesPorId = new HashMap<>();


    /**
     * URL para kas consultas de entes
     */
    static final String URL_ENTES_GRAPHQL =
            System.getenv(URL_GRAPHQL_LABEL) != null ?
                    System.getenv(URL_GRAPHQL_LABEL) : "";

    static final String URL_REPORTE_GENERAR =
            System.getenv(URL_REPORTES_GENERAR_LABEL) != null ?
                    System.getenv(URL_REPORTES_GENERAR_LABEL) : "";

    static final String URL_REPORTE_GENERAR_VISTA =
            System.getenv(URL_REPORTES_GENERAR_VISTA_LABEL) != null ?
                    System.getenv(URL_REPORTES_GENERAR_VISTA_LABEL) : "";

    static final String URL_SERVICIO_FIRMA_AUTOMATIZADA =
            System.getenv(URL_SERVICIO_FIRMA_AUTOMATIZADA_LABEL) != null ?
                    System.getenv(URL_SERVICIO_FIRMA_AUTOMATIZADA_LABEL) : "";

    static final String URL_SERVICIO_IMPRESION_VISTA =
            System.getenv(URL_SERVICIO_IMPRESION_VISTA_LABEL) != null ?
                    System.getenv(URL_SERVICIO_IMPRESION_VISTA_LABEL) : "";

    static final String URL_ENTES_RECEPTORES_TODOS =
            System.getenv(URL_ENTES_RECEPTORES_TODOS_LABEL) != null ?
                    System.getenv(URL_ENTES_RECEPTORES_TODOS_LABEL) : "";

    /**
     * Constructor
     *
     * @param vertx: el vertx
     */
    public ConsultaHttpASImpl(Vertx vertx) {
        this.vertx = vertx;
        LOGGER.info("=== Se consulta entes de GraphQL sobre : " + URL_ENTES_GRAPHQL);
        LOGGER.info("=== Se generan reportes en : " +  URL_REPORTE_GENERAR);
        LOGGER.info("=== Para la firma automatizada : " +  URL_SERVICIO_FIRMA_AUTOMATIZADA);
        LOGGER.info("=== Para el servicio de impresion: " + URL_SERVICIO_IMPRESION_VISTA);
        LOGGER.info("=== Se llama al servicio que crea las vistas en: " + URL_REPORTE_GENERAR_VISTA);
        LOGGER.info("=== Se consultan los entes receptores en :" + URL_ENTES_RECEPTORES_TODOS);
        client = WebClient.create(this.vertx, new WebClientOptions().setTrustAll(true));

        obtenerTodosEntes()
                .doOnSuccess(
                    l -> {
                        entesMemoria.set(l);
                        LOGGER.info("=== Se obtiene lista completa de entes: " + entesMemoria.get().size());
                        entesMemoria.get().forEach(e -> {
                            JsonObject o = (JsonObject) e;
                            mapaEntesPorId.put(o.getString(EnumGraphQL.ID_ENTE.getValor()), o);
                        });
                        obtenerTodosEntesReceptores()
                                .doOnSuccess(er -> {
                                    mapaEntesReceptoresPorCollname.set(er);

                                    mapaEntesReceptoresPorCollname.get().entrySet().forEach(
                                            m -> {
                                                LOGGER.info("collname " + m.getKey() + " : " + m.getValue().size());
                                            }
                                    );

                                })
                                .subscribe();
                    }
        )
                .doOnError(e -> {
                    LOGGER.info("=== No se pudieron obtener los entes: " + entesMemoria.get());
                })
                .subscribe();

    }

    /**
     * Metodo que permite llamar a otros servicios
     *
     * @param query: Objeto LlamadoDTO con la informacion del servicio
     * @return: JsonObject con la respuesta mandada por el servicio con la respuesta de cada modulo
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 26/09/2019
     */
    private Single<JsonObject> llamarServicio(JsonObject query, String consulta) {

        return client
                .postAbs(consulta)
                .rxSendJsonObject(query)
                .timeout(55, TimeUnit.SECONDS )
                .doOnError(e ->  LOGGER.info("=== Un error en + " + e.getClass() + " - " + e.getMessage()))
                .onErrorResumeNext(e -> {
                    if(e instanceof TimeoutException) {
                        return Single.error(
                                new SFPException(EnumError.ERROR_CONSULTA_HTTP,
                                        "Doble timeout vencido de 55 al consultar: " + consulta));
                    }
                    LOGGER.info("=== Conexion fallida http hacia " + consulta + " - " + e.getMessage());

                    return Single.error(new SFPException(EnumError.ERROR_CONSULTA_HTTP, e.getMessage()));
                })
                .map(respuesta -> {
                    if (respuesta.statusCode() != HttpResponseStatus.OK.code()) {
                        throw new SFPException(EnumError.ERROR_CONSULTA_HTTP, "status " + respuesta.statusCode()
                                + " - " + respuesta.bodyAsString());
                    }
                    try {
                        return respuesta.bodyAsJsonObject();
                    } catch (Exception e ) {
                        throw new SFPException(EnumError.ERROR_CONSULTA_HTTP, "status " + respuesta.statusCode()
                                + " - " + "Problema con respuesta del servicio");
                    }
                });
    }

    /**
     * Metodo que permite llamar a otros servicios
     *
     * @param query: Objeto LlamadoDTO con la informacion del servicio
     * @return: JsonObject con la respuesta mandada por el servicio con la respuesta de cada modulo
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 26/09/2019
     */
    private Single<String> llamarServicioString(JsonObject query, String consulta) {

        return client
                .postAbs(consulta)
                .rxSendJsonObject(query)
                .timeout(55, TimeUnit.SECONDS )
                .doOnError(e ->  LOGGER.info("=== Un error en + " + e.getClass() + " - " + e.getMessage()))
                .onErrorResumeNext(e -> {
                    if(e instanceof TimeoutException) {
                        return Single.error(
                                new SFPException(EnumError.ERROR_CONSULTA_HTTP,
                                        "Doble timeout vencido de 55 al consultar: " + consulta));
                    }
                    LOGGER.info("=== Conexion fallida http hacia " + consulta + " - " + e.getMessage());

                    return Single.error(new SFPException(EnumError.ERROR_CONSULTA_HTTP, e.getMessage()));
                })
                .map(respuesta -> {
                    if (respuesta.statusCode() != HttpResponseStatus.OK.code()) {
                        throw new SFPException(EnumError.ERROR_CONSULTA_HTTP, "status " + respuesta.statusCode()
                                + " - " + respuesta.bodyAsString());
                    }
                    try {
                        return respuesta.bodyAsString();
                    } catch (Exception e ) {
                        throw new SFPException(EnumError.ERROR_CONSULTA_HTTP, "status " + respuesta.statusCode()
                                + " - " + "Problema con respuesta del servicio");
                    }
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<JsonObject> agregarFirmaAutomatizadaAVista(VistaDTO nuevaVista, boolean esFirmaListado) {

        OeOficioxvista oeOficioxvista = new OeOficioxvista(
                nuevaVista.getNumOficio(),
                nuevaVista.getFechaRegistro(),
                nuevaVista.getTipoIncumplimiento(),
                nuevaVista.getTotalServidores(),
                nuevaVista.getTipoDeclaracion(),
                nuevaVista.getMes(),
                nuevaVista.getAnio(),
                nuevaVista.getRemitente(), nuevaVista.getFechaRegistro());
        String cadena = (esFirmaListado) ? oeOficioxvista.toStringListado():oeOficioxvista.toStringOficio();
        String cadenaSha;
        Hash oHash = new Hash();

        cadenaSha = Hash.conEspaciosImPares(oHash.digestCadena(cadena)," ");

        LOGGER.info("La cadena normal queda : " + cadena);

        LOGGER.info("La cadena sha queda: " + cadenaSha);

        LOGGER.info("==== Lo que se envia para la firma automatizada: " + " cliente_id:" + nuevaVista.getFirmante().getCliente_id()
                +   " secret_key:" + nuevaVista.getFirmante().getSecret_key() + " servicio:" + nuevaVista.getFirmante().getServicio()
                + " cadena:" + cadenaSha
        );

        LOGGER.info("Em la url : " + URL_SERVICIO_FIRMA_AUTOMATIZADA);
        return client
                .getAbs(URL_SERVICIO_FIRMA_AUTOMATIZADA)
                .addQueryParam(EnumServicioFiel.CLIENTE_ID.getValor(), nuevaVista.getFirmante().getCliente_id())
                .addQueryParam(EnumServicioFiel.SECRET_KEY.getValor(), nuevaVista.getFirmante().getSecret_key())
                .addQueryParam(EnumServicioFiel.SERVICIO.getValor(), nuevaVista.getFirmante().getServicio())
                .addQueryParam(EnumServicioFiel.CADENA.getValor(), cadenaSha)
                .rxSend()
                .timeout(55, TimeUnit.SECONDS )
                .doOnError(e ->  LOGGER.info("=== Un error en agregar firmaAutomatizadaAVista + " + e.getClass() + " - " + e.getMessage()))
                .onErrorResumeNext(e -> {
                    if(e instanceof TimeoutException) {
                        return Single.error(
                                new SFPException(EnumError.ERROR_CONSULTA_HTTP,
                                        "Doble timeout vencido de 55 al consultar: "));
                    }
                    LOGGER.info("=== Conexion fallida http + " + e.getMessage());
                    return Single.error(new SFPException(EnumError.ERROR_NO_ES_POSIBLE_FIRMAR_VISTA, e.getMessage()));
                })
                .map(respuesta -> {
                    if (respuesta.statusCode() != HttpResponseStatus.OK.code()) {
                        throw new SFPException(EnumError.ERROR_NO_ES_POSIBLE_FIRMAR_VISTA, "status " + respuesta.statusCode()
                                + " - " + respuesta.bodyAsString());
                    }
                    FirmaFirmaDTO firmaFirma =
                            new FirmaFirmaDTO(respuesta.bodyAsJsonObject());

                    FirmaDTO firmaOficio = new FirmaDTO();

                    FirmaDatosDTO datosFirma = new FirmaDatosDTO();
                    datosFirma.setSha(cadenaSha);
                    datosFirma.setDatosParaSha(cadena);

                    firmaOficio.setDatos(datosFirma);
                    firmaOficio.setFirma(firmaFirma);
                    if(esFirmaListado) {
                        nuevaVista.setFirmaListado(firmaOficio);
                    } else {
                        nuevaVista.setFirmaOficio(firmaOficio);
                    }
                    return nuevaVista.toJson();

                });
    }

    @Override
    public Single<String> generarImpresionVista(JsonObject query) {
        LOGGER.info("=== Se trata de generar vista en " + URL_SERVICIO_IMPRESION_VISTA);
        return llamarServicioString(query, URL_SERVICIO_IMPRESION_VISTA);
    }

    @Override
    public Single<Boolean> generarServidoresVistas(Integer collName, List<String> folios, String idPeriodo) {
        JsonObject params = new JsonObject()
            .put("collName", collName)
            .put("folios", new JsonArray(folios));
        LOGGER.info("Los params quedan : " + params);

        return llamarServicio(params, URL_REPORTE_GENERAR_VISTA )
        //return llamarServicio(params,  "http://localhost:5001/generarVistas/")
                .flatMap(r -> {
                   LOGGER.info("El resultado devuelto es " + r  + " y todo bien.");
                    return Single.just(true);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<JsonArray> consultarEntes(JsonObject query) {
            return llamarServicio(query, URL_ENTES_GRAPHQL)
                    .retryWhen(f -> f.take(25).delay(500, TimeUnit.MILLISECONDS))
                    .onErrorResumeNext(e -> Single.error(new SFPException(EnumError.ERROR_CONSULTA_EXT_GRAPHQL, e) ))
                    .map(r -> r.getJsonObject(EnumRespuestaGraphQL.DATA.getValor())
                            .getJsonArray(EnumRespuestaGraphQL.OBTENER_ENTES.getValor())
                    );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<String> generarReporte(JsonObject query) {
        //LOGGER.info("Query del reporte ---> " + query);
        //LOGGER.info("Se genera reporte sobre -> " + URL_REPORTE_GENERAR);
        return llamarServicio(query, URL_REPORTE_GENERAR)
                .onErrorResumeNext(e -> Single.error(new SFPException(EnumError.ERROR_CONSULTA_HTTP, e)))
                .map(r ->  r.getString("id"));
    }

    private Single<HashMap<Integer, JsonArray>> obtenerTodosEntesReceptores() {
            return client.getAbs(URL_ENTES_RECEPTORES_TODOS)
                .rxSend()
                .doOnError(e -> {
                    LOGGER.info("=== Ocurrio un error al obtener todos los entes receptores" + e);
                })
                .map(HttpResponse::bodyAsJsonArray
                        )
                .map(ar -> obtenerMapaConsultasEnteReceptor(ar));
    }

    /**
     *
     * @param arrayJson
     * @return
     */
    private HashMap<Integer, JsonArray> obtenerMapaConsultasEnteReceptor(JsonArray arrayJson) {
        HashMap<Integer, JsonArray> mapaEntesReceptores = new HashMap<>();
        arrayJson.forEach(
                a -> {
                    JsonObject objetoJson = (JsonObject) a;
                    try {
                        ParamsEnteReceptorDTO paramsEnte = new ParamsEnteReceptorDTO();
                        Integer collName = objetoJson.getFloat("collName").intValue();
                        paramsEnte.setCollName(collName);
                        JsonObject ente = objetoJson.getJsonObject("ente");
                        EnumAmbitoPublico ambitoPublico = EnumAmbitoPublico.valueOf(ente.getString("ambitoPublico"));
                        paramsEnte.setPoder(ambitoPublico);
                        JsonObject nivelOrdenGobierno = ente.getJsonObject("nivelOrdenGobierno");
                        EnumNivelOrdenGobierno nivel = EnumNivelOrdenGobierno.valueOf(nivelOrdenGobierno.getString("nivelOrdenGobierno"));
                        paramsEnte.setNivelGobierno(nivel);
                        if(nivel.equals(EnumNivelOrdenGobierno.ESTATAL) || nivel.equals(EnumNivelOrdenGobierno.MUNICIPAL)) {
                            JsonObject entidadFederativa = nivelOrdenGobierno.getJsonObject("entidadFederativa");
                            Integer idEntidad = entidadFederativa.getFloat("id").intValue();
                            paramsEnte.setIdEntidadFederativa(idEntidad);
                            if(nivel.equals(EnumNivelOrdenGobierno.MUNICIPAL)) {
                                JsonObject municipioAlcaldia = entidadFederativa.getJsonObject("municipioAlcaldia");
                                Integer idMunicipio = municipioAlcaldia.getFloat("id").intValue();
                                paramsEnte.setIdMunicipio(idMunicipio);
                            }
                        }
                        JsonArray arreglos = filtrarEntesMemoriaEnteReceptor(paramsEnte);
                        mapaEntesReceptores.put(collName, arreglos);
                    } catch (Exception e) {
                        LOGGER.info("Error : " + e);
                    }

                }
        );
        return mapaEntesReceptores;

    }

    /**
     *
     * @return
     */
    private Single<JsonArray> obtenerTodosEntes() {
        JsonObject filtros = new JsonObject();

        JsonObject query = new JsonObject()
                .put(EnumQueryGraphQL.QUERY.getValor(), EnumQueryGraphQL.QUERY_OBTENER_ENTES.getValor())
                .put(EnumQueryGraphQL.VARIABLES.getValor(), new JsonObject()
                        .put(EnumQueryGraphQL.FILTRO.getValor(), filtros));

        return consultarEntes(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<JsonArray> filtrarEntesMemoria(JsonObject condiciones) {

        JsonObject filtro = condiciones.getJsonObject(EnumQueryGraphQL.VARIABLES.getValor()).getJsonObject(EnumQueryGraphQL.FILTRO.getValor());
        List<Object> nuevo = entesMemoria.get().stream().filter(
                a -> {
                    JsonObject o = (JsonObject) a;
                    if (filtro.containsKey(EnumQueryGraphQL.PODER.getValor()) && ! o.getString(EnumGraphQL.PODER.getValor())
                                .equals(filtro.getString(EnumQueryGraphQL.PODER.getValor())) ) {
                            return false;
                    }
                    if (filtro.containsKey(EnumQueryGraphQL.NIVEL_GOBIERNO.getValor()) && !o.getJsonObject(EnumGraphQL.NIVEL_GOBIERNO.getValor()).getString(EnumGraphQL.NIVEL_GOBIERNO.getValor())
                                .equals(filtro.getString(EnumQueryGraphQL.NIVEL_GOBIERNO.getValor())) ) {
                            return false;
                    }
                    if (filtro.containsKey(EnumQueryGraphQL.ID.getValor()) && ! o.getString(EnumGraphQL.ID_ENTE.getValor())
                                .equals(filtro.getString(EnumQueryGraphQL.ID.getValor())) ) {
                            return false;
                    }
                    if (filtro.containsKey(EnumQueryGraphQL.RAMO.getValor()) && ! o.getInteger(EnumGraphQL.RAMO.getValor())
                                .equals(filtro.getInteger(EnumQueryGraphQL.RAMO.getValor())) ) {
                            return false;
                    }
                    return !filtro.containsKey(EnumQueryGraphQL.UNIDAD_RESPONSABLE.getValor()) || o.getString(EnumGraphQL.UR.getValor())
                            .equals(filtro.getString(EnumQueryGraphQL.UNIDAD_RESPONSABLE.getValor()));
                }
        ).collect(Collectors.toList());

        return Single.just(new JsonArray(nuevo));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<JsonArray> filtrarEntesMemoriaPorCollName(JsonObject condiciones, Integer collName) throws SFPException{

        if(! mapaEntesReceptoresPorCollname.get().containsKey(collName)) {
            throw new SFPException(EnumError.ERROR_COLLNAME_NO_DISPONIBLE);
        }

        JsonArray enteCollName = mapaEntesReceptoresPorCollname.get().get(collName);
        JsonObject filtro = condiciones.getJsonObject(EnumQueryGraphQL.VARIABLES.getValor()).getJsonObject(EnumQueryGraphQL.FILTRO.getValor());
        List<Object> nuevo = enteCollName.stream().filter(
                a -> {
                    JsonObject o = (JsonObject) a;
                    if (filtro.containsKey(EnumQueryGraphQL.ID.getValor()) && ! o.getString(EnumGraphQL.ID_ENTE.getValor())
                            .equals(filtro.getString(EnumQueryGraphQL.ID.getValor())) ) {
                        return false;
                    }
                    if (filtro.containsKey(EnumQueryGraphQL.RAMO.getValor()) && ! o.getInteger(EnumGraphQL.RAMO.getValor())
                            .equals(filtro.getInteger(EnumQueryGraphQL.RAMO.getValor())) ) {
                        return false;
                    }
                    return !filtro.containsKey(EnumQueryGraphQL.UNIDAD_RESPONSABLE.getValor()) || o.getString(EnumGraphQL.UR.getValor())
                            .equals(filtro.getString(EnumQueryGraphQL.UNIDAD_RESPONSABLE.getValor()));
                }
        ).collect(Collectors.toList());

        return Single.just(new JsonArray(nuevo));
    }

    /**
     *
     * @param paramsEnte
     * @return
     */
    private JsonArray filtrarEntesMemoriaEnteReceptor(ParamsEnteReceptorDTO paramsEnte) {
        List<JsonObject> entesReceptores = entesMemoria.get().stream().filter(
                a -> {
                    JsonObject o = (JsonObject) a;
                    if (paramsEnte.getPoder() != null && ! o.getString(EnumGraphQL.PODER.getValor())
                            .equals(paramsEnte.getPoder().getValor()))  {
                        return false;
                    }
                    JsonObject nivelOrdenGobierno = o.getJsonObject(EnumGraphQL.NIVEL_GOBIERNO.getValor());
                    if (paramsEnte.getNivelGobierno()!= null && ! nivelOrdenGobierno
                            .getString(EnumGraphQL.NIVEL_GOBIERNO.getValor()).equals(paramsEnte.getNivelGobierno().getValor()))  {
                        return false;
                    }
                    if (paramsEnte.getIdEntidadFederativa() != null || paramsEnte.getIdMunicipio() != null) {
                        JsonObject entidadFederativa = nivelOrdenGobierno.getJsonObject(EnumQueryGraphQL.ENTIDAD_FEDERATIVA.getValor());
                        if(entidadFederativa == null || entidadFederativa.getInteger("idEntidadFederativa") == null) {
                            return false;
                        }
                        if(!paramsEnte.getIdEntidadFederativa().equals(entidadFederativa.getInteger("idEntidadFederativa"))) {
                            return false;
                        }
                        if(paramsEnte.getIdMunicipio() != null) {
                               if(entidadFederativa.getJsonObject("municipio") == null ||
                                       entidadFederativa.getJsonObject("municipio").getInteger("idMunicipio") == null) {
                                   return false;
                               }
                               if(! paramsEnte.getIdMunicipio().equals(entidadFederativa.getJsonObject("municipio").getInteger("idMunicipio"))) {
                                   return false;
                               }
                        }
                    }
                    return true;
                }
        )
                .map(o -> (JsonObject) o)
                .collect(Collectors.toList())
                ;
        return new JsonArray(entesReceptores);
    }

    @Override
    public boolean validarCollName(Integer collName) throws SFPException{
        if(!mapaEntesReceptoresPorCollname.get().containsKey(collName)) {
            throw new SFPException(EnumError.ERROR_COLLNAME_NO_DISPONIBLE);
        }
        return true;
    }

}
