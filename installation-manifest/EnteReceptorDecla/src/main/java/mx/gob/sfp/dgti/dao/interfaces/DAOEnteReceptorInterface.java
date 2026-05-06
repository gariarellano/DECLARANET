/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.dao.interfaces;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.ProxyIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.List;


import mx.gob.sfp.dgti.dao.DAOEnteReceptor;

/**
 * Interface que contiene la firma de los métodos 
 * para el acceso a la base de datos.
 * 
 * @author cgarias
 * @since 06/11/2019
 */
@VertxGen
@ProxyGen
public interface DAOEnteReceptorInterface {
	
	static final String SERVICE_ADDRESS = "ente-receptor";
	
    static final String HOST="host";
    static final String HOST_IP = System.getenv(HOST);
    static final String PORT="port";
    static Integer PORT_NUMBER = System.getenv(PORT) != null ? Integer.parseInt(System.getenv(PORT)): null;
    static final String MAX_POOL_SIZE="maxPoolSize";
    static Integer MAXIM_POOL_SIZE= System.getenv(MAX_POOL_SIZE) != null ? Integer.parseInt(System.getenv(MAX_POOL_SIZE)):null;
    static final String MIN_POOL_SIZE="minPoolSize";
    static Integer MINI_POOL_SIZE = System.getenv(MIN_POOL_SIZE)!= null ? Integer.parseInt(System.getenv(MIN_POOL_SIZE)): null;
    static final String USER_NAME="username";
    static final String USER_NAME_VALUE = System.getenv(USER_NAME);
    static String P_W_D="password";
    static final String P_W_D_VALUE = System.getenv(P_W_D);
    static String DB_NAME="db_name";
    static String DB_NAME_VALUE=System.getenv(DB_NAME);
    static final String KEEP_ALIVE ="keepAlive";
	
    static DAOEnteReceptorInterface create(Vertx vertx){
    	
        return new DAOEnteReceptor(vertx, new JsonObject()
                                            .put(HOST, HOST_IP)
                                            .put(PORT, PORT_NUMBER)
                                            .put(MAX_POOL_SIZE, MAXIM_POOL_SIZE)
                                            .put(MIN_POOL_SIZE, MINI_POOL_SIZE)
                                            .put(USER_NAME, USER_NAME_VALUE)
                                            .put(P_W_D, P_W_D_VALUE)
                                            .put(DB_NAME, DB_NAME_VALUE)
                                            .put(KEEP_ALIVE, Boolean.TRUE));
    }
    
    static DAOEnteReceptorInterface createProxy(Vertx vertx,String address){
        return new DAOEnteReceptorInterfaceVertxEBProxy(vertx, address);
    }
    
    @Fluent
    public DAOEnteReceptorInterface consultaEnteReceptor(String path, Handler<AsyncResult<JsonObject>> resultHandler);
    @Fluent
    public DAOEnteReceptorInterface consultaEnteReceptorPorCollName(Integer collName, Handler<AsyncResult<JsonObject>> resultHandler);
    @Fluent
    public DAOEnteReceptorInterface consultaEnteReceptorporEnteId(String idEnte, Handler<AsyncResult<JsonObject>> resultHandler);
    @Fluent
    public DAOEnteReceptorInterface agregaEnteReceptorDeclaracion(JsonObject enteReceptor,Handler<AsyncResult<JsonObject>> resultHandler);  
    @Fluent
    public DAOEnteReceptorInterface consultarTodos(Handler<AsyncResult<List<JsonObject>>> resultHandler);
    
    
}