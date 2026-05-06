/**
 * Interes.java Apr 17, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

/**
 * @author Miriam Sanchez programador07
 * @since Apr 17, 2020
 */
public enum Interes {

	PARTICIPACION("participacion"),
	TIPO_RELACION("tipoRelacion"),
	PARTICIPANTE("participante"),
	NOMBRE_EMPRESA("nombreEmpresaSociedadAsociacion"),
	PORCENTAJE_PART("porcentajeParticipacion"),
	TIPO_PARTICIPACION("tipoParticipacion"),
	TIPO_PARTICIPACION_OTRO("tipoParticipacionOtro"),
	SECTOR("sector"),
	RECIBE_REMUNERACION("recibeRemuneracion"),
	MONTO_MENSUAL("montoMensual"),
	LOCALIZACION("localizacion"),
	INSTITUCION("institucion"),
	PUESTO_ROL("puestoRol"),
	FECHA_INICIO("fechaInicioParticipacion"),
	TIPO_INSTITUCION("tipoInstitucion"),
	TIPO_INSTITUCION_OTRO("tipoInstitucionOtro"),
	NOMBRE_INSTITUCION("nombreInstitucion"),
    APOYOS("apoyos"),
    BENEFICIARIO_PROGRAMA("beneficiarioPrograma"),
    NOMBRE_PROGRAMA("nombrePrograma"),
    INSTITUCION_OTORGANTE("institucionOtorgante"),
    NIVEL_ORDEN_GOB("nivelOrdenGobierno"),
    TIPO_APOYO("tipoApoyo"),
    FORMA_RECEPCION("formaRecepcion"),
    MONTO_APOYO_MENSUAL("montoApoyoMensual"),
    ESPECIFIQUE_APOYO("especifiqueApoyo"),
    APOYO("apoyo"),
    TIPO_APOYO_OTRO("tipoApoyoOtro"),
    REPRESENTACIONES("representaciones"),
    REPRESENTACION("representacion"),
    TIPO_REPRESENTACION("tipoRepresentacion"),
    FECHA_IN_REPRESENTACION("fechaInicioRepresentacion"),
    REPRES_REPRES("representanteRepresentado"),
    NOMBRE_RAZON_SOCIAL("nombreRazonSocial"),
    REALIZA_ACT_LUC("realizaActividadLucrativa"),
	NOMBRE_EMPRESA_SERVICIO("nombreEmpresaServicio"),
	RFC_EMPRESA("rfcEmpresa"),
	CLIENTE_PRINCIPAL("clientePrincipal"),
	MONTO_APROX_GANANCIA("montoAproximadoGanancia"), 
	CLIENTE("cliente"),
	CLIENTES("clientes"),
	EMPRESA("empresa"),
	BENEFICIOS("beneficios"),
	BENEFICIO("beneficio"),
    TIPO_BENEFICIO("tipoBeneficio"),
    TIPO_BENEFICIO_OTRO("tipoBeneficioOtro"),
    BENEFICIARIO("beneficiario"),
    OTORGANTE("otorgante"),
    ESP_BENEFICIO("especifiqueBeneficio"),
    MONTO_MENSUAL_APROX("montoMensualAproximado"),
    TIPO_FIDEICOMISO("tipoFideicomiso"),
	RFC_FIDEICOMISO("rfcFideicomiso"),
	FIDEICOMITENTE("fideicomitente"),
	FIDUCIARIO("fiduciario"),
	FIDEICOMISARIO("fideicomisario"),
	FIDEICOMISOS("fideicomisos"),
	FIDEICOMISO("fideicomiso"),
	UBICACION("ubicacion"), 
	EXTRANJERO("extranjero")
	;
	
	private final String campo;

	/**
	 * Constructor con parametros
	 * 
	 * @param campo
	 */
	Interes(String campo) {
		this.campo = campo;
	}

	/**
	 * @return the campo
	 */
	public String getCampo() {
		return campo;
	}
}
