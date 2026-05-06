/**
 * @(#)DatosControlDTO.java 11/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.constantes.Situacion;

/**
 *
 * Clase con el modelo para DatosControlDTO
 * 
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 11/02/2019
 */
@DataObject(generateConverter = true)
public class DatosDeControlDTO {

	/**
	 * Fecha de en que se captura la informacion
	 */
	private String fechaRegistro;

	/**
	 * Fecha del ultimo cambio regresado
	 */
	private String fechaUltimaActualiza;

	/**
	 * Usuario que registra
	 */
	private Integer usuarioRegistra;

	/**
	 * Usuario que actuliza
	 */
	private Integer usuarioActualiza;

	/**
	 * Registro activo, 1 es activo
	 */
	private Integer activo;

	/**
	 * Fecha de baja
	 */
	private String fechaBaja;

	/**
	 * Situacion
	 */
	private Situacion situacion;

	/**
	 * Funcion contructor sin parametros
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 12/02/2019
	 */
	public DatosDeControlDTO() {

	}

	/**
	 * Funcion contructor parametros
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 12/02/2019
	 */
	public DatosDeControlDTO(JsonObject json) {
		DatosDeControlDTOConverter.fromJson(json, this);
	}

	/**
	 * Funcion para pasar el dto a objeto json
	 * 
	 * @return json: objeto json
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 14/02/2019
	 */	
	public static DatosDeControlDTO toObject(JsonObject json) {

		DatosDeControlDTO datosDeControl = new DatosDeControlDTO();
		if(json.getJsonObject("fechaRegistro") != null)
			datosDeControl.fechaRegistro = json.getJsonObject("fechaRegistro").getString("$date");
		if(json.getJsonObject("fechaUltimaActualiza") != null) 
			datosDeControl.fechaUltimaActualiza = json.getJsonObject("fechaUltimaActualiza").getString("$date");
		if(json.getJsonObject("fechaBaja") != null)
			datosDeControl.fechaBaja = json.getJsonObject("fechaBaja").getString("$date");
		datosDeControl.usuarioRegistra = json.getInteger("usuarioRegistra");
		datosDeControl.usuarioActualiza = json.getInteger("usuarioActualiza");
		datosDeControl.activo = json.getInteger("activo");
		datosDeControl.situacion = Situacion.obtenerEnum(json.getString("situacion"));

		return datosDeControl;
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		DatosDeControlDTOConverter.toJson(this, json);
		return json;
	}

	/**
	 * Funcion para pasar el modelo a objeto json
	 * 
	 * @return json: objeto json
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 12/02/2019
	 */
	public JsonObject toJsonMongo() {

		JsonObject json = new JsonObject();
				if(fechaRegistro != null)
					json.put("fechaRegistro", new JsonObject().put("$date",fechaRegistro));
				if(fechaUltimaActualiza != null) 
					json.put("fechaUltimaActualiza", new JsonObject().put("$date",fechaUltimaActualiza));
				if(fechaBaja != null)
					json.put("fechaBaja", new JsonObject().put("$date",fechaBaja));
				if(usuarioRegistra != null)
					json.put("usuarioRegistra", usuarioRegistra);
				if(usuarioActualiza != null)
					json.put("usuarioActualiza", usuarioActualiza);
				if(activo != null)
					json.put("activo", activo);
				if(situacion != null)
					json.put("situacion", situacion);

		return json;
	}

	public String getFechaRegistro() {
		return fechaRegistro;
	}

	public DatosDeControlDTO setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
		return this;
	}

	public String getFechaUltimaActualiza() {
		return fechaUltimaActualiza;
	}

	public DatosDeControlDTO setFechaUltimaActualiza(String fechaUltimaActualiza) {
		this.fechaUltimaActualiza = fechaUltimaActualiza;
		return this;
	}

	public Integer getUsuarioRegistra() {
		return usuarioRegistra;
	}

	public void setUsuarioRegistra(Integer usuarioRegistra) {
		this.usuarioRegistra = usuarioRegistra;
	}

	public Integer getUsuarioActualiza() {
		return usuarioActualiza;
	}

	public void setUsuarioActualiza(Integer usuarioActualiza) {
		this.usuarioActualiza = usuarioActualiza;
	}

	public Integer getActivo() {
		return activo;
	}

	public void setActivo(Integer activo) {
		this.activo = activo;
	}

	public String getFechaBaja() {
		return fechaBaja;
	}

	public Situacion getSituacion() {
		return situacion;
	}

	public void setSituacion(Situacion situacion) {
		this.situacion = situacion;
	}

	public DatosDeControlDTO setFechaBaja(String fechaBaja) {
		this.fechaBaja = fechaBaja;
		return this;
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
		return "DatosDeControlDTO{" +
				"fechaRegistro='" + fechaRegistro + '\'' +
				", fechaUltimaActualiza='" + fechaUltimaActualiza + '\'' +
				", usuarioRegistra=" + usuarioRegistra +
				", usuarioActualiza=" + usuarioActualiza +
				", activo=" + activo +
				", fechaBaja='" + fechaBaja + '\'' +
				", situacion=" + situacion +
				'}';
	}
}