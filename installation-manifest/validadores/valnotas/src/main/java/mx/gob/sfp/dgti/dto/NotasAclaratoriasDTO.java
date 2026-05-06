/**
 * @(#)NotasAclaratoriasDTO.java 20/02/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO para el validador de notas
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 20/02/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class NotasAclaratoriasDTO {

    /**
     * Notas para datos generales
     */
    private NotaDTO datosGenerales;

    /**
     * Notas para domicilio del declarante
     */
    private NotaDTO domicilioDeclarante;

    /**
     * Notas para actividad anual anterior
     */
    private NotaDTO actividadAnualAnterior;

    /**
     * Notas para adeudos pasivos
     */
    private NotaDTO adeudosPasivos;

    /**
     * Notas para apoyos
     */
    private NotaDTO apoyos;

    /**
     * Notas para beneficios privados
     */
    private NotaDTO beneficiosPrivados;

    /**
     * Notas para bienes inmuebles
     */
    private NotaDTO bienesInmuebles;

    /**
     * Notas para bienes muebles
     */
    private NotaDTO bienesMuebles;

    /**
     * Notas para clientes principales
     */
    private NotaDTO clientesPrincipales;

    /**
     * Notas para datos curriculares del declarante
     */
    private NotaDTO datosCurricularesDeclarante;

    /**
     * Notas para datos de dependientes economicos
     */
    private NotaDTO datosDependientesEconomicos;

    /**
     * Notas para datos de empleo
     */
    private NotaDTO datosEmpleoCargoComision;

    /**
     * Notas para datos de parejas
     */
    private NotaDTO datosParejas;

    /**
     * Notas para experiencias laborales
     */
    private NotaDTO experienciasLaborales;

    /**
     * Notas para fideicomisos
     */
    private NotaDTO fideicomisos;

    /**
     * Notas para ingresos
     */
    private NotaDTO ingresos;

    /**
     * Notas para inversiones de cuenta de valores
     */
    private NotaDTO inversionesCuentasValores;

    /**
     * Notas para participacion de toma de decisiones
     */
    private NotaDTO participacionTomaDecisiones;

    /**
     * Notas para participacion de empresas sociedades y asociaciones
     */
    private NotaDTO participaEmpresasSociedadesAsociaciones;

    /**
     * Notas para prestamo comodato
     */
    private NotaDTO prestamoComodato;

    /**
     * Notas para representaciones
     */
    private NotaDTO representaciones;

    /**
     * Notas para vehiculos
     */
    private NotaDTO vehiculos;

    /**
     * Notas para detalle de aviso por cambio de dependencia
     */
    private NotaDTO detalleAvisoCambioDependencia;

    /**
     * Constructor
     */
    public NotasAclaratoriasDTO() {
    }

    /**
     * Constructor
     *
     * @param datosGenerales notas para datos generales
     * @param domicilioDeclarante notas para domicilio
     * @param actividadAnualAnterior notas para actividad anual anterior
     * @param adeudosPasivos notas para adeudos pasivos
     * @param apoyos notas para apoyos
     * @param beneficiosPrivados notas para beneficios privados
     * @param bienesInmuebles notas para bienes inmuebles
     * @param bienesMuebles notas para bienes muebles
     * @param clientesPrincipales notas para clinetes principales
     * @param datosCurricularesDeclarante notas para datos curriculares
     * @param datosDependientesEconomicos notas para datos dependiente
     * @param datosEmpleoCargoComision notas para datos para empleo cargo o comision
     * @param datosParejas notas para datos de parejas
     * @param experienciasLaborales notas para experiencias laboraes
     * @param fideicomisos notas para fideicomisos
     * @param ingresos notas para ingresos
     * @param inversionesCuentasValores notas para inversiones
     * @param participacionTomaDecisiones notas para participacion instituciones
     * @param participaEmpresasSociedadesAsociaciones notas para paraticipacion de empresas
     * @param prestamoComodato notas para prestamoComodatp
     * @param representaciones notas para representaciones
     * @param vehiculos notas para vehiculos
     */
    public NotasAclaratoriasDTO(NotaDTO datosGenerales, NotaDTO domicilioDeclarante, NotaDTO actividadAnualAnterior,
                                NotaDTO adeudosPasivos, NotaDTO apoyos, NotaDTO beneficiosPrivados,
                                NotaDTO bienesInmuebles, NotaDTO bienesMuebles, NotaDTO clientesPrincipales,
                                NotaDTO datosCurricularesDeclarante, NotaDTO datosDependientesEconomicos,
                                NotaDTO datosEmpleoCargoComision, NotaDTO datosParejas, NotaDTO experienciasLaborales,
                                NotaDTO fideicomisos, NotaDTO ingresos, NotaDTO inversionesCuentasValores,
                                NotaDTO participacionTomaDecisiones, NotaDTO participaEmpresasSociedadesAsociaciones,
                                NotaDTO prestamoComodato, NotaDTO representaciones, NotaDTO vehiculos,
                                NotaDTO detalleAvisoCambioDependencia) {
        this.datosGenerales = datosGenerales;
        this.domicilioDeclarante = domicilioDeclarante;
        this.actividadAnualAnterior = actividadAnualAnterior;
        this.adeudosPasivos = adeudosPasivos;
        this.apoyos = apoyos;
        this.beneficiosPrivados = beneficiosPrivados;
        this.bienesInmuebles = bienesInmuebles;
        this.bienesMuebles = bienesMuebles;
        this.clientesPrincipales = clientesPrincipales;
        this.datosCurricularesDeclarante = datosCurricularesDeclarante;
        this.datosDependientesEconomicos = datosDependientesEconomicos;
        this.datosEmpleoCargoComision = datosEmpleoCargoComision;
        this.datosParejas = datosParejas;
        this.experienciasLaborales = experienciasLaborales;
        this.fideicomisos = fideicomisos;
        this.ingresos = ingresos;
        this.inversionesCuentasValores = inversionesCuentasValores;
        this.participacionTomaDecisiones = participacionTomaDecisiones;
        this.participaEmpresasSociedadesAsociaciones = participaEmpresasSociedadesAsociaciones;
        this.prestamoComodato = prestamoComodato;
        this.representaciones = representaciones;
        this.vehiculos = vehiculos;
        this.detalleAvisoCambioDependencia = detalleAvisoCambioDependencia;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public NotasAclaratoriasDTO(JsonObject json) {
        NotasAclaratoriasDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        NotasAclaratoriasDTOConverter.toJson(this, json);
        return json;
    }

    public NotaDTO getDatosGenerales() {
        return datosGenerales;
    }

    public void setDatosGenerales(NotaDTO datosGenerales) {
        this.datosGenerales = datosGenerales;
    }

    public NotaDTO getDomicilioDeclarante() {
        return domicilioDeclarante;
    }

    public void setDomicilioDeclarante(NotaDTO domicilioDeclarante) {
        this.domicilioDeclarante = domicilioDeclarante;
    }

    public NotaDTO getActividadAnualAnterior() {
        return actividadAnualAnterior;
    }

    public void setActividadAnualAnterior(NotaDTO actividadAnualAnterior) {
        this.actividadAnualAnterior = actividadAnualAnterior;
    }

    public NotaDTO getAdeudosPasivos() {
        return adeudosPasivos;
    }

    public void setAdeudosPasivos(NotaDTO adeudosPasivos) {
        this.adeudosPasivos = adeudosPasivos;
    }

    public NotaDTO getApoyos() {
        return apoyos;
    }

    public void setApoyos(NotaDTO apoyos) {
        this.apoyos = apoyos;
    }

    public NotaDTO getBeneficiosPrivados() {
        return beneficiosPrivados;
    }

    public void setBeneficiosPrivados(NotaDTO beneficiosPrivados) {
        this.beneficiosPrivados = beneficiosPrivados;
    }

    public NotaDTO getBienesInmuebles() {
        return bienesInmuebles;
    }

    public void setBienesInmuebles(NotaDTO bienesInmuebles) {
        this.bienesInmuebles = bienesInmuebles;
    }

    public NotaDTO getBienesMuebles() {
        return bienesMuebles;
    }

    public void setBienesMuebles(NotaDTO bienesMuebles) {
        this.bienesMuebles = bienesMuebles;
    }

    public NotaDTO getClientesPrincipales() {
        return clientesPrincipales;
    }

    public void setClientesPrincipales(NotaDTO clientesPrincipales) {
        this.clientesPrincipales = clientesPrincipales;
    }

    public NotaDTO getDatosCurricularesDeclarante() {
        return datosCurricularesDeclarante;
    }

    public void setDatosCurricularesDeclarante(NotaDTO datosCurricularesDeclarante) {
        this.datosCurricularesDeclarante = datosCurricularesDeclarante;
    }

    public NotaDTO getDatosDependientesEconomicos() {
        return datosDependientesEconomicos;
    }

    public void setDatosDependientesEconomicos(NotaDTO datosDependientesEconomicos) {
        this.datosDependientesEconomicos = datosDependientesEconomicos;
    }

    public NotaDTO getDatosEmpleoCargoComision() {
        return datosEmpleoCargoComision;
    }

    public void setDatosEmpleoCargoComision(NotaDTO datosEmpleoCargoComision) {
        this.datosEmpleoCargoComision = datosEmpleoCargoComision;
    }

    public NotaDTO getDatosParejas() {
        return datosParejas;
    }

    public void setDatosParejas(NotaDTO datosParejas) {
        this.datosParejas = datosParejas;
    }

    public NotaDTO getExperienciasLaborales() {
        return experienciasLaborales;
    }

    public void setExperienciasLaborales(NotaDTO experienciasLaborales) {
        this.experienciasLaborales = experienciasLaborales;
    }

    public NotaDTO getFideicomisos() {
        return fideicomisos;
    }

    public void setFideicomisos(NotaDTO fideicomisos) {
        this.fideicomisos = fideicomisos;
    }

    public NotaDTO getIngresos() {
        return ingresos;
    }

    public void setIngresos(NotaDTO ingresos) {
        this.ingresos = ingresos;
    }

    public NotaDTO getInversionesCuentasValores() {
        return inversionesCuentasValores;
    }

    public void setInversionesCuentasValores(NotaDTO inversionesCuentasValores) {
        this.inversionesCuentasValores = inversionesCuentasValores;
    }

    public NotaDTO getParticipacionTomaDecisiones() {
        return participacionTomaDecisiones;
    }

    public void setParticipacionTomaDecisiones(NotaDTO participacionTomaDecisiones) {
        this.participacionTomaDecisiones = participacionTomaDecisiones;
    }

    public NotaDTO getParticipaEmpresasSociedadesAsociaciones() {
        return participaEmpresasSociedadesAsociaciones;
    }

    public void setParticipaEmpresasSociedadesAsociaciones(NotaDTO participaEmpresasSociedadesAsociaciones) {
        this.participaEmpresasSociedadesAsociaciones = participaEmpresasSociedadesAsociaciones;
    }

    public NotaDTO getPrestamoComodato() {
        return prestamoComodato;
    }

    public void setPrestamoComodato(NotaDTO prestamoComodato) {
        this.prestamoComodato = prestamoComodato;
    }

    public NotaDTO getRepresentaciones() {
        return representaciones;
    }

    public void setRepresentaciones(NotaDTO representaciones) {
        this.representaciones = representaciones;
    }

    public NotaDTO getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(NotaDTO vehiculos) {
        this.vehiculos = vehiculos;
    }

    public NotaDTO getDetalleAvisoCambioDependencia() {
        return detalleAvisoCambioDependencia;
    }

    public void setDetalleAvisoCambioDependencia(NotaDTO detalleAvisoCambioDependencia) {
        this.detalleAvisoCambioDependencia = detalleAvisoCambioDependencia;
    }

    /**
     * Metodo to String
     *
     * @return
     */
    @Override
    public String toString() {
        return "NotasAclaratoriasDTO{" +
                "datosGenerales=" + datosGenerales +
                ", domicilioDeclarante=" + domicilioDeclarante +
                ", actividadAnualAnterior=" + actividadAnualAnterior +
                ", adeudosPasivos=" + adeudosPasivos +
                ", apoyos=" + apoyos +
                ", beneficiosPrivados=" + beneficiosPrivados +
                ", bienesInmuebles=" + bienesInmuebles +
                ", bienesMuebles=" + bienesMuebles +
                ", clientesPrincipales=" + clientesPrincipales +
                ", datosCurricularesDeclarante=" + datosCurricularesDeclarante +
                ", datosDependientesEconomicos=" + datosDependientesEconomicos +
                ", datosEmpleoCargoComision=" + datosEmpleoCargoComision +
                ", datosParejas=" + datosParejas +
                ", experienciasLaborales=" + experienciasLaborales +
                ", fideicomisos=" + fideicomisos +
                ", ingresos=" + ingresos +
                ", inversionesCuentasValores=" + inversionesCuentasValores +
                ", participacionTomaDecisiones=" + participacionTomaDecisiones +
                ", participaEmpresasSociedadesAsociaciones=" + participaEmpresasSociedadesAsociaciones +
                ", prestamoComodato=" + prestamoComodato +
                ", representaciones=" + representaciones +
                ", vehiculos=" + vehiculos +
                ", detalleAvisoCambioDependencia=" + detalleAvisoCambioDependencia +
                "} " + super.toString();
    }
}
