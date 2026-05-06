/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "GuardaDeclaracion" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.service.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.BulkOperation;
import io.vertx.ext.web.client.WebClient;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.dao.IDAOMovimientosRusp;
import mx.gob.sfp.dgti.dao.IDAONoLocDecla;
import mx.gob.sfp.dgti.dao.IDAOCumplimiento;
import mx.gob.sfp.dgti.dao.IDAOExclusion;
import mx.gob.sfp.dgti.dto.http.response.JsonObjectHttpResponse;
import mx.gob.sfp.dgti.dto.http.response.JsonObjectRespone;
import mx.gob.sfp.dgti.service.IServiceRegistraCumplimiento;
import static mx.gob.sfp.dgti.util.Constantes.EXISTE_ENTERECEPTOR_ID_ENTE;
import mx.gob.sfp.dgti.utils.FechaUtil;

/**
 * Clase que contien la implementaion de los metodos de logica de negocio de
 * validadores de identificadores de la declaracion.
 *
 * @author cgarias
 * @since 06/11/2019
 */
public class ServiceRegistraCumplimiento implements IServiceRegistraCumplimiento {

    private static final Logger logger = Logger.getLogger(ServiceRegistraCumplimiento.class.getName());
    private final Vertx vertx;
    private final WebClient webclient;
    private final IDAOCumplimiento daoCumplimiento;
    private final IDAOMovimientosRusp daoMovimientosRusp;
    private final IDAONoLocDecla daoNoLocDecla;
    private final IDAOExclusion daoExclusion;
    private static HashMap<Integer, Integer> idNivelJerarquico;       
    private final SimpleDateFormat sDFToday = new SimpleDateFormat("yyyMMdd");
    private Integer diaConsultadoEntes;
    private static HashMap<String, Integer> consultadosEnteReceptorDelDia;    
    private static final String REG_DECLA_POR_NUMERO_CUMPLIMIENTO = "REG_DECLA_POR_NUMERO_CUMPLIMIENTO";
    private static final String REGISTRAR_DECLARACION_POR_NUM_CUMPLIMIENTO = System.getenv(REG_DECLA_POR_NUMERO_CUMPLIMIENTO);
    private static final String POCES_BUSQ_EN_CUMP = "POCES_BUSQ_EN_CUMP";
    private final boolean EJECUTAR_PROCESO_BUSQUEDA_CUMPLIMIENTO = System.getenv(POCES_BUSQ_EN_CUMP) != null ? System.getenv(POCES_BUSQ_EN_CUMP).equals("true") :true;
    

    public ServiceRegistraCumplimiento(Vertx vertx, WebClient webclient) {
        this.vertx = vertx;
        this.webclient = webclient;
        daoCumplimiento = IDAOCumplimiento.init(vertx);
        daoMovimientosRusp = IDAOMovimientosRusp.init(vertx);
        daoNoLocDecla = IDAONoLocDecla.init(vertx);
        daoExclusion= IDAOExclusion.init(vertx);
        consultadosEnteReceptorDelDia = new HashMap<>();
        idNivelJerarquico = new HashMap<>();
        diaConsultadoEntes = Integer.parseInt(sDFToday.format(new Date()));
        idNivelJerarquico.put(11, 1);
        idNivelJerarquico.put(10, 2);
        idNivelJerarquico.put(9, 3);
        idNivelJerarquico.put(8, 4);
        idNivelJerarquico.put(7, 5);
        idNivelJerarquico.put(6, 6);
        idNivelJerarquico.put(5, 7);
        idNivelJerarquico.put(4, 8);
        idNivelJerarquico.put(3, 9);
        idNivelJerarquico.put(2, 10);
        idNivelJerarquico.put(1, 11);
    }

