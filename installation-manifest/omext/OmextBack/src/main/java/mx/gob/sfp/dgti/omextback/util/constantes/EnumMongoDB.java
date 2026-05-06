/**
 * @(#)EnumQueryDSL.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con etiquetas usadas en consultas de Elastic
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public enum EnumMongoDB {

	AGGREGATE("aggregate"),
	CURSOR("cursor"),
	ALLOW_DISK_USE("allowDiskUse"),
	BATCH_SIZE("batchSize"),
	READ_PREF("readPref"),
	//READ_PREF
	PIPELINE("pipeline"),
	MATCH("$match"),
	PROJECT("$project"),
	LIMIT("$limit"),
	SKIP("$skip"),
	COUNT("$count"),
	FIRST_BATCH("firstBatch"),
	SUM("$sum"),
	GROUP("$group"),
	MERGE("$merge"),
	TEXT("$text"),
	SEARCH("$search"),
	META("$meta"),
	SCORE("score"),
	TEXT_SCORE("textScore"),
	IN("$in"),
	AND("$and"),
	OR("$or"),
	REGEX("$regex"),
	ADD_FIELDS("$addFields"),
	DATE("$date"),
	EXISTS("$exists"),
	SET("$set"),

	CASE("case"),
	BRANCHES("branches"),
	SWITCH("$switch"),
	EQ("$eq"),
	NOQ("$ne"),
	THEN("then"),
	DEFAULT("default"),

	INTO("into"),
	ON("on"),
	WHEN_MATCHED("whenMatched"),
	WHEN_NOT_MATCHED("whenNotMatched"),

	ID("_id"),
	TOTAL("total"),
	GTE("$gte"),
	LTE("$lte"),
	GT("$gt"),
	LT("$lt"),

	SORT("$sort"),
	FACET("$facet"),

	DATOS_RUSP("datosRusp"),
	DATOS_DECLA("datosDecla"),
	PUNTO("."),
	VACIO(""),

	//query
	EXTEMPORANEO("extemporaneo"),
	ID_ENTE("idEnte"),
	ENTE("ente"),
	NOMBRE_ENTE("nombreEnte"),
	FECHA_REGISTRO_AUX("fechaR"),
	DATE_FROM_STRING("$dateFromString"),
	DATE_STRING("dateString"),
	FECHA_REGISTRO("fechaRegistro"),
	FECHA_ACTUALIZACION("fechaActualizacion"),
	FECHA_LIMITE("fechaLimite"),
	VENCE_FECHA_LIMITE("venceFechaLimite"),

	EN_VISTA("enVista"),

	ACTIVO("activo"),
	ID_TEXTO_OFICIO("idTextoOficio"),
	BODY_TEXT_OFICIO("bodyTextOficio"),
	PRIMER_PARRAFO_OFICIO("primerParrafo"),
	SEGUNDO_PARRAFO_OFICIO("segundoParrafo"),
	TIPO_OBLIGACION("tipoObligacion"),
	TIPO_DECLARACION("tipoDeclaracion"),
	RAMO("ramo"),
	ID_PERIODO("idPeriodo"),
	TIPO_INCUMPLIMIENTO("tipoIncumplimiento"),
	INSTITUCION("institucion"),
	INSTITUCIONES("instituciones"),
	UR("ur"),
	CLAVE_UA("claveUa"),
	UNIDAD_ADMIN("unidadAdministrativa"),

	IMPRESION_NOMBRE("nombre"),
	IMPRESION_CURP("curp"),
	IMPRESION_UA("uniAdm"),
	IMPRESION_EMPLEO("empleo"),
	NOMBRE_COMPLETO_$("$nombreCompleto"),
	UNIDAD_ADMINISTRATIVA_$("$unidadAdministrativa"),
	EMPLEO_$("$empleoCargoComision"),

	ANIO("anio"),
	MES("mes"),
	CURP("curp"),
	NOMBRE("nombres"),
	PRIMER_APELLIDO("primerApellido"),
	SEGUNDO_APELLIDO("segundoApellido"),
	FECHA_INGRESO_INST("fechaIngresoInstitucion"),
	NOMBRE_COMPLETO("nombreCompleto"),

	EXTENSIONES("extensiones"),
	VISTAS_OMISOS_GENERADAS("vistasOmisosGeneradas"),
	VISTAS_GENERADAS("vistasGeneradas"),
	VISTA_GENERADA("vistaGenerada"),
	FIRMA_LISTADO("firmaListado"),
	FIRMA_OFICIO("firmaOficio"),
	USUARIO_CREACION("usuarioCreacion"),

	VENC_FECHA_LIMITE("vencFechaLimite"),
    RESULTADOS("resultados"),
	SERVIDORES("servidores"),
	NUMERO("numero"),
	FOLIO("folio")

	;

	/**
	 * El valor
	 */
	private final String valor;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumMongoDB(String valor) {
        this.valor = valor;
    }

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

}
