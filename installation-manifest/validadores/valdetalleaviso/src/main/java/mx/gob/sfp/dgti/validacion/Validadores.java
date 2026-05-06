/**
 * @(#)Validadores.java 31/01/2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validacion;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.declaracion.dto.general.EnteReceptorDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoUnoFkDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogosUnoFk;
import mx.gob.sfp.dgti.dto.DetalleAvisoDTO;
import mx.gob.sfp.dgti.dto.EmpleoConcluyeDTO;
import mx.gob.sfp.dgti.dto.EmpleoIniciaDTO;
import mx.gob.sfp.dgti.dto.ModuloAvisoDTO;
import mx.gob.sfp.dgti.util.campos.EnumCampos;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValDomicilio;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase con las validaciones de detalles del aviso.
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 31/01/2020
 */
public class Validadores {

	/**
	 * El logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Validadores.class);

	/**
	 * CONSTANTE URL CATALGOOS
	 */
	static final String URL_CATALOGOS_ENV = "URL_CATALOGOS";

	/**
	 * URL para catalogos
	 */
	static final String URL_CATALOGOS = System.getenv(URL_CATALOGOS_ENV);

	/**
	 * WebCLient
	 */
	private WebClient client;

	private static final DateTimeFormatter isoFecha = DateTimeFormatter.ISO_LOCAL_DATE;

	public Validadores(Vertx vertx) {

		client = WebClient.create(vertx);
	}

	/**
	 * Metodo para validar el modulo de los datos parejas
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 04/11/2019
	 */
	public Future<ModuloAvisoDTO> validar(DetalleAvisoDTO detalleAviso) {
		Promise<ModuloAvisoDTO> promise = Promise.promise();

		ModuloAvisoDTO moduloDto = new ModuloAvisoDTO(EnumCampos.DETALLE_AVISO.getCampo());
		CatalogoUnoFkDTO nivelEncabezado = null;
		CatalogoUnoFkDTO nivelEnte = null;

		//Agregar modulos
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		//Agregar propiedades
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		//Obtener fecha de ahora
		LocalDate fechaAhoraDate = LocalDate.now();

		String fechaAhora = fechaAhoraDate.format(isoFecha);
		String fechaEncargo = detalleAviso.getEncabezado().getFechaEncargo();

		String fechaConcluye = null;

		nivelEncabezado = detalleAviso.getEncabezado().getNivelJerarquico();

		EnteReceptorDTO enteConcluye = null;
		// Validacion del empleo cargo o comision que concluye
		propiedades.add(PropBase.crearModuloDebeSerNoNulo(detalleAviso.getEmpleoCargoComisionConcluye(),
				EnumCampos.EMPLEO_CONCLUYE.getCampo()));
		if (detalleAviso.getEmpleoCargoComisionConcluye() != null) {
			nivelEnte = detalleAviso.getEmpleoCargoComisionConcluye().getNivelJerarquico();
			enteConcluye = detalleAviso.getEmpleoCargoComisionConcluye().getEnte();
			if(detalleAviso.getEmpleoCargoComisionConcluye().getFechaConclusionEncargo() != null ) {
				fechaConcluye = detalleAviso.getEmpleoCargoComisionConcluye().getFechaConclusionEncargo();
			}
			modulos.add(crearEmpleoConcluye(detalleAviso.getEmpleoCargoComisionConcluye(), fechaEncargo, fechaAhora));
		}

		if(detalleAviso.getEmpleoCargoComisionInicia() == null) {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(detalleAviso.getEmpleoCargoComisionInicia(),
					EnumCampos.EMPLEO_INICIA.getCampo()));
		} else {

			if(fechaConcluye != null && enteConcluye != null && detalleAviso.getEmpleoCargoComisionInicia() != null) {
				modulos.add(crearEmpleoInicia(
						detalleAviso.getEmpleoCargoComisionInicia(), fechaAhora, fechaConcluye, enteConcluye));
			}

		}

		propiedades.add(PropBase.crearPropAclaraciones(detalleAviso.getAclaracionesObservaciones()));

		activarCambio(moduloDto, nivelEncabezado, nivelEnte);

		new ExectValidations(client, URL_CATALOGOS).ejecutarValidacionesRx(
				new ModuloValidarDTO(EnumCampos.DETALLE_AVISO.getCampo(),
						modulos,
						propiedades) ,
				moduloDto)
						.doOnComplete(() -> promise.complete(moduloDto))
						.doOnError(e -> {
							LOGGER.info("=== doOnError()");
							promise.fail("error: " + e);
				})
				.subscribe();

