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

/**
 * DTO informacion de la institucion con respecto a sus servidores publicos y su cumplimiento
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DatosUADTO {

    /**
     * Id del ente al que pertenece
     */
    private String claveUa;

    /**
     * Nombre del ente al que pertenece
     */
    private String unidadAdministrativa;

    /**
     * Id del ente al que pertenece
     */
    private String idEnte;

    /**
     * Nombre del ente al que pertenece
     */
    private String nombreEnte;

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
     * Unidad responsable a la que pertenece la institucion
     */
    private String ur;

    /**
     * Constructor
     */
    public DatosUADTO(){//Constructor
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DatosUADTO(JsonObject json) {
        DatosUADTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DatosUADTOConverter.toJson(this, json);
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

    public String getClaveUa() {
        return claveUa;
    }

    public void setClaveUa(String claveUa) {
        this.claveUa = claveUa;
    }

    public String getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(String unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public String getUr() {
        return ur;
    }

    public void setUr(String ur) {
        this.ur = ur;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DatosUADTO{");
        sb.append("claveUa='").append(claveUa).append('\'');
        sb.append(", unidadAdministrativa='").append(unidadAdministrativa).append('\'');
        sb.append(", idEnte='").append(idEnte).append('\'');
        sb.append(", nombreEnte='").append(nombreEnte).append('\'');
        sb.append(", obligado=").append(obligado);
        sb.append(", pendiente=").append(pendiente);
        sb.append(", cumplio=").append(cumplio);
        sb.append(", extemporaneo=").append(extemporaneo);
        sb.append(", porcCumplimiento=").append(porcCumplimiento);
        sb.append(", ur='").append(ur).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
