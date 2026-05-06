/**
 * @(#)ActividadDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico para el objeto de actividad laboral
 * @author Miriam Sánchez Sánchez programador07
 * @since 25/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ActividadDTO {

	/**
	 * Catalogo del ambito sector
	 */
	protected CatalogoDTO ambitoSector;

	/**
	 * Otro ambito sector
	 */
	protected String ambitoSectorOtro;

	/**
	 * Fecha de ingreso de esa actividad
	 */
	protected String fechaIngreso;

	/**
     * Constructor
     */
    public ActividadDTO(){ };

	public ActividadDTO(CatalogoDTO ambitoSector, String ambitoSectorOtro, String fechaIngreso) {
		this.ambitoSector = ambitoSector;
		this.ambitoSectorOtro = ambitoSectorOtro;
		this.fechaIngreso = fechaIngreso;
	}

	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     * @param json objeto en JsonObject
     */
    public ActividadDTO(JsonObject json) {
    	ActividadDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ActividadDTOConverter.toJson(this, json);
        return json;
    }

	public CatalogoDTO getAmbitoSector() {
		return ambitoSector;
	}

	public void setAmbitoSector(CatalogoDTO ambitoSector) {
		this.ambitoSector = ambitoSector;
	}

	public String getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getAmbitoSectorOtro() {
		return ambitoSectorOtro;
	}

	public void setAmbitoSectorOtro(String ambitoSectorOtro) {
		this.ambitoSectorOtro = ambitoSectorOtro;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ActividadDTO{");
		sb.append("ambitoSector=").append(ambitoSector);
		sb.append(", ambitoSectorOtro='").append(ambitoSectorOtro).append('\'');
		sb.append(", fechaIngreso='").append(fechaIngreso).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
