/**
 * @(#)EnumTransmisor.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los campos de transmisor
 * @author Miriam Sánchez Sánchez programador07
 * @since 24/10/2019
 */
public enum EnumTransmisor {

	RELACION_CON_TITULAR("relacionConTitular"),
	TIPO_PERSONA("tipoPersona"),
	PERSONA_FISICA("personaFisica"),
	PERSONA_MORAL("personaMoral");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
	EnumTransmisor(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}
}
