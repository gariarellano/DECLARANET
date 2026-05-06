/**
 * @EnumResult.java Mar 23, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.enums;

/**
 * @author Miriam Sanchez Sanchez programador07
 * @modifiedBy programador09
 * @since Mar 23, 2020
 */
public enum CamposPDN {

	ID("id"),
	METADATA("metadata"),
	DECLARACION("declaracion"),
	
	FECHA_ACT("actualizacion"),
	TIPO("tipo"),
        TIPO_ENUM("tipoEnum"),
        TIPO_FORMATO("tipoFormato"),
        DECLARACION_COMPLETA("declaracionCompleta"),
	INSTITUCION("institucion"),
	
	SITUACION_PATRIMONIAL("situacionPatrimonial"),
	INTERESES("interes"),
	
	CLAVE_PDN("clave"),
	PUESTO("puesto"),
	FORMATO("tipoFormato"),
	
	HOMOCLAVE("homoClave"), 
	UNIDAD("unidad"),
	MOTIVO_BAJA("motivoBaja")
	;
	
	
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
	CamposPDN(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}
}