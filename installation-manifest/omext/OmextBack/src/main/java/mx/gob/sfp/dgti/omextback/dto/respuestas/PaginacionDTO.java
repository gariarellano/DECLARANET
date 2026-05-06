/**
 * @(#)PaginacionDTO.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO para la paginacion de la consulta de servidores
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class PaginacionDTO {

    /**
     * Indica desde donde comienza la consulta
     */
    private Integer offset;

    /**
     * Indica el tamanio de la pagina que se consulta
     */
    private Integer tamanio;

    /**
     * Indica el numero total de servidores existentes en esa consulta
     */
    private Integer total;


    /**
     * Constructor
     */
    public PaginacionDTO(){//Constructor
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public PaginacionDTO(JsonObject json) {
        PaginacionDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        PaginacionDTOConverter.toJson(this, json);
        return json;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getTamanio() {
        return tamanio;
    }

    public void setTamanio(Integer tamanio) {
        this.tamanio = tamanio;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * Metodo toString
     *
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaginacionDTO{");
        sb.append("pagina=").append(offset);
        sb.append(", tamanio=").append(tamanio);
        sb.append(", total=").append(total);
        sb.append('}');
        return sb.toString();
    }
}
