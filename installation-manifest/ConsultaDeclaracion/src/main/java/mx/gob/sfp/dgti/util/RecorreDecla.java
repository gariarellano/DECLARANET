/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.Iterator;
import java.util.regex.Pattern;
import mx.gob.sfp.dgti.utils.FechaUtil;

/**
 * Clase que realiza el recorrido dle Json, y 
 * obtiene los identificadores encontrados en 
 * toda la declaracion.
 * 
 * @author cgarias
 * @since 15/01/2019
 */
public class RecorreDecla {
    private static final String VERIFICAR = "verificar";
    private static final String REGISTRO_HISTORICO = "registroHistorico";
    private static final String ACLARACIONES_OBSERVACIONES = "aclaracionesObservaciones";    
    private static final String TIPO_OPERACION = "tipoOperacion";   
    private static final String BAJA = "BAJA";
    private static final String FECHA_ADQUISICION = "fechaAdquisicion";
    private static final String EXPERIENCIA_LABORAL = "experienciaLaboral";
    private static final String ACTIVIDAD_LABORAL = "actividadLaboral";
    private static final String SECTOR_PUBLICO = "sectorPublico";
    private static final String AMBITO_PUBLICO = "ambitoPublico";
    private static final String ORGANISMO_AUTONOMO = "ORGANISMO_AUTONOMO";
    private static final String ORGANISMOS_AUTONOMOS = "ORGANISMOS_AUTONOMOS";
    private static final Pattern validador = Pattern.compile("^([a-zA-Z0-9ÑÁÉÍÓÚÜñáéíóúü~.,' ]){0,2001}$");
    
    
    
    public void iterarHijos(JsonObject jsonObject, Iterator<String> keys, String fechaEncargo){
        while(keys.hasNext()) {
            String key = keys.next();
            if (ACLARACIONES_OBSERVACIONES.equals(key)){
                jsonObject.putNull(ACLARACIONES_OBSERVACIONES);
            }
            if (TIPO_OPERACION.equals(key)){
                jsonObject.putNull(TIPO_OPERACION);
            }
            if(VERIFICAR.equals(key)){
                jsonObject.put(VERIFICAR, Boolean.FALSE);
            }
            if (jsonObject.getValue(key) instanceof JsonObject) {
                iterarHijos(jsonObject.getJsonObject(key), jsonObject.getJsonObject(key).fieldNames().iterator(), fechaEncargo);
            }
            if (jsonObject.getValue(key) instanceof JsonArray) {
                for (int x = jsonObject.getJsonArray(key).size()-1; x >= 0; x--){
                    //se agrega este IF ya que se capturaron declaraciones con valor de enum 
                    //no valido por el Schema graphQl el cual fue un BUG al comienzo
                    if (key.equals(EXPERIENCIA_LABORAL) &&
                            jsonObject.getJsonArray(key).getJsonObject(x).getJsonObject(ACTIVIDAD_LABORAL)!= null &&
                            jsonObject.getJsonArray(key).getJsonObject(x).getJsonObject(ACTIVIDAD_LABORAL).getJsonObject(SECTOR_PUBLICO) != null &&
                            jsonObject.getJsonArray(key).getJsonObject(x).getJsonObject(ACTIVIDAD_LABORAL).getJsonObject(SECTOR_PUBLICO).getString(AMBITO_PUBLICO)!= null &&
                            jsonObject.getJsonArray(key).getJsonObject(x).getJsonObject(ACTIVIDAD_LABORAL).getJsonObject(SECTOR_PUBLICO).getString(AMBITO_PUBLICO).equals(ORGANISMO_AUTONOMO)
                            ){
                        jsonObject.getJsonArray(key).getJsonObject(x).getJsonObject(ACTIVIDAD_LABORAL).getJsonObject(SECTOR_PUBLICO).put(AMBITO_PUBLICO, ORGANISMOS_AUTONOMOS);
                    }
                    
                    if (BAJA.equals(jsonObject.getJsonArray(key).getJsonObject(x).getString(TIPO_OPERACION))){
                        jsonObject.getJsonArray(key).remove(x);
                        continue;
                    }else {
                        iterarHijos(jsonObject.getJsonArray(key).getJsonObject(x), jsonObject.getJsonArray(key).getJsonObject(x).fieldNames().iterator(), fechaEncargo);
                    }
                    if(jsonObject.getJsonArray(key).getJsonObject(x).containsKey(REGISTRO_HISTORICO)){
                        if(jsonObject.getJsonArray(key).getJsonObject(x).containsKey(FECHA_ADQUISICION)){
                            if (FechaUtil.fechaMayorQue(jsonObject.getJsonArray(key).getJsonObject(x).getString(FECHA_ADQUISICION), fechaEncargo)){
                                jsonObject.getJsonArray(key).getJsonObject(x).put(REGISTRO_HISTORICO, Boolean.FALSE);
                            }else {
                                jsonObject.getJsonArray(key).getJsonObject(x).put(REGISTRO_HISTORICO, Boolean.TRUE);
                            }
                        }else{
                            jsonObject.getJsonArray(key).getJsonObject(x).put(REGISTRO_HISTORICO, Boolean.TRUE);
                        }
                    }
                }
            }
        }
    }
    
