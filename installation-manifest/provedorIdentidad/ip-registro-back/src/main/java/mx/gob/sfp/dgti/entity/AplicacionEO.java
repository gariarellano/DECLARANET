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
	
	/**
	 * Constructor sin parámetros
	 */
	public AplicacionEO() {
		//Constructor AplicacionEO 
	}

	/**
	 * Constructor con parámetros
	 * @param idAplicacion
	 * @param nombre
	 * @param secretKey
	 * @param url
	 * @param dominio
	 */
	public AplicacionEO(Integer idAplicacion, String nombre, String secretKey, String url, String dominio) {
		this.idAplicacion = idAplicacion;
		this.nombre = nombre;
		this.secretKey = secretKey;
		this.url = url;
		this.dominio = dominio;
	}

	public AplicacionEO(JsonObject jsonObject) {
		this(
				jsonObject.getInteger("idAplicacion"),
				jsonObject.getString("nombre"),
				jsonObject.getString("secretKey"),
				jsonObject.getString("url"),
				jsonObject.getString("dominio")
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
	 * toString 
	 * @return String cadena con las propiedades de la clase
	 */
	@Override
	public String toString() {
		return "AplicacionEO [idAplicacion=" + idAplicacion + ", nombre=" + nombre + ", secretKey=" + secretKey
				+ ", url=" + url + ", dominio=" + dominio + "]";
	}
	
}
