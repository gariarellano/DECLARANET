package mx.gob.sfp.dgti.service;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public interface ServiceConsultaPublicaInterface {

    /**
     * Contructor de cla clase. Recibe un objeto Vertx
     *
     * @return 
     */
    static ServiceConsultaPublicaInterface init(Vertx vertx, WebClient client) {
        return new ServiceConsultaPublicaImpl(vertx, client);
    }

    public Future<JsonObject> buscarServidorPublicoSp(String data, Integer collName);
    
//    public Future<JsonObject> buscarServidorPublico(String data, Integer collName);

    public Future<JsonObject> buscarHistorialServidorPublico(Integer idUsr, Integer collName);
    
    public Future<JsonObject> consultaGabinete(int tipoGabinete);
}
