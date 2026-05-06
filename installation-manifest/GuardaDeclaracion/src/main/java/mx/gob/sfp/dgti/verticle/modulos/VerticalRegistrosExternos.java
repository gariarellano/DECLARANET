/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.verticle.modulos;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import java.util.logging.Level;
import java.util.logging.Logger;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ANIO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_CURP;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_DATOS_EMPLEO_CARGO_COMISION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_DECLARANTE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_EMPLEO_CARGO_COMISION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ENCABEZADO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_FECHA_ENCARGO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ID;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ID_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_INSTITUCION_RECEPTORA;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_NOMBRE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_NO_COMPROBANTE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_NUMERO_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_TIPO_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes._ID;
import static mx.gob.sfp.dgti.verticle.modulos.VerticalRegHistialNobreInstitucion.REG_HIST_NOMBRE_INSTITUCION;

/**
 * Verical para realizar lo llamados de los 
 * registros de las declaraciones, tanto para pdn
 * como cumplimineto de la obligación de la declaracion.
 * 
 * @author cgarias
 * @since 27/04/2020
 */
public class VerticalRegistrosExternos extends AbstractVerticle{
    private static final Logger logger = Logger.getLogger(VerticalRegistrosExternos.class.getName());
    private static final String REGISTRO_PDN = "REGISTRO_PDN";
    private static final String REGISTRO_PDN_VALOR = System.getenv(REGISTRO_PDN);
    public static final String VERTICAL_ENVIA_PDN = "VERTICAL_ENVIA_PDN";
    private static final String REGISTRO_CUMPLIMIENTO = "REGISTRO_CUMPLIMIENTO";
    public static final String VERTICAL_ENVIA_DATOS_PUBLICOS = "VERTICAL_ENVIA_DATOS_PUBLICOS";
    public static final String VERTICAL_ENVIA_CUMPLIMIENTO = "VERTICAL_ENVIA_CUMPLIMIENTO";
    private static final String VALOR_REGISTRO_CUMPLIMIENTO = System.getenv(REGISTRO_CUMPLIMIENTO);
    private static final String NOTA = "NOTA";
    private CircuitBreaker breakerPdn;
    private CircuitBreaker breakerCumpl;
    private WebClient webClient;
    
    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context); //To change body of generated methods, choose Tools | Templates.
        webClient = WebClient.create(vertx);
        breakerPdn =   CircuitBreaker.create(REGISTRO_PDN, vertx,
                        new CircuitBreakerOptions()
                            .setMaxFailures(50) // number of failure before opening the circuit
                            .setTimeout(7000) // consider a failure if the operation does not succeed in time
                            .setFallbackOnFailure(true) // do we call the fallback on failure
                            .setResetTimeout(15000) // time spent in open state before attempting to re-try
                    );
        breakerCumpl =   CircuitBreaker.create(REGISTRO_CUMPLIMIENTO, vertx,
                        new CircuitBreakerOptions()
                            .setMaxFailures(50) // number of failure before opening the circuit
                            .setTimeout(7000) // consider a failure if the operation does not succeed in time
                            .setFallbackOnFailure(true) // do we call the fallback on failure
                            .setResetTimeout(15000) // time spent in open state before attempting to re-try
                    );
    }
    
    /**
     * Start inicializar router y la definición de paths
     * @param future
     * @throws Exception
     */
    @Override
    public void start(Future<Void> future) throws Exception {
        super.start();
        //Vertice del guardadoPdn
        vertx.eventBus().consumer(VERTICAL_ENVIA_PDN, mensaje -> {
            JsonObject recepcionWebYDeclaracion = (JsonObject) mensaje.body();            
            /**
             * Solo se devera guardar info de 
             * INICIO, MODIFICACION, CONCLUSION y AVISO
             * NOTA no se contempla
             */
            if (!NOTA.equals(recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_TIPO_DECLARACION))){
                breakerPdn.execute( futureBreaker ->{
                        try{
                            webClient.postAbs(REGISTRO_PDN_VALOR)
                                    .timeout(550000)
                                    .sendJsonObject(this.creaObjetosParaEnviarlos(recepcionWebYDeclaracion), registradoPdn -> {
                                        if (registradoPdn.succeeded() && registradoPdn.result().statusCode() == HttpResponseStatus.OK.code()){
                                            futureBreaker.handle(Future.succeededFuture());
                                        }else{
                                            if (registradoPdn.succeeded() && registradoPdn.result().statusCode() != HttpResponseStatus.OK.code()){
                                                logger.warning(String.format("El llamado al servicio REGISTRO_PDN_VALOR entrego status %d  num decla: %s", registradoPdn.result().statusCode(), recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_NUMERO_DECLARACION)));
                                                futureBreaker.handle(Future.failedFuture(String.format("El llamado al servicio REGISTRO_PDN_VALOR entrego status %d", registradoPdn.result().statusCode())));
                                            }else{
                                                logger.severe(String.format("ERROR DE VERTICAL_ENVIA_PDN %s", registradoPdn.cause()));
                                                futureBreaker.handle(Future.failedFuture(registradoPdn.cause()));
                                            }
                                        }
                                        mensaje.reply("OK");
                                    });         
                        }catch(Exception e){
                            logger.severe(String.format("Ocurrio un error en VERTICAL_ENVIA_PDN  %s objeto %n %s", e, recepcionWebYDeclaracion.toString()));
                            futureBreaker.handle(Future.failedFuture(e));
                        }
                    });
            }
        });
        
        //Vertice del registro de cumplimiento
        vertx.eventBus().consumer(VERTICAL_ENVIA_CUMPLIMIENTO, mensaje -> {
            JsonObject recepcionWebYDeclaracion = (JsonObject) mensaje.body();
            /**
             * Solo se devera guardar info de 
             * INICIO, MODIFICACION, CONCLUSION y AVISO
             * NOTA no se contempla
             */
            if (!NOTA.equals(recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_TIPO_DECLARACION))){
                if (!"AVISO".equals(recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_TIPO_DECLARACION))){
//                    for (Object encargo : recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_EMPLEO_CARGO_COMISION).getJsonArray(FIELD_EMPLEO_CARGO_COMISION)){
                        breakerCumpl.execute( futureBreaker ->{
//                            try{
                                this.generaCumplimientoPorPuesto(
                                        recepcionWebYDeclaracion, 
                                        recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_EMPLEO_CARGO_COMISION).getJsonArray(FIELD_EMPLEO_CARGO_COMISION),
                                        recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_EMPLEO_CARGO_COMISION).getJsonArray(FIELD_EMPLEO_CARGO_COMISION).size()-1
                                );
                                mensaje.reply("OK");
//                            }catch(Exception e){
//                                logger.severe(String.format("Ocurrio un error en VERTICAL_ENVIA_CUMPLIMIENTO %s objeto %n %s", e, recepcionWebYDeclaracion.toString()));
//                                logger.log(Level.SEVERE,"{0} "+e, e);
//                                futureBreaker.handle(Future.failedFuture(e));
//                            }
                        });
