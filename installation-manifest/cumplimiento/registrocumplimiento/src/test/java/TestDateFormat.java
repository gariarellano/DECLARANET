
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cgarias
 */
public class TestDateFormat {
    private static HashMap<String, Integer> consultadosEnteReceptorDelDia;    
//    public static void main(String[] args){
//        System.out.println(new SimpleDateFormat("yyyMMdd").format(new Date()));
//    }
    public static void main(String[] args){
        consultadosEnteReceptorDelDia = new HashMap<>();
        consultadosEnteReceptorDelDia.put("jk4hnjk23523", 20200901);
        
        System.out.println(consultadosEnteReceptorDelDia.containsKey("jk4hnjk23523"));
        System.out.println(consultadosEnteReceptorDelDia.get("jk4hnjk23523"));
    }
}
