package mx.gob.sfp.dgti.entity;

import java.util.Date;

import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.helper.ValidacionHelper;

/**
 * Entidad de vinculacion de un usuario con una aplicacion
 * @author  Miriam Sanchez Sanchez programador07
 * @since 25/03/2019
 *
 */
public class VinculacionUAEO {
	
	private Integer idVinculacion;
	private Integer idUsuario;
	private Integer idAplicacion;
	private Date fechaVinculacion;
	private Date fechaDesvinculacion;
	private String activo;
	
	public VinculacionUAEO(JsonObject jsonObject) {
		this(
			jsonObject.getInteger("idVinculacion"),
			jsonObject.getInteger("idUsuario"),
			jsonObject.getInteger("idAplicacion"),
			ValidacionHelper.convertStringToDate(jsonObject.getString("fechaVinculacion")),
			ValidacionHelper.convertStringToDate(jsonObject.getString("fechaDesvinculacion")),
			jsonObject.getString("activo")
		);
	}
	
	/**
	 * Constructor con parámetros
	 * @param idVinculacion
	 * @param idUsuario
	 * @param idAplicacion
	 * @param fechaVinculacion
	 * @param fechaDesvinculacion
	 * @param activo
	 */
	public VinculacionUAEO(Integer idVinculacion, Integer idUsuario, Integer idAplicacion, Date fechaVinculacion, Date fechaDesvinculacion, String activo) {
	        this.idUsuario = idUsuario;
	        this.idAplicacion = idAplicacion;
	        this.idVinculacion = idVinculacion;
	        this.fechaVinculacion = fechaVinculacion;
	        this.fechaDesvinculacion = fechaDesvinculacion;
	        this.activo = activo;
	}

	/**
	 * @return the idVinculacion
	 */
	public Integer getIdVinculacion() {
		return idVinculacion;
	}

	/**
	 * @param idVinculacion the idVinculacion to set
	 */
	public void setIdVinculacion(Integer idVinculacion) {
		this.idVinculacion = idVinculacion;
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
	 * @return the fechaVinculacion
	 */
	public Date getFechaVinculacion() {
		return fechaVinculacion;
	}

	/**
	 * @param fechaVinculacion the fechaVinculacion to set
	 */
	public void setFechaVinculacion(Date fechaVinculacion) {
		this.fechaVinculacion = fechaVinculacion;
	}

	/**
	 * @return the fechaDesvinculacion
	 */
	public Date getFechaDesvinculacion() {
		return fechaDesvinculacion;
	}

	/**
	 * @param fechaDesvinculacion the fechaDesvinculacion to set
	 */
	public void setFechaDesvinculacion(Date fechaDesvinculacion) {
		this.fechaDesvinculacion = fechaDesvinculacion;
	}

	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	/**
	 * Función toString cadena con las propiedades de la clase
	 */
	@Override
	public String toString() {
		return "VinculacionUAEO [idVinculacion=" + idVinculacion + ", idusuario=" + idUsuario + ", idAplicacion="
				+ idAplicacion + ", fechaVinculacion=" + fechaVinculacion + ", fechaDesvinculacion="
				+ fechaDesvinculacion + ", activo=" + activo + "]";
	}

}
