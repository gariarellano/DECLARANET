/**
 * EstructuraPdnService.java Apr 2, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author Miriam Sanchez programador07
 * @modifiedBy programador09
 * @since Apr 2, 2020
 */
public interface EstructuraPdnService {

    /**
     * Se crea la estructura de Result
     *
     * @param original
     *
     * @return {@link JsonObject}
     */
    public Future<JsonObject> crearEstructuraDeclaracionPDN(JsonObject original);
}
