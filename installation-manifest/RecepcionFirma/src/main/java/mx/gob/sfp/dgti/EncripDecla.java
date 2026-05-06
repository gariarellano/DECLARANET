/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "OMEXT" Sistema que permite la identificación de los Omisos y Extemporaneos en 
 * presentar la declaración patrimonial y de conflicto de intereses.
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti;

import java.security.MessageDigest;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import scala.collection.mutable.StringBuilder;

/**
 * Clase que realiza el encriptado de la contraseña así como para validarla.
 * @author cgarias
 * @since 03/03/2019
 */
public class EncripDecla {

	private static final Logger LOGGER = LoggerFactory.getLogger(EncripDecla.class);
    static String tipoDigest = "SHA";
    
    public static String conEspaciosImPares(String str, String aux) {
        String res = "";
        for (int i = 0; i <= str.length() - 1; i++) {
            if (((i + 1) % 2) == 1) {
                res = res + aux + str.substring(i, i + 1);
            } else {
                res = res + str.substring(i, i + 1);
            }
        }
        return res.trim();
    }
    
    /**
     * Encriptación de la contraseña
     * 
     * @param sLogin    campo login  
     * @param sPwd      password del usuario 
     * 
     * @return contraseña encriptada.
     */
    public static String digestPassword(String sLogin, String sPwd) {
        String sSalt = reverseString(sLogin);
        String sToDigest = sSalt + "&&" + sPwd;           
        return digestCadena(sToDigest);
    }
    
     /**
     * Invesion de cadenas, se usa para la cadena de la contraseña.
     * @param input cadena a hacer reversa.
     * @return String reversa de la cadena.
     */
    private static String reverseString(String input) {
        return new StringBuilder(input).reverse().toString();
    }  
    
    /**
     * 
     * @param sCadena
     * @return 
     */
    public static String digestCadena(String sCadena) {
        byte[] mensajeDigest;
        try {
            MessageDigest md = MessageDigest.getInstance(tipoDigest);
            md.update(sCadena.getBytes());
            mensajeDigest = md.digest();
        } catch (java.security.NoSuchAlgorithmException se) {
        	LOGGER.error(se);
            return "Error al calcular el hash :" + se.getMessage();
        }
        return convierteHexadecimal(mensajeDigest);
    }
    
    /**
     * @param mensajeDigest
     * @return 
     */
    public static String convierteHexadecimal(byte[] mensajeDigest) {

        int n;
        int d1;
        int d2;
        StringBuilder cadena = new StringBuilder();
        for (int i = 0; i < mensajeDigest.length; i++) {
            n = mensajeDigest[i];
            if (n < 0) {
                n = 256 + n;
            }
            d1 = n / 16;
            d2 = n % 16;
            cadena.append(Integer.toHexString(d1))
            	.append(Integer.toHexString(d2));
        }
        return cadena.toString();
    }
}