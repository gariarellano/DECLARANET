/**
 * @(#)ActividadAnualDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.general;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.individual.*;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoRemuneracion;

import java.util.List;

/**
 * DTO generico para el modulo de actividad anual anterior
 *
 * @author Miriam Sánchez Sánchez programador07
 * @since 05/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ActividadAnualDTO extends IngresosNetosDeclaranteDTO {
	
	/**
	 * Fecha de inicio de la actividad
	 */
	private String fechaInicio;
	
	/**
	 * Fecha de conclusion de la actividad
	 */
	private String fechaConclusion;
	
    /**
     * Constructor
     */
    public ActividadAnualDTO(){ };
    
    public ActividadAnualDTO(EnumTipoRemuneracion tipoRemuneracion, MontoMonedaDTO remuneracionNetaCargoPublico, MontoMonedaDTO otrosIngresosTotal,
    		List<ActividadIndustrialCEDTO> actividadIndustrialComercialEmpresarial, List<ActividadFinancieraDTO> actividadFinanciera, 
    		List<ServiciosProfesionalesDTO> serviciosProfesionales, List<OtrosIngresosDTO> otrosIngresos, RemuneracionDTO ingresoNetoDeclarante,
    		RemuneracionDTO ingresoNetoParejaDependiente, RemuneracionDTO totalIngresosNetos,String fechaInicio, String fechaConclusion, List<EnajenacionBienDTO> enajenacionBienes) {
    	super(tipoRemuneracion, remuneracionNetaCargoPublico, otrosIngresosTotal, actividadIndustrialComercialEmpresarial, actividadFinanciera,
    			serviciosProfesionales, otrosIngresos, ingresoNetoDeclarante, ingresoNetoParejaDependiente, totalIngresosNetos, enajenacionBienes);
    	this.fechaInicio = fechaInicio;
    	this.fechaConclusion = fechaConclusion;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public ActividadAnualDTO(JsonObject json) {
    	ActividadAnualDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ActividadAnualDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the fechaInicio
	 */
	public String getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * @param fechaInicio the fechaInicio to set
	 */
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * @return the fechaConclusion
	 */
	public String getFechaConclusion() {
		return fechaConclusion;
	}

	/**
	 * @param fechaConclusion the fechaConclusion to set
	 */
	public void setFechaConclusion(String fechaConclusion) {
		this.fechaConclusion = fechaConclusion;
	}

}
