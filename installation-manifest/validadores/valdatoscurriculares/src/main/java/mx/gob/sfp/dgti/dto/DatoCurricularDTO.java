/**
 * @(#)DatoCurricularDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.base.RegistroBaseDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumEstatusEscolaridad;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumUbicacion;

/**
 * DTO para el módulo de datos curriculares
 *
 * @author Miriam Sanchez Sanchez
 * @since 17/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DatoCurricularDTO extends RegistroBaseDTO {
	
	private CatalogoDTO nivel;

    private String institucionEducativa;

    private String carreraAreaConocimiento;

    private EnumEstatusEscolaridad estatus;

    private DocumentoObtenidoDTO documentoObtenido;

    private EnumUbicacion ubicacion;

    /**
     * Constructor
     */
    public DatoCurricularDTO(){ };


    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DatoCurricularDTO(JsonObject json) {
    	DatoCurricularDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DatoCurricularDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * Constructor con parametros
     * @param nivel
     * @param institucionEducativa
     * @param carreraAreaConocimiento
     * @param estatus
     * @param documentoObtenido
     * @param ubicacion
     */
    public DatoCurricularDTO(CatalogoDTO nivel, String institucionEducativa, String carreraAreaConocimiento, 
    		EnumEstatusEscolaridad estatus, DocumentoObtenidoDTO documentoObtenido, EnumUbicacion ubicacion, 
    		String id, String idPosicionVista, EnumTipoOperacion tipoOperacion) {
    	super(id, idPosicionVista, tipoOperacion);
    	this.nivel = nivel;
    	this.institucionEducativa = institucionEducativa;
    	this.carreraAreaConocimiento = carreraAreaConocimiento;
    	this.estatus = estatus;
    	this.documentoObtenido = documentoObtenido;
    	this.ubicacion = ubicacion;
    }


	/**
	 * @return the nivel
	 */
	public CatalogoDTO getNivel() {
		return nivel;
	}


	/**
	 * @param nivel the nivel to set
	 */
	public void setNivel(CatalogoDTO nivel) {
		this.nivel = nivel;
	}


	/**
	 * @return the institucionEducativa
	 */
	public String getInstitucionEducativa() {
		return institucionEducativa;
	}


	/**
	 * @param institucionEducativa the institucionEducativa to set
	 */
	public void setInstitucionEducativa(String institucionEducativa) {
		this.institucionEducativa = institucionEducativa;
	}

	/**
	 * @return the estatus
	 */
	public EnumEstatusEscolaridad getEstatus() {
		return estatus;
	}


	/**
	 * @param estatus the estatus to set
	 */
	public void setEstatus(EnumEstatusEscolaridad estatus) {
		this.estatus = estatus;
	}


	/**
	 * @return the ubicacion
	 */
	public EnumUbicacion getUbicacion() {
		return ubicacion;
	}


	/**
	 * @param ubicacion the ubicacion to set
	 */
	public void setUbicacion(EnumUbicacion ubicacion) {
		this.ubicacion = ubicacion;
	}


	/**
	 * @return the carreraAreaConocimiento
	 */
	public String getCarreraAreaConocimiento() {
		return carreraAreaConocimiento;
	}


	/**
	 * @param carreraAreaConocimiento the carreraAreaConocimiento to set
	 */
	public void setCarreraAreaConocimiento(String carreraAreaConocimiento) {
		this.carreraAreaConocimiento = carreraAreaConocimiento;
	}


	/**
	 * @return the documentoObtenido
	 */
	public DocumentoObtenidoDTO getDocumentoObtenido() {
		return documentoObtenido;
	}


	/**
	 * @param documentoObtenido the documentoObtenido to set
	 */
	public void setDocumentoObtenido(DocumentoObtenidoDTO documentoObtenido) {
		this.documentoObtenido = documentoObtenido;
	}


	@Override
	public String toString() {
		return "DatoCurricularDTO [nivel=" + nivel + ", institucionEducativa="
				+ institucionEducativa + ", carreraAreaConocimiento=" + carreraAreaConocimiento 
				+ ", estatus=" + estatus + ", documentoObtenido=" + documentoObtenido
				+ ", lugarInstitucion=" + ubicacion + "]";
	}

}
