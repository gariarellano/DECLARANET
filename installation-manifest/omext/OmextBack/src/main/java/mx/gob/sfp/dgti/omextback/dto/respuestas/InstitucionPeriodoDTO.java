/**
 * @(#)InstitucionPeriodoDTO.java 25/01/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumEstatusVista;

/**
 * DTO de una institucion para la consulta por periodo
 *
 * @author pavel.martinez
 * @since 25/01/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class InstitucionPeriodoDTO {

    public InstitucionPeriodoDTO(){//Constructor
    }

    public InstitucionPeriodoDTO(JsonObject json) {
        InstitucionPeriodoDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        InstitucionPeriodoDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * Id del periodo
     */
    private String idEnte;

    /**
     * Nombre del periodo
     */
    private String nombreEnte;

    /**
     * Numero de obligados
     */
    private Integer obligado;

    /**
     * Numero de obligados
     */
    private Integer cumplio;

    /**
     * Numero de extemporaneos
     */
    private Integer extemporaneo;

    /**
     * Numero de pendientes
     */
    private Integer pendiente;

    /**
     * Unidad responsable
     */
    private String ur;

    /**
     * Ramo
     */
    private String ramo;

    /**
     * Extension de periodo
     */
    private String extensionPeriodo;

    /**
     * Indica si hay vistas de omisos generadas para la institucion
     */
    private EnumEstatusVista vistaOmisosGenerada;

    /**
     * Indica si hay vistas de extemporaneos generadas para la institucion
     */
    private EnumEstatusVista vistaExtGenerada;

    public String getIdEnte() {
        return idEnte;
    }

    public void setIdEnte(String idEnte) {
        this.idEnte = idEnte;
    }

    public String getNombreEnte() {
        return nombreEnte;
    }

    public void setNombreEnte(String nombreEnte) {
        this.nombreEnte = nombreEnte;
    }

    public Integer getObligado() {
        return obligado;
    }

    public void setObligado(Integer obligado) {
        this.obligado = obligado;
    }

    public Integer getCumplio() {
        return cumplio;
    }

    public void setCumplio(Integer cumplio) {
        this.cumplio = cumplio;
    }

    public Integer getExtemporaneo() {
        return extemporaneo;
    }

    public void setExtemporaneo(Integer extemporaneo) {
        this.extemporaneo = extemporaneo;
    }

    public Integer getPendiente() {
        return pendiente;
    }

    public void setPendiente(Integer pendiente) {
        this.pendiente = pendiente;
    }

    public String getExtensionPeriodo() {
        return extensionPeriodo;
    }

    public void setExtensionPeriodo(String extensionPeriodo) {
        this.extensionPeriodo = extensionPeriodo;
    }

    public String getUr() {
        return ur;
    }

    public void setUr(String ur) {
        this.ur = ur;
    }

    public String getRamo() {
        return ramo;
    }

    public void setRamo(String ramo) {
        this.ramo = ramo;
    }

    public EnumEstatusVista getVistaOmisosGenerada() {
        return vistaOmisosGenerada;
    }

    public void setVistaOmisosGenerada(EnumEstatusVista vistaOmisosGenerada) {
        this.vistaOmisosGenerada = vistaOmisosGenerada;
    }

    public EnumEstatusVista getVistaExtGenerada() {
        return vistaExtGenerada;
    }

    public void setVistaExtGenerada(EnumEstatusVista vistaExtGenerada) {
        this.vistaExtGenerada = vistaExtGenerada;
    }

    @Override
    public String toString() {
        return "InstitucionPeriodoDTO{" +
                "idEnte='" + idEnte + '\'' +
                ", nombreEnte='" + nombreEnte + '\'' +
                ", obligado=" + obligado +
                ", cumplio=" + cumplio +
                ", extemporaneo=" + extemporaneo +
                ", pendiente=" + pendiente +
                ", ur='" + ur + '\'' +
                ", ramo='" + ramo + '\'' +
                ", extensionPeriodo='" + extensionPeriodo + '\'' +
                ", vistaOmisosGenerada=" + vistaOmisosGenerada +
                ", vistaExtGenerada=" + vistaExtGenerada +
                '}';
    }
}
