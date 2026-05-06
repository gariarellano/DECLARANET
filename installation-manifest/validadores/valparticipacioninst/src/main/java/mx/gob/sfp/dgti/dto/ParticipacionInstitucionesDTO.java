/**
 * @ParticipacionInstitucionesDTO.java Nov 14, 2019
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
 * DTO para el módulo de participacion en instituciones
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 26, 2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ParticipacionInstitucionesDTO extends ModuloBaseDTO {

	/**
	 * Bandera que indica si tiene alguna participación en instituciones 
	 */
	private boolean ninguno;
	
	/**
	 * Lista de participaciones en instituciones  
	 */
	private List<ParticipacionDTO> participaciones;
	
	/**
     * Constructor
     */
    public ParticipacionInstitucionesDTO(){ };
    
    public ParticipacionInstitucionesDTO(boolean ninguno, List<ParticipacionDTO> participaciones, String aclaracionesObservaciones, EncabezadoDTO encabezado) {
    	super(encabezado, aclaracionesObservaciones);
    	this.ninguno = ninguno;
    	this.participaciones = participaciones;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public ParticipacionInstitucionesDTO(JsonObject json) {
    	ParticipacionInstitucionesDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ParticipacionInstitucionesDTOConverter.toJson(this, json);
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
	 * @return the participaciones
	 */
	public List<ParticipacionDTO> getParticipaciones() {
		return participaciones;
	}

	/**
	 * @param participaciones the participaciones to set
	 */
	public void setParticipaciones(List<ParticipacionDTO> participaciones) {
		this.participaciones = participaciones;
	}

}
