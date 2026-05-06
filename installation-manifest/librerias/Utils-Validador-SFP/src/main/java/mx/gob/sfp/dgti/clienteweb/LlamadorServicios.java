/**
 * @(#)LlamadorServiciosImpl.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.clienteweb;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.client.WebClient;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Clase para el servicio LlamadorServiciosImpl
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
public class LlamadorServicios{
    private static final Logger logger =Logger.getLogger(LlamadorServicios.class.getName());
    /**
     * WebCLient
     */
    private WebClient client;


    /**
     * Metodo que permite llamar a otros servicios
     *
     * @param llamado: Objeto LlamadoDTO con la informacion del servicio
     * @return: JsonObject con la respuesta mandada por el servicio con la respuesta de cada modulo
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 26/09/2019
     */
    private Single<JsonObject> llamarServicioSingle(LlamadoDTO llamado) {
        return client
                .postAbs(llamado.getUrlAbs())
                .rxSendJsonObject(llamado.getInfo())
                .map(resp -> resp.bodyAsJsonObject())
                ;
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
    public Observable<JsonObject> llamarServicio(WebClient cliente, LlamadoDTO llamado) {
        return cliente
                .postAbs(llamado.getUrlAbs())
                .timeout(10000)
                .rxSendJsonObject(llamado.getInfo())
                //.map(resp -> resp.statusCode()==HttpResponseStatus.OK.code() ?  resp.bodyAsJsonObject(): null)
                .map(resp -> {
                    if (resp.statusCode() != HttpResponseStatus.OK.code()) {
                        logger.info("\n\n==== Respuesta incorrecta - "
                                + llamado.getNombreServicio() + " - " + llamado.getUrlAbs() + " - " + resp.statusCode());
                        logger.info("== Se envia: " + llamado.getInfo());
                        logger.info("== Se obtiene: " + resp.bodyAsString() + "\n\n");
                    }
                    return resp.statusCode() == HttpResponseStatus.OK.code() ?  resp.bodyAsJsonObject(): null;
                })
                .flatMapObservable(json -> Observable.just(json)
                ).doOnError(
                        error ->
                        logger.log(Level.SEVERE, "=== Error: {0}", error.getMessage())
                )
        ;


    }
}
