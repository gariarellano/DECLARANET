/**
 * @(#)ServidorVistaDTO.java 25/01/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumMes;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumTipoIncumplimiento;

import java.time.Instant;

/**
 * DTO de un servidor en una vista
 *
 * @author pavel.martinez
 * @since 25/01/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ServidorVistaDTO {

    public ServidorVistaDTO() {//Constructor
    }

    public ServidorVistaDTO(JsonObject json) {
        ServidorVistaDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ServidorVistaDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * Nombre del servidor
     */
    private String nombres;

    /**
     * Primer apellido del servidor publico
     */
    private String primerApellido;

    /**
     * Segundo apellido del servidor
     */
    private String segundoApellido;

    /**
     * Topo de declaracion
     */
    private EnumTipoDeclaracion tipoDeclaracion;

    /**
     * Tipo de incumplimiento
     */
    private EnumTipoIncumplimiento tipoIncumplimiento;

    /**
     * Anio
     */
    private Integer anio;

    /**
     * Mes
     */
    private EnumMes mes;

    /**
     * Id del periodo
     */
    private String idPeriodo;

    /**
     *
     */
    private String idEnte;

    /**
     * Curp
     */
    private String curp;

    /**
     * Id del puesto
     */
    private String idPuesto;

    /**
     * Empleo, cargo o comision del servidor publico
     */
    private String empleoCargoComision;

    /**
     * Fecha del puesto
     */
    private String fechaTomaPosesionPuesto;

    /**
     * Id de la vista
     */
    private String folio;

    /**
     * Fecha limite
     */
    private String fechaLimite;

    /**
     * Fecha de creacion
     */
    private Instant fechaCreacion;


    /**
     * Unidad administrativa a la  que pertenece la institucion del servidor
     */
    private String unidadAdministrativa;

    /**
     * Clave de la unidad administrativa
     */
    private String claveUa;

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public EnumTipoDeclaracion getTipoDeclaracion() {
        return tipoDeclaracion;
    }

    public void setTipoDeclaracion(EnumTipoDeclaracion tipoDeclaracion) {
        this.tipoDeclaracion = tipoDeclaracion;
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

    public String getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(String idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getIdEnte() {
        return idEnte;
    }

    public void setIdEnte(String idEnte) {
        this.idEnte = idEnte;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(String idPuesto) {
        this.idPuesto = idPuesto;
    }

    public String getEmpleoCargoComision() {
        return empleoCargoComision;
    }

    public void setEmpleoCargoComision(String empleoCargoComision) {
        this.empleoCargoComision = empleoCargoComision;
    }

    public String getFechaTomaPosesionPuesto() {
        return fechaTomaPosesionPuesto;
    }

    public void setFechaTomaPosesionPuesto(String fechaTomaPosesionPuesto) {
        this.fechaTomaPosesionPuesto = fechaTomaPosesionPuesto;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(String unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public String getClaveUa() {
        return claveUa;
    }

    public void setClaveUa(String claveUa) {
        this.claveUa = claveUa;
    }

    @Override
    public String toString() {
        return "ServidorVistaDTO{" +
                "nombres='" + nombres + '\'' +
                ", primerApellido='" + primerApellido + '\'' +
                ", segundoApellido='" + segundoApellido + '\'' +
                ", tipoDeclaracion=" + tipoDeclaracion +
                ", tipoIncumplimiento=" + tipoIncumplimiento +
                ", anio=" + anio +
                ", mes=" + mes +
                ", idPeriodo='" + idPeriodo + '\'' +
                ", idEnte='" + idEnte + '\'' +
                ", curp='" + curp + '\'' +
                ", idPuesto='" + idPuesto + '\'' +
                ", empleoCargoComision='" + empleoCargoComision + '\'' +
                ", fechaTomaPosesionPuesto='" + fechaTomaPosesionPuesto + '\'' +
                ", folio='" + folio + '\'' +
                ", fechaLimite=" + fechaLimite +
                ", fechaCreacion=" + fechaCreacion +
                ", unidadAdministrativa='" + unidadAdministrativa + '\'' +
                ", claveUa='" + claveUa + '\'' +
                '}';
    }
}
