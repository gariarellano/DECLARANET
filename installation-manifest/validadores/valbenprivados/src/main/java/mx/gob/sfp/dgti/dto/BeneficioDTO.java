/**
 * @(#)BeneficioDTO.java 10/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.RegistroBaseDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.MontoMonedaDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.PersonaDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumFormaRecepcion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;

/**
 * DTO para el modulo de beneficio
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 10/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class BeneficioDTO extends RegistroBaseDTO {

    /**
     * Tipo de beneficio
     */
    private CatalogoDTO tipoBeneficio;

    /**
     * Otro tipo de beneficio
     */
    private String tipoBeneficioOtro;

    /**
     * Beneficiario
     */
    private CatalogoDTO beneficiario;

    /**
     * Otorgante
     */
    private PersonaDTO otorgante;

    /**
     * Forma de recepcion
     */
    private EnumFormaRecepcion formaRecepcion;

    /**
     * Especifique el beneficio
     */
    private String especifiqueBeneficio;

    /**
     * Monto mensual aproximado
     */
    private MontoMonedaDTO montoMensualAproximado;

    /**
     * Sector
     */
    private CatalogoDTO sector;

    /**
     * Otro sector
     */
    private String sectorOtro;


    public BeneficioDTO() {
    }

    public BeneficioDTO(String id, String idPosicionVista, EnumTipoOperacion tipoOperacion, boolean registroHistorico,
                        CatalogoDTO tipoBeneficio, CatalogoDTO beneficiario, PersonaDTO otorgante,
                        EnumFormaRecepcion formaRecepcion, String especifiqueBeneficio,
                        MontoMonedaDTO montoMensualAproximado, CatalogoDTO sector, String sectorOtro, String tipoBeneficioOtro) {
        super(id, idPosicionVista, tipoOperacion, registroHistorico);
        this.tipoBeneficio = tipoBeneficio;
        this.beneficiario = beneficiario;
        this.tipoBeneficioOtro = tipoBeneficioOtro;
        this.otorgante = otorgante;
        this.formaRecepcion = formaRecepcion;
        this.especifiqueBeneficio = especifiqueBeneficio;
        this.montoMensualAproximado = montoMensualAproximado;
        this.sector = sector;
        this.sectorOtro = sectorOtro;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public BeneficioDTO(JsonObject json) {
        BeneficioDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        BeneficioDTOConverter.toJson(this, json);
        return json;
    }

    public CatalogoDTO getTipoBeneficio() {
        return tipoBeneficio;
    }

    public void setTipoBeneficio(CatalogoDTO tipoBeneficio) {
        this.tipoBeneficio = tipoBeneficio;
    }

    public CatalogoDTO getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(CatalogoDTO beneficiario) {
        this.beneficiario = beneficiario;
    }

    public PersonaDTO getOtorgante() {
        return otorgante;
    }

    public void setOtorgante(PersonaDTO otorgante) {
        this.otorgante = otorgante;
    }

    public EnumFormaRecepcion getFormaRecepcion() {
        return formaRecepcion;
    }

    public void setFormaRecepcion(EnumFormaRecepcion formaRecepcion) {
        this.formaRecepcion = formaRecepcion;
    }

    public String getEspecifiqueBeneficio() {
        return especifiqueBeneficio;
    }

    public void setEspecifiqueBeneficio(String especifiqueBeneficio) {
        this.especifiqueBeneficio = especifiqueBeneficio;
    }

    public MontoMonedaDTO getMontoMensualAproximado() {
        return montoMensualAproximado;
    }

    public void setMontoMensualAproximado(MontoMonedaDTO montoMensualAproximado) {
        this.montoMensualAproximado = montoMensualAproximado;
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

    public String getTipoBeneficioOtro() {
        return tipoBeneficioOtro;
    }

    public void setTipoBeneficioOtro(String tipoBeneficioOtro) {
        this.tipoBeneficioOtro = tipoBeneficioOtro;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BeneficioDTO{");
        sb.append("tipoBeneficio=").append(tipoBeneficio);
        sb.append(", beneficiario=").append(beneficiario);
        sb.append(", otorgante=").append(otorgante);
        sb.append(", formaRecepcion=").append(formaRecepcion);
        sb.append(", especifiqueBeneficio='").append(especifiqueBeneficio).append('\'');
        sb.append(", montoMensualAproximado=").append(montoMensualAproximado);
        sb.append(", sector=").append(sector);
        sb.append(", sectorOtro='").append(sectorOtro).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
