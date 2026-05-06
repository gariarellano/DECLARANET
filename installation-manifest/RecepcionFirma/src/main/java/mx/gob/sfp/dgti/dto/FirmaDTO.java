/**
 * @FirmaDTO.java Dec 18, 2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author Miriam Sanchez Sanchez programador07
 * @since Dec 18, 2019
 */

@DataObject(generateConverter = true, inheritConverter = true)
@JsonInclude(Include.NON_NULL)
public class FirmaDTO extends ParametrosDTO {
	
	private String fechaRecepcion;
	private String noComprobante;
	private String fechaId;
	private BitacoraDTO firmado;
	private AcuseDTO acuse;
	private DigestDTO digestion;
	
	  /**
     * Constructor
     */
    public FirmaDTO(){ };
    
    public FirmaDTO(String fechaRecepcion, String noComprobante, String fechaId, 
    		BitacoraDTO firmado, AcuseDTO acuse, DigestDTO digestion){ 
    	this.fechaRecepcion = fechaRecepcion;
    	this.noComprobante = noComprobante;
    	this.fechaId = fechaId;
    	this.firmado = firmado;
    	this.acuse = acuse;
    	this.digestion = digestion;
    };

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public FirmaDTO(JsonObject json) {
    	FirmaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        FirmaDTOConverter.toJson(this, json);
        return json;
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
	/**
	 * @return the noComprobante
	 */
	public String getNoComprobante() {
		return noComprobante;
	}
	/**
	 * @param noComprobante the noComprobante to set
	 */
	public void setNoComprobante(String noComprobante) {
		this.noComprobante = noComprobante;
	}
	/**
	 * @return the fechaId
	 */
	public String getFechaId() {
		return fechaId;
	}
	/**
	 * @param fechaId the fechaId to set
	 */
	public void setFechaId(String fechaId) {
		this.fechaId = fechaId;
	}
	/**
	 * @return the firmado
	 */
	public BitacoraDTO getFirmado() {
		return firmado;
	}
	/**
	 * @param firmado the firmado to set
	 */
	public void setFirmado(BitacoraDTO firmado) {
		this.firmado = firmado;
	}
	/**
	 * @return the acuse
	 */
	public AcuseDTO getAcuse() {
		return acuse;
	}
	/**
	 * @param acuse the acuse to set
	 */
	public void setAcuse(AcuseDTO acuse) {
		this.acuse = acuse;
	}
	/**
	 * @return the digestion
	 */
	public DigestDTO getDigestion() {
		return digestion;
	}
	/**
	 * @param digestion the digestion to set
	 */
	public void setDigestion(DigestDTO digestion) {
		this.digestion = digestion;
	}

	@Override
	public String toString() {
		return "FirmaDTO [fechaRecepcion=" + fechaRecepcion + ", noComprobante=" + noComprobante + ", fechaId="
				+ fechaId + ", firmado=" + firmado + ", acuse=" + acuse + ", digestion=" + digestion + "]";
	}
	
	
}
