/**
 * @(#)EnumDatosG.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util;

/**
 * Enum con los campos del módulo de datos generales
 * @author Miriam Sanchez Sánchez programador07 
 * @since 19/09/2019
 */
public enum EnumDatosG {

	DATOS_PERSONALES("datosPersonales"),
	CORREO_ELECTRONICO("correoElectronico"),
	TELEFONO_CASA("telefonoCasa"),
	TELEFONO_CELULAR("telefonoCelular"),
	ESTADO_CIVIL("situacionPersonalEstadoCivil"),
	REGIMEN_MATRIMONIAL("regimenMatrimonial"),
	PAIS_NACIMIENTO("paisNacimiento"),
	NACIONALIDAD("nacionalidad");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
    EnumDatosG(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
