/**
 * @(#)ConsultaApiImpl.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dao.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.mongo.MongoClient;
import mx.gob.sfp.dgti.omextback.as.ConvertidorRespuestaAS;
import mx.gob.sfp.dgti.omextback.as.impl.ConvertidorRespuestaASImpl;
import mx.gob.sfp.dgti.omextback.dao.ServidoresVistaDAO;
import mx.gob.sfp.dgti.omextback.exception.SFPException;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumError;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumGraphQL;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumMongoDB;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumMongoDBColecciones;

import java.util.concurrent.TimeUnit;

/**
 * Clase para el servicio ConsultaApiImpl
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public class ServidoresVistaDAOImpl implements ServidoresVistaDAO {

    /**
     * Creador de queries
     */
    private ConvertidorRespuestaAS convertidorRespuestaAS;

    /**
     *
     */
    private MongoClient client;

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
    static final Logger LOGGER = LoggerFactory.getLogger(ServidoresVistaDAOImpl.class);

    /**
     * Constructor
     *
     * @param vertx: el vertx
     */
    public ServidoresVistaDAOImpl(Vertx vertx) {

        LOGGER.info("=== OmextMongoDAOImpl()");
        LOGGER.info("=== Se levanta el servicio de OmextMongoDAOImpl()");

        OmextDAOImpl.iniciarCliente(vertx, client);

        convertidorRespuestaAS = new ConvertidorRespuestaASImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServidoresVistaDAO consultarServidoresVista(JsonArray pipeline, Integer offset, Integer tamanio, Integer collName,
                                                  Handler<AsyncResult<JsonObject>> resultHandler) {

        JsonObject command = OmextDAOImpl.crearCommand(EnumMongoDBColecciones.COLECCION_SERVIDORES_VISTA.getValor(), collName,  pipeline, 1000, false);
        LOGGER.info("=== El command en consultarServidoresVista es: " + command);
        client.rxRunCommand(EnumMongoDB.AGGREGATE.getValor(), command)
                .timeout(40, TimeUnit.SECONDS )
                .retry(1)
                .doOnSuccess( r -> {
                            JsonObject respuesta = r
                                    .getJsonObject(EnumMongoDB.CURSOR.getValor())
                                    .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor()).size() > 0 ?
                                    r.getJsonObject(EnumMongoDB.CURSOR.getValor())
                                            .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor()).getJsonObject(0) :
                                    new JsonObject();


                            JsonArray servidores = respuesta.getJsonArray(EnumMongoDB.SERVIDORES.getValor());
                            Integer total = (respuesta.getJsonArray(EnumMongoDB.TOTAL.getValor()).size() > 0) ?
                                    respuesta.getJsonArray(EnumMongoDB.TOTAL.getValor()).getJsonObject(0).getInteger(EnumMongoDB.TOTAL.getValor()):
                                    0;

                            JsonObject res= new JsonObject()
                                    .put(EnumGraphQL.PAGINACION.getValor(),
                                            new JsonObject()
                                                    .put(EnumMongoDB.TOTAL.getValor(),
                                                            total)
                                                    .put(EnumGraphQL.TAMANIO.getValor(), tamanio)
                                                    .put(EnumGraphQL.OFFSET.getValor(), offset))
                                    .put(EnumMongoDB.SERVIDORES.getValor(), servidores);

                            resultHandler.handle(Future.succeededFuture(res));
                        }
                ).doOnError(
                e ->
                {
                    LOGGER.info("=== Ocurrio algo: " + e);
                    LOGGER.info("=== El command: " + command);
                    resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                }

        ).subscribe();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServidoresVistaDAO consultarServidoresVistaImpresion(JsonArray pipeline, Integer collName, Integer maxSize, Handler<AsyncResult<JsonArray>> resultHandler) {

        JsonObject command = OmextDAOImpl.crearCommand(EnumMongoDBColecciones.COLECCION_SERVIDORES_VISTA.getValor(), collName,  pipeline, maxSize, true);
        LOGGER.info("=== El command en consultarServidoresVistaImpresion es: " + command);
        client.rxRunCommand(EnumMongoDB.AGGREGATE.getValor(), command)
                .timeout(40, TimeUnit.SECONDS )
                .retry(1)
                .doOnSuccess( r -> {
                    JsonArray respuesta = r
                            .getJsonObject(EnumMongoDB.CURSOR.getValor())
                            .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor()).size() > 0 ?
                            r.getJsonObject(EnumMongoDB.CURSOR.getValor())
                                    .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor()) :
                            new JsonArray();
                    resultHandler.handle(Future.succeededFuture(respuesta)); }
                ).doOnError(
                e ->
                {
                    LOGGER.info("=== Ocurrio algo consultarServidoresVistaImpresion: " + e);
                    LOGGER.info("=== El command era: " + command);
                    resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                }

        ).subscribe();
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void close() {
        LOGGER.info("=== Close un cliente MongoDB");
        client.close();
    }

}
