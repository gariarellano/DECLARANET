/**
 * @(#)DatosEmpleoCargoComisionDTO.java 22/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import java.util.List;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.base.ModuloBaseDTO;
/**
 * DTO para los datos del empleo o cargo comisión
 *
 * @author programador04
 * @since 22/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DatosEmpleoCargoComisionDTO  extends ModuloBaseDTO{
	private List<EmpleoCargoComisionDTO> empleoCargoComision;

	/**
	 * Constructor
	 */
	public DatosEmpleoCargoComisionDTO() {}
	
	public DatosEmpleoCargoComisionDTO(List<EmpleoCargoComisionDTO> empleoCargoComision, String aclaracionesObservaciones, EncabezadoDTO encabezado) {
		super(encabezado, aclaracionesObservaciones);
		this.empleoCargoComision = empleoCargoComision;
	}
	
	/**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DatosEmpleoCargoComisionDTO(JsonObject json) {
    	DatosEmpleoCargoComisionDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DatosEmpleoCargoComisionDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the empleoCargoComision
	 */
	public List<EmpleoCargoComisionDTO> getEmpleoCargoComision() {
		return empleoCargoComision;
	}

	/**
	 * @param empleoCargoComision the empleoCargoComision to set
	 */
	public void setEmpleoCargoComision(List<EmpleoCargoComisionDTO> empleoCargoComision) {
		this.empleoCargoComision = empleoCargoComision;
	}


}
