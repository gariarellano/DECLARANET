/**
 * @(#)EnumSectorPublico.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los campos de sector publico
 * @author Miriam Sánchez Sánchez programador07
 * @since 24/10/2019
 */
public enum EnumSectorPublico {
	SECTOR_PUBLICO("sectorPublico"),
	NIVEL_ORDEN_GOBIERNO("nivelOrdenGobierno"),
	AMBITO_PUBLICO("ambitoPublico"),
	NOMBRE_ENTE_PUBLICO("nombreEntePublico"),
	AREA_ADSCRIPCION("areaAdscripcion"),
	EMPLEO_CARGO_COMISION("empleoCargoComision"),
	FUNCION_PRINCIPAL("funcionPrincipal");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param campo
	 */
	EnumSectorPublico(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}
}
