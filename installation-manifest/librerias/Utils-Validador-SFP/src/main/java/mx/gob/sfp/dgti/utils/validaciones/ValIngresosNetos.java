/**
 * @ValIngresosNetos.java Nov 14, 2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.utils.validaciones;

import java.util.ArrayList;
import java.util.List;

import mx.gob.sfp.dgti.declaracion.dto.general.ActividadAnualDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.IngresosNetosDeclaranteDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.ActividadFinancieraDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.ActividadIndustrialCEDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.EnajenacionBienDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.MontoMonedaDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.OtrosIngresosDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.RemuneracionDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.ServiciosProfesionalesDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumActividadAnualAnt;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumIngresoNetoD;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumRemuneracion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoFormatoDeclaracion;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

/**
 * Validaciones para la parte de ingresos netos
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 14, 2019
 */

public class ValIngresosNetos {

	public static ModuloValidarDTO obtenerIngresosN(ModuloValidarDTO modulo, IngresosNetosDeclaranteDTO ingresos) {
		
		modulo.getListPropsTovalidate().add(
				PropBase.crearObligatoria(ingresos.getTipoRemuneracion(), EnumIngresoNetoD.TIPO_REMUNERACION.getCampo()));
		
		ValRemuneracion.obtenerMontoMonedaRem(modulo, ingresos.getRemuneracionNetaCargoPublico(), EnumIngresoNetoD.REMUNERACION_NETA_CP.getCampo());
		ValRemuneracion.obtenerMontoMonedaRem(modulo, ingresos.getOtrosIngresosTotal(), EnumIngresoNetoD.OTROS_INGRESOS_T.getCampo());
		
		obtenerObjetoRemuneracion(modulo, ingresos.getIngresoNetoDeclarante(), EnumIngresoNetoD.INGRESO_NETO_D.getCampo());
		
		if(EnumTipoFormatoDeclaracion.COMPLETO.equals(ingresos.getEncabezado().getTipoFormato())) {
			
			obtenerObjetoRemuneracion(modulo, ingresos.getIngresoNetoParejaDependiente(), EnumIngresoNetoD.INGRESO_NETO_PAREJA_D.getCampo());
			obtenerObjetoRemuneracion(modulo, ingresos.getTotalIngresosNetos(), EnumIngresoNetoD.TOTAL_INGRESOS_NETOS.getCampo());
		
		} else {
			modulo.getListPropsTovalidate().add(
					PropBase.crearModuloDebeSerNulo(ingresos.getIngresoNetoParejaDependiente(), EnumIngresoNetoD.INGRESO_NETO_PAREJA_D.getCampo()));
			modulo.getListPropsTovalidate().add(
					PropBase.crearModuloDebeSerNulo(ingresos.getTotalIngresosNetos(), EnumIngresoNetoD.TOTAL_INGRESOS_NETOS.getCampo()));
		}
		
		obtenerActividadIndustrialCE(ingresos.getActividadIndustrialComercialEmpresarial(), modulo);
		obtenerActividadFinanciera(ingresos.getActividadFinanciera(), modulo);
		obtenerServiciosProf(ingresos.getServiciosProfesionales(), modulo);
		obtenerOtrosIngresos(ingresos.getOtrosIngresos(), modulo);
		
		if(EnumTipoDeclaracion.MODIFICACION.equals(ingresos.getEncabezado().getTipoDeclaracion()) 
				|| EnumTipoDeclaracion.CONCLUSION.equals(ingresos.getEncabezado().getTipoDeclaracion())) {
			
			modulo.getListPropsTovalidate().add(
					PropBase.crearModuloDebeSerNoNulo(ingresos.getEnajenacionBienes(), EnumIngresoNetoD.ENAJENACION_BIENES.getCampo()));
			
			if(ingresos.getEnajenacionBienes() != null) {
				obtenerEnajenacionB(ingresos.getEnajenacionBienes(), modulo);
				// Todas las listas ActICE, ActFin, serv prof,otros ingresos y enajenacion de bienes
				validarOtrosIngresosTotal(modulo, ingresos, true);
			} 
			
		} else {
			// Todas las listas ActICE, ActFin, serv prof,otros ingresos
			validarOtrosIngresosTotal(modulo, ingresos, false);
		}
		
		//suma del numeral 1 y 11 remuneracion neta cargo pub + otros ingresos del declarante
		sumaMontos(modulo, ingresos.getRemuneracionNetaCargoPublico(), ingresos.getOtrosIngresosTotal(), 
				ingresos.getIngresoNetoDeclarante(), EnumIngresoNetoD.INGRESO_NETO_D.getCampo());
		// suma ingreso neto declarante + ingreso neto de la pareja
		validarTotalIngresosNtos(modulo, ingresos.getIngresoNetoDeclarante(), 
				ingresos.getIngresoNetoParejaDependiente(), ingresos.getTotalIngresosNetos()); 

		return modulo;
	}
	
