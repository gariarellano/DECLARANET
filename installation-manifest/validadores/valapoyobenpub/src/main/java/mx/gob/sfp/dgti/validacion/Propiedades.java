/**
 * @(#)Propiedades.java 08/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validacion;

import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCantidad;
import mx.gob.sfp.dgti.util.campos.EnumCampos;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase con funciones estaticas que crean los objetos requeridos para la validacion de campos
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 08/11/2019
 */
public class Propiedades {

	private Propiedades() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Metodo para crear la propiedad de Nombre Programa
	 */
	public static PropiedadesValidarDTO crearNombrePrograma(String valor) {

		List<ParametrosValicacionDTO> parametros = new ArrayList<>();
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CIENTO_UNO.getId()));

		return new PropiedadesValidarDTO(
				EnumCampos.NOMBRE_PROGRAMA.getCampo(),
				valor,
				parametros,
				true);
	}

	/**
	 * Metodo para crear la propiedad de institucion otorgante
	 */
	public static PropiedadesValidarDTO crearInstitucionOtorgante(String valor) {

		List<ParametrosValicacionDTO> parametros = new ArrayList<>();
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CIENTO_UNO.getId()));

		return new PropiedadesValidarDTO(
				EnumCampos.INSTITUCION_OTORGANTE.getCampo(),
				valor,
				parametros,
				true);
	}

	/**
	 * Metodo para crear la propiedad de especifique apoyo
	 */
	public static PropiedadesValidarDTO crearEspecifiqueApoyo(String valor) {

		List<ParametrosValicacionDTO> parametros = new ArrayList<>();
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CIENTO_UNO.getId()));

		return new PropiedadesValidarDTO(
				EnumCampos.ESPECIFIQUE_APOYO.getCampo(),
				valor,
				parametros,
				true);
	}

}
