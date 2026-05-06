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
import mx.gob.sfp.dgti.omextback.dao.impl.PeriodosDAOImpl;

/**
 * Interface para el servicio ConsultaApi
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@ProxyGen
@VertxGen
public interface PeriodosDAO {

    @GenIgnore
    static PeriodosDAO create(Vertx vertx) {
        return new PeriodosDAOImpl(vertx);
    }

    @GenIgnore
    static mx.gob.sfp.dgti.omextback.dao.reactivex.PeriodosDAO createProxy(Vertx vertx, String address) {
        return new mx.gob.sfp.dgti.omextback.dao.reactivex.PeriodosDAO(
                new PeriodosDAOVertxEBProxy(vertx.getDelegate(), address, new DeliveryOptions().setSendTimeout(55000)));
    }

    @Fluent
    public PeriodosDAO consultarPeriodos(JsonArray pipeline, Integer collName, Integer offset, Integer tamanio,
                                         Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public PeriodosDAO consultarPeriodoSimple(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler);


    @Fluent
    public PeriodosDAO guardarPeriodo(JsonObject periodo, Integer collName, Handler<AsyncResult<String>> resultHandler);

    @Fluent
    public PeriodosDAO actualizarFechaPeriodo(Integer collName, String idPeriodo, String fecha,
                                              Handler<AsyncResult<Boolean>> resultHandler);
    @Fluent
    public PeriodosDAO actualizarFechaExtensionesPeriodo(Integer collName, String idPeriodo, JsonArray extensiones,
                                                         Handler<AsyncResult<Boolean>> resultHandler);

    @ProxyIgnore
    void close();

}