/**
 * @(#)OeOficioxvista.java 24/03/2021
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.hash;

import mx.gob.sfp.dgti.omextback.util.constantes.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumTipoIncumplimiento;

/**
 * Auxiliar para obtener el hash de lo de las firmas
 *
 * @author pavel.martinez
 * @since 31/07/2021
 */
public class OeOficioxvista {

    /**
     * Numero de oficio
     */
    private Integer numOficio;

    public Integer getNumRegistros() {
        return numRegistros;
    }

    public void setNumRegistros(Integer numRegistros) {
        this.numRegistros = numRegistros;
    }

    /**
     * Numero de registros
     */
    private Integer numRegistros;

    /**
     * fecha de oficio
     */
    private String fechaOficio;

    /**
     * Tipo de incumplimiento
     */
    private EnumTipoIncumplimiento tipoIrregularidad;

    /**
     * Tipo de declaracion
     */
    private EnumTipoDeclaracion tipoDeclaracion;

    /**
     * Mes
     */
    private String mes;

    /**
     *
     */
    private Integer anio;

    /**
     *
     */
    private String fechaGeneracionOficio;

    /**
     *
     */
    private String remitente;

    /**
     * Constructor
     *
     * @param numOficio
     * @param fechaOficio
     * @param tipoIrregularidad
     * @param numRegistros
     * @param tipoDeclaracion
     * @param mes
     * @param anio
     * @param remitente
     * @param fechaGeneracionOficio
     */
    public OeOficioxvista(Integer numOficio, String fechaOficio, EnumTipoIncumplimiento tipoIrregularidad, Integer numRegistros,
                          EnumTipoDeclaracion tipoDeclaracion, String mes, Integer anio, String remitente, String fechaGeneracionOficio) {
        this.numOficio = numOficio;
        this.fechaOficio = fechaOficio;
        this.numRegistros = numRegistros;
        this.tipoIrregularidad = tipoIrregularidad;
        this.mes = mes;
        this.tipoDeclaracion = tipoDeclaracion;
        this.anio = anio;
        this.remitente = remitente;
        this.fechaGeneracionOficio = fechaGeneracionOficio;

    }

    /**
     *
     * @return
     */
    public String toStringOficio() {
        return numOficio + fechaOficio + tipoIrregularidad + numRegistros + tipoDeclaracion + mes + anio + remitente;
    }

    /**
     *
     * @return
     */
    public String  toStringListado(){
        return numRegistros + remitente + tipoIrregularidad  +  fechaGeneracionOficio ;
    }

    @Override
    public String toString() {
        return "OeOficioxvista{" +
                "numOficio=" + numOficio +
                ", numRegistros=" + numRegistros +
                ", fechaOficio='" + fechaOficio + '\'' +
                ", tipoIrregularidad=" + tipoIrregularidad +
                ", tipoDeclaracion=" + tipoDeclaracion +
                ", mes='" + mes + '\'' +
                ", anio=" + anio +
                ", fechaGeneracionOficio='" + fechaGeneracionOficio + '\'' +
                ", remitente='" + remitente + '\'' +
                '}';
    }
}
