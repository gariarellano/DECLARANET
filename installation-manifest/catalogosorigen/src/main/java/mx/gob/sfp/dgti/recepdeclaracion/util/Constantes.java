/**
 * @(#)Constantes.java 07/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.recepdeclaracion.util;

/**
 * @author  Miriam Sanchez programador07
 * @since 07/02/2019
 */
public class Constantes {

	private Constantes() {
		throw new IllegalStateException("Utility class");
	}

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
         * inmutable while not expires
         */
        public static final String HEADER_CAHCE_CONTOL_INMUTABLE = "public, max-age=604800, immutable";

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
	 * Server port
	 */
	public static final String SERVER_PORT = "SERVER_PORT";

	/**
	 * Header Authorization
	 */
	public static final String AUTHORIZATION = "Authorization";

}	
