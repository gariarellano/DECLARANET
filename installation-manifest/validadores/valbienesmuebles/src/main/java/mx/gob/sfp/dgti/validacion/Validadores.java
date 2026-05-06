/**
 * @(#)Validadores.java 13/11/2019
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
import mx.gob.sfp.dgti.declaracion.dto.general.BienMuebleDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.BienesMueblesDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.util.campos.EnumCampos;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValBien;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase con las validaciones de bienes muebles.
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 13/11/2019
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
	 * Metodo para validar el modulo de bienes muebles
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 13/11/2019
	 */
	public Future<ModuloDTO> validar(BienesMueblesDTO bienesMuebles) {
		Future<ModuloDTO> future = Future.future();

		ModuloDTO moduloDto = new ModuloDTO(EnumModulo.I_BIENES_MUEBLES.getModulo());

		//Agregar modulos
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		//Agregar propiedades
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		if(bienesMuebles.isNinguno()) {
			propiedades.add(PropBase.crearModuloDebeSerNulo(bienesMuebles.getBienesMuebles(),
					EnumCampos.BIENES_MUEBLES.getCampo()));

		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(bienesMuebles.getBienesMuebles(),
					EnumCampos.BIENES_MUEBLES.getCampo()));
			if(!bienesMuebles.getBienesMuebles().isEmpty()) {
				for (BienMuebleDTO bienInmueble : bienesMuebles.getBienesMuebles()) {
					if (bienInmueble.isVerificar()) {
						modulos.add(crearBienInmueble(bienInmueble, bienesMuebles.getEncabezado()));
					} else {
						moduloDto.setIncompleto(true);
					}
				}
			}
		}

		propiedades.add(PropBase.crearPropAclaraciones(bienesMuebles.getAclaracionesObservaciones()));

		new ExectValidations(client, urlCatalogos).ejecutarValidacionesRx(
				new ModuloValidarDTO(EnumModulo.I_BIENES_MUEBLES.getModulo(),
						modulos,
						propiedades) ,
				moduloDto
		).doOnComplete(() -> {
			future.complete(moduloDto);

		}).doOnError(e -> {
			LOGGER.info("=== doOnError()");
			future.fail("error: " + e);
		})
				.subscribe();

		return future;
	}

	/**
	 * Metodo para definir el submodulo de bien mueble
	 *
	 * @param bienMueble
	 * @return objeto ModuloValidarDTO con las validaciones
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 13/11/2019
	 */
	public static ModuloValidarDTO crearBienInmueble(BienMuebleDTO bienMueble, EncabezadoDTO encabezado){

		//Se ocupa el validador general de bienes y luego se le suma lo que falte.
		ModuloValidarDTO moduloValidar = ValBien.crearBien(bienMueble, EnumCampos.BIENES_MUEBLES.getCampo(),
				encabezado);

		//validar el tipo de bien mueble
		PropBase.crearPropCatalogoOtro(EnumCampos.TIPO_BIEN.getCampo(),
				EnumCatalogos.CAT_TIPO_BIEN_MUEBLE.name(),
				bienMueble.getTipoBien(),
				true,
				bienMueble.getTipoBienOtro(),
				moduloValidar.getListPropsTovalidate());

		//validr la descripcion general
		moduloValidar.getListPropsTovalidate().add(
				Propiedades.crearDescripcionGeneralBien(bienMueble.getDescripcionGeneralBien()));
		return moduloValidar;
	}
}
