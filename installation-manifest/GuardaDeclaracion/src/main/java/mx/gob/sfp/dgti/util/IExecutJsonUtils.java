/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.util;

import io.vertx.core.json.JsonObject;
import java.util.Iterator;

/**
 *
 * @author cgarias
 */
public interface IExecutJsonUtils {
    public static void excutTrimJson(JsonObject jsonObject, Iterator<String> keys){
        new ExecutJsonUtils().iterarHijos(jsonObject, keys);
    }
    public static void excutGeneraIds(JsonObject jsonObject, Iterator<String> keys){
        new ExecutJsonUtils().asignaIds(jsonObject, keys);
    }
}
