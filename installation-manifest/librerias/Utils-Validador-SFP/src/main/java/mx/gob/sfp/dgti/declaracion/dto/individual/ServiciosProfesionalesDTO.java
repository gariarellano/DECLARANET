/**
 * @(#)ServiciosProfesionalesDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico para servicios profesionales
 *
 * @author Miriam Sánchez Sánchez programador07
 * @since 05/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ServiciosProfesionalesDTO extends RemuneracionDTO {

	/**
	 * Tipo de servicio
	 */
	private String tipoServicio;
	
	 /**
     * Constructor
     */
    public ServiciosProfesionalesDTO(){ };
    
    public ServiciosProfesionalesDTO(MontoMonedaDTO remuneracion, String tipoServicio) {
    	super(remuneracion);
    	this.tipoServicio = tipoServicio;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public ServiciosProfesionalesDTO(JsonObject json) {
    	ServiciosProfesionalesDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ServiciosProfesionalesDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the tipoServicio
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}
}
