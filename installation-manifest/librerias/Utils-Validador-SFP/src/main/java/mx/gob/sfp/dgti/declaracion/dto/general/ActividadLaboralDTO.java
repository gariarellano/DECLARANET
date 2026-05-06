/**
 * @(#)ActividadLaboralDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.general;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.individual.ActividadDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.SectorPrivadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.SectorPublicoDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumUbicacion;

/**
 * DTO generico para el objeto de actividad laboral
 * @author Miriam Sánchez Sánchez programador07
 * @since 25/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ActividadLaboralDTO extends ActividadDTO {

	/**
	 * Sector publico 
	 */
	private SectorPublicoDTO sectorPublico;
	
	/** 
	 * Sector privado o otro 
	 */
	private SectorPrivadoDTO sectorPrivadoOtro;
	
	/**
	 * Fecha de egreso de la actividad laboral
	 */
	private String fechaEgreso;
	
	/**
	 * Ubicacion de la actividad laboral
	 */
	private EnumUbicacion ubicacion;

	/**
     * Constructor
     */
    public ActividadLaboralDTO(){};

	public ActividadLaboralDTO(CatalogoDTO ambitoSector, String ambitoSectorOtro, String fechaIngreso,
							   SectorPublicoDTO sectorPublico, SectorPrivadoDTO sectorPrivadoOtro, String fechaEgreso,
							   EnumUbicacion ubicacion) {
		super(ambitoSector, ambitoSectorOtro, fechaIngreso);
		this.sectorPublico = sectorPublico;
		this.sectorPrivadoOtro = sectorPrivadoOtro;
		this.fechaEgreso = fechaEgreso;
		this.ubicacion = ubicacion;
	}

	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     * @param json objeto en JsonObject
     */
    public ActividadLaboralDTO(JsonObject json) {
    	ActividadLaboralDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ActividadLaboralDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the sectorPublico
	 */
	public SectorPublicoDTO getSectorPublico() {
		return sectorPublico;
	}

	/**
	 * @param sectorPublico the sectorPublico to set
	 */
	public void setSectorPublico(SectorPublicoDTO sectorPublico) {
		this.sectorPublico = sectorPublico;
	}

	/**
	 * @return the sectorPrivadoOtro
	 */
	public SectorPrivadoDTO getSectorPrivadoOtro() {
		return sectorPrivadoOtro;
	}

	/**
	 * @param sectorPrivadoOtro the sectorPrivadoOtro to set
	 */
	public void setSectorPrivadoOtro(SectorPrivadoDTO sectorPrivadoOtro) {
		this.sectorPrivadoOtro = sectorPrivadoOtro;
	}

	/**
	 * @return the fechaEgreso
	 */
	public String getFechaEgreso() {
		return fechaEgreso;
	}

	/**
	 * @param fechaEgreso the fechaEgreso to set
	 */
	public void setFechaEgreso(String fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}

	/**
	 * @return the ubicacion
	 */
	public EnumUbicacion getUbicacion() {
		return ubicacion;
	}

	/**
	 * @param ubicacion the ubicacion to set
	 */
	public void setUbicacion(EnumUbicacion ubicacion) {
		this.ubicacion = ubicacion;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ActividadLaboralDTO{");
		sb.append("sectorPublico=").append(sectorPublico);
		sb.append(", sectorPrivadoOtro=").append(sectorPrivadoOtro);
		sb.append(", fechaEgreso='").append(fechaEgreso).append('\'');
		sb.append(", ubicacion=").append(ubicacion);
		sb.append(", super=").append(super.toString());
		sb.append('}');
		return sb.toString();
	}
}
