/**
 * @(#)VistasServiceVerticle.java 24/05/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.service.verticle;

import io.vertx.core.Future;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.serviceproxy.ServiceBinder;
import mx.gob.sfp.dgti.omextback.service.VistasService;
import mx.gob.sfp.dgti.omextback.util.constantes.Proxies;

/**
 * Verticle para el servicio de vistas
 *
 * @author pavel.martinez
 * @since 24/05/2021
 */
public class VistasServiceVerticle extends AbstractVerticle {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(VistasServiceVerticle.class);

    /**
     * SERVICIO
     */
    VistasService service;

    private MessageConsumer<JsonObject> binder;

    /**
     * Inixi
     * @param startFuture
     */
    @Override
    public void start(Future<Void> startFuture) {

        LOGGER.info("=== Inicia ServiceVistas()");
        service = VistasService.create(vertx);

        binder = new ServiceBinder(this.getVertx())
                .setAddress(Proxies.SERVICE_VISTAS).setTimeoutSeconds(55)
                .register(VistasService.class, service);

        binder.completionHandler(startFuture);

    }

    @Override
    public void stop(Future<Void> stopFuture) {
        binder.unregister(stopFuture);
        LOGGER.debug("=== Close ServiceVistas");
    }
}
