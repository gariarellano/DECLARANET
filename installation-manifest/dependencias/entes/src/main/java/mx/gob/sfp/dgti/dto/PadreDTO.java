/**
 * @(#)PadreDTO.java 14/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Clase con el PadreDTO
 * 
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 14/02/2019
 */
@DataObject(generateConverter = true)
public class PadreDTO {

	/**
	 * Los datos de Control
	 */
	private DatosDeControlDTO datosDeControl;

	/**
	 * Funcion contructor sin parametros
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 14/02/2019
	 */
	public PadreDTO() {

	}

	/**
	 * Funcion contructor con parametros
	 * 
	 * @param datosDeControl: los datos de control
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 12/02/2019
	 */
	public PadreDTO(DatosDeControlDTO datosDeControl) {
		this.datosDeControl = datosDeControl;
	}

	public PadreDTO(JsonObject json) {
		PadreDTOConverter.fromJson(json, this);
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		PadreDTOConverter.toJson(this, json);
		return json;
	}

	/**
	 * Funcion para pasar el dto a objeto json
	 * 
	 * @return json: objeto json
	 * 
     * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 14/02/2019
	 */
	public JsonObject toJsonMongo() {

		JsonObject jsonFecha;
		JsonObject json = this.toJson();
		if (this.getDatosDeControl() != null) {
			jsonFecha = this.getDatosDeControl().toJsonMongo();
			json.put("datosDeControl", jsonFecha);
		} else {
			json.remove("datosDeControl");
		}
		return json;
	}

	public DatosDeControlDTO getDatosDeControl() {
		return datosDeControl;
	}

	public void setDatosDeControl(DatosDeControlDTO datosDeControl) {
		this.datosDeControl = datosDeControl;
	}

	/**
	 * Funcion toString
	 * 
	 * @return String: cadena con los campos
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 14/02/2019
	 */
	@Override
	public String toString() {
			return "PadreDTO [datosDeControl=" + datosDeControl + "]";
	}

}