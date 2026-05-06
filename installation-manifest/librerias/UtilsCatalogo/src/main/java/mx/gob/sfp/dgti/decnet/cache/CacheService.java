/**
 * @(#)CacheService.java 18/07/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.decnet.cache;

import java.util.List;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import mx.gob.sfp.dgti.decnet.dto.CatalogoDTO;

/**
 * Interface para el service del cache
 *
 * @author Pavel Alexei Martinez Regalado programador02
 * @since 18/07/2019
 */
@ProxyGen
@VertxGen
public interface CacheService {

    /**
     * Direccion del servicio
     */
    static final String SERVICE_ADDRESS = "cache-service";

    static CacheService create(Vertx vertx) {
        return new CacheServiceImpl(vertx);
    }

    static CacheService createProxy(Vertx vertx, String address) {
        return new CacheServiceVertxEBProxy(vertx, address);
    }

    /**
     * Metodo para actualizar el catalogo de activos
     *
     * @param catalogo: Lista que contiene el catalogo de activos
     * @param resultHandler: handler para la respuesta de tipo Void
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 18/07/2019
     */
    @Fluent
    CacheService actualizarCatalogoActivos(List<CatalogoDTO> catalogo, Handler<AsyncResult<Void>> resultHandler ) ;

    /**
     * Metodo para obtener el catalogo de activos
     *
     * @param resultHandler: handler para la respuesta de tipo List<CatalogoDTO> con la lista
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 18/07/2019
     */
    @Fluent
    CacheService obtenerCatalogoActivos(Handler<AsyncResult<List<CatalogoDTO>>> resultHandler);

    /**
     * Metodo para actualizar el catalogo por id
     *
     * @param id: id del elemento del catalogo
     * @param elemento: elemento del catalogo
     * @param resultHandler: handler para la respuesta de tipo Void
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 18/07/2019
     */
    @Fluent
    CacheService actualizarPorId(Integer id, CatalogoDTO elemento, Handler<AsyncResult<Void>> resultHandler );

    /**
     * Metodo para obtener el catalogo por id
     *
     * @param id: id del elemento del catalogo
     * @param resultHandler: handler para la respuesta de tipo CatalogoDTO con el elemento del catalogo
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 18/07/2019
     */
    @Fluent
    CacheService obtenerPorId(Integer id, Handler<AsyncResult<CatalogoDTO>> resultHandler);

    /**
     * Metodo para actualizar un catalogo correspondiente a una fk
     *
     * @param fk : fk de ese catalogo
     * @param catalogo :catalogo para actualizar
     * @param resultHandler: handler para la respuesta de tipo Void
     * @return
     *
     * @author pavel.martinez
     * @since 03/12/2019
     */
    @Fluent
    CacheService actualizarCatalogoActivosFk(Integer fk, List<CatalogoDTO> catalogo,
                                             Handler<AsyncResult<Void>> resultHandler );

    /**
     * Metodo para obtener catalogos por fk
     *
     * @param fk: la fk para filtrar
     * @param resultHandler: handler para la respuesta de tipo CatalogoDTO con el elemento del catalogo
     * @return
     *
     * @author pavel.martinez
     * @since 03/12/2019
     */
    @Fluent
    CacheService obtenerCatalogoActivosFk(Integer fk, Handler<AsyncResult<List<CatalogoDTO>>> resultHandler);


    /**
     * Metodo para restablecer los valores de cache
     *
     * @param resultHandler: handler para la respuesta de tipo Void
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 18/07/2019
     */
    @Fluent
    CacheService restablecerCache(Handler<AsyncResult<Void>> resultHandler);
}
