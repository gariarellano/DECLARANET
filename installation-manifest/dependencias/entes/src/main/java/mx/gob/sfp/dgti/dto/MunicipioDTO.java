/**
 * @(#)MunicipioDTO.java 11/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Clase con el modelo para MunicipioDTO
 * 
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 11/02/2019
 */
@DataObject(generateConverter = true)
public class MunicipioDTO {

	/**
	 * Id del municipio
	 */
	private Integer idMunicipio;

	/**
	 * Descripcion del municipio
	 */
	private String municipioDesc;

	/**
	 * Funcion contructor sin parametros
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 12/02/2019
	 */
	public MunicipioDTO() {}

	/**
	 * Funcion contructor con parametros
	 * 
	 * @param idMunicipio: id del municipio
	 * @param municipioDesc: descripcion del municipio
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 12/02/2019
	 */
	public MunicipioDTO(Integer idMunicipio, String municipioDesc) {
		this.idMunicipio = idMunicipio;
		this.municipioDesc = municipioDesc;
	}

	public MunicipioDTO(JsonObject json) {
		MunicipioDTOConverter.fromJson(json, this);
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		MunicipioDTOConverter.toJson(this, json);
		return json;
	}

	public Integer getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	public String getMunicipioDesc() {
		return municipioDesc;
	}

	public void setMunicipioDesc(String municipioDesc) {
		this.municipioDesc = municipioDesc;
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
		return "Municipio [idMunicipio=" + idMunicipio + ", municipioDesc=" + municipioDesc + "]";
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
		result = prime * result + ((idMunicipio == null) ? 0 : idMunicipio.hashCode());
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
		MunicipioDTO other = (MunicipioDTO) obj;
		if (idMunicipio == null) {
			if (other.idMunicipio != null)
				return false;
		} else if (!idMunicipio.equals(other.idMunicipio))
			return false;
		return true;
	}
	
}