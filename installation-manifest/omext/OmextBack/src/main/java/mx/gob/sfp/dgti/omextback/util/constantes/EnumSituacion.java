/**
 * @(#)EnumSituacion.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con los valores de situacion de los entes
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public enum EnumSituacion {

	NORMAL("NORMAL"),
	TRANSICION("TRANSION"),
	LIQUIDACION("LIQUIDACION"),
	INACTIVO("INACTIVO");

	/**
	 * El valor
	 */
	private final String valor;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumSituacion(String valor) {
        this.valor = valor;
    }

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

}
