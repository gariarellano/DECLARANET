/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.appi.ip;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.appi.ip.enu.EnumPathsData;
import mx.gob.sfp.dgti.appi.ip.enu.EnumPathsEnables;
import mx.gob.sfp.dgti.appi.ip.enu.EnumPathsPermisos;

/**
 * Clase que contiene la funcionalidad para recuperar datos
 * de usuario, perfiles y permisos que le corresponden por medio
 * del token de IDP.
 * 
 * @author cgarais
 * @since 25/02/2021
 */
public class AppiExtensionPermisosSFP {
    private static final Logger logger = Logger.getLogger(AppiExtensionPermisosSFP.class.getName());
    private final AppiExtensionSFP petition;
    
    public AppiExtensionPermisosSFP(){
        petition = new AppiExtensionSFP();
    }
    
    public AppiExtensionPermisosSFP sfpPetitionToIpPermisos(
            WebClient cliente, 
            String ambiente,
            String accesToken, 
            String transaction, 
            String idCliente,
            String secretKey,
            Integer collName,
            Handler<AsyncResult<JsonObject>> respuestaPermisos) {
        petition.sfpPetitionToIpToken(cliente, EnumPathsEnables.valueOf(ambiente), accesToken, transaction, idpDat->{
            if (idpDat.succeeded()){
                
                petition.getDataUser(cliente, EnumPathsData.valueOf(ambiente.equals("REVIEW")? EnumPathsData.STAGING.name() : ambiente), idCliente, secretKey, idpDat.result().getString("curp"), usrDat ->{
                    if (usrDat.succeeded()) {
                        JsonObject jsonResponse = new JsonObject()
                                .put("identidad", idpDat.result())
                                .put("usuario", usrDat.result());
                        
                        logger.info(String.format("curl -X -H 'Authorization:%s' POST { %s } %s",
                                idpDat.result().getString("authorization"),  
                                new JsonObject(),
                                (EnumPathsPermisos.valueOf(ambiente.equals("REVIEW")? EnumPathsPermisos.PRUEBAS.name() : ambiente).getUrl().replace("#colName#", collName.toString()).replace("#sistema#", idCliente).replace("#curp#", idpDat.result().getString("curp")))));                        
                        cliente.postAbs(EnumPathsPermisos.valueOf(ambiente.equals("REVIEW")? EnumPathsPermisos.PRUEBAS.name() : ambiente).getUrl()
                                .replace("#colName#", collName.toString()).replace("#sistema#", idCliente).replace("#curp#", idpDat.result().getString("curp")))
                                .putHeader("Authorization", idpDat.result().getString("authorization"))
                                .timeout(40000)
                                .send(permisos->{
                                    if (permisos.succeeded()){                                        
                                        if (permisos.result().statusCode() == HttpResponseStatus.NO_CONTENT.code()){
                                            logger.info(String.format("El usuario %s no tiene asignaciones par ael sistema %s y colName %d",idpDat.result().getString("curp"), idCliente, collName));
                                            jsonResponse.put("authorization", permisos.result().getHeader("Authorization"));
                                            jsonResponse.put("asignaciones", new JsonObject());
                                        }else if (permisos.result().statusCode() == HttpResponseStatus.OK.code()){
                                            jsonResponse.put("authorization", permisos.result().getHeader("Authorization"));
                                            jsonResponse.put("asignaciones", permisos.result().bodyAsJsonObject());
                                        }else if (permisos.result().statusCode() == HttpResponseStatus.CONFLICT.code()){
                                            logger.info(String.format("hubo un error en la actualizacion de token para perfiles"));
                                            jsonResponse.put("authorization", permisos.result().getHeader("Authorization"));
                                            jsonResponse.put("asignaciones", new JsonObject());
                                        }else{
                                            logger.severe(String.format("Eror al responder los datos de asignacion por status %d",permisos.result().statusCode()));
                                            jsonResponse.put("asignaciones", new JsonObject());
                                        }
                                        respuestaPermisos.handle(Future.succeededFuture(jsonResponse));
                                    }else{
                                        logger.severe(String.format("Error de obtencion de Asignaciones %s", permisos.cause()));
                                        respuestaPermisos.handle(Future.failedFuture(permisos.cause()));
                                    }
                                });
                    }else{
                        logger.severe("Error al realizar la solicitud de los datos del usuario.");
                        respuestaPermisos.handle(Future.failedFuture(usrDat.cause()));
                    }
                });
            }else{
                logger.severe("Error al realizar la solicitud de los datos del usuario.");
                respuestaPermisos.handle(Future.failedFuture(idpDat.cause()));
            }
        });
        return this;
    }
}
