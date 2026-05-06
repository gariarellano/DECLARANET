/**
 * @(#)LlamadorServicios.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.servicioext;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import mx.gob.sfp.dgti.validaciondeclaracion.dto.LlamadoDTO;

import java.util.List;
import java.util.Set;

/**
 * Interface para el servicio LlamadorServicios
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
@ProxyGen
@VertxGen
public interface LlamadorServicios {

    @GenIgnore
    static LlamadorServicios create(Vertx vertx) {
        return new LlamadorServiciosImpl(vertx);
    }

    @GenIgnore
    static mx.gob.sfp.dgti.validaciondeclaracion.servicioext.reactivex.LlamadorServicios createProxy(Vertx vertx, String address) {
        return new mx.gob.sfp.dgti.validaciondeclaracion.servicioext.reactivex.LlamadorServicios(
                new LlamadorServiciosVertxEBProxy(vertx.getDelegate(), address));
    }

    /**
     * Metodo para llamar todos los servicios validadores
     *
     * @param listaServicios: Lista que contiene el catalogo de activos
     * @param resultHandler: handler para la respuesta de tipo JsonObject
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 18/07/2019
     */
    @Fluent
    LlamadorServicios llamarServicios(List<LlamadoDTO> listaServicios, JsonObject declaracion,  Set<String> mandados,
                                      Handler<AsyncResult<JsonObject>> resultHandler);

    /**
     * Metodo para llamar todos los servicios validadores para el aviso por cambio de dependencia
     *
     * @param listaServicios: Lista que contiene el catalogo de activos
     * @param resultHandler: handler para la respuesta de tipo JsonObject
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 29/01/2020
     */
    @Fluent
    LlamadorServicios llamarServiciosAviso(List<LlamadoDTO> listaServicios, JsonObject aviso,  Set<String> mandados,
                                              Handler<AsyncResult<JsonObject>> resultHandler);

    /**
     * Metodo para llamar el servicio validador de notas
     *
     * @param servicio: objeto LlamadoDTO con informacion para llamar el servicio
     * @param notas: JsonObject con las notas
     * @param resultHandler: hancler para la respuesta de tipo JsonObject
     *
     * @author pavel.martinez
     * @since 20/02/2020
     */
    @Fluent
    LlamadorServicios llamarServiciosNotasAclaratorias(LlamadoDTO servicio, JsonObject notas,
                                                          Handler<AsyncResult<JsonObject>> resultHandler);
}