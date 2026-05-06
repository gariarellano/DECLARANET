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
public class DatosGrupoDTO {

    /**
     * Grupo definido
     */
    private Integer grupo;

    /**
     * Nombre del grupo definido
     */
    private String nombreGrupo;

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
     * Constructor
     */
    public DatosGrupoDTO(){//Constructor
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DatosGrupoDTO(JsonObject json) {
        DatosGrupoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DatosGrupoDTOConverter.toJson(this, json);
        return json;
    }

    public Integer getGrupo() {
        return grupo;
    }

    public void setGrupo(Integer grupo) {
        this.grupo = grupo;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
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

    @Override
    public String toString() {
        return "DatosGrupoDTO{" +
                "grupo=" + grupo +
                ", nombreGrupo='" + nombreGrupo + '\'' +
                ", obligado=" + obligado +
                ", pendiente=" + pendiente +
                ", cumplio=" + cumplio +
                ", extemporaneo=" + extemporaneo +
                ", porcCumplimiento=" + porcCumplimiento +
                '}';
    }
}
