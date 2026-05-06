/**
 * @(#)QueriesTest.java  30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.http;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.http.HttpClient;
import mx.gob.sfp.dgti.verticle.Server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.ServerSocket;

import static mx.gob.sfp.dgti.decnet.util.Constantes.*;

/**
 *  Tests para los queries que se tienen en GraphQL
 *
 *  @author Miriam Sanchez Sanchez programador07
 *  @author Pavel Alexei Martinez Regalado aka programador02
 *  @since 30/05/2019
 */
@RunWith(VertxUnitRunner.class)
public class QueriesTest {

	/**
	 * El vertx
	 */
	Vertx vertx;

	/**
	 * Puerto donde se levanta el servicio en la pruebas
	 */
	int port;

	/**
	 * El logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(QueriesTest.class);

	/**
	 * Query obtener catalogo
	 */
	private static final String QUERY_OBTENER_CATALOGO =
			"{\"query\": \"query{obtenerCatalogo{ id valor valorUno fk}}\"}";

	/**
	 * Query obtenerCatalogo activo
	 */
	private static final String QUERY_OBTENER_CATALOGO_ACTIVO =
			"{\"query\": \"query{obtenerCatalogoActivo{ id valor valorUno fk}}\"}";

	/**
	 * Query obtenerCatalogoPorId
	 */
	private static final String QUERY_OBTENER_CATALOGO_ID =
			"{\"query\": \"query{obtenerCatalogoPorId(id:1){ id valor valorUno fk}}\"}";

	/**
	 * Query para probar validacion de catalogo
	 */
	private static final String QUERY_VALIDAR_CATALOGO =
            "{\"query\": \"query{validarInfoCatalogo(registro: {id:1 valor: \\\"cualquierCosa\\\" }) }\"}";



	/**
	 * Se levanta un server que queda la escucha de llamadas en el puerto definido.
	 *
	 * @param context
	 * @throws Exception
	 *
	 * @author Miriam Sanchez Sanchez programador07
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 30/05/2019
	 */
	@Before
	public void before(TestContext context) throws IOException {
		vertx = Vertx.vertx();
		ServerSocket socket = new ServerSocket(0);
		port = socket.getLocalPort();
		socket.close();
		JsonObject config = new JsonObject().put(SERVER_PORT, port);
		DeploymentOptions options = new DeploymentOptions().setConfig(config);
		vertx.deployVerticle(Server.class.getName(), options, context.asyncAssertSuccess());
	}

	/**
	 *
	 * @param context
	 */
	@After
	public void after(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}


	/**
	 * Test con una llamada simple sobre la ruta del servidor.
	 *
	 * @param context
	 * @throws Exception
	 *
	 * @author Miriam Sanchez Sanchez programador07
	 * @since 30/05/2019
	 */
	public void testGet(TestContext context) {
		LOGGER.info("\n\n=== Test para simple, comprobar servidor responde -- INICIA");
		HttpClient client = vertx.createHttpClient();
	    Async async = context.async();
	    client.getNow(port, "localhost", "/", resp -> {
	      resp.exceptionHandler(context.exceptionHandler());
	      resp.bodyHandler(body -> {
	    	context.assertTrue(body.toString().contains("Declaranet"));
	        client.close();
	        async.complete();
	        LOGGER.info("=== Test para simple, comprobar servidor responde -- FINALIZADO");
	      });
	    });
	}

