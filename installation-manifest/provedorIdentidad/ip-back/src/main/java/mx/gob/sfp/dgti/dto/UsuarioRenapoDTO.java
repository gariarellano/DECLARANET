package mx.gob.sfp.dgti.dto;

import io.vertx.core.json.JsonObject;

/**
 * DTO modelo del servicio de RENAPO
 * @author Miriam Sánchez Sánchez programador07
 * @since 19/07/2019
 *
 */
public class UsuarioRenapoDTO {

	private String nombres;
	private String primerApellido;
	private String segundoApellido;
	private String curp;
	private String message;
	private String statusOper;
	
	/**
	 * Constructor default sin parámetros
	 */
	public UsuarioRenapoDTO() {
		//Constructor UsuarioRenapoDTO 
	}

	/**
	 * Constructor con parámetros
	 * @param nombres
	 * @param primerApellido
	 * @param segundoApellido
	 * @param curp
	 * @param message
	 * @param statusOper
	 */
	public UsuarioRenapoDTO(String nombres, String primerApellido, String segundoApellido, 
					String curp,	String message, String statusOper) {
            this.nombres = nombres;
            this.primerApellido = primerApellido;
            this.segundoApellido = segundoApellido;
            this.curp = curp;
            this.message = message;
            this.statusOper = statusOper;
    }

	public UsuarioRenapoDTO(JsonObject jsonObject) {
		this(
			jsonObject.getString("nombres"),
			jsonObject.getString("primerApellido"),
			jsonObject.getString("segundoApellido"),
			jsonObject.getString("curp"),
			jsonObject.getString("message"),
			jsonObject.getString("statusOper")
		);
	}
	
	/**
	 * @return the nombres
	 */
	public String getNombres() {
		return nombres;
	}
	/**
	 * @param nombres the nombres to set
	 */
	public void setNombres(String nombres) {
		this.nombres = nombres;
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
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the statusOper
	 */
	public String getStatusOper() {
		return statusOper;
	}
	/**
	 * @param statusOper the statusOper to set
	 */
	public void setStatusOper(String statusOper) {
		this.statusOper = statusOper;
	}
	
	
}
