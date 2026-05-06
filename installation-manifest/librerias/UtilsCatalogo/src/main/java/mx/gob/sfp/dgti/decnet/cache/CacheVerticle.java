/**
 * @(#)CacheVerticle.java 18/07/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.decnet.cache;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.serviceproxy.ServiceBinder;

/**
 * Verticle para atender el cache
 *
 * @author Pavel Alexei Martinez Regalado programador02
 * @since 18/07/2019
 */
public class CacheVerticle  extends AbstractVerticle {


    /**
     * Servicio de cache
     */
    CacheService service;

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheVerticle.class);

    /**
     * Message consumer
     */
    private MessageConsumer<JsonObject> binder;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
    }

    /**
     * Inixi
     * @param startFuture
     */
    @Override
    public void start(Future<Void> startFuture) {

        LOGGER.info("=== Inicia CacheVerticle()");
        service = CacheService.create(vertx);

        binder = new ServiceBinder(vertx)
                .setAddress(CacheService.SERVICE_ADDRESS)
                .register(CacheService.class, service);
        binder.completionHandler(startFuture);

    }

    @Override
    public void stop(Future<Void> stopFuture) {
        binder.unregister(stopFuture);

    }

}
