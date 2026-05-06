/**
 * @(#)DetalleAvisoDTO.java 30/01/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.base.ModuloBaseDTO;

/**
 * DTO para el modulo de detalle del aviso por cambio de dependencia
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 30/01/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DetalleAvisoDTO extends ModuloBaseDTO {

    /**
     * Datos del empleo que concluye
     */
    EmpleoConcluyeDTO empleoCargoComisionConcluye;

    /**
     * Datos del empleo que inicia
     */
    EmpleoIniciaDTO empleoCargoComisionInicia;

    /**
     * Constructor
     */
    public DetalleAvisoDTO() {
    }

    /**
     * Constructor
     *
     * @param encabezado
     * @param aclaracionesObservaciones
     * @param empleoCargoComisionConcluye
     * @param empleoCargoComisionInicia
     */
    public DetalleAvisoDTO(EncabezadoDTO encabezado, String aclaracionesObservaciones,
                           EmpleoConcluyeDTO empleoCargoComisionConcluye, EmpleoIniciaDTO empleoCargoComisionInicia) {
        super(encabezado, aclaracionesObservaciones);
        this.empleoCargoComisionConcluye = empleoCargoComisionConcluye;
        this.empleoCargoComisionInicia = empleoCargoComisionInicia;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DetalleAvisoDTO(JsonObject json) {
        DetalleAvisoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DetalleAvisoDTOConverter.toJson(this, json);
        return json;
    }

    public EmpleoConcluyeDTO getEmpleoCargoComisionConcluye() {
        return empleoCargoComisionConcluye;
    }

    public void setEmpleoCargoComisionConcluye(EmpleoConcluyeDTO empleoCargoComisionConcluye) {
        this.empleoCargoComisionConcluye = empleoCargoComisionConcluye;
    }

    public EmpleoIniciaDTO getEmpleoCargoComisionInicia() {
        return empleoCargoComisionInicia;
    }

    public void setEmpleoCargoComisionInicia(EmpleoIniciaDTO empleoCargoComisionInicia) {
        this.empleoCargoComisionInicia = empleoCargoComisionInicia;
    }

    @Override
    public String toString() {
        return "DetalleAvisoDTO{" +
                "empleoCargoComisionConcluye=" + empleoCargoComisionConcluye +
                ", empleoCargoComisionInicia=" + empleoCargoComisionInicia +
                "} " + super.toString();
    }
}
