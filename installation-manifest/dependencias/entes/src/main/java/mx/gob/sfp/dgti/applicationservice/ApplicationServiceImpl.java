/**
 * @(#)EnteMongoServiceImpl.java 20/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.applicationservice;

import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;
import mx.gob.sfp.dgti.constantes.Respuestas;
import mx.gob.sfp.dgti.constantes.Situacion;
import mx.gob.sfp.dgti.dto.EnteDTO;
import mx.gob.sfp.dgti.dto.MigracionDTO;
import mx.gob.sfp.dgti.dto.ParamConsultaEnteDTO;
import mx.gob.sfp.dgti.mongoservice.EnteMongoService;
import mx.gob.sfp.dgti.utils.EnteUtils;
import mx.gob.sfp.dgti.utils.LoggerUtils;
import mx.gob.sfp.dgti.utils.ValidacionUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Clase para la implementacion EnteMongoServiceImpl
 * 
 * @author Pvel Alexei Martnez Regalado aka programador02
 * @since 20/02/2019
 */
public class ApplicationServiceImpl implements ApplicationService {

	/**
	 * Logger
	 */
	final Logger logger = Logger.getLogger(ApplicationServiceImpl.class);

	/**
	 * Servicio para las conexiones
	 */
	EnteMongoService enteMongoService;

	/**
	 * Utilidades relacionadas con Entes
	 */
	EnteUtils enteUtils;

	/**
	 * Utilidades relacionadas con validaciones
	 */
	ValidacionUtils validacionUtils;

