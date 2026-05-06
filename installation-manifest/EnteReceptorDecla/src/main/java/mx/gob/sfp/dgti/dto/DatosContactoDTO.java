package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true, inheritConverter = true)
public class DatosContactoDTO {

	private String nombreCompleto;
	private String correoElectronico;
	private String puesto;
	private String telefonoCelular;
	private String telefonoOficina;
	
	public DatosContactoDTO() {
		
	}
	
	public DatosContactoDTO(String nombreCompleto, String correoElectronico, String puesto, String telefonoCelular,
			String telefonoOficina) {
		super();
		this.nombreCompleto = nombreCompleto;
		this.correoElectronico = correoElectronico;
		this.puesto = puesto;
		this.telefonoCelular = telefonoCelular;
		this.telefonoOficina = telefonoOficina;
	}
	
	  /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DatosContactoDTO(JsonObject json) {
    	DatosContactoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DatosContactoDTOConverter.toJson(this, json);
        return json;
    }
	/**
	 * @return the nombreCompleto
	 */
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	/**
	 * @param nombreCompleto the nombreCompleto to set
	 */
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	/**
	 * @return the correoElectronico
	 */
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	/**
	 * @param correoElectronico the correoElectronico to set
	 */
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	/**
	 * @return the puesto
	 */
	public String getPuesto() {
		return puesto;
	}
	/**
	 * @param puesto the puesto to set
	 */
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
	/**
	 * @return the telefonoCelular
	 */
	public String getTelefonoCelular() {
		return telefonoCelular;
	}
	/**
	 * @param telefonoCelular the telefonoCelular to set
	 */
	public void setTelefonoCelular(String telefonoCelular) {
		this.telefonoCelular = telefonoCelular;
	}
	/**
	 * @return the telefonoOficina
	 */
	public String getTelefonoOficina() {
		return telefonoOficina;
	}
	/**
	 * @param telefonoOficina the telefonoOficina to set
	 */
	public void setTelefonoOficina(String telefonoOficina) {
		this.telefonoOficina = telefonoOficina;
	}
	
	
}
