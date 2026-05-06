/**
 * @(#)EnumGraphQL.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con etiquetas usadas en el schema de GraphQL
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public enum EnumGraphQL {

	FILTRO("filtro"),

	VISTAS_OMISOS("vistasOmisos"),
	NAME("name"),
	VALUE("value"),
	OBTENER_SERVIDORES("obtenerServidores"),
	BUSCAR_SERVIDORES("buscarServidores"),
	OBTENER_INFO_GRAF("obtenerInformacionGraficas"),
	OBTENER_INFO_INST("obtenerInformacionInst"),
	OBTENER_INFO_UA("obtenerInformacionUA"),
	GENERAR_REPORTE_SERV("generarReporteServidores"),
	GENERAR_IMPRESION_VISTA_POR_FOLIO("generarImpresionVistaPorFolio"),
	OBTENER_INFO_GRUPO("obtenerInformacionGrupos"),

	OBTENER_PERIODO("obtenerPeriodos"),
	OBTENER_INST_PERIODO("obtenerInstPeriodo"),
	OBTENER_VISTA("obtenerVistas"),
	OBTENER_SERVIDORES_VISTA("obtenerServidoresVista"),
	OBTENER_VISTA_PENDIENTES_USUARIO("obtenerVistasPendientesUsuario"),

	EXTENDER_PERIODO("extenderPeriodo"),
	EXTENDER_PERIODO_INST("extenderPeriodoInst"),
	GENERAR_VISTAS_OMISOS("generarVistasOmisos"),
	GUARDAR_PERIODO("guardarPeriodo"),
	TERMINAR_PROCESO_VISTA("terminarProcesoVista"),

	ID_ENTE("idEnte"),
	CLAVE_UA("claveUa"),
	UNIDAD_ADMIN("unidadAdministrativa"),
	UR("ur"),
	RAMO("ramo"),
	NOMBRE_CORTO("nombreCorto"),
	PODER("poder"),
	NIVEL_GOBIERNO("nivelGobierno"),
	PAGINACION("paginacion"),
	OFFSET("offset"),
	TAMANIO("tamanio"),
	TOTAL("total"),
	VISTAS("vistas"),
	SERVIDORES_PUBLICOS("servidoresPublicos"),
	RESULTADO("resultado"),
	INSTITUCIONES("instituciones"),
	NOMBRE_ENTE("nombreEnte"),
	SITUACION("situacion"),
	OBLIGADO("obligado"),
	PENDIENTE("pendiente"),
	CUMPLIO("cumplio"),
	EXCLUIDO("excluido"),
	EXTEMPORANEO("extemporaneo"),
	PORCENTAJE("porcCumplimiento"),
	ACTIVO("activo"),
	FECHA_REGISTRO("fechaRegistro"),
	DATOS_RUSP("datosRusp"),
	CUMPLIMIENTO("cumplimiento"),
	EXT_PERIODO("extensionPeriodo"),
	EXTENSION("extension"),

	VISTA_O_GENERADA("vistaOmisosGenerada"),
	VISTA_E_GENERADA("vistaExtGenerada"),

	TIPO_DECLARACION_DESC("tipoDeclaracionDesc"),
	CUMPLIMIENTO_DESC("cumplimientoDesc"),

	PERIODO("periodo"),
	PERIODOS("periodos"),
	PARAMS("params"),

	SCORE("scoreBusq");




	/**
	 * El valor
	 */
	private final String valor;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumGraphQL(String valor) {
        this.valor = valor;
    }

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

}
