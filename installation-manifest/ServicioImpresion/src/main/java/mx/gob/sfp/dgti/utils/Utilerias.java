/*
 *
 */
package mx.gob.sfp.dgti.utils;

import java.util.logging.Logger;

/**
 * Clase que contiene el método para desencriptar la
 * información que se recibe de las consultas publicas.
 * 
 * @author programador09
 */
public class Utilerias {
    private static final Logger logger = Logger.getLogger(Utilerias.class.getName());
    
    public static String decrypt(String parametro){
        int vAleat = 0;
        int i_letra = 0;
        char c_letra;
        String s_letra = "";
        String resultado = "";
        int posLlave = 0;
        try {
            if (parametro.contains("|")) {
                posLlave = parametro.indexOf("|") + 1;
            }
            c_letra = parametro.charAt(posLlave);
            vAleat = ((int) c_letra) - 64;
            for (int i = 1; i <= parametro.length(); i++) {
                    c_letra = parametro.charAt(parametro.length() - i);
                    i_letra = parametro.charAt(parametro.length() - i);
                    s_letra = parametro.substring(parametro.length() - i, parametro.length() - i + 1);

                    if (!s_letra.equalsIgnoreCase("|")) {
                        s_letra = String.valueOf((char) (i_letra - vAleat));
                    }

                    if ((parametro.length() - i) != posLlave) {
                        resultado += s_letra;
                    }
            }
        } catch (Exception e) {
            logger.severe(String.format("Error en la desencripcion de cadena. %s ", e));
        }
        return resultado;
    }
}
