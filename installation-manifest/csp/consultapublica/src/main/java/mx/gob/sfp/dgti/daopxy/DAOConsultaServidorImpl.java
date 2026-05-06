package mx.gob.sfp.dgti.daopxy;

import static mx.gob.sfp.dgti.util.Constantes.PROP_COLL_NAME;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DECLARANTE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_FECHA_RECEPCION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ID_USR_DECNET;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ID_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_INSTITUCION_RECEPTORA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_NUM_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_TIPO_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_TIPO_DECLARACION_NOTA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_NUMERO_COMPROBANTE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MATCH;
import static mx.gob.sfp.dgti.util.Constantes.PROP_TEXT;
import static mx.gob.sfp.dgti.util.Constantes.PROP_SEARCH;
import static mx.gob.sfp.dgti.util.Constantes.PROP_SORT;
import static mx.gob.sfp.dgti.util.Constantes.PROP_PROJECT;
import static mx.gob.sfp.dgti.util.Constantes.PROP_AGGREGATE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_PIPELINE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_CURSOR;
import static mx.gob.sfp.dgti.util.Constantes.PROP_FIRST_BATCH;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ANIO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_AVISO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;
import static mx.gob.sfp.dgti.util.Constantes.PIPELINE_BUSQUEDA_DATOS_PUBLICOS;

public class DAOConsultaServidorImpl implements DAOConsultaServidorInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(DAOConsultaServidorImpl.class);
    private final MongoClient mongoClient;

    private static final String COLLECTION_RECEPCION_WEB = "recepcionWeb";
    private static final String COLLECTION_DATOS_PUBLICOS = "datosPublicos";
    private static final String ID_USR_DNET = "declarante.idUsrDecnet";
    private static final String NOMBRE_DECLARANTE_RECEP_WEB = "declarante.nombre";
    private static final String NOMBRE_DECLARANTE_RFC = "declarante.rfc";
    private static final String NOMBRE_DECLARANTE_CURP = "declarante.curp";
    private static final String DECLARACION_FECHA_RECEP = "declaracion.fechaRecepcion";
    private static final String DECLARACION_FECHA_ID = "declaracion.fechaId";
    private static final String DECLARACION_TIPO_DECLARACION = "declaracion.tipoDeclaracion";
    private static final String DECLARACION_NUM_DECLA = "declaracion.numeroDeclaracion";
    private static final String INSTI_RECEP_COLLNAME = "institucionReceptora.collName";
    private static final String INSTI_RECEP_ENTE_NOMBRE = "declarante.dependencia";
    private static final String INSTI_RECEP_ENTE_CARGO_COMISION = "declarante.enteCargoComision";
    private static final String DECLARACION_NUM_COMPROBANTE = "declaracion.noComprobante";
    private static final String DECLARACION_ANIO = "declaracion.anio";
    private static final String DECLARACION_FECHA_ENCARGO ="declaracion.fechaEncargo";
    private static final String $DECLARACION_FECHA_ENCARGO ="$declaracion.fechaEncargo";
    private static final String DECLARANTE_NOMBRE ="declarante.nombre";
    private static final String $DECLARACION_ANIO = "$declaracion.anio";
    private static final String $TOSTRING = "$toString";
    private static final String PROP_ELSE = "else";
    private static final String PROP_$IN = "$in";
    private static final String PROP_$NOR = "$nor";
    private static final String PROP_$COND = "$cond";
    private static final String PROP_IF = "if";
    private static final String PROP_$EQ ="$eq";
    private static final String PROP_THEN ="then";
    private static final String PROP_$CONCAT ="$concat";
    private static final String PROP_PRIMERO_MAYO ="-05-31";
    private static final HashMap<Integer, Object> GRUPOS_GABINETES = new HashMap<>();
   
    

    
    /**
     * Constructor que recibe un objeto vertx y el json de la configuración para
     * la creacion la conexión a la bd
     *
     * @param vertx propiedad que alacena el objeto vertx
     * @param configuracion propiedad tipo JsonObject que contiene la
     * configuración de la base de datos
     */
    public DAOConsultaServidorImpl(Vertx vertx, JsonObject configuracion) {
    	GRUPOS_GABINETES.put(1, Json.decodeValue(System.getenv("CURP_GRUPO1") != null ? System.getenv("CURP_GRUPO1") : "[000111]"));
    	GRUPOS_GABINETES.put(2, Json.decodeValue(System.getenv("CURP_GRUPO2") != null ? System.getenv("CURP_GRUPO2") : "[000111]"));
    	GRUPOS_GABINETES.put(3, Json.decodeValue(System.getenv("CURP_GRUPO3") != null ? System.getenv("CURP_GRUPO3") : "[000111]"));
    	GRUPOS_GABINETES.put(4, Json.decodeValue(System.getenv("CURP_GRUPO4") != null ? System.getenv("CURP_GRUPO4") : "[000111]"));
        mongoClient = MongoClient.createShared(vertx, configuracion);
    }
    

    @Override
    public Future<List<JsonObject>> buscarHistorialSP(Integer idUsr, Integer collName) {
        Future<List<JsonObject>> datos = Future.future();
        JsonObject validacion = new JsonObject().put(ID_USR_DNET, idUsr)
                .put("$nor", new JsonArray()
                        .add(new JsonObject().put(DECLARACION_TIPO_DECLARACION, PROP_TIPO_DECLARACION_NOTA))
//                        .add(new JsonObject().put(DECLARACION_TIPO_DECLARACION, "AVISO"))
                );

        FindOptions options = new FindOptions().setFields(new JsonObject()
                .put(ID_USR_DNET, 1)
                .put(DECLARACION_NUM_DECLA, 1)
                .put(INSTI_RECEP_ENTE_NOMBRE, 1)
                .put(INSTI_RECEP_ENTE_CARGO_COMISION, 1)
                .put(INSTI_RECEP_COLLNAME, 1)
                .put(DECLARACION_TIPO_DECLARACION, 1)
                .put(DECLARACION_NUM_COMPROBANTE, 1)
                .put(DECLARACION_FECHA_RECEP, 1)
                .put(DECLARACION_ANIO, 1));
        options.setSort(new JsonObject()
                .put(DECLARACION_FECHA_RECEP, -1));
        mongoClient.findWithOptions(COLLECTION_RECEPCION_WEB + collName, validacion, options, res -> {
            if (res.succeeded()) {
                datos.handle(Future.succeededFuture(generarJsonHistorial(res.result(), false)));
            } else {
                LOGGER.error(res.cause());
                datos.handle(Future.failedFuture(res.cause()));
            }
        });
        return datos;
    }

