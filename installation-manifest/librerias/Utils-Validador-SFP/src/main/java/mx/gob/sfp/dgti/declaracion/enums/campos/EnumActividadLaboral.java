/**
 * @(#)EnumActividadLaboral.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los campos de actividad laboral
 * @author Miriam Sanchez Sánchez programador07 
 * @since 31/10/2019
 */
public enum EnumActividadLaboral {

	ACTIVIDAD_LABORAL("actividadLaboral"),
	AMBITO_SECTOR("ambitoSector"),
	SECTOR_PUBLICO("sectorPublico"),
	SECTOR_PRIVADO("sectorPrivadoOtro"),
	FECHA_INGRESO("fechaIngreso"),
	FECHA_EGRESO("fechaEgreso"),
	SALARIO("salarioMensualNeto"),
	ESPECIFIQUE("especifique"),
	UBICACION("ubicacion");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param campo
	 */
	EnumActividadLaboral(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}
	
}
