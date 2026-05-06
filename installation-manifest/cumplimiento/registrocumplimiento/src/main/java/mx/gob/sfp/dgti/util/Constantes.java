/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "GuardaDeclaracion" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.util;

/**
 * Clase con las constantes generales para el proyecto
 * @author Miriam Sanchez Sánchez programador07 
 * @since 19/09/2019
 * @modifyBy  cgarias.
 * @since 20/12/2019
 */
public class Constantes {
    // Headers
    public static final String HEADER_AUTHORIZATION_KEY = "Authorization";
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
    public static final String URL_APPI_REG_CUMP_DECLA = "/api/reg-cump-declaranet/";
    public static final String URL_APPI_REG_CUMP_RUSP = "/api/reg-cump-rusp/";
    public static final String URL_APPI_CONSULTA_OBLIGACION = "/api/consulta-obligacion/:noComprobante/:collName";
    public static final String URL_APPI_REG_CUMP_MANUAL = "/api/registrar-cumplimiento/:idDNetNoLocalizado/:idRusp/:collName";
    public static final String URL_APPI_REG_EXCL_MANUAL = "/api/registrar-cumplimiento/:idRusp/:collName";
    
    public static final String URL_APPI_REV_CUMP_MANUAL = "/api/revertir-cumplimiento/:idDNetNoLocalizado/:idRusp/:collName";
    public static final String PATH = "/";
    public static final String EMPTY = "";
    public static final String GUION = "-";
    public static final String MESSAGE_TEST_SANITY= "Hola Registro de cumplimiento";

    //Fields_objetos
    public static final String FIELD_ID = "id";
    public static final String FIELD_DECLARACION = "declaracion";
    public static final String FIELD_COLLNAME = "collName";
    
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
    public static final String ENV_EXISTE_ENTERECEPTOR_ID_ENTE = "EXISTE_ENTERECEPTOR_ID_ENTE";
    public static final String EXISTE_ENTERECEPTOR_ID_ENTE = System.getenv(ENV_EXISTE_ENTERECEPTOR_ID_ENTE);
    public static final String ENV_ELASTIC_SERCH = "ENV_ELASTIC_SERCH";
    public static final String NO_LOCALIZADOS_DECLA_IN = System.getenv(ENV_ELASTIC_SERCH)+"nolocalizadosd%d/decla/%s";
    public static final String MOVIMIENTOS_RUSP_IN = System.getenv(ENV_ELASTIC_SERCH)+"nolocalizadosr%d/rusp/%s";
    
    public static final String NO_LOCALIZADOS_DECLA_FIND = System.getenv(ENV_ELASTIC_SERCH)+"nolocalizadosd%d/decla/_search";
    public static final String MOVIMIENTOS_RUSP_FIND = System.getenv(ENV_ELASTIC_SERCH)+"nolocalizadosr%d/rusp/_search";
    
//    public static final String MOVIMIENTOS_DECLA_DELETE = System.getenv(ENV_ELASTIC_SERCH)+"nolocalizadosd%d/decla/%s";
//    public static final String MOVIMIENTOS_RUSP_DELETE = System.getenv(ENV_ELASTIC_SERCH)+"nolocalizadosr%d/rusp/%s";
    
    public static final String CUMPLIMIENTO = System.getenv(ENV_ELASTIC_SERCH)+"cumplimiento%d/cumplimiento/%s";
    public static final String VALIDA_CUMPLIMIENTO = System.getenv(ENV_ELASTIC_SERCH)+"cumplimiento%d/_search";
}
