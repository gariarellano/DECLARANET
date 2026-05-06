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
 * Interface que contiene la inicializacion de
 * la implementacion para recorrer un Json completo.
 * 
 * @author cgarias
 */
public interface IRecorreDecla {
    public static void ExcutTrimJson(JsonArray identificadores, JsonObject jsonObject, Iterator<String> keys){
        new RecorreDecla().iterarHijos(identificadores, jsonObject, keys);
    }
}