	/**
	 * Test del funcionamiento del query de obtenerCatalogo de GraphQL.
	 *
	 * @param context
	 * @throws Exception
	 *
	 * @author Miriam Sanchez Sanchez programador07
	 * @since 30/05/2019
	 */
	@Test
	public void testObtenerCatalogo(TestContext context) {
		LOGGER.info("\n\n=== Test para el query de obtenerCatalogo -- INICIA");

		String json = QUERY_OBTENER_CATALOGO;
		Async async = context.async();
		vertx.createHttpClient().post(port, "localhost", "/publico/", response -> {
			context.assertEquals(response.statusCode(), HttpResponseStatus.OK.code());
			context.assertEquals(response.headers().get(HEADER_CONTENT_TYPE), HEADER_CONTENT_APPLICATION_JSON);
			response.bodyHandler(body -> {
				context.assertNotNull(body);
				async.complete();
				LOGGER.info(body);
				LOGGER.info("=== Test para el query de obtenerCatalogo -- FINALIZADO");
			});

		}).putHeader("content-length", String.valueOf(json.length()))
			.putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON).write(json);
		;
	}

	/**
	 * Test del funcionamiento del query de obtenerCatalogoActivo de GraphQL.
	 *
	 * @param context
	 * @throws Exception
	 *
	 * @author Miriam Sanchez Sanchez programador07
	 * @since 30/05/2019
	 */
	@Test
	public void testObtenerCatalogoActivo(TestContext context) {
		LOGGER.info("\n\n=== Test para el query de obtenerCatalogoActivo -- INICIA");

		String json = QUERY_OBTENER_CATALOGO_ACTIVO;
		System.out.println(port);
		Async async = context.async();
		vertx.createHttpClient().post(port, "localhost", "/publico/", response -> {
			context.assertEquals(response.statusCode(), HttpResponseStatus.OK.code());
			context.assertEquals(response.headers().get(HEADER_CONTENT_TYPE), HEADER_CONTENT_APPLICATION_JSON);
			response.bodyHandler(body -> {
				context.assertNotNull(body);
				async.complete();
				LOGGER.info(body);
				LOGGER.info("=== Test para el query de obtenerCatalogoActivo -- FINALIZADO");
			});

		}).putHeader("content-length", String.valueOf(json.length()))
			.putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON).write(json);
	}

	/**
	 * Test del funcionamiento del query de obtenerCatalogoPorId de GraphQL.
	 *
	 * @param context
	 * @throws Exception
	 *
	 * @author Miriam Sanchez Sanchez programador07
	 * @since 30/05/2019
	 */
	@Test
	public void testObtenerCatalogoPorId(TestContext context) {
		LOGGER.info("\n\n=== Test para el query de obtenerCatalogoPorId -- INICIA");

		String json = QUERY_OBTENER_CATALOGO_ID;
		Async async = context.async();
		vertx.createHttpClient().post(port, "localhost", "/publico/", response -> {
			context.assertEquals(response.statusCode(), HttpResponseStatus.OK.code());
			context.assertEquals(response.headers().get(HEADER_CONTENT_TYPE), HEADER_CONTENT_APPLICATION_JSON);
			response.bodyHandler(body -> {
				context.assertNotNull(body);
				async.complete();
				LOGGER.info(body);
				LOGGER.info("=== Test para el query de obtenerCatalogoPorId -- FINALIZADO");
			});

		}).putHeader("content-length", String.valueOf(json.length()))
			.putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON).write(json);
	}

	/**
	 * Test del funcionamiento del validador de catalogo
	 *
	 * @param context
	 * @throws Exception
	 *
	 * @author Miriam Sanchez Sanchez programador07
	 * @since 30/05/2019
	 */
	@Test
	public void testValidarCatalogo(TestContext context) {
		LOGGER.info("\n\n=== Test para el query de validarInfoCatalogo -- INICIA");

		String json = QUERY_VALIDAR_CATALOGO;
		Async async = context.async();
		vertx.createHttpClient().post(port, "localhost", "/publico/", response -> {
			context.assertEquals(response.statusCode(), HttpResponseStatus.OK.code());
			context.assertEquals(response.headers().get(HEADER_CONTENT_TYPE), HEADER_CONTENT_APPLICATION_JSON);
			response.bodyHandler(body -> {
				context.assertNotNull(body);
				async.complete();
				LOGGER.info(body);
				LOGGER.info("=== Test para el query de validarInfoCatalogo -- FINALIZADO");
			});

		}).putHeader("content-length", String.valueOf(json.length()))
				.putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON).write(json);
	}

}
