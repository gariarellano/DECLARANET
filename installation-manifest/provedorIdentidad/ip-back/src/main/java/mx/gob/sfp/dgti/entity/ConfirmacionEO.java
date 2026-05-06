/**
 * 
 */
package mx.gob.sfp.dgti.entity;

import java.util.Date;

import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.helper.ValidacionHelper;

/**
 * Entidad de confirmacion para el registro de un usuario
 * @author Miriam Sanchez Sanchez programador07 
 * @since 12/04/2019
 */
public class ConfirmacionEO {
	
	private Integer idConfirmacion;
	private Integer idUsuario;
	private Date fecha;
	private String codigoToken;
	
	/**
	 * Constructor con parámetros
	 * @param idConfirmacion
	 * @param idUsuario
	 * @param fecha
	 * @param codigoToken
	 */
	public ConfirmacionEO(Integer idConfirmacion, Integer idUsuario, Date fecha, String codigoToken) {
		this.idConfirmacion = idConfirmacion;
		this.idUsuario = idUsuario;
        this.fecha = fecha;
        this.codigoToken = codigoToken;
    }

	public ConfirmacionEO(JsonObject jsonObject) {
		this(
			jsonObject.getInteger("idConfirmacion"),
			jsonObject.getInteger("idUsuario"),
			ValidacionHelper.convertStringToDate(jsonObject.getString("fecha")),
			jsonObject.getString("token")
		);
	}

	/**
	 * @return the idConfirmacion
	 */
	public Integer getIdConfirmacion() {
		return idConfirmacion;
	}

	/**
	 * @param idConfirmacion the idConfirmacion to set
	 */
	public void setIdConfirmacion(Integer idConfirmacion) {
		this.idConfirmacion = idConfirmacion;
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
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the codigoToken
	 */
	public String getCodigoToken() {
		return codigoToken;
	}

	/**
	 * @param codigoToken the codigoToken to set
	 */
	public void setCodigoToken(String codigoToken) {
		this.codigoToken = codigoToken;
	}
	
}
