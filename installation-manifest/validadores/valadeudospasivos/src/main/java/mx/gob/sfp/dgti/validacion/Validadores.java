/**
 * @(#)Validadores.java 03/11/2019
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
import mx.gob.sfp.dgti.declaracion.dto.individual.PersonaDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogosUno;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCoopropiedad;
import mx.gob.sfp.dgti.dto.AdeudoDTO;
import mx.gob.sfp.dgti.dto.AdeudosPasivosDTO;
import mx.gob.sfp.dgti.util.EnumTipoAdeudo;
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
 * @since 03/11/2019
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
	 * @since 04/11/2019
	 */
	public Future<ModuloDTO> validar(AdeudosPasivosDTO adeudosPasivos) {
		Promise<ModuloDTO> promise = Promise.promise();

		ModuloDTO moduloDto = new ModuloDTO(EnumCampos.ADEUDOS_PASIVOS.getCampo());

		//Agregar modulos
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		//Agregar propiedades
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		if(adeudosPasivos.isNinguno()) {
			propiedades.add(PropBase.crearModuloDebeSerNulo(adeudosPasivos.getAdeudos(),
					EnumCampos.ADEUDOS.getCampo()));
		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(adeudosPasivos.getAdeudos(),
					EnumCampos.ADEUDOS.getCampo()));
			for (AdeudoDTO datos : adeudosPasivos.getAdeudos()) {
				if(datos.isVerificar()) {
					modulos.add(crearAdeudo(datos, adeudosPasivos.getEncabezado()));
				} else {
					moduloDto.setIncompleto(true);
				}
			}
		}

		propiedades.add(PropBase.crearPropAclaraciones(adeudosPasivos.getAclaracionesObservaciones()));

		new ExectValidations(client, URL_CATALOGOS).ejecutarValidacionesRx(
				new ModuloValidarDTO(EnumModulo.I_ADEUDOS_PASIVOS.getModulo(),
						modulos,
						propiedades) ,
				moduloDto
		).doOnComplete(() -> promise.complete(moduloDto)
		).doOnError(e -> {
			LOGGER.info("=== doOnError()");
			promise.fail("error: " + e);
		})
		.subscribe();

		return promise.future();
	}

	/**
	 * Metodo para definir el submodulo de adeudo
	 *
	 * @param adeudo
	 * @return objeto ModuloValidarDTO con las validaciones
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 04/11/2019
	 */
	public static ModuloValidarDTO crearAdeudo(AdeudoDTO adeudo, EncabezadoDTO encabezado){

		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		//validar id
		propiedades.add(PropBase.crearId(adeudo.getId()));

		//validar idPosicionVista
		propiedades.add(PropBase.crearIdPosicionVista(adeudo.getIdPosicionVista()));

		// validar el tipoOperacion
		PropBase.crearPropTipoOperacion(adeudo, encabezado, propiedades);

		// validar el tipo de adeudo
		if(adeudo.getTipoAdeudo() != null) {
			if (adeudo.getTipoAdeudo().getId().intValue() == EnumTipoAdeudo.PRESTAMO_PERSONAL.getId().intValue()) {
				if(adeudo.getNumeroCuentaContrato() != null && !adeudo.getNumeroCuentaContrato().isEmpty()) {
					propiedades.add(Propiedades.crearNumeroCuentaContrato(adeudo.getNumeroCuentaContrato()));
				}
			} else {
				propiedades.add(Propiedades.crearNumeroCuentaContrato(adeudo.getNumeroCuentaContrato()));
			}
		}

		//validar saldo insoluto
		propiedades.add(Propiedades.crearSaldoInsoluto(adeudo.getSaldoInsoluto()));

		//validar titular
		propiedades.add(PropBase.crearPropCatalogo(
				EnumCampos.TITULAR.getCampo(), EnumCatalogosUno.CAT_TITULAR.name(),
				adeudo.getTitular(), true
		));

		//validar tipo de adeudo
		PropBase.crearPropCatalogoOtro(
				EnumCampos.TIPO_ADEUDO.getCampo(), EnumCatalogos.CAT_TIPO_ADEUDO.name(),
				adeudo.getTipoAdeudo(), true,
				adeudo.getTipoAdeudoOtro(), propiedades
		);

		//Vlidar paisAdeudo
		propiedades.add(PropBase.crearPropCatalogo(
				EnumCampos.PAIS_ADEUDO.getCampo(), EnumCatalogos.CAT_PAIS.name(),
				adeudo.getPaisAdeudo(), true
				));

		//validar fechaAdquision
		propiedades.add(PropBase.crearValidacionFechaContraFechaEncargo(adeudo.getFechaAdquisicion(), encabezado,
				EnumCampos.FECHA_ADQUISICION.getCampo(), true));

		//validar montoOriginal
		if (adeudo.getMontoOriginal() != null ) {
			modulos.add(ValMonto.crearMonto(adeudo.getMontoOriginal(), EnumCampos.MONTO_ORIGINAL.getCampo()));
		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(adeudo.getMontoOriginal(), EnumCampos.MONTO_ORIGINAL.getCampo()));
		}

		//validar titular
		if(adeudo.getTitular() != null) {
			if(adeudo.getTitular().getValorUno().equals(EnumCoopropiedad.COOPROPIEDAD.getDescripcion())) {
				propiedades.add(PropBase.crearModuloDebeSerNoNulo(adeudo.getTerceros(), EnumCampos.TERCEROS.getCampo()));
				if (!adeudo.getTerceros().isEmpty()) {
					for (PersonaDTO tercero : adeudo.getTerceros()){
						modulos.add(ValPersona.crearPersona(tercero, EnumCampos.TERCERO.getCampo()));
					}
				}
			} else {
				propiedades.add(PropBase.crearModuloDebeSerNulo(adeudo.getTerceros(), EnumCampos.TERCEROS.getCampo()));
			}
		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(adeudo.getTitular(), EnumCampos.TITULAR.getCampo()));
		}

		if (adeudo.getOtorganteCredito() != null) {
			modulos.add(ValPersona.crearPersona(adeudo.getOtorganteCredito(), EnumCampos.OTORGANTE_CREDITO.getCampo()));
		} else {
			propiedades.add(
					PropBase.crearModuloDebeSerNoNulo(adeudo.getOtorganteCredito(), EnumCampos.OTORGANTE_CREDITO.getCampo()));
		}
		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(
				EnumCampos.ADEUDOS.getCampo() , adeudo.getOtorganteCredito().getIdPosicionVista());
		moduloValidar.setListPropsTovalidate(propiedades);
		moduloValidar.setListModuloshijos(modulos);

		return moduloValidar;
	}

}
