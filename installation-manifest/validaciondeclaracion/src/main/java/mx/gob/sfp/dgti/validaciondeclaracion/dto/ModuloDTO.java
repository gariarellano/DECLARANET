/**
 * @(#)ModuloDTO.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoUnoFkDTO;

import java.util.List;

/**
 * Clase que contiene el DTO para LlamadoDTO
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ModuloDTO {

    /**
     * Nombre del modulo
     */
    private String modulo;

    /**
     * Lista de errores u observaciones encontradas en el modulo
     */
    private List<ErrorDTO> errores;

    /**
     * Bandera para verificar si el módulo está completo
     */
    private boolean incompleto;

    /**
     * Bandera
     */
    private CatalogoUnoFkDTO niverJerarquico;

    /**
     * Constructor
     */
    public ModuloDTO() {
        //Constructor
    }

    public ModuloDTO(String modulo) {
        this.modulo = modulo;
    }

    public ModuloDTO(String modulo, List<ErrorDTO> errores) {
        this.modulo = modulo;
        this.errores = errores;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public ModuloDTO(JsonObject json) {
        ModuloDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ModuloDTOConverter.toJson(this, json);
        return json;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public List<ErrorDTO> getErrores() {
        return errores;
    }

    public void setErrores(List<ErrorDTO> errores) {
        this.errores = errores;
    }

    public boolean isIncompleto() {
        return incompleto;
    }

    public void setIncompleto(boolean incompleto) {
        this.incompleto = incompleto;
    }

    public CatalogoUnoFkDTO getNiverJerarquico() {
        return niverJerarquico;
    }

    public void setNiverJerarquico(CatalogoUnoFkDTO niverJerarquico) {
        this.niverJerarquico = niverJerarquico;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ModuloDTO{");
        sb.append("modulo='").append(modulo).append('\'');
        sb.append(", errores=").append(errores);
        sb.append(", incompleto=").append(incompleto);
        sb.append(", nivelJerarquico=").append(niverJerarquico);
        sb.append('}');
        return sb.toString();
    }
}
