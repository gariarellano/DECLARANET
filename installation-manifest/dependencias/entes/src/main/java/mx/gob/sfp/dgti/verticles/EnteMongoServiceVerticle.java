/**
 * @(#)EnteMongoServiceVerticle.java 11/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.verticles;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.serviceproxy.ServiceBinder;
import mx.gob.sfp.dgti.mongoservice.EnteMongoService;
import org.apache.log4j.Logger;

/**
 * Clase con el verticle que levanta los servicios de mongo
 * 
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 11/02/2019
 */
public class EnteMongoServiceVerticle extends AbstractVerticle {

	EnteMongoService service;

	private MessageConsumer<JsonObject> binder;
	
	final Logger logger = Logger.getLogger(EnteMongoServiceVerticle.class);

	@Override
	public void init(Vertx vertx, Context context) {

		super.init(vertx, context);

	}
	
	@Override
	public void start(Future<Void> startFuture) {

		logger.debug("-------------START MONGO VERTICLE");

		service = EnteMongoService.create(this.getVertx(), config());
		
		binder = new ServiceBinder(vertx.getDelegate())
			.setAddress(EnteMongoService.SERVICE_ADDRESS)
			.register(EnteMongoService.class, service);
		binder.completionHandler(startFuture);

	}

	@Override
	  public void stop(Future<Void> stopFuture) {
	      binder.unregister(stopFuture);
	      logger.debug("-------------CLOSE MONGO VERTICLE");
	  }

}
