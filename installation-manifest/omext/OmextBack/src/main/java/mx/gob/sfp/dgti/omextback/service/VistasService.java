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
import mx.gob.sfp.dgti.omextback.service.impl.VistasServiceImpl;

/**
 * Interface para el servicio VistasService
 *
 * @author pavel.martinez
 * @since 24/05/2021
 */
@ProxyGen
@VertxGen
public interface VistasService {

    @GenIgnore
    static VistasService create(Vertx vertx) {
        return new VistasServiceImpl(vertx);
    }

    @GenIgnore
    static mx.gob.sfp.dgti.omextback.service.reactivex.VistasService createProxy(Vertx vertx, String address) {
        return new mx.gob.sfp.dgti.omextback.service.reactivex.VistasService(
                new VistasServiceVertxEBProxy(vertx.getDelegate(), address, new DeliveryOptions().setSendTimeout(55000)));
    }


    /**
     * Funcion para consultar las vistas
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
    VistasService obtenerVistas(JsonObject filtroJson, Handler<AsyncResult<JsonObject>> resultHandler);

    /**
     * Funcion para consultar los servidores publicos pertenecientes a una vista
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
    VistasService obtenerServidoresVista(JsonObject filtroJson, Handler<AsyncResult<JsonObject>> resultHandler);


    /**
     * Funcion generar las vistas para un periodo en particular
     *
     * @param datosParaVistasJson: datos para generar la vista
     * @param resultHandler: handler con Boolean TRUE si la operacion se realizo satisfactoriamente
     *
     * @return Informacion solicitada
     *
     * @author pavel.martinez
     * @since 08/02/2021
     */
    @Fluent
    VistasService generarVistasOmisos(JsonObject datosParaVistasJson, Handler<AsyncResult<Boolean>> resultHandler);

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
    VistasService guardarPeriodo(JsonObject periodoJson, Handler<AsyncResult<String>> resultHandler);

    /**
     * Funcion para obtener la impresion de servidores en una vista
     *
     * @param filtroJson filtro con condiciones
     * @param resultHandler: handler con Boolean TRUE si la operacion se realizo satisfactoriamente
     *
     * @return Informacion solicitada
     *
     * @author pavel.martinez
     * @since 08/03/2021
     */
    @Fluent
    VistasService generarImpresionServidoresVista(JsonObject filtroJson, Handler<AsyncResult<String>> resultHandler);


    /**
     *
     * @param paramsJson
     * @param resultHandler
     * @return
     *
     * @author pavel.martinez
     * @since 09/03/2021
     */
    @Fluent
    VistasService firmarVista(JsonObject paramsJson, Handler<AsyncResult<Boolean>> resultHandler);

}