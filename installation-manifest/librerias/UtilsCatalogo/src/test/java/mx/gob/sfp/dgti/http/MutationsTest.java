/**
 * @(#)MutationsTest.java 29/07/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.http;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import mx.gob.sfp.dgti.verticle.Server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import static mx.gob.sfp.dgti.decnet.util.Constantes.*;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

/**
 *  Tests para las mutations que se tienen en GraphQL
 *
 *  @author Miriam Sanchez Sanchez programador07
 *  @author Pavel Alexei Martinez Regalado aka programador02
 *  @since 29/07/2019
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(VertxUnitRunner.class)
@PrepareForTest({ PostgreSQLClient.class })
public class MutationsTest {

	/**
	 * Cliente de PostgreSQL, que sera "mockeado" para simular sus resultados
	 */
	private PostgreSQLClient sqlClient;

	/**
	 * El vertx
	 */
	private Vertx vertx;

	/**
	 * Puerto donde se levanta el servicio en la pruebas
	 */
	private int port;

	/**
	 * El logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MutationsTest.class);

	/**
	 * Mutation agregarRegistro
	 */
	private static final String MUTATION_AGREGAR_REGISTRO =
			"{\"query\": \"mutation{ agregarRegistro (registro: {valor: \\\"Mockito Nuevo\\\" valorUno: \\\"MR\\\"}) {id valor valorUno activo}}\"}";

	/**
	 * Mutation actualizarRegistro
	 */
	private static final String MUTATION_ACTUALIZAR_REGISTRO =
			"{\"query\": \"mutation{ actualizarRegistro (registro: {id:1 activo:1 valor: \\\"MA\\\" valorUno: \\\"MR\\\"}) {id valor valorUno activo}}\"}";

	/**
	 * Antes de realizar los tests se le indica que hago un mock del cliente de sql, se declara
	 * que en la aplicacion se debe de utilizar el el sqlClient declarado en esta clase en vez
	 * del utilizado normalmente. Finalmente se declara que cuando se utilice en el cliente el
	 * metodo de queryWithParams se mande una respuesta generica, lo que permite probar todo el
	 * flujo.
	 *
	 * Se levnta un server que queda la escucha de llamadas en el puerto definido
	 *
	 * @param context
	 * @throws Exception
	 *
	 * @author Miriam Sanchez Sanchez programador07
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 29/07/2019
	 */
	@Before
	public void before(TestContext context) throws Exception {
		LOGGER.info("\n\n=== Creacion del mock para el cliente de PostgreSQLClient.");

		sqlClient = Mockito.mock(PostgreSQLClient.class);

		PowerMockito.mockStatic(PostgreSQLClient.class);

		PowerMockito.when(PostgreSQLClient.createShared(any(), any())).thenReturn(sqlClient);

		ResultSet miResultSet = crearResultSetGenerico();

		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				LOGGER.info("=== doAnswer sobre sqlClient.queryWithParams.");
				Object[] args = invocation.getArguments();
				Handler<AsyncResult<ResultSet>> handler = (Handler<AsyncResult<ResultSet>>)args[2];
				handler.handle(Future.succeededFuture(miResultSet));
				return null;
			}
		}).when(sqlClient).queryWithParams(any(),any(), any());

		vertx = Vertx.vertx();

		LOGGER.info("=== Inicia server de pruebas.");

		ServerSocket socket = new ServerSocket(0);
		port = socket.getLocalPort();
		socket.close();
		JsonObject config = new JsonObject().put(SERVER_PORT, port);
		DeploymentOptions options = new DeploymentOptions().setConfig(config);
		vertx.deployVerticle(Server.class.getName(), options, context.asyncAssertSuccess());
	}

	/**
	 * After
	 *
	 * @param context
	 */
	@After
	public void after(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}

	/**
	 * Test del funcionamiento de la mutation de agregarRegistro de GraphQL.
	 *
	 * @param context
	 * @throws Exception
	 *
	 * @author Miriam Sanchez Sanchez programador07
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 29/07/2019
	 */
	@Test
	public void testAgregarRegistro(TestContext context) throws Exception{
		LOGGER.info("\n\n=== Test para la Mutation de agregarRegistro -- INICIA");

		String json = MUTATION_AGREGAR_REGISTRO;

		Async async = (Async) context.async();
		vertx.createHttpClient().post(port, "localhost", "/privado/", response -> {
			context.assertEquals(response.statusCode(), HttpResponseStatus.OK.code());
			context.assertEquals(response.headers().get(HEADER_CONTENT_TYPE), HEADER_CONTENT_APPLICATION_JSON);
			response.bodyHandler(body -> {
				context.assertNotNull(body);
				async.complete();
				LOGGER.info(body);
				LOGGER.info("=== Test para la Mutation de agregarRegistro -- FINALIZADO");
			});

		}).putHeader("content-length", String.valueOf(json.length()))
			.putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON).write(json);
	}

	/**
	 * Test del funcionamiento de la mutation de actualizarRegistro de GraphQL.
	 *
	 * @param context
	 * @throws Exception
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 29/07/2019
	 */
	@Test
	public void testActualizarRegistro(TestContext context) throws Exception{
		LOGGER.info("\n\n=== Test para la Mutation de actualizarRegistro -- INICIA " + port);

		String json = MUTATION_ACTUALIZAR_REGISTRO;

		Async async = (Async) context.async();
		vertx.createHttpClient().post(port, "localhost", "/privado/", response -> {
			context.assertEquals(response.statusCode(), HttpResponseStatus.OK.code());
			context.assertEquals(response.headers().get(HEADER_CONTENT_TYPE), HEADER_CONTENT_APPLICATION_JSON);
			response.bodyHandler(body -> {
				context.assertNotNull(body);
				async.complete();
				LOGGER.info(body);
				LOGGER.info("=== Test para la Mutation de actualizarRegistro -- FINALIZADO");
			});

		}).putHeader("content-length", String.valueOf(json.length()))
				.putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON).write(json);
	}

	/**
	 * Crea un ResultSet para sustituir la respuesta que podria traer de la base.
	 *
	 * @return ResultSet
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 29/07/2019
	 */
	private ResultSet crearResultSetGenerico() {

		ResultSet resultSet = new ResultSet();
		List<String> columnas = new ArrayList<>();
		columnas.add("id");
		columnas.add("valor");
		columnas.add("valorUno");
		columnas.add("activo");
		columnas.add("fechaRegistro");

		JsonArray resultados = new JsonArray();
		resultados.add(1);
		resultados.add("Mockito Generico");
		resultados.add("MG");
		resultados.add(1);
		resultados.add("01/01/2019");

		List<JsonArray> listaArreglo = new ArrayList<>();
		listaArreglo.add(resultados);

		resultSet.setColumnNames(columnas);
		resultSet.setResults(listaArreglo);

		return resultSet;
	}
}
