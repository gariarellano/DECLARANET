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
import mx.gob.sfp.dgti.omextback.dao.OficiosDAO;
import mx.gob.sfp.dgti.omextback.exception.SFPException;
import mx.gob.sfp.dgti.omextback.util.constantes.*;

/**
 * Clase para el servicio ConsultaApiImpl
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public class OficiosDAOImpl implements OficiosDAO  {


    /**
     * Creador de queries
     */
    private ConvertidorRespuestaAS convertidorRespuestaAS;

    /**
     * El logger
     */
    static final Logger LOGGER = LoggerFactory.getLogger(OficiosDAOImpl.class);

    /**
     *
     */
    private MongoClient client;

    /**
     * Constructor
     *
     * @param vertx: el vertx
     */
    public OficiosDAOImpl(Vertx vertx) {
        OmextDAOImpl.iniciarCliente(vertx, client);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public OficiosDAO obtenerDatosUsuarioFirma(Integer collName, Handler<AsyncResult<JsonObject>> resultHandler) {



        client.rxFindOne(EnumMongoDBColecciones.COLECCION_FIRMANTE.getValor() + collName,
                new JsonObject().put(EnumMongoDB.ACTIVO.getValor(), 1), null)
        .subscribe(
                r-> {
                    // Se guarda bien
                    resultHandler.handle(Future.succeededFuture(r));
                },
                e -> {
                    LOGGER.info("=== Ocurrio algo obtenerDatosUsuarioFirma: " + e);
                    resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                },
                () -> {
                    // No se guarda
                    resultHandler.handle(Future.succeededFuture(null));
                }
        );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OficiosDAO obtenerTextoOficioActualPorId(String id, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler) {

        client.rxFindOne(EnumMongoDBColecciones.COLECCION_TEXTOS_OFICIOS.getValor() + collName,
                new JsonObject()
                        .put(EnumMongoDB.ID_TEXTO_OFICIO.getValor(), id),
                null)
                .subscribe(
                        r-> {
                            resultHandler.handle(Future.succeededFuture(r));
                        },
                        e -> {
                            LOGGER.info("=== Ocurrio algo obtenerTextoOficioActualPorId: " + e);
                            resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                        },
                        () -> {
                            resultHandler.handle(Future.succeededFuture(null));
                        }
                );
        return this;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public OficiosDAO obtenerTextoOficioActual(Integer collName, Handler<AsyncResult<JsonObject>> resultHandler) {

        client.rxFindOne(EnumMongoDBColecciones.COLECCION_TEXTOS_OFICIOS.getValor() + collName,
                new JsonObject().put(EnumMongoDB.ACTIVO.getValor(), 1), null)
                .subscribe(
                        r-> {
                            resultHandler.handle(Future.succeededFuture(r));
                        },
                        e -> {
                            LOGGER.info("=== Ocurrio algo obtenerTextoOficioActual: " + e);
                            resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                        },
                        () -> {
                            resultHandler.handle(Future.succeededFuture(null));
                        }
                );
        return this;
    }

    //Actualizar count vista

    /**
     * {@inheritDoc}
     */
    @Override
    public OficiosDAO consumirActualizarCountVista(Integer collName, Integer posiciones, EnumTipoDeclaracion tipoDeclaracion,
                                                      Integer anio, EnumMes mes, Handler<AsyncResult<Integer>> resultHandler) {
        JsonObject consulta = new JsonObject()
                .put(EnumMongoDB.TIPO_DECLARACION.getValor(), tipoDeclaracion.getValor())
                .put(EnumMongoDB.ANIO.getValor(), anio);
        if(mes != null ) {
            consulta.put(EnumMongoDB.MES.getValor(), mes);
        }
        client.rxFindOne(EnumMongoDBColecciones.COLECCION_CONTEO_OFICIOS_VISTAS.getValor() + collName, consulta, null)
                .subscribe(
                        // Se encontro el conteo
                        r-> {
                            Integer numero = r.getInteger(EnumMongoDB.NUMERO.getValor()) + 1;
                            JsonObject update = new JsonObject().put(EnumMongoDB.SET.getValor(), new JsonObject()
                                    .put(EnumMongoDB.NUMERO.getValor(), numero + posiciones));
                            JsonObject consultaPorId = new JsonObject().put(EnumMongoDB.ID.getValor(), r.getString(EnumMongoDB.ID.getValor()));

                            client.rxFindOneAndUpdate(EnumMongoDBColecciones.COLECCION_CONTEO_OFICIOS_VISTAS.getValor() + collName,
                                    consultaPorId, update)
                                    .subscribe(
                                            r2 -> {
                                                resultHandler.handle(Future.succeededFuture(numero));
                                            },
                                            e -> {
                                                LOGGER.info("=== Ocurrio algo consumirActualizarCountVista: " + e);
                                                resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                                            },
                                            // No se encontro el conteo
                                            () -> {
                                                resultHandler.handle(Future.succeededFuture(numero));
                                            }

                                    );
                        },
                        e -> {
                            LOGGER.info("=== Ocurrio algo consumirActualizarCountVista: " + e);
                            resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                        },
                        // No se encontro el conteo
                        () -> {

                            String nuevoId = tipoDeclaracion.getValor() + anio.toString();
                            if (mes != null) {
                                nuevoId = nuevoId + mes.getValor();
                            }
                            JsonObject nuevo = consulta.put(EnumMongoDB.NUMERO.getValor(), 1)
                                    .put(EnumMongoDB.ID.getValor(), nuevoId);
                            client.rxSave(EnumMongoDBColecciones.COLECCION_CONTEO_OFICIOS_VISTAS.getValor()+collName, nuevo)
                                    .doOnSuccess(r -> resultHandler.handle(Future.succeededFuture(1)))
                                    .doOnError(e -> resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)))
                                    ).subscribe(
                                        r -> resultHandler.handle(Future.succeededFuture(1)),
                                        e -> resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e))),
                                        () -> resultHandler.handle(Future.succeededFuture(1))
                            );
                        });
        return this;
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
    private JsonObject crearCommand(String tipoColeccion, Integer collName, JsonArray pipeline, Integer batchSize,
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

    /**
     * @inheritDoc
     */
    @Override
    public void close() {
        LOGGER.info("=== Close un cliente MongoDB");
        client.close();
    }

}
