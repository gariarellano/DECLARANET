/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.declaracion.dto.general;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.individual.NivelOrdenGobiernoDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumAmbitoPoder;

/**
 * Clase que contiene las propiedades de un 
 * ente del servicio de entes
 * 
 * @author cgarias
 * @since 27/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class EnteReceptorDTO {
    private String id;
    private String nombre;
    private NivelOrdenGobiernoDTO nivelOrdenGobierno;	
    private EnumAmbitoPoder ambitoPublico;
    
    public EnteReceptorDTO() {}
    
    public EnteReceptorDTO(String id, String nombre, NivelOrdenGobiernoDTO nivelOrdenGobierno, EnumAmbitoPoder ambitoPublico){
        this.id = id;
        this.nombre = nombre;
        this.nivelOrdenGobierno = nivelOrdenGobierno;
        this.ambitoPublico = ambitoPublico;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public EnteReceptorDTO(JsonObject json) {
    	EnteReceptorDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        EnteReceptorDTOConverter.toJson(this, json);
        return json;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public NivelOrdenGobiernoDTO getNivelOrdenGobierno() {
        return nivelOrdenGobierno;
    }

    public void setNivelOrdenGobierno(NivelOrdenGobiernoDTO nivelOrdenGobierno) {
        this.nivelOrdenGobierno = nivelOrdenGobierno;
    }

    public EnumAmbitoPoder getAmbitoPublico() {
        return ambitoPublico;
    }

    public void setAmbitoPublico(EnumAmbitoPoder ambitoPublico) {
        this.ambitoPublico = ambitoPublico;
    }
}
