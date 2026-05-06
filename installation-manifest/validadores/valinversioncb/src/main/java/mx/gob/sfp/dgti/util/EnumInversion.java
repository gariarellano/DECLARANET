/**
 * @(#)EnumInversionCB.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util;

/**
 * Enum con los campos del módulo de inversion
 * @author Miriam Sanchez Sánchez programador07 
 * @since 15/11/2019
 */
public enum EnumInversion {

	TIPO_INVERSION("tipoInversion"),
	SUBTIPO_INVERSION("subTipoInversion"),
	TITULAR("titular"),
	TERCEROS("terceros"),
	CUENTA_CONTRATO("numeroCuentaContrato"),
	LOCALIZACION("localizacionInversion"),
	INSTITUCION_RAZONS("institucionRazonSocial"),	
	SALDO("saldo"), 
	UBICACION("ubicacion");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
    EnumInversion(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
