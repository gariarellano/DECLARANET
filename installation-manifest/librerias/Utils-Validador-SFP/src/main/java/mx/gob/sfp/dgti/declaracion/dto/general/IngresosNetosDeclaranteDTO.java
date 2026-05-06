/**
 * @(#)IngresosNetosDeclaranteDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.ModuloBaseDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.*;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoRemuneracion;

import java.util.List;

/**
 * DTO generico para los datos del dependiente económico
 *
 * @author Miriam Sánchez Sánchez programador07
 * @since 31/10/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
@JsonInclude(value = Include.NON_NULL)
public class IngresosNetosDeclaranteDTO extends ModuloBaseDTO {

	/**
	 * Tipo de remuneracion de los ingresos
	 */
	private EnumTipoRemuneracion tipoRemuneracion;
	
	/**
	 * Remuneracion neta del cargo publico
	 */
	private MontoMonedaDTO remuneracionNetaCargoPublico;
	
	/**
	 * Total de otros ingresos
	 */
	private MontoMonedaDTO otrosIngresosTotal;
	
	/**
	 * Ingresos por Actividad industrial, comercial o empresarial
	 */
	private List<ActividadIndustrialCEDTO> actividadIndustrialComercialEmpresarial;
	
	/**
	 * Ingresos por actividad financiera
	 */
    private List<ActividadFinancieraDTO> actividadFinanciera;
    
    /**
     * Ingresos por servicios profesionales
     */
	private List<ServiciosProfesionalesDTO> serviciosProfesionales;
	
	/**
	 * Otros ingresos
	 */
    private List<OtrosIngresosDTO> otrosIngresos;
    
    /**
     * Ingresos netos del declarante
     */
    private RemuneracionDTO ingresoNetoDeclarante;
    
    /**
     * Ingreso neto de la pareja o dependiente
     */
    private RemuneracionDTO ingresoNetoParejaDependiente;
    
    /**
     * Total de ingresos netos
     */
    private RemuneracionDTO totalIngresosNetos;

    /**
	 * Lista de enajenacion de bienes del declarante
	 */
    private List<EnajenacionBienDTO> enajenacionBienes;
    
	/**
     * Constructor 
     */
    public IngresosNetosDeclaranteDTO(){ };
    
    
    /**
     * Constructor con parámetros
     */
    public IngresosNetosDeclaranteDTO(EnumTipoRemuneracion tipoRemuneracion, MontoMonedaDTO remuneracionNetaCargoPublico, MontoMonedaDTO otrosIngresosTotal,
    		List<ActividadIndustrialCEDTO> actividadIndustrialComercialEmpresarial, List<ActividadFinancieraDTO> actividadFinanciera, 
    		List<ServiciosProfesionalesDTO> serviciosProfesionales, List<OtrosIngresosDTO> otrosIngresos, RemuneracionDTO ingresoNetoDeclarante,
    		RemuneracionDTO ingresoNetoParejaDependiente, RemuneracionDTO totalIngresosNetos, List<EnajenacionBienDTO> enajenacionBienes) {
    	this.tipoRemuneracion = tipoRemuneracion;
    	this.remuneracionNetaCargoPublico = remuneracionNetaCargoPublico;
    	this.otrosIngresosTotal = otrosIngresosTotal;
    	this.actividadIndustrialComercialEmpresarial = actividadIndustrialComercialEmpresarial;
    	this.actividadFinanciera = actividadFinanciera;
    	this.serviciosProfesionales = serviciosProfesionales;
    	this.otrosIngresos = otrosIngresos;
    	this.ingresoNetoDeclarante = ingresoNetoDeclarante;
    	this.ingresoNetoParejaDependiente = ingresoNetoParejaDependiente;
    	this.totalIngresosNetos = totalIngresosNetos;
    };


    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json objeto en JsonObject
     */
    public IngresosNetosDeclaranteDTO(JsonObject json) {
    	IngresosNetosDeclaranteDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        IngresosNetosDeclaranteDTOConverter.toJson(this, json);
        return json;
    }


	/**
	 * @return the tipoRemuneracion
	 */
	public EnumTipoRemuneracion getTipoRemuneracion() {
		return tipoRemuneracion;
	}


	/**
	 * @param tipoRemuneracion the tipoRemuneracion to set
	 */
	public void setTipoRemuneracion(EnumTipoRemuneracion tipoRemuneracion) {
		this.tipoRemuneracion = tipoRemuneracion;
	}


	/**
	 * @return the remuneracionNetaCargoPublico
	 */
	public MontoMonedaDTO getRemuneracionNetaCargoPublico() {
		return remuneracionNetaCargoPublico;
	}


	/**
	 * @param remuneracionNetaCargoPublico the remuneracionNetaCargoPublico to set
	 */
	public void setRemuneracionNetaCargoPublico(MontoMonedaDTO remuneracionNetaCargoPublico) {
		this.remuneracionNetaCargoPublico = remuneracionNetaCargoPublico;
	}


	/**
	 * @return the otrosIngresosTotal
	 */
	public MontoMonedaDTO getOtrosIngresosTotal() {
		return otrosIngresosTotal;
	}


	/**
	 * @param otrosIngresosTotal the otrosIngresosTotal to set
	 */
	public void setOtrosIngresosTotal(MontoMonedaDTO otrosIngresosTotal) {
		this.otrosIngresosTotal = otrosIngresosTotal;
	}


	/**
	 * @return the actividadIndustrialComercialEmpresarial
	 */
	public List<ActividadIndustrialCEDTO> getActividadIndustrialComercialEmpresarial() {
		return actividadIndustrialComercialEmpresarial;
	}


	/**
	 * @param actividadIndustrialComercialEmpresarial the actividadIndustrialComercialEmpresarial to set
	 */
	public void setActividadIndustrialComercialEmpresarial(
			List<ActividadIndustrialCEDTO> actividadIndustrialComercialEmpresarial) {
		this.actividadIndustrialComercialEmpresarial = actividadIndustrialComercialEmpresarial;
	}


	/**
	 * @return the actividadFinanciera
	 */
	public List<ActividadFinancieraDTO> getActividadFinanciera() {
		return actividadFinanciera;
	}


	/**
	 * @param actividadFinanciera the actividadFinanciera to set
	 */
	public void setActividadFinanciera(List<ActividadFinancieraDTO> actividadFinanciera) {
		this.actividadFinanciera = actividadFinanciera;
	}


	/**
	 * @return the serviciosProfesionales
	 */
	public List<ServiciosProfesionalesDTO> getServiciosProfesionales() {
		return serviciosProfesionales;
	}


	/**
	 * @param serviciosProfesionales the serviciosProfesionales to set
	 */
	public void setServiciosProfesionales(List<ServiciosProfesionalesDTO> serviciosProfesionales) {
		this.serviciosProfesionales = serviciosProfesionales;
	}


	/**
	 * @return the otrosIngresos
	 */
	public List<OtrosIngresosDTO> getOtrosIngresos() {
		return otrosIngresos;
	}


	/**
	 * @param otrosIngresos the otrosIngresos to set
	 */
	public void setOtrosIngresos(List<OtrosIngresosDTO> otrosIngresos) {
		this.otrosIngresos = otrosIngresos;
	}


	/**
	 * @return the ingresoNetoDeclarante
	 */
	public RemuneracionDTO getIngresoNetoDeclarante() {
		return ingresoNetoDeclarante;
	}


	/**
	 * @param ingresoNetoDeclarante the ingresoNetoDeclarante to set
	 */
	public void setIngresoNetoDeclarante(RemuneracionDTO ingresoNetoDeclarante) {
		this.ingresoNetoDeclarante = ingresoNetoDeclarante;
	}


	/**
	 * @return the ingresoNetoParejaDependiente
	 */
	public RemuneracionDTO getIngresoNetoParejaDependiente() {
		return ingresoNetoParejaDependiente;
	}


	/**
	 * @param ingresoNetoParejaDependiente the ingresoNetoParejaDependiente to set
	 */
	public void setIngresoNetoParejaDependiente(RemuneracionDTO ingresoNetoParejaDependiente) {
		this.ingresoNetoParejaDependiente = ingresoNetoParejaDependiente;
	}


	/**
	 * @return the totalIngresosNetos
	 */
	public RemuneracionDTO getTotalIngresosNetos() {
		return totalIngresosNetos;
	}


	/**
	 * @param totalIngresosNetos the totalIngresosNetos to set
	 */
	public void setTotalIngresosNetos(RemuneracionDTO totalIngresosNetos) {
		this.totalIngresosNetos = totalIngresosNetos;
	}
	
	/**
	 * @return the enajenacionBienes
	 */
	public List<EnajenacionBienDTO> getEnajenacionBienes() {
		return enajenacionBienes;
	}

	/**
	 * @param enajenacionBienes the enajenacionBienes to set
	 */
	public void setEnajenacionBienes(List<EnajenacionBienDTO> enajenacionBienes) {
		this.enajenacionBienes = enajenacionBienes;
	}
	
	
}
