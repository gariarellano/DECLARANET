/**
 * @(#)EnumSiNo.java 10/08/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con valores de SI O NO
 *
 * @author pavel.martinez
 * @since 10/08/2020
 */
public enum EnumSindicalizados {

	SOLO_SINDICALIZADOS("SOLO_SINDICALIZADOS", true),
	SIN_SINDICALIZADOS("SIN_SINDICALIZADOS", false),
	INCLUIR_SINDICALIZADOS("INCLUIR_SINDICALIZADOS", true);

	/**
	 * El valor
	 */
	private final String valor;

	/**
	 * Valor en MongoDB
	 */
	private final Boolean valorMongo;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumSindicalizados(String valor, boolean valorMongo) {
		this.valor = valor;
		this.valorMongo = valorMongo;
	}

	/**
     * @return the valor
     */
	public String getValor() {
		return valor;
	}

	public Boolean getValorMongo() {
		return valorMongo;
	}
}
