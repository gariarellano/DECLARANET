/**
 * @(#)LlamadorCatalogos.java 19/12/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.recepdeclaracion.servicioext;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;

/**
 * Interface para el servicio LlamadorCatalogos
 *
 * @author pavel.martinez
 * @since 19/12/2019
 */
@ProxyGen
@VertxGen
public interface LlamadorCatalogos {

        @GenIgnore
        static LlamadorCatalogos create(Vertx vertx) {
            return new LlamadorCatalogosImpl(vertx);
        }

        @GenIgnore
        static mx.gob.sfp.dgti.recepdeclaracion.servicioext.reactivex.LlamadorCatalogos createProxy(Vertx vertx, String address) {
            return new mx.gob.sfp.dgti.recepdeclaracion.servicioext.reactivex.LlamadorCatalogos(
                    new LlamadorCatalogosVertxEBProxy(vertx.getDelegate(), address));
        }

        /**
         * Metodo para obtener un catalogo por su nombre
         *
         * @param resultHandler: handler para la respuesta de JsonObject
         * @param catalogo: catalogo a consultar
         *
         * @author pavel.martinez
         * @since 19/12/2019
         */
        @Fluent
        LlamadorCatalogos llamarCatalogo(String catalogo,
                                          Handler<AsyncResult<JsonObject>> resultHandler);

        /**
         * Metodo para obtener toooodos los catalogos
         *
         * @param resultHandler: handler para la respuesta de JsonObject
         *
         * @author pavel.martinez
         * @since 19/12/2019
         */
        @Fluent
        LlamadorCatalogos llamarTodosCatalogos(Handler<AsyncResult<JsonObject>> resultHandler);


        /**
         * Metodo para validar un catalogo
         *
         * @param resultHandler: handler para la respuesta de JsonObject
         * @param catalogo: Nombre del catalogo
         * @param info: la info del catalogo que se va a validar
         *
         * @author pavel.martinez
         * @since 19/12/2019
         */
        @Fluent
        LlamadorCatalogos validarCatalogo(String catalogo, JsonObject info, Handler<AsyncResult<JsonObject>> resultHandler);

    }

