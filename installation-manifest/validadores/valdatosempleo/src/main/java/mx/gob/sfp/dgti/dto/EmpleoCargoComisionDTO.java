/**
 * @(#)EmpleoCargoComisionDTO.java 22/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.base.RegistroBaseDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.DomicilioDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.EnteReceptorDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoUnoFkDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.MontoMonedaDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.TelefonoOficinaDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoRemuneracion;
/**
 * DTO generico para el modulo de actividad anual anterior
 *
 * @author programador04
 * @since 22/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class EmpleoCargoComisionDTO extends RegistroBaseDTO{
	
	private EnteReceptorDTO ente;
	private String idMovimiento;
	private String areaAdscripcion;
	private String empleoCargoComision;
	private CatalogoUnoFkDTO nivelJerarquico;
	private String nivelEmpleoCargoComision;
	private Boolean contratadoPorHonorarios;
	private MontoMonedaDTO remuneracionNeta;
	private EnumTipoRemuneracion tipoRemuneracion;
	private String funcionPrincipal;
	private String fechaEncargo;
	private TelefonoOficinaDTO telefonoOficina;
	private DomicilioDTO domicilio;

	public EmpleoCargoComisionDTO() {}
	
	/**
	 * @param idPosicionVista
	 * @param registroHistorico
	 * @param tipoOperacion
	 * @param id
	 * @param ente
	 * @param areaAdscripcion
	 * @param empleoCargoComision
	 * @param nivelJerarquico
	 * @param nivelEmpleoCargoComision
	 * @param contratadoPorHonorarios
	 * @param remuneracionNeta
	 * @param tipoRemuneracion
	 * @param funcionPrincipal
	 * @param fechaEncargo
	 * @param telefonoOficina
	 * @param domicilio
	 * @param nivelJerarquico
	 */
	public EmpleoCargoComisionDTO(String idPosicionVista, Boolean registroHistorico, boolean verificar, EnumTipoOperacion tipoOperacion,
			String id, EnteReceptorDTO ente, String areaAdscripcion, String empleoCargoComision,
			CatalogoUnoFkDTO nivelJerarquico, String nivelEmpleoCargoComision, Boolean contratadoPorHonorarios,
			MontoMonedaDTO remuneracionNeta, EnumTipoRemuneracion tipoRemuneracion, String funcionPrincipal,
			String fechaEncargo, TelefonoOficinaDTO telefonoOficina, DomicilioDTO domicilio, String idMovimiento) {
		super(id , idPosicionVista,tipoOperacion , registroHistorico, verificar);
		this.ente = ente;
		this.areaAdscripcion = areaAdscripcion;
		this.empleoCargoComision = empleoCargoComision;
		this.nivelJerarquico = nivelJerarquico;
		this.nivelEmpleoCargoComision = nivelEmpleoCargoComision;
		this.contratadoPorHonorarios = contratadoPorHonorarios;
		this.remuneracionNeta = remuneracionNeta;
		this.tipoRemuneracion = tipoRemuneracion;
		this.funcionPrincipal = funcionPrincipal;
		this.fechaEncargo = fechaEncargo;
		this.telefonoOficina = telefonoOficina;
		this.domicilio = domicilio;
		this.idMovimiento = idMovimiento;
	}

	/**
	 * Constructor desde JsonObject, utiliza convertidores generados
	 *
	 * @param json: objeto en JsonObject
	 */
	public EmpleoCargoComisionDTO(JsonObject json) {
		EmpleoCargoComisionDTOConverter.fromJson(json, this);
	}

	/**
	 * Metodo para obtener el JsonObject, utiliza convertidores generados
	 *
	 * @return JsonObject a partir del objeto
	 */
	@Override
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		EmpleoCargoComisionDTOConverter.toJson(this, json);
		return json;
	}

	/**
	 * @return the ente
	 */
	public EnteReceptorDTO getEnte() {
		return ente;
	}

	/**
	 * @param ente the ente to set
	 */
	public void setEnte(EnteReceptorDTO ente) {
		this.ente = ente;
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
	 * @return the nivelJerarquico
	 */
	public CatalogoUnoFkDTO getNivelJerarquico() {
		return nivelJerarquico;
	}

	/**
	 * @param nivelJerarquico the nivelJerarquico to set
	 */
	public void setNivelJerarquico(CatalogoUnoFkDTO nivelJerarquico) {
		this.nivelJerarquico = nivelJerarquico;
	}

	/**
	 * @return the nivelEmpleoCargoComision
	 */
	public String getNivelEmpleoCargoComision() {
		return nivelEmpleoCargoComision;
	}

	/**
	 * @param nivelEmpleoCargoComision the nivelEmpleoCargoComision to set
	 */
	public void setNivelEmpleoCargoComision(String nivelEmpleoCargoComision) {
		this.nivelEmpleoCargoComision = nivelEmpleoCargoComision;
	}

	/**
	 * @return the contratadoPorHonorarios
	 */
	public Boolean getContratadoPorHonorarios() {
		return contratadoPorHonorarios;
	}

	/**
	 * @param contratadoPorHonorarios the contratadoPorHonorarios to set
	 */
	public void setContratadoPorHonorarios(Boolean contratadoPorHonorarios) {
		this.contratadoPorHonorarios = contratadoPorHonorarios;
	}

	/**
	 * @return the remuneracionNeta
	 */
	public MontoMonedaDTO getRemuneracionNeta() {
		return remuneracionNeta;
	}

	/**
	 * @param remuneracionNeta the remuneracionNeta to set
	 */
	public void setRemuneracionNeta(MontoMonedaDTO remuneracionNeta) {
		this.remuneracionNeta = remuneracionNeta;
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

	/**
	 * @return the telefonoOficina
	 */
	public TelefonoOficinaDTO getTelefonoOficina() {
		return telefonoOficina;
	}

	/**
	 * @param telefonoOficina the telefonoOficina to set
	 */
	public void setTelefonoOficina(TelefonoOficinaDTO telefonoOficina) {
		this.telefonoOficina = telefonoOficina;
	}

	/**
	 * @return the domicilio
	 */
	public DomicilioDTO getDomicilio() {
		return domicilio;
	}

	/**
	 * @param domicilio the domicilio to set
	 */
	public void setDomicilio(DomicilioDTO domicilio) {
		this.domicilio = domicilio;
	}

	/**
	 * @return the fechaEncargo
	 */
	public String getFechaEncargo() {
		return fechaEncargo;
	}

	/**
	 * @param fechaEncargo the fechaEncargo to set
	 */
	public void setFechaEncargo(String fechaEncargo) {
		this.fechaEncargo = fechaEncargo;
	}

	public String getIdMovimiento() {
		return idMovimiento;
	}

	public void setIdMovimiento(String idMovimiento) {
		this.idMovimiento = idMovimiento;
	}
	
}
