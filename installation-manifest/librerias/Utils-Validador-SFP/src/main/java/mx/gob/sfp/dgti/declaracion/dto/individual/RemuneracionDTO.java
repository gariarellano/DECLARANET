/**
 * @(#)RemuneracionDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.RegistroBaseDTO;

/**
 * DTO generico para remuneracion
 *
 * @author Miriam Sánchez Sánchez programador07
 * @since 05/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class RemuneracionDTO extends RegistroBaseDTO {

	/**
	 * Remuneracion 
	 */
	private MontoMonedaDTO remuneracion;
	
	/**
     * Constructor
     */
    public RemuneracionDTO(){ };
    
    public RemuneracionDTO(MontoMonedaDTO remuneracion) {
    	this.remuneracion = remuneracion;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public RemuneracionDTO(JsonObject json) {
    	RemuneracionDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RemuneracionDTOConverter.toJson(this, json);
        return json;
    }
	
	
	/**
	 * @return the remuneracion
	 */
	public MontoMonedaDTO getRemuneracion() {
		return remuneracion;
	}
	/**
	 * @param remuneracion the remuneracion to set
	 */
	public void setRemuneracion(MontoMonedaDTO remuneracion) {
		this.remuneracion = remuneracion;
	}
}
