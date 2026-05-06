/**
 * @(#)EnumParticipacionInst.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los campos del módulo de clientes principales
 * @author Miriam Sanchez Sánchez programador07 
 * @since 25/11/2019
 */
public enum EnumParticipacion {

	NINGUNO("ninguno"),
	PARTICIPACIONES("participaciones");
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
    EnumParticipacion(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
