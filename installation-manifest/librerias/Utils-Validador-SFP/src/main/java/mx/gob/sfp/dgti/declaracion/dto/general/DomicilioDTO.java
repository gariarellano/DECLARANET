/**
 * @(#)DomicilioDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.general;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.individual.DomicilioExtranjeroDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.DomicilioMexicoDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumUbicacion;

/**
 * DTO para el domicilio del declarante
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 12/09/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DomicilioDTO {

    /**
     * Tipo de domicilio 1: Mexico, 2: extranjero
     */
    private EnumUbicacion ubicacion;

    /**
     * Domicilio en Mexico
     */
    private DomicilioMexicoDTO domicilioMexico;

    /**
     * Domicilio en el extranjero
     */
    private DomicilioExtranjeroDTO domicilioExtranjero;

    /**
     * Constructor
     */
    public DomicilioDTO(){ };

    public DomicilioDTO(EnumUbicacion ubicacion, DomicilioMexicoDTO domicilioMexico, DomicilioExtranjeroDTO domicilioExtranjero) {
        this.ubicacion = ubicacion;
        this.domicilioMexico = domicilioMexico;
        this.domicilioExtranjero = domicilioExtranjero;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DomicilioDTO(JsonObject json) {
        DomicilioDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DomicilioDTOConverter.toJson(this, json);
        return json;
    }

    public EnumUbicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(EnumUbicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public DomicilioMexicoDTO getDomicilioMexico() {
        return domicilioMexico;
    }

    public void setDomicilioMexico(DomicilioMexicoDTO domicilioMexico) {
        this.domicilioMexico = domicilioMexico;
    }

    public DomicilioExtranjeroDTO getDomicilioExtranjero() {
        return domicilioExtranjero;
    }

    public void setDomicilioExtranjero(DomicilioExtranjeroDTO domicilioExtranjero) {
        this.domicilioExtranjero = domicilioExtranjero;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DomicilioDTO{");
        sb.append("ubicacion=").append(ubicacion);
        sb.append(", domicilioMexico=").append(domicilioMexico);
        sb.append(", domicilioExtranjero=").append(domicilioExtranjero);
        sb.append('}');
        return sb.toString();
    }
}
