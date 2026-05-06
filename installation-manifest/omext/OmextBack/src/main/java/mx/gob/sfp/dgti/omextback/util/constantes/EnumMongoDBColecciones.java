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
public enum EnumMongoDBColecciones {

	/**
	 * Coleccion de no localizados en RUSP
	 */
	COLECCION_NO_LOCALIZADOS_RUSP("nolocalizadosr"),

	/**
	 * Coleccion de cumplimiento en relacion a Declaranet
	 */
	COLECCION_CUMPLIMIENTO("cumplimiento"),

	/**
	 * Coleccion de instituciones actuales e historicas
	 */
	COLECCION_INSTITUCIONES("instituciones"),

	/**
	 * Coleccion de periodos
	 */
	COLECCION_PERIODOS("periodos"),

	/**
	 * Coleccion de servidores pasados a una vista
	 */
	COLECCION_SERVIDORES_VISTA("servidoresvista"),

	/**
	 * Coleccion de vistas
	 */
	COLECCION_VISTAS("vistas"),

	/**
	 * Coleccion con los datos de firma
	 */
	COLECCION_FIRMANTE("firmante"),

	/**
	 * Coleccion para obtener el id del oficio de las vistas
	 */
	COLECCION_CONTEO_OFICIOS_VISTAS("conteosoficiosvistas"),

	/**
	 * Coleccion con los textos de oficios
	 */
	COLECCION_TEXTOS_OFICIOS("textosoficios"),

	/**
	 * Coleccion preconteos vista principal
	 */
	COLECCION_PRECONTEOS_INICIO("preconteosinicio");

	/**
	 * El valor
	 */
	private final String valor;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumMongoDBColecciones(String valor) {
        this.valor = valor;
    }

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

}