	/**
	 * Obtener los objetos que tienen como hijo remuneración 
	 * @param modulo módulo padre
	 * @param remuneracion objeto Remuneración
	 * @param nombre del objeto de remuneración
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO obtenerObjetoRemuneracion(ModuloValidarDTO modulo, RemuneracionDTO remuneracion, String nombre) {
		
		if(remuneracion != null) {
			ModuloValidarDTO submodulo = new ModuloValidarDTO(nombre);
			
			if(remuneracion.getRemuneracion() != null) {
				ValRemuneracion.obtenerMontoMonedaRem(submodulo, remuneracion.getRemuneracion(), EnumRemuneracion.REMUNERACION.getCampo());
			
			} else {
				submodulo.getListPropsTovalidate().add(
					PropBase.crearObligatoria(remuneracion.getRemuneracion(), EnumRemuneracion.REMUNERACION.getCampo()));
			}
			modulo.getListModuloshijos().add(submodulo);
		} else {
			modulo.getListPropsTovalidate().add(PropBase.crearObligatoria(remuneracion, nombre));
		}
		return modulo;
	}

	/**
	 * Obtener el submodulo de enajenación de bienes
	 * @param enajenacionBienes lista de enajenacion de bienes
	 * @param modulo modulo al cual se le agregarán los hijos de la lista de enajenacion de bienes
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO obtenerEnajenacionB(List<EnajenacionBienDTO> enajenacionBienes, ModuloValidarDTO modulo) {
		if(enajenacionBienes != null  && !enajenacionBienes.isEmpty()) {
			for(EnajenacionBienDTO enajenacionBien: enajenacionBienes) {
				modulo.getListModuloshijos().add(
						ValEnajenacionB.obtenerEnajenacionB(enajenacionBien, EnumActividadAnualAnt.ENAJENACION_BIENES.getCampo()));
			}
		} 

		modulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(
				enajenacionBienes, EnumActividadAnualAnt.ENAJENACION_BIENES.getCampo()));
		return modulo;
	}
	
	/**
	 * 
	 * Obtener el submodulo de otros ingresos
	 * @param otrosIngresos lista de otros ingresos
	 * @param modulo modulo al cual se le agregarán los hijos de la lista de otros ingresos
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO obtenerOtrosIngresos(List<OtrosIngresosDTO> otrosIngresos, ModuloValidarDTO modulo) {
		
		if(otrosIngresos != null) {
			if(!otrosIngresos.isEmpty()) {
				for(OtrosIngresosDTO otroIngreso: otrosIngresos) {
					modulo.getListModuloshijos().add(
							ValOtrosIngresos.obtenerOtroIngreso(otroIngreso, EnumIngresoNetoD.OTROS_INGRESOS.getCampo()));
				}
			}
		} 
		modulo.getListPropsTovalidate().add(PropBase.crearObligatoria(
				otrosIngresos, EnumIngresoNetoD.OTROS_INGRESOS.getCampo()));
		
		return modulo;
	}
	
	/**
	 * Obtener el submodulo de actividades industriales
	 * @param actividadesInd lista de actividades industriales
	 * @param modulo modulo al cual se le agregarán los hijos de la lista de actividades industriales
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO obtenerActividadIndustrialCE(List<ActividadIndustrialCEDTO> actividadesInd, ModuloValidarDTO modulo) {
		if(actividadesInd != null) {
			if(!actividadesInd.isEmpty()) {
				for(ActividadIndustrialCEDTO actividad: actividadesInd) {
					modulo.getListModuloshijos().add(
							ValActividadIndCE.obtenerActIndustrial(actividad, EnumIngresoNetoD.ACTIVIDAD_INDUSTRIAL_CE.getCampo()));
				}
			}
		} 
		modulo.getListPropsTovalidate().add(
				PropBase.crearObligatoria(actividadesInd, EnumIngresoNetoD.ACTIVIDAD_INDUSTRIAL_CE.getCampo()));
		
		return modulo;
	}
	
	/**
	 * Obtener el submodulo de actividades financieras
	 * @param actividadesFin lista de actividades financieras
	 * @param modulo modulo al cual se le agregarán los hijos de la lista de actividades financieras
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO obtenerActividadFinanciera(List<ActividadFinancieraDTO> actividadesFin, ModuloValidarDTO modulo) {
		
		if(actividadesFin != null && !actividadesFin.isEmpty()) {
			for(ActividadFinancieraDTO actividad: actividadesFin) {
				modulo.getListModuloshijos().add(
						ValActividadFin.obtenerActividadFin(actividad, EnumIngresoNetoD.ACTIVIDAD_FINANCIERA.getCampo()));
			}
		} 

		modulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(
				actividadesFin, EnumIngresoNetoD.ACTIVIDAD_FINANCIERA.getCampo()));
		return modulo;
	}
	
	/**
	 * Obtener el submodulo de servicios profesionales
	 * @param serviciosProf lista de los servicios profesionales
	 * @param modulo  modulo al cual se le agregarán los hijos de la lista de servicios profesionales
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO obtenerServiciosProf(List<ServiciosProfesionalesDTO> serviciosProf, ModuloValidarDTO modulo) {
		if(serviciosProf != null) {
			
			if(!serviciosProf.isEmpty()) {
				for(ServiciosProfesionalesDTO servicioP: serviciosProf) {
					modulo.getListModuloshijos().add(
								ValServiciosProf.obtenerServiciosProf(servicioP, EnumIngresoNetoD.SERVICIOS_PROFESIONALES.getCampo()));
				}
			}
		} 
		modulo.getListPropsTovalidate().add(PropBase.crearObligatoria(
				 serviciosProf,	EnumIngresoNetoD.SERVICIOS_PROFESIONALES.getCampo()));
		
		return modulo;
	}

	/**
	 * Validar total de ingresos netos del declarante 
	 * Suma @netoDecl e @ingNetoPareja deberá ser igual al @totalIng del declarante
	 * @param modulo modulo padre
	 * @param netoDecl objeto de ingreso neto del declarante
	 * @param ingNetoPareja objeto del ingreso neto de la pareja o dependiente
	 * @param totalIng objeto del total de ingresos del declarante
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO validarTotalIngresosNtos(ModuloValidarDTO modulo, RemuneracionDTO netoDecl,
			RemuneracionDTO ingNetoPareja, RemuneracionDTO totalIng) {
		
		if(netoDecl != null && ingNetoPareja != null && totalIng != null) {
			
			if(netoDecl.getRemuneracion() != null && ingNetoPareja.getRemuneracion() != null && totalIng.getRemuneracion() != null) {
				sumaMontos(modulo, netoDecl.getRemuneracion(), ingNetoPareja.getRemuneracion(), totalIng, EnumIngresoNetoD.TOTAL_INGRESOS_NETOS.getCampo());
			}
		} 
		return modulo;
	}

	/**
	 * Metodo para sumar montos y agregar la validación de la suma obtenida es igual a la cantidad de montoTotal 
	 * @param modulo modulo padre
	 * @param montoUno primer monto a sumar 
	 * @param montoDos segundo monto a sumar
	 * @param montoTotal el monto con el que se validará la suma
	 * @param nombre nombre que se le dará al campo para el mensaje de error
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO sumaMontos(ModuloValidarDTO modulo, MontoMonedaDTO montoUno, MontoMonedaDTO montoDos, RemuneracionDTO montoTotal, String nombre) {

		if(montoUno != null && montoDos != null && montoTotal != null && montoTotal.getRemuneracion() != null
				&& montoUno.getMonto() != null && montoDos.getMonto() != null) {
			long suma = 0;
			suma = suma + montoUno.getMonto() + montoDos.getMonto();
			PropiedadesValidarDTO ingresosNetos = new PropiedadesValidarDTO(nombre, suma, validacionMontos(montoTotal.getRemuneracion().getMonto()));
			modulo.getListPropsTovalidate().add(ingresosNetos);
		}
		
		return modulo;
	}

	/**
	 * Método para obtener la validación de la cantidad de otrosIngresosTotal contra la suma de las secciones II.1 al II.5  
	 * Se iteran las listas de actividadIndustrialCE, ActFinanciera, Servicios profesionales, 
	 * Enajenacion de bienes y Otros ingresos para obtener los montos declarados y la suma de ellos.
	 * @param modulo modulo padre
	 * @param actAnual el objeto de la actividad anual del declarante
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO validarOtrosIngresosTotal(ModuloValidarDTO modulo, ActividadAnualDTO actAnual) {
		if(actAnual.getActividadIndustrialComercialEmpresarial() != null
				&& actAnual.getActividadFinanciera() != null && actAnual.getServiciosProfesionales() != null
				&& actAnual.getEnajenacionBienes() != null && actAnual.getOtrosIngresos() != null) {
			
			long suma = obtenerSuma(actAnual.getActividadIndustrialComercialEmpresarial())
						+ obtenerSuma(actAnual.getActividadFinanciera())
						+ obtenerSuma(actAnual.getServiciosProfesionales())
						+ obtenerSuma(actAnual.getEnajenacionBienes())
						+ obtenerSuma(actAnual.getOtrosIngresos());
			PropiedadesValidarDTO ingresosNetos = new PropiedadesValidarDTO(EnumIngresoNetoD.OTROS_INGRESOS_T.getCampo(), suma, 
					validacionMontos(actAnual.getOtrosIngresosTotal().getMonto()));
			modulo.getListPropsTovalidate().add(ingresosNetos);
		}
		return modulo;
	}

	/**
	 * Método para obtener la validación de la cantidad de otrosIngresosTotal contra la suma de las secciones II.1 al II.4  
	 * Se iteran las listas de actividadIndustrialCE, ActFinanciera, Servicios profesionales, 
	 * y Otros ingresos para obtener los montos declarados y la suma de ellos.
	 * @param modulo modulo padre
	 * @param ingresos el objeto de la actividad anual del declarante
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO validarOtrosIngresosTotal(ModuloValidarDTO modulo, IngresosNetosDeclaranteDTO ingresos, boolean enajenacion) {
		if(ingresos.getActividadIndustrialComercialEmpresarial() != null && ingresos.getActividadFinanciera() != null 
				&& ingresos.getServiciosProfesionales() != null	&& ingresos.getOtrosIngresos() != null) {
			
			long suma = obtenerSuma(ingresos.getActividadIndustrialComercialEmpresarial())
						+ obtenerSuma(ingresos.getActividadFinanciera())
						+ obtenerSuma(ingresos.getServiciosProfesionales())
						+ obtenerSuma(ingresos.getOtrosIngresos());
			
			if(enajenacion) {
				suma = suma + obtenerSuma(ingresos.getEnajenacionBienes());
			}
			
			PropiedadesValidarDTO ingresosNetos = new PropiedadesValidarDTO(EnumIngresoNetoD.OTROS_INGRESOS_T.getCampo(), suma, 
					validacionMontos(ingresos.getOtrosIngresosTotal().getMonto()));
			modulo.getListPropsTovalidate().add(ingresosNetos);
		}
		return modulo;
	}
	
	/**
	 * Obtener la suma de los montos de MontoMoneda de las listas de actividades
	 * @param actividades lista de objetos que traen como hijos RemuneraciónDTO
	 * @return Integer suma
	 */
	public static long obtenerSuma(Object actividades) {
		long suma = 0;
		if(actividades != null && actividades instanceof List) {
			for(Object act: (List<Object>) actividades) {
				if(((RemuneracionDTO)act).getRemuneracion() != null && ((RemuneracionDTO)act).getRemuneracion().getMonto() != null) {
					suma = suma + ((RemuneracionDTO)act).getRemuneracion().getMonto();
				}
			}
		}
		return suma;
	}
	
	
	/**
	 * Se asignan validaciones para la suma de montos
	 * @return lista de ParametrosValicacionDTO
	 */
	public static List<ParametrosValicacionDTO> validacionMontos(Long monto) {
		List<ParametrosValicacionDTO> validaciones = new ArrayList<>();
		validaciones.add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_LONG_IGUAL, monto));
		return validaciones;
	}
}
