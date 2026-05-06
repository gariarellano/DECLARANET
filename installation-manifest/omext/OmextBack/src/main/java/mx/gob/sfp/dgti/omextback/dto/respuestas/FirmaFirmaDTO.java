/**
 * @(#)FirmaFirmaDTO.java 03/03/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO para datos de la firma obtenidos del servicio
 *
 * @author pavel.martinez
 * @since 03/03/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class FirmaFirmaDTO {

    /**
     * Dato obtenido del servicio de firma
     */
    private String codigoSha;

    /**
     * Dato obtenido del servicio de firma
     */
    private String estatus;

    /**
     * Dato obtenido del servicio de firma
     */
    private String folio;

    /**
     * Dato obtenido del servicio de firma
     */
    private String nombreFirm;

    /**
     * Dato obtenido del servicio de firma
     */
    private String numeroCertificado;

    /**
     * Dato obtenido del servicio de firma
     */
    private String rfc;

    /**
     * Dato obtenido del servicio de firma
     */
    private String respuestaValidacion;

    /**
     * Dato obtenido del servicio de firma
     */
    private Integer transaccion;

    /**
     * Constructor
     */
    public FirmaFirmaDTO(){//Constructor
    }

    public FirmaFirmaDTO(String codigoSha, String estatus, String folio, String nombreFirm, String numeroCertificado,
                         String rfc, String respuestaValidacion, Integer transaccion) {
        this.codigoSha = codigoSha;
        this.estatus = estatus;
        this.folio = folio;
        this.nombreFirm = nombreFirm;
        this.numeroCertificado = numeroCertificado;
        this.rfc = rfc;
        this.respuestaValidacion = respuestaValidacion;
        this.transaccion = transaccion;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public FirmaFirmaDTO(JsonObject json) {
        FirmaFirmaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        FirmaFirmaDTOConverter.toJson(this, json);
        return json;
    }

    public String getCodigoSha() {
        return codigoSha;
    }

    public void setCodigoSha(String codigoSha) {
        this.codigoSha = codigoSha;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getNombreFirm() {
        return nombreFirm;
    }

    public void setNombreFirm(String nombreFirm) {
        this.nombreFirm = nombreFirm;
    }

    public String getNumeroCertificado() {
        return numeroCertificado;
    }

    public void setNumeroCertificado(String numeroCertificado) {
        this.numeroCertificado = numeroCertificado;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getRespuestaValidacion() {
        return respuestaValidacion;
    }

    public void setRespuestaValidacion(String respuestaValidacion) {
        this.respuestaValidacion = respuestaValidacion;
    }

    public Integer getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(Integer transaccion) {
        this.transaccion = transaccion;
    }

    @Override
    public String toString() {
        return "FirmaFirmaDTO{" +
                "codigoSha='" + codigoSha + '\'' +
                ", estatus='" + estatus + '\'' +
                ", folio='" + folio + '\'' +
                ", nombreFirm='" + nombreFirm + '\'' +
                ", numeroCertificado='" + numeroCertificado + '\'' +
                ", rfc='" + rfc + '\'' +
                ", respuestaValidacion='" + respuestaValidacion + '\'' +
                ", transaccion=" + transaccion +
                '}';
    }
}
