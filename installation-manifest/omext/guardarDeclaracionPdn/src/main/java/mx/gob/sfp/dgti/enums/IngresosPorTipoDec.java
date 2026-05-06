/**
 * IngresosPorTipoDec.java Apr 29, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

/**
 * @author Miriam Sanchez programador07
 * @since Apr 29, 2020
 */
public enum IngresosPorTipoDec {

	REMUNERACION_CARGO_PUBLICO("remuneracionMensualCargoPublico", "remuneracionAnualCargoPublico", "remuneracionConclusionCargoPublico"),
	OTROS_INGRESOS_TOTAL("otrosIngresosMensualesTotal", "otrosIngresosAnualesTotal", "otrosIngresosConclusionTotal"),
	INGRESO_NETO_DEC("ingresoMensualNetoDeclarante", "ingresoAnualNetoDeclarante", "ingresoConclusionNetoDeclarante"),
	TOTAL_INGRESOS_NETOS("totalIngresosMensualesNetos", "totalIngresosAnualesNetos", "totalIngresosConclusionNetos"),
        TOTAL_INGRESOS_NETOS_GENERAL("totalIngresosNetos", "totalIngresosNetos", "totalIngresosNetos");
	
	
	private final String inicio;
	private final String modificacion;
	private final String conclusion;

	/**
     * Constructor con parametros
     * @param inicio 
	 */
	IngresosPorTipoDec(String inicio, String modificacion, String conclusion) {
        this.inicio = inicio;
        this.modificacion = modificacion;
        this.conclusion = conclusion;
    }
	
    /**
	 * @return the inicio
	 */
	public String getInicio() {
		return inicio;
	}

	/**
	 * @return the modificacion
	 */
	public String getModificacion() {
		return modificacion;
	}

	/**
	 * @return the conclusion
	 */
	public String getConclusion() {
		return conclusion;
	}

}