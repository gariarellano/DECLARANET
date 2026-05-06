/**
 * @(#)EnumQueryGraphQL.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con etiquetas y queries usadas en consultas de GraphQL
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public enum EnumQueryGraphQL {
	QUERY("query"),
	VARIABLES("variables"),
	FILTRO("filtro"),
	ID("id"),
	PODER("poder"),
	NIVEL_GOBIERNO("nivelGobierno"),
	ENTIDAD_FEDERATIVA("entidadFederativa"),
	MUNICIPIO("municipio"),
	RAMO("ramo"),
	UNIDAD_RESPONSABLE("unidadResponsable"),
	QUERY_OBTENER_ENTES(
			new StringBuilder()
					.append("query obtenerEntes($filtro: FiltroEntes!) \n")
					.append("  {\n")
					.append("  obtenerEntes(filtro: $filtro) {\n")
					.append("    idEnte :id\n")
					.append("    nombreEnte :enteDesc\n")
					.append("    ur : unidadResponsable\n")
					.append("	 nombreCorto\n")
					.append("    poder\n")
					.append("    nivelGobierno {\n")
					.append("      entidadFederativa {\n")
					.append("        entidadFederativaDesc\n")
					.append("        idEntidadFederativa\n")
					.append("      }\n")
					.append("      nivelGobierno\n")
					.append("    }\n")
					.append("    ramo\n")
					.append("  }\n")
					.append("}").toString());

	/**
	 * El valor
	 */
	private final String valor;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumQueryGraphQL(String valor) {
        this.valor = valor;
    }

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

}
