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
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumGeneral;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.dto.DatoCurricularDTO;
import mx.gob.sfp.dgti.dto.DatosCurricularesDTO;
import mx.gob.sfp.dgti.util.EnumCurricular;
import mx.gob.sfp.dgti.util.EnumDocObtenido;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValGenerales;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

/**
 * Clase de validaciones para el módulo de datos curriculares de la declaración
 * @author Miriam Sánchez Sánchez programador07
 * @since 07/10/2019
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
	 * Se construye el módulo con propiedades y se asignan las validaciones por cada campo.
	 * @param DatosCurricularesDTO módulo de datos curriculares del funcionario público
	 * @return future
	 */
	public Future<ModuloDTO> obtenerValidaciones(DatosCurricularesDTO datos, Vertx vertx) {
		Future<ModuloDTO> future = Future.future();
		ModuloDTO modulo = new ModuloDTO(EnumModulo.I_DATOS_CURRICULARES.getModulo());
		
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
	 * Se genera el módulo de datos curriculares, sus propiedades y submódulos con sus validaciones correspondientes
	 * @param DatosCurricularesDTO módulo de datos curriculares del funcionario público
	 * @return ModuloValidarDTO
	 */
	private ModuloValidarDTO obtenerModulo(DatosCurricularesDTO datos, ModuloDTO moduloDTO) {
		ModuloValidarDTO modulo = new ModuloValidarDTO(EnumModulo.I_DATOS_CURRICULARES.getModulo());
		modulo.getListPropsTovalidate().add(PropBase.crearPropAclaraciones(datos.getAclaracionesObservaciones()));
		
		modulo.getListPropsTovalidate().add(
				PropBase.crearModuloDebeSerNoNulo(datos.getEncabezado(), EnumGeneral.ENCABEZADO.getCampo()));
		modulo.getListPropsTovalidate().add(
				PropBase.crearModuloDebeSerNoNulo(datos.getEscolaridad(), EnumCurricular.ESCOLARIDAD.getCampo()));
		
		if(datos.getEscolaridad() != null) {
			
			for(DatoCurricularDTO datoCurricular: datos.getEscolaridad()) {

				if(!datoCurricular.isVerificar()) {
					moduloDTO.setIncompleto(true);
					continue;
				}

				modulo.getListModuloshijos().add(obtenerEscolaridad(datoCurricular, datos.getEncabezado()));
			}
		} 
		
        return modulo;
	}
	
	/**
	 * Método para obtener la escolaridad
	 * @param escolaridad
	 * @return ModuloValidarDTO	
	 */
	private ModuloValidarDTO obtenerEscolaridad(DatoCurricularDTO datos, EncabezadoDTO encabezado) {
		ModuloValidarDTO submodulo = new ModuloValidarDTO(EnumCurricular.ESCOLARIDAD.getCampo(), datos.getIdPosicionVista());

		//Validacion Tipo Operacion
		PropBase.crearPropTipoOperacion(datos, encabezado, submodulo.getListPropsTovalidate());

		PropiedadesValidarDTO institucionEducativa = new PropiedadesValidarDTO(EnumCurricular.INSTITUCION_EDUCATIVA.getCampo(), 
        		datos.getInstitucionEducativa(), ValGenerales.validacionesCadenaAlfa(EnumCantidad.CIENTO_UNO.getId()), true);
        
        PropiedadesValidarDTO carrera = new PropiedadesValidarDTO(EnumCurricular.CARRERA_AREA.getCampo(), 
        		datos.getCarreraAreaConocimiento(), ValGenerales.validacionesCadena(EnumCantidad.CIENTO_UNO.getId()), true);
        submodulo.getListPropsTovalidate().add(carrera);
        submodulo.getListPropsTovalidate().add(institucionEducativa);
        submodulo.getListPropsTovalidate().add(PropBase.crearId(datos.getId()));
        submodulo.getListPropsTovalidate().add(PropBase.crearIdPosicionVista(datos.getIdPosicionVista()));

		// validar el tipoOperacion
		PropBase.crearPropTipoOperacion(datos, encabezado, submodulo.getListPropsTovalidate());
        
        submodulo.getListPropsTovalidate().add(
        		PropBase.crearObligatoria(datos.getUbicacion(), EnumCurricular.UBICACION.getCampo()));
        
        submodulo.getListPropsTovalidate().add(
        		PropBase.crearObligatoria(datos.getEstatus(), EnumCurricular.ESTATUS.getCampo()));
        
        submodulo.getListPropsTovalidate().add(PropBase.crearPropCatalogo(
        		EnumCurricular.NIVEL.getCampo(), EnumCatalogos.CAT_NIVEL_ACADEMICO.name(),
        		datos.getNivel(), true
        ));
        
        obtenerDocumentoObt(submodulo, datos, encabezado, EnumCurricular.DOCUMENTO_OBTENIDO.getCampo());

        return submodulo;
	}

	/**
	 * Obtener las propiedades del documento obtenido de escolaridad
	 * @param datos
	 * @param nombre
	 * @return
	 */
	private ModuloValidarDTO obtenerDocumentoObt(ModuloValidarDTO modulo, DatoCurricularDTO datos, EncabezadoDTO encabezado, String nombre) {
		
		if(datos.getDocumentoObtenido() != null) {
			ModuloValidarDTO submodulo = new ModuloValidarDTO(nombre);

			PropiedadesValidarDTO fechaObtencionDoc = new PropiedadesValidarDTO(EnumDocObtenido.FECHA_OBTENCION.getCampo(), 
	        		datos.getDocumentoObtenido().getFechaObtencion(), ValGenerales.validacionesFecha(), true);

			submodulo.getListPropsTovalidate().add(fechaObtencionDoc);
			submodulo.getListPropsTovalidate().add(
					PropBase.crearObligatoria(datos.getDocumentoObtenido().getTipo(), EnumDocObtenido.TIPO.getCampo()));
			
			if(encabezado != null) {
				submodulo.getListPropsTovalidate().add(PropBase.crearValidacionFechaContraFechaEncargo(
						datos.getDocumentoObtenido().getFechaObtencion(), encabezado, EnumDocObtenido.FECHA_OBTENCION.getCampo(), true));
			}
			
			if(datos.getDocumentoObtenido() != null && datos.getEstatus() != null && datos.getNivel() != null) {
			    List<ParametrosValicacionDTO> parametros = new ArrayList<>();
		        parametros.add(new ParametrosValicacionDTO(EnumValidacion.NIVEL_ACADEMICO_DOC, datos.getEstatus().getId(), datos.getNivel()));
	
				submodulo.getListPropsTovalidate().add(new PropiedadesValidarDTO(EnumDocObtenido.TIPO.getCampo(),
								datos.getDocumentoObtenido().getTipo().getId(), parametros));
			}
			modulo.getListModuloshijos().add(submodulo);
			
		} else {
			modulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(datos.getDocumentoObtenido(), nombre));
		}
		
		return modulo;
	}
	
	
}
