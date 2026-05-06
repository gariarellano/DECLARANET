/**
 * @(#)EnumInversionCB.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util;

/**
 * Enum con los campos del módulo de inversion, cuenta bancaria
 * @author Miriam Sanchez Sánchez programador07 
 * @since 15/11/2019
 */
public enum EnumInversionCB {

	NINGUNO("ninguno"),
	INVERSION("inversion");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
    EnumInversionCB(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
