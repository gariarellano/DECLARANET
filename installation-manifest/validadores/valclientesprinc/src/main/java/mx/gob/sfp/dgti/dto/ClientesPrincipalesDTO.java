/**
 * @ClientesPrincipalesDTO.java Nov 14, 2019
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

/**
 * DTO para el módulo de clientes principales
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 25, 2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ClientesPrincipalesDTO extends ModuloBaseDTO {

	/**
	 * Bandera que indica si realiza actividad lucrativa
	 */
	private boolean realizaActividadLucrativa;
	
	/**
	 * Lista de clientes a declarar
	 */
	private List<ClienteDTO> clientes;
	
	/**
     * Constructor
     */
    public ClientesPrincipalesDTO(){ };
    
    public ClientesPrincipalesDTO(boolean realizaActividadLucrativa, List<ClienteDTO> clientes, String aclaracionesObservaciones, EncabezadoDTO encabezado) {
    	super(encabezado, aclaracionesObservaciones);
    	this.realizaActividadLucrativa = realizaActividadLucrativa;
    	this.clientes = clientes;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public ClientesPrincipalesDTO(JsonObject json) {
    	ClientesPrincipalesDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ClientesPrincipalesDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the clientes
	 */
	public List<ClienteDTO> getClientes() {
		return clientes;
	}

	/**
	 * @param clientes the clientes to set
	 */
	public void setClientes(List<ClienteDTO> clientes) {
		this.clientes = clientes;
	}

	/**
	 * @return the realizaActividadLucrativa
	 */
	public boolean isRealizaActividadLucrativa() {
		return realizaActividadLucrativa;
	}

	/**
	 * @param realizaActividadLucrativa the realizaActividadLucrativa to set
	 */
	public void setRealizaActividadLucrativa(boolean realizaActividadLucrativa) {
		this.realizaActividadLucrativa = realizaActividadLucrativa;
	}

}
