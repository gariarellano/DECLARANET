/**
 * @(#)EnteDTO.java 11/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.constantes.Poder;
import mx.gob.sfp.dgti.constantes.SeguridadNacional;
import mx.gob.sfp.dgti.constantes.TipoEntidad;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase con el modelo para EnteDTO
 * 
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 11/02/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class EnteDTO extends PadreDTO {

	/**
	 * Id del ente
	 */
	private String id;

	/**
	 * Ramo del ente
	 */
	private Integer ramo;

	/**
	 * Unidad responsable del ente
	 */
	private String unidadResponsable;

	/**
	 * Descripcion del ente
	 */
	private String enteDesc;

	/**
	 * Nombre corto del ente
	 */
	private String nombreCorto;

	/**
	 * RFC del ente
	 */
	private String rfc;

	/**
	 * Poder al que pertenece el ente
	 */
	private Poder poder;
	
	/**
	 * TipoEntidad al que pertenece el ente
	 */
	private TipoEntidad tipoEntidad;

	/**
	 * Nivel de gobierno al que pertenece el ente
	 */
	private NivelGobiernoDTO nivelGobierno;

	/**
	 * Id del INAI
	 */
	private int identificadorINAI;
	
	/**
	 * Pertenece a seguridad nacional o no
	 * 
	 * 0: no
	 * 1: si
	 */
	private SeguridadNacional segNac;
	

	/**
	 * Id de ente origen
	 */
	private String idEnteOrigen;

	/**
	 * Funcion contructor sin parametros
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 14/02/2019
	 */
	public EnteDTO() {

	}

	public EnteDTO(JsonObject json) {
		EnteDTOConverter.fromJson(json, this);
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		EnteDTOConverter.toJson(this, json);
		return json;
	}


	public static List<JsonObject> toJsonObjectList(List<EnteDTO> list) {
		List<JsonObject> jsonObjects = new ArrayList<>();
		for (EnteDTO ente : list ) {
			jsonObjects.add(ente.toJson());
		}
		return jsonObjects;
	}

	/**
	 * Funcion para pasar el modelo a objeto json
	 * 
	 * @return json: objeto json
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 12/02/2019
	 */
	@Override
	public JsonObject toJsonMongo() {
		JsonObject json = super.toJsonMongo();
		if(this.id != null) {
			json.put("_id", id);
		}
		json.remove("id");
		return json;
	}
	
	/**
	 * Funcion para pasar de json a objeto java
	 * 
	 * @return json: objeto json
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 14/02/2019
	 */
	public static EnteDTO toObject(JsonObject json) {
		//ID de mongo
		String id = json.getString("_id");
		json.remove("_id");
		
		//Datos de control
		DatosDeControlDTO datosDeControl = null;
		if (json.getJsonObject("datosDeControl") != null) {
			datosDeControl = DatosDeControlDTO.toObject(
					json.getJsonObject("datosDeControl"));
		}
		json.remove("datosDeControl");
		
		EnteDTO enteDto = Json.decodeValue(Json.encode(json), EnteDTO.class);
		
		enteDto.id = id;
		enteDto.setDatosDeControl(datosDeControl); 
		
		return enteDto;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public Integer getRamo() {
		return this.ramo;
	}

	public void setRamo(final Integer ramo) {
		this.ramo = ramo;
	}

	public String getUnidadResponsable() {
		return this.unidadResponsable;
	}

	public void setUnidadResponsable(final String unidadResponsable) {
		this.unidadResponsable = unidadResponsable;
	}

	public String getEnteDesc() {
		return this.enteDesc;
	}

	public void setEnteDesc(final String enteDesc) {
		this.enteDesc = enteDesc;
	}

	public String getNombreCorto() {
		return this.nombreCorto;
	}

	public void setNombreCorto(final String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	public String getRfc() {
		return this.rfc;
	}

	public void setRfc(final String rfc) {
		this.rfc = rfc;
	}

	public Poder getPoder() {
		return this.poder;
	}

	public void setPoder(final Poder poder) {
		this.poder = poder;
	}

	public TipoEntidad getTipoEntidad() {
		return this.tipoEntidad;
	}

	public void setTipoEntidad(final TipoEntidad tipoEntidad) {
		this.tipoEntidad = tipoEntidad;
	}

	public NivelGobiernoDTO getNivelGobierno() {
		return this.nivelGobierno;
	}

	public void setNivelGobierno(final NivelGobiernoDTO nivelGobierno) {
		this.nivelGobierno = nivelGobierno;
	}

	public int getIdentificadorINAI() {
		return this.identificadorINAI;
	}

	public void setIdentificadorINAI(final int identificadorINAI) {
		this.identificadorINAI = identificadorINAI;
	}

	public SeguridadNacional getSegNac() {
		return this.segNac;
	}

	public void setSegNac(final SeguridadNacional segNac) {
		this.segNac = segNac;
	}

	public String getIdEnteOrigen() {
		return this.idEnteOrigen;
	}

	public void setIdEnteOrigen(final String idEnteOrigen) {
		this.idEnteOrigen = idEnteOrigen;
	}

	/**
	 * Metodo toString
	 * 
	 * @return string: el string
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 19/03/2019
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("EnteDTO{");
		sb.append("id='").append(id).append('\'');
		sb.append(", ramo=").append(ramo);
		sb.append(", unidadResponsable='").append(unidadResponsable).append('\'');
		sb.append(", enteDesc='").append(enteDesc).append('\'');
		sb.append(", nombreCorto='").append(nombreCorto).append('\'');
		sb.append(", rfc='").append(rfc).append('\'');
		sb.append(", poder=").append(poder);
		sb.append(", tipoEntidad=").append(tipoEntidad);
		sb.append(", nivelGobierno=").append(nivelGobierno);
		sb.append(", identificadorINAI=").append(identificadorINAI);
		sb.append(", segNac=").append(segNac);
		sb.append(", idEnteOrigen='").append(idEnteOrigen).append('\'');
		sb.append(", super='").append(super.toString()).append('\'');
		sb.append('}');
		return sb.toString();
	}
}