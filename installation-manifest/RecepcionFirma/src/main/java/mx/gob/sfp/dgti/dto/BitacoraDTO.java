package mx.gob.sfp.dgti.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Entidad de bitácora para la firma electrónica
 * 
 * @author Miriam Sanchez Sanchez programador07
 * @since 28/08/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
@JsonInclude(Include.NON_NULL)
public class BitacoraDTO {

	private Integer estatus;
	private String transaccion;
	private String rfc;
	private String curp;
	private String numeroCertificado;
	private String nombreFirm;
	private String folio;
	private String respuestaValidacion;
	private String codigoSha;
	
	public BitacoraDTO() {
	}
	/**
	 * Constructor con parámetros
	 * 
	 * @param idBitacora
	 */
	public BitacoraDTO( Integer estatus, Long transaccion, String rfc, String numeroCertificado, String nombreFirm, 
			String folio, String respuestaValidacion, String codigoSha) {
		this.transaccion = String.valueOf(transaccion);
		this.rfc = rfc;
		this.numeroCertificado = numeroCertificado;
		this.nombreFirm = nombreFirm;
		this.respuestaValidacion = respuestaValidacion;
		this.codigoSha = codigoSha;
		this.estatus = estatus;
		this.folio = folio;
	}

	public BitacoraDTO(String curp, String transaccion, String numeroCertificado, String nombreFirm, 
			String folio, String respuestaValidacion, String codigoSha, Integer estatus) {
		this.transaccion = transaccion;
		this.curp = curp;
		this.numeroCertificado = numeroCertificado;
		this.nombreFirm = nombreFirm;
		this.respuestaValidacion = respuestaValidacion;
		this.codigoSha = codigoSha;
		this.estatus = estatus;
		this.folio = folio;
	}
	
	 /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public BitacoraDTO(JsonObject json) {
    	BitacoraDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        BitacoraDTOConverter.toJson(this, json);
        return json;
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
	 * @return the numeroCertificado
	 */
	public String getNumeroCertificado() {
		return numeroCertificado;
	}

	/**
	 * @param numeroCertificado the numeroCertificado to set
	 */
	public void setNumeroCertificado(String numeroCertificado) {
		this.numeroCertificado = numeroCertificado;
	}

	/**
	 * @return the nombreFirm
	 */
	public String getNombreFirm() {
		return nombreFirm;
	}

	/**
	 * @param nombreFirm the nombreFirm to set
	 */
	public void setNombreFirm(String nombreFirm) {
		this.nombreFirm = nombreFirm;
	}

	/**
	 * @return the respuestaValidacion
	 */
	public String getRespuestaValidacion() {
		return respuestaValidacion;
	}

	/**
	 * @param respuestaValidacion the respuestaValidacion to set
	 */
	public void setRespuestaValidacion(String respuestaValidacion) {
		this.respuestaValidacion = respuestaValidacion;
	}

	/**
	 * @return the codigoSha
	 */
	public String getCodigoSha() {
		return codigoSha;
	}
	/**
	 * @param codigoSha the codigoSha to set
	 */
	public void setCodigoSha(String codigoSha) {
		this.codigoSha = codigoSha;
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
	 * @return the folio
	 */
	public String getFolio() {
		return folio;
	}
	/**
	 * @param folio the folio to set
	 */
	public void setFolio(String folio) {
		this.folio = folio;
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
	@Override
	public String toString() {
		return "BitacoraDTO [estatus=" + estatus + ", transaccion=" + transaccion + ", rfc=" + rfc + ", curp=" + curp
				+ ", numeroCertificado=" + numeroCertificado + ", nombreFirm=" + nombreFirm + ", folio=" + folio
				+ ", respuestaValidacion=" + respuestaValidacion + ", codigoSha=" + codigoSha + "]";
	}
	
}
