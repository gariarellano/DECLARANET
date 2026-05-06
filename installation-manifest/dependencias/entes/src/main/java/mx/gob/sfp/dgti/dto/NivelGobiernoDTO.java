/**
 * @(#)NivelGobiernoDTO.java 11/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.constantes.NivelGobierno;

import java.util.Objects;

/**
 *
 * Clase con el modelo para NivelGobiernoDTO
 * 
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 11/02/2019
 */
@DataObject(generateConverter = true)
public class NivelGobiernoDTO {

	/**
	 * Descripcion del nivel de gobierno
	 */
	private NivelGobierno nivelGobierno;

	/**
	 * Entidad federativa que contiene el nivel de gobierno
	 */
	private EntidadFederativaDTO entidadFederativa;

	/**
	 * Funcion contructor sin parametros
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 12/02/2019
	 */
	public NivelGobiernoDTO() {

	}

	/**
	 * Funcion contructor con parametros
	 * 
	 * @param nivelGobierno: nivel de gobierno
	 * @param entidadFederativa: objeto con la entidad federativa
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 12/02/2019
	 */
	public NivelGobiernoDTO(final NivelGobierno nivelGobierno, final EntidadFederativaDTO entidadFederativa) {
		this.nivelGobierno = nivelGobierno;
		this.entidadFederativa = entidadFederativa;
	}

	public NivelGobiernoDTO(JsonObject json) {
		NivelGobiernoDTOConverter.fromJson(json, this);
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		NivelGobiernoDTOConverter.toJson(this, json);
		return json;
	}

	public EntidadFederativaDTO getEntidadFederativa() {
		return entidadFederativa;
	}

	public NivelGobiernoDTO setEntidadFederativa(EntidadFederativaDTO entidadFederativa) {
		this.entidadFederativa = entidadFederativa;
		return this;
	}

	public NivelGobierno getNivelGobierno() {
		return this.nivelGobierno;
	}

	public void setNivelGobierno(final NivelGobierno nivelGobierno) {
		this.nivelGobierno = nivelGobierno;
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
		final StringBuilder sb = new StringBuilder("NivelGobiernoDTO{");
		sb.append("nivelGobierno=").append(nivelGobierno);
		sb.append(", entidadFederativa=").append(entidadFederativa);
		sb.append('}');
		return sb.toString();
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
		return Objects.hash(this.nivelGobierno, this.getEntidadFederativa());
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
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof NivelGobiernoDTO)) return false;
		final NivelGobiernoDTO that = (NivelGobiernoDTO) o;
		return this.nivelGobierno == that.nivelGobierno &&
				Objects.equals(this.getEntidadFederativa(), that.getEntidadFederativa());
	}
}