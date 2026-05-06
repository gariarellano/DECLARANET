/**
 * @ValGenerales.java Nov 14, 2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.utils.validaciones;

import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Validaciones generales
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 14, 2019
 */
public class ValGenerales {

	/**
	 * Se asignan validaciones para fecha
	 * @return lista de ParametrosValicacionDTO
	 */
	public static List<ParametrosValicacionDTO> validacionesFecha() {
		List<ParametrosValicacionDTO> validaciones = new ArrayList<>();
		validaciones.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_FORMATO));
		return validaciones;
	}
	
	/**
	 * Se asignan validaciones básicas para las cadenas sin caracteres especiales
	 * @param longitud 
	 * @return ParametrosValicacionDTO
	 */
	public static List<ParametrosValicacionDTO> validacionesCadena(Integer longitud) {
		List<ParametrosValicacionDTO> validaciones = new ArrayList<ParametrosValicacionDTO>();
		validaciones.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_SIN_CARACTERES_ESPECIALES));
		
		if(longitud > 0) {
			validaciones.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, longitud));
		}
		return validaciones;
	}
	
	/**
	 * Se asignan validaciones básicas para las cadenas alfanumericas
	 * @param longitud 
	 * @return ParametrosValicacionDTO
	 */
	public static List<ParametrosValicacionDTO> validacionesCadenaAlfa(Integer longitud) {
		List<ParametrosValicacionDTO> validaciones = new ArrayList<ParametrosValicacionDTO>();
		validaciones.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
		
		if(longitud > 0) {
			validaciones.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, longitud));
		}
		
		return validaciones;
	}
	
	/**
	 * Se genera la lista de validaciones para las fechas de actividad anual anterior
	 * @param fechaConclusion
	 * @return List<ParametrosValicacionDTO>
	 */
	public static PropiedadesValidarDTO validacionesFechaIE(String fechaInicio, String fechaConclusion, String nombre) {
		List<ParametrosValicacionDTO> validaciones = new ArrayList<>();
		validaciones.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_MENORQUE, fechaConclusion));
		return new PropiedadesValidarDTO(nombre, fechaInicio, validaciones, true);
	}
	
}
