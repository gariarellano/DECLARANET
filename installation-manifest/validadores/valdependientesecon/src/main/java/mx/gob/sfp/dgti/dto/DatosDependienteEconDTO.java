/**
 * @(#)DatosParejaDTO.java 03/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.RegistroBaseDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.ActividadLaboralNoDeclDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.DatosPersonalesNoDeclDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;

/**
 * DTO para el modulo de datos de pareja
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 03/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DatosDependienteEconDTO extends RegistroBaseDTO {

    /**
     * Lista de datos de pareja
     */
    private DatosPersonalesNoDeclDTO datosPersonales;

    /**
     * Enum con la relacion del declarante
     */
    private CatalogoDTO parentescoRelacion;

    /**
     * Otro parentesco relacion
     */
    private String parentescoRelacionOtro;

    /**
     * Si se trata de un ciudadano extranjero
     */
    private CiudadanoExtranjeroDTO ciudadanoExtranjero;

    /**
     * Habita domicilio declarante
     */
    private Boolean habitaDomicilioDeclarante;

    /**
     * Domicilio del dependiente economico
     */
    private DomicilioDepEconDTO domicilioDependienteEconomico;

    /**
     * Boolean que indica si cuenta con una actividad laboral, true es que sí
     */
    private boolean ninguno;

    /**
     * Actividad laboral
     */
    private ActividadLaboralNoDeclDTO actividadLaboral;

    public DatosDependienteEconDTO() {
    }

    public DatosDependienteEconDTO(String id, String idPosicionVista, EnumTipoOperacion tipoOperacion,
                                   DatosPersonalesNoDeclDTO datosPersonales, CatalogoDTO parentescoRelacion,
                                   CiudadanoExtranjeroDTO ciudadanoExtranjero,
                                   Boolean habitaDomicilioDeclarante, DomicilioDepEconDTO domicilioDependienteEconomico,
                                   boolean ninguno, ActividadLaboralNoDeclDTO actividadLaboral,
                                   String parentescoRelacionOtro) {
        super(id, idPosicionVista, tipoOperacion);
        this.datosPersonales = datosPersonales;
        this.parentescoRelacion = parentescoRelacion;
        this.ciudadanoExtranjero = ciudadanoExtranjero;
        this.habitaDomicilioDeclarante = habitaDomicilioDeclarante;
        this.domicilioDependienteEconomico = domicilioDependienteEconomico;
        this.ninguno = ninguno;
        this.parentescoRelacionOtro = parentescoRelacionOtro;
        this.actividadLaboral = actividadLaboral;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DatosDependienteEconDTO(JsonObject json) {
        DatosDependienteEconDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DatosDependienteEconDTOConverter.toJson(this, json);
        return json;
    }

    public DatosPersonalesNoDeclDTO getDatosPersonales() {
        return datosPersonales;
    }

    public void setDatosPersonales(DatosPersonalesNoDeclDTO datosPersonales) {
        this.datosPersonales = datosPersonales;
    }

    public CatalogoDTO getParentescoRelacion() {
        return parentescoRelacion;
    }

    public void setParentescoRelacion(CatalogoDTO parentescoRelacion) {
        this.parentescoRelacion = parentescoRelacion;
    }

    public CiudadanoExtranjeroDTO getCiudadanoExtranjero() {
        return ciudadanoExtranjero;
    }

    public void setCiudadanoExtranjero(CiudadanoExtranjeroDTO ciudadanoExtranjero) {
        this.ciudadanoExtranjero = ciudadanoExtranjero;
    }

    public DomicilioDepEconDTO getDomicilioDependienteEconomico() {
        return domicilioDependienteEconomico;
    }

    public void setDomicilioDependienteEconomico(DomicilioDepEconDTO domicilioDependienteEconomico) {
        this.domicilioDependienteEconomico = domicilioDependienteEconomico;
    }

    public boolean isNinguno() {
        return ninguno;
    }

    public void setNinguno(boolean ninguno) {
        this.ninguno = ninguno;
    }

    public ActividadLaboralNoDeclDTO getActividadLaboral() {
        return actividadLaboral;
    }

    public void setActividadLaboral(ActividadLaboralNoDeclDTO actividadLaboral) {
        this.actividadLaboral = actividadLaboral;
    }

    public Boolean getHabitaDomicilioDeclarante() {
        return habitaDomicilioDeclarante;
    }

    public void setHabitaDomicilioDeclarante(Boolean habitaDomicilioDeclarante) {
        this.habitaDomicilioDeclarante = habitaDomicilioDeclarante;
    }

    public String getParentescoRelacionOtro() {
        return parentescoRelacionOtro;
    }

    public void setParentescoRelacionOtro(String parentescoRelacionOtro) {
        this.parentescoRelacionOtro = parentescoRelacionOtro;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DatosDependienteEconDTO{");
        sb.append("datosPersonales=").append(datosPersonales);
        sb.append(", parentescoRelacion=").append(parentescoRelacion);
        sb.append(", parentescoRelacionOtro='").append(parentescoRelacionOtro).append('\'');
        sb.append(", ciudadanoExtranjero=").append(ciudadanoExtranjero);
        sb.append(", habitaDomicilioDeclarante=").append(habitaDomicilioDeclarante);
        sb.append(", domicilioDependienteEconomico=").append(domicilioDependienteEconomico);
        sb.append(", ninguno=").append(ninguno);
        sb.append(", actividadLaboral=").append(actividadLaboral);
        sb.append('}');
        return sb.toString();
    }
}
