/**
 * @(#)ParamsProcesoVistaInputDTO.java 08/02/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.input;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO con la informacion para terminar el proceso de generacion
 *
 * @author pavel.martinez
 * @since 09/03/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ParamsProcesoVistaInputDTO {

    public ParamsProcesoVistaInputDTO(){//Constructor
    }

    public ParamsProcesoVistaInputDTO(JsonObject json) {
        ParamsProcesoVistaInputDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ParamsProcesoVistaInputDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * id del periodo
     */
    private String folio;

    /**
     * collName de la consulta
     */
    private Integer collName;


    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public Integer getCollName() {
        return collName;
    }

    public void setCollName(Integer collName) {
        this.collName = collName;
    }

    @Override
    public String toString() {
        return "ParametrosProcesoVistaInputDTO{" +
                "folio='" + folio + '\'' +
                ", collName=" + collName +
                '}';
    }
}
