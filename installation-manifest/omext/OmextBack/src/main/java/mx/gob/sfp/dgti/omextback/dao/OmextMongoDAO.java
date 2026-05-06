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
import mx.gob.sfp.dgti.omextback.dao.impl.OmextMongoDAOImpl;
import mx.gob.sfp.dgti.omextback.util.constantes.*;

import java.util.List;

/**
 * Interface para el servicio ConsultaApi
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@ProxyGen
@VertxGen
public interface OmextMongoDAO {

    @GenIgnore
    static OmextMongoDAO create(Vertx vertx) {
        return new OmextMongoDAOImpl(vertx);
    }

    @GenIgnore
    static mx.gob.sfp.dgti.omextback.dao.reactivex.OmextMongoDAO createProxy(Vertx vertx, String address) {
        return new mx.gob.sfp.dgti.omextback.dao.reactivex.OmextMongoDAO(
                new OmextMongoDAOVertxEBProxy(vertx.getDelegate(), address, new DeliveryOptions().setSendTimeout(55000)));
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
    public OmextMongoDAO buscarCumplServ(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                          Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public OmextMongoDAO consultarCumplServ(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                            Integer tamanio, Handler<AsyncResult<JsonObject>> resultHandler);

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
    public OmextMongoDAO contarCumplPorInst(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
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
    public OmextMongoDAO contarCumplPorUA(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                          Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public OmextMongoDAO contarCumpExtPorInst(JsonArray pipeline, Integer collName,
                                              Handler<AsyncResult<JsonArray>> resultHandler);

    @Fluent
    public OmextMongoDAO consultarPeriodos(JsonArray pipeline, Integer collName, Integer offset, Integer tamanio,
                                           Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public OmextMongoDAO obtenerDatosUsuarioFirma(Integer collName, Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public OmextMongoDAO consultarPeriodoSimple(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler);

    /**
     * Consulta de conteo de cumplimiento en general
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
    public OmextMongoDAO contarCumpl(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                     Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public OmextMongoDAO consumirActualizarCountVista(Integer collName, Integer posiciones, EnumTipoDeclaracion tipoDeclaracion,
                                                      Integer anio, EnumMes mes, Handler<AsyncResult<Integer>> resultHandler);

    @Fluent
    public OmextMongoDAO consultarInstitucionesPorPeriodo(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public OmextMongoDAO consultarInstitucionesVistaPorPeriodo(String idPeriodo, Integer collName, Handler<AsyncResult<List<String>>> resultHandler);

    @Fluent
    public OmextMongoDAO consultarVistas(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public OmextMongoDAO consultarServidoresVista(JsonArray pipeline, Integer offset, Integer tamanio, Integer collName,
                                                  Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public OmextMongoDAO guardarPeriodo(JsonObject periodo, Integer collName, Handler<AsyncResult<String>> resultHandler);

    @Fluent
    public OmextMongoDAO guardarVista(JsonObject vista, Integer collName, Handler<AsyncResult<String>> resultHandler);

    @Fluent
    public OmextMongoDAO consultarVistasPendientesUsuario(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public OmextMongoDAO obtenerTextoOficioActual(Integer collName, Handler<AsyncResult<JsonObject>> resultHandler);

    /**
     * Funcion para actualizar el estatus de un periodo
     * @param collName
     * @param idPeriodo
     * @param estatus
     * @param resultHandler
     * @return
     */
    @Fluent
    public OmextMongoDAO guardarEstatusVistasEnPeriodo(Integer collName, String idPeriodo, EnumEstatusPeriodo estatus,
                                                       Handler<AsyncResult<Boolean>> resultHandler);
    @Fluent
    public OmextMongoDAO obtenerTextoOficioActualPorId(String id, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public OmextMongoDAO consultarServidoresVistaImpresion(JsonArray pipeline, Integer collName, Integer maxSize, Handler<AsyncResult<JsonArray>> resultHandler);

    @Fluent
    public OmextMongoDAO obtenerVistaPorFolio(Integer collName, JsonArray pipeline, Handler<AsyncResult<JsonObject>> resultHandler) ;

    @Fluent
    public OmextMongoDAO guardarEstatusVista(Integer collName, String folio, EnumEstatusVista estatus,
                                             Handler<AsyncResult<Boolean>> resultHandler);
    @Fluent
    public OmextMongoDAO guardarFirmaVista(Integer collName, String tipoFirmado, String folio, JsonObject datosFirma,
                                                  Handler<AsyncResult<Boolean>> resultHandler);

    @Fluent
    public OmextMongoDAO actualizarFechaPeriodo(Integer collName, String idPeriodo, String fecha,
                                                Handler<AsyncResult<Boolean>> resultHandler);
    @Fluent
    public OmextMongoDAO actualizarFechaExtensionesPeriodo(Integer collName, String idPeriodo, JsonArray extensiones,
                                                           Handler<AsyncResult<Boolean>> resultHandler);

    @Fluent
    public OmextMongoDAO todasVistasDelPeriodoGeneradas(String idPeriodo, Integer collName,
                                                      Handler<AsyncResult<Boolean>> resultHandler);

    @Fluent
    public OmextMongoDAO obtenerPreconteosInicio(Integer collName, Integer anio, Handler<AsyncResult<List<JsonObject>>> resultHandler);

    /***
     *
     * @param pipeline
     * @param collName
     * @param resultHandler
     * @return
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    @Fluent
    OmextMongoDAO obtenerPreconteosInicioGrupo(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonArray>> resultHandler);

    @ProxyIgnore
    void close();

}