
import io.vertx.core.json.JsonObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import mx.gob.sfp.dgti.utils.FechaUtil;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cgarias
 */
public class test {
    private static final Pattern pattern = Pattern.compile("\\d+");
    private static final Pattern validador = Pattern.compile("^([a-zA-Z0-9ÑÁÉÍÓÚÜñáéíóúü~.,' ]){0,2001}$");
    public static void main(String[] args){
        String valoresRempla = "TECNICA (ASUNTOS INTERNACIONALES";
        System.out.println(validador.matcher("TECNICA (ASUNTOS INTERNACIONALES)").matches());
        System.out.println(
                valoresRempla
                        .replaceAll("\\{", "").replaceAll("}", "")
                            .replaceAll("\\[", "").replaceAll("]", "")
                            .replaceAll("¿", "").replaceAll("\\?", "")
                            .replaceAll("¡", "").replaceAll("!", "")
                            .replaceAll("%", "").replaceAll("\\*", "")
                            .replaceAll("\\+", "").replaceAll("=", "")
                            .replaceAll("\\-", "").replaceAll("\\.", "")
                            .replaceAll(",", "").replaceAll(":", "")
                            .replaceAll(";", "").replaceAll("\\$", "")
                            .replaceAll("\"", "").replaceAll("/", "")
                            .replaceAll("&", "").replaceAll("\\^", "")
                            .replaceAll("\\|", "").replaceAll("°", "")
                            .replaceAll("¬", "").replaceAll("\\\\", "")
                            .replaceAll("\\(", "").replaceAll("\\)", "")
                                
        );
        Calendar fEncargo = Calendar.getInstance();
        try {
            fEncargo.setTime(new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-10"));
        } catch (ParseException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        fEncargo.add(Calendar.DAY_OF_MONTH, 15);
        System.out.println(FechaUtil.getFechaFormatoISO8601(fEncargo.getTime()));
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(fEncargo.getTime()));
        fEncargo.add(Calendar.MONTH, -1);
        System.out.println(FechaUtil.getFechaFormatoISO8601(fEncargo.getTime()));
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(fEncargo.getTime()));
        
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2012+1);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        System.out.println(FechaUtil.getFechaFormatoISO8601(cal.getTime()));
        
        //se contemplara las modificaciones a 31 de mayo. cambio Javier
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.YEAR, 2012);
        cal1.set(Calendar.MONTH, 4);
        cal1.set(Calendar.DAY_OF_MONTH, cal1.getActualMaximum(Calendar.DATE));
        System.out.println(FechaUtil.getFechaFormatoISO8601(cal1.getTime()));
        System.out.println();
        
        
        
        System.out.println(pattern.matcher("150").matches());
        System.out.println(pattern.matcher("1").matches());
        System.out.println(pattern.matcher("2500").matches());
        System.out.println(pattern.matcher("350").matches());
        System.out.println(pattern.matcher("1000").matches());
        System.out.println(pattern.matcher("50000").matches());
        System.out.println(pattern.matcher("8000000").matches());
        System.out.println(pattern.matcher("5416354354384").matches());
        System.out.println(pattern.matcher("2341354354354358435453").matches());
        System.out.println(pattern.matcher("54345").matches());
        System.out.println(pattern.matcher("5654348").matches());
        System.out.println(pattern.matcher("4653468944694864").matches());
        System.out.println(pattern.matcher("28548374384843843543848").matches());
        System.out.println(pattern.matcher("574354").matches());
        System.out.println(pattern.matcher("5435").matches());
        System.out.println(pattern.matcher("abc").matches());
        System.out.println(pattern.matcher("0000000000").matches());
    }
}
