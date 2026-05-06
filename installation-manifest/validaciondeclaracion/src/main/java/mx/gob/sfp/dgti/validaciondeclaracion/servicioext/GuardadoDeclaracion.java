/**
 * @(#)GuardadoDeclaracion.java 10/12/2019
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
import mx.gob.sfp.dgti.validaciondeclaracion.dto.RespuestaDTO;

/**
 * Interface para el servicio GuardadoDeclaracion
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 10/12/2019
 */
@ProxyGen
@VertxGen
public interface GuardadoDeclaracion {

        @GenIgnore
        static GuardadoDeclaracion create(Vertx vertx) {
            return new GuardadoDeclaracionImpl(vertx);
        }

        @GenIgnore
        static mx.gob.sfp.dgti.validaciondeclaracion.servicioext.reactivex.GuardadoDeclaracion createProxy(Vertx vertx, String address) {
            return new mx.gob.sfp.dgti.validaciondeclaracion.servicioext.reactivex.GuardadoDeclaracion(
                    new GuardadoDeclaracionVertxEBProxy(vertx.getDelegate(), address));
        }

        /**
         * Metodo para guardar la declaracion o aviso por cambio de dependencia
         *
         * @param resultHandler: handler para la respuesta de tipo RespuestaDTO
         *
         * @author Pavel Alexei Martinez Regalado aka programador02
         * @since 10/12/2019
         */
        @Fluent
        GuardadoDeclaracion guardarDeclaracion(JsonObject declaracion,
                                            Handler<AsyncResult<RespuestaDTO>> resultHandler);

    }

