/**
 * @(#)NotaDTO.java 20/02/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO para la informacion de nota
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 20/02/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class NotaDTO {

    /**
     * La nota o aclaraciones
     */
    private String aclaracionesObservaciones;

    /**
     * Constructor
     */
    public NotaDTO() {
    }

    /**
     * Constructor
     *
     * @param aclaracionesObservaciones: String con las notas
     */
    public NotaDTO(String aclaracionesObservaciones) {
        this.aclaracionesObservaciones = aclaracionesObservaciones;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public NotaDTO(JsonObject json) {
        NotaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        NotaDTOConverter.toJson(this, json);
        return json;
    }

    public String getAclaracionesObservaciones() {
        return aclaracionesObservaciones;
    }

    public void setAclaracionesObservaciones(String aclaracionesObservaciones) {
        this.aclaracionesObservaciones = aclaracionesObservaciones;
    }

    /**
     * Metodo toString
     * @return
     */
    @Override
    public String toString() {
        return "NotaDTO{" +
                "aclaracionesObservaciones='" + aclaracionesObservaciones + '\'' +
                '}';
    }
}