	/**
	 * Constructor
	 *
	 * @param vertx: el vertx
	 *
	 * @author Pvel Alexei Martnez Regalado aka programador02
	 * @since 22/02/2019
	 */
	ApplicationServiceImpl(Vertx vertx) {

		logger.info("ApplicationServiceImpl()");

		enteMongoService = EnteMongoService
				.createProxy(vertx, EnteMongoService.SERVICE_ADDRESS);

		enteUtils = new EnteUtils();

		validacionUtils = new ValidacionUtils();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public ApplicationService agregarEnteNuevo(EnteDTO ente,
											   Handler<AsyncResult<EnteDTO>> resultHandler) {
		logger.info("-- agregarEnteNuevo - AS");
		try {

			JsonObject condiciones = enteUtils.obtenerCondicionesLlave(ente);

			enteMongoService.agregarEnteNuevo(ente.toJsonMongo(), condiciones, ar -> {
				if (ar.succeeded()) {
					if (ar.result() == null) {
						resultHandler.handle(
								ServiceException.fail(Respuestas.ERROR.getId(), ar.cause().getMessage()));
					} else {

						enteMongoService.consultarEntePorId(ar.result(), busq -> {

							if (busq.succeeded() && busq.result() != null) {

								EnteDTO enteResp = EnteDTO.toObject(busq.result());

								resultHandler.handle(Future.succeededFuture(enteResp));

							} else {
								resultHandler.handle(
										ServiceException.fail(Respuestas.ERROR_BASE.getId(), busq.cause().getMessage()));
							}
						});
					}
				} else {
					if (ar.cause() instanceof ServiceException) {
						ServiceException exc = (ServiceException) ar.cause();
						resultHandler.handle(ServiceException.fail(exc.failureCode(), exc.getMessage()));
					}
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
	public ApplicationService actualizarEnte(EnteDTO ente, Handler<AsyncResult<EnteDTO>> resultHandler) {
		logger.info("-- actualizarEnte - AS");

		try {
			//Buscar
			enteMongoService.consultarEntePorId(ente.getId(), busq -> {
			if (busq.succeeded()) {

				if (busq.result() == null || busq.result().size() == 0) {

					resultHandler.handle(
							ServiceException.fail(Respuestas.ENTE_NO_ENCONTRADO.getId(), Respuestas.ENTE_NO_ENCONTRADO.getDescripcion()));
				} else {

					logger.info(busq.result());
					//Se obtiene el ente que esta en base
					EnteDTO enteBase = EnteDTO.toObject(busq.result());


					if(ente.getIdEnteOrigen().equals(enteBase.getIdEnteOrigen())) {

						int accionRealizar = enteUtils.evaluarEntes(ente, enteBase);

						if (accionRealizar == 0) { //Actualizar solo info
							enteMongoService.actualizarInfoEnte(ente.toJsonMongo(), actu -> {
								if (actu.succeeded()) {
									enteMongoService.consultarEntePorId(ente.getId(), busq2 -> {
										if (busq2.succeeded()) {

											EnteDTO enteResp = EnteDTO.toObject(busq2.result());
											resultHandler.handle(Future.succeededFuture(enteResp));

										} else {
											resultHandler.handle(
													ServiceException.fail(Respuestas.ERROR_BASE.getId(), busq.cause().getMessage()));
										}
									});

								} else {
									if (actu.cause() instanceof ServiceException) {

										ServiceException exc = (ServiceException) actu.cause();
										resultHandler.handle(
												ServiceException.fail(exc.failureCode(), actu.cause().getMessage()));
									}
								}
							});
						} else {//Agregar documento directamente

							ente.getDatosDeControl().setSituacion(enteBase.getDatosDeControl().getSituacion());

							enteMongoService.actualizarEnte(ente.toJsonMongo(), ar -> {
								if (ar.succeeded()) {
									if (ar.result() == null) {

										resultHandler.handle(
												ServiceException.fail(Respuestas.ERROR.getId(), ar.cause().getMessage()));

									} else {

										enteMongoService.consultarEntePorId(ar.result(), busq2 -> {
											if (busq2.succeeded()) {
												EnteDTO enteResp = EnteDTO.toObject(busq2.result());
												resultHandler.handle(Future.succeededFuture(enteResp));

											} else {
												resultHandler.handle(
														ServiceException.fail(Respuestas.ERROR_BASE.getId(),
																busq.cause().getMessage()));
											}
										});
									}
								} else {
									if (ar.cause() instanceof ServiceException) {

										ServiceException exc = (ServiceException) ar.cause();
										resultHandler.handle(
												ServiceException.fail(exc.failureCode(), ar.cause().getMessage()));
									}
								}
							});
						}
					} else {
						resultHandler.handle(
								ServiceException.fail(Respuestas.ENTE_NO_ENCONTRADO.getId(), Respuestas.ENTE_NO_ENCONTRADO.getDescripcion()));
					}
				}
			} else {
				if (busq.cause() instanceof ServiceException) {
					ServiceException exc = (ServiceException) busq.cause();
					resultHandler.handle(
							ServiceException.fail(exc.failureCode(), busq.cause().getMessage()));

				}
			}});
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
	public ApplicationService consultarEntes(ParamConsultaEnteDTO parametros,
											 Handler<AsyncResult<List<EnteDTO>>> resultHandler) {
		try {
			JsonObject condiciones = enteUtils.obtenerCondicionesParam(parametros);

			enteMongoService.consultarEntes(condiciones, busq -> {

				if(busq.succeeded()) {

					List<JsonObject> objects = busq.result();
					List<EnteDTO> entes = objects.stream()
							.map(x -> EnteDTO.toObject(x)).collect(Collectors.toList());

					resultHandler.handle(Future.succeededFuture(entes));

				} else {
					if (busq.cause() instanceof ServiceException) {
						ServiceException exc = (ServiceException) busq.cause();
						resultHandler.handle(
								ServiceException.fail(exc.failureCode(), busq.cause().getMessage()));

					}
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
	public ApplicationService consultarHistorialEntePorEnte(String idEnteOrigen,
															Handler<AsyncResult<List<EnteDTO>>> resultHandler) {

		enteMongoService.consultarHistorialEntePorEnte(idEnteOrigen, busq -> {
			if(busq.succeeded()) {

				List<JsonObject> objects = busq.result();
				List<EnteDTO> entes = objects.stream()
						.map(x -> EnteDTO.toObject(x)).collect(Collectors.toList());

				resultHandler.handle(Future.succeededFuture(entes));

			} else {
				if (busq.cause() instanceof ServiceException) {
					ServiceException exc = (ServiceException) busq.cause();
					resultHandler.handle(
							ServiceException.fail(exc.failureCode(), busq.cause().getMessage()));

				}
			}
		});
		return this;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public ApplicationService agregarEntesNuevos(MigracionDTO migracion,
												 Handler<AsyncResult<Void>> resultHandler) {

		List<Future> futuros = new ArrayList<>();

		try {

			for(EnteDTO ente : migracion.getListaEntes()) {
				Future<Void> futuro = Future.future();
				futuros.add(futuro);

				JsonObject condiciones = enteUtils.obtenerCondicionesLlave(ente);
				enteMongoService.agregarEnteNuevo(ente.toJsonMongo(), condiciones, ar -> {
					if (ar.succeeded()) {
						if (ar.result() == null) {
							logger.info("No insertado:" + ar.result());
						} else {
							logger.info("Insertado:" + ar.result());
						}
						futuro.complete();

					} else {
						if (ar.cause() instanceof ServiceException) {
							logger.info("NO INSERTADO :" + ar.result());

						}
						futuro.fail(ar.cause());
					}});
			}

			CompositeFuture.all(futuros)
				.setHandler(ar->{

					resultHandler.handle(Future.succeededFuture());


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
	public ApplicationService desactivarEnte(boolean desactivarHistorico, String id, Handler<AsyncResult<Void>> resultHandler) {

		logger.info("-- desactivarEnte - AS");

		enteMongoService.desactivarEntePorId(desactivarHistorico, id,  resp -> {
			if(resp.succeeded()) {
				resultHandler.handle(Future.succeededFuture());

			} else {
				resultHandler.handle(
					ServiceException.fail(Respuestas.ERROR_BASE.getId(), resp.cause().getMessage()));

			}
		});
		return this;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public ApplicationService definirSituacionEnte(String idEnte, Situacion situacion,
												   Handler<AsyncResult<EnteDTO>> resultHandler) {
		logger.info("-- habilitarTransicionEnte - AS");

		enteMongoService.definirSituacionEnte(idEnte, situacion, resp -> {
			if(resp.succeeded()) {
				enteMongoService.consultarEntePorId(idEnte, busq -> {
					if(busq.succeeded()) {
						logger.info(busq.result());
						EnteDTO ente = EnteDTO.toObject(busq.result());

						resultHandler.handle(Future.succeededFuture(ente));
					} else {
						resultHandler.handle(
								ServiceException.fail(Respuestas.ERROR_BASE.getId(), busq.cause().getMessage()));
					}
				});
			} else {
				resultHandler.handle(
						ServiceException.fail(Respuestas.ERROR.getId(), resp.cause().getMessage()));
			}
		});
		return this;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void close() {
		logger.info("-------------CLOSE DUMMY");

	}

}
