package mx.gob.sfp.dgti.declaracion.dto.general;


import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoUnoFkDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.UsuarioDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoFormatoDeclaracion;

/**
 * DTO generico usado para el encabezado de la declaracion
 *
 * @author programador04
 * @since 19/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class EncabezadoGralDTO extends EncabezadoDTO {

    private String fechaRegistro;
    private String fechaActualizacion;
    private UsuarioDTO usuario;
    private EnumTipoFormatoDeclaracion tipoFormato;
    private Integer versionDeclaracion;
    
    public EncabezadoGralDTO() {}


	
    /**
	 * @param numeroDeclaracion
	 * @param tipoDeclaracion
	 * @param anio
	 * @param fechaEncargo
	 * @param fechaRegistro
	 * @param fechaActualizacion
	 * @param UsuarioDTO usuario
	 * @param Object versionDeclaracion
	 */
	public EncabezadoGralDTO(String numeroDeclaracion, EnumTipoDeclaracion tipoDeclaracion, Integer anio,
			String fechaEncargo, String fechaRegistro, String fechaActualizacion, UsuarioDTO usuario,
			EnumTipoFormatoDeclaracion tipoFormato, Integer versionDeclaracion, CatalogoUnoFkDTO nivelJerarquico) {
		super(numeroDeclaracion, tipoDeclaracion, anio, fechaEncargo, nivelJerarquico);
		this.fechaRegistro = fechaEncargo;
		this.fechaActualizacion = fechaActualizacion;
		this.usuario = usuario;
		this.tipoFormato = tipoFormato;
		this.versionDeclaracion = versionDeclaracion;
	}

	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public EncabezadoGralDTO(JsonObject json) {
    	EncabezadoGralDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        EncabezadoGralDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the fechaRegistro
	 */
	public String getFechaRegistro() {
		return fechaRegistro;
	}

	/**
	 * @param fechaRegistro the fechaRegistro to set
	 */
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	/**
	 * @return the fechaActualizacion
	 */
	public String getFechaActualizacion() {
		return fechaActualizacion;
	}

	/**
	 * @param fechaActualizacion the fechaActualizacion to set
	 */
	public void setFechaActualizacion(String fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	/**
	 * @return the usuario
	 */
	public UsuarioDTO getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
	
	/**
	 * @return the tipoFormato
	 */
	public EnumTipoFormatoDeclaracion getTipoFormato() {
		return tipoFormato;
	}

	/**
	 * @param tipoFormato the tipoFormato to set
	 */
	public void setTipoFormato(EnumTipoFormatoDeclaracion tipoFormato) {
		this.tipoFormato = tipoFormato;
	}

	/**
	 * @return the versionDeclaracion
	 */
	public Integer getVersionDeclaracion() {
		return versionDeclaracion;
	}

	/**
	 * @param versionDeclaracion the versionDeclaracion to set
	 */
	public void setVersionDeclaracion(Integer versionDeclaracion) {
		this.versionDeclaracion = versionDeclaracion;
	}
}
