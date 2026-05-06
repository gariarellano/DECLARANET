/**
 * @(#)ActividadFinancieraDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico para actividad financiera
 *
 * @author Miriam Sánchez Sánchez programador07
 * @since 31/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ActividadFinancieraDTO extends RemuneracionDTO {

	/**
	 * Tipo de instrumento que genero el rendimiento o ganancia
	 */
	private CatalogoDTO tipoInstrumento;

	/**
	 * Otro tipo instrumento
	 */
	private String tipoInstrumentoOtro;
	
	/**
     * Constructor
     */
    public ActividadFinancieraDTO(){ };

	public ActividadFinancieraDTO(MontoMonedaDTO remuneracion, CatalogoDTO tipoInstrumento, String tipoInstrumentoOtro) {
		super(remuneracion);
		this.tipoInstrumento = tipoInstrumento;
		this.tipoInstrumentoOtro = tipoInstrumentoOtro;
	}

	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public ActividadFinancieraDTO(JsonObject json) {
    	ActividadFinancieraDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ActividadFinancieraDTOConverter.toJson(this, json);
        return json;
    }
	
	/**
	 * @return the tipoInstrumento
	 */
	public CatalogoDTO getTipoInstrumento() {
		return tipoInstrumento;
	}
	/**
	 * @param tipoInstrumento the tipoInstrumento to set
	 */
	public void setTipoInstrumento(CatalogoDTO tipoInstrumento) {
		this.tipoInstrumento = tipoInstrumento;
	}

	public String getTipoInstrumentoOtro() {
		return tipoInstrumentoOtro;
	}

	public void setTipoInstrumentoOtro(String tipoInstrumentoOtro) {
		this.tipoInstrumentoOtro = tipoInstrumentoOtro;
	}
}
