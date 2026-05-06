/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "GuardaDeclaracion" Sistema que permite realizar el guardado de declaraciones
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
 * @modifyBy  cgarias.
 * @since 20/12/2019
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
    public static final String URL_APPI_VALIDADOR = "/validador";
    public static final String PATH = "/";
    public static final String EMPTY = "";
    public static final String GUION = "-";
    public static final String MESSAGE_TEST_SANITY= "Hola Validador identificadores";

    //Fields_objetos
    public static final String FIELD_ID = "id";
    public static final String FIELD_DECLARACION = "declaracion";
    public static final String FIELD_COLLNAME = "collName";
    public static final String FIELD_INSTITUCION_RECEPTORA = "institucionReceptora";
    public static final String FIELD_NUMERO_DECLARACION = "numeroDeclaracion";
    public static final String FIELD_ENCABEZADO = "encabezado";
    public static final String FIELD_ID_USUARIO = "idUsuario";
    public static final String FIELD_USUARIO = "usuario";
    public static final String FIELD_ACTIVIDAD_ANUAL = "actividadAnual";
    
    //Response whit errors
    public static final String ERROR_ERROR_ID= "errorId";
    public static final String ERROR_MENSAJE ="mensaje";
    public static final String ERROR_MENSAJE_ALTERNO = "mensajeAlterno";
    public static final String ERROR_MENSAJE_IDS_ALTERADOS = "Los identificadores han sido alterados";
    public static final String ERROR_NOMBRE_CAMPO = "nombreCampo";
    public static final String ERROR_PROPIEDAD_VALOR = "propiedadValor";
    public static final String ERROR_LISTA_ERROR_MENSAJE = "listErrorMensajes";
    public static final String ERROR_MODULO = "modulo";
    public static final String ERROR_MODULO_IDS_INCORRECTOS = "IdentificadoresIncorrectos";
    public static final String ERROR_INCOMPLETO = "incompleto";
    public static final String ERROR_ERRORES = "errores";
    
    public static final Pattern PATHERN_DIGITOS = Pattern.compile("\\d+");
    
}
