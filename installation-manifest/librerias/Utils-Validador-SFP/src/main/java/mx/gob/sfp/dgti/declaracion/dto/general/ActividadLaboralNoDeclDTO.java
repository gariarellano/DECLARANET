/**
 * @(#)ActividadLaboralNoDeclDTO.java 30/05/2019
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
import mx.gob.sfp.dgti.declaracion.dto.individual.SectorPrivadoProvedorDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.SectorPublicoDTO;

/**
 * DTO generico para el objeto de actividad laboral
 * @author Miriam Sánchez Sánchez programador07
 * @since 25/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ActividadLaboralNoDeclDTO extends ActividadDTO {

	/**
	 * Sector publico
	 */
	private SectorPublicoDTO sectorPublico;
	
	/**
	 * Sector privado o otro
	 */
	private SectorPrivadoProvedorDTO sectorPrivadoOtro;
	
	/**
	 * Salario mensual neto
	 */
	private Integer salarioMensualNeto;

	/**
     * Constructor
     */
    public ActividadLaboralNoDeclDTO(){};

	public ActividadLaboralNoDeclDTO(CatalogoDTO ambitoSector, String ambitoSectorOtro, String fechaIngreso,
									 SectorPublicoDTO sectorPublico, SectorPrivadoProvedorDTO sectorPrivadoOtro,
									 Integer salarioMensualNeto) {
		super(ambitoSector, ambitoSectorOtro, fechaIngreso);
		this.sectorPublico = sectorPublico;
		this.sectorPrivadoOtro = sectorPrivadoOtro;
		this.salarioMensualNeto = salarioMensualNeto;
	}

	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     * @param json objeto en JsonObject
     */
    public ActividadLaboralNoDeclDTO(JsonObject json) {
    	ActividadLaboralNoDeclDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ActividadLaboralNoDeclDTOConverter.toJson(this, json);
        return json;
    }

	public SectorPublicoDTO getSectorPublico() {
		return sectorPublico;
	}

	public void setSectorPublico(SectorPublicoDTO sectorPublico) {
		this.sectorPublico = sectorPublico;
	}

	public SectorPrivadoProvedorDTO getSectorPrivadoOtro() {
		return sectorPrivadoOtro;
	}

	public void setSectorPrivadoOtro(SectorPrivadoProvedorDTO sectorPrivadoOtro) {
		this.sectorPrivadoOtro = sectorPrivadoOtro;
	}

	public Integer getSalarioMensualNeto() {
		return salarioMensualNeto;
	}

	public void setSalarioMensualNeto(Integer salarioMensualNeto) {
		this.salarioMensualNeto = salarioMensualNeto;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ActividadLaboralNoDeclDTO{");
		sb.append("sectorPublico=").append(sectorPublico);
		sb.append(", sectorPrivadoOtro=").append(sectorPrivadoOtro);
		sb.append(", salarioMensualNeto=").append(salarioMensualNeto);
		sb.append(", ambitoSector=").append(ambitoSector);
		sb.append(", fechaIngreso='").append(fechaIngreso).append('\'');
		sb.append(", super='").append(super.toString()).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
