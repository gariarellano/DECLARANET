/**
 * @(#)Validadores.java 10/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validacion;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.dto.RepresentacionDTO;
import mx.gob.sfp.dgti.dto.RepresentacionesDTO;
import mx.gob.sfp.dgti.util.campos.EnumCampos;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValDomicilio;
import mx.gob.sfp.dgti.utils.validaciones.ValMonto;
import mx.gob.sfp.dgti.utils.validaciones.ValPersona;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase con las validaciones de representaciones.
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 10/11/2019
 */
public class Validadores {

	/**
	 * El logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Validadores.class);

	/**
	 * CONSTANTE URL CATALGOOS
	 */
	final static String URL_CATALOGOS = "URL_CATALOGOS";

	/**
	 * URL para catalogos
	 */
	final static String urlCatalogos = 	System.getenv(URL_CATALOGOS);

	/**
	 * WebCLient
	 */
	private WebClient client;

	public Validadores(Vertx vertx) {

		client = WebClient.create(vertx);
	}

	/**
	 * Metodo para validar el modulo de los datos parejas
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 10/11/2019
	 */
	public Future<ModuloDTO> validar(RepresentacionesDTO representaciones) {
		Future<ModuloDTO> future = Future.future();

		ModuloDTO moduloDto = new ModuloDTO(EnumModulo.II_REPRESENTACION.getModulo());

		//Agregar modulos
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		//Agregar propiedades
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		if(representaciones.isNinguno()) {
			propiedades.add(PropBase.crearModuloDebeSerNulo(representaciones.getRepresentaciones(),
					EnumCampos.REPRESENTACIONES.getCampo()));

		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(representaciones.getRepresentaciones(),
					EnumCampos.REPRESENTACIONES.getCampo()));
			if(representaciones.getRepresentaciones() != null) {
				for (RepresentacionDTO representacion : representaciones.getRepresentaciones()) {
					if(representacion.isVerificar()) {
						modulos.add(crearRepresentacion(representacion, representaciones.getEncabezado()));
					} else {
						moduloDto.setIncompleto(true);
					}
				}
			}
		}

		propiedades.add(PropBase.crearPropAclaraciones(representaciones.getAclaracionesObservaciones()));

		new ExectValidations(client, urlCatalogos).ejecutarValidacionesRx(
				new ModuloValidarDTO(EnumModulo.II_REPRESENTACION.getModulo(),
						modulos,
						propiedades) ,
				moduloDto
		).doOnComplete(() -> {
			future.complete(moduloDto);

		}).doOnError(e -> {
			LOGGER.info("=== doOnError()");
			future.fail("error:" + e);
		})
				.subscribe();

		return future;
	}

	/**
	 * Metodo para definir el submodulo de datos de pareja
	 *
	 * @param representacion
	 * @return objeto ModuloValidarDTO con las validaciones
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 10/11/2019
	 */
	public static ModuloValidarDTO crearRepresentacion(RepresentacionDTO representacion, EncabezadoDTO encabezado) {

		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		propiedades.add(PropBase.crearId(representacion.getId()));

		//Agregar validacion de tipoOperacion
		PropBase.crearPropTipoOperacion(representacion, encabezado, propiedades);

		propiedades.add(PropBase.crearObligatoria(representacion.getParticipante(),
				EnumCampos.PARTICIPANTE.getCampo()));

		propiedades.add(PropBase.crearValidacionFechaContraFechaEncargo(representacion.getFechaInicioRepresentacion(),
				encabezado,	EnumCampos.FECHA_IN_REPRESENTACION.getCampo(), true));

		// AGREGAR CAT_SECTOR
		PropBase.crearPropCatalogoOtro(
				EnumCampos.SECTOR.getCampo(), EnumCatalogos.CAT_SECTOR_PRIVADO.name(),
				representacion.getSector(), true,
				representacion.getSectorOtro(),propiedades
		);

		if(representacion.getRepresentanteRepresentado() != null ) {
			modulos.add(ValPersona.crearPersona(representacion.getRepresentanteRepresentado(),
					EnumCampos.REPRES_REPRES.getCampo()));
		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(representacion.getRepresentanteRepresentado(),
					EnumCampos.REPRES_REPRES.getCampo()));
		}

		if(representacion.isRecibeRemuneracion()) {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(
					representacion.getMontoMensual(), EnumCampos.MONTO_MENSUAL.getCampo()));
			if(representacion.getMontoMensual() != null) {
				modulos.add(ValMonto.crearMonto(representacion.getMontoMensual(), EnumCampos.MONTO_MENSUAL.getCampo()));
			}
		} else {
			propiedades.add(PropBase.crearModuloDebeSerNulo(
					representacion.getMontoMensual(), EnumCampos.MONTO_MENSUAL.getCampo()));
		}
		if(representacion.getLocalizacion() != null) {
			modulos.add(ValDomicilio.crearLocalizacion(representacion.getLocalizacion(), EnumCampos.LOCALIZACION.getCampo()));
		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(representacion.getLocalizacion(),
					EnumCampos.LOCALIZACION.getCampo()));
		}
		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(
				EnumCampos.REPRESENTACIONES.getCampo() , representacion.getIdPosicionVista());
		moduloValidar.setListPropsTovalidate(propiedades);
		moduloValidar.setListModuloshijos(modulos);

		return moduloValidar;
	}
}
