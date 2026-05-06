package mx.gob.sfp.dgti.dto;

import java.util.List;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.ModuloBaseDTO;

/**
 * DTO para los datos del empleo o cargo comisión
 *
 * @author programador04
 * @since 09/12/2019
 */

@DataObject(generateConverter = true, inheritConverter = true)
public class PrestamoComodatoDTO extends ModuloBaseDTO{

	private Boolean ninguno;
	private List<PrestamoDTO> prestamo;
	
	
	public PrestamoComodatoDTO() {}
	
	
	/**
	 * @param ninguno
	 * @param prestamo
	 */
	public PrestamoComodatoDTO(Boolean ninguno, List<PrestamoDTO> prestamo) {
		super();
		this.ninguno = ninguno;
		this.prestamo = prestamo;
	}


	/**
	 * Constructor desde JsonObject, utiliza convertidores generados
	 *
	 * @param json: objeto en JsonObject
	 */
	public PrestamoComodatoDTO(JsonObject json) {
		PrestamoComodatoDTOConverter.fromJson(json, this);
	}

	/**
	 * Metodo para obtener el JsonObject, utiliza convertidores generados
	 *
	 * @return JsonObject a partir del objeto
	 */
	@Override
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		PrestamoComodatoDTOConverter.toJson(this, json);
		return json;
	}


	/**
	 * @return the ninguno
	 */
	public Boolean getNinguno() {
		return ninguno;
	}


	/**
	 * @param ninguno the ninguno to set
	 */
	public void setNinguno(Boolean ninguno) {
		this.ninguno = ninguno;
	}


	/**
	 * @return the prestamo
	 */
	public List<PrestamoDTO> getPrestamo() {
		return prestamo;
	}


	/**
	 * @param prestamo the prestamo to set
	 */
	public void setPrestamo(List<PrestamoDTO> prestamo) {
		this.prestamo = prestamo;
	}
}
