/**
 * @InversionDTO.java Nov 14, 2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.dto;

import java.util.List;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.RegistroBaseDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoFkDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoUnoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.LocalizacionInversionDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.MontoMonedaDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.PersonaDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumUbicacion;

/**
 * Dto para el objeto de inversión
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 14, 2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class InversionDTO extends RegistroBaseDTO {

	/**
	 * Tipo de inversion que se está declarando
	 */
	private CatalogoDTO tipoInversion;
	
	/**
	 * Subtipo de inversión
	 */
	private CatalogoFkDTO subTipoInversion;
	
	/**
	 * Titular de la inversion
	 */
	private CatalogoUnoDTO titular;
	
	/**
	 * Lista de terceros
	 */
	private List<PersonaDTO> terceros;
	
	/**
	 * Numero de cuenta o contrato de la inversion
	 */
	private String numeroCuentaContrato;
	
	/**
	 * Ubicacion
	 */
	private EnumUbicacion ubicacion;
	
	/**
	 * Localizacion de la inversion
	 */
	private LocalizacionInversionDTO localizacionInversion;
	
	/**
	 * Saldo de la inversion
	 */
	private MontoMonedaDTO saldo;
	
	
	/**
     * Constructor
     */
    public InversionDTO(){ };
    
    public InversionDTO(CatalogoDTO tipoInversion, CatalogoFkDTO subTipoInversion, CatalogoUnoDTO titular, List<PersonaDTO> terceros, 
    		String numeroCuentaContrato, EnumUbicacion ubicacion, LocalizacionInversionDTO localizacionInversion, 
    		MontoMonedaDTO saldo, String id, String idPosicionVista, EnumTipoOperacion tipoOperacion) {
    	super(id, idPosicionVista, tipoOperacion);
    	this.tipoInversion = tipoInversion;
    	this.subTipoInversion = subTipoInversion;
    	this.titular = titular;
    	this.terceros = terceros;
    	this.numeroCuentaContrato = numeroCuentaContrato;
    	this.ubicacion = ubicacion;
    	this.localizacionInversion = localizacionInversion;
    	this.saldo = saldo;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public InversionDTO(JsonObject json) {
    	InversionDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        InversionDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the tipoInversion
	 */
	public CatalogoDTO getTipoInversion() {
		return tipoInversion;
	}

	/**
	 * @param tipoInversion the tipoInversion to set
	 */
	public void setTipoInversion(CatalogoDTO tipoInversion) {
		this.tipoInversion = tipoInversion;
	}

	/**
	 * @return the titular
	 */
	public CatalogoUnoDTO getTitular() {
		return titular;
	}

	/**
	 * @param titular the titular to set
	 */
	public void setTitular(CatalogoUnoDTO titular) {
		this.titular = titular;
	}

	/**
	 * @return the terceros
	 */
	public List<PersonaDTO> getTerceros() {
		return terceros;
	}

	/**
	 * @param terceros the terceros to set
	 */
	public void setTerceros(List<PersonaDTO> terceros) {
		this.terceros = terceros;
	}

	/**
	 * @return the numeroCuentaContrato
	 */
	public String getNumeroCuentaContrato() {
		return numeroCuentaContrato;
	}

	/**
	 * @param numeroCuentaContrato the numeroCuentaContrato to set
	 */
	public void setNumeroCuentaContrato(String numeroCuentaContrato) {
		this.numeroCuentaContrato = numeroCuentaContrato;
	}

	/**
	 * @return the localizacionInversion
	 */
	public LocalizacionInversionDTO getLocalizacionInversion() {
		return localizacionInversion;
	}

	/**
	 * @param localizacionInversion the localizacionInversion to set
	 */
	public void setLocalizacionInversion(LocalizacionInversionDTO localizacionInversion) {
		this.localizacionInversion = localizacionInversion;
	}

	/**
	 * @return the saldo
	 */
	public MontoMonedaDTO getSaldo() {
		return saldo;
	}

	/**
	 * @param saldo the saldo to set
	 */
	public void setSaldo(MontoMonedaDTO saldo) {
		this.saldo = saldo;
	}

	/**
	 * @return the ubicacion
	 */
	public EnumUbicacion getUbicacion() {
		return ubicacion;
	}

	/**
	 * @param ubicacion the ubicacion to set
	 */
	public void setUbicacion(EnumUbicacion ubicacion) {
		this.ubicacion = ubicacion;
	}

	/**
	 * @return the subTipoInversion
	 */
	public CatalogoFkDTO getSubTipoInversion() {
		return subTipoInversion;
	}

	/**
	 * @param subTipoInversion the subTipoInversion to set
	 */
	public void setSubTipoInversion(CatalogoFkDTO subTipoInversion) {
		this.subTipoInversion = subTipoInversion;
	}
	
}
