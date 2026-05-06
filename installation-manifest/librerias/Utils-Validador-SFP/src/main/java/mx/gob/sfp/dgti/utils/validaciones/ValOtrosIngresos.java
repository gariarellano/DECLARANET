/**
 * @(#)ValOtrosIngresos.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.validaciones;

import mx.gob.sfp.dgti.declaracion.dto.individual.OtrosIngresosDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCantidad;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumRemuneracion;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumServiciosProfesionales;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;

/**
 * Clase de validaciones para el submódulo de otros ingresos
 * 
 * @author Miriam Sánchez Sánchez programador07
 * @since 07/11/2019
 */
public class ValOtrosIngresos {

	/**
	 * Obtener el submodulo de Otros ingresos
	 * @param otroIngreso
	 * @param nombre del objeto
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO obtenerOtroIngreso(OtrosIngresosDTO otroIngreso, String nombre) {
		ModuloValidarDTO submodulo = null; 

		if (otroIngreso != null) {
			submodulo = new ModuloValidarDTO(nombre, otroIngreso.getIdPosicionVista());
			
			PropiedadesValidarDTO tipoIngreso = new PropiedadesValidarDTO(
					EnumServiciosProfesionales.TIPO_INGRESO.getCampo(), otroIngreso.getTipoIngreso(), 
					ValGenerales.validacionesCadena(EnumCantidad.CUATRO_MIL_UNO.getId()));
			
			submodulo.getListPropsTovalidate().add(PropBase.crearId(otroIngreso.getId()));
			submodulo.getListPropsTovalidate().add(tipoIngreso);
			ValRemuneracion.obtenerMontoMonedaRem(submodulo, otroIngreso.getRemuneracion(), EnumRemuneracion.REMUNERACION.getCampo());
		
		} else {
			submodulo = new ModuloValidarDTO(nombre);
			submodulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(otroIngreso, nombre));
		}
		return submodulo;
	}
	
}
