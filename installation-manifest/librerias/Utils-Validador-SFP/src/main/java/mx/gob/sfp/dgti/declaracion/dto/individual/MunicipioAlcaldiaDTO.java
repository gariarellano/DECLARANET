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
public class MunicipioAlcaldiaDTO extends CatalogoDTO{

	public MunicipioAlcaldiaDTO() {}
	
	public MunicipioAlcaldiaDTO(Integer id, String valor, String clavePdn) {
		super(id, valor);
	}
	
	  /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public MunicipioAlcaldiaDTO(JsonObject json) {
    	MunicipioAlcaldiaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        MunicipioAlcaldiaDTOConverter.toJson(this, json);
        return json;
    }
}
