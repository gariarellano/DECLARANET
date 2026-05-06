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
import mx.gob.sfp.dgti.declaracion.dto.base.ModuloBaseDTO;

import java.util.List;

/**
 * DTO para el modulo de datos de pareja
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 03/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DatosDependientesEconDTO extends ModuloBaseDTO {

    /**
     * Lista de datos de parejas
     */
    private List<DatosDependienteEconDTO> datosDependientesEconomicos;

    /**
     * Indica si no se agrega ningún dato. True si no hay ninguno
     */
    private boolean ninguno;

    public DatosDependientesEconDTO() {
    }

    public List<DatosDependienteEconDTO> getDatosDependientesEconomicos() {
        return datosDependientesEconomicos;
    }

    public void setDatosDependientesEconomicos(List<DatosDependienteEconDTO> datosDependientesEconomicos) {
        this.datosDependientesEconomicos = datosDependientesEconomicos;
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
    public DatosDependientesEconDTO(JsonObject json) {
        DatosDependientesEconDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DatosDependientesEconDTOConverter.toJson(this, json);
        return json;
    }

    @Override
    public String toString() {
        return "DatosDependientesEconDTO{" +
                "datosDependientesEconomicos=" + datosDependientesEconomicos +
                ", ninguno=" + ninguno +
                "} " + super.toString();
    }
}
