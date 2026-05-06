/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "EnteReceptorDecla" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.dao.interfaces;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.List;
import mx.gob.sfp.dgti.dao.DAOConsultDecla;

/**
 * Interface que contiene la firma de los métodos 
 * para el acceso a la base de datos.
 * 
 * @author cgarias
 * @since 06/11/2019
 * @edited by programador04 23/12/2019
 */
public interface DAOConsultDeclaInterface {
    static final String HOST="host";
    static final String HOST_IP = System.getenv(HOST)!= null ? System.getenv(HOST) : "";
    static final String PORT="port";
    static Integer PORT_NUMBER = System.getenv(PORT) != null ? Integer.parseInt(System.getenv(PORT)): 27021;
    static final String MAX_POOL_SIZE="maxPoolSize";
    static Integer MAXIM_POOL_SIZE= System.getenv(MAX_POOL_SIZE) != null ? Integer.parseInt(System.getenv(MAX_POOL_SIZE)):20;
    static final String MIN_POOL_SIZE="minPoolSize";
    static Integer MINI_POOL_SIZE = System.getenv(MIN_POOL_SIZE)!= null ? Integer.parseInt(System.getenv(MIN_POOL_SIZE)): 2;
    static final String USER_NAME="username";
    static final String USER_NAME_VALUE = System.getenv(USER_NAME)!= null ? System.getenv(USER_NAME): "";
    static String P_W_D="password";
    static final String P_W_D_VALUE = System.getenv(P_W_D) != null ? System.getenv(P_W_D): "";
    static String DB_NAME="db_name";
    static String DB_NAME_VALUE=System.getenv(DB_NAME) != null ? System.getenv(DB_NAME): "";
    static final String KEEP_ALIVE ="keepAlive";
    
    static DAOConsultDeclaInterface init(Vertx vertx){
        return new DAOConsultDecla(vertx,
                                    new JsonObject()
                                            .put(HOST, HOST_IP)
                                            .put(PORT, PORT_NUMBER)
                                            .put(MAX_POOL_SIZE, MAXIM_POOL_SIZE)
                                            .put(MIN_POOL_SIZE, MINI_POOL_SIZE)
                                            .put(USER_NAME, USER_NAME_VALUE)
                                            .put(P_W_D, P_W_D_VALUE)
                                            .put(DB_NAME, DB_NAME_VALUE)
                                            .put(KEEP_ALIVE, Boolean.TRUE)
        );
    }
   
    public DAOConsultDeclaInterface consultaDeclaracion(JsonObject parametrosDeclaracion, boolean isTemp, Handler<AsyncResult<JsonObject>> resultHandler);        
    
    public DAOConsultDeclaInterface consultaDeclaracionHtml(JsonObject parametrosDeclaracion, Handler<AsyncResult<JsonObject>> localizada);
    
    public DAOConsultDeclaInterface consultaEnteReceptor(Integer collName, boolean isTemp,
			Handler<AsyncResult<JsonObject>> resultHandler);
    
    public DAOConsultDeclaInterface consultaFirmante(JsonObject parametros, Handler<AsyncResult<JsonObject>> resultHandler);
    
    public DAOConsultDeclaInterface consultaRecepcionWeb(JsonObject parametrosDeclaracion, Handler<AsyncResult<JsonObject>> resultHandler);
    
    public DAOConsultDeclaInterface consultaHistorialUsuario(JsonObject parametros, Handler<AsyncResult<List<JsonObject>>> resultHandler);
    
}