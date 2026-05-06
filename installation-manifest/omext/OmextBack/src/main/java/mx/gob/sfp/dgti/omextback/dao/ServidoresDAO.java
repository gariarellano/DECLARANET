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
import mx.gob.sfp.dgti.omextback.dao.impl.ServidoresDAOImpl;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumCumplimiento;

/**
 * Interface para el servicio ConsultaApi
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@ProxyGen
@VertxGen
public interface ServidoresDAO {

    @GenIgnore
    static ServidoresDAO create(Vertx vertx) {
        return new ServidoresDAOImpl(vertx);
    }

    @GenIgnore
    static mx.gob.sfp.dgti.omextback.dao.reactivex.ServidoresDAO createProxy(Vertx vertx, String address) {
        return new mx.gob.sfp.dgti.omextback.dao.reactivex.ServidoresDAO(
                new ServidoresDAOVertxEBProxy(vertx.getDelegate(), address, new DeliveryOptions().setSendTimeout(55000)));
    }

    /**
     * Consulta de servidores publicos en relacion a su cumplimiento
     *
     * @param query condiciones requeridas el la consulta
     * @param collName coleccion donde se debe de realizar la busqueda
     * @param cumplimiento tipo de cumplimiento con el que se define tambien donde se hace la consulta
     *
     * @return
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    @Fluent
    public ServidoresDAO buscarCumplServ(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                         Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public ServidoresDAO consultarCumplServ(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                            Integer tamanio, Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public ServidoresDAO contarCumpl(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                     Handler<AsyncResult<JsonObject>> resultHandler);

    /**
     * Consulta de conteo de cumplimiento agrupados por institucion
     *
     * @param query condiciones requeridas el la consulta
     * @param collName coleccion donde se debe de realizar la busqueda
     * @param cumplimiento tipo de cumplimiento con el que se define tambien donde se hace la consulta
     *
     * @return objeto JSON con los conteos
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    @Fluent
    public ServidoresDAO contarCumplPorInst(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                            Handler<AsyncResult<JsonObject>> resultHandler);

    /**
     * Consulta de conteo de cumplimiento agrupados por unidad administrativa
     *
     * @param query condiciones requeridas el la consulta
     * @param collName coleccion donde se debe de realizar la busqueda
     * @param cumplimiento tipo de cumplimiento con el que se define tambien donde se hace la consulta
     *
     * @return objeto JSON con los conteos
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    @Fluent
    public ServidoresDAO contarCumplPorUA(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                          Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public ServidoresDAO contarCumpExtPorInst(JsonArray pipeline, Integer collName,
                                              Handler<AsyncResult<JsonArray>> resultHandler);


    @ProxyIgnore
    void close();

}