/**
 * @(#)CacheServiceImpl.java 18/07/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.decnet.cache;

import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;

import java.util.List;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import mx.gob.sfp.dgti.decnet.dto.CatalogoDTO;

/**
 * Implementacion para el service del cache
 *
 * @author Pavel Alexei Martinez Regalado programador02
 * @since 18/07/2019
 */
public class CacheServiceImpl implements CacheService{

    /**
     * El vertx
     */
    Vertx vertx;

    /**
     * Cache Manager
     */
    CacheManager cacheManager;

    /**
     * Cache que almacena todo el catalogo completo de registros activos
     */
    Cache<String , List<CatalogoDTO>> catalogoActivos;

    /**
     * Cache que almacena catalogos por fk
     */
    Cache<Integer , List<CatalogoDTO>> catalogoActivosFk;

    /**
     * Nombre con el que se busca al cache de activos
     */
    private final String CACHE_MANAGER_ACTIVOS_FK = "catalogoFk";


    /**
     * Nombre con el que se busca al cache de activos
     */
    private final String CACHE_MANAGER_ACTIVOS = "catalogoActivos";

    /**
     * Llave donde se encuentra el valor del catalogo completo
     */
    private final String CAT_ACTIVOS = "A";

    /**
     * Cache que almacena registros por id
     */
    Cache<Integer, CatalogoDTO> catalogoPorId;

    /**
     * Nombre con el que se busca al cache del catalogo por id
     */
    private final String CACHE_MANAGER_POR_ID = "catalogoPorId";

    /**
     * Inicializa las variables de cache
     *
     * @param vertx
     */
    public CacheServiceImpl(Vertx vertx) {

        this.vertx = vertx;

        cacheManager = newCacheManagerBuilder()
                .withCache("preConfigured",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                Long.class, String.class, ResourcePoolsBuilder.heap(10)))
                .build();

        cacheManager.init();

        catalogoActivos = (Cache) cacheManager.createCache(CACHE_MANAGER_ACTIVOS,
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        String.class, List.class, ResourcePoolsBuilder.heap(10)));

        catalogoActivosFk = (Cache) cacheManager.createCache(CACHE_MANAGER_ACTIVOS_FK,
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        Integer.class, List.class, ResourcePoolsBuilder.heap(10)));

        catalogoPorId = cacheManager.createCache(CACHE_MANAGER_POR_ID,
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        Integer.class, CatalogoDTO.class, ResourcePoolsBuilder.heap(10)));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CacheService actualizarCatalogoActivosFk(Integer fk, List<CatalogoDTO> catalogo,
                                                  Handler<AsyncResult<Void>> resultHandler ) {
        catalogoActivosFk.put(fk, catalogo);
        resultHandler.handle(Future.succeededFuture());
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CacheService obtenerCatalogoActivosFk(Integer fk, Handler<AsyncResult<List<CatalogoDTO>>> resultHandler) {

        resultHandler.handle(Future.succeededFuture(catalogoActivosFk.get(fk)));

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CacheService actualizarCatalogoActivos(List<CatalogoDTO> catalogo,
                                                  Handler<AsyncResult<Void>> resultHandler ) {
        catalogoActivos.put(CAT_ACTIVOS, catalogo);
        resultHandler.handle(Future.succeededFuture());
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CacheService obtenerCatalogoActivos(Handler<AsyncResult<List<CatalogoDTO>>> resultHandler) {

        resultHandler.handle(Future.succeededFuture(catalogoActivos.get(CAT_ACTIVOS)));

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CacheService actualizarPorId(Integer id, CatalogoDTO elemento,
                                        Handler<AsyncResult<Void>> resultHandler ) {
        catalogoPorId.put(elemento.getId(), elemento);
        resultHandler.handle(Future.succeededFuture());
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CacheService obtenerPorId(Integer id, Handler<AsyncResult<CatalogoDTO>> resultHandler) {
        resultHandler.handle(Future.succeededFuture(catalogoPorId.get(id)));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CacheService restablecerCache(Handler<AsyncResult<Void>> resultHandler) {
        catalogoPorId.clear();
        catalogoActivos.clear();
        resultHandler.handle(Future.succeededFuture());
        return this;
    }
}
