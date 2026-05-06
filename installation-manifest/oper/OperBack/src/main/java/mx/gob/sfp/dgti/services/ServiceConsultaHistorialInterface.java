package mx.gob.sfp.dgti.services;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public interface ServiceConsultaHistorialInterface {

	static ServiceConsultaHistorialInterface init(Vertx vertx, WebClient client) {
		return new ServiceConsultaHistorialImpl(vertx, client);
	}
	
	public Future<JsonObject> consultaHistorial(String curp, String rfc, String nombre, String idUsrDecnet, Integer collName, JsonObject usuario);
	
	public Future<JsonObject> obtenerDeclaracion(JsonObject parametros);
	
	public Future<JsonObject> obtenerAcuse(JsonObject parametros);
	
	public Future<JsonObject> obtenerDomicilios(String numeroDeclaracion, Integer collName,JsonObject usuario, Integer idUsuario);
}
