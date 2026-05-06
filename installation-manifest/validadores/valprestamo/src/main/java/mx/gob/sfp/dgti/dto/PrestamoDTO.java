package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.RegistroBaseDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.VehiculoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.PersonaDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoBienPrestamo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;

/**
 * DTO para los datos del empleo o cargo comisión
 *
 * @author programador04
 * @since 22/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class PrestamoDTO extends RegistroBaseDTO{

	private EnumTipoBienPrestamo tipoBien;
	private InmuebleDTO inmueble;
	private VehiculoDTO vehiculo;
	private PersonaDTO duenoTitular;
	private String relacionConTitular;
	
	public PrestamoDTO() {}
	
	
	
	/**
	 * @param id
	 * @param idPosicionVista
	 * @param registroHistorico
	 * @param verificar
	 * @param tipoOperacion
	 * @param tipoBien
	 * @param inmueble
	 * @param vehiculo
	 * @param duenoTitular
	 */
	public PrestamoDTO(String id, String idPosicionVista, Boolean registroHistorico, boolean verificar, EnumTipoOperacion tipoOperacion,
			EnumTipoBienPrestamo tipoBien, InmuebleDTO inmueble, VehiculoDTO vehiculo, PersonaDTO duenoTitular,
			String relacionConTitular) {
		super(id , idPosicionVista,tipoOperacion , registroHistorico, verificar);
		this.tipoBien = tipoBien;
		this.inmueble = inmueble;
		this.vehiculo = vehiculo;
		this.duenoTitular = duenoTitular;
		this.relacionConTitular = relacionConTitular;
	}



	/**
	 * Constructor desde JsonObject, utiliza convertidores generados
	 *
	 * @param json: objeto en JsonObject
	 */
	public PrestamoDTO(JsonObject json) {
		PrestamoDTOConverter.fromJson(json, this);
	}

	/**
	 * Metodo para obtener el JsonObject, utiliza convertidores generados
	 *
	 * @return JsonObject a partir del objeto
	 */
	@Override
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		PrestamoDTOConverter.toJson(this, json);
		return json;
	}

	/**
	 * @return the tipoBien
	 */
	public EnumTipoBienPrestamo getTipoBien() {
		return tipoBien;
	}



	/**
	 * @param tipoBien the tipoBien to set
	 */
	public void setTipoBien(EnumTipoBienPrestamo tipoBien) {
		this.tipoBien = tipoBien;
	}



	/**
	 * @return the inmueble
	 */
	public InmuebleDTO getInmueble() {
		return inmueble;
	}



	/**
	 * @param inmueble the inmueble to set
	 */
	public void setInmueble(InmuebleDTO inmueble) {
		this.inmueble = inmueble;
	}



	/**
	 * @return the vehiculo
	 */
	public VehiculoDTO getVehiculo() {
		return vehiculo;
	}



	/**
	 * @param vehiculo the vehiculo to set
	 */
	public void setVehiculo(VehiculoDTO vehiculo) {
		this.vehiculo = vehiculo;
	}



	/**
	 * @return the duenoTitular
	 */
	public PersonaDTO getDuenoTitular() {
		return duenoTitular;
	}



	/**
	 * @param duenoTitular the duenoTitular to set
	 */
	public void setDuenoTitular(PersonaDTO duenoTitular) {
		this.duenoTitular = duenoTitular;
	}



	/**
	 * @return the relacionConTitular
	 */
	public String getRelacionConTitular() {
		return relacionConTitular;
	}

	/**
	 * @param relacionConTitular the relacionConTitular to set
	 */
	public void setRelacionConTitular(String relacionConTitular) {
		this.relacionConTitular = relacionConTitular;
	}	
}
