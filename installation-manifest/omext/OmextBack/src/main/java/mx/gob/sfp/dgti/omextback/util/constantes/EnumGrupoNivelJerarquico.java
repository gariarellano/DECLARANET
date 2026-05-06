/**
 * @(#)EnumGrupoNivelJerarquico.java 31/07/2020
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
 * @since 31/07/2020
 */
public enum EnumGrupoNivelJerarquico {

	NJ_31_07_20("NJ_31_07_20"),
	NJ_31_12_20("NJ_31_12_20");

	/**
	 * El valor
	 */
	private final String valor;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumGrupoNivelJerarquico(String valor) {
        this.valor = valor;
    }

    /**
     * @return the valor
     */
	public String getValor() {
		return valor;
	}

}
