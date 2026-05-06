/**
 * @(#)EnumCoopropiedad.java 07/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum que indica si hay coopropiedad o no con el declarante
 *
 * @author pavel.martinez
 * @since 07/11/2019
 */
public enum EnumCoopropiedad {

	NO_COOPROPIEDAD("D"),
	COOPROPIEDAD("DC");

	private final String descripcion;

    /**
     * Constructor con parametros
	 */
    EnumCoopropiedad(String descripcion) {
        this.descripcion = descripcion;
    }

	public String getDescripcion() {
		return descripcion;
	}
}
