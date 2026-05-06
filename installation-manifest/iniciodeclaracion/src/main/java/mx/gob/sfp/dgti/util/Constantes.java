/* 
 * Sistema Propiedad de la Secretaría de la Función Pública DGTI
 * "VerificarDeclaraciones" Servicio que permite verificar si 
 * existen declaraciones en proceso o insertar el tipo de declaración a presentar
 * 
 * Proyecto desarrollado por programador04@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.util;

import java.util.regex.Pattern;

public class Constantes {
    // Headers
    public static final String HEADER_DOMINIO = "dominio";
    public static final String HEADER_TRANSACCION = "TRANSACTION";
    public static final String HEADER_AUTHORIZATION_KEY = "Authorization";

    public static final String HEADER_CONTENT_TYPE = "content-type";
    public static final String HEADER_CONTENT_APPLICATION_JSON = "application/json; charset=UTF8";
    public static final String TEXT_HTML = "text/html";
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
    public static final String PATH = "/";
    public static final String PATH_INICIAR = "/iniciar";
    public static final String PATH_VERIFICAR = "/api/verificar";
    public static final String PATH_CREAR_TEMPORAL = "/api/crearDeclaracionTemporal";
    public static final String PATH_CONSULTA_ANIOS_DECLA_MOD_EXT = "/api/consultarAniosDeclaModExtemp";
    public static final String PATH_ELIMINAR_DECLARACION = "/api/eliminarDeclaracionTemporal";
    public static final String PATH_CONSULTAR_HISTORIAL = "/api/historial-declaraciones";
    public static final String PATH_CREAR_NOTA = "/api/crear-nota";
    public static final String PATH_CONSULTAR_NOTAS = "/api/consultar-notas";
    public static final String CONFIG_PORT = "port";
    public static final String SERVER_PORT = "SERVER_PORT";
    public static final String URL_PRIVATE_VALIDATE = "/validador";
    public static final String EMPTY = "";
    public static final String URL_DECLARACION = "URL_DECLARACION";
    public static final String URL_DECLARACION_HISTORIAL = "URL_DECLARACION_HISTORIAL";
    public static final String URL_DECLARACION_PRECARGA = "URL_DECLARACION_PRECARGA";

    //ANIOS PARA DECLARAR
    public static final Integer ANIO_MIN_CAMBIO_DEP = 2017;
    public static final Integer ANIO_MIN_MODIFICACION_INICO_CONCLUSION = 2002;

    //STRING USADOS
    public static final String ACCES_TOKEN = "acces_token";
    public static final String TRANSACTION = "transaction";
    public static final String INTITUCION_RECEPTORA = "institucionReceptora";
    public static final String ID_CLIENTE = "ID_CLIENTE";
    public static final String ID_SP = "idSp";
    public static final String SECRET_KEY = "SECRET_KEY";
    public static final String CURP = "curp";
    public static final String ID_USUARIO = "idUsuario";
    
    public static final String VERSION_DECLARACION = "VERSION_DECLARACION";
    public static final String VERSION_NOTA = "VERSION_NOTA";
    public static final String VERSION_NOTA_STRING = "20200414";
    public static final String VERSION_DECLARACION_ORACLE = "20191231";

    //MENSAJES GENERALES
    public static final String ERROR_FIDELIDAD = "Error al validar la fidelidad de la petición";
    public static final String ERROR_CREAR_NOTA = "Ocurrió un error al crear la nota";
    public static final String PETICION_DATOS_INCOMPLETOS = "Datos incompletos en la petición";
    public static final String ERROR_VALIDAR_PROPIEDADES = "Error al validar las propiedades";
    public static final String VALIDACION_CON_ERRORES = "Propiedades validadas con errores";
    public static final String DECLARACION_FIRMADA = "La declaración ya se encuentra presentada y firmada";
    public static final String ERROR_CREAR_DECLARACION_TEMP = "Error al crear la declaración temporal";
    public static final String ERROR_VERIFICAR_DECLARACION_A_PRESENTAR = "Error al verificar los datos de la declaración a presentar";
    public static final String ERROR_OBTENER_LISTA_DECLARACIONES_FIRMADAS = "Error al obtener la lista de las declaraciones firmadas";
    public static final String ERROR_ELIMINAR_DECLARACION_TEMPORAL = "Error al eliminar la declaración temporal";
    public static final String ERROR_VERIFICANDO_DECLARACIONES = "Error al verificar las declaraciones";
    public static final String ERROR_CONSULTA_ANIOS_DECLA_MOD_EXT = "Error verificando años declaración de modificación extemporanea";
    public static final String ERROR_CONSULTA_HISTORIAL_DECLA = "Error consultando el historial de declaraciones";
    public static final String ERROR_VERIFICA_DECLA_EN_PROCESO = "Erro verificando las declaración en proceso";
    public static final String ERROR_OBTENER_HISTORIAL = "Error al obtener el historial de declaraciones";
    public static final String ERROR_OBTENER_HISTORIAL_NOTA = "Error al obtener el historial de notas de la declaración";
    public static final String NOTA_CREADA_CORRECTAMENTE = "Nota creada correctamente";
    public static final String NOTA_NO_CREADA_DECLA_PEMDIENTE = "No puedes crear un nota ya que tienes una declaración en proceso";
    public static final String DECLARACION_PRECARGA_VACIA = "Declaración a precargar vacía";
    public static final String ERROR_SOLICITUD_DECLARACION = "Error al solicitar la declaración a precargar";
    public static final String ERROR_CONSULTAR_HISTORIAL = "Error al solicitar el historial";
    public static final String ERROR_CONSULTAR_DECLARACION_PRECARGA = "Error al solicitar la declaración a precargar";
    public static final String FORMATO_SIMPLIFICADO_INICIAL = "S";

    //PROPIEDADES PARA JSONS
    public static final String PROP_RESPUESTA = "respuesta";
    public static final String PROP_ESTATUS = "estatus";
    public static final String PROP_MENSAJE = "mensaje";
    public static final String PROP_ERROR = "error";
    public static final String PROP_MENSAJE_VERIFICAR = "Error al procesar la solicitud, verifique el contenido y estructura de los datos enviados";

    public static final String PROP_ENCABEZADO = "encabezado";
    public static final String PROP_NUM_DECLARACION = "numeroDeclaracion";
    public static final String PROP_ID = "_id";
    public static final String PROP_AUTHORIZATION = "authorization";
    public static final String PROP_DECLARACION_PENDIENTE = "declaracionPendiente";
    public static final String PROP_MENSJAE_CONTINUAR_DECLA = "Continuar con la declaración";
    public static final String PROP_DECLARACION = "declaracion";

    public static final String PROP_NOMBRE = "nombre";
    public static final String PROP_PRIMER_APELLIDO = "primerApellido";
    public static final String PROP_SEGUNDO_APELLIDO = "segundoApellido";
    public static final String PROP_RFC = "rfc";
    public static final String PROP_CURP = "curp";
    public static final String PROP_EMAIL = "email";
    public static final String PROP_PERSONAL_ALTERNO = "personalAlterno";
    public static final String PROP_HOMOCLAVE = "homoclave";

    public static final String PROP_MENSAJE_DECLARACION_NO_PENDIENTE = "No tienes declaraciones pendientes";
    public static final String PROP_DECLARACION_A_PRESENTAR = "declaracionAPresentar";
    public static final String PROP_TIPO_DECLARACION = "tipoDeclaracion";
    public static final String PROP_ANIO = "anio";
    public static final String PROP_ANIOS = "anios";
    public static final String PROP_FECHA_ENCARGO = "fechaEncargo";
    public static final String PROP_FECHA_REGISTRO = "fechaRegistro";
    public static final String PROP_FECHA_RECEPCION = "fechaRecepcion";
    public static final String PROP_FECHA_ACTUALIZACION = "fechaActualizacion";
    public static final String PROP_USUARIO = "usuario";
    public static final String PROP_ID_USUARIO = "idUsuario";
    public static final String PROP_ID_MOVIMIENTO = "id_movimiento";
    public static final String PROP_TIPO_FORMATO = "tipoFormato";
    public static final String PROP_VERSION_DECLARACION = "versionDeclaracion";
    public static final String PROP_INSTITUCION_RECEPTORA = "institucionReceptora";
    public static final String PROP_DATOS_GENERALES = "datosGenerales";
    public static final String PROP_DATOS_PERSONALES = "datosPersonales";
    public static final String PROP_SELLECIONAR_ANIO_EXTEMPORANEA = "Seleccione el año a presentar";
    public static final String PROP_HISTORIAL = "historial";
    public static final String PROP_NIVEL_JERARQUICO = "nivelJerarquico";
    public static final String PROP_ID_NIVEL_JERARQUICO = "id";
    public static final String PROP_VALOR_NIVEL_JERARQUICO = "valor";
    public static final String PROP_VALOR_UNO_NIVEL_JERARQUICO = "valorUno";
    public static final String PROP_COLL_NAME = "collName";
    public static final String PROP_FORMATO_FECHA_MONGO = "yyyy-MM-dd'T'HH:mm:ss.S";
    public static final String PROP_FIRMADA = "firmada";
    public static final String PROP_MODIFICACION = "MODIFICACION";
    public static final String PROP_TELEFONO_CASA = "telefonoCasa";
    public static final String PROP_PAIS_CELULAR_PERSONAL = "paisCelularPersonal";
    public static final String PROP_DECLARANTE = "declarante";
    public static final String PROP_ID_USR_DECNET = "idUsrDecnet";
    public static final String PROP_REGISTRO = "registro";
    public static final String PROP_TIPO_DECLARACION_NOTA = "NOTA";
    public static final String PROP_ACUSE = "acuse";
    public static final String PROP_DECLARACION_ORIGEN = "declaracionOrigen";
    public static final String PROP_VERSION_NOTA = "versionNota";
    public static final String PROP_NOTA = "nota";
    public static final String PROP_FECHA_ENCARGO_DATE = "fechaEncargoDate";
    public static final String PROP_NUM_DECLA_PRECARGA = "numeroDeclaracionPrecarga";
    public static final String PROP_ID_CATALOGO = "id";
    public static final String DECLARACION_CEROS = "0000000000";

    //NOMBRES DE AMBIENTES
    public static final String AMBIENTE_AMBIENTE = "AMBIENTE";
    public static final Pattern PATHERN_DIGITOS = Pattern.compile("\\d+");
}
