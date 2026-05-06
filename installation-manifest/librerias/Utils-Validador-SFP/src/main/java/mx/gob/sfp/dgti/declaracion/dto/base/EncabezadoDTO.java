/**
 * @(#)EncabezadoDTO.java 13/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.base;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoUnoFkDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoFormatoDeclaracion;

/**
 * DTO del encabezado de la declaracion con informacion que puede ser utilizada
 * para validacion
 *
 * @author pavel.martinez
 * @since 13/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class EncabezadoDTO {

	/**
	 * Numero de la declaracion
	 */
	private String numeroDeclaracion;

	/**
	 * Tipo de la declaracion
	 */
	private EnumTipoDeclaracion tipoDeclaracion;

	/**
	 * Anio de la declaracion
	 */
	private Integer anio;

	/**
	 * Fecha del encargo
	 */
	private String fechaEncargo;

	/**
	 * Tipo formato
	 */
	private EnumTipoFormatoDeclaracion tipoFormato;

	/**
	 *NivelJerarquico
	 */
	private CatalogoUnoFkDTO nivelJerarquico;
	
	/**
	 * Constructor
	 */
	public EncabezadoDTO() {
	};

	public EncabezadoDTO(String numeroDeclaracion, EnumTipoDeclaracion tipoDeclaracion, Integer anio,
			String fechaEncargo, EnumTipoFormatoDeclaracion tipoFormato, CatalogoUnoFkDTO nivelJerarquico) {
		this.numeroDeclaracion = numeroDeclaracion;
		this.tipoDeclaracion = tipoDeclaracion;
		this.anio = anio;
		this.fechaEncargo = fechaEncargo;
		this.tipoFormato = tipoFormato;
		this.nivelJerarquico = nivelJerarquico;
	}

	public EncabezadoDTO(String numeroDeclaracion, EnumTipoDeclaracion tipoDeclaracion, Integer anio,
			String fechaEncargo, CatalogoUnoFkDTO nivelJerarquico) {
		this.numeroDeclaracion = numeroDeclaracion;
		this.tipoDeclaracion = tipoDeclaracion;
		this.anio = anio;
		this.fechaEncargo = fechaEncargo;
		this.nivelJerarquico = nivelJerarquico;
	}

	/**
	 * Constructor desde JsonObject, utiliza convertidores generados
	 *
	 * @param json: objeto en JsonObject
	 */
	public EncabezadoDTO(JsonObject json) {
		EncabezadoDTOConverter.fromJson(json, this);
	}

	/**
	 * Metodo para obtener el JsonObject, utiliza convertidores generados
	 *
	 * @return JsonObject a partir del objeto
	 */
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		EncabezadoDTOConverter.toJson(this, json);
		return json;
	}

	public String getNumeroDeclaracion() {
		return numeroDeclaracion;
	}

	public void setNumeroDeclaracion(String numeroDeclaracion) {
		this.numeroDeclaracion = numeroDeclaracion;
	}

	public EnumTipoDeclaracion getTipoDeclaracion() {
		return tipoDeclaracion;
	}

	public void setTipoDeclaracion(EnumTipoDeclaracion tipoDeclaracion) {
		this.tipoDeclaracion = tipoDeclaracion;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public String getFechaEncargo() {
		return fechaEncargo;
	}

	public void setFechaEncargo(String fechaEncargo) {
		this.fechaEncargo = fechaEncargo;
	}

	public EnumTipoFormatoDeclaracion getTipoFormato() {
		return tipoFormato;
	}

	public void setTipoFormato(EnumTipoFormatoDeclaracion tipoFormato) {
		this.tipoFormato = tipoFormato;
	}
	
	

	/**
	 * @return the nivelJerarquico
	 */
	public CatalogoUnoFkDTO getNivelJerarquico() {
		return nivelJerarquico;
	}

	/**
	 * @param nivelJerarquico the nivelJerarquico to set
	 */
	public void setNivelJerarquicoActual(CatalogoUnoFkDTO nivelJerarquico) {
		this.nivelJerarquico = nivelJerarquico;
	}

	/**
	 * Metodo toString
	 *
	 * @return toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("EncabezadoDTO{");
		sb.append("numeroDeclaracion='").append(numeroDeclaracion).append('\'');
		sb.append(", tipoDeclaracion=").append(tipoDeclaracion);
		sb.append(", anio=").append(anio);
		sb.append(", fechaEncargo='").append(fechaEncargo).append('\'');
		sb.append(", tipoFormato='").append(tipoFormato).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
