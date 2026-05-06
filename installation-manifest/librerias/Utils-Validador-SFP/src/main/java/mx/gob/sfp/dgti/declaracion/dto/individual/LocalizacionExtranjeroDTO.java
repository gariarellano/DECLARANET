/**
 * @(#)LocalizacionExtranjeroDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO para una ubicacion en el extranjero
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 12/09/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class LocalizacionExtranjeroDTO {

    /**
     * Pais
     */
    protected CatalogoDTO pais;

    /**
     * Constructor
     */
    public LocalizacionExtranjeroDTO(){ };

    /**
     *
     * @param pais
     */
    public LocalizacionExtranjeroDTO(CatalogoDTO pais) {
        this.pais = pais;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public LocalizacionExtranjeroDTO(JsonObject json) {
        LocalizacionExtranjeroDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        LocalizacionExtranjeroDTOConverter.toJson(this, json);
        return json;
    }

    public CatalogoDTO getPais() {
        return pais;
    }

    public void setPais(CatalogoDTO pais) {
        this.pais = pais;
    }

    @Override
    public String toString() {
        return "LocalizacionExtranjeroDTO{" +
                "pais=" + pais +
                '}';
    }
}
