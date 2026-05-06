/**
 * @(#)CatalogoDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico para los catalogos
 * @author Miriam Sánchez Sánchez programador07
 * @since 24/09/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class CatalogoDTO {

	/**
	 * Identificador unico del registro del catalogo
	 */
	private Integer id;
	
	/**
	 * Descripcion del registro
	 */
	private String valor;

	/**
     * Constructor
     */
    public CatalogoDTO(){ };

	public CatalogoDTO(Integer id, String valor) {
		this.id = id;
		this.valor = valor;
	}

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     * @param json objeto en JsonObject
     */
    public CatalogoDTO(JsonObject json) {
    	CatalogoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        CatalogoDTOConverter.toJson(this, json);
        return json;
    }
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}
	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}


	/**
	 * Función toString
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("CatalogoDTO{");
		sb.append("id=").append(id);
		sb.append(", valor='").append(valor).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
