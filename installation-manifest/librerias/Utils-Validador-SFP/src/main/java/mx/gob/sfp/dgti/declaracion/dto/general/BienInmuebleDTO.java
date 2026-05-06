/**
 * @(#)BienInmuebleDTO.java 11/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.general;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.individual.*;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumFormaPago;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumValorConformeA;

import java.util.List;

/**
 * DTO para el modulo de bien inmueble
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 11/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class BienInmuebleDTO extends BienDTO {


    /**
     * Tipo de inmueble que se declara: casa, departamento, edificio, local ...
     */
    private CatalogoDTO tipoInmueble;

    /**
     * Otro tipo de inmueble
     */
    private String tipoInmuebleOtro;

    /**
     * Porcentaje de propiedad del declarante conforme a escrituracion o contrato
     */
    private Integer porcentajePropiedad;

    /**
     * Superficie del terreno en m2
     */
    private Integer superficieTerrenoM2;

    /**
     * Superficie total de construccion en m2
     */
    private Integer superficieConstruccionM2;

    /**
     * Datos del registro publico de la propiedad, folio real u otro dato que permita identificacion
     */
    private String datoIdentificacion;

    /**
     * El valor de adquisicion del inmueble es conforme a
     */
    private EnumValorConformeA valorConformeA;

    /**
     * Domicilio
     */
    private DomicilioDTO domicilio;

    public BienInmuebleDTO() {
    }

    public BienInmuebleDTO(String id, String idPosicionVista, EnumTipoOperacion tipoOperacion, boolean registroHistorico,
                           boolean verificar, CatalogoUnoDTO titular, List<PersonaDTO> terceros,
                           List<TransmisorDTO> transmisores, String fechaAdquisicion, CatalogoDTO formaAdquisicion,
                           EnumFormaPago formaPago, MontoMonedaDTO valorAdquisicion, CatalogoDTO motivoBaja,
                           String motivoBajaOtro, MontoMonedaDTO valorVenta, CatalogoDTO tipoInmueble,
                           String tipoInmuebleOtro, Integer porcentajePropiedad, Integer superficieTerrenoM2,
                           Integer superficieConstruccionM2, String datoIdentificacion, EnumValorConformeA valorConformeA,
                           DomicilioDTO domicilio) {
        super(id, idPosicionVista, tipoOperacion, registroHistorico, verificar, titular, terceros, transmisores,
                fechaAdquisicion, formaAdquisicion, formaPago, valorAdquisicion, motivoBaja, motivoBajaOtro, valorVenta);
        this.tipoInmueble = tipoInmueble;
        this.tipoInmuebleOtro = tipoInmuebleOtro;
        this.porcentajePropiedad = porcentajePropiedad;
        this.superficieTerrenoM2 = superficieTerrenoM2;
        this.superficieConstruccionM2 = superficieConstruccionM2;
        this.datoIdentificacion = datoIdentificacion;
        this.valorConformeA = valorConformeA;
        this.domicilio = domicilio;
    }

    public BienInmuebleDTO(CatalogoDTO tipoInmueble, DomicilioDTO domicilio) {
    	this.tipoInmueble = tipoInmueble;
    	this.domicilio = domicilio;
    }
    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public BienInmuebleDTO(JsonObject json) {
        BienInmuebleDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        BienInmuebleDTOConverter.toJson(this, json);
        return json;
    }

    public CatalogoDTO getTipoInmueble() {
        return tipoInmueble;
    }

    public void setTipoInmueble(CatalogoDTO tipoInmueble) {
        this.tipoInmueble = tipoInmueble;
    }

    public Integer getPorcentajePropiedad() {
        return porcentajePropiedad;
    }

    public void setPorcentajePropiedad(Integer porcentajePropiedad) {
        this.porcentajePropiedad = porcentajePropiedad;
    }

    public Integer getSuperficieTerrenoM2() {
        return superficieTerrenoM2;
    }

    public void setSuperficieTerrenoM2(Integer superficieTerrenoM2) {
        this.superficieTerrenoM2 = superficieTerrenoM2;
    }

    public Integer getSuperficieConstruccionM2() {
        return superficieConstruccionM2;
    }

    public void setSuperficieConstruccionM2(Integer superficieConstruccionM2) {
        this.superficieConstruccionM2 = superficieConstruccionM2;
    }

    public String getDatoIdentificacion() {
        return datoIdentificacion;
    }

    public void setDatoIdentificacion(String datoIdentificacion) {
        this.datoIdentificacion = datoIdentificacion;
    }

    public EnumValorConformeA getValorConformeA() {
        return valorConformeA;
    }

    public void setValorConformeA(EnumValorConformeA valorConformeA) {
        this.valorConformeA = valorConformeA;
    }

    public DomicilioDTO getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(DomicilioDTO domicilio) {
        this.domicilio = domicilio;
    }

    public String getTipoInmuebleOtro() {
        return tipoInmuebleOtro;
    }

    public void setTipoInmuebleOtro(String tipoInmuebleOtro) {
        this.tipoInmuebleOtro = tipoInmuebleOtro;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BienInmuebleDTO{");
        sb.append("tipoInmueble=").append(tipoInmueble);
        sb.append(", tipoInmuebleOtro='").append(tipoInmuebleOtro).append('\'');
        sb.append(", porcentajePropiedad=").append(porcentajePropiedad);
        sb.append(", superficieTerrenoM2=").append(superficieTerrenoM2);
        sb.append(", superficieConstruccionM2=").append(superficieConstruccionM2);
        sb.append(", datoIdentificacion='").append(datoIdentificacion).append('\'');
        sb.append(", valorConformeA=").append(valorConformeA);
        sb.append(", domicilio=").append(domicilio);
        sb.append('}');
        return sb.toString();
    }
}
