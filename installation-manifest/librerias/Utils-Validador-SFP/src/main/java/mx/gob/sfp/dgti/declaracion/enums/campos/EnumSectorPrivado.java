/**
 * @(#)EnumSectorPrivado.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los campos de sector privado
 * @author Miriam Sánchez Sánchez programador07
 * @since 24/10/2019
 */
public enum EnumSectorPrivado {
	SECTOR_PRIVADO("sectorPrivado"),
	NOMBRE_EMPRESA("nombreEmpresaSociedadAsociacion"),
	RFC("rfc"),
	AREA("area"),
	EMPLEO_CARGO("empleoCargo"),
	SECTOR("sector"),
	FECHA_INGRESO("fechaIngreso"),
	PROVEDOR("proveedorContratistaGobierno");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param campo
	 */
	EnumSectorPrivado(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}
}
