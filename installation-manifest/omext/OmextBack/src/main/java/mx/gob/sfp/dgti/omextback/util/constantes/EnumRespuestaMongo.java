/**
 * @(#)EnumRespuestaDSL.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con etiquetas para la respuesta al query DSL
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public enum EnumRespuestaMongo {

	VALUE("value")
	;

	/**
	 * El valor
	 */
	private final String valor;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumRespuestaMongo(String valor) {
        this.valor = valor;
    }

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

}
