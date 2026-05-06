/**
 * @(#)EnumNivelOrdenGobierno.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con los nombres para nivel orden gobierno
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public enum EnumNivelOrdenGobierno {

	FEDERAL("FEDERAL"),
	ESTATAL("ESTATAL"),
	MUNICIPAL("MUNICIPAL");

	/**
	 * El valor
	 */
	private final String valor;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumNivelOrdenGobierno(String valor) {
        this.valor = valor;
    }

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

}
