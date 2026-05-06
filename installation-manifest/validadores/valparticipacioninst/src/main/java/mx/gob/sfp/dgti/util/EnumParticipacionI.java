/**
 * @(#)EnumParticipacion.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util;

/**
 * Enum con los campos del módulo de participacion en instituciones
 * @author Miriam Sanchez Sánchez programador07 
 * @since 26/11/2019
 */
public enum EnumParticipacionI {

	PARTICIPANTE("participante"),
	TIPO_INSTITUCION("tipoInstitucion"),
	INSTITUCION("institucion"),
	PUESTO_ROL("puestoRol"),
	FECHA_INICIO("fechaInicioParticipacion"),
	RECIBE_REMUNERACION("recibeRemuneracion"),
	MONTO_MENSUAL("montoMensual"),
	LOCALIZACION("localizacion");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
    EnumParticipacionI(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
