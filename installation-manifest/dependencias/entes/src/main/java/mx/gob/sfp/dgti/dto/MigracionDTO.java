/**
 * @(#)PoderDTO.java 11/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * Clase con el modelo para PoderDTO
 * 
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 11/02/2019
 */
@DataObject(generateConverter = true)
public class MigracionDTO {

	/**
	 * Id del poder
	 */
	private List<EnteDTO> listaEntes;

	/**
	 * Funcion contructor sin parametros
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 12/02/2019
	 */
	public MigracionDTO() {

	}

	public MigracionDTO(JsonObject json) {
		MigracionDTOConverter.fromJson(json, this);
	}

	/**
	 * Funcion contructor con parametros
	 * 
	 * @param idPoder: id del poder
	 * @param poderDesc: descripcion del poder
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 12/02/2019
	 */
	public MigracionDTO(Integer idPoder, String poderDesc) {
		
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		MigracionDTOConverter.toJson(this, json);
		return json;
	}


	/**
	 * Funcion toString
	 * 
	 * @return String: cadena con los campos
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 12/02/2019
	 */
	@Override
	public String toString() {
		return "MigracionDTO [listaEntes=" + listaEntes + "]";
	}

	public List<EnteDTO> getListaEntes() {
		return listaEntes;
	}

	public void setListaEntes(List<EnteDTO> listaEntes) {
		this.listaEntes = listaEntes;
	}

}