/**
 * @(#)RespuestaServidoresVista.java 25/01/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * DTO con la respuesta de la consulta de servidores de una vista
 *
 * @author pavel.martinez
 * @since 25/01/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class RespuestaServidoresVistaDTO {

    public RespuestaServidoresVistaDTO(){//Constructor
    }

    public RespuestaServidoresVistaDTO(JsonObject json) {
        RespuestaServidoresVistaDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RespuestaServidoresVistaDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * Paginacion de la respuesta
     */
    private PaginacionDTO paginacion;

    /**
     * Lista de los servidores en la vista
     */
    private List<ServidorVistaDTO> servidores;

    public PaginacionDTO getPaginacion() {
        return paginacion;
    }

    public void setPaginacion(PaginacionDTO paginacion) {
        this.paginacion = paginacion;
    }

    public List<ServidorVistaDTO> getServidores() {
        return servidores;
    }

    public void setServidores(List<ServidorVistaDTO> servidores) {
        this.servidores = servidores;
    }

    @Override
    public String toString() {
        return "RespuestaServidoresVistaDTO{" +
                "paginacion=" + paginacion +
                ", servidores=" + servidores +
                '}';
    }
}
