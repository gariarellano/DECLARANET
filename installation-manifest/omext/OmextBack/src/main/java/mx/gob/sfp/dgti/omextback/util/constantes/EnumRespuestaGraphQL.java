/**
 * @(#)EnumRespuestaGraphQL.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum utilizado para la respuesta de GraphQL
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public enum EnumRespuestaGraphQL {
	DATA("data"),
	ENTES("entes"),
	OBTENER_ENTES("obtenerEntes"),
	ID_ENTE("idEnte"),
	NOMBRE_ENTE("nombreEnte");

	/**
	 * El valor
	 */
	private final String valor;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumRespuestaGraphQL(String valor) {
        this.valor = valor;
    }

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

}
