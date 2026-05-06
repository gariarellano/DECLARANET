/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;


/**
 * Enum que contiene los niveles de gobiero.
 * 
 * @author cgarias
 * @since 21/11/2019
 */
public enum EnumNivelGobierno {
    FEDERAL(1, "FEDERAL"),
    ESTATAL(2, "ESTATAL"),
    MUNICIPAL(3, "MUNICIPAL/ALCALDÍA");
    
	private final Integer id;
	private final String descripcion;
	
	 /**
     * Constructor
     * @param id
     */
	EnumNivelGobierno(Integer id, String descripcion) {
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
