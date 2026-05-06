/**
 * @Constantes.java Mar 23, 2020
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
 * @modifiedBy programador09
 * @since 19/09/2019
 */
public class Constantes {
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
    public static final String HEADER_ACCESS_CONTROL_ALLOW_METHODS = "POST, GET, OPTIONS";
    public static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "authorization,content-type";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String PRAGMA = "Pragma";
    public static final String SERVER_PORT = "SERVER_PORT";
    public static final String CONFIG_PORT = "port";
    public static final String EMPTY = "";
    public static final String TYPE = "/_doc/";
    public static final String DECLARACION = "declaracion";
    public static final String MAPPING = "/_mapping";
    public static final String INDEX = "index";

    public static final String PATH = "/";
    public static final String PATH_GUARDADO = "/api/guarda-declaracion";
    public static final String PATH_GENERAR_INDEX = "/api/genera-index";
    public static final String PATH_CONSULTAR_CONF_INDEX = "/api/get-config";
    public static final String PATH_CONSULTAR_INDEX = "/api/index";
    public static final String NUMERO_DECLARACION = "numeroDeclaracion";
    public static final String COLLNAME = "collname";
    public static final String ERROR = "error";
    public static final String ID_USUARIO = "idUsuario";
    
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String MAX_POOL_SIZE = "maxPoolSize";
    public static final String MIN_POOL_SIZE = "minPoolSize";
    public static final String USER_NAME = "username";
    public static final String P_W_D = "password";
    public static final String DB_NAME = "db_name";
    
    public static final String QUERY_ID = "_id";
    public static final String COLLECTION_DECLARACION = "declaracion";
    public static final String POOL_ENTES_RECEPTOR = "pool-entes-receptor";
    public static final String FIELD_ENCABEZADO = "encabezado";
    public static final String FIELD_ID = "_id";

    public static final String CAT_DNET_DATABASE_HOST = "CAT_DNET_DATABASE_HOST";
    public static final String CAT_DNET_DATABASE_PORT = "CAT_DNET_DATABASE_PORT";
    public static final String CAT_DNET_DATABASE_NAME = "CAT_DNET_DATABASE_NAME";
    public static final String CAT_DNET_DATABASE_USERNAME = "CAT_DNET_DATABASE_USERNAME";
    public static final String CAT_DNET_DATABASE_PSW = "CAT_DNET_DATABASE_PASSWORD";
    public static final String CONFIG_HOST = "host";
    public static final String CONFIG_DATABASE = "database";
    public static final String CONFIG_USERNAME = "username";
    public static final String CONFIG_PSW = "password";

    public static final String CATALOGO = "json_agg";

    public static final String STRING_FORMAT_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static final Integer CERO = 0;
    public static final Integer UNO = 1;
    public static final Integer DOS = 2;
    public static final Integer TRES = 3;
    public static final Integer CUATRO = 4;
    public static final Integer CINCO = 5;
    public static final Integer SEIS = 6;
    public static final Integer SIETE = 7;
    public static final Integer OCHO = 8;
    public static final Integer NUEVE = 9;
    public static final Integer DIEZ = 10;
    public static final Integer ONCE = 11;
    public static final Integer DOCE = 12;
    public static final Integer TRECE = 13;
    public static final Integer CATORCE = 14;
    public static final Integer QUINCE = 15;
    public static final Integer DIECISEIS = 16;
    public static final Integer DIECISIETE = 17;
    public static final Integer DIECIOCHO = 18;
    public static final Integer DIECINUEVE = 19;
    public static final Integer VEINTE = 20;
    public static final Integer VEINTIUNO = 21;

