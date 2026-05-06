/**
 * @(#)EnumQueryDSL.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con etiquetas usadas en consultas de Elastic
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public enum EnumTipoColeccion {


	COLECCION_NO_LOCALIZADOS_RUSP("nolocalizadosr"),
	COLECCION_CUMPLIMIENTO("cumplimiento")
	;

	/**
	 * El valor
	 */
	private final String valor;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumTipoColeccion(String valor) {
        this.valor = valor;
    }

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

}
