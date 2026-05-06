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
import mx.gob.sfp.dgti.omextback.as.CreadorQueryPeriodosVistasAS;
import mx.gob.sfp.dgti.omextback.as.CreadorQueryUtils;
import mx.gob.sfp.dgti.omextback.as.impl.ConsultaHttpASImpl;
import mx.gob.sfp.dgti.omextback.as.impl.ConvertidorRespuestaASImpl;
import mx.gob.sfp.dgti.omextback.as.impl.CreadorQueryASImpl;
import mx.gob.sfp.dgti.omextback.as.impl.CreadorQueryPeriodosVistasASImpl;
import mx.gob.sfp.dgti.omextback.service.PeriodosService;
import mx.gob.sfp.dgti.omextback.dao.OmextMongoDAO;
import mx.gob.sfp.dgti.omextback.dto.input.*;
import mx.gob.sfp.dgti.omextback.dto.respuestas.EnteExtensionDTO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.PeriodoDTO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.TextoOficioDTO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.VistaDTO;
import mx.gob.sfp.dgti.omextback.exception.SFPException;
import mx.gob.sfp.dgti.omextback.util.constantes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Clase para el servicio VistasServiceImpl
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public class PeriodosServiceImpl implements PeriodosService {

    /**
     * Creador de queries
     */
    private final  CreadorQueryAS creadorQueryAS;

    /**
     * Creador de queries para periodos y vistas
     */
    private final CreadorQueryPeriodosVistasAS creadorQueryPeriodosVistasAS;

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
    static final Logger LOGGER = LoggerFactory.getLogger(PeriodosServiceImpl.class);

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
    public PeriodosServiceImpl(Vertx vertx) {

        LOGGER.info("=== ConsultaApiImpl()");
        LOGGER.info("=== Se levanta el servicio de PeriodosServiceImpl()");

        LOGGER.info("=== Prefix de descarga: " + URL_REPORTES_DESCARGAR);
        consultaHttpAS = new ConsultaHttpASImpl(vertx);
        creadorQueryAS = new CreadorQueryASImpl();

        creadorQueryPeriodosVistasAS = new CreadorQueryPeriodosVistasASImpl();
        convertidorRespuestaAS = new ConvertidorRespuestaASImpl();

        omextMongoDao = OmextMongoDAO.createProxy(vertx, Proxies.SERVICE_MONGO_CUMPLIMIENTO_OMEXT);

        LOGGER.info("EL PROXY + + " + omextMongoDao);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PeriodosService obtenerPeriodos(JsonObject filtroJson, Handler<AsyncResult<JsonObject>> resultHandler) {
        FiltroPeriodosVistasInputDTO filtro = new FiltroPeriodosVistasInputDTO(filtroJson);
        Integer collName = filtro.getCollName();

        JsonArray pipeline = creadorQueryPeriodosVistasAS.crearPipelineConsultaPeriodos(filtro);

        omextMongoDao.rxConsultarPeriodos(pipeline, collName, filtro.getOffset(), filtro.getTamanio())

                .subscribe(
                        respuesta -> {
                            resultHandler.handle(Future.succeededFuture(respuesta));
                        },
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
    public PeriodosService obtenerInstPeriodo(JsonObject filtroJson, Handler<AsyncResult<JsonObject>> resultHandler) {
        FiltroPeriodosVistasInputDTO filtro = new FiltroPeriodosVistasInputDTO(filtroJson);

        AtomicReference<FiltroOmextInputDTO> filtroCumpl = new AtomicReference<>(new FiltroOmextInputDTO());
        LOGGER.info("=== ConsultaApi obtenerInstPeriodo ");
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
        AtomicReference<HashMap<String, Integer>> mapaPendienteVistaOmisos = new AtomicReference<>(new HashMap<>());
        AtomicReference<HashMap<String, Integer>> mapaExt = new AtomicReference<>(new HashMap<>());
        AtomicReference<HashMap<String, String>> mapaExtensiones = new AtomicReference<>(new HashMap<>());
        AtomicReference<JsonArray> entes = new AtomicReference<>(new JsonArray());
        AtomicReference<JsonObject> periodoJson = new AtomicReference<>(new JsonObject());
        AtomicReference<JsonArray> pipelineVistasOmisos = new AtomicReference<>(null);
        AtomicReference<HashMap<String, EnumEstatusVista>> estVistaOmisos = new AtomicReference<>(new HashMap<>());
        AtomicReference<HashMap<String, EnumEstatusVista>> estVistaExt = new AtomicReference<>(new HashMap<>());
        AtomicReference<String> fechaLimiteGeneral = new AtomicReference<>();

        JsonArray pipeline = creadorQueryPeriodosVistasAS.crearPipelineConsultaPeriodos(filtro);

        // 1 .- Checar informacion del periodo, importante los excepciones de instituciones, si se indica que para el
        // periodo ya se generaron vistas de omisos en una bandera, agregar la condicion de la enPeriodo=id del periodo y enVista=true
        // de lo contrario enVista no debe de existir en las condiciones de servidores
        omextMongoDao.rxConsultarPeriodos(pipeline, collName, 0 , 1)
            .flatMap(p -> {
                if(p != null && p.getJsonObject(EnumGraphQL.PAGINACION.getValor()).getInteger(EnumGraphQL.TOTAL.getValor()) == 1) {
                    //obtener el periodo
                    PeriodoDTO periodo = new PeriodoDTO(p.getJsonArray(EnumGraphQL.PERIODOS.getValor()).getJsonObject(0));
                    fechaLimiteGeneral.set(periodo.getFechaLimite());
                    periodoJson.set(periodo.toJson());

                    periodo.getExtensiones().forEach(ext -> {
                         mapaExtensiones.get().put(ext.getIdEnte(), ext.getFechaLimite());
                    });
                    //Se crea un filtro para llamar a cumplimiento y no localizadosr
                    filtroCumpl.set(cambiarTipoFiltro(filtro, periodo));

                    // Se cream los pipelines para las consultas necesarias -- se prodria hacer con los preconteos
                    JsonArray pipelineCumplExt = creadorQueryAS.crearPipelineCountCumplExtServ(filtroCumpl.get());
                    JsonArray pipelinePendientes = creadorQueryAS.crearPipelineAggConteoInst(filtroCumpl.get(), EnumCumplimiento.PENDIENTE);


                    // Se consulta en vistas en caso de que ya tenga
                    if(! periodo.getVistasGeneradas().equals(EnumEstatusPeriodo.SIN_VISTAS)) {
                        FiltroPeriodosVistasInputDTO filtroVistasOmisos = new FiltroPeriodosVistasInputDTO();
                        CondicionesPeriodosVistasInputDTO condV = new CondicionesPeriodosVistasInputDTO();
                        condV.setIdPeriodo(periodo.getIdPeriodo());
                        condV.setTipoDeclaracion(periodo.getTipoDeclaracion());
                        condV.setTipoIncumplimiento(EnumTipoIncumplimiento.OMISO);
                        filtroVistasOmisos.setCondiciones(condV);
                        LOGGER.info("== El nuevo filtro de vistas de omisos -> " + filtroVistasOmisos);
                        // Se crea pipeline de vistas
                        pipelineVistasOmisos.set(creadorQueryPeriodosVistasAS.crearPipelineVista(filtroVistasOmisos));
                    }
                    // 2.- Hacer la consulta de conteo de vistas con las condiciones puestas
                    return
                            consultaHttpAS.filtrarEntesMemoriaPorCollName(
                                    creadorQueryAS.crearQueryConsultaEntes(filtroCumpl.get(), collName),collName)
                            .doOnSuccess(entes::set)
                            .flatMap(e -> omextMongoDao.rxContarCumplPorInst(pipelinePendientes, collName, EnumCumplimiento.PENDIENTE)
                                    .doOnSuccess(r -> {
                                        mapaPendiente.set(
                                                convertidorRespuestaAS.extraerConteosPorEntes(
                                                        r.getJsonArray(EnumCumplimiento.PENDIENTE.getValor())));
                                    })
                            .flatMap(a -> omextMongoDao.rxContarCumpExtPorInst(pipelineCumplExt, collName))
                            .doOnSuccess(r -> {
                                convertidorRespuestaAS.extraerConteosPorEntesCumplExt(r, mapaCumplio.get(), mapaExt.get());
                            }))

                            // En caso de que ya exista la vista por institucion, sobreescribir conteo de omisos con el de la vista
                            .flatMap(x -> {
                                if(pipelineVistasOmisos.get() != null) {
                                    return omextMongoDao.rxConsultarVistas(pipelineVistasOmisos.get(), collName)
                                            .flatMap(r -> {

                                                    //HashMap<String, Integer> mapa = new HashMap<>();
                                                    if (r.getJsonArray(EnumGraphQL.VISTAS.getValor()) != null) {
                                                        for (Object o : r.getJsonArray(EnumGraphQL.VISTAS.getValor())) {
                                                            VistaDTO v = new VistaDTO(((JsonObject) o));

                                                            if(v.getVistaGenerada().equals(EnumEstatusVista.GENERADA)) {
                                                                mapaPendiente.get().put(v.getIdEnte(), v.getTotalServidores());
                                                            }
                                                            estVistaOmisos.get().put(v.getIdEnte(), v.getVistaGenerada());
                                                        }
                                                    }
                                                    //mapaPendiente.set(mapa);
                                                return Single.just("");
                                            });
                                } else {
                                    return Single.just("");
                                }

                            })
                            .map(x -> convertidorRespuestaAS.formatearConteoPorEntesPeriodo(
                                    entes.get(),
                                    mapaCumplio.get(),
                                    mapaPendiente.get(),
                                    mapaExt.get(),
                                    filtroCumpl.get().getCondiciones().getIdEnte(),
                                    mapaExtensiones.get(),
                                    fechaLimiteGeneral.get(),
                                    estVistaOmisos.get(),
                                    estVistaExt.get()
                                    ))
                            .map(lista -> new JsonObject()
                                    .put(EnumGraphQL.TOTAL.getValor(), lista.size())
                                    .put(EnumGraphQL.INSTITUCIONES.getValor(), lista)
                                    .put(EnumGraphQL.PERIODO.getValor(), periodoJson.get()));
                } else {
                    return Single.error(new SFPException(EnumError.ERROR_PERIODO_NO_EXISTE));
                }})
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
    public PeriodosService extenderPeriodoInstitucion(JsonObject extensionJson, Handler<AsyncResult<Boolean>> resultHandler) {
        ExtensionPeriodoInstitucionInputDTO extension = new ExtensionPeriodoInstitucionInputDTO(extensionJson);
        // Verificar collname
        try {
            consultaHttpAS.validarCollName(extension.getCollName());
        }catch (SFPException sfpe) {
            resultHandler.handle(Future.failedFuture(sfpe));
            return this;
        }
        AtomicReference<JsonArray> extInstituciones = new AtomicReference<>(new JsonArray());


        Single.just(extension.getIdPeriodo())
                .map(id -> creadorQueryPeriodosVistasAS.crearPipelinePeriodoPorId(id))
                .flatMap(pipeline -> omextMongoDao.rxConsultarPeriodoSimple(pipeline, extension.getCollName()))
                .map(periodoJson -> {
                    if(periodoJson != null) {
                        PeriodoDTO periodo = new PeriodoDTO(periodoJson);

                        if(periodo.getVistasGeneradas().equals(EnumEstatusVista.NO_GENERADA)) {
                            JsonObject fechaJson = CreadorQueryUtils.crearFechaMongo(extension.getFechaLimite());
                            if(fechaJson == null) {
                                throw new SFPException(EnumError.ERROR_PARAMETROS_INCORRECTOS);
                            }
                            for (EnteExtensionDTO ext : periodo.getExtensiones()) {
                                if(!ext.getIdEnte().equals(extension.getIdEnte()))  {
                                    extInstituciones.get().add(new JsonObject()
                                            .put(EnumMongoDB.ID_ENTE.getValor() , ext.getIdEnte())
                                            .put(EnumMongoDB.FECHA_LIMITE.getValor(),
                                                    CreadorQueryUtils.crearFechaMongo(ext.getFechaLimite())));
                                }
                            }
                            if (extension.getFechaLimite() != null) {
                                extInstituciones.get().add(new JsonObject()
                                        .put(EnumMongoDB.ID_ENTE.getValor() , extension.getIdEnte())
                                        .put(EnumMongoDB.FECHA_LIMITE.getValor(), fechaJson));
                            }
                            return periodo;
                        } else {
                            throw new SFPException(EnumError.ERROR_VISTA_PERIODO_YA_GENERADAS);
                        }
                    } else {
                        throw new SFPException(EnumError.ERROR_PERIODO_NO_EXISTE);
                    }
                })
                .flatMap(p -> omextMongoDao.rxActualizarFechaExtensionesPeriodo(
                        extension.getCollName(),
                        p.getIdPeriodo(),
                        extInstituciones.get()
                        ))
                .subscribe(
                        r -> resultHandler.handle(Future.succeededFuture(r)),
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
    public PeriodosService extenderPeriodo(JsonObject extensionJson, Handler<AsyncResult<Boolean>> resultHandler) {

        ExtensionPeriodoInputDTO extension = new ExtensionPeriodoInputDTO(extensionJson);
        // Verificar collname
        try {
            consultaHttpAS.validarCollName(extension.getCollName());
        }catch (SFPException sfpe) {
            resultHandler.handle(Future.failedFuture(sfpe));
            return this;
        }

        Single.just(extension.getIdPeriodo())
                .map(creadorQueryPeriodosVistasAS::crearPipelinePeriodoPorId)
                .flatMap(pipeline -> omextMongoDao.rxConsultarPeriodoSimple(pipeline, extension.getCollName()))
                .map(periodoJson -> {
                   if(periodoJson != null) {
                        PeriodoDTO periodo = new PeriodoDTO(periodoJson);
                        if(periodo.getVistasGeneradas().equals(EnumEstatusVista.NO_GENERADA)) {
                           return periodo;
                        } else {
                            throw new SFPException(EnumError.ERROR_VISTA_PERIODO_YA_GENERADAS);
                        }
                   } else {
                       throw new SFPException(EnumError.ERROR_PERIODO_NO_EXISTE);
                   }
                })
                .flatMap(p -> omextMongoDao.rxActualizarFechaPeriodo(extension.getCollName(), p.getIdPeriodo(),
                        extension.getFechaLimite()))
                .subscribe(
                        r -> resultHandler.handle(Future.succeededFuture(r)),
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

    /**0
     * {@inheritDoc}
     */
    @Override
    public PeriodosService guardarPeriodo(JsonObject periodoJson, Handler<AsyncResult<String>> resultHandler) {

        PeriodoInputDTO periodo = new PeriodoInputDTO(periodoJson);
        // Verificar collname
        try {
            consultaHttpAS.validarCollName(periodo.getCollName());
        }catch (SFPException sfpe) {
            resultHandler.handle(Future.failedFuture(sfpe));
            return this;
        }

        JsonArray pipelineUltimoPeriodo = creadorQueryPeriodosVistasAS.crearPipelineUltimoPeriodo(periodo);
        omextMongoDao.rxConsultarPeriodoSimple(pipelineUltimoPeriodo, periodo.getCollName())
                .flatMap(p -> {
                    String version = "";
                    if(p != null) {
                        String idPeriodo = p.getString(EnumMongoDB.ID.getValor());
                        String[] partes = idPeriodo.split("_");
                        if(partes.length > 1) {
                            Integer count = 0;
                            try {
                                count = Integer.parseInt(partes[1])+ 1;
                            } catch (Exception e) {
                                Random rand = new Random();
                                count = rand.nextInt(100);
                             }
                            version = "_" + count;
                        } else {
                            version = "_1";
                        }
                    }
                    return Single.just(version);
                })
                .map(v -> creadorQueryPeriodosVistasAS.crearNuevoPeriodo(periodo, v))
                .flatMap(nuevoPeriodo -> omextMongoDao.rxGuardarPeriodo(nuevoPeriodo, periodo.getCollName()))
                .subscribe(
                        idNuevo -> resultHandler.handle(Future.succeededFuture(idNuevo)),
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