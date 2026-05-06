package mx.gob.sfp.dgti.entity;

import java.util.Date;

import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.util.ValidacionHelper;

/**
 * Entidad de la informacion de los usuarios
 * 
 * @author Miriam Sanchez Sanchez programador07 
 * @since 07/02/2019
 */
public class UsuarioEO {

	private Integer idUsuario;
	private Integer idAplicacion;
	private String login;
	private String pwdEnc;
	private String nombre;
	private String primerApellido;
	private String segundoApellido;
	private String curp;
	private String rfc;
	private String homoclave;
	private String numCelular;
	private String email;
	private String emailAlt;
	private Integer validadoRenapo;
	private Date fechaRegistro;
	private Integer activo;

	/**
	 * Constructor sin parámetros
	 */
	public UsuarioEO() {
		//Constructor UsuarioEO 
	}

	/**
	 * Constructor con parámetros
	 * @param idUsuario id del usuario
	 * @param login del usuario
	 * @param pwdEnc password encriptada del usuario
	 * @param nombre del usuario
	 * @param primerApellido apellido paterno del usuario
	 * @param segundoApellido apellido materno del usuario 
	 * @param curp del usuario
	 * @param rfc del usuario
	 * @param homoclave del rfc del usuario
	 * @param numCelular del usuario
	 * @param email del usuario
	 * @param emailAlt email alterno del usuario
	 * @param validadoRenapo estatus de la validación con Renapo 
	 * @param fechaRegistro fecha del registro del usuario
	 * @param activo si se encuentra activo el usuario
	 */
	public UsuarioEO(Integer idUsuario, String login, String pwdEnc, String nombre, String primerApellido, 
					String segundoApellido, String curp, String rfc, String homoclave, String numCelular, String email, 
					String emailAlt, Integer validadoRenapo, Date fechaRegistro, Integer activo) {
            this.idUsuario = idUsuario;
            this.login = login;
            this.pwdEnc = pwdEnc;
            this.nombre = nombre;
            this.primerApellido = primerApellido;
            this.segundoApellido = segundoApellido;
            this.curp = curp;
            this.rfc = rfc;
            this.homoclave = homoclave;
            this.numCelular = numCelular;
            this.email = email;
            this.emailAlt = emailAlt;
            this.validadoRenapo = validadoRenapo;
            this.fechaRegistro = fechaRegistro;
            this.activo = activo;
    }
	
	public UsuarioEO(Integer idUsuario, String email, String emailAlt) {
		this.idUsuario = idUsuario;
		this.email = email;
		this.emailAlt = emailAlt;
	}

	public UsuarioEO(JsonObject jsonObject) {
		this(
			jsonObject.getInteger("idUsuario"),
			jsonObject.getString("login"),
			jsonObject.getString("pwdEnc"),
			jsonObject.getString("nombre"),
			jsonObject.getString("primerApellido"),
			jsonObject.getString("segundoApellido"),
			jsonObject.getString("curp"),
			jsonObject.getString("rfc"),
			jsonObject.getString("homoclave"),
			jsonObject.getString("numCelular"),
			jsonObject.getString("email"),
			jsonObject.getString("emailAlt"),
			jsonObject.getInteger("validadoRenapo"),
			ValidacionHelper.convertStringToDate(jsonObject.getString("fechaRegistro")),
			jsonObject.getInteger("activo")
		);
	}
	
	/**
	 * @return the idUsuario
	 */
	public Integer getIdUsuario() {
		return idUsuario;
	}

	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**
	 * @return the idaAplicacion
	 */
	public Integer getIdAplicacion() {
		return idAplicacion;
	}

	/**
	 * @param idaAplicacion the idaAplicacion to set
	 */
	public void setIdAplicacion(Integer idAplicacion) {
		this.idAplicacion = idAplicacion;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the pwdEnc
	 */
	public String getPwdEnc() {
		return pwdEnc;
	}

	/**
	 * @param pwdEnc the pwdEnc to set
	 */
	public void setPwdEnc(String pwdEnc) {
		this.pwdEnc = pwdEnc;
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

	/**
	 * @return the homoclave
	 */
	public String getHomoclave() {
		return homoclave;
	}

	/**
	 * @param homoclave the homoclave to set
	 */
	public void setHomoclave(String homoclave) {
		this.homoclave = homoclave;
	}

	/**
	 * @return the numCelular
	 */
	public String getNumCelular() {
		return numCelular;
	}

	/**
	 * @param numCelular the numCelular to set
	 */
	public void setNumCelular(String numCelular) {
		this.numCelular = numCelular;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the emailAlt
	 */
	public String getEmailAlt() {
		return emailAlt;
	}

	/**
	 * @param emailAlt the emailAlt to set
	 */
	public void setEmailAlt(String emailAlt) {
		this.emailAlt = emailAlt;
	}

	/**
	 * @return the validadoRenapo
	 */
	public Integer getValidadoRenapo() {
		return validadoRenapo;
	}

	/**
	 * @param validadoRenapo the validadoRenapo to set
	 */
	public void setValidadoRenapo(Integer validadoRenapo) {
		this.validadoRenapo = validadoRenapo;
	}

	/**
	 * @return the fechaRegistro
	 */
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	/**
	 * @param fechaRegistro the fechaRegistro to set
	 */
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	/**
	 * @return the activo
	 */
	public Integer getActivo() {
		return activo;
	}
	/**
	 * @param activo the activo to set
	 */
	public void setActivo(Integer activo) {
		this.activo = activo;
	}

	/**
	 * toString 
	 * @return String cadena con las propiedades de la clase
	 */
	@Override
	public String toString() {
		return "UsuarioEO [idUsuario=" + idUsuario + ", idAplicacion=" + idAplicacion + ", login=" + login + ", pwdEnc="
				+ pwdEnc + ", nombre=" + nombre + ", primerApellido=" + primerApellido + ", segundoApellido="
				+ segundoApellido + ", curp=" + curp + ", rfc=" + rfc + ", homoclave=" + homoclave + ", numCelular="
				+ numCelular + ", email=" + email + ", emailAlt=" + emailAlt + ", validadoRenapo=" + validadoRenapo
				+ ", fechaRegistro=" + fechaRegistro + "]";
	}

}
