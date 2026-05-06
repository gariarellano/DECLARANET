package mx.gob.sfp.dgti.util;

/**
 * Clase con las constantes generales para el proyecto
 * @author Miriam Sanchez Sánchez programador07 
 * @since 28/08/2019
 */
public class Constantes {
	
	// Headers
	public static final String HEADER_CONTENT_TYPE = "content-type";
	public static final String HEADER_CONTENT_APPLICATION_JSON = "application/json; charset=utf-8";
	public static final String TEXT_HTML = "text/html";
	public static final String TOKEN_BEARER_KEY = "Bearer ";
	public static final String HEADER_DOMINIO = "dominio";
	public static final String HEADER_AUTHORIZATION_KEY = "Authorization";
	public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
	public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "*";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "true";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_METHODS = "POST, GET, OPTIONS, DELETE";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "authorization,content-type";
	public static final String AMBIENTE = "AMBIENTE";
	public static final String CACHE_CONTROL = "Cache-Control";
	public static final String PRAGMA = "Pragma";
	public static final String HEADER_PRAGMA = "no-cache";
	public static final String HEADER_CACHE_CONTROL = "no-store";
	public static final String X_REQUESTED_WITH = "x-requested-with";
	public static final String ACCEPT = "accept";
	public static final String X_PINGARUNER = "X-PINGARUNER";
	public static final String CONFIG_PORT = "port";
	public static final Integer PUERTO = 5000;
	public static final String DOM_CONSULTA_DECLA = "DOM_CONSULTA_DECLA";
	public static final String DOM_CONSULTA_ACUSE = "DOM_CONSULTA_ACUSE";
	public static final String DOM_CONSULTA_DECLA_REPORTE = "DOM_CONSULTA_DECLA_REPORTE";
	public static final String DOM_BITACORA_FIEL = "DOM_BITACORA_FIEL";
	public static final String DOM_GUARDAR_DECLA = "DOM_GUARDAR_DECLA";
	public static final String MENSAJE_ERROR_DECLARACION_DIFERENTE = "La declaración que está intentando firmar no corresponde a la versión que se encuentra guardada";
	public static final String MENSAJE_ERROR_CORREO = "No se pudo enviar el correo, pero la declaración ha sido guardada";
	public static final String ERROR_DECLARACION_MODIFICACION_PERIODO = "La declaración de modificación no podrá ser firmada hasta el 1 de mayo.";
	public static final String EMPTY = "";
	
	public static final String TIEMPO_30MIN = "TIEMPO_30MIN";

	public static final String PROPERTIES = "recepcionFirma.properties";
	
	public static final String PARAMETRO_CLIENTE_ID = "cliente_id";
	
	public static final String URL_FIRMADO = "/api/valida-firmado";
	public static final String URL_FIRMA = "/api/inicia-firma";
	public static final String PATH_RAIZ = "/";
	public static final String JWT_JCEKS = "jceks";
	public static final String JWT_PSW = "secret";
	public static final String KEYSTORE = "keystore.jceks";
	
	public static final String MAS_PARAMETRO = "?";
	public static final String IGUAL = "=";
	public static final String DIAGONAL = "/";
	public static final String DOS_PUNTOS = ":";
	public static final String AMPERSAND = "&";
	public static final String PARAMETRO_RFC = "rfc";
	public static final String PARAMETRO_TRANSACCION = "transaccion";
	public static final Object JSON_VACIO = "{}";
	
	public static final String NUMERO_DECLARACION = "numeroDeclaracion";
	public static final String NUMERO_CERTIFICADO = "numeroCertificado";
	public static final String NOMBRE_FIRM = "nombreFirm";
	public static final String ID_USUARIO = "idUsuario";
	public static final String COLLNAME = "collName";
	public static final String PARAM_FIRMADA = "firmada";
	public static final String ESTATUS = "estatus";
	public static final String TRANSACCION = "transaccion";
	public static final String CURP = "curp";
	public static final String NOMBRE_FIRMA = "nombreFirm";
	public static final String FOLIO = "folio";
	public static final String RESPUESTA_VALIDACION = "respuestaValidacion";
	public static final String CODIGO_SHA = "codigoSha";
	public static final String EXITO = "EXITO";
	public static final Integer CERO = 0;
	public static final String EXT_FIRM = ".firm";
	public static final String DIGEST = "digest";
	public static final String ENCABEZADO = "encabezado";
	public static final String DECLARACION = "declaracion";
	public static final String INSTITUCION_RECEPTORA = "institucionReceptora";
	public static final String ENTE = "ente";
	public static final String NOMBRE = "nombre";
	public static final String USUARIO = "usuario";
	public static final String SHADECLARACION = "shaDeclaracion";
	public static final String DIGITO_VERIFICADOR = "digitoVerificador";
	public static final String MENSAJE = "MENSAJE";
	public static final String ESPACIO = " ";
	public static final String FECHA_ANIO_A_MILLIS = "yyyyMMddHHmmss.SSS";
	public static final String FECHA_HASTA_SEGUNDOS = "yyyyMMddHHmmss";
	public static final String FECHA_FORMATO_CORTA = "yyyyMMdd";
	public static final String PUNTO = "\\.";
	public static final String FORMATO = "%09d";
	public static final String EXT_FUP = ".fup";
	public static final String NO_CERTIFICADO = "0000000000000000";
	public static final String RFC = "rfc";
	public static final String DATOS_GENERALES = "datosGenerales";
	public static final String DATOS_PERSONALES = "datosPersonales";
	public static final String CORREO_ELECTRONICO = "correoElectronico";
	public static final String CORREO_INSTITUCIONAL= "institucional";
	public static final String CORREO_ALTERNO = "personalAlterno";
	public static final String TIPO_DECLARACION = "tipoDeclaracion";
	public static final String DECLARACION_ORIGEN = "declaracionOrigen";
	public static final String ANIO = "anio";
	public static final String FECHA_REGISTRO = "fechaRegistro";
	public static final String ADDRESS = "envio-correo-firma";
	public static final String MENSAJE_ERROR = "mensajeError";
	//Mensajes error de validaciones
	public static final String ERROR_COMUNICACION = "Hubo un error de comunicación";
	public static final String ERROR_VALIDACION_CURP = "La CURP no corresponde con su usuario";
        public static final String ERROR_VALIDACION_CURP_HEADER_DATOS_GENERALES = "La CURP con la que ingresó al sistema no corresponde con la proporcionada en datos generales";
	public static final String ERROR_VALIDACION_RFC = "El rfc no corresponde con su usuario";
	public static final String ERROR_VALIDACION_DECLARACION = "La declaración consultada es inválida";
	public static final String ERROR_AL_FIRMAR = "Error al realizar el firmado, intente nuevamente";
	public static final String ERROR_DATOS_USUARIO = "Se recomienda guardar previo a firmar";
}
