/**
 * @(#)EnumActividadFinanciera.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los campos de actividad industrial
 * @author Miriam Sánchez Sánchez programador07
 * @since 06/11/2019
 */
public enum EnumActividadFinanciera {

	TIPO_INSTRUMENTO("tipoInstrumento"); 
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param campo
	 */
	EnumActividadFinanciera(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}
}
