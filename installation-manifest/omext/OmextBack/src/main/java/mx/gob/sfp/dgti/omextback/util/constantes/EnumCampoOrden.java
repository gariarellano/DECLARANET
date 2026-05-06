/**
 * @(#)EnumCampoOrden.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con los tipos de ordenamiento para consultas
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public enum EnumCampoOrden {
	NOMBRES("NOMBRES", "nombres"),
	PRIMER_APELLIDO("PRIMER_APELLIDO","primerApellido"),
	SEGUNDO_APELLIDO("SEGUNDO_APELLIDO","segundoApellido"),
	CURP("CURP","curp");

	/**
	 * El valor
	 */
	private final String valor;

	/**
	 * El campo de Elastic
	 */
	private final String campo;

	/**
	 * Constructor con parametros
	 * @param valor
	 */
	EnumCampoOrden(String valor, String campo) {

		this.valor = valor;
		this.campo = campo;
	}

	/**
	 * @return the modulo
	 */
	public String getValor() {
		return valor;
	}

	public String getCampo() {
		return campo;
	}
}
