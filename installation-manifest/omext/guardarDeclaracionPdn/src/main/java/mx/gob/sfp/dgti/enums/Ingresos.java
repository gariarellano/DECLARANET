/**
 * Ingresos.java Apr 3, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

/**
 * @author Miriam Sanchez programador07
 * @since Apr 3, 2020
 */
public enum Ingresos {

	INGRESO_NETO_ANUAL("ingresoNetoAnualDeclarante"),
	TOTAL_INGRESOS_ANUALES("totalIngresosNetosAnuales"),
	REMUNERACION_TOTAL("remuneracionTotal"),
	ACTIVIDADES("actividades"), 
	SERVICIOS("servicios"), 
	INGRESOS("ingresos"),
	BIENES("bienes"),
	TIPO_BIEN("tipoBienEnajenado"),
	FECHA_INGRESO("fechaIngreso"),
	ACTIVIDAD_INDUSTRIAL_CE("actividadIndustialComercialEmpresarial"),
	MXN("MXN"),
	TIPO_INSTRUMENTO_OTRO("tipoInstrumentoOtro")
	;
	
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param campo 
	 */
	Ingresos(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}
}
