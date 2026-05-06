/**
 * @(#)EnumFormaPagoBienDTO.java 15/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum de catálogo de forma de pago
 * @author pavel.martinez
 * @since 15/11/2019
 */
public enum EnumFormaAdquiscion {
	SIN_DESCRIPCION(0, "SIN DESCRIPCIÓN"),
	COMPRAVENTA(1, "COMPRAVENTA"),
	CESION(2, "CESIÓN"),
	DONACION(3, "DONACIÓN"),
	HERENCIA(4, "HERENCIA"),
	PERMUTA(5, "PERMUTA"),
	RIFA_SORTEO(6, "RIFA O SORTEO"),
	SENTENCIA(7, "SENTENCIA");

	private final Integer id;
	private final String descripcion;

    /**
     * Constructor con parametros
     * @param id
     * @param descripcion
	 */
	EnumFormaAdquiscion(Integer id, String descripcion) {
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
