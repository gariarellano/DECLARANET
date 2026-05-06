/**
 * @(#)EnumEnajenacionBien.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los campos de enajenacion de bien
 * @author Miriam Sánchez Sánchez programador07
 * @since 06/11/2019
 */
public enum EnumEnajenacionBien {

	TIPO_BIEN("tipoBien"); 
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
	EnumEnajenacionBien(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}
}
