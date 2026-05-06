/**
 * @(#)RepresentacionesDTO.java 10/11/2019
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
 * DTO para el modulo de representaciones
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 10/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class RepresentacionesDTO extends ModuloBaseDTO {

    /**
     * Lista de datos de parejas
     */
    private List<RepresentacionDTO> representaciones;

    /**
     * Indica si no se agrega ningún dato. True si no hay ninguno
     */
    private boolean ninguno;

    public RepresentacionesDTO() {
    }

    public RepresentacionesDTO(EncabezadoDTO encabezado, String aclaracionesObservaciones,
                               List<RepresentacionDTO> representaciones, boolean ninguno) {
        super(encabezado, aclaracionesObservaciones);
        this.representaciones = representaciones;
        this.ninguno = ninguno;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public RepresentacionesDTO(JsonObject json) {
        RepresentacionesDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RepresentacionesDTOConverter.toJson(this, json);
        return json;
    }

    public List<RepresentacionDTO> getRepresentaciones() {
        return representaciones;
    }

    public void setRepresentaciones(List<RepresentacionDTO> representaciones) {
        this.representaciones = representaciones;
    }

    public boolean isNinguno() {
        return ninguno;
    }

    public void setNinguno(boolean ninguno) {
        this.ninguno = ninguno;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RepresentacionesDTO{");
        sb.append("representaciones=").append(representaciones);
        sb.append(", ninguno=").append(ninguno);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
