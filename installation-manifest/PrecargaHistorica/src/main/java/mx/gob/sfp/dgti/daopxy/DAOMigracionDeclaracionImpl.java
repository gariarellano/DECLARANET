package mx.gob.sfp.dgti.daopxy;


import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

/**
 * Clase que implemnta la interface DAOAdministrarRelacionesInterface Clase que
 * coontiene los metodos uilizados para la manipulación de información en la
 * base de datos.
 *
 * @author gustavo gutierrez ponce
 * @since 10/01/2020
 */
public class DAOMigracionDeclaracionImpl implements DAOMigracionDeclaracionInterface {

	private static final Logger logger = Logger.getLogger(DAOMigracionDeclaracionImpl.class.getName());
	private static final String ERRROR_PARAM = "ERROR %s %s";
        private static final String QUERY_PROPERTIES = "querys.properties";
        private static final String QUERY_DATOS_CURRICULARES  = "moduloDatosCurriculares";
        private static final String QUERY_EXPERIENCIA_LABORAL  = "moduloExperienciaLab";
        private static final String QUERY_DATOS_PAREJA  = "moduloDatosParejas";
        private static final String QUERY_DEPENDIENTES_ECONOMICOS = "moduloDependientesEco";
        private static final String QUERY_INVERSIONES = "moduloInversiones";
        private static final String QUERY_ADEUDOS = "moduloAdeudos";
        private static final String QUERY_MUEBLES = "moduloMuebles";
        private static final String QUERY_INMUEBLES = "moduloInmuebles";
        private static final String QUERY_VEHICULOS = "moduloVehiculos";
        
	/**
	 * Cliente para la DB
	 */
	private final JDBCClient conexion;
	
	private static final Properties properties = new Properties();
	

