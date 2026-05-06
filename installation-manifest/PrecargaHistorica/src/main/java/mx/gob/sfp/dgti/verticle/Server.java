package mx.gob.sfp.dgti.verticle;

import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_PORT;
import static mx.gob.sfp.dgti.util.Constantes.EMPTY;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_HEADERS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_METHODS;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_APPLICATION_JSON;
import static mx.gob.sfp.dgti.util.Constantes.HEADER_CONTENT_TYPE;
import static mx.gob.sfp.dgti.util.Constantes.PATH;
import static mx.gob.sfp.dgti.util.Constantes.SERVER_PORT;
import static mx.gob.sfp.dgti.util.Constantes.TEXT_HTML;
import static mx.gob.sfp.dgti.util.Constantes.URL_PATH;

import java.util.HashSet;
import java.util.Set;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import mx.gob.sfp.dgti.service.ServiceMigracionDeclaracionInterface;

/**
 * Clase con la definición de los paths para validar el módulo de datos
 * generales de Declaranet
 * 
 * @author programador04
 * @since 10/01/2020
 */
public class Server extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

	private ServiceMigracionDeclaracionInterface serviceMigracionDeclaracionInterface;

	/**
	 * Start inicializar router y la definición de paths
	 * 
	 * @throws Exception
	 */
	@Override
	public void start(Future<Void> future) throws Exception {
		super.start();
		serviceMigracionDeclaracionInterface = ServiceMigracionDeclaracionInterface.init(vertx);
		Router router = Router.router(vertx);
		router.route()
				.handler(CorsHandler.create(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).allowedHeaders(getAllowedHeaders()));
		router.route(PATH).handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader(HEADER_CONTENT_TYPE, TEXT_HTML).end("Precarga de declaracion");
		});

		router.route().handler(BodyHandler.create());
		router.post(PATH+URL_PATH).handler(this::obtenerJsonDeclaracion);
		router.route().handler(StaticHandler.create());
		this.getVertx().createHttpServer().requestHandler(router::accept)
				.listen(config().getInteger(CONFIG_PORT,
						Integer.parseInt(System.getenv(SERVER_PORT) != null ? System.getenv(SERVER_PORT) : "5000")),
						ar -> {
							if (ar.succeeded()) {
								LOGGER.info("Running");
								future.complete();
							} else {
								LOGGER.info(ar.cause());
								future.fail(ar.cause());
							}
						});
	}

	/**
	 * Validador del módulo de datos generales
	 * 
	 * @param context contexto de la petición que contiene los stringParams, header
	 *                y body de la petición
	 */
	private void obtenerJsonDeclaracion(RoutingContext context) {
		LOGGER.info("====================== OBTENER JSON DNET ====================== ");
		if (!EMPTY.equals(context.getBodyAsString())) {
			try {
				serviceMigracionDeclaracionInterface.generarJsonDeclaracion(context.getBodyAsJson().getInteger("numDeclaracion")).setHandler(declaracion -> {
					if (declaracion.succeeded()) {
						context.response().end(Json.encode(declaracion.result()));
					} else {
						context.response().end(Json.encode(declaracion.cause()));
					}
				});
			}catch (Exception e) {
				LOGGER.fatal("Error en el servicio precarga:",e.fillInStackTrace());
				context.response().end("Verifica el envío de datos a la petición");
			}
			
		} else {
			context.response().end("Verifica el envío de datos a la petición");
		}
	}

	/**
	 * Se agregan los headers correspondientes para el Intercambio de recursos de
	 * origen cruzado (CORS).
	 * 
	 * @param routingContext contexto
	 * @return HttpServerResponse
	 */
	public HttpServerResponse obtenerHeaders(RoutingContext routingContext) {
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
		allowedHeaders.add(HEADER_CONTENT_TYPE);
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