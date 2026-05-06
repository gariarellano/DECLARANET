/**
 * @(#)ConsultaApiVerticle.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dao;

import io.vertx.core.Future;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.serviceproxy.ServiceBinder;
import mx.gob.sfp.dgti.omextback.util.constantes.Proxies;

/**
 * Verticle para el servicio ConsultaApiVerticle
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public class OmextMongoDAOVerticle extends AbstractVerticle {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OmextMongoDAOVerticle.class);

    /**
     * SERVICIO
     */
    OmextMongoDAO service;

    private MessageConsumer<JsonObject> binder;

    /**
     * Inixi
     * @param startFuture
     */
    @Override
    public void start(Future<Void> startFuture) {

        LOGGER.info("=== Inicia OmextMongoDAOVerticle() ");
        service = OmextMongoDAO.create(vertx);

        binder = new ServiceBinder(this.getVertx())
                .setAddress(Proxies.SERVICE_MONGO_CUMPLIMIENTO_OMEXT)
                .register(OmextMongoDAO.class, service);

        binder.completionHandler(startFuture);

    }

    @Override
    public void stop(Future<Void> stopFuture) {
        binder.unregister(stopFuture);
        LOGGER.debug("=== Close Mongo verticle");
    }
}
