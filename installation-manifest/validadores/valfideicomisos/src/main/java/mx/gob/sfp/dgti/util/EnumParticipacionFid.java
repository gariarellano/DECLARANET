/**
 * @(#)EnumParticipacionFid.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util;

/**
 * Enum con los campos del módulo de fideicomiso
 * @author Miriam Sanchez Sánchez programador07 
 * @since 22/11/2019
 */
public enum EnumParticipacionFid {

	NINGUNO("ninguno"),
	FIDEICOMISO("fideicomiso");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
    EnumParticipacionFid(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
