/**
 * @(#)EnumEstatusVista.java 23/02/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con los estatus disponibles del periodo con respecto a la generacion de vistas
 *
 * @author pavel.martinez
 * @since 23/02/2021
 */
public enum EnumEstatusVista {

	NO_GENERADA("NO_GENERADA"),
	EN_PROCESO("EN_PROCESO"),
	GENERADA_SIN_FIRMAS("GENERADA_SIN_FIRMAS"),
	GENERADA("GENERADA");

	/**
	 * El valor
	 */
	private final String valor;


    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumEstatusVista(String valor) {
		this.valor = valor;
	}

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

}
