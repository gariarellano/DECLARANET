/**
 * @(#)ParamsEnteReceptorDTO.java 22/04/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.enteReceptor;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumAmbitoPublico;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumNivelOrdenGobierno;

/**
 * DTO con parametros obtenidos de entes receptores
 *
 * @author pavel.martinez
 * @since 22/04/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ParamsEnteReceptorDTO {

    public ParamsEnteReceptorDTO(){//Constructor
    }

    public ParamsEnteReceptorDTO(JsonObject json) {
        ParamsEnteReceptorDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ParamsEnteReceptorDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * Nivel de gobierno
     */
    private EnumNivelOrdenGobierno nivelGobierno;

    /**
     * Poder o ambito publico de las instituciones
     */
    private EnumAmbitoPublico poder;

    /**
     * Entidad federativa en caso de nivel ESTATAL o MUNICIPAL
     */
    private Integer idEntidadFederativa;

    /**
     * Minicipio en caso de nivel MUNICIPAL
     */
    private Integer idMunicipio;

    /**
     * collName de la consulta
     */
    private Integer collName;



    public EnumNivelOrdenGobierno getNivelGobierno() {
        return nivelGobierno;
    }

    public void setNivelGobierno(EnumNivelOrdenGobierno nivelGobierno) {
        this.nivelGobierno = nivelGobierno;
    }

    public EnumAmbitoPublico getPoder() {
        return poder;
    }

    public void setPoder(EnumAmbitoPublico poder) {
        this.poder = poder;
    }

    public Integer getIdEntidadFederativa() {
        return idEntidadFederativa;
    }

    public void setIdEntidadFederativa(Integer idEntidadFederativa) {
        this.idEntidadFederativa = idEntidadFederativa;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public Integer getCollName() {
        return collName;
    }

    public void setCollName(Integer collName) {
        this.collName = collName;
    }

    @Override
    public String toString() {
        return "ParamsEnteReceptorDTO{" +
                "nivelGobierno=" + nivelGobierno +
                ", poder=" + poder +
                ", idEntidadFederativa=" + idEntidadFederativa +
                ", idMunicipio=" + idMunicipio +
                ", collName=" + collName +
                '}';
    }
}
