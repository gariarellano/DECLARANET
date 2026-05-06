/**
 * @(#)DomicilioExtranjeroDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico para el telefono
 * @author Miriam Sánchez Sánchez programador07
 * @since 24/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class TelefonoDTO  extends TelefonoNumeroDTO{

	/**
	 * El pais al que pertenece el numero celular personal
	 */
	private CatalogoDTO paisCelularPersonal;
	
	/**
     * Constructor
     */
    public TelefonoDTO(){ };

    public TelefonoDTO(String numero, CatalogoDTO paisCelularPersonal) {
        super(numero);
        this.paisCelularPersonal = paisCelularPersonal;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public TelefonoDTO(JsonObject json) {
    	TelefonoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        TelefonoDTOConverter.toJson(this, json);
        return json;
    }
	
	/**
	 * @return the paisCelularPersonal
	 */
	public CatalogoDTO getPaisCelularPersonal() {
		return paisCelularPersonal;
	}
	/**
	 * @param paisCelularPersonal the paisCelularPersonal to set
	 */
	public void setPaisCelularPersonal(CatalogoDTO paisCelularPersonal) {
		this.paisCelularPersonal = paisCelularPersonal;
	}
	
}