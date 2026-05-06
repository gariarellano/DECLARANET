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
import mx.gob.sfp.dgti.omextback.as.CreadorQueryUtils;
import mx.gob.sfp.dgti.omextback.as.impl.ConvertidorRespuestaASImpl;
import mx.gob.sfp.dgti.omextback.dao.VistasDAO;
import mx.gob.sfp.dgti.omextback.exception.SFPException;
import mx.gob.sfp.dgti.omextback.util.constantes.*;

import java.util.concurrent.TimeUnit;

/**
 * Clase para el servicio ConsultaApiImpl
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public class VistasDAOImpl implements VistasDAO {

    /**
     * Creador de queries
     */
    private ConvertidorRespuestaAS convertidorRespuestaAS;

    /**
     *
     */
    private MongoClient client;


    /**
     * El logger
     */
    static final Logger LOGGER = LoggerFactory.getLogger(VistasDAOImpl.class);

    /**
     * Constructor
     *
     * @param vertx: el vertx
     */
    public VistasDAOImpl(Vertx vertx) {

        LOGGER.info("=== OmextMongoDAOImpl()");
        LOGGER.info("=== Se levanta el servicio de OmextMongoDAOImpl()");

        OmextDAOImpl.iniciarCliente(vertx, client);


        convertidorRespuestaAS = new ConvertidorRespuestaASImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VistasDAO consultarInstitucionesPorPeriodo(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler) {

            JsonObject command = OmextDAOImpl.crearCommand(EnumMongoDBColecciones.COLECCION_VISTAS.getValor(), collName,  pipeline, 1000, false);
            LOGGER.info("=== El command en consultarInstitucionesPorPeriodo es: " + command);

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

                                JsonObject objeto = new JsonObject()
                                        .put(EnumGraphQL.TOTAL.getValor(), respuesta.size())
                                        .put(EnumGraphQL.PERIODOS.getValor(), respuesta);

                                LOGGER.info("=== Respuesta consultarInstitucionesPorPeriodo" + objeto);
                                resultHandler.handle(Future.succeededFuture(
                                        objeto
                                ));
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
    public VistasDAO consultarVistas(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler) {

        JsonObject command = OmextDAOImpl.crearCommand(EnumMongoDBColecciones.COLECCION_VISTAS.getValor(), collName,  pipeline, 1000, false);
        LOGGER.info("=== El command en consultarVistas es: " + command);

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

                        JsonObject objeto = new JsonObject()
                                .put(EnumGraphQL.TOTAL.getValor(), respuesta.size())
                                .put(EnumGraphQL.VISTAS.getValor(), respuesta);

                        LOGGER.info("=== Respuesta en consultarVistas" + objeto);

                        resultHandler.handle(Future.succeededFuture(
                                objeto
                        ));
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
    public VistasDAO guardarEstatusVistasEnPeriodo(Integer collName, String idPeriodo, EnumEstatusVista estatus,
                                                       Handler<AsyncResult<Boolean>> resultHandler) {
        client.rxFindOneAndUpdate(EnumMongoDBColecciones.COLECCION_VISTAS.getValor() + collName,
                new JsonObject()
                    .put(EnumMongoDB.ID_PERIODO.getValor(), idPeriodo),
                new JsonObject().put(EnumMongoDB.SET.getValor(), new JsonObject()
                .put(EnumMongoDB.VISTAS_OMISOS_GENERADAS.getValor(), estatus.getValor()))
        ) .subscribe(
                r-> {
                    // Se guarda y si existe
                    resultHandler.handle(Future.succeededFuture(Boolean.TRUE));
                },
                e -> {
                    LOGGER.info("=== Ocurrio algo: " + e);
                    LOGGER.info("=== Se guardaba status: " + estatus.getValor() + " en idPeriodo: " + idPeriodo);
                    resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                },
                () -> {
                    //No guarda, sin error
                    resultHandler.handle(Future.succeededFuture(Boolean.FALSE));
                }
        );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VistasDAO guardarFirmaVista(Integer collName, String tipoFirmado, String folio, JsonObject datosFirma,
                                           Handler<AsyncResult<Boolean>> resultHandler) {
        client.rxFindOneAndUpdate(EnumMongoDBColecciones.COLECCION_VISTAS.getValor() + collName,
                new JsonObject()
                        .put(EnumMongoDB.FOLIO.getValor(), folio),
                new JsonObject().put(EnumMongoDB.SET.getValor(), new JsonObject()
                        .put(tipoFirmado, datosFirma))
        ) .subscribe(
                r-> {
                    // Se guarda y si existe
                    resultHandler.handle(Future.succeededFuture(Boolean.TRUE));
                },
                e -> {
                    LOGGER.info("=== Ocurrio algo: " + e);
                    LOGGER.info("=== Se guardaba en folio: " + folio + " datosFirma : " + datosFirma);
                    resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                },
                () -> {
                    //No guarda, sin error
                    resultHandler.handle(Future.succeededFuture(Boolean.FALSE));
                }
        );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VistasDAO guardarEstatusVista(Integer collName, String folio, EnumEstatusVista estatus,
                                                       Handler<AsyncResult<Boolean>> resultHandler) {
        client.rxFindOneAndUpdate(EnumMongoDBColecciones.COLECCION_VISTAS.getValor() + collName,
                new JsonObject()
                        .put(EnumMongoDB.FOLIO.getValor(), folio),
                new JsonObject().put(EnumMongoDB.SET.getValor(), new JsonObject()
                        .put(EnumMongoDB.VISTA_GENERADA.getValor(), estatus.getValor()))
        ) .subscribe(
                r-> {
                    // Se guardo bien
                    resultHandler.handle(Future.succeededFuture(Boolean.TRUE));
                },
                e -> {
                    LOGGER.info("=== Ocurrio algo: " + e);
                    LOGGER.info("=== Se guardaba el estatus: " + estatus.getValor() + " en folio: " + folio);
                    resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                },
                () -> {
                    // No se guardo
                    resultHandler.handle(Future.succeededFuture(Boolean.TRUE));
                }
        );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VistasDAO guardarVista(JsonObject vista, Integer collName, Handler<AsyncResult<String>> resultHandler) {

        vista.put(EnumMongoDB.ID.getValor(), vista.getString(EnumMongoDB.FOLIO.getValor()));
        vista.put(EnumMongoDB.FECHA_LIMITE.getValor(),
                CreadorQueryUtils.crearFechaMongo(vista.getString(EnumMongoDB.FECHA_LIMITE.getValor())));
        vista.put(EnumMongoDB.FECHA_REGISTRO.getValor(), CreadorQueryUtils.crearFechaMongo(null));
        LOGGER.info("=== Se guardarVista: " + vista);
        client.rxSave(EnumMongoDBColecciones.COLECCION_VISTAS.getValor() + collName, vista)
                .doOnSuccess(
                r-> {
                    if(!r.isEmpty()) {
                        LOGGER.info("=== Respuesta : " + r);
                        resultHandler.handle(Future.succeededFuture(r));
                    } else {
                        resultHandler.handle(Future.succeededFuture(""));
                    }
                }
            ).doOnError(
                    e ->
                            resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)))
            )
            .doOnComplete(() -> {
                resultHandler.handle(Future.succeededFuture(vista.getString(EnumMongoDB.FOLIO.getValor())));

            })
            .subscribe();
        return this;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public VistasDAO obtenerVistaPorFolio(Integer collName, JsonArray pipeline, Handler<AsyncResult<JsonObject>> resultHandler) {

        JsonObject command = OmextDAOImpl.crearCommand(EnumMongoDBColecciones.COLECCION_VISTAS.getValor(), collName,  pipeline, 1, false);
        LOGGER.info("=== El command en obtenerVistaPorFolio es: " + command);

        client.rxRunCommand(EnumMongoDB.AGGREGATE.getValor(), command)
                .timeout(40, TimeUnit.SECONDS )
                .retry(1)
                .doOnSuccess( r -> {
                    JsonObject respuesta = r
                            .getJsonObject(EnumMongoDB.CURSOR.getValor())
                            .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor()).size() > 0 ?
                            r.getJsonObject(EnumMongoDB.CURSOR.getValor())
                                    .getJsonArray(EnumMongoDB.FIRST_BATCH.getValor()).getJsonObject(0) :
                            null;

                    LOGGER.info("=== Respuesta de obtenerVistaPorFolio " + respuesta);
                    resultHandler.handle(Future.succeededFuture(respuesta)); }
                ).doOnError(
                e ->
                {
                    LOGGER.info("=== Ocurrio algo obtenerVistaPorFolio: " + e);
                    LOGGER.info("=== El command era: " + command);
                    resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                }

        ).subscribe();

        return this;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public VistasDAO consultarVistasPendientesUsuario(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler) { LOGGER.info("MONGO DB CONSULTAR PERIODO.");
        JsonObject command = OmextDAOImpl.crearCommand(EnumMongoDBColecciones.COLECCION_VISTAS.getValor(), collName,  pipeline, 1000, false);
        LOGGER.info("=== El command en consultarVistasPendientesUsuario: " + command);
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

                        JsonObject objeto = new JsonObject()
                                .put(EnumGraphQL.TOTAL.getValor(), respuesta.size())
                                .put(EnumGraphQL.PERIODOS.getValor(), respuesta);

                        LOGGER.info("=== Respuesta en consultarVistasPendientesUsuario: " + objeto);

                        resultHandler.handle(Future.succeededFuture(
                                objeto
                        ));
                    }
            ).doOnError(
                e -> {
                    LOGGER.info("Ocurrio un error en consultarVistasPendientesUsuario : " + e);
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
