/**
 * @ParticipacionDTO.java Nov 14, 2019
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
import mx.gob.sfp.dgti.declaracion.dto.individual.PersonaMoralDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumParticipante;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;

/**
 * Dto para el objeto de instituciones
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 26, 2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ParticipacionDTO extends RegistroBaseDTO {

	/**
	 * Declarar el participante
	 */
	private EnumParticipante participante;
	
	/**
	 * Señalar el tipo de institucion 
	 */
	private CatalogoDTO tipoInstitucion;

	/**
	 * Otro tipo de institucion
	 */
	private String tipoInstitucionOtro;
	
	/**
	 * Nombre de la institucion
	 */
	private PersonaMoralDTO institucion;
	
	/**
	 * Indicar el cargo o función que ejerce el declarante
	 */
	private String puestoRol;

	/**
	 * Fecha en que inicio funciones en la institución u organizacion.
	 */
	private String fechaInicioParticipacion;
	
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
    public ParticipacionDTO(){ };
    
    public ParticipacionDTO(EnumParticipante participante, CatalogoDTO tipoInstitucion, PersonaMoralDTO institucion, 
    		String puestoRol, String fechaInicioParticipacion, boolean recibeRemuneracion, MontoMonedaDTO montoMensual,
    		LocalizacionDTO localizacion, String id, String idPosicionVista, EnumTipoOperacion tipoOperacion,
							String tipoInstitucionOtro) {
    	super(id, idPosicionVista, tipoOperacion);
    	this.participante = participante;
    	this.tipoInstitucion = tipoInstitucion;
    	this.institucion = institucion;
    	this.puestoRol = puestoRol;
    	this.fechaInicioParticipacion = fechaInicioParticipacion;
    	this.recibeRemuneracion = recibeRemuneracion;
    	this.montoMensual = montoMensual;
    	this.localizacion = localizacion;
    	this.tipoInstitucionOtro = tipoInstitucionOtro;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public ParticipacionDTO(JsonObject json) {
    	ParticipacionDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ParticipacionDTOConverter.toJson(this, json);
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
	 * @return the tipoInstitucion
	 */
	public CatalogoDTO getTipoInstitucion() {
		return tipoInstitucion;
	}

	/**
	 * @param tipoInstitucion the tipoInstitucion to set
	 */
	public void setTipoInstitucion(CatalogoDTO tipoInstitucion) {
		this.tipoInstitucion = tipoInstitucion;
	}

	/**
	 * @return the institucion
	 */
	public PersonaMoralDTO getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(PersonaMoralDTO institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the puestoRol
	 */
	public String getPuestoRol() {
		return puestoRol;
	}

	/**
	 * @param puestoRol the puestoRol to set
	 */
	public void setPuestoRol(String puestoRol) {
		this.puestoRol = puestoRol;
	}

	/**
	 * @return the fechaInicioParticipacion
	 */
	public String getFechaInicioParticipacion() {
		return fechaInicioParticipacion;
	}

	/**
	 * @param fechaInicioParticipacion the fechaInicioParticipacion to set
	 */
	public void setFechaInicioParticipacion(String fechaInicioParticipacion) {
		this.fechaInicioParticipacion = fechaInicioParticipacion;
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

	public String getTipoInstitucionOtro() {
		return tipoInstitucionOtro;
	}

	public void setTipoInstitucionOtro(String tipoInstitucionOtro) {
		this.tipoInstitucionOtro = tipoInstitucionOtro;
	}
}
