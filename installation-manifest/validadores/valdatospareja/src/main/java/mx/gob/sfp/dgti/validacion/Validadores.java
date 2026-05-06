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
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumDomicilio;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumLugarReside;
import mx.gob.sfp.dgti.dto.CiudadanoExtranjeroDTO;
import mx.gob.sfp.dgti.dto.DatosParejaDTO;
import mx.gob.sfp.dgti.dto.DatosParejasDTO;
import mx.gob.sfp.dgti.dto.DomicilioDepEconDTO;
import mx.gob.sfp.dgti.util.campos.EnumCampos;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValActividadLab;
import mx.gob.sfp.dgti.utils.validaciones.ValDomicilio;
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
	public Future<ModuloDTO> validar(DatosParejasDTO datosParejas) {
		Promise<ModuloDTO> promise = Promise.promise();

		ModuloDTO moduloDto = new ModuloDTO(EnumCampos.DATOS_PAREJAS.getCampo());

		//Agregar modulos
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		//Agregar propiedades
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		if(datosParejas.isNinguno()) {
			propiedades.add(PropBase.crearModuloDebeSerNulo(datosParejas.getDatosParejas(),
					EnumCampos.DATOS_PAREJA.getCampo()));

		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(datosParejas.getDatosParejas(),
					EnumCampos.DATOS_PAREJA.getCampo()));
			for (DatosParejaDTO datos : datosParejas.getDatosParejas()) {
				if(datos.isVerificar()) {
					modulos.add(crearDatosPareja(datos, datosParejas.getEncabezado()));
				} else {
					moduloDto.setIncompleto(true);
				}
			}
		}

		propiedades.add(PropBase.crearPropAclaraciones(datosParejas.getAclaracionesObservaciones()));

		new ExectValidations(client, URL_CATALOGOS).ejecutarValidacionesRx(
				new ModuloValidarDTO(EnumModulo.I_DATOS_PAREJA.getModulo(),
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
	 * Metodo para definir el submodulo de datos de pareja
	 *
	 * @param datosPareja
	 * @return objeto ModuloValidarDTO con las validaciones
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 04/11/2019
	 */
	public static ModuloValidarDTO crearDatosPareja(DatosParejaDTO datosPareja, EncabezadoDTO encabezado){

		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		// validar el tipoOperacion
		PropBase.crearPropTipoOperacion(datosPareja, encabezado, propiedades);

		//Validar id
		propiedades.add(PropBase.crearId(datosPareja.getId()));

		//Validar idPosicionVista
		propiedades.add(PropBase.crearIdPosicionVista(datosPareja.getIdPosicionVista()));

		//Valida datos personales
		propiedades.add(PropBase.crearModuloDebeSerNoNulo(datosPareja.getDatosPersonales(),
				EnumCampos.DATOS_PERSONALES.getCampo()));
		if(datosPareja.getDatosPersonales() != null) {
			modulos.add(ValPersona.crearDatosPersonalesNoDecl(datosPareja.getDatosPersonales()));
		}

		//Valida ciudadano extranjero
		propiedades.add(PropBase.crearModuloDebeSerNoNulo(datosPareja.getCiudadanoExtranjero(),
				EnumCampos.CIUDADANO_EXTRANJERO.getCampo()));
		if(datosPareja.getCiudadanoExtranjero() != null) {
			modulos.add(crearCiudadanoExtranjero(datosPareja.getCiudadanoExtranjero()));
		}

		//Validar enum relacionConDeclarante
		propiedades.add(PropBase.crearObligatoria(datosPareja.getRelacionConDeclarante(),
				EnumCampos.RELACION_CON_DECL.getCampo()));

		//Valida domicilio de dependiente
		if(datosPareja.getHabitaDomicilioDeclarante().booleanValue()) {
			propiedades.add(PropBase.crearModuloDebeSerNulo(datosPareja.getDomicilioDependienteEconomico(),
					EnumCampos.DOMICILIO_DEP_ECON.getCampo()));
		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(datosPareja.getDomicilioDependienteEconomico(),
					EnumCampos.DOMICILIO_DEP_ECON.getCampo()));
			if(datosPareja.getDomicilioDependienteEconomico() != null) {
				modulos.add(crearDomicilioDepEcon(datosPareja.getDomicilioDependienteEconomico()));
			}
		}

		//Valida actividades laborales
		if (datosPareja.isNinguno()) {
			propiedades.add(PropBase.crearModuloDebeSerNulo(datosPareja.getActividadLaboral(),
					EnumCampos.ACTIVIDAD_LABORAL.getCampo()));
		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(datosPareja.getActividadLaboral(),
					EnumCampos.ACTIVIDAD_LABORAL.getCampo()));
			modulos.add(ValActividadLab.crearActividadLaboral(datosPareja.getActividadLaboral(), encabezado));
		}

		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(
				EnumCampos.DATOS_PAREJA.getCampo() , datosPareja.getIdPosicionVista());
		moduloValidar.setListPropsTovalidate(propiedades);
		moduloValidar.setListModuloshijos(modulos);

		return moduloValidar;
	}

	/**
	 * Metodo para definir el submodulo de ciudadano extranjero
	 *
	 * @param ciudadanoExt
	 * @return objeto ModuloValidarDTO con las validaciones
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 04/11/2019
	 */
	public static ModuloValidarDTO crearCiudadanoExtranjero(CiudadanoExtranjeroDTO ciudadanoExt) {
		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(EnumCampos.CIUDADANO_EXTRANJERO.getCampo());

		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		if (!ciudadanoExt.getEsExtranjero().booleanValue()) {
			propiedades.add(Propiedades.crearCurp(ciudadanoExt.getCurp()));
		}
		moduloValidar.setListPropsTovalidate(propiedades);
		return moduloValidar;
	}

	/**
	 * Metodo para definir el submodulo de domicilio de la pareja
	 *
	 * @param
	 * @return objeto ModuloValidarDTO con las validaciones
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 04/11/2019
	 */
	public static ModuloValidarDTO crearDomicilioDepEcon(DomicilioDepEconDTO domicilioDepEcon) {
		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(EnumCampos.DOMICILIO_DEP_ECON.getCampo());

		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		if(domicilioDepEcon.getLugarDondeReside().equals(EnumLugarReside.SE_DESCONOCE)) {
			propiedades.add(PropBase.crearModuloDebeSerNulo(domicilioDepEcon.getDomicilio(),
					EnumCampos.DOMICILIO.getCampo()));
		} else {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(domicilioDepEcon.getDomicilio(),
					EnumCampos.DOMICILIO.getCampo()));
			if (domicilioDepEcon.getDomicilio() != null) {
				modulos.add(ValDomicilio.crearDomicilio(domicilioDepEcon.getDomicilio(), EnumDomicilio.DOMICILIO.getNombre()));
			}
		}
		moduloValidar.setListPropsTovalidate(propiedades);
		moduloValidar.setListModuloshijos(modulos);
		return moduloValidar;
	}

}
