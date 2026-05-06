/**
 * @(#)DatosCurricularesDTO.java 30/05/2019
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
 * DTO para módulo de datos curriculares
 *
 * @author Miriam Sanchez Sanchez
 * @since 07/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DatosCurricularesDTO extends ModuloBaseDTO {

	private List<DatoCurricularDTO> escolaridad;
	
	  /**
     * Constructor
     */
    public DatosCurricularesDTO(){ };
    
    /**
     * Constructor con parametros
     */
    public DatosCurricularesDTO(List<DatoCurricularDTO> escolaridad, String aclaracionesObservaciones, 
    		EncabezadoDTO encabezado) {
    	super(encabezado, aclaracionesObservaciones);
    	this.escolaridad = escolaridad;
    };


    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DatosCurricularesDTO(JsonObject json) {
    	DatosCurricularesDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DatosCurricularesDTOConverter.toJson(this, json);
        return json;
    }


	/**
	 * @return the escolaridad
	 */
	public List<DatoCurricularDTO> getEscolaridad() {
		return escolaridad;
	}


	/**
	 * @param escolaridad the escolaridad to set
	 */
	public void setEscolaridad(List<DatoCurricularDTO> escolaridad) {
		this.escolaridad = escolaridad;
	}

}
