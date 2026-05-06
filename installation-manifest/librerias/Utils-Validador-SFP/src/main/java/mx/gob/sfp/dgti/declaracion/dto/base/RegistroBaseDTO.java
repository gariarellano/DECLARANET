/**
 * @(#)RegistroBaseDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.base;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.general.FilaDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;

/**
 * DTO generico para la base de los registros
 * @author Miriam Sánchez Sánchez programador07
 * @since 24/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class RegistroBaseDTO extends FilaDTO {

	private EnumTipoOperacion tipoOperacion;
	
	private boolean registroHistorico;
	
	 /**
     * Constructor
     */
    public RegistroBaseDTO(){ };

    public RegistroBaseDTO(String id, String idPosicionVista, EnumTipoOperacion tipoOperacion) {
		super(id, idPosicionVista);
		this.tipoOperacion = tipoOperacion;
    }
    
	public RegistroBaseDTO(String id, String idPosicionVista, EnumTipoOperacion tipoOperacion, boolean registroHistorico) {
		super(id, idPosicionVista);
		this.tipoOperacion = tipoOperacion;
		this.registroHistorico = registroHistorico;
	}
	public RegistroBaseDTO(String id, String idPosicionVista, EnumTipoOperacion tipoOperacion, boolean registroHistorico, boolean verificar) {
		super(id, idPosicionVista, verificar);
		this.tipoOperacion = tipoOperacion;
		this.registroHistorico = registroHistorico;
	}

	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public RegistroBaseDTO(JsonObject json) {
    	RegistroBaseDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RegistroBaseDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the tipoOperacion
	 */
	public EnumTipoOperacion getTipoOperacion() {
		return tipoOperacion;
	}
	/**
	 * @param tipoOperacion the tipoOperacion to set
	 */
	public void setTipoOperacion(EnumTipoOperacion tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	/**
	 * @return the registroHistorico
	 */
	public boolean isRegistroHistorico() {
		return registroHistorico;
	}

	/**
	 * @param registroHistorico the registroHistorico to set
	 */
	public void setRegistroHistorico(boolean registroHistorico) {
		this.registroHistorico = registroHistorico;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("RegistroBaseDTO{");
		sb.append("tipoOperacion=").append(tipoOperacion);
		sb.append("super=").append(super.toString());
		sb.append('}');
		return sb.toString();
	}

}
