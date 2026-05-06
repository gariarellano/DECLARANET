/**
 * @(#)DatosRuspDTO.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumSexo;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumTipoObligacion;

/**
 * DTO para los datos provenientes de RUSP
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DatosRuspDTO {

    /**
     * idMovimiento
     */
    private Integer idMovimiento;

    /**
     * Indica el tipo de obligación en RUSP
     */
    private EnumTipoObligacion tipoObligacion;

    /**
     * Indica si la institucion pertenece a seguridad nacional
     */
    private Integer seguridadNacional;

    /**
     * idSP
     */
    private Integer idSp;

    /**
     * CURP del servidor
     */
    private String curp;

    /**
     * Nombres del servidor
     */
    private String nombres;

    /**
     * Primer apellido del servidor
     */
    private String primerApellido;

    /**
     * Segundo apellido del servidor
     */
    private String segundoApellido;

    /**
     * Nombre completo
     */
    private String nombreCompleto;

    /**
     * Sexo del servidor
     */
    private EnumSexo sexo;

    /**
     * idEnte al que pertenece el servidor
     */
    private String idEnte;

    /**
     * nombre del ente al que pertenece el servidor
     */
    private String nombreEnte;

    /**
     * Ramo al que pertenece la institucion del servidor
     */
    private Integer ramo;

    /**
     * Unidad responsable al que pertenece
     */
    private String ur;

    /**
     * id del tipo de ente
     */
    private Integer idTipoEnte;

    /**
     * Tipo de ente
     */
    private String tipoEnte;

    /**
     * Clave ua
     */
    private String claveUa;

    /**
     * Unidad Administrativa
     */
    private String unidadAdministrativa;

    /**
     * Id puesto
     */
    private String idPuesto;

    /**
     * Empleo cargo o comision
     */
    private String empleoCargoComision;

    /**
     * Nivel de empleo, cargo o comision
     */
    private String nivelEmpleoCargoComision;

    /**
     * id de nivel jerarquico
     */
    private Integer idNivelJerarquico;

    /**
     * Valor de nivel jerarquico
     */
    private String valorNivelJerarquico;

    /**
     * Id de puesto estrategico
     */
    private Integer idPuestoEstrategico;

    /**
     * Puesto estrategico
     */
    private String puestoEstrategico;

    /**
     * Fecha de ingreso a la institucion
     */
    private String fechaIngresoInstitucion;

    /**
     * Fecha de toma de posecion de puesto
     */
    private String fechaTomaPosesionPuesto;

    /**
     * Fecha de obligacion de la declaracion
     */
    private String fechaObligacionDeclara;

    /**
     * Fecha de baja
     */
    private String fechaBaja;

    /**
     * ANio
     */
    private Integer anio;

    /**
     * Declaracion patrimonial
     */
    private String declaracionPatrimonial;

    /**
     * Id del motivo de la declaracion patrimonial
     */
    private Integer idMotivoDeclaracionPatrimonial;

    /**
     * Motivo de la declaracion patrimonial
     */
    private String motivoDeclaracionPatrimonial;

    /**
     * Id del tipo de contratacion
     */
    private Integer idTipoContratacion;

    /**
     * Tipo de contratacion
     */
    private String tipoContratacion;

    /**
     * Politicamente expuesto
     */
    private String politicamenteExpuesto;

    /**
     * Id del motivo de baja
     */
    private String idMotivoBaja;

    /**
     * Motivo de baja
     */
    private String motivoBaja;

    /**
     * Id de alta asociada baja
     */
    private String idAltaAsociadaBaja;

    /**
     * Id
     */
    private String id;

    /**
     * Indica si esta activo
     */
    private Integer activo;

    /**
     * Fecha de registro
     */
    private String fechaRegistro;

    /**
     * Tipo de Obligacion para vista de front
     */
    private String tipoDeclaracionDesc;

    /**
     * Cumplimiento para vista de front
     */
    private String cumplimientoDesc;

    /**
     * Id del periodo en caso de que se encuentre en alguno
     */
    private String idPeriodo;

    /**
     * Id de la vista en caso de que se encuentre alguno
     */
    private String idVista;

    /**
     * Constructor
     */
    public DatosRuspDTO(){//Constructor
    }


    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DatosRuspDTO(JsonObject json) {
        DatosRuspDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DatosRuspDTOConverter.toJson(this, json);
        return json;
    }

    public Integer getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public EnumTipoObligacion getTipoObligacion() {
        return tipoObligacion;
    }

    public void setTipoObligacion(EnumTipoObligacion tipoObligacion) {
        this.tipoObligacion = tipoObligacion;
    }

    public Integer getSeguridadNacional() {
        return seguridadNacional;
    }

    public void setSeguridadNacional(Integer seguridadNacional) {
        this.seguridadNacional = seguridadNacional;
    }

    public Integer getIdSp() {
        return idSp;
    }

    public void setIdSp(Integer idSp) {
        this.idSp = idSp;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public EnumSexo getSexo() {
        return sexo;
    }

    public void setSexo(EnumSexo sexo) {
        this.sexo = sexo;
    }

    public String getIdEnte() {
        return idEnte;
    }

    public void setIdEnte(String idEnte) {
        this.idEnte = idEnte;
    }

    public String getNombreEnte() {
        return nombreEnte;
    }

    public void setNombreEnte(String nombreEnte) {
        this.nombreEnte = nombreEnte;
    }

    public Integer getRamo() {
        return ramo;
    }

    public void setRamo(Integer ramo) {
        this.ramo = ramo;
    }

    public String getUr() {
        return ur;
    }

    public void setUr(String ur) {
        this.ur = ur;
    }

    public Integer getIdTipoEnte() {
        return idTipoEnte;
    }

    public void setIdTipoEnte(Integer idTipoEnte) {
        this.idTipoEnte = idTipoEnte;
    }

    public String getTipoEnte() {
        return tipoEnte;
    }

    public void setTipoEnte(String tipoEnte) {
        this.tipoEnte = tipoEnte;
    }

    public String getClaveUa() {
        return claveUa;
    }

    public void setClaveUa(String claveUa) {
        this.claveUa = claveUa;
    }

    public String getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(String unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public String getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(String idPuesto) {
        this.idPuesto = idPuesto;
    }

    public String getEmpleoCargoComision() {
        return empleoCargoComision;
    }

    public void setEmpleoCargoComision(String empleoCargoComision) {
        this.empleoCargoComision = empleoCargoComision;
    }

    public String getNivelEmpleoCargoComision() {
        return nivelEmpleoCargoComision;
    }

    public void setNivelEmpleoCargoComision(String nivelEmpleoCargoComision) {
        this.nivelEmpleoCargoComision = nivelEmpleoCargoComision;
    }

    public Integer getIdNivelJerarquico() {
        return idNivelJerarquico;
    }

    public void setIdNivelJerarquico(Integer idNivelJerarquico) {
        this.idNivelJerarquico = idNivelJerarquico;
    }

    public String getValorNivelJerarquico() {
        return valorNivelJerarquico;
    }

    public void setValorNivelJerarquico(String valorNivelJerarquico) {
        this.valorNivelJerarquico = valorNivelJerarquico;
    }

    public Integer getIdPuestoEstrategico() {
        return idPuestoEstrategico;
    }

    public void setIdPuestoEstrategico(Integer idPuestoEstrategico) {
        this.idPuestoEstrategico = idPuestoEstrategico;
    }

    public String getPuestoEstrategico() {
        return puestoEstrategico;
    }

    public void setPuestoEstrategico(String puestoEstrategico) {
        this.puestoEstrategico = puestoEstrategico;
    }

    public String getFechaIngresoInstitucion() {
        return fechaIngresoInstitucion;
    }

    public void setFechaIngresoInstitucion(String fechaIngresoInstitucion) {
        this.fechaIngresoInstitucion = fechaIngresoInstitucion;
    }

    public String getFechaTomaPosesionPuesto() {
        return fechaTomaPosesionPuesto;
    }

    public void setFechaTomaPosesionPuesto(String fechaTomaPosesionPuesto) {
        this.fechaTomaPosesionPuesto = fechaTomaPosesionPuesto;
    }

    public String getFechaObligacionDeclara() {
        return fechaObligacionDeclara;
    }

    public void setFechaObligacionDeclara(String fechaObligacionDeclara) {
        this.fechaObligacionDeclara = fechaObligacionDeclara;
    }

    public String getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(String fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getDeclaracionPatrimonial() {
        return declaracionPatrimonial;
    }

    public void setDeclaracionPatrimonial(String declaracionPatrimonial) {
        this.declaracionPatrimonial = declaracionPatrimonial;
    }

    public Integer getIdMotivoDeclaracionPatrimonial() {
        return idMotivoDeclaracionPatrimonial;
    }

    public void setIdMotivoDeclaracionPatrimonial(Integer idMotivoDeclaracionPatrimonial) {
        this.idMotivoDeclaracionPatrimonial = idMotivoDeclaracionPatrimonial;
    }

    public String getMotivoDeclaracionPatrimonial() {
        return motivoDeclaracionPatrimonial;
    }

    public void setMotivoDeclaracionPatrimonial(String motivoDeclaracionPatrimonial) {
        this.motivoDeclaracionPatrimonial = motivoDeclaracionPatrimonial;
    }

    public Integer getIdTipoContratacion() {
        return idTipoContratacion;
    }

    public void setIdTipoContratacion(Integer idTipoContratacion) {
        this.idTipoContratacion = idTipoContratacion;
    }

    public String getTipoContratacion() {
        return tipoContratacion;
    }

    public void setTipoContratacion(String tipoContratacion) {
        this.tipoContratacion = tipoContratacion;
    }

    public String getPoliticamenteExpuesto() {
        return politicamenteExpuesto;
    }

    public void setPoliticamenteExpuesto(String politicamenteExpuesto) {
        this.politicamenteExpuesto = politicamenteExpuesto;
    }

    public String getIdMotivoBaja() {
        return idMotivoBaja;
    }

    public void setIdMotivoBaja(String idMotivoBaja) {
        this.idMotivoBaja = idMotivoBaja;
    }

    public String getMotivoBaja() {
        return motivoBaja;
    }

    public void setMotivoBaja(String motivoBaja) {
        this.motivoBaja = motivoBaja;
    }

    public String getIdAltaAsociadaBaja() {
        return idAltaAsociadaBaja;
    }

    public void setIdAltaAsociadaBaja(String idAltaAsociadaBaja) {
        this.idAltaAsociadaBaja = idAltaAsociadaBaja;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTipoDeclaracionDesc() {
        return tipoDeclaracionDesc;
    }

    public void setTipoDeclaracionDesc(String tipoDeclaracionDesc) {
        this.tipoDeclaracionDesc = tipoDeclaracionDesc;
    }

    public String getCumplimientoDesc() {
        return cumplimientoDesc;
    }

    public void setCumplimientoDesc(String cumplimientoDesc) {
        this.cumplimientoDesc = cumplimientoDesc;
    }

    public String getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(String idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getIdVista() {
        return idVista;
    }

    public void setIdVista(String idVista) {
        this.idVista = idVista;
    }

    @Override
    public String toString() {
        return "DatosRuspDTO{" +
                "idMovimiento=" + idMovimiento +
                ", tipoObligacion=" + tipoObligacion +
                ", seguridadNacional=" + seguridadNacional +
                ", idSp=" + idSp +
                ", curp='" + curp + '\'' +
                ", nombres='" + nombres + '\'' +
                ", primerApellido='" + primerApellido + '\'' +
                ", segundoApellido='" + segundoApellido + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", sexo=" + sexo +
                ", idEnte='" + idEnte + '\'' +
                ", nombreEnte='" + nombreEnte + '\'' +
                ", ramo=" + ramo +
                ", ur='" + ur + '\'' +
                ", idTipoEnte=" + idTipoEnte +
                ", tipoEnte='" + tipoEnte + '\'' +
                ", claveUa='" + claveUa + '\'' +
                ", unidadAdministrativa='" + unidadAdministrativa + '\'' +
                ", idPuesto='" + idPuesto + '\'' +
                ", empleoCargoComision='" + empleoCargoComision + '\'' +
                ", nivelEmpleoCargoComision='" + nivelEmpleoCargoComision + '\'' +
                ", idNivelJerarquico=" + idNivelJerarquico +
                ", valorNivelJerarquico='" + valorNivelJerarquico + '\'' +
                ", idPuestoEstrategico=" + idPuestoEstrategico +
                ", puestoEstrategico='" + puestoEstrategico + '\'' +
                ", fechaIngresoInstitucion='" + fechaIngresoInstitucion + '\'' +
                ", fechaTomaPosesionPuesto='" + fechaTomaPosesionPuesto + '\'' +
                ", fechaObligacionDeclara='" + fechaObligacionDeclara + '\'' +
                ", fechaBaja='" + fechaBaja + '\'' +
                ", anio=" + anio +
                ", declaracionPatrimonial='" + declaracionPatrimonial + '\'' +
                ", idMotivoDeclaracionPatrimonial=" + idMotivoDeclaracionPatrimonial +
                ", motivoDeclaracionPatrimonial='" + motivoDeclaracionPatrimonial + '\'' +
                ", idTipoContratacion=" + idTipoContratacion +
                ", tipoContratacion='" + tipoContratacion + '\'' +
                ", politicamenteExpuesto='" + politicamenteExpuesto + '\'' +
                ", idMotivoBaja='" + idMotivoBaja + '\'' +
                ", motivoBaja='" + motivoBaja + '\'' +
                ", idAltaAsociadaBaja='" + idAltaAsociadaBaja + '\'' +
                ", id='" + id + '\'' +
                ", activo=" + activo +
                ", fechaRegistro='" + fechaRegistro + '\'' +
                ", tipoDeclaracionDesc='" + tipoDeclaracionDesc + '\'' +
                ", cumplimientoDesc='" + cumplimientoDesc + '\'' +
                ", idPeriodo='" + idPeriodo + '\'' +
                ", idVista='" + idVista + '\'' +
                '}';
    }
}
