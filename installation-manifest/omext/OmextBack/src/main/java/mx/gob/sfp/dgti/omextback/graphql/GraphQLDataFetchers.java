/**
 * @(#)GraphQLDataFetchers.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.graphql;

import io.reactivex.Single;
import io.vertx.core.Future;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.serviceproxy.ServiceException;
import mx.gob.sfp.dgti.omextback.service.CumplimientoService;
import mx.gob.sfp.dgti.omextback.service.PeriodosService;
import mx.gob.sfp.dgti.omextback.service.VistasService;
import mx.gob.sfp.dgti.omextback.dto.input.FiltroOmextInputDTO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.*;
import mx.gob.sfp.dgti.omextback.exception.SFPException;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumError;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumEtiquetas;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumGraphQL;
import mx.gob.sfp.dgti.omextback.util.constantes.Proxies;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * GraphQLDataFetchers con metodos que resuelveran los queries y mutations
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public class GraphQLDataFetchers {

    /**
     * El logger
     */
	static final Logger LOGGER = LoggerFactory.getLogger(GraphQLDataFetchers.class);

    /**
     * Servicio asincrono llamador de servicios
     */
    //private static mx.gob.sfp.dgti.omextback.consultaapi.reactivex.ConsultaApi consultaApi;

    /**
     * Servicio asincrono llamador de servicios
     */
    private static mx.gob.sfp.dgti.omextback.service.reactivex.CumplimientoService cumplimientoService;


    /**
     * Servicio asincrono llamador de servicios
     */
    private static mx.gob.sfp.dgti.omextback.service.reactivex.PeriodosService periodosService;


    /**
     * Servicio asincrono llamador de servicios
     */
    private static mx.gob.sfp.dgti.omextback.service.reactivex.VistasService vistasService;

    /**
     * Inicializa el DataFetcher
     *
     * @param vertx
     * @return
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    static GraphQLDataFetchers init(Vertx vertx) {
        LOGGER.info(EnumEtiquetas.INICIA_GRAPHQL.getNombre());

        //consultaApi = ConsultaApi
        //        .createProxy(vertx, Proxies.SERVICE_ADDRESS_LLAMADOR);

        periodosService = PeriodosService
                .createProxy(vertx, Proxies.SERVICE_PERIODOS);

        vistasService = VistasService
                .createProxy(vertx, Proxies.SERVICE_VISTAS);

        cumplimientoService = CumplimientoService
                .createProxy(vertx, Proxies.SERVICE_CUMPLIMIENTO);

    	return new GraphQLDataFetchers();
    }

    /**
     * Funcion que consulta los servidores publicos, obtiene los pametros de filtros, ordenamiento, paginacion y lo
     * resuelve a un objeto RespuestaServidoresDTO
     *
     * @author pavel.martinez
     * @since 19/05/2020
     *
     * @return RespuestaServidoresDTO, incluye lista de servidores
     */
    public AsyncDataFetcher<RespuestaServidoresDTO> obtenerServidores() {
        return obtenerServidores;
    }

    /**
     * Funcion de busqueda de los los servidores publicos, obtiene los pametros de filtros,y lo resuelve a un objeto
     * RespuestaServidoresDTO
     *
     * @author pavel.martinez
     * @since 26/07/2020
     *
     * @return RespuestaServidoresDTO, incluye lista de servidores
     */
    public AsyncDataFetcher<RespuestaServidoresDTO> buscarServidores() {
        return buscarServidores;
    }

    /**
     * Funcion que consulta la informacion para generar graficas, obtiene los pametros de filtros, ordenamiento,
     * paginacion y lo resuelve a un objeto RespuestaGraficaDTO
     *
     * @author pavel.martinez
     * @since 19/05/2020
     *
     * @return RespuestaGraficaDTO, información de cumplimiento, lista para generar graficas
     */
    public AsyncDataFetcher<RespuestaGraficaDTO> obtenerInformacionGraficas() {
        return obtenerInformacionGraficas;
    }

    /**
     * Funcion que consulta cumplimiento ordenado por instituciones, obtiene los pametros de filtros, ordenamiento,
     * paginacion y lo resuelve a un objeto RespuestaCumplimientoPorInstDTO
     *
     * @author pavel.martinez
     * @since 19/05/2020
     *
     * @return RespuestaCumplimientoPorInstDTO, que contiene lista de instituciones con su respectiva informacion de
     * cumplimiento
     */
    public AsyncDataFetcher<RespuestaCumplimientoPorInstDTO> obtenerInformacionCumplimiento() {
        return obtenerInformacionCumplimiento;
    }

    /**
     * Funcion que consulta cumplimiento ordenado por grupos de instituciones, obtiene los pametros de basivos
     *
     * @author pavel.martinez
     * @since 24/05/2021
     *
     * @return RespuestaCumplimientoPorGrupoDTO, que contiene lista de grupos con su respectiva informacion de
     * cumplimiento
     */
    public AsyncDataFetcher<RespuestaCumplimientoPorGrupoDTO> obtenerInformacionGrupoCumplimiento() {
        return obtenerInformacionGruposCumplimiento;
    }

    /**
     *
     * @return
     */
    public AsyncDataFetcher<RespuestaCumplimientoPorUADTO> obtenerInformacionCumplimientoUA() {
        return obtenerInformacionCumplimientoUA;
    }

    /**
     *
     * @return
     */
    public AsyncDataFetcher<Boolean> generarVistasOmisos() {
        return generarVistasOmisos;
    }

    /**
     *
     * @return
     */
    public AsyncDataFetcher<RespuestaPeriodosDTO> obtenerPeriodos() {
        return obtenerPeriodos;
    }

    /**
     *
     * @return
     */
    public AsyncDataFetcher<RespuestaServidoresVistaDTO> obtenerServidoresVistas() {
        return obtenerServidoresVistas;
    }

    /**
     *
     * @return
     */
    public AsyncDataFetcher<RespuestaVistaDTO> obtenerVistas() {
        return obtenerVistas;
    }

    /**
     *
     * @return
     */
    public AsyncDataFetcher<RespuestaPeriodosPorInstDTO> obtenerInstPeriodo() {
        return obtenerInstPeriodo;
    }

    public AsyncDataFetcher<Boolean> terminarProcesoVista() {
        return terminarProcesoVista;
    }

    /**
     *
     * @return
     */
    public AsyncDataFetcher<String> generarImpresionVistaPorFolio() {
        return generarImpresionVistaPorFolio;
    }

    /**
     * Funcion que guarda un periodo nuevo, regresa el id del periodo generado
     *
     * @author pavel.martinez
     * @since 21/02/2021
     *
     * @return String con el id nuevo
     */
    public AsyncDataFetcher<String> guardarPeriodo() {
        return guardarPeriodo;
    }

    /**
     *
     * @return
     */
    public AsyncDataFetcher<String> generarReporteServ() {
        return generarReporteServ;
    }

    /**
     *
     * @return
     */
    public AsyncDataFetcher<Boolean> extenderPeriodo() {
        return extenderPeriodo;
    }

    /**
     *
     * @return
     */
    public AsyncDataFetcher<Boolean> extenderPeriodoInstitucion() {
        return extenderPeriodoInstitucion;
    }


    /**
     * Resuelve la mutation para obtener los servidores publicos para OMEXT
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
   private final AsyncDataFetcher<RespuestaServidoresDTO> obtenerServidores = (env, handler) -> {
        Map<String, Object> argumentos = env.getArguments();
        JsonObject filtroJson = JsonObject.mapFrom(argumentos.get(EnumGraphQL.FILTRO.getValor()));
        FiltroOmextInputDTO filtro = new FiltroOmextInputDTO(filtroJson);

        Single.just(argumentos)

                .flatMap(q -> cumplimientoService.rxObtenerCumplServ(filtro))

                .map(RespuestaServidoresDTO::new)
                .subscribe(resp -> handler.handle(Future.succeededFuture(resp))
                        , error -> {
                            if (error instanceof ServiceException) {
                                SFPException se = (SFPException) error;
                                handler.handle(Future.failedFuture(se));
                                // En caso de errores, se devuelve el error especifico
                            } else if(error instanceof ReplyException) {
                                handler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_TIME_OUT, error) ));
                            } else {
                                handler.handle(Future.failedFuture(error));
                            }
                        });
   };

    /**
     * Resuelve la mutation para obtener los servidores publicos para OMEXT
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    private final AsyncDataFetcher<RespuestaServidoresDTO> buscarServidores = (env, handler) -> {

        Map<String, Object> argumentos = env.getArguments();
        JsonObject filtroJson = JsonObject.mapFrom(argumentos.get(EnumGraphQL.FILTRO.getValor()));
        FiltroOmextInputDTO filtro = new FiltroOmextInputDTO(filtroJson);

        filtro.setOffset(0);
        filtro.setTamanio(200);

        Single.just(argumentos)

                .flatMap(q -> cumplimientoService.rxObtenerCumplServ(filtro))

                .map(RespuestaServidoresDTO::new)
                .subscribe(resp -> handler.handle(Future.succeededFuture(resp))
                        , error -> {
                            if (error instanceof ServiceException) {
                                SFPException se = (SFPException) error;
                                handler.handle(Future.failedFuture(se));
                                // En caso de errores, se devuelve el error especifico
                            } else if(error instanceof ReplyException) {
                                handler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_TIME_OUT, error) ));
                            } else {
                                handler.handle(Future.failedFuture(error));
                            }
                        });
    };

    /**
     * Resuelve la mutation para obtener la información para crear gráficas del cumplimiento de servidores publicos en
     * OMEXT
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    private final AsyncDataFetcher<RespuestaGraficaDTO> obtenerInformacionGraficas = (env, handler) -> {

        Map<String, Object> argumentos = env.getArguments();

        JsonObject filtro = JsonObject.mapFrom(argumentos.get(EnumGraphQL.FILTRO.getValor()));

        Single.just(argumentos)

                .flatMap(q -> cumplimientoService.rxObtenerCumplGraficas(filtro))
                .map(RespuestaGraficaDTO::new)
                .subscribe(resp -> handler.handle(Future.succeededFuture(resp))
                        , error -> {
                            if (error instanceof ServiceException) {
                                SFPException se = (SFPException) error;
                                handler.handle(Future.failedFuture(se));
                                // En caso de errores, se devuelve el error especifico
                            } else if(error instanceof ReplyException) {
                                handler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_TIME_OUT, error) ));
                            } else {
                                handler.handle(Future.failedFuture(error));
                            }
                        });
    };



    /**+
     * Resuelve la mutation para obtener informacion por institucion de las estadisticas de cumplimiento de servidores
     * publicos en OMEXT
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    private final AsyncDataFetcher<RespuestaCumplimientoPorInstDTO> obtenerInformacionCumplimiento = (env, handler) -> {

        Map<String, Object> argumentos = env.getArguments();

        JsonObject filtro = JsonObject.mapFrom(argumentos.get(EnumGraphQL.FILTRO.getValor()));

        Single.just(argumentos)

                .flatMap(q -> cumplimientoService.rxObtenerCumplPorInst(filtro))
                .map(RespuestaCumplimientoPorInstDTO::new)
                .subscribe(resp -> handler.handle(Future.succeededFuture(resp))
                        , error -> {
                            if (error instanceof ServiceException) {
                                SFPException se = (SFPException) error;
                                handler.handle(Future.failedFuture(se));
                                // En caso de errores, se devuelve el error especifico
                            } else if(error instanceof ReplyException) {
                                handler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_TIME_OUT, error) ));
                            } else {
                                handler.handle(Future.failedFuture(error));
                            }
                        });
    };

    /**+
     * Resuelve la mutation para obtener informacion por grupo de institucion de las estadisticas de cumplimiento de servidores
     * publicos en OMEXT
     *
     * @author pavel.martinez
     * @since 24/05/2021
     */
    private final AsyncDataFetcher<RespuestaCumplimientoPorGrupoDTO> obtenerInformacionGruposCumplimiento = (env, handler) -> {

        Map<String, Object> argumentos = env.getArguments();

        JsonObject filtro = JsonObject.mapFrom(argumentos.get(EnumGraphQL.FILTRO.getValor()));

        Single.just(argumentos)

                .flatMap(q -> cumplimientoService.rxObtenerCumplPorGrupo(filtro))
                .map(RespuestaCumplimientoPorGrupoDTO::new)

                .subscribe(resp -> handler.handle(Future.succeededFuture(resp))
                        , error -> {
                            if (error instanceof ServiceException) {
                                SFPException se = (SFPException) error;
                                handler.handle(Future.failedFuture(se));
                                // En caso de errores, se devuelve el error especifico
                            } else if(error instanceof ReplyException) {
                                handler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_TIME_OUT, error) ));
                            } else {
                                handler.handle(Future.failedFuture(error));
                            }
                        });
    };

    /**+
     * Resuelve la mutation para obtener informacion por institucion de las estadisticas de cumplimiento de servidores
     * publicos en OMEXT
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    private final AsyncDataFetcher<String> generarReporteServ = (env, handler) -> {

        Map<String, Object> argumentos = env.getArguments();

        JsonObject filtro = JsonObject.mapFrom(argumentos.get(EnumGraphQL.FILTRO.getValor()));

        Single.just(argumentos)

                .flatMap(q -> cumplimientoService.rxObtenerReporteServ(filtro))
                .timeout(55, TimeUnit.SECONDS)
                .subscribe(resp -> handler.handle(Future.succeededFuture(resp))
                        , error -> {
                            if (error instanceof ServiceException) {
                                SFPException se = (SFPException) error;
                                handler.handle(Future.failedFuture(se));
                                // En caso de errores, se devuelve el error especifico
                            } else if(error instanceof ReplyException) {
                                handler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_TIME_OUT, error) ));
                            } else {
                                handler.handle(Future.failedFuture(error));
                            }
                        });
    };

    /**
     * Resuelve la mutation para obtener informacion por institucion de las estadisticas de cumplimiento de servidores
     * publicos en OMEXT
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    private final AsyncDataFetcher<RespuestaCumplimientoPorUADTO> obtenerInformacionCumplimientoUA = (env, handler) -> {

        Map<String, Object> argumentos = env.getArguments();

        JsonObject filtro = JsonObject.mapFrom(argumentos.get(EnumGraphQL.FILTRO.getValor()));

        Single.just(argumentos)

                .flatMap(q -> cumplimientoService.rxObtenerCumplPorUA(filtro))
                .map(RespuestaCumplimientoPorUADTO::new)
                .subscribe(resp -> handler.handle(Future.succeededFuture(resp))
                        , error -> {
                            if (error instanceof ServiceException) {
                                SFPException se = (SFPException) error;
                                handler.handle(Future.failedFuture(se));
                                // En caso de errores, se devuelve el error especifico
                            } else if(error instanceof ReplyException) {
                                handler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_TIME_OUT, error) ));
                            } else {
                                handler.handle(Future.failedFuture(error));
                            }
                        });
    };


    /**
     *
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    private final AsyncDataFetcher<String> guardarPeriodo = (env, handler) -> {

        LOGGER.info("AQUI EN GUARDAR_PERIODO + ");
        Map<String, Object> argumentos = env.getArguments();
        JsonObject periodo = JsonObject.mapFrom(argumentos.get(EnumGraphQL.PERIODO.getValor()));
        Single.just(argumentos)

                .flatMap(q -> periodosService.rxGuardarPeriodo(periodo))
                .subscribe(idPeriodo ->
                    {
                        handler.handle(Future.succeededFuture(idPeriodo));
                    }
                        , error -> {
                            if (error instanceof ServiceException) {
                                SFPException se = (SFPException) error;
                                handler.handle(Future.failedFuture(se));
                                // En caso de errores, se devuelve el error especifico
                            } else if(error instanceof ReplyException) {
                                handler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_TIME_OUT, error) ));
                            } else {
                                handler.handle(Future.failedFuture(error));
                            }
                        });
    };

    private final AsyncDataFetcher<RespuestaPeriodosDTO> obtenerPeriodos = (env, handler) -> {
        LOGGER.info("OBTENER PERIODOS.");
        Map<String, Object> argumentos = env.getArguments();

        JsonObject filtro = JsonObject.mapFrom(argumentos.get(EnumGraphQL.FILTRO.getValor()));
        LOGGER.info("EL FILTRO -> " + filtro);
        Single.just(argumentos)

                .flatMap(q -> periodosService.rxObtenerPeriodos(filtro))
                .map(RespuestaPeriodosDTO::new)
                .subscribe(resp -> {
                            LOGGER.info("=== En datafetcher la respuesta + " + resp);
                            handler.handle(Future.succeededFuture(resp));
                        }
                        , error -> {
                            if (error instanceof ServiceException) {
                                SFPException se = (SFPException) error;
                                handler.handle(Future.failedFuture(se));
                                // En caso de errores, se devuelve el error especifico
                            } else if(error instanceof ReplyException) {
                                handler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_TIME_OUT, error) ));
                            } else {
                                handler.handle(Future.failedFuture(error));
                            }
                        });
    };

    private final AsyncDataFetcher<RespuestaServidoresVistaDTO> obtenerServidoresVistas = (env, handler) -> {
        LOGGER.info("OBTENER SERVIDORES VISTAS.");
        Map<String, Object> argumentos = env.getArguments();

        JsonObject filtro = JsonObject.mapFrom(argumentos.get(EnumGraphQL.FILTRO.getValor()));
        LOGGER.info("EL FILTRO -> " + filtro);
        Single.just(argumentos)

                .flatMap(q -> vistasService.rxObtenerServidoresVista(filtro))
                .map(RespuestaServidoresVistaDTO::new)
                .subscribe(resp -> {
                            LOGGER.info("=== En datafetcher la respuesta + " + resp);
                            handler.handle(Future.succeededFuture(resp));
                        }
                        , error -> {
                            if (error instanceof ServiceException) {
                                SFPException se = (SFPException) error;
                                handler.handle(Future.failedFuture(se));
                                // En caso de errores, se devuelve el error especifico
                            } else if(error instanceof ReplyException) {
                                handler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_TIME_OUT, error) ));
                            } else {
                                handler.handle(Future.failedFuture(error));
                            }
                        });
    };

    private final AsyncDataFetcher<RespuestaVistaDTO> obtenerVistas = (env, handler) -> {
        LOGGER.info("OBTENER SERVIDORES VISTAS.");
        Map<String, Object> argumentos = env.getArguments();

        JsonObject filtro = JsonObject.mapFrom(argumentos.get(EnumGraphQL.FILTRO.getValor()));
        LOGGER.info("EL FILTRO -> " + filtro);
        Single.just(argumentos)

                .flatMap(q -> vistasService.rxObtenerVistas(filtro))
                .map(RespuestaVistaDTO::new)
                .subscribe(resp -> {
                            LOGGER.info("=== En datafetcher la respuesta + " + resp);
                            handler.handle(Future.succeededFuture(resp));
                        }
                        , error -> {
                            if (error instanceof ServiceException) {
                                SFPException se = (SFPException) error;
                                handler.handle(Future.failedFuture(se));
                                // En caso de errores, se devuelve el error especifico
                            } else if(error instanceof ReplyException) {
                                handler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_TIME_OUT, error) ));
                            } else {
                                handler.handle(Future.failedFuture(error));
                            }
                        });
    };


    private final AsyncDataFetcher<String> generarImpresionVistaPorFolio = (env, handler) -> {
        LOGGER.info("Generar Impresion vista por folio.");
        Map<String, Object> argumentos = env.getArguments();

        JsonObject filtro = JsonObject.mapFrom(argumentos.get(EnumGraphQL.FILTRO.getValor()));
        LOGGER.info("EL FILTRO -> " + filtro);
        Single.just(argumentos)

                .flatMap(q -> vistasService.rxGenerarImpresionServidoresVista(filtro))
                .subscribe(resp -> {
                            LOGGER.info("=== En datafetcher la respuesta + " + resp);
                            handler.handle(Future.succeededFuture(resp));
                        }
                        , error -> {
                            if (error instanceof ServiceException) {
                                SFPException se = (SFPException) error;
                                handler.handle(Future.failedFuture(se));
                                // En caso de errores, se devuelve el error especifico
                            } else if(error instanceof ReplyException) {
                                handler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_TIME_OUT, error) ));
                            } else {
                                handler.handle(Future.failedFuture(error));
                            }
                        });
    };

    private final AsyncDataFetcher<RespuestaPeriodosPorInstDTO> obtenerInstPeriodo = (env, handler) -> {
        LOGGER.info("OBTENER Instituciones por PERIDOSO.");
        Map<String, Object> argumentos = env.getArguments();

        JsonObject filtro = JsonObject.mapFrom(argumentos.get(EnumGraphQL.FILTRO.getValor()));
        LOGGER.info("EL FILTRO -> " + filtro);
        Single.just(argumentos)

                .flatMap(q -> periodosService.rxObtenerInstPeriodo(filtro))
                .map(RespuestaPeriodosPorInstDTO::new)
                .subscribe(resp -> {
                            LOGGER.info("=== En datafetcher la respuesta + " + resp);
                            handler.handle(Future.succeededFuture(resp));
                        }
                        , error -> {
                            if (error instanceof ServiceException) {
                                SFPException se = (SFPException) error;
                                handler.handle(Future.failedFuture(se));
                                // En caso de errores, se devuelve el error especifico
                            } else if(error instanceof ReplyException) {
                                handler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_TIME_OUT, error) ));
                            } else {
                                handler.handle(Future.failedFuture(error));
                            }
                        });
    };

    private final AsyncDataFetcher<Boolean> generarVistasOmisos = (env, handler) -> {
        LOGGER.info("=== GENERAR VISTAS DE OMISOS");
        Map<String, Object> argumentos = env.getArguments();

        JsonObject vistasOmisos = JsonObject.mapFrom(argumentos.get(EnumGraphQL.VISTAS_OMISOS.getValor()));
        LOGGER.info("Info para generar vistas de omisos -> " + vistasOmisos);
        Single.just(argumentos)

                .flatMap(q -> vistasService.rxGenerarVistasOmisos(vistasOmisos))
                .subscribe(resp -> {
                            LOGGER.info("=== En datafetcher la respuesta + " + resp);
                            handler.handle(Future.succeededFuture(resp));
                        }
                        , error -> {
                            if (error instanceof ServiceException) {
                                SFPException se = (SFPException) error;
                                handler.handle(Future.failedFuture(se));
                                // En caso de errores, se devuelve el error especifico
                            } else if(error instanceof ReplyException) {
                                handler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_TIME_OUT, error) ));
                            } else {
                                handler.handle(Future.failedFuture(error));
                            }
                        });
    };


    private final AsyncDataFetcher<Boolean> terminarProcesoVista = (env, handler) -> {
        LOGGER.info("=== TERMINAR");
        Map<String, Object> argumentos = env.getArguments();

        JsonObject params = JsonObject.mapFrom(argumentos.get(EnumGraphQL.PARAMS.getValor()));
        LOGGER.info("params para terminar el proceso -> " + params);
        Single.just(argumentos)

                .flatMap(q -> vistasService.rxFirmarVista(params))
                .subscribe(resp -> {
                            LOGGER.info("=== En datafetcher la respuesta + " + resp);
                            handler.handle(Future.succeededFuture(resp));
                        }
                        , error -> {
                            if (error instanceof ServiceException) {
                                SFPException se = (SFPException) error;
                                handler.handle(Future.failedFuture(se));
                                // En caso de errores, se devuelve el error especifico
                            } else if(error instanceof ReplyException) {
                                handler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_TIME_OUT, error) ));
                            } else {
                                handler.handle(Future.failedFuture(error));
                            }
                        });
    };

    /**+
     * Extiende la fecha limite de un periodo
     *
     * @author pavel.martinez
     * @since 31/03/2021
     */
    private final AsyncDataFetcher<Boolean> extenderPeriodo = (env, handler) -> {
        LOGGER.info("=== extenderPeriodo en DataFetcher");
        Map<String, Object> argumentos = env.getArguments();

        JsonObject extension = JsonObject.mapFrom(argumentos.get(EnumGraphQL.EXTENSION.getValor()));

        Single.just(argumentos)

                .flatMap(q -> periodosService.rxExtenderPeriodo(extension))
                .timeout(55, TimeUnit.SECONDS)
                .subscribe(resp -> handler.handle(Future.succeededFuture(resp))
                        , error -> {
                            if (error instanceof ServiceException) {
                                SFPException se = (SFPException) error;
                                handler.handle(Future.failedFuture(se));
                                // En caso de errores, se devuelve el error especifico
                            } else if(error instanceof ReplyException) {
                                handler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_TIME_OUT, error) ));
                            } else {
                                handler.handle(Future.failedFuture(error));
                            }
                        });
    };

    /**+
     * Extiende las fechas limites de periodos por institucion
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    private final AsyncDataFetcher<Boolean> extenderPeriodoInstitucion = (env, handler) -> {

        Map<String, Object> argumentos = env.getArguments();

        JsonObject extension = JsonObject.mapFrom(argumentos.get(EnumGraphQL.EXTENSION.getValor()));

        Single.just(argumentos)

                .flatMap(q -> periodosService.rxExtenderPeriodoInstitucion(extension))
                .timeout(55, TimeUnit.SECONDS)
                .subscribe(resp -> handler.handle(Future.succeededFuture(resp))
                        , error -> {
                            if (error instanceof ServiceException) {
                                SFPException se = (SFPException) error;
                                handler.handle(Future.failedFuture(se));
                                // En caso de errores, se devuelve el error especifico
                            } else if(error instanceof ReplyException) {
                                handler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_TIME_OUT, error) ));
                            } else {
                                handler.handle(Future.failedFuture(error));
                            }
                        });
    };

}
