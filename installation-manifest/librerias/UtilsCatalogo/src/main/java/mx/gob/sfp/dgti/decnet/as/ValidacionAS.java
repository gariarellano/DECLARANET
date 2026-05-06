/**
 * @(#)ValidacionAS.java 30/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.decnet.as;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import mx.gob.sfp.dgti.decnet.dto.CatalogoDTO;

/**
 * Interface para el service de validacion
 *
 * @author Pavel Alexei Martinez Regalado pavel.martinez
 * @since 28/10/2019
 */
public interface ValidacionAS {

    static ValidacionAS create(Vertx vertx){
        return new ValidacionASImpl(vertx);
    }

    /**
     * Validar que la informacion de un elemento de catalogo corresponda con la informacion existente en base o cache
     *
     * @param catalogo: objeto con el catálogo
     * @return Boolean
     *
     * @author Pavel Alexei Martinez Regalado pavel.martinez
     * @since 28/10/2019
     */
    public Future<Boolean> validarInfoCatalogo (CatalogoDTO catalogo);
}
