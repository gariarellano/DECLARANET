/**
 * @(#)ParamsSituacionDTO.java 23/04/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.constantes.*;

/**
 * Clase con el modelo para ParamsSituacionDTO
 * parametros para las consultas
 * 
 * @author pavel.martinez
 * @since 23/04/2020
 */
@DataObject(generateConverter = true)
public class ParamsSituacionDTO {

	/**
	 * Id del ramo
	 */
	private String idEnte;

	/**
	 * Id de la unidad responsable
	 */
	private Situacion situacion;


	/**
	 * Funcion contructor sin parametros
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 22/02/2019
	 */
	public ParamsSituacionDTO() {

	}

	public ParamsSituacionDTO(JsonObject json) {
		ParamsSituacionDTOConverter.fromJson(json, this);
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		ParamsSituacionDTOConverter.toJson(this, json);
		return json;
	}

	/**
	 * Mis getters y setters
	 */

	public String getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(String idEnte) {
		this.idEnte = idEnte;
	}

	public Situacion getSituacion() {
		return situacion;
	}

	public void setSituacion(Situacion situacion) {
		this.situacion = situacion;
	}

	/**
	 * Funcion toString
	 * 
	 * @return String: cadena con los campos
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 23/04/2020
	 */
	@Override
	public String toString() {
		return "ParamsSituacionDTO{" +
				"idEnte=" + idEnte +
				", situacion=" + situacion +
				'}';
	}
}