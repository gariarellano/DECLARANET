/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.bson.types.ObjectId;

/**
 * Clase que permite cambiar todas las propiedades 
 * String de un Json con espacios al principio y al final 
 * se los elimine para que llegue un Json limpio de espacios.
 * 
 * @author cgarias
 * @since 15/01/2019
 */
public class ExecutJsonUtils {
    private static final String ID = "id";
    private static final String FIELD_VALOR = "valor";//ESta propiedad significa que es un valor de catalogo, por lo que se ignora ya uqe sus validaciuones las hacen los catalogos.    
    private static final String FIEL_NIVEL_ORDEN_GOBIERNO = "nivelOrdenGobierno";//Esta propiedad indica que el catalogo viene de entes, por lo que se ignora
    private static final String FIEL_AMBITO_PUBLICO = "ambitoPublico";//Esta propiedad indica que el catalogo viene de entes, por lo que se ignora
    
    public void iterarHijos(JsonObject jsonObject, Iterator<String> keys){
        while(keys.hasNext()) {
            String key = keys.next();
            if (jsonObject.getValue(key) instanceof String) {
                  jsonObject.put(key, jsonObject.getString(key).trim());
            }
            if (jsonObject.getValue(key) instanceof JsonObject) {
                  iterarHijos(jsonObject.getJsonObject(key), jsonObject.getJsonObject(key).fieldNames().iterator());
            }
            if (jsonObject.getValue(key) instanceof JsonArray) {
                  for(Object index: jsonObject.getJsonArray(key)){
                    iterarHijos((JsonObject)index, ((JsonObject)index).fieldNames().iterator());
                  }
            }
        }
    }
    public void asignaIds(JsonObject jsonObject, Iterator<String> keys){
        HashMap<String, Boolean> llaves = new HashMap<>();
        while(keys.hasNext()) {
            String key = keys.next();
            if (key.equals(ID) && !jsonObject.containsKey(FIELD_VALOR) && !(jsonObject.containsKey(FIEL_NIVEL_ORDEN_GOBIERNO) && jsonObject.containsKey(FIEL_AMBITO_PUBLICO))) {
                jsonObject.put(ID, this.generarLlave(llaves, jsonObject.getString(key)));
            }
            if (jsonObject.getValue(key) instanceof JsonObject) {
                asignaIds(jsonObject.getJsonObject(key), jsonObject.getJsonObject(key).fieldNames().iterator());
            }
            if (jsonObject.getValue(key) instanceof JsonArray) {
                for(Object index: jsonObject.getJsonArray(key)){
                    asignaIds((JsonObject)index, ((JsonObject)index).fieldNames().iterator());
                }
            }
        }
    }
    
    private String generarLlave(HashMap llaves ,String llave){
        String llaveGen = llave == null ? new ObjectId(new Date()).toString() : llave;
        if (llaves.containsKey(llaveGen)){
            llaveGen = generaNuevaLlave(llaves);
        }
        llaves.put(llaveGen,null);
        return llaveGen;
    }
    
    private String generaNuevaLlave(HashMap<String, Boolean> llaves){
        String llaveGen = new ObjectId(new Date()).toString();
        if (llaves.containsKey(llaveGen))
            llaveGen = generaNuevaLlave(llaves);
        
        return llaveGen;
    }
}
