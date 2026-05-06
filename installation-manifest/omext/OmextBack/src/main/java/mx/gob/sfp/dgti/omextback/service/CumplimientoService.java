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
import mx.gob.sfp.dgti.omextback.service.impl.CumplimientoServiceImpl;
import mx.gob.sfp.dgti.omextback.dto.input.FiltroOmextInputDTO;

/**
 * Interface para el servicio VistasService
 *
 * @author pavel.martinez
 * @since 24/05/2021
 */
@ProxyGen
@VertxGen
public interface CumplimientoService {

    @GenIgnore
    static CumplimientoService create(Vertx vertx) {
        return new CumplimientoServiceImpl(vertx);
    }

    @GenIgnore
    static mx.gob.sfp.dgti.omextback.service.reactivex.CumplimientoService createProxy(Vertx vertx, String address) {
        return new mx.gob.sfp.dgti.omextback.service.reactivex.CumplimientoService(
                new CumplimientoServiceVertxEBProxy(vertx.getDelegate(), address, new DeliveryOptions().setSendTimeout(55000)));
    }

    /**
     * Metodo para hacer consultas de las consultas de servidores y su cumplimiento
     *
     * @param filtroJson requerido para la operacion introducidos por el usuario
     * @param resultHandler: handler para la respuesta de tipo JsonObject
     *
     * @return JsonObject con la informacion solicitada
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    @Fluent
    public CumplimientoService obtenerCumplServ(FiltroOmextInputDTO filtro, Handler<AsyncResult<JsonObject>> resultHandler);


    /**
     * Metodo para obtener datos para crear graficas de cumplimiento
     *
     * @param filtroJson filtros requeridos para la operacion introducidos por el usuario
     * @param resultHandler: handler para la respuesta de tipo JsonObject
     *
     * @return JsonObject con la informacion solicitada
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    @Fluent
    public CumplimientoService obtenerCumplGraficas(JsonObject filtroJson, Handler<AsyncResult<JsonObject>> resultHandler);

    /**
     * Metodo para obtener conteos de cumplimiento por institucion
     *
     * @param filtroJson filtros requeridos para la operacion introducidos por el usuario
     * @param resultHandler: handler para la respuesta de tipo JsonObject
     *
     * @return JsonObject con la informacion solicitada
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    @Fluent
    public CumplimientoService obtenerCumplPorInst(JsonObject filtroJson, Handler<AsyncResult<JsonObject>> resultHandler);

    /**
     * Metodo para obtener conteos de cumplimiento por unidad administrativa
     *
     * @param filtroJson filtros requeridos para la operacion introducidos por el usuario
     * @param resultHandler: handler para la respuesta de tipo JsonObject
     *
     * @return Informacion solicitada
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    @Fluent
    public CumplimientoService obtenerCumplPorUA(JsonObject filtroJson, Handler<AsyncResult<JsonObject>> resultHandler);

    /**
     * Funcion para mandar llamar a otro servicio para crear un reporte de los servidores publicos
     *
     * @param filtroJson: filtros requeridos
     * @param resultHandler: handler para la respuesta de tipo JsonObject
     *
     * @return Informacion solicitada
     */
    @Fluent
    CumplimientoService obtenerReporteServ(JsonObject filtroJson, Handler<AsyncResult<String>> resultHandler);

    /**
     *
     *
     * @param filtroJson
     * @param resultHandler
     * @return
     *
     * @author pavel.martinez
     * @since 25/05/2021
     */
    @Fluent
    CumplimientoService obtenerCumplPorGrupo(JsonObject filtroJson, Handler<AsyncResult<JsonObject>> resultHandler);

}