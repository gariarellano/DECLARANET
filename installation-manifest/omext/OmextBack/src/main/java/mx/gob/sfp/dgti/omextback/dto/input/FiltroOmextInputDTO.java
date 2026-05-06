/**
 * @(#)FiltroInputDTO.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.input;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumCumplimiento;

import java.util.List;

/**
 * DTO para el filtro de la busqueda de servidores publicos
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class FiltroOmextInputDTO {

    /**
     * Indica el offsert a partir del cual se traen los resultados
     */
    private Integer offset;

    /**
     * Tamanio de las paginas del resultado
     */
    private Integer tamanio;

    /**
     * Condiciones a cumplir en la consulta
     */
    private CondicionesOmextInputDTO condiciones;

    /**
     * CollName en el que se buscara
     */
    private Integer collName;

    /**
     * Lista que indica el ordenamiento de la busqueda
     */
    private List<OrdenamientoInputDTO> ordenamiento;

    /**
     * Incica el cumplimiento de la busqueda
     */
    private EnumCumplimiento cumplimiento;

    /**
     * Nombre del reporte para el caso de reportes
     */
    private String nombreReporte;

    /**
     * Constructor
     */
    public FiltroOmextInputDTO(){//Constructor
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public FiltroOmextInputDTO(JsonObject json) {
        FiltroOmextInputDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        FiltroOmextInputDTOConverter.toJson(this, json);
        return json;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getTamanio() {
        return tamanio;
    }

    public void setTamanio(Integer tamanio) {
        this.tamanio = tamanio;
    }

    public CondicionesOmextInputDTO getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(CondicionesOmextInputDTO condiciones) {
        this.condiciones = condiciones;
    }

    public List<OrdenamientoInputDTO> getOrdenamiento() {
        return ordenamiento;
    }

    public void setOrdenamiento(List<OrdenamientoInputDTO> ordenamiento) {
        this.ordenamiento = ordenamiento;
    }

    public EnumCumplimiento getCumplimiento() {
        return cumplimiento;
    }

    public void setCumplimiento(EnumCumplimiento cumplimiento) {
        this.cumplimiento = cumplimiento;
    }

    public Integer getCollName() {
        return collName;
    }

    public void setCollName(Integer collName) {
        this.collName = collName;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    @Override
    public String toString() {
        return "FiltroOmextInputDTO{" +
                "offset=" + offset +
                ", tamanio=" + tamanio +
                ", condiciones=" + condiciones +
                ", collName=" + collName +
                ", ordenamiento=" + ordenamiento +
                ", cumplimiento=" + cumplimiento +
                ", nombreReporte='" + nombreReporte + '\'' +
                '}';
    }
}