	/**
	 * Constructor que recibe un objeto vertx y el json de la configuración para la
	 * creacion la conexión a la bd
	 * 
	 * @param vertx  propiedad que alacena el objeto vertx
	 * @param config propiedad tipo JsonObject que contiene la configuración de la
	 *               base de datos
	 */
	public DAOMigracionDeclaracionImpl(Vertx vertx, JsonObject config) {
		conexion = JDBCClient.createShared(vertx, config, "share-final-connection");
		try {
			properties.load(DAOMigracionDeclaracionImpl.class.getClassLoader().getResourceAsStream(QUERY_PROPERTIES));
			logger.info(" archivo propeties encontrado");
		} catch (IOException e) {
			logger.severe(String.format(ERRROR_PARAM, "Error al leer el archivo properties", e));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Future<List<JsonObject>> obtenerDatosCurriculares(int numDeclaracion) {
		Future<List<JsonObject>> future = Future.future();
		conexion.getConnection(conn -> {
			if (conn.succeeded()) {
				SQLConnection open = conn.result();
				open.queryWithParams(properties.getProperty(QUERY_DATOS_CURRICULARES), new JsonArray().add(numDeclaracion),
						resultado -> {
							open.close();
							if (resultado.succeeded()) {
								future.handle(resultado.map(rs ->{return rs.getRows();}));
							} else {
								future.handle(Future.failedFuture(resultado.cause().getMessage()));
                                                                logger.severe(String.format(ERRROR_PARAM, "Error al obtener los datos curriculares: ",resultado.cause().getMessage()));
							}
						});
			} else {
				future.handle(Future.failedFuture(conn.cause()));
                                logger.severe(String.format(ERRROR_PARAM, "Error conexion obtenerDatosCurriculares: ", conn.cause().getMessage()));
			}
		});
		return future;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Future<List<JsonObject>> obtenerExperienciaLaboral(int numDeclaracion) {
		Future<List<JsonObject>> future = Future.future();
		conexion.getConnection(conn -> {
			if (conn.succeeded()) {
				SQLConnection open = conn.result();
				open.queryWithParams(properties.getProperty(QUERY_EXPERIENCIA_LABORAL), new JsonArray().add(numDeclaracion),
						resultado -> {
							open.close();
							if (resultado.succeeded()) {
								future.handle(resultado.map(rs ->{return rs.getRows();}));
							} else {
								future.handle(Future.failedFuture(resultado.cause().getMessage()));
                                                                logger.severe(String.format(ERRROR_PARAM, "Error al obtener la experiencia laboral: ",resultado.cause().getMessage()));
							}
						});
			} else {
				future.handle(Future.failedFuture(conn.cause()));
                                logger.severe(String.format(ERRROR_PARAM, "Error conexion obtenerExperienciaLaboral: ", conn.cause().getMessage()));
			}
		});
		
		return future;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Future<List<JsonObject>> obtenerDatosParejas(int numDeclaracion) {
		Future<List<JsonObject>> future = Future.future();
		conexion.getConnection(conn -> {
			if (conn.succeeded()) {
				SQLConnection open = conn.result();
				open.queryWithParams(properties.getProperty(QUERY_DATOS_PAREJA), new JsonArray().add(numDeclaracion),
						resultado -> {
							open.close();
							if (resultado.succeeded()) {
								future.handle(resultado.map(rs ->{return rs.getRows();}));
							} else {
								future.handle(Future.failedFuture(resultado.cause().getMessage()));
                                                                logger.severe(String.format(ERRROR_PARAM, "Error al obtener datos parejas: ",resultado.cause().getMessage()));
							}
						});
			} else {
				future.handle(Future.failedFuture(conn.cause()));
                                logger.severe(String.format(ERRROR_PARAM, "Error conexion obtenerDatosParejas: ",conn.cause().getMessage()));
			}
		});
		
		return future;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Future<List<JsonObject>> obtenerDependientesEconomicos(int numDeclaracion) {
		Future<List<JsonObject>> future = Future.future();
		conexion.getConnection(conn -> {
			if (conn.succeeded()) {
				SQLConnection open = conn.result();
				open.queryWithParams(properties.getProperty(QUERY_DEPENDIENTES_ECONOMICOS), new JsonArray().add(numDeclaracion),
						resultado -> {
							open.close();
							if (resultado.succeeded()) {
								future.handle(resultado.map(rs ->{return rs.getRows();}));
							} else {
								future.handle(Future.failedFuture(resultado.cause().getMessage()));
                                                                logger.severe(String.format(ERRROR_PARAM, "Error al obtener dependientes economicos: ",resultado.cause().getMessage()));
							}
						});
			} else {
				future.handle(Future.failedFuture(conn.cause()));
                                logger.severe(String.format(ERRROR_PARAM, "Error conexion obtenerDependientesEconomicos: ",conn.cause().getMessage()));
			}
		});
		
		return future;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Future<List<JsonObject>> obtenerInversionesCuentasValores(int numDeclaracion) {
		Future<List<JsonObject>> future = Future.future();
		conexion.getConnection(conn -> {
			if (conn.succeeded()) {
				SQLConnection open = conn.result();
				open.queryWithParams(properties.getProperty(QUERY_INVERSIONES), new JsonArray().add(numDeclaracion),
						resultado -> {
							open.close();
							if (resultado.succeeded()) {
								future.handle(resultado.map(rs ->{return rs.getRows();}));
							} else {
								future.handle(Future.failedFuture(resultado.cause().getMessage()));
                                                                logger.severe(String.format(ERRROR_PARAM, "Error al obtener inversiones cuentas valores: ",resultado.cause().getMessage()));
							}
						});
			} else {
				future.handle(Future.failedFuture(conn.cause()));
                                logger.severe(String.format(ERRROR_PARAM, "Error conexion obtenerInversionesCuentasValores: ", conn.cause().getMessage()));
			}
		});
		
		return future;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Future<List<JsonObject>> obtenerAdeudosPasivos(int numDeclaracion) {
		Future<List<JsonObject>> future = Future.future();
		conexion.getConnection(conn -> {
			if (conn.succeeded()) {
				SQLConnection open = conn.result();
				open.queryWithParams(properties.getProperty(QUERY_ADEUDOS), new JsonArray().add(numDeclaracion),
						resultado -> {
							open.close();
							if (resultado.succeeded()) {
								future.handle(resultado.map(rs ->{return rs.getRows();}));
							} else {
								future.handle(Future.failedFuture(resultado.cause().getMessage()));
                                                                logger.severe(String.format(ERRROR_PARAM, "Error al obtener adeudos/pasivos: ",resultado.cause().getMessage()));
							}
						});
			} else {
				future.handle(Future.failedFuture(conn.cause()));
                                logger.severe(String.format(ERRROR_PARAM, "Error conexion obtenerAdeudosPasivos: ",conn.cause().getMessage()));
			}
		});
		
		return future;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Future<List<JsonObject>> obtenerBienesMuebles(int numDeclaracion) {
		Future<List<JsonObject>> future = Future.future();
		conexion.getConnection(conn -> {
			if (conn.succeeded()) {
				SQLConnection open = conn.result();
				open.queryWithParams(properties.getProperty(QUERY_MUEBLES), new JsonArray().add(numDeclaracion),
						resultado -> {
							open.close();
							if (resultado.succeeded()) {
								future.handle(resultado.map(rs ->{return rs.getRows();}));
							} else {
								future.handle(Future.failedFuture(resultado.cause().getMessage()));
                                                                logger.severe(String.format(ERRROR_PARAM, "Error al obtener bienes muebles: ", resultado.cause().getMessage()));
							}
						});
			} else {
				future.handle(Future.failedFuture(conn.cause()));
                                logger.severe(String.format(ERRROR_PARAM, "Error conexion obtenerBienesMuebles: ", conn.cause().getMessage()));
			}
		});
		
		return future;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Future<List<JsonObject>> obtenerBienesInmubeles(int numDeclaracion) {
		Future<List<JsonObject>> future = Future.future();
		conexion.getConnection(conn -> {
			if (conn.succeeded()) {
				SQLConnection open = conn.result();
				open.queryWithParams(properties.getProperty(QUERY_INMUEBLES), new JsonArray().add(numDeclaracion),
						resultado -> {
							open.close();
							if (resultado.succeeded()) {
								future.handle(resultado.map(rs ->{return rs.getRows();}));
							} else {
								future.handle(Future.failedFuture(resultado.cause().getMessage()));
                                                                logger.severe(String.format(ERRROR_PARAM, "Error al obtener bienes inmuebles: ", resultado.cause().getMessage()));
							}
						});
			} else {
				future.handle(Future.failedFuture(conn.cause()));
                                logger.severe(String.format(ERRROR_PARAM, "Error conexion obtenerBienesInmubeles: ", conn.cause().getMessage()));
			}
		});
		
		return future;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Future<List<JsonObject>> obtenerVehiculos(int numDeclaracion) {
		Future<List<JsonObject>> future = Future.future();
		conexion.getConnection(conn -> {
			if (conn.succeeded()) {
				SQLConnection open = conn.result();
				open.queryWithParams(properties.getProperty(QUERY_VEHICULOS), new JsonArray().add(numDeclaracion),
						resultado -> {
							open.close();
							if (resultado.succeeded()) {
								future.handle(resultado.map(rs ->{return rs.getRows();}));
							} else {
								future.handle(Future.failedFuture(resultado.cause().getMessage()));
                                                                logger.severe(String.format(ERRROR_PARAM, "Error al obtener vehiculos: ", resultado.cause().getMessage()));
							}
						});
			} else {
				future.handle(Future.failedFuture(conn.cause()));
                                logger.severe(String.format(ERRROR_PARAM, "Error conexion obtenerVehiculos: ", conn.cause().getMessage()));
			}
		});
		
		return future;
	}
}