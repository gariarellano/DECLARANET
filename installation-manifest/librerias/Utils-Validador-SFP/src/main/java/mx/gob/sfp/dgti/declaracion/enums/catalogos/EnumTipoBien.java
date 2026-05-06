/**
 * @(#)EnumTipoBien.java 03/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum para el tipo de bien
 *
 * @author Miriam Sánchez Sánchez programador07
 * @since 07/11/2019
 */
public enum EnumTipoBien {

	MUEBLE(1,"MUEBLE"),
	INMUEBLE(2,"INMUEBLE"),
	VEHICULO(3,"VEHÍCULO");
	
	private final Integer id;
	private final String descripcion;

    /**
     * Constructor
     * @param id
     */
	EnumTipoBien(Integer id, String descripcion) {
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
