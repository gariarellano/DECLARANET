package mx.gob.sfp.dgti.validacion;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.declaracion.dto.base.CabeceraDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.InstitucionReceptoraDTO;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.validaciones.ValCabeceraDecla;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;

public class ValidacionDatosInicio {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidacionDatosInicio.class);

	/**
	 * CONSTANTE URL CATALGOOS
	 */
	private static final String URL_CATALOGOS = "URL_CATALOGOS";
	
	/*
	 * CONSTANTE CABECERA
	 */
	private static final String PROP_CABECERA = "Cabecera";
	
	/*
	 * CONSTANTE CABECERA
	 */
	private static final String PROP_INSTITUCION = "institucion";
	
	/*
	 * CONSTANTE DO_ON_ERROR
	 */
	private static final String DO_ON_ERROR = "=== doOnError()";
	
	/*
	 * CONSTANTE DO_ON_COMPLETE
	 */
	private static final String DO_ON_COMPLETE = "=== doOnComplete()";
	
	/**
	 * URL para catalogos
	 */
	private static final String URL_CATALOGOS_ENV = System.getenv(URL_CATALOGOS);
	
	
	/**
	 * WebCLient
	 */
	private WebClient client;

	public ValidacionDatosInicio(Vertx vertx) {
		client = WebClient.create(vertx);
	}

	/**
	 * Método para obtener las validaciones de la cabecera
	 * 
	 * @param CabeceraDTO cabecera de la declaración
	 * @return future
	 */
	@SuppressWarnings("unchecked")
	public Future<ModuloDTO> obtenerValidaciones(CabeceraDTO cabecera) {
		Future<ModuloDTO> future = Future.future();
		ModuloDTO modulo = new ModuloDTO(PROP_CABECERA);

		new ExectValidations(client, URL_CATALOGOS_ENV)
				.ejecutarValidacionesRx(ValCabeceraDecla.crearCabeceraInicial(cabecera), modulo).doOnError(error -> {
					LOGGER.error(DO_ON_ERROR);
					LOGGER.error(error);
					future.failed();
				}).doOnComplete(() -> {
					LOGGER.info(DO_ON_COMPLETE);
					future.complete(modulo);
				}).subscribe();

		return future;
	}

	/**
	 * Método para obtener las validaciones de la cabecera cuando ya existe una
	 * declaración
	 * 
	 * @param CabeceraDTO cabecera de la declaración
	 * @return future
	 * @since 28/11/2019
	 */
	@SuppressWarnings("unchecked")
	public Future<ModuloDTO> obtenerValidacionesExistente(CabeceraDTO cabecera) {
		Future<ModuloDTO> future = Future.future();
		ModuloDTO modulo = new ModuloDTO(PROP_CABECERA);

		new ExectValidations(client, URL_CATALOGOS_ENV)
		.ejecutarValidacionesRx(ValCabeceraDecla.crearCabeceraDeclaracionExistente(cabecera),
				modulo).doOnError(error -> {
			LOGGER.error(DO_ON_ERROR);
			LOGGER.error(error);
			future.failed();
		}).doOnComplete(() -> {
			LOGGER.info(DO_ON_COMPLETE);
			future.complete(modulo);
		}).subscribe();
		
		return future;
	}

	/**
	 * Método para obtener las validaciones de la institución receptora para las
	 * peticiones a los servicios
	 * 
	 * @param InstitucionReceptoraDTO institución receptora de la declaración
	 * @return future
	 */
	@SuppressWarnings("unchecked")
	public Future<ModuloDTO> obtenerValidacionesInstitucion(InstitucionReceptoraDTO institucionReceptoraDTO) {
		Future<ModuloDTO> future = Future.future();
		ModuloDTO modulo = new ModuloDTO(PROP_CABECERA);
		ModuloValidarDTO institucion = new ModuloValidarDTO(PROP_INSTITUCION);
		ValCabeceraDecla.realizaValidacioInstitucionRecep(institucion, institucionReceptoraDTO);
		
		new ExectValidations(client, URL_CATALOGOS_ENV)
		.ejecutarValidacionesRx(institucion, modulo).doOnError(error -> {
			LOGGER.error(DO_ON_ERROR);
			LOGGER.error(error);
			future.failed();
		}).doOnComplete(() -> {
			LOGGER.info(DO_ON_COMPLETE);
			future.complete(modulo);
		}).subscribe();

		return future;
	}

}
