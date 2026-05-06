/**
 * @(#)CatalogoDAOImpl.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.decnet.dao;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import mx.gob.sfp.dgti.decnet.cache.CacheService;
import mx.gob.sfp.dgti.decnet.dto.CatalogoDTO;
import static mx.gob.sfp.dgti.decnet.util.Constantes.CAT_DNET_DATABASE_HOST;
import static mx.gob.sfp.dgti.decnet.util.Constantes.CAT_DNET_DATABASE_NAME;
import static mx.gob.sfp.dgti.decnet.util.Constantes.CAT_DNET_DATABASE_PORT;
import static mx.gob.sfp.dgti.decnet.util.Constantes.CAT_DNET_DATABASE_PSW;
import static mx.gob.sfp.dgti.decnet.util.Constantes.CAT_DNET_DATABASE_USERNAME;
import static mx.gob.sfp.dgti.decnet.util.Constantes.CAT_DNET_TABLE;
import static mx.gob.sfp.dgti.decnet.util.Constantes.CONFIG_DATABASE;
import static mx.gob.sfp.dgti.decnet.util.Constantes.CONFIG_HOST;
import static mx.gob.sfp.dgti.decnet.util.Constantes.CONFIG_PORT;
import static mx.gob.sfp.dgti.decnet.util.Constantes.CONFIG_PSW;
import static mx.gob.sfp.dgti.decnet.util.Constantes.CONFIG_USERNAME;
import static mx.gob.sfp.dgti.decnet.util.Constantes.ESTADO_ACTIVO;
import static mx.gob.sfp.dgti.decnet.util.Constantes.PROPERTIES;
import mx.gob.sfp.dgti.verticle.Server;

/**
 * Implementacion de DatabaseService
 * 
 * @author Miriam Sanchez Sanchez programador07
 * @since 30/05/2019
 */
