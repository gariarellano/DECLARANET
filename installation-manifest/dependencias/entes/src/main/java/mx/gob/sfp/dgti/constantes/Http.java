/**
 * @(#)Http.java 11/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.constantes;

/**
 * Constantes para http
 *
 * @author programador07 Miriam  Sánchez
 * @author Pavel Alexei Martinez Regalado aka programador02
 *
 * @since 07/02/2019
 */
public class Http {

    // Headers
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_APPLICATION_JSON = "application/json; charset=utf-8";
    public static final String TOKEN_BEARER_KEY = "Bearer ";
    public static final String HEADER_AUTHORIZATION_KEY = "Authorization";
    public static final String HEADER_ORIGIN = "origin";

    // Mas headers
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    public static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "*";
    public static final String HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "true";
    public static final String HEADER_ACCESS_CONTROL_ALLOW_METHODS = "POST, GET, OPTIONS, DELETE";
    public static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "authorization,content-type";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String PRAGMA = "Pragma";
    public static final String HEADER_PRAGMA = "no-cache";
    public static final String HEADER_CACHE_CONTROL = "no-store";
    public static final String X_REQUESTED_WITH = "x-requested-with";
    public static final String ACCEPT = "accept";
    public static final String X_PINGARUNER = "X-PINGARUNER";

    // Para autorizar por medio de un header y su valor
    /**
     * Nombre del header que debe de existir para hacer uso de algunas funciones.
     */
    public static final String NOMBRE_VARIABLE_HEADER_API_ENTES = "HEADER_API_ENTES";

    /**
     * Nombre del valor del header que debe existir.
     */
    public static final String NOMBRE_VARIABLE_VALOR_API_ENTES = "API_ENTES";

    /**
     * Header que debe de existir para hacer uso de algunas funciones.
     */
    public static final String VARIABLE_ENTORNO_HEADER_API_ENTES = System.getenv(NOMBRE_VARIABLE_HEADER_API_ENTES);

    /**
     * Valor del header que debe existir.
     */
    public static final String VARIABLE_ENTORNO_VALOR_API_ENTES = System.getenv(NOMBRE_VARIABLE_VALOR_API_ENTES);


}
