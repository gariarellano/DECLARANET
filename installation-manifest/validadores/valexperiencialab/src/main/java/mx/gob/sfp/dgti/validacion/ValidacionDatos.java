package mx.gob.sfp.dgti.validacion;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumGeneral;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.dto.ExperienciaLaboralDTO;
import mx.gob.sfp.dgti.dto.ExperienciasLaboralesDTO;
import mx.gob.sfp.dgti.util.EnumExperienciaLab;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValActividadLab;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;

/**
 * Clase de validaciones para el módulo de experiencia laboral de la declaración
 * 
 * @author Miriam Sánchez Sánchez programador07
 * @since 31/10/2019
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
	 * @param ExperienciaLaboralDTO Módulo de experiencia laboral
	 * @return future
	 */
	public Future<ModuloDTO> obtenerValidaciones(ExperienciasLaboralesDTO datos, Vertx vertx) {
		Future<ModuloDTO> future = Future.future();
		ModuloDTO modulo = new ModuloDTO(EnumModulo.I_EXPERIENCIA_LABORAL.getModulo());

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
	 * Se genera el módulo de experiencia laboral, sus propiedades y submódulos con sus
	 * validaciones correspondientes
	 * 
	 * @param ExperienciaLaboralDTO Módulo de experiencia laboral
	 * @return ModuloValidarDTO
	 */
	private ModuloValidarDTO obtenerModulo(ExperienciasLaboralesDTO experienciaLaboral, ModuloDTO moduloDTO) {

		ModuloValidarDTO modulo = new ModuloValidarDTO(EnumModulo.I_EXPERIENCIA_LABORAL.getModulo());
		modulo.getListPropsTovalidate().add(PropBase.crearPropAclaraciones(experienciaLaboral.getAclaracionesObservaciones()));
		
		if(!experienciaLaboral.isNinguno()) {
			
			modulo.getListPropsTovalidate().add(
					PropBase.crearModuloDebeSerNoNulo(experienciaLaboral.getEncabezado(), EnumGeneral.ENCABEZADO.getCampo()));

			modulo.getListPropsTovalidate().add(
					PropBase.crearModuloDebeSerNoNulo(experienciaLaboral.getExperienciaLaboral(), EnumExperienciaLab.EXPERIENCIA_LABORAL.getCampo()));
			
			if(experienciaLaboral.getExperienciaLaboral() != null && experienciaLaboral.getEncabezado() != null) {
				for(ExperienciaLaboralDTO experienciaLaboralDTO: experienciaLaboral.getExperienciaLaboral()) {
					
					if(!experienciaLaboralDTO.isVerificar()) {
						moduloDTO.setIncompleto(true);
						continue;
					}
					modulo.getListModuloshijos().add(obtenerExperienciaLaboral(experienciaLaboralDTO, experienciaLaboral.getEncabezado()));
				}
			} 

		} else {
			modulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNulo(
					experienciaLaboral.getExperienciaLaboral(), EnumExperienciaLab.EXPERIENCIA_LABORAL.getCampo()));
		}

		return modulo;
	}

	private ModuloValidarDTO obtenerExperienciaLaboral(ExperienciaLaboralDTO experienciaLaboral, EncabezadoDTO encabezado) {
		ModuloValidarDTO submoduloExperienciaL = new ModuloValidarDTO(
				EnumExperienciaLab.EXPERIENCIA_LABORAL.getCampo(), experienciaLaboral.getIdPosicionVista());

		submoduloExperienciaL.getListModuloshijos().add(ValActividadLab.crearActividadLaboral(experienciaLaboral.getActividadLaboral(), encabezado));
		submoduloExperienciaL.getListPropsTovalidate().add(PropBase.crearId(experienciaLaboral.getId()));

		// validar el tipoOperacion
		PropBase.crearPropTipoOperacion(experienciaLaboral, encabezado, submoduloExperienciaL.getListPropsTovalidate());

		return submoduloExperienciaL;
	}

}
