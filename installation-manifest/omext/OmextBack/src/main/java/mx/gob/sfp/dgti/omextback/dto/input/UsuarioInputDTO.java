/**
 * @(#)PeriodoInputDTO.java 08/02/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.input;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO con la informacion del usuario
 *
 * @author pavel.martinez
 * @since 30/03/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class UsuarioInputDTO {

    public UsuarioInputDTO(){//Constructor
    }

    public UsuarioInputDTO(JsonObject json) {
        UsuarioInputDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        UsuarioInputDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * Nombre de usuario
     */
    private String nombre;

    /**
     * Primer apellido de usuario
     */
    private String primerApellido;

    /**
     * Segundo apellido de usuario
     */
    private String segundoApellido;

    /**
     * CURP del usuario
     */
    private String curp;

    /**
     * RFC del usuario
     */
    private String rfc;

    /**
     * homoclave del usuario
     */
    private String homoclave;

    /**
     * email
     */
    private String email;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getHomoclave() {
        return homoclave;
    }

    public void setHomoclave(String homoclave) {
        this.homoclave = homoclave;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UsuarioInputDTO{" +
                "nombre='" + nombre + '\'' +
                ", primerApellido='" + primerApellido + '\'' +
                ", segundoApellido='" + segundoApellido + '\'' +
                ", curp='" + curp + '\'' +
                ", rfc='" + rfc + '\'' +
                ", homoclave='" + homoclave + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
