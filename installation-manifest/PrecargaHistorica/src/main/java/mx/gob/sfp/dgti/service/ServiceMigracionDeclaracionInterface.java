package mx.gob.sfp.dgti.service;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * Clase utilizada para las reglas de negocio del 
 * sistema y llamados a la base de datos
 * 
 * @author programador04@sfp.gob.mx
 * @since 10/01/2020
 */
public interface ServiceMigracionDeclaracionInterface {
	
	/**
	 * Contructor de cla clase. Recibe un objeto Vertx
	 * @param vertx
	 */
	static ServiceMigracionDeclaracionInterface init(Vertx vertx) {
		return new ServiceMigracionDeclaracionImpl(vertx);
	}
	
	
 public Future<JsonObject> generarJsonDeclaracion(int numDeclaracion);
	 
	
}
