/**
 * @(#)RespuestaVistaPendienteDTO.java 25/01/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * DTO con la respuesta de vistas pendientes
 *
 * @author pavel.martinez
 * @since 25/01/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class RespuestaVistaPendienteDTO {

    public RespuestaVistaPendienteDTO(){//Constructor
    }

    public RespuestaVistaPendienteDTO(JsonObject json) {
        RespuestaVistaPendienteDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RespuestaVistaPendienteDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * Total de instituciones
     */
    private Integer total;

    /**
     * Lista de instituciones
     */
    private List<VistaDTO> instituciones;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<VistaDTO> getInstituciones() {
        return instituciones;
    }

    public void setInstituciones(List<VistaDTO> instituciones) {
        this.instituciones = instituciones;
    }

    @Override
    public String toString() {
        return "RespuestaVistaPendienteDTO{" +
                "total=" + total +
                ", instituciones=" + instituciones +
                '}';
    }
}
