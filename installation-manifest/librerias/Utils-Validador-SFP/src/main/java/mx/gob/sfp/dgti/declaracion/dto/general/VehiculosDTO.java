/**
 * @(#)VehiculosDTO.java 12/11/2019
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
 * DTO para el modulo de vehiculos
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 12/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class VehiculosDTO extends ModuloBaseDTO {

    /**
     * Lista de vehiculos
     */
    private List<VehiculoDTO> vehiculos;

    /**
     * Indica si no se agrega ningún dato. True si no hay ninguno
     */
    private boolean ninguno;

    public VehiculosDTO() {
    }

    public VehiculosDTO(EncabezadoDTO encabezado, String aclaracionesObservaciones, List<VehiculoDTO> vehiculos,
                        boolean ninguno) {
        super(encabezado, aclaracionesObservaciones);
        this.vehiculos = vehiculos;
        this.ninguno = ninguno;
    }

    public List<VehiculoDTO> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<VehiculoDTO> vehiculos) {
        this.vehiculos = vehiculos;
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
    public VehiculosDTO(JsonObject json) {
        VehiculosDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        VehiculosDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * Metodo toString
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VehiculosDTO{");
        sb.append("vehiculos=").append(vehiculos);
        sb.append(", ninguno=").append(ninguno);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
