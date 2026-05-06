/**
 * @(#)AdeudoDTO.java 07/11/2019
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
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoUnoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.MontoMonedaDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.PersonaDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;

import java.util.List;

/**
 * DTO para el modulo de adeudos y pasivos
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 07/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class AdeudoDTO extends RegistroBaseDTO {

    /**
     * Tipo de titular
     */
    private CatalogoUnoDTO titular;

    /**
     * Tipo de adeudo
     */
    private CatalogoDTO tipoAdeudo;

    /**
     * Otro tipo de adeudo
     */
    private String tipoAdeudoOtro;

    /**
     * Numero de contrato, cuenta o cualquier dato que permita su identificacion
     */
    private String numeroCuentaContrato;

    /**
     * Fecha de adquisicion
     */
    private String fechaAdquisicion;

    /**
     * Cantidad sin comas
     */
    private MontoMonedaDTO montoOriginal;

    /**
     * Cantidades sin comas, sin puntos, sin centavos y sin ceros a la izquierda
     */
    private Integer saldoInsoluto;

    /**
     * En caso de ser codeudor con un tercero
     */
    private List<PersonaDTO> terceros;

    /**
     * Otorgante
     */
    private PersonaDTO otorganteCredito;

    /**
     * Pais en el donde se localiza el adeudo
     */
    private CatalogoDTO paisAdeudo;


    public AdeudoDTO() {
    }

    public AdeudoDTO(String id, String idPosicionVista, EnumTipoOperacion tipoOperacion, boolean registroHistorico,
                     boolean verificar, CatalogoUnoDTO titular, CatalogoDTO tipoAdeudo, String tipoAdeudoOtro,
                     String numeroCuentaContrato, String fechaAdquisicion, MontoMonedaDTO montoOriginal,
                     Integer saldoInsoluto, List<PersonaDTO> terceros, PersonaDTO otorganteCredito,
                     CatalogoDTO paisAdeudo) {
        super(id, idPosicionVista, tipoOperacion, registroHistorico, verificar);
        this.titular = titular;
        this.tipoAdeudo = tipoAdeudo;
        this.tipoAdeudoOtro = tipoAdeudoOtro;
        this.numeroCuentaContrato = numeroCuentaContrato;
        this.fechaAdquisicion = fechaAdquisicion;
        this.montoOriginal = montoOriginal;
        this.saldoInsoluto = saldoInsoluto;
        this.terceros = terceros;
        this.otorganteCredito = otorganteCredito;
        this.paisAdeudo = paisAdeudo;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public AdeudoDTO(JsonObject json) {
        AdeudoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        AdeudoDTOConverter.toJson(this, json);
        return json;
    }

    public CatalogoUnoDTO getTitular() {
        return titular;
    }

    public void setTitular(CatalogoUnoDTO titular) {
        this.titular = titular;
    }

    public CatalogoDTO getTipoAdeudo() {
        return tipoAdeudo;
    }

    public void setTipoAdeudo(CatalogoDTO tipoAdeudo) {
        this.tipoAdeudo = tipoAdeudo;
    }

    public String getNumeroCuentaContrato() {
        return numeroCuentaContrato;
    }

    public void setNumeroCuentaContrato(String numeroCuentaContrato) {
        this.numeroCuentaContrato = numeroCuentaContrato;
    }

    public String getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(String fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public MontoMonedaDTO getMontoOriginal() {
        return montoOriginal;
    }

    public void setMontoOriginal(MontoMonedaDTO montoOriginal) {
        this.montoOriginal = montoOriginal;
    }

    public Integer getSaldoInsoluto() {
        return saldoInsoluto;
    }

    public void setSaldoInsoluto(Integer saldoInsoluto) {
        this.saldoInsoluto = saldoInsoluto;
    }

    public List<PersonaDTO> getTerceros() {
        return terceros;
    }

    public void setTerceros(List<PersonaDTO> terceros) {
        this.terceros = terceros;
    }

    public PersonaDTO getOtorganteCredito() {
        return otorganteCredito;
    }

    public void setOtorganteCredito(PersonaDTO otorganteCredito) {
        this.otorganteCredito = otorganteCredito;
    }

    public CatalogoDTO getPaisAdeudo() {
        return paisAdeudo;
    }

    public void setPaisAdeudo(CatalogoDTO paisAdeudo) {
        this.paisAdeudo = paisAdeudo;
    }

    public String getTipoAdeudoOtro() {
        return tipoAdeudoOtro;
    }

    public void setTipoAdeudoOtro(String tipoAdeudoOtro) {
        this.tipoAdeudoOtro = tipoAdeudoOtro;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AdeudoDTO{");
        sb.append("titular=").append(titular);
        sb.append(", tipoAdeudo=").append(tipoAdeudo);
        sb.append(", tipoAdeudoOtro='").append(tipoAdeudoOtro).append('\'');
        sb.append(", numeroCuentaContrato='").append(numeroCuentaContrato).append('\'');
        sb.append(", fechaAdquisicion='").append(fechaAdquisicion).append('\'');
        sb.append(", montoOriginal=").append(montoOriginal);
        sb.append(", saldoInsoluto=").append(saldoInsoluto);
        sb.append(", terceros=").append(terceros);
        sb.append(", otorganteCredito=").append(otorganteCredito);
        sb.append(", paisAdeudo=").append(paisAdeudo);
        sb.append('}');
        return sb.toString();
    }
}
