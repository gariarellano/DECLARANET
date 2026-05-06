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
import mx.gob.sfp.dgti.omextback.dao.impl.VistasDAOImpl;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumEstatusVista;

/**
 * Interface para el servicio ConsultaApi
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@ProxyGen
@VertxGen
public interface VistasDAO {

    @GenIgnore
    static VistasDAO create(Vertx vertx) {
        return new VistasDAOImpl(vertx);
    }

    @GenIgnore
    static mx.gob.sfp.dgti.omextback.dao.reactivex.VistasDAO createProxy(Vertx vertx, String address) {
        return new mx.gob.sfp.dgti.omextback.dao.reactivex.VistasDAO(
                new VistasDAOVertxEBProxy(vertx.getDelegate(), address, new DeliveryOptions().setSendTimeout(55000)));
    }

    @Fluent
    public VistasDAO consultarVistas(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public VistasDAO consultarInstitucionesPorPeriodo(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public VistasDAO guardarVista(JsonObject vista, Integer collName, Handler<AsyncResult<String>> resultHandler);

    @Fluent
    public VistasDAO consultarVistasPendientesUsuario(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public VistasDAO obtenerVistaPorFolio(Integer collName, JsonArray pipeline, Handler<AsyncResult<JsonObject>> resultHandler) ;

    @Fluent
    public VistasDAO guardarEstatusVista(Integer collName, String folio, EnumEstatusVista estatus,
                                         Handler<AsyncResult<Boolean>> resultHandler);
    @Fluent
    public VistasDAO guardarFirmaVista(Integer collName, String tipoFirmado, String folio, JsonObject datosFirma,
                                       Handler<AsyncResult<Boolean>> resultHandler);

    @Fluent
    public VistasDAO guardarEstatusVistasEnPeriodo(Integer collName, String idPeriodo, EnumEstatusVista estatus,
                                                   Handler<AsyncResult<Boolean>> resultHandler);

    @ProxyIgnore
    void close();

}