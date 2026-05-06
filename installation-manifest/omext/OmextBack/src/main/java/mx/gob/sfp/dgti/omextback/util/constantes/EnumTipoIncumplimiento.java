/**
 * @(#)EnumTipoDeclaracion.java 02/02/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con los tipos de incumplimiento
 *
 * @author pavel.martinez
 * @since 02/02/2021
 */
public enum EnumTipoIncumplimiento {

	OMISO("OMISO"),
	EXTEMPORANEO("EXTEMPORANEO");

	EnumTipoIncumplimiento(String valor) {
		this.valor = valor;
	}

	/**
	 * El valor
	 */
	private final String valor;

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

}
