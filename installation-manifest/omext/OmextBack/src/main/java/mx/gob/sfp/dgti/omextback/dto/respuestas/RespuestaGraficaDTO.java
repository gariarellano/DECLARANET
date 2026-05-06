/**
 * @(#)RespuestaGraficaDTO.java 19/05/2020
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
 * DTO con la respuesta de la consulta de informacion de graficas
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class RespuestaGraficaDTO {

    /**
     * Indica el total de servidores
     */
    private Integer total;

    /**
     * Informacion de cada tipo de cumplimiento
     */
    private List<DatoGraficaDTO> resultado;

    /**
     * Constructor
     */
    public RespuestaGraficaDTO(){//Constructor

    }

    /**
     * Constructor
     *
     * @param total
     * @param resultado
     */
    public RespuestaGraficaDTO(Integer total, List<DatoGraficaDTO> resultado) {
        this.total = total;
        this.resultado = resultado;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public RespuestaGraficaDTO(JsonObject json) {
        RespuestaGraficaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RespuestaGraficaDTOConverter.toJson(this, json);
        return json;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<DatoGraficaDTO> getResultado() {
        return resultado;
    }

    public void setResultado(List<DatoGraficaDTO> resultado) {
        this.resultado = resultado;
    }

    /**
     * Metodo toString
     *
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RespuestaGraficaDTO{");
        sb.append("valor=").append(total);
        sb.append(", resultado=").append(resultado);
        sb.append('}');
        return sb.toString();
    }
}
