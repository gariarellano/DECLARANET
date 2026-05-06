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
public class IpRegistroLauncher extends VerticalConfig {

	private static final String REGISTRO_VERTICLE = "mx.gob.sfp.dgti.verticle.RegistroVerticle";
	private static final String VALIDADO_RENAPO_VERTICLE = "mx.gob.sfp.dgti.verticle.ValidadoRenapoVerticle";
	private static final String EMAIL_VERTICLE = "mx.gob.sfp.dgti.verticle.EnvioEmailVerticle";
        private static final String HEALTH_VERTICLE = "mx.gob.sfp.dgti.helper.HealthChecks";
	
	/**
	 * Main para pruebas locales
	 * @param args
	 */
	public static void main(String[] args) {
		 Vertx vertex = Vertx.vertx();
		 vertex.deployVerticle(new IpRegistroLauncher());
	}

	/**
	 * Constructor default
	 */
	public IpRegistroLauncher(){

	}

	/**
	 * Start del verticle
	 */
	@Override
	public void start(Future<Void> startFuture) throws Exception {
		List<String> vertices = new ArrayList<>(); 
		vertices.add(REGISTRO_VERTICLE);
		vertices.add(VALIDADO_RENAPO_VERTICLE);
		vertices.add(EMAIL_VERTICLE);
                vertices.add(HEALTH_VERTICLE);
		this.startDeploy(vertices);
	}
}
