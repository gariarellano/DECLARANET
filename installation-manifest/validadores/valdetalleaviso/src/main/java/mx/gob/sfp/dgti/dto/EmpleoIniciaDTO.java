/**
 * @(#)EmpleoIniciaDTO.java 30/01/2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.RegistroBaseDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.DomicilioDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.EnteReceptorDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoUnoFkDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;

/**
 * DTO para el modulo de adeudos y pasivos
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 30/01/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class EmpleoIniciaDTO extends RegistroBaseDTO {

    /**
     * Ente receptor
     */
    private EnteReceptorDTO ente;

    /**
     * Fecha de conclusion del encargo
     */
    private String fechaInicioEncargo;

    /**
     * Empleo cargo o comision
     */
    private String empleoCargoComision;

    /**
     * Nivel jerarquico
     */
    private CatalogoUnoFkDTO nivelJerarquico;

    /**
     * Nivel del empleo, cargo o comision
     */
    private String nivelEmpleoCargoComision;

    /**
     * Indica si es contratado por honorarios
     */
    private Boolean contratadoPorHonorarios;

    /**
     * Area de adscripcion
     */
    private String areaAdscripcion;

    /**
     * Domicilio del encargo
     */
    private DomicilioDTO domicilio;


    public EmpleoIniciaDTO() {
    }

    public EmpleoIniciaDTO(String id, String idPosicionVista, EnumTipoOperacion tipoOperacion,
                           boolean registroHistorico, boolean verificar, EnteReceptorDTO ente,
                           String fechaInicioEncargo, String empleoCargoComision, CatalogoUnoFkDTO nivelJerarquico,
                           String nivelEmpleoCargoComision, Boolean contratadoPorHonorarios, String areaAdscripcion,
                           DomicilioDTO domicilio) {
        super(id, idPosicionVista, tipoOperacion, registroHistorico, verificar);
        this.ente = ente;
        this.fechaInicioEncargo = fechaInicioEncargo;
        this.empleoCargoComision = empleoCargoComision;
        this.nivelJerarquico = nivelJerarquico;
        this.nivelEmpleoCargoComision = nivelEmpleoCargoComision;
        this.contratadoPorHonorarios = contratadoPorHonorarios;
        this.areaAdscripcion = areaAdscripcion;
        this.domicilio = domicilio;
    }

    public String getAreaAdscripcion() {
        return areaAdscripcion;
    }

    public void setAreaAdscripcion(String areaAdscripcion) {
        this.areaAdscripcion = areaAdscripcion;
    }

    public EnteReceptorDTO getEnte() {
        return ente;
    }

    public void setEnte(EnteReceptorDTO ente) {
        this.ente = ente;
    }

    public String getFechaInicioEncargo() {
        return fechaInicioEncargo;
    }

    public void setFechaInicioEncargo(String fechaInicioEncargo) {
        this.fechaInicioEncargo = fechaInicioEncargo;
    }

    public String getEmpleoCargoComision() {
        return empleoCargoComision;
    }

    public void setEmpleoCargoComision(String empleoCargoComision) {
        this.empleoCargoComision = empleoCargoComision;
    }

    public CatalogoUnoFkDTO getNivelJerarquico() {
        return nivelJerarquico;
    }

    public void setNivelJerarquico(CatalogoUnoFkDTO nivelJerarquico) {
        this.nivelJerarquico = nivelJerarquico;
    }

    public String getNivelEmpleoCargoComision() {
        return nivelEmpleoCargoComision;
    }

    public void setNivelEmpleoCargoComision(String nivelEmpleoCargoComision) {
        this.nivelEmpleoCargoComision = nivelEmpleoCargoComision;
    }

    public Boolean getContratadoPorHonorarios() {
        return contratadoPorHonorarios;
    }

    public void setContratadoPorHonorarios(Boolean contratadoPorHonorarios) {
        this.contratadoPorHonorarios = contratadoPorHonorarios;
    }

    public DomicilioDTO getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(DomicilioDTO domicilio) {
        this.domicilio = domicilio;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public EmpleoIniciaDTO(JsonObject json) {
        EmpleoIniciaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        EmpleoIniciaDTOConverter.toJson(this, json);
        return json;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EmpleoIniciaDTO{");
        sb.append("ente=").append(ente);
        sb.append(", fechaInicioEncargo='").append(fechaInicioEncargo).append('\'');
        sb.append(", empleoCargoComision='").append(empleoCargoComision).append('\'');
        sb.append(", nivelJerarquico=").append(nivelJerarquico);
        sb.append(", nivelEmpleoCargoComision='").append(nivelEmpleoCargoComision).append('\'');
        sb.append(", contratadoPorHonorarios=").append(contratadoPorHonorarios);
        sb.append(", domicilio=").append(domicilio);
        sb.append('}');
        return sb.toString();
    }
}


