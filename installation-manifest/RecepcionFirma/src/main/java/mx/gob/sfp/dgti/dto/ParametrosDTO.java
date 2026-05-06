package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.util.EnumTipoFirma;

/**
 * Entidad de respuesta a las peticiones
 * @author Miriam Sánchez Sánchez programador07 
 * @since 15/12/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ParametrosDTO {
	
	private EnumTipoFirma tipoFirma;
	private String numeroDeclaracion;
	private Integer idUsuario; 
	private Integer collName;
	private String transaccion;
	private String rfc;
	private String token;
	private String digest;
	private Integer code;
	private String declaracion;
	private String fechaRecepcion;
	
	/**
     * Constructor
     */
    public ParametrosDTO(){ };

    /**
     * Constructor
     */
    public ParametrosDTO(String numeroDeclaracion, Integer idUsuario, Integer collName){ 
    	this.numeroDeclaracion = numeroDeclaracion;
    	this.idUsuario = idUsuario;
    	this.collName = collName;
    };
    /**
     * Constructor
     */
    public ParametrosDTO(EnumTipoFirma tipoFirma, String numeroDeclaracion, Integer idUsuario, 
    		Integer collName, String transaccion, String rfc, String token, String digest) {
        this.tipoFirma = tipoFirma;
        this.numeroDeclaracion = numeroDeclaracion;
        this.idUsuario = idUsuario;
        this.collName = collName;
        this.transaccion = transaccion;
        this.rfc = rfc;
        this.token = token;
        this.digest = digest;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public ParametrosDTO(JsonObject json) {
    	ParametrosDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ParametrosDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the numeroDeclaracion
	 */
	public String getNumeroDeclaracion() {
		return numeroDeclaracion;
	}
	/**
	 * @param numeroDeclaracion the numeroDeclaracion to set
	 */
	public void setNumeroDeclaracion(String numeroDeclaracion) {
		this.numeroDeclaracion = numeroDeclaracion;
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
	 * @return the collName
	 */
	public Integer getCollName() {
		return collName;
	}
	/**
	 * @param collName the collName to set
	 */
	public void setCollName(Integer collName) {
		this.collName = collName;
	}
	/**
	 * @return the tipoFirma
	 */
	public EnumTipoFirma getTipoFirma() {
		return tipoFirma;
	}
	/**
	 * @param tipoFirma the tipoFirma to set
	 */
	public void setTipoFirma(EnumTipoFirma tipoFirma) {
		this.tipoFirma = tipoFirma;
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

	/**
	 * @return the digest
	 */
	public String getDigest() {
		return digest;
	}

	/**
	 * @param digest the digest to set
	 */
	public void setDigest(String digest) {
		this.digest = digest;
	}

	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * @return the declaracion
	 */
	public String getDeclaracion() {
		return declaracion;
	}

	/**
	 * @param declaracion the declaracion to set
	 */
	public void setDeclaracion(String declaracion) {
		this.declaracion = declaracion;
	}

	/**
	 * @return the fechaRecepcion
	 */
	public String getFechaRecepcion() {
		return fechaRecepcion;
	}

	/**
	 * @param fechaRecepcion the fechaRecepcion to set
	 */
	public void setFechaRecepcion(String fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}
	
}
