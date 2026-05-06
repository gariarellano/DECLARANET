/**
 * @(#)BeneficiosPrivadosDTO.java 10/11/2019
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
 * DTO para el modulo de beneficios privados
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 10/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class BeneficiosPrivadosDTO extends ModuloBaseDTO {

    /**
     * Lista de beneficios privados
     */
    private List<BeneficioDTO> beneficios;

    /**
     * Indica si no se agrega ningún dato. True si no hay ninguno
     */
    private boolean ninguno;

    public BeneficiosPrivadosDTO() {
    }

    public BeneficiosPrivadosDTO(EncabezadoDTO encabezado, String aclaracionesObservaciones,
                                 List<BeneficioDTO> beneficios, boolean ninguno) {
        super(encabezado, aclaracionesObservaciones);
        this.beneficios = beneficios;
        this.ninguno = ninguno;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public BeneficiosPrivadosDTO(JsonObject json) {
        BeneficiosPrivadosDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        BeneficiosPrivadosDTOConverter.toJson(this, json);
        return json;
    }

    public List<BeneficioDTO> getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(List<BeneficioDTO> beneficios) {
        this.beneficios = beneficios;
    }

    public boolean isNinguno() {
        return ninguno;
    }

    public void setNinguno(boolean ninguno) {
        this.ninguno = ninguno;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BeneficiosPrivadosDTO{");
        sb.append("beneficios=").append(beneficios);
        sb.append(", ninguno=").append(ninguno);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
