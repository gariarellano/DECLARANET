/**
 * @(#)Propiedades.java 11/11/2019
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
 * @since 11/11/2019
 */
public class Propiedades {

	private Propiedades() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Metodo para crear la propiedad de porcentaje propiedad
	 */
	public static PropiedadesValidarDTO crearPorcentajePropiedad(Integer valor) {

		List<ParametrosValicacionDTO> parametros = new ArrayList<>();
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_MAYORQUE_MENORQUE,-1,101));

		return new PropiedadesValidarDTO(
				EnumCampos.PORCENTAJE_PROPIEDAD.getCampo(),
				valor,
				parametros,
				true);
	}

	/**
	 * Metodo para crear la propiedad de superficio terreno m2
	 */
	public static PropiedadesValidarDTO crearSuperfTerrenoM2(Integer valor) {

		List<ParametrosValicacionDTO> parametros = new ArrayList<>();
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_MAYOR_QUE,0));

		return new PropiedadesValidarDTO(
				EnumCampos.SUPERF_TERRENO_M2.getCampo(),
				valor,
				parametros,
				true);
	}

	/**
	 * Metodo para crear la propiedad de superficio de construccion en m2
	 */
	public static PropiedadesValidarDTO crearSuperfConstruccionM2(Integer valor, boolean esTerreno) {

		List<ParametrosValicacionDTO> parametros = new ArrayList<>();
		if (esTerreno) {
			parametros.add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_MAYOR_QUE,-1));
		} else {
			parametros.add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_MAYOR_QUE, 0));
		}
		return new PropiedadesValidarDTO(
				EnumCampos.SUPERF_CONSTR_M2.getCampo(),
				valor,
				parametros,
				true);
	}

	/**
	 * Metodo para crear la propiedad de dato de identificacion
	 */
	public static PropiedadesValidarDTO crearDatoIdentificacion(String valor) {

		List<ParametrosValicacionDTO> parametros = new ArrayList<>();
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.TRESCIENTOS_UNO.getId()));

		return new PropiedadesValidarDTO(
				EnumCampos.DATO_IDENTIFICACION.getCampo(),
				valor,
				parametros,
				true);
	}

}
