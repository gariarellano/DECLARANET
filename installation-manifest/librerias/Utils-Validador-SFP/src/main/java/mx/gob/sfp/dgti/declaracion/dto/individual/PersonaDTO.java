/**
 * @(#)PersonaDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.general.FilaDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoPersona;

/**
 * DTO generico para las personas
 * @author Miriam Sánchez Sánchez programador07
 * @since 24/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class PersonaDTO extends FilaDTO {

	/**
	 * Tipo de persona moral o fisica
	 */
	private EnumTipoPersona tipoPersona;
	
	/**
	 * Persona fisica
	 */
	private PersonaFisicaDTO personaFisica;
	
	/**
	 * Persona moral
	 */
	private PersonaMoralDTO personaMoral;

	/**
     * Constructor
     */
    public PersonaDTO(){ };

	public PersonaDTO(String id, String idPosicionVista, EnumTipoPersona tipoPersona, PersonaFisicaDTO personaFisica,
                      PersonaMoralDTO personaMoral) {
		super(id, idPosicionVista);
		this.tipoPersona = tipoPersona;
		this.personaFisica = personaFisica;
		this.personaMoral = personaMoral;
	}

	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public PersonaDTO(JsonObject json) {
    	PersonaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        PersonaDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the tipoPersona
	 */
	public EnumTipoPersona getTipoPersona() {
		return tipoPersona;
	}
	/**
	 * @param tipoPersona the tipoPersona to set
	 */
	public void setTipoPersona(EnumTipoPersona tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	/**
	 * @return the personaFisica
	 */
	public PersonaFisicaDTO getPersonaFisica() {
		return personaFisica;
	}
	/**
	 * @param personaFisica the personaFisica to set
	 */
	public void setPersonaFisica(PersonaFisicaDTO personaFisica) {
		this.personaFisica = personaFisica;
	}
	/**
	 * @return the personaMoral
	 */
	public PersonaMoralDTO getPersonaMoral() {
		return personaMoral;
	}
	/**
	 * @param personaMoral the personaMoral to set
	 */
	public void setPersonaMoral(PersonaMoralDTO personaMoral) {
		this.personaMoral = personaMoral;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("PersonaDTO{");
		sb.append("tipoPersona=").append(tipoPersona);
		sb.append(", personaFisica=").append(personaFisica);
		sb.append(", personaMoral=").append(personaMoral);
		sb.append(", super=").append(super.toString());
		sb.append('}');
		return sb.toString();
	}
}
