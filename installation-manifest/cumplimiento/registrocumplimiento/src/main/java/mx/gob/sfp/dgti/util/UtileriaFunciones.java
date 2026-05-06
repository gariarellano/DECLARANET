/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import mx.gob.sfp.dgti.util.enums.EnumTipoDeclaracion;

/**
 *
 * @author cgarias
 */
public class UtileriaFunciones {
    private static final String EMPTY = "";
    private static final String SPACE = " ";
    private static final String FORMATO_FECHA = "yyyy-MM-dd";
    
    /**
     * Método que permite realizar el concatenado de los nombre de completos, en caso de no tener primer apellido, 
     * haga la concatenación correcta sin el primer apellido.
     * 
     * @param nombre		Nombre(s) a concatenar.
     * @param pApellido	Primer apellido del servidor publico.
     * @param sApellido	Segundo apellido del servidor publico.
     * 
     * @return String 	Nombre completo con apellidos.
     */
    public static String getNombreCompleto(String nombre, String pApellido, String sApellido){
        String apellidos= EMPTY;
        if (pApellido!= null && !EMPTY.equals(pApellido)){
            apellidos += SPACE+pApellido;
        }
        if (sApellido!= null && !EMPTY.equals(sApellido)){
            apellidos += SPACE+sApellido;
        }
        return nombre+apellidos;
    }
    
    public static boolean esDeclaracionExtemporanea(EnumTipoDeclaracion tipoDeclaracion, String sFecRecepcion, String sFecEncargo, String sAnioDeclaracion, Integer nivelJerarquico,
            Boolean sindicalizado) {
        Calendar fechaEncargo = Calendar.getInstance();
        Calendar fechaRecepcion = Calendar.getInstance();
        
        fechaRecepcion.set(getYear(sFecRecepcion), getMonth(sFecRecepcion) - 1, getDay(sFecRecepcion));
        if (EnumTipoDeclaracion.INICIO.equals(tipoDeclaracion)){
            fechaEncargo.set(getYear(sFecEncargo), getMonth(sFecEncargo) - 1, getDay(sFecEncargo));
            if (obtieneDiferenciaEnDias(fechaRecepcion, fechaEncargo) > 60) {
                return true;
            }
        }

        if (EnumTipoDeclaracion.CONCLUSION.equals(tipoDeclaracion)){
            fechaEncargo.set(getYear(sFecEncargo), getMonth(sFecEncargo) - 1, getDay(sFecEncargo));
            if (obtieneDiferenciaEnDias(fechaRecepcion, fechaEncargo) > 60) {
                return true;
            }
        }

        if (EnumTipoDeclaracion.MODIFICACION.equals(tipoDeclaracion)){
            if (fechaRecepcion.get(Calendar.YEAR) != Integer.valueOf(sAnioDeclaracion) &&
                    2020 != Integer.valueOf(sAnioDeclaracion)) {//se agrega validacion ya que para la declaración 2020 se alarga el plazo hasta 10 enero 2021
                return true;
            }
            Calendar _FechaLimite = Calendar.getInstance();
            if (2020 == Integer.valueOf(sAnioDeclaracion)){//Se extiende periodo por Covid 19 hasta el 1 de Agosto --cgarias 31/05/2020
                if (4 > nivelJerarquico || Boolean.TRUE.equals(sindicalizado)){//Se extiende periodo hasta el 10 de enero para decla 2020 y aquellos que nivel jerarquico sea menor a subdirector o detectado en rusp como sindicalizado. --cgarias 26/12/2020
                    _FechaLimite.set(Calendar.YEAR, 2021);
                    _FechaLimite.set(Calendar.MONTH, Calendar.JANUARY);
                    _FechaLimite.set(Calendar.DATE, 10);
                }else{
                    _FechaLimite.set(Calendar.YEAR, 2020);
                    _FechaLimite.set(Calendar.MONTH, Calendar.JULY);
                    _FechaLimite.set(Calendar.DATE, 31);
                }
            }else{
                _FechaLimite.set(Calendar.MONTH, Calendar.MAY);   
                _FechaLimite.set(Calendar.DATE, 31);
            }
            _FechaLimite.set(Calendar.HOUR_OF_DAY, 23);
            _FechaLimite.set(Calendar.MINUTE, 59);
            _FechaLimite.set(Calendar.SECOND, 59);
            if (fechaRecepcion.after(_FechaLimite)) {
                return true;
            }
        }
        return false;
    }
    
    public static int getYear(String fecha){
        return Integer.parseInt(fecha.substring(6, 10));

    }

    public static int getMonth(String fecha){
        return Integer.parseInt(fecha.substring(3, 5));
    }

    public static int getDay(String fecha){
        return Integer.parseInt(fecha.substring(0, 2));
    }
    
    public static long obtieneDiferenciaEnDias(java.util.Calendar fEncargo, java.util.Calendar FEnvio) {
        java.sql.Date fechaEncargo = new java.sql.Date(fEncargo.getTimeInMillis());
        java.sql.Date fechaEnvio = new java.sql.Date(FEnvio.getTimeInMillis());
        LocalDate endtDateEncargo = LocalDate.parse(new SimpleDateFormat(FORMATO_FECHA).format(fechaEncargo));
        LocalDate startDateEnvio = LocalDate.parse(new SimpleDateFormat(FORMATO_FECHA).format(fechaEnvio));
        return ChronoUnit.DAYS.between(endtDateEncargo,startDateEnvio );
    }
}
