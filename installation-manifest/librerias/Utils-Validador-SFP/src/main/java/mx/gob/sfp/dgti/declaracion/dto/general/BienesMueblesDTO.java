/**
 * @(#)BienesMueblesDTO.java 13/11/2019
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
 * DTO para el modulo de bienes muebles
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 13/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class BienesMueblesDTO extends ModuloBaseDTO {

    /**
     * Lista de bienes muebles
     */
    private List<BienMuebleDTO> bienesMuebles;

    /**
     * Indica si no se agrega ningún dato. True si no hay ninguno
     */
    private boolean ninguno;

    public BienesMueblesDTO() {
    }

    public BienesMueblesDTO(EncabezadoDTO encabezado, String aclaracionesObservaciones, List<BienMuebleDTO> bienesMuebles,
                            boolean ninguno) {
        super(encabezado, aclaracionesObservaciones);
        this.bienesMuebles = bienesMuebles;
        this.ninguno = ninguno;
    }

    public List<BienMuebleDTO> getBienesMuebles() {
        return bienesMuebles;
    }

    public void setBienesMuebles(List<BienMuebleDTO> bienesMuebles) {
        this.bienesMuebles = bienesMuebles;
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
    public BienesMueblesDTO(JsonObject json) {
        BienesMueblesDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        BienesMueblesDTOConverter.toJson(this, json);
        return json;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BienesMueblesDTO{");
        sb.append("bienesMuebles=").append(bienesMuebles);
        sb.append(", ninguno=").append(ninguno);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