		return promise.future();
	}

	/**
	 *
	 * @param empleoConcluye
	 * @param fechaEncargo
	 * @param fechaAhora
	 * @return
	 */
	public static ModuloValidarDTO crearEmpleoConcluye(EmpleoConcluyeDTO empleoConcluye,
													   String fechaEncargo, String fechaAhora) {
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		propiedades.add(PropBase.crearId(empleoConcluye.getId()));

		if(empleoConcluye.getEnte() == null ) {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(
					empleoConcluye.getEnte(), EnumCampos.EMPLEO_CONCLUYE.getCampo()));
		} else {
			//Validacion de ente
			modulos.add(Propiedades.crearEnte(empleoConcluye.getEnte(), EnumCampos.ENTE.getCampo()));
		}

		//Validacion del nivel jerarquico
		if (empleoConcluye.getNivelJerarquico() == null) {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(empleoConcluye.getNivelJerarquico(),
					EnumCampos.NIVEL_JERARQUICO.getCampo()));
		} else {
			//Validacion del nivel jerarquico
			propiedades.add(PropBase.crearPropCatalogo(
					EnumCampos.NIVEL_JERARQUICO.getCampo(), EnumCatalogosUnoFk.CAT_NIVEL_JERARQUICO.name(),
					empleoConcluye.getNivelJerarquico(), true
			));
		}

		//Validaciones para las fecha de conclusion
		propiedades.add(Propiedades.crearValidacionFechaConcluye(
				EnumCampos.FECHA_CONCLUYE.getCampo(),
				empleoConcluye.getFechaConclusionEncargo(),
				fechaEncargo,
				fechaAhora));

		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(EnumCampos.EMPLEO_CONCLUYE.getCampo());

		moduloValidar.setListPropsTovalidate(propiedades);
		moduloValidar.setListModuloshijos(modulos);

		return moduloValidar;
	}

	/**
	 *
	 * @param empleoInicia
	 * @param fechaAhora
	 * @param fechaConcluye
	 * @return
	 */
	public static ModuloValidarDTO crearEmpleoInicia(EmpleoIniciaDTO empleoInicia, String fechaAhora,
													 String fechaConcluye, EnteReceptorDTO enteConcluye) {
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		//Validacion de id
		propiedades.add(PropBase.crearId(empleoInicia.getId()));

		//Validacion del empleo que inicia
		if( empleoInicia.getEnte() == null ) {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(
					empleoInicia.getEnte(), EnumCampos.EMPLEO_INICIA.getCampo()));
		} else {
			modulos.add(Propiedades.crearEnte(empleoInicia.getEnte(), EnumCampos.ENTE.getCampo()));
			if( enteConcluye != null ) {
				Propiedades.crearComparacionEntes(enteConcluye, empleoInicia.getEnte(), propiedades);
			}
		}

		//Validacion del nivel jerarquico
		propiedades.add(PropBase.crearPropCatalogo(
				EnumCampos.NIVEL_JERARQUICO.getCampo(), EnumCatalogosUnoFk.CAT_NIVEL_JERARQUICO.name(),
				empleoInicia.getNivelJerarquico(), true
		));

		//Validaciones para la fecha de inicio
		propiedades.add( Propiedades.crearValidacionFechaInicia(
				EnumCampos.FECHA_INICIO.getCampo(),
				empleoInicia.getFechaInicioEncargo(),
				fechaConcluye,
				fechaAhora));

		//Validacion del empleo
		propiedades.add(Propiedades.crearEmpleoCargoComision(empleoInicia.getEmpleoCargoComision()));

		//Validacion del nivel jerarquico
		if (empleoInicia.getNivelJerarquico() == null) {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(empleoInicia.getNivelJerarquico(),
					EnumCampos.NIVEL_JERARQUICO.getCampo()));
		}

		//Validacion del nivel de empleo
		propiedades.add(Propiedades.crearNivelEmpleoCargoComision(empleoInicia.getNivelEmpleoCargoComision()));

		//Validacion de domicilio
		propiedades.add(PropBase.crearModuloDebeSerNoNulo(empleoInicia.getDomicilio(),
				EnumCampos.DOMICILIO.getCampo()));
		if (empleoInicia.getDomicilio() != null) {
			modulos.add(ValDomicilio.crearDomicilio(empleoInicia.getDomicilio(), EnumCampos.DOMICILIO.getCampo()));
		}

		//Validacion de area de adscripcion
		propiedades.add(Propiedades.crearAreaAdscripcion(empleoInicia.getAreaAdscripcion()));

		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(EnumCampos.EMPLEO_INICIA.getCampo());

		moduloValidar.setListPropsTovalidate(propiedades);
		moduloValidar.setListModuloshijos(modulos);

		return moduloValidar;
	}

	/**
	 *
	 * @param modulo
	 * @param nivelEncabezado
	 * @param nivelEnte
	 */
	private void activarCambio(ModuloAvisoDTO modulo, CatalogoUnoFkDTO nivelEncabezado,
									  CatalogoUnoFkDTO nivelEnte) {
		if(nivelEncabezado != null && nivelEnte != null && nivelEncabezado.getValorUno() != null &&
			nivelEnte.getValorUno() != null && nivelEnte.getFk() != null && nivelEncabezado.getFk() != null) {
			if (!nivelEncabezado.getValorUno().equals(nivelEnte.getValorUno())
					|| !nivelEncabezado.getFk().equals(nivelEnte.getFk())) {
				modulo.setNiverJerarquico(nivelEnte);
			}
		}
	}

}
