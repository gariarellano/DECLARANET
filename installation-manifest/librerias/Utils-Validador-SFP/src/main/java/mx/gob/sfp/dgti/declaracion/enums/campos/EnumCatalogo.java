/**
 * @(#)EnumCatalogo.java 19/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los campos del modulo de catalogo
 *
 * @author Miriam Sanchez Sánchez programador07 
 * @since 19/09/2019
 */
public enum EnumCatalogo {

	ID("id"),
	VALOR("valor"),
	CLAVE_PDN("clavePdn"),
	VALOR_UNO("valorUno");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param campo
	 */
    EnumCatalogo(String campo) {
        this.campo = campo;
    }

	public String getCampo() {
		return campo;
	}
}
