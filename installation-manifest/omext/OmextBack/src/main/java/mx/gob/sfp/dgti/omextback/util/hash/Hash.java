/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.omextback.util.hash;

import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que contiene metodos para realizar
 * calculos de encriptación de cadenas.
 *
 * @author cgarias
 * @since 19/05/2017
 */
public class Hash {

    private static final  Logger logger = Logger.getLogger(Hash.class.getName());
    private final String tipoDigest = "SHA";

    public String digestCadena(String sCadena) {
        byte[] mensajeDigest;
        try {
            MessageDigest md = MessageDigest.getInstance(tipoDigest);
            md.update(sCadena.getBytes());
            mensajeDigest = md.digest();
        } catch (java.security.NoSuchAlgorithmException se) {
            logger.log(Level.SEVERE, "NoSuchAlgorithmException : {0}", se);
            return "Error al calcular el hash :" + se.getMessage();
        }
        return convierteHexadecimal(mensajeDigest);
    }

    private String convierteHexadecimal(byte[] mensajeDigest) {
        int n, d1, d2;
        StringBuilder cadena = new StringBuilder();
        for (int i = 0; i < mensajeDigest.length; i++) {
            n = mensajeDigest[i];
            if (n < 0) {
                n = 256 + n;
            }
            d1 = n / 16;
            d2 = n % 16;
            cadena.append(Integer.toHexString(d1)).append(Integer.toHexString(d2));
        }
        return cadena.toString();
    }

    public static String conEspaciosImPares(String str, String aux) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i <= str.length() - 1; i++) {
            if (((i + 1) % 2) == 1) {
                res.append(aux).append(str.substring(i, i + 1));
            } else {
                res.append(str.substring(i, i + 1));
            }
        }
        return res.toString().trim();
    }

}
