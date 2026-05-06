/**
 * @(#)RespuestaServicioUsuarioFirmaDTO.java 23/02/2021
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO para la respuesta del servicio relacionado a la firma
 *
 * @author pavel.martinez
 * @since 23/02/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class RespuestaServicioFirmaAutomatizadaDTO {

    /**
     *
     */
    private Integer estatus;

    /**
     *
     */
    private Integer transaccion;

    /**
     *
     */
    private String rfc;

    /**
     *
     */
    private String numeroCertificado;

    /**
     *
     */
    private String nombreFirm;

    /**
     *
     */
    private String folio;

    /**
     *
     */
    private String respuestaValidacion;

    /**
     *
     */
    private String codigoSha;

    /**
     * Constructor
     */
    public RespuestaServicioFirmaAutomatizadaDTO(){//Constructor
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public RespuestaServicioFirmaAutomatizadaDTO(JsonObject json) {
        RespuestaServicioFirmaAutomatizadaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RespuestaServicioFirmaAutomatizadaDTOConverter.toJson(this, json);
        return json;
    }

    public Integer getEstatus() {
        return estatus;
    }

    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }

    public Integer getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(Integer transaccion) {
        this.transaccion = transaccion;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getNumeroCertificado() {
        return numeroCertificado;
    }

    public void setNumeroCertificado(String numeroCertificado) {
        this.numeroCertificado = numeroCertificado;
    }

    public String getNombreFirm() {
        return nombreFirm;
    }

    public void setNombreFirm(String nombreFirm) {
        this.nombreFirm = nombreFirm;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getRespuestaValidacion() {
        return respuestaValidacion;
    }

    public void setRespuestaValidacion(String respuestaValidacion) {
        this.respuestaValidacion = respuestaValidacion;
    }

    public String getCodigoSha() {
        return codigoSha;
    }

    public void setCodigoSha(String codigoSha) {
        this.codigoSha = codigoSha;
    }

    @Override
    public String toString() {
        return "RespuestaServicioFirmaAutomatizadaDTO{" +
                "estatus=" + estatus +
                ", transaccion=" + transaccion +
                ", rfc='" + rfc + '\'' +
                ", numeroCertificado='" + numeroCertificado + '\'' +
                ", nombreFirm='" + nombreFirm + '\'' +
                ", folio='" + folio + '\'' +
                ", respuestaValidacion='" + respuestaValidacion + '\'' +
                ", codigoSha='" + codigoSha + '\'' +
                '}';
    }
}
