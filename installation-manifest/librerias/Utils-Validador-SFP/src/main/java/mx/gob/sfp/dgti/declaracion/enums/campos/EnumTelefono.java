/**
 * @(#)EnumTelefono.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los campos de telefono
 * @author Miriam Sanchez Sánchez programador07 
 * @since 29/10/2019
 */
public enum EnumTelefono {
	
	NUMERO("numero"),
	PAIS_CELULAR_PERSONAL("paisCelularPersonal");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
	EnumTelefono(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
