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
import mx.gob.sfp.dgti.omextback.dao.ServidoresDAO;
import mx.gob.sfp.dgti.omextback.exception.SFPException;
import mx.gob.sfp.dgti.omextback.util.constantes.*;

import java.util.concurrent.TimeUnit;

/**
 * Clase para el servicio ConsultaApiImpl
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public class ServidoresDAOImpl implements ServidoresDAO {

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
    static final Logger LOGGER = LoggerFactory.getLogger(ServidoresDAOImpl.class);

    /**
     * Constructor
     *
     * @param vertx: el vertx
     */
    public ServidoresDAOImpl(Vertx vertx) {

        LOGGER.info("=== OmextMongoDAOImpl()");
        LOGGER.info("=== Se levanta el servicio de OmextMongoDAOImpl()");

        OmextDAOImpl.iniciarCliente(vertx,client);

        convertidorRespuestaAS = new ConvertidorRespuestaASImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServidoresDAO buscarCumplServ(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                          Handler<AsyncResult<JsonObject>> resultHandler) {
        String tipoColeccion = (cumplimiento.equals(EnumCumplimiento.PENDIENTE)) ?
                EnumMongoDBColecciones.COLECCION_NO_LOCALIZADOS_RUSP.getValor()
                : EnumMongoDBColecciones.COLECCION_CUMPLIMIENTO.getValor();

        JsonObject command = OmextDAOImpl.crearCommand(tipoColeccion, collName,  pipeline, 200, false);

        client.rxRunCommand(EnumMongoDB.AGGREGATE.getValor(), command)
                .timeout(40, TimeUnit.SECONDS )
                .retry(1)
                .doOnSuccess(
                        resp -> {
                            JsonArray respuesta = resp
                                    .getJsonObject(EnumMongoDB.CURSOR.getValor())
                                    .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor()).size() > 0 ?
                                    resp
                                            .getJsonObject(EnumMongoDB.CURSOR.getValor())
                                            .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor()) :
                                    new JsonArray();

                            resultHandler.handle(Future.succeededFuture(
                                    convertidorRespuestaAS.extraerRespServ(respuesta.getJsonObject(0), cumplimiento)
                            ));
                        }
                )
                .doOnError(
                        e -> {
                           LOGGER.info("=== Hay error en buscarCumplServ() " + e);
                            LOGGER.info("=== El command en buscarCumplServ() era + " + command);
                            resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                        }
                )
        .subscribe();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServidoresDAO contarCumpl(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                     Handler<AsyncResult<JsonObject>> resultHandler) {
        String tipoColeccion = (cumplimiento.equals(EnumCumplimiento.PENDIENTE)) ?
                EnumMongoDBColecciones.COLECCION_NO_LOCALIZADOS_RUSP.getValor()
                : EnumMongoDBColecciones.COLECCION_CUMPLIMIENTO.getValor();

        JsonObject command = OmextDAOImpl.crearCommand(tipoColeccion, collName,  pipeline, 1000, false);

        client.rxRunCommand(EnumMongoDB.AGGREGATE.getValor(), command)
                .timeout(40, TimeUnit.SECONDS )
                .retry(1)
                .doOnSuccess(
                        resp -> {
                            Integer total = resp
                                    .getJsonObject(EnumMongoDB.CURSOR.getValor())
                                    .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor()).size() > 0 ?
                                    resp
                                            .getJsonObject(EnumMongoDB.CURSOR.getValor())
                                            .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor())
                                            .getJsonObject(0).getInteger(EnumMongoDB.TOTAL.getValor()) :
                                    0;
                            resultHandler.handle(Future.succeededFuture(
                                    new JsonObject()
                                            .put(EnumGraphQL.NAME.getValor(), cumplimiento.getValor())
                                            .put(EnumGraphQL.VALUE.getValor(), total))
                            );
                        }
                )
                .doOnError(
                        e -> {
                            LOGGER.info("=== contarCumpl() " + e.getMessage());
                            LOGGER.info("=== El command en contarCumpl() era + " + command);
                            resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                        }
                )
        .subscribe();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServidoresDAO consultarCumplServ(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                            Integer tamanio, Handler<AsyncResult<JsonObject>> resultHandler) {
        String tipoColeccion = (cumplimiento.equals(EnumCumplimiento.PENDIENTE)) ?
                EnumMongoDBColecciones.COLECCION_NO_LOCALIZADOS_RUSP.getValor()
                : EnumMongoDBColecciones.COLECCION_CUMPLIMIENTO.getValor();

        Integer maxSize = (tamanio.intValue() > 5000) ? 5000: tamanio;

        JsonObject command = OmextDAOImpl.crearCommand(tipoColeccion, collName,  pipeline, maxSize, false);
        client.rxRunCommand(EnumMongoDB.AGGREGATE.getValor(), command)
                .timeout(40, TimeUnit.SECONDS )
                .retry(1)
                .doOnSuccess(
                        resp -> {
                            JsonObject respuesta = resp
                                    .getJsonObject(EnumMongoDB.CURSOR.getValor())
                                    .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor()).size() > 0 ?
                                    resp.getJsonObject(EnumMongoDB.CURSOR.getValor())
                                            .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor()).getJsonObject(0) :
                                    new JsonObject();

                            if(!respuesta.containsKey(EnumMongoDB.RESULTADOS.getValor())) {
                                respuesta.put(EnumMongoDB.RESULTADOS.getValor(), new JsonArray());
                            }
                            resultHandler.handle(Future.succeededFuture(
                                    convertidorRespuestaAS.extraerRespServ(respuesta, cumplimiento)
                            ));
                        }
                )
                .doOnError(
                        e -> {
                            LOGGER.info("=== Hay error en consultarCumplServ() " + e);
                            LOGGER.info("=== El command en consultarCumplServ() era + " + command);
                            resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                        }
                )
        .subscribe();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServidoresDAO contarCumpExtPorInst(JsonArray pipeline, Integer collName,
                                            Handler<AsyncResult<JsonArray>> resultHandler) {

        JsonObject command = OmextDAOImpl.crearCommand(
                EnumMongoDBColecciones.COLECCION_CUMPLIMIENTO.getValor(), collName,  pipeline, 1000, false);

        //LOGGER.info("=== El command en contarCumpExtPorInst es: " + command);

        client.rxRunCommand(EnumMongoDB.AGGREGATE.getValor(), command)
                .timeout(40, TimeUnit.SECONDS )
                .retry(1)
                .doOnSuccess(
                        resp -> {
                            JsonArray conteos = resp
                                    .getJsonObject(EnumMongoDB.CURSOR.getValor())
                                    .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor()).size() > 0 ?
                                    resp
                                            .getJsonObject(EnumMongoDB.CURSOR.getValor())
                                            .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor()) : new JsonArray();
                            resultHandler.handle(Future.succeededFuture(conteos));
                        }
                )
                .doOnError(
                        e -> {
                            LOGGER.info("=== Hay error en contarCumplPorInst() " + e);
                            LOGGER.info("=== El command era + " + command);
                            resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                        }
                )
                .subscribe();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServidoresDAO contarCumplPorInst(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                            Handler<AsyncResult<JsonObject>> resultHandler) {
        String tipoColeccion = (cumplimiento.equals(EnumCumplimiento.PENDIENTE)) ?
                EnumMongoDBColecciones.COLECCION_NO_LOCALIZADOS_RUSP.getValor()
                : EnumMongoDBColecciones.COLECCION_CUMPLIMIENTO.getValor();

        JsonObject command = OmextDAOImpl.crearCommand(tipoColeccion, collName,  pipeline, 1000, false);

        //LOGGER.info("=== El command en contarCumplPorInst es: " + command);

        client.rxRunCommand(EnumMongoDB.AGGREGATE.getValor(), command)
                .timeout(40, TimeUnit.SECONDS )
                .retry(1)
                .doOnSuccess(
                        resp -> {
                            JsonArray conteos = resp
                                    .getJsonObject(EnumMongoDB.CURSOR.getValor())
                                    .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor()).size() > 0 ?
                                    resp
                                            .getJsonObject(EnumMongoDB.CURSOR.getValor())
                                            .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor()) : new JsonArray();

                            resultHandler.handle(Future.succeededFuture(
                                    new JsonObject()
                                            .put(cumplimiento.getValor(), conteos))
                            );
                        }
                )
                .doOnError(
                        e -> {
                            LOGGER.info("=== Hay error en contarCumplPorInst() " + e);
                            LOGGER.info("=== El command era + " + command);
                            resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                        }
                )
        .subscribe();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServidoresDAO contarCumplPorUA(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                               Handler<AsyncResult<JsonObject>> resultHandler) {
        String tipoColeccion = (cumplimiento.equals(EnumCumplimiento.PENDIENTE)) ?
                EnumMongoDBColecciones.COLECCION_NO_LOCALIZADOS_RUSP.getValor()
                : EnumMongoDBColecciones.COLECCION_CUMPLIMIENTO.getValor();

        JsonObject command = OmextDAOImpl.crearCommand(tipoColeccion, collName,  pipeline, 1000, false);
        //LOGGER.info("=== El command en contarCumplPorUA es: " + command);

        client.rxRunCommand(EnumMongoDB.AGGREGATE.getValor(), command)
                .timeout(40, TimeUnit.SECONDS )
                .retry(1)
                .doOnSuccess(
                        resp -> {

                            JsonArray conteos = resp
                                    .getJsonObject(EnumMongoDB.CURSOR.getValor())
                                    .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor()).size() > 0 ?
                                    resp.getJsonObject(EnumMongoDB.CURSOR.getValor())
                                            .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor()) : new JsonArray();

                            //LOGGER.info("=== Los conteos en contarCumplPorUA : " + conteos);

                            resultHandler.handle(Future.succeededFuture(
                                    new JsonObject()
                                            .put(cumplimiento.getValor(), conteos))
                            );
                        }
                )
                .doOnError(
                        e -> {
                            LOGGER.info("=== contarCumplPorUA() " + e);
                            LOGGER.info("=== El command era + " + command);
                            resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                        }
                )
        .subscribe();
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
