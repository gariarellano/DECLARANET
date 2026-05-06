/**
 * @(#)PeriodoInputDTO.java 08/02/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.input;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.dto.respuestas.EnteExtensionDTO;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumMes;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumTipoIncumplimiento;

import java.util.List;

/**
 * DTO con la informacion del periodo a guardar
 *
 * @author pavel.martinez
 * @since 08/02/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class PeriodoInputDTO {

    public PeriodoInputDTO(){//Constructor
    }

    public PeriodoInputDTO(JsonObject json) {
        PeriodoInputDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        PeriodoInputDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * id del periodo
     */
    private String idPeriodo;

    /**
     * collName de la consulta
     */
    private Integer collName;

    /**
     * Instituciones del periodo *
     */
    private List<String> instituciones;

    /**
     * Lista de extensiones
     */
    private List<EnteExtensionDTO> extensiones;

    /**
     * Tipo de incumplimiento
     */
    private EnumTipoIncumplimiento tipoIncumplimiento;

    /**
     * Anio de la declaracion
     */
    private Integer anio;

    /**
     * Mes si aplica
     */
    private EnumMes mes;

    /**
     * Usuario creacion
     */
    private UsuarioInputDTO usuarioRegistro;

    /**
     * Tipo de declaracion
     */
    private EnumTipoDeclaracion tipoDeclaracion;

    /**
     * Fecha limite del periodo
     */
    private String fechaLimite;

    /**
     * Bandera que indica si de acuerdo a la fecha limite y a las extensiones se puede generar vista si se han vencido
     * dichas fechas, si es true indica que el dia actual pasa las fechas limites y por lo tanto se puede generar vista
     */
    private Boolean venceFechaLimite;

    public Integer getCollName() {
        return collName;
    }

    public void setCollName(Integer collName) {
        this.collName = collName;
    }

    public List<String> getInstituciones() {
        return instituciones;
    }

    public void setInstituciones(List<String> instituciones) {
        this.instituciones = instituciones;
    }

    public EnumTipoIncumplimiento getTipoIncumplimiento() {
        return tipoIncumplimiento;
    }

    public void setTipoIncumplimiento(EnumTipoIncumplimiento tipoIncumplimiento) {
        this.tipoIncumplimiento = tipoIncumplimiento;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public EnumMes getMes() {
        return mes;
    }

    public void setMes(EnumMes mes) {
        this.mes = mes;
    }

    public EnumTipoDeclaracion getTipoDeclaracion() {
        return tipoDeclaracion;
    }

    public void setTipoDeclaracion(EnumTipoDeclaracion tipoDeclaracion) {
        this.tipoDeclaracion = tipoDeclaracion;
    }

    public String getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(String idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public List<EnteExtensionDTO> getExtensiones() {
        return extensiones;
    }

    public void setExtensiones(List<EnteExtensionDTO> extensiones) {
        this.extensiones = extensiones;
    }

    public UsuarioInputDTO getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(UsuarioInputDTO usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    public Boolean getVenceFechaLimite() {
        return venceFechaLimite;
    }

    public void setVenceFechaLimite(Boolean venceFechaLimite) {
        this.venceFechaLimite = venceFechaLimite;
    }

    @Override
    public String toString() {
        return "PeriodoInputDTO{" +
                "idPeriodo=" + idPeriodo +
                ", collName=" + collName +
                ", instituciones=" + instituciones +
                ", extensiones=" + extensiones +
                ", tipoIncumplimiento=" + tipoIncumplimiento +
                ", anio=" + anio +
                ", mes=" + mes +
                ", usuarioRegistro='" + usuarioRegistro + '\'' +
                ", tipoDeclaracion=" + tipoDeclaracion +
                ", fechaLimite='" + fechaLimite + '\'' +
                ", venceFechaLimite=" + venceFechaLimite +
                '}';
    }
}
