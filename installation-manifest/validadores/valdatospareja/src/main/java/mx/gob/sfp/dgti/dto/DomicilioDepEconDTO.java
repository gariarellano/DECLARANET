/**
 * @(#)DomicilioDepEconDTO.java 03/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.general.DomicilioDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumLugarReside;

/**
 * DTO para el submodulo del Domicilio del Dependiente Economico
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 03/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DomicilioDepEconDTO  {

    /**
     * Lugar donde reside
     */
    EnumLugarReside lugarDondeReside;

    /**
     * Domicilio del dependiente economico
     */
    private DomicilioDTO domicilio;

    public DomicilioDepEconDTO() {
    }

    public DomicilioDepEconDTO(EnumLugarReside lugarDondeReside, DomicilioDTO domicilio) {
        this.lugarDondeReside = lugarDondeReside;
        this.domicilio = domicilio;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DomicilioDepEconDTO(JsonObject json) {
        DomicilioDepEconDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DomicilioDepEconDTOConverter.toJson(this, json);
        return json;
    }

    public EnumLugarReside getLugarDondeReside() {
        return lugarDondeReside;
    }

    public void setLugarDondeReside(EnumLugarReside lugarDondeReside) {
        this.lugarDondeReside = lugarDondeReside;
    }

    public DomicilioDTO getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(DomicilioDTO domicilio) {
        this.domicilio = domicilio;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DomicilioDepEconDTO{");
        sb.append(", lugarDondeReside=").append(lugarDondeReside);
        sb.append(", domicilio=").append(domicilio);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
