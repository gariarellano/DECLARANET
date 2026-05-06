package mx.gob.sfp.dgti.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.reactivex.core.Vertx;

/**
 * Clase utilizada para las reglas de negocio del 
 * sistema y llamados a la base de datos
 * 
 * @author programador04@sfp.gob.mx
 * @since 17/12/2019
 * @edited 22/12/2019
 */
public interface ServiceInicioDeclaracionInterface {
	
	/**
	 * Contructor de cla clase. Recibe un objeto Vertx
	 * @param vertx
	 */
	static ServiceInicioDeclaracionInterface init(Vertx vertx,  WebClient client) {
		return new ServiceInicioDeclaracionImpl(vertx, client);
	}
	
	 
	/**
	 *Método para verificar las declaraciones del usuario 
	 * @param data
	 * @return Future<'JsonObject'>
	 * @author programador04@sfp.gob.mx
	 * @since 17/12/2019
	 */
	public Future<JsonObject> inicioDeclaracion(JsonObject data);
	
	/**
	 *Método para verificar las declaraciones del usuario 
	 *cuando ya existe la sesión
	 * @param data
	 * @return Future<'JsonObject'>
	 * @author programador04@sfp.gob.mx
	 * @since 26/12/2019
	 */
	public Future<JsonObject> verificarDeclaracion(JsonObject data);
	
	 /**
	  * Método para crear la declaración temporal del usuario
	 * @param data
	 * @return Future<'JsonObject'>
	 * @since 17/12/2019
	 * @author programador04@sfp.gob.mx
	 */
	public Future<JsonObject> crearDeclaracionTemporal(JsonObject data);
	
	 /**
	  * Método para consultar los años para una declaración extemporanea
	 * @param data
	 * @return Future<'JsonObject'>
	 * @since 17/12/2019
	 * @author programador04@sfp.gob.mx
	 */
	public Future<JsonObject> consultaAniosDeclaExtemporanea(JsonObject data);
	
	 /**
	  * Método para eliminar una declaración temporal
	 * @param data
	 * @return Future<'JsonObject'>
	 * @since 17/12/2019
	 * @author programador04@sfp.gob.mx
	 */
	public Future<JsonObject> eliminarDeclaracionTemporal(JsonObject data);
	
	 /**
	  * Método para obtener el historial de declaraciones
	 * @param data
	 * @return Future<'JsonObject'>
	 * @since 17/12/2019
	 * @author programador04@sfp.gob.mx
	 */
	public Future<JsonObject> obtenerHistorialDeclaraciones(JsonObject data);
	 
	 /**
	  * Método para crear la nota de la declaración
	 * @param data
	 * @return Future<'JsonObject'>
	 * @since 01/03/2020
	 * @author programador04@sfp.gob.mx
	 */
	public Future<JsonObject> crearNotaDeDeclaracion(JsonObject dataComplete);
	 
	 /**
	  * Método para obtener el historial de notas de una declaracion
	 * @param data
	 * @return Future<'JsonObject'>
	 * @since 05/03/2020
	 * @author programador04@sfp.gob.mx
	 */
	public Future<JsonObject> obtenerHistorialNotasDeclaracion(JsonObject dataComplete);
	
	
}
