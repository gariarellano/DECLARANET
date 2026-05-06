/**
 * @(#)PersonaMoralDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico para las personas morales
 * @author Miriam Sánchez Sánchez programador07
 * @since 24/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class PersonaMoralDTO {
	
	/**
	 * RFC de la persona moral
	 */
	private String rfc;
	
	/**
	 * Nombre o denominacion de la persona moral
	 */
	private String nombre;
	
	/**
     * Constructor
     */
    public PersonaMoralDTO(){ };

    public PersonaMoralDTO(String rfc, String nombre) {
        this.nombre = nombre;
        this.rfc = rfc;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public PersonaMoralDTO(JsonObject json) {
    	PersonaMoralDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        PersonaMoralDTOConverter.toJson(this, json);
        return json;
    }
	
	/**
	 * @return the rfc
	 */
	public String getRfc() {
		return rfc;
	}
	/**
	 * @param rfc the rfc to set
	 */
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}
