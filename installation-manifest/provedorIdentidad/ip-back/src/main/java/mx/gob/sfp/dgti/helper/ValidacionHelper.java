package mx.gob.sfp.dgti.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import java.security.SecureRandom;
import mx.gob.sfp.dgti.util.Constantes;

/**
 * Helper para validaciones
 *
 * @author programador07
 * @since 08/03/2019
 */
public class ValidacionHelper implements java.io.Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidacionHelper.class);    
    private static final String DATE_FORMAT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss";    
    private static final String CHARS_TO_OTP = "A0B1C2D3E4F5G6H7I8J9K1L2M3N4O5P6Q7R8S9T1U2V3W4X5Y6Z";

    /**
     * Constructor privado para que la clase no pueda ser instanciada
     */
    private ValidacionHelper() {

    }

    /**
     * Metodo que valida si un objeto es nulo
     *
     * @param value
     * @return boolean
     */
    public static boolean isNull(Object value) {
        return value == Constantes.OBJNULL;
    }

    /**
     * Metodo que valida si un objeto no es nulo
     *
     * @param value
     * @return boolean
     */
    public static boolean isNotNull(Object value) {
        return !isNull(value);
    }

    /**
     * Metodo que valida si un objeto está vacío
     *
     * @param value
     * @return boolean
     */
    public static boolean isEmpty(Object value) {
        return value.toString().trim().isEmpty();
    }

    /**
     * Metodo que realiza la conversion de fecha a String con formato
     * "yyyy-MM-dd'T'hh:mm:ss"
     *
     * @param fecha Fecha que se pretende regresar en String.
     * @return String Fecha con formato "yyyy-MM-dd'T'hh:mm:ss"
     * @author cgarias
     * @since 01/06/2017
     */
    public static String getFechaFormatoISO8601(Date fecha) {
        if (fecha != null) {
            return new SimpleDateFormat(DATE_FORMAT_ISO_8601).format(fecha);
        }
        return null;
    }

    /**
     * Metodo que realiza la conversión de fecha String a Date con formato
     * "yyyy-MM-dd'T'hh:mm:ss.S"
     *
     * @param fecha Fecha que se pretende regresar en Date.
     * @return Date Fecha con formato "yyyy-MM-dd'T'hh:mm:ss"
     * @author cgarias
     * @since 01/06/2017
     */
    public static Date convertStringToDate(String fecha) {
        Date date = null;
        if (fecha != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_ISO_8601);
            try {
                date = formatter.parse(fecha);
            } catch (ParseException e) {
                LOGGER.error(e.getMessage());
            }
        }
        return date;
    }

    public static String createOtpToTelegram() {
        int len = 8;
        char[] password = new char[len];
        for (int i = 0; i < len; i++) {
            password[i] = CHARS_TO_OTP.charAt(new SecureRandom().nextInt(CHARS_TO_OTP.length()));
        }
        return String.valueOf(password);
    }
    
    public static String getFechaLikeNumber(){
        return new SimpleDateFormat(DATE_FORMAT_ISO_8601).format(new Date());
    }
}
