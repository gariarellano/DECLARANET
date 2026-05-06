/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "GuardaDeclaracion" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.service.impl;


import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.dao.DAOConsultaDeclaracionInterface;
import mx.gob.sfp.dgti.service.ServiceConsultaDeclaracionInterface;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_ERRORES;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_ERROR_ID;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_INCOMPLETO;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_LISTA_ERROR_MENSAJE;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_MENSAJE;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_MENSAJE_ALTERNO;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_MENSAJE_IDS_ALTERADOS;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_MODULO;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_MODULO_IDS_INCORRECTOS;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_NOMBRE_CAMPO;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_PROPIEDAD_VALOR;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_COLLNAME;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ENCABEZADO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ID_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_INSTITUCION_RECEPTORA;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_NUMERO_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.PATHERN_DIGITOS;
import mx.gob.sfp.dgti.util.IRecorreDecla;

/**
 * Clase que contien la implementaion de los metodos de logica de negocio de 
 * validadores de identificadores de la declaracion.
 * 
 * @author cgarias
 * @since 06/11/2019
 */
public class ServiceConsultaDeclracion implements ServiceConsultaDeclaracionInterface{
    private static final Logger logger = Logger.getLogger(ServiceConsultaDeclracion.class.getName());
    
    private final DAOConsultaDeclaracionInterface daoConsultaDeclaracionInterface;

    public ServiceConsultaDeclracion(){
        daoConsultaDeclaracionInterface = null;
    }
    
    public ServiceConsultaDeclracion(Vertx vertx, WebClient webClient){        
        daoConsultaDeclaracionInterface = DAOConsultaDeclaracionInterface.init(vertx, webClient);
    }
    
