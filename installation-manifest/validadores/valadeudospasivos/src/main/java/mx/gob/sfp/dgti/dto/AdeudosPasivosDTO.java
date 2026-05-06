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
public class AdeudosPasivosDTO extends ModuloBaseDTO {

    /**
     * Lista de datos de parejas
     */
    private List<AdeudoDTO> adeudos;

    /**
     * Indica si no se agrega ningún dato. True si no hay ninguno
     */
    private boolean ninguno;

    public AdeudosPasivosDTO() {
    }

    public AdeudosPasivosDTO(EncabezadoDTO encabezado, String aclaracionesObservaciones, List<AdeudoDTO> adeudos,
                             boolean ninguno) {
        super(encabezado, aclaracionesObservaciones);
        this.adeudos = adeudos;
        this.ninguno = ninguno;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public AdeudosPasivosDTO(JsonObject json) {
        AdeudosPasivosDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        AdeudosPasivosDTOConverter.toJson(this, json);
        return json;
    }

    public List<AdeudoDTO> getAdeudos() {
        return adeudos;
    }

    public void setAdeudos(List<AdeudoDTO> adeudos) {
        this.adeudos = adeudos;
    }

    public boolean isNinguno() {
        return ninguno;
    }

    public void setNinguno(boolean ninguno) {
        this.ninguno = ninguno;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AdeudosPasivosDTO{");
        sb.append("adeudos=").append(adeudos);
        sb.append(", ninguno=").append(ninguno);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
