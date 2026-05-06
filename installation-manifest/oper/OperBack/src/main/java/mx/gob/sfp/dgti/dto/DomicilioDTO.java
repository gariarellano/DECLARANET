package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true, inheritConverter = true)
public class DomicilioDTO {

	private String seccion;
	private String ubicacion;
	private String pais;
	
	private String entidadProvincia;
	
	private String municipioAlcaldia;
	
	private String ciudadLocalidad;
	
	private String calle;
	private String codigoPostal;
	private String numeroExterior;
	private String numeroInterior;
	
	private String propietario;
	
	public DomicilioDTO() {
	}
	
	/**
	 * @param seccion
	 * @param ubicacion
	 * @param pais
	 * @param entidadFederativa
	 * @param estadoProvincia
	 * @param municipioAlcaldia
	 * @param ciudadLocalidad
	 * @param coloniaLocalidad
	 * @param calle
	 * @param codigoPostal
	 * @param numeroExterior
	 * @param numeroInterior
	 * @param propietario
	 */
	public DomicilioDTO(String seccion, String ubicacion, String pais, String entidadProvincia,
			String municipioAlcaldia, String ciudadLocalidad,String calle,
			String codigoPostal, String numeroExterior, String numeroInterior, String propietario) {
		super();
		this.seccion = seccion;
		this.ubicacion = ubicacion;
		this.pais = pais;
		
		this.entidadProvincia = entidadProvincia;
		this.municipioAlcaldia = municipioAlcaldia;
		this.ciudadLocalidad = ciudadLocalidad;
		
		this.calle = calle;
		this.codigoPostal = codigoPostal;
		this.numeroExterior = numeroExterior;
		this.numeroInterior = numeroInterior;
		this.propietario = propietario;
	}
	
	public DomicilioDTO(JsonObject json) {
		DomicilioDTOConverter.fromJson(json, this);
    }
	
	public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DomicilioDTOConverter.toJson(this, json);
        return json;
    }
	public String getSeccion() {
		return seccion;
	}
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	
	public String getMunicipioAlcaldia() {
		return municipioAlcaldia;
	}
	public void setMunicipioAlcaldia(String municipioAlcaldia) {
		this.municipioAlcaldia = municipioAlcaldia;
	}
	public String getCiudadLocalidad() {
		return ciudadLocalidad;
	}
	
	public String getEntidadProvincia() {
		return entidadProvincia;
	}

	public void setEntidadProvincia(String entidadProvincia) {
		this.entidadProvincia = entidadProvincia;
	}

	public void setCiudadLocalidad(String ciudadLocalidad) {
		this.ciudadLocalidad = ciudadLocalidad;
	}
	
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getNumeroExterior() {
		return numeroExterior;
	}
	public void setNumeroExterior(String numeroExterior) {
		this.numeroExterior = numeroExterior;
	}
	public String getNumeroInterior() {
		return numeroInterior;
	}
	public void setNumeroInterior(String numeroInterior) {
		this.numeroInterior = numeroInterior;
	}

	public String getPropietario() {
		return propietario;
	}

	public void setPropietario(String propietario) {
		this.propietario = propietario;
	}
	
}
