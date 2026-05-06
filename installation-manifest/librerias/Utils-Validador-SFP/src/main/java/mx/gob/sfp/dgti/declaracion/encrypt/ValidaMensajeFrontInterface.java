/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.declaracion.encrypt;

import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.encrypt.im.ValidaMensajeFront;

/**
 * Interface que contiene la firma de los métodos
 * para el encriptado de la declaracion patrimonial.
 * 
 * @author cgarias
 * @since 22/11/2019
 */
public interface ValidaMensajeFrontInterface{
    
    /**
     * Método que realiza la validacion de los objetos recibidos por el front.
     * 
     * @param jsonMessage         Objeto Json que hace referencia a un mensaje del front.     
     * 
     * @return JsonObject Objeto Json en caso de que todo haya salido correctamente en el mensaje.
     */
    public static  JsonObject fidelidadMensaje(JsonObject jsonMessage){
        return  ValidaMensajeFront.fidelidadMensaje(jsonMessage);
    }
}
