/**
 * @(#)ValCorreo.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.validaciones;

import mx.gob.sfp.dgti.declaracion.dto.individual.CorreoDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCantidad;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCorreo;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase de validaciones para el módulo de datos generales de la declaración
 * 
 * @author Miriam Sánchez Sánchez programador07
 * @since 19/09/2019
 */
public class ValCorreo {
	
	private static final String EMPTY = "";
	private static final String GUION = "-";

	/**
	 * Se obtienen las propiedades de correo electronico
	 * @param correo objeto de correo
	 * @param nombre del submodulo
	 * @param alMenosUnoObligatorio 
	 * @param obligatorio indica si el submódulo es obligatorio
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO obtenerCorreo(CorreoDTO correo, String nombre, boolean alMenosUnoObligatorio, boolean obligatorio) {
		ModuloValidarDTO submodulo = new ModuloValidarDTO(nombre);
		
		if(correo != null) {
			if(!EMPTY.equals(correo.getInstitucional())) {
				PropiedadesValidarDTO institucional = new PropiedadesValidarDTO(EnumCorreo.INSTITUCIONAL.getCampo(),
						correo.getInstitucional(), validacionesCorreo(), false);
				submodulo.getListPropsTovalidate().add(institucional);
			}
			
			if(!EMPTY.equals(correo.getPersonalAlterno())) {
				PropiedadesValidarDTO personal = new PropiedadesValidarDTO(EnumCorreo.PERSONAL_ALTERNO.getCampo(),
						correo.getPersonalAlterno(), validacionesCorreo(), false);
				submodulo.getListPropsTovalidate().add(personal);
			}
			if(alMenosUnoObligatorio) {
				PropiedadesValidarDTO correoObligatorio = new PropiedadesValidarDTO(
						EnumCorreo.PERSONAL_ALTERNO.getCampo() + GUION + EnumCorreo.INSTITUCIONAL.getCampo(), 
						correo.getPersonalAlterno() + GUION + correo.getInstitucional(), 
						validacionCorreoAlMenosUno(correo.getInstitucional(), correo.getPersonalAlterno()), false);
				submodulo.getListPropsTovalidate().add(correoObligatorio);
			}
		
		} else if (obligatorio && correo == null) {
			submodulo.getListPropsTovalidate().add(PropBase.crearObligatoria(correo, nombre));
		}
		
		return submodulo;
	}
	
	/**
	 * Se asignan validaciones para los campos de correo
	 * @return lista de ParametrosValicacionDTO
	 */
	private static List<ParametrosValicacionDTO> validacionesCorreo() {
		List<ParametrosValicacionDTO> validaciones = new ArrayList<>();
		validaciones.add(new ParametrosValicacionDTO(EnumValidacion.CORREO));
		validaciones.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CINCUENTA_Y_UNO.getId()));
		return validaciones;
	}
	
	/**
	 * Se asignan validaciones para los campos de correo
	 * @return lista de ParametrosValicacionDTO
	 */
	private static List<ParametrosValicacionDTO> validacionCorreoAlMenosUno(String correoInstitucional, String correoPersonal) {
		List<ParametrosValicacionDTO> validaciones = new ArrayList<>();
		validaciones.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_TEXTO_AL_MENOS_UNO, correoInstitucional, correoPersonal));
		return validaciones;
	}
	
}