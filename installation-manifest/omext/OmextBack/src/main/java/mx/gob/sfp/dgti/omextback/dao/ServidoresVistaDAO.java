/**
 * @(#)ConsultaApi.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dao;

import io.vertx.codegen.annotations.*;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import mx.gob.sfp.dgti.omextback.dao.impl.ServidoresVistaDAOImpl;

/**
 * Interface para el servicio ConsultaApi
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@ProxyGen
@VertxGen
public interface ServidoresVistaDAO {

    @GenIgnore
    static ServidoresVistaDAO create(Vertx vertx) {
        return new ServidoresVistaDAOImpl(vertx);
    }

    @GenIgnore
    static mx.gob.sfp.dgti.omextback.dao.reactivex.ServidoresVistaDAO createProxy(Vertx vertx, String address) {
        return new mx.gob.sfp.dgti.omextback.dao.reactivex.ServidoresVistaDAO(
                new ServidoresVistaDAOVertxEBProxy(vertx.getDelegate(), address, new DeliveryOptions().setSendTimeout(55000)));
    }


    @Fluent
    public ServidoresVistaDAO consultarServidoresVista(JsonArray pipeline, Integer offset, Integer tamanio, Integer collName,
                                                       Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public ServidoresVistaDAO consultarServidoresVistaImpresion(JsonArray pipeline, Integer collName, Integer maxSize, Handler<AsyncResult<JsonArray>> resultHandler);

    @ProxyIgnore
    void close();

}