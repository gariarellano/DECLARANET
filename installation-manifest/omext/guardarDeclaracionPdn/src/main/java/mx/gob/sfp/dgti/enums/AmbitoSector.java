/**
 * AmbitoSector.java Apr 2, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumAmbitoSector;

/**
 * @author Miriam Sanchez programador07
 * @since Apr 2, 2020
 */
public enum AmbitoSector {
	
	PUBLICO(EnumAmbitoSector.PUBLICO.name(), "PUB"),
	PRIVADO(EnumAmbitoSector.PRIVADO.name(), "PRV"),
	OTRO(EnumAmbitoSector.OTRO.name(), "OTR");

	/**
	 * Descripcion que se tiene en el enum de la librerìa de declaranet 
	 */
	private final String clave;
	private final String clavePdn;

    /**
     * Constructor con parametros
     * @param clave
	 */
	AmbitoSector(String clave, String clavePdn) {
        this.clave = clave;
        this.clavePdn = clavePdn;
    }

    /**
	 * @return the clavePdn
	 */
	public String getClavePdn() {
		return clavePdn;
	}

	/**
     * @return the clave
     */
	public String getClave() {
		return clave;
	}

}
