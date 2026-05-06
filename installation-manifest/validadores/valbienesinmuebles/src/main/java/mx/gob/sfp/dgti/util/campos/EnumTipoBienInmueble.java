/**
 * @(#)EnumTipoBienInmueble.java 26/02/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util.campos;

/**
 * Enum de catálogo de tipo de bien inmueble
 * 
 * @author pavel.martinez
 * @since 26/02/2020
 */
public enum EnumTipoBienInmueble {
	CASA(1, "CASA"),
	DEPARTAMENTO(2, "DEPARTAMENTO"),
	LOCAL_COMERCIAL(3, "LOCAL COMERCIAL"),
	TERRENO(4, "TERRENO"),
	EDIFICIO(5, "EDIFICIO"),
	PALCO(6, "PALCO"),
	BODEGA(7, "BODEGA"),
	RANCHO(10, "RANCHO"),
	OTRO(9999, "OTRO (ESPECIFIQUE)");

	private Integer id;
	private String descripcion;

	private EnumTipoBienInmueble(Integer id, String descripcion) {
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
