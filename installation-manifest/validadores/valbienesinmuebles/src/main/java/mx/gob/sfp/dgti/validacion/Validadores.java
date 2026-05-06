/**
 * @(#)Validadores.java 10/11/2019
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
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.BienInmuebleDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.BienesInmueblesDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumDomicilio;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.util.campos.EnumCampos;
import mx.gob.sfp.dgti.util.campos.EnumTipoBienInmueble;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValBien;
import mx.gob.sfp.dgti.utils.validaciones.ValDomicilio;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase con las validaciones de bienes inmuebles.
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
	static final String URL_CATALOGOS_ENV = "URL_CATALOGOS";

	/**
	 * URL para catalogos
	 */
	static final String URL_CATALOGOS = System.getenv(URL_CATALOGOS_ENV);

	/**
	 * WebCLient
	 */
	private WebClient client;

	public Validadores(Vertx vertx) {

		client = WebClient.create(vertx);
	}

	/**
	 * Metodo para validar el modulo de bienes inmuebles
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 10/11/2019
	 */
	public Future<ModuloDTO> validar(BienesInmueblesDTO bienesInmuebles) {
		Promise<ModuloDTO> promise = Promise.promise();

		ModuloDTO moduloDto = new ModuloDTO(EnumModulo.I_BIENES_INMUEBLES.getModulo());

		//Agregar modulos
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		//Agregar propiedades
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		if(bienesInmuebles.isNinguno()) {
			propiedades.add(PropBase.crearModuloDebeSerNulo(bienesInmuebles.getBienesInmuebles(),
					EnumCampos.BIENES_INMUEBLES.getCampo()));

		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(bienesInmuebles.getBienesInmuebles(),
					EnumCampos.BIENES_INMUEBLES.getCampo()));
			if(!bienesInmuebles.getBienesInmuebles().isEmpty()) {
				for (BienInmuebleDTO bienInmueble : bienesInmuebles.getBienesInmuebles()) {
					if(bienInmueble.isVerificar()) {
						modulos.add(crearBienInmueble(bienInmueble, bienesInmuebles.getEncabezado()));
					} else {
						moduloDto.setIncompleto(true);
					}
				}
			}
		}

		propiedades.add(PropBase.crearPropAclaraciones(bienesInmuebles.getAclaracionesObservaciones()));

		new ExectValidations(client, URL_CATALOGOS).ejecutarValidacionesRx(
				new ModuloValidarDTO(EnumModulo.I_BIENES_INMUEBLES.getModulo(),
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
	 * Metodo para definir el submodulo de bien inmueble
	 *
	 * @param bienInmueble
	 * @return objeto ModuloValidarDTO con las validaciones
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 11/11/2019
	 */
	public static ModuloValidarDTO crearBienInmueble(BienInmuebleDTO bienInmueble, EncabezadoDTO encabezado){

		//Se ocupa el validador general de bienes y luego se le suma lo que falte.
		ModuloValidarDTO moduloValidar = ValBien.crearBien(bienInmueble, EnumCampos.BIENES_INMUEBLES.getCampo(),
				encabezado);

		//validar tipo inmueble
		PropBase.crearPropCatalogoOtro(EnumCampos.TIPO_INMUEBLE.getCampo(),
			EnumCatalogos.CAT_TIPO_BIEN_INMUEBLE.name(),
			bienInmueble.getTipoInmueble(),
			true,
			bienInmueble.getTipoInmuebleOtro(),
			moduloValidar.getListPropsTovalidate()
		);

		//validar porcentaje propiedad
		moduloValidar.getListPropsTovalidate().add(
				Propiedades.crearPorcentajePropiedad(bienInmueble.getPorcentajePropiedad()));

		//Validar superficie de terreno
		moduloValidar.getListPropsTovalidate().add(
				Propiedades.crearSuperfTerrenoM2(bienInmueble.getSuperficieTerrenoM2()));

		//Validar superficie de construccion
		if(bienInmueble.getTipoInmueble() != null
				&& bienInmueble.getTipoInmueble().getId().equals(EnumTipoBienInmueble.TERRENO.getId()))  {
			moduloValidar.getListPropsTovalidate().add(
					Propiedades.crearSuperfConstruccionM2(bienInmueble.getSuperficieConstruccionM2(), true));
		} else {
			moduloValidar.getListPropsTovalidate().add(
					Propiedades.crearSuperfConstruccionM2(bienInmueble.getSuperficieConstruccionM2(), false));
		}

		//validar dato de identificacion
		moduloValidar.getListPropsTovalidate().add(
				Propiedades.crearDatoIdentificacion(bienInmueble.getDatoIdentificacion()));

		//validar domicilio
		if(bienInmueble.getDomicilio() != null ) {
			moduloValidar.getListModuloshijos().add(
					ValDomicilio.crearDomicilio(bienInmueble.getDomicilio(), EnumCampos.DOMICILIO.getCampo()));
		} else {
			moduloValidar.getListPropsTovalidate()
					.add(PropBase.crearModuloDebeSerNoNulo(bienInmueble.getDomicilio(),
							EnumDomicilio.DOMICILIO.getNombre()));
		}
		return moduloValidar;
	}
}
