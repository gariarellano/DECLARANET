/**
 * @(#)EnumSexo.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con valores de sexo
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public enum EnumSexo {

	HOMBRE("HOMBRE"),
	MUJER("MUJER");

	/**
	 * El valor
	 */
	private final String valor;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumSexo(String valor) {
        this.valor = valor;
    }

    /**
     * @return the valor
     */
	public String getValor() {
		return valor;
	}

}
