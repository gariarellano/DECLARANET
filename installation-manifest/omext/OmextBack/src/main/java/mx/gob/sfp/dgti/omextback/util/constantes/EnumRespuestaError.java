/**
 * @(#)EnumRespuestaError.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con etiquetas para la respuesta en caso de error
 *
 * @author pavel.martinez
 * @since 05/06/2020
 */
public enum EnumRespuestaError {

	LINEA("\n\n    <==================================>"),
	USER_AGENT("User-Agent"),
	PARAMETROS_ENVIADOS("Parametros enviados : "),
	QUERY("query : "),
	ID("id"),
	OPERACION("Operacion: "),
	DESCRIPCION("descripcion"),
	MENSAJE_GRAPHQL("mensajeGraphQL"),
	DETALLE("detalle");

	/**
	 * El valor
	 */
	private final String valor;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumRespuestaError(String valor) {
        this.valor = valor;
    }

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

}
