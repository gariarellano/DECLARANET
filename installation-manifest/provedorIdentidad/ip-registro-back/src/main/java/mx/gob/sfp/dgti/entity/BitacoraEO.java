package mx.gob.sfp.dgti.entity;

import io.vertx.core.json.JsonObject;

/**
 * Entidad de la bitacora  
 * @author Miriam Sanchez Sanchez programador07
 * @since 02/04/2019
 */
public class BitacoraEO {
	
	private Integer idAplicacion;
	private String fecha;
	private String curp;
	private Integer accion;
	
	/**
	 * Constructor sin parámetros
	 */
	public BitacoraEO() {
		// Constructor
	}

	public BitacoraEO(JsonObject jsonObject) {
		this(
			jsonObject.getInteger("idAplicacion"),
			jsonObject.getString("fecha"),
			jsonObject.getString("curp"),
			jsonObject.getInteger("accion")
		);
	}
	
	/**
	 * Constructor con parámetros
	 * @param idAplicacion
	 * @param fecha
	 * @param curp
	 * @param accion
	 */
	public BitacoraEO(Integer idAplicacion, String fecha, String curp, Integer accion) { 
            this.idAplicacion = idAplicacion;
            this.fecha = fecha;
            this.curp = curp;
            this.accion = accion;
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
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}
	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
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
	 * @return the accion
	 */
	public Integer getAccion() {
		return accion;
	}
	/**
	 * @param accion the accion to set
	 */
	public void setAccion(Integer accion) {
		this.accion = accion;
	}
	/**
	 * Función toString 
	 * @return String cadena con las propiedades de la clase
	 */
	@Override
	public String toString() {
		return "BitacoraEO [idAplicacion=" + idAplicacion + ", fecha=" + fecha + ", curp=" + curp + ", accion=" + accion
				+ "]";
	}
	
}
