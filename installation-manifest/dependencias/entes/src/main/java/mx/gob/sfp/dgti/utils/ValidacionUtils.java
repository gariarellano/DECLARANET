/**
 * @(#)ValidacionBO.java 11/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils;

import mx.gob.sfp.dgti.dto.ParamConsultaEnteDTO;

/**
 * Utils para ValidacionUtils
 * 
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 28/02/2019
 */
public class ValidacionUtils {
	
	/**
	 * Funcion constructor
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 28/02/2019
	 */
	public ValidacionUtils() {
		
	}

	/**
	 * Funcion para validar los cambios necesarios para realizar la consulta.
	 * 
	 * @param params ParamConsultaEnteDTO para obtener los ids necesarios
	 * @return boolean que indica si esta validado o no
	 * 		true: los datos estan validados
	 * 		false: los datos no estan validados
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 28/02/2019
	 */
	public boolean validarParametrosConsultaEntes(ParamConsultaEnteDTO params) {
		if(
				params.getId() != null
				|| params.getRamo() != null 
				|| params.getUnidadResponsable() != null
				|| params.getNivelGobierno() != null
				|| params.getPoder() != null
				|| params.getIdEntidadFederativa() != null
				|| params.getIdMunicipio() != null) {
			return true;
		}
		return false;
		
	}



}
