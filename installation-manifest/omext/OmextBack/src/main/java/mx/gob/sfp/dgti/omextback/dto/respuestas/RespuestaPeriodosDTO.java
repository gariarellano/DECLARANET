/**
 * @(#)RespuestaPeriodos.java 25/01/2021
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
 * DTO con la respuesta de la consulta de periodos
 *
 * @author pavel.martinez
 * @since 25/01/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class RespuestaPeriodosDTO {

    public RespuestaPeriodosDTO(){//Constructor
    }

    public RespuestaPeriodosDTO(JsonObject json) {
        RespuestaPeriodosDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RespuestaPeriodosDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * Total de periodos
     */
    private PaginacionDTO paginacion;

    /**
     * Lista de periodos Periodos
     */
    private List<PeriodoDTO> periodos;

    public PaginacionDTO getPaginacion() {
        return paginacion;
    }

    public void setPaginacion(PaginacionDTO paginacion) {
        this.paginacion = paginacion;
    }

    public List<PeriodoDTO> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<PeriodoDTO> periodos) {
        this.periodos = periodos;
    }


    @Override
    public String toString() {
        return "RespuestaPeriodosDTO{" +
                "total=" + paginacion +
                ", periodos=" + periodos +
                '}';
    }
}
