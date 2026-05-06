/**
 * 
 */
package mx.gob.sfp.dgti.entity;

import io.vertx.core.json.JsonObject;

/**
 * Entidad de aplicacion cliente
 * @author Miriam Sanchez Sanchez programador07
 * @since 11/03/2019
 */
public class AplicacionEO {
	
	private Integer idAplicacion;
	private String nombre;
	private String secretKey;
	private String url;
	private String dominio;
	private Integer obligatorioCurpValida;
	private Integer minutos;
	private String iss;
	
	/**
	 * Constructor con parámetros
	 * @param idAplicacion
	 * @param nombre
	 * @param secretKey
	 * @param url
	 * @param dominio
	 * @param obligatorioCurpValida
	 */
	public AplicacionEO(Integer idAplicacion, String nombre, String secretKey, String url, String dominio, 
			Integer obligatorioCurpValida, String iss, Integer minutos) {
		this.idAplicacion = idAplicacion;
		this.nombre = nombre;
		this.secretKey = secretKey;
		this.url = url;
		this.dominio = dominio;
		this.obligatorioCurpValida = obligatorioCurpValida;
		this.iss = iss;
		this.minutos = minutos;
	}

	public AplicacionEO(JsonObject jsonObject) {
		this(
				jsonObject.getInteger("idAplicacion"),
				jsonObject.getString("nombre"),
				jsonObject.getString("secretKey"),
				jsonObject.getString("url"),
				jsonObject.getString("dominio"),
				jsonObject.getInteger("obligatorioCurpValida"),
				jsonObject.getString("iss"),
				jsonObject.getInteger("minutos")
		);
	}
	
	/**
	 * @return the idAplicacion
	 */
	public Integer getIdAplicacion() {
		return idAplicacion;
	}

	/**
	 * @param idAplicacion the idAplicacion to set
	 */
	public void setIdAplicacion(Integer idAplicacion) {
		this.idAplicacion = idAplicacion;
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
	 * @return the secretKey
	 */
	public String getSecretKey() {
		return secretKey;
	}

	/**
	 * @param secretKey the secretKey to set
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the dominio
	 */
	public String getDominio() {
		return dominio;
	}

	/**
	 * @param dominio the dominio to set
	 */
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}

	/**
	 * @return the obligatorioCurpValida
	 */
	public Integer getObligatorioCurpValida() {
		return obligatorioCurpValida;
	}

	/**
	 * @param obligatorioCurpValida the obligatorioCurpValida to set
	 */
	public void setObligatorioCurpValida(Integer obligatorioCurpValida) {
		this.obligatorioCurpValida = obligatorioCurpValida;
	}

	/**
	 * @return the minutos
	 */
	public Integer getMinutos() {
		return minutos;
	}

	/**
	 * @param minutos the minutos to set
	 */
	public void setMinutos(Integer minutos) {
		this.minutos = minutos;
	}

	/**
	 * @return the iss
	 */
	public String getIss() {
		return iss;
	}

	/**
	 * @param iss the iss to set
	 */
	public void setIss(String iss) {
		this.iss = iss;
	}

	/**
	 * toString 
	 * @return String cadena con las propiedades de la clase
	 */
	@Override
	public String toString() {
		return "AplicacionEO [idAplicacion=" + idAplicacion + ", nombre=" + nombre + ", secretKey=" + secretKey
				+ ", url=" + url + ", dominio=" + dominio + ", obligatorioCurpValida=" + obligatorioCurpValida
				+ ", minutos=" + minutos + ", iss=" + iss + "]";
	}

}
