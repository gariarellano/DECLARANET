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
import mx.gob.sfp.dgti.constantes.*;

/**
 * Clase con el modelo para ParamConsultaDTO, utilizado para ingresar los
 * parametros para las consultas
 * 
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 22/02/2019
 */
@DataObject(generateConverter = true)
public class ParamConsultaEnteDTO {

	/**
	 * Id del ramo
	 */
	private Integer ramo;

	/**
	 * Id de la unidad responsable
	 */
	private String unidadResponsable;

	/**
	 * Enum del poder
	 */
	private Poder poder;

	/**
	 * Enum del nivel de gobierno
	 */
	private NivelGobierno nivelGobierno;

	/**
	 * Id de la entidad federativa
	 */
	private Integer idEntidadFederativa;

	/**
	 * Id del municipio
	 */
	private Integer idMunicipio;

	/**
	 * Enum del tipo de entidad
	 */
	private TipoEntidad tipoEntidad;

	/**
	 * Id del ente
	 */
	private String id;

	/**
	 * Pertenece a seguridad nacional o no
	 * 
	 * 0: no
	 * 1: si
	 */
	private SeguridadNacional segNac;

	/**
	 * Enum de situacion
	 */
	private Situacion situacion;
	
	
	/**
	 * Funcion contructor sin parametros
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 22/02/2019
	 */
	public  ParamConsultaEnteDTO() {
		
	}

	public ParamConsultaEnteDTO(JsonObject json) {
		ParamConsultaEnteDTOConverter.fromJson(json, this);
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		ParamConsultaEnteDTOConverter.toJson(this, json);
		return json;
	}

	/**
	 * Mis getters y setters
	 */

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

	public Poder getPoder() {
		return this.poder;
	}

	public void setPoder(final Poder poder) {
		this.poder = poder;
	}

	public NivelGobierno getNivelGobierno() {
		return this.nivelGobierno;
	}

	public void setNivelGobierno(final NivelGobierno nivelGobierno) {
		this.nivelGobierno = nivelGobierno;
	}

	public Integer getIdEntidadFederativa() {
		return this.idEntidadFederativa;
	}

	public void setIdEntidadFederativa(final Integer idEntidadFederativa) {
		this.idEntidadFederativa = idEntidadFederativa;
	}

	public Integer getIdMunicipio() {
		return this.idMunicipio;
	}

	public void setIdMunicipio(final Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	public TipoEntidad getTipoEntidad() {
		return this.tipoEntidad;
	}

	public void setTipoEntidad(final TipoEntidad tipoEntidad) {
		this.tipoEntidad = tipoEntidad;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public SeguridadNacional getSegNac() {
		return this.segNac;
	}

	public void setSegNac(final SeguridadNacional segNac) {
		this.segNac = segNac;
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
	 * @since 12/02/2019
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ParamConsultaEnteDTO{");
		sb.append("ramo=").append(ramo);
		sb.append(", unidadResponsable='").append(unidadResponsable).append('\'');
		sb.append(", poder=").append(poder);
		sb.append(", nivelGobierno=").append(nivelGobierno);
		sb.append(", idEntidadFederativa=").append(idEntidadFederativa);
		sb.append(", idMunicipio=").append(idMunicipio);
		sb.append(", tipoEntidad=").append(tipoEntidad);
		sb.append(", id='").append(id).append('\'');
		sb.append(", segNac=").append(segNac);
		sb.append('}');
		return sb.toString();
	}
}