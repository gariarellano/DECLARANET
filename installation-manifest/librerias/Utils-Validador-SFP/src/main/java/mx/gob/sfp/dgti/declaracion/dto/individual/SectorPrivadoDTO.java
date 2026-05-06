/**
 * @(#)SectorPrivadoDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico para el objeto de sector privado
 * @author Miriam Sánchez Sánchez programador07
 * @since 25/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class SectorPrivadoDTO {

	/**
	 * Nombre de la empresa o sociedad o asociacion
	 */
	private String nombreEmpresaSociedadAsociacion;
	
	/**
	 * RFC de la empresa
	 */
	private String rfc;
	
	/**
	 * Area de la empresa
	 */
	private String area;
	
	/**
	 * Nombre del empleo cargo o comisión que se estableció en su recibo de nómina
	 */
	private String empleoCargo;
	
	/**
	 * Sector publico, privado u otro
	 */
	private CatalogoDTO sector;

	/**
	 * Otro sector
	 */
	private String sectorOtro;

	/**
     * Constructor
     */
    public SectorPrivadoDTO(){ };

	public SectorPrivadoDTO(String nombreEmpresaSociedadAsociacion, String rfc, String area, String empleoCargo,
							CatalogoDTO sector, String sectorOtro) {
		this.nombreEmpresaSociedadAsociacion = nombreEmpresaSociedadAsociacion;
		this.rfc = rfc;
		this.area = area;
		this.empleoCargo = empleoCargo;
		this.sector = sector;
		this.sectorOtro = sectorOtro;
	}

	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public SectorPrivadoDTO(JsonObject json) {
    	SectorPrivadoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        SectorPrivadoDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the nombreEmpresaSociedadAsociacion
	 */
	public String getNombreEmpresaSociedadAsociacion() {
		return nombreEmpresaSociedadAsociacion;
	}

	/**
	 * @param nombreEmpresaSociedadAsociacion the nombreEmpresaSociedadAsociacion to set
	 */
	public void setNombreEmpresaSociedadAsociacion(String nombreEmpresaSociedadAsociacion) {
		this.nombreEmpresaSociedadAsociacion = nombreEmpresaSociedadAsociacion;
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
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the empleoCargo
	 */
	public String getEmpleoCargo() {
		return empleoCargo;
	}

	/**
	 * @param empleoCargo the empleoCargo to set
	 */
	public void setEmpleoCargo(String empleoCargo) {
		this.empleoCargo = empleoCargo;
	}

	/**
	 * @return the sector
	 */
	public CatalogoDTO getSector() {
		return sector;
	}

	/**
	 * @param sector the sector to set
	 */
	public void setSector(CatalogoDTO sector) {
		this.sector = sector;
	}

	public String getSectorOtro() {
		return sectorOtro;
	}

	public void setSectorOtro(String sectorOtro) {
		this.sectorOtro = sectorOtro;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("SectorPrivadoDTO{");
		sb.append("nombreEmpresaSociedadAsociacion='").append(nombreEmpresaSociedadAsociacion).append('\'');
		sb.append(", rfc='").append(rfc).append('\'');
		sb.append(", area='").append(area).append('\'');
		sb.append(", empleoCargo='").append(empleoCargo).append('\'');
		sb.append(", sector=").append(sector);
		sb.append(", sectorOtro='").append(sectorOtro).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
