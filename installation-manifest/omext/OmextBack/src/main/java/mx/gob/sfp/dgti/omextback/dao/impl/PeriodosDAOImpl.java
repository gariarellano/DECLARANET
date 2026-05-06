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
import mx.gob.sfp.dgti.omextback.as.CreadorQueryUtils;
import mx.gob.sfp.dgti.omextback.dao.PeriodosDAO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.RespuestaPeriodosDTO;
import mx.gob.sfp.dgti.omextback.exception.SFPException;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumError;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumGraphQL;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumMongoDB;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumMongoDBColecciones;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * Clase para el servicio ConsultaApiImpl
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public class PeriodosDAOImpl implements PeriodosDAO {


    /**
     *
     */
    private MongoClient client;

    /**
     * El logger
     */
    static final Logger LOGGER = LoggerFactory.getLogger(PeriodosDAOImpl.class);

    /**
     * Constructor
     *
     * @param vertx: el vertx
     */
    public PeriodosDAOImpl(Vertx vertx) {
       OmextDAOImpl.iniciarCliente(vertx, client);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PeriodosDAOImpl consultarPeriodos(JsonArray pipeline, Integer collName, Integer offset, Integer tamanio,
                                           Handler<AsyncResult<JsonObject>> resultHandler) {

        JsonObject command = OmextDAOImpl.crearCommand(EnumMongoDBColecciones.COLECCION_PERIODOS.getValor(), collName,  pipeline, 1000, false);
        //LOGGER.info("=== El command en consultarPeriodos es: " + command);
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

                    JsonArray resJson = respuesta.getJsonArray(EnumMongoDB.RESULTADOS.getValor());
                    JsonArray  vencFechasLim = respuesta.getJsonArray(EnumMongoDB.VENCE_FECHA_LIMITE.getValor());

                    Integer total = (respuesta.getJsonArray(EnumMongoDB.TOTAL.getValor()) !=null
                            && respuesta.getJsonArray(EnumMongoDB.TOTAL.getValor()).size() > 0)?
                            respuesta.getJsonArray(EnumMongoDB.TOTAL.getValor())
                                    .getJsonObject(0).getInteger(EnumMongoDB.TOTAL.getValor())
                            : 0;

                    //Instituciones que NO cumplen con la fecha
                    HashSet<String> instVencFechas = new HashSet<String>();
                    vencFechasLim.forEach(inst -> {
                            JsonObject i = (JsonObject) inst;
                            instVencFechas.add(i.getString(EnumMongoDB.ID_PERIODO.getValor()));
                        }
                    );

                    resJson.forEach(per -> {
                        JsonObject p = (JsonObject) per;
                        if(instVencFechas.contains(p.getString(EnumMongoDB.ID_PERIODO.getValor()))) {
                            p.put(EnumMongoDB.VENCE_FECHA_LIMITE.getValor(), false);

                        } else {
                            p.put(EnumMongoDB.VENCE_FECHA_LIMITE.getValor(), true);
                        }
                    });

                    JsonObject objeto = new JsonObject()
                            .put(EnumGraphQL.PAGINACION.getValor(),  new JsonObject()
                                    .put(EnumGraphQL.OFFSET.getValor(), offset)
                                    .put(EnumGraphQL.TAMANIO.getValor(), tamanio)
                                    .put(EnumGraphQL.TOTAL.getValor(), total))
                            .put(EnumGraphQL.PERIODOS.getValor(), resJson);

                    LOGGER.info("=== Respuesta completa en consultarPeriodos: " + objeto);

                    RespuestaPeriodosDTO periodosDTO = new RespuestaPeriodosDTO(objeto);

                resultHandler.handle(Future.succeededFuture(objeto)); }
            ).doOnError(
                    e ->
                    {
                        LOGGER.info("=== Ocurrio un error en consultarPeriodos" + e);
                        LOGGER.info("=== El command era + " + command);
                        resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                    }

            ).subscribe();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PeriodosDAOImpl consultarPeriodoSimple(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler) {

        JsonObject command = OmextDAOImpl.crearCommand(EnumMongoDBColecciones.COLECCION_PERIODOS.getValor(), collName,  pipeline, 1000, false);
        LOGGER.info("=== El command en consultarPeriodoSimple es: " + command);

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

                    JsonObject periodo = (respuesta.size() > 0 ) ? respuesta.getJsonObject(0) : null;

                    LOGGER.info("=== Objeto consultarPeriodoSimple: " + periodo);

                    resultHandler.handle(Future.succeededFuture(periodo));
                })
                .doOnError(e ->{
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
    public PeriodosDAOImpl guardarPeriodo(JsonObject periodo, Integer collName, Handler<AsyncResult<String>> resultHandler) {
        LOGGER.info("=== Se guardarPeriodo en " + EnumMongoDBColecciones.COLECCION_PERIODOS.getValor()+ collName + " ->"
                + periodo);

        client.rxInsert(EnumMongoDBColecciones.COLECCION_PERIODOS.getValor()+ collName, periodo)
            .subscribe(
                r-> {
                    // Se guarda y si existe
                    resultHandler.handle(Future.succeededFuture(periodo.getString(EnumMongoDB.ID.getValor())));
                },
                    e -> {
                        LOGGER.info("=== Ocurrio algo: " + e);
                        LOGGER.info("=== Se guardaba: " + periodo);
                        resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                    },
                    () -> {
                        //No guarda, sin error
                        resultHandler.handle(Future.succeededFuture(periodo.getString("")));
                    }
            );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PeriodosDAOImpl actualizarFechaPeriodo(Integer collName, String idPeriodo, String fecha,
                                                Handler<AsyncResult<Boolean>> resultHandler) {
        JsonObject fechaObject = CreadorQueryUtils.crearFechaMongo(fecha);
        if (fechaObject == null) {
            resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_PARAMETROS_INCORRECTOS)));
        } else {

            client.rxFindOneAndUpdate(EnumMongoDBColecciones.COLECCION_PERIODOS.getValor() + collName,
                    new JsonObject()
                            .put(EnumMongoDB.ID_PERIODO.getValor(), idPeriodo),
                    new JsonObject().put(EnumMongoDB.SET.getValor(), new JsonObject()
                            .put(EnumMongoDB.FECHA_LIMITE.getValor(), fechaObject))
            ).subscribe(
                    r -> {
                        resultHandler.handle(Future.succeededFuture(Boolean.TRUE));
                    },
                    e -> {
                        LOGGER.info("=== Ocurrio algo actualizarFechaPeriodo: " + e);
                        resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                    },
                    () -> {
                        resultHandler.handle(Future.succeededFuture(Boolean.FALSE));
                    }
            );
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PeriodosDAOImpl actualizarFechaExtensionesPeriodo(Integer collName, String idPeriodo, JsonArray extensiones,
                                                           Handler<AsyncResult<Boolean>> resultHandler) {
            LOGGER.info("=== Las extensiones que se insertarn : " + extensiones);
            client.rxFindOneAndUpdate(EnumMongoDBColecciones.COLECCION_PERIODOS.getValor() + collName,
                    new JsonObject()
                            .put(EnumMongoDB.ID_PERIODO.getValor(), idPeriodo),
                    new JsonObject().put(EnumMongoDB.SET.getValor(), new JsonObject()
                            .put(EnumMongoDB.EXTENSIONES.getValor(), extensiones))
            ).subscribe(
                    r -> {
                        resultHandler.handle(Future.succeededFuture(Boolean.TRUE));
                    },
                    e -> {
                        LOGGER.info("=== Ocurrio algo actualizarFechaExtensionesPeriodo: " + e);
                        resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                    },
                    () -> {
                        resultHandler.handle(Future.succeededFuture(Boolean.FALSE));
                    }
            );

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
