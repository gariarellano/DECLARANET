/**
 * @(#)EnumModulo.java 28/01/2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.util;

/**
 * Clase con los nombres de los proxies
 * @author pavel
 * @since 28/01/2020
 */
public class Proxies {

    /**
     * Direccion del servicio
     */
    public static final String SERVICE_ADDRESS_GUARDADO = "guardado-declaracion";

    /**
     * Direccion del servicio
     */
    public static final String SERVICE_ADDRESS_LLAMADOR = "llamador-servicios";

    private Proxies() {
        throw new IllegalStateException("Utility class");
    }

}
