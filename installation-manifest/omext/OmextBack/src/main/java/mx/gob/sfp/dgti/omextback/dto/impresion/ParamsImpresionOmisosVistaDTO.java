/**
 * @(#)DatosRuspDTO.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.impresion;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * DTO para los datos provenientes de RUSP
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ParamsImpresionOmisosVistaDTO {

    /**
     * Parametros servicio impresion vistas numero oficio
     */
    private String numeroOficio;

    /**
     * Parametros servicio impresion vistas fecha geeracion
     */
    private String fechaGeneracionVista;

    /**
     * Parametros servicio impresion vistas dependencia
     */
    private String dependenciaEntidad;

    /**
     * Parametros servicio impresion vistas fecha vencimiento
     */
    private String fechaVenciemiento;

    /**
     * Parametros servicio impresion vistas texto del oficio
     */
    private String bodyTextOficio;

    /**
     * Parametros servicio impresion vistas logo
     */
    private String logoImagen;

    /**
     * Parametros servicio impresion vistas parrafo
     */
    private String primerParrafo;

    /**
     * Parametros servicio impresion vistas parrafo
     */
    private String segundoParrafo;

    /**
     * Parametros servicio impresion vistas puesto firmante
     */
    private String puestoFirmante;

    /**
     * Parametros servicio impresion vistas firma
     */
    private String firmaOficioCaracteresAutenticidad;

    /**
     * Parametros servicio impresion vistas firma
     */
    private String firmaListadoCaracteresAutenticidad;

    /**
     * Parametros servicio impresion vistas nombre de firmante
     */
    private String firmanteNombre;

    /**
     * Parametros servicio impresion vistas tipo de declaracion
     */
    private String tipoDeclaracion;

    /**
     * Parametros servicio impresion vistas anio
     */
    private String anio;

    /**
     * Listado de servidores publicos
     */
    private List<ParamsIntsImpresionOmisosVistaDTO> data;

    /**
     * Constructor
     */
    public ParamsImpresionOmisosVistaDTO(){//Constructor
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public ParamsImpresionOmisosVistaDTO(JsonObject json) {
        ParamsImpresionOmisosVistaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ParamsImpresionOmisosVistaDTOConverter.toJson(this, json);
        return json;
    }

    public String getNumeroOficio() {
        return numeroOficio;
    }

    public void setNumeroOficio(String numeroOficio) {
        this.numeroOficio = numeroOficio;
    }

    public String getFechaGeneracionVista() {
        return fechaGeneracionVista;
    }

    public void setFechaGeneracionVista(String fechaGeneracionVista) {
        this.fechaGeneracionVista = fechaGeneracionVista;
    }

    public String getDependenciaEntidad() {
        return dependenciaEntidad;
    }

    public void setDependenciaEntidad(String dependenciaEntidad) {
        this.dependenciaEntidad = dependenciaEntidad;
    }

    public String getFechaVenciemiento() {
        return fechaVenciemiento;
    }

    public void setFechaVenciemiento(String fechaVenciemiento) {
        this.fechaVenciemiento = fechaVenciemiento;
    }

    public String getBodyTextOficio() {
        return bodyTextOficio;
    }

    public void setBodyTextOficio(String bodyTextOficio) {
        this.bodyTextOficio = bodyTextOficio;
    }

    public String getLogoImagen() {
        return logoImagen;
    }

    public void setLogoImagen(String logoImagen) {
        this.logoImagen = logoImagen;
    }

    public String getPuestoFirmante() {
        return puestoFirmante;
    }

    public void setPuestoFirmante(String puestoFirmante) {
        this.puestoFirmante = puestoFirmante;
    }

    public String getFirmaOficioCaracteresAutenticidad() {
        return firmaOficioCaracteresAutenticidad;
    }

    public void setFirmaOficioCaracteresAutenticidad(String firmaOficioCaracteresAutenticidad) {
        this.firmaOficioCaracteresAutenticidad = firmaOficioCaracteresAutenticidad;
    }

    public String getFirmaListadoCaracteresAutenticidad() {
        return firmaListadoCaracteresAutenticidad;
    }

    public void setFirmaListadoCaracteresAutenticidad(String firmaListadoCaracteresAutenticidad) {
        this.firmaListadoCaracteresAutenticidad = firmaListadoCaracteresAutenticidad;
    }

    public String getFirmanteNombre() {
        return firmanteNombre;
    }

    public void setFirmanteNombre(String firmanteNombre) {
        this.firmanteNombre = firmanteNombre;
    }

    public String getPrimerParrafo() {
        return primerParrafo;
    }

    public void setPrimerParrafo(String primerParrafo) {
        this.primerParrafo = primerParrafo;
    }

    public String getSegundoParrafo() {
        return segundoParrafo;
    }

    public void setSegundoParrafo(String segundoParrafo) {
        this.segundoParrafo = segundoParrafo;
    }

    public String getTipoDeclaracion() {
        return tipoDeclaracion;
    }

    public void setTipoDeclaracion(String tipoDeclaracion) {
        this.tipoDeclaracion = tipoDeclaracion;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public List<ParamsIntsImpresionOmisosVistaDTO> getData() {
        return data;
    }

    public void setData(List<ParamsIntsImpresionOmisosVistaDTO> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ParamsImpresionOmisosVistaDTO{" +
                "numeroOficio='" + numeroOficio + '\'' +
                ", fechaGeneracionVista='" + fechaGeneracionVista + '\'' +
                ", dependenciaEntidad='" + dependenciaEntidad + '\'' +
                ", fechaVenciemiento='" + fechaVenciemiento + '\'' +
                ", bodyTextOficio='" + bodyTextOficio + '\'' +
                ", logoImagen='" + logoImagen + '\'' +
                ", puestoFirmante='" + puestoFirmante + '\'' +
                ", firmaOficioCaracteresAutenticidad='" + firmaOficioCaracteresAutenticidad + '\'' +
                ", firmaListadoCaracteresAutenticidad='" + firmaListadoCaracteresAutenticidad + '\'' +
                ", firmanteNombre='" + firmanteNombre + '\'' +
                ", primerParrafo='" + primerParrafo + '\'' +
                ", segundoParrafo='" + segundoParrafo + '\'' +
                ", tipoDeclaracion='" + tipoDeclaracion + '\'' +
                ", anio='" + anio + '\'' +
                ", data=" + data +
                '}';
    }
}
