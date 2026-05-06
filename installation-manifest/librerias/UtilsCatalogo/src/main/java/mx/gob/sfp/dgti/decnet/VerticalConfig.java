/*
 * Objeto generico que permite agregar options before deploy a verticle
 */
package mx.gob.sfp.dgti.decnet;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.List;

/**
 * Clase que permite heredar las opciones de workers as
 * Deployment options to deploy verticle para el proyecto.
 * 
 * @author cgarias
 * @since 07/08/2019
 */
public abstract class VerticalConfig extends AbstractVerticle  {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerticalConfig.class);
    private static final String VAR_NAME_VRTX_INSTANCE = "VRTX_INSTANCE";
    private static final String VAR_NAME_VRTX_WKER_POOL = "VRTX_WKER_POOL";
    private static final String LOG_DESPLIEGUE_CORRECTO = "Iniciado correctamente : ";
    private static final String LOG_DESPLIEGUE_INCORRECTO ="No se pudo iniciar";
    private static final String LOG_INSTANCIAS_DESPLEGAR = "INSTANCIAS A DESPLEGAR : ";
    private static final String LOG_WORKERS_DESPLEGAR ="WORKER POR INTANCIA A DESPLEGAR (default 20) : ";
    private static final Integer VRTX_INSTANCE = Integer.parseInt(System.getenv(VAR_NAME_VRTX_INSTANCE) != null ? System.getenv(VAR_NAME_VRTX_INSTANCE):"2");
    private static final Integer VRTX_WKER_POOL = Integer.parseInt(System.getenv(VAR_NAME_VRTX_WKER_POOL) != null ? System.getenv(VAR_NAME_VRTX_WKER_POOL) : "2");
    /**
     * Propiedad que contendra la configuracion del DeploymentOptions.
     */
    protected DeploymentOptions opciones;
    
    
    /**
     * Método que permite desplegar 1 o N verticals a 
     * la hora de arrancar el proyecto
     * 
     * @param verticles Lista de String's que definen el paquetes.nombreVerticle donde se encuentran el vertical que se va a desplegar.
     */
    protected void startDeploy(List<String> verticles) {
        opciones = 
                new DeploymentOptions()
                        .setConfig(config())
                        .setWorker(true)
                        .setInstances(VRTX_INSTANCE)
                        .setWorkerPoolSize(VRTX_WKER_POOL);
        LOGGER.info(LOG_INSTANCIAS_DESPLEGAR.concat(VRTX_INSTANCE.toString()));
        LOGGER.info(LOG_WORKERS_DESPLEGAR.concat(VRTX_WKER_POOL.toString()));
        verticles.stream().forEach((index) -> {
            this.startDeploy(index);
         });
    }
    
    /**
     * Método que permite desplegar un verticle del proyecto.
     * 
     * @param verticle String que define el groupId.nombreVerticle del vertical que se va a levantar.
     */
    protected void startDeploy(String verticle) {
        if (opciones == null)
            opciones = new DeploymentOptions()
                                .setConfig(config())
                                .setWorker(true)
                                .setInstances(Integer.parseInt(System.getenv(VAR_NAME_VRTX_INSTANCE) != null ? System.getenv(VAR_NAME_VRTX_INSTANCE):"2"))
                                .setWorkerPoolSize(Integer.parseInt(System.getenv(VAR_NAME_VRTX_WKER_POOL) != null ? System.getenv(VAR_NAME_VRTX_WKER_POOL) : "20"));
        Future.<Void>succeededFuture()
            .compose(v -> Future.<String>future(
                    f -> vertx.deployVerticle(verticle, opciones, f)))
            .compose(v -> LOGGER.info(LOG_DESPLIEGUE_CORRECTO + verticle),
                    Future.future().setHandler(ar -> {
                        if (ar.failed()) {
                            LOGGER.error(LOG_DESPLIEGUE_INCORRECTO, ar.cause());
                        }
                    }));
    }

    @Override
    public abstract void start(Future<Void> startFuture) throws Exception;
}
