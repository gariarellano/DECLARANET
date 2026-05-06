/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.dto.http.response;

import io.vertx.core.json.JsonObject;

/**
 * Clase que permite mantener un standar de respuesta 
 * de los objetos par un cliente.
 * 
 * @author cgarias
 * @since 09/06/2020
 */
public class JsonObjectRespone extends JsonObject{
    public JsonObjectRespone(){
        super();
        this.putNull("message");
        this.putNull("data");
    }
    public JsonObjectRespone(Object message, Object data){
        super();
        this.put("message", message);
        this.put("data", data);
    }
}
