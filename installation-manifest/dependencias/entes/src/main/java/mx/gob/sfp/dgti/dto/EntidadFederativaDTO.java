/**
 * @(#)EntidadFederativaDTO.java 11/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Clase con el modelo para EntidadFederativaDTO
 * 
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 11/02/2019
 */
@DataObject(generateConverter = true)
public class EntidadFederativaDTO {

	/**
	 * Id de la entidad federativa
	 */
	private Integer idEntidadFederativa;

	/**
	 * Descripcion de la entidad federativa
	 */
	private String entidadFederativaDesc;

	/**
	 * Municipio que contiene la entidad
	 */
	private MunicipioDTO municipio;

	/**
	 * Funcion contructor con parametros
	 * 
	 * @param idEntidadFederativa: id de la entidad federativa
	 * @param municipioDesc: descripcion de la entidad federativa
	 * @param municipio: objeto con el municipio
	 * 
 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 12/02/2019
	 */
	public EntidadFederativaDTO(Integer idEntidadFederativa, String entidadFederativaDesc, MunicipioDTO municipio) {
		this.idEntidadFederativa = idEntidadFederativa;
		this.entidadFederativaDesc = entidadFederativaDesc;
		this.municipio = municipio;
	}

	/**
	 * Funci�n contructor sin parametros
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 12/02/2019
	 */
	public EntidadFederativaDTO() {
	}

	public EntidadFederativaDTO(JsonObject json) {
		EntidadFederativaDTOConverter.fromJson(json, this);
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		EntidadFederativaDTOConverter.toJson(this, json);
		return json;
	}

	public Integer getIdEntidadFederativa() {
		return idEntidadFederativa;
	}

	public EntidadFederativaDTO setIdEntidadFederativa(Integer idEntidadFederativa) {
		this.idEntidadFederativa = idEntidadFederativa;
		return this;
	}

	public String getEntidadFederativaDesc() {
		return entidadFederativaDesc;
	}

	public EntidadFederativaDTO setEntidadFederativaDesc(String entidadFederativaDesc) {
		this.entidadFederativaDesc = entidadFederativaDesc;
		return this;
	}

	public MunicipioDTO getMunicipio() {
		return municipio;
	}

	public EntidadFederativaDTO setMunicipio(MunicipioDTO municipio) {
		this.municipio = municipio;
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
		return "EntidadFederativa [idEntidadFederativa=" + idEntidadFederativa + ", entidadFederativaDesc="
				+ entidadFederativaDesc + ", municipio=" + municipio + "]";
	}

	/**
	 * Funcion hashCode
	 * @return int hashcode
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 04/03/2019
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idEntidadFederativa == null) ? 0 : idEntidadFederativa.hashCode());
		result = prime * result + ((municipio == null) ? 0 : municipio.hashCode());
		return result;
	}

	/**
	 * Funcion equals
	 * 
	 * @return boolean equals
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 04/03/2019
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntidadFederativaDTO other = (EntidadFederativaDTO) obj;
		if (idEntidadFederativa == null) {
			if (other.idEntidadFederativa != null)
				return false;
		} else if (!idEntidadFederativa.equals(other.idEntidadFederativa))
			return false;
		if (municipio == null) {
			if (other.municipio != null)
				return false;
		} else if (!municipio.equals(other.municipio))
			return false;
		return true;
	}

}