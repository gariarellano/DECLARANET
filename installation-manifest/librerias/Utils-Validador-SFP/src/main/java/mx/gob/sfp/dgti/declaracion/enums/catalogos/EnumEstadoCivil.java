/**
 * @EnumEstadoCivil.java Dec 24, 2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * @author Miriam Sanchez Sanchez programador07
 * @since Dec 24, 2019
 */
public enum EnumEstadoCivil {

	SOLTERO(1, "SOLTERO (A)"),
	CASADO(2, "CASADO (A)"),
	DIVORCIADO(3, "DIVORCIADO (A)"),
	VIUDO(4, "VIUDO (A)"),
	CONCUBINO (5, "CONCUBINO (A) / UNION LIBRE"),
	SOCIEDAD_CONVIVENCIA(6, "SOCIEDAD EN CONVIVENCIA");
	
	private Integer id;
	private String descripcion;

	private EnumEstadoCivil(Integer id, String descripcion) {
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
