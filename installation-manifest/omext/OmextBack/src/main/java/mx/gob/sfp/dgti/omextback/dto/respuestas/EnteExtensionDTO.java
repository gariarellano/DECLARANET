/**
 * @(#)EnteExtensionInputDTO.java 08/02/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO para las extensiones individuales de periodos
 *
 * @author pavel.martinez
 * @since 08/02/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class EnteExtensionDTO {

    public EnteExtensionDTO(){//Constructor
    }

    public EnteExtensionDTO(JsonObject json) {
        EnteExtensionDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        EnteExtensionDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * id del periodo
     */
    private String idEnte;

    /**
     * collName de la consulta
     */
    private String fechaLimite;


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
        return "EnteExtensionInputDTO{" +
                "idEnte='" + idEnte + '\'' +
                ", fecha='" + fechaLimite + '\'' +
                '}';
    }
}
