/**
 * @(#)ValActividadLab.java 02/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.validaciones;

import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.ActividadLaboralDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.ActividadLaboralNoDeclDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumActividadLaboral;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumAmbitoSector;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.utils.propiedades.PropActividadLab;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase con las validaciones de el domicilio del declarante.
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 02/11/2019
 */
public class ValActividadLab {

	/**
	 * Metodo para definir el submodulo de actividadLaboral
	 *
	 * @param actividadLaboral:
	 * @return objeto ModuloValidarDTO con las validaciones
	 * @author Pavel Alexei Martinez Regalado
	 * @since 02/11/2019
	 */
	public static ModuloValidarDTO crearActividadLaboral(ActividadLaboralDTO actividadLaboral, EncabezadoDTO encabezado) {
		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(EnumActividadLaboral.ACTIVIDAD_LABORAL.getCampo());
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		// + AGREGAR CAT_AMBITO_SECTOR
		PropBase.crearPropCatalogoOtro(EnumActividadLaboral.AMBITO_SECTOR.getCampo(),
				EnumCatalogos.CAT_AMBITO_SECTOR.name(), actividadLaboral.getAmbitoSector(), true,
				actividadLaboral.getAmbitoSectorOtro(), propiedades);

		propiedades.add(ValGenerales.validacionesFechaIE(
				actividadLaboral.getFechaIngreso(), actividadLaboral.getFechaEgreso(), EnumActividadLaboral.FECHA_INGRESO.getCampo()));
		
		propiedades.add(PropBase.crearValidacionFechaContraFechaEncargo(
				actividadLaboral.getFechaIngreso(), encabezado, EnumActividadLaboral.FECHA_INGRESO.getCampo(), true));
		propiedades.add(PropBase.crearValidacionFechaContraFechaEncargo(
				actividadLaboral.getFechaEgreso(), encabezado, EnumActividadLaboral.FECHA_EGRESO.getCampo(), true));

		propiedades.add(PropBase.crearObligatoria(actividadLaboral.getUbicacion(), EnumActividadLaboral.UBICACION.getCampo()));

		if (actividadLaboral.getAmbitoSector() != null) {

			if (actividadLaboral.getAmbitoSector().getId().equals(EnumAmbitoSector.PUBLICO.getId())) {
				propiedades.add(PropBase.crearModuloDebeSerNoNulo(actividadLaboral.getSectorPublico(),
						EnumActividadLaboral.SECTOR_PUBLICO.getCampo()));
				if(actividadLaboral.getSectorPublico() != null ) {
					modulos.add(ValSector.crearSectorPublico(actividadLaboral.getSectorPublico()));
				}
			} else if (actividadLaboral.getAmbitoSector().getId().equals(EnumAmbitoSector.PRIVADO.getId()) ||
					actividadLaboral.getAmbitoSector().getId().equals(EnumAmbitoSector.OTRO.getId())) {

				propiedades.add(PropBase.crearModuloDebeSerNoNulo(actividadLaboral.getSectorPrivadoOtro(),
						EnumActividadLaboral.SECTOR_PRIVADO.getCampo()));
				if (actividadLaboral.getSectorPrivadoOtro() != null) {
					modulos.add(ValSector.crearSectorPrivado(actividadLaboral.getSectorPrivadoOtro()));
				}
			}

			if (actividadLaboral.getSectorPublico() != null || actividadLaboral.getSectorPrivadoOtro() != null) {
				propiedades.add(PropBase.crearPropUnSoloObjetoNoNulo(
						new Object[]{
								actividadLaboral.getSectorPublico(),
								actividadLaboral.getSectorPrivadoOtro()},
						new String[]{
								EnumActividadLaboral.SECTOR_PUBLICO.getCampo(),
								EnumActividadLaboral.SECTOR_PRIVADO.getCampo()
						}));
			}

			moduloValidar.setListPropsTovalidate(propiedades);
			moduloValidar.setListModuloshijos(modulos);

		}
		return moduloValidar;
	}

	/**
	 * Metodo para definir el submodulo de actividadLaboral pero anade el salario
	 *
	 * @param actividadLaboral:
	 * @return objeto ModuloValidarDTO con las validaciones
	 * @author Pavel Alexei Martinez Regalado
	 * @since 02/11/2019
	 */
	public static ModuloValidarDTO crearActividadLaboral(ActividadLaboralNoDeclDTO actividadLaboral,
														 EncabezadoDTO encabezado) {

		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(EnumActividadLaboral.ACTIVIDAD_LABORAL.getCampo());
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		PropBase.crearPropCatalogoOtro(EnumActividadLaboral.AMBITO_SECTOR.getCampo(),
				EnumCatalogos.CAT_AMBITO_SECTOR.name(), actividadLaboral.getAmbitoSector(), true,
				actividadLaboral.getAmbitoSectorOtro(), propiedades);

		propiedades.add(PropBase.crearValidacionFechaContraFechaEncargo(
				actividadLaboral.getFechaIngreso(), encabezado, EnumActividadLaboral.FECHA_INGRESO.getCampo(), true));
		propiedades.add(PropActividadLab.crearPropSalario(actividadLaboral.getSalarioMensualNeto()));

		if (actividadLaboral.getAmbitoSector() != null) {

			if (actividadLaboral.getAmbitoSector().getId().equals(EnumAmbitoSector.PUBLICO.getId())) {

				propiedades.add(PropBase.crearModuloDebeSerNoNulo(actividadLaboral.getSectorPublico(),
						EnumActividadLaboral.SECTOR_PUBLICO.getCampo()));
				if(actividadLaboral.getSectorPublico() != null) {
					modulos.add(ValSector.crearSectorPublico(actividadLaboral.getSectorPublico()));
				}
			} else if (actividadLaboral.getAmbitoSector().getId().equals(EnumAmbitoSector.PRIVADO.getId()) ||
					actividadLaboral.getAmbitoSector().getId().equals(EnumAmbitoSector.OTRO.getId())) {

				propiedades.add(PropBase.crearModuloDebeSerNoNulo(actividadLaboral.getSectorPrivadoOtro(),
						EnumActividadLaboral.SECTOR_PRIVADO.getCampo()));
				if(actividadLaboral.getSectorPrivadoOtro() != null ) {
					modulos.add(ValSector.crearSectorPrivadoProvedor(actividadLaboral.getSectorPrivadoOtro()));
				}
			}

			if (actividadLaboral.getSectorPublico() != null || actividadLaboral.getSectorPrivadoOtro() != null) {
				propiedades.add(PropBase.crearPropUnSoloObjetoNoNulo(
						new Object[]{
								actividadLaboral.getSectorPublico(),
								actividadLaboral.getSectorPrivadoOtro()},
						new String[]{
								EnumActividadLaboral.SECTOR_PUBLICO.getCampo(),
								EnumActividadLaboral.SECTOR_PRIVADO.getCampo()
						}));
			}

			moduloValidar.setListPropsTovalidate(propiedades);
			moduloValidar.setListModuloshijos(modulos);

		}
		return moduloValidar;
	}
}