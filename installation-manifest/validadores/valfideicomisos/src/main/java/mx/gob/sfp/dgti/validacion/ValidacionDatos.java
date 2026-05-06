package mx.gob.sfp.dgti.validacion;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.dto.FideicomisoDTO;
import mx.gob.sfp.dgti.dto.ParticipacionFideicomisoDTO;
import mx.gob.sfp.dgti.util.EnumFideicomiso;
import mx.gob.sfp.dgti.util.EnumParticipacionFid;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.propiedades.PropPersona;
import mx.gob.sfp.dgti.utils.validaciones.ValPersona;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;

/**
 * Clase de validaciones para el módulo de fideicomisos
 * 
 * @author Miriam Sánchez Sánchez programador07
 * @since 22/11/2019
 */
public class ValidacionDatos {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidacionDatos.class);
	private static final int RFC_PM = 12;
	private final static String NOMBRE_URL_CATALOGOS = "URL_CATALOGOS";
	private final static String URL_CATALOGOS = System.getenv(NOMBRE_URL_CATALOGOS);
	private WebClient client;

	public ValidacionDatos(Vertx vertx) {
		client = WebClient.create(vertx);
	}
	
	/**
	 * Se construye el módulo con propiedades y se asignan las validaciones por c/u.
	 * @param ParticipacionFideicomisoDTO Módulo de fideicomisos
	 * @return future
	 */
	public Future<ModuloDTO> obtenerValidaciones(ParticipacionFideicomisoDTO datos, Vertx vertx) {
		Future<ModuloDTO> future = Future.future();
		ModuloDTO modulo = new ModuloDTO(EnumModulo.II_FIDEICOMISOS.getModulo());

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
	 * Se genera el módulo de fideicomisos del declarante
	 * validaciones correspondientes
	 * 
	 * @param datos Módulo de fideicomisos
	 * @return ModuloValidarDTO
	 */
	private ModuloValidarDTO obtenerModulo(ParticipacionFideicomisoDTO datos, ModuloDTO moduloDTO) {
		ModuloValidarDTO modulo = new ModuloValidarDTO(EnumModulo.II_FIDEICOMISOS.getModulo());
		modulo.getListPropsTovalidate().add(PropBase.crearPropAclaraciones(datos.getAclaracionesObservaciones()));
		
		if(!datos.isNinguno()) {
			
			if(datos.getFideicomisos() != null) {
				for(FideicomisoDTO fideicomiso: datos.getFideicomisos()) {
					
					if(!fideicomiso.isVerificar()) {
						moduloDTO.setIncompleto(true);
						continue;
					}
					
					modulo.getListModuloshijos().add(obtenerFideicomiso(fideicomiso, datos.getEncabezado()));
				}
			}
			
		} else {
			modulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNulo(
					datos.getFideicomisos(), EnumParticipacionFid.FIDEICOMISO.getCampo()));
		}
        return modulo;
	}

	private ModuloValidarDTO obtenerFideicomiso(FideicomisoDTO fideicomiso, EncabezadoDTO encabezado) {
		ModuloValidarDTO modulo = new ModuloValidarDTO(EnumParticipacionFid.FIDEICOMISO.getCampo());
		
		modulo.getListPropsTovalidate().add(PropBase.crearId(fideicomiso.getId()));

		//Agregar validacion de tipoOperacion
		PropBase.crearPropTipoOperacion(fideicomiso, encabezado, modulo.getListPropsTovalidate());

		modulo.getListPropsTovalidate().add(
				PropBase.crearObligatoria(fideicomiso.getLocalizacion(), EnumFideicomiso.LOCALIZACION.getCampo()));
		modulo.getListPropsTovalidate().add(
				PropBase.crearObligatoria(fideicomiso.getParticipante(), EnumFideicomiso.PARTICIPANTE.getCampo()));
		modulo.getListPropsTovalidate().add(
				PropBase.crearObligatoria(fideicomiso.getTipoFideicomiso(), EnumFideicomiso.TIPO_FIDEICOMISO.getCampo()));
		modulo.getListPropsTovalidate().add(
				PropBase.crearObligatoria(fideicomiso.getTipoParticipacion(), EnumFideicomiso.TIPO_PARTICIPACION.getCampo()));

		modulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(
				fideicomiso.getFideicomisario(), EnumFideicomiso.FIDEICOMISARIO.getCampo()));
		modulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(
				fideicomiso.getFideicomitente(), EnumFideicomiso.FIDEICOMITENTE.getCampo()));
		modulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(
				fideicomiso.getFiduciario(), EnumFideicomiso.FIDUCIARIO.getCampo()));
		
		if(fideicomiso.getRfcFideicomiso() != null && !fideicomiso.getRfcFideicomiso().isEmpty()) {
			
			if(fideicomiso.getRfcFideicomiso().length() == RFC_PM) {
				modulo.getListPropsTovalidate().add(
						PropPersona.crearPropRfcPersMoral(fideicomiso.getRfcFideicomiso(), EnumFideicomiso.RFC_FIDEICOMISO.getCampo()));
			} else {
				modulo.getListPropsTovalidate().add(
						PropPersona.crearPropRfc(fideicomiso.getRfcFideicomiso(), EnumFideicomiso.RFC_FIDEICOMISO.getCampo()));
			}
		}
		
		if(fideicomiso.getFideicomisario() != null) {
			modulo.getListModuloshijos().add(
					ValPersona.crearPersona(fideicomiso.getFideicomisario(), EnumFideicomiso.FIDEICOMISARIO.getCampo()));
		}
		
		if(fideicomiso.getFideicomitente() != null) {
			modulo.getListModuloshijos().add(
					ValPersona.crearPersona(fideicomiso.getFideicomitente(), EnumFideicomiso.FIDEICOMITENTE.getCampo()));
		}
		
		if(fideicomiso.getFiduciario() != null) {
			modulo.getListModuloshijos().add(
					ValPersona.crearPersonaMoral(fideicomiso.getFiduciario(), EnumFideicomiso.FIDUCIARIO.getCampo(), false));
		}
		
		PropBase.crearPropCatalogoOtro(
        		EnumFideicomiso.SECTOR.getCampo(), EnumCatalogos.CAT_SECTOR_PRIVADO.name(),
        		fideicomiso.getSector(), true,
				fideicomiso.getSectorOtro(), modulo.getListPropsTovalidate()
        );
		
		return modulo;
	}
	
}
