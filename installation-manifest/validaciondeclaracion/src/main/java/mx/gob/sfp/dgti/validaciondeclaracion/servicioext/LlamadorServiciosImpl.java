/**
 * @(#)LlamadorServiciosImpl.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.servicioext;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.serviceproxy.ServiceException;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoFormatoDeclaracion;
import mx.gob.sfp.dgti.validaciondeclaracion.as.impl.VerificadorASImpl;
import mx.gob.sfp.dgti.validaciondeclaracion.dto.LlamadoDTO;
import mx.gob.sfp.dgti.validaciondeclaracion.dto.ModuloDTO;
import mx.gob.sfp.dgti.validaciondeclaracion.dto.RespuestaDTO;
import mx.gob.sfp.dgti.validaciondeclaracion.util.EnumCampos;
import mx.gob.sfp.dgti.validaciondeclaracion.util.EnumModulo;
import mx.gob.sfp.dgti.validaciondeclaracion.util.EnumTipoResp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Clase para el servicio LlamadorServiciosImpl
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
public class LlamadorServiciosImpl implements LlamadorServicios{

    /**
     * WebCLient
     */
    private WebClient client;

    /**
     * El logger
     */
    static final Logger LOGGER = LoggerFactory.getLogger(LlamadorServiciosImpl.class);

    /**
     * Constructor
     *
     * @param vertx: el vertx
     */
    public LlamadorServiciosImpl(Vertx vertx) {

        LOGGER.info("=== LlamadorServiciosImpl()");
        LOGGER.info("=== Se levanta el servicio deLlamadorServiciosImpl()");
        client = WebClient.create(vertx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LlamadorServicios llamarServicios(List<LlamadoDTO> listaServicios, JsonObject declaracion,  Set<String> mandados,
                                             Handler<AsyncResult<JsonObject>> resultHandler){

        List<ModuloDTO> erroresSecciones = new ArrayList<>();
        AtomicReference<Boolean> algunoIncompleto = new AtomicReference<>(false);
        AtomicReference<LlamadoDTO> llamadoIngresos = new AtomicReference<>(new LlamadoDTO());

        //Se agregan banderas para los bienes para cuando están validados, se envia la informacion
        AtomicReference<JsonObject> mueblesValidados = new AtomicReference<>(new JsonObject());
        AtomicReference<JsonObject> inmueblesValidados = new AtomicReference<>(new JsonObject());
        AtomicReference<JsonObject> vehiculosValidados = new AtomicReference<>(new JsonObject());

        AtomicReference<JsonObject> cambioNivelJerarquico = new AtomicReference<>(new JsonObject());

        Single<List<JsonObject>> elFlow = Single.just(listaServicios)
                .flatMap(servicios -> Observable.fromIterable(servicios)
                        //Se filtra que no sea ingresos, ingresos se puede mandar despues
                        .filter(servicio -> {

                            if(servicio.getServicio().getModulo().equals(EnumModulo.I_INGRESOS_NETOS.getModulo())) {
                                llamadoIngresos.set(servicio);
                                return false;
                            }
                            return true;
                        })
                        .flatMapSingle(
                                servicio -> llamarServicio(servicio)
                                        .timeout(15, TimeUnit.SECONDS)
                                        .doOnSuccess(
                                                respuesta -> {
                                                    if(!respuesta.isEmpty()) {
                                                        ModuloDTO modulo = new ModuloDTO(respuesta);
                                                        if(modulo.isIncompleto()) {
                                                            algunoIncompleto.set(true);
                                                        }
                                                        if (!modulo.getErrores().isEmpty()) {
                                                            erroresSecciones.add(modulo);
                                                        } else if (modulo.getNiverJerarquico() != null) {
                                                            cambioNivelJerarquico.set(modulo.getNiverJerarquico().toJson());
                                                        }
                                                    } else {

                                                        switch (servicio.getServicio()) {

                                                            case I_BIENES_INMUEBLES:
                                                                inmueblesValidados.set(servicio.getInfo());
                                                                break;
                                                            case I_BIENES_MUEBLES:
                                                                mueblesValidados.set(servicio.getInfo());
                                                                break;
                                                            case I_VEHICULOS:
                                                                vehiculosValidados.set(servicio.getInfo());
                                                                break;
                                                            default:
                                                                break;
                                                        }
                                                    }
                                                })
                                        .doOnError(error -> {

                                            LOGGER.info("=== Ocurrio algo en servicio individual");
                                            LOGGER.info("=== El servicio es: ".concat(servicio.getServicio().getModulo()));
                                            LOGGER.info("=== Se envio: " + servicio.getInfo());
                                            if (error.getMessage() != null) {
                                                LOGGER.info("=== El error es: "
                                                        . concat(error.toString())
                                                        . concat(", mensaje: ")
                                                        . concat(error.getMessage()));
                                            } else {
                                                LOGGER.info("=== El error es: " . concat(error.toString()));
                                            }
                                        })
                        )
                        .toList()
                );

        elFlow
            .flatMap(laLista -> {

                if (llamadoIngresos.get().getServicio() != null) {

                    // Se verifica si los 3 bienes están validados para enviarlos con ingresos
                    if(!inmueblesValidados.get().isEmpty() && !mueblesValidados.get().isEmpty()
                            && !vehiculosValidados.get().isEmpty()) {

                        llamadoIngresos.get().getInfo()
                                .put(EnumModulo.I_VEHICULOS.getModulo(), vehiculosValidados.get())
                                .put(EnumModulo.I_BIENES_MUEBLES.getModulo(), mueblesValidados.get())
                                .put(EnumModulo.I_BIENES_INMUEBLES.getModulo(), inmueblesValidados.get());
                    }

                    return llamarServicio(llamadoIngresos.get())
                            .timeout(15, TimeUnit.SECONDS)
                            .doOnSuccess(
                                    respuesta -> {
                                        if(!respuesta.isEmpty()) {
                                            ModuloDTO modulo = new ModuloDTO(respuesta);
                                            if(modulo.isIncompleto()) {
                                                algunoIncompleto.set(true);
                                            }
                                            if (!modulo.getErrores().isEmpty()) {
                                                erroresSecciones.add(modulo);
                                            }
                                        }
                                    })
                            .doOnError(error -> {
                                LOGGER.info("=== Ocurrio algo en servicio individual");
                                LOGGER.info("=== El servicio es: ".concat(llamadoIngresos.get().getServicio().getModulo()));
                                LOGGER.info("=== Se envio: " + llamadoIngresos.get().getInfo());
                                if (error.getMessage() != null) {
                                    LOGGER.info("=== El error es: "
                                            . concat(error.toString())
                                            . concat(", mensaje: ")
                                            . concat(error.getMessage()));
                                } else {
                                    LOGGER.info("=== El error es: " . concat(error.toString()));
                                }

                            });
                } else {
                    return Single.just(laLista);
                }
            })
            .subscribe(
                response -> {
                    RespuestaDTO respuesta;

                    if (!erroresSecciones.isEmpty()) {

                        respuesta = new RespuestaDTO(EnumTipoResp.CON_OBSERVACIONES.name(), erroresSecciones);

                        resultHandler.handle(ServiceException.fail(
                                EnumTipoResp.CON_OBSERVACIONES.getId(),
                                EnumTipoResp.CON_OBSERVACIONES.getMensaje(),
                                respuesta.toJson()));

                    }  else {
                        //Al final se actualiza la declaracion si es necesario
                        actualizarDeclaracion(declaracion, cambioNivelJerarquico.get(), algunoIncompleto.get(), mandados);


                        resultHandler.handle(Future.succeededFuture(declaracion));
                    }

                },
                error -> {
                    LOGGER.info("=== Ocurrio un algo y se corto el flujo: \n" + error.getMessage());
                    LOGGER.error("=== Error: " + error + " : " + error.getLocalizedMessage() + " : " + error.getCause());

                    RespuestaDTO respuesta = new RespuestaDTO(EnumTipoResp.ERROR_COMUNICACION.name());
                    resultHandler.handle(ServiceException.fail(
                            EnumTipoResp.ERROR.getId(),
                            EnumTipoResp.ERROR.getMensaje(),
                            respuesta.toJson()));
                }
            );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LlamadorServicios llamarServiciosAviso(List<LlamadoDTO> listaServicios, JsonObject aviso,  Set<String> mandados,
                                             Handler<AsyncResult<JsonObject>> resultHandler) {
        AtomicReference<JsonObject> cambioNivelJerarquico = new AtomicReference<>(new JsonObject());

        List<ModuloDTO> erroresSecciones = new ArrayList<>();

        Single.just(listaServicios)
                .flatMap(servicios -> Observable.fromIterable(servicios)
                        .flatMapSingle(
                                servicio -> llamarServicio(servicio)
                                        .timeout(15, TimeUnit.SECONDS)
                                        .doOnSuccess(
                                                respuesta -> {
                                                    if(!respuesta.isEmpty()) {
                                                        ModuloDTO modulo = new ModuloDTO(respuesta);
                                                        if (!modulo.getErrores().isEmpty()) {
                                                            erroresSecciones.add(modulo);
                                                        } else if (modulo.getNiverJerarquico() != null) {
                                                            cambioNivelJerarquico.set(modulo.getNiverJerarquico().toJson());
                                                        }
                                                    }
                                                })
                                        .doOnError(error -> {
                                            LOGGER.info("=== Ocurrio algo en servicio individual");
                                            LOGGER.info("=== El servicio es: ".concat(servicio.getServicio().getModulo()));
                                            LOGGER.info("=== Se envio: " + servicio.getInfo());
                                            if (error.getMessage() != null) {
                                                LOGGER.info("=== El error es: "
                                                        . concat(error.toString())
                                                        . concat(", mensaje: ")
                                                        . concat(error.getMessage()));
                                            } else {
                                                LOGGER.info("=== El error es: " . concat(error.toString()));
                                            }
                                        })
                        )
                        .toList()
                )
                .subscribe(
                        response -> {
                            RespuestaDTO respuesta;
                            if(!erroresSecciones.isEmpty()) {


                                respuesta = new RespuestaDTO(EnumTipoResp.CON_OBSERVACIONES.name(), erroresSecciones);

                                resultHandler.handle(ServiceException.fail(
                                        EnumTipoResp.CON_OBSERVACIONES.getId(),
                                        EnumTipoResp.CON_OBSERVACIONES.getMensaje(),
                                        respuesta.toJson()));

                            }  else {

                                //Al final se actualiza la declaracion si es necesario
                                actualizarDeclaracion(aviso, cambioNivelJerarquico.get(), false, mandados);
                                VerificadorASImpl.activarFirmadoAviso(aviso, mandados);
                                resultHandler.handle(Future.succeededFuture(aviso));
                            }

                        },
                        error -> {
                            LOGGER.info("=== Ocurrio un algo y se corto el flujo: \n" + error.getMessage());
                            LOGGER.error("=== Error: " + error + " : " + error.getLocalizedMessage() + " : "
                                    + error.getCause());

                            RespuestaDTO respuesta = new RespuestaDTO(EnumTipoResp.ERROR_COMUNICACION.name());
                            resultHandler.handle(ServiceException.fail(
                                    EnumTipoResp.ERROR.getId(),
                                    EnumTipoResp.ERROR.getMensaje(),
                                    respuesta.toJson()));
                        }
                );
        return this;
    }


    /**
     *
     * @param servicio
     * @param notas
     * @param resultHandler
     * @return
     */
    @Override
    public LlamadorServicios llamarServiciosNotasAclaratorias(LlamadoDTO servicio, JsonObject notas,
                                                  Handler<AsyncResult<JsonObject>> resultHandler) {

        List<ModuloDTO> erroresSecciones = new ArrayList<>();

        Single.just(servicio)
                .flatMap(this::llamarServicio)
                    .timeout(15, TimeUnit.SECONDS)
                    .doOnSuccess(
                        respuesta -> {
                            if(!respuesta.isEmpty()) {
                                ModuloDTO modulo = new ModuloDTO(respuesta);
                                if (!modulo.getErrores().isEmpty()) {
                                    erroresSecciones.add(modulo);
                                }
                            }
                        })
                .doOnError(error -> {
                    LOGGER.info("=== Ocurrio algo en servicio de notas");
                    LOGGER.info("=== El servicio es: ".concat(servicio.getServicio().getModulo()));
                    LOGGER.info("=== El contenido es: " + servicio.getInfo());
                    if (error.getMessage() != null) {
                        LOGGER.info("=== El error es: "
                                . concat(error.toString())
                                . concat(", mensaje: ")
                                . concat(error.getMessage()));
                    } else {
                        LOGGER.info("=== El error es: " . concat(error.toString()));
                    }
                }).subscribe(
                    response -> {
                    RespuestaDTO respuesta;
                    if(!erroresSecciones.isEmpty()) {


                        respuesta = new RespuestaDTO(EnumTipoResp.CON_OBSERVACIONES.name(), erroresSecciones);

                        resultHandler.handle(ServiceException.fail(
                                EnumTipoResp.CON_OBSERVACIONES.getId(),
                                EnumTipoResp.CON_OBSERVACIONES.getMensaje(),
                                respuesta.toJson()));

                    }  else {

                        VerificadorASImpl.activarFirmadoNotas(notas);
                        resultHandler.handle(Future.succeededFuture(notas));
                    }
                },
                error -> {
                    LOGGER.info("=== Ocurrio un algo y se corto el flujo: \n" + error.getMessage());
                    LOGGER.error("=== Error: " + error + " : " + error.getLocalizedMessage() + " : "
                            + error.getCause());

                    RespuestaDTO respuesta = new RespuestaDTO(EnumTipoResp.ERROR_COMUNICACION.name());
                    resultHandler.handle(ServiceException.fail(
                            EnumTipoResp.ERROR.getId(),
                            EnumTipoResp.ERROR.getMensaje(),
                            respuesta.toJson()));
                }

        );
        return this;

    }


    /**
     * Funcion para modificar la declaracion en ciertos casos
     *
     * @param declaracion: declaracion que se modifica
     * @param cambioNivelJerarquico: indica si se debe de cambiar el nivelJerarquico y el formato
     * @param algunoIncompleto: indica si es aun esta incompleta la declaracion para despues definir si se puede firmar
     *                        o no
     * @param mandados: modulos mandados
     *
     * @author pavel.martinez
     * @since 16/01/2020
     */
    private void actualizarDeclaracion(JsonObject declaracion, JsonObject cambioNivelJerarquico,
                                       boolean algunoIncompleto, Set<String> mandados ) {

        if(cambioNivelJerarquico != null && !cambioNivelJerarquico.isEmpty()) {
            declaracion
                    .getJsonObject(EnumCampos.ENCABEZADO.getNombre())
                    .put(EnumCampos.NIVEL_JERARQUICO.getNombre(), cambioNivelJerarquico);

            if(cambioNivelJerarquico.getString(EnumCampos.VALOR_UNO.getNombre()) != null) {
                EnumTipoFormatoDeclaracion enumTipoFormatoDeclaracion = EnumTipoFormatoDeclaracion.obtenerEnumPorValorUno(
                        cambioNivelJerarquico.getString(EnumCampos.VALOR_UNO.getNombre()));

                declaracion
                        .getJsonObject(EnumCampos.ENCABEZADO.getNombre())
                        .put(EnumCampos.TIPO_FORMATO.getNombre(), enumTipoFormatoDeclaracion.name());
            }
        }

        VerificadorASImpl.activarFirmado(declaracion, algunoIncompleto, mandados);
    }

    /**
     * Metodo que permite llamar a otros servicios
     *
     * @param llamado: Objeto LlamadoDTO con la informacion del servicio
     * @return: JsonObject con la respuesta mandada por el servicio con la respuesta de cada modulo
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 26/09/2019
     */
    private Single<JsonObject> llamarServicio(LlamadoDTO llamado) {

        return client
                .postAbs(llamado.getUrlAbs())
                .rxSendJsonObject(llamado.getInfo())
                .map(respuesta -> {
                    if (respuesta.statusCode() != 200) {
                        LOGGER.info("\n\n==== Se envio otra respuesta - "
                                + llamado.getServicio().getModulo() + " - " + respuesta.statusCode());
                        LOGGER.info("== Se envia: " + llamado.getInfo());
                        LOGGER.info("== Se obtiene: " + respuesta.bodyAsString() + "\n\n");
                    }
                    return respuesta.bodyAsJsonObject();
                })
                ;
    }

}