public class CatalogoDAOImpl implements CatalogoDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogoDAOImpl.class);
        private static final String CATALOGO_POLL_NAME = "CatalogoPool";
        private static final String INITIAL_POOL_SIZE = "initial_pool_size";
        private static final String MIN_POOL_SIZE = "min_pool_size";
        private static final String MAX_POOL_SIZE = "max_pool_size";
	private static final String TABLA = System.getenv(CAT_DNET_TABLE) != null ? System.getenv(CAT_DNET_TABLE)
			: "cat_ambito_sector";


	/**
	 * Valor del host de la base del catalogo tomado desde variable de entorno o desde archivo .properties.
	 */
	private static final String CAT_DNET_HOST =
			System.getenv(CAT_DNET_DATABASE_HOST) != null ?
					System.getenv(CAT_DNET_DATABASE_HOST) : obtenerPropiedad("host");

	/**
	 * Valor del puerto de la base del catalogo tomado desde variable de entorno o desde archivo .properties.
	 */
	private static final Integer CAT_DNET_PORT =
			System.getenv(CAT_DNET_DATABASE_PORT) != null ?
					Integer.parseInt(System.getenv(CAT_DNET_DATABASE_PORT)) : Integer.parseInt(obtenerPropiedad("port"));

	/**
	 * Valor del nombre de la base del catalogo tomado desde variable de entorno o desde archivo .properties.*
	 */
	private static final String CAT_DNET_DATABASE =
			System.getenv(CAT_DNET_DATABASE_NAME) != null ?
					System.getenv(CAT_DNET_DATABASE_NAME) : obtenerPropiedad("database");

	/**
	 * Valor del username de la base del catalogo tomado desde variable de entorno o desde archivo .properties.
	 */
	private static final String CAT_DNET_USERNAME =
			System.getenv(CAT_DNET_DATABASE_USERNAME) != null ?
					System.getenv(CAT_DNET_DATABASE_USERNAME) : obtenerPropiedad("username");

	/**
	 * Valor del password de la base del catalogo tomado desde variable de entorno o desde archivo .properties.
	 */
	private static final String CAT_DNET_PWD =
			System.getenv(CAT_DNET_DATABASE_PSW) != null ?
					System.getenv(CAT_DNET_DATABASE_PSW) : obtenerPropiedad("password");

	private SQLClient postgreSQLt;

	/**
	 * Servicio utilizado para obtener datos del cache
	 */
	CacheService cache;

	/**
	 * Constructor
	 */
	public CatalogoDAOImpl() {

	}

	/**
	 * Constructor
	 *
	 * @param vertx
	 */
	public CatalogoDAOImpl(Vertx vertx) {

		postgreSQLt = PostgreSQLClient.createShared(vertx, getConfig(), CATALOGO_POLL_NAME);

		cache  = CacheService.create(vertx);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Future<List<CatalogoDTO>> obtenerCatalogoActivos() {
		LOGGER.info("=== obtenerCatalogoActivos()");
		Future<List<CatalogoDTO>> future = Future.future();

		cache.obtenerCatalogoActivos(c -> {
			if(c.succeeded() && c.result() != null) {

				future.handle(Future.succeededFuture(c.result()));
			} else {
				postgreSQLt.queryWithParams(obtenerSqlCatalogo(true, false, false),
						new JsonArray(), resultado -> {
					postgreSQLt.close();
					if (resultado.succeeded()) {

						List<CatalogoDTO> catalogoBase =
								resultado.result().getRows().stream().map(CatalogoDTO::new).collect(Collectors.toList());

						cache.actualizarCatalogoActivos(catalogoBase, act -> {

							if(act.succeeded()) {
								LOGGER.info("=== Cache actualizado");
								future.handle(Future.succeededFuture(catalogoBase));
							} else {
							    future.fail(act.cause());
                            }
						});

					} else {
						LOGGER.error("fail " + resultado.cause());
						future.fail(resultado.cause());
					}
				});
			}
		});

		return future;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Future<List<CatalogoDTO>> obtenerCatalogo() {
		LOGGER.info("=== obtenerCatalogo()");
		Future<List<CatalogoDTO>> future = Future.future();

			postgreSQLt.queryWithParams(obtenerSqlCatalogo(false, false, false), new JsonArray(),
					resultado -> {
				postgreSQLt.close();
				if (resultado.succeeded()) {

					List<CatalogoDTO> catalogoBase =
							resultado.result().getRows().stream().map(CatalogoDTO::new).collect(Collectors.toList());

					future.handle(Future.succeededFuture(catalogoBase));

				} else {
					LOGGER.error("fail " + resultado.cause());
					future.fail(resultado.cause());
				}
			});

		return future;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Future<List<CatalogoDTO>> obtenerCatalogoActivosFk(int fk) {
		LOGGER.info("=== obtenerCatalogoActivosFk() fk: " + fk);
		Future<List<CatalogoDTO>> future = Future.future();

		cache.obtenerCatalogoActivosFk(fk, c -> {
			if(c.succeeded() && c.result() != null) {
				LOGGER.info("=== Valor obtenido de cache");
				future.handle(Future.succeededFuture(c.result()));
			} else {
				postgreSQLt.queryWithParams(obtenerSqlCatalogo(
						false, false, true), new JsonArray().add(fk),
						resultado -> {
							postgreSQLt.close();
							if (resultado.succeeded()) {

								List<CatalogoDTO> catalogoBase =
										resultado.result().getRows().stream().map(CatalogoDTO::new).collect(Collectors.toList());

								cache.actualizarCatalogoActivosFk(fk, catalogoBase, act -> {
									if (act.succeeded()) {
										LOGGER.info("=== Cache actualizado para fk: " + fk);
										future.handle(Future.succeededFuture(catalogoBase));
									} else {
										future.fail(act.cause());
									}
								});

							} else {
								LOGGER.error("fail " + resultado.cause());
								future.fail(resultado.cause());
							}
						});
			}

		});
		return future;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Future<CatalogoDTO> obtenerCatalogoPorId(int id) {
		LOGGER.info("=== obtenerCatalogoPorId()");
		Future<CatalogoDTO> future = Future.future();

		cache.obtenerPorId(Integer.valueOf(id), c -> {
			if (c.succeeded() && c.result() != null) {
				future.handle(Future.succeededFuture(c.result()));
			} else {
				postgreSQLt.queryWithParams(obtenerSqlCatalogo(false, true, false), new JsonArray().add(id), resultado -> {
					postgreSQLt.close();
					if (resultado.succeeded()) {
						List<JsonObject> objects = resultado.result().getRows();
						if (objects.isEmpty()) {
							future.handle(resultado.map(rs -> new CatalogoDTO()));
						} else {

							CatalogoDTO elemento = new CatalogoDTO(resultado.result().getRows().get(0));
							cache.actualizarPorId(Integer.valueOf(id), elemento, act -> {
								if (act.succeeded()) {
									LOGGER.info("=== Cache actualizado por id");
									future.handle(resultado.map(rs -> elemento));
								} else {
									future.fail(act.cause());
								}
							});

						}
					} else {
						LOGGER.error("fail " + resultado.cause());
						future.fail(resultado.cause());
					}
				});
			}
		});

		return future;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Future<CatalogoDTO> obtenerCatalogoPorIdFk(int id, int fk) {
		LOGGER.info("=== obtenerCatalogoPorIdPk() -- id: " + id + " fk: " + fk);
		Future<CatalogoDTO> future = Future.future();

			postgreSQLt.queryWithParams(obtenerSqlCatalogo(false, true, true), new JsonArray()
					.add(id).add(fk), resultado -> {
				postgreSQLt.close();
				if (resultado.succeeded()) {
					List<JsonObject> objects = resultado.result().getRows();
					if (objects.isEmpty()) {
						future.handle(resultado.map(rs -> new CatalogoDTO()));
					} else {

						CatalogoDTO elemento = new CatalogoDTO(resultado.result().getRows().get(0));
						future.handle(resultado.map(rs -> elemento));

					}
				} else {
					LOGGER.error("fail " + resultado.cause());
					future.fail(resultado.cause());
				}
			});


		return future;
	}

	/**
	 * Obtener el query para consulta de catálogo
	 *
	 * @param activo
	 * @param porId
	 * @return sql
	 */
	private String obtenerSqlCatalogo(boolean activo, boolean porId, boolean porFk) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" id, valor, valor_uno as \"valorUno\", activo, fecha_registro as \"fechaRegistro\", fk ");
		//sql.delete(sql.length() - 2, sql.length());
		sql.append(" FROM ").append(TABLA);

		if (activo || porId || porFk) {
			sql.append(" WHERE ");

			List<String> condiciones = new ArrayList<>();

			if (activo) {
				condiciones.add(" activo = " + ESTADO_ACTIVO);
			}
			if (porId) {
				condiciones.add(" id = ? ");
			}
			if (porFk) {
				condiciones.add(" fk = ? ");
			}
			sql.append(String.join(" and ", condiciones));
		}
		sql.append(" order by orden ");
		LOGGER.info("=== obtenerSqlCatalogo " + sql.toString());
		return sql.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Future<CatalogoDTO> agregarRegistro(CatalogoDTO catalogo) {
		LOGGER.info("=== agregarRegistro()");
		Future<CatalogoDTO> future = Future.future();
		StringBuilder sql = new StringBuilder();

		JsonArray valores = new JsonArray()
				.add(catalogo.getValor())
				.add(ESTADO_ACTIVO)
				.add(getFechaFormatoISO8601(new Date()));
		if((catalogo.getValorUno() == null)) {
			valores.addNull();
		} else {
			valores.add(catalogo.getValorUno());
		}
		if(catalogo.getFk() == null) {
			valores.addNull();
		} else {
			valores.add(catalogo.getFk());
		}

		sql.append("INSERT INTO ").append(TABLA).append(
				" (valor, activo, fecha_registro, valor_uno, fk) VALUES (?,?,?,?,?) RETURNING id, valor, valor_uno " +
						"as \"valorUno\", activo, fecha_registro as \"fechaRegistro\", fk");
		LOGGER.info(sql);
		LOGGER.info(catalogo);
		postgreSQLt.queryWithParams(sql.toString(),
				valores,
				resultado -> {
					postgreSQLt.close();
					if (resultado.failed()) {
						LOGGER.error("=== Fallo guardar registro");
						LOGGER.error(resultado.cause());
						future.fail(resultado.cause());
					} else {
						LOGGER.info("=== Se agregó exitosamente");
						ResultSet result = resultado.result();
						JsonObject  objeto= result.getRows().get(0);

						CatalogoDTO registroNuevo = new CatalogoDTO(result.getRows().get(0));
                        cache.restablecerCache(rs -> {
                           if(rs.succeeded()) {
							   LOGGER.info("=== Cache restablecido");
                               future.handle(resultado.map(res -> registroNuevo));
                           } else {
                               future.fail(rs.cause());
                           }
                        });

					}
				});
		return future;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Future<CatalogoDTO> actualizarRegistro(CatalogoDTO catalogo) {
		Future<CatalogoDTO> future = Future.future();
		LOGGER.info("=== actulizarRegistro()");
		postgreSQLt.queryWithParams(generarQueryActualizar(catalogo), generarJsonParams(catalogo), resultado -> {

			postgreSQLt.close();
			if (resultado.succeeded()) {
				ResultSet result = resultado.result();
				if (!result.getRows().isEmpty()) {
					CatalogoDTO registroNuevo = new CatalogoDTO(result.getRows().get(0));

					cache.restablecerCache(rs -> {
						LOGGER.info("=== Cache restablecido");
						if(rs.succeeded()) {
							future.handle(resultado.map(res -> registroNuevo));
						} else {
							future.fail(rs.cause());
						}

					});
				} else {
					LOGGER.error("=== No existe el registro a actualizar en la BD");
					future.handle(resultado.map(res -> new CatalogoDTO()));
				}

			} else {
				LOGGER.error("=== Fallo actualizarRegistro " + resultado.cause());
				future.fail(resultado.cause());
			}
		});
		return future;
	}

	/**
	 * Metodo para crear el query de actualizacion
	 *
	 * @param catalogo
	 * @return
	 */
	private String generarQueryActualizar(CatalogoDTO catalogo) {
		System.out.println(catalogo);
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ").append(TABLA).append(" SET ");

		List<String> campos = new ArrayList<>();

		if (catalogo.getActivo() != null) {
			campos.add(" activo = ? ");
		}
		if (catalogo.getValor() != null && !"".equals(catalogo.getValor())) {
			campos.add(" valor = ? ");
		}
		if (catalogo.getValorUno() != null && !"".equals(catalogo.getValorUno())) {
			campos.add(" valor_uno = ? ");
		}
/*		if (catalogo.getFk() != null && !"".equals(catalogo.getFk())) {
			campos.add(" fk = ? ");
		}*/
		sql.append(String.join(",", campos));

		List<String> condiciones = new ArrayList<>();
		condiciones.add(" WHERE id = ? ");

		if (catalogo.getFk() != null) {
			condiciones.add(" fk = ? ");
		}
		sql.append(String.join("and", condiciones));

		sql.append(" RETURNING id, valor, clave_pdn as \"clavePdn\", activo, fecha_registro as \"fechaRegistro\"");
		LOGGER.info("=== generarQueryActualizar " + sql.toString());
		return sql.toString();
	}

	/**
	 * Metodo para generar un json
	 *
	 * @param catalogo
	 * @return
	 */
	private JsonArray generarJsonParams(CatalogoDTO catalogo) {
		JsonArray json = new JsonArray();
		if (catalogo.getActivo() != null) {
			json.add(catalogo.getActivo());
		}
		if (catalogo.getValor() != null && !"".equals(catalogo.getValor())) {
			json.add(catalogo.getValor());
		}

		if (catalogo.getValorUno() != null && !"".equals(catalogo.getValorUno())) {
			json.add(catalogo.getValorUno());
		}

/*		if (catalogo.getFk() != null ) {
			json.add(catalogo.getFk());
		}*/
		json.add(catalogo.getId());

		if (catalogo.getFk() != null) {
			json.add(catalogo.getFk());
		}

		LOGGER.info("=== generarJsonParams " + json.toString());
		return json;
	}

	/**
	 * Metodo que realiza la conversion de fecha a String con formato "yyyy-MM-dd"
	 * 
	 * @param fecha Fecha que se pretende regresar en String.
	 * @return String Fecha con formato "yyyy-MM-dd"
	 * @author cgarias
	 * @since 01/06/2017
	 */
	public static String getFechaFormatoISO8601(Date fecha) {
		if (fecha != null) {
			return new SimpleDateFormat("yyyy-MM-dd").format(fecha);
		}
		return null;
	}

	/**
	 * Se obtiene la configuracion de la base de datos
	 *
	 * @return JsonObject
	 */
	private static JsonObject getConfig() {
		return new JsonObject()
				.put(CONFIG_HOST, CAT_DNET_HOST)
				.put(CONFIG_PORT, CAT_DNET_PORT)
				.put(CONFIG_DATABASE, CAT_DNET_DATABASE)
				.put(CONFIG_USERNAME, CAT_DNET_USERNAME)
				.put(CONFIG_PSW, CAT_DNET_PWD)
                                .put(INITIAL_POOL_SIZE, Integer.parseInt(System.getenv(INITIAL_POOL_SIZE)))
                                .put(MIN_POOL_SIZE, Integer.parseInt(System.getenv(MIN_POOL_SIZE)))
                                .put(MAX_POOL_SIZE, Integer.parseInt(System.getenv(MAX_POOL_SIZE)))
                        ;
	}

	/**
	 * Obtener configuración en el properties para ambiente
	 *
	 * @param nombre
	 * @return
	 */
	private static String obtenerPropiedad(String nombre) {
		InputStream input = null;
		Properties prop = new Properties();
		try {
			input = Server.class.getClassLoader().getResourceAsStream(PROPERTIES);
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

}
