/**
 * @(#)RespuestaServidoresDTO.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * DTO para la respuesta de la consulta de servidores publicos
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class RespuestaServidoresDTO {

    /**
     * Informacion de la paginacion de los resultados
     */
    private PaginacionDTO paginacion;

    /**
     * Lista de servidores publicos
     */
    private List<ServidorPublicoDTO> servidoresPublicos;

    /**
     * Constructor
     */
    public RespuestaServidoresDTO(){//Constructor
    }

    /**
     * Constructor
     *
     * @param paginacion
     * @param servidoresPublicos
     */
    public RespuestaServidoresDTO(PaginacionDTO paginacion, List<ServidorPublicoDTO> servidoresPublicos) {
        this.paginacion = paginacion;
        this.servidoresPublicos = servidoresPublicos;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public RespuestaServidoresDTO(JsonObject json) {
        RespuestaServidoresDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RespuestaServidoresDTOConverter.toJson(this, json);
        return json;
    }

    public PaginacionDTO getPaginacion() {
        return paginacion;
    }

    public void setPaginacion(PaginacionDTO paginacion) {
        this.paginacion = paginacion;
    }

    public List<ServidorPublicoDTO> getServidoresPublicos() {
        return servidoresPublicos;
    }

    public void setServidoresPublicos(List<ServidorPublicoDTO> servidoresPublicos) {
        this.servidoresPublicos = servidoresPublicos;
    }

    /**
     * Metodo to String
     *
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RespuestaServidoresDTO{");
        sb.append("paginacion=").append(paginacion);
        sb.append(", servidoresPublicos=").append(servidoresPublicos);
        sb.append('}');
        return sb.toString();
    }
}
