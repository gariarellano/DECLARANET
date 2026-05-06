/**
 * @(#)DomicilioDeclaranteModDTO.java 31/10/2019
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
import mx.gob.sfp.dgti.declaracion.dto.general.DomicilioDTO;

/**
 * DTO para el modulo de domicilio del declarante
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 31/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DomicilioDeclaranteDTO extends ModuloBaseDTO {

    private DomicilioDTO domicilio;

    private boolean verificar;

    public DomicilioDeclaranteDTO() {
    }

    public DomicilioDeclaranteDTO(EncabezadoDTO encabezado, String aclaracionesObservaciones, DomicilioDTO domicilio) {
        super(encabezado, aclaracionesObservaciones);
        this.domicilio = domicilio;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DomicilioDeclaranteDTO(JsonObject json) {
        DomicilioDeclaranteDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DomicilioDeclaranteDTOConverter.toJson(this, json);
        return json;
    }

    public DomicilioDTO getDomicilio() {
        return domicilio;
    }

    public boolean isVerificar() {
        return verificar;
    }

    public void setVerificar(boolean verificar) {
        this.verificar = verificar;
    }

    public void setDomicilio(DomicilioDTO domicilio) {
        this.domicilio = domicilio;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DomicilioDeclaranteDTO{");
        sb.append("domicilio=").append(domicilio);
        sb.append(", verificar=").append(verificar);
        sb.append('}');
        return sb.toString();
    }
}
