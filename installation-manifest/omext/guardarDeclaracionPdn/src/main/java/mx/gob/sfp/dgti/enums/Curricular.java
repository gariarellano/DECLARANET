/**
 * @(#)EnumCurricular.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

/**
 * Enum con los campos del módulo de datos curriculares
 * @author Miriam Sanchez Sánchez programador07 
 * @since 03/10/2019
 */
public enum Curricular {

	ESCOLARIDAD("escolaridad"),
	NIVEL("nivel"),
	INSTITUCION_EDUCATIVA("institucionEducativa"),
	CARRERA_AREA("carreraAreaConocimiento"),
	ESTATUS("estatus"),
	NOMBRE("nombre"),
	DOCUMENTO_OBTENIDO("documentoObtenido"),
	UBICACION("ubicacion");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
	Curricular(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
