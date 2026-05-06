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
import mx.gob.sfp.dgti.omextback.dao.OmextMongoDAO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.RespuestaPeriodosDTO;
import mx.gob.sfp.dgti.omextback.exception.SFPException;
import mx.gob.sfp.dgti.omextback.util.constantes.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Clase para el servicio ConsultaApiImpl
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public class OmextMongoDAOImpl implements OmextMongoDAO {

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
    static final Logger LOGGER = LoggerFactory.getLogger(OmextMongoDAOImpl.class);

    /**
     * Constructor
     *
     * @param vertx: el vertx
     */
    public OmextMongoDAOImpl(Vertx vertx) {

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

        convertidorRespuestaAS = new ConvertidorRespuestaASImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OmextMongoDAO buscarCumplServ(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                          Handler<AsyncResult<JsonObject>> resultHandler) {
        String tipoColeccion = (cumplimiento.equals(EnumCumplimiento.PENDIENTE)) ?
                EnumMongoDBColecciones.COLECCION_NO_LOCALIZADOS_RUSP.getValor()
                : EnumMongoDBColecciones.COLECCION_CUMPLIMIENTO.getValor();

        JsonObject command = crearCommand(tipoColeccion, collName,  pipeline, 200, true);

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
    public OmextMongoDAO contarCumpl(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                     Handler<AsyncResult<JsonObject>> resultHandler) {
        String tipoColeccion = (cumplimiento.equals(EnumCumplimiento.PENDIENTE)) ?
                EnumMongoDBColecciones.COLECCION_NO_LOCALIZADOS_RUSP.getValor()
                : EnumMongoDBColecciones.COLECCION_CUMPLIMIENTO.getValor();

        JsonObject command = crearCommand(tipoColeccion, collName,  pipeline, 1000, false);

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
    public OmextMongoDAO consultarCumplServ(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                            Integer tamanio, Handler<AsyncResult<JsonObject>> resultHandler) {
        String tipoColeccion = (cumplimiento.equals(EnumCumplimiento.PENDIENTE)) ?
                EnumMongoDBColecciones.COLECCION_NO_LOCALIZADOS_RUSP.getValor()
                : EnumMongoDBColecciones.COLECCION_CUMPLIMIENTO.getValor();

        Integer maxSize = (tamanio.intValue() > 5000) ? 5000: tamanio;

        JsonObject command = crearCommand(tipoColeccion, collName,  pipeline, maxSize, true);
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

    @Override
    public OmextMongoDAO obtenerPreconteosInicio(Integer collName, Integer anio, Handler<AsyncResult<List<JsonObject>>> resultHandler)  {
        client.rxFind(EnumMongoDBColecciones.COLECCION_PRECONTEOS_INICIO.getValor() + collName,
                    new JsonObject()
                        .put(EnumMongoDB.ANIO.getValor(), anio)
                        .put(EnumMongoDB.TIPO_OBLIGACION.getValor(), EnumTipoObligacion.ACTIVO_MAYO.getValor())
                )
                .doOnSuccess(preconteos -> {
                    resultHandler.handle(Future.succeededFuture(preconteos));
                })
                .doOnError(
                        e -> {
                            LOGGER.info("=== Hay error en obtenerPreconteosInicio() " + e);
                            resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                        }
                )
                .subscribe();
        ;
        return this;
    }

    @Override
    public OmextMongoDAO obtenerPreconteosInicioGrupo(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonArray>> resultHandler)  {

        JsonObject command = crearCommand(
            EnumMongoDBColecciones.COLECCION_PRECONTEOS_INICIO.getValor(), collName,  pipeline, 1000, false);

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
                            LOGGER.info("=== Hay error en obtenerPreconteosInicioGrupo() " + e);
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
    public OmextMongoDAO contarCumpExtPorInst(JsonArray pipeline, Integer collName,
                                            Handler<AsyncResult<JsonArray>> resultHandler) {

        JsonObject command = crearCommand(
                EnumMongoDBColecciones.COLECCION_CUMPLIMIENTO.getValor(), collName,  pipeline, 1000, false);

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
    public OmextMongoDAO contarCumplPorInst(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                            Handler<AsyncResult<JsonObject>> resultHandler) {
        String tipoColeccion = (cumplimiento.equals(EnumCumplimiento.PENDIENTE)) ?
                EnumMongoDBColecciones.COLECCION_NO_LOCALIZADOS_RUSP.getValor()
                : EnumMongoDBColecciones.COLECCION_CUMPLIMIENTO.getValor();

        JsonObject command = crearCommand(tipoColeccion, collName,  pipeline, 1000, false);

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
    public OmextMongoDAO contarCumplPorUA(JsonArray pipeline, Integer collName, EnumCumplimiento cumplimiento,
                                               Handler<AsyncResult<JsonObject>> resultHandler) {
        String tipoColeccion = (cumplimiento.equals(EnumCumplimiento.PENDIENTE)) ?
                EnumMongoDBColecciones.COLECCION_NO_LOCALIZADOS_RUSP.getValor()
                : EnumMongoDBColecciones.COLECCION_CUMPLIMIENTO.getValor();

        JsonObject command = crearCommand(tipoColeccion, collName,  pipeline, 1000, false);
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
     * {@inheritDoc}
     */
    @Override
    public OmextMongoDAO consultarPeriodos(JsonArray pipeline, Integer collName, Integer offset, Integer tamanio,
                                           Handler<AsyncResult<JsonObject>> resultHandler) {

        JsonObject command = crearCommand(EnumMongoDBColecciones.COLECCION_PERIODOS.getValor(), collName,  pipeline, 1000, false);
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
    public OmextMongoDAO consultarPeriodoSimple(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler) {

        JsonObject command = crearCommand(EnumMongoDBColecciones.COLECCION_PERIODOS.getValor(), collName,  pipeline, 1000, false);
        //LOGGER.info("=== El command en consultarPeriodoSimple es: " + command);

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
    public OmextMongoDAO consultarInstitucionesPorPeriodo(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler) {

            JsonObject command = crearCommand(EnumMongoDBColecciones.COLECCION_VISTAS.getValor(), collName,  pipeline, 1000, false);
            //LOGGER.info("=== El command en consultarInstitucionesPorPeriodo es: " + command);

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
    public OmextMongoDAO consultarInstitucionesVistaPorPeriodo(String idPeriodo, Integer collName, Handler<AsyncResult<List<String>>> resultHandler) {
        client.rxFind(EnumMongoDBColecciones.COLECCION_VISTAS.getValor().concat(collName.toString()),
                new JsonObject().put(EnumMongoDB.ID_PERIODO.getValor(), idPeriodo))
                .map(r -> {
                    List<String> entes = new ArrayList<>();
                    r.forEach(v -> entes.add(v.getString(EnumMongoDB.ID_ENTE.getValor())));
                    return entes;
                })
                .doOnSuccess(en -> resultHandler.handle(Future.succeededFuture(en)))
                .doOnError(e ->
                    {
                        LOGGER.info("=== Ocurrio algo: " + e);
                        resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                    }
                ).subscribe();

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OmextMongoDAO consultarVistas(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler) {

        JsonObject command = crearCommand(EnumMongoDBColecciones.COLECCION_VISTAS.getValor(), collName,  pipeline, 1000, false);
        //LOGGER.info("=== El command en consultarVistas es: " + command);

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
    public OmextMongoDAO consultarServidoresVista(JsonArray pipeline, Integer offset, Integer tamanio, Integer collName,
                                                  Handler<AsyncResult<JsonObject>> resultHandler) {

        JsonObject command = crearCommand(EnumMongoDBColecciones.COLECCION_SERVIDORES_VISTA.getValor(), collName,  pipeline, 1000, false);
        //LOGGER.info("=== El command en consultarServidoresVista es: " + command);
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
    public OmextMongoDAO guardarPeriodo(JsonObject periodo, Integer collName, Handler<AsyncResult<String>> resultHandler) {
        LOGGER.info("=== Se guardarPeriodo en " + EnumMongoDBColecciones.COLECCION_PERIODOS.getValor()+ collName + " ->"
                + periodo);

        client.rxInsert(EnumMongoDBColecciones.COLECCION_PERIODOS.getValor()+ collName, periodo)
            .subscribe(
                r-> {
                    // Se guarda y si existe
                    LOGGER.info("=== Guardar periodo : primer caso : " + r);
                    resultHandler.handle(Future.succeededFuture(periodo.getString(EnumMongoDB.ID.getValor())));
                },
                    e -> {
                        LOGGER.info("=== Ocurrio algo: " + e);
                        LOGGER.info("=== Se guardaba: " + periodo);
                        resultHandler.handle(Future.failedFuture(new SFPException(EnumError.ERROR_CONSULTA_MONGODB, e)));
                    },
                    () -> {
                        //Si guarda, no existia
                        resultHandler.handle(Future.succeededFuture(periodo.getString(EnumMongoDB.ID.getValor())));
                    }
            );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OmextMongoDAO guardarEstatusVistasEnPeriodo(Integer collName, String idPeriodo, EnumEstatusPeriodo estatus,
                                                       Handler<AsyncResult<Boolean>> resultHandler) {
        client.rxFindOneAndUpdate(EnumMongoDBColecciones.COLECCION_PERIODOS.getValor() + collName,
                new JsonObject()
                    .put(EnumMongoDB.ID_PERIODO.getValor(), idPeriodo),
                new JsonObject().put(EnumMongoDB.SET.getValor(), new JsonObject()
                .put(EnumMongoDB.VISTAS_GENERADAS.getValor(), estatus.getValor()))
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
    public OmextMongoDAO guardarFirmaVista(Integer collName, String tipoFirmado, String folio, JsonObject datosFirma,
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
    public OmextMongoDAO guardarEstatusVista(Integer collName, String folio, EnumEstatusVista estatus,
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
    public OmextMongoDAO guardarVista(JsonObject vista, Integer collName, Handler<AsyncResult<String>> resultHandler) {

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
    public OmextMongoDAO obtenerDatosUsuarioFirma(Integer collName, Handler<AsyncResult<JsonObject>> resultHandler) {

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
    public OmextMongoDAO obtenerTextoOficioActualPorId(String id, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler) {

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
    public OmextMongoDAO obtenerVistaPorFolio(Integer collName, JsonArray pipeline, Handler<AsyncResult<JsonObject>> resultHandler) {

        JsonObject command = crearCommand(EnumMongoDBColecciones.COLECCION_VISTAS.getValor(), collName,  pipeline, 1, false);
        //LOGGER.info("=== El command en obtenerVistaPorFolio es: " + command);

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
    public OmextMongoDAO consultarServidoresVistaImpresion(JsonArray pipeline, Integer collName, Integer maxSize, Handler<AsyncResult<JsonArray>> resultHandler) {

        JsonObject command = crearCommand(EnumMongoDBColecciones.COLECCION_SERVIDORES_VISTA.getValor(), collName,  pipeline, maxSize, true);
        //LOGGER.info("=== El command en consultarServidoresVistaImpresion es: " + command);
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
     * {@inheritDoc}
     */
    @Override
    public OmextMongoDAO actualizarFechaPeriodo(Integer collName, String idPeriodo, String fecha,
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
    public OmextMongoDAO actualizarFechaExtensionesPeriodo(Integer collName, String idPeriodo, JsonArray extensiones,
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
     * {@inheritDoc}
     */
    @Override
    public OmextMongoDAO obtenerTextoOficioActual(Integer collName, Handler<AsyncResult<JsonObject>> resultHandler) {

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
    public OmextMongoDAO consumirActualizarCountVista(Integer collName, Integer posiciones, EnumTipoDeclaracion tipoDeclaracion,
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
                            Integer numero = r.getInteger(EnumMongoDB.NUMERO.getValor()) ;
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
     * {@inheritDoc}
     */
    @Override
    public OmextMongoDAO consultarVistasPendientesUsuario(JsonArray pipeline, Integer collName, Handler<AsyncResult<JsonObject>> resultHandler) { LOGGER.info("MONGO DB CONSULTAR PERIODO.");
        JsonObject command = crearCommand(EnumMongoDBColecciones.COLECCION_VISTAS.getValor(), collName,  pipeline, 1000, false);
        //LOGGER.info("=== El command en consultarVistasPendientesUsuario: " + command);
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

    public OmextMongoDAO todasVistasDelPeriodoGeneradas(String idPeriodo, Integer collName,
                                                      Handler<AsyncResult<Boolean>> resultHandler) {
        JsonObject query = new JsonObject()
                .put(EnumMongoDB.ID_PERIODO.getValor(), idPeriodo)
                .put(EnumMongoDB.VISTA_GENERADA.getValor(), new JsonObject()
                    .put(EnumMongoDB.NOQ.getValor(), EnumEstatusVista.GENERADA.getValor()));

        client.rxFind(EnumMongoDBColecciones.COLECCION_VISTAS.getValor() + collName, query)
                .doOnSuccess( r -> {

                            LOGGER.info("Todas las vistas del periodo generadas: " + r);
                            if(r.isEmpty()) {
                                resultHandler.handle(Future.succeededFuture(Boolean.TRUE));
                            } else {
                                resultHandler.handle(Future.succeededFuture(Boolean.FALSE));
                            }
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
