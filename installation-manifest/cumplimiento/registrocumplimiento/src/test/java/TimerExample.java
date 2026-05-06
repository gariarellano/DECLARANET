
import io.vertx.core.Vertx;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gio_j
 */
public class TimerExample {
     public static void main(String... args) {
         Vertx vertx = Vertx.vertx();
        System.out.println("Starting, waiting for greetings");
        vertx.setTimer(5000, l -> {
           System.out.println("Hello");
        });
        vertx.setPeriodic(5000, l -> {
           System.out.println("Hello periodic");
        });
    }
    
}
