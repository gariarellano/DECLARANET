/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "FIELAUTO" proyecto que realizar el firmado del contrato para utilzar los archivos para 
 * firma automatizada para los sistemas de la secretaría de la funcion pública.
 */
package mx.gob.sfp.dgti;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 *
 * @author cgarias
 */
public abstract class DAOGeneric {
    private static final String POOL_ENTES_RECEPTOR = "pool-aplicacion-permisos";
    private static final String HOST="host";
    private static final String HOST_IP = System.getenv(HOST)!= null ? System.getenv(HOST) : "192.168.1.145";
    private static final String PORT="port";
    private static final Integer PORT_NUMBER = System.getenv(PORT) != null ? Integer.parseInt(System.getenv(PORT)): 27020;
    private static final String MAX_POOL_SIZE="maxPoolSize";
    private static final Integer MAXIM_POOL_SIZE= System.getenv(MAX_POOL_SIZE) != null ? Integer.parseInt(System.getenv(MAX_POOL_SIZE)):2;
    private static final String MIN_POOL_SIZE="minPoolSize";
    private static final Integer MINI_POOL_SIZE = System.getenv(MIN_POOL_SIZE)!= null ? Integer.parseInt(System.getenv(MIN_POOL_SIZE)): 1;
    private static final String USER_NAME = "username";
    private static final String USER_NAME_DB = "usernameDb";
    private static final String USER_NAME_VALUE = System.getenv(USER_NAME_DB)!= null ? System.getenv(USER_NAME_DB): "mongo";
    private static final String P_W_D = "password";
    private static final String P_W_D_VALUE = System.getenv(P_W_D) != null ? System.getenv(P_W_D): "123";
    private static final String DB_NAME = "db_name";
    private static final String DB_NAME_VALUE=System.getenv(DB_NAME) != null ? System.getenv(DB_NAME): "prueba";
    private static final String KEEP_ALIVE ="keepAlive";
    protected final MongoClient mongoClient;
        
    DAOGeneric(Vertx vertx){
        JsonObject connection = new JsonObject()
                                            .put(HOST, HOST_IP)
                                            .put(PORT, PORT_NUMBER)
                                            .put(MAX_POOL_SIZE, MAXIM_POOL_SIZE)
                                            .put(MIN_POOL_SIZE, MINI_POOL_SIZE)
                                            .put(USER_NAME, USER_NAME_VALUE)
                                            .put(P_W_D, P_W_D_VALUE)
                                            .put(DB_NAME, DB_NAME_VALUE)
                                            .put(KEEP_ALIVE, Boolean.TRUE);
        mongoClient = MongoClient.createShared(vertx, 
                connection, 
                POOL_ENTES_RECEPTOR);        
    }
}
