/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.declaracion.encrypt.im;

import io.vertx.core.json.JsonObject;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que realiza el generado de 
 * codigo verificador para el front.
 *  
 * @author cgarias
 * @since 06/12/2019
 */
public class GeneraMensajeFront {
    private static final Logger logger = Logger.getLogger(GeneraMensajeFront.class.getName());
    private static final String FIELD_DECLARACION = "declaracion";
    private static final String DIGITO_VERIFICADOR = "digitoVerificador";
    private static final String FIELD_ID_USUARIO = "idUsuario";
    private static final String FIEL_ENCABEZADO = "encabezado";
    private static final String FIEL_USUARIO = "usuario";

    public static JsonObject fidelidadMensaje(JsonObject jsonMessage) {
        JsonObject response = null;
        try{
            if (jsonMessage != null){
                if (jsonMessage.containsKey(FIELD_DECLARACION)){
                    response = new GeneraMensajeFront().esDeclaracion(jsonMessage);
                }else{
                    response = new GeneraMensajeFront().esObjetoGenerico(jsonMessage);
                }
            }else{
                logger.log(Level.SEVERE, "Objeto entrada es null", 
                        new Object[]{jsonMessage});
            }
        }catch(ClassCastException e){
            logger.log(Level.SEVERE, "Json recibido con parametros incorrectos \r\n ***********{0} debe ser Objeto y {1} debe ser String ************", new Object[]{FIELD_DECLARACION, DIGITO_VERIFICADOR});
        }
        return response;
    }
    
    private JsonObject esDeclaracion(JsonObject jsonMessage){
        JsonObject jsonParaEncript = new JsonObject()
                                        .put(FIELD_ID_USUARIO, jsonMessage.getJsonObject(FIEL_ENCABEZADO).getJsonObject(FIEL_USUARIO).getInteger(FIELD_ID_USUARIO))
                                        .put(FIELD_DECLARACION, jsonMessage);
//        logger.log(Level.INFO, "Se genera mensaje validado es declaracion como siguietne ejemplo {declaracion:{idUsurio : valorInteger, declaracion : {OBJEntrante} }, digitoVerificador:digestionDe(idUsurio : valorInteger, declaracion : {OBJEntrante})}");
        return this.generaDigitoVerificador(jsonMessage, jsonParaEncript);
    }
    
    private JsonObject esObjetoGenerico(JsonObject jsonMessage){            
//        logger.log(Level.INFO, "Se genera mensaje validado es generico como siguietne ejemplo {declaracion:{OBJEntrante}, digitoVerificador: digestionDe({OBJEntrante})}");
        return this.generaDigitoVerificador(jsonMessage, jsonMessage);
    }
    
    /**
     * Método que genera el digito verificador para 
     * la respuesta para el front.
     * 
     * @param json Objeto a generar el digito verificador.
     * @return String digito verificador.
     */
    private JsonObject generaDigitoVerificador(JsonObject jsonInput, JsonObject jsonParaEncript){
//        logger.log(Level.INFO, "Se encapsula respuesta");
        return new JsonObject()
                .put(FIELD_DECLARACION, jsonInput)
                .put(DIGITO_VERIFICADOR, Hex.toHexString(new SHA3.Digest512().digest(jsonParaEncript.toString().getBytes())));
    }
}
