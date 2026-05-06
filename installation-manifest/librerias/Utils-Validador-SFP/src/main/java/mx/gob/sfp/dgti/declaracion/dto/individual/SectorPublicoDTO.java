/**
 * @(#)SectorPublicoDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumAmbitoPoder;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumNivelGobierno;

/**
 * DTO generico para el objeto de sector publico
 * @author Miriam Sánchez Sánchez programador07
 * @since 25/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class SectorPublicoDTO {

	/**
	 * Nivel de orden de gobierno
	 */
	private EnumNivelGobierno nivelOrdenGobierno;
	
	/**
	 * Naturaleza jurídica al que pertenece: ejecutivo, legislativo, judicial u órgano autónomo.
	 */
	private EnumAmbitoPoder ambitoPublico;

	/**
	 * El ente público al cual se encontró adscrita la plaza. 
	 */
	private String nombreEntePublico;
	
	/**
	 * Nombre de la Unidad Administrativa u homologa superior inmediata en la que estuvo adscrito. 
	 */
	private String areaAdscripcion;
	
	/**
	 * Nombre del empleo cargo o comisión que se estableció en su recibo de nómina.
	 */
	private String empleoCargoComision;
	
	/**
	 * La función o actividad principal que desempeño.
	 */
	private String funcionPrincipal;
	
	/**
     * Constructor
     */
    public SectorPublicoDTO(){ };

    public SectorPublicoDTO(EnumNivelGobierno nivelOrdenGobierno, EnumAmbitoPoder ambitoPublico, String nombreEntePublico,
							String areaAdscripcion, String empleoCargoComision, String funcionPrincipal) {
    	this.nivelOrdenGobierno = nivelOrdenGobierno;
        this.ambitoPublico = ambitoPublico;
        this.nombreEntePublico = nombreEntePublico;
        this.areaAdscripcion = areaAdscripcion;
        this.empleoCargoComision = empleoCargoComision;
        this.funcionPrincipal = funcionPrincipal;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public SectorPublicoDTO(JsonObject json) {
    	SectorPublicoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        SectorPublicoDTOConverter.toJson(this, json);
        return json;
    }

	/**
	 * @return the nivelOrdenGobierno
	 */
	public EnumNivelGobierno getNivelOrdenGobierno() {
		return nivelOrdenGobierno;
	}

	/**
	 * @param nivelOrdenGobierno the nivelOrdenGobierno to set
	 */
	public void setNivelOrdenGobierno(EnumNivelGobierno nivelOrdenGobierno) {
		this.nivelOrdenGobierno = nivelOrdenGobierno;
	}

	/**
	 * @return the ambitoPublico
	 */
	public EnumAmbitoPoder getAmbitoPublico() {
		return ambitoPublico;
	}

	/**
	 * @param ambitoPublico the ambitoPublico to set
	 */
	public void setAmbitoPublico(EnumAmbitoPoder ambitoPublico) {
		this.ambitoPublico = ambitoPublico;
	}

	/**
	 * @return the nombreEntePublico
	 */
	public String getNombreEntePublico() {
		return nombreEntePublico;
	}

	/**
	 * @param nombreEntePublico the nombreEntePublico to set
	 */
	public void setNombreEntePublico(String nombreEntePublico) {
		this.nombreEntePublico = nombreEntePublico;
	}

	/**
	 * @return the areaAdscripcion
	 */
	public String getAreaAdscripcion() {
		return areaAdscripcion;
	}

	/**
	 * @param areaAdscripcion the areaAdscripcion to set
	 */
	public void setAreaAdscripcion(String areaAdscripcion) {
		this.areaAdscripcion = areaAdscripcion;
	}

	/**
	 * @return the empleoCargoComision
	 */
	public String getEmpleoCargoComision() {
		return empleoCargoComision;
	}

	/**
	 * @param empleoCargoComision the empleoCargoComision to set
	 */
	public void setEmpleoCargoComision(String empleoCargoComision) {
		this.empleoCargoComision = empleoCargoComision;
	}

	/**
	 * @return the funcionPrincipal
	 */
	public String getFuncionPrincipal() {
		return funcionPrincipal;
	}

	/**
	 * @param funcionPrincipal the funcionPrincipal to set
	 */
	public void setFuncionPrincipal(String funcionPrincipal) {
		this.funcionPrincipal = funcionPrincipal;
	}
	
}
