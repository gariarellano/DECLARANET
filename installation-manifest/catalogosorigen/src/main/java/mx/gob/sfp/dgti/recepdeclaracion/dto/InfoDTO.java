/**
 * @(#)LlamadoDTO.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.recepdeclaracion.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Clase que contiene el DTO para LlamadoDTO
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class InfoDTO {

    /**
     * Nombre del servicio
     */
    private String url;

    /**
     * URL absoluta del servicio
     */
    private JsonObject query;

    /**
     * Constructor
     */
    public InfoDTO() {}

    public InfoDTO(String url, JsonObject query) {
        this.url = url;
        this.query = query;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public InfoDTO(JsonObject json) {
        InfoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        InfoDTOConverter.toJson(this, json);
        return json;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JsonObject getQuery() {
        return query;
    }

    public void setQuery(JsonObject query) {
        this.query = query;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InfoDTO{");
        sb.append("url='").append(url).append('\'');
        sb.append(", query=").append(query);
        sb.append('}');
        return sb.toString();
    }
}
