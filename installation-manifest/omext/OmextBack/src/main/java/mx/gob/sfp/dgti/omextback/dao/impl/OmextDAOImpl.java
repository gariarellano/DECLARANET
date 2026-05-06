/**
 * @(#)ConsultaApiImpl.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dao.impl;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.mongo.MongoClient;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumMongoDB;

/**
 * Clase para el servicio ConsultaApiImpl
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public class OmextDAOImpl{


    /**
     * Nombre variable de entorno del name
     */
    static final String ENV_MONGO_DB_NAME = "db_name_o";

    /**
     * Nombre variable de entorno del host
     */
    static final String ENV_MONGO_DB_HOST = "host_o";

    /**
     * Nombre variable de entorno del password
     */
    static final String ENV_MONGO_DB_PASSWORD = "password_o";

    /**
     * Nombre variable de entorno del port
     */
    static final String ENV_MONGO_DB_PORT = "port_o";

    /**
     * Nombre variable de entorno del host
     */
    static final String ENV_MONGO_DB_USERNAME = "username_o";

    /**
     * Nombre de prop del name
     */
    static final String PROP_MONGO_DB_NAME = "db_name";

    /**
     * Nombre de prop del host
     */
    static final String PROP_MONGO_DB_HOST = "host";

    /**
     * Nombre de prop del password
     */
    static final String PROP_MONGO_DB_PASSWORD = "password";

    /**
     * Nombre de prop del port
     */
    static final String PROP_MONGO_DB_PORT = "port";

    /**
     * Nombre de prop del username
     */
    static final String PROP_MONGO_DB_USERNAME = "username";

    /**
     * El logger
     */
    static final Logger LOGGER = LoggerFactory.getLogger(OmextDAOImpl.class);

    /**
     * Constructor
     */
    public OmextDAOImpl() {}

    static public void iniciarCliente(Vertx vertx, MongoClient client) {

        LOGGER.info("=== OmextMongoDAOImpl()");
        LOGGER.info("=== Se levanta el servicio de OmextMongoDAOImpl()");

        Integer port = System.getenv(ENV_MONGO_DB_PORT) != null ? Integer.valueOf(System.getenv(ENV_MONGO_DB_PORT)): 0;

        JsonObject config = new JsonObject()
                .put(PROP_MONGO_DB_NAME, System.getenv(ENV_MONGO_DB_NAME) )
                .put(PROP_MONGO_DB_HOST, System.getenv(ENV_MONGO_DB_HOST))
                .put(PROP_MONGO_DB_PORT, port)
                .put(PROP_MONGO_DB_USERNAME, System.getenv(ENV_MONGO_DB_USERNAME))
                .put(PROP_MONGO_DB_PASSWORD, System.getenv(ENV_MONGO_DB_PASSWORD));

        client = MongoClient.createShared(vertx, config);

    }

    /**
     * Metodo para crear el command del query
     *
     * @param tipoColeccion
     * @param collName
     * @param pipeline
     * @param batchSize
     * @return
     */
    static public JsonObject crearCommand(String tipoColeccion, Integer collName, JsonArray pipeline, Integer batchSize,
                                    Boolean allowDiskUse) {

        JsonObject command =  new JsonObject()
                .put(EnumMongoDB.AGGREGATE.getValor(), tipoColeccion.concat(collName.toString()))
                .put(EnumMongoDB.CURSOR.getValor(), new JsonObject().put(EnumMongoDB.BATCH_SIZE.getValor(), batchSize))
                .put(EnumMongoDB.PIPELINE.getValor(), pipeline)
                ;
        if(allowDiskUse.booleanValue()) {
            command.put(EnumMongoDB.ALLOW_DISK_USE.getValor(), true);
        }
        return command;
    }

}
