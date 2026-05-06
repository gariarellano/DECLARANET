/**
 * @(#)NivelOrdenGobiernoDTO.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumNivelOrdenGobierno;

/**
 * DTO para el nivel orden de gobierno
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class NivelOrdenGobiernoDTO {

    /**
     * Enum con el nivel
     */
    private EnumNivelOrdenGobierno nivelOrdenGobierno;

    /**
     * Constructor
     */
    public NivelOrdenGobiernoDTO(){//Constructor
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public NivelOrdenGobiernoDTO(JsonObject json) {
        NivelOrdenGobiernoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        NivelOrdenGobiernoDTOConverter.toJson(this, json);
        return json;
    }

    public EnumNivelOrdenGobierno getNivelOrdenGobierno() {
        return nivelOrdenGobierno;
    }

    public void setNivelOrdenGobierno(EnumNivelOrdenGobierno nivelOrdenGobierno) {
        this.nivelOrdenGobierno = nivelOrdenGobierno;
    }

    /**
     * Metodo toString
     * @return
     */
    @Override
    public String toString() {
        return "NivelOrdenGobiernoDTO{" +
                "nivelOrdenGobierno=" + nivelOrdenGobierno +
                '}';
    }
}
