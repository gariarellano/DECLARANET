/**
 * @(#)MontoMonedaDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico para el objeto de valor de adquisicion
 * @author Miriam Sánchez Sánchez programador07
 * @since 24/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class MontoMonedaDTO {

	/**
	 * Catalogo de moneda
	 */
	private CatalogoDTO moneda;
	
	/**
	 * Monto 
	 */
	private Long monto;
	
	/**
     * Constructor
     */
    public MontoMonedaDTO(){ };

    public MontoMonedaDTO(CatalogoDTO moneda, Long monto) {
    	this.moneda = moneda;
        this.monto = monto;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public MontoMonedaDTO(JsonObject json) {
    	MontoMonedaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        MontoMonedaDTOConverter.toJson(this, json);
        return json;
    }
	
	/**
	 * @return the tipoMoneda
	 */
	public CatalogoDTO getMoneda() {
		return moneda;
	}
	/**
	 * @param tipoMoneda the tipoMoneda to set
	 */
	public void setMoneda(CatalogoDTO moneda) {
		this.moneda = moneda;
	}
	/**
	 * @return the monto
	 */
	public Long getMonto() {
		return monto;
	}
	/**
	 * @param monto the monto to set
	 */
	public void setMonto(Long monto) {
		this.monto = monto;
	}
	
	
}
