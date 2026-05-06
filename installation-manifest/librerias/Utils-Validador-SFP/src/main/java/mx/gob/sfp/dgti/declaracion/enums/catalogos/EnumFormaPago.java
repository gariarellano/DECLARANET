/**
 * @(#)EnumFormaPagoDTO.java 11/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum de catálogo de forma de pago
 * @author pavel.martinez
 * @since 11/11/2019
 */
public enum EnumFormaPago {

	CREDITO(1, "CRÉDITO"),
	CONTADO(2, "CONTADO"),
	NO_APLICA(3, "NO APLICA");

	private final Integer id;
	private final String descripcion;

    /**
     * Constructor con parametros
     * @param id
     * @param descripcion
	 */
	EnumFormaPago(Integer id, String descripcion) {
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
