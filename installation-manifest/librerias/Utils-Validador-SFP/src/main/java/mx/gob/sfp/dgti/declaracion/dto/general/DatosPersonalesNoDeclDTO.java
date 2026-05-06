/**
 * @(#)DatosPersonalesNoDeclDTO.java 02/11/2019
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
 * DTO generico para los datos personales que incluyen fecha de nacimiento
 * @author pavel.martinez
 * @since 02/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DatosPersonalesNoDeclDTO extends PersonaFisicaDTO {

	private String fechaNacimiento;

	/**
     * Constructor
     */
    public DatosPersonalesNoDeclDTO(){ };

	public DatosPersonalesNoDeclDTO(String nombre, String primerApellido, String segundoApellido, String rfc,
									String fechaNacimiento) {
		super(nombre, primerApellido, segundoApellido, rfc);
		this.fechaNacimiento = fechaNacimiento;
	}

	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DatosPersonalesNoDeclDTO(JsonObject json) {
    	DatosPersonalesNoDeclDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DatosPersonalesNoDeclDTOConverter.toJson(this, json);
        return json;
    }

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
}
