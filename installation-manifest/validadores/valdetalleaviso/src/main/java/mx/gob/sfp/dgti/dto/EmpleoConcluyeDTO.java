/**
 * @(#)EmpleoConcluyeDTO.java 30/01/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.RegistroBaseDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.EnteReceptorDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoUnoFkDTO;

/**
 * DTO para el modulo de adeudos y pasivos
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 30/01/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class EmpleoConcluyeDTO extends RegistroBaseDTO {

    /**
     * Ente receptor
     */
    private EnteReceptorDTO ente;

    /**
     * Nivel jerarquico
     */
    private CatalogoUnoFkDTO nivelJerarquico;

    /**
     * Fecha de conclusion del encargo
     */
    private String fechaConclusionEncargo;

    public EmpleoConcluyeDTO() {
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public EmpleoConcluyeDTO(JsonObject json) {
        EmpleoConcluyeDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        EmpleoConcluyeDTOConverter.toJson(this, json);
        return json;
    }

    public EnteReceptorDTO getEnte() {
        return ente;
    }

    public void setEnte(EnteReceptorDTO ente) {
        this.ente = ente;
    }

    public String getFechaConclusionEncargo() {
        return fechaConclusionEncargo;
    }

    public void setFechaConclusionEncargo(String fechaConclusionEncargo) {
        this.fechaConclusionEncargo = fechaConclusionEncargo;
    }

    public CatalogoUnoFkDTO getNivelJerarquico() {
        return nivelJerarquico;
    }

    public void setNivelJerarquico(CatalogoUnoFkDTO nivelJerarquico) {
        this.nivelJerarquico = nivelJerarquico;
    }

    @Override
    public String toString() {
        return "EmpleoConcluyeDTO{" +
                "ente=" + ente +
                ", nivelJerarquico=" + nivelJerarquico +
                ", fechaConclusionEncargo='" + fechaConclusionEncargo + '\'' +
                "} " + super.toString();
    }
}
