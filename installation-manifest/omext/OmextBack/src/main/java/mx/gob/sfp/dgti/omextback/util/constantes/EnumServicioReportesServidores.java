/**
 * @(#)EnumGraphQL.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con etiquetas usadas para el servicio de reportes
 *
 * @author pavel.martinez
 * @since 08/03/2021
 */
public enum EnumServicioReportesServidores {

	NOMBRE("nombre"),
	COLLNAME("collName"),
	COLECCION("coleccion"),
	CUMPLIMIENTO("cumplimiento"),
	REPORTES("reportes"),
	PIPELINE("pipeline")
	;

	/**
	 * El valor
	 */
	private final String valor;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumServicioReportesServidores(String valor) {
        this.valor = valor;
    }

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

}
