package mx.gob.sfp.dgti.helper;

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
import static mx.gob.sfp.dgti.util.Constantes.HEADER_TRANSACCION;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.RoutingContext;
import mx.gob.sfp.dgti.dto.RespuestaDTO;
import mx.gob.sfp.dgti.util.Constantes;

/**
 * Clase helper para el verticle de Autentication
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
	private static Properties prop = null;
	private static final String HTTP = "http://";
	private static final String HTTPS = "https://";
	public static final String CACHE_CONTROL = "Cache-Control";
	public static final String PRAGMA = "Pragma";
	public static final String HEADER_PRAGMA = "no-cache";
	public static final String HEADER_CACHE_CONTROL = "no-store";
	public static final String JWT_JCEKS = "jceks";
	public static final String JWT_PSW = "secret";
	public static final String KEYSTORE = "keystore.jceks";
	public static final String SEPARATOR = "+";
	
	/**
	 * Mátodo para retornar la respuesta y estatus
	 * @param context RoutingContext
	 * @return Handler<AsyncResult<T>>
	 */
	public static <T> Handler<AsyncResult<T>> writeJsonResponse(RoutingContext context) {
		
		return ar -> {
			if (ar.failed()) {
				if (ar.cause() instanceof NoSuchElementException) {
					context.response().setStatusCode(200).end(ar.cause().getMessage());
				} else {
					context.fail(ar.cause());
				}
			} else {
				context.response().setStatusCode(HttpResponseStatus.OK.code())
							.putHeader(Constantes.HEADER_CONTENT_TYPE, Constantes.HEADER_CONTENT_APPLICATION_JSON)
			    			.end(Json.encodePrettily(ar.result()));
			}
		};
	}
	
	/**
	 * Método para construir un JsonObject a partir de un objeto Response
	 * @param response Objeto de respuesta
	 * @return JsonObject
	 */
	public static JsonObject jsonConvert(RespuestaDTO response) {
		return new JsonObject(Json.encode(response));
	}

	/**
	 * Obtener JWTAuthOptions para la autenticación con tokens
	 * 
	 * @return JWTAuthOptions
	 * @throws URISyntaxException
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
	 * @param dominio dominio del cliente que hace la petición
	 * @return String ip o dns para validación
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
	 * @param parametro nombre del parámetro
	 *            nombre de la propiedad con la que se generó el token
	 * @return String valor de la propiedad
	 */
	public String obtenerPropiedadToken(RoutingContext routingContext, String parametro) {
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
	 * @param token cadena con el token 
	 * @param clienteId id de la aplicación cliente
	 * @param curp curp del usuario
	 * @return String Devuelve una cadena codificada en base64
	 */
	public String codificarBase64(String token, Integer clienteId, String curp, String fecha) {
		String cadena = token + SEPARATOR + clienteId + SEPARATOR + curp + SEPARATOR + fecha;
		byte[] cadenaBytes = new Base64(true).encodeBase64(cadena.getBytes());
		return new String(cadenaBytes);
	}

	/**
	 * Método para decodificar en base64
	 * 
	 * @param codigo cadena codificada
	 * @return String cadena decodificada
	 */
	public String decodificarBase64(String codigo) {
		byte[] valorDecodificado = Base64.decodeBase64(codigo);
		return new String(valorDecodificado);
	}

	/**
	 * Método para obtener una propiedad de la transacción
	 * 
	 * @param transaccion Cadena de transacción 
	 * @param propiedad propiedad a obtener de la transacción
	 * @return String cadena con el valor de la propiedad
	 */
	public String obtenerPropiedadTransaccion(String transaccion, Integer propiedad) {
		String[] array = transaccion.split("\\" + SEPARATOR);
		return array[propiedad];
	}
	
    public static String obtenerPropiedad(String nombre) {
        if (prop==null){
            prop = new Properties();
            try {
                prop.load(AutenticacionHelper.class.getClassLoader().getResourceAsStream(PROPERTIES));
            } catch (IOException e) {
                    LOGGER.error(e);
            }
        }
        return prop.getProperty(nombre);
    }
	
	/**
	 * Se obtiene la configuracion de la base de datos
	 * @return JsonObject json con la configuración de la BD
	 */
	public static JsonObject getConfig() {
		return new JsonObject()
				.put(CONFIG_HOST, IP_HOST)
				.put(CONFIG_PORT, IP_PORT)
				.put(CONFIG_DATABASE, IP_DATABASE)
				.put(CONFIG_USERNAME, IP_USERNAME)
				.put(CONFIG_PSW, IP_PWD);
	}
	
	/**
	 * Headers permitidos para CORS
	 * @return Set<String> Headers permitidos en las peticiones
	 */
	public static Set<String> getAllowedHeaders() {
		Set<String> allowedHeaders = new HashSet<>();
	    allowedHeaders.add(HEADER_DOMINIO);
	    allowedHeaders.add(HEADER_AUTHORIZATION_KEY);
	    allowedHeaders.add(HEADER_CONTENT_TYPE);
	    allowedHeaders.add(HEADER_TRANSACCION);
	    return allowedHeaders;
	}
	
	/**
	* Se agregan los headers correspondientes para el Intercambio de recursos de
	* origen cruzado (CORS).
	* 
	* @param routingContext contexto
	* @return HttpServerResponse
	*/
	public static HttpServerResponse obtenerHeaders(RoutingContext routingContext) {
		return routingContext.response().putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
				.putHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS)
				.putHeader(ACCESS_CONTROL_ALLOW_METHODS, HEADER_ACCESS_CONTROL_ALLOW_METHODS)
				.putHeader(ACCESS_CONTROL_ALLOW_HEADERS, HEADER_ACCESS_CONTROL_ALLOW_HEADERS)
				.putHeader(CACHE_CONTROL, HEADER_CACHE_CONTROL).putHeader(PRAGMA, HEADER_PRAGMA)
				.putHeader(ACCESS_CONTROL_EXPOSE_HEADERS, HEADER_TRANSACCION)
				.putHeader(HEADER_DOMINIO, HEADER_DOMINIO)
				.putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON);
	}
}
