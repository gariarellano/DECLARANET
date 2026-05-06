/**
 * @(#)BienDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.general;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.RegistroBaseDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.*;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumFormaPago;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;

import java.util.List;

/**
 * DTO generico para la base general de los bienes
 *
 * @author Miriam Sánchez Sánchez programador07
 * @author pavel.martinez
 * @since 24/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class BienDTO extends RegistroBaseDTO {

	/**
	 * Titular del bien
	 */
	private CatalogoUnoDTO titular;

	/**
	 * Nombre del tercero o terceros
	 */
	private List<PersonaDTO> terceros;

	/**
	 * Transmisor o transmisores
	 */
	private List<TransmisorDTO> transmisores;

	/**
	 * Fecha de adquision del bien
	 */
	private String fechaAdquisicion;

	/**
	 * Forma de adquisicion
	 */
	private CatalogoDTO formaAdquisicion;

	/**
	 * Forma del pago
	 */
	private EnumFormaPago formaPago;

	/**
	 * Valor de adquisicion
	 */
	private MontoMonedaDTO valorAdquisicion;

	/**
	 * En caso de baja incluir motivo
	 */
	private CatalogoDTO motivoBaja;

	/**
	 * Otro motivo baja
	 */
	private String motivoBajaOtro;

    /**
     * En caso de Baja y Venta indicar el valor de venta
     */
    private MontoMonedaDTO valorVenta;
	 /**
     * Constructor
     */
    public BienDTO(){ };


	public BienDTO(String id, String idPosicionVista, EnumTipoOperacion tipoOperacion, boolean registroHistorico,
				   boolean verificar, CatalogoUnoDTO titular, List<PersonaDTO> terceros, List<TransmisorDTO> transmisores,
				   String fechaAdquisicion, CatalogoDTO formaAdquisicion, EnumFormaPago formaPago,
				   MontoMonedaDTO valorAdquisicion, CatalogoDTO motivoBaja, String motivoBajaOtro, MontoMonedaDTO valorVenta) {
		super(id, idPosicionVista, tipoOperacion, registroHistorico, verificar);
		this.titular = titular;
		this.terceros = terceros;
		this.transmisores = transmisores;
		this.fechaAdquisicion = fechaAdquisicion;
		this.formaAdquisicion = formaAdquisicion;
		this.formaPago = formaPago;
		this.valorAdquisicion = valorAdquisicion;
		this.motivoBaja = motivoBaja;
		this.motivoBajaOtro = motivoBajaOtro;
		this.valorVenta = valorVenta;
	}

	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public BienDTO(JsonObject json) {
    	BienDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        BienDTOConverter.toJson(this, json);
        return json;
    }

	public CatalogoUnoDTO getTitular() {
		return titular;
	}

	public void setTitular(CatalogoUnoDTO titular) {
		this.titular = titular;
	}

	public List<PersonaDTO> getTerceros() {
		return terceros;
	}

	public void setTerceros(List<PersonaDTO> terceros) {
		this.terceros = terceros;
	}

	public List<TransmisorDTO> getTransmisores() {
		return transmisores;
	}

	public void setTransmisores(List<TransmisorDTO> transmisores) {
		this.transmisores = transmisores;
	}

	public String getFechaAdquisicion() {
		return fechaAdquisicion;
	}

	public void setFechaAdquisicion(String fechaAdquisicion) {
		this.fechaAdquisicion = fechaAdquisicion;
	}

	public CatalogoDTO getFormaAdquisicion() {
		return formaAdquisicion;
	}

	public void setFormaAdquisicion(CatalogoDTO formaAdquisicion) {
		this.formaAdquisicion = formaAdquisicion;
	}

	public EnumFormaPago getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(EnumFormaPago formaPago) {
		this.formaPago = formaPago;
	}

	public MontoMonedaDTO getValorAdquisicion() {
		return valorAdquisicion;
	}

	public void setValorAdquisicion(MontoMonedaDTO valorAdquisicion) {
		this.valorAdquisicion = valorAdquisicion;
	}

	public CatalogoDTO getMotivoBaja() {
		return motivoBaja;
	}

	public void setMotivoBaja(CatalogoDTO motivoBaja) {
		this.motivoBaja = motivoBaja;
	}

    public MontoMonedaDTO getValorVenta() {
        return valorVenta;
    }

    public void setValorVenta(MontoMonedaDTO valorVenta) {
        this.valorVenta = valorVenta;
    }

	public String getMotivoBajaOtro() {
		return motivoBajaOtro;
	}

	public void setMotivoBajaOtro(String motivoBajaOtro) {
		this.motivoBajaOtro = motivoBajaOtro;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("BienDTO{");
		sb.append("titular=").append(titular);
		sb.append(", terceros=").append(terceros);
		sb.append(", transmisores=").append(transmisores);
		sb.append(", fechaAdquisicion='").append(fechaAdquisicion).append('\'');
		sb.append(", formaAdquisicion=").append(formaAdquisicion);
		sb.append(", formaPago=").append(formaPago);
		sb.append(", valorAdquisicion=").append(valorAdquisicion);
		sb.append(", motivoBaja=").append(motivoBaja);
		sb.append(", motivoBajaOtro='").append(motivoBajaOtro).append('\'');
		sb.append(", valorVenta=").append(valorVenta);
		sb.append('}');
		return sb.toString();
	}
}
