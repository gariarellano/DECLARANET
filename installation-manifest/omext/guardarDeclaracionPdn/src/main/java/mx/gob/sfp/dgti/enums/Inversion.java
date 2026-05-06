/**
 * Inversion.java Apr 13, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

/**
 * @author Miriam Sanchez programador07
 * @since Apr 13, 2020
 */
public enum Inversion {

	NINGUNO("ninguno"),
	INVERSION("inversion"),
	TIPO_INVERSION("tipoInversion"),
	SUBTIPO_INVERSION("subTipoInversion"),
	TITULAR("titular"),
	TERCEROS("terceros"),
	CUENTA_CONTRATO("numeroCuentaContrato"),
	LOCALIZACION("localizacionInversion"),
	INSTITUCION_RAZONS("institucionRazonSocial"),	
	SALDO("saldo"), 
	UBICACION("ubicacion"), 
	SALDO_SIT("saldoSituacionActual"), 
	PAIS("pais");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
    Inversion(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}


}
