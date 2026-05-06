/**
 * @EnumTipoParticipacionF.java Nov 22, 2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum del catalogo de tipos de participacion en el fideicomiso
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 22, 2019
 */
public enum EnumTipoParticipacionF {

	FIDEICOMITENTE(1, "FIDEICOMITENTE"),
	FIDUCIARIO(2, "FIDUCIARIO"),
	FIDEICOMISARIO(3, "FIDEICOMISARIO"),
	COMITE_TECNICO(4, "COMITÉ TÉCNICO");

	private final Integer id;
	private final String descripcion;

    /**
     * Constructor con parametros
     * @param id
     * @param descripcion
	 */
	EnumTipoParticipacionF(Integer id, String descripcion) {
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
