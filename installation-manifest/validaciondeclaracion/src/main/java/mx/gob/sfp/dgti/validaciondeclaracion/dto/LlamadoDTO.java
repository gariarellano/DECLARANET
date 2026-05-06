/**
 * @(#)LlamadoDTO.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.validaciondeclaracion.util.EnumModulo;

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
    private String nombre;

    /**
     * Nombre del servicio
     */
    private EnumModulo servicio;

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
    public LlamadoDTO() {
        //Constructor
    }

    /**
     * Constructor
     *
     * @param nombre: String con el nombre
     * @param servicio: String con el nombre del servicio
     * @param urlAbs: String con la url absoluta
     * @param info: JsonObject con la informacion para validar
     */
    public LlamadoDTO(String nombre, EnumModulo servicio, String urlAbs, JsonObject info) {
        this.nombre = nombre;
        this.servicio = servicio;
        this.urlAbs = urlAbs;
        this.info = info;
    }

    /**
     * Constructor
     *
     * @param servicio: String con el nombre del servicio
     * @param urlAbs: String con la url absoluta
     * @param info: JsonObject con la informacion para validar
     */
    public LlamadoDTO(EnumModulo servicio, String urlAbs, JsonObject info) {
        this.servicio = servicio;
        this.urlAbs = urlAbs;
        this.info = info;
    }

    /**
     * Constructor
     *
     * @param nombre: String con el nombre del servicio
     * @param urlAbs: String con la url absoluta
     * @param info: JsonObject con la informacion para validar
     */
    public LlamadoDTO(String nombre, String urlAbs, JsonObject info) {
        this.nombre = nombre;
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

    public EnumModulo getServicio() {
        return servicio;
    }

    public void setServicio(EnumModulo servicio) {
        this.servicio = servicio;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LlamadoDTO{");
        sb.append("nombre='").append(nombre).append('\'');
        sb.append(", servicio=").append(servicio);
        sb.append(", urlAbs='").append(urlAbs).append('\'');
        sb.append(", info=").append(info);
        sb.append(", respuestaObtenida=").append(respuestaObtenida);
        sb.append('}');
        return sb.toString();
    }
}
