/* 
 * Sistema Propiedad de la Secretaría de la Función Pública DGTI
 * "VerificarDeclaraciones" Servicio que permite verificar si 
 * existen declaraciones en proceso o insertar el tipo de declaración a presentar
 * 
 * Proyecto desarrollado por programador04@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.verticle;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.reactivex.core.AbstractVerticle;
import java.util.HashSet;
import java.util.Set;
import mx.gob.sfp.dgti.declaracion.encrypt.im.GeneraMensajeFront;
import mx.gob.sfp.dgti.service.ServiceInicioDeclaracionInterface;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_PORT;
import static mx.gob.sfp.dgti.util.Constantes.EMPTY;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_CONSULTA_ANIOS_DECLA_MOD_EXT;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_CONSULTA_HISTORIAL_DECLA;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_CREAR_DECLARACION_TEMP;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_CREAR_NOTA;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_ELIMINAR_DECLARACION_TEMPORAL;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_OBTENER_HISTORIAL_NOTA;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_VERIFICANDO_DECLARACIONES;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_AUTHORIZATION_KEY;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_APPLICATION_JSON;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_TYPE;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_DOMINIO;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_TRANSACCION;
import static mx.gob.sfp.dgti.util.Constantes.PATH;
import static mx.gob.sfp.dgti.util.Constantes.PATHERN_DIGITOS;
import static mx.gob.sfp.dgti.util.Constantes.PATH_CONSULTAR_HISTORIAL;
import static mx.gob.sfp.dgti.util.Constantes.PATH_CONSULTAR_NOTAS;
import static mx.gob.sfp.dgti.util.Constantes.PATH_CONSULTA_ANIOS_DECLA_MOD_EXT;
import static mx.gob.sfp.dgti.util.Constantes.PATH_CREAR_NOTA;
import static mx.gob.sfp.dgti.util.Constantes.PATH_CREAR_TEMPORAL;
import static mx.gob.sfp.dgti.util.Constantes.PATH_ELIMINAR_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.PATH_INICIAR;
import static mx.gob.sfp.dgti.util.Constantes.PATH_VERIFICAR;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ESTATUS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MENSAJE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MENSAJE_VERIFICAR;
import static mx.gob.sfp.dgti.util.Constantes.PROP_RESPUESTA;
import static mx.gob.sfp.dgti.util.Constantes.TEXT_HTML;



/**
 * Clase que permite realizar los llamados a los path's del microservicio y
 * poder obtener los resultados.
 * 
 * @author programador04@sfp.gob.mx
 * @since 29/10/2019
 */
public class Server extends AbstractVerticle {
	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
	private ServiceInicioDeclaracionInterface serviceInicioDeclaracionInterface;

	private static final String MESSAGE_TEST_SANITY = "Servicio para el incio de las declaraciones";
	private static Router router;
    private WebClient client;

