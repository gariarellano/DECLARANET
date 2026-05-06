/* 
 * Sistema Propiedad de la Secretaría de la Función Pública DGTI
 * "VerificarDeclaraciones" Servicio que permite verificar si 
 * existen declaraciones en proceso o insertar el tipo de declaración a presentar
 * 
 * Proyecto desarrollado por programador04@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.daopxy;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mx.gob.sfp.dgti.declaracion.dto.general.DatosGeneralesDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.DatosPersonalesDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CorreoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.TelefonoDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoFormatoDeclaracion;
import static mx.gob.sfp.dgti.util.Constantes.PROP_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ID_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_TIPO_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ANIO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_FECHA_ENCARGO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_COLL_NAME;
import static mx.gob.sfp.dgti.util.Constantes.PROP_TIPO_FORMATO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_NIVEL_JERARQUICO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_VALOR_UNO_NIVEL_JERARQUICO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_NUM_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_FECHA_REGISTRO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_FORMATO_FECHA_MONGO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_FECHA_ACTUALIZACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ENCABEZADO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_INSTITUCION_RECEPTORA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_FIRMADA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DATOS_PERSONALES;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MODIFICACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ID;
import static mx.gob.sfp.dgti.util.Constantes.PROP_TELEFONO_CASA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_PAIS_CELULAR_PERSONAL;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DATOS_GENERALES;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DECLARANTE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ID_USR_DECNET;
import static mx.gob.sfp.dgti.util.Constantes.PROP_FECHA_RECEPCION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_TIPO_DECLARACION_NOTA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_REGISTRO;
import static mx.gob.sfp.dgti.util.Constantes.FORMATO_SIMPLIFICADO_INICIAL;

/**
 * Clase que implemnta la interface DAODeclaracionAPresentarInterface Clase que
 * contiene los métodos uilizados para la manipulación de información en la base
 * de datos.
 *
 * @author programador04@sfp.gob.mx
 * @since 29/10/2019
 */
public class DAODeclaracionAPresentar implements DAODeclaracionAPresentarInterface {
	private static final Logger LOGGER = LoggerFactory.getLogger(DAODeclaracionAPresentar.class);

