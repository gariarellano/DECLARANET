/**
 * @(#)EnumTipoOperacion.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum de estatus de escolaridad
 * @author Miriam Sanchez Sánchez programador07 
 * @since 29/10/2019
 */
public enum EnumTipoOperacion {

	//NINGUNO(1, "NINGUNO"),
	AGREGAR(1, "AGREGAR"),
	MODIFICAR(2, "MODIFICAR"),
	SIN_CAMBIO(3, "SIN CAMBIO"),
	BAJA(4, "BAJA");
	
	
	private final Integer id;
	private final String descripcion;
	
    /**
     * Constructor con parametros
     * @param id
	 */
	EnumTipoOperacion(Integer id, String descripcion) {
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
