/**
 * @(#)EnteMongoService.java 11/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.mongoservice;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.ProxyIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.constantes.Situacion;

import java.util.List;

/**
 * Interface para el MongoService EnteMongoService
 * 
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 11/02/2019
 */
@ProxyGen
@VertxGen
public interface EnteMongoService {
	
	static final String SERVICE_ADDRESS = "ente-mongo-service";

	static EnteMongoService create(Vertx vertx, JsonObject config) {
		    return new EnteMongoServiceImpl(vertx, config);
	}

	static EnteMongoService createProxy(Vertx vertx, String address) {
		return new EnteMongoServiceVertxEBProxy(vertx, address);
	}  
	
	/**
	 * Funcion que agregar un nuevo ente, el primero de su historia, es su propio padre, verifica
	 * primero que no existan entes con la misma llave, si es el caso no permite la insercion
	 * 
	 * @param ente: JsonObject Ente nuevo que se va a guardar
	 * @param condicionesLlave: JsonObjects Objeto con las condiciones de la llave para ver si
	 * no existe un ente ya creado que tenga la misma llave
	 * @param resultHandler: Handler<> handler para la respuesta de tipo String
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 19/02/2019
	 */
	@Fluent
	EnteMongoService agregarEnteNuevo(JsonObject ente, JsonObject condicionesLlave, 
			Handler<AsyncResult<String>> resultHandler);

	/**
	 * Funcion que actualiza unicamente la informacion del ente, sin crear un documento nuevo,
	 * existen campos especificos que pueden ser editados por este medio, de lo contrario se
	 * debe de utilizar la funcionalidad de actualizarEnte()
	 * 
	 * @param ente: JsonObject Ente nuevo con la informacion que se ha modificado
	 * @param resultHandler: Handler<> handler para la respuesta de tipo Void
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 19/02/2019
	 */
	@Fluent
	EnteMongoService actualizarInfoEnte(JsonObject ente, Handler<AsyncResult<Void>> resultHandler);

	/**
	 * Funcion que actualiza un ente creando un nuevo documento, desactivando los ultimos documentos
	 * del ente que se encuentre en el historico
	 * 
	 * @param ente: JsonObject Ente con informacion modificada que se agregara como un documento
	 * nuevo pero dentro del historico
	 * @param resultHandler: Handler<> handler para la respuesta de tipo Void
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 19/02/2019
	 */
	@Fluent
	EnteMongoService actualizarEnte(JsonObject ente, Handler<AsyncResult<String>> resultHandler);

	/**
	 * Funcion que consulta los entes que se encuentran activos de forma dinamica de acuerdo a los
	 * parametros encontrados
	 * 
	 * @param condiciones: JsonObject ParamConsultaEnteDTO con los parametros agregados por el usuario,
	 * si hay parametros se mostraran todos
	 * @param resultHandler: Handler<> handler para la respuesta de tipo List<JsonObject> con la lista
	 * de entes
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 19/02/2019
	 */
	@Fluent
	EnteMongoService consultarEntes(JsonObject condiciones, 
			Handler<AsyncResult<List<JsonObject>>> resultHandler);

	/**
	 * Funcion que consulta los el historial completo de un ente cuando se le envia cualquiera de los
	 * entes dentro de la historia
	 * 
	 * @param idEnteOrigen: String id origen que corresponde a todos los miembros de un historico
	 * @param resultHandler: Handler<> handler para la respuesta de tipo List<JsonObject> con la lista
	 * de entes
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 19/02/2019
	 */
	@Fluent
	EnteMongoService consultarHistorialEntePorEnte(String idEnteOrigen, 
			Handler<AsyncResult<List<JsonObject>>> resultHandler);

	/**
	 * Funcion que obtiene un ente por su id
	 * 
	 * @param idEnte: String Id del ente que se quiere consultar
	 * @param resultHandler: Handler<>  handler para la respuesta de tipo JsonObject con el ente
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 25/02/2019
	 */	
	@Fluent
	public EnteMongoService consultarEntePorId(String idEnte, Handler<AsyncResult<JsonObject>> resultHandler);

	/**
	 * Funcion para desactivar un ente(pasar el valor de datosDeControl.activo a 0), tambien puede desactivar
	 * el historico completo de un ente
	 * 
	 * @param desactivarHistorico: boolean
	 * 		true: desactivara todo el historico a partir del idEnteOrigen
	 * 		false: solo desactivara un ente
	 * @param id: String  id con el idEnte o idEnteOrigen
	 * @param resultHandler: Handler<> handler para la respuesta
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 03/04/2019
	 */
	@Fluent
	public EnteMongoService desactivarEntePorId(boolean desactivarHistorico, String id,
			Handler<AsyncResult<Void>> resultHandler);

	/**
	 * Funcion para definir la situacion del ente
	 * 
	 * @param idEnte: String id con el idEnte
	 * @param situacion: Situacion del ente
	 * @param resultHandler: Handler<> handler para la respuesta
	 * 
	 * @author pavel.martinez
	 * @since 23/04/2020
	 */	
	@Fluent
	public EnteMongoService definirSituacionEnte(String idEnte, Situacion situacion,
												 Handler<AsyncResult<Void>> resultHandler) ;

    @ProxyIgnore
    void close();

}
