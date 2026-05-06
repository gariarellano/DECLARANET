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
    public static final String URL_APPI_GUARDA_DECLARACION = "/api/guarda-declaracion";
    public static final String URL_APPI_RECEPCION_DECLARACION = "/api/recepcion-declaracion";
    public static final String URL_APPI_REALIZA_REPORTE_PERIODO = "/api/reporta-periodo";
    public static final String URL_APPI_REALIZA_REPORTE_POR_NUM_CUMPL = "/api/reportar-por-no-cumplimiento/";
    public static final String URL_APPI_PROCESO_UNICO_DATOS_PUB = "/api/genera-datos-publicos/";
    public static final String PATH = "/";
    public static final String EMPTY = "";
    public static final String GUION = "-";
    public static final Integer CASADO = 3;

    //Fields_objetos
    public static final String FIELD_ID = "id";
    public static final String FIELD_ACTIVO = "activo";
    public static final String FIELD_TITULO_FIRMANTE = "tituloFirmante";
    public static final String FIELD_NOMBRE = "nombre";
    public static final String FIELD_PRIMER_APELLIDO = "primerApellido";
    public static final String FIELD_SEGUNDO_APELLIDO ="segundoApellido";
    public static final String FIELD_PUESTO_FIRMANTE ="puestoFirmante";
    public static final String FIELD_DIGESTION ="digestion";
    public static final String FIELD_FIRMADA = "FIRMADA";

    //FIELDS_DATABASE
    public static final String _ID = "_id";
    public static final String COLLECTION_NAME_DECLARACIONES_TEMP = "declaracionTemp";
    public static final String COLLECTION_NAME_DECLARACIONES = "declaracion";
    public static final String COLLECTION_NAME_TRANSACCION_FUP = "transaccionFup";
    public static final String COLLECTION_NAME_RECEPCION_WEB = "recepcionWeb";
    public static final String COLLECTION_NAME_FIRMANTE = "firmante";
    public static final String FIELD_INSTITUCION_RECEPTORA = "institucionReceptora";
    public static final String FIELD_ENCABEZADO = "encabezado";
    public static final String FIELD_COLL_NAME = "collName";
    public static final String FIELD_NUMERO_DECLARACION = "numeroDeclaracion";
    public static final String FIELD_TIPO_FORMATO = "tipoFormato";
    public static final String FIELD_ERROR_MESSAGE = "errorMessage";
    public static final String ERROR_MESSAGE = "La declaración que se intenta guardar no es la misma que la existente en la base de datos.";
    public static final String PROPERTY_QUERY_ID_USUARIO = "encabezado.usuario.idUsuario";
    public static final String FIELD_USUARIO = "usuario";
    public static final String FIELD_ID_USUARIO = "idUsuario";
    public static final String FIELD_FECHA_REGISTRO = "fechaRegistro";
    public static final String FIELD_FECHA_ACTUALIZACION = "fechaActualizacion";
    public static final String FIELD_DECLARACION = "declaracion";
    public static final String FIELD_TIPO_DECLARACION = "tipoDeclaracion";
    public static final String FIELD_FECHA_RECEPCION = "fechaRecepcion";
    public static final String FIELD_ANIO = "anio";
    public static final String FIELD_FECHA_ENCARGO = "fechaEncargo";
    public static final String FIELD_NO_COMPROBANTE = "noComprobante";
    public static final String FIELD_DATOS_GENERALES = "datosGenerales";
    public static final String FIELD_DATOS_PERSONALES = "datosPersonales";
    public static final String FIELD_RFC = "rfc";
    public static final String FIELD_CURP = "curp";
    public static final String FIELD_EXTEMPORANEA = "extemporanea";
    public static final String FIELD_SITUACION = "situacion";
    public static final String FIELD_ID_USR_DECNET = "idUsrDecnet";    
    public static final String FIELD_CORREO_ELECTRONICO = "correoElectronico";
    public static final String FIELD_INSTITUCIONAL = "institucional";
    public static final String FIELD_PERSONAL_ALTERNO = "personalAlterno";
    public static final String FIELD_DATOS_EMPLEO_CARGO_COMISION = "datosEmpleoCargoComision";
    public static final String FIELD_EMPLEO_CARGO_COMISION = "empleoCargoComision";
    public static final String FIELD_DEPENDECIA = "dependencia";
    public static final String FIELD_ENTE = "ente";
    public static final String FIELD_FECHA_ID ="fechaId";
    public static final String FIELD_ENTE_CARGO_COMISION = "enteCargoComision";
    public static final String FIELD_FIRMADO = "firmado";
    public static final String FIELD_DIGESTION_DCN = "digestionDcn";
    public static final String FIELD_FIRMA_DECLARACION = "firmaDeclaracion";
    public static final String FIELD_FIRMA_ACUSE = "firmaAcuse";
    public static final String FIELD_ID_FIRMANTE = "idFirmante";
    public static final String FIELD_NOMBRE_FIRMANTE = "nombreFirmante";
    public static final String FIELD_ACUSE = "acuse";
    public static final String FIELD_TIPO_FIRMA = "tipoFirma";
    public static final String FIELD_DECLARANTE = "declarante";
    public static final String FIELD_DETALLE_AVISO_CAMBIO_DEP = "detalleAvisoCambioDependencia";
    public static final String FIELD_CARGO_COMISION_INICIA = "empleoCargoComisionInicia";
    public static final String FIELD_VERSION_RECEPCION_WEB = "versionRecepcionWeb";
        
        
    //MENSAJES-VERTICALS
    public static final String MESSAGE = "message";
    public static final String OK = "OK";
    public static final String ERRO_AL_GUARADAR_EN_FUP = "ERROR- AL GUARDAR EN FUP";
    public static final String GUARDADO = "guardado";
    
    //vertices para registros externos
    
    //vertice para registrar cumplimiento por numero de cumplimineto de consulta publica.
}
