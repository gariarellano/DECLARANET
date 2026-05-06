/**
 * @(#)FilaDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.general;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico para la base de los registros
 * @author Miriam Sánchez Sánchez programador07
 * @since 24/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class FilaDTO {
	
	private String id;
	private String idPosicionVista;
	private boolean verificar;
	
	 /**
     * Constructor
     */
    public FilaDTO(){ };

	public FilaDTO(String id, String idPosicionVista, boolean verificar) {
		this.id = id;
		this.idPosicionVista = idPosicionVista;
		this.verificar = verificar;
	}
	
	public FilaDTO(String id, String idPosicionVista) {
		this.id = id;
		this.idPosicionVista = idPosicionVista;
	}

	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public FilaDTO(JsonObject json) {
    	FilaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        FilaDTOConverter.toJson(this, json);
        return json;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdPosicionVista() {
		return idPosicionVista;
	}

	public void setIdPosicionVista(String idPosicionVista) {
		this.idPosicionVista = idPosicionVista;
	}

	/**
	 * @return the verificar
	 */
	public boolean isVerificar() {
		return verificar;
	}

	/**
	 * @param verificar the verificar to set
	 */
	public void setVerificar(boolean verificar) {
		this.verificar = verificar;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("FilaDTO{");
		sb.append("id=").append(id);
		sb.append(", idPosicionVista='").append(idPosicionVista).append('\'');
		sb.append(" verificar=").append(verificar);
		sb.append('}');
		return sb.toString();
	}
}
