/**
 * @(#)ValSector.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.validaciones;

import mx.gob.sfp.dgti.declaracion.dto.individual.SectorPrivadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.SectorPrivadoProvedorDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.SectorPublicoDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumActividadLaboral;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumSectorPrivado;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumSectorPublico;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.propiedades.PropSector;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase con las validaciones de los sectores publico, privado y privado con provedor
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 02/11/2019
 */
public class ValSector {

    /**
     * Metodo para definir el submodulo de sector publico
     *
     * @param sector: objeto con el domicilio del declarante
     * @return Objeto ModuloValidarDTO
     *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 02/11/2019
     */
	public static ModuloValidarDTO crearSectorPublico(SectorPublicoDTO sector) {
		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(EnumActividadLaboral.SECTOR_PUBLICO.getCampo());
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		
		propiedades.add(PropBase.crearModuloDebeSerNoNulo(sector, EnumActividadLaboral.SECTOR_PUBLICO.getCampo()));
		
		if(sector != null) {
	
			propiedades.add(PropSector.crearPropNombreEnte(sector.getNombreEntePublico()));
			propiedades.add(PropSector.crearPropAreaAdscrip(sector.getAreaAdscripcion()));
			propiedades.add(PropSector.crearPropEmpleo(sector.getEmpleoCargoComision()));
			propiedades.add(PropSector.crearPropFuncion(sector.getFuncionPrincipal()));
			
			propiedades.add(PropBase.crearObligatoria(sector.getNivelOrdenGobierno(), 
					EnumSectorPublico.NIVEL_ORDEN_GOBIERNO.getCampo()));

		} 
		moduloValidar.setListPropsTovalidate(propiedades);
		return moduloValidar;
	}

	/**
	 * Metodo para definir el submodulo de sector privado
	 *
	 * @param sector:
	 * @return objeto ModuloValidarDTO con las validaciones
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 02/11/2019
	 */
	public static ModuloValidarDTO crearSectorPrivado(SectorPrivadoDTO sector) {

		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(EnumActividadLaboral.SECTOR_PRIVADO.getCampo());
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		propiedades.add(PropSector.crearPropNomEmpr(sector.getNombreEmpresaSociedadAsociacion()));
		if (sector.getRfc() != null && !sector.getRfc().isEmpty()) {
			propiedades.add(PropSector.crearPropRfc(sector.getRfc()));
		}

		propiedades.add(PropSector.crearPropArea(sector.getArea()));
		propiedades.add(PropSector.crearPropEmpleo(sector.getEmpleoCargo()));

		// + AGREGAR CAT_SECTOR

		PropBase.crearPropCatalogoOtro(EnumSectorPrivado.SECTOR.getCampo(),
				EnumCatalogos.CAT_SECTOR_PRIVADO.name(), sector.getSector(), true,
				sector.getSectorOtro(), propiedades);

		moduloValidar.setListPropsTovalidate(propiedades);
		return moduloValidar;
	}

    /**
     * Metodo para definir el submodulo de sector privado con provedor
     *
     * @param sector:
     * @return objeto ModuloValidarDTO con las validaciones
     *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 02/11/2019
     */
	public static ModuloValidarDTO crearSectorPrivadoProvedor(SectorPrivadoProvedorDTO sector){
		ModuloValidarDTO moduloValidar = crearSectorPrivado(sector);
		return moduloValidar;
	}

}
