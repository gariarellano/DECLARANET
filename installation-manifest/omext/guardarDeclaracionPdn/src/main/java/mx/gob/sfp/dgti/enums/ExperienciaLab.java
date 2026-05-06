/**
 * @(#)EnumExperienciaLab.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

/**
 * Enum con los campos del módulo de experiencia laboral
 * @author Miriam Sanchez Sánchez programador07 
 * @since 31/10/2019
 */
public enum ExperienciaLab {

	NINGUNO("ninguno"),
	EXPERIENCIA_LABORAL("experienciaLaboral"),
	ACTIVIDAD_LABORAL("actividadLaboral"),
	EXPERIENCIA("experiencia"),
	SECTOR_OTRO("sectorOtro"),
	AMBITO_SECTOR_OTRO("ambitoSectorOtro");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
    ExperienciaLab(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
