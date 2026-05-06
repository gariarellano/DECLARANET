/**
 * @(#)Constantes.java 07/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.decnet.util;

/**
 * @author  Miriam Sanchez programador07
 * @since 07/02/2019
 */
public class Constantes {

	/**
	 * Header
	 */
	public static final String HEADER_CONTENT_TYPE = "content-type";

	/**
	 * Header
	 */
	public static final String HEADER_CONTENT_APPLICATION_JSON = "application/json; charset=UTF8";

	/**
	 * Text html
	 */
	public static final String TEXT_HTML = "text/html";

	/**
	 * Control
	 */
	public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

	/**
	 * Credenciales
	 */
	public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

	/**
	 * Metodos permitidos
	 */
	public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";

	/**
	 * Headers permitidos
	 */
	public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

	/**
	 * Headers
	 */
	public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

	/**
	 * Header Origin
	 */
	public static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "*";

	/**
	 * Header credenciales
	 */
	public static final String HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "true";

	/**
	 * Header metodos permitidos
	 */
	public static final String HEADER_ACCESS_CONTROL_ALLOW_METHODS = "POST, GET, OPTIONS, DELETE";

	/**
	 * Header headers permitidos
	 */
	public static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "authorization,content-type";

	/**
	 * Cache-Control
	 */
	public static final String CACHE_CONTROL = "Cache-Control";

	/**
	 * Pragma
	 */
	public static final String PRAGMA = "Pragma";

	/**
	 * no-cache
	 */
	public static final String HEADER_PRAGMA = "no-cache";

	/**
	 * no-store
	 */
	public static final String HEADER_CACHE_CONTROL = "no-store";

	/**
	 * x-request-with
	 */
	public static final String X_REQUESTED_WITH = "x-requested-with";

	/**
	 * accept
	 */
	public static final String ACCEPT = "accept";

	/**
	 * X-PINGARUNER
	 */
	public static final String X_PINGARUNER = "X-PINGARUNER";

	/**
	 * Nombre de la configuracion de host de la base cuando se usa el archivo .properties.
	 */
	public static final String CONFIG_HOST = "host";

	/**
	 * Nombre de la configuracion del puerto de la base cuando se usa el archivo .properties.
	 */
	public static final String CONFIG_PORT = "port";

	/**
	 * Nombre de la configuracion del nombre de a base se usa el archivo .properties.
	 */
	public static final String CONFIG_DATABASE = "database";

	/**
	 * Nombre de la configuracion del nombre de usuario cuando se usa el archivo .properties.
	 */
	public static final String CONFIG_USERNAME = "username";

	/**
	 * Nombre de la configuracion de la contraseña de la base cuando se usa el archivo .properties.
	 */
	public static final String CONFIG_PSW = "password";

	/**
	 * Nombre de la variable de entorno que contiene el host de la base.
	 */
	public static final String CAT_DNET_DATABASE_HOST = "CAT_DNET_DATABASE_HOST";

	/**
	 * Nombre de la variable de entorno que contiene el puerto de la base.
	 */
	public static final String CAT_DNET_DATABASE_PORT = "CAT_DNET_DATABASE_PORT";

	/**
	 * Nombre de la variable de entorno que contiene el nombre de la base de datos.
	 */
	public static final String CAT_DNET_DATABASE_NAME = "CAT_DNET_DATABASE_NAME";

	/**
	 * Nombre de la variable de entorno que contiene el nombre del usuario de la base.
	 */
	public static final String CAT_DNET_DATABASE_USERNAME = "CAT_DNET_DATABASE_USERNAME";

	/**
	 * Nombre de la variable de entorno que contiene la contrasenia a la base.
	 */
	public static final String CAT_DNET_DATABASE_PSW = "CAT_DNET_DATABASE_PASSWORD";

	/**
	 * Nombre de la variable de entorno con la tabla del catalogo que se va a consultar.
	 */
	public static final String CAT_DNET_TABLE = "CAT_DNET_TABLE";

	/**
	 * Server port
	 */
	public static final String SERVER_PORT = "http.port";

	/**
	 * Archivo que contiene propiedades por defecto.
	 */
	public static final String PROPERTIES = "catalogodnet.properties";

	/**
	 * Estado que indica que el registro esta activo en la base de datos.
	 */
	public static final Integer ESTADO_ACTIVO = 1;


}	
