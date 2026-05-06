/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.Iterator;

/**
 * Clase que realiza el recorrido dle Json, y 
 * obtiene los identificadores encontrados en 
 * toda la declaracion.
 * 
 * @author cgarias
 * @since 15/01/2019
 */
public class RecorreDecla {
    private static final String ID = "id";
    private static final String FIELD_VALOR = "valor";//ESta propiedad significa que es un valor de catalogo, por lo que se ignora ya uqe sus validaciuones las hacen los catalogos.    
    private static final String FIEL_NIVEL_ORDEN_GOBIERNO = "nivelOrdenGobierno";//Esta propiedad indica que el catalogo viene de entes, por lo que se ignora
    private static final String FIEL_AMBITO_PUBLICO = "ambitoPublico";//Esta propiedad indica que el catalogo viene de entes, por lo que se ignora
    
    public void iterarHijos(JsonArray identificadores, JsonObject jsonObject, Iterator<String> keys){        
        while(keys.hasNext()) {
            String key = keys.next();
            if (key.equals(ID) && jsonObject.getValue(key) != null && !jsonObject.containsKey(FIELD_VALOR) && !(jsonObject.containsKey(FIEL_NIVEL_ORDEN_GOBIERNO) && jsonObject.containsKey(FIEL_AMBITO_PUBLICO))) {
                identificadores.add(jsonObject.getValue(key));
            }
            if (jsonObject.getValue(key) instanceof JsonObject) {
                iterarHijos(identificadores, jsonObject.getJsonObject(key), jsonObject.getJsonObject(key).fieldNames().iterator());
            }
            if (jsonObject.getValue(key) instanceof JsonArray) {
                for(Object index: jsonObject.getJsonArray(key)){
                    iterarHijos(identificadores, (JsonObject)index, ((JsonObject)index).fieldNames().iterator());
                }
            }
        }
    }
}
