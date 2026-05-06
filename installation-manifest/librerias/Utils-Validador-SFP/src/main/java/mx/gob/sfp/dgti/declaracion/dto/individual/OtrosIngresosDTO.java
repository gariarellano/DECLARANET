/**
 * @(#)OtrosIngresosDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico para otros ingresos
 *
 * @author Miriam Sánchez Sánchez programador07
 * @since 07/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class OtrosIngresosDTO extends RemuneracionDTO {

	/**
	 * Tipo de ingreso
	 */
	private String tipoIngreso;

	/**
	 * Constructor
	 */
	public OtrosIngresosDTO() {
	};

	public OtrosIngresosDTO(MontoMonedaDTO remuneracion, String tipoIngreso) {
		super(remuneracion);
		this.tipoIngreso = tipoIngreso;
	}

	/**
	 * Constructor desde JsonObject, utiliza convertidores generados
	 *
	 * @param json objeto en JsonObject
	 */
	public OtrosIngresosDTO(JsonObject json) {
		OtrosIngresosDTOConverter.fromJson(json, this);
	}

	/**
	 * Metodo para obtener el JsonObject, utiliza convertidores generados
	 *
	 * @return JsonObject a partir del objeto
	 */
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		OtrosIngresosDTOConverter.toJson(this, json);
		return json;
	}

	/**
	 * @return the tipoIngreso
	 */
	public String getTipoIngreso() {
		return tipoIngreso;
	}

	/**
	 * @param tipoIngreso the tipoIngreso to set
	 */
	public void setTipoIngreso(String tipoIngreso) {
		this.tipoIngreso = tipoIngreso;
	}
}
