package mx.gob.sfp.dgti.daopxy;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import static mx.gob.sfp.dgti.util.Constantes.HOST;
import static mx.gob.sfp.dgti.util.Constantes.PORT;
import static mx.gob.sfp.dgti.util.Constantes.MAX_POOL_SIZE;
import static mx.gob.sfp.dgti.util.Constantes.MIN_POOL_SIZE;
import static mx.gob.sfp.dgti.util.Constantes.USER_NAME;
import static mx.gob.sfp.dgti.util.Constantes.P_W_D;
import static mx.gob.sfp.dgti.util.Constantes.DB_NAME;
import static mx.gob.sfp.dgti.util.Constantes.KEEP_ALIVE;

public interface DAOConsultaHistorialInterface {

    public static final String HOST_IP = System.getenv(HOST) != null ? System.getenv(HOST) : "";
    public static final Integer PORT_NUMBER = System.getenv(PORT) != null ? Integer.parseInt(System.getenv(PORT)) : 27021;
    public static final Integer MAXIM_POOL_SIZE = System.getenv(MAX_POOL_SIZE) != null ? Integer.parseInt(System.getenv(MAX_POOL_SIZE)) : 20;
    public static final Integer MINI_POOL_SIZE = System.getenv(MIN_POOL_SIZE) != null ? Integer.parseInt(System.getenv(MIN_POOL_SIZE)) : 2;
    public static final String USER_NAME_VALUE = System.getenv(USER_NAME) != null ? System.getenv(USER_NAME) : "";
    public static final String P_W_D_VALUE = System.getenv(P_W_D) != null ? System.getenv(P_W_D) : "";
    public static final String DB_NAME_VALUE = System.getenv(DB_NAME) != null ? System.getenv(DB_NAME) : "";
	
    
    static DAOConsultaHistorialInterface create(Vertx vertx) {
    	return new  DAOConsultaHistorialImpl(vertx,
                new JsonObject()
                .put(HOST, HOST_IP)
                .put(PORT, PORT_NUMBER)
                .put(MAX_POOL_SIZE, MAXIM_POOL_SIZE)
                .put(MIN_POOL_SIZE, MINI_POOL_SIZE)
                .put(USER_NAME, USER_NAME_VALUE)
                .put(P_W_D, P_W_D_VALUE)
                .put(DB_NAME, DB_NAME_VALUE)
                .put(KEEP_ALIVE, Boolean.TRUE));
    }
    
    /**
     * Método para busqueda del historial por curp o rfc o nombre
     * 
     *
     * @param datoCapturado
     * @param opcion
     * @param collName
     * @return  */
    public Future<JsonObject> busquedaHistorialIdUsuario(String datoCapturado, Integer opcion, Integer collName);
    
    /**
     *Método para obtener la declaracion solicitada 
     *
     * @param params
     * @param exitoso
     * @param collName
     * @return 
     */
    public Future<Boolean> guardarBitacoraOPER(JsonObject params, boolean exitoso, Integer collName);
    
    public Future<JsonArray> busquedaPorNombreOrfc(String nombre, String rfc, Integer collName);  
}
