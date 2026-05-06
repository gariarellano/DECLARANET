
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cgarias
 */
public class ArrayToStringExample {
    public static void main(String args[]){
        List<String> identificadores = new ArrayList<>();
        identificadores.add("HOLA");
        identificadores.add("HOLA1");
        identificadores.add("HOLA2");
        
        List<String> identifi = new ArrayList<>(Arrays.asList("HOLA------"));
        System.out.println(identifi.toString());
        
        System.out.println(identificadores.toString());
        JsonObject query  = new JsonObject().put("_id", new JsonObject().put("$in", identificadores));
        System.out.println(query.toString());

    }
}
