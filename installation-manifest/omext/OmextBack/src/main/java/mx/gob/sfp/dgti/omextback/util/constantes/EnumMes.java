/**
 * @(#)EnumMes.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con los meses disponibles para hacer consultas
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public enum EnumMes {

	ENERO("ENERO","01"),
	FEBRERO("FEBRERO","02"),
	MARZO("MARZO","03"),
	ABRIL("ABRIL","04"),
	MAYO("MAYO","05"),
	JUNIO("JUNIO","06"),
	JULIO("JULIO","07"),
	AGOSTO("AGOSTO","08"),
	SEPTIEMBRE("SEPTIEMBRE","09"),
	OCTUBRE("OCTUBRE","10"),
	NOVIEMBRE("NOVIEMBRE","11"),
	DICIEMBRE("DICIEMBRE","12");

	/**
	 * El valor
	 */
	private final String valor;

	/**
	 * Numero correspondiente al mes a fines practicos
	 */
	private final String num;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumMes(String valor, String num) {
		this.valor = valor;
		this.num = num;
	}

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

	public String getNum() {
		return num;
	}
}
