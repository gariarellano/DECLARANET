/**
 * @(#)Constantes.java 12/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util;

/**
 * Clase con las constantes generales para el proyecto
 *
 * @author Miriam Sanchez Sánchez programador07
 * @since 19/09/2019
 */
public class Constantes {
	private Constantes() {
		throw new IllegalStateException("Utility class");
	}

	public static final String HEADER_CONTENT_TYPE = "content-type";
	public static final String HEADER_CONTENT_APPLICATION_JSON = "application/json; charset=utf-8";

	public static final String TEXT_HTML = "text/html";

	public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
	public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

	public static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "*";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "true";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_METHODS = "POST, GET, OPTIONS, DELETE";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "authorization,content-type, dominio";

	public static final String SERVER_PORT = "SERVER_PORT_VAL_DOM";
	public static final String CONFIG_PORT = "port";

}
