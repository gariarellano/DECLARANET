/**
 * UnidadMedida.java Apr 28, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

/**
 * @author Miriam Sanchez programador07
 * @since Apr 28, 2020
 */
public enum UnidadMedida {

	METRO_CUADRADO("m2", "METRO CUADRADO"),
	HECTAREA("ha", "HECTÁREA"),
	KILOMETRO_CUADRADO("km2", "KILÓMETRO CUADRADO");
	
	private String clave;
	private String descripcion;
	
	/**
	 * 
	 */
	private UnidadMedida(String clave, String descripcion) {
		this.clave = clave;
		this.descripcion = descripcion;
	}
	
	/**
	 * @return the clave
	 */
	public String getClave() {
		return clave;
	}
	/**
	 * @param clave the clave to set
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
