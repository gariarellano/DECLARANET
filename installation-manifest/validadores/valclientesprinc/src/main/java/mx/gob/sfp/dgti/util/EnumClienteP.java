/**
 * @(#)EnumClienteP.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util;

/**
 * Enum con los campos del módulo de clientes principales
 * @author Miriam Sanchez Sánchez programador07 
 * @since 25/11/2019
 */
public enum EnumClienteP {

	NINGUNO("ninguno"),
	CLIENTES("clientes");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
    EnumClienteP(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
