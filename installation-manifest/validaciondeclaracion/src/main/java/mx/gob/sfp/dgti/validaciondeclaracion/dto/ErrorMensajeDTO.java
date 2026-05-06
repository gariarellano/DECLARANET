/**
 * @(#)ErrorMensajeDTO.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;


/**
 * Clase que contiene el DTO para ErrorMensajeDTO
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ErrorMensajeDTO {

    /**
     * Identificador del error
     */
    private Integer errorId;

    /**
     * Mensaje del error
     */
    private String mensaje;

    /**
     * Otro mensaje del error
     */
    private String mensajeAlterno;

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public ErrorMensajeDTO(JsonObject json) {
        ErrorMensajeDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ErrorMensajeDTOConverter.toJson(this, json);
        return json;
    }

    public Integer getErrorId() {
        return errorId;
    }

    public void setErrorId(Integer errorId) {
        this.errorId = errorId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensajeAlterno() {
        return mensajeAlterno;
    }

    public void setMensajeAlterno(String mensajeAlterno) {
        this.mensajeAlterno = mensajeAlterno;
    }

    @Override
    public String toString() {
        return "ErrorMensajeDTO{" +
                "errorId=" + errorId +
                ", mensaje='" + mensaje + '\'' +
                ", mensajeAlterno='" + mensajeAlterno + '\'' +
                '}';
    }
}
