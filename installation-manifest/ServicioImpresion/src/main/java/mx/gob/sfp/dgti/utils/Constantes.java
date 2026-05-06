/**
 * 
 */
package mx.gob.sfp.dgti.utils;

/**
 * @author programador09@sfp.gob.mx
 *
 */
public class Constantes {

	public static final String HEADER_CONTENT_TYPE = "content-type";
	public static final String HEADER_CONTENT_APPLICATION_JSON = "application/json; charset=utf-8";
	public static final String HEADER_AUTHORIZATION= "Authorization";
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
	public static final String URL_APPI_CONSULTA_DECLARACION = "/api/consulta-declaracion/";
	public static final String URL_APPI_CONSULTA_ACUSE = "/api/consulta-acuse/";
	public static final String URL_APPI_AVISO_CAMBIO_DEPENDENCIA = "/api/aviso-cambio-dependencia/";
	public static final String URL_APPI_CONSULTA_PUBLICA = "/api/consulta-publica/";
	public static final String URL_APPI_NOTA_ACLARATORIA = "/api/nota-aclaratoria/";
	public static final String PATH = "/";
	public static final String EMPTY = "";
	public static final String GUION = "-";
	public static final Integer CASADO = 3;
	
	public static final String PATH_CONSULTA_DECLARACION_SI = "consulta-declaracion-si/";
	public static final String PATH_CONSULTA_FIRMADO_DECLARACION = "consulta-firmado-declaracion/";
	public static final String PATH_CONSULTA_DECLARACION_PUBLICA = "consulta-declaracion-historico/";
	
	public static final String URL_DECLARACION = "URL_DECLARACION";
	public static final String PATH_DECLARACION = "PATH_DECLARACION";
	
	/**
	 * 
	 */
	private Constantes() {
		throw new IllegalStateException("Constantes class........");
	}

}
