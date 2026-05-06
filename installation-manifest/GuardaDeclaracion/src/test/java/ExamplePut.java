
import java.util.Date;
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
public class ExamplePut {
    
    public static void main(String args[]){
//        JsonObject json = new JsonObject()
//                .put("nuevo", "nuevo")
//                .put("segundo", "segundo");
//        System.out.println(json.getString("nuevo"));
//        json.put("nuevo", "alterado");
//        System.out.println(json.getString("nuevo"));
//        System.out.println(new ObjectId(new Date()));
        System.out.println(FechaUtil.getFechaFormatoISO8601(new Date()));
//        for(EnumModuloArreglo index : EnumModuloArreglo.values()){
//            System.out.println(index.getArregoName());
//        }
        String rfc = "AICC900114DS0";
        System.out.println(rfc.substring(0,10));
        System.out.println(rfc.substring(10));
    }
}
