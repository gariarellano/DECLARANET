/**
 * @(#)GraphQLDataFetchers.java 25/04/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.graphql;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.serviceproxy.ServiceException;
import mx.gob.sfp.dgti.applicationservice.ApplicationService;
import mx.gob.sfp.dgti.constantes.Http;
import mx.gob.sfp.dgti.constantes.Respuestas;
import mx.gob.sfp.dgti.dto.EnteDTO;
import mx.gob.sfp.dgti.dto.ParamConsultaEnteDTO;
import mx.gob.sfp.dgti.dto.ParamsSituacionDTO;
import mx.gob.sfp.dgti.graphql.queryExecutor.AsyncDataFetcher;
import mx.gob.sfp.dgti.utils.EnteUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Clase que contiene los DataFetcher para resolver los queries y mutations de graphql
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 25/04/2019
 */
public class GraphQLDataFetchers {

    /**
     * Servicio
     */
    ApplicationService applicationService;

    /**
     * Utilidades para entes
     */
    EnteUtils enteUtils;

    /**
     * Logger
     */
    final Logger logger = Logger.getLogger(GraphQLDataFetchers.class);

    /**
     * DataFetcher para obtener entes
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 25/04/2019
     */
    private final AsyncDataFetcher<List<EnteDTO>> obtenerEntes = (env, handler) -> {

        Map<String, Object> argumentos = env.getArguments();

        ParamConsultaEnteDTO params = new ParamConsultaEnteDTO();

        if(argumentos.get("filtro") != null) {
            JsonObject obje = JsonObject.mapFrom(argumentos.get("filtro"));
            params = Json.decodeValue(Json.encode(obje), ParamConsultaEnteDTO.class);
            //params = enteUtils.convertirParamEnum(paramsEnum);
        }

        applicationService.consultarEntes(params, busq -> {
            if(busq.succeeded()) {
                List<EnteDTO> entes = busq.result();

                handler.handle(Future.succeededFuture(entes));
            } else {
                handler.handle(
                        ServiceException.fail(Respuestas.ERROR.getId(), busq.cause().getMessage()));
            }
        });
    };

    /**
     * DataFetcher para obtener el historico de algun ente
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 25/04/2019
     */
    private final AsyncDataFetcher<List<EnteDTO>> obtenerHistoricoEnte = (env, handler) -> {

        Map<String, Object> argumentos = env.getArguments();

        String idEnteOrigen = argumentos.get("idEnteOrigen").toString();

        applicationService.consultarHistorialEntePorEnte(idEnteOrigen, resp -> {
            if(resp.succeeded()) {

                handler.handle(Future.succeededFuture(resp.result()));
            } else {
                if (resp.cause() instanceof ServiceException) {
                    ServiceException exc = (ServiceException) resp.cause();
                    handler.handle(
                            ServiceException.fail(exc.failureCode(), exc.getMessage()));
                } else {
                    handler.handle(
                            ServiceException.fail(Respuestas.ERROR.getId(), resp.cause().getMessage()));
                }
            }
        });
    };

    /**
     * DataFetcher para agregar un nuevo ente
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 25/04/2019
     */
    private final AsyncDataFetcher<EnteDTO> agregarEnteNuevo = (env, handler) -> {
        logger.info("\\nDataFetcher - agregarEnteNuevo()");
        // Se requiere validar el header
        if(!validarHeaderRequerido(env.getContext())) {
            handler.handle(ServiceException.fail(0, "NO AUTORIZADO PARA ESTA FUNCIONALIDAD"));
            return;
        }

        Map<String, Object> argumentos = env.getArguments();

        JsonObject objetoJson = JsonObject.mapFrom(argumentos.get("ente"));
        EnteDTO ente = new EnteDTO(objetoJson);
        logger.info("el ente : " + ente);

        applicationService.agregarEnteNuevo(ente, resp -> {
            if (resp.succeeded()) {
                handler.handle(Future.succeededFuture(resp.result()));

            } else {
                if (resp.cause() instanceof ServiceException) {
                    ServiceException exc = (ServiceException) resp.cause();
                    handler.handle(
                            ServiceException.fail(exc.failureCode(), exc.getMessage()));
                } else {
                    handler.handle(
                            ServiceException.fail(Respuestas.ERROR.getId(), resp.cause().getMessage()));
                }
            }
        });
    };

    /**
     *  DataFetcher para desactivar un ente
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 25/04/2019
     */
    private final AsyncDataFetcher<Boolean> desactivarEnte = (env, handler) -> {

        // Se requiere validar el header
        if(!validarHeaderRequerido(env.getContext())) {
            handler.handle(ServiceException.fail(0, "NO AUTORIZADO PARA ESTA FUNCIONALIDAD"));
            return;
        }

        Map<String, Object> argumentos = env.getArguments();

        final String id;
        final boolean desactivarHistorico;

        final String idEnte = (argumentos.get("idEnte") != null)?
                argumentos.get("idEnte").toString():null;
        final String idEnteOrigen = (argumentos.get("idEnteOrigen") != null)?
                argumentos.get("idEnteOrigen").toString():null;

        if(idEnteOrigen != null) {
            id = idEnteOrigen;
            desactivarHistorico = true;
        }  else if(idEnte != null) {
            id = idEnte;
            desactivarHistorico = false;
        } else {
            id = null;
            desactivarHistorico = false;
        }

        applicationService.desactivarEnte(desactivarHistorico, id, resp -> {
            if (resp.succeeded()) {

                handler.handle(Future.succeededFuture(true));

            } else {
                if (resp.cause() instanceof ServiceException) {
                    ServiceException exc = (ServiceException) resp.cause();
                    handler.handle(
                            ServiceException.fail(exc.failureCode(), exc.getMessage()));
                } else {
                    handler.handle(
                            ServiceException.fail(Respuestas.ERROR.getId(), resp.cause().getMessage()));
                }
            }
        });
    };

