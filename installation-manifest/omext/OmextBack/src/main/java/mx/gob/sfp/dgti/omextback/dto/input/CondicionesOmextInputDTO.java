/**
 * @(#)CondicionesOmextInputDTO.java 17/02/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.input;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumMes;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumSindicalizados;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumTipoDeclaracion;

import java.util.List;

/**
 * DTO para las condiciones generales para consultas sobre servidores en noLocalizados o cumplimiento
 *
 * @author pavel.martinez
 *
 * Se refactorizo para simplificar la logica
 *
 * @since 17/02/2021
 * @since 19/05/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class CondicionesOmextInputDTO {

    /**
     * Id del ente
     */
    private List<String> idEnte;

    /**
     * Nombres y apellidos o curp
     */
    private String nombresApellidos;

    /**
     * Nombre del ente
     */
    private String nombreEnte;

    /**
     * Clave del la unidad administrativa en caso de que la ur sea 0
     */
    private String claveUa;

    /**
     * Tipo de declaracion a consultar
     */
    private EnumTipoDeclaracion tipoDeclaracion;

    /**
     * Ramo de la institucion
     */
    private Integer ramo;

    /**
     * Unidad responsable de la institucion
     */
    private String ur;

    /**
     * Anio del cumplimiento
     */
    private Integer anio;

    /**
     * Mes del cumplimiento
     */
    private List<EnumMes> mes;

    /**
     * Ids con el nivel jerarquico
     */
    private List<Integer> idNivelJerarquico;

    /**
     * Indica si es sindicalizado
     */
    private EnumSindicalizados sindicalizado;

    /**
     * Indica si el servidor ya está en vista
     */
    private Boolean enVista;

    /**
     * En caso de que pertenezca a una vista, se indica el id del periodo al que pertenece
     */
    private String idPeriodo;

    /**
     * Constructor
     */
    public CondicionesOmextInputDTO() {//Constructor
    }


    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public CondicionesOmextInputDTO(JsonObject json) {
        CondicionesOmextInputDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        CondicionesOmextInputDTOConverter.toJson(this, json);
        return json;
    }



    public EnumTipoDeclaracion getTipoDeclaracion() {
        return tipoDeclaracion;
    }

    public void setTipoDeclaracion(EnumTipoDeclaracion tipoDeclaracion) {
        this.tipoDeclaracion = tipoDeclaracion;
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

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public List<EnumMes> getMes() {
        return mes;
    }

    public void setMes(List<EnumMes> mes) {
        this.mes = mes;
    }

    public List<Integer> getIdNivelJerarquico() {
        return idNivelJerarquico;
    }

    public void setIdNivelJerarquico(List<Integer> idNivelJerarquico) {
        this.idNivelJerarquico = idNivelJerarquico;
    }

    public EnumSindicalizados getSindicalizado() {
        return sindicalizado;
    }

    public void setSindicalizado(EnumSindicalizados sindicalizado) {
        this.sindicalizado = sindicalizado;
    }

    public List<String> getIdEnte() {
        return idEnte;
    }

    public void setIdEnte(List<String> idEnte) {
        this.idEnte = idEnte;
    }

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public String getNombreEnte() {
        return nombreEnte;
    }

    public void setNombreEnte(String nombreEnte) {
        this.nombreEnte = nombreEnte;
    }

    public String getClaveUa() {
        return claveUa;
    }

    public void setClaveUa(String claveUa) {
        this.claveUa = claveUa;
    }

    public boolean isEnVista() {
        return enVista;
    }

    public void setEnVista(boolean enVista) {
        this.enVista = enVista;
    }

    public String getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(String idPeriodo) {
        this.idPeriodo = idPeriodo;
    }


    public Boolean getEnVista() {
        return enVista;
    }

    public void setEnVista(Boolean enVista) {
        this.enVista = enVista;
    }

    @Override
    public String toString() {
        return "CondicionesOmextInputDTO{" +
                "idEnte=" + idEnte +
                ", nombresApellidos='" + nombresApellidos + '\'' +
                ", nombreEnte='" + nombreEnte + '\'' +
                ", claveUa='" + claveUa + '\'' +
                ", tipoDeclaracion=" + tipoDeclaracion +
                ", ramo=" + ramo +
                ", ur='" + ur + '\'' +
                ", anio=" + anio +
                ", mes=" + mes +
                ", idNivelJerarquico=" + idNivelJerarquico +
                ", sindicalizado=" + sindicalizado +
                ", enVista=" + enVista +
                ", idPeriodo='" + idPeriodo + '\'' +
                '}';
    }
}
