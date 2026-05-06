/**
 * @(#)EnumServicioFiel.java 23/02/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con etiquetas para el servicio de usuarios fiel
 *
 * @author pavel.martinez
 * @since 23/02/2021
 */
public enum EnumServicioFiel {

	CLIENTE_ID("cliente_id"),
	SECRET_KEY("secret_key"),
	SERVICIO("servicio"),
	CADENA("cadena");

	/**
	 * El valor
	 */
	private final String valor;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumServicioFiel(String valor) {
        this.valor = valor;
    }

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

}
