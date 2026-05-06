/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "Utils-Validator-SFP" Libreria que permite realizar la validacion 
 * de propiedades que se deceen validar, mediante la asignacion de 
 * una posicion, el nombre de la propiedad, el valor de la propiedad y 
 * las validaciones a ejecutar.
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.validador.dto.out;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que contiene las porpiedades de 
 * las validaciones a ejecutar y que pertenecen 
 * a un módulo y sus hijos.
 * 
 * @author cgarias
 * @since 25/09/2019
 */
public class ErroresDTO implements Serializable{
    /**
     * propiedad que contiene el nombre del módulo al que le pertenecen las validadciones.
     */
    private String nombreModulo;
    /**
     * Propiedad que contoiene las la propiedad a validar y sus errrores.
     */
    private List<PropiedadesErrorDTO> listErorres;
    /**
     * Propiedad que contiene los Módulos hijos para realiaer las validaciones de sus propiedades.
     */
    private List<ErroresDTO> listModHijos;
    
    /**
     * Constructor principal de la clase.
     * No permitira tener null.
     */
    public ErroresDTO(){
        this.listErorres = new ArrayList<>();
        this.listModHijos = new ArrayList<>();
    }

    /**
     * @return the nombreModulo
     */
    public String getNombreModulo() {
        return nombreModulo;
    }

    /**
     * @param nombreModulo the nombreModulo to set
     */
    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }

    /**
     * @return the listErorres
     */
    public List<PropiedadesErrorDTO> getListErorres() {
        return listErorres;
    }

    /**
     * @param listErorres the listErorres to set
     */
    public void setListErorres(List<PropiedadesErrorDTO> listErorres) {
        this.listErorres = listErorres;
    }

    /**
     * @return the listModHijos
     */
    public List<ErroresDTO> getListModHijos() {
        return listModHijos;
    }

    /**
     * @param listModHijos the listModHijos to set
     */
    public void setListModHijos(List<ErroresDTO> listModHijos) {
        this.listModHijos = listModHijos;
    }
}
