/**
 * @EnumNivelAcademico.java Nov 19, 2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 19, 2019
 */
public enum EnumNivelAcademico {

	PRIMARIA(1,"PRIMARIA"),
	SECUNDARIA(2,"SECUNDARIA"),
	BACHILLERATO(3,"BACHILLERATO"),
	CARRERA_TECNICA(4,"CARRERA TÉCNICA O COMERCIAL"),
	LICENCIATURA(5,"LICENCIATURA"),
	ESPECIALIDAD(6,"ESPECIALIDAD"),
	MAESTRIA(7,"MAESTRIA"),
	DOCTORADO(8,"DOCTORADO");
	
	private final Integer id;
	private final String descripcion;

    /**
     * Constructor con parametros
     * @param id
     * @param descripcion
	 */
	EnumNivelAcademico(Integer id, String descripcion) {
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
