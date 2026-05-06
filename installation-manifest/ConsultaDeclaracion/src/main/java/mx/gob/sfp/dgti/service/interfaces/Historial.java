/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.service.interfaces;

import java.util.Date;

/**
 *
 * @author cgarias
 */
public class Historial implements Comparable<Historial>{
    private final Integer idUsuario;
    private final String numeroDeclaracion;
    private final String fechaRecepcion;
    private final Integer collName; 
    private final String tipoDeclaracion;
    private final String fechaEncargo;
    private final Integer anio;
    private final Date fechaEncargoDate;
    
    public Historial(Integer idUsuario, String numeroDeclaracion,   String fechaRecepcion,
                     Integer collName,  String tipoDeclaracion,     String fechaEncargo,
                     Integer anio, Date fechaEncargoDate){
        this.idUsuario= idUsuario;
        this.numeroDeclaracion=numeroDeclaracion;
        this.fechaRecepcion=fechaRecepcion;
        this.collName=  collName; 
        this.tipoDeclaracion=tipoDeclaracion;
        this.fechaEncargo=fechaEncargo;
        this.anio= anio;
        this.fechaEncargoDate = fechaEncargoDate;
    }
    
    @Override
    public int compareTo(Historial o) {
        return getFechaEncargoDate().compareTo(o.fechaEncargoDate);
    }

    /**
     * @return the idUsuario
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }

    /**
     * @return the numeroDeclaracion
     */
    public String getNumeroDeclaracion() {
        return numeroDeclaracion;
    }

    /**
     * @return the fechaRecepcion
     */
    public String getFechaRecepcion() {
        return fechaRecepcion;
    }

    /**
     * @return the collName
     */
    public Integer getCollName() {
        return collName;
    }

    /**
     * @return the tipoDeclaracion
     */
    public String getTipoDeclaracion() {
        return tipoDeclaracion;
    }

    /**
     * @return the fechaEncargo
     */
    public String getFechaEncargo() {
        return fechaEncargo;
    }

    /**
     * @return the anio
     */
    public Integer getAnio() {
        return anio;
    }

    /**
     * @return the fechaencagoDate
     */
    public Date getFechaEncargoDate() {
        return fechaEncargoDate;
    }
}
