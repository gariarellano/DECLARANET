/**
 * @(#)EnumMotivoBaja.java 05/12/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum de catálogo de motivo de baja
 * @author pavel.martinez
 * @since 05/12/2019
 */
public enum EnumMotivoBaja {
	SIN_DESCRIPCION(0, "SIN DESCRIPCIÓN"),
	VENTA(1, "VENTA"),
	DONACION(2, "DONACIÓN"),
	SINIESTRO(3, "SINIESTRO"),
	OTRO(9999, "OTRO (ESPECIFIQUE)");

	private final Integer id;
	private final String descripcion;

    /**
     * Constructor con parametros
     * @param id
     * @param descripcion
	 */
	EnumMotivoBaja(Integer id, String descripcion) {
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
