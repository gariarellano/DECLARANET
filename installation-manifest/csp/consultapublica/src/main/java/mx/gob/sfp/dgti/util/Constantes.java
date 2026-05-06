package mx.gob.sfp.dgti.util;

import java.util.Arrays;
import java.util.List;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class Constantes {

    public static final String PATH = "/";
    public static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "*";
    public static final String HEADER_CONTENT_TYPE = "content-type";
    public static final String CONFIG_PORT = "port";
    public static final String TEXT_HTML = "text/html";
    public static final String HEADER_DOMINIO = "dominio";
    public static final String HEADER_TRANSACCION = "TRANSACTION";
    public static final String HEADER_AUTHORIZATION_KEY = "Authorization";
    public static final String PATH_BUSQUEDA = "/api/buscar";
    public static final String PATH_BUSQUEDA_SP = "/api/buscarsp";
    public static final String PATH_HISTORIAL = "/api/historico";
    public static final String PATH_DECLA_PUBLICA = "/api/consulta-declaracion/";
    public static final String PATH_CONSULTA_GABINETE = "/api/consulta-gabinete/";
    public static final String PATH_CONSULTA_DATOS_EXPUESTOS = "/private/consulta-datos-expuesto/:CURP/:RFC";
    public static final String PATH_CONSULTA_DATOS_EXPUESTOS_ADMIN = "/private/consulta-datos-expuesto-admin/:CURPADM/:PARAMETROBUSQUEDA/";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    public static final String HEADER_CONTENT_APPLICATION_JSON = "application/json; charset=UTF8";
    public static final String HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "true";
    public static final String HEADER_ACCESS_CONTROL_ALLOW_METHODS = "POST, GET, OPTIONS, DELETE";
    public static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "authorization,content-type";
    public static final String EMPTY = "";

    public static final String PATTERN_RFC_MIN = "^[a-zA-z]{4}.*";
    public static final String PATTERN_NOMBRE = "^([A-Za-zÁÉÍÓÚñáéíóúÑ]{1,}?[A-Za-zÁÉÍÓÚñáéíóúÑ\\']+[\\s])+([A-Za-zÁÉÍÓÚñáéíóúÑ]{1,}?[A-Za-zÁÉÍÓÚñáéíóúÑ\\'])+[\\s]?([A-Za-zÁÉÍÓÚñáéíóúÑ]{1,}?[A-Za-zÁÉÍÓÚñáéíóúÑ\\'])?$";
    public static final String ERROR_ESTRUCTURA = "Verifique los datos enviados";
    public static final String ERROR_PROCESO_SOLICITUD = "Error al procesar la solicitud";
    public static final String SOLICITUD_NO_MODIFICADA = "No se ha modificado la solicitud";

    public static final String SOLICITUD_EXITOSA = "Datos solicitados con éxito";
    public static final String PROP_MENSAJE = "mensaje";
    public static final String PROP_ESTATUS = "estatus";
    public static final String PROP_DATOS = "datos";

    public static final String PROP_ANIO = "anio";
    public static final String PROP_COLL_NAME = "collName";
    public static final String PROP_DECLARACION = "declaracion";
    public static final String PROP_DECLARANTE = "declarante";
    public static final String PROP_FECHA_ENCARGO = "fechaEncargo";
    public static final String PROP_FECHA_RECEPCION = "fechaRecepcion";
    public static final String PROP_ID_USR_DECNET = "idUsrDecnet";
    public static final String PROP_ID_USUARIO = "idUsuario";
    public static final String PROP_INSTITUCION_RECEPTORA = "institucionReceptora";
    public static final String PROP_NUM_DECLARACION = "numeroDeclaracion";
    public static final String PROP_TIPO_DECLARACION = "tipoDeclaracion";
    public static final String PROP_TIPO_DECLARACION_NOTA = "NOTA";
    public static final String PROP_NOMBRE = "nombre";
    public static final String PROP_NUMERO_COMPROBANTE = "noComprobante";
    public static final String CONSULTA_DECLARACION = "CONSULTA_DECLARACION";
    public static final String CONSULTA_DECLARACION_VALUE = System.getenv(CONSULTA_DECLARACION);
    public static final String ENTES_RECEPTORES = "ENTES_RECEPTORES";
    public static final String CONSULTAR_ENTES = System.getenv(ENTES_RECEPTORES);

    public static final String PROP_MATCH = "$match";
    public static final String PROP_TEXT = "$text";
    public static final String PROP_SEARCH = "$search";
    public static final String PROP_SORT = "$sort";
    public static final String PROP_SCORE = "score";
    public static final String PROP_META = "$meta";
    public static final String PROP_TEST_SCORE = "textScore";
    public static final String PROP_PROJECT = "$project";
    public static final String PROP_AGGREGATE = "aggregate";
    public static final String PROP_PIPELINE = "pipeline";
    public static final String PROP_CURSOR = "cursor";
    public static final String PROP_FIRST_BATCH = "firstBatch";
    public static final String PROP_TIPO_GABINETE = "tipoGabinete";
    public static final String PROP_BUSQUEDA = "busqueda";
    public static final String PROP_AVISO = "AVISO";
    public static final String PROP_DEPENDENCIA = "dependencia";
    public static final String PROP_RFC = "rfc";
    public static final String PROP_CURP = "curp";
    public static final String PROP__ID = "_id";

    private static final List<String> match_in = Arrays.asList("INICIO", "MODIFICACION", "CONCLUSION");
    private static final List<String> dependencia_usuario_nin = Arrays.asList("", null);
    private static final List<Object> ifNull_dependencia = Arrays.asList("$declarante.dependencia", new JsonObject().put("$toUpper", "$declarante.enteCargoComision.dependencia"));
    private static final List<String> ifNull_declarante = Arrays.asList("$declarante.curp", null);

    public static final JsonArray PIPELINE_BUSQUEDA
            = new JsonArray()
            .add(new JsonObject()
                    //match
                    .put(PROP_MATCH, new JsonObject().put(PROP_TEXT, new JsonObject().putNull(PROP_SEARCH))
                            .put("declaracion.tipoDeclaracion", new JsonObject().put("$in", match_in))
                            .put("declarante.idUsrDecnet", new JsonObject().put("$nin", dependencia_usuario_nin))))
            //UNWIND
            .add(new JsonObject().put("$unwind", new JsonObject().put("path", "$declarante.enteCargoComision").put("preserveNullAndEmptyArrays", true)))
            //project
            .add(new JsonObject().put("$project", new JsonObject()
                            .put("declarante.nombre", 1)
                            .put("declarante.dependencia", new JsonObject().put("$ifNull", ifNull_dependencia))
                            .put("declarante.idUsrDecnet", 1)
                            .put("declarante.rfc", 1)
                            .put("declarante.curp", new JsonObject().put("$ifNull", ifNull_declarante))
                            .put("declaracion.fechaId", 1)
                    ))
            //GROUP
            .add(new JsonObject()
                    .put("$group", new JsonObject()
                            .put("_id", new JsonObject()
                                    .put("nombre", "$declarante.nombre")
                                    .put("dependencia", "$declarante.dependencia")
                                    .put("idUsrDecnet", "$declarante.idUsrDecnet")
                                    .put("rfc", "$declarante.rfc")
                                    .put("curp", "$declarante.curp"))
                            .put("fechaId", new JsonObject().put("$max", "$declaracion.fechaId"))
                            .put("nombre", new JsonObject().put("$max", "$declarante.nombre"))
                            .put("score", new JsonObject().put("$max", new JsonObject().put("$meta", "textScore")))
                    ))
            //match
            .add(new JsonObject().put("$match", new JsonObject().put("score", new JsonObject().put("$gte", 0.8))))
            //sort
            .add(new JsonObject().put("$sort", new JsonObject()
                            .put("score", -1)
                            .put("nombre", 1)
                            .put("fechaId", -1)))
            //limit
            .add(new JsonObject().put("$limit", 200));
    
    public static final JsonArray PIPELINE_BUSQUEDA_DATOS_PUBLICOS 
            = new JsonArray()
            //match
            .add(new JsonObject()        
                    .put(PROP_MATCH, new JsonObject().put(PROP_TEXT, new JsonObject().putNull(PROP_SEARCH)))
            )
            //project
            .add(new JsonObject().put("$project", new JsonObject()
                            .put("nombre", 1)
                            .put("dependencia", 1)
                            .put("idDependencia",1)
                            .put("idUsrDecnet", 1)
                            .put("rfc", 1)
                            .put("curp", 1)
                            .put("score", new JsonObject().put("$meta","textScore"))
                    ))
            //LOOKUP
            .add(new JsonObject()
                    .put("$lookup", new JsonObject()
                            .put("from", "idUsrDecnet")
                            .put("localField", "idUsrDecnet")
                            .put("foreignField", "idUsrDecnet")
                            .put("as", "reservaSN")
                    ))
            //match lookUp
            .add(new JsonObject().put("$match", new JsonObject().put("reservaSN.idUsrDecnet", new JsonObject().put("$exists", Boolean.FALSE))))
            //sort
            .add(new JsonObject().put("$sort", new JsonObject()
                            .put("score", -1)
                            .put("nombre", 1)
            ))
            //limit
            .add(new JsonObject().put("$limit", 50));

}
