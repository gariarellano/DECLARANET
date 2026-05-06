/**
 * @(#)ApplicationService.java 02/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.applicationservice;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.ProxyIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import mx.gob.sfp.dgti.constantes.Situacion;
import mx.gob.sfp.dgti.dto.EnteDTO;
import mx.gob.sfp.dgti.dto.MigracionDTO;
import mx.gob.sfp.dgti.dto.ParamConsultaEnteDTO;

import java.util.List;

/**
 * Interface para el ApplicationService
 * 
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 02/05/2019
 */
@ProxyGen
@VertxGen
public interface ApplicationService {

	static final String SERVICE_ADDRESS = "application-service";

	static ApplicationService create(Vertx vertx) {
		    return new ApplicationServiceImpl(vertx);
	}

	static ApplicationService createProxy(Vertx vertx, String address) {
		return new ApplicationServiceVertxEBProxy(vertx, address);
	}  

	/**
	 * Funcion que agregar un nuevo ente, el primero de su historia, es su propio padre
	 *
	 * @param ente: objeto EnteDTO que se va a agregar
	 * @param resultHandler: handler para la respuesta de tipo EnteDTO
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 02/05/2019
	 */
	@Fluent
	ApplicationService agregarEnteNuevo(EnteDTO ente, Handler<AsyncResult<EnteDTO>> resultHandler);

	/**
	 * Funcion que actualiza un ente ya sea solo modificando algunos campos o creando un nuevo documento
	 *
	 * @param ente: objeto EnteDTO que se va a actualizar
	 * @param resultHandler: handler para la respuesta de tipo EnteDTO
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 02/05/2019
	 */
	@Fluent
	ApplicationService actualizarEnte(EnteDTO ente, Handler<AsyncResult<EnteDTO>> resultHandler);

	/**
	 * Funcion que consulta los entes de forma dinamica de acuerdo a los parametros mandados
	 *
	 * @param parametros: parametros para la busqueda
	 * @param resultHandler: handler para la respuesta de tipo List<EnteDTO></EnteDTO>
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 02/05/2019
	 */
	@Fluent
	ApplicationService consultarEntes(ParamConsultaEnteDTO parametros, Handler<AsyncResult<List<EnteDTO>>> resultHandler);

	/**
	 * Funcion que consulta los el historial completo de un ente cuando se le envia le idEnteOrigen
	 *
	 * @param ente: objeto EnteDTO que se va a agregar
	 * @param resultHandler: handler para la respuesta de tipo List<EnteDTO>
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 02/05/2019
	 */
	@Fluent
	ApplicationService consultarHistorialEntePorEnte(String idEnteOrigen, Handler<AsyncResult<List<EnteDTO>>> resultHandler);

	/**
	 * Funcion para insertar varios entes nuevos
	 *
	 * @param migracion: objeto EnteDTO que se va a agregar
	 * @param resultHandler: handler para la respuesta de tipo Void
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 02/05/2019
	 */
	@Fluent
	ApplicationService agregarEntesNuevos(MigracionDTO migracion, Handler<AsyncResult<Void>> resultHandler);

	/**
	 * Funcion para desactivar unicamente un ente o el ente y su historial
	 *
	 * @param desactivarHistorico : true: desactivara todos los entes relacionados con el idEnteOrigen
	 *                            	false: desactivara unicamente el ente relacionado con el idEnte
	 * @param id: id mandado, ya sea idEnteOrigen o idEnte
	 * @param resultHandler: handler para la respuesta de tipo Void
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 02/05/2019
	 */
	@Fluent
	ApplicationService desactivarEnte(boolean desactivarHistorico, String id, Handler<AsyncResult<Void>> resultHandler);

	/**
	 * Funcion para definir la situacion
	 *
	 * @param idEnte: id del ente al que se le activara la transicion
	 * @param situacion: Enum con la situacion del ente
	 * @param resultHandler: handler para la respuesta de tipo EnteDTO
	 *
	 * @author pavel.martinez
	 * @since 23/04/2020
	 */
	@Fluent
	ApplicationService definirSituacionEnte(String idEnte, Situacion situacion,
												   Handler<AsyncResult<EnteDTO>> resultHandler) ;

    @ProxyIgnore
    void close();

}
