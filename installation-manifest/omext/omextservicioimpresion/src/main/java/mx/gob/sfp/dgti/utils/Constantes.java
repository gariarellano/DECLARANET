/**
 * 
 */
package mx.gob.sfp.dgti.utils;

/**
 * @author programador09@sfp.gob.mx
 *
 */
public class Constantes {
    public static final String REPORTES_DESA_RUTA = System.getenv("REPORTES_RUTA") != null ? System.getenv("REPORTES_RUTA") :"";
    public static final String DIRECTORIO = REPORTES_DESA_RUTA+"//reportes//";    

    public static final String HEADER_CONTENT_TYPE = "content-type";
    public static final String HEADER_CONTENT_APPLICATION_JSON = "application/json; charset=utf-8";
    public static final String HEADER_AUTHORIZATION = "Authorization";
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

    public static final String HEADER_PRAGMA = "no-cache";
    public static final String HEADER_CACHE_CONTROL = "no-store";

    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String PRAGMA = "Pragma";

    public static final String SERVER_PORT = "SERVER_PORT";
    public static final String CONFIG_PORT = "port";
    public static final String URL_APPI_VISTA_OMISOS = "/api/genera-vita-omiso/";
    public static final String PATH = "/";
    public static final String EMPTY = "";
    
    //localizacion de archivos
    public static final String VISTA_OMISO_JASPER = DIRECTORIO +"vista-omisos.jasper";
    public static final String VISTA_OMISO_DETALLE_JASPER = DIRECTORIO +"vista-omisos-detalle.jasper";
    
    //Nombre parametros
    public static final String NUMERO_OFICIO = "numeroOficio";
    public static final String FECHA_VISTA = "fechaGeneracionVista";
    public static final String DEPENDENCIA_ENTIDAD = "dependenciaEntidad";
    public static final String BODY_TEXT_OFICIO = "bodyTextOficio";
    public static final String FECHA_VENCIMIENTO = "fechaVenciemiento";
    public static final String LOGO_IMAGEN = "logoImagen";
    public static final String PUESTO_FIRMANTE = "puestoFirmante";
    public static final String FIRM_CAR_AUTENT_OFICIO = "firmaOficioCaracteresAutenticidad";
    public static final String FIRMANTE_NOMBRE = "firmanteNombre";
    public static final String PRIMER_PARRAFO = "primerParrafo";
    public static final String SEGUNDO_PARRAFO = "segundoParrafo";
    public static final String FIRM_CAR_AUTENT_LISTADO = "firmaListadoCaracteresAutenticidad";
    public static final String TIPO_DECLARACION = "tipoDeclaracion";
    public static final String ANIO = "anio";
    public static final String DATA = "data";

    public static final String PARAMETROS_VISTA = "parametersVista";
    public static final String PARAMETROS_VISTA_DETALLE = "parametersVistaDetalle";

    private Constantes() {

    }
}
