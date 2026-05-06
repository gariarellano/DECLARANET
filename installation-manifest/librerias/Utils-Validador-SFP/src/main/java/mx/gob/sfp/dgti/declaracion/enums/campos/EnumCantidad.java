/**
 * @EnumCantidad.java Dec 9, 2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * @author Miriam Sanchez Sanchez programador07
 * @since Dec 9, 2019
 */
public enum EnumCantidad {

	CERO(0),
	UNO(1),
	TRES(3),
	CINCO(5),
	SEIS(6),
	DIEZ(10),
	ONCE(11),
	TRECE(13),
	QUINCE(15),
	DIECIOCHO(18),
	VEINTE(20),
	TREINTA(30),
	TREINTA_Y_SEIS(36),
	CUARENTA(40),
	CINCUENTA_Y_UNO(51),
	CINCUENTA_Y_SIETE(57),
	CINCUENTA_Y_SEIS(56),
	SETENTA_Y_CINCO(75),
	CIENTO_UNO(101),
	CIENTO_SIETE(107),
	DOSCIENTOS_CINCUENTA(250),
	TRESCIENTOS_UNO(301),
	TRESCIENTOS_SETENTA_Y_NUEVE(379),
	CUETROCIENTOS_UNO(401),
	DOS_MIL_UNO(2001),
	CUATRO_MIL_UNO(4001);
	
	private final Integer id;

    /**
     * Constructor con parametros
     * @param id 
	 */
	EnumCantidad(Integer id) {
        this.id = id;
    }

    /**
     * @return the id
     */
	public Integer getId() {
		return id;
	}
}
