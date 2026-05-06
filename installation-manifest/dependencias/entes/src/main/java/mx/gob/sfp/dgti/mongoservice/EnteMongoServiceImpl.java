/**
 * @(#)EnteMongoServiceImpl.java 20/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.mongoservice;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.UpdateOptions;
import io.vertx.serviceproxy.ServiceException;
import mx.gob.sfp.dgti.constantes.EstatusControl;
import mx.gob.sfp.dgti.constantes.Respuestas;
import mx.gob.sfp.dgti.constantes.Situacion;
import mx.gob.sfp.dgti.utils.EnteUtils;
import mx.gob.sfp.dgti.utils.LoggerUtils;
import org.apache.log4j.Logger;

import java.util.List;

/**
 *
 * Clase para la implementacion EnteMongoServiceImpl
 * 
 * @author Pvel Alexei Martnez Regalado aka programador02
 * @since 20/02/2019
 */
public class EnteMongoServiceImpl implements EnteMongoService {

	/**
	 * Logger
	 */
	final Logger logger = Logger.getLogger(EnteMongoService.class);

	/**
	 * Nombre de la coleccion
	 */
	private static String COLLECTION_ENTE = "entes";

	/**
	 * Cliente de MongoDB
	 */
	private MongoClient miMongo;

	/**
	 * Objeto con la configuracion
	 */
	private JsonObject config;
	
	/**
	 * Objeto con funciones de ayuda
	 */
	private EnteUtils enteUtils;

