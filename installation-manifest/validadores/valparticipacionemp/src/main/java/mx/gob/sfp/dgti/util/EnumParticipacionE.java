/**
 * @(#)EnumParticipacionE.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util;

/**
 * Enum con los campos del módulo de participacion en empresas
 * @author Miriam Sanchez Sánchez programador07 
 * @since 27/11/2019
 */
public enum EnumParticipacionE {

	PARTICIPANTE("participante"),
	NOMBRE_EMPRESA("nombreEmpresaSociedadAsociacion"),
	PORCENTAJE_PART("porcentajeParticipacion"),
	TIPO_PARTICIPACION("tipoParticipacion"),
	SECTOR("sector"),
	RECIBE_REMUNERACION("recibeRemuneracion"),
	MONTO_MENSUAL("montoMensual"),
	LOCALIZACION("localizacion");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
    EnumParticipacionE(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
