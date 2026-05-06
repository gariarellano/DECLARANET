/**
 * @(#)EnteVistaDTO.java 03/03/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
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
 * @since 03/03/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class EnteVistaDTO {

    /**
     * Indica el tipo de ambito
     */
    private EnumAmbitoPublico ambitoPublico;

    /**
     * Id del ente
     */
    private String idEnte;

    /**
     *
     */
    private String nombreCorto;

    /**
     *
     */
    private String nombreEnte;

    /**
     * Nivel de gobierno
     */
    private EnumNivelOrdenGobierno nivelOrdenGobierno;

    /**
     *
     */
    private Integer ramo;

    /**
     *
     */
    private String ur;

    /**
     * Constructor
     */
    public EnteVistaDTO(){//Constructor
    }


    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public EnteVistaDTO(JsonObject json) {
        EnteVistaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        EnteVistaDTOConverter.toJson(this, json);
        return json;
    }

    public EnumAmbitoPublico getAmbitoPublico() {
        return ambitoPublico;
    }

    public void setAmbitoPublico(EnumAmbitoPublico ambitoPublico) {
        this.ambitoPublico = ambitoPublico;
    }

    public String getIdEnte() {
        return idEnte;
    }

    public void setIdEnte(String idEnte) {
        this.idEnte = idEnte;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    public String getNombreEnte() {
        return nombreEnte;
    }

    public void setNombreEnte(String nombreEnte) {
        this.nombreEnte = nombreEnte;
    }

    public EnumNivelOrdenGobierno getNivelOrdenGobierno() {
        return nivelOrdenGobierno;
    }

    public void setNivelOrdenGobierno(EnumNivelOrdenGobierno nivelOrdenGobierno) {
        this.nivelOrdenGobierno = nivelOrdenGobierno;
    }

    public Integer getRamo() {
        return ramo;
    }

    public void setRamo(Integer ramo) {
        this.ramo = ramo;
    }

    public String getUr() {
        return ur;
    }

    public void setUr(String ur) {
        this.ur = ur;
    }

    @Override
    public String toString() {
        return "EnteVistaDTO{" +
                "ambitoPublico=" + ambitoPublico +
                ", idEnte='" + idEnte + '\'' +
                ", nombreCorto='" + nombreCorto + '\'' +
                ", nombreEnte='" + nombreEnte + '\'' +
                ", nivelOrdenGobierno=" + nivelOrdenGobierno +
                ", ramo=" + ramo +
                ", ur='" + ur + '\'' +
                '}';
    }
}
