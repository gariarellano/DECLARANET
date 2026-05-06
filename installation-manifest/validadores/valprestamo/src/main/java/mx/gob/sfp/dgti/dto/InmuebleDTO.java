package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.general.DomicilioDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoDTO;

/**
 * DTO para los datos del empleo o cargo comisión
 *
 * @author programador04
 * @since 10/12/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class InmuebleDTO {
	
	private CatalogoDTO tipoInmueble;
	private String tipoInmuebleOtro;
	private DomicilioDTO domicilio;
	
	public InmuebleDTO() {}
	
	/**
	 * @param tipoInmueble
	 * @param domicilio
	 */
	public InmuebleDTO(CatalogoDTO tipoInmueble, String tipoInmuebleOtro,DomicilioDTO domicilio) {
		super();
		this.tipoInmueble = tipoInmueble;
		this.domicilio = domicilio;
		this.tipoInmuebleOtro = tipoInmuebleOtro;
	}

	/**
	 * Constructor desde JsonObject, utiliza convertidores generados
	 *
	 * @param json: objeto en JsonObject
	 */
	public InmuebleDTO(JsonObject json) {
		InmuebleDTOConverter.fromJson(json, this);
	}

	/**
	 * Metodo para obtener el JsonObject, utiliza convertidores generados
	 *
	 * @return JsonObject a partir del objeto
	 */
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		InmuebleDTOConverter.toJson(this, json);
		return json;
	}


	/**
	 * @return the tipoInmueble
	 */
	public CatalogoDTO getTipoInmueble() {
		return tipoInmueble;
	}
	/**
	 * @param tipoInmueble the tipoInmueble to set
	 */
	public void setTipoInmueble(CatalogoDTO tipoInmueble) {
		this.tipoInmueble = tipoInmueble;
	}
	/**
	 * @return the domicilio
	 */
	public DomicilioDTO getDomicilio() {
		return domicilio;
	}
	/**
	 * @param domicilio the domicilio to set
	 */
	public void setDomicilio(DomicilioDTO domicilio) {
		this.domicilio = domicilio;
	}

	/**
	 * @return the tipoInmuebleOtro
	 */
	public String getTipoInmuebleOtro() {
		return tipoInmuebleOtro;
	}

	/**
	 * @param tipoInmuebleOtro the tipoInmuebleOtro to set
	 */
	public void setTipoInmuebleOtro(String tipoInmuebleOtro) {
		this.tipoInmuebleOtro = tipoInmuebleOtro;
	}
	
	
	
	
	
}
