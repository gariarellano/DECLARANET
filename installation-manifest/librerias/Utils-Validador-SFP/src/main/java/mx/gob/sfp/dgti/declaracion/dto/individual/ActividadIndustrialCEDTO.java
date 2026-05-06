/**
 * @(#)ActividadIndustrialCEDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico para enajenación de bienes
 *
 * @author Miriam Sánchez Sánchez programador07
 * @since 05/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ActividadIndustrialCEDTO extends RemuneracionDTO  {

	/**
	 * Nombre o razón social o denominacion del negocio en el cual se lleva a cabo la actividad
	 */
	private String nombreRazonSocial;
	
	/**
	 * Senalar el tipo de negocio por el cual obtuvo el ingreso.
	 */
	private String tipoNegocio;
	
	 /**
     * Constructor
     */
    public ActividadIndustrialCEDTO(){ };
    
    public ActividadIndustrialCEDTO(MontoMonedaDTO remuneracion, String nombreRazonSocial, String tipoNegocio) {
    	super(remuneracion);
    	this.nombreRazonSocial = nombreRazonSocial;
    	this.tipoNegocio = tipoNegocio;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public ActividadIndustrialCEDTO(JsonObject json) {
    	ActividadIndustrialCEDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ActividadIndustrialCEDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the nombreRazonSocial
	 */
	public String getNombreRazonSocial() {
		return nombreRazonSocial;
	}

	/**
	 * @param nombreRazonSocial the nombreRazonSocial to set
	 */
	public void setNombreRazonSocial(String nombreRazonSocial) {
		this.nombreRazonSocial = nombreRazonSocial;
	}

	/**
	 * @return the tipoNegocio
	 */
	public String getTipoNegocio() {
		return tipoNegocio;
	}

	/**
	 * @param tipoNegocio the tipoNegocio to set
	 */
	public void setTipoNegocio(String tipoNegocio) {
		this.tipoNegocio = tipoNegocio;
	}

	
}
