/**
 * @(#)ValidacionException.java 28/01/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util;

/**
 * Clase para excepciones
 *
 * @author pavel.martinez
 * @since 28/01/2020
 */
public class ValidacionException extends Exception {

    public ValidacionException() {
        super("=== Excepcion en validacion");
    }

    public ValidacionException(String message) {
        super(message);
    }
}
