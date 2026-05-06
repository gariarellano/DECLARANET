/**
 * @(#)DatosDeclaDTO.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumTipoDeclaracion;

/**
 * DTO para la informacion del servidor tomada de Declaranet
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DatosDeclaDTO {

    /**
     * Id Declaranet no localizado
     */
    private String idDNetNoLocalizado;

    /**
     * Id el ente
     */
    private String idEnte;

    /**
     * Id del nivel jerarquico
     */
    private Integer idNivelJerarquico;

    /**
     * Valor del nivel jerarquico
     */
    private String valorNivelJerarquico;

    /**
     * Area de adscripcion
     */
    private String areaAdscripcion;

    /**
     * Empleo, cargo o comision
     */
    private String empleoCargoComision;

    /**
     * Nivel de empleo, cargo o comision
     */
    private String nivelEmpleoCargoComision;

    /**
     * Id movimiento
     */
    private String idMovimiento;

    /**
     * Id recepcion web
     */
    private String idRecepcionWeb;

    /**
     * Numero de comprobante
     */
    private String noComprobante;

    /**
     * Fecha de recepcion
     */
    private String fechaRecepcion;

    /**
     * Anio
     */
    private Integer anio;

    /**
     * Tipo de declaracion
     */
    private EnumTipoDeclaracion tipoDeclaracion;

    /**
     * CURP
     */
    private String curp;

    /**
     * idUsrDecNet
     */
    private Integer idUsrDecNet;

    /**
     * Numero de declaracion
     */
    private String numeroDeclaracion;

    /**
     * idSP
     */
    private Integer idSp;

    /**
     * Objeto con la institucion receptora
     */
    private InstitucionReceptoraDTO institucionReceptora;

    /**
     * Constructor
     */
    public DatosDeclaDTO(){//Constructor
    }


    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DatosDeclaDTO(JsonObject json) {
        DatosDeclaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DatosDeclaDTOConverter.toJson(this, json);
        return json;
    }

    public String getIdDNetNoLocalizado() {
        return idDNetNoLocalizado;
    }

    public void setIdDNetNoLocalizado(String idDNetNoLocalizado) {
        this.idDNetNoLocalizado = idDNetNoLocalizado;
    }

    public String getIdEnte() {
        return idEnte;
    }

    public void setIdEnte(String idEnte) {
        this.idEnte = idEnte;
    }

    public Integer getIdNivelJerarquico() {
        return idNivelJerarquico;
    }

    public void setIdNivelJerarquico(Integer idNivelJerarquico) {
        this.idNivelJerarquico = idNivelJerarquico;
    }

    public String getValorNivelJerarquico() {
        return valorNivelJerarquico;
    }

    public void setValorNivelJerarquico(String valorNivelJerarquico) {
        this.valorNivelJerarquico = valorNivelJerarquico;
    }

    public String getAreaAdscripcion() {
        return areaAdscripcion;
    }

    public void setAreaAdscripcion(String areaAdscripcion) {
        this.areaAdscripcion = areaAdscripcion;
    }

    public String getEmpleoCargoComision() {
        return empleoCargoComision;
    }

    public void setEmpleoCargoComision(String empleoCargoComision) {
        this.empleoCargoComision = empleoCargoComision;
    }

    public String getNivelEmpleoCargoComision() {
        return nivelEmpleoCargoComision;
    }

    public void setNivelEmpleoCargoComision(String nivelEmpleoCargoComision) {
        this.nivelEmpleoCargoComision = nivelEmpleoCargoComision;
    }

    public String getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(String idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getIdRecepcionWeb() {
        return idRecepcionWeb;
    }

    public void setIdRecepcionWeb(String idRecepcionWeb) {
        this.idRecepcionWeb = idRecepcionWeb;
    }

    public String getNoComprobante() {
        return noComprobante;
    }

    public void setNoComprobante(String noComprobante) {
        this.noComprobante = noComprobante;
    }

    public String getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(String fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public EnumTipoDeclaracion getTipoDeclaracion() {
        return tipoDeclaracion;
    }

    public void setTipoDeclaracion(EnumTipoDeclaracion tipoDeclaracion) {
        this.tipoDeclaracion = tipoDeclaracion;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public Integer getIdUsrDecNet() {
        return idUsrDecNet;
    }

    public void setIdUsrDecNet(Integer idUsrDecNet) {
        this.idUsrDecNet = idUsrDecNet;
    }

    public String getNumeroDeclaracion() {
        return numeroDeclaracion;
    }

    public void setNumeroDeclaracion(String numeroDeclaracion) {
        this.numeroDeclaracion = numeroDeclaracion;
    }

    public Integer getIdSp() {
        return idSp;
    }

    public void setIdSp(Integer idSp) {
        this.idSp = idSp;
    }

    public InstitucionReceptoraDTO getInstitucionReceptora() {
        return institucionReceptora;
    }

    public void setInstitucionReceptora(InstitucionReceptoraDTO institucionReceptora) {
        this.institucionReceptora = institucionReceptora;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DatosDeclaDTO{");
        sb.append("idDNetNoLocalizado='").append(idDNetNoLocalizado).append('\'');
        sb.append(", idEnte='").append(idEnte).append('\'');
        sb.append(", idNivelJerarquico=").append(idNivelJerarquico);
        sb.append(", valorNivelJerarquico='").append(valorNivelJerarquico).append('\'');
        sb.append(", areaAdscripcion='").append(areaAdscripcion).append('\'');
        sb.append(", empleoCargoComision='").append(empleoCargoComision).append('\'');
        sb.append(", nivelEmpleoCargoComision='").append(nivelEmpleoCargoComision).append('\'');
        sb.append(", idMovimiento='").append(idMovimiento).append('\'');
        sb.append(", idRecepcionWeb='").append(idRecepcionWeb).append('\'');
        sb.append(", noComprobante='").append(noComprobante).append('\'');
        sb.append(", fechaRecepcion='").append(fechaRecepcion).append('\'');
        sb.append(", anio=").append(anio);
        sb.append(", tipoDeclaracion=").append(tipoDeclaracion);
        sb.append(", curp='").append(curp).append('\'');
        sb.append(", idUsrDecNet=").append(idUsrDecNet);
        sb.append(", numeroDeclaracion='").append(numeroDeclaracion).append('\'');
        sb.append(", idSp=").append(idSp);
        sb.append(", institucionReceptora=").append(institucionReceptora);
        sb.append('}');
        return sb.toString();
    }
}
