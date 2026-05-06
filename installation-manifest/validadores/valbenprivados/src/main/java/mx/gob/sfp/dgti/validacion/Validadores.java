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
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumFormaRecepcion;
import mx.gob.sfp.dgti.dto.BeneficioDTO;
import mx.gob.sfp.dgti.dto.BeneficiosPrivadosDTO;
import mx.gob.sfp.dgti.util.campos.EnumCampos;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValMonto;
import mx.gob.sfp.dgti.utils.validaciones.ValPersona;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase con las validaciones de datos de pareja.
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
	 * Metodo para validar el modulo de beneficios
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 10/11/2019
	 */
	public Future<ModuloDTO> validar(BeneficiosPrivadosDTO beneficiosPrivados) {
		Promise<ModuloDTO> promise = Promise.promise();

		ModuloDTO moduloDto = new ModuloDTO(EnumModulo.II_BENEFICIOS_PRIVADOS.getModulo());

		//Agregar modulos
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		//Agregar propiedades
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		if(beneficiosPrivados.isNinguno()) {
			propiedades.add(PropBase.crearModuloDebeSerNulo(beneficiosPrivados.getBeneficios(),
					EnumCampos.BENEFICIOS.getCampo()));

		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(beneficiosPrivados.getBeneficios(),
					EnumCampos.BENEFICIOS.getCampo()));
			for (BeneficioDTO beneficio : beneficiosPrivados.getBeneficios()) {
				if (beneficio.isVerificar()) {
					modulos.add(crearBeneficio(beneficio, beneficiosPrivados.getEncabezado()));
				} else {
					moduloDto.setIncompleto(true);
				}
			}
		}

		propiedades.add(PropBase.crearPropAclaraciones(beneficiosPrivados.getAclaracionesObservaciones()));

		new ExectValidations(client, URL_CATALOGOS).ejecutarValidacionesRx(
				new ModuloValidarDTO(EnumModulo.II_BENEFICIOS_PRIVADOS.getModulo(),
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
	 * Metodo para definir el submodulo de benificio
	 *
	 * @param beneficio
	 * @return objeto ModuloValidarDTO con las validaciones
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 10/11/2019
	 */
	public static ModuloValidarDTO crearBeneficio(BeneficioDTO beneficio, EncabezadoDTO encabezado){

		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		propiedades.add(PropBase.crearId(beneficio.getId()));

		//Agregar validacion de tipoOperacion
		PropBase.crearPropTipoOperacion(beneficio, encabezado, propiedades);

		// CAT_TIPO_BENEFICIO
		propiedades.add(PropBase.crearPropCatalogo(
				EnumCampos.TIPO_BENEFICIO.getCampo(), EnumCatalogos.CAT_TIPO_BENEFICIO.name(),
				beneficio.getTipoBeneficio(), true
		));
		// CAT_BENEFICIARIO
		propiedades.add(PropBase.crearPropCatalogo(
				EnumCampos.BENEFICIARIO.getCampo(), EnumCatalogos.CAT_BENEFICIARIO_PROGRAMA.name(),
				beneficio.getBeneficiario(), true
		));
		// CAT_SECTOR
		PropBase.crearPropCatalogoOtro(
				EnumCampos.SECTOR.getCampo(), EnumCatalogos.CAT_SECTOR_PRIVADO.name(),
				beneficio.getSector(), true,
				beneficio.getSectorOtro(), propiedades
		);

		if(beneficio.getOtorgante() != null) {
			modulos.add(ValPersona.crearPersona(beneficio.getOtorgante(),
					EnumCampos.OTORGANTE.getCampo()));
		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(beneficio.getOtorgante(), EnumCampos.OTORGANTE.getCampo()));
		}
		propiedades.add(PropBase.crearObligatoria(beneficio.getFormaRecepcion(),
				EnumCampos.FORMA_RECEPCION.getCampo()));

		if(beneficio.getFormaRecepcion() != null) {
			if (beneficio.getFormaRecepcion().equals(EnumFormaRecepcion.MONETARIO)) {
				propiedades.add(PropBase.crearPropDebeSerNulo(
						beneficio.getEspecifiqueBeneficio(), EnumCampos.ESP_BENEFICIO.getCampo()));
			} else {
				propiedades.add(Propiedades.crearEspBeneficio(beneficio.getEspecifiqueBeneficio()));
			}
		}

		if (beneficio.getMontoMensualAproximado() != null) {
			modulos.add(ValMonto.crearMonto(beneficio.getMontoMensualAproximado(),
					EnumCampos.MONTO_MENSUAL_APROX.getCampo()));
		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(beneficio.getMontoMensualAproximado(),
					EnumCampos.MONTO_MENSUAL_APROX.getCampo()));
		}

		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(
				EnumCampos.BENEFICIOS.getCampo() , beneficio.getIdPosicionVista());
		moduloValidar.setListPropsTovalidate(propiedades);
		moduloValidar.setListModuloshijos(modulos);

		
		return moduloValidar;
	}
}
