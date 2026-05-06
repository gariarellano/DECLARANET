/*
 * Objeto generico que permite agregar options before deploy a verticle
 */
package mx.gob.sfp.dgti.decnet;

import io.vertx.core.*;
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
public abstract class VerticleConfig extends AbstractVerticle  {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(VerticleConfig.class);

    /**
     * Mensaje para el LOG que indica que se inicia el despliegue de algun verticle
     */
    private static final String LOG_DESPLIEGUE_INICIO = "\n\n=== INICIA DESPLIEGUE DE ";

    /**
     * Mensaje para el LOG que indica que se inicio correctamente un verticle
     */
    private static final String LOG_DESPLIEGUE_VERTICLE_CORRECTO = "\n\n=== INICIADO CORRECTAMENTE: ";

    /**
     * Mensaje para el LOG que indica que se desplegaron correctamente todos los verticles en la lista
     */
    private static final String LOG_DESPLIEGUE_TODO_CORRECTO = "\n\n=== SE DESPLEGÓ TODO CORRECTAMENTE.";

    /**
     * Mensaje para el LOG que indica que no se pudo iniciar algun verticle.
     */
    private static final String LOG_DESPLIEGUE_INCORRECTO ="\n\n=== NO SE PUDO INICIAR";

    /**
     * Mensaje para el LOG que indica el numero de instancias a desplegar
     */
    private static final String LOG_INSTANCIAS_DESPLEGAR = "=== INSTANCIAS A DESPLEGAR : ";

    /**
     * Mensaje para el LOG que indica el numero de workers de pool por por instancia a desplegar
     */
    private static final String LOG_WORKERS_DESPLEGAR ="=== WORKER POR INTANCIA A DESPLEGAR (default 20): ";

    /**
     * Nombre de la variable de entorno que toma el numero de instancias a levantar para todos los verticles
     */
    private static final String VAR_NAME_VRTX_INSTANCE = "VRTX_INSTANCE";

    /**
     * Nombre de la variable de entorno que toma el numero de pool de workers a levantar para todos los verticles
     */
    private static final String VAR_NAME_VRTX_WKER_POOL = "VRTX_WKER_POOL";

    /**
     * Numero de instancias a levantar de un verticle. Por defecto se levantan 2.
     */
    private static final int VRTX_INSTANCE = System.getenv(VAR_NAME_VRTX_INSTANCE) != null ?
            Integer.parseInt(System.getenv(VAR_NAME_VRTX_INSTANCE)) : 2;

    /**
     * Numero de workers por instancia. Por defecto se levantan 20.
     */
    private static final int VRTX_WKER_POOL = System.getenv(VAR_NAME_VRTX_WKER_POOL) != null ?
            Integer.parseInt(System.getenv(VAR_NAME_VRTX_WKER_POOL)) : 20;

    /**
     * Propiedad que contendra la configuracion del DeploymentOptions.
     */
    protected DeploymentOptions opciones;


    /**
     * Método que permite desplegar 1 o N verticals al arrancar el proyecto por medio del nombre de los verticles.
     *
     * @param verticles Lista de String's que definen el paquete's.nombreVerticle donde se encuentran el vertical que
     *                  se pretenden levantar.
     */
    protected void startDeploy(List<String> verticles) {
        opciones =
                new DeploymentOptions()
                        .setConfig(config())
                        .setWorker(true)
                        .setInstances(VRTX_INSTANCE)
                        .setWorkerPoolSize(VRTX_WKER_POOL);

        deployRecursivo(verticles,0, res -> {
            if (res.succeeded()) {
                LOGGER.info(LOG_DESPLIEGUE_TODO_CORRECTO);
            } else {
                LOGGER.info(LOG_DESPLIEGUE_INCORRECTO);
            }
        });

    }

    /**
     * Metodo que permite desplegar 1 o N verticles al arrancar el proyecto y permite especificar por cada verticle
     * el numero de instancias y de workers requeridos.
     *
     * @param deploys Lista de DeployConfig en donde se tienen los vertices: su nombre, numero de instancias
     *                y numero de workers.
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 15/08/2019
     */
    protected void startDeployConObjetos(List<DeployConfig> deploys) {
        deployRecursivoConObjetos(deploys,0, res -> {
            if (res.succeeded()) {
                LOGGER.info(LOG_DESPLIEGUE_TODO_CORRECTO);
            } else {
                LOGGER.info(LOG_DESPLIEGUE_INCORRECTO);
                LOGGER.info(res.cause());
            }
        });
    }

    /**
     * Metodo recursivo para hacer deploy de los verticles en una lista de String con los nombres de verticles.
     *
     * @param lista Lista de String en donde se tienen los nombres de los verticles que se van a desplegar.
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 15/08/2019
     */
    public void deployRecursivo(List<String> lista, int index, Handler<AsyncResult<String>> resultHandler) {

        Future future = Future.future();
        future.setHandler(resultHandler);

        if (index < lista.size()) {
            LOGGER.info(LOG_DESPLIEGUE_INICIO + lista.get(index));

            Future.<Void>succeededFuture()
                    .compose(v -> Future.<String>future(
                            w -> vertx.deployVerticle(lista.get(index), opciones, w)))
                    .compose(x -> Future.<String>future(
                            y -> deployRecursivo(lista, (index + 1), y)))
                    .compose(f -> {
                        LOGGER.info(LOG_DESPLIEGUE_VERTICLE_CORRECTO + lista.get(index));
                        resultHandler.handle(Future.succeededFuture());
                    }, future);
        } else {
            resultHandler.handle(Future.succeededFuture());
        }
    }

    /**
     * Metodo recursivo para hacer deploy de los verticles en una lista de DeployConfig, en donde esta definido el
     * nombre del verticle, numero de instancias, numero de workers.
     *
     * @param lista Lista de DeployConfig en donde se tienen los vertices: su nombre, numero de instancias
     *                y numero de workers.
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 15/08/2019
     */
    public void deployRecursivoConObjetos(List<DeployConfig> lista, int index, Handler<AsyncResult<String>> resultHandler) {

        Future future = Future.future();
        future.setHandler(resultHandler);

        if (index < lista.size() ) {
            LOGGER.info(LOG_DESPLIEGUE_INICIO + lista.get(index).getNombreVerticle());
            LOGGER.info(LOG_INSTANCIAS_DESPLEGAR + lista.get(index).getNumInstancias());
            LOGGER.info(LOG_WORKERS_DESPLEGAR + lista.get(index).getNumWorkers());

            DeploymentOptions opcionesDeploy = new DeploymentOptions()
                    .setConfig(config())
                    .setWorker(true)
                    .setInstances(lista.get(index).getNumInstancias())
                    .setWorkerPoolSize(lista.get(index).getNumWorkers());

            Future.<Void>succeededFuture()
                    .compose(v -> Future.<String>future(
                            w -> vertx.deployVerticle(lista.get(index).getNombreVerticle(), opcionesDeploy, w)))
                    .compose(x -> Future.<String>future(
                            y -> deployRecursivoConObjetos(lista, (index + 1), y)))

                    .compose(f -> {
                        LOGGER.info(LOG_DESPLIEGUE_VERTICLE_CORRECTO + lista.get(index).getNombreVerticle());
                        resultHandler.handle(Future.succeededFuture());
                    }, future);
        } else {
            resultHandler.handle(Future.succeededFuture());
        }
    }

    @Override
    public abstract void start(Future<Void> startFuture) throws Exception;

}
