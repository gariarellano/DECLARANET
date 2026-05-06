/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author cgarias
 */
public interface ServicePortalVulneradosInterface {
    static ServicePortalVulneradosInterface init(Vertx vertx){
        return new ServicePortalVulnerado(vertx);
    }
    
    public ServicePortalVulneradosInterface consultaDatosExpuestos(Integer collName, String curp, String rfc, Handler<AsyncResult<JsonObject>> respuesta);
    public ServicePortalVulneradosInterface consultaDatosExpuestosAdmin(Integer collName, String param, Handler<AsyncResult<JsonObject>> respuesta);
}
