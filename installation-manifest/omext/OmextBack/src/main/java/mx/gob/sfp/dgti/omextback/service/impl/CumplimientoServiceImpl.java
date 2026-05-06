/**
 * @(#)VistasServiceImpl.java 24/05/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.service.impl;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import mx.gob.sfp.dgti.omextback.as.ConvertidorRespuestaAS;
import mx.gob.sfp.dgti.omextback.as.CreadorQueryAS;
import mx.gob.sfp.dgti.omextback.as.impl.ConsultaHttpASImpl;
import mx.gob.sfp.dgti.omextback.as.impl.ConvertidorRespuestaASImpl;
import mx.gob.sfp.dgti.omextback.as.impl.CreadorQueryASImpl;
import mx.gob.sfp.dgti.omextback.service.CumplimientoService;
import mx.gob.sfp.dgti.omextback.dao.OmextMongoDAO;
import mx.gob.sfp.dgti.omextback.dto.input.CondicionesOmextInputDTO;
import mx.gob.sfp.dgti.omextback.dto.input.FiltroOmextInputDTO;
import mx.gob.sfp.dgti.omextback.dto.input.FiltroPeriodosVistasInputDTO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.PeriodoDTO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.TextoOficioDTO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.VistaDTO;
import mx.gob.sfp.dgti.omextback.exception.SFPException;
import mx.gob.sfp.dgti.omextback.util.constantes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Clase para el servicio VistasServiceImpl
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public class CumplimientoServiceImpl implements CumplimientoService {

    /**
     * Creador de queries
     */
    private final  CreadorQueryAS creadorQueryAS;

    /**
     * Creador de queries
     */
    private final ConvertidorRespuestaAS convertidorRespuestaAS;

    /**
     * Consultas
     */
    private final ConsultaHttpASImpl consultaHttpAS;

    /**
     * Servicio asincrono llamador de servicios
     */
    private static mx.gob.sfp.dgti.omextback.dao.reactivex.OmextMongoDAO omextMongoDao;

    /**
     * El logger
     */
    static final Logger LOGGER = LoggerFactory.getLogger(CumplimientoServiceImpl.class);

    /**
     *
     */
    static final EnumCumplimiento[] ARREGLO_CUMPLIMIENTO =
            new EnumCumplimiento[]{EnumCumplimiento.CUMPLIO, EnumCumplimiento.PENDIENTE, EnumCumplimiento.EXTEMPORANEO};

    /**
     * Observable con cumplimientos necesarios para la obtencion de la info de las graficas
     */
    static final Observable<EnumCumplimiento> CUMPLIMIENTOS_GRAFICA =
            Observable.fromArray(EnumCumplimiento.CUMPLIO, EnumCumplimiento.PENDIENTE, EnumCumplimiento.EXTEMPORANEO);

    /**
     * Observable con cumplimientos necesarios para la obtencion de cumplimiento por instituciones
     */
    static final Observable<EnumCumplimiento> CUMPLIMIENTOS_INSTITUCIONES =
            Observable.fromArray(ARREGLO_CUMPLIMIENTO);

    /**
     * Observable con cumplimientos necesarios para la obtencion de cumplimiento por unidad administrativa
     */
    static final Observable<EnumCumplimiento> CUMPLIMIENTOS_UA =
            Observable.fromArray(EnumCumplimiento.CUMPLIO, EnumCumplimiento.PENDIENTE, EnumCumplimiento.EXTEMPORANEO);

    static final String URL_REPORTES_DESCARGAR_LABEL = "URL_REPORTES_DESCARGAR";

    static final String URL_REPORTES_DESCARGAR =
            System.getenv(URL_REPORTES_DESCARGAR_LABEL) != null ?
                    System.getenv(URL_REPORTES_DESCARGAR_LABEL).concat("?a=") : "";

    /**
     * Constructor
     *
     * @param vertx: el vertx
     */
    public CumplimientoServiceImpl(Vertx vertx) {

        LOGGER.info("=== ConsultaApiImpl()");
        LOGGER.info("=== Se levanta el servicio de CumplimientoServiceImpl()");

        LOGGER.info("=== Prefix de descarga: " + URL_REPORTES_DESCARGAR);
        consultaHttpAS = new ConsultaHttpASImpl(vertx);
        creadorQueryAS = new CreadorQueryASImpl();

        convertidorRespuestaAS = new ConvertidorRespuestaASImpl();

        omextMongoDao = OmextMongoDAO.createProxy(vertx, Proxies.SERVICE_MONGO_CUMPLIMIENTO_OMEXT);

        LOGGER.info("EL PROXY + + " + omextMongoDao);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CumplimientoService obtenerCumplServ(FiltroOmextInputDTO filtro, Handler<AsyncResult<JsonObject>> resultHandler) {

        EnumCumplimiento cumplimiento = filtro.getCumplimiento();
        Integer tamanio = filtro.getTamanio();
        Integer offset = filtro.getOffset();
        Integer collName = filtro.getCollName();
        AtomicReference<Integer> total = new AtomicReference<>(0);

        // Verificar collname
        try {
            consultaHttpAS.validarCollName(collName);
        }catch (SFPException sfpe) {
            resultHandler.handle(Future.failedFuture(sfpe));
            return this;
        }

        if(cumplimiento.equals(EnumCumplimiento.OBLIGADO)) {

            JsonArray pipelineCumplimiento = creadorQueryAS.crearPipelineConsultaServ(
                    filtro, offset, tamanio, cumplimiento,true ,true);

            Single.just(pipelineCumplimiento)
                    .flatMap(pip -> omextMongoDao.rxConsultarCumplServ(pip, collName, cumplimiento, tamanio))
                    .flatMap(respuesta -> {

                        int totalCumplimiento = respuesta.getInteger(EnumMongoDB.TOTAL.getValor());
                        int sizeRespuesta = respuesta.getJsonArray(EnumMongoDB.RESULTADOS.getValor()).size();

                        total.set(totalCumplimiento);

                        AtomicReference<Boolean> segundaConsultaRequerida = new AtomicReference<>(false);
                        int nuevoOffset = 0;
                        int nuevoTamanio = 0;

                        //Si la consulta es igual al tamanio no es necesaria otro

                        JsonArray pipelineNoLocalizados;
                        if(sizeRespuesta == tamanio) {
                            pipelineNoLocalizados =
                                    creadorQueryAS.crearPipelineConsultaServ(
                                            filtro, 0, 1,
                                            EnumCumplimiento.PENDIENTE, true, false);
                        } else {
                            segundaConsultaRequerida.set(true);
                            if (sizeRespuesta > 0) {
                                nuevoOffset = 0;
                            } else {
                                nuevoOffset = offset - totalCumplimiento;
                            }
                            nuevoTamanio = tamanio - sizeRespuesta;
                            pipelineNoLocalizados =
                                    creadorQueryAS.crearPipelineConsultaServ(
                                            filtro, nuevoOffset, nuevoTamanio,
                                            EnumCumplimiento.PENDIENTE, true, false);
                        }

                        return omextMongoDao.rxConsultarCumplServ(
                                    pipelineNoLocalizados, collName, EnumCumplimiento.PENDIENTE, tamanio)
                                    .map(r2 -> {
                                        // Si no se requirio una segunda consulta se pone nulo el resultado
                                        if(! segundaConsultaRequerida.get()) {
                                            r2.put(EnumMongoDB.RESULTADOS.getValor(), new JsonArray());
                                        }
                                        respuesta.getJsonArray(
                                                EnumMongoDB.RESULTADOS.getValor())
                                                .addAll(r2.getJsonArray(EnumMongoDB.RESULTADOS.getValor()));
                                        total.set(total.get() + r2.getInteger(EnumMongoDB.TOTAL.getValor()));

                                        return respuesta;

                                    });


                        })
                    .map(resultados -> {
                        return new JsonObject()
                                .put(EnumGraphQL.PAGINACION.getValor(), new JsonObject()
                                        .put(EnumGraphQL.OFFSET.getValor(), offset)
                                        .put(EnumGraphQL.TAMANIO.getValor(), tamanio)
                                        .put(EnumGraphQL.TOTAL.getValor(), total.get()))
                                .put(EnumGraphQL.SERVIDORES_PUBLICOS.getValor(),
                                        resultados.getJsonArray(EnumMongoDB.RESULTADOS.getValor()));

                    })
                    .subscribe(
                            respuesta -> resultHandler.handle(Future.succeededFuture(respuesta)),
                            error -> {
                                if (error instanceof SFPException) {
                                    SFPException sfpe = (SFPException) error;
                                    resultHandler.handle(Future.failedFuture(sfpe));
                                }
                                resultHandler.handle(Future.failedFuture(error));
                            }
                    );

        } else {
            JsonArray pipeline = creadorQueryAS.crearPipelineConsultaServ(
                    filtro, offset, tamanio, cumplimiento, true, false);
            Single.just(pipeline)
                    .flatMap(pip -> omextMongoDao.rxConsultarCumplServ(pip, collName, cumplimiento, tamanio))
                    .map(resultados -> new JsonObject()
                            .put(EnumGraphQL.PAGINACION.getValor(), new JsonObject()
                                    .put(EnumGraphQL.OFFSET.getValor(), offset)
                                    .put(EnumGraphQL.TAMANIO.getValor(), tamanio)
                                    .put(EnumGraphQL.TOTAL.getValor(), resultados.getInteger(EnumMongoDB.TOTAL.getValor())))
                            .put(EnumGraphQL.SERVIDORES_PUBLICOS.getValor(),
                                    resultados.getJsonArray(EnumMongoDB.RESULTADOS.getValor())))
                    .subscribe(
                            respuesta -> resultHandler.handle(Future.succeededFuture(respuesta)),
                            error -> {
                                if (error instanceof SFPException) {
                                    SFPException sfpe = (SFPException) error;
                                    resultHandler.handle(Future.failedFuture(sfpe));
                                }
                                resultHandler.handle(Future.failedFuture(error));
                            }
                    );
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CumplimientoService obtenerCumplGraficas(JsonObject filtroJson, Handler<AsyncResult<JsonObject>> resultHandler) {

        FiltroOmextInputDTO filtro = new FiltroOmextInputDTO(filtroJson);

        Integer collName = filtro.getCollName();
        AtomicReference<Integer> total = new AtomicReference<>(0);
        JsonArray resultados = new JsonArray();

        // Verificar collname
        try {
            consultaHttpAS.validarCollName(collName);
        }catch (SFPException sfpe) {
            resultHandler.handle(Future.failedFuture(sfpe));
            return this;
        }

        CUMPLIMIENTOS_GRAFICA
                .flatMapSingle(c -> omextMongoDao.rxContarCumpl(
                        creadorQueryAS.crearQueryGraficasConteo(filtro, c), collName, c)
                    .doOnSuccess(r -> {
                        total.set(total.get() + r.getInteger(EnumRespuestaMongo.VALUE.getValor()));
                        resultados.add(r);
                    })
                )
                .toList()
                .map(lista -> new JsonObject()
                        .put(EnumGraphQL.TOTAL.getValor(), total.get())
                        .put(EnumGraphQL.RESULTADO.getValor(), resultados))
                .subscribe(
                        respuesta -> resultHandler.handle(Future.succeededFuture(respuesta)),
                        error -> {
                            if (error instanceof SFPException) {
                                SFPException sfpe = (SFPException) error;
                                resultHandler.handle(Future.failedFuture(sfpe));
                            }
                            resultHandler.handle(Future.failedFuture(error));
                        }
                );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CumplimientoService obtenerCumplPorInst(JsonObject filtroJson, Handler<AsyncResult<JsonObject>> resultHandler) {

        FiltroOmextInputDTO filtro = new FiltroOmextInputDTO(filtroJson);
        Integer collName = filtro.getCollName();

        // Verificar collname
        try {
            consultaHttpAS.validarCollName(collName);
        }catch (SFPException sfpe) {
            resultHandler.handle(Future.failedFuture(sfpe));
            return this;
        }

        AtomicReference<HashMap<String, Integer>> mapaCumplio = new AtomicReference<>(new HashMap<>());
        AtomicReference<HashMap<String, Integer>> mapaPendiente = new AtomicReference<>(new HashMap<>());
        AtomicReference<HashMap<String, Integer>> mapaExt = new AtomicReference<>(new HashMap<>());
        AtomicReference<JsonArray> entes = new AtomicReference<>(new JsonArray());

        if(creadorQueryAS.esConsultaInicio(filtro)) {
            //Si es la consulta con parametros basicos
            consultaHttpAS.filtrarEntesMemoriaPorCollName(creadorQueryAS.crearQueryConsultaEntes(filtro, collName), collName)
                    .doOnSuccess(entes::set)
                    .flatMap(e -> omextMongoDao.rxObtenerPreconteosInicio(collName, filtro.getCondiciones().getAnio()))
                       .doOnSuccess(res -> {
                       convertidorRespuestaAS.extraerPreConteosInicio(res,
                               mapaCumplio.get(), mapaExt.get(), mapaPendiente.get());
                   })
                   .map(x -> convertidorRespuestaAS.formatearConteoPorEntes(
                           entes.get(),
                           mapaCumplio.get(),
                           mapaPendiente.get(),
                           mapaExt.get(),
                           filtro.getCondiciones().getIdEnte()))
                   .map(lista -> new JsonObject()
                           .put(EnumGraphQL.TOTAL.getValor(), lista.size())
                           .put(EnumGraphQL.RESULTADO.getValor(), lista))
                   .subscribe(
                           respuesta -> resultHandler.handle(Future.succeededFuture(respuesta)),
                           error -> {
                               if (error instanceof SFPException) {
                                   SFPException sfpe = (SFPException) error;
                                   resultHandler.handle(Future.failedFuture(sfpe));
                               }
                               resultHandler.handle(Future.failedFuture(error));
                           }
                   );
        } else {
            try {
                JsonArray pipelineCumplExt = creadorQueryAS.crearPipelineCountCumplExtServ(filtro);
                JsonArray pipelinePendientes = creadorQueryAS.crearPipelineAggConteoInst(filtro, EnumCumplimiento.PENDIENTE);

                consultaHttpAS.filtrarEntesMemoriaPorCollName(creadorQueryAS.crearQueryConsultaEntes(filtro, collName), collName)
                        .doOnSuccess(entes::set)
                        .flatMap(e -> omextMongoDao.rxContarCumplPorInst(pipelinePendientes, collName, EnumCumplimiento.PENDIENTE)
                                .doOnSuccess(r -> {
                                    mapaPendiente.set(
                                            convertidorRespuestaAS.extraerConteosPorEntes(
                                                    r.getJsonArray(EnumCumplimiento.PENDIENTE.getValor())));
                                })
                                .flatMap(a -> omextMongoDao.rxContarCumpExtPorInst(pipelineCumplExt, collName))
                                .doOnSuccess(r -> {
                                    convertidorRespuestaAS.extraerConteosPorEntesCumplExt(r, mapaExt.get(), mapaCumplio.get());
                                }))
                        .map(x -> convertidorRespuestaAS.formatearConteoPorEntes(
                                entes.get(),
                                mapaCumplio.get(),
                                mapaPendiente.get(),
                                mapaExt.get(),
                                filtro.getCondiciones().getIdEnte()))
                        .map(lista -> new JsonObject()
                                .put(EnumGraphQL.TOTAL.getValor(), lista.size())
                                .put(EnumGraphQL.RESULTADO.getValor(), lista))
                        .subscribe(
                                respuesta -> resultHandler.handle(Future.succeededFuture(respuesta)),
                                error -> {
                                    if (error instanceof SFPException) {
                                        SFPException sfpe = (SFPException) error;
                                        resultHandler.handle(Future.failedFuture(sfpe));
                                    }
                                    resultHandler.handle(Future.failedFuture(error));
                                }
                        );
            } catch (SFPException sfpe) {
                resultHandler.handle(Future.failedFuture(sfpe));
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CumplimientoService obtenerCumplPorGrupo(JsonObject filtroJson, Handler<AsyncResult<JsonObject>> resultHandler) {

        FiltroOmextInputDTO filtro = new FiltroOmextInputDTO(filtroJson);
        Integer collName = filtro.getCollName();

        // Verificar collname
        try {
            consultaHttpAS.validarCollName(collName);
        }catch (SFPException sfpe) {
            resultHandler.handle(Future.failedFuture(sfpe));
            return this;
        }

        JsonArray pipeline = creadorQueryAS.crearPipelineCumplimientoGrupo(filtro);
        LOGGER.info("el pipeline quedo : " + pipeline);
        Single.just(pipeline)
            .flatMap(p -> omextMongoDao.rxObtenerPreconteosInicioGrupo(pipeline, collName))
            .map(lista -> new JsonObject()
                .put(EnumGraphQL.TOTAL.getValor(), lista.size())
                .put(EnumGraphQL.RESULTADO.getValor(), lista))
                .map(respuesta -> {
                    convertidorRespuestaAS.formatearConteoPorGrupos(respuesta);
                    LOGGER.info("La respuesta final final " + respuesta);
                    return respuesta;
                })
            .subscribe(
                    respuesta -> resultHandler.handle(Future.succeededFuture(respuesta)),
                    error -> {
                        if (error instanceof SFPException) {
                            SFPException sfpe = (SFPException) error;
                            resultHandler.handle(Future.failedFuture(sfpe));
                        }
                        resultHandler.handle(Future.failedFuture(error));
                    }
            );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CumplimientoService obtenerCumplPorUA(JsonObject filtroJson, Handler<AsyncResult<JsonObject>> resultHandler) {

        FiltroOmextInputDTO filtro = new FiltroOmextInputDTO(filtroJson);
        Integer collName = filtro.getCollName();
        // Verificar collname
        try {
            consultaHttpAS.validarCollName(collName);
        }catch (SFPException sfpe) {
            resultHandler.handle(Future.failedFuture(sfpe));
            return this;
        }

        TreeMap<String, String> mapaNombresOrd = new TreeMap<>();
        AtomicReference<HashMap<String, Integer>> mapaCumplio = new AtomicReference<>(new HashMap<>());
        AtomicReference<HashMap<String, Integer>> mapaPendiente = new AtomicReference<>(new HashMap<>());
        AtomicReference<HashMap<String, Integer>> mapaExt = new AtomicReference<>(new HashMap<>());

        CUMPLIMIENTOS_UA
            .flatMapSingle(c -> omextMongoDao.rxContarCumplPorUA(
                    creadorQueryAS.crearQueryAggConteoUA(filtro, c),
                    collName,
                    c)
                    .doOnSuccess(r -> {

                        if (r.getJsonArray(EnumCumplimiento.CUMPLIO.getValor()) != null ) {
                            mapaCumplio.set(
                                    convertidorRespuestaAS.extraerConteosPorUA(
                                            r.getJsonArray(EnumCumplimiento.CUMPLIO.getValor()), mapaNombresOrd));
                        } else if (r.getJsonArray(EnumCumplimiento.PENDIENTE.getValor()) != null ){
                            mapaPendiente.set(
                                    convertidorRespuestaAS.extraerConteosPorUA(
                                            r.getJsonArray(EnumCumplimiento.PENDIENTE.getValor()),mapaNombresOrd));
                        }  else if (r.getJsonArray(EnumCumplimiento.EXTEMPORANEO.getValor()) != null ){
                            mapaExt.set(
                                    convertidorRespuestaAS.extraerConteosPorUA(
                                            r.getJsonArray(EnumCumplimiento.EXTEMPORANEO.getValor()),mapaNombresOrd));
                        }
                    })
            )
            .toList()
            .map(x -> {

                return convertidorRespuestaAS.formatearConteoPorUA(
                        mapaNombresOrd,
                        mapaCumplio.get(),
                        mapaPendiente.get(),
                        mapaExt.get(),
                        filtro.getCondiciones().getIdEnte().get(0),
                        filtro.getCondiciones().getUr()
                );
            })
            .map(lista -> new JsonObject()
                    .put(EnumGraphQL.TOTAL.getValor(), lista.size())
                    .put(EnumGraphQL.RESULTADO.getValor(), lista)
            )
            .subscribe(
                    respuesta -> resultHandler.handle(Future.succeededFuture(respuesta)),
                    error -> {
                        if (error instanceof SFPException) {
                            SFPException sfpe = (SFPException) error;
                            resultHandler.handle(Future.failedFuture(sfpe));
                        }
                        resultHandler.handle(Future.failedFuture(error));
                    }
            );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CumplimientoService obtenerReporteServ(JsonObject filtroJson, Handler<AsyncResult<String>> resultHandler) {

        FiltroOmextInputDTO filtro = new FiltroOmextInputDTO(filtroJson);

        EnumCumplimiento cumplimiento = filtro.getCumplimiento();
        Integer collName = filtro.getCollName();
        // Verificar collname
        try {
            consultaHttpAS.validarCollName(collName);
        }catch (SFPException sfpe) {
            resultHandler.handle(Future.failedFuture(sfpe));
            return this;
        }

        final Observable<EnumCumplimiento> cumplimientos = (cumplimiento.equals(EnumCumplimiento.OBLIGADO)) ?
                CUMPLIMIENTOS_INSTITUCIONES : Observable.fromArray(cumplimiento);

        cumplimientos
                .flatMapSingle(c -> Single.just(creadorQueryAS.crearPipelineReporteServ(filtro, c))
                        .map(r -> new JsonObject()
                                            .put(EnumServicioReportesServidores.COLLNAME.getValor(), collName)
                                            .put(EnumServicioReportesServidores.CUMPLIMIENTO.getValor(), c)
                                            .put(EnumServicioReportesServidores.PIPELINE.getValor(), r)

                        )
                )
                .toList()
                .map(r ->
                {
                    return new JsonObject()
                        .put(EnumServicioReportesServidores.NOMBRE.getValor(), filtro.getNombreReporte())
                        .put(EnumServicioReportesServidores.REPORTES.getValor(), new JsonArray(r));
                }
                ).flatMap(consultaHttpAS::generarReporte)
                .subscribe(
                        miArchivo -> resultHandler.handle(
                                Future.succeededFuture(URL_REPORTES_DESCARGAR.concat(miArchivo))),
                        error -> {
                            if (error instanceof SFPException) {
                                SFPException sfpe = (SFPException) error;
                                resultHandler.handle(Future.failedFuture(sfpe));
                            }
                            resultHandler.handle(Future.failedFuture(error));
                        }
                );
        return this;
    }

    /**
     *
     * @param filtroVistas
     * @param periodo
     * @return
     */
    public FiltroOmextInputDTO cambiarTipoFiltro(FiltroPeriodosVistasInputDTO filtroVistas, PeriodoDTO periodo) {
        FiltroOmextInputDTO filtroCambiado = new FiltroOmextInputDTO();

        CondicionesOmextInputDTO condicionesOmext  =new CondicionesOmextInputDTO();
        condicionesOmext.setTipoDeclaracion(periodo.getTipoDeclaracion());

        if(!periodo.getVistasGeneradas().equals(EnumEstatusPeriodo.SIN_VISTAS)) {
            condicionesOmext.setEnVista(true);
            condicionesOmext.setIdPeriodo(periodo.getIdPeriodo());
        }
        if(periodo.getMes() != null) {
            List<EnumMes> mes = new ArrayList<EnumMes>();
            mes.add(periodo.getMes());
            condicionesOmext.setMes(mes);
        }
        condicionesOmext.setEnVista(false);
        condicionesOmext.setAnio(periodo.getAnio());

        filtroCambiado.setCumplimiento(EnumCumplimiento.OBLIGADO);
        filtroCambiado.setCollName(filtroVistas.getCollName());
        filtroCambiado.setCondiciones(condicionesOmext);

        return filtroCambiado;

    }

    JsonObject crearParametrosServicioImpresion(VistaDTO vista, TextoOficioDTO textoOficio, JsonArray listado) {

        return new JsonObject()
                .put(EnumServicioImpresion.NUMERO_OFICIO.getValor(),vista.getFolio())
                .put(EnumServicioImpresion.FECHA_GENERACION_VISTA.getValor(),vista.getFechaRegistro())
                .put(EnumServicioImpresion.DEPENDENCIA_ENTIDAD.getValor(),vista.getEnte().getNombreEnte())
                .put(EnumServicioImpresion.FECHA_VENCIMIENTO.getValor(),vista.getFechaLimite())
                .put(EnumServicioImpresion.BODY_TEXT_OFICIO.getValor(),textoOficio.getBodyTextOficio())
                .put(EnumServicioImpresion.LOGO_IMAGEN.getValor(),textoOficio.getLogoImagen())
                //TODO CAMBIAR LA FIRMA DEL LISTADO
                .put(EnumServicioImpresion.PUESTO_FIRMANTE.getValor(), "PENDIENTE - OBTENER DATOS")
                .put(EnumServicioImpresion.FIRMA_OFICIO.getValor(), vista.getFirmaOficio().getDatos().getSha())
                .put(EnumServicioImpresion.FIRMA_LISTADO.getValor(), vista.getFirmaOficio().getDatos().getSha())
                .put(EnumServicioImpresion.FIRMANTE_NOMBRE.getValor(), vista.getUsuarioRegistro().getNombre()
                        .concat(" ").concat(vista.getUsuarioRegistro().getPrimerApellido())
                        .concat(" ").concat(vista.getUsuarioRegistro().getSegundoApellido()))
                .put(EnumServicioImpresion.PRIMER_PARRAFO.getValor(), textoOficio.getPrimerParrafo())
                .put(EnumServicioImpresion.SEGUNDO_PARRAFO.getValor(),textoOficio.getSegundoParrafo())
                .put(EnumServicioImpresion.TIPO_DECLARACION.getValor(), vista.getTipoDeclaracion())
                .put(EnumServicioImpresion.ANIO.getValor(), vista.getAnioDeclaracion().toString())
                .put(EnumServicioImpresion.DATA.getValor(), listado);
    }

}