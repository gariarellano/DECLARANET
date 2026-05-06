/**
 * @(#)VistaPorInstitucionDTO.java 25/01/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.dto.input.UsuarioInputDTO;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumEstatusVista;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumMes;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumTipoIncumplimiento;

/**
 * DTO de una vista por institucion
 *
 * @author pavel.martinez
 * @since 25/01/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class VistaDTO {

    public VistaDTO() {//Constructor
    }

    public VistaDTO(JsonObject json) {
        VistaDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        VistaDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * Tipo de declaracion
     */
    private EnumTipoDeclaracion tipoDeclaracion;

    /**
     * Id de la vista
     */
    private String folio;

    /**
     * Tipo incumplimiento
     */
    private EnumTipoIncumplimiento tipoIncumplimiento;

    /**
     * Anio de la declaracion
     */
    private Integer anioDeclaracion;

    /**
     * Mes
     */
    private EnumMes mesDeclaracion;

    /**
     * Id del ente
     */

    private String idEnte;

    /**
     * Id del periodo al que pertenecen
     */
    private String idPeriodo;

    /**
     * Id texto oficio
     */
    private String idTextoOficio;

    /**
     * Total de omisos
     */
    private Integer totalServidores;

    /**
     * indica si la vista ha sido generada
     */
    private EnumEstatusVista vistaGenerada;

    /**
     * Fecha limite de la institucion
     */
    private String fechaLimite;

    /**
     * Fecha de creacion
     */
    private String fechaRegistro;

    /**
     * Información del ente
     */
    private EnteVistaDTO ente;

    /**
     * Informacion del firmante
     */
    private FirmanteDTO firmante;

    /**
     * Datos de firma
     */
    private FirmaDTO firmaOficio;

    /**
     * Datos de firma con toddo el listado
     */
    private FirmaDTO firmaListado;

    /**
     * Remitente de la generacion de la vista
     */
    private String remitente;

    /**
     * Anio en el que se genera la vista
     */
    private Integer anio;

    /**
     * Mes en el que se genera la vista, dos digitos
     */
    private String mes;

    /**
     * Numero de oficio
     */
    private Integer numOficio;

    /**
     * Datos del usuario que registra
     */
    private UsuarioInputDTO usuarioRegistro;


    public EnumTipoDeclaracion getTipoDeclaracion() {
        return tipoDeclaracion;
    }

    public void setTipoDeclaracion(EnumTipoDeclaracion tipoDeclaracion) {
        this.tipoDeclaracion = tipoDeclaracion;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public EnumTipoIncumplimiento getTipoIncumplimiento() {
        return tipoIncumplimiento;
    }

    public void setTipoIncumplimiento(EnumTipoIncumplimiento tipoIncumplimiento) {
        this.tipoIncumplimiento = tipoIncumplimiento;
    }

    public Integer getAnioDeclaracion() {
        return anioDeclaracion;
    }

    public void setAnioDeclaracion(Integer anioDeclaracion) {
        this.anioDeclaracion = anioDeclaracion;
    }

    public EnumMes getMesDeclaracion() {
        return mesDeclaracion;
    }

    public void setMesDeclaracion(EnumMes mesDeclaracion) {
        this.mesDeclaracion = mesDeclaracion;
    }

    public String getIdEnte() {
        return idEnte;
    }

    public void setIdEnte(String idEnte) {
        this.idEnte = idEnte;
    }

    public String getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(String idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getIdTextoOficio() {
        return idTextoOficio;
    }

    public void setIdTextoOficio(String idTextoOficio) {
        this.idTextoOficio = idTextoOficio;
    }

    public Integer getTotalServidores() {
        return totalServidores;
    }

    public void setTotalServidores(Integer totalServidores) {
        this.totalServidores = totalServidores;
    }

    public EnumEstatusVista getVistaGenerada() {
        return vistaGenerada;
    }

    public void setVistaGenerada(EnumEstatusVista vistaGenerada) {
        this.vistaGenerada = vistaGenerada;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public EnteVistaDTO getEnte() {
        return ente;
    }

    public void setEnte(EnteVistaDTO ente) {
        this.ente = ente;
    }

    public FirmanteDTO getFirmante() {
        return firmante;
    }

    public void setFirmante(FirmanteDTO firmante) {
        this.firmante = firmante;
    }

    public FirmaDTO getFirmaOficio() {
        return firmaOficio;
    }

    public void setFirmaOficio(FirmaDTO firmaOficio) {
        this.firmaOficio = firmaOficio;
    }

    public FirmaDTO getFirmaListado() {
        return firmaListado;
    }

    public void setFirmaListado(FirmaDTO firmaListado) {
        this.firmaListado = firmaListado;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Integer getNumOficio() {
        return numOficio;
    }

    public void setNumOficio(Integer numOficio) {
        this.numOficio = numOficio;
    }

    public UsuarioInputDTO getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(UsuarioInputDTO usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    @Override
    public String toString() {
        return "VistaDTO{" +
                "tipoDeclaracion=" + tipoDeclaracion +
                ", folio='" + folio + '\'' +
                ", tipoIncumplimiento=" + tipoIncumplimiento +
                ", anioDeclaracion=" + anioDeclaracion +
                ", mesDeclaracion=" + mesDeclaracion +
                ", idEnte='" + idEnte + '\'' +
                ", idPeriodo='" + idPeriodo + '\'' +
                ", idTextoOficio='" + idTextoOficio + '\'' +
                ", totalServidores=" + totalServidores +
                ", vistaGenerada=" + vistaGenerada +
                ", fechaLimite='" + fechaLimite + '\'' +
                ", fechaRegistro='" + fechaRegistro + '\'' +
                ", ente=" + ente +
                ", firmante=" + firmante +
                ", firmaOficio=" + firmaOficio +
                ", firmaListado=" + firmaListado +
                ", remitente='" + remitente + '\'' +
                ", anio=" + anio +
                ", mes='" + mes + '\'' +
                ", numOficio=" + numOficio +
                ", usuarioRegistro=" + usuarioRegistro +
                '}';
    }
}
