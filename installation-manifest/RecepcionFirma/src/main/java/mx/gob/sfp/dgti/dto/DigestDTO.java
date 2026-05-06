/**
 * @DigestDTO.java Dec 19, 2019
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
public class DigestDTO {

	private String numeroDeclaracion;
	private Integer idUsuario;
	private Integer collName;
	private JsonObject declaracion;
	private String digestionDcn;
	
	/**
     * Constructor
     */
    public DigestDTO(){ };

    /**
     * Constructor
     */
    public DigestDTO(String numeroDeclaracion, Integer idUsuario, Integer collName, JsonObject declaracion, String digestionDcn) { 
    	this.numeroDeclaracion = numeroDeclaracion;
    	this.idUsuario = idUsuario;
    	this.collName = collName;
    	this.declaracion = declaracion;
    	this.digestionDcn = digestionDcn;
    };

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DigestDTO(JsonObject json) {
    	DigestDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DigestDTOConverter.toJson(this, json);
        return json;
    }

	
	
	/**
	 * @return the noDeclaracion
	 */
	public String getNumeroDeclaracion() {
		return numeroDeclaracion;
	}
	/**
	 * @param noDeclaracion the noDeclaracion to set
	 */
	public void setNumeroDeclaracion(String noDeclaracion) {
		this.numeroDeclaracion = noDeclaracion;
	}
	/**
	 * @return the idUsuario
	 */
	public Integer getIdUsuario() {
		return idUsuario;
	}
	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	/**
	 * @return the collName
	 */
	public Integer getCollName() {
		return collName;
	}
	/**
	 * @param collName the collName to set
	 */
	public void setCollName(Integer collName) {
		this.collName = collName;
	}
	/**
	 * @return the declaracion
	 */
	public JsonObject getDeclaracion() {
		return declaracion;
	}
	/**
	 * @param declaracion the declaracion to set
	 */
	public void setDeclaracion(JsonObject declaracion) {
		this.declaracion = declaracion;
	}
	/**
	 * @return the digestionDcn
	 */
	public String getDigestionDcn() {
		return digestionDcn;
	}
	/**
	 * @param digestionDcn the digestionDcn to set
	 */
	public void setDigestionDcn(String digestionDcn) {
		this.digestionDcn = digestionDcn;
	}

	@Override
	public String toString() {
		return "DigestDTO [numeroDeclaracion=" + numeroDeclaracion + ", idUsuario=" + idUsuario + ", collName="
				+ collName + ", declaracion=" + declaracion + ", digestionDcn=" + digestionDcn + "]";
	}
}
