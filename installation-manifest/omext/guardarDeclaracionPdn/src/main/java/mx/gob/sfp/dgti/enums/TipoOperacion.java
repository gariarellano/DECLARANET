/**
 * TipoOperacion.java Apr 29, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;

/**
 * @author Miriam Sanchez programador07
 * @since Apr 29, 2020
 */
public enum TipoOperacion {

	AGREGAR(EnumTipoOperacion.AGREGAR.name(), "AGREGAR"),
	MODIFICAR(EnumTipoOperacion.MODIFICAR.name(), "MODIFICAR"),
	SIN_CAMBIO(EnumTipoOperacion.SIN_CAMBIO.name(), "SIN_CAMBIOS"),
	BAJA(EnumTipoOperacion.BAJA.name(), "BAJA");
	
	/**
	 * Descripcion que se tiene en el enum de la librerìa de declaranet 
	 */
	private final String clave;
	private final String clavePdn;

    /**
     * Constructor con parametros
     * @param clave
     * @param clavePdn
	 */
	TipoOperacion(String clave, String clavePdn) {
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
