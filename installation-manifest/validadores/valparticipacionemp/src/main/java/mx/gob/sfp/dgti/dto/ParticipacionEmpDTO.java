/**
 * @ParticipacionEmpDTO.java Nov 14, 2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.RegistroBaseDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.LocalizacionDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.MontoMonedaDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.PersonaMoralDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumParticipante;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;

/**
 * Dto para el objeto de participacion en empresas
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 27, 2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ParticipacionEmpDTO extends RegistroBaseDTO {

	/**
	 * Declarar el participante
	 */
	private EnumParticipante participante;
	
	/**
	 * Nombre o razón social completo de la empresa, sociedad o asociación
	 */
	private PersonaMoralDTO nombreEmpresaSociedadAsociacion;
	
	/**
	 * Porcentaje de participación en la empresa
	 */
	private Integer porcentajeParticipacion;
	
	/**
	 * Tipo de participacion en la empresa
	 */
	private CatalogoDTO tipoParticipacion;

	/**
	 * Otro tipo de participacion
	 */
	private String tipoParticipacionOtro;
	
	/**
	 * Elegir el sector al que pertenece la empresa
	 */
	private CatalogoDTO sector;

	/**
	 * Otro tipo de sector
	 */
	private String sectorOtro;
	
	/**
	 * Indica si recibe remuneracion por la participacion en la empresa
	 */
	private boolean recibeRemuneracion;
	
	/**
	 * Monto mensual neto que percibe por la participacion en la empresa
	 */
	private MontoMonedaDTO montoMensual;
	
	/**
	 * Localización de la institucion 
	 */
	private LocalizacionDTO localizacion;
	
	
	/**
     * Constructor
     */
    public ParticipacionEmpDTO(){ };

	public ParticipacionEmpDTO(String id, String idPosicionVista, EnumTipoOperacion tipoOperacion,
							   boolean registroHistorico, boolean verificar, EnumParticipante participante,
							   PersonaMoralDTO nombreEmpresaSociedadAsociacion, Integer porcentajeParticipacion,
							   CatalogoDTO tipoParticipacion, String tipoParticipacionOtro, CatalogoDTO sector,
							   String sectorOtro, boolean recibeRemuneracion, MontoMonedaDTO montoMensual,
							   LocalizacionDTO localizacion) {
		super(id, idPosicionVista, tipoOperacion, registroHistorico, verificar);
		this.participante = participante;
		this.nombreEmpresaSociedadAsociacion = nombreEmpresaSociedadAsociacion;
		this.porcentajeParticipacion = porcentajeParticipacion;
		this.tipoParticipacion = tipoParticipacion;
		this.tipoParticipacionOtro = tipoParticipacionOtro;
		this.sector = sector;
		this.sectorOtro = sectorOtro;
		this.recibeRemuneracion = recibeRemuneracion;
		this.montoMensual = montoMensual;
		this.localizacion = localizacion;
	}

	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public ParticipacionEmpDTO(JsonObject json) {
    	ParticipacionEmpDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ParticipacionEmpDTOConverter.toJson(this, json);
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
	 * @return the nombreEmpresaSociedadAsociacion
	 */
	public PersonaMoralDTO getNombreEmpresaSociedadAsociacion() {
		return nombreEmpresaSociedadAsociacion;
	}

	/**
	 * @param nombreEmpresaSociedadAsociacion the nombreEmpresaSociedadAsociacion to set
	 */
	public void setNombreEmpresaSociedadAsociacion(PersonaMoralDTO nombreEmpresaSociedadAsociacion) {
		this.nombreEmpresaSociedadAsociacion = nombreEmpresaSociedadAsociacion;
	}

	/**
	 * @return the porcentajeParticipacion
	 */
	public Integer getPorcentajeParticipacion() {
		return porcentajeParticipacion;
	}

	/**
	 * @param porcentajeParticipacion the porcentajeParticipacion to set
	 */
	public void setPorcentajeParticipacion(Integer porcentajeParticipacion) {
		this.porcentajeParticipacion = porcentajeParticipacion;
	}

	/**
	 * @return the tipoParticipacion
	 */
	public CatalogoDTO getTipoParticipacion() {
		return tipoParticipacion;
	}

	/**
	 * @param tipoParticipacion the tipoParticipacion to set
	 */
	public void setTipoParticipacion(CatalogoDTO tipoParticipacion) {
		this.tipoParticipacion = tipoParticipacion;
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
	 * @return the recibeRemuneracion
	 */
	public boolean isRecibeRemuneracion() {
		return recibeRemuneracion;
	}

	/**
	 * @param recibeRemuneracion the recibeRemuneracion to set
	 */
	public void setRecibeRemuneracion(boolean recibeRemuneracion) {
		this.recibeRemuneracion = recibeRemuneracion;
	}

	/**
	 * @return the montoMensual
	 */
	public MontoMonedaDTO getMontoMensual() {
		return montoMensual;
	}

	/**
	 * @param montoMensual the montoMensual to set
	 */
	public void setMontoMensual(MontoMonedaDTO montoMensual) {
		this.montoMensual = montoMensual;
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

	public String getTipoParticipacionOtro() {
		return tipoParticipacionOtro;
	}

	public String getSectorOtro() {
		return sectorOtro;
	}

	public void setSectorOtro(String sectorOtro) {
		this.sectorOtro = sectorOtro;
	}

	public void setTipoParticipacionOtro(String tipoParticipacionOtro) {
		this.tipoParticipacionOtro = tipoParticipacionOtro;
	}
}
