/**
 * @(#)EnumDocObtenido.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util;

/**
 * Enum con los campos de documento obtenido
 * @author Miriam Sanchez Sánchez programador07 
 * @since 29/10/2019
 */
public enum EnumDocObtenido {

	TIPO("tipo"),
	FECHA_OBTENCION("fechaObtencion");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
	EnumDocObtenido(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}
}
