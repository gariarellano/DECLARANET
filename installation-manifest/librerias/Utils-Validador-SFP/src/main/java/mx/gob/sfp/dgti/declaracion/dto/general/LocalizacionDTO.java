/**
 * @(#)LocalizacionDTO.java 03/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.general;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.individual.LocalizacionExtranjeroDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.LocalizacionMexicoDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumUbicacion;

/**
 * DTO para una ubicacion
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 12/09/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class LocalizacionDTO {

    /**
     * Tipo de domicilio 1: Mexico, 2: extranjero
     */
    private EnumUbicacion ubicacion;

    /**
     * Domicilio en Mexico
     */
    private LocalizacionMexicoDTO localizacionMexico;

    /**
     * Domicilio en el extranjero
     */
    private LocalizacionExtranjeroDTO localizacionExtranjero;

    /**
     * Constructor
     */
    public LocalizacionDTO(){ };

    public LocalizacionDTO(EnumUbicacion ubicacion, LocalizacionMexicoDTO localizacionMexico,
                           LocalizacionExtranjeroDTO localizacionExtranjero) {
        this.ubicacion = ubicacion;
        this.localizacionMexico = localizacionMexico;
        this.localizacionExtranjero = localizacionExtranjero;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public LocalizacionDTO(JsonObject json) {
        LocalizacionDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        LocalizacionDTOConverter.toJson(this, json);
        return json;
    }

    public EnumUbicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(EnumUbicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalizacionMexicoDTO getLocalizacionMexico() {
        return localizacionMexico;
    }

    public void setLocalizacionMexico(LocalizacionMexicoDTO localizacionMexico) {
        this.localizacionMexico = localizacionMexico;
    }

    public LocalizacionExtranjeroDTO getLocalizacionExtranjero() {
        return localizacionExtranjero;
    }

    public void setLocalizacionExtranjero(LocalizacionExtranjeroDTO localizacionExtranjero) {
        this.localizacionExtranjero = localizacionExtranjero;
    }

    @Override
    public String toString() {
        return "LocalizacionDTO{" +
                "ubicacion=" + ubicacion +
                ", localizacionMexico=" + localizacionMexico +
                ", localizacionExtranjero=" + localizacionExtranjero +
                '}';
    }
}
