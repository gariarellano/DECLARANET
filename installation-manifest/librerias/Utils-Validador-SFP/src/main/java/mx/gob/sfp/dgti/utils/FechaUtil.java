package mx.gob.sfp.dgti.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Clase que contiene las validaciones para fechas
 * @author Miriam Sánchez Sánchez programador07
 * @since 2019/10/15
 */
public class FechaUtil {

    private static final String FORMATO = "yyyy-MM-dd";
    private static final String FORMATO_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.S";
	
    /**
     * Función, valida que una fecha se encuentre entre un rango de fechas, menor qué y mayor qué
     * Fechas en formato yyyy-MM-dd
     * @param fechaAValidar 
     * @param rangoMinimo: rango mínimo
     * @param rangoMaximo: rango máximo
     * @return boolean
     */
    public static boolean fechaMayorMenorQue(String fechaAValidar, String rangoMinimo, String rangoMaximo) {
            LocalDate localAValidar = obtenerLocalDate(fechaAValidar);
            LocalDate minimo = obtenerLocalDate(rangoMinimo);
            LocalDate maximo = obtenerLocalDate(rangoMaximo);
            boolean validacion = false;
            if (LocalDate.of(localAValidar.getYear(), localAValidar.getMonthValue(), localAValidar.getDayOfMonth())
                            .isBefore(LocalDate.of(maximo.getYear(), maximo.getMonthValue(), maximo.getDayOfMonth()))
                            && LocalDate
                                            .of(localAValidar.getYear(), localAValidar.getMonthValue(), localAValidar.getDayOfMonth())
                                            .isAfter(LocalDate.of(minimo.getYear(), minimo.getMonthValue(),
                                                            minimo.getDayOfMonth()))) {
                    validacion = true;
            }
            return validacion;
    }

    /**
     * Función, valida que una fecha se encuentre en un rango, menor o igual qué y mayor o igual qué
     * Fechas en formato yyyy-MM-dd
     * @param fechaAValidar
     * @param rangoMinimo: rango mínimo
     * @param rangoMaximo: rango máximo
     * @return boolean
     */
    public static boolean mayorOIgualQueMenorOIgualQue(String fechaAValidar, String rangoMinimo, String rangoMaximo) {
            LocalDate localAValidar = obtenerLocalDate(fechaAValidar);
            LocalDate minimo = obtenerLocalDate(rangoMinimo);
            LocalDate maximo = obtenerLocalDate(rangoMaximo);
            boolean validacion = false;
            if ((LocalDate.of(localAValidar.getYear(), localAValidar.getMonthValue(), localAValidar.getDayOfMonth())
                            .isBefore(LocalDate.of(maximo.getYear(), maximo.getMonthValue(), maximo.getDayOfMonth()))
                            || LocalDate
                                            .of(localAValidar.getYear(), localAValidar.getMonthValue(), localAValidar.getDayOfMonth())
                                            .isEqual(LocalDate.of(maximo.getYear(), maximo.getMonthValue(),
                                                            maximo.getDayOfMonth())))
                            && (LocalDate
                                            .of(localAValidar.getYear(), localAValidar.getMonthValue(), localAValidar.getDayOfMonth())
                                            .isAfter(LocalDate.of(minimo.getYear(), minimo.getMonthValue(),
                                                            minimo.getDayOfMonth()))
                                            || LocalDate
                                                            .of(localAValidar.getYear(), localAValidar.getMonthValue(),
                                                                            localAValidar.getDayOfMonth())
                                                            .isEqual(LocalDate.of(minimo.getYear(), minimo.getMonthValue(),
                                                                            minimo.getDayOfMonth())))) {
                    validacion = true;
            }
            return validacion;
    }

    /**
     * Función, valida que una fecha sea mayor 
     * Fechas en formato yyyy-MM-dd
     * @param fechaAValidar
     * @param rangoMaximo: rango máximo
     * @return boolean
     */
    public static boolean fechaMayorQue(String fechaAValidar, String rangoMaximo) {
            LocalDate localAValidar = obtenerLocalDate(fechaAValidar);
            LocalDate maximo = obtenerLocalDate(rangoMaximo);
            boolean validacion = false;
            if (LocalDate.of(localAValidar.getYear(), localAValidar.getMonthValue(), localAValidar.getDayOfMonth())
                            .isAfter(LocalDate.of(maximo.getYear(), maximo.getMonthValue(),
                                            maximo.getDayOfMonth()))) {
                    validacion = true;
            }
            return validacion;
    }

