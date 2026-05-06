/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.service;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.io.Serializable;
import java.util.Iterator;

/**
 * Clase que realiza el recorrido de la estructura pdn para poner datos como 
 * "#M#A#N#T#E#N#E#R#" para no volar la informacion en la limpieza de campos
 * 
 * @author programador 09
 */
public class NulosAmantener implements Serializable{
    private static final String MANTENER = "#M#A#N#T#E#N#E#R#";
    
    
    public NulosAmantener(JsonObject jsonObject){
        iterarHijos(jsonObject, jsonObject.fieldNames().iterator());
    }
    
    private void iterarHijos(JsonObject jsonObject, Iterator<String> keys){
        while(keys.hasNext()) {
            String key = keys.next();
            if (jsonObject.getValue(key) == null) {
                jsonObject.put(key, MANTENER);
            }else if (jsonObject.getValue(key) instanceof JsonObject) {
                iterarHijos(jsonObject.getJsonObject(key), jsonObject.getJsonObject(key).fieldNames().iterator());
            }else if (jsonObject.getValue(key) instanceof JsonArray) {
                for (int x = jsonObject.getJsonArray(key).size()-1; x >= 0; x--){
                    iterarHijos(jsonObject.getJsonArray(key).getJsonObject(x), jsonObject.getJsonArray(key).getJsonObject(x).fieldNames().iterator());
                }
            }
        }
    }
}
