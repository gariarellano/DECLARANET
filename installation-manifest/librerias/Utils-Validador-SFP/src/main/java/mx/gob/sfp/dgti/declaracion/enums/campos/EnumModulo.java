/**
 * @(#)EnumModulo.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los nombres para los módulos
 * @author Miriam Sanchez Sánchez programador07 
 * @since 29/10/2019
 */
public enum EnumModulo {
        
	I_DATOS_GENERALES("datosGenerales"),
	I_DOMICILIO_DECLARANTE("domicilioDeclarante"),
	I_DATOS_CURRICULARES("datosCurricularesDeclarante"),
	I_DATOS_EMPLEO("datosEmpleoCargoComision"),
	I_EXPERIENCIA_LABORAL("experienciasLaborales"),
	I_DATOS_PAREJA("datosParejas"),
	I_DATOS_DEPENDIENTE("datosDependientesEconomicos"),
	I_INGRESOS_NETOS("ingresos"),
	I_ACTIVIDAD_ANUAL_ANT("actividadAnualAnterior"),
	I_BIENES_INMUEBLES("bienesInmuebles"),
	I_VEHICULOS("vehiculos"),
	I_BIENES_MUEBLES("bienesMuebles"),
	I_INVERSIONES("inversionesCuentasValores"),
	I_ADEUDOS_PASIVOS("adeudosPasivos"),
	I_PRESTAMO_COMODATO("prestamoComodato"),
	II_PARTICIPACION_EMPRESAS("participaEmpresasSociedadesAsociaciones"),
	II_PARTICIPACION_INSTITUCIONES("participacionTomaDecisiones"),
	II_APOYOS_BENEFICIOS("apoyos"),
	II_REPRESENTACION("representaciones"),
	II_CLIENTES_PRINCIPALES("clientesPrincipales"),
	II_BENEFICIOS_PRIVADOS("beneficiosPrivados"),
	II_FIDEICOMISOS("fideicomisos");
	
	private final String modulo;

    /**
     * Constructor con parametros
     * @param modulo 
	 */
	EnumModulo(String modulo) {
        this.modulo = modulo;
    }

    /**
     * @return the modulo
     */
	public String getModulo() {
		return modulo;
	}
}
