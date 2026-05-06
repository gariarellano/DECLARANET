/**
 * @(#)LoggerUtils.java 06/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils;

import io.vertx.core.json.DecodeException;
import org.apache.log4j.Logger;

/**
 * Utils para el logger
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 06/06/2019
 */
public class LoggerUtils {

    /**
     * Logger
     */
    private static Logger logger =  Logger.getLogger(LoggerUtils.class);

    /**
     * Funcion constructor
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 22/02/2019
     */
    public LoggerUtils() {

    }

    /**
     * Funcion para hacer log de una excepcion
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 06/05/2019
     */
    public static void logError(Exception e) {
        if (e instanceof DecodeException) {
            logger.error("\nOcurrió una excepción por decode:" +
                    "\nmessage: " + e);
        } else {
            logger.error("\nOcurrió una excepción:" +
                    "\nmessage: " + e);
        }
    }

}
