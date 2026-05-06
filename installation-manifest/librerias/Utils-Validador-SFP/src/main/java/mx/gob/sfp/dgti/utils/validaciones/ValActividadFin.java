/**
 * @(#)ValActividadFin.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.validaciones;

import mx.gob.sfp.dgti.declaracion.dto.individual.ActividadFinancieraDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumActividadFinanciera;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumRemuneracion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;

/**
 * Clase de validaciones para el objeto de actividad
 * 
 * @author Miriam Sánchez Sánchez programador07
 * @since 07/11/2019
 */
public class ValActividadFin {

	public static ModuloValidarDTO obtenerActividadFin(ActividadFinancieraDTO actividad, String nombre) {
		ModuloValidarDTO submodulo = null;
		
		if(actividad != null) {
			submodulo = new ModuloValidarDTO(nombre, actividad.getIdPosicionVista());

			 PropBase.crearPropCatalogoOtro(EnumActividadFinanciera.TIPO_INSTRUMENTO.getCampo(), EnumCatalogos.CAT_TIPO_INSTRUMENTO.name(),
					 actividad.getTipoInstrumento(), true,
					 actividad.getTipoInstrumentoOtro(), submodulo.getListPropsTovalidate());

			submodulo.getListPropsTovalidate().add(PropBase.crearId(actividad.getId()));
			ValRemuneracion.obtenerMontoMonedaRem(submodulo, actividad.getRemuneracion(), EnumRemuneracion.REMUNERACION.getCampo());
		
		} else {
			submodulo = new ModuloValidarDTO(nombre);
			submodulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(actividad, nombre));
		}
		return submodulo;
	}
	
}
