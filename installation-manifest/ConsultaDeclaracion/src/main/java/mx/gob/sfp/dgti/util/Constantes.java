/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "EnteReceptorDecla" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.util;

import java.util.regex.Pattern;

/**
 * Clase con las constantes generales para el proyecto
 * @author Miriam Sanchez Sánchez programador07 
 * @since 19/09/2019
 */
public class Constantes {

	// Headers
	public static final String HEADER_CONTENT_TYPE = "content-type";
	public static final String HEADER_CONTENT_APPLICATION_JSON = "application/json; charset=utf-8";
	public static final String TEXT_HTML = "text/html";
	public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
	public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "*";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "true";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_METHODS = "POST, GET, OPTIONS, DELETE";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "authorization,content-type, dominio";
	public static final String CACHE_CONTROL = "Cache-Control";
	public static final String PRAGMA = "Pragma";
	public static final String SERVER_PORT = "SERVER_PORT";
	public static final String CONFIG_PORT = "port";
	public static final String URL_APPI_CONSULTA_DECLARACION = "/api/consulta-declaracion/";
	public static final String URL_APPI_CONSULTA_FIRMADO_DECLARACION = "/api/consulta-firmado-declaracion/";
	public static final String URL_APPI_CONSULTA_DECLARACION_SERVICIO_IMPRESION = "/api/consulta-declaracion-si/";
        public static final String URL_APPI_CONSULTA_PRECARGA = "/api/consulta-precarga/";
        public static final String URL_APPI_CONSULTA_DECLA_HIST = "/api/consulta-declaracion-historico";
        public static final String PATH_CONSULTAR_HISTORIAL = "/api/consulta-historial-precarga/";
        public static final String URL_APPI_CONSULTA_DECLARACION_DIRECCIONES = "/api/consulta-declaracion-direccion/";
        public static final String URL_APPI_REALIZA_REPORTE_PERIODO = "/api/reporta-periodo";
	public static final String PATH = "/";
	public static final String EMPTY = "";
	public static final String GUION = "-";
        public static final String URL_PRECARGA_HISTORICO = "URL_PRECARGA_HISTORICO";
        public static final String NUM_DECLARACION = "numDeclaracion";
	
        public static final  String ERROR_FIDELIDAD ="Error al validar la fidelidad de la petición";
        public static final  String ERROR_CONSULTA_HISTORIAL_DECLA = "Error consultando el historial de declaraciones";
	public static final  String ERROR_VERIFICA_DECLA_EN_PROCESO = "Erro verificando las declaración en proceso";
	public static final  String ERROR_OBTENER_HISTORIAL = "Error al obtener el historial de declaraciones";
        
        public static final String FIELD_NUMERO_DECLARACION = "numeroDeclaracion";
        public static final String FIELD_NUMERO_DECLARACION_PRECARGA = "numeroDeclaracionPrecarga";
        
        public static final String FIELD_ID_USUARIO = "idUsuario";
        public static final String FIELD_USUARIO = "usuario";
        public static final String FIELD_COLLECTION_NAME = "collName";
        public static final  String FIELD_CURP = "curp";
        public static final Pattern PATHERN_DIGITOS = Pattern.compile("\\d+");
        
        public static final String VERTICLE_REPORTA_DATOS_POR_PERIODO = "VERTICLE_REPORTA_DATOS_POR_PERIODO";
}

