/**
 * @(#)ConsultaServidoresDTO.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.input;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumCumplimiento;

/**
 * DTO Para la consulta de servidores
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ConsultaServidoresDTO {

    /**
     * Query
     */
    private JsonObject query;

    /**
     * Enum del cumplimiento
     */
    private EnumCumplimiento cumplimiento;

    public ConsultaServidoresDTO(JsonObject query, EnumCumplimiento cumplimiento) {
        this.query = query;
        this.cumplimiento = cumplimiento;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public ConsultaServidoresDTO(JsonObject json) {
        ConsultaServidoresDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ConsultaServidoresDTOConverter.toJson(this, json);
        return json;
    }

    public JsonObject getQuery() {
        return query;
    }

    public void setQuery(JsonObject query) {
        this.query = query;
    }

    public EnumCumplimiento getCumplimiento() {
        return cumplimiento;
    }

    public void setCumplimiento(EnumCumplimiento cumplimiento) {
        this.cumplimiento = cumplimiento;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConsultaServidoresDTO{");
        sb.append("query=").append(query);
        sb.append(", cumplimiento=").append(cumplimiento);
        sb.append('}');
        return sb.toString();
    }
}