    @Override
    public IServiceRegistraCumplimiento validaYregistraCumplimientoDecla(JsonObject cumplimientoDecla, Handler<AsyncResult<JsonObject>> response) {
        //validamos que el movimiento no este en cumplimineto, de estarlo, ya no se puede hacer nada
        daoCumplimiento
                .consultaCumplimientoById(
                        cumplimientoDecla.getJsonObject("institucionReceptora").getInteger("collName"), 
                        cumplimientoDecla.getString("idDNetNoLocalizado"), 
                        sePuedeAgregar->{
                            if (sePuedeAgregar.succeeded()){
                                if (sePuedeAgregar.result()){
                                    cumplimientoDecla.put("_id",cumplimientoDecla.getString("idDNetNoLocalizado"))
                                            .put("activo", 1).put("fechaRegistro", FechaUtil.getFechaFormatoISO8601(new Date()));
                                    daoMovimientosRusp
                                            .localizaMovimiento(
                                                    cumplimientoDecla, 
                                                    cumplimientoDecla.getJsonObject("institucionReceptora").getInteger("collName"),
                                                    localizado->{
                                                        //solo se regiustra cumplimiento si y solo si el registro en cumplimiento es unico por ello el ==1.
//                                                      if (localizado.succeeded() && localizado.result().size() == 1){
                                                        //La linea anterior de codigo (if) cambia. ya que el usuario pidio que 
                                                        //al presentar su declaración independientemente de los puestos presentados en la 
                                                        //declaración, haga cumplimiento en todos los registros rusp.
                                                        if (localizado.succeeded()){
                                                            if (!localizado.result().isEmpty()){
                                                                List<BulkOperation> insertar = new ArrayList<>();
                                                                List<String> borrar = new ArrayList<>();
                                                                int x=0;
                                                                for (JsonObject index : localizado.result()){
                                                                    if ((cumplimientoDecla.getInteger("idMovimiento") != null
                                                                            && (
                                                                                index.getInteger("idSp").equals(cumplimientoDecla.getInteger("idSp")) ||
                                                                                index.getString("curp").equals(cumplimientoDecla.getString("curp")))
                                                                                )
                                                                            || cumplimientoDecla.getInteger("idMovimiento") == null) {
                                                                        JsonObject data = new JsonObject()
                                                                                .put("_id", cumplimientoDecla.getString("idDNetNoLocalizado")+(x==0 ? "" :"-"+x))
                                                                                .put("datosRusp", index)
                                                                                .put("datosDecla", cumplimientoDecla)
                                                                                .put("fechaRegistro", FechaUtil.getFechaFormatoISO8601(new Date()))
                                                                                .put("activo", 1);

                                                                        daoCumplimiento.isExtemporaneo(data);
                                                                        insertar.add(BulkOperation.createInsert(data));
                                                                        borrar.add(index.getString("_id"));
                                                                        x++;
                                                                    }
                                                                }
                                                                if (!insertar.isEmpty()){
                                                                    daoCumplimiento.registraCumplimiento(cumplimientoDecla, insertar, guardado ->{
                                                                        if (guardado.succeeded()){
                                                                            this.eliminaDatosRusp(
                                                                                    cumplimientoDecla.getJsonObject("institucionReceptora").getInteger("collName"),
                                                                                    borrar,
                                                                                    response);
                                                                        }else{
                                                                            logger.severe(String.format("El guardado del registro en cumplimeinto fallo. D->C %s", guardado.cause()));
                                                                            response.handle(
                                                                                    Future.succeededFuture(
                                                                                            new JsonObjectHttpResponse(
                                                                                                    HttpResponseStatus.CONFLICT.code(),
                                                                                                    new JsonObjectRespone("FAIL", "El guardado del registro en cumplimeinto fallo."))));
                                                                        }
                                                                    });
                                                                }else{
                                                                    this.guardaNolocalizadoDecla(cumplimientoDecla, response);
                                                                }
                                                            }else{
                                                                if (EJECUTAR_PROCESO_BUSQUEDA_CUMPLIMIENTO){
                                                                    daoCumplimiento
                                                                            .localizaMovimiento(
                                                                                    cumplimientoDecla.getJsonObject("institucionReceptora").getInteger("collName"),
                                                                                    cumplimientoDecla, 
                                                                                    cumplimientoLocalizado->{
                                                                                        if (cumplimientoLocalizado.succeeded() && cumplimientoLocalizado.result().size()==1){
                                                                                            JsonObject datosReusp = cumplimientoLocalizado.result().get(0).getJsonObject("datosRusp").copy();
                                                                                            daoCumplimiento.registraCumplimiento(cumplimientoDecla, datosReusp, insertado->{
                                                                                                if (insertado.succeeded()){
                                                                                                    daoCumplimiento.eliminaCumplimiento(
                                                                                                            cumplimientoDecla.getJsonObject("institucionReceptora").getInteger("collName"), 
                                                                                                            cumplimientoLocalizado.result().get(0), response);
                                                                                                }else{
                                                                                                    this.guardaNolocalizadoDecla(cumplimientoDecla, response);
                                                                                                }
                                                                                            });
                                                                                        }else{
                                                                                            this.guardaNolocalizadoDecla(cumplimientoDecla, response);
                                                                                        }
                                                                                    });
                                                                }else{
                                                                    this.guardaNolocalizadoDecla(cumplimientoDecla, response);
                                                                    response.handle(
                                                                            Future.succeededFuture(
                                                                                    new JsonObjectHttpResponse(
                                                                                            HttpResponseStatus.OK.code(),
                                                                                            new JsonObjectRespone("OK", "Pro-Bus-Apagado-Registro guarado-nld"))));
                                                                }
                                                            }
                                                        }else{
                                                            logger.severe(String.format("La busqueda en datos rusp fallo %s", localizado.cause()));
                                                            response.handle(
                                                                    Future.succeededFuture(
                                                                            new JsonObjectHttpResponse(
                                                                                    HttpResponseStatus.CONFLICT.code(),
                                                                                    new JsonObjectRespone("FAIL", "La busqueda en datos rusp fallo"))));
                                                        }
                                                    });
                                }else{
                                    response.handle(
                                            Future.succeededFuture(
                                                    new JsonObjectHttpResponse(
                                                            HttpResponseStatus.FOUND.code(),
                                                            new JsonObjectRespone("FOUND", "El movimiento ya esta en cunplimiento, por tal motivo no se edita"))));
                                }
                            }else{
                                logger.severe("Fallo el realizar la consulta de cumplimiento por decla");
                                response.handle(
                                            Future.succeededFuture(
                                                    new JsonObjectHttpResponse(
                                                            HttpResponseStatus.INTERNAL_SERVER_ERROR.code(),
                                                            new JsonObjectRespone("ERROR", "No se logro consultar cumplimiento."))));
                            }
                        });
        return this;
    }
    
