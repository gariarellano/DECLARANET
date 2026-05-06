package mx.gob.sfp.dgti.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class Constantes {

    private Constantes() {
    }

    public static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "*";
    public static final String HEADER_DOMINIO = "dominio";
    public static final String HEADER_TRANSACCION = "TRANSACTION";
    public static final String HEADER_AUTHORIZATION_KEY = "Authorization";
    public static final String HEADER_CONTENT_TYPE = "content-type";
    public static final String PATH = "/";
    public static final String TEXT_HTML = "text/html";
    public static final String CONFIG_PORT = "port";
    public static final String PATH_INICIAR = "/inicio/iniciar";
    public static final String PATH_BUSQUEDA = "/api/busqueda";
    public static final String PATH_OBTENER_DECLARACION = "/api/obtenerDeclaracion";
    public static final String PATH_OBTENER_ACUSE = "/api/obtenerAcuse";
    public static final String PATH_OBTENER_DOMICILIOS = "/api/obtenerDomicilios";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String HEADER_CONTENT_APPLICATION_JSON = "application/json; charset=UTF8";
    public static final String HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "true";
    public static final String HEADER_ACCESS_CONTROL_ALLOW_METHODS = "GET,POST,OPTIONS";
    public static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "Authorization,content-type,authorization";
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

    //constantes para conexion
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String MAX_POOL_SIZE = "maxPoolSize";
    public static final String MIN_POOL_SIZE = "minPoolSize";
    public static final String USER_NAME = "username";
    public static final String P_W_D = "password";
    public static final String DB_NAME = "db_name";
    public static final String KEEP_ALIVE = "keepAlive";

    public static final String PROP_MENSAJE = "mensaje";
    public static final Boolean BOOLEAN_TRUE = Boolean.TRUE;
    public static final Boolean BOOLEAN_FALSE = Boolean.FALSE;
    public static final String PROP_ESTATUS = "estatus";
    public static final String PROP_HISTORIAL = "historial";
    public static final String MENSAJE_HISTORIAL_EXITO = "Historial obtenido con éxito";
    public static final String PROP_DATA = "data";
    public static final String MENSAJE_DECLA_PDF_EXITO = "Declaración solicitada con éxito";
    public static final String PROP_JUSTIFICACION = "justificacion";
    public static final String PROP_USUARIO = "usuario";
    public static final String PROP_ID_USUARIO = "idUsuario";
    public static final String PROP_NUM_DECLARACION = "numeroDeclaracion";
    public static final String PROP_TIPO_DECLARACION = "tipoDeclaracion";
    public static final String PROP_FECHA_RECEPCION = "fechaRecepcion";
    public static final String PROP_COLL_NAME = "collName";
    public static final String PROP_NUMERO_COMPROBANTE = "noComprobante";
    public static final String PROP_ANIO = "anio";
    public static final String PROP_DECLARANTE = "declarante";
    public static final String PROP_INSTITUCION_RECEPTORA = "institucionReceptora";
    public static final String PROP_ID_USR_DECNET = "idUsrDecnet";
    public static final String PROP_CABECERA = "cabecera";
    public static final String PROP_RESERVADO = "reservado";
    public static final String PROP_DECLARACION = "declaracion";
    public static final String PROP_JSON_A_CONSULTAR = "jsonAConsultar";
    public static final String PROP_NOMBRE = "nombre";
    public static final String PROP_RFC = "rfc";
    public static final String PROP_CURP = "curp";
    public static final String PROP_DEPENDENCIA = "dependencia";
    public static final String PROP_ENTE_CARGO_COMISION = "enteCargoComision";
    public static final String PROP_PRIMER_APELLIDO = "primerApellido";
    public static final String PROP_SEGUNDO_APELLIDO = "segundoApellido";
    public static final String PROP_HOMOCLAVE = "homoclave";
    public static final String PROP_EMAIL = "email";

    public static final String PROP_AGGREGATE = "aggregate";
    public static final String PROP_PIPELINE = "pipeline";
    public static final String PROP_CURSOR = "cursor";
    public static final String PROP_FIRST_BATCH = "firstBatch";
    public static final String SOLICITUD_EXITOSA = "Datos solicitados con éxito";

    public static final String PATTERN_RFC = "^([A-ZÑ&]{4})(\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01]))([A-Z\\d]{3})$";
    public static final String PATTERN_RFC_DIEZ = "^([A-ZÑ&]{4})(\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01]))$";
    public static final String PATTERN_CURP = "^([A-Z][AEIOUX][A-Z]{2}\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\\d])(\\d)$";
    public static final String PATTERN_NOMBRE = "^[a-zA-ZÁÉÍÓÚñáéíóúÑ]{2,}(\\s[a-zA-ZÁÉÍÓÚñáéíóúÑ]{2,}){1,}$";
    public static final String PROP_MATCH = "$match";
    public static final String PROP_TEXT = "$text";
    public static final String PROP_SEARCH = "$search";
    public static final String PROP_UNWIND = "$unwind";
    public static final String PROP_PRESERVE = "preserveNullAndEmptyArrays";
    public static final String PROP_PROJECT = "$project";
    public static final String PROP_SORT = "$sort";

    public static final String ERROR_BITACORA_FALSE = "Bitacora no guardada, false";
    public static final String ERROR_BITACORA_TRUE = "Bitacora no guardada, true";
    public static final String ERROR_BITACORA = "Bitacora no guardada";
    public static final String ERROR_DATOS_PETICION = "Datos en la petición incompletos";
    public static final String ERROR_OBTENER_ACUSE = "Error al obtener los datos pdf del acuse";
    public static final String ERROR_OBTENER_PDF = "Error al obtener los datos pdf de la declaración";
    public static final String ERROR_VERIFICAR_DATOS = "Verifique el envío de los datos";
    public static final String ERROR_OBTENER_DOMICILIOS = "Ocurió un error al obtener los domicilios, inténtelo más tarde";
    
    public static final String ACCES_TOKEN = "accesToken";
    public static final String TRANSACTION = "transaction";
    public static final String COLL_NAME = "collName";

    public static final JsonArray PIPELINE_BUSQUEDA_HISTORIAL
            = new JsonArray()
            .add(new JsonObject().put(PROP_MATCH, new JsonObject().putNull("declarante.idUsrDecnet")))
            .add(new JsonObject().put(PROP_UNWIND, new JsonObject()
                            .put("path", "$declarante.enteCargoComision")
                            .put(PROP_PRESERVE, true)))
            .add(new JsonObject().put(PROP_PROJECT, new JsonObject()
                            .put("declarante.idUsrDecnet", 1)
                            .put("declaracion.numeroDeclaracion", 1)
                            .put("declaOrigen", "$declaracion.declaracionOrigen.encabezado.numeroDeclaracion")
                            .put("declarante.dependencia", 1)
                            .put("declarante.enteCargoComision", 1)
                            .put("institucionReceptora.collName", 1)
                            .put("declaracion.tipoDeclaracion", 1)
                            .put("declaracion.noComprobante", 1)
                            .put("declaracion.fechaRecepcion", 1)
                            .put("declaracion.anio", 1)))
            .add(new JsonObject().put(PROP_SORT, new JsonObject()
                            .put("declaracion.fechaRecepcion", -1)));

    public static final JsonArray PIPELINE_BUSQUEDA_DATOS_PUBLICOS
            = new JsonArray()
            //match
            .add(new JsonObject()
                    .put(PROP_MATCH, new JsonObject())
            )
            //lookup
            .add(new JsonObject().put("$lookup", new JsonObject()
                            .putNull("from")
                            .put("localField", PROP_ID_USR_DECNET)
                            .put("foreignField", PROP_ID_USR_DECNET)
                            .put("as", "reservaSnID")))
            //unwind
            .add(new JsonObject().put(PROP_UNWIND, new JsonObject()
                            .put("path", "$reservaSnID")
                            .put(PROP_PRESERVE, true)))
            //project
            .add(new JsonObject().put(PROP_PROJECT, new JsonObject()
                            .put(PROP_NOMBRE, 1)
                            .put(PROP_DEPENDENCIA, 1)
                            .put("idDependencia", 1)
                            .put(PROP_ID_USR_DECNET, 1)
                            .put("rfc", 1)
                            .put("curp", 1)
                            .put(PROP_RESERVADO, "$reservaSnID.idUsrDecnet")
                            .put("score", new JsonObject().put("$meta", "textScore"))
                    ))
            //sort
            .add(new JsonObject().put(PROP_SORT, new JsonObject()
                            .put("score", -1)
                            .put(PROP_NOMBRE, 1)
                    ))
            //limit
            .add(new JsonObject().put("$limit", 50));

	 //QUERY PARA OBTENER SOLO EL ENCABEZADO
    public static final JsonArray PIPELINE_ENCABEZADO_HISTORIAL
            = new JsonArray()
            //match
            .add(new JsonObject()
                    .putNull(PROP_MATCH)
            )
            //lookup
            .add(new JsonObject().put("$lookup", new JsonObject()
                            .putNull("from")
                            .put("localField", PROP_ID_USR_DECNET)
                            .put("foreignField", PROP_ID_USR_DECNET)
                            .put("as", "reservaSnID")))
            //unwind
            .add(new JsonObject().put(PROP_UNWIND, new JsonObject()
                            .put("path", "$reservaSnID")
                            .put(PROP_PRESERVE, true)))
            //project
            .add(new JsonObject().put(PROP_PROJECT, new JsonObject()
                            .put(PROP_ID_USR_DECNET, 1)
                            .put(PROP_NOMBRE, 1)
                            .put(PROP_RESERVADO, "$reservaSnID.idUsrDecnet")
                    ))
            //sort
            .add(new JsonObject().put(PROP_SORT, new JsonObject()
                            .put(PROP_NOMBRE, 1)
                    ))
            //limit
            .add(new JsonObject().put("$limit", 1));

}
