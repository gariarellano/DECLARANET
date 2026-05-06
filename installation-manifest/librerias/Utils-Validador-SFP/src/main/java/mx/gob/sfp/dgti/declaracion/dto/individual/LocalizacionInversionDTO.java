/**
 * @LocalizacionInversionDTO.java Nov 15, 2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Clase de localización de las inversiones del declarante
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 15, 2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class LocalizacionInversionDTO {
	
	/**
	 * Pais donde se localiza la inversion
	 */
	private CatalogoDTO pais;
	
	/**
	 * Nombre de la institucion o razon social donde se encuentran registradas las inversiones cuentas o valores
	 */
	private PersonaMoralDTO institucionRazonSocial;

	
	/**
     * Constructor
     */
    public LocalizacionInversionDTO(){ };
    
    public LocalizacionInversionDTO(CatalogoDTO pais, PersonaMoralDTO institucionRazonSocial) {
    	this.institucionRazonSocial = institucionRazonSocial;
    	this.pais = pais;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public LocalizacionInversionDTO(JsonObject json) {
    	LocalizacionInversionDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        LocalizacionInversionDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the pais
	 */
	public CatalogoDTO getPais() {
		return pais;
	}

	/**
	 * @param pais the pais to set
	 */
	public void setPais(CatalogoDTO pais) {
		this.pais = pais;
	}

	/**
	 * @return the institucionRazonSocial
	 */
	public PersonaMoralDTO getInstitucionRazonSocial() {
		return institucionRazonSocial;
	}

	/**
	 * @param institucionRazonSocial the institucionRazonSocial to set
	 */
	public void setInstitucionRazonSocial(PersonaMoralDTO institucionRazonSocial) {
		this.institucionRazonSocial = institucionRazonSocial;
	}
	
}
