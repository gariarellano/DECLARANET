/**
 * @(#)InstitucionReceptoraDTO.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO para institucion receptora
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class InstitucionReceptoraDTO {

    /**
     * EL id
     */
    private String id;

    /**
     * Collname
     */
    private Integer collName;

    /**
     * Ente receptor
     */
    private EnteDTO ente;

    /**
     * Constructor
     */
    public InstitucionReceptoraDTO(){//Constructor
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public InstitucionReceptoraDTO(JsonObject json) {
        InstitucionReceptoraDTOConverter.fromJson(json, this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCollName() {
        return collName;
    }

    public void setCollName(Integer collName) {
        this.collName = collName;
    }

    public EnteDTO getEnte() {
        return ente;
    }

    public void setEnte(EnteDTO ente) {
        this.ente = ente;
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        InstitucionReceptoraDTOConverter.toJson(this, json);
        return json;
    }

    @Override
    public String toString() {
        return "InstitucionReceptoraDTO{" +
                "id='" + id + '\'' +
                ", collName=" + collName +
                ", ente=" + ente +
                '}';
    }
}
