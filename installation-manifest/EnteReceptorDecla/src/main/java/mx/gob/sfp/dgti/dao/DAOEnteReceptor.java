/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "EnteReceptorDecla" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.dao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.reactivex.ext.mongo.MongoClient;
import java.util.ArrayList;
import java.util.List;
import mx.gob.sfp.dgti.dao.interfaces.DAOEnteReceptorInterface;

import static mx.gob.sfp.dgti.service.interfaces.ServiceEnteReceptorInerface.DEFAULT_PATH_ENTE_RECEPTOR;
import static mx.gob.sfp.dgti.service.interfaces.ServiceEnteReceptorInerface.PROPERTY_COLL_NAME;
import static mx.gob.sfp.dgti.service.interfaces.ServiceEnteReceptorInerface.PROPERTY_PATH;

/**
 * Clase que contiene la implementacion de los métodos para la interaccion con
 * la base y consulta de informacion de entes receptores de información.
 *
 * @author cgarias
 * @since 06/11/2019
 */
public class DAOEnteReceptor implements DAOEnteReceptorInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(DAOEnteReceptor.class);
    private static final String COLLECTION_ENTES_RECEPTORES = "enteReceptorDeclaracion";
    private final MongoClient mongoClient;
    private static final String POOL_ENTES_RECEPTOR = "pool-entes-receptor";
    private static final String PROPERTY_ACTIVO = "activo";
    private static final String MENSAJE = "mensaje";
    private static final String MENSAJE_INDEX_ERROR = "Error al llamar el path de creación de index";
    private static final String MENSAJE_INDEX = "Error al guardar el ente, verifique los datos enviados";
    private static final String PROPERTY_COLLECTION_NAME = "collName";
    private static final String MENSAJE_EXITO = "Se agrego el ente correctamente";
    private static final String MENSAJE_PATH_EXISTE = "Actualmente ya se encuentra un ente dado de alta con este path favor de asignar otro.";
    private static final String MENSAJE_COLLECTION_EXISTE = "Actualmente ya se encuentra un ente dado de alta con la propiedad collName igual favor de proporcionar otro.";
    private static final String PROPERTY_ENTE_ID = "ente.id";
    private static final String ID_EXITO_EVENT = "1";
    private Vertx vertx;

    public DAOEnteReceptor(Vertx vertx, JsonObject jsonConfig) {
    	this.vertx = vertx;
        mongoClient = MongoClient.createShared(io.vertx.reactivex.core.Vertx.vertx(), jsonConfig, POOL_ENTES_RECEPTOR);
    }
    
    @Override
    public DAOEnteReceptorInterface consultaEnteReceptor(String path, Handler<AsyncResult<JsonObject>> resultHandler) {
        JsonObject query = new JsonObject();
        query.put(PROPERTY_PATH, path);
        query.put(PROPERTY_ACTIVO, true);

        if (query.getString(PROPERTY_PATH) == null) {
            query.put(PROPERTY_PATH, DEFAULT_PATH_ENTE_RECEPTOR);
        }
        this.consultaEnteReceptor(query, new JsonObject().put("_id", 1).put("nombre", 1).put("imagenB64", 1).put("prefijo", 1).put("ente", 1).put(PROPERTY_COLLECTION_NAME, 1).put("parametrosEspecificos", 1), resultaConsulta -> {
            if (resultaConsulta.succeeded()) {
                    resultHandler.handle(Future.succeededFuture(resultaConsulta.result()));
            } else {
                resultHandler.handle(Future.failedFuture(resultaConsulta.cause()));
            }
        });
        return this;
    }

    @Override
    public DAOEnteReceptorInterface consultaEnteReceptorPorCollName(Integer collName,
            Handler<AsyncResult<JsonObject>> resultHandler) {
        JsonObject query = new JsonObject();
        query.put(PROPERTY_COLL_NAME, collName);
        query.put(PROPERTY_ACTIVO, true);

        this.consultaEnteReceptor(query, new JsonObject().put("ente", 1).put("prefijo", 1).put("imagenInstitucionalB64", 1).put("imagenOficialB64", 1), resultaConsulta -> {
            if (resultaConsulta.succeeded()) {
                resultHandler.handle(Future.succeededFuture(resultaConsulta.result()));
            } else {
                resultHandler.handle(Future.failedFuture(resultaConsulta.cause()));
            }
        });
        return this;
    }

    @Override
    public DAOEnteReceptorInterface consultaEnteReceptorporEnteId(String idEnte, Handler<AsyncResult<JsonObject>> resultHandler){
        JsonObject query = new JsonObject();
        query.put(PROPERTY_ENTE_ID, idEnte);
        query.put(PROPERTY_ACTIVO, true);
        this.consultaEnteReceptor(query, new JsonObject().put("ente", 1).put(PROPERTY_COLLECTION_NAME, 1), resultaConsulta -> {
            if (resultaConsulta.succeeded()) {
                resultHandler.handle(Future.succeededFuture(resultaConsulta.result()));
            } else {
                resultHandler.handle(Future.failedFuture(resultaConsulta.cause()));
            }
        });
        return this;
    }
    
    @Override
    public DAOEnteReceptorInterface agregaEnteReceptorDeclaracion(JsonObject enteReceptor,
            Handler<AsyncResult<JsonObject>> resultHandler) {
        this.consultaEnteReceptor(new JsonObject().put(PROPERTY_PATH, enteReceptor.getString(PROPERTY_PATH)), null,
                resultapath -> {
                    if (resultapath.succeeded()) {
                        if (resultapath.result().isEmpty()) {
                            this.consultaEnteReceptor(
                                    new JsonObject().put(PROPERTY_COLLECTION_NAME,
                                            enteReceptor.getInteger(PROPERTY_COLLECTION_NAME)),
                                    null, resultaConsulta -> {
                                        if (resultaConsulta.succeeded()) {
                                            if (resultaConsulta.result().isEmpty()) {
                                            			 enteReceptor.remove("_id");
                                                         mongoClient.save(COLLECTION_ENTES_RECEPTORES, enteReceptor, result -> {
                                                             if (result.succeeded()) {
                                                                 if (!result.result().isEmpty()) {
                                                                     resultHandler.handle(Future.succeededFuture(
                                                                                     new JsonObject().put(MENSAJE, MENSAJE_EXITO).put(
                                                                                             COLLECTION_ENTES_RECEPTORES,
                                                                                             enteReceptor.put("id", result.result()))));
                                                                 } else {
                                                                     resultHandler
                                                                     .handle(Future.succeededFuture(new JsonObject()));
                                                                 }
                                                             } else {
                                                                 resultHandler.handle(Future.failedFuture(result.cause()));
                                                             }
                                                         });
                                            } else {
                                                resultHandler.handle(Future.succeededFuture(
                                                                new JsonObject().put(MENSAJE, MENSAJE_COLLECTION_EXISTE)));
                                            }
                                        } else {
                                            resultHandler.handle(Future.failedFuture(resultapath.cause()));
                                        }
                                    });
                        } else {
                            resultHandler
                            .handle(Future.succeededFuture(new JsonObject().put(MENSAJE, MENSAJE_PATH_EXISTE)));
                        }
                    } else {
                        resultHandler.handle(Future.failedFuture(resultapath.cause()));
                    }
                });

        return this;
    }

    private DAOEnteReceptorInterface consultaEnteReceptor(JsonObject query, JsonObject fields,
            Handler<AsyncResult<JsonObject>> resultHandler) {

        mongoClient.findOne(COLLECTION_ENTES_RECEPTORES, query, fields, result -> {
                    if (result.succeeded()) {
                        if (result.result() != null  && !result.result().isEmpty()) {
                            
                            resultHandler.handle(Future.succeededFuture(result.result()));
                        } else {
                            resultHandler.handle(Future.succeededFuture(new JsonObject()));
                        }
                    } else {
                        resultHandler.handle(Future.failedFuture(result.cause()));
                    }
                });
        return this;
    }

    @Override
    public DAOEnteReceptorInterface consultarTodos(Handler<AsyncResult<List<JsonObject>>> resultHandler) {
        FindOptions fields = new FindOptions().setFields(new JsonObject().put("collName", 1).put("nombre", 1).put("ente", 1));
        mongoClient.findWithOptions(COLLECTION_ENTES_RECEPTORES, new JsonObject(), fields, result -> {
                    if (result.succeeded()) {
                        if (result.result() != null  && !result.result().isEmpty()) {
                            resultHandler.handle(Future.succeededFuture(result.result()));
                        } else {
                            resultHandler.handle(Future.succeededFuture(new ArrayList<>()));
                        }
                    } else {
                        resultHandler.handle(Future.failedFuture(result.cause()));
                    }
                });
        return this;
    }

}
