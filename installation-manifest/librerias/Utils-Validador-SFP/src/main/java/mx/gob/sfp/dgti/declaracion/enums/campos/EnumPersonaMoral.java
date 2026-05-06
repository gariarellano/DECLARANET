/**
 * @(#)EnumPersonaMoral.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los campos de persona moral
 * @author Miriam Sánchez Sánchez programador07
 * @since 24/10/2019
 */
public enum EnumPersonaMoral {

	RFC("rfc"),
	NOMBRE("nombre");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
	EnumPersonaMoral(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}
}
