/**
 * @(#)ValEnajenacionB.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.validaciones;

import mx.gob.sfp.dgti.declaracion.dto.individual.EnajenacionBienDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumEnajenacionBien;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumRemuneracion;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;

/**
 * Clase de validaciones para el submódulo de enajenación de bienes
 * 
 * @author Miriam Sánchez Sánchez programador07
 * @since 07/11/2019
 */
public class ValEnajenacionB {

	/**
	 * Obtener el submodulo de enajenación de bienes 
	 * @param enajenacionBien
	 * @param nombre nombre del objeto
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO obtenerEnajenacionB(EnajenacionBienDTO enajenacionBien, String nombre) {
		ModuloValidarDTO submodulo = null;
		
		if(enajenacionBien != null) {
			submodulo = new ModuloValidarDTO(nombre, enajenacionBien.getIdPosicionVista());
			
			submodulo.getListPropsTovalidate().add(PropBase.crearId(enajenacionBien.getId()));
			submodulo.getListPropsTovalidate().add(
						new PropiedadesValidarDTO(EnumEnajenacionBien.TIPO_BIEN.getCampo(), enajenacionBien.getTipoBien(), true));
			ValRemuneracion.obtenerMontoMonedaRem(submodulo, enajenacionBien.getRemuneracion(), EnumRemuneracion.REMUNERACION.getCampo());

		} else {
			submodulo = new ModuloValidarDTO(nombre);
			submodulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(enajenacionBien, nombre));
		}
		return submodulo;
	}
}
