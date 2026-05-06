/**
 * @(#)LocalizacionMexicoDTO.java 12/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO para el domicilio en Mexico
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 12/09/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class LocalizacionMexicoDTO {

    /**
     * Entidad federativa
     */
    protected CatalogoDTO entidadFederativa;

    /**
     * Constructor
     */
    public LocalizacionMexicoDTO(){ };

    public LocalizacionMexicoDTO(CatalogoDTO entidadFederativa) {
        this.entidadFederativa = entidadFederativa;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public LocalizacionMexicoDTO(JsonObject json) {
        LocalizacionMexicoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        LocalizacionMexicoDTOConverter.toJson(this, json);
        return json;
    }

    public CatalogoDTO getEntidadFederativa() {
        return entidadFederativa;
    }

    public void setEntidadFederativa(CatalogoDTO entidadFederativa) {
        this.entidadFederativa = entidadFederativa;
    }

    @Override
    public String toString() {
        return "LocalizacionMexicoDTO{" +
                "entidadFederativa=" + entidadFederativa +
                '}';
    }
}
