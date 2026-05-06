/**
 * ConsultaCatalogoDAO.java Mar 31, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.dao;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * Clase para obtener los catàlogos de la BD 
 * @author Miriam Sanchez Sanchez programador07
 * @since Mar 23, 2020
 */
public interface CatalogoDAO {

	static CatalogoDAO create(Vertx vertx) {
        return new CatalogoDAOImpl(vertx);
    }
	
	/**
	 * @return
	 */
	Future<Void> obtenerCatalogos();

}
