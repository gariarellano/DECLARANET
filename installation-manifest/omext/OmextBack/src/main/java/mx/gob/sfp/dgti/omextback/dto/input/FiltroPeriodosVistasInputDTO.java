/**
 * @(#)FiltroServidoresVistaInputDTO.java 02/02/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.input;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * DTO de una institucion para la consulta por periodo
 *
 * @author pavel.martinez
 * @since 02/02/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class FiltroPeriodosVistasInputDTO {

    public FiltroPeriodosVistasInputDTO(){//Constructor
    }

    public FiltroPeriodosVistasInputDTO(CondicionesPeriodosVistasInputDTO condiciones) {
        this.condiciones = condiciones;
    }


    public FiltroPeriodosVistasInputDTO(JsonObject json) {
        FiltroPeriodosVistasInputDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        FiltroPeriodosVistasInputDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * Id del periodo
     */
    private CondicionesPeriodosVistasInputDTO condiciones;

    /**
     * Lista que indica el ordenamiento de la busqueda
     */
    private List<OrdenamientoInputDTO> ordenamiento;


    /**
     * Offset de la consulta
     */
    private Integer offset;

    /**
     * Tamanio de la consulta en caso de que se especifique
     */
    private Integer tamanio;

    /**
     * collName
     */
    private Integer collName;

    public List<OrdenamientoInputDTO> getOrdenamiento() {
        return ordenamiento;
    }

    public void setOrdenamiento(List<OrdenamientoInputDTO> ordenamiento) {
        this.ordenamiento = ordenamiento;
    }

    public Integer getCollName() {
        return collName;
    }

    public void setCollName(Integer collName) {
        this.collName = collName;
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

    public CondicionesPeriodosVistasInputDTO getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(CondicionesPeriodosVistasInputDTO condiciones) {
        this.condiciones = condiciones;
    }

    @Override
    public String toString() {
        return "FiltroPeriodosVistasInputDTO{" +
                "condiciones=" + condiciones +
                ", ordenamiento=" + ordenamiento +
                ", offset=" + offset +
                ", tamanio=" + tamanio +
                ", collName=" + collName +
                '}';
    }
}
