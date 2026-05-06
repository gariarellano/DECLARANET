package mx.gob.sfp.dgti.verticle;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import mx.gob.sfp.dgti.VerticalConfig;

/**
 * Clase que levanta los verticles 
 * @author Miriam Sánchez Sánchez programador07
 * @since 11/06/2019
 */
public class IPLauncher extends VerticalConfig {

	private static final String AUTENTICACION_VERTICLE = "mx.gob.sfp.dgti.verticle.AutenticacionVerticle";
	private static final String VALIDADO_RENAPO_VERTICLE = "mx.gob.sfp.dgti.verticle.ValidadoRenapoVerticle";
        private static final String VALIDADO_MAIL_SENDER = "mx.gob.sfp.dgti.verticle.MailSender";
        private static final String HEALT_CHECKER = "mx.gob.sfp.dgti.helper.HealthChecks";

	/**
	 * Main para pruebas locales
	 * @param args
	 */
	public static void main(String[] args) {
		 Vertx vertex = Vertx.vertx();
		 vertex.deployVerticle(new IPLauncher());
	}

	/**
	 * Constructor default
	 */
	public IPLauncher(){

	}

	/**
	 * Start del verticle
	 */
	@Override
	public void start(Future<Void> startFuture) throws Exception {
		List<String> vertices = new ArrayList<>(); 
		vertices.add(AUTENTICACION_VERTICLE);
		vertices.add(VALIDADO_RENAPO_VERTICLE);
                vertices.add(VALIDADO_MAIL_SENDER);
                vertices.add(HEALT_CHECKER);                
		this.startDeploy(vertices);
	}
}
