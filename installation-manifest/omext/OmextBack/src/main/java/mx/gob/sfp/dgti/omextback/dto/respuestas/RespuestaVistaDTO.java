/**
 * @(#)RespuestaVista.java 25/01/2021
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
 * DTO con la respuesta de la consulta de vistas
 *
 * @author pavel.martinez
 * @since 25/01/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class RespuestaVistaDTO {

    public RespuestaVistaDTO(){//Constructor
    }

    public RespuestaVistaDTO(JsonObject json) {
        RespuestaVistaDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RespuestaVistaDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * Total de instituciones por vista
     */
    private Integer total;

    /**
     * Lista de instituciones
     */
    private List<VistaDTO> vistas;

    /**
     * Informacion del periodo al que pertenecen las vistas
     */
    private PeriodoDTO periodo;





    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<VistaDTO> getVistas() {
        return vistas;
    }

    public void setVistas(List<VistaDTO> vistas) {
        this.vistas = vistas;
    }

    public PeriodoDTO getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodoDTO periodo) {
        this.periodo = periodo;
    }

    @Override
    public String toString() {
        return "RespuestaVistaDTO{" +
                "total=" + total +
                ", instituciones=" + vistas +
                ", periodo=" + periodo +
                '}';
    }
}
