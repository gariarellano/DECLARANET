package mx.gob.sfp.dgti.util;

import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_EXPOSE_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_DATABASE;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_HOST;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_PORT;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_PSW;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_USERNAME;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_AUTHORIZATION_KEY;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_APPLICATION_JSON;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_TYPE;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_DOMINIO;
import static mx.gob.sfp.dgti.util.Constantes.IP_DATABASE_HOST;
import static mx.gob.sfp.dgti.util.Constantes.IP_DATABASE_NAME;
import static mx.gob.sfp.dgti.util.Constantes.IP_DATABASE_PORT;
import static mx.gob.sfp.dgti.util.Constantes.IP_DATABASE_PSW;
import static mx.gob.sfp.dgti.util.Constantes.IP_DATABASE_USERNAME;
import static mx.gob.sfp.dgti.util.Constantes.PROPERTIES;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.RoutingContext;
import mx.gob.sfp.dgti.dto.RespuestaDTO;

/**
 * Clase helper para el verticle de Registro
 * @author Miriam Sánchez Sánchez programador07 
 * @since 05/03/2019
 */
public class AutenticacionHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutenticacionHelper.class);
        private static final String JSN_CONFIG_CONNECTION = "JSN_CONFIG_CONNECTION";
        private static final JsonObject CONFIG_CONNECTION = new JsonObject(System.getenv(JSN_CONFIG_CONNECTION));
	private static final String IP_HOST = CONFIG_CONNECTION.getString(IP_DATABASE_HOST);
	private static final Integer IP_PORT = CONFIG_CONNECTION.getInteger(IP_DATABASE_PORT);
	private static final String IP_DATABASE = CONFIG_CONNECTION.getString(IP_DATABASE_NAME);
	private static final String IP_USERNAME = CONFIG_CONNECTION.getString(IP_DATABASE_USERNAME);
	private static final String IP_PWD = CONFIG_CONNECTION.getString(IP_DATABASE_PSW);
	private static final String CACHE_CONTROL = "Cache-Control";
	private static final String HEADER_PRAGMA = "no-cache";
	private static final String HEADER_CACHE_CONTROL = "no-store";
	private static final String PRAGMA = "Pragma";
	private static final String HTTP = "http://";
	private static final String HTTPS = "https://";
	private static final String JWT_JCEKS = "jceks";
	private static final String JWT_PSW = "secret";
	private static final String KEYSTORE = "keystore.jceks";
	private static final String SEPARATOR = "+";
	
	/**
	 *Metodo para retornar la respuesta y estatus
	 * @param context
	 * @return generated handler
	 */
	public static <T> Handler<AsyncResult<T>> writeJsonResponse(RoutingContext context) {
		
		return ar -> {
			if (ar.failed()) {
				if (ar.cause() instanceof NoSuchElementException) {
					RespuestaDTO respuesta = new RespuestaDTO();
					respuesta.setError(ar.cause().getMessage());
					context.response().setStatusCode(200).end(Json.encodePrettily(respuesta));
				} else {
					context.fail(ar.cause());
				}
			} else {
				context.response().setStatusCode(HttpResponseStatus.OK.code())
							.putHeader(Constantes.HEADER_CONTENT_TYPE, Constantes.HEADER_CONTENT_APPLICATION_JSON).end();
			}
		};
	}
	
	/**
	 * Método para construir un JsonObject a partir de un objeto Response
	 * 
	 * @param response
	 * @return JsonObject
	 */
	public static JsonObject jsonConvert(RespuestaDTO response) {
		return new JsonObject(Json.encode(response));
	}

	/**
	 * Obtener JWTAuthOptions para la autenticación con tokens
	 * 
	 * @return JWTAuthOptions
	 */
	public static JWTAuthOptions obtenerJwt() {
		JWTAuthOptions authConfiguracion = null;
		try {
			authConfiguracion = new JWTAuthOptions()
					.setKeyStore(new KeyStoreOptions().setType(JWT_JCEKS).setPath(KEYSTORE).setPassword(JWT_PSW));

		} catch (Exception e) {
			LOGGER.error(e);
		}

		return authConfiguracion;
	}

	/**
	 * Método para obtener el dominio de la url origin de donde se hace la
	 * petición
	 * 
	 * @param dominio de la aplicación cliente
	 * @return String: cadena con el dominio 
	 */
	public static String validarDominio(String dominio) {
		String dominioV = dominio.replace(HTTP, "").replace(HTTPS, "");
		if (dominioV.contains(":")) {
			dominioV = dominioV.substring(0, dominioV.indexOf(':'));
		} else if (dominioV.contains("/")) {
			dominioV = dominioV.substring(0, dominioV.indexOf('/'));
		}
		LOGGER.info("Dominio >>> " + dominioV);
		return dominioV;
	}

	/**
	 * Método para obtener un parámetro que exista en un token
	 * 
	 * @param routingContext
	 * @param parametro
	 *            nombre de la propiedad con la que se generó el token
	 * @return String: valor de la propiedad
	 */
	public static String obtenerPropiedadToken(RoutingContext routingContext, String parametro) {
		String valor = null;
		if (ValidacionHelper.isNotNull(routingContext.user().principal().getValue(parametro).toString())) {
			valor = routingContext.user().principal().getValue(parametro).toString();
		}
		return valor;
	}

	/**
	 * Método para generar una cadena codificada en base64 con la información del
	 * cliente, el usuario y la fecha
	 * 
	 * @param token generado
	 * @param clienteId id de la aplicación 
	 * @param curp el usuario
	 * @return String: cadena codificada base 64
	 */
	public static String codificarBase64(String token, Integer clienteId, String curp, String fecha) {
		String cadena = token + SEPARATOR + clienteId + SEPARATOR + curp + SEPARATOR + fecha;
		byte[] cadenaBytes = new Base64(true).encodeBase64(cadena.getBytes());
		return new String(cadenaBytes);
	}

	/**
	 * Método para decodificar en base64
	 * 
	 * @param codigo a decodificar
	 * @return String: cadena con el valor decodificado
	 */
	public static String decodificarBase64(String codigo) {
		byte[] valorDecodificado = Base64.decodeBase64(codigo);
		return new String(valorDecodificado);
	}

	/**
	 * Método para obtener una propiedad de la transacción
	 * 
	 * @param transaccion
	 * @param propiedad
	 *            número de posición
	 * @return String: cadena con el valor de la propiedad
	 */
	public static String obtenerPropiedadTransaccion(String transaccion, Integer propiedad) {
		String[] array = transaccion.split("\\" + SEPARATOR);
		return array[propiedad];
	}
	
	/**
	 * Obtener una propiedad del archivo properties
	 * @param nombre de la propiedad
	 * @return String: valor de la propiedad
	 */
	public static String obtenerPropiedad(String nombre) {
		InputStream input = null;
		Properties prop = new Properties();
        try {
        	input = AutenticacionHelper.class.getClassLoader().getResourceAsStream(PROPERTIES);
            prop.load(input);
		} catch (IOException e) {
			LOGGER.error(e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOGGER.error(e);
				}
			}
		}
        return prop.getProperty(nombre);
	}
	
	/**
	 * Headers permitidos para CORS
	 * @return Set<String>
	 */
	public static Set<String> getAllowedHeaders() {
		Set<String> allowedHeaders = new HashSet<>();
	    allowedHeaders.add(HEADER_DOMINIO);
	    allowedHeaders.add(HEADER_AUTHORIZATION_KEY);
	    allowedHeaders.add(HEADER_CONTENT_TYPE);
	    return allowedHeaders;
	}
	
	/**
	 * Métodos permitidos para CORS
	 * @return Set<HttpMethod>
	 */
	public static Set<HttpMethod> getAllowedMethods() {
		Set<HttpMethod> allowedMethods = new HashSet<>();
		allowedMethods.add(HttpMethod.GET);
		allowedMethods.add(HttpMethod.POST);
		allowedMethods.add(HttpMethod.OPTIONS);
		allowedMethods.add(HttpMethod.PUT);  
		return allowedMethods;
	}
	
	/**
	 * Se obtiene la configuracion de la base de datos
	 * @return JsonObject
	 */
	public static JsonObject getConfig() {
		return new JsonObject()
				.put(CONFIG_HOST, IP_HOST)
				.put(CONFIG_PORT, IP_PORT)
				.put(CONFIG_DATABASE, IP_DATABASE)
				.put(CONFIG_USERNAME, IP_USERNAME)
				.put(CONFIG_PSW, IP_PWD);
//				.put(MAX_POOL_SIZE, MAX_POOL_SIZE_VALUE);
	}
	
	/**
	* Se agregan los headers correspondientes para el Intercambio de recursos de
	* origen cruzado (CORS).
	* 
	* @param routingContext: contexto
	* @return HttpServerResponse
	*/
	public static HttpServerResponse obtenerHeaders(RoutingContext routingContext) {
		return routingContext.response().putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
				.putHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS)
				.putHeader(ACCESS_CONTROL_ALLOW_METHODS, HEADER_ACCESS_CONTROL_ALLOW_METHODS)
				.putHeader(ACCESS_CONTROL_ALLOW_HEADERS, HEADER_ACCESS_CONTROL_ALLOW_HEADERS)
				.putHeader(CACHE_CONTROL, HEADER_CACHE_CONTROL).putHeader(PRAGMA, HEADER_PRAGMA)
				.putHeader(ACCESS_CONTROL_EXPOSE_HEADERS, HEADER_ACCESS_CONTROL_ALLOW_HEADERS)
				.putHeader(HEADER_DOMINIO, HEADER_DOMINIO)
				.putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON);
	}
}