    /**
     * Función, valida que una fecha sea menor
     * Fechas en formato yyyy-MM-dd
     * @param fechaAValidar
     * @param rangoMinimo: rango mínimo
     * @return boolean
     */
    public static boolean fechaMenorQue(String fechaAValidar, String rangoMinimo) {
            LocalDate localAValidar = obtenerLocalDate(fechaAValidar);
            LocalDate minimo = obtenerLocalDate(rangoMinimo);
            boolean validacion = false;
            if (LocalDate.of(localAValidar.getYear(), localAValidar.getMonthValue(), localAValidar.getDayOfMonth())
                            .isBefore(LocalDate.of(minimo.getYear(), minimo.getMonthValue(),
                                            minimo.getDayOfMonth()))) {
                    validacion = true;
            }
            return validacion;
    }

    /**
     * Función, valida que una fecha sea mayor
     * Fechas en formato yyyy-MM-dd
     * @param fechaAValidar
     * @param rangoMaximo: rango máximo
     * @return boolean
     */
    public static boolean fechaMayorIgualQue(String fechaAValidar, String rangoMaximo) {
            LocalDate localAValidar = obtenerLocalDate(fechaAValidar);
            LocalDate maximo = obtenerLocalDate(rangoMaximo);
            boolean validacion = false;
            if (LocalDate.of(localAValidar.getYear(), localAValidar.getMonthValue(), localAValidar.getDayOfMonth())
                            .isAfter(LocalDate.of(maximo.getYear(), maximo.getMonthValue(),
                                            maximo.getDayOfMonth())) ||
                            LocalDate.of(localAValidar.getYear(), localAValidar.getMonthValue(), localAValidar.getDayOfMonth())
                            .isEqual(LocalDate.of(maximo.getYear(), maximo.getMonthValue(),
                                            maximo.getDayOfMonth()))) {
                    validacion = true;
            }
            return validacion;
    }

    /**
     * Función, valida que una fecha sea menor
     * Fechas en formato yyyy-MM-dd
     * @param fechaAValidar
     * @param rangoMinimo: rango mínimo
     * @return boolean
     */
    public static boolean fechaMenorIgualQue(String fechaAValidar, String rangoMinimo) {
            LocalDate localAValidar = obtenerLocalDate(fechaAValidar);
            LocalDate minimo = obtenerLocalDate(rangoMinimo);
            boolean validacion = false;
            if (LocalDate.of(localAValidar.getYear(), localAValidar.getMonthValue(), localAValidar.getDayOfMonth())
                            .isBefore(LocalDate.of(minimo.getYear(), minimo.getMonthValue(),
                                            minimo.getDayOfMonth())) ||
                            LocalDate.of(localAValidar.getYear(), localAValidar.getMonthValue(),
                                            localAValidar.getDayOfMonth())
                            .isEqual(LocalDate.of(minimo.getYear(), minimo.getMonthValue(),
                                            minimo.getDayOfMonth()))) {
                    validacion = true;
            }
            return validacion;
    }

    /**
     * Función para obtener el LocalDate de una fecha
     * Fechas en formato yyyy-MM-dd
     * @param fecha
     * @return LocalDate
     */
    public static LocalDate obtenerLocalDate(String fecha) {
            return LocalDate.parse(fecha, DateTimeFormatter.ofPattern(FORMATO));
    }

    /**
     * Función para restar anios a una fecha
     * Fechas en formato yyyy-MM-dd
     * @param fecha: la fecha a la cual se le restarán anios
     * @param anios: la cantidad de anios a restar
     * @return String
     */
    public static String restarAniosAFecha(String fecha, int anios) {
            return obtenerLocalDate(fecha).minusYears(anios).toString();
    }

    /**
     * Función para sumar anios a una fecha
     * Fechas en formato yyyy-MM-dd
     * @param fecha: la fecha a la cual se le restarán anios
     * @param anios: la cantidad de anios a sumar
     * @return String
     */
    public static String sumarAniosAFecha(String fecha, int anios) {
            return obtenerLocalDate(fecha).plusYears(anios).toString();
    }


    /**
     *Función para saber si el anio es extracto de la fecha 
     *@param fechaEncargo
     *@param anio
     *@return boolean
     */
    public static boolean validarFechaEncargo(String fechaEncargo, Integer anio) {
            if (fechaEncargo == null || anio == null)
                    return false;
            try {
                    LocalDate fechaEn = FechaUtil.obtenerLocalDate(fechaEncargo);
                    if (fechaEn.getYear() == anio)
                            return true;
            } catch (Exception e) {
                    return false;
            }
            return false;
    }
        
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
    
    /**
     *Metodo que devuelve un int del año actual
     */
    public static int getAnioActual() {
            return new GregorianCalendar().get(Calendar.YEAR);
    }
    
    
    /**
     *Metodo que devuelve un String de fecha actual
     */
    public static String getFechaActualString() {
            return new SimpleDateFormat(FORMATO).format(new Date());
    }
}
