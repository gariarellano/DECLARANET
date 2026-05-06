/**
 * @(#)BienMuebleDTO.java 13/11/2019
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

import java.util.List;

/**
 * DTO para el modulo de bien mueble
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 13/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class BienMuebleDTO extends BienDTO {


    /**
     * Tipo de bien
     */
    private CatalogoDTO tipoBien;

    /**
     * Otro tipo de bien
     */
    private String tipoBienOtro;

    /**
     * Descripcion general del bien, referir brevemente el bien reportado
     */
    private String descripcionGeneralBien;


    public BienMuebleDTO() {
    }

    public BienMuebleDTO(String id, String idPosicionVista, EnumTipoOperacion tipoOperacion, boolean registroHistorico,
                         boolean verificar, CatalogoUnoDTO titular, List<PersonaDTO> terceros,
                         List<TransmisorDTO> transmisores, String fechaAdquisicion, CatalogoDTO formaAdquisicion,
                         EnumFormaPago formaPago, MontoMonedaDTO valorAdquisicion, CatalogoDTO motivoBaja,
                         String motivoBajaOtro, MontoMonedaDTO valorVenta, CatalogoDTO tipoBien, String tipoBienOtro,
                         String descripcionGeneralBien) {
        super(id, idPosicionVista, tipoOperacion, registroHistorico, verificar, titular, terceros, transmisores,
                fechaAdquisicion, formaAdquisicion, formaPago, valorAdquisicion, motivoBaja, motivoBajaOtro, valorVenta);
        this.tipoBien = tipoBien;
        this.tipoBienOtro = tipoBienOtro;
        this.descripcionGeneralBien = descripcionGeneralBien;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public BienMuebleDTO(JsonObject json) {
        BienMuebleDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        BienMuebleDTOConverter.toJson(this, json);
        return json;
    }

    public CatalogoDTO getTipoBien() {
        return tipoBien;
    }

    public void setTipoBien(CatalogoDTO tipoBien) {
        this.tipoBien = tipoBien;
    }

    public String getDescripcionGeneralBien() {
        return descripcionGeneralBien;
    }

    public void setDescripcionGeneralBien(String descripcionGeneralBien) {
        this.descripcionGeneralBien = descripcionGeneralBien;
    }

    public String getTipoBienOtro() {
        return tipoBienOtro;
    }

    public void setTipoBienOtro(String tipoBienOtro) {
        this.tipoBienOtro = tipoBienOtro;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BienMuebleDTO{");
        sb.append("tipoBien=").append(tipoBien);
        sb.append(", tipoBienOtro='").append(tipoBienOtro).append('\'');
        sb.append(", descripcionGeneralBien='").append(descripcionGeneralBien).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
