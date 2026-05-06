/**
 * @(#)EnumTipoRemuneracion.java 03/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum para el tipo de remuneracion
 *
 * @author Miriam Sánchez Sánchez programador07
 * @since 05/11/2019
 */
public enum EnumTipoRemuneracion {

	MENSUAL(1, "MENSUAL"),
	ANUAL_ANTERIOR(2, "ANUAL AÑO ANTERIOR"),
	ANUAL_ACTUAL(3, "ANUAL AÑO EN CURSO");

	private final Integer id;
	private final String descripcion;

    /**
     * Constructor
     * @param id
     */
	EnumTipoRemuneracion(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    /**
     * @return the id
     */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	
}

