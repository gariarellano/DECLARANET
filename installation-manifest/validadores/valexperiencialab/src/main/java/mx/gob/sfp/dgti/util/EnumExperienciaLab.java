/**
 * @(#)EnumExperienciaLab.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util;

/**
 * Enum con los campos del módulo de experiencia laboral
 * @author Miriam Sanchez Sánchez programador07 
 * @since 31/10/2019
 */
public enum EnumExperienciaLab {

	NINGUNO("ninguno"),
	EXPERIENCIA_LABORAL("experienciaLaboral"),
	ACTIVIDAD_LABORAL("actividadLaboral");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
    EnumExperienciaLab(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
