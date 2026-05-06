/**
 * @(#)Validadores.java 08/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validacion;

import java.util.ArrayList;
import java.util.List;

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
import mx.gob.sfp.dgti.dto.ApoyoDTO;
import mx.gob.sfp.dgti.dto.ApoyosDTO;
import mx.gob.sfp.dgti.util.campos.EnumCampos;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValMonto;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;

/**
 * Clase con las validaciones de datos de pareja.
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 08/11/2019
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
	 * Metodo para validar el modulo de los datos parejas
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 08/11/2019
	 */
	public Future<ModuloDTO> validar(ApoyosDTO apoyos) {
		Promise<ModuloDTO> promise = Promise.promise();

		ModuloDTO moduloDto = new ModuloDTO(EnumCampos.APOYOS.getCampo());

		//Agregar modulos
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		//Agregar propiedades
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		if(apoyos.isNinguno()) {
			propiedades.add(PropBase.crearModuloDebeSerNulo(apoyos.getApoyos(),
					EnumCampos.APOYOS.getCampo()));

		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(apoyos.getApoyos(),
					EnumCampos.APOYOS.getCampo()));
			for (ApoyoDTO apoyo : apoyos.getApoyos()) {
				if(apoyo.isVerificar()) {
					modulos.add(crearApoyo(apoyo, apoyos.getEncabezado()));
				} else {
					moduloDto.setIncompleto(true);
				}
			}
		}

		propiedades.add(PropBase.crearPropAclaraciones(apoyos.getAclaracionesObservaciones()));

		new ExectValidations(client, URL_CATALOGOS).ejecutarValidacionesRx(
				new ModuloValidarDTO(EnumModulo.II_APOYOS_BENEFICIOS.getModulo(),
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
	 * Metodo para definir el submodulo de apoyo
	 *
	 * @param apoyo
	 * @return objeto ModuloValidarDTO con las validaciones
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 04/11/2019
	 */
	public static ModuloValidarDTO crearApoyo(ApoyoDTO apoyo, EncabezadoDTO encabezado){

		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		propiedades.add(PropBase.crearId(apoyo.getId()));

		//Agregar validacion de tipoOperacion
		PropBase.crearPropTipoOperacion(apoyo, encabezado, propiedades);

		propiedades.add(PropBase.crearObligatoria(apoyo.getFormaRecepcion(), EnumCampos.FORMA_RECEPCION.getCampo()));
		propiedades.add(PropBase.crearObligatoria(apoyo.getNivelOrdenGobierno(), EnumCampos.NIVEL_ORDEN_GOB.getCampo()));

		// AGREGAR CAT_BENEFICIARIO_PROGRAMA
		propiedades.add(PropBase.crearPropCatalogo(
				EnumCampos.BENEFICIARIO.getCampo(), EnumCatalogos.CAT_BENEFICIARIO_PROGRAMA.name(),
				apoyo.getBeneficiarioPrograma(), true
		));
		// AGREGAR CAT_TIPO_APOYO
		PropBase.crearPropCatalogoOtro(
			EnumCampos.TIPO_APOYO.getCampo(), EnumCatalogos.CAT_TIPO_APOYO.name(),
			apoyo.getTipoApoyo(), true,
			apoyo.getTipoApoyoOtro(), propiedades
		);

		propiedades.add(Propiedades.crearNombrePrograma(apoyo.getNombrePrograma()));
		propiedades.add(Propiedades.crearInstitucionOtorgante(apoyo.getInstitucionOtorgante()));

		propiedades.add(PropBase.crearObligatoria(apoyo.getFormaRecepcion(),
				EnumCampos.FORMA_RECEPCION.getCampo()));

		if(apoyo.getFormaRecepcion() != null) {
			if (apoyo.getFormaRecepcion().equals(EnumFormaRecepcion.MONETARIO)) {
				propiedades.add(PropBase.crearPropDebeSerNulo(apoyo.getEspecifiqueApoyo(),
						EnumCampos.ESPECIFIQUE_APOYO.getCampo()));
			} else {
				propiedades.add(Propiedades.crearEspecifiqueApoyo(apoyo.getEspecifiqueApoyo()));
			}
		}

		if (apoyo.getMontoApoyoMensual() != null) {
			modulos.add(ValMonto.crearMonto(apoyo.getMontoApoyoMensual(), EnumCampos.MONTO_APOYO_MENSUAL.getCampo()));
		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(apoyo.getMontoApoyoMensual(),
					EnumCampos.MONTO_APOYO_MENSUAL.getCampo()));
		}

		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(
				EnumCampos.APOYOS.getCampo() , apoyo.getIdPosicionVista());
		moduloValidar.setListPropsTovalidate(propiedades);
		moduloValidar.setListModuloshijos(modulos);

		return moduloValidar;
	}

}
