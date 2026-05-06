package mx.gob.sfp.dgti.service;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import mx.gob.sfp.dgti.daopxy.DAOConsultaServidorInterface;
import mx.gob.sfp.dgti.util.Helper;

import static mx.gob.sfp.dgti.util.Constantes.PATTERN_RFC_MIN;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ESTATUS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MENSAJE;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_ESTRUCTURA;
import static mx.gob.sfp.dgti.util.Constantes.PATTERN_NOMBRE;
import static mx.gob.sfp.dgti.util.Constantes.SOLICITUD_EXITOSA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DATOS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ID_USR_DECNET;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DECLARANTE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import static mx.gob.sfp.dgti.util.Constantes.CONSULTAR_ENTES;
import static mx.gob.sfp.dgti.util.Constantes.PROP_INSTITUCION_RECEPTORA;

public class ServiceConsultaPublicaImpl implements ServiceConsultaPublicaInterface {

    private static final Logger logger = Logger.getLogger(ServiceConsultaPublicaImpl.class.getName());
    private static DAOConsultaServidorInterface daoConsulta;
    private final WebClient client;
//    private static HashMap<Integer, HashMap> reservados;
    private static HashMap<Integer, JsonObject> gabinete ;

    /**
     * Constructor de la clase, recibe ub objecto Vertx
     *
     * @param vertx
     * @param client
     */
    public ServiceConsultaPublicaImpl(Vertx vertx, WebClient client){
        daoConsulta = DAOConsultaServidorInterface.create(vertx);
        this.client = client;
        this.obtenRecerbadosDeEntes().setHandler(resultEntes ->{
            this.obtenReservadosDeEntes(resultEntes.result());
        });
        
        this.cargaGabinete();
        vertx.setPeriodic(86400000, id -> {
        	this.cargaGabinete();
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public Future<JsonObject> buscarServidorPublicoSp(String data, Integer collName) {
        Future<JsonObject> busqueda = Future.future();
        try {
            if (isNombreValido(data)) {
                daoConsulta.buscarPorNombreSp(genererBusquedaNombre(data), collName).setHandler(nom -> {
                    if (nom.succeeded()) {
                        if (nom.result()!= null){
                            busqueda.handle(Future.succeededFuture(crearRespuestaExitosa(nom.result().getList(), nom.result().size())));
                        }else{
                            crearRespuestaExitosa(new ArrayList<>(), 0);
                        }
                    } else {
                        busqueda.handle(Future.failedFuture(nom.cause()));
                    }
                });
            } else if (isRFCValido(data)) {
                busqueda.handle(Future.succeededFuture(Helper.crearRespuestaErrorEstructura(ERROR_ESTRUCTURA)));
            } else {
                busqueda.handle(Future.succeededFuture(Helper.crearRespuestaErrorEstructura(ERROR_ESTRUCTURA)));
            }
        }catch (Exception e) {
        	logger.severe(e.getMessage());
        	 busqueda.handle(Future.failedFuture("Error al ejecutar la petición"));
		}
        return busqueda;
    }

    @Override
    public Future<JsonObject> buscarHistorialServidorPublico(Integer idUsr, Integer collName) {
        Future<JsonObject> busqueda = Future.future();
        daoConsulta.buscarHistorialSP(idUsr, collName).setHandler(his -> {
            if (his.succeeded()) {
                busqueda.handle(Future.succeededFuture(crearRespuestaExitosa(his.result(), 0)));
            } else {
                busqueda.handle(Future.failedFuture(his.cause()));
            }
        });
        return busqueda;
    }

    @Override
    public Future<JsonObject> consultaGabinete(int tipoGabinete) {
            Future<JsonObject> consulta = Future.future();
       if(gabinete.containsKey(tipoGabinete)) {
               consulta.handle(Future.succeededFuture(gabinete.get(tipoGabinete)));
       }else {
               logger.info(String.format("GRUPO DE GABINETE NO ENCONTRADO %s ",tipoGabinete));
               consulta.handle(Future.failedFuture("GRUPO DE GABINETE NO ENCONTRADO"+tipoGabinete));
       }

            return consulta;
    }

    public boolean isRFCValido(String rfc) {
        boolean valido = false;
        if (Pattern.matches(PATTERN_RFC_MIN, rfc) && !rfc.contains(" ")
                && rfc.length() < 14) {
            valido = true;
        }
        return valido;
    }

    public boolean isNombreValido(String nombre) {
        return Pattern.matches(PATTERN_NOMBRE, nombre);
    }

    public JsonObject crearRespuestaExitosa(List<JsonObject> datos, int size) {
    	
    	return  new  JsonObject().put(PROP_ESTATUS, Boolean.TRUE)
                .put(PROP_MENSAJE, size == 50 ? "200" : SOLICITUD_EXITOSA)
                .put(PROP_DATOS, datos);
    }

    public String genererBusquedaNombre(String nombre) {
        StringBuilder strNombre = new StringBuilder();
        for (String palabra : nombre.split(" ")) {
            strNombre.append("\"").append(palabra).append("\" ");
        }
        strNombre.delete(strNombre.length()-1, strNombre.length());
        return strNombre.toString();
    }
    
    public List<JsonObject> filtrarJson(List<JsonObject> jsonData, HashMap<String, String> reservados) {
        HashMap<Integer, JsonObject> filtrados = new HashMap<>();
        List<JsonObject> filtro = new ArrayList<>(); 
        jsonData.stream().forEach((reg) -> {
            try{
                if (reg.getJsonObject(PROP_DECLARANTE).getInteger(PROP_ID_USR_DECNET) != null) {                    
                    if (!filtrados.containsKey(reg.getJsonObject(PROP_DECLARANTE).getInteger(PROP_ID_USR_DECNET))) {
                        if (!reservados.containsKey(reg.getJsonObject(PROP_DECLARANTE).getString("rfc")) &&
                                !reservados.containsKey(reg.getJsonObject(PROP_DECLARANTE).getString("curp"))){
                            JsonObject declarante = reg.getJsonObject(PROP_DECLARANTE);
                            declarante.remove("rfc");
                            declarante.remove("curp");
                            if (reg.getJsonObject(PROP_DECLARANTE).getString("dependencia") != null){
                                declarante.put(PROP_INSTITUCION_RECEPTORA,reg.getJsonObject(PROP_DECLARANTE).getString("dependencia").toUpperCase());
                            }else if (reg.getJsonObject(PROP_DECLARANTE).getJsonArray("enteCargoComision") != null){
                                declarante.put(PROP_INSTITUCION_RECEPTORA,reg.getJsonObject(PROP_DECLARANTE).getJsonArray("enteCargoComision").getJsonObject(0).getString("dependencia").toUpperCase());
                            }
                            filtro.add(declarante);
                            filtrados.put(reg.getJsonObject(PROP_DECLARANTE).getInteger(PROP_ID_USR_DECNET), declarante);
                        }
                    }
                }
            }catch(NullPointerException e ){
                logger.log(Level.SEVERE, "Error: {0}", e);
            }
        });
        return filtro;
    }

    private void obtenReservadosDeEntes(JsonArray entes) {  
//        reservados= new HashMap<>();
        for(Object enteIndex : entes) {
            JsonObject enteJsn = (JsonObject)enteIndex;
            daoConsulta.consultaReservados(enteJsn.getInteger("collName")).setHandler(response-> {
                if(response.succeeded()){
                    HashMap<String,String> reservadosBd = new HashMap<>();
                    
                    for (JsonObject index : response.result()){
                        if (index.getString("rfc") != null && !index.getString("rfc").isEmpty()){
                            reservadosBd.put(index.getString("rfc"),null);
                        }
                        if (index.getString("curp") != null && !index.getString("curp").isEmpty()){
                            reservadosBd.put(index.getString("curp"),null);
                        }
                    }
//                    reservados.put(enteJsn.getInteger("collName"), reservadosBd);
                    if(!response.result().isEmpty())
                        logger.info(String.format("===>>BD collName %d con %d registros en base %n ====>>>MAP collName %d con %d registros en map", enteJsn.getInteger("collName"), response.result().size(), enteJsn.getInteger("collName"), reservadosBd.size()));
                }else{
                    logger.severe(String.format("ERROR al realizar consulta de reservados %s", response.cause()));
                }
            });
        }
    }
    
    private Future<JsonArray> obtenRecerbadosDeEntes(){
        Future<JsonArray> reservadosf = Future.future();
        client.getAbs(CONSULTAR_ENTES)
                .timeout(15000)
                .send(entes ->{
                    if (entes.succeeded() && entes.result().statusCode() == HttpResponseStatus.OK.code()){
                        reservadosf.handle(Future.succeededFuture(entes.result().bodyAsJsonArray()));
                    }else{
                        logger.severe("ocurrio un error");
                        logger.severe(String.format("sucedio Error al consultar entes %s", entes.cause()));
                    }
                });
        return reservadosf;
    }
    
    public void cargaGabinete() {
    	Future<JsonObject> grupo1 = Future.future();
    	Future<JsonObject> grupo2 = Future.future();
    	Future<JsonObject> grupo3 = Future.future();
    	Future<JsonObject> grupo4 = Future.future();
    	
    	 daoConsulta.consultaGabinete(1).setHandler(cons ->{
      	   if(cons.succeeded()) {
      		 grupo1.handle(Future.succeededFuture(crearRespuestaExitosa(cons.result(),0)));
      	   }else {
      		 grupo1.handle(Future.failedFuture(cons.cause()));
      		   logger.severe(String.format("Error al optener los datos de la bd grupo1 %s",cons.cause()));
      	   }
         });
    	 daoConsulta.consultaGabinete(2).setHandler(cons ->{
    		 if(cons.succeeded()) {
    			 grupo2.handle(Future.succeededFuture(crearRespuestaExitosa(cons.result(),0)));
        	}else {
        		grupo2.handle(Future.failedFuture(cons.cause()));
        		logger.severe(String.format("Error al optener los datos de la bd grupo2 %s",cons.cause()));
        	}
          });
    	 daoConsulta.consultaGabinete(3).setHandler(cons ->{
        	   if(cons.succeeded()) {
        		   grupo3.handle(Future.succeededFuture(crearRespuestaExitosa(cons.result(),0)));
        	   }else {
        		   grupo3.handle(Future.failedFuture(cons.cause()));
        		   logger.severe(String.format("Error al optener los datos de la bd grupo3 %s",cons.cause()));
        	   }
           });
    	 daoConsulta.consultaGabinete(4).setHandler(cons ->{
      	   if(cons.succeeded()) {
      		   grupo4.handle(Future.succeededFuture(crearRespuestaExitosa(cons.result(),0)));
      	   }else {
      		   grupo4.handle(Future.failedFuture(cons.cause()));
      		   logger.severe(String.format("Error al optener los datos de la bd grupo4  %s",cons.cause()));
      	   }
         });
    	 
    	 CompositeFuture.all(grupo1, grupo2, grupo3,grupo4).setHandler(grupos ->{
    		if(grupos.succeeded()) {
                    gabinete = new HashMap<>();
                    gabinete.put(1, grupo1.result());
                    gabinete.put(2, grupo2.result());
                    gabinete.put(3, grupo3.result());
                    gabinete.put(4, grupo4.result());
                    logger.info("Datos gabinete precargados con éxito");
    		}
    	 });
    	 
    }
    
    public List<JsonObject> filtrarJsonBusquedaNombre(JsonArray jsonData, HashMap<String, String> reservados){
         return  jsonData.getList();
    }

//    public static HashMap<Integer, HashMap> getReservados(){
//        return reservados;
//    }
}