//                    }
                }else{
                    breakerCumpl.execute( futureBreaker ->{
                       try{
                            JsonObject cumplimientoDecla= this.creaJsonCumplimientoAviso(recepcionWebYDeclaracion);
                            this.generaCumplimiento(cumplimientoDecla, futureBreaker);
                            mensaje.reply("OK");
                        }catch(Exception e){
                            logger.severe(String.format("Ocurrio un error en VERTICAL_ENVIA_CUMPLIMIENTO al parsear este objeto en, metodo creaJsonCumplimientoAviso : %s ", recepcionWebYDeclaracion.toString()));
                            logger.log(Level.SEVERE,"{0} "+e, e);
                            futureBreaker.handle(Future.failedFuture(e));
                        }
                    });
                }
            }
        }); 
        
        //Vertice del registro de datos publicos para listado unico.
        vertx.eventBus().consumer(VERTICAL_ENVIA_DATOS_PUBLICOS, mensaje -> {
            JsonObject recepcionWebYDeclaracion = (JsonObject) mensaje.body();
            /**
             * Solo se devera guardar info d elos datos para 
             * INICIO, MODIFICACION, CONCLUSION y AVISO
             * NOTA no se contempla
             */
            if (!NOTA.equals(recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_TIPO_DECLARACION))){
                if (!"AVISO".equals(recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_TIPO_DECLARACION))){
                    try{
                        this.enviaDatosPublicos(
                                recepcionWebYDeclaracion.getJsonObject("recepcionWeb"), 
                                recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_EMPLEO_CARGO_COMISION).getJsonArray(FIELD_EMPLEO_CARGO_COMISION),
                                recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_EMPLEO_CARGO_COMISION).getJsonArray(FIELD_EMPLEO_CARGO_COMISION).size()-1,
                                procesado->{
                                    mensaje.reply("OK");
                                });
                    }catch(Exception e){
                        logger.severe(String.format("Ocurrio un error en VERTICAL_ENVIA_DATOS_PUBLICOS %s objeto %n %s", e, recepcionWebYDeclaracion.toString()));
                    }
                }else{
                    try{
                        JsonObject declarante = recepcionWebYDeclaracion.getJsonObject("recepcionWeb").getJsonObject("declarante").copy();
                        declarante.put("idDependencia", recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject("detalleAvisoCambioDependencia").getJsonObject("empleoCargoComisionInicia").getJsonObject("ente").getString(FIELD_ID));
                        declarante.put("dependencia", recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject("detalleAvisoCambioDependencia").getJsonObject("empleoCargoComisionInicia").getJsonObject("ente").getString(FIELD_NOMBRE));
                        vertx.eventBus().send(
                                REG_HIST_NOMBRE_INSTITUCION, 
                                new JsonObject().put("collName", recepcionWebYDeclaracion.getJsonObject("recepcionWeb").getJsonObject("institucionReceptora").getInteger("collName")).put("declarante", declarante), 
                                procesado ->{
                        mensaje.reply("OK");
                    });
                        
                     }catch(Exception e){
                         logger.severe(String.format("Ocurrio un error en VERTICAL_ENVIA_CUMPLIMIENTO al parsear este objeto en, metodo creaJsonCumplimientoAviso : %s ", recepcionWebYDeclaracion.toString()));
                         logger.log(Level.SEVERE,"{0} "+e, e);
                     }
                }
            }
        }); 
    }
    
    private void generaCumplimientoPorPuesto(JsonObject recepcionWebYDeclaracion,  JsonArray puestos, int posicion){
        if (posicion >= 0){
            vertx.setTimer(3000, id->{
                breakerCumpl.execute( futureBreaker ->{
                    try{
                        JsonObject cumplimientoDecla= this.creaJsonCumplimientoDecla((JsonObject) puestos.getJsonObject(posicion), recepcionWebYDeclaracion);
                        this.generaCumplimiento(cumplimientoDecla, futureBreaker);
                    }catch(Exception e){
                        logger.severe(String.format("Ocurrio un error en VERTICAL_ENVIA_CUMPLIMIENTO %s objeto %n %s", e, recepcionWebYDeclaracion.toString()));
                        logger.log(Level.SEVERE,"{0} "+e, e);
                        futureBreaker.handle(Future.failedFuture(e));
                    }
                });
                generaCumplimientoPorPuesto(recepcionWebYDeclaracion, puestos, posicion-1);
            });
        }
    }
   
    private void generaCumplimiento(JsonObject cumplimiento, Promise<Object> futureBreaker){
        webClient.postAbs(VALOR_REGISTRO_CUMPLIMIENTO)
            .timeout(550000)
            .sendJsonObject(cumplimiento, registradoCumplimiento -> {
                if (registradoCumplimiento.succeeded() && registradoCumplimiento.result().statusCode() == HttpResponseStatus.OK.code()){
                    futureBreaker.handle(Future.succeededFuture());
                }else{
                    if (registradoCumplimiento.succeeded() && registradoCumplimiento.result().statusCode() != HttpResponseStatus.OK.code()){
                        logger.warning(String.format("El llamado al servicio VERTICAL_ENVIA_CUMPLIMIENTO entrego status %d", registradoCumplimiento.result().statusCode()));
                        futureBreaker.handle(Future.failedFuture(String.format("El llamado al servicio VALOR_REGISTRO_CUMPLIMIENTO entrego status %d", registradoCumplimiento.result().statusCode())));
                    }else{
                        logger.severe(String.format("ERROR DE VERTICAL_ENVIA_CUMPLIMIENTO  %s", registradoCumplimiento.cause()));
                        futureBreaker.handle(Future.failedFuture(registradoCumplimiento.cause()));
                    }
                }
            }); 
    }
    
    
    private JsonObject creaObjetosParaEnviarlos(JsonObject recWebYdeclaracion){
        JsonObject response  = new JsonObject().put(FIELD_DECLARACION, recWebYdeclaracion.getJsonObject(FIELD_DECLARACION));
        JsonObject recepcionWeb = recWebYdeclaracion.getJsonObject("recepcionWeb");
        response.put("recepcionWeb", 
                new JsonObject()
                        .put("idRecepcionWeb", recepcionWeb.getString(_ID))
                        .put("versionRecepcionWeb", recepcionWeb.getString("versionRecepcionWeb"))
                        .put("fechaRecepcion", recepcionWeb.getJsonObject(FIELD_DECLARACION).getString("fechaRecepcion"))
                        .put("extemporanea", recepcionWeb.getJsonObject(FIELD_DECLARACION).getBoolean("extemporanea"))
                        .put(FIELD_NO_COMPROBANTE, recepcionWeb.getJsonObject(FIELD_DECLARACION).getString(FIELD_NO_COMPROBANTE))
                        .put("nombreArchivo", recepcionWeb.getJsonObject("firmaDeclaracion").getString("nombreFirm"))
        );
        return response;
    }
    
    private JsonObject creaJsonCumplimientoDecla(JsonObject encargo, JsonObject recepcionWebYDeclaracion){
        JsonObject cumplimiento= new JsonObject();
        //referente a ecargo
        cumplimiento.put("idDNetNoLocalizado", encargo.getString(FIELD_ID)+recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getInteger(FIELD_ANIO));
        cumplimiento.put("idEnte",encargo.getJsonObject("ente").getString(FIELD_ID));
        cumplimiento.put("nombreEnte",encargo.getJsonObject("ente").getString(FIELD_NOMBRE));
        cumplimiento.put("idNivelJerarquico",encargo.getJsonObject("nivelJerarquico").getInteger(FIELD_ID));
        cumplimiento.put("valorNivelJerarquico",encargo.getJsonObject("nivelJerarquico").getString("valor"));
        cumplimiento.put("areaAdscripcion",encargo.getString("areaAdscripcion"));
        cumplimiento.put("empleoCargoComision",encargo.getString("empleoCargoComision"));
        cumplimiento.put("nivelEmpleoCargoComision",encargo.getString("nivelEmpleoCargoComision"));
        if (encargo.getInteger("idMovimiento") != null)
            cumplimiento.put("idMovimiento",encargo.getInteger("idMovimiento"));
        
        //referente recepcioWeb
        cumplimiento.put("idRecepcionWeb", recepcionWebYDeclaracion.getJsonObject("recepcionWeb").getString(_ID));
        cumplimiento.put(FIELD_NO_COMPROBANTE, recepcionWebYDeclaracion.getJsonObject("recepcionWeb").getJsonObject(FIELD_DECLARACION).getString(FIELD_NO_COMPROBANTE));
        cumplimiento.put("fechaRecepcion", recepcionWebYDeclaracion.getJsonObject("recepcionWeb").getJsonObject(FIELD_DECLARACION).getString("fechaRecepcion"));
        cumplimiento.put("nombreCompleto", recepcionWebYDeclaracion.getJsonObject("recepcionWeb").getJsonObject(FIELD_DECLARANTE).getString(FIELD_NOMBRE));
        
        //referente Declaracion ecabezado
        cumplimiento.put(FIELD_ANIO,recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getInteger(FIELD_ANIO));
        cumplimiento.put("tipoDeclaracion", recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_TIPO_DECLARACION));
        cumplimiento.put("curp", recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getJsonObject(FIELD_USUARIO).getString(FIELD_CURP));
        cumplimiento.put("idUsrDecNet", recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getJsonObject(FIELD_USUARIO).getInteger(FIELD_ID_USUARIO));
        cumplimiento.put(FIELD_NUMERO_DECLARACION, recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_NUMERO_DECLARACION));
        
        if (recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_FECHA_ENCARGO)!= null)
            cumplimiento.put(FIELD_FECHA_ENCARGO,recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_FECHA_ENCARGO));
        if (recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getJsonObject(FIELD_USUARIO).getInteger("idSp")!= null)
            cumplimiento.put("idSp", recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getJsonObject(FIELD_USUARIO).getInteger("idSp"));
        
        //referente Declaracion ente receptor
        cumplimiento.put(FIELD_INSTITUCION_RECEPTORA, recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_INSTITUCION_RECEPTORA));
        
        return cumplimiento;
    }
    
    private JsonObject creaJsonCumplimientoAviso(JsonObject recepcionWebYDeclaracion){
        JsonObject cumplimiento= new JsonObject();
        //referente a ecargo
        cumplimiento.put("idDNetNoLocalizado", recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_NUMERO_DECLARACION)+recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getInteger(FIELD_ANIO));
        cumplimiento.put("idEnte",recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject("detalleAvisoCambioDependencia").getJsonObject("empleoCargoComisionInicia").getJsonObject("ente").getString(FIELD_ID));
        cumplimiento.put("nombreEnte",recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject("detalleAvisoCambioDependencia").getJsonObject("empleoCargoComisionInicia").getJsonObject("ente").getString(FIELD_NOMBRE));
        cumplimiento.put("idNivelJerarquico",recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject("detalleAvisoCambioDependencia").getJsonObject("empleoCargoComisionInicia").getJsonObject("nivelJerarquico").getInteger(FIELD_ID));
        cumplimiento.put("valorNivelJerarquico",recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject("detalleAvisoCambioDependencia").getJsonObject("empleoCargoComisionInicia").getJsonObject("nivelJerarquico").getString("valor"));
        cumplimiento.put("areaAdscripcion",recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject("detalleAvisoCambioDependencia").getJsonObject("empleoCargoComisionInicia").getString("areaAdscripcion"));
        cumplimiento.put("empleoCargoComision",recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject("detalleAvisoCambioDependencia").getJsonObject("empleoCargoComisionInicia").getString("empleoCargoComision"));
        cumplimiento.put("nivelEmpleoCargoComision",recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject("detalleAvisoCambioDependencia").getJsonObject("empleoCargoComisionInicia").getString("nivelEmpleoCargoComision"));
        cumplimiento.put("empleoCargoComisionConcluye",recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DECLARACION).getJsonObject("detalleAvisoCambioDependencia").getJsonObject("empleoCargoComisionConcluye"));
