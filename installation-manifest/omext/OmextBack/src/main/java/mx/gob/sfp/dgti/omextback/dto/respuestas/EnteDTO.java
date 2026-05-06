/**
 * @(#)EnteDTO.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumAmbitoPublico;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumNivelOrdenGobierno;

/**
 * DTO para el ente
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class EnteDTO {

    /**
     * Indica el tipo de ambito
     */
    private EnumAmbitoPublico ambitoPublico;

    /**
     * Id del ente
     */
    private String id;

    /**
     * Nivel de gobierno
     */
    private EnumNivelOrdenGobierno nivelOrdenGobierno;

    /**
     * Nombre del ente
     */
    private String nombre;

    /**
     * Constructor
     */
    public EnteDTO(){//Constructor
    }


    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public EnteDTO(JsonObject json) {
        EnteDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        EnteDTOConverter.toJson(this, json);
        return json;
    }

    public EnumAmbitoPublico getAmbitoPublico() {
        return ambitoPublico;
    }

    public void setAmbitoPublico(EnumAmbitoPublico ambitoPublico) {
        this.ambitoPublico = ambitoPublico;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EnumNivelOrdenGobierno getNivelOrdenGobierno() {
        return nivelOrdenGobierno;
    }

    public void setNivelOrdenGobierno(EnumNivelOrdenGobierno nivelOrdenGobierno) {
        this.nivelOrdenGobierno = nivelOrdenGobierno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Metodo toString
     * @return
     */
    @Override
    public String toString() {
        return "EnteDTO{" +
                "ambitoPublico=" + ambitoPublico +
                ", id='" + id + '\'' +
                ", nivelOrdenGobierno=" + nivelOrdenGobierno +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
