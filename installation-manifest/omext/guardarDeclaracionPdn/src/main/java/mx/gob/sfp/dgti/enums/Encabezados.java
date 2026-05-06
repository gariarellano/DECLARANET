/**
 * @EnumVarios.java Mar 23, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.enums;

/**
 * @author Miriam Sanchez Sanchez programador07
 * @since Mar 23, 2020
 */

public enum Encabezados {

	ID("id"), 
	VERIFICAR("verificar"), 
	ID_("_id"),
	FECHA_ACTUALIZACION("fechaActualizacion"),
	INSTITUCION_RECEPTORA("institucionReceptora"),
	NOMBRE("nombre"),
	TIPO_DECLARACION("tipoDeclaracion"),
	TIPO_FORMATO("tipoFormato"),
	CLAVE("clave"),
	CLAVE_PDN("clavePdn"),
	ENTE("ente"),
	FK("fk"), 
	ID_TITULAR_DECLARANTE("1"), 
	RECEPCION_WEB("recepcionWeb"), 
	ENCABEZADO("encabezado"),
	NUM_DEC("numeroDeclaracion"), 
	COLL_NAME("collName")
	;

	private final String campo;

	/**
	 * Constructor con parametros
	 * 
	 * @param campo
	 */
	Encabezados(String campo) {
		this.campo = campo;
	}

	/**
	 * @return the campo
	 */
	public String getCampo() {
		return campo;
	}

}