    @SuppressWarnings("deprecation")
	@Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        router = Router.router(vertx);
        router.route().handler(
                CorsHandler.create(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).allowedHeaders(getAllowedHeaders()));
        router.route(PATH).handler(routingContext -> {
            HttpServerResponse httpResponse = routingContext.response();
            httpResponse.putHeader(HEADER_CONTENT_TYPE, TEXT_HTML).end(MESSAGE_TEST_SANITY);
        });
        client = WebClient.create(vertx);
        this.getVertx().createHttpServer().requestHandler(router::accept)
                .listen(config().getInteger(CONFIG_PORT, 5000), ar -> {
                    if (ar.succeeded()) {
                        LOGGER.info("Hola inicio declaración");
                    } else {
                        LOGGER.info(ar.cause());
                    }
                });
    }

    
    
	/**
	 * Start inicializar router y la definición de paths
	 * 
	 * @param future
	 * @throws Exception
	 */
	@Override
	public void start(Future<Void> future) throws Exception {
		super.start(future);
		serviceInicioDeclaracionInterface = ServiceInicioDeclaracionInterface.init(vertx, client);
		router.route().handler(BodyHandler.create());
		router.post(PATH_INICIAR).handler(this::iniciarDeclaraciones);
		router.post(PATH_VERIFICAR).handler(this::verificarDeclaraciones);
		router.post(PATH_CREAR_TEMPORAL).handler(this::crearDeclaracionTemporal);
		router.post(PATH_CONSULTA_ANIOS_DECLA_MOD_EXT).handler(this::consultarAniosDeclaModExtemporanea);
		router.post(PATH_ELIMINAR_DECLARACION).handler(this::eliminarDeclaracionTemporal);
		router.post(PATH_CONSULTAR_HISTORIAL).handler(this::historialDeclaracionesUsuario);
		router.post(PATH_CREAR_NOTA).handler(this::crearNotaDeDeclaracion);
		router.post(PATH_CONSULTAR_NOTAS).handler(this::consultarNotas);
	}

	/**
	 * Método que verifica las declaraciones al iniciar sesión
	 * @param routingContext
	 */
	private void iniciarDeclaraciones(RoutingContext routingContext) {
		obtenerHeaders(routingContext);
		if (!EMPTY.equals(routingContext.getBodyAsString())) {
			serviceInicioDeclaracionInterface.inicioDeclaracion(routingContext.getBodyAsJson()).setHandler(inicio -> {
				if (inicio.succeeded()) {
					routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
							.end(Json.encode(inicio.result()));
				} else {
					routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
							.end(Json.encode(Json.decodeValue(inicio.cause().getMessage())));
                                        LOGGER.fatal(inicio.cause().getMessage());
				}
			});
		} else {
			LOGGER.fatal(ERROR_VERIFICANDO_DECLARACIONES);
			routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
					.end(Json.encode(generarVerificarDatos()));
		}
	}
	
	/**
	 * Método que verifica las declaraciones cuando ya existe la sesión
	 * @param routingContext
	 */
	private void verificarDeclaraciones(RoutingContext routingContext) {
		obtenerHeaders(routingContext);
		if (!EMPTY.equals(routingContext.getBodyAsString())) {
			serviceInicioDeclaracionInterface.verificarDeclaracion(routingContext.getBodyAsJson()).setHandler(verificar -> {
				if (verificar.succeeded()) {
					routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
							.end(Json.encode(verificar.result()));
				} else {
					LOGGER.fatal(verificar.cause().getMessage());
					routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
							.end(Json.encode(Json.decodeValue(verificar.cause().getMessage())));
				}
			});
		} else {
			LOGGER.fatal(ERROR_VERIFICANDO_DECLARACIONES);
			routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
					.end(Json.encode(generarVerificarDatos()));
		}
		
	}

	/**
	 * Método que crea la declaración temporal
	 * @param routingContext
	 */
	private void crearDeclaracionTemporal(RoutingContext routingContext) {
		obtenerHeaders(routingContext);
		if (!EMPTY.equals(routingContext.getBodyAsString())) {
			serviceInicioDeclaracionInterface.crearDeclaracionTemporal(routingContext.getBodyAsJson()).setHandler(crear -> {
				if (crear.succeeded()) {
					routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
							.end(Json.encode(crear.result()));
				} else {
					LOGGER.fatal(crear.cause().getMessage());
					routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
							.end(Json.encode(Json.decodeValue(crear.cause().getMessage())));
				}
			});
		} else {
			LOGGER.fatal(ERROR_CREAR_DECLARACION_TEMP);
			routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
					.end(Json.encode(generarVerificarDatos()));
		}
	}

	/**
	 * Método que consulta los años disponibles para una declaración
	 * de modificación extemporanea
	 * @param routingContext
	 */
	private void consultarAniosDeclaModExtemporanea(RoutingContext routingContext) {
		obtenerHeaders(routingContext);
		if (!EMPTY.equals(routingContext.getBodyAsString())) {
			serviceInicioDeclaracionInterface.consultaAniosDeclaExtemporanea(routingContext.getBodyAsJson()).setHandler(anios -> {
				if (anios.succeeded()) {
					routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
							.end(Json.encode(anios.result()));
				} else {
					LOGGER.fatal(anios.cause().getMessage());
					routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
							.end(Json.encode(Json.decodeValue(anios.cause().getMessage())));
				}
			});
		} else {
			LOGGER.fatal(ERROR_CONSULTA_ANIOS_DECLA_MOD_EXT);
			routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
					.end(Json.encode(generarVerificarDatos()));
		}
	}

	/**
	 * Método usado para eliminar la declaración temporal
	 * @param routingContext
	 */
	private void eliminarDeclaracionTemporal(RoutingContext routingContext) {
		obtenerHeaders(routingContext);
		if (!EMPTY.equals(routingContext.getBodyAsString())) {
			serviceInicioDeclaracionInterface.eliminarDeclaracionTemporal(routingContext.getBodyAsJson()).setHandler(elimina -> {
				if (elimina.succeeded()) {
					routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
							.end(Json.encode(elimina.result()));
				} else {
					LOGGER.fatal(elimina.cause().getMessage());
					routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
							.end(Json.encode(Json.decodeValue(elimina.cause().getMessage())));
				}
			});
		} else {
			LOGGER.fatal(ERROR_ELIMINAR_DECLARACION_TEMPORAL);
			routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
					.end(Json.encode(generarVerificarDatos()));
		}
	}

	/**
	 * Método usado para consultar el historial de declaraciones del usuario
	 * @param routingContext
	 */
	private void historialDeclaracionesUsuario(RoutingContext routingContext) {
		obtenerHeaders(routingContext);
		if (!EMPTY.equals(routingContext.getBodyAsString())) {
			try {
			serviceInicioDeclaracionInterface.obtenerHistorialDeclaraciones(routingContext.getBodyAsJson()).setHandler(histo -> {
				if (histo.succeeded()) {
					routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
							.end(Json.encode(histo.result()));
				} else {
					LOGGER.fatal(histo.cause().getMessage());
					routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
							.end(Json.encode(Json.decodeValue(histo.cause().getMessage())));
				}
			});
			}catch (Exception e) {
				LOGGER.fatal(ERROR_CONSULTA_HISTORIAL_DECLA+": "+e);
				routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
						.end(Json.encode(generarVerificarDatos()));
			}
		} else {
			LOGGER.fatal(ERROR_CONSULTA_HISTORIAL_DECLA);
			routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
					.end(Json.encode(generarVerificarDatos()));
		}
	}
	
	/**
	 * Método usado para consultar las notas de una declaracion
	 * @param routingContext
	 */
	private void consultarNotas(RoutingContext routingContext) {
		obtenerHeaders(routingContext);
		if (!EMPTY.equals(routingContext.getBodyAsString())) {
			try {
			serviceInicioDeclaracionInterface.obtenerHistorialNotasDeclaracion(routingContext.getBodyAsJson()).setHandler(histo -> {
				if (histo.succeeded()) {
					routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
							.end(Json.encode(histo.result()));
				} else {
					LOGGER.fatal(histo.cause().getMessage());
					routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
							.end(Json.encode(Json.decodeValue(histo.cause().getMessage())));
				}
			});
			}catch (Exception e) {
				LOGGER.fatal(String.format("%s %s", ERROR_OBTENER_HISTORIAL_NOTA, e));
				routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
						.end(Json.encode(generarVerificarDatos()));
			}
		} else {
			LOGGER.fatal(ERROR_OBTENER_HISTORIAL_NOTA);
			routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
					.end(Json.encode(generarVerificarDatos()));
		}
	}
	
	/**
	 * Método que consulta los años disponibles para una declaración
	 * de modificación extemporanea
	 * @param routingContext
	 */
	private void crearNotaDeDeclaracion(RoutingContext routingContext) {
		obtenerHeaders(routingContext);
		if (!EMPTY.equals(routingContext.getBodyAsString())) {
			try {
				serviceInicioDeclaracionInterface.crearNotaDeDeclaracion( routingContext.getBodyAsJson()).setHandler(crearNota ->{
					  if(crearNota.succeeded()) {
				           routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
				        			.end(Json.encode(crearNota.result()));
				      }else {
				          LOGGER.fatal(crearNota.cause().getMessage());
			               routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
			     			.end(Json.encode(Json.decodeValue(crearNota.cause().getMessage())));
			        }
				});
			}catch (Exception e) {
				LOGGER.fatal(String.format("%s %s", ERROR_CREAR_NOTA,e));
				routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
						.end(Json.encode(generarVerificarDatos()));
			}
		} else {
			LOGGER.fatal(ERROR_CONSULTA_ANIOS_DECLA_MOD_EXT);
			routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
					.end(Json.encode(generarVerificarDatos()));
		}
	}
	

    /**
     * @param cadena
     * @return
     */
    public boolean isNumber(String cadena) {
       return PATHERN_DIGITOS.matcher(cadena).matches();
    }
	

	/**
	 * Método que genera el json si hay algún error al validar datos
	 * 
	 * @param data JsonObject que se recibe en la peetición
	 * @return retorna un JsonObject que incluye la declaración a continuar junto
	 *         con la data de la petición
	 * 
	 * @author programador04@sfp.gob.mx
	 * @since 21/11/2019 edited 27/11/2019
	 */
	private JsonObject generarVerificarDatos() {
		return crearMensajeConFidelidad(new JsonObject().put(PROP_RESPUESTA,
				new JsonObject().put(PROP_ESTATUS, false).put(PROP_MENSAJE, PROP_MENSAJE_VERIFICAR)));
	}

	private JsonObject crearMensajeConFidelidad(JsonObject respuesta) {
		return GeneraMensajeFront.fidelidadMensaje(respuesta);
	}

	/**
	 * Se agregan los headers correspondientes para el Intercambio de recursos de
	 * origen cruzado (CORS).
	 * 
	 * @param routingContext
	 * @return
	 */
	public static HttpServerResponse obtenerHeaders(RoutingContext routingContext) {
		return routingContext.response().putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
				.putHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS)
				.putHeader(ACCESS_CONTROL_ALLOW_METHODS, HEADER_ACCESS_CONTROL_ALLOW_METHODS)
				.putHeader(ACCESS_CONTROL_ALLOW_HEADERS, HEADER_ACCESS_CONTROL_ALLOW_HEADERS)
				.putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON);
	}

	/**
	 * Headers permitidos para CORS
	 * 
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
	 * Main para pruebas locales
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Vertx vertex = Vertx.vertx();
		vertex.deployVerticle(new ServerLauncher());
	}
}
