/**
 * @(#)CumplimientoServiceVerticle.java 24/05/2021
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
import mx.gob.sfp.dgti.omextback.service.CumplimientoService;
import mx.gob.sfp.dgti.omextback.util.constantes.Proxies;

/**
 * Verticle para el servicio de cumplimiento
 *
 * @author pavel.martinez
 * @since 24/05/2021
 */
public class CumplimientoServiceVerticle extends AbstractVerticle {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CumplimientoServiceVerticle.class);

    /**
     * SERVICIO
     */
    CumplimientoService service;

    private MessageConsumer<JsonObject> binder;

    /**
     * Inixi
     * @param startFuture
     */
    @Override
    public void start(Future<Void> startFuture) {

        LOGGER.info("=== Inicia ServiceCumplimiento()");
        service = CumplimientoService.create(vertx);

        binder = new ServiceBinder(this.getVertx())
                .setAddress(Proxies.SERVICE_CUMPLIMIENTO).setTimeoutSeconds(55)
                .register(CumplimientoService.class, service);

        binder.completionHandler(startFuture);

    }

    @Override
    public void stop(Future<Void> stopFuture) {
        binder.unregister(stopFuture);
        LOGGER.debug("=== Close ServiceCumplimiento");
    }
}