    private void guardaNolocalizadoDecla(JsonObject cumplimientoDecla, Handler<AsyncResult<JsonObject>> response){
        daoNoLocDecla.guardaNoLocalizadoDecla(cumplimientoDecla, guardadoEnNld->{
                    if (guardadoEnNld.succeeded()){
//                        logger.info("Se registro no localizados de Decla.");
                        response.handle(
                                Future.succeededFuture(
                                        new JsonObjectHttpResponse(
                                                HttpResponseStatus.OK.code(),
                                                new JsonObjectRespone("OK", "Registro guarado-nld"))));
                    }else{
                        logger.severe(String.format("El guardado del registro en rusp fallo. %s", guardadoEnNld.cause()));
                        response.handle(
                                Future.succeededFuture(
                                        new JsonObjectHttpResponse(
                                                HttpResponseStatus.CONFLICT.code(),
                                                new JsonObjectRespone("FAIL", "El guardado del registro fallo en decla."))));
                    }
                });
    }
    
    private void eliminaDatosRusp(Integer collName, List<String> datosRusp, Handler<AsyncResult<JsonObject>> response){
        daoMovimientosRusp.eliminaDatosRusp(collName, datosRusp, eliminado->{
            if (eliminado.succeeded()){
                response.handle(
                        Future.succeededFuture(
                                new JsonObjectHttpResponse(
                                        HttpResponseStatus.OK.code(),
                                        new JsonObjectRespone("OK", "Registro guarado-cump"))));
            }
        });
    }
    
    @Override
    public IServiceRegistraCumplimiento validaYregistraCumplimientoRusp(JsonObject movimientoRusp, Handler<AsyncResult<JsonObject>> response) {
        movimientoRusp.put("idNivelJerarquico", idNivelJerarquico.get(movimientoRusp.getInteger("idNivelJerarquico")));
        
        this.consultaEnteReceptorToday(movimientoRusp.getString("idEnte"), enteReceptor -> {                  
            if (enteReceptor.succeeded()) {                        
                logger.info("termino cargaenteToday correctamente");
                String idNoLocalizadoRusp = movimientoRusp.getString("tipoObligacion") + movimientoRusp.getInteger("idMovimiento");
                movimientoRusp.put("_id", idNoLocalizadoRusp).put("id", idNoLocalizadoRusp).put("activo", 1).put("fechaRegistro", FechaUtil.getFechaFormatoISO8601(new Date()));
                movimientoRusp.put("nombreCompleto",new StringBuilder(movimientoRusp.getString("nombres")).append(" ").append(movimientoRusp.getString("primerApellido")).append(" ").append(movimientoRusp.getString("segundoApellido")!= null && !movimientoRusp.getString("segundoApellido").isEmpty() ? movimientoRusp.getString("segundoApellido"):"").toString().trim());
                Integer collName = enteReceptor.result();

                daoExclusion.existeMovimientoExcluido(collName, idNoLocalizadoRusp, encontrado->{
                    if (encontrado.succeeded()){
                        logger.info("busca en escluidos y no lo encontro.");
                        if (null == encontrado.result() || encontrado.result().isEmpty()){
                            daoCumplimiento.consultaCumplimientoByIdRusp(collName, idNoLocalizadoRusp, sepuedeAgregar->{
                                if (sepuedeAgregar.succeeded()){
                                    logger.info(String.format("lo busco en cumplimiento y no lo encontro, sepuede agregar? %s", sepuedeAgregar.result().toString()));
                                    if (sepuedeAgregar.result()){
                                        logger.info("va a consultar localizaMovimiento");
                                        daoNoLocDecla.
                                                localizaMovimiento(collName, movimientoRusp, localizados->{
                                                    if (localizados.succeeded() && localizados.result().size()==1){
                                                        logger.info("localizo 1 ");
                                                        daoCumplimiento.registraCumplimiento(localizados.result().get(0), movimientoRusp, guardado ->{
                                                            if (guardado.succeeded()){
                                                                if ("NO_CORRESPONDE_MOVIMIENTO".equals(guardado.result())){
                                                                    this.guardaNolocalizadoRusp(collName, movimientoRusp, response);
                                                                }else if ("OK".equals(guardado.result())){
                                                                    this.eliminaDatosDecla(collName, localizados.result().get(0), response);
                                                                }
                                                            }else{
                                                                logger.severe(String.format("El guardado del registro en cumplimeinto fallo. R->C %s", guardado.cause()));
                                                                response.handle(
                                                                        Future.succeededFuture(
                                                                                new JsonObjectHttpResponse(
                                                                                        HttpResponseStatus.CONFLICT.code(),
                                                                                        new JsonObjectRespone("FAIL", "El guardado del registro en cumplimeinto fallo."))));
                                                            }
                                                        });
                                                    }else{
                                                        if (localizados.succeeded()){
                                                            logger.info("localizo varios para el que enviar rusp");
                                                            this.guardaNolocalizadoRusp(collName, movimientoRusp, response);
                                                        }else{
                                                            logger.severe(String.format("La busqueda en datos en decla fallo %s", localizados.cause()));
                                                            response.handle(
                                                                    Future.succeededFuture(
                                                                            new JsonObjectHttpResponse(
                                                                                    HttpResponseStatus.CONFLICT.code(),
                                                                                    new JsonObjectRespone("FAIL", "La busqueda en datos en decla fallo"))));
                                                        }
                                                    }
                                                });

                                    }else{
                                        logger.info("se responde que el movimiento ya esta en cumplimiento");
                                        response.handle(
                                                Future.succeededFuture(
                                                        new JsonObjectHttpResponse(
                                                                HttpResponseStatus.ACCEPTED.code(),
                                                                new JsonObjectRespone("ACCEPTED", "El movimiento ya esta en cunplimiento, por tal motivo no se edita"))));
                                    }
                                }else{
                                    logger.severe("Fallo el realizar la consulta de cumplimiento por rusp");
                                    response.handle(
                                            Future.succeededFuture(
                                                    new JsonObjectHttpResponse(
                                                            HttpResponseStatus.CONFLICT.code(),
                                                            new JsonObjectRespone("FAIL", "Fallo el realizar la consulta de cumplimiento por rusp"))));
                                }
                            });
                        }else {
                            response.handle(
                                Future.succeededFuture(
                                        new JsonObjectHttpResponse(
                                                HttpResponseStatus.ACCEPTED.code(),
                                                new JsonObjectRespone("ACCEPTED", "El movimiento esta excluido, por tal motivo no se edita"))));
                        }
                    }else{
                        logger.severe("Fallo el realizar la validacion de excluido");
                        response.handle(
                                Future.succeededFuture(
                                        new JsonObjectHttpResponse(
                                                HttpResponseStatus.CONFLICT.code(),
                                                new JsonObjectRespone("FAIL", "Fallo el realizar la validacion de exclusion"))));
                    }
                });
            } else {
                response.handle(
                    Future.succeededFuture(
                            new JsonObjectHttpResponse(
                                    HttpResponseStatus.INTERNAL_SERVER_ERROR.code(),
                                    new JsonObjectRespone("FAIL", "Fallo el realizar la consulta de cumplimiento por rusp"))));
            }
        });
        return this;
    }
    
