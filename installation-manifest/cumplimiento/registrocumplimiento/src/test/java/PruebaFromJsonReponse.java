
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.dto.http.response.JsonObjectHttpResponse;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gio_j
 */
public class PruebaFromJsonReponse {
    public static void main(String args[]){
        JsonObject respnse = new JsonObjectHttpResponse();
        System.out.println(respnse.containsKey("statusCode"));
        System.out.println(respnse.containsKey("response"));
        System.out.println(respnse.containsKey("otro"));
    }
}
