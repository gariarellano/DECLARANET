/**
 * @EnumModulosPDN.java Mar 20, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.enums;

/**
 * @author Miriam Sanchez Sanchez programador07
 * @modifiedBy programador09
 * @since Mar 20, 2020
 */
public enum ModulosPDN {

	I_DATOS_GENERALES("datosGenerales"),
	I_DOMICILIO_DECLARANTE("domicilioDeclarante"),
	I_DATOS_CURRICULARES("datosCurricularesDeclarante"),
	I_DATOS_EMPLEO("datosEmpleoCargoComision"),
	I_EXPERIENCIA_LABORAL("experienciaLaboral"),
	I_DATOS_PAREJA("datosPareja"),
	I_DATOS_DEPENDIENTE("datosDependienteEconomico"),
	I_INGRESOS_NETOS("ingresos"),
	I_ACTIVIDAD_ANUAL_ANT("actividadAnualAnterior"),
	I_BIENES_INMUEBLES("bienesInmuebles"),
	I_VEHICULOS("vehiculos"),
	I_BIENES_MUEBLES("bienesMuebles"),
	I_INVERSIONES_CUENTAS_VALORES("inversionesCuentasValores"),
	I_ADEUDOS_PASIVOS("adeudosPasivos"),
	I_PRESTAMO_COMODATO("prestamoComodato"),
	II_PARTICIPACION("participacion"),
	II_PARTICIPACION_TOMA_D("participacionTomaDecisiones"),
	II_APOYOS("apoyos"),
	II_REPRESENTACION("representaciones"),
	II_CLIENTES_PRINCIPALES("clientesPrincipales"),
	II_BENEFICIOS_PRIVADOS("beneficiosPrivados"),
	II_FIDEICOMISOS("fideicomisos");
	
	private final String modulo;

    /**
     * Constructor con parametros
     * @param modulo 
	 */
	ModulosPDN(String modulo) {
        this.modulo = modulo;
    }

    /**
     * @return the modulo
     */
	public String getModulo() {
		return modulo;
	}
}
