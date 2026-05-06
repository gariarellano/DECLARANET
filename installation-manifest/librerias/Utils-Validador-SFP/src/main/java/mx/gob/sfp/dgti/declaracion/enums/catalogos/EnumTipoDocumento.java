/**
 * @(#)EnumTipoDocumento.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum del catálogo de documento obtenido
 * @author Miriam Sanchez Sánchez programador07 
 * @since 29/10/2019
 */
public enum EnumTipoDocumento {

	BOLETA(1, "BOLETA"),
	CERTIFICADO(2, "CERTIFICADO"),
	CONSTANCIA(3, "CONSTANCIA"),
	TITULO(4, "TITULO");
	
	private final Integer id;
	private final String descripcion;
	
    /**
     * Constructor con parametros
     * @param id
     * @param descripcion
	 */
	EnumTipoDocumento(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    /**
     * @return the id
     */
	public Integer getId() {
		return id;
	}
	
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
}
