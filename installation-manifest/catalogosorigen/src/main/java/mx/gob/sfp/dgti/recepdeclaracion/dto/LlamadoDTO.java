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
public class LlamadoDTO {

    /**
     * Nombre del servicio
     */
    private String nombreServicio;

    /**
     * URL absoluta del servicio
     */
    private String urlAbs;

    /**
     * JsonObject con el objeto con la informacion a validar de cada
     * seccion
     */
    private JsonObject info;

    /**
     *
     */
    private JsonObject respuestaObtenida;

    /**
     * Constructor
     */
    public LlamadoDTO() {}

    /**
     * Constructor
     *
     * @param nombreServicio: String con el nombre del servicio
     * @param urlAbs: String con la url absoluta
     * @param info: JsonObject con la informacion para validar
     */
    public LlamadoDTO(String nombreServicio, String urlAbs, JsonObject info) {
        this.nombreServicio = nombreServicio;
        this.urlAbs = urlAbs;
        this.info = info;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public LlamadoDTO(JsonObject json) {
        LlamadoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        LlamadoDTOConverter.toJson(this, json);
        return json;
    }

    public String getUrlAbs() {
        return urlAbs;
    }

    public void setUrlAbs(String urlAbs) {
        this.urlAbs = urlAbs;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public JsonObject getInfo() {
        return info;
    }

    public void setInfo(JsonObject info) {
        this.info = info;
    }

    public JsonObject getRespuestaObtenida() {
        return respuestaObtenida;
    }

    public void setRespuestaObtenida(JsonObject respuestaObtenida) {
        this.respuestaObtenida = respuestaObtenida;
    }

    @Override
    public String toString() {
        return "LlamadoDTO{" +
                "nombreServicio='" + nombreServicio + '\'' +
                ", urlAbs='" + urlAbs + '\'' +
                ", info=" + info +
                ", respuestaObtenida=" + respuestaObtenida +
                '}';
    }
}
