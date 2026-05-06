/* 
 * Sistema Propiedad de la Secretaría de la Función Pública DGTI
 * "VerificarDeclaraciones" Servicio que permite verificar si 
 * existen declaraciones en proceso o insertar el tipo de declaración a presentar
 * 
 * Proyecto desarrollado por programador04@funcionpublica.gob.mx
 */
/**
 * Clase que contiene los métodos uilizados para la manipulación de información
 * en la base de datos
 *
 * @author programador04@sfp.gob.mx
 * @since 29/10/2019
 */
package mx.gob.sfp.dgti.daopxy;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.List;

public interface DAODeclaracionAPresentarInterface {

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
    public static final String KEEP_ALIVE = "keepAlive";

    static DAODeclaracionAPresentarInterface create(Vertx vertx) {
        return new DAODeclaracionAPresentar(vertx,
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
     * Método que verifica la existencia de una declaración temporal con base en
     * el id del usuario
     *
     * @param institucionReceptora
     * @param idUsuario propiedad que almacena el id del usuario
     * @return Future(Lis) lista con el json de resultados
     * @author programador04@sfp.gob.mx
     * @since 29/10/2019 edited 26/11/2019
     */
    public Future<List<JsonObject>> verificarDeclaracionEnProceso(JsonObject institucionReceptora, Integer idUsuario);

    /**
     * Método que elimina la declaracion temporal del usuario
     *
     * @param encabezado
     * @param institucionReceptora
     * @return Future<'Boolean '> lista con el json de resultados
     * @author programador04@sfp.gob.mx
     * @since 26/11/2019
     */
    public Future<Boolean> eliminarDeclaracionTemp(JsonObject encabezado, JsonObject institucionReceptora);

    /**
     * Método que inserta la declaración en proceso
     *
     * @param institucionReceptora propiedad que almacena la institución
     * receptora
     * @param encabezado
     * @param declaracion
     * @return Future<"JsonObject"> retorna la declaración ya guardada con id
     * generado
     * @author programador04@sfp.gob.mx
     * @since 05/11/2019
     */
    public Future<JsonObject> crearNuevaDeclaracion(JsonObject institucionReceptora, JsonObject encabezado, JsonObject declaracion);

    /**
     * Metodo que obtiene un true o false dependiendo si ya se encuentra la
     * declaración firmada
     *
     * @param encabezado propiedad que almacena los valores enviados desde el
     * frontend
     * @param institucionReceptora
     * @return Future<"Boolean"> retorna true o false si se ecnuentra la
     * declaración firmada
     * @author programador04@sfp.gob.mx
     * @since 19/11/2019
     */
    public Future<Boolean> verificarDeclaracionFirmada(JsonObject encabezado, JsonObject institucionReceptora);

    /**
     * Metodo que obtiene la lista de declaraciones de tipo modificación ya
     * firmadas
     *
     * @param encabezado encabezado de la declaraión
     * @param institucionReceptora institucionReceptora de la declaraión
     * @return Future(List(JsonObject)) lista con el json de resultados
     * @author programador04@sfp.gob.mx
     * @since 22/11/2019 edited 27/11/2019
     */
    public Future<List<JsonObject>> obtenerListaDeclaModificacionFirmadas(JsonObject encabezado, JsonObject institucionReceptora);

    /**
     * Metodo para obtener el historial de declaraciones
     *
     * @param parametros
     * @return lista de JsonObject
     * @author programador04@sfp.gob.mx
     * @since 13/12/2019
     */
    public Future<List<JsonObject>> obtenerHistorialDeclaracionesUsuario(JsonObject parametros);

    /**
     * Metodo para obtener el historial de notas por declaracion
     *
     * @param parametros
     * @return lista de JsonObject
     * @author programador04@sfp.gob.mx
     * @since 05/03/2020
     */
    public Future<List<JsonObject>> obtenerHistorialNotasPorDeclaracion(JsonObject parametros);
   
}
