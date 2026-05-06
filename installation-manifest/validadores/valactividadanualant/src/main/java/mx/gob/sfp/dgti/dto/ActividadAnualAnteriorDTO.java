/**
 * @(#)ActividadAnualAnteriorDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.base.ModuloBaseDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.ActividadAnualDTO;

/**
 * DTO generico para el modulo de actividad anual anterior
 *
 * @author Miriam Sánchez Sánchez programador07
 * @since 05/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ActividadAnualAnteriorDTO extends ModuloBaseDTO {
	
	/**
	 * Bandera para indicar si fue servidor publico el anio inmediato anterior a la fecha del encargo
	 */
	private boolean servidorPublicoAnioAnterior;
	/**
	 * Objeto de la actividad anual anterior
	 */
	private ActividadAnualDTO actividadAnual;
    
    /**
     * Constructor
     */
    public ActividadAnualAnteriorDTO(){ };
    
    public ActividadAnualAnteriorDTO(String aclaracionesObservaciones, boolean servidorPublicoAnioAnterior, 
    		ActividadAnualDTO actividadAnual, EncabezadoDTO encabezado) {
    	super(encabezado, aclaracionesObservaciones);
    	this.servidorPublicoAnioAnterior = servidorPublicoAnioAnterior;
    	this.actividadAnual = actividadAnual;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public ActividadAnualAnteriorDTO(JsonObject json) {
    	ActividadAnualAnteriorDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ActividadAnualAnteriorDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the servidorPublicoAnioAnterior
	 */
	public boolean isServidorPublicoAnioAnterior() {
		return servidorPublicoAnioAnterior;
	}

	/**
	 * @param servidorPublicoAnioAnterior the servidorPublicoAnioAnterior to set
	 */
	public void setServidorPublicoAnioAnterior(boolean servidorPublicoAnioAnterior) {
		this.servidorPublicoAnioAnterior = servidorPublicoAnioAnterior;
	}

	/**
	 * @return the actividadAnual
	 */
	public ActividadAnualDTO getActividadAnual() {
		return actividadAnual;
	}

	/**
	 * @param actividadAnual the actividadAnual to set
	 */
	public void setActividadAnual(ActividadAnualDTO actividadAnual) {
		this.actividadAnual = actividadAnual;
	}


}
