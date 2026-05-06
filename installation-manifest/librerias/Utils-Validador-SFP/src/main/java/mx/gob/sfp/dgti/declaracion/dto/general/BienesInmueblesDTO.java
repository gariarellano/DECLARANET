/**
 * @(#)BienesInmueblesDTO.java 11/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.general;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.base.ModuloBaseDTO;

import java.util.List;

/**
 * DTO para el modulo de bienes inmuebles
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 11/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class BienesInmueblesDTO extends ModuloBaseDTO {

    /**
     * Lista de bienes inmuebles
     */
    private List<BienInmuebleDTO> bienesInmuebles;

    /**
     * Indica si no se agrega ningún dato. True si no hay ninguno
     */
    private boolean ninguno;

    public BienesInmueblesDTO() {
    }

    public BienesInmueblesDTO(EncabezadoDTO encabezado, String aclaracionesObservaciones, List<BienInmuebleDTO>
            bienesInmuebles, boolean ninguno) {
        super(encabezado, aclaracionesObservaciones);
        this.bienesInmuebles = bienesInmuebles;
        this.ninguno = ninguno;
    }

    public List<BienInmuebleDTO> getBienesInmuebles() {
        return bienesInmuebles;
    }

    public void setBienesInmuebles(List<BienInmuebleDTO> bienesInmuebles) {
        this.bienesInmuebles = bienesInmuebles;
    }

    public boolean isNinguno() {
        return ninguno;
    }

    public void setNinguno(boolean ninguno) {
        this.ninguno = ninguno;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public BienesInmueblesDTO(JsonObject json) {
        BienesInmueblesDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        BienesInmueblesDTOConverter.toJson(this, json);
        return json;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BienesInmueblesDTO{");
        sb.append("bienesInmuebles=").append(bienesInmuebles);
        sb.append(", ninguno=").append(ninguno);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
