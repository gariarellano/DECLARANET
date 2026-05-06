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
 * Clase que realiza la validacion del mensaje dle front que llegue completo 
 * y con su respuesta correcta.
 * 
 * @author cgarias
 * @since 22/11/2019
 */
public class ValidaMensajeFront {
    private static final Logger logger = Logger.getLogger(ValidaMensajeFront.class.getName());
    private static final String FIELD_DECLARACION = "declaracion";
    private static final String DIGITO_VERIFICADOR = "digitoVerificador";
    private static final String FIELD_ID_USUARIO = "idUsuario";
    private static final String FIEL_ENCABEZADO = "encabezado";
    private static final String FIEL_USUARIO = "usuario";

    public static JsonObject fidelidadMensaje(JsonObject jsonMessage) {
        JsonObject response = null;
        try{
            if (jsonMessage != null && jsonMessage.getJsonObject(FIELD_DECLARACION) != null && jsonMessage.getString(DIGITO_VERIFICADOR) != null){
                if (jsonMessage.getJsonObject(FIELD_DECLARACION).containsKey(FIELD_DECLARACION)){
                    response = new ValidaMensajeFront().esDeclaracion(jsonMessage);
                }else{
                    response = new ValidaMensajeFront().esObjetoGenerico(jsonMessage);
                }
            }else{
                logger.log(Level.SEVERE, "Algun objeto entrada es null objetoPrincipal {0}, {1} = {2}, {3} = {4}", 
                        new Object[]{jsonMessage, FIELD_DECLARACION, jsonMessage.getJsonObject(FIELD_DECLARACION), DIGITO_VERIFICADOR, jsonMessage.getString(DIGITO_VERIFICADOR)});
            }
        }catch(ClassCastException e){
            logger.log(Level.SEVERE, "Json recibido con parametros incorrectos \r\n ***********{0} debe ser Objeto y {1} debe ser String ************", new Object[]{FIELD_DECLARACION, DIGITO_VERIFICADOR});
        }
        return response;
    }
    
    private JsonObject esDeclaracion(JsonObject jsonMessage){
        JsonObject toEncrypt = new JsonObject()
                                        .put(FIELD_ID_USUARIO, jsonMessage.getJsonObject(FIELD_DECLARACION).getJsonObject(FIEL_ENCABEZADO).getJsonObject(FIEL_USUARIO).getInteger(FIELD_ID_USUARIO))
                                        .put(FIELD_DECLARACION, jsonMessage.getJsonObject(FIELD_DECLARACION));
//        logger.log(Level.INFO, "Se validará mensaje es declaracion como siguietne ejemplo digitoVerificador:digestionDe({idUsurio : OBJEntrante.declaracion.encabezado.usuario.idUsuarioValorInteger, declaracion : {OBJEntrante.declaracion} })  = DIGITO_VERIFICADOR ");
        if (this.verifica(toEncrypt.toString(), jsonMessage.getString(DIGITO_VERIFICADOR))){
            return jsonMessage.getJsonObject(FIELD_DECLARACION);
        }
//        logger.log(Level.SEVERE, "La validacion de la declaracion es incorrecta Sha distintos");
        return null;
    }
    
    private JsonObject esObjetoGenerico(JsonObject jsonMessage){            
//        logger.log(Level.INFO, "Se validará mensaje es generico como siguietne ejemplo digitoVerificador:digestionDe({OBJEntrante.declaracion})  = DIGITO_VERIFICADOR ");
        if (this.verifica(jsonMessage.getJsonObject(FIELD_DECLARACION).toString(), jsonMessage.getString(DIGITO_VERIFICADOR))){
            return jsonMessage.getJsonObject(FIELD_DECLARACION);
        }
        logger.log(Level.SEVERE, "La validacion del objeto generico es incorrecta Sha distintos");
        return null;
    }
    
    /**
     * Método que realiza la validacion de la encripcion as sha-3-256
     * @param json
     * @param digitoVerificador
     * @return 
     */
    private boolean verifica(String json, String digitoVerificador){
        byte[] digest = new SHA3.Digest512().digest(json.getBytes());
        boolean bandera = Hex.toHexString(digest).equals(digitoVerificador); 
        if(!bandera)
            logger.log(Level.SEVERE, "verificador Objeto a encriptar en JAVA: {0} \r\n SHA-512 JAVA: {1} \r\n Enviado desde el front: {2}", new Object[]{json, Hex.toHexString(digest),digitoVerificador});
        return bandera;
    }
}
