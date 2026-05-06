/**
 * @(#)EnumAmbitoPoder.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum de catálogo de ámbito sector
 * 
 * @author programador04
 * @since 20/11/2019
 */
public enum EnumAmbitoPoder {
	EJECUTIVO(1, "EJECUTIVO"), 
	JUDICIAL(2, "JUDICIAL"), 
	LEGISLATIVO(3, "LEGISLATIVO"),
	ORGANISMOS_AUTONOMOS(4, "ÓRGANO AUTÓNOMO");

	private Integer id;
	private String descripcion;

	private EnumAmbitoPoder(Integer id, String descripcion) {
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
	 * @return the descOrganismo
	 */
	public String getDescripcion() {
		return descripcion;
	}

}