	/**
	 * Constructor
	 *
	 * @param vertx: el vertx
	 * @param config: configuracion para el mongo: puerto, database...
	 * 
	 * @author Pvel Alexei Martnez Regalado aka programador02
	 * @since 22/02/2019
	 */
	EnteMongoServiceImpl(Vertx vertx, JsonObject config) {

		logger.info("EnteMongoService - EnteMongoServiceImpl()");

		if (!config.isEmpty() && config.getString("modo") != null) {

			config
				.put("db_name", System.getenv("MONGO_DB"))
				.put("connection_string", System.getenv("MONGO_CONNECTION_STRING"));

				if(System.getenv("TEST_MONGO_USERNAME") != null)
					config.put("username", System.getenv("TEST_MONGO_USERNAME"));

				if(System.getenv("TEST_MONGO_PASSWORD") != null)
					config.put("password", System.getenv("TEST_MONGO_PASSWORD"));

		} else {
			config
				.put("db_name", System.getenv("MONGO_DB"))
				.put("connection_string", System.getenv("MONGO_CONNECTION_STRING"));

				if(System.getenv("MONGO_USERNAME") != null)
					config.put("username", System.getenv("MONGO_USERNAME"));
					  
				if(System.getenv("MONGO_PASSWORD") != null)
					config.put("password", System.getenv("MONGO_PASSWORD"));

		}
		
		if(config.getString("modo") != null) COLLECTION_ENTE = "entes-pruebas";
		config.remove("modo");
		miMongo = MongoClient.createShared(vertx, config);
		
		enteUtils = new EnteUtils();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public EnteMongoService agregarEnteNuevo(JsonObject ente, JsonObject condicionesLlave,
			Handler<AsyncResult<String>> resultHandler) {
		logger.info("\\nEnteMongoService - agregarEnteNuevo()");

		try {

			miMongo.find(COLLECTION_ENTE, condicionesLlave, busq -> {
				if (busq.succeeded()) {
					if(busq.result().isEmpty()) {
						// No se encontro ente con la misma llave, insertar el nuevo ente

						enteUtils.agregarDatosDeControl(ente);

						miMongo.insert(COLLECTION_ENTE, ente, inse -> {
							if (inse.succeeded()) {
								if (inse.result() != null) {
									JsonObject condiciones = new JsonObject().put("_id", inse.result());
									JsonObject actualizable = new JsonObject().put("$set",
											new JsonObject().put("idEnteOrigen", inse.result()));
									// Actualizar el id_padre para que sea el mismo _id
									miMongo.updateCollection(COLLECTION_ENTE, condiciones, actualizable, actu -> {
										if (actu.succeeded()) {
											resultHandler.handle(Future.succeededFuture(inse.result()));
										} else {
											resultHandler.handle(
													ServiceException.fail(Respuestas.ERROR_BASE.getId(), inse.cause().getMessage()));
										}
									});
								} else {
									resultHandler.handle(
											ServiceException.fail(Respuestas.ERROR_BASE.getId(), inse.cause().getMessage()));
								}
							} else {
								resultHandler.handle(
										ServiceException.fail(Respuestas.ERROR.getId(), "No se pudo insertar el ente"));
							}
						});
					} else {
						//Ya existe un ente con la llave
						resultHandler.handle(
								ServiceException.fail(Respuestas.ENTE_EXISTENTE.getId(), "El ente ya existe"));
					}
				} else {
					resultHandler.handle(
							ServiceException.fail(Respuestas.ERROR_BASE.getId(), busq.cause().getMessage()));
				}
			});

		} catch (Exception e) {
			LoggerUtils.logError(e);
			resultHandler.handle(
					ServiceException.fail(Respuestas.ERROR.getId(), e.getCause().getMessage()));
		}
		return this;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public EnteMongoService actualizarInfoEnte(JsonObject ente, Handler<AsyncResult<Void>> resultHandler) {
		logger.info("\\nEnteMongoService - actualizarInfoEnte()");
		
		try {
			JsonObject condiciones = new JsonObject().put("_id", ente.getString("_id"));
			enteUtils.agregarDatosDeControl(ente);

			JsonObject actualizable = new JsonObject().put("$set", new JsonObject()
					.put("enteDesc", ente.getString("enteDesc"))
					.put("nombreCorto", ente.getString("nombreCorto"))
					.put("identificadorINAI", ente.getString("identificadorINAI"))
					.put("datosDeControl.fechaUltimaActualiza", new JsonObject()
							.put("$date", ente.getJsonObject("datosDeControl")
									.getJsonObject("fechaUltimaActualiza").getString("$date")))
					);

			miMongo.updateCollection(COLLECTION_ENTE, condiciones, actualizable, act -> {
				if(act.succeeded()) {
					resultHandler.handle(Future.succeededFuture());
				} else {
					resultHandler.handle(ServiceException.fail(Respuestas.ERROR_BASE.getId(), act.cause().getMessage()));
				}
			});
		} catch (Exception e) {
			resultHandler.handle(
					ServiceException.fail(Respuestas.ERROR.getId(), e.getCause().getMessage()));
			LoggerUtils.logError(e);
		}
		return this;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public EnteMongoService actualizarEnte(JsonObject ente, Handler<AsyncResult<String>> resultHandler) {
		logger.info("\\nEnteMongoService - actualizarEnte()");
		
		try {
			String idEnteOrigen = ente.getString("idEnteOrigen");

			if (idEnteOrigen != null) {

				JsonObject condiciones = new JsonObject().put("idEnteOrigen",idEnteOrigen);
				JsonObject datosControl = ente.getJsonObject("datosDeControl");

				if(datosControl.getString("situacion") == null ||
						datosControl.getString("situacion").equals(Situacion.NORMAL.getId())) {
					condiciones.put("$or", new JsonArray()
							.add(new JsonObject().put("datosDeControl.situacion", Situacion.NORMAL.getId()))
							.add(new JsonObject().putNull("datosDeControl.situacion"))
							.add(new JsonObject().put("datosDeControl.situacion", new JsonObject().put("$exists",false))));
				} else {
					condiciones.put("datosDeControl.situacion", datosControl.getString("situacion"));
				}

				JsonObject actualizable = new JsonObject();
				enteUtils.agregarDatosBaja(actualizable);

				UpdateOptions opciones = new UpdateOptions();
				opciones.setMulti(true);

				miMongo.updateCollectionWithOptions(COLLECTION_ENTE, condiciones, actualizable, opciones, act -> {
					if (act.succeeded()) {
						ente.remove("_id");
						enteUtils.agregarDatosDeControl(ente);
						miMongo.insert(COLLECTION_ENTE, ente, ins -> {
							if (ins.succeeded()) {
								if (ins.result() != null) {
									resultHandler.handle(Future.succeededFuture(ins.result()));
								} else {
									resultHandler.handle(ServiceException.fail(Respuestas.ERROR.getId(), "No se pudo insertar el ente"));
								}
							} else {
								resultHandler.handle(ServiceException.fail(Respuestas.ERROR_BASE.getId(), ins.cause().getMessage()));
							}
						});
					} else {
						resultHandler.handle(ServiceException.fail(Respuestas.ERROR_BASE.getId(), act.cause().getMessage()));
					}
				});


			} else {
				resultHandler.handle(ServiceException.fail(Respuestas.ERROR.getId(), "No se encontraron entes con el id"));
			}
		} catch (Exception e) {
			resultHandler.handle(
					ServiceException.fail(Respuestas.ERROR.getId(), e.getCause().getMessage()));
			LoggerUtils.logError(e);
		}
		return this;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public EnteMongoService consultarEntes(JsonObject condiciones, 
			Handler<AsyncResult<List<JsonObject>>> resultHandler) {
		logger.info("\\nEnteMongoService - consultarEntes()");

		try {
			FindOptions opciones = new FindOptions();
			condiciones.put("datosDeControl.activo", EstatusControl.ACTIVO.getId());
			JsonObject sort = new JsonObject().put("enteDesc",1).put("datosDeControl.fechaUltimaActualiza",-1);

			opciones.setSort(sort);

			miMongo.findWithOptions(COLLECTION_ENTE, condiciones, opciones, busq -> {
				if(busq.succeeded())  {
					resultHandler.handle(Future.succeededFuture(busq.result()));
				}else {
					resultHandler.handle(ServiceException.fail(Respuestas.ERROR_BASE.getId(), busq.cause().getMessage()));
				}
			});
		} catch (Exception e) {
			LoggerUtils.logError(e);
			resultHandler.handle(
					ServiceException.fail(Respuestas.ERROR.getId(), e.getCause().getMessage()));
		}
		return this;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public EnteMongoService consultarHistorialEntePorEnte(String idEnteOrigen, 
			Handler<AsyncResult<List<JsonObject>>> resultHandler) {

		try {

			FindOptions opciones = new FindOptions();
			JsonObject condiciones = new JsonObject().put("idEnteOrigen", idEnteOrigen);

			JsonObject sort = new JsonObject().put("enteDesc", 1).put("datosDeControl.fechaUltimaActualiza", -1);
			opciones.setSort(sort);
			miMongo.findWithOptions(COLLECTION_ENTE, condiciones, opciones, busq -> {
				if (busq.succeeded()) {
					resultHandler.handle(Future.succeededFuture(busq.result()));
					logger.info(busq.result());
				} else {
					resultHandler.handle(ServiceException.fail(Respuestas.ERROR_BASE.getId(), busq.cause().getMessage()));
				}
			});

		} catch (Exception e) {
			resultHandler.handle(
					ServiceException.fail(Respuestas.ERROR.getId(), e.getCause().getMessage()));
			LoggerUtils.logError(e);
		}
		return this;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public EnteMongoService consultarEntePorId(String idEnte, Handler<AsyncResult<JsonObject>> resultHandler) {

		try {

			miMongo.findOne(COLLECTION_ENTE, new JsonObject().put("_id", idEnte), null, busq -> {
				if (busq.succeeded()) {
						resultHandler.handle(Future.succeededFuture(busq.result()));
				} else {
					resultHandler.handle(ServiceException.fail(Respuestas.ERROR_BASE.getId(), busq.cause().getMessage()));
				}
			});

		} catch (Exception e) {
			resultHandler.handle(
					ServiceException.fail(Respuestas.ERROR.getId(), e.getCause().getMessage()));
			LoggerUtils.logError(e);
		}
		return this;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public EnteMongoService desactivarEntePorId(boolean desactivarHistorico, String id, Handler<AsyncResult<Void>> resultHandler) {
		logger.info("\\nEnteMongoService - desactivarEntePorId()");

		try {
			JsonObject condiciones;

			if(desactivarHistorico) {
				condiciones = new JsonObject().put("idEnteOrigen", id);
			} else {
				condiciones = new JsonObject().put("_id", id);
			}

			JsonObject actualizable = new JsonObject();
			enteUtils.agregarDatosBaja(actualizable);

			UpdateOptions opciones = new UpdateOptions();
			opciones.setMulti(true);

			miMongo.updateCollectionWithOptions(COLLECTION_ENTE, condiciones, actualizable, opciones, act -> {
				if (act.succeeded()) {
					resultHandler.handle(Future.succeededFuture());
				} else {
					resultHandler.handle(ServiceException.fail(Respuestas.ERROR_BASE.getId(), act.cause().getMessage()));
				}
			});
		} catch (Exception e) {
			resultHandler.handle(
					ServiceException.fail(Respuestas.ERROR.getId(), e.getCause().getMessage()));
			LoggerUtils.logError(e);
		}
		return this;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public EnteMongoService definirSituacionEnte(String idEnte, Situacion situacion, Handler<AsyncResult<Void>> resultHandler) {
		logger.info("\\nEnteMongoService - definirSituacion()");

		try {
			JsonObject condiciones = new JsonObject().put("_id", idEnte);

			JsonObject actualizable = new JsonObject();
			enteUtils.agregarDatosSituacion(actualizable, situacion);

			UpdateOptions opciones = new UpdateOptions();
			opciones.setMulti(false);

			miMongo.updateCollectionWithOptions(COLLECTION_ENTE, condiciones, actualizable, opciones, act -> {
				if (act.succeeded()) {
					resultHandler.handle(Future.succeededFuture());
				} else {
					resultHandler.handle(ServiceException.fail(Respuestas.ERROR_BASE.getId(), act.cause().getMessage()));
				}
			});

		} catch (Exception e) {
			resultHandler.handle(
					ServiceException.fail(Respuestas.ERROR.getId(), e.getCause().getMessage()));
			LoggerUtils.logError(e);
		}
		return this;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public void close() {
		logger.info("-------------CLOSE ENTEMONGOSERVICE");
		miMongo.close();
	}

}
