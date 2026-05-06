/**
 * @(#)Propiedades.java 09/12/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.validacion;

import mx.gob.sfp.dgti.enums.EnumCampos;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase con funciones estaticas que crean los objetos requeridos para la validacion de campos
 *
 * @author programador04
 * @since 09/12/2019
 */

public class Propiedades {

	private Propiedades() {}
	/**
	 * Metodo para crear la propiedad de marca
	 */
	public static PropiedadesValidarDTO crearMarca(String valor) {

		List<ParametrosValicacionDTO> parametros = new ArrayList<>();
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));

		return new PropiedadesValidarDTO(
				EnumCampos.MARCA.getCampo(),
				valor,
				parametros,
				true);
	}

	/**
	 * Metodo para crear la propiedad de modelo
	 */
	public static PropiedadesValidarDTO crearModelo(String valor) {

		List<ParametrosValicacionDTO> parametros = new ArrayList<>();
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));

		return new PropiedadesValidarDTO(
				EnumCampos.MODELO.getCampo(),
				valor,
				parametros,
				true);
	}

	/**
	 * Metodo para crear la propiedad de numero de serie o registro
	 */
	public static PropiedadesValidarDTO crearNumSerieRegistro(String valor) {

		List<ParametrosValicacionDTO> parametros = new ArrayList<>();
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));

		return new PropiedadesValidarDTO(
				EnumCampos.NUMERO_SERIE_REGISTRO.getCampo(),
				valor,
				parametros,
				true);
	}

}
