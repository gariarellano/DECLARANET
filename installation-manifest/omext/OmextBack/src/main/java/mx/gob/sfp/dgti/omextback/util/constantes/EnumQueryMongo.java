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
public enum EnumQueryMongo {

	DATOS_RUSP("datosRusp"),
	DATOS_DECLA("datosDecla"),
	PUNTO("."),
	VACIO(""),

	//query
	ID_ENTE("idEnte"),
	NOMBRE_ENTE("nombreEnte"),

	ACTIVO("activo"),
	TIPO_DECLARACION("tipoObligacion"),
	RAMO("ramo"),
	UR("ur"),
	CLAVE_UA("claveUa"),
	ANIO("anio"),
	CURP("curp"),
	ID_NIVEL_JERARQUICO("idNivelJerarquico"),
	SINDICALIZADO("sindicalizado")
	;

	/**
	 * El valor
	 */
	private final String valor;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumQueryMongo(String valor) {
        this.valor = valor;
    }

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

}
