/**
 * @(#)FirmaDatosDTO.java 03/03/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO con datos de la firma
 *
 * @author pavel.martinez
 * @since 03/03/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class FirmaDatosDTO {

    /**
     * Datos para sha
     */
    private String datosParaSha;

    /**
     * Sha generado
     */
    private String sha;

    /**
     * Constructor
     */
    public FirmaDatosDTO(){//Constructor
    }

    public FirmaDatosDTO(String datosParaSha, String sha) {
        this.datosParaSha = datosParaSha;
        this.sha = sha;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public FirmaDatosDTO(JsonObject json) {
        FirmaDatosDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        FirmaDatosDTOConverter.toJson(this, json);
        return json;
    }

    public String getDatosParaSha() {
        return datosParaSha;
    }

    public void setDatosParaSha(String datosParaSha) {
        this.datosParaSha = datosParaSha;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    @Override
    public String toString() {
        return "FirmaDatosDTO{" +
                "datosParaSha='" + datosParaSha + '\'' +
                ", sha='" + sha + '\'' +
                '}';
    }
}
