package mx.gob.sfp.dgti.util;

/**
 * Clase con las constantes generales para el proyecto
 * @author Miriam Sanchez Sánchez programador07 
 * @since 07/02/2019
 */
public class Constantes {

	// Mensajes
	public static final String MENSAJE_KEY_ERROR = "Las credenciales de la aplicación son erróneas";
	public static final String MENSAJE_TRANSACCION_ERROR = "La transaccion es erronea";

	// Headers
	public static final String HEADER_CONTENT_TYPE = "content-type";
	public static final String HEADER_CONTENT_APPLICATION_JSON = "application/json; charset=utf-8";
	public static final String TEXT_HTML = "text/html";
	public static final String TOKEN_BEARER_KEY = "Bearer ";
	public static final String HEADER_AUTHORIZATION_KEY = "Authorization";
	public static final String HEADER_DOMINIO = "dominio";
	public static final String HEADER_ORIGEN = "origin";
	public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
	public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "*";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "true";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_METHODS = "POST, GET, OPTIONS, DELETE";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "authorization,content-type, dominio, transaction";
	public static final String HEADER_VALUE_API = "VALUEAPP";
	public static final String HEADER_TRANSACCION = "TRANSACTION";
	public static final String CACHE_CONTROL = "Cache-Control";
	public static final String PRAGMA = "Pragma";
	public static final String HEADER_PRAGMA = "no-cache";
	public static final String HEADER_CACHE_CONTROL = "no-store";
	public static final String X_REQUESTED_WITH = "x-requested-with";
	public static final String ACCEPT = "accept";
	public static final String X_PINGARUNER = "X-PINGARUNER";
	
	// Códigos de las validaciones para login   
	
	public static final Integer VINCULACION_Y_CREDENCIALES_VALIDAS = 1;
	public static final Integer SIN_VINCULACION = 2;
	public static final Integer CREDENCIALES_INVALIDAS = 3;
	public static final Integer INACTIVO_FRONT = 4;
	public static final Integer ESTATUS_RENAPO = 5;
	
	// Códigos de estatus de validado de curps en RENAPO
	
	public static final Integer RENAPO_ESTATUS_NO_VALIDADO = 0;
	public static final Integer RENAPO_ESTATUS_VALIDADO = 1;
	public static final Integer RENAPO_ESTATUS_NO_EXISTE = 2;
	public static final Integer RENAPO_ESTATUS_INCONSISTENTE = 3;
        public static final Integer TELEGRAM_ESTATUS_PRE_VINCULACION = 100;
	
	// Obligatorio validado renapo 
	
	public static final Integer NO_OBLIGATORIO_CURP_VALIDA = 0;
	
	public static final String CURP = "curp";
	public static final String FECHA = "FECHA";
	public static final Object OBJNULL = null;
	public static final String NO_EXISTE = "No existe";
	public static final String PARAMETRO_CLIENTE_ID = "cliente_id";
	public static final String PARAMETRO_CLIENTE_OBLIGATORIO = "obligatorio";
	public static final String PARAMETRO_SECRET_KEY = "secret_key";
	public static final String PARAMETRO_ID_CONFIRMACION = "id";
	public static final Integer ERROR = 99;
        
        //parametros para PDN
        public static final String PARAMETRO_USER_NAME = "username";
        public static final String PARAMETRO_PASSWORD = "password";
        public static final String PARAMETRO_CLIENT_ID = "client_id";
        public static final String PARAMETRO_SECRET= "client_secret";
        public static final String PARAMETRO_SCOPE = "scope";
        public static final String PARAM_TOKEN_ONE_TIME = "token";
        
        public static final String LOCALIZADO = "LOCALIZADO";
        public static final String NO_LOCALIZADO = "NO_LOCALIZADO";
	
	public static final String CONFIG_HOST = "host";
	public static final String CONFIG_PORT = "port";
	public static final String CONFIG_DATABASE = "database";
	public static final String CONFIG_USERNAME = "username";
	public static final String CONFIG_PSW = "password";
	public static final String IP_DATABASE_HOST = "IP_DATABASE_HOST";
	public static final String IP_DATABASE_PORT = "IP_DATABASE_PORT";
	public static final String IP_DATABASE_NAME = "IP_DATABASE_NAME";
	public static final String IP_DATABASE_USERNAME = "IP_DATABASE_USERNAME";
	public static final String IP_DATABASE_PSW = "IP_DATABASE_PASSWORD";
	public static final String IP_SERVER_PORT = "IP_SERVER_PORT";
	public static final String IP_DOMINIO_FRONT = "IP_DOMINIO_FRONT";
	public static final String IP_DOMINIO_IDENTIDAD = "IP_DOMINIO_IDENTIDAD";
	public static final String URL_LOGIN = "URL_LOGIN";
	public static final String URL_ERROR = "URL_ERROR";

	public static final Integer ACTIVO = 1;
	public static final Integer INACTIVO = 0;
	
	// PROCESOS PARA GUARDAR EN LA BITACORA
	public static final Integer LOGIN_APLICACION = 0;
	public static final Integer VINCULACION = 1;
	public static final Integer LOGIN_USUARIO = 2;
	public static final Integer ACCESO = 3;
	public static final Integer DESVINCULACION = 4;
	public static final Integer REGISTRO_USUARIO = 5;
	public static final Integer REGISTRO_APLICACION = 6;
	public static final Integer RECUPERAR_PWD = 7;
	
	public static final String PROPERTIES = "ip.properties";
	
	public static final String KEY_API = System.getenv("KEY_API");
	
	public static final String ADDRESS = "validar-curp-con-renapo";
	public static final String VALIDA_CURP_MENSAJE_NO_EXITOSO = "La CURP no se encuentra en la base de datos";
	public static final String PARAMETRO_ESTATUS_VALIDADO_RENAPO = "estatus";
	public static final String VALIDA_CURP_ESTATUS_NO_EXITOSO = "NO EXITOSO";
	public static final String VALIDA_CURP_ESTATUS_EXITOSO = "EXITOSO";
	
	public static final String USUARIO = "usuario";
	public static final String ISS = "iss";
	public static final String EXP_MINUTOS = "expMinutos";
        public static final String DATA = "data";
	
	public static final String REGEX_PARA_CORREO = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
}
