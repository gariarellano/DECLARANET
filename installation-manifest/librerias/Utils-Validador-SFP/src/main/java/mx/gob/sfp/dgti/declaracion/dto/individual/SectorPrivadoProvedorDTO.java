/**
 * @(#)SectorPrivadoProvedorDTO.java 30/05/2019
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
 * @author pavel.martinez
 * @since 01/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class SectorPrivadoProvedorDTO  extends SectorPrivadoDTO{

	private boolean proveedorContratistaGobierno;

	/**
     * Constructor
     */
    public SectorPrivadoProvedorDTO(){ };

	public SectorPrivadoProvedorDTO(String nombreEmpresaSociedadAsociacion, String rfc, String area, String empleoCargo,
									CatalogoDTO sector, String sectorOtro, boolean proveedorContratistaGobierno) {
		super(nombreEmpresaSociedadAsociacion, rfc, area, empleoCargo, sector, sectorOtro);
		this.proveedorContratistaGobierno = proveedorContratistaGobierno;
	}

	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public SectorPrivadoProvedorDTO(JsonObject json) {
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

	public boolean isProveedorContratistaGobierno() {
		return proveedorContratistaGobierno;
	}

	public void setProveedorContratistaGobierno(boolean proveedorContratistaGobierno) {
		this.proveedorContratistaGobierno = proveedorContratistaGobierno;
	}
}