    private ServiceRegistraCumplimiento consultaEnteReceptorToday(String idEnte, Handler<AsyncResult<Integer>> resultado){
        if (diaConsultadoEntes != Integer.parseInt(sDFToday.format(new Date()))){
            consultadosEnteReceptorDelDia = new HashMap<>();
            diaConsultadoEntes = Integer.parseInt(sDFToday.format(new Date()));
            logger.info("Refresco la lista de entes por dia");
        }
        
        if (consultadosEnteReceptorDelDia.containsKey(idEnte)){
            logger.info("se localizo el ente en la lista del dia.");
            resultado.handle(Future.succeededFuture(consultadosEnteReceptorDelDia.get(idEnte)));
        }else{
            logger.info("se ira a consultar a entes");
            webclient.getAbs(String.format(EXISTE_ENTERECEPTOR_ID_ENTE, idEnte))
                    .timeout(50000).putHeader("Content-Type", "application/json")
                    .send(enteReceptor->{
                        if (enteReceptor.succeeded() && enteReceptor.result().statusCode() == HttpResponseStatus.OK.code()) {
                            JsonObject enteReceptorObj = enteReceptor.result().bodyAsJsonObject();
                            consultadosEnteReceptorDelDia.put(idEnte, (enteReceptorObj.isEmpty() ? 100 : enteReceptor.result().bodyAsJsonObject().getInteger("collName")));
                            resultado.handle(Future.succeededFuture(enteReceptorObj.isEmpty() ? 100 : enteReceptor.result().bodyAsJsonObject().getInteger("collName")));
                        }else {
                            if (enteReceptor.succeeded() && enteReceptor.result().statusCode() != 200) {
                                logger.severe(String.format("Error de estatus code %d", enteReceptor.result().statusCode()));
                            }
                            if (!enteReceptor.succeeded()) {
                                logger.severe(String.format("Cause %s", enteReceptor.cause()));
                            }
                            logger.severe(String.format("Error al consultar: curl -X GET %s", String.format(EXISTE_ENTERECEPTOR_ID_ENTE, idEnte)));
                            resultado.handle(Future.failedFuture("Ocurrio un errror al realizar petición a ente receptor."));
                        }
                    });
        }
        return this;
    }
    
    private void eliminaDatosDecla(Integer collName, JsonObject datosDecla, Handler<AsyncResult<JsonObject>> response){
        daoNoLocDecla.eliminaDatosDecla(collName, datosDecla, eliminado->{
            logger.info(String.format("Eliminando datos de nolocalizadosd %d %s", collName, datosDecla));
            if (eliminado.succeeded()){
                response.handle(
                        Future.succeededFuture(
                                new JsonObjectHttpResponse(
                                        HttpResponseStatus.OK.code(),
                                        new JsonObjectRespone("OK", "Registro guarado-cump"))));
            }else{
                logger.severe(String.format("no se pudo eliminar el registro de datosRusp _id = %s", datosDecla.getString("_id")));
            }
        });
    }

