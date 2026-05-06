/**
 * @(#)EnumCumplimiento.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

import java.util.Arrays;

/**
 * Enum con los tipos de cumplimiento para los servidores publicos
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public enum EnumCumplimiento {
	OBLIGADO("OBLIGADO", "OBLIGADO"),
	CUMPLIO("CUMPLIO", "CUMPLIÓ"),
	PENDIENTE("PENDIENTE", "PENDIENTE"),
	EXTEMPORANEO("EXTEMPORANEO", "EXTEMPORÁNEO");

	/**
	 * El valor
	 */
	private final String valor;

	/**
	 * Valor front
	 */
	private final String valorFront;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumCumplimiento(String valor, String valorFront) {
        this.valor = valor;
        this.valorFront = valorFront;
    }

	public String getValorFront() {
		return valorFront;
	}

	/**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

	public static EnumCumplimiento obtenerEnumDeString(String string) {

		return Arrays.asList(values()).stream()
				.filter(miEnum -> miEnum.valor.equals(string))
				.findFirst()
				.orElse(null);

	}

}
