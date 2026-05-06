/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

import java.util.EnumSet;

/**
 * Clase que contiene los métodos que realiza
 * la comversion de un Json a Objeto JSon.
 * 
 * @author cgarias
 * @since 12/12/2019
 */
public class EnumToJson {
    public static String enumAmbitoPoder(){        
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumAmbitoPoder.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumAmbitoSector(){        
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumAmbitoSector.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }    
    public static String enumEstatusEscolaridad(){        
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumEstatusEscolaridad.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumFormaAdquiscion(){        
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumFormaAdquiscion.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumFormaPago(){
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumFormaPago.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumFormaPagoOtro(){
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumFormaPagoOtro.class)
                .forEach(enu ->
                        json.append("{\"id\": \"").append(enu.name()).append("\",")
                                .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                                .append("},")
                );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumFormaRecepcion(){
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumFormaRecepcion.class)
                .forEach(enu ->
                        json.append("{\"id\": \"").append(enu.name()).append("\",")
                                .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                                .append("},")
                );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumMotivoBaja(){        
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumMotivoBaja.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumNivelAcademico(){        
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumNivelAcademico.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumNivelGobierno(){        
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumNivelGobierno.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumParticipante(){        
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumParticipante.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumRelacionDeclarante(){
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumRelacionDeclarante.class)
                .forEach(enu ->
                        json.append("{\"id\": \"").append(enu.name()).append("\",")
                                .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                                .append("},")
                );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumTipoBien(){        
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumTipoBien.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumTipoBienPrestamo(){
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumTipoBienPrestamo.class)
                .forEach(enu ->
                        json.append("{\"id\": \"").append(enu.name()).append("\",")
                                .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                                .append("},")
                );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumTipoDeclaracion(){        
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumTipoDeclaracion.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumTipoDocumento(){        
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumTipoDocumento.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumTipoFideicomiso(){
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumTipoFideicomiso.class)
            .forEach(enu ->
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumTipoFormatoDeclaracion(){
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumTipoFormatoDeclaracion.class)
                .forEach(enu ->
                        json.append("{\"id\": \"").append(enu.name()).append("\",")
                                .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                                .append("},")
                );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumTipoOperacion(){        
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumTipoOperacion.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumTipoParticipacionF(){        
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumTipoParticipacionF.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumTipoPersona(){        
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumTipoPersona.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumTipoRemuneracion(){        
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumTipoRemuneracion.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
    public static String enumTipoRepresentacion(){
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumTipoRepresentacion.class)
                .forEach(enu ->
                        json.append("{\"id\": \"").append(enu.name()).append("\",")
                                .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                                .append("},")
                );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }

    public static String enumUbicacion(){        
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumUbicacion.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }

    public static String enumLugarReside(){
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumLugarReside.class)
                .forEach(enu ->
                        json.append("{\"id\": \"").append(enu.name()).append("\",")
                                .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                                .append("},")
                );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }

    public static String enumValorConformeA(){        
        StringBuilder json = new StringBuilder("[");
        EnumSet.allOf(EnumValorConformeA.class)
            .forEach(enu -> 
                    json.append("{\"id\": \"").append(enu.name()).append("\",")
                        .append("\"valor\": \"").append(enu.getDescripcion()).append("\"")
                        .append("},")
        );
        json.delete(json.length()-1, json.length());
        json.append("]");
        return json.toString();
    }
}