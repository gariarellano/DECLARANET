/**
 * @(#)DatosParejasDTO.java 03/11/2019
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
 * DTO para el modulo de datos de pareja
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 03/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DatosParejasDTO extends ModuloBaseDTO {

    /**
     * Lista de datos de parejas
     */
    private List<DatosParejaDTO> datosParejas;

    /**
     * Indica si no se agrega ningún dato. True si no hay ninguno
     */
    private boolean ninguno;

    public DatosParejasDTO() {
    }

    public DatosParejasDTO(EncabezadoDTO encabezado, String aclaracionesObservaciones, List<DatosParejaDTO> datosParejas,
                           boolean ninguno) {
        super(encabezado, aclaracionesObservaciones);
        this.datosParejas = datosParejas;
        this.ninguno = ninguno;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DatosParejasDTO(JsonObject json) {
        DatosParejasDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DatosParejasDTOConverter.toJson(this, json);
        return json;
    }

    public List<DatosParejaDTO> getDatosParejas() {
        return datosParejas;
    }

    public void setDatosParejas(List<DatosParejaDTO> datosParejas) {
        this.datosParejas = datosParejas;
    }

    public boolean isNinguno() {
        return ninguno;
    }

    public void setNinguno(boolean ninguno) {
        this.ninguno = ninguno;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DatosParejasDTO{");
        sb.append("datosParejas=").append(datosParejas);
        sb.append(", ninguno=").append(ninguno);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
