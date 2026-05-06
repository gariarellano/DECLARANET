package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico para catalogos que incluyen fk y valorUno
 * @author programador04
 * @since 14/01/2020
 */


@DataObject(generateConverter = true, inheritConverter = true)
public class CatalogoUnoFkDTO  extends CatalogoFkDTO {

	private String valorUno;

	public CatalogoUnoFkDTO() {
		
	}
	
	public CatalogoUnoFkDTO(Integer id, String valor, Integer fk, String valorUno) {
        super(id, valor, fk);
        this.valorUno = valorUno;
    }
	
	
	  /**
     * Constructor desde JsonObject, utiliza convertidores generados
     * @param json objeto en JsonObject
     */
    public CatalogoUnoFkDTO(JsonObject json) {
    	CatalogoUnoFkDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        CatalogoUnoFkDTOConverter.toJson(this, json);
        return json;
    }
	/**
	 * @return the valorUno
	 */
	public String getValorUno() {
		return valorUno;
	}

	/**
	 * @param valorUno the valorUno to set
	 */
	public void setValorUno(String valorUno) {
		this.valorUno = valorUno;
	}
	
	
}
