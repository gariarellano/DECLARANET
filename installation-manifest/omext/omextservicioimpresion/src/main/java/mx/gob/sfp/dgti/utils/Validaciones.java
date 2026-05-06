/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clas eque conitiene métodos estaticos 
 * para realizar validaciones de datos.
 * 
 * @author programador09
 * @since 21/01/2021
 */
public class Validaciones {
    private static final Logger logger = Logger.getLogger(Validaciones.class.getName());
    private static final String FORMATO_FECHA = "yyyy-MM-dd";
    
    /**
     * Método que realiza la validacion de una 
     * fecha existente y con formato correcto.
     * 
     * @param fecha parametro de Strign con formato fecha
     * @return bandera que indica si es una fecha correcta
     */
    public static boolean validaFormatoFecha(String fecha){
        try {
            new SimpleDateFormat(FORMATO_FECHA).parse(fecha);
        } catch (ParseException e) {
           logger.log(Level.WARNING, "Error formato de fecha: {0}", e);
            return false;
        }
        return true;
    }
}
