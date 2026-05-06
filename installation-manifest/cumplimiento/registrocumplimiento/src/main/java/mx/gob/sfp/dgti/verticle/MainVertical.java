/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "GuardaDeclaracion" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.verticle;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import java.util.ArrayList;
import java.util.List;
import mx.gob.sfp.dgti.VerticalConfig;

/**
 *
 * @author cgarias
 */
public class MainVertical extends VerticalConfig{

    private static final String SERVER = "mx.gob.sfp.dgti.verticle.Server";
//    private static final String ID_VALIDATOR = "mx.gob.sfp.dgti.verticle.modulos.VerticalConsumersValidatorsIds";
    
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        List<String> verticles = new ArrayList<>();
        verticles.add(SERVER);
        this.startDeploy(verticles);
    }    
    /**
     * Main para pruebas locales
     * @param args
     */
    public static void main(String[] args) {
        Vertx vertex = Vertx.vertx();
        vertex.deployVerticle(new MainVertical());
    }
}
