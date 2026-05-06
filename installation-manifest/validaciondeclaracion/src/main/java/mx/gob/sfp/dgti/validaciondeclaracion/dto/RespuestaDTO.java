/**
 * @(#)RespuestaDTO.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * Clase que contiene el DTO para RespuestaDTO
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class RespuestaDTO {

    /**
     * Estado
     */
    private String estado;

    /**
     * Lista de las observaciones encontradas por modulo
     */
    private List<ModuloDTO> modulos;

    /**
     * Objeto con la declaracion y digito verificador
     */
    private String declaracion;

    public RespuestaDTO() {
        //Constructor
    }

    public RespuestaDTO(String estado, List<ModuloDTO> modulos) {
        this.estado = estado;
        this.modulos = modulos;
    }

    public RespuestaDTO(String estado, String declaracion) {
        this.estado = estado;
        this.declaracion = declaracion;
    }

    public RespuestaDTO(String estado, List<ModuloDTO> modulos, String declaracion) {
        this.estado = estado;
        this.modulos = modulos;
        this.declaracion = declaracion;
    }

    public RespuestaDTO(String estado) {
        this.estado = estado;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public RespuestaDTO(JsonObject json) {
        RespuestaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RespuestaDTOConverter.toJson(this, json);
        return json;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<ModuloDTO> getModulos() {
        return modulos;
    }

    public void setModulos(List<ModuloDTO> modulos) {
        this.modulos = modulos;
    }

    public String getDeclaracion() {
        return declaracion;
    }

    public void setDeclaracion(String declaracion) {
        this.declaracion = declaracion;
    }
}
