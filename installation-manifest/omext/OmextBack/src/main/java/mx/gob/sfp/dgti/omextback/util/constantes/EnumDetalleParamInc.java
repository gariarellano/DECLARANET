/**
 * @(#)EnumDetalleParamInc.java 10/06/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con los nombres para los tipos de declaracion
 *
 * @author pavel.martinez
 * @since 10/06/2020
 */
public enum EnumDetalleParamInc {

	MESES_REQUERIDOS("Se requiere establecer el MES o MESES para declaraciones de tipo INICIAL y CONCLUSION."),
	MESES_MODIFICACION("La consulta por tipo de declaracion MODIFICACION no utiliza MES."),
	CURP_NO_VALIDA("La CURP enviada no es válida para la búsqueda."),
	PARAMETROS_NO_VALIDOS("Los parámetros enviados para la búsqueda no son válidos.")
	;

	/**
	 * El valor
	 */
	private final String valor;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumDetalleParamInc(String valor) {
        this.valor = valor;
    }

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

}
