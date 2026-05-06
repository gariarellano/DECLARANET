package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
/**
 * 
 * @since 20/11/2019 edited 29/11/2019
 * @autor programador04
 * 
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class EntidadFederativaDTO extends CatalogoDTO{

	private MunicipioAlcaldiaDTO municipioAlcaldia;
	
	public EntidadFederativaDTO() {}
	
	public EntidadFederativaDTO(Integer id, String valor, String clavePdn,  MunicipioAlcaldiaDTO municipioAlcaldia) {
		super(id, valor);
		this.municipioAlcaldia = municipioAlcaldia;
	}


    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public EntidadFederativaDTO(JsonObject json) {
    	EntidadFederativaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        EntidadFederativaDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the municipioAlcaldia
	 */
	public MunicipioAlcaldiaDTO getMunicipioAlcaldia() {
		return municipioAlcaldia;
	}


	/**
	 * @param municipioAlcaldia the municipioAlcaldia to set
	 */
	public void setMunicipioAlcaldia(MunicipioAlcaldiaDTO municipioAlcaldia) {
		this.municipioAlcaldia = municipioAlcaldia;
	}
	
	
}
