/**
 * @ParticipacionEmpresasDTO.java Nov 14, 2019
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
 * DTO para el módulo de participacion en empresas
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 27, 2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ParticipacionEmpresasDTO extends ModuloBaseDTO {

	/**
	 * Bandera que indica si tiene alguna participación en empresas 
	 */
	private boolean ninguno;
	
	/**
	 * Lista de participaciones en empresas  
	 */
	private List<ParticipacionEmpDTO> participaciones;
	
	/**
     * Constructor
     */
    public ParticipacionEmpresasDTO(){ };
    
    public ParticipacionEmpresasDTO(boolean ninguno, List<ParticipacionEmpDTO> participaciones, String aclaracionesObservaciones, EncabezadoDTO encabezado) {
    	super(encabezado, aclaracionesObservaciones);
    	this.ninguno = ninguno;
    	this.participaciones = participaciones;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public ParticipacionEmpresasDTO(JsonObject json) {
    	ParticipacionEmpresasDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ParticipacionEmpresasDTOConverter.toJson(this, json);
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
	public List<ParticipacionEmpDTO> getParticipaciones() {
		return participaciones;
	}

	/**
	 * @param participaciones the participaciones to set
	 */
	public void setParticipaciones(List<ParticipacionEmpDTO> participaciones) {
		this.participaciones = participaciones;
	}

}
