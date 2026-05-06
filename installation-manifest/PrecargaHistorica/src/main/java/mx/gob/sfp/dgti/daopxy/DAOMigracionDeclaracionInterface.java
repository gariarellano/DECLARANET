package mx.gob.sfp.dgti.daopxy;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * Clase que contiene los metodos utilizados desde el fetchers de graphql
 *
 * @author gustavo gutierrez ponce
 * @since 10/01/2020
 */
public interface DAOMigracionDeclaracionInterface {

	
	static DAOMigracionDeclaracionInterface create(Vertx vertx) {
		
		JsonObject conexion = new JsonObject();
		if(System.getenv("CONEXION") != null) {
			conexion = new JsonObject(System.getenv("CONEXION")) ;
		}else {
			conexion.put("url",System.getenv("DATABASE_HOST") != null ? System.getenv("DATABASE_HOST"): "jdbc:oracle:thin:@172.29.149.10:1521:DEV12C")
				.put("user",System.getenv("DATABASE_USERNAME") != null ? System.getenv("DATABASE_USERNAME"): "DECLARANET")
				.put("password",System.getenv("DATABASE_PASSWORD") != null ?  System.getenv("DATABASE_PASSWORD") : "R>IRNL6BDy");
		}
		
		conexion.put("driver_class","oracle.jdbc.driver.OracleDriver")
			.put("initial_pool_size",System.getenv("INIT_POOL") != null ? Integer.parseInt(System.getenv("INIT_POOL")) : 1)
			.put("min_pool_size",System.getenv("MIN_POOL") != null ? Integer.parseInt(System.getenv("MIN_POOL")) : 1)
			.put("max_pool_size",System.getenv("MAX_POOL") != null ? Integer.parseInt(System.getenv("MAX_POOL")) : 1)
			.put("max_idle_time",System.getenv("IDEL_POLL") != null ? Integer.parseInt(System.getenv("IDEL_POLL")) : 1);
		return new DAOMigracionDeclaracionImpl(vertx, conexion);
	}
	
	/**
	 *Metodo para obtener el modulo de datos curriculares
	 *@param numDeclaracion; objeto que contiene el numero de la declaracion a precargar
	 *@return Future<List<JsonObject>> ; retorna una lista JsonObject con los registros
	 */
	public Future<List<JsonObject>> obtenerDatosCurriculares(int numDeclaracion);
	
	/**
	 *Metodo para obtener el modulo de expericia laboral
	 *@param numDeclaracion; objeto que contiene el numero de la declaracion a precargar
	 *@return Future<List<JsonObject>> ; retorna una lista JsonObject con los registros
	 */
	public Future<List<JsonObject>> obtenerExperienciaLaboral(int numDeclaracion);
	
	/**
	 *Metodo para obtener el modulo de datos pareja
	 *@param numDeclaracion; objeto que contiene el numero de la declaracion a precargar
	 *@return Future<List<JsonObject>> ; retorna una lista JsonObject con los registros
	 */
	public Future<List<JsonObject>> obtenerDatosParejas(int numDeclaracion);
	
	/**
	 *Metodo para obtener el modulo de dependientes economicos
	 *@param numDeclaracion; objeto que contiene el numero de la declaracion a precargar
	 *@return Future<List<JsonObject>> ; retorna una lista JsonObject con los registros
	 */
	public Future<List<JsonObject>> obtenerDependientesEconomicos(int numDeclaracion);
	
	/**
	 *Metodo para obtener el modulo de inversiones
	 *@param numDeclaracion; objeto que contiene el numero de la declaracion a precargar
	 *@return Future<List<JsonObject>> ; retorna una lista JsonObject con los registros
	 */
	public Future<List<JsonObject>> obtenerInversionesCuentasValores(int numDeclaracion);
	
	/**
	 *Metodo para obtener el modulo de adeudos/pasivos
	 *@param numDeclaracion; objeto que contiene el numero de la declaracion a precargar
	 *@return Future<List<JsonObject>> ; retorna una lista JsonObject con los registros
	 */
	public Future<List<JsonObject>> obtenerAdeudosPasivos(int numDeclaracion);
	
	/**
	 *Metodo para obtener el modulo de bienes muebles
	 *@param numDeclaracion; objeto que contiene el numero de la declaracion a precargar
	 *@return Future<List<JsonObject>> ; retorna una lista JsonObject con los registros
	 */
	public Future<List<JsonObject>> obtenerBienesMuebles(int numDeclaracion);
	
	/**
	 *Metodo para obtener el modulo de bienes innuebles
	 *@param numDeclaracion; objeto que contiene el numero de la declaracion a precargar
	 *@return Future<List<JsonObject>> ; retorna una lista JsonObject con los registros
	 */
	public Future<List<JsonObject>> obtenerBienesInmubeles(int numDeclaracion);
	
	/**
	 *Metodo para obtener el modulo de vehiculos 
	 *@param numDeclaracion; objeto que contiene el numero de la declaracion a precargar
	 *@return Future<List<JsonObject>> ; retorna una lista JsonObject con los registros
	 */
	public Future<List<JsonObject>> obtenerVehiculos(int numDeclaracion);

}
