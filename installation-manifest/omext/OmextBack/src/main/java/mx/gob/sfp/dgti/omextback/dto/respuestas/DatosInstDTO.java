/**
 * @(#)DatosInstDTO.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumSituacion;

/**
 * DTO informacion de la institucion con respecto a sus servidores publicos y su cumplimiento
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DatosInstDTO {

    /**
     * Id del ente al que pertenece
     */
    private String idEnte;

    /**
     * Nombre del ente al que pertenece
     */
    private String nombreEnte;

    /**
     * Nombre corto del ente
     */
    private String nombreCorto;

    /**
     * Situacion del servidor
     */
    private EnumSituacion situacion;

    /**
     * Numero de servidores obligados a presentar la declaracion
     */
    private Integer obligado;

    /**
     * Numero de servidores pendientes con la declaracion
     */
    private Integer pendiente;

    /**
     * Numero de servidores que cumplieron con la declaracion
     */
    private Integer cumplio;

    /**
     * Numero de servidores que cumplieron con la declaracion de forma extemporanea
     */
    private Integer extemporaneo;

    /**
     * Porcentaje de cumplimiento en la institucion
     */
    private Float porcCumplimiento;

    /**
     * Ramo al que pertenece la insitucion
     */
    private Integer ramo;

    /**
     * Unidad responsable a la que pertenece la institucion
     */
    private String ur;

    /**
     * Constructor
     */
    public DatosInstDTO(){//Constructor
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DatosInstDTO(JsonObject json) {
        DatosInstDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DatosInstDTOConverter.toJson(this, json);
        return json;
    }

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

    public EnumSituacion getSituacion() {
        return situacion;
    }

    public void setSituacion(EnumSituacion situacion) {
        this.situacion = situacion;
    }

    public Integer getObligado() {
        return obligado;
    }

    public void setObligado(Integer obligado) {
        this.obligado = obligado;
    }

    public Integer getPendiente() {
        return pendiente;
    }

    public void setPendiente(Integer pendiente) {
        this.pendiente = pendiente;
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

    public Float getPorcCumplimiento() {
        return porcCumplimiento;
    }

    public void setPorcCumplimiento(Float porcCumplimiento) {
        this.porcCumplimiento = porcCumplimiento;
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

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    @Override
    public String toString() {
        return "DatosInstDTO{" +
                "idEnte='" + idEnte + '\'' +
                ", nombreEnte='" + nombreEnte + '\'' +
                ", nombreCorto='" + nombreCorto + '\'' +
                ", situacion=" + situacion +
                ", obligado=" + obligado +
                ", pendiente=" + pendiente +
                ", cumplio=" + cumplio +
                ", extemporaneo=" + extemporaneo +
                ", porcCumplimiento=" + porcCumplimiento +
                ", ramo=" + ramo +
                ", ur='" + ur + '\'' +
                '}';
    }
}
