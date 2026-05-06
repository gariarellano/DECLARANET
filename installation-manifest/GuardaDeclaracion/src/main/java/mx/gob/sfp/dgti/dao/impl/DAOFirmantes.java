/*/
 * To change this license header, choose License Headers in Project Properties./
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.dao.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.dao.DAOFirmantesInterface;
import mx.gob.sfp.dgti.dao.DAOGeneric;
import static mx.gob.sfp.dgti.util.Constantes.COLLECTION_NAME_FIRMANTE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ACTIVO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_NOMBRE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_PRIMER_APELLIDO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_PUESTO_FIRMANTE;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_SEGUNDO_APELLIDO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_TITULO_FIRMANTE;

/**
 * Clas que ccontiene la implementacion de los étodos de acceso
 * a la tabla de firmantes de recepcion declaracion.
 * 
 * @author cgarias
 * @since 19/12/2019
 */
public class DAOFirmantes extends DAOGeneric implements DAOFirmantesInterface {

    public DAOFirmantes(Vertx  vertx){
        super(vertx);
    }
    
    @Override
    public DAOFirmantesInterface consultaFirmanteActivo(Integer collName, Handler<AsyncResult<JsonObject>> resultHandler) {
        String collectionFirmante  = COLLECTION_NAME_FIRMANTE+collName;
        JsonObject query = new JsonObject().put(FIELD_ACTIVO,1);
        JsonObject etiquetas = new JsonObject()
                .put(FIELD_TITULO_FIRMANTE,1)
                .put(FIELD_NOMBRE,1)
                .put(FIELD_PRIMER_APELLIDO,1)
                .put(FIELD_SEGUNDO_APELLIDO,1)
                .put(FIELD_PUESTO_FIRMANTE,1);
        mongoClient.findOne(collectionFirmante, query, etiquetas, firmante -> {
            if(firmante.succeeded()){
                resultHandler.handle(firmante);
            }else {
                resultHandler.handle(Future.failedFuture(firmante.cause()));
            }
        });
        return this;
    }
}
