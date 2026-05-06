/**
 * @(#)AdeudosDTO.java 08/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.base.ModuloBaseDTO;

import java.util.List;

/**
 * DTO para el modulo de adeudos
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 08/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ApoyosDTO extends ModuloBaseDTO {

    /**
     * Lista de datos de parejas
     */
    private List<ApoyoDTO> apoyos;

    /**
     * Indica si no se agrega ningún dato. True si no hay ninguno
     */
    private boolean ninguno;

    public ApoyosDTO() {
    }

    public ApoyosDTO(EncabezadoDTO encabezado, String aclaracionesObservaciones, List<ApoyoDTO> apoyos, boolean ninguno) {
        super(encabezado, aclaracionesObservaciones);
        this.apoyos = apoyos;
        this.ninguno = ninguno;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public ApoyosDTO(JsonObject json) {
        ApoyosDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ApoyosDTOConverter.toJson(this, json);
        return json;
    }

    public List<ApoyoDTO> getApoyos() {
        return apoyos;
    }

    public void setApoyos(List<ApoyoDTO> apoyos) {
        this.apoyos = apoyos;
    }

    public boolean isNinguno() {
        return ninguno;
    }

    public void setNinguno(boolean ninguno) {
        this.ninguno = ninguno;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ApoyosDTO{");
        sb.append("apoyos=").append(apoyos);
        sb.append(", ninguno=").append(ninguno);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
