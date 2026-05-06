/**
 * @(#)RespuestaPeriodosPorInstDTO.java 25/01/2021
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
 * DTO de los periodos por institucion
 *
 * @author pavel.martinez
 * @since 25/01/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class RespuestaPeriodosPorInstDTO {

    public RespuestaPeriodosPorInstDTO(){//Constructor
    }

    public RespuestaPeriodosPorInstDTO(JsonObject json) {
        RespuestaPeriodosPorInstDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RespuestaPeriodosPorInstDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * Total de instituciones
     */
    private Integer total;

    /**
     * Informacion del periodo solicitado
     */
    private PeriodoDTO periodo;

    /**
     * Informacion del ente con respecto al periodo
     */
    private List<InstitucionPeriodoDTO> instituciones;


    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public PeriodoDTO getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodoDTO periodo) {
        this.periodo = periodo;
    }

    public List<InstitucionPeriodoDTO> getInstituciones() {
        return instituciones;
    }

    public void setInstituciones(List<InstitucionPeriodoDTO> instituciones) {
        this.instituciones = instituciones;
    }

    @Override
    public String toString() {
        return "RespuestaPeriodosPorInstDTO{" +
                "total=" + total +
                ", periodo=" + periodo +
                ", instituciones=" + instituciones +
                '}';
    }
}
