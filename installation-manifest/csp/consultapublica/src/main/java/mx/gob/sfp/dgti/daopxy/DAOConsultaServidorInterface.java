package mx.gob.sfp.dgti.daopxy;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface DAOConsultaServidorInterface {

    public static final String POOL_ENTES_RECEPTOR = "pool-entes-receptor";
    public static final String HOST = "host";
    public static final String HOST_IP = System.getenv(HOST) != null ? System.getenv(HOST) : "";
    public static final String PORT = "port";
    public static final Integer PORT_NUMBER = System.getenv(PORT) != null ? Integer.parseInt(System.getenv(PORT)) : 27021;
    public static final String MAX_POOL_SIZE = "maxPoolSize";
    public static final Integer MAXIM_POOL_SIZE = System.getenv(MAX_POOL_SIZE) != null ? Integer.parseInt(System.getenv(MAX_POOL_SIZE)) : 20;
    public static final String MIN_POOL_SIZE = "minPoolSize";
    public static final Integer MINI_POOL_SIZE = System.getenv(MIN_POOL_SIZE) != null ? Integer.parseInt(System.getenv(MIN_POOL_SIZE)) : 2;
    public static final String USER_NAME = "username";
    public static final String USER_NAME_VALUE = System.getenv(USER_NAME) != null ? System.getenv(USER_NAME) : "";
    public static final String P_W_D = "password";
    public static final String P_W_D_VALUE = System.getenv(P_W_D) != null ? System.getenv(P_W_D) : "";
    public static final String DB_NAME = "db_name";
    public static final String DB_NAME_VALUE = System.getenv(DB_NAME) != null ? System.getenv(DB_NAME) : "";
    public static final String MAX_IDLE_TIME_MS = "maxIdleTimeMS";
    public static final String MAINTENANCE_FREQUENCY_MS = "maintenanceFrequencyMS";
    public static final Integer MAX_IDLE_TIME_MS_VALUE = System.getenv("MAX_IDLE_TIME_MS") != null ? Integer.parseInt(System.getenv("MAX_IDLE_TIME_MS")): 240000;
    public static final Integer MAINTENANCE_FREQUENCY_MS_VALUE = System.getenv("MAINTENANCE_FREQUENCY_MS") != null ? Integer.parseInt(System.getenv("MAINTENANCE_FREQUENCY_MS")): 300000;
    public static final String KEEP_ALIVE = "keepAlive";

    static DAOConsultaServidorInterface create(Vertx vertx) {
        return new DAOConsultaServidorImpl(vertx,
                new JsonObject()
                .put(HOST, HOST_IP)
                .put(PORT, PORT_NUMBER)
                .put(MAX_POOL_SIZE, MAXIM_POOL_SIZE)
                .put(MIN_POOL_SIZE, MINI_POOL_SIZE)
                .put(USER_NAME, USER_NAME_VALUE)
                .put(P_W_D, P_W_D_VALUE)
                .put(DB_NAME, DB_NAME_VALUE)
                .put(MAX_IDLE_TIME_MS, MAX_IDLE_TIME_MS_VALUE)
//                .put(MAINTENANCE_FREQUENCY_MS, MAINTENANCE_FREQUENCY_MS_VALUE) peticion--JZE
                .put(KEEP_ALIVE, Boolean.TRUE));
    }
    public Future<JsonArray> buscarPorNombreSp(String nombre, Integer collName);
    
//    public Future<List<JsonObject>> buscarPorNombre(String nombre, Integer collName);

    public Future<List<JsonObject>> buscarHistorialSP(Integer idUsr, Integer collName);

    public Future<List<JsonObject>> buscarPorRFC(String rfc);
        
    public Future<List<JsonObject>> consultaReservados(Integer idEnte);
    
    public Future<List<JsonObject>> consultaGabinete(int idGabinete);
    
    

}
