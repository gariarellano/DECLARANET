/**
 * @(#)ApoyoDTO.java 08/11/2019
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
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumFormaRecepcion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumNivelGobierno;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;

/**
 * DTO para el modulo apoyo
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 08/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ApoyoDTO extends RegistroBaseDTO {

    /**
     * Beneficiario de algun programa publico
     */
    private CatalogoDTO beneficiarioPrograma;

    /**
     * Nombre del programa
     */
    private String nombrePrograma;

    /**
     * Institucion que otorga el apoyo
     */
    private String institucionOtorgante;

    /**
     * Nivel u orden de gobierno
     */
    private EnumNivelGobierno nivelOrdenGobierno;

    /**
     * Tipo de apoyo
     */
    private CatalogoDTO tipoApoyo;

    /**
     * Otro tipo de apoyo
     */
    private String tipoApoyoOtro;

    /**
     * Forma de recepcion del apoyo
     */
    private EnumFormaRecepcion formaRecepcion;

    /**
     * Monto aproximado del apoyo
     */
    private MontoMonedaDTO montoApoyoMensual;

    /**
     * Especifique el apoyo
     */
    private String especifiqueApoyo;

    public ApoyoDTO() {
    }

    public ApoyoDTO(String id, String idPosicionVista, EnumTipoOperacion tipoOperacion, boolean registroHistorico,
                    CatalogoDTO beneficiarioPrograma, String nombrePrograma, String institucionOtorgante,
                    EnumNivelGobierno nivelOrdenGobierno, CatalogoDTO tipoApoyo, EnumFormaRecepcion formaRecepcion,
                    MontoMonedaDTO montoApoyoMensual, String especifiqueApoyo, String tipoApoyoOtro) {
        super(id, idPosicionVista, tipoOperacion, registroHistorico);
        this.beneficiarioPrograma = beneficiarioPrograma;
        this.nombrePrograma = nombrePrograma;
        this.institucionOtorgante = institucionOtorgante;
        this.nivelOrdenGobierno = nivelOrdenGobierno;
        this.tipoApoyo = tipoApoyo;
        this.formaRecepcion = formaRecepcion;
        this.montoApoyoMensual = montoApoyoMensual;
        this.especifiqueApoyo = especifiqueApoyo;
        this.tipoApoyoOtro = tipoApoyoOtro;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public ApoyoDTO(JsonObject json) {
        ApoyoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ApoyoDTOConverter.toJson(this, json);
        return json;
    }

    public CatalogoDTO getBeneficiarioPrograma() {
        return beneficiarioPrograma;
    }

    public void setBeneficiarioPrograma(CatalogoDTO beneficiarioPrograma) {
        this.beneficiarioPrograma = beneficiarioPrograma;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    public String getInstitucionOtorgante() {
        return institucionOtorgante;
    }

    public void setInstitucionOtorgante(String institucionOtorgante) {
        this.institucionOtorgante = institucionOtorgante;
    }

    public EnumNivelGobierno getNivelOrdenGobierno() {
        return nivelOrdenGobierno;
    }

    public void setNivelOrdenGobierno(EnumNivelGobierno nivelOrdenGobierno) {
        this.nivelOrdenGobierno = nivelOrdenGobierno;
    }

    public CatalogoDTO getTipoApoyo() {
        return tipoApoyo;
    }

    public void setTipoApoyo(CatalogoDTO tipoApoyo) {
        this.tipoApoyo = tipoApoyo;
    }

    public EnumFormaRecepcion getFormaRecepcion() {
        return formaRecepcion;
    }

    public void setFormaRecepcion(EnumFormaRecepcion formaRecepcion) {
        this.formaRecepcion = formaRecepcion;
    }

    public MontoMonedaDTO getMontoApoyoMensual() {
        return montoApoyoMensual;
    }

    public void setMontoApoyoMensual(MontoMonedaDTO montoApoyoMensual) {
        this.montoApoyoMensual = montoApoyoMensual;
    }

    public String getEspecifiqueApoyo() {
        return especifiqueApoyo;
    }

    public void setEspecifiqueApoyo(String especifiqueApoyo) {
        this.especifiqueApoyo = especifiqueApoyo;
    }

    public String getTipoApoyoOtro() {
        return tipoApoyoOtro;
    }

    public void setTipoApoyoOtro(String tipoApoyoOtro) {
        this.tipoApoyoOtro = tipoApoyoOtro;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ApoyoDTO{");
        sb.append("beneficiarioPrograma=").append(beneficiarioPrograma);
        sb.append(", nombrePrograma='").append(nombrePrograma).append('\'');
        sb.append(", institucionOtorgante='").append(institucionOtorgante).append('\'');
        sb.append(", nivelOrdenGobierno=").append(nivelOrdenGobierno);
        sb.append(", tipoApoyo=").append(tipoApoyo);
        sb.append(", tipoApoyoOtro='").append(tipoApoyoOtro).append('\'');
        sb.append(", formaRecepcion=").append(formaRecepcion);
        sb.append(", montoApoyoMensual=").append(montoApoyoMensual);
        sb.append(", especifiqueApoyo='").append(especifiqueApoyo).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
