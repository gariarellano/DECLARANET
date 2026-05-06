/**
 * @IngresosModulosDTO.java Dec 4, 2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

package mx.gob.sfp.dgti.declaracion.dto.general;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO que contiene los módulos de vehículos, bien inmueble y mueble 
 * para obtener los ingresos de enajenación de bienes en el validador de ingresos netos 
 * @author Miriam Sanchez Sanchez programador07
 * @since Dec 4, 2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class IngresosModulosDTO {
	
	private IngresosNetosDeclaranteDTO ingresos;
	
	private VehiculosDTO vehiculos;
	
	private BienesMueblesDTO bienesMuebles;
	
	private BienesInmueblesDTO bienesInmuebles;
	
	public IngresosModulosDTO() { }

	public IngresosModulosDTO(IngresosNetosDeclaranteDTO ingresos, VehiculosDTO vehiculos,
			BienesMueblesDTO muebles, BienesInmueblesDTO inmuebles) {
	        this.ingresos = ingresos;
	        this.bienesMuebles = muebles;
	        this.vehiculos = vehiculos;
	        this.bienesInmuebles = inmuebles;
    }
	
	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public IngresosModulosDTO(JsonObject json) {
    	IngresosModulosDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        IngresosModulosDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the ingresos
	 */
	public IngresosNetosDeclaranteDTO getIngresos() {
		return ingresos;
	}

	/**
	 * @param ingresos the ingresos to set
	 */
	public void setIngresos(IngresosNetosDeclaranteDTO ingresos) {
		this.ingresos = ingresos;
	}

	/**
	 * @return the vehiculos
	 */
	public VehiculosDTO getVehiculos() {
		return vehiculos;
	}

	/**
	 * @param vehiculos the vehiculos to set
	 */
	public void setVehiculos(VehiculosDTO vehiculos) {
		this.vehiculos = vehiculos;
	}

	/**
	 * @return the muebles
	 */
	public BienesMueblesDTO getBienesMuebles() {
		return bienesMuebles;
	}

	/**
	 * @param muebles the muebles to set
	 */
	public void setBienesMuebles(BienesMueblesDTO muebles) {
		this.bienesMuebles = muebles;
	}

	/**
	 * @return the inmuebles
	 */
	public BienesInmueblesDTO getBienesInmuebles() {
		return bienesInmuebles;
	}

	/**
	 * @param inmuebles the inmuebles to set
	 */
	public void setBienesInmuebles(BienesInmueblesDTO inmuebles) {
		this.bienesInmuebles = inmuebles;
	}
	
}
