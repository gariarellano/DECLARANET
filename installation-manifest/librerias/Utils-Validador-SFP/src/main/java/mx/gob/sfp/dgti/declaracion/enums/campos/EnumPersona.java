/**
 * @(#)EnumPersona.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los campos de tercero
 * @author Miriam Sanchez Sánchez programador07 
 * @since 29/10/2019
 */
public enum EnumPersona {

	TIPO_PERSONA("tipoPersona"),
	PERSONA_FISICA("personaFisica"),
	PERSONA_MORAL("personaMoral");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
	EnumPersona(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
