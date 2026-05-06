/**
 * @FideicomisoDTO.java Nov 22, 2019
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
import mx.gob.sfp.dgti.declaracion.dto.individual.PersonaDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.PersonaMoralDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumParticipante;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoFideicomiso;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoParticipacionF;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumUbicacion;

/**
 * DTO para el módulo de fideicomiso
 * 
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 22, 2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class FideicomisoDTO extends RegistroBaseDTO {

	/**
	 * Persona que participa en el fideicomiso
	 */
	private EnumParticipante participante;
	
	/**
	 * Tipo de fideicomiso que se declara 
	 */
	private EnumTipoFideicomiso tipoFideicomiso;
	
	/**
	 * Tipo de participación que se tiene en el fideicomiso
	 */
	private EnumTipoParticipacionF tipoParticipacion;
	
	/**
	 * Rfc del fideicomiso
	 */
	private String rfcFideicomiso;
	
	/**
	 * Datos del fideicomitente
	 */
	private PersonaDTO fideicomitente;
	
	/**
	 * Datos del fiduciario
	 */
	private PersonaMoralDTO fiduciario;
	
	/**
	 * Datos del fideicomisario
	 */
	private PersonaDTO fideicomisario;
	
	/**
	 * Sector productivo al que pertenece
	 */
	private CatalogoDTO sector;

	/**
	 * Otro sector
	 */
	private String sectorOtro;
	
	/**
	 * Localizacion del fideicomiso 
	 */
	private EnumUbicacion localizacion;

	/**
	 * Constructor
	 */
	public FideicomisoDTO() {
	};

	public FideicomisoDTO(String id, String idPosicionVista, EnumTipoOperacion tipoOperacion,
			EnumParticipante participante, EnumTipoFideicomiso tipoFideicomiso, String sectorOtro,
			EnumTipoParticipacionF tipoParticipacion, String rfcFideicomiso, PersonaDTO fideicomitente,
			PersonaMoralDTO fiduciario, PersonaDTO fideicomisario, CatalogoDTO sector, EnumUbicacion localizacion) {
		super(id, idPosicionVista, tipoOperacion);
		this.participante = participante;
		this.tipoFideicomiso = tipoFideicomiso;
		this.tipoParticipacion = tipoParticipacion;
		this.rfcFideicomiso = rfcFideicomiso;
		this.fideicomitente = fideicomitente;
		this.fiduciario = fiduciario;
		this.fideicomisario = fideicomisario;
		this.sector = sector;
		this.sectorOtro = sectorOtro;

		this.localizacion = localizacion;
	}

	/**
	 * Constructor desde JsonObject, utiliza convertidores generados
	 *
	 * @param json objeto en JsonObject
	 */
	public FideicomisoDTO(JsonObject json) {
		FideicomisoDTOConverter.fromJson(json, this);
	}

	/**
	 * Metodo para obtener el JsonObject, utiliza convertidores generados
	 *
	 * @return JsonObject a partir del objeto
	 */
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		FideicomisoDTOConverter.toJson(this, json);
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
	 * @return the tipoFideicomiso
	 */
	public EnumTipoFideicomiso getTipoFideicomiso() {
		return tipoFideicomiso;
	}

	/**
	 * @param tipoFideicomiso the tipoFideicomiso to set
	 */
	public void setTipoFideicomiso(EnumTipoFideicomiso tipoFideicomiso) {
		this.tipoFideicomiso = tipoFideicomiso;
	}

	/**
	 * @return the tipoParticipacion
	 */
	public EnumTipoParticipacionF getTipoParticipacion() {
		return tipoParticipacion;
	}

	/**
	 * @param tipoParticipacion the tipoParticipacion to set
	 */
	public void setTipoParticipacion(EnumTipoParticipacionF tipoParticipacion) {
		this.tipoParticipacion = tipoParticipacion;
	}

	/**
	 * @return the rfcFideicomiso
	 */
	public String getRfcFideicomiso() {
		return rfcFideicomiso;
	}

	/**
	 * @param rfcFideicomiso the rfcFideicomiso to set
	 */
	public void setRfcFideicomiso(String rfcFideicomiso) {
		this.rfcFideicomiso = rfcFideicomiso;
	}

	/**
	 * @return the fideicomitente
	 */
	public PersonaDTO getFideicomitente() {
		return fideicomitente;
	}

	/**
	 * @param fideicomitente the fideicomitente to set
	 */
	public void setFideicomitente(PersonaDTO fideicomitente) {
		this.fideicomitente = fideicomitente;
	}

	/**
	 * @return the fiduciario
	 */
	public PersonaMoralDTO getFiduciario() {
		return fiduciario;
	}

	/**
	 * @param fiduciario the fiduciario to set
	 */
	public void setFiduciario(PersonaMoralDTO fiduciario) {
		this.fiduciario = fiduciario;
	}

	/**
	 * @return the fideicomisario
	 */
	public PersonaDTO getFideicomisario() {
		return fideicomisario;
	}

	/**
	 * @param fideicomisario the fideicomisario to set
	 */
	public void setFideicomisario(PersonaDTO fideicomisario) {
		this.fideicomisario = fideicomisario;
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
	 * @return the localizacion
	 */
	public EnumUbicacion getLocalizacion() {
		return localizacion;
	}

	/**
	 * @param localizacion the localizacion to set
	 */
	public void setLocalizacion(EnumUbicacion localizacion) {
		this.localizacion = localizacion;
	}

	public String getSectorOtro() {
		return sectorOtro;
	}

	public void setSectorOtro(String sectorOtro) {
		this.sectorOtro = sectorOtro;
	}
}
