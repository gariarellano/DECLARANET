/**
 * @(#)EnumTipoPersona.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum con los campos de tipo persona
 * @author Miriam Sanchez Sánchez programador07 
 * @since 29/10/2019
 */
public enum EnumTipoPersona {

	PERSONA_FISICA(1, "PERSONA FÍSICA"),
	PERSONA_MORAL(2, "PERSONA MORAL");
	
	private final Integer id;
	private final String descripcion;

    /**
     * Constructor con parametros
     * @param id 
     * @param descripcion
	 */
	EnumTipoPersona(Integer id, String descripcion) {
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