    private void guardaNolocalizadoRusp(Integer collName, JsonObject movimientoRusp, Handler<AsyncResult<JsonObject>> response){
        daoMovimientosRusp.guardaNoLocalizado(collName, movimientoRusp, guardadoEnNlr->{
            if (guardadoEnNlr.succeeded()){
                logger.info("Se registro no localizados de Rusp.");
                response.handle(
                        Future.succeededFuture(
                                new JsonObjectHttpResponse(
                                        HttpResponseStatus.OK.code(),
                                        new JsonObjectRespone("OK", "Registro guarado-nlr"))));
            }else{
                logger.severe(String.format("El guardado del registro en rusp fallo. %s", guardadoEnNlr.cause()));
                response.handle(
                        Future.succeededFuture(
                                new JsonObjectHttpResponse(
                                        HttpResponseStatus.CONFLICT.code(),
                                        new JsonObjectRespone("FAIL", "El guardado del registro en rusp fallo."))));
            }
        });
    }

    @Override
    public IServiceRegistraCumplimiento consultaObligacion(String noComprobante, String collNameS, Integer ejecuciones, Handler<AsyncResult<JsonObject>> handleResultHttp) {
        Integer collName = Integer.parseInt(collNameS);     
        daoNoLocDecla.localizaMovimientoPorNoComprobante(collName, noComprobante, respuesta->{
            if (respuesta.succeeded()){
                if (!respuesta.result().isEmpty()){
                    handleResultHttp.handle(Future.succeededFuture(
                            new JsonObjectHttpResponse(
                                    HttpResponseStatus.OK.code(), 
                                    new JsonObjectRespone(respuesta.result().size(), respuesta.result())
                            )
                    ));
                }else{
                    daoCumplimiento.localizaMovimientoPorNoComprobante(collName, noComprobante, localizado->{
                        if (localizado.succeeded()){
                            if (!localizado.result()){
                                if (ejecuciones > 0){
                                    JsonObject generaReporte = new JsonObject().put("collName", collName).put("noComprobante", noComprobante);
                                        webclient.postAbs(REGISTRAR_DECLARACION_POR_NUM_CUMPLIMIENTO)
                                                .timeout(2000)
                                                .sendJsonObject(generaReporte, procesaDecla ->{
                                                    if (procesaDecla.failed()){
                                                        logger.info(String.format("Error al llamado de  ERROR : %s %n curl -X POST -d '%s' %s", procesaDecla.cause(), generaReporte, REGISTRAR_DECLARACION_POR_NUM_CUMPLIMIENTO));
                                                    }
                                                });
                                        vertx.setTimer(3000, id ->{
                                            consultaObligacion(noComprobante, collNameS, ejecuciones-1, handleResultHttp);
                                        });
                                }else{
                                    handleResultHttp.handle(
                                        Future.succeededFuture(
                                                new JsonObjectHttpResponse(
                                                        HttpResponseStatus.NO_CONTENT.code(), 
                                                        new JsonObjectRespone(HttpResponseStatus.NOT_FOUND.code(), "El comprobante proporcionado no existe."))));
                                }
                            }else{
                                if (ejecuciones==2){
                                    handleResultHttp.handle(
                                            Future.succeededFuture(
                                                    new JsonObjectHttpResponse(
                                                            HttpResponseStatus.FOUND.code(), 
                                                            new JsonObjectRespone(HttpResponseStatus.FOUND.code(), "El comprobante proporcionado se encuentra actualmente en cumplimiento."))));
                                }else{
                                    handleResultHttp.handle(
                                            Future.succeededFuture(
                                                    new JsonObjectHttpResponse(
                                                            HttpResponseStatus.ACCEPTED.code(), 
                                                            new JsonObjectRespone(HttpResponseStatus.ACCEPTED.code(), "Se registro el cumplimiento exitosamente."))));
                                }
                            }
                        }else{
                             logger.severe(String.format("Errro al realizar la consulta de localizar cumplimiento por Comprobante %s", localizado.cause()));
                             handleResultHttp.handle(Future.succeededFuture(new JsonObjectHttpResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), null)));
                        }
                    });
                }
            }else{
                logger.severe(String.format("Errro al realizar la consulta de localizar por Comprobante %s", respuesta.cause()));
                handleResultHttp.handle(Future.succeededFuture(new JsonObjectHttpResponse(HttpResponseStatus.CONFLICT.code(), null)));
            }
        });  
        return this;
    }
    
    @Override
    public IServiceRegistraCumplimiento registrarCumplimientoManual(String idNoLocDecla, String idRusp, String sCollName, JsonObject body, Handler<AsyncResult<JsonObject>> handleResultHttp) {
        Integer collName = Integer.parseInt(sCollName);        
        
        if (validaDatosBodyCumplimieto(body)){
            Future<JsonObject> fLocalizadoD = Future.future(enFuture->{
                daoNoLocDecla.localizaMovimientoById(collName, idNoLocDecla, localizadoD ->{
                    enFuture.handle(localizadoD);
                });
            });    
            Future<JsonObject> fLocalizadoR = Future.future(enFuture->{
                daoMovimientosRusp.localizaMovimientoById(collName, idRusp, localizadoR->{
                    enFuture.handle(localizadoR);
                });
            });

            CompositeFuture.all(fLocalizadoD, fLocalizadoR).setHandler(completados->{
                if (completados.succeeded()){
                    if (validaTipoMovYtipoDecla(fLocalizadoD.result(), fLocalizadoR.result())){
                        JsonObject data = 
                                new JsonObject()
                                        .put("_id", idNoLocDecla).put("datosRusp", fLocalizadoR.result())
                                        .put("datosDecla", fLocalizadoD.result()).put("deteccionCumplimiento", "manual")
                                        .put("motivo", body).put("activo", 1)
                                        .put("fechaRegistro", FechaUtil.getFechaFormatoISO8601(new Date()));
                        asignaCalificacion(collName, data);
                        daoCumplimiento.guardaCumplimientoManual(collName, data, guardado->{
                            if (guardado.succeeded()){
                                JsonObject inactivoF = fLocalizadoD.result().copy().put("activo", 0);
                                daoNoLocDecla.guardaNoLocalizadoDecla(inactivoF, actualizadoD->{
                                    if(actualizadoD.succeeded()){
                                        daoNoLocDecla.eliminaDatosDecla(collName, inactivoF, eliminado->{
                                            if (eliminado.succeeded()){}
                                        });
                                    }
                                });
                                JsonObject inactivoR = fLocalizadoR.result().copy().put("activo", 0);
                                daoMovimientosRusp.guardaNoLocalizado(collName, inactivoR, actualizadoR->{
                                    if(actualizadoR.succeeded()){
                                        daoMovimientosRusp.eliminaDatosRusp(collName, new ArrayList<>(Arrays.asList(inactivoR.getString("_id"))), eliminado->{
                                            if (eliminado.succeeded()){}
                                        });
                                    }
                                });
                                handleResultHttp.handle(Future.succeededFuture(
                                    new JsonObjectHttpResponse(
                                            HttpResponseStatus.OK.code(), 
                                            new JsonObjectRespone("OK","Se registro el cumplimiento correctamente.")
                                    )
                                ));
                            }else{
                                logger.severe("Error al realizar guardado de cumplimiento manual");
                                handleResultHttp.handle(
                                Future.succeededFuture(
                                        new JsonObjectHttpResponse(
                                                HttpResponseStatus.CONFLICT.code(), 
                                                new JsonObjectRespone(HttpResponseStatus.CONFLICT.code(), "Se tubo conflicto tecnico en el guardado del cumplimiento manual"))));

                            }
                        });
                    }else{
                        logger.severe("El tipo de movimiento no coincide, tipoOperacion y tipoDeclaraion.");
                        handleResultHttp.handle(
                                Future.succeededFuture(
                                        new JsonObjectHttpResponse(
                                                HttpResponseStatus.METHOD_NOT_ALLOWED.code(), 
                                                new JsonObjectRespone(HttpResponseStatus.CONFLICT.code(), "El tipo de movimiento no coincide, tipoObligacion y tipoDeclaraion."))));
                    }
                }else{
                    logger.severe(String.format("Error al consultar daotos de %s", completados.cause()));
                    handleResultHttp.handle(
                                Future.succeededFuture(
                                        new JsonObjectHttpResponse(
                                                HttpResponseStatus.NO_CONTENT.code(), 
                                                new JsonObjectRespone(HttpResponseStatus.NOT_FOUND.code(), "No se localizaron los registros."))));
                }
            });
        }else{
            logger.severe("Error de validacion en datos del body.");
            handleResultHttp.handle(
                        Future.succeededFuture(
                                new JsonObjectHttpResponse(
                                        HttpResponseStatus.BAD_REQUEST.code(), 
                                        new JsonObjectRespone(HttpResponseStatus.BAD_REQUEST.code(), "Parametros incorrectos body"))));
            
        }
        return this;
    }
    
    /**
     * Método que realiza la validacion de datos de body para registrar un cumplimiento.
     * 
     * @param body objetos que se reciben del request.
     * @return bandera que indica si las validaciones son correctas.
     */
    private Boolean validaDatosBodyCumplimieto(JsonObject body){
        Boolean validacion = Boolean.TRUE;
        if (body.containsKey("usuario") && body.getJsonObject("estatus") != null && body.getJsonObject("estatus").containsKey("id")){
            if ("2".equals(body.getJsonObject("estatus").getValue("id").toString()) && 
                    (body.getString("comentarios") == null || body.getString("comentarios").trim().isEmpty())){
                validacion = Boolean.FALSE;
            }
        }else{
            validacion = Boolean.FALSE;
        }
        return validacion;
    }
    
    private Boolean validaTipoMovYtipoDecla(JsonObject localizadosD, JsonObject localizadosR){
        Boolean esIgual;
        switch(localizadosD.getString("tipoDeclaracion")){
            case "INICIO":
                esIgual = "ALTA".equals(localizadosR.getString("tipoObligacion"));
                break;
            case "CONCLUSION":
                esIgual = "BAJA".equals(localizadosR.getString("tipoObligacion"));
                break;
            case "MODIFICACION":
                esIgual = "ACTIVO_MAYO".equals(localizadosR.getString("tipoObligacion"));
                if (!Objects.equals(localizadosD.getInteger("anio"), localizadosR.getInteger("anio"))){
                    esIgual = false;
                }
                break;    
            default:
                esIgual = false;
                break;
        }
        return esIgual;
    }
    
    private void asignaCalificacion(Integer collName, JsonObject data){
        
        if (!data.getJsonObject("datosRusp").getString("curp").equals(data.getJsonObject("datosDecla").getString("curp"))){
            data.put("diferenciaCurp", Boolean.TRUE);
        }
        Future<Boolean> enCumplimiento = Future.future(esteFuture->{
                daoCumplimiento.localizaMovimientoPorNoComprobante(collName, data.getJsonObject("datosDecla").getString("noComprobante"), localizado->{
                    esteFuture.handle(localizado);
                });
        });
        
        Future<List<JsonObject>> enNolocalizadosD = Future.future(esteFuture->{
                daoNoLocDecla.localizaMovimientoPorNoComprobante(collName, data.getJsonObject("datosDecla").getString("noComprobante"), localizado->{
                    esteFuture.handle(localizado);
                });
        });
        
        CompositeFuture.all(enCumplimiento, enNolocalizadosD).setHandler(complete->{
            if (complete.succeeded()){
                if (enCumplimiento.result() && !enNolocalizadosD.result().isEmpty()){
                    data.put("masDeUnMovimiento", Boolean.TRUE);
                }    
            }else{
                logger.warning("Error, no se logro calificar el mas de un movimiento por noComprobante.");
            }
        });
    }
    
    @Override
    public IServiceRegistraCumplimiento registrarExclusionManual(String idRusp, String sCollName, JsonObject body, Handler<AsyncResult<JsonObject>> handleResultHttp){
        Integer collName = Integer.parseInt(sCollName);
        if (validaDatosBodyCumplimieto(body)){
            daoMovimientosRusp.localizaMovimientoById(collName, idRusp, localizadoR->{
                if (localizadoR.succeeded()){
                    JsonObject data = 
                            new JsonObject()
                                    .put("_id", idRusp).put("datosRusp", localizadoR.result())
                                    .put("exclusion", "manual").put("motivo", body)
                                    .put("activo", 1).put("fechaRegistro", FechaUtil.getFechaFormatoISO8601(new Date()));
                    daoExclusion.registraExclusion(sCollName, data, guardado->{
                        if (guardado.succeeded()){
                            daoMovimientosRusp.guardaNoLocalizado(collName, localizadoR.result().copy().put("activo",0) , actualizado->{
                                if (actualizado.succeeded()){                                    
                                    daoMovimientosRusp.eliminaDatosRusp(collName, new ArrayList<>(Arrays.asList(localizadoR.result().getString("_id"))), eliminado->{});
                                    handleResultHttp.handle(Future.succeededFuture(
                                            new JsonObjectHttpResponse(
                                                    HttpResponseStatus.OK.code(), 
                                                    new JsonObjectRespone("OK","Se registro la exclusión correctamente.")
                                            )
                                    ));
                                }else{
                                    logger.severe(String.format("Errro al realizar la actualizacion de datos rusp inactivo %s", actualizado.cause()));
                                    handleResultHttp.handle(Future.succeededFuture(new JsonObjectHttpResponse(HttpResponseStatus.CONFLICT.code(), null)));   
                                }                                
                            });
                            
                        }else{
                            logger.severe(String.format("Errro al realizar el guardado de exclusion %s", guardado.cause()));
                            handleResultHttp.handle(Future.succeededFuture(new JsonObjectHttpResponse(HttpResponseStatus.CONFLICT.code(), null)));
                        }
                    });
                }else{
                    logger.severe(String.format("Error al consultar de datos rusp para localizar el movimiento y generar exclusion %s", localizadoR.cause()));
                    handleResultHttp.handle(
                                Future.succeededFuture(
                                        new JsonObjectHttpResponse(
                                                HttpResponseStatus.NO_CONTENT.code(), 
                                                new JsonObjectRespone(HttpResponseStatus.NOT_FOUND.code(), "No se localizo el registro a excluir"))));
                    
                }
            });
        }else {
            logger.severe("Error de validacion en datos del body para exclusion.");
            handleResultHttp.handle(
                        Future.succeededFuture(
                                new JsonObjectHttpResponse(
                                        HttpResponseStatus.BAD_REQUEST.code(), 
                                        new JsonObjectRespone(HttpResponseStatus.BAD_REQUEST.code(), "Parametros incorrectos para realizar exlusión"))));
        }
        return this;
    }
