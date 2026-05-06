/**
 * @(#)ExtensionPeriodoInstitucionInputDTO.java 08/02/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.input;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO con la informacion para extender periodos independientes por institucion
 *
 * @author pavel.martinez
 * @since 08/02/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ExtensionPeriodoInputDTO {

    public ExtensionPeriodoInputDTO(){//Constructor
    }

    public ExtensionPeriodoInputDTO(JsonObject json) {
        ExtensionPeriodoInputDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ExtensionPeriodoInputDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * collName del mutation
     */
    private Integer collName;

    /**
     * Id del periodo
     */
    private String idPeriodo;

    /**
     * Fecha limite
     */
    private String fechaLimite;

    public Integer getCollName() {
        return collName;
    }

    public void setCollName(Integer collName) {
        this.collName = collName;
    }

    public String getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(String idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    @Override
    public String toString() {
        return "ExtensionPeriodoInputDTO{" +
                "collName=" + collName +
                ", idPerido='" + idPeriodo + '\'' +
                ", fechaLimite='" + fechaLimite + '\'' +
                '}';
    }
}
