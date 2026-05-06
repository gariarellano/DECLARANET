/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti;


import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.util.EnumCampos;

/**
 * Clase que contiene la implementacion de los métodos para
 * interactuar con base de datos mongo y realizar cuardados 
 * @author cgarias
 */
public class DAOPermisos extends DAOGeneric implements DAOPermisosInterface{
    private static final Logger logger = Logger.getLogger(DAOPermisos.class.getName());
    private static final String FIELDS = "fields";
    private static final String COLLECTION_PERFIL = "perfil";
    private static final String COLLECTION_ASIGNACION = "asignacion";
    
    public DAOPermisos(Vertx vertx) {
        super(vertx);
    }

    @Override
    public DAOPermisosInterface buscaPerfil(JsonObject query, Handler<AsyncResult<List<JsonObject>>> objetos) {
        FindOptions options = new FindOptions();
        if (query.containsKey(FIELDS)){
            options.setFields(query.getJsonObject(FIELDS));
            query.remove(FIELDS);
        }
        mongoClient.findWithOptions(COLLECTION_PERFIL, query, options, objetos);
        return this;
    }
    
    @Override
    public DAOPermisosInterface guardaPerfil(JsonObject perfil, Handler<AsyncResult<String>> id) {
        mongoClient.save(COLLECTION_PERFIL, perfil, id);
        return this;
    }

    @Override
    public DAOPermisosInterface eliminaPerfil(JsonObject perfil, Handler<AsyncResult<String>> id) {
        String _id = perfil.getString("_id");
        mongoClient.removeDocument(COLLECTION_PERFIL, perfil, response->{
            if (response.succeeded() && response.result().getRemovedCount()==1){
                id.handle(Future.succeededFuture(_id));
            }else{
                if (response.failed()){
                    logger.log(Level.SEVERE, "Error de eliminacion en base de datos", response.cause());
                }else{
                    id.handle(Future.succeededFuture("No localizado"));
                }
            }
        });
        return this;
    }

    @Override
    public DAOPermisosInterface buscaAsignacion(JsonObject query, Handler<AsyncResult<List<JsonObject>>> objetos) {
        FindOptions options = new FindOptions();
        if (query.containsKey(FIELDS)){
            options.setFields(query.getJsonObject(FIELDS));
            query.remove(FIELDS);
        }
        if (query.containsKey(EnumCampos.ID_USUARIO.getValor())){
            JsonObject queryIdUsr = query.copy();
            queryIdUsr.remove(EnumCampos.CURP.getValor());
            mongoClient.findWithOptions(COLLECTION_ASIGNACION, queryIdUsr, options, datosIdUsr->{
                if (datosIdUsr.succeeded()){
                    if (datosIdUsr.result() != null && !datosIdUsr.result().isEmpty()){
                        objetos.handle(datosIdUsr);
                    }else{
                        JsonObject queryCurp = query.copy();
                        queryCurp.remove(EnumCampos.ID_USUARIO.getValor());
                        mongoClient.findWithOptions(COLLECTION_ASIGNACION, queryCurp, options, datosCurp->{
                            if (datosCurp.result()!= null && !datosCurp.result().isEmpty()) {
                                logger.info(String.format("Se cambiara curp %s por id %d",
                                        queryCurp.getString(EnumCampos.CURP.getValor()),
                                        queryIdUsr.getInteger(EnumCampos.ID_USUARIO.getValor()))
                                );
                                JsonObject objBd = datosCurp.result().get(0);
                                objBd.put(EnumCampos.ID_USUARIO.getValor(), queryIdUsr.getInteger(EnumCampos.ID_USUARIO.getValor()));
                                objBd.remove(EnumCampos.CURP.getValor());
                                mongoClient.save(COLLECTION_ASIGNACION, objBd ,actualizado->{});
                            }
                            objetos.handle(datosCurp);
                        });
                    }
                }else{
                    objetos.handle(datosIdUsr);
                }
            });
        }else{
            mongoClient.findWithOptions(COLLECTION_ASIGNACION, query, options, objetos);
        }
        return this;
    }
    
    @Override
    public DAOPermisosInterface guardaAsignaciones(JsonObject asignaciones, Handler<AsyncResult<String>> id) {
        mongoClient.save(COLLECTION_ASIGNACION, asignaciones, idnuevo -> {
            if (idnuevo.succeeded()){
                if (asignaciones.containsKey("_id")){
                    id.handle(Future.succeededFuture(asignaciones.getString("_id")));
                }else{
                    id.handle(idnuevo);
                }
            }else{
                if (asignaciones.containsKey("_id")){
                    logger.severe(String.format("Error al realizar actualizadod de asignacion   %s", idnuevo.cause()));
                }else{
                    logger.severe(String.format("Error al realizar guaraddo de asignacion   %s", idnuevo.cause()));
                }
                id.handle(null);
            }
        });
        return this;
    }

    @Override
    public DAOPermisosInterface eliminaAsignaciones(JsonObject asignaciones, Handler<AsyncResult<String>> id) {
        mongoClient.removeDocument(COLLECTION_ASIGNACION, asignaciones, response->{
            String _id = asignaciones.getString("_id");
            if (response.succeeded() && response.result().getRemovedCount()==1){
                id.handle(Future.succeededFuture(_id));
            }else{
                if (response.failed()){
                    logger.log(Level.SEVERE, "Error de eliminacion en base de datos", response.cause());
                }else{
                    id.handle(Future.succeededFuture("No localizado"));
                }
            }
        });
        return this;
    }
    
    @Override
    public DAOPermisosInterface buscaAsignacionCurpOYidUsuario(JsonObject query, Handler<AsyncResult<List<JsonObject>>> objetos){
         mongoClient.find(COLLECTION_ASIGNACION, query, objetos);
         return this;
    }
}
