/**
 * @(#)MainDeployer.java 11/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.verticles;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * Clase con el verticle principal que levanta los otros verticles y 
 * toma valores de las variables de entorno
 * 
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 11/02/2019
 */
public class MainDeployer extends VerticleConfig {

	/**
	 * Verticle de Http
	 */
	private static final String VERTICLE_MONGO = "mx.gob.sfp.dgti.verticles.EnteMongoServiceVerticle";

	/**
	 * Verticle de Http
	 */
	private static final String VERTICLE_AS = "mx.gob.sfp.dgti.verticles.ApplicationServiceVerticle";

	/**
	 * Verticle de Http
	 */
	private static final String VERTICLE_HTTP = "mx.gob.sfp.dgti.verticles.AppVerticle";

	final Logger logger = Logger.getLogger(MainDeployer.class);

	public static void main(String[] args) {
		 Vertx vertex = Vertx.vertx();
		 vertex.deployVerticle(new MainDeployer());
	}

	public MainDeployer(){

	}

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		logger.info("Start the future");
		List<String> verticles = new ArrayList<>();
		verticles.add(VERTICLE_MONGO);
		verticles.add(VERTICLE_AS);
		verticles.add(VERTICLE_HTTP);
		startDeploy(verticles);
		
	}

}