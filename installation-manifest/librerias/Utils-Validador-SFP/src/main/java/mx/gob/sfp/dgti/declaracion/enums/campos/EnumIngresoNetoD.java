/**
 * @(#)EnumIngresoNetoD.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los campos de ingresos netos del declarante
 * @author Miriam Sánchez Sánchez programador07
 * @since 06/11/2019
 */
public enum EnumIngresoNetoD {

	TIPO_REMUNERACION("tipoRemuneracion"),
	REMUNERACION_NETA_CP("remuneracionNetaCargoPublico"),
	OTROS_INGRESOS_T("otrosIngresosTotal"),
	ACTIVIDAD_INDUSTRIAL_CE("actividadIndustrialComercialEmpresarial"),
	ACTIVIDAD_FINANCIERA("actividadFinanciera"),
	SERVICIOS_PROFESIONALES("serviciosProfesionales"),
	OTROS_INGRESOS("otrosIngresos"),
	INGRESO_NETO_D("ingresoNetoDeclarante"),
	INGRESO_NETO_PAREJA_D("ingresoNetoParejaDependiente"),
	TOTAL_INGRESOS_NETOS("totalIngresosNetos"),
	ENAJENACION_BIENES("enajenacionBienes"); 
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
	EnumIngresoNetoD(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}
}