//    @Override
//    public IServiceRegistraCumplimiento revierteCumplimientoManual(String idNoLocDecla, String idRusp, String sCollName, JsonObject usuario, Handler<AsyncResult<JsonObject>> handleResultHttp) {
//        Integer collName = Integer.valueOf(sCollName);
//        obtenSourceParaCumplimientoManual(
//                String.format(NO_LOCALIZADOS_DECLA_FIND,collName), 
//                QuerySourcesConstant.getQueryInactivoTerm(new JsonObject().put("_id", idNoLocDecla)), 
//                resultNoLocDecla->{
//                    if (resultNoLocDecla.succeeded()){
//                        obtenSourceParaCumplimientoManual(
//                                String.format(MOVIMIENTOS_RUSP_FIND,collName), 
//                                QuerySourcesConstant.getQueryInactivoTerm(new JsonObject().put("_id", idRusp)), 
//                                resulNoLocRusp->{
//                                   if (resulNoLocRusp.succeeded()){
//                                        JsonObject data = new JsonObject().put("datosRusp", resultNoLocDecla.result()).put("datosDecla", resulNoLocRusp.result());
//                                        data.put("deteccionCumplimiento", "manual").put("usaurio", usuario);
//                                        data.put("activo", 0).put("fechaRegistro", FechaUtil.getFechaFormatoISO8601(new Date()));
//                                        webclient.postAbs(String.format(CUMPLIMIENTO, collName, idNoLocDecla))
//                                                .timeout(50000)
//                                                .sendJsonObject(data,
//                                                        registradoCump -> {
//                                                            if (registradoCump.succeeded() && registradoCump.result().statusCode() == HttpResponseStatus.CREATED.code()
//                                                            || registradoCump.succeeded() && registradoCump.result().statusCode() != HttpResponseStatus.OK.code()) {
//                                                                eliminaNoLocalizado(
//                                                                        String.format(
//                                                                                MOVIMIENTOS_RUSP_IN,
//                                                                                collName,
//                                                                                idRusp),
//                                                                        resulNoLocRusp.result().put("activo", 1), r->{
//                                                                            logger.info(String.format("actualiza rusp to activo = 1 %s ",r.result()));
//                                                                        });
//                                                                eliminaNoLocalizado(
//                                                                        String.format(
//                                                                                NO_LOCALIZADOS_DECLA_IN,
//                                                                                collName,
//                                                                                idNoLocDecla),
//                                                                        resultNoLocDecla.result().put("activo", 1), r->{
//                                                                            logger.info(String.format("actualiza rusp to activo = 1 %s ",r.result()));
//                                                                        });
//                                                                
//                                                                logger.info("Se registro Cumplimiento Manual correctamente");
//                                                                handleResultHttp.handle(
//                                                                        Future.succeededFuture(
//                                                                                new JsonObjectHttpResponse(
//                                                                                        HttpResponseStatus.CREATED.code(), 
//                                                                                        new JsonObjectRespone(HttpResponseStatus.OK.code(), "Creado correctamente"))));
//                                                                
//                                                            }else {
//                                                                if (registradoCump.succeeded() && registradoCump.result().statusCode() != HttpResponseStatus.OK.code()) {
//                                                                    logger.severe(String.format("ElasticSerch response code: %s", registradoCump.result().statusCode()));
//                                                                    handleResultHttp.handle(
//                                                                            Future.succeededFuture(
//                                                                                    new JsonObjectHttpResponse(registradoCump.result().statusCode(), 
//                                                                                            new JsonObjectRespone(registradoCump.result().statusCode(), "Error al crear el cumplimiento"))));
//                                                                }else {
//                                                                    logger.severe(String.format("Error al crear cumplimiento : %s", registradoCump.cause()));
//                                                                    handleResultHttp.handle(
//                                                                            Future.succeededFuture(
//                                                                                    new JsonObjectHttpResponse(HttpResponseStatus.CONFLICT.code(), 
//                                                                                            new JsonObjectRespone(registradoCump.result().statusCode(), "Conflicto al crear el cumplimiento"))));
//                                                                }
//                                                            }
//                                                        });
//                                   } else{
//                                       logger.warning(String.format("Error en localizar el id en nolocalizadosr%d  id: %s", collName, idRusp.toLowerCase()));
//                                       handleResultHttp.handle(
//                                               Future.succeededFuture(
//                                                       new JsonObjectHttpResponse(
//                                                               Integer.valueOf(resulNoLocRusp.cause().getMessage()), 
//                                                               new JsonObjectRespone(Integer.valueOf(resulNoLocRusp.cause().getMessage()), String.format("Error en localizar el idrusp %s",idRusp.toLowerCase())))));
//                                   }
//                                });
//                    }else{
//                        logger.warning(String.format("Error en localizar el nolocalizadosd%d  id: %s", collName, idNoLocDecla));
//                        handleResultHttp.handle(
//                                               Future.succeededFuture(
//                                                       new JsonObjectHttpResponse(
//                                                               Integer.valueOf(resultNoLocDecla.cause().getMessage()), 
//                                                               new JsonObjectRespone(Integer.valueOf(resultNoLocDecla.cause().getMessage()), "Error en localizar el objeto declaranet."))));
//                    }
//                });
//        return this;
//    }
}
