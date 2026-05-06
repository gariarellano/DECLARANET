/**
 * @(#)VistasService.java 24/05/2020
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.service;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import mx.gob.sfp.dgti.omextback.service.impl.PeriodosServiceImpl;

/**
 * Interface para el servicio VistasService
 *
 * @author pavel.martinez
 * @since 24/05/2021
 */
@ProxyGen
@VertxGen
public interface PeriodosService {

    @GenIgnore
    static PeriodosService create(Vertx vertx) {
        return new PeriodosServiceImpl(vertx);
    }

    @GenIgnore
    static mx.gob.sfp.dgti.omextback.service.reactivex.PeriodosService createProxy(Vertx vertx, String address) {
        return new mx.gob.sfp.dgti.omextback.service.reactivex.PeriodosService(
                new PeriodosServiceVertxEBProxy(vertx.getDelegate(), address, new DeliveryOptions().setSendTimeout(55000)));
    }

    /**
     * Funcion para obtener los periodos
     *
     * @param filtroJson: filtros requeridos
     * @param resultHandler: handler para la respuesta de tipo JsonObject
     *
     * @return Informacion solicitada
     *
     * @author pavel.martinez
     * @since 04/02/2021
     */
    @Fluent
    PeriodosService obtenerPeriodos(JsonObject filtroJson, Handler<AsyncResult<JsonObject>> resultHandler);

    /**
     * Funcion para obtener las instituciones por periodo
     *
     * @param filtroJson: filtros requeridos
     * @param resultHandler: handler para la respuesta de tipo JsonObject
     *
     * @return Informacion solicitada
     *
     * @author pavel.martinez
     * @since 04/02/2021
     */
    @Fluent
    PeriodosService obtenerInstPeriodo(JsonObject filtroJson, Handler<AsyncResult<JsonObject>> resultHandler);

    /**
     * Funcion para extender periodos individuales a una o varias instituciones
     *
     * @param extensionJson: datos de la extension
     * @param resultHandler: handler con Boolean TRUE si la operacion se realizo satisfactoriamente
     *
     * @return Informacion solicitada
     *
     * @author pavel.martinez
     * @since 08/02/2021
     */
    @Fluent
    PeriodosService extenderPeriodoInstitucion(JsonObject extensionJson, Handler<AsyncResult<Boolean>> resultHandler);

    /**
     *
     * @param extensionJson
     * @param resultHandler
     * @return
     */
    @Fluent
    PeriodosService extenderPeriodo(JsonObject extensionJson, Handler<AsyncResult<Boolean>> resultHandler);

    /**
     * Funcion para guardar un periodo
     *
     * @param periodoJson: datos del periodo
     * @param resultHandler: handler con Boolean TRUE si la operacion se realizo satisfactoriamente
     *
     * @return Informacion solicitada
     *
     * @author pavel.martinez
     * @since 08/02/2021
     */
    @Fluent
    PeriodosService guardarPeriodo(JsonObject periodoJson, Handler<AsyncResult<String>> resultHandler);


}