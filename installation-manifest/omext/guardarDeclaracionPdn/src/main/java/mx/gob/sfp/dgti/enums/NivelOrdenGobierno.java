/**
 * @NivelOrdenGobierno.java Apr 1, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumNivelGobierno;

/**
 * @author Miriam Sanchez programador07
 * @since Apr 1, 2020
 */
public enum NivelOrdenGobierno {

	FEDERAL(EnumNivelGobierno.FEDERAL.name(), "FEDERAL"),
    ESTATAL(EnumNivelGobierno.ESTATAL.name(), "ESTATAL"),
    MUNICIPAL(EnumNivelGobierno.MUNICIPAL.name(), "MUNICIPAL_ALCALDIA");
	
	/**
	 * Descripcion que se tiene en el enum de la librerìa de declaranet 
	 */
	private final String clave;
	private final String clavePdn;

    /**
     * Constructor con parametros
     * @param clave
	 */
	NivelOrdenGobierno(String clave, String clavePdn) {
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
