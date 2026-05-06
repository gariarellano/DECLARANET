/**
 * @(#)CondicionesVistaInputDTO.java 02/02/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.input;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumMes;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumTipoIncumplimiento;

/**
 * DTO de las condiciones de la vista
 *
 * @author pavel.martinez
 * @since 02/02/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class CondicionesPeriodosVistasInputDTO {

    public CondicionesPeriodosVistasInputDTO(){//Constructor
    }

    public CondicionesPeriodosVistasInputDTO(JsonObject json) {
        CondicionesPeriodosVistasInputDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        CondicionesPeriodosVistasInputDTOConverter.toJson(this, json);
        return json;
    }


    /**
     * Enum del incumplimiento
     */
    private EnumTipoIncumplimiento tipoIncumplimiento;

    /**
     * Enum tipo de declaracion
     */
    private EnumTipoDeclaracion tipoDeclaracion;

    /**
     * Anio de la declaracion
     */
    private Integer anio;

    /**
     * Mes de la declaracion
     */
    private EnumMes mes;

    /**
     * Periodo
     */
    private String idPeriodo;

    /**
     * Id del ente
     */
    private String idEnte;

    /**
     * Folio vista
     */
    private String folio;

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

    public EnumTipoDeclaracion getTipoDeclaracion() {
        return tipoDeclaracion;
    }

    public void setTipoDeclaracion(EnumTipoDeclaracion tipoDeclaracion) {
        this.tipoDeclaracion = tipoDeclaracion;
    }

    public String getIdEnte() {
        return idEnte;
    }

    public void setIdEnte(String idEnte) {
        this.idEnte = idEnte;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    @Override
    public String toString() {
        return "CondicionesPeriodosVistasInputDTO{" +
                "tipoIncumplimiento=" + tipoIncumplimiento +
                ", tipoDeclaracion=" + tipoDeclaracion +
                ", anio=" + anio +
                ", mes=" + mes +
                ", idPeriodo='" + idPeriodo + '\'' +
                ", idEnte='" + idEnte + '\'' +
                ", folio='" + folio + '\'' +
                '}';
    }
}
