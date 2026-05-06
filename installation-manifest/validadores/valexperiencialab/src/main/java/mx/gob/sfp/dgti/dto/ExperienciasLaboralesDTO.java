/**
 * @(#)ExperienciasLaboralesDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import java.util.List;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.base.ModuloBaseDTO;

/**
 * DTO generico para el modulo de experiencias laborales
 *
 * @author Miriam Sánchez Sánchez programador07
 * @since 31/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ExperienciasLaboralesDTO extends ModuloBaseDTO {
	
	/**
	 * Bandera que indica si tiene experiencia laboral a declarar
	 */
	private boolean ninguno;
	
	/**
	 * Lista de las experiencias laborales a declarar
	 */
	private List<ExperienciaLaboralDTO> experienciaLaboral;
    
    /**
     * Constructor
     */
    public ExperienciasLaboralesDTO(){ };
    
    public ExperienciasLaboralesDTO(boolean ninguno, List<ExperienciaLaboralDTO> experienciaLaboral, 
    		String aclaracionesObservaciones, EncabezadoDTO encabezado) {
    	super(encabezado, aclaracionesObservaciones);
    	this.ninguno = ninguno;
    	this.experienciaLaboral = experienciaLaboral;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public ExperienciasLaboralesDTO(JsonObject json) {
    	ExperienciasLaboralesDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ExperienciasLaboralesDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the ninguno
	 */
	public boolean isNinguno() {
		return ninguno;
	}

	/**
	 * @param ninguno the ninguno to set
	 */
	public void setNinguno(boolean ninguno) {
		this.ninguno = ninguno;
	}

	/**
	 * @return the experienciaLaboral
	 */
	public List<ExperienciaLaboralDTO> getExperienciaLaboral() {
		return experienciaLaboral;
	}

	/**
	 * @param experienciaLaboral the experienciaLaboral to set
	 */
	public void setExperienciaLaboral(List<ExperienciaLaboralDTO> experienciaLaboral) {
		this.experienciaLaboral = experienciaLaboral;
	}

}
