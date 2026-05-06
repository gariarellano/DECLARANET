package mx.gob.sfp.dgti.daopxy;

import static mx.gob.sfp.dgti.util.Constantes.PIPELINE_BUSQUEDA_DATOS_PUBLICOS;
import static mx.gob.sfp.dgti.util.Constantes.PIPELINE_BUSQUEDA_HISTORIAL;
import static mx.gob.sfp.dgti.util.Constantes.PIPELINE_ENCABEZADO_HISTORIAL;
import static mx.gob.sfp.dgti.util.Constantes.PROP_AGGREGATE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_CURP;
import static mx.gob.sfp.dgti.util.Constantes.PROP_CURSOR;
import static mx.gob.sfp.dgti.util.Constantes.PROP_FIRST_BATCH;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ID_USR_DECNET;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MATCH;
import static mx.gob.sfp.dgti.util.Constantes.PROP_PIPELINE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_RFC;
import static mx.gob.sfp.dgti.util.Constantes.PROP_SEARCH;
import static mx.gob.sfp.dgti.util.Constantes.PROP_TEXT;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;
import static mx.gob.sfp.dgti.util.Constantes.PROP_PROJECT;

public class DAOConsultaHistorialImpl implements DAOConsultaHistorialInterface {
	private static final Logger LOGGER = LoggerFactory.getLogger(DAOConsultaHistorialImpl.class);
	private final MongoClient mongoClient;
	
	private static final String COLLECTION_DATOS_PUBLICOS = "datosPublicos";
	private static final String COLLECTION_RECEPCION_WEB = "recepcionWeb";
	private static final String COLLECTION_BITACORA_OPER = "bitacoraOper";
	private static final String ID_USR_DNET = "declarante.idUsrDecnet";
	
	private static final String FORMATO_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.S";
	
	public DAOConsultaHistorialImpl(Vertx vertx, JsonObject configuracion) {
		mongoClient = MongoClient.createShared(vertx, configuracion);
	}

	@Override
	public Future<JsonObject> busquedaHistorialIdUsuario(String datoCapturado, Integer opcion, Integer collName) {
		return Future.future(historial->{
			if(opcion == null) {
				historial.fail("No hay  opción para consultar el historial");
			}else {
				obtenerEncabezado(datoCapturado, opcion, collName).setHandler(idUsu ->{
						if(idUsu.succeeded()) {
							JsonArray pipe = PIPELINE_BUSQUEDA_HISTORIAL.copy();
							 pipe.getJsonObject(0).getJsonObject(PROP_MATCH).put( ID_USR_DNET, idUsu.result().getInteger(PROP_ID_USR_DECNET));
							
							 JsonObject agg = new JsonObject();
							 agg.put(PROP_AGGREGATE, COLLECTION_RECEPCION_WEB+collName);
								agg.put(PROP_PIPELINE, pipe);
								agg.put(PROP_CURSOR,  new JsonObject());

							mongoClient.runCommand(PROP_AGGREGATE, agg, histo->{
								if(histo.succeeded()) {
									JsonObject his = new  JsonObject().put("cabecera", idUsu.result()).put("historial", histo.result().getJsonObject(PROP_CURSOR).getJsonArray(PROP_FIRST_BATCH));
									historial.handle(Future.succeededFuture(his));
								}else {
									LOGGER.error("Error al obtener el historial: "+histo.cause().toString());
									historial.handle(Future.failedFuture(histo.cause().toString()));
								}
							});
						}else {
							LOGGER.error("Error al obtener el historial: "+idUsu.cause().toString());
							 historial.handle(Future.failedFuture(idUsu.cause().toString()));
						}
					});
			}
		});
	}
	

	@Override
	public Future<Boolean> guardarBitacoraOPER(JsonObject params, boolean exitoso, Integer collName) {
		params.put("fechaConsulta", new SimpleDateFormat(FORMATO_ISO_8601).format(new Date()));
		params.put("exitoSolicitud", exitoso);
		return Future.future( guar-> 
			mongoClient.save(COLLECTION_BITACORA_OPER + collName, params, save -> {
				if (save.succeeded()) {
					guar.handle(Future.succeededFuture(Boolean.TRUE));
				} else {
					LOGGER.error(save.cause());
					guar.handle(Future.failedFuture(save.cause().toString()));
				}
			})
		);
	}

