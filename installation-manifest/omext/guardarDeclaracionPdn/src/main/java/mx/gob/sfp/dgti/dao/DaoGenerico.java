/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.dao;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * Clase abstracta que contiene la configuracion 
 * para acceder a la base de datos mongo.
 * 
 * @author programador09
 */
public abstract class DaoGenerico {
    private static final String POOL_CUMPLIMIENTO = "POOL_CUMPLIMIENTO";
    private static final String HOST_O="host_o";
    private static final String HOST="host";
    private static final String HOST_IP = System.getenv(HOST_O)!= null ? System.getenv(HOST_O) : "";
    private static final String PORT_O="port_o";
    private static final String PORT="port";
    private static final Integer PORT_NUMBER = System.getenv(PORT_O) != null ? Integer.parseInt(System.getenv(PORT_O)): 27021;
    private static final String MAX_POOL_SIZE="maxPoolSize";
    private static final Integer MAXIM_POOL_SIZE= System.getenv(MAX_POOL_SIZE) != null ? Integer.parseInt(System.getenv(MAX_POOL_SIZE)):20;
    private static final String MIN_POOL_SIZE="minPoolSize";
    private static final Integer MINI_POOL_SIZE = System.getenv(MIN_POOL_SIZE)!= null ? Integer.parseInt(System.getenv(MIN_POOL_SIZE)): 2;
    private static final String USER_NAME="username";
    private static final String USER_NAME_O="username_o";
    private static final String USER_NAME_VALUE = System.getenv(USER_NAME_O)!= null ? System.getenv(USER_NAME_O): "";
    private static final String P_W_D="password";
    private static final String P_W_D_O="password_o";
    private static final String P_W_D_VALUE = System.getenv(P_W_D_O) != null ? System.getenv(P_W_D_O): "";
    private static final String DB_NAME="db_name";
    private static final String DB_NAME_O="db_name_o";
    private static final String DB_NAME_VALUE=System.getenv(DB_NAME_O) != null ? System.getenv(DB_NAME_O): "";
    private static final String KEEP_ALIVE ="keepAlive";
    protected final MongoClient mongoClient;
    
    protected DaoGenerico(Vertx vertx){
        mongoClient = MongoClient.createShared(vertx, new JsonObject()
                                            .put(HOST, HOST_IP)
                                            .put(PORT, PORT_NUMBER)
                                            .put(MAX_POOL_SIZE, MAXIM_POOL_SIZE)
                                            .put(MIN_POOL_SIZE, MINI_POOL_SIZE)
                                            .put(USER_NAME, USER_NAME_VALUE)
                                            .put(P_W_D, P_W_D_VALUE)
                                            .put(DB_NAME, DB_NAME_VALUE)
                                            .put(KEEP_ALIVE, Boolean.TRUE), 
                POOL_CUMPLIMIENTO)
                                            
                ;        
    }
}
