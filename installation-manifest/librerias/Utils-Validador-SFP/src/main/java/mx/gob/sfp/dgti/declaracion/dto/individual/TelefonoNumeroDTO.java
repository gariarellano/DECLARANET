/**
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico para el telefono (solo numero)
 * @author  programador04
 * @since 08/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class TelefonoNumeroDTO {

	/**
	 * Numero de telefono
	 */
	private String numero;
	
	/**
	 * Constructor
	 * */
	public TelefonoNumeroDTO() {}

	public TelefonoNumeroDTO(String numero ) {
        this.numero = numero;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public TelefonoNumeroDTO(JsonObject json) {
    	TelefonoNumeroDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        TelefonoNumeroDTOConverter.toJson(this, json);
        return json;
    }
	
	
	/**
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	
}
