package mx.gob.sfp.dgti.dto;

/**
 * Entidad de respuesta a las peticiones
 * @author Miriam Sánchez Sánchez programador07 
 * @since 07/02/2019
 */
public class RespuestaDTO {

	private String error;
	private String url;
	private Integer codigo;
	private Integer obligatorio;
	private Integer estatus;
	private String aplicacion;
	private String transaccion;
	private String curp;
	private String correo;
	private String token;
	
	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
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
	 * @return the codigo
	 */
	public Integer getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return the aplicacion
	 */
	public String getAplicacion() {
		return aplicacion;
	}
	/**
	 * @param aplicacion the aplicacion to set
	 */
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}
	/**
	 * @return the transaccion
	 */
	public String getTransaccion() {
		return transaccion;
	}
	/**
	 * @param transaccion the transaccion to set
	 */
	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}
	/**
	 * @return the curp
	 */
	public String getCurp() {
		return curp;
	}
	/**
	 * @param curp the curp to set
	 */
	public void setCurp(String curp) {
		this.curp = curp;
	}
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	/**
	 * @return the obligatorio
	 */
	public Integer getObligatorio() {
		return obligatorio;
	}
	/**
	 * @param obligatorio the obligatorio to set
	 */
	public void setObligatorio(Integer obligatorio) {
		this.obligatorio = obligatorio;
	}
	/**
	 * @return the estatus
	 */
	public Integer getEstatus() {
		return estatus;
	}
	/**
	 * @param estatus the estatus to set
	 */
	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}
	/**
	 * toString
	 * @return String cadena de las propiedades
	 */
	@Override
	public String toString() {
		return "RespuestaDTO [error=" + error + ", url=" + url + ", codigo=" + codigo + ", obligatorio=" + obligatorio
				+ ", estatus=" + estatus + ", aplicacion=" + aplicacion + ", transaccion=" + transaccion + ", curp="
				+ curp + ", correo=" + correo + ", token=" + token + "]";
	}
	
}
