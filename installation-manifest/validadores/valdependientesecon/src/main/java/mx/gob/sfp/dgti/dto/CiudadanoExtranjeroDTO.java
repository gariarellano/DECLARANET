/**
 * @(#)CiudadanoExtranjeroDTO.java 04/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO para ciudadano extranjero
 *
 * @author pavel.martinez
 * @since 04/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class CiudadanoExtranjeroDTO {

    /**
     * Indica si es extranjero o no
     */
    private Boolean esExtranjero;

    /**
     * Curp
     */
    private String curp;


    public CiudadanoExtranjeroDTO() {
    }

    public CiudadanoExtranjeroDTO(Boolean esExtranjero, String curp) {
        this.esExtranjero = esExtranjero;
        this.curp = curp;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public CiudadanoExtranjeroDTO(JsonObject json) {
        CiudadanoExtranjeroDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        CiudadanoExtranjeroDTOConverter.toJson(this, json);
        return json;
    }

    public Boolean getEsExtranjero() {
        return esExtranjero;
    }

    public void setEsExtranjero(Boolean esExtranjero) {
        this.esExtranjero = esExtranjero;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CiudadanoExtranjeroDTO{");
        sb.append("esExtranjero=").append(esExtranjero);
        sb.append(", curp='").append(curp).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
