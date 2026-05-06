/**
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico para el telefono de oficina (número y exten)
 * @author  programador04
 * @since 08/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class TelefonoOficinaDTO extends TelefonoNumeroDTO{

	private String  extension;

	public TelefonoOficinaDTO() {}
	
	public TelefonoOficinaDTO(String numero, String extension) {
		super(numero);
		this.extension = extension;
	}
	
	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public TelefonoOficinaDTO(JsonObject json) {
    	TelefonoOficinaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        TelefonoOficinaDTOConverter.toJson(this, json);
        return json;
    }
    
	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	
	
}
