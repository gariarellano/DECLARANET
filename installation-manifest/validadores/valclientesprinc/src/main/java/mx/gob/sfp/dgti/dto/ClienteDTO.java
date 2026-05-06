/**
 * @ClienteDTO.java Nov 14, 2019
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
import mx.gob.sfp.dgti.declaracion.dto.general.LocalizacionDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoSubInversionDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.LocalizacionInversionDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.MontoMonedaDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.PersonaDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumParticipante;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;

/**
 * Dto para el objeto de cliente
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 25, 2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ClienteDTO extends RegistroBaseDTO {

	/**
	 * Declarar el participante
	 */
	private EnumParticipante participante;
	
	/**
	 * Señalar nombre o razón social completos de la empresa 
	 * o en su caso el servicio que proporciona.
	 */
	private String nombreEmpresaServicio;
	
	/**
	 * Establecer los doce dígitos de la empresa.
	 */
	private String rfcEmpresa;
	
	/**
	 * Seleccionar si es persona física o persona moral.
	 */
	private PersonaDTO clientePrincipal;
	
	/**
	 * Sector al que pertenece
	 */
	private CatalogoDTO sector;

	/**
	 * Otro sector
	 */
	private String sectorOtro;
	
	/**
	 * Ganancia que le genera el cliente principal. 
	 */
	private MontoMonedaDTO montoAproximadoGanancia;
	
	/**
	 * Localización del cliente  
	 */
	private LocalizacionDTO localizacion;
	
	
	/**
     * Constructor
     */
    public ClienteDTO(){ };
    
    public ClienteDTO(EnumParticipante participante, String nombreEmpresaServicio,
    		String rfcEmpresa, PersonaDTO clientePrincipal, CatalogoDTO sector, MontoMonedaDTO montoAproximadoGanancia,
    		LocalizacionDTO localizacion, String id, String idPosicionVista, EnumTipoOperacion tipoOperacion,
					  String sectorOtro) {
    	super(id, idPosicionVista, tipoOperacion);
    	this.participante = participante;
    	this.nombreEmpresaServicio = nombreEmpresaServicio;
    	this.rfcEmpresa = rfcEmpresa;
    	this.clientePrincipal = clientePrincipal;
    	this.sector = sector;
    	this.montoAproximadoGanancia = montoAproximadoGanancia;
    	this.localizacion = localizacion;
    	this.sectorOtro = sectorOtro;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public ClienteDTO(JsonObject json) {
    	ClienteDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ClienteDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the participante
	 */
	public EnumParticipante getParticipante() {
		return participante;
	}

	/**
	 * @param participante the participante to set
	 */
	public void setParticipante(EnumParticipante participante) {
		this.participante = participante;
	}

	/**
	 * @return the nombreEmpresaServicio
	 */
	public String getNombreEmpresaServicio() {
		return nombreEmpresaServicio;
	}

	/**
	 * @param nombreEmpresaServicio the nombreEmpresaServicio to set
	 */
	public void setNombreEmpresaServicio(String nombreEmpresaServicio) {
		this.nombreEmpresaServicio = nombreEmpresaServicio;
	}

	/**
	 * @return the rfcEmpresa
	 */
	public String getRfcEmpresa() {
		return rfcEmpresa;
	}

	/**
	 * @param rfcEmpresa the rfcEmpresa to set
	 */
	public void setRfcEmpresa(String rfcEmpresa) {
		this.rfcEmpresa = rfcEmpresa;
	}

	/**
	 * @return the clientePrincipal
	 */
	public PersonaDTO getClientePrincipal() {
		return clientePrincipal;
	}

	/**
	 * @param clientePrincipal the clientePrincipal to set
	 */
	public void setClientePrincipal(PersonaDTO clientePrincipal) {
		this.clientePrincipal = clientePrincipal;
	}

	/**
	 * @return the sector
	 */
	public CatalogoDTO getSector() {
		return sector;
	}

	/**
	 * @param sector the sector to set
	 */
	public void setSector(CatalogoDTO sector) {
		this.sector = sector;
	}

	/**
	 * @return the montoAproximadoGanancia
	 */
	public MontoMonedaDTO getMontoAproximadoGanancia() {
		return montoAproximadoGanancia;
	}

	/**
	 * @param montoAproximadoGanancia the montoAproximadoGanancia to set
	 */
	public void setMontoAproximadoGanancia(MontoMonedaDTO montoAproximadoGanancia) {
		this.montoAproximadoGanancia = montoAproximadoGanancia;
	}

	/**
	 * @return the localizacion
	 */
	public LocalizacionDTO getLocalizacion() {
		return localizacion;
	}

	/**
	 * @param localizacion the localizacion to set
	 */
	public void setLocalizacion(LocalizacionDTO localizacion) {
		this.localizacion = localizacion;
	}


	public String getSectorOtro() {
		return sectorOtro;
	}

	public void setSectorOtro(String sectorOtro) {
		this.sectorOtro = sectorOtro;
	}
}
