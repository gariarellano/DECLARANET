/**
 * @(#)EnumOrden.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con los tipos de ordenamiento, ascendente y descendente
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public enum EnumOrden {

	ASC("ASC", 1),
	DESC("DESC", -1);

	/**
	 * El valor
	 */
	private final String valor;

	/**
	 * Valor de campo de Elastic
	 */
	private final Integer campo;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumOrden(String valor, Integer campo) {
        this.valor = valor;
        this.campo = campo;
    }

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

	public Integer getCampo() {
		return campo;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("EnumOrden{");
		sb.append("valor='").append(valor).append('\'');
		sb.append(", campo='").append(campo).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