//    @SuppressWarnings("deprecation")
//    @Override
//    public Future<List<JsonObject>> buscarPorNombre(String nombre, Integer collName) {
//        Future<List<JsonObject>> busqueda = Future.future();
//        
//        JsonArray pipe = new JsonArray();
//        pipe.add(new JsonObject().put(PROP_MATCH, new JsonObject().put(PROP_TEXT, new JsonObject().put(PROP_SEARCH, nombre))));
//        pipe.add(new JsonObject().put(PROP_PROJECT,new JsonObject()
//        		.put(PROP_SCORE, new JsonObject().put(PROP_META, PROP_TEST_SCORE))
//        		.put(ID_USR_DNET, 1)
//                .put(INSTI_RECEP_ENTE_NOMBRE, 1)
//                .put(INSTI_RECEP_ENTE_CARGO_COMISION,1)
//                .put(NOMBRE_DECLARANTE_RECEP_WEB, 1)
//                .put(NOMBRE_DECLARANTE_RFC, 1)
//                .put(NOMBRE_DECLARANTE_CURP, 1)
//                ));
//        pipe.add(new JsonObject()
//                .put(PROP_SORT, new JsonObject().put(PROP_SCORE, new JsonObject().put(PROP_META, PROP_TEST_SCORE))
//                .put(NOMBRE_DECLARANTE_RECEP_WEB, 1)
//                .put(DECLARACION_FECHA_ID, -1)));
//        JsonObject agg = new JsonObject();
//        
//        agg.put(PROP_AGGREGATE, COLLECTION_RECEPCION_WEB+collName);
//        agg.put(PROP_PIPELINE, pipe);
//        agg.put(PROP_CURSOR,  new JsonObject());
//        mongoClient.runCommand(PROP_AGGREGATE, agg, rc ->{
//        	if(rc.succeeded()) {
//        		List<JsonObject> ff = new ArrayList<>();
//        		for (Object reg : rc.result().getJsonObject(PROP_CURSOR).getJsonArray(PROP_FIRST_BATCH)) {
//					ff.add((JsonObject)reg);
//				}
//        		 busqueda.handle(Future.succeededFuture(ff));
//        	}else {
//        		LOGGER.info(String.format("error al consultar datos: %s",rc.cause()));
//        		busqueda.handle(Future.failedFuture(rc.cause()));
//        	}
//        });
//        
//        return busqueda;
//    }
    
    
    @SuppressWarnings("deprecation")
    @Override
    public Future<JsonArray> buscarPorNombreSp(String nombre, Integer collName) {
        Future<JsonArray> busqueda = Future.future();
        JsonArray pipe = PIPELINE_BUSQUEDA_DATOS_PUBLICOS.copy();
        pipe.getJsonObject(0).getJsonObject(PROP_MATCH).getJsonObject(PROP_TEXT).put(PROP_SEARCH, nombre);
        pipe.getJsonObject(2).getJsonObject("$lookup").put("from", "reservaSN".concat(collName.toString()));
        
        JsonObject agg = new JsonObject();
        
        agg.put(PROP_AGGREGATE, COLLECTION_DATOS_PUBLICOS.concat(collName.toString()));
        agg.put(PROP_PIPELINE, pipe);
        agg.put(PROP_CURSOR,  new JsonObject());
        mongoClient.runCommand(PROP_AGGREGATE, agg, rc ->{
        	if(rc.succeeded()) {
                    LOGGER.info(String.format("Total size %d", rc.result().getJsonObject(PROP_CURSOR).getJsonArray(PROP_FIRST_BATCH).size()));
                    busqueda.handle(Future.succeededFuture(rc.result().getJsonObject(PROP_CURSOR).getJsonArray(PROP_FIRST_BATCH)));
//        		List<JsonObject> ff = new ArrayList<>();
//        		for (Object reg : ) {
//        			JsonObject registro = new JsonObject();
//        			registro.put(PROP_NOMBRE, ((JsonObject)reg).getJsonObject(PROP__ID).getString(PROP_NOMBRE));
//        			registro.put(PROP_DEPENDENCIA, ((JsonObject)reg).getJsonObject(PROP__ID).getString(PROP_DEPENDENCIA));
//        			registro.put(PROP_ID_USR_DECNET, ((JsonObject)reg).getJsonObject(PROP__ID).getInteger(PROP_ID_USR_DECNET));
//        			registro.put(PROP_RFC, ((JsonObject)reg).getJsonObject(PROP__ID).getString(PROP_RFC));
//        			registro.put(PROP_CURP, ((JsonObject)reg).getJsonObject(PROP__ID).getString(PROP_CURP));
//					ff.add(registro);
//				}
//        		 busqueda.handle(Future.succeededFuture(ff));
        	}else {
        		LOGGER.info(String.format("error al consultar datos: %s",rc.cause()));
        		busqueda.handle(Future.failedFuture(rc.cause()));
        	}
        });
        
        return busqueda;
    }

    @Override
    public Future<List<JsonObject>> buscarPorRFC(String rfc) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<JsonObject> generarJsonHistorial(List<JsonObject> histoData, boolean isGabinete) {
        List<JsonObject> historial = new ArrayList<>();
        for (JsonObject data : histoData) {
            JsonObject historico = new JsonObject()
                    .put(PROP_ID_USUARIO, data.getJsonObject(PROP_DECLARANTE).getInteger(PROP_ID_USR_DECNET))
                    .put(PROP_NUM_DECLARACION, data.getJsonObject(PROP_DECLARACION).getString(PROP_NUM_DECLARACION))
                    .put(PROP_FECHA_RECEPCION, data.getJsonObject(PROP_DECLARACION).getString(PROP_FECHA_RECEPCION).substring(0, 10))
                    .put(PROP_COLL_NAME, data.getJsonObject(PROP_INSTITUCION_RECEPTORA).getInteger(PROP_COLL_NAME))
                    .put(PROP_TIPO_DECLARACION, data.getJsonObject(PROP_DECLARACION).getString(PROP_TIPO_DECLARACION))
                    .put(PROP_NUMERO_COMPROBANTE, data.getJsonObject(PROP_DECLARACION).getString(PROP_NUMERO_COMPROBANTE))
                    .put(PROP_ANIO,  data.getJsonObject(PROP_DECLARACION).getInteger(PROP_ANIO));
            if(isGabinete) {
            	historico.put("nombre", data.getJsonObject(PROP_DECLARANTE).getString("nombre"));
            }
            if (data.getJsonObject(PROP_DECLARANTE).getString("dependencia") != null){
                historico.put(PROP_INSTITUCION_RECEPTORA,  data.getJsonObject(PROP_DECLARANTE).getString("dependencia").toUpperCase());
            }else if (data.getJsonObject(PROP_DECLARANTE).getValue("enteCargoComision") != null){
                if (data.getJsonObject(PROP_DECLARANTE).getValue("enteCargoComision") instanceof JsonObject){
                    historico.put(PROP_INSTITUCION_RECEPTORA, data.getJsonObject(PROP_DECLARANTE).getJsonObject("enteCargoComision").getString("dependencia").toUpperCase());
                }else{
                    historico.put(PROP_INSTITUCION_RECEPTORA, data.getJsonObject(PROP_DECLARANTE).getJsonArray("enteCargoComision").getJsonObject(0).getString("dependencia").toUpperCase());
                }
            }
            historial.add(historico);
        }
        return historial;
    }

    @Override
    public Future<List<JsonObject>> consultaReservados(Integer idEnte) {
        Future<List<JsonObject>> busqueda = Future.future();
        FindOptions options = new FindOptions().setFields(new JsonObject()
                .put("rfc", 1)
                .put("curp", 1));

        mongoClient.findWithOptions("reservaSN" + idEnte, new JsonObject(), options, res -> {
            if (res.succeeded()) {
                if (res.result() != null) {
                    busqueda.handle(Future.succeededFuture(res.result()));
                } else {
                    busqueda.handle(Future.succeededFuture(new ArrayList<>()));
                }
            } else {
                LOGGER.error(res.cause());
                busqueda.handle(Future.failedFuture(res.cause()));
            }
        });
        return busqueda;
    }

    @Override
	public Future<List<JsonObject>> consultaGabinete(int idGabinete) {Future<List<JsonObject>> datos = Future.future();
    	JsonArray pipe = new JsonArray();
    	if(GRUPOS_GABINETES.containsKey(idGabinete)) {
        pipe.add(new JsonObject().put(PROP_MATCH, new JsonObject().put(ID_USR_DNET, new JsonObject().put(PROP_$IN, GRUPOS_GABINETES.get(idGabinete)))
        		 .put(PROP_$NOR, new JsonArray()
                 .add(new JsonObject().put(DECLARACION_TIPO_DECLARACION, PROP_TIPO_DECLARACION_NOTA))
                 .add(new JsonObject().put(DECLARACION_TIPO_DECLARACION, PROP_AVISO)))));
        
         ArrayList<String> eq = new ArrayList<>();
        eq.add($DECLARACION_FECHA_ENCARGO);
        eq.add(null);
        ArrayList<Object> concat = new ArrayList<>();
        concat.add(new JsonObject().put($TOSTRING, $DECLARACION_ANIO));
        concat.add(PROP_PRIMERO_MAYO);
        pipe.add(new JsonObject().put(PROP_PROJECT,new JsonObject()
        		.put("fechaEncargoT", 
               		 new JsonObject().put(PROP_$COND, 
               				 new JsonObject().put(PROP_IF, 
               						 new JsonObject().put(PROP_$EQ, eq)).put(PROP_THEN,
               								 new JsonObject().put(PROP_$CONCAT, concat)).put(PROP_ELSE, $DECLARACION_FECHA_ENCARGO)))
        		 .put(ID_USR_DNET, 1)
                 .put(DECLARACION_NUM_DECLA, 1)
                 .put(INSTI_RECEP_ENTE_NOMBRE, 1)
                 .put(INSTI_RECEP_ENTE_CARGO_COMISION, 1)
                 .put(INSTI_RECEP_COLLNAME, 1)
                 .put(DECLARACION_TIPO_DECLARACION, 1)
                 .put(DECLARACION_NUM_COMPROBANTE, 1)
                 .put(DECLARACION_FECHA_RECEP, 1)
                 .put(DECLARACION_ANIO, 1)
                 .put(DECLARANTE_NOMBRE, 1)
                 ));
        
        pipe.add(new JsonObject().put(PROP_SORT, new JsonObject().put("fechaEncargoT", -1)));
        
         JsonObject agg = new JsonObject();
         
         agg.put(PROP_AGGREGATE, COLLECTION_RECEPCION_WEB+100);
         agg.put(PROP_PIPELINE, pipe);
         agg.put(PROP_CURSOR,  new JsonObject());
         mongoClient.runCommand(PROP_AGGREGATE, agg, rc ->{
         	if(rc.succeeded()) {
         		List<JsonObject> ff = new ArrayList<>();
         		for (Object reg : rc.result().getJsonObject(PROP_CURSOR).getJsonArray(PROP_FIRST_BATCH)) {
 					ff.add((JsonObject)reg);
 				}
         		datos.handle(Future.succeededFuture(generarJsonHistorial(filtrarConsultaGabinete(ff), true)));
         	}else {
         		LOGGER.info(String.format("error al consultar datos consultaGabinete: %s",rc.cause()));
         		datos.handle(Future.failedFuture(rc.cause()));
         	}
         });
         
    	}else {
    		LOGGER.info("No existe id grupo a consultar"+idGabinete);
     		datos.handle(Future.failedFuture("No existe id grupo a consultar"));
    	}
		return datos;
	}
    
    public List<JsonObject> filtrarConsultaGabinete(List<JsonObject> consulta) {
    	 HashMap<Integer, JsonObject> filtrados = new HashMap<>();
         List<JsonObject> filtro = new ArrayList<>();
         consulta.stream().forEach((reg) -> {
        	 if (reg.getJsonObject(PROP_DECLARANTE).getInteger(PROP_ID_USR_DECNET) != null) {
        		 if (!filtrados.containsKey(reg.getJsonObject(PROP_DECLARANTE).getInteger(PROP_ID_USR_DECNET))) {
        			 filtro.add(reg);
        			 filtrados.put(reg.getJsonObject(PROP_DECLARANTE).getInteger(PROP_ID_USR_DECNET), new JsonObject());
        		 }
        	 }
         });
    	return filtro;
    }

}
