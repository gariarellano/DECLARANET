/**
 * @(#)GuardadoDeclaracionVerticle.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.servicioext;

import io.vertx.core.Future;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.serviceproxy.ServiceBinder;
import mx.gob.sfp.dgti.validaciondeclaracion.util.Proxies;

/**
 * Verticle para el servicio LlamadorServiciosVerticle
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
public class GuardadoDeclaracionVerticle extends AbstractVerticle {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GuardadoDeclaracionVerticle.class);

    /**
     * Servicio
     */
    GuardadoDeclaracion service;

    /**
     * Inicia
     * @param startFuture
     */
    @Override
    public void start(Future<Void> startFuture) {

        MessageConsumer<JsonObject> binder;

        LOGGER.info("=== Inicia GuardadoDeclaracionVerticle()");
        service = GuardadoDeclaracion.create(vertx);

        binder = new ServiceBinder(this.getVertx())
                .setAddress(Proxies.SERVICE_ADDRESS_GUARDADO)
                .register(GuardadoDeclaracion.class, service);

        binder.completionHandler(startFuture);

    }
}
