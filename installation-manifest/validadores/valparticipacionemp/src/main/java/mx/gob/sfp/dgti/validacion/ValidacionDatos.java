package mx.gob.sfp.dgti.validacion;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCantidad;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumParticipacion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.dto.ParticipacionEmpDTO;
import mx.gob.sfp.dgti.dto.ParticipacionEmpresasDTO;
import mx.gob.sfp.dgti.util.EnumParticipacionE;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValDomicilio;
import mx.gob.sfp.dgti.utils.validaciones.ValMonto;
import mx.gob.sfp.dgti.utils.validaciones.ValPersona;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

/**
 * Clase de validaciones para el módulo participacion en empresas
 * 
 * @author Miriam Sánchez Sánchez programador07
 * @since 27/11/2019
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
	 * @param ParticipacionEmpresasDTO Módulo de participacion en empresas
	 * @return future
	 */
	public Future<ModuloDTO> obtenerValidaciones(ParticipacionEmpresasDTO datos, Vertx vertx) {
		Future<ModuloDTO> future = Future.future();
		ModuloDTO modulo = new ModuloDTO(EnumModulo.II_PARTICIPACION_EMPRESAS.getModulo());

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
	 * Se genera el módulo de participacion en empresas
	 * validaciones correspondientes
	 * 
	 * @param datos Módulo de participacion en empresas
	 * @return ModuloValidarDTO
	 */
	private ModuloValidarDTO obtenerModulo(ParticipacionEmpresasDTO datos, ModuloDTO moduloDTO) {
		ModuloValidarDTO modulo = new ModuloValidarDTO(EnumModulo.II_PARTICIPACION_EMPRESAS.getModulo());
		modulo.getListPropsTovalidate().add(PropBase.crearPropAclaraciones(datos.getAclaracionesObservaciones()));
		
		if(!datos.isNinguno()) {
			
			modulo.getListPropsTovalidate().add(
					PropBase.crearModuloDebeSerNoNulo(datos.getParticipaciones(), EnumParticipacion.PARTICIPACIONES.getCampo()));
			
			if(datos.getParticipaciones() != null) {
				for(ParticipacionEmpDTO participacion: datos.getParticipaciones()) {
					
					if(!participacion.isVerificar()) {
						moduloDTO.setIncompleto(true);
						continue;
					}
					
					modulo.getListModuloshijos().add(obtenerParticipacion(participacion, datos.getEncabezado()));
				}
			}
			
		} else {
			modulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNulo(
					datos.getParticipaciones(), EnumParticipacion.PARTICIPACIONES.getCampo()));
		}
        return modulo;
	}
	
	/**
	 * Método para obtener la participacion
	 * @param participacion
	 * @param encabezado
	 * @return ModuloValidarDTO	
	 */
	private ModuloValidarDTO obtenerParticipacion(ParticipacionEmpDTO participacion, EncabezadoDTO encabezado) {
		ModuloValidarDTO modulo = new ModuloValidarDTO(EnumParticipacion.PARTICIPACIONES.getCampo());

		//Agregar validacion de tipoOperacion
		PropBase.crearPropTipoOperacion(participacion, encabezado, modulo.getListPropsTovalidate());

		PropiedadesValidarDTO porcentaje = new PropiedadesValidarDTO(EnumParticipacionE.PORCENTAJE_PART.getCampo(), 
        		participacion.getPorcentajeParticipacion(), 
        		validacionesPorcentaje(EnumCantidad.CERO.getId(), EnumCantidad.CIENTO_UNO.getId()), true);
		
		modulo.getListPropsTovalidate().add(porcentaje);
		modulo.getListPropsTovalidate().add(PropBase.crearId(participacion.getId()));
		modulo.getListPropsTovalidate().add(PropBase.crearObligatoria(
				participacion.getParticipante(), EnumParticipacionE.PARTICIPANTE.getCampo()));
		
		modulo.getListPropsTovalidate().add(PropBase.crearObligatoria(
				participacion.isRecibeRemuneracion(), EnumParticipacionE.RECIBE_REMUNERACION.getCampo()));
		
		modulo.getListPropsTovalidate().add(
				PropBase.crearModuloDebeSerNoNulo(participacion.getNombreEmpresaSociedadAsociacion(), EnumParticipacionE.NOMBRE_EMPRESA.getCampo()));
		
		modulo.getListPropsTovalidate().add(
				PropBase.crearModuloDebeSerNoNulo(participacion.getLocalizacion(), EnumParticipacionE.LOCALIZACION.getCampo()));
		
		modulo.getListPropsTovalidate().add(
				PropBase.crearModuloDebeSerNoNulo(participacion.getSector(), EnumParticipacionE.SECTOR.getCampo()));
		
		modulo.getListPropsTovalidate().add(
				PropBase.crearModuloDebeSerNoNulo(participacion.getTipoParticipacion(), EnumParticipacionE.TIPO_PARTICIPACION.getCampo()));
		
		if(participacion.getNombreEmpresaSociedadAsociacion() != null) {
			modulo.getListModuloshijos().add(ValPersona.crearPersonaMoral(
					participacion.getNombreEmpresaSociedadAsociacion(), EnumParticipacionE.NOMBRE_EMPRESA.getCampo(), false));
		}
		
		if(participacion.isRecibeRemuneracion()) {
			
			modulo.getListPropsTovalidate().add(
					PropBase.crearModuloDebeSerNoNulo(participacion.getMontoMensual(), EnumParticipacionE.MONTO_MENSUAL.getCampo()));
			
			if(participacion.getMontoMensual() != null) {
				modulo.getListModuloshijos().add(
						ValMonto.crearMonto(participacion.getMontoMensual(), EnumParticipacionE.MONTO_MENSUAL.getCampo()));
			}
		
		} else {
			modulo.getListPropsTovalidate().add(
					PropBase.crearModuloDebeSerNulo(participacion.getMontoMensual(), EnumParticipacionE.MONTO_MENSUAL.getCampo()));
		}
		
		if(participacion.getLocalizacion() != null) {
			modulo.getListModuloshijos().add(
					ValDomicilio.crearLocalizacion(participacion.getLocalizacion(), EnumParticipacionE.LOCALIZACION.getCampo()));
		}
		
		PropBase.crearPropCatalogoOtro(
             EnumParticipacionE.TIPO_PARTICIPACION.getCampo(), EnumCatalogos.CAT_TIPO_PARTICIPACION.name(),
             participacion.getTipoParticipacion(), true,
				participacion.getTipoParticipacionOtro(), modulo.getListPropsTovalidate()
		);
		 
		PropBase.crearPropCatalogoOtro(
			 EnumParticipacionE.SECTOR.getCampo(), EnumCatalogos.CAT_SECTOR_PRIVADO.name(),
             participacion.getSector(), true,
				participacion.getSectorOtro(), modulo.getListPropsTovalidate()
	    );
		
		return modulo;
	}

	/**
	 * Validaciones para el porcentaje entre 0 y 100
	 * @param rangoMin
	 * @param rangoMax
	 * @return
	 */
	private List<ParametrosValicacionDTO> validacionesPorcentaje(Integer rangoMin, Integer rangoMax) {
		List<ParametrosValicacionDTO> validaciones = new ArrayList<>();
		validaciones.add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_MAYORIGUALQ_MENORIGUALQ, rangoMax, rangoMin));
		return validaciones;
	}
	
}
