/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.dto.http.response;

import io.vertx.core.json.JsonObject;

/**
 * Clase que permite mantener un standar de respuesta 
 * a nivel http los clientes.
 * 
 * @author cgarias
 * @since 09/06/2020
 */
public class JsonObjectHttpResponse extends JsonObject{
    /**
     * Constructor principal de la clase.
     */
    public JsonObjectHttpResponse(){
        super();
        this.put("statusCode", 204);
        this.put("response", new JsonObject());
    }
    
    public JsonObjectHttpResponse(Integer statusCode, JsonObjectRespone response){
        super();
        this.put("statusCode", statusCode);
        this.put("response", response);
    }
    
}
