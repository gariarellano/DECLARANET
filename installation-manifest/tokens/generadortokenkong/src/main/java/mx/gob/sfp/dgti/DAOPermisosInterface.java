/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.List;

/**
 * Interface que contiene la firma de los 
 * métodos par interactuar con la base de datos
 * de permisos para su manejo de los datos.
 * 
 * @author cgarias
 * @since 
 */
public interface DAOPermisosInterface {
   
    static DAOPermisos init(Vertx vertx){
        return new DAOPermisos(vertx);
    }
    
    public DAOPermisosInterface buscaPerfil(JsonObject query, Handler<AsyncResult<List<JsonObject>>> objetos);
    public DAOPermisosInterface guardaPerfil(JsonObject perfil, Handler<AsyncResult<String>> id);
    public DAOPermisosInterface eliminaPerfil(JsonObject perfil, Handler<AsyncResult<String>> id);
    
    public DAOPermisosInterface buscaAsignacion(JsonObject query, Handler<AsyncResult<List<JsonObject>>> objetos);
    public DAOPermisosInterface guardaAsignaciones(JsonObject asignaciones, Handler<AsyncResult<String>> id);
    public DAOPermisosInterface eliminaAsignaciones(JsonObject asignaciones, Handler<AsyncResult<String>> id);
    
    public DAOPermisosInterface buscaAsignacionCurpOYidUsuario(JsonObject query, Handler<AsyncResult<List<JsonObject>>> objetos);
}
