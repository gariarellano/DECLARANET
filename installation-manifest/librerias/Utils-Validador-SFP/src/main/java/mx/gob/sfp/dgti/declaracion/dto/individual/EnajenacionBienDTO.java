/**
 * @(#)EnajenacionBienDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoBien;

/**
 * DTO generico para enajenación de bienes
 *
 * @author Miriam Sánchez Sánchez programador07
 * @since 05/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class EnajenacionBienDTO extends RemuneracionDTO  {

	/**
	 * Tipo del bien
	 */
	private EnumTipoBien tipoBien;
	
	 /**
     * Constructor
     */
    public EnajenacionBienDTO(){ };
    
    public EnajenacionBienDTO(MontoMonedaDTO remuneracion, EnumTipoBien tipoBien) {
    	super(remuneracion);
    	this.tipoBien = tipoBien;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public EnajenacionBienDTO(JsonObject json) {
    	EnajenacionBienDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        EnajenacionBienDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the tipoBien
	 */
	public EnumTipoBien getTipoBien() {
		return tipoBien;
	}

	/**
	 * @param tipoBien the tipoBien to set
	 */
	public void setTipoBien(EnumTipoBien tipoBien) {
		this.tipoBien = tipoBien;
	}
	
}
