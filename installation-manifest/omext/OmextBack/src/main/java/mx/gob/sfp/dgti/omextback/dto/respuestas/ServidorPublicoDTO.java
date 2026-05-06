/**
 * @(#)ServidorPublicoDTO.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumCumplimiento;

/**
 * DTO para los datos del servidor publico, se toma de datos generados de RUSP y Declaranet
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ServidorPublicoDTO {

    /**
     * Datos del servidor provenientes de RUSP
     */
    private DatosRuspDTO datosRusp;

    /**
     * Datos del servidor provenientes de Declaranet
     */
    private DatosDeclaDTO datosDecla;

    /**
     * Indica si el servidor público se encuentra activo
     */
    private Integer activo;

    /**
     * Se indica la fecha de registro
     */
    private String fechaRegistro;

    /**
     * Indica si es extemporaneo
     */
    private Boolean extemporaneo;

    /**
     * Score busqueda
     */
    private Double scoreBusq;

    /**
     * Indica el estado del cumplimiento de declaracion
     */
    private EnumCumplimiento cumplimiento;

    /**
     * Constructor
     */
    public ServidorPublicoDTO(){//Constructor
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public ServidorPublicoDTO(JsonObject json) {
        ServidorPublicoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ServidorPublicoDTOConverter.toJson(this, json);
        return json;
    }

    public DatosRuspDTO getDatosRusp() {
        return datosRusp;
    }

    public void setDatosRusp(DatosRuspDTO datosRusp) {
        this.datosRusp = datosRusp;
    }

    public DatosDeclaDTO getDatosDecla() {
        return datosDecla;
    }

    public void setDatosDecla(DatosDeclaDTO datosDecla) {
        this.datosDecla = datosDecla;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public EnumCumplimiento getCumplimiento() {
        return cumplimiento;
    }

    public void setCumplimiento(EnumCumplimiento cumplimiento) {
        this.cumplimiento = cumplimiento;
    }

    public Double getScoreBusq() {
        return scoreBusq;
    }

    public void setScoreBusq(Double scoreBusq) {
        this.scoreBusq = scoreBusq;
    }

    public Boolean getExtemporaneo() {
        return extemporaneo;
    }

    public void setExtemporaneo(Boolean extemporaneo) {
        this.extemporaneo = extemporaneo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ServidorPublicoDTO{");
        sb.append("datosRusp=").append(datosRusp);
        sb.append(", datosDecla=").append(datosDecla);
        sb.append(", activo=").append(activo);
        sb.append(", fechaRegistro='").append(fechaRegistro).append('\'');
        sb.append(", extemporaneo='").append(extemporaneo).append('\'');
        sb.append(", scoreBusq=").append(scoreBusq);
        sb.append(", cumplimiento=").append(cumplimiento);
        sb.append('}');
        return sb.toString();
    }
}
