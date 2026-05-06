/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.declaranet;

import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.encrypt.im.ValidaMensajeFront;
import org.junit.*;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author cgarias
 */
public class ValidaEncryptDeclaranetInterfaceTest {
    
    public ValidaEncryptDeclaranetInterfaceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of fidelidadDeclaracion method, of class ValidaEncryptDeclaranetInterface.
     */
    @Test
    public void testFidelidadDeclaracion() {
        System.out.println("fidelidadDeclaracion");
        String valorEncrypt = "69cebc7218297d88503309144a0797283511ac2b6cfd1ba714d0b8c689346b5023651247edd70ffe0ee6c2f7706e0c237d283250554dde56eadea4eec0152882";
        JsonObject decla = new JsonObject().put("unObjeto","\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\",\"valor\":\"nuevo\"");
//        StringBuilder declaracionAsJson = new StringBuilder("{\"declaracion\":").append(decla).append(", \"digitoVerificador\":\"").append(valorEncrypt).append("\"}");        
                            //"dcc7e2f1a4583b23243144750025c0c2cdd8a50a313c5da83929788ebe6d80399458d989ca0b76cc22ec0e0eb1e4a2ef7bacbfa486a102d2b79db47f4b3b9f54"
                            //"7cc8e1418daa1c63c4c0d33106b9b58c50615be3310115ae8036a5cb1a984dfa6bb3db466dd5136516d04ae7249bf443b0951ed148ce1bbf2e771f81f1a44049"
        
        //JsonObject expResult = new JsonObject().put("declaracion", decla);
        JsonObject result = 
                ValidaMensajeFront
                        .fidelidadMensaje(
                                new JsonObject()
                                    .put("declaracion", decla)
                                    .put("digitoVerificador", valorEncrypt)
                        );
        assertEquals(decla.toString(), result!= null?result.toString():null);
    }
    
    public static void main(String args[]){
        new ValidaEncryptDeclaranetInterfaceTest().testFidelidadDeclaracion();
    }
}