    @Override
    public Future<JsonObject> validaDeclaracion(JsonObject declaracion) {
        Future<JsonObject> response = Future.future();
        //<<<<<<<<<<<<<<<<<-declaración con precarga->>>>>>>>>>>>>>>>>>>
        Future<JsonArray> responseOracle = Future.future();
        Future<JsonArray>  responseMongo = Future.future();
        List<Future> listafuturos = new ArrayList<>();
        if (declaracion.containsKey("numeroDeclaracioPrecarga") && declaracion.getString("numeroDeclaracioPrecarga")!= null){
            
            daoConsultaDeclaracionInterface.consultaDeclaracionDePrecarga(obtenQuery(declaracion), declaracionHistorico ->{
               if(declaracionHistorico.succeeded() && declaracionHistorico.result() != null && !declaracionHistorico.result().isEmpty()){
                   responseOracle.handle(new ServiceConsultaDeclracion().obtenerIdentificadoresDecla(declaracionHistorico.result()));
                   listafuturos.add(0, responseOracle);
               }else{
                   logger.warning(String.format("Ocurrio un error al buscar la declaración historica Oracle. %s", declaracionHistorico.cause()));
                   responseOracle.fail(declaracionHistorico.cause());
               }
            });
        }
        //<<<<<<<<<<<<<<<<<-declaracion ya guardada en mongo->>>>>>>>>>>>>>>>>>>
        if (declaracion.getJsonObject(FIELD_ENCABEZADO).getString(FIELD_NUMERO_DECLARACION)!= null){
            daoConsultaDeclaracionInterface.consultaDeclaracion(obtenQuery(declaracion), findDecla->{
                if (findDecla.succeeded() && findDecla.result() != null && !findDecla.result().isEmpty()){
                    responseMongo.handle(new ServiceConsultaDeclracion().obtenerIdentificadoresDecla(findDecla.result()));
                    listafuturos.add(responseMongo);
                }else{
                    logger.warning(String.format("Ocurrio un error al buscar la declaración mongo. %s ", findDecla.cause()));
                    responseMongo.fail(findDecla.cause());
                }
            });
        }
            
        if (!listafuturos.isEmpty()){
            CompositeFuture.all(listafuturos).setHandler(termino->{
                Future<JsonArray> idsFront = new ServiceConsultaDeclracion().obtenerIdentificadoresDecla(declaracion);
                if(termino.succeeded() && idsFront.succeeded()){
                    boolean avisaInexistente = false;
                    JsonArray listIdFornt = (JsonArray)idsFront.result();
                    JsonArray listIdOracle = null;
                    JsonArray listIdMongo = null;

                    if (listafuturos.size() == 1 && declaracion.getString("numeroDeclaracioPrecarga")!= null){
                        listIdOracle = (JsonArray)listafuturos.get(0).result();
                    }else if (listafuturos.size() == 2){
                        listIdOracle = (JsonArray)listafuturos.get(0).result();
                        listIdMongo = (JsonArray)listafuturos.get(1).result();
                    }else{
                        listIdMongo = (JsonArray)listafuturos.get(0).result();
                    }

                    if (listIdOracle!= null){
                        for (Object index: listIdOracle){
                            if (listIdFornt.contains(index)){
                                continue;
                            }
                            logger.info("---->>>>>> NO CONTIENE el ID DE ORACLE de precarga <<<<<<<--------------");
                            logger.log(Level.INFO, "---->>>>>> VALOR DE ORACLE<<<<<<<-------------- VALOR = {0}", index);
                            logger.log(Level.INFO, "---->>>>>> VALORES QUE CONTIENE LA DECLARACION  y qu eno contiene el valor <<<<<<<-------------- VALORES = {0}", listIdFornt.toString());
                            avisaInexistente = true;
                            break;
                        }
                    }
                    if (listIdMongo!= null){
                        for (Object index: listIdFornt){
                            if (listIdMongo.contains(index)){
                                continue;
                            }
                            logger.info("---->>>>>> NO LO CONTIENE <<<<<<<--------------");
                            logger.log(Level.INFO, "---->>>>>> NO LO CONTIENE <<<<<<<-------------- VALOR = {0}", index);
                            logger.log(Level.INFO, "---->>>>>> VALORES QUE CONTIENE LA ECLARACION <<<<<<<-------------- VALORES = {0}", listIdMongo.toString());
                            avisaInexistente = true;
                            break;
                        }
                    }
                    if(!avisaInexistente){
                        JsonArray errors = new JsonArray();
                            errors.add(new JsonObject()
                                                .put(ERROR_ERROR_ID, 500)
                                                .put(ERROR_MENSAJE, ERROR_MENSAJE_IDS_ALTERADOS)
                                                .putNull(ERROR_MENSAJE_ALTERNO)
                            );
                            JsonArray errores = new JsonArray();
                            errores.add(new JsonObject()
                                                .put(ERROR_NOMBRE_CAMPO, ERROR_MENSAJE_IDS_ALTERADOS)
                                                .put(ERROR_PROPIEDAD_VALOR, ERROR_MENSAJE_IDS_ALTERADOS)
                                                .put(ERROR_LISTA_ERROR_MENSAJE, errors));

                            response.handle(
                                    Future.succeededFuture(
                                            new JsonObject()
                                                    .put(ERROR_MODULO, ERROR_MODULO_IDS_INCORRECTOS)
                                                    .put(ERROR_INCOMPLETO, false)
                                                    .put(ERROR_ERRORES, errores)));
                    }else{
                        response.handle(Future.succeededFuture(new JsonObject()));
                    }
                }else {
                    response.handle(Future.failedFuture(termino.cause()));
                }
            });
        }else {
            response.handle(Future.succeededFuture(new JsonObject()));
        }
        return response;
    }
   
    
    private Future<JsonArray> obtenerIdentificadoresDecla(JsonObject declaracion){
        Future<JsonArray> identificadoresF = Future.future();
        JsonArray identificadores = new JsonArray();
        IRecorreDecla.ExcutTrimJson(identificadores, declaracion, declaracion.fieldNames().iterator());
        identificadoresF.handle(Future.succeededFuture(identificadores));
        return identificadoresF;
    }
    
    private JsonObject obtenQuery(JsonObject declaracion){
        JsonObject query = new JsonObject();
        query.put(FIELD_COLLNAME, declaracion.getJsonObject(FIELD_INSTITUCION_RECEPTORA).getInteger(FIELD_COLLNAME))
                .put(FIELD_NUMERO_DECLARACION, declaracion.getJsonObject(FIELD_ENCABEZADO).getString(FIELD_NUMERO_DECLARACION))
                .put(FIELD_ID_USUARIO, declaracion.getJsonObject(FIELD_ENCABEZADO).getJsonObject(FIELD_USUARIO).getInteger(FIELD_ID_USUARIO));
        return query;
    }
    
    /**
     * @param cadena
     * @return
     */
    public boolean isNumber(String cadena) {
        return PATHERN_DIGITOS.matcher(cadena).matches();
    }
}