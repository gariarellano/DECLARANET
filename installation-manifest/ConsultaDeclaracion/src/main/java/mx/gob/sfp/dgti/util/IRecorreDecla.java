/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.util;

import io.vertx.core.json.JsonObject;
import java.util.Iterator;

/**
 * Interface que contiene la inicializacion de
 * la implementacion para recorrer un Json completo.
 * 
 * @author cgarias
 */
public interface IRecorreDecla {
    public static void resetValuesForPrecarga(JsonObject jsonObject, Iterator<String> keys, String fechaEncargo){
        new RecorreDecla().iterarHijos(jsonObject, keys, fechaEncargo);
    }
    public static void evaluateDataOracle(JsonObject jsonObject, Iterator<String> keys, String fechaEncargo){
        new RecorreDecla().iterarDeclaOracle(jsonObject, keys, fechaEncargo);
    }
}
