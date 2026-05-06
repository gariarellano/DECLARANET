/**
 * @EnumTipoFideicomiso.java Nov 22, 2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum del catalogo de tipos de fideicomisos
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 22, 2019
 */
public enum EnumTipoFideicomiso {

	PUBLICO(1, "PÚBLICO"),
	PRIVADO(2, "PRIVADO"),
	MIXTO(3, "MIXTO");
	
	private final Integer id;
	private final String descripcion;

    /**
     * Constructor con parametros
     * @param id
     * @param descripcion
	 */
	EnumTipoFideicomiso(Integer id, String descripcion) {
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
