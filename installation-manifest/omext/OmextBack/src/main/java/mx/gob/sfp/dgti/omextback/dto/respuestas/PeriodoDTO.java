/**
 * @(#)PeriodoDTO.java 25/01/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumEstatusPeriodo;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumMes;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumTipoDeclaracion;

import java.util.HashMap;
import java.util.List;

/**
 * DTO de un periodo
 *
 * @author pavel.martinez
 * @since 25/01/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class PeriodoDTO {

    public PeriodoDTO(){//Constructor
    }

    public PeriodoDTO(JsonObject json) {
        PeriodoDTOConverter.fromJson(json, this);
        this.extensionesMapa = new HashMap<>();
        this.extensionesMapa.put("" , this.fechaLimite);
        this.getExtensiones().forEach(e -> {
            this.extensionesMapa.put(e.getIdEnte(), e.getFechaLimite());
        });
        System.out.println("LA EXTENSION EN MAPAS QUEDO + " + extensionesMapa);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        PeriodoDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * Id del periodo
     */
    private String idPeriodo;

    /**
     * Tipo de declaracion
     */
    private EnumTipoDeclaracion tipoDeclaracion;

    /**
     * Nombre del periodo
     */
    private String nombre;

    /**
     * Anio de la declaracion
     */
    private Integer anio;

    /**
     * Numero de servidores obligados
     */
    private EnumMes mes;

    /**
     * Fecha de registro
     */
    private String fechaRegistro;

    /**
     * Fecha de actualizacion
     */
    private String fechaActualizacion;

    /**
     * Id del periodo
     */
    private String fechaLimite;

    /**
     * usuario que actualiza
     */
    private String usuarioActualiza;

    /**
     * Usuario que creo
     */
    private String usuarioCreacion;

    /**
     * Bandera que indica si de acuerdo a la fecha limite y a las extensiones se puede generar vista si se han vencido
     * dichas fechas, si es true indica que el dia actual pasa las fechas limites y por lo tanto se puede generar vista
     * de toddo el grupo
     */
    private Boolean venceFechaLimite;

    /**
     * Indica si se ha generado las vistas de omisos posibles
     */
    private EnumEstatusPeriodo vistasGeneradas;

    /**
     * Extension
     */
    private List<EnteExtensionDTO> extensiones;

    /**
     * Mapa de extensiones
     */
    private HashMap<String, String> extensionesMapa;

    public HashMap<String, String> getExtensionesMapa() {
        return extensionesMapa;
    }

    public void setExtensionesMapa(HashMap<String, String> extensionesMapa) {
        this.extensionesMapa = extensionesMapa;
    }

    public String getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(String idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public EnumEstatusPeriodo getVistasGeneradas() {
        return vistasGeneradas;
    }

    public void setVistasGeneradas(EnumEstatusPeriodo vistasGeneradas) {
        this.vistasGeneradas = vistasGeneradas;
    }

    public EnumMes getMes() {
        return mes;
    }

    public void setMes(EnumMes mes) {
        this.mes = mes;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public EnumTipoDeclaracion getTipoDeclaracion() {
        return tipoDeclaracion;
    }

    public void setTipoDeclaracion(EnumTipoDeclaracion tipoDeclaracion) {
        this.tipoDeclaracion = tipoDeclaracion;
    }

    public List<EnteExtensionDTO> getExtensiones() {
        return extensiones;
    }

    public void setExtensiones(List<EnteExtensionDTO> extensiones) {
        this.extensiones = extensiones;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getUsuarioActualiza() {
        return usuarioActualiza;
    }

    public void setUsuarioActualiza(String usuarioActualiza) {
        this.usuarioActualiza = usuarioActualiza;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }


    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Boolean getVenceFechaLimite() {
        return venceFechaLimite;
    }

    public void setVenceFechaLimite(Boolean venceFechaLimite) {
        this.venceFechaLimite = venceFechaLimite;
    }

    @Override
    public String toString() {
        return "PeriodoDTO{" +
                "idPeriodo='" + idPeriodo + '\'' +
                ", tipoDeclaracion=" + tipoDeclaracion +
                ", nombre='" + nombre + '\'' +
                ", anio=" + anio +
                ", mes=" + mes +
                ", fechaRegistro='" + fechaRegistro + '\'' +
                ", fechaActualizacion='" + fechaActualizacion + '\'' +
                ", fechaLimite='" + fechaLimite + '\'' +
                ", usuarioActualiza='" + usuarioActualiza + '\'' +
                ", usuarioCreacion='" + usuarioCreacion + '\'' +
                ", venceFechaLimite=" + venceFechaLimite +
                ", vistasOmisosGeneradas=" + vistasGeneradas +
                ", extensiones=" + extensiones +
                '}';
    }
}
