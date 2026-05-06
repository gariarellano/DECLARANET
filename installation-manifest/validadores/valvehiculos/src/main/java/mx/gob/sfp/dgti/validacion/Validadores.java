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
import mx.gob.sfp.dgti.declaracion.dto.general.VehiculoDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.VehiculosDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.util.campos.EnumCampos;
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
 * Clase con las validaciones de vehiculos.
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
	 * Metodo para validar el modulo de bienes inmuebles
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 10/11/2019
	 */
	public Future<ModuloDTO> validar(VehiculosDTO vehiculos) {
		Future<ModuloDTO> future = Future.future();

		ModuloDTO moduloDto = new ModuloDTO(EnumModulo.I_VEHICULOS.getModulo());

		//Agregar modulos
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		//Agregar propiedades
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		if(vehiculos.isNinguno()) {
			propiedades.add(PropBase.crearModuloDebeSerNulo(vehiculos.getVehiculos(),
					EnumCampos.VEHICULOS.getCampo()));

		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(vehiculos.getVehiculos(),
					EnumCampos.VEHICULOS.getCampo()));
			if(!vehiculos.getVehiculos().isEmpty()) {
				for (VehiculoDTO vehiculo : vehiculos.getVehiculos()) {
					if(vehiculo.isVerificar()) {
						modulos.add(crearVehiculo(vehiculo, vehiculos.getEncabezado()));
					} else {
						moduloDto.setIncompleto(true);
					}
				}
			}
		}

		propiedades.add(PropBase.crearPropAclaraciones(vehiculos.getAclaracionesObservaciones()));

		new ExectValidations(client, urlCatalogos).ejecutarValidacionesRx(
				new ModuloValidarDTO(EnumModulo.I_VEHICULOS.getModulo(),
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
	 * Metodo para definir el submodulo de bien inmueble
	 *
	 * @param vehiculo
	 * @return objeto ModuloValidarDTO con las validaciones
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 11/11/2019
	 */
	public static ModuloValidarDTO crearVehiculo(VehiculoDTO vehiculo, EncabezadoDTO encabezado){

		//Se ocupa el validador general de bienes y luego se le suma lo que falte.
		ModuloValidarDTO moduloValidar = ValBien.crearBien(vehiculo, EnumCampos.VEHICULOS.getCampo(),
				encabezado);

		//Validar tipo vehiculo
		PropBase.crearPropCatalogoOtro(EnumCampos.TIPO_VEHICULO.getCampo(),
				EnumCatalogos.CAT_TIPO_VEHICULO.name(), vehiculo.getTipoVehiculo(),true,
				vehiculo.getTipoVehiculoOtro(), moduloValidar.getListPropsTovalidate());

		//Validar marca
		moduloValidar.getListPropsTovalidate().add(
				Propiedades.crearMarca(vehiculo.getMarca()));
		//Validar modelo
		moduloValidar.getListPropsTovalidate().add(
				Propiedades.crearModelo(vehiculo.getModelo()));

		//validar numero de serie o registro
		moduloValidar.getListPropsTovalidate().add(
				Propiedades.crearNumSerieRegistro(vehiculo.getNumeroSerieRegistro()));

		//Validar el anio
		moduloValidar.getListPropsTovalidate().add(
				Propiedades.crearAnio(vehiculo.getAnio(), encabezado));

		//Validar el lugar de registro
		moduloValidar.getListModuloshijos().add(
				ValDomicilio.crearLocalizacion(vehiculo.getLugarRegistro(), EnumCampos.LUGAR_REGISTRO.getCampo()));


		return moduloValidar;
	}
}
