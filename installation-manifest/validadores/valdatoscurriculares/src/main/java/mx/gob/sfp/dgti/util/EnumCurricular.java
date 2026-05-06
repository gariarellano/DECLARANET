/**
 * @(#)EnumCurricular.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util;

/**
 * Enum con los campos del módulo de datos curriculares
 * @author Miriam Sanchez Sánchez programador07 
 * @since 03/10/2019
 */
public enum EnumCurricular {

	ESCOLARIDAD("escolaridad"),
	NIVEL("nivel"),
	INSTITUCION_EDUCATIVA("institucionEducativa"),
	CARRERA_AREA("carreraAreaConocimiento"),
	ESTATUS("estatus"),
	DOCUMENTO_OBTENIDO("documentoObtenido"),
	UBICACION("ubicacion");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
	EnumCurricular(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
