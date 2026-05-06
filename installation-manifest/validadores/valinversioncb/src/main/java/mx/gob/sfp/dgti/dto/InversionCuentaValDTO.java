/**
 * @InversionCuentaValDTO.java Nov 14, 2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.dto;

import java.util.List;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.base.ModuloBaseDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.ActividadAnualDTO;

/**
 * DTO para el módulo de inversiones y cuentas bancarias
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 14, 2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class InversionCuentaValDTO extends ModuloBaseDTO {

	/**
	 * Bandera que indica si tiene invesiones a declarar
	 */
	private boolean ninguno;
	
	/**
	 * Lista de inversiones a declarar
	 */
	private List<InversionDTO> inversion;
	
	/**
     * Constructor
     */
    public InversionCuentaValDTO(){ };
    
    public InversionCuentaValDTO(boolean ninguno, List<InversionDTO> inversion, 
    		String aclaracionesObservaciones, EncabezadoDTO encabezado) {
    	super(encabezado, aclaracionesObservaciones);
    	this.ninguno = ninguno;
    	this.inversion = inversion;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public InversionCuentaValDTO(JsonObject json) {
    	InversionCuentaValDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        InversionCuentaValDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the ninguno
	 */
	public boolean isNinguno() {
		return ninguno;
	}

	/**
	 * @param ninguno the ninguno to set
	 */
	public void setNinguno(boolean ninguno) {
		this.ninguno = ninguno;
	}

	/**
	 * @return the inversion
	 */
	public List<InversionDTO> getInversion() {
		return inversion;
	}

	/**
	 * @param inversion the inversion to set
	 */
	public void setInversion(List<InversionDTO> inversion) {
		this.inversion = inversion;
	}
}
