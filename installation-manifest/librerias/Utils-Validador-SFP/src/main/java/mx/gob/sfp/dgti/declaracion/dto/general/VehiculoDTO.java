/**
 * @(#)VehiculoDTO.java 12/11/2019
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
 * DTO para el modulo de vehiculo
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 12/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class VehiculoDTO extends BienDTO {

    /**
     * Tipo de vehiculo
     */
    private CatalogoDTO tipoVehiculo;

    /**
     * Otro tipo vehiculo
     */
    private String tipoVehiculoOtro;

    /**
     * Marca del vehiculo
     */
    private String marca;

    /**
     * Modelo del vehiculo
     */
    private String modelo;

    /**
     * Anio del vehiculo
     */
    private Integer anio;

    /**
     * Anio del vehiculo
     */
    private String numeroSerieRegistro;

    /**
     * Lugar del registro
     */
    private LocalizacionDTO lugarRegistro;


    /**
     * Constructor
     */
    public VehiculoDTO() {
    }

    public VehiculoDTO(String id, String idPosicionVista, EnumTipoOperacion tipoOperacion, boolean registroHistorico,
                       boolean verificar, CatalogoUnoDTO titular, List<PersonaDTO> terceros,
                       List<TransmisorDTO> transmisores, String fechaAdquisicion, CatalogoDTO formaAdquisicion,
                       EnumFormaPago formaPago, MontoMonedaDTO valorAdquisicion, CatalogoDTO motivoBaja,
                       String motivoBajaOtro, MontoMonedaDTO valorVenta, CatalogoDTO tipoVehiculo,
                       String tipoVehiculoOtro, String marca, String modelo, Integer anio, String numeroSerieRegistro,
                       LocalizacionDTO lugarRegistro) {
        super(id, idPosicionVista, tipoOperacion, registroHistorico, verificar, titular, terceros, transmisores,
                fechaAdquisicion, formaAdquisicion, formaPago, valorAdquisicion, motivoBaja, motivoBajaOtro, valorVenta);
        this.tipoVehiculo = tipoVehiculo;
        this.tipoVehiculoOtro = tipoVehiculoOtro;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.numeroSerieRegistro = numeroSerieRegistro;
        this.lugarRegistro = lugarRegistro;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public VehiculoDTO(JsonObject json) {
        VehiculoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        VehiculoDTOConverter.toJson(this, json);
        return json;
    }

    public CatalogoDTO getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(CatalogoDTO tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerieRegistro() {
        return numeroSerieRegistro;
    }

    public void setNumeroSerieRegistro(String numeroSerieRegistro) {
        this.numeroSerieRegistro = numeroSerieRegistro;
    }

    public LocalizacionDTO getLugarRegistro() {
        return lugarRegistro;
    }

    public void setLugarRegistro(LocalizacionDTO lugarRegistro) {
        this.lugarRegistro = lugarRegistro;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getTipoVehiculoOtro() {
        return tipoVehiculoOtro;
    }

    public void setTipoVehiculoOtro(String tipoVehiculoOtro) {
        this.tipoVehiculoOtro = tipoVehiculoOtro;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VehiculoDTO{");
        sb.append("tipoVehiculo=").append(tipoVehiculo);
        sb.append(", tipoVehiculoOtro='").append(tipoVehiculoOtro).append('\'');
        sb.append(", marca='").append(marca).append('\'');
        sb.append(", modelo='").append(modelo).append('\'');
        sb.append(", anio='").append(anio).append('\'');
        sb.append(", numeroSerieRegistro='").append(numeroSerieRegistro).append('\'');
        sb.append(", lugarRegistro=").append(lugarRegistro);
        sb.append('}');
        return sb.toString();
    }
}
