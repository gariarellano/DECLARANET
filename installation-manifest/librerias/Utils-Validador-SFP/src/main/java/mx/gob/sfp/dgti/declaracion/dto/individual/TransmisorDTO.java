/**
 * @(#)TransmisorDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoPersona;

/**
 * DTO generico para los transmisores
 * @author Miriam Sánchez Sánchez programador07
 * @since 24/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class TransmisorDTO extends PersonaDTO{

	/**
	 * Relacion con el titular
	 */
	private CatalogoDTO relacionConTitular;

	/**
	 * Otro relacion con titular
	 */
	private String relacionConTitularOtro;

	
	/**
     * Constructor
     */
    public TransmisorDTO(){ };

	public TransmisorDTO(String id, String idPosicionVista, EnumTipoPersona tipoPersona, PersonaFisicaDTO personaFisica,
						 PersonaMoralDTO personaMoral, CatalogoDTO relacionConTitular, String relacionConTitularOtro) {
		super(id, idPosicionVista, tipoPersona, personaFisica, personaMoral);
		this.relacionConTitular = relacionConTitular;
		this.relacionConTitularOtro = relacionConTitularOtro;
	}

	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public TransmisorDTO(JsonObject json) {
    	TransmisorDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        TransmisorDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the relacionConTitular
	 */
	public CatalogoDTO getRelacionConTitular() {
		return relacionConTitular;
	}
	/**
	 * @param relacionConTitular the relacionConTitular to set
	 */
	public void setRelacionConTitular(CatalogoDTO relacionConTitular) {
		this.relacionConTitular = relacionConTitular;
	}

	public String getRelacionConTitularOtro() {
		return relacionConTitularOtro;
	}

	public void setRelacionConTitularOtro(String relacionConTitularOtro) {
		this.relacionConTitularOtro = relacionConTitularOtro;
	}
}
