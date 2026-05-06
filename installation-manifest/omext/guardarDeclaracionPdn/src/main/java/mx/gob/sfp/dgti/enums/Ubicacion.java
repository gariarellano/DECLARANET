/**
 * Ubicacion.java Mar 31, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

/**
 * @author Miriam Sanchez programador07
 * @since Mar 31, 2020
 */
public enum Ubicacion {

    MEXICO("MX"),
    EXTRANJERO("EX");
	
	private final String clave;

    /**
     * Constructor con parametros
     * @param clave
	 */
	Ubicacion(String clave) {
        this.clave = clave;
    }

    /**
     * @return the clave
     */
	public String getClave() {
		return clave;
	}

}
