/**
 * @(#)CatalogoDAO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.decnet.dao;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import mx.gob.sfp.dgti.decnet.dto.CatalogoDTO;

/**
 * Implementacion de DatabaseService
 *
 * @author Miriam Sanchez Sanchez programador07
 * @since 30/05/2019
 */
public interface CatalogoDAO {

	static CatalogoDAO create(Vertx vertx) {
		return new CatalogoDAOImpl(vertx);
	}

	/**
	 * Obtener la informacion de catalogo
	 *
	 * @return Lista con el catalogo
	 */
	public Future<List<CatalogoDTO>> obtenerCatalogo();

	/**
	 * Obtener catalogo de activos. Utiliza cache
	 *
	 * @return Lista con catalogo de activos
	 */
	public Future<List<CatalogoDTO>> obtenerCatalogoActivos();


	/**
	 * Obtener la informacion de catalogo por id. Utiliza cache
	 *
	 * @param id del elemento en catalogo
	 * @return elemento del catalogo
	 */
	public Future<CatalogoDTO> obtenerCatalogoPorId(int id);

	/**
	 * Obtener la informacion de un catalogo por id y su pk
	 *
	 * @param id
	 * @param fk
	 * @return
	 */
	public Future<CatalogoDTO> obtenerCatalogoPorIdFk(int id, int fk);

	/**
	 * Obtener la informacion de catalogo filtrando por fk.
	 *
	 * @param fk de los elementos en catalogo
	 * @return elemento del catalogo
	 */
	public Future<List<CatalogoDTO>> obtenerCatalogoActivosFk(int fk);

	/**
	 * Función para agregar un registro nuevo
	 *
	 * @param catalogo: elemento nuevo
	 * @return elemento nuevo
	 */
	public Future<CatalogoDTO> agregarRegistro(CatalogoDTO catalogo);


	/**
	 * Función para actualizar un registro del catálogo
	 *
	 * @param catalogo: elemento con cambios
	 * @return elemento con cambios
	 */
	public Future<CatalogoDTO> actualizarRegistro(CatalogoDTO catalogo);



}
