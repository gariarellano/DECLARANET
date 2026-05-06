/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "EnteReceptorDecla" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.dao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import mx.gob.sfp.dgti.dao.interfaces.DAOConsultDeclaInterface;

/**
 * Clase que contiene la implementacion de los métodos para la interaccion con
 * la base y consulta de informacion de entes receptores de información.
 *
 * @author cgarias
 * @since 06/11/2019
 * @edited by programador04 23/12/2019
 */
public class DAOConsultDecla implements DAOConsultDeclaInterface {
    private final MongoClient mongoClient;
    private static final String FIELD_COLLECTION_NAME = "collName";
    private static final String QUERY_ID = "_id";
    private static final String COLLECTION_DECLARACION_TEMP = "declaracionTemp";
    private static final String COLLECTION_DECLARACION = "declaracion";
    private static final String COLLECTION_RECEPCION_WEB = "recepcionWeb";
    private static final String COLLECTION_ENTE_RECEPTOR_DECLA = "enteReceptorDeclaracion";
    private static final String COLLECTION_DECLARACION_HTML = "declaracionHtml";
    private static final String POOL_ENTES_RECEPTOR = "pool-entes-receptor";
    private static final String FIELD_ENCABEZADO = "encabezado";
    private static final String FIELD_ENTE = "ente";
    private static final String FIELD_PREFIJO = "prefijo";
    private static final String FIELD_ID_USUARIO = "idUsuario";
    private static final String FIELD_NUMERO_DECLARACION = "numeroDeclaracion";
    private static final String FIELD_ID = "_id";
    private static final String FIELD_IMG_INST_B64 = "imagenInstitucionalB64";
    private static final String FIELD_IMG_OFICIAL_B64 = "imagenOficialB64";
    private static final String PROPERTY_COLL_NAME = "collName";
    private static final String PROPERTY_ACTIVO = "activo";
    private static final String QUERY_ID_USUARIO_RECEP_WEB = "declarante.idUsrDecnet";
    private static final String QUERY_RECEPCION_WEB_NO_COMPROBANTE = "declaracion.noComprobante";
    private static final String QUERY_RECEPCION_WEB_FECHA_RECEP = "declaracion.fechaRecepcion";
    private static final String QUERY_RECEPCION_WEB_TIPO_DECLA = "declaracion.tipoDeclaracion";
    private static final String QUERY_RECEPCION_WEB_NO_DECLA = "declaracion.numeroDeclaracion";
    private static final String QUERY_RECEPCION_WEB_TIPO_FORMATO = "declaracion.tipoFormato";
    private static final String QUERY_RECEPCION_WEB_ANIO = "declaracion.anio";
    private static final String QUERY_RECEPCION_WEB_FECHA_ENCARGO = "declaracion.fechaEncargo";
    private static final String QUERY_RECEPCION_DIGESTION = "firmaDeclaracion.digestionDcn";
    private static final String QUERY_RECEPCION_NOMBRE_FIRM = "firmaDeclaracion.nombreFirm";
    private static final String QUERY_RECEPCION_FOLIO = "firmaDeclaracion.folio";
    private static final String QUERY_RECEPCION_TRANSACCION = "firmaDeclaracion.transaccion";
    private static final String QUERY_RECEPCION_NUMERO_CERTIFICADO = "firmaDeclaracion.numeroCertificado";
    private static final String QUERY_RECEPCION_NOMBRE = "declarante.nombre";
    private static final String QUERY_RECEPCION_RFC = "declarante.rfc";
    private static final String QUERY_RECEPCION_CORREO_INSTITUCIONAL = "declarante.correoElectronico";
    private static final String QUERY_RECEPCION_CURP = "declarante.curp";
    private static final String QUERY_RECEPCION_DIGES_ACUSE = "firmaAcuse.digestionAcuse";
    private static final String QUERY_RECEPCION_PUESTO_FIRMANTE = "firmaAcuse.puestoFirmante";
    private static final String QUERY_RECEPCION_ID_FIRMANTE = "firmaAcuse.idFirmante";
    private static final String QUERY_RECEPCION_FIRMA_ACUSE = "firmaAcuse.firmaAcuse";
    private static final String QUERY_RECEPCION_TITULO_FIRMANTE = "firmaAcuse.tituloFirmante";
    private static final String QUERY_RECEPCION_NOMBRE_FIRMANTE = "firmaAcuse.nombreFirmante";
    private static final String PROP_TIPO_DECLARACION_NOTA = "NOTA";
    private static final String QUERY_INSTI_RECEP_COLLNAME = "institucionReceptora.collName";