	private static final String COLLECTION_DECLA_TEMP = "declaracionTemp";
	private static final String COLLECTION_RECEPCION_WEB = "recepcionWeb";
	
	
	//validaciones en los querys
	private static final String ID_USR_DNET = "declarante.idUsrDecnet";
	private static final String DECLARACION_TIPO_DECLARACION = "declaracion.tipoDeclaracion";
	private static final String NUM_DECLARACION_ORIGEN_NOTA = "declaracion.declaracionOrigen.encabezado.numeroDeclaracion";
	private static final String DECLARACION_ANIO = "declaracion.anio";
	private static final String DECLARACION_FECHA_ENCARGO = "declaracion.fechaEncargo";
	private static final String ENCABEZADO_USUARIO_IDUSUARIO = "encabezado.usuario.idUsuario";
	//private static final String ENCABEZADO_TIPO_DECLARACION = "encabezado.tipoDeclaracion";
	private static final String DECLARACION_NUM_DECLA = "declaracion.numeroDeclaracion";
	private static final String DECLARACION_FECHA_RECEP = "declaracion.fechaRecepcion";
	private static final String INSTI_RECEP_COLLNAME = "institucionReceptora.collName";
	
	
	/**
	 * Cliente de MongoDB
	 */
	private final MongoClient mongoClient;
	/**
	 * Constructor que recibe un objeto vertx y el json de la configuración para la
	 * creacion la conexión a la bd
	 * 
	 * @param vertx  propiedad que alacena el objeto vertx
	 * @param configuracion propiedad tipo JsonObject que contiene la configuración de la
	 *               base de datos
	 */
	public DAODeclaracionAPresentar(Vertx vertx, JsonObject configuracion) {
		mongoClient = MongoClient.createShared(vertx, configuracion);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Future<Boolean> verificarDeclaracionFirmada(JsonObject encabezado, JsonObject institucionReceptora) {
		Future<Boolean> existe = Future.future();
		JsonObject validaciones = new JsonObject()
                                .put(ID_USR_DNET, encabezado.getJsonObject(PROP_USUARIO).getInteger(PROP_ID_USUARIO))
				.put(DECLARACION_TIPO_DECLARACION, encabezado.getString(PROP_TIPO_DECLARACION))
				.put(DECLARACION_ANIO, encabezado.getInteger(PROP_ANIO))
				.put(DECLARACION_FECHA_ENCARGO, encabezado.getString(PROP_FECHA_ENCARGO));
		mongoClient.find(COLLECTION_RECEPCION_WEB+institucionReceptora.getInteger(PROP_COLL_NAME), validaciones, res -> {
			if (res.succeeded()) {
				existe.handle(Future.succeededFuture((!res.result().isEmpty())));
			} else {
				LOGGER.error(res.cause());
				existe.handle(Future.failedFuture(res.cause()));
			}
		});
		
		return existe;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Future<List<JsonObject>> verificarDeclaracionEnProceso(JsonObject institucionReceptora, Integer idUsuario) {
		Future<List<JsonObject>> existe = Future.future();
		JsonObject validacion = new JsonObject().put(ENCABEZADO_USUARIO_IDUSUARIO, idUsuario);
		mongoClient.find(COLLECTION_DECLA_TEMP+institucionReceptora.getInteger(PROP_COLL_NAME), validacion, res -> {
			if (res.succeeded()) {
				existe.handle(Future.succeededFuture(res.result()));
			} else {
				LOGGER.error(res.cause());
				existe.handle(Future.failedFuture(res.cause()));
			}
		});
		return existe;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Future<JsonObject> crearNuevaDeclaracion(JsonObject institucionReceptora, JsonObject encabezado, JsonObject datosGenerales) {
		if(EnumTipoDeclaracion.AVISO.name().equals(encabezado.getString(PROP_TIPO_DECLARACION))) {
			encabezado.put(PROP_TIPO_FORMATO, EnumTipoFormatoDeclaracion.COMPLETO.name());
		}else {
			encabezado.put(PROP_TIPO_FORMATO, FORMATO_SIMPLIFICADO_INICIAL.equals(encabezado.getJsonObject(PROP_NIVEL_JERARQUICO).getString(PROP_VALOR_UNO_NIVEL_JERARQUICO)) ? EnumTipoFormatoDeclaracion.SIMPLIFICADO.name() : EnumTipoFormatoDeclaracion.COMPLETO.name());
		}
		
		Future<JsonObject> declaracionTemp = Future.future();
					encabezado.putNull(PROP_NUM_DECLARACION);
					encabezado.put(PROP_FECHA_REGISTRO, new SimpleDateFormat(PROP_FORMATO_FECHA_MONGO).format(new Date()));
					encabezado.put(PROP_FECHA_ACTUALIZACION, new SimpleDateFormat(PROP_FORMATO_FECHA_MONGO).format(new Date()));
					JsonObject registro = new JsonObject()
							.put(PROP_ENCABEZADO, encabezado).put(PROP_INSTITUCION_RECEPTORA, institucionReceptora)
							.put(PROP_FIRMADA, false).put(PROP_DECLARACION, crearModuloDatosGenerales(datosGenerales)).put(PROP_DATOS_PERSONALES, datosGenerales);
					declaracionTemp.handle(Future.succeededFuture(registro));
		return declaracionTemp;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Future<List<JsonObject>> obtenerListaDeclaModificacionFirmadas(JsonObject encabezado, JsonObject institucionReceptora) {
		Future<List<JsonObject>> existe = Future.future();
		JsonObject validacion = new JsonObject().put(ID_USR_DNET, encabezado.getJsonObject(PROP_USUARIO).getInteger(PROP_ID_USUARIO))
				.put(DECLARACION_TIPO_DECLARACION, PROP_MODIFICACION);
		mongoClient.find(COLLECTION_RECEPCION_WEB+institucionReceptora.getInteger(PROP_COLL_NAME), validacion, res -> {
			if (res.succeeded()) {
				existe.handle(Future.succeededFuture(res.result()));
			} else {
				LOGGER.error(res.cause());
				existe.handle(Future.failedFuture(res.cause()));
			}
		});
		return existe;
	}

	/**
	 *{@inheritDoc} 
	 */
	@Override
	public Future<Boolean> eliminarDeclaracionTemp(JsonObject encabezado, JsonObject institucionReceptora) {
		Future<Boolean> eliminar = Future.future();
		JsonObject validacion = new JsonObject()
				.put(PROP_ID, encabezado.getString(PROP_NUM_DECLARACION));
		mongoClient.removeDocument(COLLECTION_DECLA_TEMP+institucionReceptora.getInteger(PROP_COLL_NAME), validacion, res -> {
			if (res.succeeded()) {
				eliminar.handle(Future.succeededFuture(true));
			} else {
				LOGGER.error(res.cause());
				eliminar.handle(Future.failedFuture(res.cause()));
			}
		});
		return eliminar;
	}

	public JsonObject crearModuloDatosGenerales(JsonObject datosUser) {
		DatosGeneralesDTO datosGeneralesDTO = new DatosGeneralesDTO();
		datosGeneralesDTO.setDatosPersonales(new DatosPersonalesDTO(datosUser));
		datosGeneralesDTO.setCorreoElectronico(new CorreoDTO());
		
		//inicializar datos para que queden como json
		datosGeneralesDTO.setTelefonoCasa(new TelefonoDTO());
		datosGeneralesDTO.setTelefonoCelular(new TelefonoDTO());
		datosGeneralesDTO.getTelefonoCelular().setPaisCelularPersonal(new CatalogoDTO());
		datosGeneralesDTO.setSituacionPersonalEstadoCivil(new CatalogoDTO());
		datosGeneralesDTO.setRegimenMatrimonial(new CatalogoDTO());
		datosGeneralesDTO.setPaisNacimiento(new CatalogoDTO());
		datosGeneralesDTO.setNacionalidad(new CatalogoDTO());
		
		
		JsonObject datosGrals = new JsonObject(Json.encode(datosGeneralesDTO));
		datosGrals.remove(PROP_ENCABEZADO);
		datosGrals.getJsonObject(PROP_TELEFONO_CASA).remove(PROP_PAIS_CELULAR_PERSONAL);
		return new JsonObject().put(PROP_DATOS_GENERALES, datosGrals);
		
	}

	@Override
	/**
	 *{@inheritDoc} 
	 */
	public Future<List<JsonObject>> obtenerHistorialDeclaracionesUsuario(JsonObject parametros) {
		Future<List<JsonObject>> existe = Future.future();
		JsonObject validacion = new JsonObject().put(ID_USR_DNET, parametros.getInteger(PROP_ID_USUARIO))
					.put("$nor", new JsonArray().add(new JsonObject().put(DECLARACION_TIPO_DECLARACION, PROP_TIPO_DECLARACION_NOTA)));
						
		FindOptions options = new FindOptions().setFields(new JsonObject()
				.put(ID_USR_DNET,1)
				.put(DECLARACION_NUM_DECLA,1)
				.put(INSTI_RECEP_COLLNAME,1)
				.put(DECLARACION_TIPO_DECLARACION,1)
				.put(DECLARACION_FECHA_ENCARGO,1)
				.put(DECLARACION_ANIO,1)
				.put(DECLARACION_FECHA_RECEP,1));
		options.setSort(new JsonObject()
				.put(DECLARACION_FECHA_RECEP,-1));
		mongoClient.findWithOptions(COLLECTION_RECEPCION_WEB+parametros.getJsonObject(PROP_INSTITUCION_RECEPTORA).getInteger(PROP_COLL_NAME), validacion, options, res -> {
			if (res.succeeded()) {
				existe.handle(Future.succeededFuture(generarJsonHistorial(res.result())));
			} else {
				LOGGER.error(res.cause());
				existe.handle(Future.failedFuture(res.cause()));
			}
		});
		return existe;
	}
	
	@Override
	/**
	 *{@inheritDoc} 
	 */
	public Future<List<JsonObject>> obtenerHistorialNotasPorDeclaracion(JsonObject parametros) {
		Future<List<JsonObject>> historial = Future.future();
		JsonObject validacion = new JsonObject().put(ID_USR_DNET, parametros.getJsonObject(PROP_REGISTRO).getInteger(PROP_ID_USUARIO))
				.put(NUM_DECLARACION_ORIGEN_NOTA, parametros.getJsonObject(PROP_REGISTRO).getString(PROP_NUM_DECLARACION));
		FindOptions options = new FindOptions().setFields(new JsonObject()
				.put(ID_USR_DNET,1)
				.put(DECLARACION_NUM_DECLA,1)
				.put(INSTI_RECEP_COLLNAME,1)
				.put(DECLARACION_FECHA_RECEP,1));
		options.setSort(new JsonObject()
				.put(DECLARACION_FECHA_RECEP,-1));
		mongoClient.findWithOptions(COLLECTION_RECEPCION_WEB+parametros.getJsonObject(PROP_INSTITUCION_RECEPTORA).getInteger(PROP_COLL_NAME), validacion, options, res -> {
			if (res.succeeded()) {
				historial.handle(Future.succeededFuture(generarJsonHistorialNotas(res.result())));
			} else {
				LOGGER.error(res.cause());
				historial.handle(Future.failedFuture(res.cause()));
			}
		});
		
		return historial;
	}
	
	public List<JsonObject> generarJsonHistorial(List<JsonObject> histoData) {
		List<JsonObject> historial = new ArrayList<>();
		for(JsonObject data : histoData) {
			historial.add(new JsonObject()
					.put(PROP_ID_USUARIO, data.getJsonObject(PROP_DECLARANTE).getInteger(PROP_ID_USR_DECNET))
					.put(PROP_NUM_DECLARACION, data.getJsonObject(PROP_DECLARACION).getString(PROP_NUM_DECLARACION))
					.put(PROP_FECHA_RECEPCION, data.getJsonObject(PROP_DECLARACION).getString(PROP_FECHA_RECEPCION).substring(0, 10))
					.put(PROP_COLL_NAME, data.getJsonObject(PROP_INSTITUCION_RECEPTORA).getInteger(PROP_COLL_NAME))
					.put(PROP_TIPO_DECLARACION, data.getJsonObject(PROP_DECLARACION).getString(PROP_TIPO_DECLARACION))
					.put(PROP_FECHA_ENCARGO, data.getJsonObject(PROP_DECLARACION).getString(PROP_FECHA_ENCARGO))
					.put(PROP_ANIO, data.getJsonObject(PROP_DECLARACION).getInteger(PROP_ANIO)));
		}
		return historial;
	}

	public List<JsonObject> generarJsonHistorialNotas(List<JsonObject> histoData) {
		List<JsonObject> historial = new ArrayList<>();
		for(JsonObject data : histoData) {
			historial.add(new JsonObject()
					.put(PROP_ID_USUARIO, data.getJsonObject(PROP_DECLARANTE).getInteger(PROP_ID_USR_DECNET))
					.put(PROP_NUM_DECLARACION, data.getJsonObject(PROP_DECLARACION).getString(PROP_NUM_DECLARACION))
					.put(PROP_COLL_NAME, data.getJsonObject(PROP_INSTITUCION_RECEPTORA).getInteger(PROP_COLL_NAME))
					.put(PROP_FECHA_RECEPCION, data.getJsonObject(PROP_DECLARACION).getString(PROP_FECHA_RECEPCION).substring(0, 10)));
		}
		return historial;
	}
	
	
}
