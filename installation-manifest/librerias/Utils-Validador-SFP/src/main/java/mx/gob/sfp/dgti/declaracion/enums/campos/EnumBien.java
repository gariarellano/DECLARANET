/**
 * @(#)EnumBien.java 11/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los nombres de todos los campos utilizados por el validador
 * Un Bien es utilizado por bienes muebles, bienes inmuebles y vehiculos
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 11/11/2019
 */
public enum EnumBien {

	TITULAR("titular"),
	TRANSMISORES("transmisores"),
	TERCEROS("terceros"),
	FECHA_ADQUISICION("fechaAdquisicion"),
	VALOR_ADQUISICION("valorAdquisicion"),
	FORMA_ADQUISICION("formaAdquisicion"),
	FORMA_PAGO("formaPago"),
	MOTIVO_BAJA("motivoBaja"),
	VALOR_VENTA("valorVenta");

	private final String campo;

    /**
     * Constructor
     * @param
	 */
    EnumBien(String nombre) {
        this.campo = nombre;
    }

    /**
     * @return the campo
     */
	public String getNombre() {
		return campo;
	}

}
