/**
 * @(#)Propiedades.java 10/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validacion;

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
 * @since 10/11/2019
 */
public class Propiedades {

	/**
	 * Metodo para crear la propiedad de Monto
	 */
	public static PropiedadesValidarDTO crearFechaInRepresentacion(String valor) {

		List<ParametrosValicacionDTO> parametros = new ArrayList<>();
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_FORMATO));

		return new PropiedadesValidarDTO(
				EnumCampos.FECHA_IN_REPRESENTACION.getCampo(),
				valor,
				parametros,
				true);
	}

}
