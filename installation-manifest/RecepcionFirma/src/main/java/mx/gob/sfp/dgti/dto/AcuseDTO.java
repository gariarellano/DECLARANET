/**
 * @AcuseDTO.java Dec 19, 2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author Miriam Sanchez Sanchez programador07
 * @since Dec 19, 2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class AcuseDTO {

	private String digestionAcuse;
	private String firmaAcuse;
	private String idFirmante;
	
	/**
     * Constructor
     */
    public AcuseDTO(){ };

    /**
     * Constructor
     */
    public AcuseDTO(String digestionAcuse, String firmaAcuse, String idFirmante){ 
    	this.digestionAcuse = digestionAcuse;
    	this.firmaAcuse = firmaAcuse;
    	this.idFirmante = idFirmante;
    };

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public AcuseDTO(JsonObject json) {
    	AcuseDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        AcuseDTOConverter.toJson(this, json);
        return json;
    }

	
	/**
	 * @return the digestionAcuse
	 */
	public String getDigestionAcuse() {
		return digestionAcuse;
	}
	/**
	 * @param digestionAcuse the digestionAcuse to set
	 */
	public void setDigestionAcuse(String digestionAcuse) {
		this.digestionAcuse = digestionAcuse;
	}
	/**
	 * @return the firmaAcuse
	 */
	public String getFirmaAcuse() {
		return firmaAcuse;
	}
	/**
	 * @param firmaAcuse the firmaAcuse to set
	 */
	public void setFirmaAcuse(String firmaAcuse) {
		this.firmaAcuse = firmaAcuse;
	}
	/**
	 * @return the idFirmante
	 */
	public String getIdFirmante() {
		return idFirmante;
	}
	/**
	 * @param idFirmante the idFirmante to set
	 */
	public void setIdFirmante(String idFirmante) {
		this.idFirmante = idFirmante;
	}

	@Override
	public String toString() {
		return "AcuseDTO [digestionAcuse=" + digestionAcuse + ", firmaAcuse=" + firmaAcuse + ", idFirmante="
				+ idFirmante + "]";
	}
}
