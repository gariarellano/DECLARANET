/**
 * @(#)EnumGeneral.java 03/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los campos de general
 * @author pavel.martinez
 * @since 03/11/2019
 */
public enum EnumGeneral {

	ACLARACIONES("aclaracionesObservaciones"),
	TIPO_OPERACION("tipoOperacion"),
	TIPO_DECLARACION("tipoDeclaracion"),
	ID_POSICION_VISTA("idPosicionVista"),
	ENCABEZADO("encabezado"),
	ID("id");

	private final String campo;

	/**
	 * Constructor con parametros
	 * @param campo
	 */
	EnumGeneral(String campo) {
		this.campo = campo;
	}

	/**
	 * @return the campo
	 */
	public String getCampo() {
		return campo;
	}

}
