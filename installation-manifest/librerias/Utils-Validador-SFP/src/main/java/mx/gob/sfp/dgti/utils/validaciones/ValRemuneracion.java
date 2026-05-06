/**
 * @(#)ValRemuneracion.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.validaciones;

import mx.gob.sfp.dgti.declaracion.dto.individual.MontoMonedaDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.RemuneracionDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumRemuneracion;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;

/**
 * Clase de validaciones para el submódulo de remuneración
 * 
 * @author Miriam Sánchez Sánchez programador07
 * @since 07/11/2019
 */
public class ValRemuneracion {

	/**
	 * Obtener el submodulo de remuneracion 
	 * @param modulo modulo padre
	 * @param remuneracion
	 * @param nombre del objeto
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO obtenerRemuneracion(RemuneracionDTO remuneracion, String nombre) {
		ModuloValidarDTO submodulo = new ModuloValidarDTO(nombre);

		if(remuneracion != null) {
			
			if(remuneracion.getRemuneracion() != null) {
				submodulo.getListModuloshijos().add(ValMonto.crearMonto(remuneracion.getRemuneracion(), EnumRemuneracion.REMUNERACION.getCampo()));
			
			} else {
				submodulo.getListPropsTovalidate().add(
					PropBase.crearObligatoria(remuneracion.getRemuneracion(), EnumRemuneracion.REMUNERACION.getCampo()));
			}
		} else {
			submodulo.getListPropsTovalidate().add(
					PropBase.crearObligatoria(remuneracion, nombre));
		}
		return submodulo;
	}
	
	/**
	 * Obtener monto moneda 
	 * @param modulo modulo padre
	 * @param monto objeto monto moneda 
	 * @param nombre del submodulo a agregar
	 * @return ModuloValidarDTO modulo padre
	 */
	public static ModuloValidarDTO obtenerMontoMonedaRem(ModuloValidarDTO modulo, MontoMonedaDTO monto, String nombre) {
		
		if(monto != null) {
			modulo.getListModuloshijos().add(ValMonto.crearMonto(monto, nombre)); 
			
		} else {
			modulo.getListPropsTovalidate().add(
					PropBase.crearObligatoria(monto, nombre));
		}
		return modulo;
	}
	
}