    public void iterarDeclaOracle(JsonObject jsonObject, Iterator<String> keys, String fechaEncargo){
        while(keys.hasNext()) {
            String key = keys.next();            
            if (jsonObject.getValue(key) instanceof JsonObject) {
                iterarDeclaOracle(jsonObject.getJsonObject(key), jsonObject.getJsonObject(key).fieldNames().iterator(), fechaEncargo);
            }
            if (jsonObject.getValue(key) instanceof JsonArray) {
                for (int x = jsonObject.getJsonArray(key).size()-1; x >= 0; x--){
                    if (BAJA.equals(jsonObject.getJsonArray(key).getJsonObject(x).getString(TIPO_OPERACION))){
                        jsonObject.getJsonArray(key).remove(x);
                        continue;
                    }else {
                        iterarDeclaOracle(jsonObject.getJsonArray(key).getJsonObject(x), jsonObject.getJsonArray(key).getJsonObject(x).fieldNames().iterator(), fechaEncargo);
                    }                   
                    if(jsonObject.getJsonArray(key).getJsonObject(x).containsKey(REGISTRO_HISTORICO)){
                        if(jsonObject.getJsonArray(key).getJsonObject(x).containsKey(FECHA_ADQUISICION)){
                            if (jsonObject.getJsonArray(key).getJsonObject(x).getString(FECHA_ADQUISICION)!= null &&
                                    !jsonObject.getJsonArray(key).getJsonObject(x).getString(FECHA_ADQUISICION).trim().isEmpty() &&
                                    FechaUtil.fechaMayorQue(jsonObject.getJsonArray(key).getJsonObject(x).getString(FECHA_ADQUISICION), fechaEncargo)){
                                jsonObject.getJsonArray(key).getJsonObject(x).put(REGISTRO_HISTORICO, Boolean.FALSE);
                            }else {
                                jsonObject.getJsonArray(key).getJsonObject(x).put(REGISTRO_HISTORICO, Boolean.TRUE);
                            }
                        }else{
                            jsonObject.getJsonArray(key).getJsonObject(x).put(REGISTRO_HISTORICO, Boolean.TRUE);
                        }
                    }
                }
            }
            if (!"valor".equals(key) && !"valorUno".equals(key) && //catalogos
                    !key.contains("fecha") && // fechas yyyy-mm-dd
                    !key.contains("numeroSerieRegistro") && !key.contains("numeroCuentaContrato")//los demas numeros no importan.
                    ){
                if (jsonObject.getValue(key) instanceof String) {
                    if (!validador.matcher(jsonObject.getString(key)).matches()){
                        jsonObject.put(key, jsonObject.getString(key)
                                .replaceAll("\\{", "").replaceAll("}", "")
                                .replaceAll("\\[", "").replaceAll("]", "")
                                .replaceAll("¿", "").replaceAll("\\?", "")
                                .replaceAll("¡", "").replaceAll("!", "")
                                .replaceAll("%", "").replaceAll("\\*", "")
                                .replaceAll("\\+", "").replaceAll("=", "")
                                .replaceAll("\\-", "").replaceAll("\\.", "")
                                .replaceAll(",", "").replaceAll(":", "")
                                .replaceAll(";", "").replaceAll("\\$", "")
                                .replaceAll("\"", "").replaceAll("/", "")
                                .replaceAll("&", "").replaceAll("\\^", "")
                                .replaceAll("\\|", "").replaceAll("°", "")
                                .replaceAll("¬", "").replaceAll("\\\\", "")
                                .replaceAll("\\(", "").replaceAll("\\)", "")
                        );
                    }
                }
            }
        }
    }
}
