/**
 * @(#)RepresentacionDTO.java 10/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.RegistroBaseDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.LocalizacionDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.MontoMonedaDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.PersonaDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumParticipante;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoRepresentacion;

/**
 * DTO para el modulo de representacion
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 10/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class RepresentacionDTO extends RegistroBaseDTO {

    /**
     * Participante
     */
    private EnumParticipante participante;

    /**
     * Tipo de representacion
     */
    private EnumTipoRepresentacion tipoRepresentacion;

    /**
     * Fecha de inicio de la representacion
     */
    private String fechaInicioRepresentacion;

    /**
     * Representante o representado
     */
    private PersonaDTO representanteRepresentado;

    /**
     * Indica si recibe remuneracion
     */
    private boolean recibeRemuneracion;

    /**
     * El monto mensual
     */
    private MontoMonedaDTO montoMensual;

    /**
     * Localizacion
     */
    private LocalizacionDTO localizacion;

    /**
     * Sector
     */
    private CatalogoDTO sector;

    /**
     * Otro sector
     */
    private String sectorOtro;


    public RepresentacionDTO() {
    }

    public RepresentacionDTO(String id, String idPosicionVista, EnumTipoOperacion tipoOperacion,
                             EnumParticipante participante, EnumTipoRepresentacion tipoRepresentacion,
                             String fechaInicioRepresentacion, PersonaDTO representanteRepresentado,
                             boolean recibeRemuneracion, MontoMonedaDTO montoMensual, LocalizacionDTO localizacion,
                             CatalogoDTO sector, String sectorOtro) {
        super(id, idPosicionVista, tipoOperacion);
        this.participante = participante;
        this.tipoRepresentacion = tipoRepresentacion;
        this.fechaInicioRepresentacion = fechaInicioRepresentacion;
        this.representanteRepresentado = representanteRepresentado;
        this.recibeRemuneracion = recibeRemuneracion;
        this.montoMensual = montoMensual;
        this.localizacion = localizacion;
        this.sector = sector;
        this.sectorOtro= sectorOtro;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public RepresentacionDTO(JsonObject json) {
        RepresentacionDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RepresentacionDTOConverter.toJson(this, json);
        return json;
    }

    public EnumParticipante getParticipante() {
        return participante;
    }

    public void setParticipante(EnumParticipante participante) {
        this.participante = participante;
    }

    public EnumTipoRepresentacion getTipoRepresentacion() {
        return tipoRepresentacion;
    }

    public void setTipoRepresentacion(EnumTipoRepresentacion tipoRepresentacion) {
        this.tipoRepresentacion = tipoRepresentacion;
    }

    public String getFechaInicioRepresentacion() {
        return fechaInicioRepresentacion;
    }

    public void setFechaInicioRepresentacion(String fechaInicioRepresentacion) {
        this.fechaInicioRepresentacion = fechaInicioRepresentacion;
    }

    public PersonaDTO getRepresentanteRepresentado() {
        return representanteRepresentado;
    }

    public void setRepresentanteRepresentado(PersonaDTO representanteRepresentado) {
        this.representanteRepresentado = representanteRepresentado;
    }

    public boolean isRecibeRemuneracion() {
        return recibeRemuneracion;
    }

    public void setRecibeRemuneracion(boolean recibeRemuneracion) {
        this.recibeRemuneracion = recibeRemuneracion;
    }

    public MontoMonedaDTO getMontoMensual() {
        return montoMensual;
    }

    public void setMontoMensual(MontoMonedaDTO montoMensual) {
        this.montoMensual = montoMensual;
    }

    public LocalizacionDTO getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(LocalizacionDTO localizacion) {
        this.localizacion = localizacion;
    }

    public CatalogoDTO getSector() {
        return sector;
    }

    public void setSector(CatalogoDTO sector) {
        this.sector = sector;
    }

    public String getSectorOtro() {
        return sectorOtro;
    }

    public void setSectorOtro(String sectorOtro) {
        this.sectorOtro = sectorOtro;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RepresentacionDTO{");
        sb.append("participante=").append(participante);
        sb.append(", tipoRepresentacion=").append(tipoRepresentacion);
        sb.append(", fechaInicioRepresentacion='").append(fechaInicioRepresentacion).append('\'');
        sb.append(", representanteRepresentado=").append(representanteRepresentado);
        sb.append(", recibeRemuneracion=").append(recibeRemuneracion);
        sb.append(", montoMensual=").append(montoMensual);
        sb.append(", localizacion=").append(localizacion);
        sb.append(", sector=").append(sector);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
