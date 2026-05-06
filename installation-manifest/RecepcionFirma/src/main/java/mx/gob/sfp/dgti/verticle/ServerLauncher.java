package mx.gob.sfp.dgti.verticle;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import mx.gob.sfp.dgti.VerticalConfig;

/**
 * Clase que levanta los verticles 
 * @author Miriam Sánchez Sánchez programador07
 * @since 28/08/2019
 */
public class ServerLauncher extends VerticalConfig {

	private static final String VERTICLE = "mx.gob.sfp.dgti.verticle.Server";
	private static final String MAIL_VERTICLE = "mx.gob.sfp.dgti.verticle.MailServer";
	
	/**
	 * Main para pruebas locales
	 * @param args
	 */
	public static void main(String[] args) {
		 Vertx vertex = Vertx.vertx();
		 vertex.deployVerticle(new ServerLauncher());
	}

	public ServerLauncher() {
		
	}
	/**
	 * Start del verticle
	 */
	@Override
	public void start(Future<Void> startFuture) throws Exception {
		this.startDeploy(VERTICLE);
		this.startDeploy(MAIL_VERTICLE);
	}
}
