/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.daopxy;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author cgarias
 */
public interface DAOExpuestosInterface {
    
    public static final String POOL_ENTES_RECEPTOR = "pool-entes-receptor";
    public static final String HOST = "host";
    public static final String HOST_EX = "host_ex";
    public static final String HOST_IP = System.getenv(HOST_EX) != null ? System.getenv(HOST_EX) : "";
    public static final String PORT = "port";
    public static final String PORT_EX = "port_ex";
    public static final Integer PORT_NUMBER = System.getenv(PORT_EX) != null ? Integer.parseInt(System.getenv(PORT_EX)) : 27021;
    public static final String MAX_POOL_SIZE = "maxPoolSize";
    public static final Integer MAXIM_POOL_SIZE = System.getenv(MAX_POOL_SIZE) != null ? Integer.parseInt(System.getenv(MAX_POOL_SIZE)) : 20;
    public static final String MIN_POOL_SIZE = "minPoolSize";
    public static final Integer MINI_POOL_SIZE = System.getenv(MIN_POOL_SIZE) != null ? Integer.parseInt(System.getenv(MIN_POOL_SIZE)) : 2;
    public static final String USER_NAME = "username";
    public static final String USER_NAME_EX = "username_ex";
    public static final String USER_NAME_VALUE = System.getenv(USER_NAME_EX) != null ? System.getenv(USER_NAME_EX) : "";
    public static final String P_W_D = "password";
    public static final String P_W_D_EX = "password_ex";
    public static final String P_W_D_VALUE = System.getenv(P_W_D_EX) != null ? System.getenv(P_W_D_EX) : "";
    public static final String DB_NAME = "db_name";
    public static final String DB_NAME_EX = "db_name_ex";
    public static final String DB_NAME_VALUE = System.getenv(DB_NAME_EX) != null ? System.getenv(DB_NAME_EX) : "";
    public static final String MAX_IDLE_TIME_MS = "maxIdleTimeMS";
    public static final String MAINTENANCE_FREQUENCY_MS = "maintenanceFrequencyMS";
    public static final Integer MAX_IDLE_TIME_MS_VALUE = System.getenv("MAX_IDLE_TIME_MS") != null ? Integer.parseInt(System.getenv("MAX_IDLE_TIME_MS")): 240000;
    public static final Integer MAINTENANCE_FREQUENCY_MS_VALUE = System.getenv("MAINTENANCE_FREQUENCY_MS") != null ? Integer.parseInt(System.getenv("MAINTENANCE_FREQUENCY_MS")): 300000;
    public static final String KEEP_ALIVE = "keepAlive";

    static DAOExpuestosInterface create(Vertx vertx) {
        return new DAOExpuestos(vertx,
                new JsonObject()
                .put(HOST, HOST_IP)
                .put(PORT, PORT_NUMBER)
                .put(MAX_POOL_SIZE, MAXIM_POOL_SIZE)
                .put(MIN_POOL_SIZE, MINI_POOL_SIZE)
                .put(USER_NAME, USER_NAME_VALUE)
                .put(P_W_D, P_W_D_VALUE)
                .put(DB_NAME, DB_NAME_VALUE)
                .put(MAX_IDLE_TIME_MS, MAX_IDLE_TIME_MS_VALUE)
                .put(KEEP_ALIVE, Boolean.TRUE));
    }
    
    public DAOExpuestosInterface consultaDatosExpuestos(String rfc, String curp, Handler<AsyncResult<JsonObject>> existe);
}
