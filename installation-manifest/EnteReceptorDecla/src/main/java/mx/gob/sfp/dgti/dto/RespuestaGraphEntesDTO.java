package mx.gob.sfp.dgti.dto;

import java.util.List;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;
import mx.gob.sfp.dgti.validador.dto.out.PropiedadesErrorDTO;

@DataObject(generateConverter = true, inheritConverter = true)
public class RespuestaGraphEntesDTO extends ModuloDTO{
	
	private EnteReceptorDeclaDTO ente;

	public RespuestaGraphEntesDTO() {
		
	}
	
	public RespuestaGraphEntesDTO(ModuloDTO modulo, EnteReceptorDeclaDTO ente) {
		setModulo(modulo.getModulo());
		setIncompleto(modulo.isIncompleto());
		setErrores(modulo.getErrores());
		this.ente = ente;
	}
	
	  /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public RespuestaGraphEntesDTO(JsonObject json) {
    	RespuestaGraphEntesDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RespuestaGraphEntesDTOConverter.toJson(this, json);
        return json;
    }
	/**
	 * @return the ente
	 */
	public EnteReceptorDeclaDTO getEnte() {
		return ente;
	}

	/**
	 * @param ente the ente to set
	 */
	public void setEnte(EnteReceptorDeclaDTO ente) {
		this.ente = ente;
	}

}
