/**
 * @(#)ValActividadIndCE.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.validaciones;

import mx.gob.sfp.dgti.declaracion.dto.individual.ActividadIndustrialCEDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumActividadIndustrial;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCantidad;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumRemuneracion;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;

/**
 * Clase de validaciones para el submódulo de actividad industrial comercial empresarial
 * 
 * @author Miriam Sánchez Sánchez programador07
 * @since 07/11/2019
 */
public class ValActividadIndCE {

	/**
	 * Obtener el submódulo de actividad industrial comercial empresarial
	 * @param actividad actividad industrial
	 * @param nombre nombre del objeto
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO obtenerActIndustrial(ActividadIndustrialCEDTO actividad, String nombre) {
		ModuloValidarDTO submodulo = null;
		
		if(actividad != null) {
			submodulo = new ModuloValidarDTO(nombre, actividad.getIdPosicionVista());
			
			PropiedadesValidarDTO nombreRazonS = new PropiedadesValidarDTO(EnumActividadIndustrial.NOMBRE_RAZON_SOCIAL.getCampo(), 
					actividad.getNombreRazonSocial(), ValGenerales.validacionesCadena(EnumCantidad.CIENTO_UNO.getId()));
			
			PropiedadesValidarDTO tipoNegocio = new PropiedadesValidarDTO(EnumActividadIndustrial.TIPO_NEGOCIO.getCampo(), 
					actividad.getTipoNegocio(), ValGenerales.validacionesCadena(EnumCantidad.CIENTO_UNO.getId()));
			submodulo.getListPropsTovalidate().add(PropBase.crearId(actividad.getId()));
			submodulo.getListPropsTovalidate().add(nombreRazonS);
			submodulo.getListPropsTovalidate().add(tipoNegocio);
			ValRemuneracion.obtenerMontoMonedaRem(submodulo, actividad.getRemuneracion(), EnumRemuneracion.REMUNERACION.getCampo());
		
		} else {
			submodulo = new ModuloValidarDTO(nombre);
			submodulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(actividad, nombre));
		}
		return submodulo;
	}
	
}
