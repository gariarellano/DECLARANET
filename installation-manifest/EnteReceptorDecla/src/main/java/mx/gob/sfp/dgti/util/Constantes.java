/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "EnteReceptorDecla" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.util;

/**
 * Clase con las constantes generales para el proyecto
 *
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
    public static final String CACHE_CONTOL = "Cache-Control";
    public static final String HEADER_CACHE_CONTOL_INMUTABLE = "public, max-age=604800, immutable";
    public static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "*";
    public static final String HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "true";
    public static final String HEADER_ACCESS_CONTROL_ALLOW_METHODS = "POST, GET, OPTIONS, DELETE";
    public static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "authorization,content-type, dominio";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String PRAGMA = "Pragma";
    public static final String SERVER_PORT = "SERVER_PORT";
    public static final String CONFIG_PORT = "port";
    public static final String URL_APPI_CONSULTA_ENTE_RECEPTOR = "/api/ente-receptor/";
    public static final String URL_APPI_CONSULTA_ENTE_RECEPTOR_COLLNAME = "/api/ente-receptor-coll-name/:collName";
    public static final String URL_APPI_CONSULTA_ENTE_RECEPTOR_AS_PARAM = "/api/ente-receptor/:path";
    public static final String URL_APPI_CONSULTA_ENTE_RECEPTOR_POR_ID = "/api/ente-receptor-id/:id";
    public static final String URL_APPI_AGREGA_ENTE_RECEPTOR = "/api/ente-receptor/agrega";
    public static final String URL_APPI_CONSULTAR_ENTE = "/api/consultar-ente/";
    public static final String URL_APPI_CONSULTA_TODOS_ENTES = "/api/consultar-todos/entes/";
    public static final String PATH = "/";
    public static final String PATH_ROUTE = "/*";
    public static final String PATH_API_GRAPHQL = "/api/graphql/";
    public static final String PATH_GRAPHQL_ROUTE ="/graphiql/*";
    public static final String GRAPHIQL = "graphiql";
    public static final String EMPTY = "";
    public static final String GUION = "-";
    public static final String CONSULTA_ENTES = "{\"query\":\"{catalogo:obtenerEntes(filtro:{poder:%s, nivelGobierno:%s %s }){id nombre:enteDesc nivelOrdenGobierno:nivelGobierno{nivelOrdenGobierno:nivelGobierno entidadFederativa {id:idEntidadFederativa valor:entidadFederativaDesc  municipioAlcaldia:municipio{id:idMunicipio valor:municipioDesc}}} ambitoPublico: poder}}\"}";
    public static final String URL_ENTES = "URL_ENTES";
    public final static String NOMBRE_URL_CATALOGOS = "URL_CATALOGOS";
    public static final String ID_CONSULTA = ", id:\\\"%s\\\"";
    public static final String ID_ENTIDAD_FEDERATIVA = ", idEntidadFederativa:";
    public static final String ID_MUNICIPIO = ", idMunicipio:";
    public static final String INSTITUCION_RECEPTORA = "institucionReceptora";
    public static final String ENTE = "ente";
    public static final String ID = "id";
    public static final String DATA = "data";
    public static final String NIVEL_ORDEN_GOB = "nivelOrdenGobierno";
    public static final String ENTIDAD_FED = "entidadFederativa";
    public static final String MUNICIPIO = "municipioAlcaldia";
    public static final String AMBITO_PUB = "ambitoPublico";
    public static final String ERROR_GRAPHQL = "error";
    public static final String ERROR_INTERNO_GRAPHQL = "Error interno";
    public static final String ERROR_QUERYS_GRAPHQL = "=== Problema ejecutando query de GraphQL: ";
    public static final String ERROR_PARAMETROS_GRAPHQL = "=== Parametros con problema: ";
}
