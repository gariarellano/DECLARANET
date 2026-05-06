/**
 * @(#)SFPException.java 03/06/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.exception;

import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.serviceproxy.ServiceException;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumError;

/**
 * Excepcion base
 *
 * @author pavel.martinez
 * @since 03/06/2020
 */
public class SFPException extends ServiceException {

    public static String STATUS_HTTP = "statusHttp";

    public static String DESCRIPCION = "descripcion";

    public static String MENSAJE = "mensaje";



    public SFPException(EnumError error, String message) {
        super(error.getId(),
                error.getId() + " - " + error.getDescripcion() + " - " + "Mensaje : " + message,
                new JsonObject()
                        .put(STATUS_HTTP, error.getStatusHttp())
                        .put(DESCRIPCION, error.getDescripcion())
                        .put(MENSAJE, message));
    }

    public SFPException(EnumError error, Throwable cause) {
        super(error.getId(),
                error.getId() + " - " + error.getDescripcion() + " - " + "Excepcion : " + cause +
                        " - Causa : " + cause.getCause() + " - Mensaje: " + cause.getMessage(),
                new JsonObject()
                        .put(STATUS_HTTP, error.getStatusHttp())
                        .put(DESCRIPCION, error.getDescripcion()));
    }

    public SFPException(EnumError error) {
        super(error.getId(),
                error.getId() + " - " + error.getDescripcion(),
                new JsonObject()
                        .put(STATUS_HTTP, error.getStatusHttp())
                        .put(DESCRIPCION, error.getDescripcion()));
    }

    public void logError(Logger logger) {
        logger.info("==> Ocurrio un error: " .concat(this.getMessage()));
    }

}