    public DAOConsultDecla(Vertx vertx, JsonObject jsonConfig) {
        mongoClient = MongoClient.createShared(vertx, jsonConfig, POOL_ENTES_RECEPTOR);
    }

    @Override
    public DAOConsultDeclaInterface consultaDeclaracion(JsonObject parametrosDeclaracion, boolean isTemp,
            Handler<AsyncResult<JsonObject>> resultHandler) {
        String coleccion = (isTemp ? COLLECTION_DECLARACION_TEMP : COLLECTION_DECLARACION)
                + parametrosDeclaracion.getInteger(FIELD_COLLECTION_NAME);
        JsonObject query = new JsonObject().put(QUERY_ID, parametrosDeclaracion.getString(FIELD_NUMERO_DECLARACION));

        mongoClient.findOne(coleccion, query, null, result -> {
            if (result.succeeded()) {
                if (result.result() != null) {
                    JsonObject declaracion = result.result();
                    declaracion.getJsonObject(FIELD_ENCABEZADO).put(FIELD_NUMERO_DECLARACION,
                            declaracion.getString(FIELD_ID));
                    declaracion.remove(FIELD_ID);
                    resultHandler.handle(Future.succeededFuture(declaracion));
                } else {
                    resultHandler.handle(Future.succeededFuture(new JsonObject()));
                }
            } else {
                resultHandler.handle(Future.failedFuture(result.cause()));
            }
        });
        return this;
    }
    
    @Override
    public DAOConsultDeclaInterface consultaDeclaracionHtml(JsonObject parametrosDeclaracion, Handler<AsyncResult<JsonObject>> localizada) {
        mongoClient.findOne(
                COLLECTION_DECLARACION_HTML.concat(parametrosDeclaracion.getInteger(FIELD_COLLECTION_NAME).toString()), 
                new JsonObject().put(FIELD_ID, parametrosDeclaracion.getString(FIELD_NUMERO_DECLARACION)).put(PROPERTY_ACTIVO, Boolean.TRUE), 
                new JsonObject(), 
                localiza->{
                    localizada.handle(localiza);
                });
        return this;
    }

    @Override
    public DAOConsultDeclaInterface consultaEnteReceptor(Integer collName, boolean isTemp,
            Handler<AsyncResult<JsonObject>> resultHandler) {
        JsonObject query = new JsonObject();
        JsonObject fields = new JsonObject();
        if (isTemp) {
            fields.put(FIELD_PREFIJO, 1);
        } else {
            fields.put(FIELD_ENTE, 1)
                    .put(FIELD_PREFIJO, 1)
                    .put(FIELD_IMG_INST_B64, 1).put(FIELD_IMG_OFICIAL_B64, 1);
        }

        query.put(PROPERTY_COLL_NAME, collName);
        query.put(PROPERTY_ACTIVO, true);
        mongoClient.findOne(COLLECTION_ENTE_RECEPTOR_DECLA, query, fields, result -> {
            if (result.succeeded()) {
                JsonObject resul = result.result();
                if (resul != null) {
                    resul.remove(FIELD_ID);
                }

                resultHandler.handle(Future.succeededFuture(resul));
            } else {
                resultHandler.handle(Future.failedFuture(result.cause()));
            }
        });
        return this;
    }
    
    @Override
    public DAOConsultDeclaInterface consultaFirmante(JsonObject parametros, Handler<AsyncResult<JsonObject>> resultHandler){
        JsonObject query = new JsonObject().put(QUERY_ID, parametros.getString("idFirmante"));
        JsonObject fields =new JsonObject().put("texto",1);
        mongoClient.findOne("firmante"+parametros.getInteger(PROPERTY_COLL_NAME), query, fields, result -> {
            if (result.succeeded()) {
                resultHandler.handle(Future.succeededFuture(result.result()));
            } else {
                resultHandler.handle(Future.failedFuture(result.cause()));
            }
        });
        
        return this;
    }

