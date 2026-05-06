package mx.gob.sfp.dgti.declaracion.dto.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.general.EncabezadoGralDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.InstitucionReceptoraDTO;

/**
 * DTO generico usado para la cabecera de la declaracion
 *
 * @author programador04
 * @since 19/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CabeceraDTO {
	private EncabezadoGralDTO encabezado;
	private InstitucionReceptoraDTO institucionReceptora;
	private Boolean firmada;
	
	public CabeceraDTO() {}

    /**
     * @param encabezado
     * @param institucionReceptora
     * @param firmada
     */
    public CabeceraDTO(EncabezadoGralDTO encabezado , InstitucionReceptoraDTO institucionReceptora, Boolean firmada) {
            this.encabezado = encabezado;
            this.institucionReceptora = institucionReceptora;
            this.firmada = firmada;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public CabeceraDTO(JsonObject json) {
    	CabeceraDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        CabeceraDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the encabezado
	 */
	public EncabezadoGralDTO getEncabezado() {
		return encabezado;
	}

	/**
	 * @param encabezado the encabezado to set
	 */
	public void setEncabezado(EncabezadoGralDTO encabezado) {
		this.encabezado = encabezado;
	}

	/**
	 * @return the institucionReceptora
	 */
	public InstitucionReceptoraDTO getInstitucionReceptora() {
		return institucionReceptora;
	}

	/**
	 * @param institucionReceptora the institucionReceptora to set
	 */
	public void setInstitucionReceptora(InstitucionReceptoraDTO institucionReceptora) {
		this.institucionReceptora = institucionReceptora;
	}

	/**
	 * @return the firmada
	 */
	public Boolean getFirmada() {
		return firmada;
	}

	/**
	 * @param firmada the firmada to set
	 */
	public void setFirmada(Boolean firmada) {
		this.firmada = firmada;
	}
}
