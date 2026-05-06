/**
 * CatalogoSubInversionDTO.java Nov 21, 2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Objecto para subcatalogos
 * @author Miriam Sanchez Sanchez programador07
 * @since Nov 21, 2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class CatalogoSubInversionDTO extends CatalogoDTO {

	private CatalogoUnoDTO subTipoInversion;
	
	/**
     * Constructor
     */
    public CatalogoSubInversionDTO(){ };

	public CatalogoSubInversionDTO(CatalogoUnoDTO subTipoInversion) {
		this.subTipoInversion = subTipoInversion;
	}

	public CatalogoSubInversionDTO(Integer id, String valor, String clavePdn, CatalogoUnoDTO subTipoInversion) {
        super(id, valor);
        this.subTipoInversion = subTipoInversion;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     * @param json objeto en JsonObject
     */
    public CatalogoSubInversionDTO(JsonObject json) {
    	CatalogoSubInversionDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        CatalogoSubInversionDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the subTipoInversion
	 */
	public CatalogoUnoDTO getSubTipoInversion() {
		return subTipoInversion;
	}

	/**
	 * @param subTipoInversion the subTipoInversion to set
	 */
	public void setSubTipoInversion(CatalogoUnoDTO subTipoInversion) {
		this.subTipoInversion = subTipoInversion;
	}
}
