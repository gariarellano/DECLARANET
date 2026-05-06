/**
 * @(#)DatosGeneralesDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.general;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.base.ModuloBaseDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CorreoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.TelefonoDTO;

/**
 * DTO generico para el módulo de datos generales
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 12/09/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DatosGeneralesDTO extends ModuloBaseDTO {

	/**
	 * Datos personales del declarante
	 */
    private DatosPersonalesDTO datosPersonales;
    
    /**
     * Correo electronico del declarante
     */
    private CorreoDTO correoElectronico;
    
    /**
     * Telefono de casa
     */
    private TelefonoDTO telefonoCasa;
    
    /**
     * Telefono celular
     */
    private TelefonoDTO telefonoCelular;

    /**
     * Situacion personal/ estado civil
     */
    private CatalogoDTO situacionPersonalEstadoCivil;

    /**
     * Regimen matrimonial del declarante
     */
    private CatalogoDTO regimenMatrimonial;

	/**
	 * Otro regimen matrimonial
	 */
	private String regimenMatrimonialOtro;

	/**
     * Pais de nacimiento del declarante
     */
    private CatalogoDTO paisNacimiento;

    /**
     * Nacionalidad del declarante
     */
    private CatalogoDTO nacionalidad;

    /**
     * Bandera verificar
     */
    private boolean verificar;
    
    
    /**
     * Constructor
     */
    public DatosGeneralesDTO(){ };


    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public DatosGeneralesDTO(JsonObject json) {
        DatosGeneralesDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DatosGeneralesDTOConverter.toJson(this, json);
        return json;
    }

    public DatosGeneralesDTO(DatosPersonalesDTO datosPersonales, CorreoDTO correoElectronico, TelefonoDTO telefonoCasa, TelefonoDTO telefonoCelular, 
    			CatalogoDTO situacionPersonalEstadoCivil, CatalogoDTO regimenMatrimonial, CatalogoDTO paisNacimiento, CatalogoDTO nacionalidad, 
    			boolean verificar, String aclaracionesObservaciones, EncabezadoDTO encabezado) {
    	super(encabezado, aclaracionesObservaciones);
    	this.datosPersonales = datosPersonales;
        this.correoElectronico = correoElectronico;
        this.telefonoCasa = telefonoCasa;
        this.telefonoCelular = telefonoCelular;
        this.situacionPersonalEstadoCivil = situacionPersonalEstadoCivil;
        this.regimenMatrimonial = regimenMatrimonial;
        this.paisNacimiento = paisNacimiento;
        this.nacionalidad = nacionalidad;
        this.verificar = verificar;
    }

	public DatosGeneralesDTO(EncabezadoDTO encabezado, String aclaracionesObservaciones,
							 DatosPersonalesDTO datosPersonales, CorreoDTO correoElectronico,
							 TelefonoDTO telefonoCasa, TelefonoDTO telefonoCelular, CatalogoDTO
									 situacionPersonalEstadoCivil, CatalogoDTO regimenMatrimonial,
							 String regimenMatrimonialOtro, CatalogoDTO paisNacimiento, CatalogoDTO nacionalidad,
							 boolean verificar) {
		super(encabezado, aclaracionesObservaciones);
		this.datosPersonales = datosPersonales;
		this.correoElectronico = correoElectronico;
		this.telefonoCasa = telefonoCasa;
		this.telefonoCelular = telefonoCelular;
		this.situacionPersonalEstadoCivil = situacionPersonalEstadoCivil;
		this.regimenMatrimonial = regimenMatrimonial;
		this.regimenMatrimonialOtro = regimenMatrimonialOtro;
		this.paisNacimiento = paisNacimiento;
		this.nacionalidad = nacionalidad;
		this.verificar = verificar;
	}

	/**
	 * @return the datosPersonales
	 */
	public DatosPersonalesDTO getDatosPersonales() {
		return datosPersonales;
	}


	/**
	 * @param datosPersonales the datosPersonales to set
	 */
	public void setDatosPersonales(DatosPersonalesDTO datosPersonales) {
		this.datosPersonales = datosPersonales;
	}


	/**
	 * @return the correoElectronico
	 */
	public CorreoDTO getCorreoElectronico() {
		return correoElectronico;
	}


	/**
	 * @param correoElectronico the correoElectronico to set
	 */
	public void setCorreoElectronico(CorreoDTO correoElectronico) {
		this.correoElectronico = correoElectronico;
	}


	/**
	 * @return the telefonoCasa
	 */
	public TelefonoDTO getTelefonoCasa() {
		return telefonoCasa;
	}


	/**
	 * @param telefonoCasa the telefonoCasa to set
	 */
	public void setTelefonoCasa(TelefonoDTO telefonoCasa) {
		this.telefonoCasa = telefonoCasa;
	}


	/**
	 * @return the telefonoCelular
	 */
	public TelefonoDTO getTelefonoCelular() {
		return telefonoCelular;
	}


	/**
	 * @param telefonoCelular the telefonoCelular to set
	 */
	public void setTelefonoCelular(TelefonoDTO telefonoCelular) {
		this.telefonoCelular = telefonoCelular;
	}


	/**
	 * @return the situacionPersonalEstadoCivil
	 */
	public CatalogoDTO getSituacionPersonalEstadoCivil() {
		return situacionPersonalEstadoCivil;
	}


	/**
	 * @param situacionPersonalEstadoCivil the situacionPersonalEstadoCivil to set
	 */
	public void setSituacionPersonalEstadoCivil(CatalogoDTO situacionPersonalEstadoCivil) {
		this.situacionPersonalEstadoCivil = situacionPersonalEstadoCivil;
	}


	/**
	 * @return the regimenMatrimonial
	 */
	public CatalogoDTO getRegimenMatrimonial() {
		return regimenMatrimonial;
	}


	/**
	 * @param regimenMatrimonial the regimenMatrimonial to set
	 */
	public void setRegimenMatrimonial(CatalogoDTO regimenMatrimonial) {
		this.regimenMatrimonial = regimenMatrimonial;
	}


	/**
	 * @return the paisNacimiento
	 */
	public CatalogoDTO getPaisNacimiento() {
		return paisNacimiento;
	}


	/**
	 * @param paisNacimiento the paisNacimiento to set
	 */
	public void setPaisNacimiento(CatalogoDTO paisNacimiento) {
		this.paisNacimiento = paisNacimiento;
	}


	/**
	 * @return the nacionalidad
	 */
	public CatalogoDTO getNacionalidad() {
		return nacionalidad;
	}


	/**
	 * @param nacionalidad the nacionalidad to set
	 */
	public void setNacionalidad(CatalogoDTO nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	/**
	 * @return the verificar
	 */
	public boolean isVerificar() {
		return verificar;
	}


	/**
	 * @param verificar the verificar to set
	 */
	public void setVerificar(boolean verificar) {
		this.verificar = verificar;
	}

	public String getRegimenMatrimonialOtro() {
		return regimenMatrimonialOtro;
	}

	public void setRegimenMatrimonialOtro(String regimenMatrimonialOtro) {
		this.regimenMatrimonialOtro = regimenMatrimonialOtro;
	}
}