	@Override
	public Future<JsonArray> busquedaPorNombreOrfc(String nombre, String rfc, Integer collName) {
		 JsonArray pipe = PIPELINE_BUSQUEDA_DATOS_PUBLICOS.copy();
                 if (nombre != null ){
                    pipe.getJsonObject(0).getJsonObject(PROP_MATCH).put(PROP_TEXT, new JsonObject().put(PROP_SEARCH, nombre));                    
                 }else{
                     pipe.getJsonObject(0).getJsonObject(PROP_MATCH).put("rfc", new JsonObject().put("$regex", rfc+".*").put("$options","m"));
                     pipe.getJsonObject(3).getJsonObject(PROP_PROJECT).remove("score");
                 }
		 pipe.getJsonObject(1).getJsonObject("$lookup").put("from", "reservaSN"+collName);
		 
		 JsonObject agg = new JsonObject();
		 agg.put(PROP_AGGREGATE, COLLECTION_DATOS_PUBLICOS.concat(collName.toString()));
		 agg.put(PROP_PIPELINE, pipe);
		 agg.put(PROP_CURSOR,  new JsonObject());
		return  Future.future(busqu ->
			 mongoClient.runCommand(PROP_AGGREGATE, agg, rc -> {
				 if (rc.succeeded()) {
					 busqu.handle(Future.succeededFuture(rc.result().getJsonObject(PROP_CURSOR).getJsonArray(PROP_FIRST_BATCH)));
				 } else {
					 LOGGER.error(String.format("error al consultar datos: %s", rc.cause().toString()));
					 busqu.handle(Future.failedFuture(rc.cause().toString()));

				 }
			 })
		 );
	}
	
	public Future<JsonObject> obtenerEncabezado(String datoCapturado, Integer opcion, Integer collName) {
		
		JsonArray pipe = PIPELINE_ENCABEZADO_HISTORIAL.copy();
		
		pipe.getJsonObject(0).put(PROP_MATCH, new JsonObject().put(parametroParaConsulta(opcion), opcion == 3 ? Integer.parseInt(datoCapturado) :datoCapturado));
		pipe.getJsonObject(1).getJsonObject("$lookup").put("from", "reservaSN"+collName);
		 
		 JsonObject agg = new JsonObject();
		 agg.put(PROP_AGGREGATE, COLLECTION_DATOS_PUBLICOS.concat(collName.toString()));
		 agg.put(PROP_PIPELINE, pipe);
		 agg.put(PROP_CURSOR,  new JsonObject());
		 
		return  Future.future(busqu ->
			 mongoClient.runCommand(PROP_AGGREGATE, agg, rc -> {
				 if (rc.succeeded() ) {
					 if(!rc.result().getJsonObject(PROP_CURSOR).getJsonArray(PROP_FIRST_BATCH).getList().isEmpty()) {
						 busqu.handle(Future.succeededFuture((JsonObject)rc.result().getJsonObject(PROP_CURSOR).getJsonArray(PROP_FIRST_BATCH).getList().get(0)));
					 }else {
						 LOGGER.error("no hay datos datos para mostrar");
						 busqu.handle(Future.failedFuture("no hay datos datos para mostrar"));
					 }
						 
				} else {
					 LOGGER.error(String.format("error al consultar dato encabezado: %s", rc.cause().toString()));
					 busqu.handle(Future.failedFuture(rc.cause().toString()));
				 }
			 })
		 );
	}
	
	public String parametroParaConsulta(int opcion) {
		if(opcion ==1) {
			return PROP_CURP;
		}else if(opcion == 2) {
			return PROP_RFC;
		}else {
			return PROP_ID_USR_DECNET;
		}
	}
	
}
