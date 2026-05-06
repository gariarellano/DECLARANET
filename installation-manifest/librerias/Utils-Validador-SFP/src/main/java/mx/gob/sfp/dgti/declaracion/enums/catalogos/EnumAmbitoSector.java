/**
 * @(#)EnumAmbitoSector.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum de catálogo de ámbito sector
 * @author Miriam Sanchez Sánchez programador07 
 * @since 31/10/2019
 */
public enum EnumAmbitoSector {

	PUBLICO(1, "PUBLICO"),
	PRIVADO(2, "PRIVADO"),
	OTRO(9999, "OTRO (ESPECIFIQUE)");
	
	private final Integer id;
	private final String descripcion;

    /**
     * Constructor con parametros
     * @param id
     * @param descripcion
	 */
	EnumAmbitoSector(Integer id, String descripcion) {
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
