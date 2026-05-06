/**
 * @(#)GuardadoDeclaracionImpl.java 10/12/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.servicioext;

import io.reactivex.Single;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.serviceproxy.ServiceException;
import mx.gob.sfp.dgti.declaracion.encrypt.im.GeneraMensajeFront;
import mx.gob.sfp.dgti.validaciondeclaracion.dto.LlamadoDTO;
import mx.gob.sfp.dgti.validaciondeclaracion.dto.RespuestaDTO;
import mx.gob.sfp.dgti.validaciondeclaracion.util.Constantes;
import mx.gob.sfp.dgti.validaciondeclaracion.util.EnumCampos;
import mx.gob.sfp.dgti.validaciondeclaracion.util.EnumTipoResp;

/**
 * Clase para el servicio GuardadoDeclaracionImpl
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
public class GuardadoDeclaracionImpl implements GuardadoDeclaracion {

    /**
     * WebCLient
     */
    private WebClient client;

    /**
     * El logger
     */
    static final Logger LOGGER = LoggerFactory.getLogger(GuardadoDeclaracionImpl.class);

    /**
     * Url del servicio de guardado
     */
    private static final String SERVICIO_GUARDADO = System.getenv(Constantes.SERVICIO_GUARDADO) != null ?
            System.getenv(Constantes.SERVICIO_GUARDADO) : "";

    /**
     * Constructor
     *
     * @param vertx: el vertx
     */
    public GuardadoDeclaracionImpl(Vertx vertx) {

        client = WebClient.create(vertx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GuardadoDeclaracion guardarDeclaracion(JsonObject declaracion,
                                             Handler<AsyncResult<RespuestaDTO>> resultHandler){

        guardarDeclaracion(declaracion)
                .subscribe(
                        respuesta -> {
                            resultHandler.handle(Future.succeededFuture(respuesta));
                        },
                        error -> {
                            LOGGER.info("=== Ocurrio algo durante el guardado y se corto flujo: \n" + error.getMessage());

                            RespuestaDTO respuesta = new RespuestaDTO(EnumTipoResp.ERROR_GUARDADO.name());
                            resultHandler.handle(ServiceException.fail(
                                    EnumTipoResp.ERROR_GUARDADO.getId(),
                                    EnumTipoResp.ERROR_GUARDADO.getMensaje(),
                                    respuesta.toJson()));
                        }
                );
        return this;
    }

    /**
     * Metodo que guarda la declaracion
     *
     * @param declaracion JsonObject con la declaracion
     * @return el JsonObject con la declaracion
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 10/12/2019
     */
    private Single<RespuestaDTO> guardarDeclaracion(JsonObject declaracion) {

        LlamadoDTO llamadoGuardar = new LlamadoDTO(
                EnumCampos.SERVICIO_GUARDAR_DECLARACION.getNombre(),
                SERVICIO_GUARDADO,
                declaracion);
        return llamarServicio(llamadoGuardar)
                .flatMap(objetoGuardado -> {

                    if (objetoGuardado.getString(EnumCampos.MENSAJE_ERROR_EN_GUARDADO.getNombre()) != null) {
                        return Single.error(
                                new Exception(objetoGuardado.getString(
                                        EnumCampos.MENSAJE_ERROR_EN_GUARDADO.getNombre())));
                    }
                    String fidelidad = GeneraMensajeFront.fidelidadMensaje(objetoGuardado).toString();
                    RespuestaDTO respuesta = new RespuestaDTO(EnumTipoResp.CORRECTO.name(), fidelidad);
                    return Single.just(respuesta);
                });
    }

    /**
     * Metodo que permite llama a otro servicio
     *
     * @param llamado: Objeto LlamadoDTO con la informacion de la declaracion
     * @return: JsonObject con la respuesta
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 10/12/2019
     */
    private Single<JsonObject> llamarServicio(LlamadoDTO llamado) {

        return client
                .postAbs(llamado.getUrlAbs())
                .rxSendJsonObject(llamado.getInfo())
                .map(HttpResponse::bodyAsJsonObject)
                ;
    }

}
