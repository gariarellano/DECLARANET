/**
 * @(#)DocumentoObtenidoDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.util.EnumTipoDocumento;

/**
 * DTO para el módulo de documento obtenido
 *
 * @author Miriam Sanchez Sanchez
 * @since 17/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DocumentoObtenidoDTO {

	private EnumTipoDocumento tipo;
	private String fechaObtencion;

	/**
	 * Constructor
	 */
	public DocumentoObtenidoDTO() {
	};

	/**
	 * Constructor con parámetros
	 */
	public DocumentoObtenidoDTO(EnumTipoDocumento tipo, String fechaObtencion) {
		this.tipo = tipo;
		this.fechaObtencion = fechaObtencion;
	};

	/**
	 * Constructor desde JsonObject, utiliza convertidores generados
	 *
	 * @param json: objeto en JsonObject
	 */
	public DocumentoObtenidoDTO(JsonObject json) {
		DocumentoObtenidoDTOConverter.fromJson(json, this);
	}

	/**
	 * Metodo para obtener el JsonObject, utiliza convertidores generados
	 *
	 * @return JsonObject a partir del objeto
	 */
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		DocumentoObtenidoDTOConverter.toJson(this, json);
		return json;
	}

	/**
	 * @return the tipo
	 */
	public EnumTipoDocumento getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(EnumTipoDocumento tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the fechaObtencion
	 */
	public String getFechaObtencion() {
		return fechaObtencion;
	}

	/**
	 * @param fechaObtencion the fechaObtencion to set
	 */
	public void setFechaObtencion(String fechaObtencion) {
		this.fechaObtencion = fechaObtencion;
	}

	@Override
	public String toString() {
		return "DocumentoObtenidoDTO [tipo=" + tipo + ", fechaObtencion=" + fechaObtencion + "]";
	}

}
