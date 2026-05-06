/**
 * 
 */
package mx.gob.sfp.dgti.enums;

/**
 * @author programador09@sfp.gob.mx
 *
 */
public enum EnumReporte {

	ENUM(01, "ENUM VALOR"),
	/**
	 * EnumTipoDeclaracion ENUM_TIPO_DECLARACION
	 */
	INICIO(0, "INICIO"), 
	CONCLUSION(1, "CONCLUSIÓN"), 
	MODIFICACION(2, "MODIFICACIÓN"), 
	AVISO(6, "AVISO"),
	NOTA(7,"NOTA ACLARATORIA"),
	
	/**
	 * EnumValorConformeA
	 */
	ESCRITURA_PUBLICA(1, "Escritura publica"),
    SENTENCIA(2, "Sentencia"),
    CONTRATO(3, "Contrato"),
    
    /**
     * EnumTipoRemuneracion
     */
    MENSUAL(1, "MENSUAL"),
	ANUAL_ANTERIOR(2, "ANUAL AÑO ANTERIOR"),
	ANUAL_ACTUAL(3, "ANUAL AÑO EN CURSO"),
	
	/**
	 * EnumTipoPersona ENUM_TIPO_PERSONA
	 */
	PERSONA_FISICA(1, "PERSONA FISICA"),
	PERSONA_MORAL(2, "PERSONA MORAL"),
	
	/**
	 * EnumTipoParticipacionF
	 */
	FIDEICOMITENTE(1, "FIDEICOMITENTE"),
	FIDUCIARIO(2, "FIDUCIARIO"),
	FIDEICOMISARIO(3, "FIDEICOMISARIO"),
	COMITE_TECNICO(4, "COMITÉ TÉCNICO"),
	
	/**
	 * EnumTipoFideicomiso
	 */
	PUBLICO(1, "PUBLICO"),
	PRIVADO(2, "PRIVADO"),
	MIXTO(3, "MIXTO"),
	
	/**
	 * EnumTipoDocumento
	 */
	BOLETA(1, "BOLETA"),
	CERTIFICADO(2, "CERTIFICADO"),
	CONSTANCIA(3, "CONSTANCIA"),
	TITULO(4, "TITULO"),
	
	/**
	 * EnumTipoBien
	 */
	MUEBLE(1,"MUEBLE"),
	INMUEBLE(2,"INMUEBLE"),
	VEHICULO(3,"VEHÍCULO"),
	
	/**
	 * EnumParticipante
	 */
	DECLARANTE(1, "DECLARANTE"),
	PAREJA(2, "PAREJA"),
	DEPENDIENTE_ECONOMICO(3, "DEPENDIENTE ECONÓMICO"),
	
	/**
	 * EnumNivelGobierno
	 */
	FEDERAL(1, "FEDERAL"),
    ESTATAL(2, "ESTATAL"),
    MUNICIPAL(3, "MUNICIPAL/ALCALDÍA"),
    
    /**
     * EnumNivelAcademico
     */
    PRIMARIA(1,"PRIMARIA"),
	SECUNDARIA(2,"SECUNDARIA"),
	BACHILLERATO(3,"BACHILLERATO"),
	CARRERA_TECNICA(4,"CARRERA TÉCNICA O COMERCIAL"),
	LICENCIATURA(5,"LICENCIATURA"),
	ESPECIALIDAD(6,"ESPECIALIDAD"),
	MAESTRIA(7,"MAESTRIA"),
	DOCTORADO(8,"DOCTORADO"),
	
	/**
	 * EnumMotivoBaja
	 */
	SIN_DESCRIPCION(0, "Sin descripción"),
	VENTA(1, "Venta"),
	DONACION(2, "Donación"),
	SINIESTRO(3, "Siniestro"),
	OTRO(999999, "OTRO (ESPECIFIQUE)"),
	
	/**
	 * EnumFormaPago
	 */
	CREDITO(1, "CRÉDITO"),
	CONTADO(2, "CONTADO"),
	NO_APLICA(3, "NO APLICA"),
	
	/**
	 * EnumFormaAdquiscion
	 */
	COMPRAVENTA(1, "Compraventa"),
	CESION(2, "Cesión"),
	//DONACION(3, "Donación"),
	HERENCIA(4, "Herencia"),
	PERMUTA(5, "Permuta"),
	RIFA_SORTEO(6, "Rifa o sorteo"),
	//SENTENCIA(7, "Sentencia"),
	
	/**
	 * EnumEstatusEscolaridad
	 */
	FINALIZADO(1, "FINALIZADO"),
	CURSANDO(2, "CURSANDO"), 
	TRUNCO(3, "TRUNCO"),
	
	/**
	 * EnumAmbitoSector
	 */
	//PUBLICO(1, "Público"),
	//PRIVADO(2, "Privado"),
	//OTRO(99, "Otro"),
	
	/***
	 * EnumAmbitoPoder
	 */
	EJECUTIVO(1, "EJECUTIVO"), 
	JUDICIAL(2, "JUDICIAL"), 
	LEGISLATIVO(3, "LEGISLATIVO"),
	ORGANISMO_AUTONOMO(4, "ORGANO AUTÓNOMO"),
	ORGANISMOS_AUTONOMOS(5, "ORGANO AUTÓNOMO"),
	/**
	 * EnumUbicacion ENUM_UBICACION
	 */
	MEXICO(1, "MEXICO"),
    EXTRANJERO(2, "EXTRANJERO"),
    
    /**
     * EnumTipoOperacion
     */
    NINGUNO(1, "NINGUNO"),
	AGREGAR(2, "AGREGAR"),
	MODIFICAR(3, "MODIFICAR"),
	SIN_CAMBIO(4, "SIN CAMBIO"),
	BAJA(5, "BAJA"),
	
	/**
	 * EnumTipoFormatoDeclaracion
	 */
	SIMPLIFICADO(1, "SIMPLIFICADO"), NORMAL(2,"Normal"),COMPLETO(3,"COMPLETO"),
	
	REPRESENTANTE(1,"REPRESENTANTE"),REPRESENTADO(2,"REPRESENTADO"),
	MONETARIO(1,"MONETARIO"),ESPECIE(2,"ESPECIE"),
	
	/**
	 * ENUM_RELACION_CON_DECLARANTE
	 */
	CONYUGE(0, "CÓNYUGE"),
	CONCUBINARIO(1, "CONCUBINA / CONCUBINARIO / UNIÓN LIBRE"),
	SOCIEDAD_CONVIVENCIA(2, "SOCIEDAD EN CONVIVENCIA"),
	
	/**
	 * ENUM_LUGAR_RESIDE
	 */
	SE_DESCONOCE(0, "SE DESCONOCE")
	;
	private final Integer id;
	private final String descripcion;

	/**
	 * @param id
	 * @param descripcion
	 */
	private EnumReporte(Integer id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

}
