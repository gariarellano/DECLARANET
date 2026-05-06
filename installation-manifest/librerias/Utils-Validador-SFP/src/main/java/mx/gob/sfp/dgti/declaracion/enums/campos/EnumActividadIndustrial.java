/**
 * @(#)EnumActividadIndustrial.java 30/05/2019
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
public enum EnumActividadIndustrial {

	NOMBRE_RAZON_SOCIAL("nombreRazonSocial"), 
	TIPO_NEGOCIO("tipoNegocio");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
	EnumActividadIndustrial(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}
}
