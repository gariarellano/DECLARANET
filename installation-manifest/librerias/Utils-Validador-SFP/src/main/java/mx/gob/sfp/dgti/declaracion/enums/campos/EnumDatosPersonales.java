/**
 * @(#)EnumDatosPersonales.java 29/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los campos de datos personales
 * @author Miriam Sanchez Sánchez programador07 
 * @since 29/10/2019
 */
public enum EnumDatosPersonales {
	DATOS_PERSONALES("datosPersonales"),
	NOMBRE("nombre"),
	PRIMER_APELLIDO("primerApellido"),
	SEGUNDO_APELLIDO("segundoApellido"),
	CURP("curp"),
	FECHA_NACIMIENTO("fechaNacimiento"),
	RFC("rfc");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param campo
	 */
	EnumDatosPersonales(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}
}
