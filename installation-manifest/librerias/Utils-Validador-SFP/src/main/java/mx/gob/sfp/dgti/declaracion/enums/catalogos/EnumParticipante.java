/**
 * @EnumParticipante.java Nov 22, 2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum del catalogo para participantes del fideicomiso y clientes principales
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 22, 2019
 */
public enum EnumParticipante {

	DECLARANTE(1, "DECLARANTE"),
	PAREJA(2, "PAREJA"),
	DEPENDIENTE_ECONOMICO(3, "DEPENDIENTE ECONÓMICO");
	
	private final Integer id;
	private final String descripcion;

    /**
     * Constructor con parametros
     * @param id
     * @param descripcion
	 */
	EnumParticipante(Integer id, String descripcion) {
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
