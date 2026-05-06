/**
 * @(#)ValTelefono.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.validaciones;

import mx.gob.sfp.dgti.declaracion.dto.individual.TelefonoDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCantidad;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumTelefono;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase de validaciones para el objeto de telefono
 * 
 * @author Miriam Sánchez Sánchez programador07
 * @since 19/09/2019
 */
public class ValTelefono {

	/**
	 * Se obtienen las propiedades de teléfono de casa
	 *	
	 * @param telefono objeto TelefonoDTO
	 * @param nombre nombre del submodulo
	 * @param obligatorio indica si el submódulo de teléfono es obligatorio
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO obtenerTelefonoCasa(TelefonoDTO telefono, String nombre, boolean obligatorio) {
		ModuloValidarDTO submodulo = new ModuloValidarDTO(nombre);
		
		if(telefono != null && telefono.getNumero() != null && !telefono.getNumero().isEmpty()) {
			PropiedadesValidarDTO numero = new PropiedadesValidarDTO( EnumTelefono.NUMERO.getCampo(), 
					telefono.getNumero(), validacionesTelCasa(), false);
			submodulo.getListPropsTovalidate().add(numero);
		
		} else if (obligatorio && telefono == null){
			submodulo.getListPropsTovalidate().add(
					PropBase.crearObligatoria(telefono, nombre));
		}
		return submodulo;
	}
	
	/**
	 * Se obtienen las propiedades de teléfono celular
	 *
	 * @param telefono objeto TelefonoDTO
	 * @param nombre nombre del submodulo
	 * @param obligatorio indica si el submódulo de teléfono es obligatorio
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO obtenerTelefonoCelular(TelefonoDTO telefono, String nombre, boolean obligatorio) {
		ModuloValidarDTO submodulo = new ModuloValidarDTO(nombre);
		
		if(telefono != null) {
			if((telefono.getNumero() != null && !telefono.getNumero().isEmpty()) || telefono.getPaisCelularPersonal() != null) {
				PropiedadesValidarDTO numero = new PropiedadesValidarDTO( EnumTelefono.NUMERO.getCampo(), 
						telefono.getNumero(), validacionesCelular(), true);
		
				submodulo.getListPropsTovalidate().add(PropBase.crearPropCatalogo(
		                EnumTelefono.PAIS_CELULAR_PERSONAL.getCampo(), EnumCatalogos.CAT_PAIS.name(),
		                telefono.getPaisCelularPersonal(), true
		        ));
				submodulo.getListPropsTovalidate().add(numero);
			}
		
		} else if (obligatorio && telefono == null){
			submodulo.getListPropsTovalidate().add(
					PropBase.crearObligatoria(telefono, nombre));
		}
		
		return submodulo;
	}

	/**
	 * Se asignan validaciones para los campos de teléfono casa
	 * @return lista de ParametrosValicacionDTO
	 */
	private static List<ParametrosValicacionDTO> validacionesTelCasa() {
		List<ParametrosValicacionDTO> validaciones = new ArrayList<>();
		validaciones.add(new ParametrosValicacionDTO(EnumValidacion.TELEFONO_CASA));
		validaciones.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.QUINCE.getId()));
		return validaciones;
	}
	
	/**
	 * Se asignan validaciones para los campos de celular nacional
	 * @return lista de ParametrosValicacionDTO
	 */
	private static List<ParametrosValicacionDTO> validacionesCelular() {
		List<ParametrosValicacionDTO> validaciones = new ArrayList<>();
		validaciones.add(new ParametrosValicacionDTO(EnumValidacion.CELULAR_NACIONAL));
		return validaciones;
	}
	
	/**
	 * Se asignan validaciones para los campos de celular extranjero
	 * @return lista de ParametrosValicacionDTO
	 */
	private static List<ParametrosValicacionDTO> validacionesCelularExt() {
		List<ParametrosValicacionDTO> validaciones = new ArrayList<>();
		validaciones.add(new ParametrosValicacionDTO(EnumValidacion.CELULAR_EXTRANJERO));
		return validaciones;
	}
}
