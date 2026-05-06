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
public class RespuestaCumplimientoPorGrupoDTO {

    /**
     * Total de instituciones obtenidas
     */
    private Integer total;

    /**
     * Lista con las instituciones y su informacion de cumplimiento
     */
    private List<DatosGrupoDTO> resultado;

    /**
     * Constructor
     */
    public RespuestaCumplimientoPorGrupoDTO(){//Constructor
    }

    public RespuestaCumplimientoPorGrupoDTO(Integer total, List<DatosGrupoDTO> resultado) {
        this.total = total;
        this.resultado = resultado;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public RespuestaCumplimientoPorGrupoDTO(JsonObject json) {
        RespuestaCumplimientoPorGrupoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RespuestaCumplimientoPorGrupoDTOConverter.toJson(this, json);
        return json;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<DatosGrupoDTO> getResultado() {
        return resultado;
    }

    public void setResultado(List<DatosGrupoDTO> resultado) {
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return "RespuestaCumplimientoPorGrupoDTO{" +
                "total=" + total +
                ", resultado=" + resultado +
                '}';
    }
}
