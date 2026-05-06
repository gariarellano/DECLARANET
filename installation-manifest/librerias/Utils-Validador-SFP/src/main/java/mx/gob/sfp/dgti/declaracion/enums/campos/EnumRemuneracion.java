/**
 * @(#)EnumRemuneracion.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con el campo de remuneracion
 * @author Miriam Sánchez Sánchez programador07
 * @since 06/11/2019
 */
public enum EnumRemuneracion {

	REMUNERACION("remuneracion");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
	EnumRemuneracion(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}
}
