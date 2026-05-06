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
public class ExtensionPeriodoInstitucionInputDTO {

    public ExtensionPeriodoInstitucionInputDTO(){//Constructor
    }

    public ExtensionPeriodoInstitucionInputDTO(JsonObject json) {
        ExtensionPeriodoInstitucionInputDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ExtensionPeriodoInstitucionInputDTOConverter.toJson(this, json);
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
     * Id del ente o entes a los que se les extendera la fecha limite
     */
    private String idEnte;

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

    public String getIdEnte() {
        return idEnte;
    }

    public void setIdEnte(String idEnte) {
        this.idEnte = idEnte;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    @Override
    public String toString() {
        return "ExtensionPeriodoInstitucionInputDTO{" +
                "collName=" + collName +
                ", idPerido='" + idPeriodo + '\'' +
                ", idEnte=" + idEnte +
                ", fechaLimite='" + fechaLimite + '\'' +
                '}';
    }
}
