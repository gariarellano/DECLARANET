/**
 * @(#)ErrorDTO.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * DTO para mostrar errores
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 30/09/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ErrorDTO {

    /**
     * Nombre del campo
     */
    private String nombreCampo;

    /**
     * Valor en el campo
     */
    private String propiedadValor;

    /**
     * Lista de errores
     */
    private List<ErrorMensajeDTO> listErrorMensajes;

    /**
     * Constructor
     */
    public ErrorDTO(){//Constructor
    }

    public ErrorDTO(String nombreCampo, String propiedadValor, List<ErrorMensajeDTO> listErrorMensajes) {
        this.nombreCampo = nombreCampo;
        this.propiedadValor = propiedadValor;
        this.listErrorMensajes = listErrorMensajes;
    }
    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public ErrorDTO(JsonObject json) {
        ErrorDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ErrorDTOConverter.toJson(this, json);
        return json;
    }

    public String getNombreCampo() {
        return nombreCampo;
    }

    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    public String getPropiedadValor() {
        return propiedadValor;
    }

    public void setPropiedadValor(String propiedadValor) {
        this.propiedadValor = propiedadValor;
    }

    public List<ErrorMensajeDTO> getListErrorMensajes() {
        return listErrorMensajes;
    }

    public void setListErrorMensajes(List<ErrorMensajeDTO> listErrorMensajes) {
        this.listErrorMensajes = listErrorMensajes;
    }

    @Override
    public String toString() {
        return "ErrorDTO{" +
                "nombreCampo='" + nombreCampo + '\'' +
                ", propiedadValor='" + propiedadValor + '\'' +
                ", listErrorMensajes=" + listErrorMensajes +
                '}';
    }
}
