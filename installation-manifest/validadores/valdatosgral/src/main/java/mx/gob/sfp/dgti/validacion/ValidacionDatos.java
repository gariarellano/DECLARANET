package mx.gob.sfp.dgti.validacion;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.declaracion.dto.general.DatosGeneralesDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.util.EnumDatosG;
import mx.gob.sfp.dgti.util.EnumEstadoCivil;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValCorreo;
import mx.gob.sfp.dgti.utils.validaciones.ValPersona;
import mx.gob.sfp.dgti.utils.validaciones.ValTelefono;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;

/**
 * Clase de validaciones para el módulo de datos generales de la declaración
 * 
 * @author Miriam Sánchez Sánchez programador07
 * @since 19/09/2019
 */
public class ValidacionDatos {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidacionDatos.class);
	
	private final static String NOMBRE_URL_CATALOGOS = "URL_CATALOGOS";
	private final static String URL_CATALOGOS = System.getenv(NOMBRE_URL_CATALOGOS);
	private WebClient client;

	public ValidacionDatos(Vertx vertx) {
		client = WebClient.create(vertx);
	}


	/**
	 * Se construye el módulo con propiedades y se asignan las validaciones por c/u.
	 * 
	 * @param DatosGeneralesDTO Módulo de datos generales
	 * @return future
	 */
	public Future<ModuloDTO> obtenerValidaciones(DatosGeneralesDTO datos, Vertx vertx) {
		Future<ModuloDTO> future = Future.future();
		ModuloDTO modulo = new ModuloDTO(EnumModulo.I_DATOS_GENERALES.getModulo());

		new ExectValidations(client, URL_CATALOGOS).ejecutarValidacionesRx(
				obtenerModulo(datos, modulo), modulo
		).doOnComplete(() -> {
			LOGGER.info("=== doOnComplete()");
			future.complete(modulo);

		}).doOnError(e -> {
			LOGGER.info("=== doOnError()");
			future.fail("error");
		})
		.subscribe();

		return future;
	}

	/**
	 * Se genera el módulo de datos generales, sus propiedades y submódulos con sus
	 * validaciones correspondientes
	 * 
	 * @param DatosGeneralesDTO Módulo de datos generales
	 * @return ModuloValidarDTO
	 */
	private ModuloValidarDTO obtenerModulo(DatosGeneralesDTO datos, ModuloDTO moduloDTO) {
		
		ModuloValidarDTO modulo = new ModuloValidarDTO(EnumModulo.I_DATOS_GENERALES.getModulo());
		modulo.getListPropsTovalidate().add(PropBase.crearPropAclaraciones(datos.getAclaracionesObservaciones()));
		
		modulo.getListModuloshijos().add(ValPersona.crearDatosPersonales(
						datos.getDatosPersonales(), EnumDatosG.DATOS_PERSONALES.getCampo()));
		modulo.getListModuloshijos().add(ValTelefono.obtenerTelefonoCelular(
						datos.getTelefonoCelular(), EnumDatosG.TELEFONO_CELULAR.getCampo(), true));
		modulo.getListModuloshijos().add(ValTelefono.obtenerTelefonoCasa(
						datos.getTelefonoCasa(), EnumDatosG.TELEFONO_CASA.getCampo(), true));
		modulo.getListModuloshijos().add(ValCorreo.obtenerCorreo(
						datos.getCorreoElectronico(), EnumDatosG.CORREO_ELECTRONICO.getCampo(), true, true));

        modulo.getListPropsTovalidate().add(PropBase.crearPropCatalogo(
                EnumDatosG.ESTADO_CIVIL.getCampo(), EnumCatalogos.CAT_ESTADO_CIVIL.name(),
                datos.getSituacionPersonalEstadoCivil(), true
        ));
        modulo.getListPropsTovalidate().add(PropBase.crearPropCatalogo(
                EnumDatosG.NACIONALIDAD.getCampo(), EnumCatalogos.CAT_NACIONALIDAD.name(),
                datos.getNacionalidad(), true
        ));
        modulo.getListPropsTovalidate().add(PropBase.crearPropCatalogo(
                EnumDatosG.PAIS_NACIMIENTO.getCampo(), EnumCatalogos.CAT_PAIS.name(),
                datos.getPaisNacimiento(), true
        ));

		if(EnumEstadoCivil.CASADO.getId() == datos.getSituacionPersonalEstadoCivil().getId()) {

			PropBase.crearPropCatalogoOtro(
					EnumDatosG.REGIMEN_MATRIMONIAL.getCampo(), EnumCatalogos.CAT_REGIMEN_MATRIMONIAL.name(),
					datos.getRegimenMatrimonial(), false,
					datos.getRegimenMatrimonialOtro(), modulo.getListPropsTovalidate()
			);
	        
		} else {
			modulo.getListPropsTovalidate().add(
					PropBase.crearPropDebeSerNulo(datos.getRegimenMatrimonial(),  EnumDatosG.REGIMEN_MATRIMONIAL.getCampo()));
		}
		if(!datos.isVerificar()) {
			moduloDTO.setIncompleto(true);
		}

		return modulo;
	}

	

}
