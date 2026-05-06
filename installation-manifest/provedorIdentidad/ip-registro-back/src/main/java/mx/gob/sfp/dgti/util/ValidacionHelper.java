package mx.gob.sfp.dgti.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Helper para validaciones
 * @author Miriam Sánchez Sánchez programador07
 * @since 08/03/2019
 */
public class ValidacionHelper implements java.io.Serializable {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(ValidacionHelper.class);
	
	/**
     * Constructor privado para que la clase no pueda ser instanciada
     */
    private ValidacionHelper(){
        
    }
    /**
     * Método que valida si un objeto es nulo
     * @param value
     * @return boolean
     */
    public static boolean isNull(Object value){
        return value == Constantes.OBJNULL;
    }
    
    /**
     * Método que valida si un objeto no es nulo
     * @param value
     * @return boolean
     */
    public static boolean isNotNull(Object value){
        return !isNull(value);
    }
    
    /**
     * Método que valida si un objeto está vacío
     * @param value
     * @return boolean
     */
    public static boolean isEmpty(Object value){
        return value.toString().trim().isEmpty();
    }
    
    /**
	 * Método que realiza la conversion de fecha a String con formato
	 * "yyyy-MM-dd'T'hh:mm:ss.S"
	 * @param fecha Fecha que se pretende regresar en String.
	 * @return String Fecha con formato "yyyy-MM-dd'T'hh:mm:ss.S"
	 * @author cgarias
	 * @since 01/06/2017
	 */
   public static String getFechaFormatoISO8601(Date fecha) {
       if (fecha != null) {
               return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S").format(fecha);
       }
       return null;
   }
   
   /**
	 * Método que realiza la conversion de fecha a Date con formato
	 * "yyyy-MM-dd'T'hh:mm:ss.S"
	 * @param fecha Fecha que se pretende regresar en Date.
	 * @return String Fecha con formato "yyyy-MM-dd'T'hh:mm:ss.S"
	 * @author cgarias
	 * @since 01/06/2017
	 */
   public static Date convertStringToDate(String fecha) {
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
	   Date date = null;
	   try {
		   date = formatter.parse(fecha);
		} catch (ParseException e) {
			LOGGER.info(e);
		}
		return date;
   }

}
