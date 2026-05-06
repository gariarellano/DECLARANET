/**
 * @(#)FirmaDTO.java 03/03/2021
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
public class FirmaDTO {

    /**
     * Datos basicos de la firma
     */
    private FirmaDatosDTO datos;

    /**
     * Datos de la firma del servicio
     */
    private FirmaFirmaDTO firma;

    /**
     * Constructor
     */
    public FirmaDTO(){//Constructor
    }

    public FirmaDTO(FirmaDatosDTO datos, FirmaFirmaDTO firma) {
        this.datos = datos;
        this.firma = firma;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public FirmaDTO(JsonObject json) {
        FirmaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        FirmaDTOConverter.toJson(this, json);
        return json;
    }

    public FirmaDatosDTO getDatos() {
        return datos;
    }

    public void setDatos(FirmaDatosDTO datos) {
        this.datos = datos;
    }

    public FirmaFirmaDTO getFirma() {
        return firma;
    }

    public void setFirma(FirmaFirmaDTO firma) {
        this.firma = firma;
    }

    @Override
    public String toString() {
        return "FirmaDTO{" +
                "datos=" + datos +
                ", firma=" + firma +
                '}';
    }
}