//        if (encargo.getInteger("idMovimiento") != null)
//            cumplimiento.put("idMovimiento",encargo.getInteger("idMovimiento"));
        
        //referente recepcioWeb
        cumplimiento.put("idRecepcionWeb", recepcionWebYDeclaracion.getJsonObject("recepcionWeb").getString(_ID));
        cumplimiento.put(FIELD_NO_COMPROBANTE, recepcionWebYDeclaracion.getJsonObject("recepcionWeb").getJsonObject(FIELD_DECLARACION).getString(FIELD_NO_COMPROBANTE));
        cumplimiento.put("fechaRecepcion", recepcionWebYDeclaracion.getJsonObject("recepcionWeb").getJsonObject(FIELD_DECLARACION).getString("fechaRecepcion"));
        
        //referente Declaracion ecabezado
        cumplimiento.put(FIELD_ANIO,recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getInteger(FIELD_ANIO));
        cumplimiento.put("tipoDeclaracion", recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_TIPO_DECLARACION));
        cumplimiento.put("curp", recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getJsonObject(FIELD_USUARIO).getString(FIELD_CURP));
        cumplimiento.put("idUsrDecNet", recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getJsonObject(FIELD_USUARIO).getInteger(FIELD_ID_USUARIO));
        cumplimiento.put(FIELD_NUMERO_DECLARACION, recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_NUMERO_DECLARACION));
        
        if (recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_FECHA_ENCARGO)!= null)
            cumplimiento.put(FIELD_FECHA_ENCARGO,recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getString(FIELD_FECHA_ENCARGO));
        if (recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getJsonObject(FIELD_USUARIO).getInteger("idSp")!= null)
            cumplimiento.put("idSp", recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_ENCABEZADO).getJsonObject(FIELD_USUARIO).getInteger("idSp"));
        
        //referente Declaracion ente receptor
        cumplimiento.put(FIELD_INSTITUCION_RECEPTORA, recepcionWebYDeclaracion.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_INSTITUCION_RECEPTORA));
        
        return cumplimiento;
    }
    
    private VerticalRegistrosExternos enviaDatosPublicos(JsonObject recepcionWeb, JsonArray datosEncargo, Integer posicion, Handler<AsyncResult<String>> procesado){
        if (posicion>=0){
            JsonObject declarante = recepcionWeb.getJsonObject("declarante").copy();
            declarante.put("idDependencia", datosEncargo.getJsonObject(posicion).getJsonObject("ente").getString(FIELD_ID));
            declarante.put("dependencia", datosEncargo.getJsonObject(posicion).getJsonObject("ente").getString(FIELD_NOMBRE));
            vertx.eventBus().send(
                    REG_HIST_NOMBRE_INSTITUCION, 
                    new JsonObject().put("collName", recepcionWeb.getJsonObject("institucionReceptora").getInteger("collName")).put("declarante", declarante), res ->{
                        enviaDatosPublicos(recepcionWeb, datosEncargo, posicion-1, procesado);
                    });
        }else{
            procesado.handle(Future.succeededFuture("OK"));
        }
        return this;
    }
}
