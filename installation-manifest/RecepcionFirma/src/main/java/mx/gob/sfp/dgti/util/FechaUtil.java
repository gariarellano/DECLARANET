/**
 * @FechaUtil.java Dec 18, 2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Miriam Sanchez Sanchez programador07
 * @since Dec 18, 2019
 */
public class FechaUtil {
	
	private static final String FORMATO_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.S";
	
	 /**
     * Método que realiza la comvercion de fecha a String con formato
     * "yyyy-MM-dd'T'hh:mm:ss.S"
     * 
     * @param fecha Fecha que se pretende regresar en String.
     * @return String Fecha con formato "yyyy-MM-dd'T'hh:mm:ss.S"
     * 
     * @author cgarias
     * @since 04/12/2019
     */
    public static String getFechaFormatoISO8601(Date fecha) {
        if (fecha != null) {
                return new SimpleDateFormat(FORMATO_ISO_8601).format(fecha);
        }
        return null;
    }
    
    /**
     * Método que realiza la comvercion de fecha a String con formato
     * "yyyy-MM-dd'T'hh:mm:ss.S"
     * 
     * @param fechaIso Fecha que se pretende regresar en String.
     * @return String Fecha con formato "yyyy-MM-dd'T'hh:mm:ss.S"
     * 
     * @throws java.text.ParseException en caso de suceder un exception de conversión 
     * 
     * @author cgarias
     * @since 04/12/2019
     */
    public static Date getFechaFormatoISO8601(String fechaIso) throws ParseException {
        if (fechaIso != null) {
            return new SimpleDateFormat(FORMATO_ISO_8601).parse(fechaIso);
        }
        return null;
    }
}
