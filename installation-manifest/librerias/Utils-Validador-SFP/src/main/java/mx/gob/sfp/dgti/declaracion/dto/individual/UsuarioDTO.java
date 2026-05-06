package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico usado para el encabezado de la declaracion
 *
 * @author programador04
 * @since 19/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class UsuarioDTO {
	 private Integer idUsuario;
     private String curp;
     private Integer id_movimiento;
     private Integer idSp;
     
     public UsuarioDTO() {}
     
	/**
	 * @param idUsuario
	 * @param curp
	 * @param id_movimiento
	 */
	public UsuarioDTO(Integer idUsuario, String curp, Integer id_movimiento,Integer idSp) {
		super();
		this.idUsuario = idUsuario;
		this.curp = curp;
		this.id_movimiento = id_movimiento;
		this.idSp = idSp;
	}

	 /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public UsuarioDTO(JsonObject json) {
    	UsuarioDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        UsuarioDTOConverter.toJson(this, json);
        return json;
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
	 * @return the id_movimiento
	 */
	public Integer getId_movimiento() {
		return id_movimiento;
	}
	/**
	 * @param id_movimiento the id_movimiento to set
	 */
	public void setId_movimiento(Integer id_movimiento) {
		this.id_movimiento = id_movimiento;
	}

	/**
	 * @return the idPs
	 */
	public Integer getIdSp() {
		return idSp;
	}

	/**
	 * @param idPs the idPs to set
	 */
	public void setIdSp(Integer idSp) {
		this.idSp = idSp;
	} 
     
}
