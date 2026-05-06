/**
 * @(#)ValServiciosProf.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.validaciones;

import mx.gob.sfp.dgti.declaracion.dto.individual.ServiciosProfesionalesDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCantidad;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumRemuneracion;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumServiciosProfesionales;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;

/**
 * Clase de validaciones para el objeto de servicios profesionales
 * 
 * @author Miriam Sánchez Sánchez programador07
 * @since 07/11/2019
 */
public class ValServiciosProf {

	public static ModuloValidarDTO obtenerServiciosProf(ServiciosProfesionalesDTO servicioP, String nombre) {
		ModuloValidarDTO submodulo = null;
		
		if(servicioP != null) {
			submodulo = new ModuloValidarDTO(nombre, servicioP.getIdPosicionVista());
			
			PropiedadesValidarDTO tipoServicio = new PropiedadesValidarDTO(
					EnumServiciosProfesionales.TIPO_SERVICIO.getCampo(), 
					servicioP.getTipoServicio(), ValGenerales.validacionesCadena(EnumCantidad.CIENTO_UNO.getId()));
			
			submodulo.getListPropsTovalidate().add(PropBase.crearId(servicioP.getId()));
			submodulo.getListPropsTovalidate().add(tipoServicio);
			ValRemuneracion.obtenerMontoMonedaRem(submodulo, servicioP.getRemuneracion(), EnumRemuneracion.REMUNERACION.getCampo());
		
		} else {
			submodulo = new ModuloValidarDTO(nombre);
			submodulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(servicioP, nombre));
		}
		return submodulo;
	}
	
	
}
