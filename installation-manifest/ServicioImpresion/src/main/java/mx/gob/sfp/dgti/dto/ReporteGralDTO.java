/**
 * 
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author programador09@sfp.gob.mx
 *
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ReporteGralDTO {

	private String nombre;
	private String primerApellido;
	private String segundoApellido;
	private String rfc;
	
	/**
	 * 
	 */
	public ReporteGralDTO() {
		// Empty constructor   
	}
	/**
	 * ;
	 */
	
	/**
	 * @param nombre
	 * @param primerApellido
	 * @param segundoApellido
	 * @param rfc
	 */
	public ReporteGralDTO(String nombre, String primerApellido, String segundoApellido, String rfc) {
		this.nombre = nombre;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.rfc = rfc;
	}

	/**
	 * Constructor desde JsonObject, utiliza convertidores generados
	 * 
	 * @param json objeto en JsonObject
	 */
	public ReporteGralDTO(JsonObject json) {
		ReporteGralDTOConverter.fromJson(json, this);
	}

	/**
	 * Metodo para obtener el JsonObject, utiliza convertidores generados
	 * 
	 * @return JsonObject a partir del objeto
	 */
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		ReporteGralDTOConverter.toJson(this, json);
		return json;
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
	 * @return the primerApellido
	 */
	public String getPrimerApellido() {
		return primerApellido;
	}

	/**
	 * @param primerApellido the primerApellido to set
	 */
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	/**
	 * @return the segundoApellido
	 */
	public String getSegundoApellido() {
		return segundoApellido;
	}

	/**
	 * @param segundoApellido the segundoApellido to set
	 */
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	/**
	 * @return the rfc
	 */
	public String getRfc() {
		return rfc;
	}

	/**
	 * @param rfc the rfc to set
	 */
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	@Override
	public String toString() {
		return "ReporteGralDTO [nombre=" + nombre + ", primerApellido=" + primerApellido + ", segundoApellido="
				+ segundoApellido + ", rfc=" + rfc + "]";
	}

}
