/**
 * @(#)CorreoDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico para los correos
 * @author Miriam Sánchez Sánchez programador07
 * @since 24/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class CorreoDTO {

	/**
	 * Correo institucional
	 */
	private String institucional;
	
	/**
	 * Correo personal alterno
	 */
	private String personalAlterno;
	
	/**
     * Constructor
     */
    public CorreoDTO(){ };

    public CorreoDTO(String institucional,String personalAlterno) {
        this.institucional = institucional;
        this.personalAlterno = personalAlterno;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public CorreoDTO(JsonObject json) {
    	CorreoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        CorreoDTOConverter.toJson(this, json);
        return json;
    }
	
	/**
	 * @return the institucional
	 */
	public String getInstitucional() {
		return institucional;
	}
	/**
	 * @param institucional the institucional to set
	 */
	public void setInstitucional(String institucional) {
		this.institucional = institucional;
	}
	/**
	 * @return the personalAlterno
	 */
	public String getPersonalAlterno() {
		return personalAlterno;
	}
	/**
	 * @param personalAlterno the personalAlterno to set
	 */
	public void setPersonalAlterno(String personalAlterno) {
		this.personalAlterno = personalAlterno;
	}
	
}
