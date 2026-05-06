/**
 * @(#)DatosPersonalesDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.general;


import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.individual.PersonaFisicaDTO;

/**
 * DTO generico para los datos personales
 * @author Miriam Sánchez Sánchez programador07
 * @since 24/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DatosPersonalesDTO extends PersonaFisicaDTO {

	/**
	 * Curp 18 posiciones
	 */
	private String curp;

	/**
     * Constructor
     */
    public DatosPersonalesDTO(){ };

	public DatosPersonalesDTO(String nombre, String primerApellido, String segundoApellido, String rfc, String curp) {
		super(nombre, primerApellido, segundoApellido, rfc);
		this.curp = curp;
	}

	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DatosPersonalesDTO(JsonObject json) {
    	DatosPersonalesDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DatosPersonalesDTOConverter.toJson(this, json);
        return json;
    }
	
	/**
	 * @return the curp
	 */
	public String getCurp() {
		return curp;
	}

	/**
	 * @param curp the curp to set
	 */
	public void setCurp(String curp) {
		this.curp = curp;
	}
}