    /**
     * DataFetcher para habilitar la transicion en un ente
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 25/04/2019
     */
    private final AsyncDataFetcher<EnteDTO> definirSituacionEnte = (env, handler) -> {

        // Se requiere validar el header
        if(!validarHeaderRequerido(env.getContext())) {
            handler.handle(ServiceException.fail(0, "NO AUTORIZADO PARA ESTA FUNCIONALIDAD"));
            return;
        }

        Map<String, Object> argumentos = env.getArguments();

        JsonObject objetoJson = JsonObject.mapFrom(argumentos.get("params"));
        ParamsSituacionDTO params = new ParamsSituacionDTO(objetoJson);

        applicationService.definirSituacionEnte(params.getIdEnte(), params.getSituacion(), resp -> {
            if (resp.succeeded()) {
                EnteDTO enteResp = Json.decodeValue(Json.encode(resp.result()), EnteDTO.class);
                handler.handle(Future.succeededFuture(enteResp));

            } else {
                if (resp.cause() instanceof ServiceException) {
                    ServiceException exc = (ServiceException) resp.cause();
                    handler.handle(
                            ServiceException.fail(exc.failureCode(), exc.getMessage()));
                } else {
                    handler.handle(
                            ServiceException.fail(Respuestas.ERROR.getId(), resp.cause().getMessage()));
                }
            }
        });
    };

    /**
     * DAtaFetcher para actualizar un ente
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 25/04/2019
     */
    private final AsyncDataFetcher<EnteDTO> actualizarEnte = (env, handler) -> {

        // Se requiere validar el header
        if(!validarHeaderRequerido(env.getContext())) {
            handler.handle(ServiceException.fail(0, "NO AUTORIZADO PARA ESTA FUNCIONALIDAD"));
            return;
        }

        Map<String, Object> argumentos = env.getArguments();

        JsonObject objetoJson = JsonObject.mapFrom(argumentos.get("ente"));
        EnteDTO ente = new EnteDTO(objetoJson);


        applicationService.actualizarEnte(ente, resp -> {
            if (resp.succeeded()) {
                EnteDTO enteResp = Json.decodeValue(Json.encode(resp.result()), EnteDTO.class);
                handler.handle(Future.succeededFuture(enteResp));

            } else {
                if (resp.cause() instanceof ServiceException) {
                    ServiceException exc = (ServiceException) resp.cause();
                    handler.handle(
                            ServiceException.fail(exc.failureCode(), exc.getMessage()));
                } else {
                    handler.handle(
                            ServiceException.fail(Respuestas.ERROR.getId(), resp.cause().getMessage()));
                }
            }
        });
    };

    /**
     * Init
     *
     * @param vertx
     */
    public void init(Vertx vertx){

        applicationService = ApplicationService
                .createProxy(vertx, ApplicationService.SERVICE_ADDRESS);

        enteUtils = new EnteUtils();
    }

    /**
     * Funcion para validar que existan los haeders requeridos para utilizar las mutation y tengan los valores correctos
     *
     * @param rc: RoutingContext utilizado para obtener los headers
     *
     * @return boolean: true si se valido el header, false si no se valido
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 10/07/2019
     */
    private boolean validarHeaderRequerido(RoutingContext rc) {
        logger.info(Http.VARIABLE_ENTORNO_HEADER_API_ENTES);
        logger.info(Http.VARIABLE_ENTORNO_VALOR_API_ENTES);
        logger.info(rc.request().headers());
        try {

            logger.info(rc.request().getHeader("keyHeaderMutations"));

            if (rc.request().getHeader(Http.VARIABLE_ENTORNO_HEADER_API_ENTES) != null &&
                    rc.request().getHeader(Http.VARIABLE_ENTORNO_HEADER_API_ENTES)
                            .equals(Http.VARIABLE_ENTORNO_VALOR_API_ENTES)) {
                logger.info("Permiso autorizado por header.");
                return true;
            }
            logger.info("Acceso no autorizado por header.");
        } catch (Exception e) {
            logger.info("Acceso no autorizado por header.");
        }
        return false;
    }


    public AsyncDataFetcher<List<EnteDTO>> getObtenerEntes() {
        return obtenerEntes;
    }

    public AsyncDataFetcher<List<EnteDTO>> getObtenerHistoricoEnte() {
        return obtenerHistoricoEnte;
    }

    public AsyncDataFetcher<EnteDTO> getAgregarEnteNuevo() {
        return agregarEnteNuevo;
    }

    public AsyncDataFetcher<Boolean> getDesactivarEnte() {
        return desactivarEnte;
    }

    public AsyncDataFetcher<EnteDTO> getActualizarEnte() {
        return actualizarEnte;
    }

    public AsyncDataFetcher<EnteDTO> getDefinirSituacionEnte() {
        return definirSituacionEnte;
    }
}