    public static final String CONFIGURACION_PDN = 	"{\"mappings\": {\"dynamic\": false,\"properties\": {\"declaracion\": {\"properties\": {\"declaracion\": "
			+ "{\"properties\": {\"situacionPatrimonial\": {\"properties\": {\"datosGenerales\": {\"properties\": {\"nombre\": {\"type\": \"text\",\"fields\":"
			+ " {\"keyword\": {\"type\": \"keyword\",\"ignore_above\": 256}}},\"primerApellido\": {\"type\": \"text\",\"fields\": {\"keyword\": {\"type\": "
			+ "\"keyword\",\"ignore_above\": 256}}},\"segundoApellido\": {\"type\": \"text\",\"fields\": {\"keyword\": {\"type\": \"keyword\",\"ignore_above\": 256}}},"
			+ "\"rfc\": {\"properties\": {\"rfc\": {\"type\": \"text\",\"fields\": {\"keyword\": {\"type\": \"keyword\",\"ignore_above\": 256}}},\"homoClave\": "
			+ "{\"type\": \"text\",\"fields\": {\"keyword\": {\"type\": \"keyword\",\"ignore_above\": 256}}}}}}},\"datosCurricularesDeclarante\": {\"properties\": "
			+ "{\"escolaridad\": {\"properties\": {\"nivel\": {\"properties\": {\"clave\": {\"type\": \"text\",\"fields\": {\"keyword\": {\"type\": \"keyword\","
			+ "\"ignore_above\": 256}}},\"valor\": {\"type\": \"text\",\"fields\": {\"keyword\": {\"type\": \"keyword\",\"ignore_above\": 256}}}}}}}}},"
			+ "\"bienesInmuebles\": {\"properties\": {\"bienInmueble\": {\"properties\": {\"formaAdquisicion\": {\"properties\": {\"clave\": {\"type\": \"text\","
			+ "\"fields\": {\"keyword\": {\"type\": \"keyword\",\"ignore_above\": 256}}},\"valor\": {\"type\": \"text\",\"fields\": {\"keyword\": {\"type\": "
			+ "\"keyword\",\"ignore_above\": 256}}}}},\"superficieConstruccion\": {\"properties\": {\"valor\": {\"type\": \"long\",\"fields\": {\"keyword\": "
			+ "{\"type\": \"keyword\",\"ignore_above\": 256}}}}},\"superficieTerreno\": {\"properties\": {\"valor\": {\"type\": \"long\",\"fields\": {\"keyword\":"
			+ " {\"type\": \"keyword\",\"ignore_above\": 256}}}}},\"valorAdquisicion\": {\"properties\": {\"valor\": {\"type\": \"long\",\"fields\": {\"keyword\": "
			+ "{\"type\": \"keyword\",\"ignore_above\": 256}}}}}}}}},\"datosEmpleoCargoComision\": {\"properties\": {\"domicilioMexico\": {\"properties\": "
			+ "{\"entidadFederativa\": {\"properties\": {\"clave\": {\"type\": \"text\",\"fields\": {\"keyword\": {\"type\": \"keyword\",\"ignore_above\": 256}}},"
			+ "\"valor\": {\"type\": \"text\",\"fields\": {\"keyword\": {\"type\": \"keyword\",\"ignore_above\": 256}}}}},\"municipioAlcaldia\": {\"properties\": "
			+ "{\"clave\": {\"type\": \"text\",\"fields\": {\"keyword\": {\"type\": \"keyword\",\"ignore_above\": 256}}},\"valor\": {\"type\": \"text\",\"fields\": "
			+ "{\"keyword\": {\"type\": \"keyword\",\"ignore_above\": 256}}}}}}},\"empleoCargoComision\": {\"type\": \"text\",\"fields\": {\"keyword\": {\"type\": "
			+ "\"keyword\",\"ignore_above\": 256}}},\"nombreEntePublico\": {\"type\": \"text\",\"fields\": {\"keyword\": {\"type\": \"keyword\",\"ignore_above\": 256}}},"
			+ "\"nivelEmpleoCargoComision\": {\"type\": \"text\",\"fields\": {\"keyword\": {\"type\": \"keyword\",\"ignore_above\": 256}}},\"nivelOrdenGobierno\": "
			+ "{\"type\": \"text\",\"fields\": {\"keyword\": {\"type\": \"keyword\",\"ignore_above\": 256}}}}},\"ingresos\": {\"properties\": {\"totalIngresosMensualesNetos\": "
			+ "{\"properties\": {\"valor\": {\"type\": \"long\",\"fields\": {\"keyword\": {\"type\": \"keyword\",\"ignore_above\": 256}}}}}}}}}}},\"id\": {\"type\": "
			+ "\"text\",\"fields\": {\"keyword\": {\"type\": \"keyword\",\"ignore_above\": 256}}},\"metadata\": {\"properties\": {\"actualizacion\": {\"type\": \"date\"},"
			+ "\"institucion\": {\"type\": \"text\",\"fields\": {\"keyword\": {\"type\": \"keyword\",\"ignore_above\": 256}}},\"tipo\": {\"type\": \"text\",\"fields\": "
			+ "{\"keyword\": {\"type\": \"keyword\",\"ignore_above\": 256}}}}}}}}}}";
}