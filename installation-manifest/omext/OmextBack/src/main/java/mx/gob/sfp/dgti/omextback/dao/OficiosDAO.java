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
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import mx.gob.sfp.dgti.omextback.dao.impl.OficiosDAOImpl;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumMes;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumTipoDeclaracion;

/**
 * Interface para el servicio ConsultaApi
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@ProxyGen
@VertxGen
public interface OficiosDAO {

    @GenIgnore
    static OficiosDAO create(Vertx vertx) {
        return new OficiosDAOImpl(vertx);
    }

    @GenIgnore
    static mx.gob.sfp.dgti.omextback.dao.reactivex.OficiosDAO createProxy(Vertx vertx, String address) {
        return new mx.gob.sfp.dgti.omextback.dao.reactivex.OficiosDAO(
                new OficiosDAOVertxEBProxy(vertx.getDelegate(), address, new DeliveryOptions().setSendTimeout(55000)));
    }

    @Fluent
    public OficiosDAO obtenerDatosUsuarioFirma(Integer collName, Handler<AsyncResult<JsonObject>> resultHandler);


    @Fluent
    public OficiosDAO consumirActualizarCountVista(Integer collName, Integer posiciones, EnumTipoDeclaracion tipoDeclaracion,
                                                   Integer anio, EnumMes mes, Handler<AsyncResult<Integer>> resultHandler);

    @Fluent
    public OficiosDAO obtenerTextoOficioActual(Integer collName, Handler<AsyncResult<JsonObject>> resultHandler);


    @Fluent
    public OficiosDAO obtenerTextoOficioActualPorId(String id, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler);


    @ProxyIgnore
    void close();

}