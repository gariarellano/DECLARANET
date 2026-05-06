/**
 * @(#)VistaInputDTO.java 08/02/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.input;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * DTO con la informacion para generar vistas de un periodo
 *
 * @author pavel.martinez
 * @since 08/02/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class VistasOmisosInputDTO {

    public VistasOmisosInputDTO(){//Constructor
    }

    public VistasOmisosInputDTO(JsonObject json) {
        VistasOmisosInputDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        VistasOmisosInputDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * collName de la consulta
     */
    private Integer collName;

    /**
     * Id del periodo
     */
    private String idPeriodo;

    /**
     * Lista de instituciones a los que generar las vistas
     */
    private List<String> instituciones;

    /**
     * Remitente de las vistas
     */
    private String remitente;

    /**
     * Usurio que registro
     */
    private UsuarioInputDTO usuarioRegistro;


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

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public List<String> getInstituciones() {
        return instituciones;
    }

    public void setInstituciones(List<String> instituciones) {
        this.instituciones = instituciones;
    }

    public UsuarioInputDTO getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(UsuarioInputDTO usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    @Override
    public String toString() {
        return "VistasOmisosInputDTO{" +
                "collName=" + collName +
                ", idPeriodo='" + idPeriodo + '\'' +
                ", instituciones=" + instituciones +
                ", remitente='" + remitente + '\'' +
                ", usuarioRegistro=" + usuarioRegistro +
                '}';
    }
}
