/**
 * @Otros.java May 28, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

/**
 * @author Miriam Sanchez programador07
 * @since May 28, 2020
 */
public enum Otros {
	
	OTROS_ID(9999);

	private final Integer id;
	
	
	Otros(Integer id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

}
