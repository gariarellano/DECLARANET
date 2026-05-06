
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.utils.FechaUtil;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gio_j
 */
public class Testcalendat {
    private static Ejemplo ejemplo;
    public static void main(String args[]){
        ejemplo = new Ejemplo();
         ejemplo.prueba();
    }
    
    
    public static class Ejemplo{
        private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        public Ejemplo(){}
        
        public void prueba(){
            
            Calendar _FechaLimite = Calendar.getInstance();
            _FechaLimite.set(Calendar.YEAR, 2021);
            _FechaLimite.set(Calendar.DATE, 31);
            _FechaLimite.set(Calendar.MONTH, Calendar.JANUARY);
            
//            _FechaLimite.set(Calendar.DAY_OF_MONTH, 10);
            //_FechaLimite.set(Calendar.DATE, 31);
            _FechaLimite.set(Calendar.HOUR_OF_DAY, 23);
            _FechaLimite.set(Calendar.MINUTE, 59);
            _FechaLimite.set(Calendar.SECOND, 59);
            
            System.out.println(format.format(_FechaLimite.getTime()));
            
//            try {
//                Calendar calendar = Calendar.getInstance();
//                String fechaEvaluacion = "2020-02-01";            
//                String fechaFin = "2020-05-31";            
//                while(FechaUtil.fechaMenorQue(fechaEvaluacion, fechaFin)){                    
//                    calendar.setTime(format.parse(fechaEvaluacion));
//                    calendar.add(Calendar.DAY_OF_MONTH, 1);
//                    String fechaMasUno = format.format(calendar.getTime());                    
//                    System.out.println(String.format("Periodo a ejecutar %s a %s", fechaEvaluacion, fechaMasUno));
//                    
//                    
//                    fechaEvaluacion= fechaMasUno;
//                }
//            } catch (ParseException ex) {
//                Logger.getLogger(Ejemplo.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }
}
