/**
 * @ParticipacionFideicomisoDTO.java Nov 14, 2019
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
 * DTO para el módulo de participacion de fideicomisos
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 22, 2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ParticipacionFideicomisoDTO extends ModuloBaseDTO {

	/**
	 * Bandera que indica si tiene fideicomisos a declarar
	 */
	private boolean ninguno;
	
	/**
	 * Lista de fideicomisos a declarar
	 */
	private List<FideicomisoDTO> fideicomisos;
	
	/**
     * Constructor
     */
    public ParticipacionFideicomisoDTO(){ };
    
    public ParticipacionFideicomisoDTO(boolean ninguno, List<FideicomisoDTO> fideicomisos, 
    		String aclaracionesObservaciones, EncabezadoDTO encabezado) {
    	super(encabezado, aclaracionesObservaciones);
    	this.ninguno = ninguno;
    	this.fideicomisos = fideicomisos;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public ParticipacionFideicomisoDTO(JsonObject json) {
    	ParticipacionFideicomisoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ParticipacionFideicomisoDTOConverter.toJson(this, json);
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
	 * @return the fideicomiso
	 */
	public List<FideicomisoDTO> getFideicomisos() {
		return fideicomisos;
	}

	/**
	 * @param fideicomiso the fideicomiso to set
	 */
	public void setFideicomisos(List<FideicomisoDTO> fideicomisos) {
		this.fideicomisos = fideicomisos;
	}


}
