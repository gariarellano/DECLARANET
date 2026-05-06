/**
 * @(#)RespuestaDTO.java 19/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.constantes.Respuestas;

/**
 * Clase con el modelo para RespuestaDTO
 * 
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 19/02/2019
 */
@DataObject(generateConverter = true)
public class RespuestaDTO {

	/**
	 * Id de la respuesta
	 */
	private Integer idRespuesta;

	/**
	 * Descripcion de la respuesta
	 */
	private String respuestaDesc;

	/**
	 * Funcion contructor sin parametros
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 19/02/2019
	 */
	public RespuestaDTO() {
		
	}

	/**
	 * Funcion contructor con parametros
	 * 
	 * @param idRespuesta: id del poder
	 * @param respuestaDesc: descripcion del poder
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 12/02/2019
	 */
	public RespuestaDTO(Integer idRespuesta, String respuestaDesc) {
		this.idRespuesta = idRespuesta;
		this.respuestaDesc = respuestaDesc;
	}

	public RespuestaDTO(JsonObject json) {
		RespuestaDTOConverter.fromJson(json, this);
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		RespuestaDTOConverter.toJson(this, json);
		return json;
	}

	/**
	 * Funcion para crear una respuesta json a partir de los 
	 * 
	 * @return json: objeto json
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 19/02/2019
	 */
	public JsonObject crearRespuestaJson(Respuestas resp) {
		return new JsonObject()
				.put("idRespuesta", resp.getId())
				.put("respuestaDesc", resp.getDescripcion());
	}

	public Integer getIdRespuesta() {
		return this.idRespuesta;
	}

	public void setIdRespuesta(final Integer idRespuesta) {
		this.idRespuesta = idRespuesta;
	}

	public String getRespuestaDesc() {
		return this.respuestaDesc;
	}

	public void setRespuestaDesc(final String respuestaDesc) {
		this.respuestaDesc = respuestaDesc;
	}
}