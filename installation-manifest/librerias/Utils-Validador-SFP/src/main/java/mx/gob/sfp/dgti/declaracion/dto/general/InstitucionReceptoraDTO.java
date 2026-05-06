package mx.gob.sfp.dgti.declaracion.dto.general;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico usado para la institución receptora
 *
 * @author programador04
 * @since 20/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class InstitucionReceptoraDTO {
	private String _id;
	private String nombre;
	private EnteReceptorDTO ente;
	private Integer collName;
	
	public InstitucionReceptoraDTO() {}
	
    /**
     * @param id    
     * @param nombre
     * @param ente
     * @param collName
     */
    public InstitucionReceptoraDTO(String _id, String nombre, EnteReceptorDTO ente,Integer collName) {
            this._id = _id;
            this.nombre = nombre;
            this.ente = ente;
            this.collName = collName;
    }
        
    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public InstitucionReceptoraDTO(JsonObject json) {
    	InstitucionReceptoraDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        InstitucionReceptoraDTOConverter.toJson(this, json);
        return json;
    }

   

    /**
	 * @return the _id
	 */
	public String get_id() {
		return _id;
	}

	/**
	 * @param _id the _id to set
	 */
	public void set_id(String _id) {
		this._id = _id;
	}

	/**
     * @return the nombre
     */
    public String getNombre() {
            return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
            this.nombre = nombre;
    }


    /**
     * @return the collName
     */
    public Integer getCollName() {
            return collName;
    }

    /**
     * @param collName the collName to set
     */
    public void setCollName(Integer collName) {
            this.collName = collName;
    }

    public EnteReceptorDTO getEnte() {
        return ente;
    }

    public void setEnte(EnteReceptorDTO ente) {
        this.ente = ente;
    }
}
