/**
 * @(#)ValPersona.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.validaciones;

import mx.gob.sfp.dgti.declaracion.dto.general.DatosPersonalesDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.DatosPersonalesNoDeclDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.PersonaDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.PersonaFisicaDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.PersonaMoralDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.TransmisorDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumDatosPersonales;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumPersona;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumTransmisor;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoPersona;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.propiedades.PropPersona;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase con las validaciones de el domicilio del declarante.
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 12/09/2019
 */
public class ValPersona {

    /**
     * Metodo para definir el modulo de domicilio
     *
     * @param persona: objeto con el domicilio del declarante
     * @return Objeto ModuloValidarDTO
     *
     * @author Pavel Alexei Martinez Regalado
     * @since 12/09/2019
     */
	public static ModuloValidarDTO crearPersonaFisica(PersonaFisicaDTO persona, boolean rfcObligatorio) {

		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(EnumPersona.PERSONA_FISICA.getCampo());
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		propiedades.add(PropPersona.crearPropNombre(persona.getNombre()));
		propiedades.add(PropPersona.crearPropPrimerAp(persona.getPrimerApellido()));
		
		if(persona.getSegundoApellido() != null && !persona.getSegundoApellido().isEmpty()) {
			propiedades.add(PropPersona.crearPropSegundoAp(persona.getSegundoApellido()));
		}
		
		if(rfcObligatorio || (persona.getRfc() != null && !persona.getRfc().isEmpty()))
			propiedades.add(PropPersona.crearPropRfc(persona.getRfc()));

		moduloValidar.setListPropsTovalidate(propiedades);

		return moduloValidar;
	}


	public static ModuloValidarDTO crearPersonaMoral(PersonaMoralDTO persona,  boolean rfcObligatorio) {

		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(EnumPersona.PERSONA_MORAL.getCampo());
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		propiedades.add(PropPersona.crearPropNombrePersMoral(persona.getNombre()));
		if(rfcObligatorio || (persona.getRfc() != null && !persona.getRfc().isEmpty()))
			propiedades.add(PropPersona.crearPropRfcPersMoral(persona.getRfc()));

		moduloValidar.setListPropsTovalidate(propiedades);

		return moduloValidar;
	}

	public static ModuloValidarDTO crearPersonaMoral(PersonaMoralDTO persona, String nombre, boolean rfcObligatorio) {

		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(nombre);
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		propiedades.add(PropPersona.crearPropNombrePersMoral(persona.getNombre()));

		if(rfcObligatorio || (persona.getRfc() != null && !persona.getRfc().isEmpty())) {
			propiedades.add(PropPersona.crearPropRfcPersMoral(persona.getRfc()));
		}

		moduloValidar.setListPropsTovalidate(propiedades);

		return moduloValidar;
	}

	
	public static ModuloValidarDTO crearDatosPersonalesNoDecl(DatosPersonalesNoDeclDTO persona) {

		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(EnumDatosPersonales.DATOS_PERSONALES.getCampo());
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		propiedades.add(PropPersona.crearPropNombre(persona.getNombre()));
		propiedades.add(PropPersona.crearPropPrimerAp(persona.getPrimerApellido()));
		
		if(persona.getSegundoApellido() != null && !persona.getSegundoApellido().isEmpty()) {
			propiedades.add(PropPersona.crearPropSegundoAp(persona.getSegundoApellido()));
		}

		if(persona.getRfc() != null && !persona.getRfc().isEmpty()) {
			propiedades.add(PropPersona.crearPropRfc(persona.getRfc()));
		}
		propiedades.add(PropPersona.crearPropFechaNacimiento(persona.getFechaNacimiento()));

		moduloValidar.setListPropsTovalidate(propiedades);

		return moduloValidar;
	}

	/**
	 * Metodo para definir el modulo de transmisor que es casi igual pero añade el campo de la relacion con el titular
	 *
	 * @param transmisor: objeto con el transmisor, ya sea moral o fisica
	 * @param nombre: objeto con el nombre del transmisor
	 * @return Objeto ModuloValidarDTO
	 *
	 * @author pavel.martinez
	 * @since 11/11/2019
	 */
	public static ModuloValidarDTO crearTransmisor(TransmisorDTO transmisor, String nombre) {
		ModuloValidarDTO moduloValidar = crearPersona(transmisor, nombre);
		//CAT RELACION_BIEN
		PropBase.crearPropCatalogoOtro(EnumTransmisor.RELACION_CON_TITULAR.getCampo(),
				EnumCatalogos.CAT_TIPO_RELACION_BIENES.name(), transmisor.getRelacionConTitular(),
				true, transmisor.getRelacionConTitularOtro(), moduloValidar.getListPropsTovalidate());

		return moduloValidar;
	}

