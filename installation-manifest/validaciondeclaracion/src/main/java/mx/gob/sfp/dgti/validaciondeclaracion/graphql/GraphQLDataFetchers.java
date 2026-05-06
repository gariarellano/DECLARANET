/**
 * @(#)GraphQLDataFetchers.java 26/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.graphql;

import io.reactivex.Single;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.serviceproxy.ServiceException;
import mx.gob.sfp.dgti.validaciondeclaracion.as.RepartidorAS;
import mx.gob.sfp.dgti.validaciondeclaracion.as.VerificadorAS;
import mx.gob.sfp.dgti.validaciondeclaracion.as.impl.RepartidorASImpl;
import mx.gob.sfp.dgti.validaciondeclaracion.as.impl.VerificadorASImpl;
import mx.gob.sfp.dgti.validaciondeclaracion.dto.RespuestaDTO;
import mx.gob.sfp.dgti.validaciondeclaracion.servicioext.GuardadoDeclaracion;
import mx.gob.sfp.dgti.validaciondeclaracion.servicioext.LlamadorServicios;
import mx.gob.sfp.dgti.validaciondeclaracion.util.EnumCampos;
import mx.gob.sfp.dgti.validaciondeclaracion.util.EnumEtiquetas;
import mx.gob.sfp.dgti.validaciondeclaracion.util.EnumTipoResp;
import mx.gob.sfp.dgti.validaciondeclaracion.util.Proxies;

import java.util.Map;
import java.util.Set;

/**
 * GraphQLDataFetchers con metodos que resuelveran los queries y mutations
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
public class GraphQLDataFetchers {

    /**
     * El logger
     */
	static final Logger LOGGER = LoggerFactory.getLogger(GraphQLDataFetchers.class);

    /**
     * Servicio asincrono llamador de servicios
     */
    private static mx.gob.sfp.dgti.validaciondeclaracion.servicioext.reactivex.LlamadorServicios llamadorServicios;

    /**
     * Servicio asincrono para guardar declaracion
     */
    private static mx.gob.sfp.dgti.validaciondeclaracion.servicioext.reactivex.GuardadoDeclaracion guardadoDeclaracion;

    /**
     * Servicio de Verificador de declaracion
     */
    private static VerificadorAS verificador;

    /**
     * Servicio de Repartidor de declaracion
     */
    private static RepartidorAS repartidor;

    /**
     * Inicializa el DataFetcher
     *
     * @param vertx
     * @return
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 26/09/2019
     */
    static GraphQLDataFetchers init(Vertx vertx) {
        LOGGER.info(EnumEtiquetas.INICIA_GRAPHQL.getNombre());

        verificador = new VerificadorASImpl();

        repartidor = new RepartidorASImpl();

        llamadorServicios = LlamadorServicios
                .createProxy(vertx, Proxies.SERVICE_ADDRESS_LLAMADOR);

        guardadoDeclaracion = GuardadoDeclaracion
                .createProxy(vertx, Proxies.SERVICE_ADDRESS_GUARDADO);
    	return new GraphQLDataFetchers();
    }

    /**
     * Agregar un registro nuevo al catálogo
     *
     * @return agregarRegistro
     */
    public AsyncDataFetcher<RespuestaDTO> querySimple() {
        return querySimple;
    }

    /**
     * Resuelve la mutation para agregar elementos al catalogo.
     */
    private final AsyncDataFetcher<RespuestaDTO> querySimple = (env, handler) -> {

        RespuestaDTO retro = new RespuestaDTO();
        handler.handle(Future.succeededFuture(retro));

    };

    /**
     * Guardar declaración
     *
     * @return guardarDeclaracion
     */
    public AsyncDataFetcher<RespuestaDTO> guardarDeclaracion() {
        return guardarDeclaracion;
    }

    /**
     * Guardar aviso por cambio de dependencia
     *
     * @return guardarAviso()
     */
    public AsyncDataFetcher<RespuestaDTO> guardarAviso() {
        return guardarAviso;
    }

    /**
     * Guardar notas aclaratorias
     *
     * @return guardarNotasAclaratorias
     */
    public AsyncDataFetcher<RespuestaDTO> guardarNotasAclaratorias() {
        return guardarNotasAclaratorias;
    }

    /**
     * Resuelve la mutation para validar y mandar a guardar una declaracion.
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 26/09/2019
     */
   private final AsyncDataFetcher<RespuestaDTO> guardarDeclaracion = (env, handler) -> {

       Map<String, Object> argumentos = env.getArguments();
       JsonObject declaracion = JsonObject.mapFrom(argumentos.get(EnumCampos.PARAMETROS.getNombre()));

       //Se obtienen los modulos que si seran mandados y que no son nulos
       Set<String> modulosNoNulos = VerificadorASImpl.obtenerModulosNoNulos(declaracion);

       Single.just(declaracion)

            // Primera validacion donde se verifica que se manden los modulos validos dependiendo del tipo de formato
            .flatMapCompletable(declaracionCompleta ->
                   verificador.validarDeclaracion(declaracionCompleta, modulosNoNulos))
            .toSingleDefault(declaracion.getJsonObject(EnumCampos.DECLARACION.getNombre()))

            // Se manda la declaracion para obtener la lista de LlamadoDTO requerido para llamar a los servicios
            // validadores
            .flatMap(declaracionRepartida ->
                repartidor.repartirDeclaracion(declaracionRepartida, modulosNoNulos))

            // Se llaman a los servicios validadores, se obtienen observaciones de todos los servicios en caso de
            // que haya, se modifica la declaracion en ciertos casos, se activa el firmado
            .flatMap(resultadoServicios ->
                llamadorServicios.rxLlamarServicios(
                        resultadoServicios,
                        declaracion.getJsonObject(EnumCampos.DECLARACION.getNombre()),
                        modulosNoNulos))

            // Se guarda en base la declaracion
            .flatMap(declaracionLista -> guardadoDeclaracion.rxGuardarDeclaracion(
                        declaracionLista))
            // Suscripcion, se devuelve respuesta satisfactoria
            .subscribe(resp ->
                handler.handle(Future.succeededFuture(resp))
             , error -> {
                // En caso de errores, se devuelve el error especifico

                if (error instanceof ServiceException) {
                    ServiceException exc = (ServiceException) error;
                    if(exc.failureCode() != EnumTipoResp.CON_OBSERVACIONES.getId()) {
                        LOGGER.info(EnumEtiquetas.ERROR_INESPERADO.getNombre() +
                                error + " - " + error.getCause() + " - " +error.getMessage());
                    }
                    handler.handle(Future.succeededFuture(new RespuestaDTO(exc.getDebugInfo())));

                } else {
                    LOGGER.info(EnumEtiquetas.ERROR_INESPERADO.getNombre() +
                            error + " - " + error.getCause() + " - " +error.getMessage());
                    handler.handle(Future.succeededFuture(
                            new RespuestaDTO(EnumTipoResp.ERROR.name())));
                }

            });
   };

    /**
     * Resuelve la mutation para validar y mandar a guardar un aviso por cambio de dependencia.
     *
     * @author pavel.martinez
     * @since 29/01/2020
     */
    private final AsyncDataFetcher<RespuestaDTO> guardarAviso = (env, handler) -> {

        Map<String, Object> argumentos = env.getArguments();
        JsonObject aviso = JsonObject.mapFrom(argumentos.get(EnumCampos.PARAMETROS.getNombre()));
        //Se obtienen los modulos que si seran mandados y que no son nulos
        Set<String> modulosNoNulos = VerificadorASImpl.obtenerModulosNoNulos(aviso);

        Single.just(aviso)

                // Primera validacion donde se verifica que se manden los modulos validos dependiendo del tipo de formato
                .flatMapCompletable(avisoCompleto ->
                        verificador.validarAvisoCambio(avisoCompleto, modulosNoNulos))
                .toSingleDefault(aviso.getJsonObject(EnumCampos.DECLARACION.getNombre()))

                // Se manda la declaracion para obtener la lista de LlamadoDTO requerido para llamar a los servicios
                // validadores
                .flatMap(avisoRepartido ->
                        repartidor.repartirAvisoCambio(avisoRepartido, modulosNoNulos))

                // Se llaman a los servicios validadores, se obtienen observaciones de todos los servicios en caso de
                // que haya, se modifica la declaracion en ciertos casos, se activa el firmado
                .flatMap(resultadoServicios ->
                        llamadorServicios.rxLlamarServiciosAviso(
                                resultadoServicios,
                                aviso.getJsonObject(EnumCampos.DECLARACION.getNombre()),
                                modulosNoNulos))


                // Se guarda en base la declaracion
                .flatMap(avisoListo -> {
                    return guardadoDeclaracion.rxGuardarDeclaracion(
                                avisoListo);
                })
                // Suscripcion, se devuelve respuesta satisfactoria
                .subscribe(resp -> {
                            handler.handle(Future.succeededFuture(resp));
                        }
                        , error -> {
                            // En caso de errores, se devuelve el error especifico
                            if (error instanceof ServiceException) {
                                ServiceException exc = (ServiceException) error;
                                if(exc.failureCode() != EnumTipoResp.CON_OBSERVACIONES.getId()) {
                                    LOGGER.info(EnumEtiquetas.ERROR_INESPERADO.getNombre() +
                                            error + " - " + error.getCause() + " - " +error.getMessage());
                                }
                                handler.handle(Future.succeededFuture(new RespuestaDTO(exc.getDebugInfo())));

                            } else {
                                LOGGER.info(EnumEtiquetas.ERROR_INESPERADO.getNombre() +
                                        error + " - " + error.getCause() + " - " +error.getMessage());
                                handler.handle(Future.succeededFuture(
                                        new RespuestaDTO(EnumTipoResp.ERROR.name())));
                            }

                        });
    };

    /**
     * Resuelve la mutation para validar y mandar a guardar un aviso por cambio de dependencia.
     *
     * @author pavel.martinez
     * @since 29/01/2020
     */
    private final AsyncDataFetcher<RespuestaDTO> guardarNotasAclaratorias = (env, handler) -> {

        Map<String, Object> argumentos = env.getArguments();
        JsonObject notas = JsonObject.mapFrom(argumentos.get(EnumCampos.PARAMETROS.getNombre()));
        //Se obtienen los modulos que si seran mandados y que no son nulos
        Set<String> modulosNoNulos = VerificadorASImpl.obtenerModulosNoNulos(notas);

        Single.just(notas)

                // Primera validacion donde se verifica que se manden los modulos validos dependiendo del tipo de formato
                .flatMapCompletable(notasAclaratorias ->
                        verificador.validarNotaAclaratoria(notasAclaratorias, modulosNoNulos))
                .toSingleDefault(notas.getJsonObject(EnumCampos.DECLARACION.getNombre()))

                // Se manda la declaracion para obtener la lista de LlamadoDTO requerido para llamar a los servicios
                // validadores
                .flatMap(notasVerificadas ->
                        repartidor.repartirNotas(notasVerificadas))

                // Se llaman a los servicios validadores, se obtienen observaciones de todos los servicios en caso de
                // que haya, se modifica la declaracion en ciertos casos, se activa el firmado
                .flatMap(notasRepartidas ->
                        llamadorServicios.rxLlamarServiciosNotasAclaratorias(
                                notasRepartidas,
                                notas.getJsonObject(EnumCampos.DECLARACION.getNombre())))

                // Se guarda en base la declaracion
                .flatMap(notasListas -> {
                    return guardadoDeclaracion.rxGuardarDeclaracion(notasListas);
                })
                // Suscripcion, se devuelve respuesta satisfactoria
                .subscribe(resp -> {
                            handler.handle(Future.succeededFuture(resp));
                        }
                        , error -> {
                            // En caso de errores, se devuelve el error especifico
                            if (error instanceof ServiceException) {
                                ServiceException exc = (ServiceException) error;
                                if(exc.failureCode() != EnumTipoResp.CON_OBSERVACIONES.getId()) {
                                    LOGGER.info(EnumEtiquetas.ERROR_INESPERADO.getNombre() +
                                            error + " - " + error.getCause() + " - " +error.getMessage());
                                }
                                handler.handle(Future.succeededFuture(new RespuestaDTO(exc.getDebugInfo())));

                            } else {
                                LOGGER.info(EnumEtiquetas.ERROR_INESPERADO.getNombre() +
                                        error + " - " + error.getCause() + " - " +error.getMessage());
                                handler.handle(Future.succeededFuture(
                                        new RespuestaDTO(EnumTipoResp.ERROR.name())));
                            }

                        });
    };

}
