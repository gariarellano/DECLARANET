/**
 * AmbitoPublico.java Apr 1, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumAmbitoPoder;

/**
 * @author Miriam Sanchez programador07
 * @since Apr 1, 2020
 */
public enum AmbitoPublico {

	EJECUTIVO(EnumAmbitoPoder.EJECUTIVO.name(), "EJECUTIVO"), 
	JUDICIAL(EnumAmbitoPoder.JUDICIAL.name(), "JUDICIAL"), 
	LEGISLATIVO(EnumAmbitoPoder.LEGISLATIVO.name(), "LEGISLATIVO"),
	ORGANISMOS_AUTONOMOS(EnumAmbitoPoder.ORGANISMOS_AUTONOMOS.name(), "ORGANO_AUTONOMO")
	;


	/**
	 * Descripcion que se tiene en el enum de la librerìa de declaranet 
	 */
	private final String clave;
	private final String clavePdn;

    /**
     * Constructor con parametros
     * @param clave
	 */
	AmbitoPublico(String clave, String clavePdn) {
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