	/**
	 * Metodo para definir el modulo de persona
	 *
	 * @param persona: objeto con la persona, ya sea moral o fisica
	 * @param nombre: objeto con el nombre de la persona
	 * @return Objeto ModuloValidarDTO
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 12/09/2019
	 */
	public static ModuloValidarDTO crearPersona(PersonaDTO persona, String nombre) {

		ModuloValidarDTO moduloValidar;
		if(persona.getIdPosicionVista() == null || persona.getIdPosicionVista().isEmpty()){
			moduloValidar= new ModuloValidarDTO(nombre);
		} else {
			moduloValidar= new ModuloValidarDTO(nombre, persona.getIdPosicionVista());
		}

		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		propiedades.add(PropBase.crearId(persona.getId()));

		propiedades.add(PropBase.crearObligatoria(persona.getTipoPersona(), EnumPersona.TIPO_PERSONA.getCampo()));
		
		if(persona.getTipoPersona() != null) {
			if (persona.getTipoPersona().equals(EnumTipoPersona.PERSONA_FISICA)) {
				propiedades.add(PropBase.crearModuloDebeSerNoNulo(persona.getPersonaFisica(), EnumTipoPersona.PERSONA_FISICA.getDescripcion()));
				propiedades.add(PropBase.crearModuloDebeSerNulo(persona.getPersonaMoral(), EnumTipoPersona.PERSONA_MORAL.getDescripcion()));

				if(persona.getPersonaFisica() != null) {
					modulos.add(crearPersonaFisica(persona.getPersonaFisica(), false));
				}
	
			} else if (persona.getTipoPersona().equals(EnumTipoPersona.PERSONA_MORAL)) {
				propiedades.add(PropBase.crearModuloDebeSerNoNulo(persona.getPersonaMoral(), EnumTipoPersona.PERSONA_MORAL.getDescripcion()));
				propiedades.add(PropBase.crearModuloDebeSerNulo(persona.getPersonaFisica(), EnumTipoPersona.PERSONA_FISICA.getDescripcion()));

				if(persona.getPersonaMoral() != null) {
					modulos.add(crearPersonaMoral(persona.getPersonaMoral(), false));
				}
			}
		}

		moduloValidar.setListModuloshijos(modulos);
		moduloValidar.setListPropsTovalidate(propiedades);

		return moduloValidar;
	}
	
	/**
	 * Metodo para definir el modulo de persona
	 *
	 * @param persona: objeto con la persona, ya sea moral o fisica( para el declarante)
	 * @param nombre: objeto con el nombre de la persona
	 * @return Objeto ModuloValidarDTO
	 *
	 * @author Programador04
	 * @since 16/01/2020
	 */
	public static ModuloValidarDTO crearPersonaRfcObligatorio(PersonaDTO persona, String nombre) {

		ModuloValidarDTO moduloValidar;
		if(persona.getIdPosicionVista() == null || persona.getIdPosicionVista().isEmpty()){
			moduloValidar= new ModuloValidarDTO(nombre);
		} else {
			moduloValidar= new ModuloValidarDTO(nombre, persona.getIdPosicionVista());
		}

		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		propiedades.add(PropBase.crearId(persona.getId()));

		propiedades.add(PropBase.crearObligatoria(persona.getTipoPersona(), EnumPersona.TIPO_PERSONA.getCampo()));
		
		if(persona.getTipoPersona() != null) {
			if (persona.getTipoPersona().equals(EnumTipoPersona.PERSONA_FISICA)) {
				propiedades.add(PropBase.crearModuloDebeSerNoNulo(persona.getPersonaFisica(), EnumTipoPersona.PERSONA_FISICA.getDescripcion()));
				propiedades.add(PropBase.crearModuloDebeSerNulo(persona.getPersonaMoral(), EnumTipoPersona.PERSONA_MORAL.getDescripcion()));

				if(persona.getPersonaFisica() != null) {
					modulos.add(crearPersonaFisica(persona.getPersonaFisica(), true));
				}
	
			} else if (persona.getTipoPersona().equals(EnumTipoPersona.PERSONA_MORAL)) {
				propiedades.add(PropBase.crearModuloDebeSerNoNulo(persona.getPersonaMoral(), EnumTipoPersona.PERSONA_MORAL.getDescripcion()));
				propiedades.add(PropBase.crearModuloDebeSerNulo(persona.getPersonaFisica(), EnumTipoPersona.PERSONA_FISICA.getDescripcion()));

				if(persona.getPersonaMoral() != null) {
					modulos.add(crearPersonaMoral(persona.getPersonaMoral(), true));
				}
			}
		}

		moduloValidar.setListModuloshijos(modulos);
		moduloValidar.setListPropsTovalidate(propiedades);

		return moduloValidar;
	}
	

	/**
	 * Metodo para definir el modulo de datos personales
	 * @param datosPersonales
	 * @param submodulo
	 * @return ModuloValidarDTO
	 */
	public static ModuloValidarDTO crearDatosPersonales(DatosPersonalesDTO datosPersonales, String submodulo) {

		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(submodulo);
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		propiedades.add(PropPersona.crearPropNombre(datosPersonales.getNombre()));

		// Si se tiene un solo apellido debera colocarse en el espacio del primer apellido y dejar el espacio del
		// segundo apellido en blanco
		propiedades.add(PropPersona.crearPropPrimerAp(datosPersonales.getPrimerApellido()));
		
		if(datosPersonales.getSegundoApellido() != null && !datosPersonales.getSegundoApellido().isEmpty()) {
			propiedades.add(PropPersona.crearPropSegundoAp(datosPersonales.getSegundoApellido()));
			
		} 

		propiedades.add(PropPersona.crearPropRfc(datosPersonales.getRfc()));
		propiedades.add(PropPersona.crearPropCurp(datosPersonales.getCurp()));

		moduloValidar.setListPropsTovalidate(propiedades);

		return moduloValidar;
	}
}
