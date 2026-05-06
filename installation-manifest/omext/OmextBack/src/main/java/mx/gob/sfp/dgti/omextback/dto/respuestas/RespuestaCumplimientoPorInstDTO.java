/**
 * @(#)RespuestaCumplimientoPorInstDTO.java 19/05/2020
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
 * DTO para la respuesta de informacion de cumplimiento agrupada por institucion
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class RespuestaCumplimientoPorInstDTO {

    /**
     * Total de instituciones obtenidas
     */
    private Integer total;

    /**
     * Lista con las instituciones y su informacion de cumplimiento
     */
    private List<DatosInstDTO> resultado;

    /**
     * Constructor
     */
    public RespuestaCumplimientoPorInstDTO(){//Constructor
    }

    /**
     * Constructor
     *
     * @param total
     * @param resultado
     */
    public RespuestaCumplimientoPorInstDTO(Integer total, List<DatosInstDTO> resultado) {
        this.total = total;
        this.resultado = resultado;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public RespuestaCumplimientoPorInstDTO(JsonObject json) {
        RespuestaCumplimientoPorInstDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RespuestaCumplimientoPorInstDTOConverter.toJson(this, json);
        return json;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<DatosInstDTO> getResultado() {
        return resultado;
    }

    public void setResultado(List<DatosInstDTO> resultado) {
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RespuestaCumplimientoPorInstDTO{");
        sb.append("total=").append(total);
        sb.append(", resultado=").append(resultado);
        sb.append('}');
        return sb.toString();
    }
}
