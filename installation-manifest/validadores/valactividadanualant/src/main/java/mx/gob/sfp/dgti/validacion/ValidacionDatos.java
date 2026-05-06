package mx.gob.sfp.dgti.validacion;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.ActividadAnualDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumActividadAnualAnt;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumGeneral;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumIngresoNetoD;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoFormatoDeclaracion;
import mx.gob.sfp.dgti.dto.ActividadAnualAnteriorDTO;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValIngresosNetos;
import mx.gob.sfp.dgti.utils.validaciones.ValRemuneracion;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

/**
 * Clase de validaciones para el módulo de actividad anual anterior
 * 
 * @author Miriam Sánchez Sánchez programador07
 * @since 06/11/2019
 */
public class ValidacionDatos {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidacionDatos.class);
	private static final String INICIO_ANIO = "-01-01";
	private static final String TERMINO_ANIO = "-12-31";
	private static final String NOMBRE_URL_CATALOGOS = "URL_CATALOGOS";
	private static final String URL_CATALOGOS = System.getenv(NOMBRE_URL_CATALOGOS);
	private WebClient client;

	public ValidacionDatos(Vertx vertx) {
		client = WebClient.create(vertx);
	}

	
	/**
	 * Se construye el módulo con propiedades y se asignan las validaciones por c/u.
	 * 
	 * @param ActividadAnualAnteriorDTO Módulo de actividad anual anterior
	 * @return future
	 */
	public Future<ModuloDTO> obtenerValidaciones(ActividadAnualAnteriorDTO datos, Vertx vertx) {
		Future<ModuloDTO> future = Future.future();
		ModuloDTO modulo = new ModuloDTO(EnumModulo.I_ACTIVIDAD_ANUAL_ANT.getModulo());
		
		new ExectValidations(client, URL_CATALOGOS).ejecutarValidacionesRx(
				obtenerModulo(datos), modulo
		).doOnComplete(() -> {
			future.complete(modulo);

		}).doOnError(e -> {
			LOGGER.info("=== doOnError()");
			future.fail("error");
		})
		.subscribe();


		return future;
	}
	
	private ModuloValidarDTO obtenerModulo(ActividadAnualAnteriorDTO actividad) {

		ModuloValidarDTO modulo = new ModuloValidarDTO(EnumModulo.I_ACTIVIDAD_ANUAL_ANT.getModulo());
		
		modulo.getListPropsTovalidate().add(
				PropBase.crearModuloDebeSerNoNulo(actividad.getEncabezado(), EnumGeneral.ENCABEZADO.getCampo()));
		
		if(actividad.getEncabezado() != null) {
			if(EnumTipoDeclaracion.MODIFICACION != actividad.getEncabezado().getTipoDeclaracion()) {
				modulo.getListPropsTovalidate().add(PropBase.crearPropAclaraciones(actividad.getAclaracionesObservaciones()));
				
				if(actividad.isServidorPublicoAnioAnterior()) {
					
					if(actividad.getActividadAnual() != null) {
						modulo.getListModuloshijos().add(obtenerActividadAA(actividad.getActividadAnual(), actividad.getEncabezado()));
							
					} else {
						modulo.getListPropsTovalidate().add(new PropiedadesValidarDTO(
								EnumActividadAnualAnt.ACTIVIDAD_ANUAL.getCampo(), actividad.getActividadAnual(), true));	
					}
					
				} else {
					modulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNulo(
							actividad.getActividadAnual(), EnumActividadAnualAnt.ACTIVIDAD_ANUAL.getCampo()));
				}
				
			} else {
				modulo.getListPropsTovalidate().add(
						validacionTipoDeclaracion(EnumTipoDeclaracion.MODIFICACION, EnumModulo.I_ACTIVIDAD_ANUAL_ANT.getModulo()));
			}
		}

		return modulo;
	}

	/**
	 * Se genera el módulo de actividad Anual Anterior, sus propiedades y submódulos con sus
	 * validaciones correspondientes
	 * 
	 * @param ExperienciaLaboralDTO Módulo de actividad Anual Anterior
	 * @return ModuloValidarDTO
	 */
	private ModuloValidarDTO obtenerActividadAA(ActividadAnualDTO actividadA, EncabezadoDTO encabezado) {
		ModuloValidarDTO modulo = new ModuloValidarDTO(EnumActividadAnualAnt.ACTIVIDAD_ANUAL.getCampo());
		
		modulo.getListPropsTovalidate().add(
				this.validacionFechaContraFechaEncargo(actividadA.getFechaInicio(), encabezado, EnumActividadAnualAnt.FECHA_INICIO.getCampo()));
		
		modulo.getListPropsTovalidate().add(
				this.validacionFechaContraFechaEncargo(actividadA.getFechaConclusion(), encabezado, EnumActividadAnualAnt.FECHA_CONCLUSION.getCampo()));
		
		modulo.getListPropsTovalidate().add(
				this.validacionesFecha(actividadA.getFechaInicio(), actividadA.getFechaConclusion(), EnumActividadAnualAnt.FECHA_INICIO.getCampo()));
		
		modulo.getListPropsTovalidate().add(
				PropBase.crearObligatoria(actividadA.getTipoRemuneracion(), EnumIngresoNetoD.TIPO_REMUNERACION.getCampo()));
		
		ValRemuneracion.obtenerMontoMonedaRem(modulo, actividadA.getRemuneracionNetaCargoPublico(), EnumIngresoNetoD.REMUNERACION_NETA_CP.getCampo());
		ValRemuneracion.obtenerMontoMonedaRem(modulo, actividadA.getOtrosIngresosTotal(), EnumIngresoNetoD.OTROS_INGRESOS_T.getCampo());
		
		ValIngresosNetos.obtenerObjetoRemuneracion(modulo, actividadA.getIngresoNetoDeclarante(), EnumIngresoNetoD.INGRESO_NETO_D.getCampo());
		
		if(EnumTipoFormatoDeclaracion.COMPLETO.equals(encabezado.getTipoFormato())) {
			ValIngresosNetos.obtenerObjetoRemuneracion(modulo, actividadA.getIngresoNetoParejaDependiente(), EnumIngresoNetoD.INGRESO_NETO_PAREJA_D.getCampo());
			ValIngresosNetos.obtenerObjetoRemuneracion(modulo, actividadA.getTotalIngresosNetos(), EnumIngresoNetoD.TOTAL_INGRESOS_NETOS.getCampo());
		
		} else {
			modulo.getListPropsTovalidate().add(
					PropBase.crearModuloDebeSerNulo(actividadA.getIngresoNetoParejaDependiente(), EnumIngresoNetoD.INGRESO_NETO_PAREJA_D.getCampo()));
			modulo.getListPropsTovalidate().add(
					PropBase.crearModuloDebeSerNulo(actividadA.getTotalIngresosNetos(), EnumIngresoNetoD.TOTAL_INGRESOS_NETOS.getCampo()));
		}
		
		ValIngresosNetos.obtenerActividadIndustrialCE(actividadA.getActividadIndustrialComercialEmpresarial(), modulo);
		ValIngresosNetos.obtenerActividadFinanciera(actividadA.getActividadFinanciera(), modulo);
		ValIngresosNetos.obtenerServiciosProf(actividadA.getServiciosProfesionales(), modulo);
		ValIngresosNetos.obtenerOtrosIngresos(actividadA.getOtrosIngresos(), modulo);
		ValIngresosNetos.obtenerEnajenacionB(actividadA.getEnajenacionBienes(), modulo);
		
		// Todas las listas ActICE, ActFin, serv prof, enajenacion, otros ingresos
		ValIngresosNetos.validarOtrosIngresosTotal(modulo, actividadA); 
		//suma del numeral 1 y 11 remuneracion neta cargo pub + otros ingresos del declarante
		ValIngresosNetos.sumaMontos(modulo, actividadA.getRemuneracionNetaCargoPublico(), actividadA.getOtrosIngresosTotal(), 
				actividadA.getIngresoNetoDeclarante(), EnumIngresoNetoD.INGRESO_NETO_D.getCampo());
		// suma ingreso neto declarante + ingreso neto de la pareja
		
		ValIngresosNetos.validarTotalIngresosNtos(modulo, actividadA.getIngresoNetoDeclarante(), 
				actividadA.getIngresoNetoParejaDependiente(), actividadA.getTotalIngresosNetos()); 
		
		return modulo;
	}
	
	 /**
     * Se genera la lista con validaciones para fecha contra fecha de encargo
     * @param fecha fecha que se va a validar
     * @param encabezado informacion sobre la declaracion necesaria para hacer estas validaciones
     * @param nombre nombre del campo de la fecha
     * @return PropiedadesValidarDTO
     */
    private PropiedadesValidarDTO validacionFechaContraFechaEncargo(String fechaInicio, EncabezadoDTO encabezado, String nombre){
    	Integer anioAnterior = encabezado.getAnio() -1;
        List<ParametrosValicacionDTO> parametros = new ArrayList<>();

        parametros.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_FORMATO));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_MAYORIGUAL_MENORIGUAL_QUE, (anioAnterior + INICIO_ANIO), (anioAnterior + TERMINO_ANIO)));

        return new PropiedadesValidarDTO(nombre, fechaInicio, parametros, true);
    }
    
    private PropiedadesValidarDTO validacionTipoDeclaracion(EnumTipoDeclaracion tipoDeclaracion, String nombre) {
        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.TIPO_DECLARACION, nombre));

        return new PropiedadesValidarDTO(nombre, tipoDeclaracion, parametros, true);
    }

	
	/**
	 * Se genera la lista de validaciones para las fechas de actividad anual anterior
	 * @param fechaConclusion
	 * @return List<ParametrosValicacionDTO>
	 */
	private PropiedadesValidarDTO validacionesFecha(String fechaInicio, String fechaConclusion, String nombre) {
		List<ParametrosValicacionDTO> validaciones = new ArrayList<>();
		validaciones.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_MENORQUE, fechaConclusion));
		return new PropiedadesValidarDTO(nombre, fechaInicio, validaciones, true);
	}

}
