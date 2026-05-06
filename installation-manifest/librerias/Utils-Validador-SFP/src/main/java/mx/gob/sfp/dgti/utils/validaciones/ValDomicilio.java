/**
 * @(#)ValDomicilio.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.validaciones;

import mx.gob.sfp.dgti.declaracion.dto.general.DomicilioDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.LocalizacionDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.*;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumDomicilio;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogosFk;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumUbicacion;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.propiedades.PropDomicilio;
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
public class ValDomicilio {

    /**
     * Metodo para definir el modulo de domicilio
     *
     * @param domicilio: objeto con el domicilio del declarante
     * @return Objeto ModuloValidarDTO
     *
     * @author Pavel Alexei Martinez Regalado
     * @since 12/09/2019
     */
	public static ModuloValidarDTO crearDomicilio(DomicilioDTO domicilio, String nombre) {

		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(nombre);
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		propiedades.add(PropBase.crearObligatoria(domicilio.getUbicacion(), EnumDomicilio.UBICACION.getNombre()));

		if(domicilio.getUbicacion() != null) {
			if (domicilio.getUbicacion().equals(EnumUbicacion.MEXICO)) {
				propiedades.add(PropBase.crearModuloDebeSerNoNulo(domicilio.getDomicilioMexico(),
						EnumDomicilio.DOMICILIO_MEXICO.getNombre()));
				propiedades.add(PropBase.crearModuloDebeSerNulo(domicilio.getDomicilioExtranjero(),
						EnumDomicilio.DOMICILIO_EXTRANJERO.getNombre()));
				if (domicilio.getDomicilioMexico() != null) {
					modulos.add(crearDomicilioMexico(domicilio.getDomicilioMexico()));
				}

			} else if (domicilio.getUbicacion().equals(EnumUbicacion.EXTRANJERO)) {
				propiedades.add(PropBase.crearModuloDebeSerNoNulo(domicilio.getDomicilioExtranjero(),
						EnumDomicilio.DOMICILIO_EXTRANJERO.getNombre()));
				propiedades.add(PropBase.crearModuloDebeSerNulo(domicilio.getDomicilioMexico(),
						EnumDomicilio.DOMICILIO_MEXICO.getNombre()));
				if (domicilio.getDomicilioExtranjero() != null) {
					modulos.add(crearDomicilioExtranjero(domicilio.getDomicilioExtranjero()));
				}
			}
		}

		moduloValidar.setListModuloshijos(modulos);
		moduloValidar.setListPropsTovalidate(propiedades);

		return moduloValidar;
	}

	/**
	 * Metodo para definir el modulo de localizacion
	 *
	 * @param loc: objeto con el domicilio del declarante
	 * @return Objeto ModuloValidarDTO
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 12/09/2019
	 */
	public static ModuloValidarDTO crearLocalizacion(LocalizacionDTO loc, String nombre) {

		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(nombre);
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		propiedades.add(PropBase.crearObligatoria(loc.getUbicacion(), EnumDomicilio.UBICACION.getNombre()));
		
		if(loc.getUbicacion() != null) {
			if (loc.getUbicacion().equals(EnumUbicacion.MEXICO)) {
				propiedades.add(PropBase.crearModuloDebeSerNoNulo(loc.getLocalizacionMexico(),
						EnumDomicilio.DOMICILIO_MEXICO.getNombre()));
				propiedades.add(PropBase.crearModuloDebeSerNulo(loc.getLocalizacionExtranjero(),
						EnumDomicilio.DOMICILIO_EXTRANJERO.getNombre()));
				
				if(loc.getLocalizacionMexico() != null) {
					modulos.add(crearLocalizacionMexico(loc.getLocalizacionMexico()));
				}
	
			} else if (loc.getUbicacion().equals(EnumUbicacion.EXTRANJERO)) {
				propiedades.add(PropBase.crearModuloDebeSerNoNulo(loc.getLocalizacionExtranjero(),
						EnumDomicilio.DOMICILIO_EXTRANJERO.getNombre()));
				propiedades.add(PropBase.crearModuloDebeSerNulo(loc.getLocalizacionMexico(),
						EnumDomicilio.DOMICILIO_MEXICO.getNombre()));
				
				if(loc.getLocalizacionExtranjero() != null) {
					modulos.add(crearLocalizacionExtranjero(loc.getLocalizacionExtranjero()));
				}
			}
		}

		moduloValidar.setListModuloshijos(modulos);
		moduloValidar.setListPropsTovalidate(propiedades);

		return moduloValidar;
	}

    /**
     * Metodo para definir el submodulo de domicilio en Mexico
     *
     * @param domicilioMexico:
     * @return objeto ModuloValidarDTO con las validaciones
     *
     * @author Pavel Alexei Martinez Regalado
     * @since 12/09/2019
     */
	public static ModuloValidarDTO crearDomicilioMexico(DomicilioMexicoDTO domicilioMexico){

		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		propiedades.add(PropDomicilio.crearPropCalle(domicilioMexico.getCalle()));
		propiedades.add(PropDomicilio.crearPropNumExt(domicilioMexico.getNumeroExterior()));
		propiedades.add(PropDomicilio.crearPropNumInt(domicilioMexico.getNumeroInterior()));

		propiedades.add(PropDomicilio.crearPropCodigoPostalMexico(domicilioMexico.getCodigoPostal(),
				domicilioMexico.getMunicipioAlcaldia()));

		propiedades.add(PropDomicilio.crearPropColonia(domicilioMexico.getColoniaLocalidad()));

		propiedades.add(PropBase.crearPropCatalogo(
				EnumDomicilio.ENTIDAD_FED.getNombre(), EnumCatalogos.CAT_ENTIDAD_FEDERATIVA.name(),
				domicilioMexico.getEntidadFederativa(), true));

		propiedades.add(PropBase.crearPropCatalogo(
				EnumDomicilio.MUNICIPIO.getNombre(), EnumCatalogosFk.CAT_MUNICIPIO_ALCALDIA.name(),
				domicilioMexico.getMunicipioAlcaldia(), true));

		ModuloValidarDTO modDomicilioMex = new ModuloValidarDTO(EnumDomicilio.DOMICILIO_MEXICO.getNombre());
		modDomicilioMex.setListPropsTovalidate(propiedades);
		modDomicilioMex.setListModuloshijos(modulos);

		return modDomicilioMex;
	}

    /**
     * Metodo para definir el submodulo de domicilio en el extranjero
     *
     * @param domicilioExt
     * @return objeto ModuloValidarDTO con las validaciones
     *
     * @author Pavel Alexei Martinez Regalado
     * @since 12/09/2019
     */
	public static ModuloValidarDTO crearDomicilioExtranjero(DomicilioExtranjeroDTO domicilioExt){

	    if (domicilioExt == null) {
	    	domicilioExt = new DomicilioExtranjeroDTO();
	    	domicilioExt.setPais(new CatalogoDTO());
	    }

		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		List<ModuloValidarDTO> modulos = new ArrayList<>();

		propiedades.add(PropDomicilio.crearPropCalle(domicilioExt.getCalle()));
		propiedades.add(PropDomicilio.crearPropNumExt(domicilioExt.getNumeroExterior()));
		propiedades.add(PropDomicilio.crearPropNumInt(domicilioExt.getNumeroInterior()));
		propiedades.add(PropDomicilio.crearPropCodigoPostal(domicilioExt.getCodigoPostal()));

		propiedades.add(PropDomicilio.crearPropCiudadLoc(domicilioExt.getCiudadLocalidad()));
		propiedades.add(PropDomicilio.crearPropEstadoProv(domicilioExt.getEstadoProvincia()));

		propiedades.add(PropBase.crearPropCatalogo(
				EnumDomicilio.PAIS.getNombre(), EnumCatalogos.CAT_PAIS.name(),
				domicilioExt.getPais(), true));

		ModuloValidarDTO modDomicilioExt = new ModuloValidarDTO(EnumDomicilio.DOMICILIO_EXTRANJERO.getNombre());
		modDomicilioExt.setListPropsTovalidate(propiedades);
		modDomicilioExt.setListModuloshijos(modulos);

		return modDomicilioExt;
	}

	/**
	 * Metodo para definir el submodulo de ubicacion en el extranjero
	 *
	 * @param locExt
	 * @return objeto ModuloValidarDTO con las validaciones
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 12/09/2019
	 */
	public static ModuloValidarDTO crearLocalizacionExtranjero(LocalizacionExtranjeroDTO locExt){

		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(EnumDomicilio.LOCALIZACION_EXTRANJERO.getNombre());
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		propiedades.add(PropBase.crearPropCatalogo(
				EnumDomicilio.PAIS.getNombre(), EnumCatalogos.CAT_PAIS.name(),
				locExt.getPais(), true));

		moduloValidar.setListPropsTovalidate(propiedades);

		return moduloValidar;
	}

	/**
	 * Metodo para definir el submodulo de ubicacion en Mexico
	 *
	 * @param locMex
	 * @return objeto ModuloValidarDTO con las validaciones
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 12/09/2019
	 */
	public static ModuloValidarDTO crearLocalizacionMexico(LocalizacionMexicoDTO locMex){

		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(EnumDomicilio.LOCALIZACION_MEXICO.getNombre());
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		propiedades.add(PropBase.crearPropCatalogo(
				EnumDomicilio.ENTIDAD_FED.getNombre(), EnumCatalogos.CAT_ENTIDAD_FEDERATIVA.name(),
				locMex.getEntidadFederativa(), true));

		moduloValidar.setListPropsTovalidate(propiedades);

		return moduloValidar;
	}

}
