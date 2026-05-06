/**
 * @(#)MainDeployer.java 18/07/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import mx.gob.sfp.dgti.omextback.despliegue.DeployConfig;
import mx.gob.sfp.dgti.omextback.despliegue.VerticleConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Verticle principal para inicializar la aplicacion
 *
 * @author Pavel Alexei Martinez Regalado programador02
 * @since 18/07/2019
 */
public class MainDeployer extends VerticleConfig {

    /**
     * Verticle de Http
     */
    private static final String VERTICLE_HTTP =
            "mx.gob.sfp.dgti.omextback.server.ServerVerticle";

    /**
     * Verticle de Http
     */
    private static final String VERTICLE_SERVICE_PERIODOS =
            "mx.gob.sfp.dgti.omextback.service.verticle.PeriodosServiceVerticle";

    /**
     * Verticle de Http
     */
    private static final String VERTICLE_SERVICE_VISTAS =
            "mx.gob.sfp.dgti.omextback.service.verticle.VistasServiceVerticle";

    /**
     * Verticle de Http
     */
    private static final String VERTICLE_SERVICE_CUMPLIMIENTO =
            "mx.gob.sfp.dgti.omextback.service.verticle.CumplimientoServiceVerticle";

    /**
     * Mongo de Omext
     */
    private static final String VERTICLE_MONGO_OMEXT =
            "mx.gob.sfp.dgti.omextback.dao.OmextMongoDAOVerticle";

    /**
     * Nombre de la variable de entorno que toma el numero de pool de workers a
     * levantar para todos los verticles
     */
    private static final String VAR_NAME_VRTX_WKER_POOL = "VRTX_WKER_POOL";

    /**
     * Numero de workers por instancia. Por defecto se levantan 20.
     */
    private static final int VRTX_WKER_POOL = System.getenv(VAR_NAME_VRTX_WKER_POOL) != null ?
            Integer.parseInt(System.getenv(VAR_NAME_VRTX_WKER_POOL)) : 20;

    /**
     * Nombre de la variable de entorno que toma el numero de pool de workers a
     * levantar para todos los verticles
     */
    private static final String VAR_NAME_VRTX_INSTANCIAS_HTTP = "VRTX_INSTANCIAS_HTTP";

    /**
     * Numero de workers por instancia. Por defecto se levantan 2.
     */
    private static final int VRTX_INSTANCIAS_HTTP = System.getenv(VAR_NAME_VRTX_INSTANCIAS_HTTP) != null ?
            Integer.parseInt(System.getenv(VAR_NAME_VRTX_INSTANCIAS_HTTP)) : 2;

    /**
     * Nombre de la variable de entorno que toma el numero de pool de workers a
     * levantar para todos los verticles
     */
    private static final String VAR_NAME_VRTX_INSTANCIAS_LLAMADOR = "VRTX_INSTANCIAS_LLAMADOR";

    /**
     * Numero de workers por instancia. Por defecto se levantan 2.
     */
    private static final int VRTX_INSTANCIAS_LLAMADOR = System.getenv(VAR_NAME_VRTX_INSTANCIAS_LLAMADOR) != null ?
            Integer.parseInt(System.getenv(VAR_NAME_VRTX_INSTANCIAS_LLAMADOR)) : 2;

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MainDeployer.class);

    public static void main(String[] args) {
        Vertx vertex = Vertx.vertx();
        vertex.deployVerticle(new MainDeployer());
    }

    public MainDeployer() { //Constructor
    }

    /**
     * Inicia la aplicacion, levanta los verticles requeridos
     *
     * @param startFuture
     * @throws Exception
     *
     * @author Pavel Alexei Martinez Regalado programador02
     * @since 18/09/2019
     */
    @Override
    public void start(Future<Void> startFuture) throws Exception {

        LOGGER.info("=== Main deployer");
        List<DeployConfig> deploys = new ArrayList<>();

        deploys.add(new DeployConfig(VERTICLE_HTTP, VRTX_INSTANCIAS_HTTP, VRTX_WKER_POOL));
        deploys.add(new DeployConfig(VERTICLE_MONGO_OMEXT, VRTX_INSTANCIAS_LLAMADOR, VRTX_WKER_POOL));

        deploys.add(new DeployConfig(VERTICLE_SERVICE_CUMPLIMIENTO, 1, 2));
        deploys.add(new DeployConfig(VERTICLE_SERVICE_PERIODOS, 1, 2));
        deploys.add(new DeployConfig(VERTICLE_SERVICE_VISTAS, 1, 2));

        startDeployConObjetos(deploys);

    }
}
