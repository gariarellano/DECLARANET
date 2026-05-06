package mx.gob.sfp.dgti.verticle;

import io.vertx.core.Future;
import mx.gob.sfp.dgti.VerticalConfig;

/**
 * Clase que levanta los verticles 
 * @author Miriam Sánchez Sánchez programador07
 * @since 30/10/2019
 */
public class ServerLauncher extends VerticalConfig {

    private static final String VERTICLE = "mx.gob.sfp.dgti.verticle.Server";

    /**
     * Constructor default
     */
    public ServerLauncher(){//Utility class
    }

    /**
     * Start del verticle
     */
    @Override
    public void start(Future<Void> startFuture) throws Exception {
            this.startDeploy(VERTICLE);
    }
}
