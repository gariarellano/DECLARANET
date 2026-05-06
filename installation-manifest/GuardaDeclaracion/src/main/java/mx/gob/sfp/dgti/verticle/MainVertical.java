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
    private static final String VERTICAL_PROC_FUP = "mx.gob.sfp.dgti.verticle.modulos.VerticalConsumerPocesoFup";
    private static final String VERTICAL_CUMPLIMIENTO = "mx.gob.sfp.dgti.verticle.modulos.VerticalCumplimiento";
    private static final String VERTICAL_MIGRA_DECLARACION = "mx.gob.sfp.dgti.verticle.modulos.VerticalConsumerMigraDeclaracion";
    private static final String VERTICAL_REGISTROS_EXTERNOS = "mx.gob.sfp.dgti.verticle.modulos.VerticalRegistrosExternos";
    private static final String VERTICAL_CREA_REGISTROS_PUBLICOS = "mx.gob.sfp.dgti.verticle.modulos.VerticalRegHistialNobreInstitucion";
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        List<String> verticles = new ArrayList<>();
        verticles.add(SERVER);
        verticles.add(VERTICAL_PROC_FUP);
        verticles.add(VERTICAL_CUMPLIMIENTO);
        verticles.add(VERTICAL_MIGRA_DECLARACION);
        verticles.add(VERTICAL_REGISTROS_EXTERNOS);
        verticles.add(VERTICAL_CREA_REGISTROS_PUBLICOS);
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
