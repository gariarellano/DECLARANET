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
import mx.gob.sfp.dgti.omextback.as.impl.ConsultaHttpASImpl;
import mx.gob.sfp.dgti.omextback.as.impl.ConvertidorRespuestaASImpl;
import mx.gob.sfp.dgti.omextback.as.impl.CreadorQueryASImpl;
import mx.gob.sfp.dgti.omextback.as.impl.CreadorQueryPeriodosVistasASImpl;
import mx.gob.sfp.dgti.omextback.service.VistasService;
import mx.gob.sfp.dgti.omextback.dao.OmextMongoDAO;
import mx.gob.sfp.dgti.omextback.dto.input.*;
import mx.gob.sfp.dgti.omextback.dto.respuestas.FirmanteDTO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.PeriodoDTO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.TextoOficioDTO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.VistaDTO;
import mx.gob.sfp.dgti.omextback.exception.SFPException;
import mx.gob.sfp.dgti.omextback.util.constantes.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Clase para el servicio VistasServiceImpl
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public class VistasServiceImpl implements VistasService {

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
    static final Logger LOGGER = LoggerFactory.getLogger(VistasServiceImpl.class);

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
    public VistasServiceImpl(Vertx vertx) {

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
    public VistasService obtenerVistas(JsonObject filtroJson, Handler<AsyncResult<JsonObject>> resultHandler) {
        FiltroPeriodosVistasInputDTO filtro = new FiltroPeriodosVistasInputDTO(filtroJson);
        Integer collName = filtro.getCollName();
        // Verificar collname
        try {
            consultaHttpAS.validarCollName(collName);
        }catch (SFPException sfpe) {
            resultHandler.handle(Future.failedFuture(sfpe));
            return this;
        }

        AtomicReference<JsonObject> periodoJson = new AtomicReference<>(new JsonObject());

        //Exclusivamente para cuando existan las vistas
        LOGGER.info("Filtro PERIODO + "  + filtro);

        JsonArray pipelinePeriodo = creadorQueryPeriodosVistasAS.crearPipelineConsultaPeriodos(filtro);
        JsonArray pipelineVistas = creadorQueryPeriodosVistasAS.crearPipelineVista(filtro);

        omextMongoDao.rxConsultarPeriodos(pipelinePeriodo, collName, 0, 1)
                .flatMap(p -> {
                    LOGGER.info("=== El valor de p " + p);
                    if(p != null
                            && p.getJsonObject(EnumGraphQL.PAGINACION.getValor()) != null
                            && p.getJsonObject(EnumGraphQL.PAGINACION.getValor()).getInteger(EnumGraphQL.TOTAL.getValor()) == 1) {
                        periodoJson.set(p.getJsonArray(EnumGraphQL.PERIODOS.getValor()).getJsonObject(0));

                        LOGGER.info("El periodo Json es : " + periodoJson);
                        return omextMongoDao.rxConsultarVistas(pipelineVistas, collName)
                                .flatMap(r -> {
                                    // Pasame a convertidorRespuestaASimpl
                                    LOGGER.info("==== RespuestaVistas -> " + r);
                                    LOGGER.info("Despues de iterar ver si camibo ->>> " + r.getJsonArray("vistas"));

                                    r.put(EnumGraphQL.PERIODO.getValor(), periodoJson.get());


                                    LOGGER.info("lA RESPUESTA DE R me esta dando: " + r);
                                    return Single.just(r);
                                });

                    } else {
                        return Single.error(new SFPException(EnumError.ERROR_PERIODO_NO_EXISTE));
                    }
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
    public VistasService firmarVista(JsonObject paramsJson, Handler<AsyncResult<Boolean>> resultHandler) {

        // 1.- Se consulta unicamente cuando se ha creado la vista, en la tabla de vista servidores
        ParamsProcesoVistaInputDTO params = new ParamsProcesoVistaInputDTO(paramsJson);

        final Integer collName = params.getCollName();
        // Verificar collname
        try {
            consultaHttpAS.validarCollName(collName);
        }catch (SFPException sfpe) {
            resultHandler.handle(Future.failedFuture(sfpe));
            return this;
        }

        FiltroPeriodosVistasInputDTO filtro = new FiltroPeriodosVistasInputDTO();
        CondicionesPeriodosVistasInputDTO condiciones =  new CondicionesPeriodosVistasInputDTO();
        condiciones.setFolio(params.getFolio());
        filtro.setCondiciones(condiciones);
        AtomicReference<VistaDTO> vista = new AtomicReference<>();
        JsonArray pipeline = creadorQueryPeriodosVistasAS.crearPipelineVistaPorFolio(filtro);

        Single.just(params)
                .flatMap(p -> omextMongoDao.rxObtenerVistaPorFolio(params.getCollName(), pipeline))
                .map(v -> {
                    if(v!= null) {
                        vista.set(new VistaDTO(v));
                        if (vista.get().getVistaGenerada().equals(EnumEstatusVista.GENERADA_SIN_FIRMAS)) {
                          return vista.get();
                        } else {
                            throw new SFPException(EnumError.ERROR_NO_ES_POSIBLE_FIRMAR_VISTA);
                        }
                    } else {
                        throw new SFPException(EnumError.ERROR_VISTA_NO_EXISTE);
                    }
                })
                .flatMap(v -> consultaHttpAS.agregarFirmaAutomatizadaAVista(v, false))
                .flatMap(v ->omextMongoDao.rxGuardarFirmaVista(
                        collName,
                        EnumMongoDB.FIRMA_OFICIO.getValor(),
                        v.getString(EnumMongoDB.FOLIO.getValor()),
                        v.getJsonObject(EnumMongoDB.FIRMA_OFICIO.getValor())))

                .flatMap(x -> consultaHttpAS.agregarFirmaAutomatizadaAVista(vista.get(), true))
                .flatMap(v ->omextMongoDao.rxGuardarFirmaVista(
                        collName,
                        EnumMongoDB.FIRMA_LISTADO.getValor(),
                        v.getString(EnumMongoDB.FOLIO.getValor()),
                        v.getJsonObject(EnumMongoDB.FIRMA_LISTADO.getValor())))
                // Se guarda estatus de la vista
                .flatMap(v -> omextMongoDao.rxGuardarEstatusVista(collName, params.getFolio(), EnumEstatusVista.GENERADA))
                .flatMap(x -> omextMongoDao.rxTodasVistasDelPeriodoGeneradas(vista.get().getIdPeriodo(), collName))
                .flatMap(vistasListas -> {
                    if(vistasListas) {
                        // TODO Aqui cambia el generada
                        return omextMongoDao.rxGuardarEstatusVistasEnPeriodo(collName, vista.get().getIdPeriodo(), EnumEstatusPeriodo.CON_VISTAS);
                    } else {
                        return Single.just(true);
                    }
                })
                .subscribe(respuesta -> resultHandler.handle(Future.succeededFuture(respuesta)),
                        error -> {
                            if (error instanceof SFPException) {
                                SFPException sfpe = (SFPException) error;
                                resultHandler.handle(Future.failedFuture(sfpe));
                            }
                            resultHandler.handle(Future.failedFuture(error));
                        });

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VistasService obtenerServidoresVista(JsonObject filtroJson, Handler<AsyncResult<JsonObject>> resultHandler) {
        FiltroPeriodosVistasInputDTO filtro = new FiltroPeriodosVistasInputDTO(filtroJson);
        // 1.- Se consulta unicamente cuando se ha creado la vista, en la tabla de vista servidores
        Integer collName = filtro.getCollName();
        // Verificar collname
        try {
            consultaHttpAS.validarCollName(collName);
        }catch (SFPException sfpe) {
            resultHandler.handle(Future.failedFuture(sfpe));
            return this;
        }
        Integer tamanio = filtro.getTamanio();
        Integer offset = filtro.getOffset();

        JsonArray pipelineServidoresVista = creadorQueryPeriodosVistasAS.crearPipelineServidoresVista(filtro);

        Single.just(collName)
                .flatMap(f -> omextMongoDao.rxConsultarServidoresVista(pipelineServidoresVista, offset, tamanio, collName))
                .subscribe(respuesta -> resultHandler.handle(Future.succeededFuture(respuesta)),
                        error -> {
                            if (error instanceof SFPException) {
                                SFPException sfpe = (SFPException) error;
                                resultHandler.handle(Future.failedFuture(sfpe));
                            }
                            resultHandler.handle(Future.failedFuture(error));
                        });

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VistasService generarImpresionServidoresVista(JsonObject filtroJson, Handler<AsyncResult<String>> resultHandler) {
        FiltroPeriodosVistasInputDTO filtro = new FiltroPeriodosVistasInputDTO(filtroJson);
        // 1.- Se consulta unicamente cuando se ha creado la vista, en la tabla de vista servidores
        Integer collName = filtro.getCollName();
        // Verificar collname
        try {
            consultaHttpAS.validarCollName(collName);
        }catch (SFPException sfpe) {
            resultHandler.handle(Future.failedFuture(sfpe));
            return this;
        }
        AtomicReference<VistaDTO> vista = new AtomicReference<>();
        AtomicReference<TextoOficioDTO> textoOficio = new AtomicReference<>();
        AtomicReference<Integer> totalServidores = new AtomicReference<>(2000);
        JsonArray pipeline = creadorQueryPeriodosVistasAS.crearPipelineVistaPorFolio(filtro);

        Single.just(pipeline)
                .flatMap(pip -> omextMongoDao.rxObtenerVistaPorFolio(collName, pip))
                .map(v -> {
                    if(v != null) {
                        VistaDTO vistaDto = new VistaDTO(v);

                        if(vistaDto.getVistaGenerada().equals(EnumEstatusVista.GENERADA)) {
                            if(vistaDto.getTotalServidores()!= null) {
                                totalServidores.set(vistaDto.getTotalServidores());
                            }
                            vista.set(vistaDto);
                            return vistaDto;
                        } else {
                            throw new SFPException(EnumError.ERROR_VISTA_NO_GENERADA);
                        }
                    } else {
                        throw new SFPException(EnumError.ERROR_VISTA_NO_EXISTE);
                    }
                })
                .flatMap(v -> omextMongoDao.rxObtenerTextoOficioActualPorId(v.getIdTextoOficio(), collName))
                .map(to -> {
                    textoOficio.set(new TextoOficioDTO(to));
                    return true;
                })
                .map(x -> creadorQueryPeriodosVistasAS.crearPipelineServidoresVistaImpresion(filtro))
                .flatMap(pip -> omextMongoDao.rxConsultarServidoresVistaImpresion(pip, collName, totalServidores.get()))
                .map(lis -> crearParametrosServicioImpresion(vista.get(), textoOficio.get(), lis))
                .flatMap(params -> consultaHttpAS.generarImpresionVista(params))
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
                        });
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VistasService generarVistasOmisos(JsonObject datosParaVistasJson, Handler<AsyncResult<Boolean>> resultHandler) {
        VistasOmisosInputDTO vistasOmisos = new VistasOmisosInputDTO(datosParaVistasJson);

        //periodo con el que se trabaja para generar las vistas
        AtomicReference<PeriodoDTO> periodo = new AtomicReference<>(new PeriodoDTO());
        // Datos de la firma
        AtomicReference<FirmanteDTO> datosUsuarioFirma = new AtomicReference<>(new FirmanteDTO());
        // Informacion del oficio
        AtomicReference<TextoOficioDTO> textoOficio = new AtomicReference<>(new TextoOficioDTO());
        // Mapa de idEnte con la informacion del con
        AtomicReference<HashMap<String, JsonObject>> instPendientes = new AtomicReference<>(new HashMap<>());
        // Lista de entes con vistas ya generadas
        AtomicReference<HashSet<String>> instExistentes = new AtomicReference<>(new HashSet<>());

        AtomicReference<Integer> posicionFolio = new AtomicReference<>(0);

        Integer collName = vistasOmisos.getCollName();
        // Verificar collname
        try {
            consultaHttpAS.validarCollName(collName);
        } catch (SFPException sfpe) {
            resultHandler.handle(Future.failedFuture(sfpe));
            return this;
        }

        FiltroPeriodosVistasInputDTO filtroPeriodo = new FiltroPeriodosVistasInputDTO();
        CondicionesPeriodosVistasInputDTO condicionesPeriodo = new CondicionesPeriodosVistasInputDTO();
        condicionesPeriodo.setIdPeriodo(vistasOmisos.getIdPeriodo());
        filtroPeriodo.setCondiciones(condicionesPeriodo);

        JsonArray pipelinePeriodo = creadorQueryPeriodosVistasAS.crearPipelineConsultaPeriodos(filtroPeriodo);

        // 1  Obtener el periodo asociado para la generacion de las vistas
        Single.just(pipelinePeriodo)
                // Obtener el periodo
                .flatMap(pip -> omextMongoDao.rxConsultarPeriodos(pip, collName, 0, 1))
                .flatMap(per -> {
                    if (per != null && per.getJsonObject(EnumGraphQL.PAGINACION.getValor()).getInteger(EnumGraphQL.TOTAL.getValor()) == 1) {

                        periodo.set(new PeriodoDTO(per.getJsonArray(EnumGraphQL.PERIODOS.getValor()).getJsonObject(0)));
                        LOGGER.info("El periodo queda de la siguiente forma -> " + periodo.get());
                        // 2.- Validar que se pueda generar la vista dependiendo de las fechas limites de lo contrario
                        // rechazar proceso
//                        if (!periodo.get().getVistasGeneradas().equals(EnumEstatusVista.NO_GENERADA)) {
//                            return Single.error(new SFPException(EnumError.ERROR_VISTA_PERIODO_YA_GENERADAS));
//                        }
                        //TODO revision de las condiciones
                        if (!periodo.get().getVenceFechaLimite()) {
                            return Single.error(new SFPException(EnumError.ERROR_FECHA_LIMITE_NO_SUPERADA));
                        }
                        return Single.just(Boolean.TRUE);
                    } else {
                        return Single.error(new SFPException(EnumError.ERROR_PERIODO_NO_EXISTE));
                    }
                })

                // Obtener instituciones que ya tienen generada una vista
                .flatMap(x -> omextMongoDao.rxConsultarInstitucionesVistaPorPeriodo(periodo.get().getIdPeriodo(), collName))
                    .doOnSuccess(l -> instExistentes.set(new HashSet<>(l)))

                // 3.1 Crear un query a partir de la info del periodo de los servidores a agregar en vista
                .flatMap(x -> {
                    FiltroOmextInputDTO filtroInstPend = new FiltroOmextInputDTO();
                    CondicionesOmextInputDTO condiciones = new CondicionesOmextInputDTO();
                    condiciones.setAnio(periodo.get().getAnio());
                    // TODO condiciones de mes
                    if(!periodo.get().getTipoDeclaracion().equals(EnumTipoDeclaracion.MODIFICACION)) {
                        List<EnumMes> mes = new ArrayList<>();
                        mes.add(periodo.get().getMes());
                        condiciones.setMes(mes);
                    }
                    if (vistasOmisos.getInstituciones() != null && !vistasOmisos.getInstituciones().isEmpty()) {
                        condiciones.setIdEnte(vistasOmisos.getInstituciones());
                    }

                    condiciones.setTipoDeclaracion(periodo.get().getTipoDeclaracion());
                    condiciones.setEnVista(Boolean.FALSE);
                    filtroInstPend.setCondiciones(condiciones);
                    filtroInstPend.setCollName(vistasOmisos.getCollName());
                    JsonArray pipelineInstPendientes =
                            creadorQueryAS.crearPipelineAggConteoInst(filtroInstPend, EnumCumplimiento.PENDIENTE);
                    return Single.just(pipelineInstPendientes);
                })
                // 3.2  Conteo o consulta de instituciones con omisos para saber a cuáles se les creará vista
                .flatMap(pip -> omextMongoDao.rxContarCumplPorInst(pip, collName, EnumCumplimiento.PENDIENTE))
                .flatMap(conteos -> {
                    LOGGER.info("CONTEOS : " + conteos);

                    conteos.getJsonArray(EnumCumplimiento.PENDIENTE.getValor()).forEach(c -> {
                        JsonObject o = (JsonObject) c;
                        if(consultaHttpAS.mapaEntesPorId.containsKey(o.getString(EnumMongoDB.ID.getValor()))) {
                            instPendientes.get().put(o.getString(EnumMongoDB.ID.getValor()),
                                    consultaHttpAS.mapaEntesPorId.get(o.getString(EnumMongoDB.ID.getValor())));
                        }
                    });
                    LOGGER.info("=== El resultado de conteo queda -> " + instPendientes.get());
                    // Remover instituciones existentes para que no se vuelva a generar
                    LOGGER.info("Instituciones pendientes antes " + instPendientes.get());
                    instExistentes.get().stream().forEach(exis -> instPendientes.get().remove(exis));
                    LOGGER.info("Instituciones pendientes despues " + instPendientes.get());
                    
                    // Si no hay registros pendientes se corta flujo porque no hay registros para generar las vistas
                    if(instPendientes.get() == null || instPendientes.get().size() == 0) {
                        return Single.error(new SFPException(EnumError.ERROR_NO_REGISTROS_PARA_GENERAR_VISTA));
                    }
                    return Single.just(conteos);
                })

                // 4  Obtener texto del oficio de base de datos
                .flatMap(x -> omextMongoDao.rxObtenerTextoOficioActual(collName))
                .doOnSuccess(to ->  {
                    textoOficio.set(new TextoOficioDTO(to));
                })

                // 5  Consultar informacion de firmante del collname
                .flatMap(x -> omextMongoDao.rxObtenerDatosUsuarioFirma(collName))
                .flatMap(datosJson -> {
                    datosUsuarioFirma.set(new FirmanteDTO(datosJson));
                    return Single.just(new FirmanteDTO(datosJson));
                })

                // 6.- Obtener posicion del conteo para la generacion del folio que va numerado, como sequencias
                .flatMap(x-> omextMongoDao.rxConsumirActualizarCountVista(
                        collName, instPendientes.get().size(),
                        periodo.get().getTipoDeclaracion(),
                        periodo.get().getAnio(),
                        periodo.get().getMes()))
                .doOnSuccess(
                        pos -> posicionFolio.set(pos)
                )
                // 7  Setear valor EN_PROCESO al periodo para que no se le puedan generar más vistas
                .flatMap(x-> omextMongoDao.rxGuardarEstatusVistasEnPeriodo(collName,
                        periodo.get().getIdPeriodo(),
                        EnumEstatusPeriodo.VISTAS_EN_PROCESO )
                )

                // 8.1  Iterar cada institucion seteandole la informacion de firmante, luego se convierte a observable y
                // se manda a guardar cada vista
                .flatMapObservable(x -> creadorQueryPeriodosVistasAS.obtenerVistasNuevasOmisos(
                        vistasOmisos.getRemitente(),
                        periodo.get(),
                        datosUsuarioFirma.get(),
                        instPendientes.get(),
                        textoOficio.get(),
                        posicionFolio,
                        vistasOmisos.getUsuarioRegistro()))

                    .map(VistaDTO::toJson)
                    .flatMapSingle(
                            vistaNuevaJson -> omextMongoDao.rxGuardarVista(vistaNuevaJson, collName)
                    )
                .toList()

                // 9  Se manda a llamar al servicio que generara las vistas.
                .flatMap(l -> consultaHttpAS.generarServidoresVistas(collName, l, periodo.get().getIdPeriodo()))


        .subscribe(
                respuesta -> resultHandler.handle(Future.succeededFuture(respuesta)),
                error -> {
                    LOGGER.info("=== Hubo un error en todo el proceso. " + error);
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
    public VistasService guardarPeriodo(JsonObject periodoJson, Handler<AsyncResult<String>> resultHandler) {

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