/**
 * @(#)ExperienciaLaboralDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.RegistroBaseDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.ActividadLaboralDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;

/**
 * DTO generico para el modulo de experiencia laboral
 *
 * @author Miriam Sánchez Sánchez programador07
 * @since 31/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ExperienciaLaboralDTO extends RegistroBaseDTO {
	
	/**
	 * Actividad laboral del declarante
	 */
	private ActividadLaboralDTO actividadLaboral;
    
    /**
     * Constructor
     */
    public ExperienciaLaboralDTO(){ };


    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public ExperienciaLaboralDTO(JsonObject json) {
    	ExperienciaLaboralDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ExperienciaLaboralDTOConverter.toJson(this, json);
        return json;
    }

    public ExperienciaLaboralDTO(ActividadLaboralDTO actividadLaboral, String id, 
    		String idPosicionVista, EnumTipoOperacion tipoOperacion) {
    	super(id, idPosicionVista, tipoOperacion);
    	this.actividadLaboral = actividadLaboral;
    }


	/**
	 * @return the actividadLaboral
	 */
	public ActividadLaboralDTO getActividadLaboral() {
		return actividadLaboral;
	}


	/**
	 * @param actividadLaboral the actividadLaboral to set
	 */
	public void setActividadLaboral(ActividadLaboralDTO actividadLaboral) {
		this.actividadLaboral = actividadLaboral;
	}


}