    @Override
    public DAOConsultDeclaInterface consultaRecepcionWeb(JsonObject parametrosDeclaracion,
            Handler<AsyncResult<JsonObject>> resultHandler) {
        JsonObject query = new JsonObject();
        JsonObject fields = new JsonObject().put(QUERY_RECEPCION_WEB_NO_COMPROBANTE, 1).put(QUERY_RECEPCION_WEB_FECHA_RECEP, 1)
                .put(QUERY_RECEPCION_WEB_TIPO_DECLA, 1).put(QUERY_RECEPCION_WEB_NO_DECLA, 1)
                .put(QUERY_RECEPCION_WEB_TIPO_FORMATO, 1)
                .put(QUERY_RECEPCION_WEB_ANIO, 1).put(QUERY_RECEPCION_WEB_FECHA_ENCARGO, 1)
                .put(QUERY_RECEPCION_DIGESTION, 1).put(QUERY_RECEPCION_NOMBRE_FIRM, 1)
                .put(QUERY_RECEPCION_FOLIO, 1).put(QUERY_RECEPCION_TRANSACCION, 1).put(QUERY_RECEPCION_NUMERO_CERTIFICADO, 1)
                .put(QUERY_RECEPCION_NOMBRE, 1).put(QUERY_RECEPCION_RFC, 1).put(QUERY_RECEPCION_CORREO_INSTITUCIONAL, 1)
                .put(QUERY_RECEPCION_CURP, 1).put(QUERY_RECEPCION_DIGES_ACUSE, 1).put(QUERY_RECEPCION_PUESTO_FIRMANTE, 1)
                .put(QUERY_RECEPCION_FIRMA_ACUSE, 1).put(QUERY_RECEPCION_TITULO_FIRMANTE, 1)
                .put(QUERY_RECEPCION_NOMBRE_FIRMANTE, 1).put(QUERY_RECEPCION_ID_FIRMANTE,1);
        query.put(QUERY_RECEPCION_WEB_NO_DECLA, parametrosDeclaracion.getString(FIELD_NUMERO_DECLARACION));
        query.put(QUERY_ID_USUARIO_RECEP_WEB, parametrosDeclaracion.getInteger(FIELD_ID_USUARIO));
        mongoClient.findOne(COLLECTION_RECEPCION_WEB + parametrosDeclaracion.getInteger(PROPERTY_COLL_NAME), query, fields,
                result -> {
                    if (result.succeeded()) {
                        if (result.result() == null) {
                            resultHandler.handle(Future.succeededFuture(result.result()));
                        } else {
                            JsonObject resul = result.result();
                            resul.remove(FIELD_ID);
                            resultHandler.handle(Future.succeededFuture(resul));
                        }
                    } else {
                        resultHandler.handle(Future.failedFuture(result.cause()));
                    }
                });
        return this;
    }

    @Override
    public DAOConsultDeclaInterface consultaHistorialUsuario(JsonObject parametros, Handler<AsyncResult<List<JsonObject>>> resultHandler) {
        
        JsonObject validacion = new JsonObject().put(QUERY_ID_USUARIO_RECEP_WEB, parametros.getInteger(FIELD_ID_USUARIO))
					.put("$nor", 
                                                new JsonArray()
                                                        .add(new JsonObject().put(QUERY_RECEPCION_WEB_TIPO_DECLA, PROP_TIPO_DECLARACION_NOTA))
                                                        .add(new JsonObject().put(QUERY_RECEPCION_WEB_TIPO_DECLA, "AVISO"))
                                        );
        FindOptions etiquetasYorden 
                = new FindOptions()
                        .setFields(new JsonObject()
				.put(QUERY_ID_USUARIO_RECEP_WEB,1)
				.put(QUERY_RECEPCION_WEB_NO_DECLA,1)
				.put(QUERY_INSTI_RECEP_COLLNAME,1)
				.put(QUERY_RECEPCION_WEB_TIPO_DECLA,1)
				.put(QUERY_RECEPCION_WEB_FECHA_ENCARGO,1)
				.put(QUERY_RECEPCION_WEB_ANIO,1)
				.put(QUERY_RECEPCION_WEB_FECHA_RECEP,1));
		etiquetasYorden
                        .setSort(new JsonObject()
				.put(QUERY_RECEPCION_WEB_FECHA_RECEP,-1));
                
        mongoClient.findWithOptions(COLLECTION_RECEPCION_WEB+parametros.getInteger(PROPERTY_COLL_NAME), validacion, etiquetasYorden, historial -> {
                if (historial.succeeded()) {
                    resultHandler.handle(Future.succeededFuture(historial.result()));
                } else {                        
                    resultHandler.handle(Future.failedFuture(historial.cause()));
                }
        });
        return this;
    }
}
