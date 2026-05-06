/**
 * @(#)EnumFideicomiso.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util;

/**
 * Enum con los campos del módulo de fideicomisos
 * @author Miriam Sanchez Sánchez programador07 
 * @since 22/11/2019
 */
public enum EnumFideicomiso {

	PARTICIPANTE("participante"),
	TIPO_FIDEICOMISO("tipoFideicomiso"),
	TIPO_PARTICIPACION("tipoParticipacion"),
	RFC_FIDEICOMISO("rfcFideicomiso"),
	FIDEICOMITENTE("fideicomitente"),
	FIDUCIARIO("fiduciario"),
	FIDEICOMISARIO("fideicomisario"),
	SECTOR("sector"),
	LOCALIZACION("localizacion");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
    EnumFideicomiso(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
