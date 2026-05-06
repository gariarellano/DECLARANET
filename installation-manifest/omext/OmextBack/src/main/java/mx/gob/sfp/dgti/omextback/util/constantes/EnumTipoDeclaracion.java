/**
 * @(#)EnumTipoDeclaracion.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con los nombres para los tipos de declaracion para Declaranet
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public enum EnumTipoDeclaracion {

	INICIO("INICIO", "ALTA", "Inicio"),
	MODIFICACION("MODIFICACION", "ACTIVO_MAYO", "Modificación"),
	CONCLUSION("CONCLUSION", "BAJA" , "Conclusión"),
	AVISO("AVISO", "", "Aviso");

	/**
	 * El valor
	 */
	private final String valor;

	/**
	 * Nombre equivalente para RUSP
	 */
	private final String nombreRusp;

	/**
	 * Valor que coincide con el de front
	 */
	private final String valorFront;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumTipoDeclaracion(String valor, String nombreRusp, String valorFront) {

		this.valor = valor;
		this.nombreRusp = nombreRusp;
		this.valorFront = valorFront;
    }

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

	public String getNombreRusp() {
		return nombreRusp;
	}

	public String getValorFront() {
		return valorFront;
	}
}
